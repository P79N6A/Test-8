package com.tydic.beijing.billing.reconciliation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.service.impl.RechargeCallbackImpl;

public class RechargeReconciliation {
	private final static Logger LOGGER = Logger
			.getLogger(RechargeReconciliation.class);

	private final static String RESULT_OK = "OK";
	private final static String RESULT_FAIL = "FAILED";

	private static final String RECHARGE_TYPE_CASH = "A";
	private static final String RECHARGE_TYPE_JINGQUAN = "B";
	private static final String RECHARGE_TYPE_JINGDOU = "C";
	private static final String RECHARGE_TYPE_DONGQUAN = "D";
	private static final String RECHARGE_TYPE_LIPINKA = "E";
	private static final String RECHARGE_TYPE_BAITIAO = "F";

	private final static int BATCH_COUNT = 100;

	private static String urlOra = null;
	private static String usernameOra = null;
	private static String passwordOra = null;
	private static Connection conOra = null;
	private static PreparedStatement stmt4QueryLogActPay = null;
	private static PreparedStatement stmt4InsLogRechargeCons = null;
	private static PreparedStatement stmt4QueryOnlyInBoss = null;

	private static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final String sqlQueryLogActPay = "select a.pay_charge_id, a.service_nbr, a.pay_time, a.pay_channel, a.rece_fee, c.recharge_type from log_act_pay a, info_pay_balance b, rule_recharge_type_mapping c where a.balance_id = b.balance_id and b.balance_type_id = c.balance_type_id and a.pay_charge_id = ?";
	private static final String sqlInsertLogRechargeCons = "insert into log_recharge_recons(file_name, raw_record, sn, device_nbr, amount, compare_result, error_message, compare_time) values(?, ?, ?, ?, ?, ?, ?, sysdate)";
	private static final String sqlQueryOnlyInBoss = "select a.pay_charge_id, a.service_nbr, a.pay_channel, a.pay_time, a.rece_fee from log_act_pay a where to_char(a.pay_time, 'YYYYMMDD') = ? and not exists(select * from log_recharge_recons b where a.pay_charge_id = b.sn)";

	public void process(String scanPath) {
		File directory = new File(scanPath);
		if (!directory.isDirectory()) {
			LOGGER.error("Provide Scan Path[" + scanPath
					+ "] is NOT Exists or is Not a Directory!");
			return;
		}
		List<File> files = findMatchedFiles(directory);
		if (files == null) {
			LOGGER.warn("Provide Scan Path[" + scanPath
					+ "] contains NO Matched File!");
			return;
		}
		try {
			initDBStatement();
		} catch (Exception e1) {
			LOGGER.error("Inital OracleDB Connection Failed!["
					+ e1.getMessage() + "]");
			return;
		}
		Set<String> dates = new HashSet<String>();
		for (File file : files) {
			BufferedReader br = null;
			try {
				dates.add(getDateFromFileName(file.getName()));
				File onProcessFile = new File(file.getAbsolutePath() + ".bak");
				boolean ret = file.renameTo(onProcessFile);
				if (ret) {
					br = new BufferedReader(new InputStreamReader(
							new FileInputStream(onProcessFile),
							Charset.forName("UTF-8")));
					fileBatchProcess(file, br);
				} else {
					LOGGER.warn("File[" + file.getAbsolutePath()
							+ "] Rename Failed!");
				}
			} catch (FileNotFoundException e) {
				LOGGER.warn("Reading File [" + file.getAbsolutePath()
						+ "] Failed![" + e.getMessage() + "]");
			} catch (IOException e) {
				LOGGER.error("Reading File [" + file.getAbsolutePath()
						+ "] Failed![" + e.getMessage() + "]");
			} catch (SQLException e) {
				LOGGER.error("DML Failed![" + e.getMessage() + "]");
				try {
					conOra.rollback();
				} catch (SQLException e1) {
					LOGGER.error("Rollback Failed![" + e.getMessage() + "]");
				}
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("Closing File [" + file.getAbsolutePath()
							+ "] Failed![" + e.getMessage() + "]");
				}
			}
		}
		for (String fileDate : dates) {
			checkOnlyExsitsInBoss(fileDate);
		}
		closeAll();
	}
	
	private String getDateFromFileName(String fileName) {
		int index = fileName.lastIndexOf("_");
		return fileName.substring(index + 1, fileName.length());
	}

	private void checkOnlyExsitsInBoss(String date) {
		try {
			int count = 0;
			List<LogProcessResult> lprs = new ArrayList<LogProcessResult>();
			stmt4QueryOnlyInBoss.setString(1, date);
			ResultSet rs = stmt4QueryOnlyInBoss.executeQuery();
			while(rs.next()) {
				LogProcessResult lpr = new LogProcessResult();
				lpr.setFileName("OnlyInBossDB");
				lpr.setSn(rs.getString(1));
				lpr.setDeviceNumber(rs.getString(2));
				lpr.setRawRecord(null);
				lpr.setAmount(rs.getLong(5));
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("SN["+ rs.getString(1) +"] Only Exists In BOSS DB!");
				lprs.add(lpr);
				count++;
				if (count % BATCH_COUNT == 0) {
					recordLogRechargeCons(lprs);
					conOra.commit();
					lprs.clear();
				}
			}
			recordLogRechargeCons(lprs);
			conOra.commit();
			lprs.clear();
		} catch (SQLException e) {
			LOGGER.error("DML Failed![" + e.getMessage() + "]");
		}
	}

	private void closeAll() {
		try {
			stmt4QueryLogActPay.close();
			stmt4InsLogRechargeCons.close();
			stmt4QueryOnlyInBoss.close();
			conOra.close();
		} catch (SQLException e) {
			LOGGER.error("Closing Failed![" + e.getMessage() + "]");
		}
	}

	private String[] splitString(String line) {
		if (line.lastIndexOf("|") == (line.length() - 1)) {
			line = line + " ";
		}
		return line.split("\\|");
	}

	private RechargeReconsObject assembleRRO(String line, LogProcessResult lpr) {
		RechargeReconsObject rro = new RechargeReconsObject();
		String[] pieces = splitString(line);
		if (pieces.length != 12) {
			lpr.setCompareResult(RESULT_FAIL);
			lpr.setErrorMessage("Record Format Error!");
			return null;
		} else {
			int flag = 0;
			lpr.setAmount(0L);
			if (pieces[0] != null) {
				rro.setSn(pieces[0]);
			} else {
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("SN is NULL!");
				return null;
			}
			if (pieces[1] != null) {
				rro.setDeviceNumber(pieces[1]);
			} else {
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("DeviceNumber is NULL!");
				return null;
			}
			if (pieces[2] != null) {
				rro.setContactChannel(pieces[2]);
			} else {
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("ContactChannel is NULL!");
				return null;
			}
			if (pieces[3] != null) {
				rro.setJdAcctNbr(pieces[3]);
			} else {
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("JDAcctNbr is NULL!");
				return null;
			}
			if (pieces[4] != null) {
				// didn't store send time
				// rro.setPayTime(pieces[4]);
			} else {
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("Timestamp is NULL!");
				return null;
			}
			if (pieces[5] != null) {
				rro.setPayTime(pieces[5]);
			} else {
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("FillTime is NULL!");
				return null;
			}
			if ((pieces[6] != null) && (!pieces[6].trim().equals(""))) {
				try {
					flag++;
					long money = Long.parseLong(pieces[6]);
					rro.setCashAmount(money);
					lpr.setAmount(lpr.getAmount() + money);
				} catch (Exception ex) {
					LOGGER.warn("Parse String Money to Long error[" + pieces[6]
							+ "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("Cash String2Long Failed!");
					return null;
				}
			}
			if ((pieces[7] != null) && (!pieces[7].trim().equals(""))) {
				try {
					flag++;
					long money = Long.parseLong(pieces[7]);
					rro.setJqAmount(money);
					lpr.setAmount(lpr.getAmount() + money);
				} catch (Exception ex) {
					LOGGER.warn("Parse String Money to Long error[" + pieces[7]
							+ "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("JingQuan String2Long Failed!");
					return null;
				}
			}
			if ((pieces[8] != null) && (!pieces[8].trim().equals(""))) {
				try {
					flag++;
					long money = Long.parseLong(pieces[8]);
					rro.setJdAmount(money);
					lpr.setAmount(lpr.getAmount() + money);
				} catch (Exception ex) {
					LOGGER.warn("Parse String Money to Long error[" + pieces[8]
							+ "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("JingDou String2Long Failed!");
					return null;
				}
			}
			if ((pieces[9] != null) && (!pieces[9].trim().equals(""))) {
				try {
					flag++;
					long money = Long.parseLong(pieces[9]);
					rro.setDqAmount(money);
					lpr.setAmount(lpr.getAmount() + money);
				} catch (Exception ex) {
					LOGGER.warn("Parse String Money to Long error[" + pieces[9]
							+ "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("DongQuan String2Long Failed!");
					return null;
				}
			}
			if ((pieces[10] != null) && (!pieces[10].trim().equals(""))) {
				try {
					flag++;
					long money = Long.parseLong(pieces[10]);
					rro.setLpkAmount(money);
					lpr.setAmount(lpr.getAmount() + money);
				} catch (Exception ex) {
					LOGGER.warn("Parse String Money to Long error["
							+ pieces[10] + "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("LiPinKa String2Long Failed!");
					return null;
				}
			}
			if ((pieces[11] != null) && (!pieces[11].trim().equals(""))) {
				try {
					flag++;
					long money = Long.parseLong(pieces[11]);
					rro.setBtAmount(money);
					lpr.setAmount(lpr.getAmount() + money);
				} catch (Exception ex) {
					LOGGER.warn("Parse String Money to Long error["
							+ pieces[11] + "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("BaiTiao String2Long Failed!");
					return null;
				}
			}
			if (flag == 0) {
				LOGGER.warn("All Money Slot is NULL!");
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("All Money Slot is NULL!");
				return null;
			}
		}
		return rro;
	}

	private void fileBatchProcess(File file, BufferedReader br)
			throws IOException, SQLException {
		int count = 0;
		long totalAmount = 0L;
		List<LogProcessResult> lprs = new ArrayList<LogProcessResult>();
		String line = br.readLine();
		while (line != null) {
			LogProcessResult lpr = new LogProcessResult();
			setBasicLogInfo(lpr, file, line);
			if (line.indexOf("total") != -1) {
				compareCountsAndAmounts(line, count, totalAmount, lpr);
				lprs.add(lpr);
			} else {
				RechargeReconsObject rro = assembleRRO(line, lpr);
				if (rro != null) {
					totalAmount += lpr.getAmount();
					compareRecord(rro, lpr);
				}
				lprs.add(lpr);
			}
			count++;
			if ((count % BATCH_COUNT) == 0) {
				recordLogRechargeCons(lprs);
				conOra.commit();
				lprs.clear();
			}
			line = br.readLine();
		}
		recordLogRechargeCons(lprs);
		conOra.commit();
		lprs.clear();
	}

	private void recordLogRechargeCons(List<LogProcessResult> lprs) {
		for (LogProcessResult lpr : lprs) {
			try {
				stmt4InsLogRechargeCons.setString(1, lpr.getFileName());
				stmt4InsLogRechargeCons.setString(2, lpr.getRawRecord());
				stmt4InsLogRechargeCons.setString(3, lpr.getSn());
				stmt4InsLogRechargeCons.setString(4, lpr.getDeviceNumber());
				stmt4InsLogRechargeCons.setLong(5, lpr.getAmount());
				stmt4InsLogRechargeCons.setString(6, lpr.getCompareResult());
				stmt4InsLogRechargeCons.setString(7, lpr.getErrorMessage());

				stmt4InsLogRechargeCons.execute();
			} catch (SQLException e) {
				LOGGER.error("Insert Log_Recharge_Recons Failed!["
						+ e.getMessage() + "]");
			}
		}
	}

	private boolean compareRecord(RechargeReconsObject rro, LogProcessResult lpr) {
		if (stmt4QueryLogActPay == null) {
			LOGGER.error("OracleDB Connection Not Ready!");
			System.exit(0);
		}
		lpr.setSn(rro.getSn());
		lpr.setDeviceNumber(rro.getDeviceNumber());
		ResultSet rs = null;
		int flag4counts = 0;
		long fileTotalAmount = lpr.getAmount();
		try {
			stmt4QueryLogActPay.setString(1, rro.getSn());
			rs = stmt4QueryLogActPay.executeQuery();
			while (rs.next()) {
				flag4counts++;
				String sn = rs.getString(1);
				String deviceNumber = rs.getString(2);
				Date payTime = rs.getDate(3);
				String payChannel = rs.getString(4);
				long receFee = rs.getLong(5);
				String rechargeType = rs.getString(6);
				if ((deviceNumber == null)
						|| (!deviceNumber.trim().equalsIgnoreCase(
								rro.getDeviceNumber()))) {
					LOGGER.info("SN[" + sn
							+ "] DeviceNumber Mismatched! InFile["
							+ rro.getDeviceNumber() + "] Expect["
							+ deviceNumber + "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setSn(sn);
					lpr.setErrorMessage("SN[" + sn
							+ "] DeviceNumber Mismatched! InFile["
							+ rro.getDeviceNumber() + "] Expect["
							+ deviceNumber + "]");
					return false;
				}
				if ((payTime == null)
						|| (!(df.format(payTime)).trim().equalsIgnoreCase(
								rro.getPayTime()))) {
					LOGGER.info("SN[" + sn + "] Fill_time Mismatched! InFile["
							+ rro.getPayTime() + "] Expect[" + payTime + "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("SN[" + sn
							+ "] Fill_time Mismatched! InFile["
							+ rro.getPayTime() + "] Expect[" + payTime + "]");
					return false;
				}
				if ((payChannel == null)
						|| (!payChannel.trim().equalsIgnoreCase(
								rro.getContactChannel()))) {
					LOGGER.info("SN[" + sn
							+ "] ContactChannel Mismatched! InFile["
							+ rro.getContactChannel() + "] Expect["
							+ payChannel + "]");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("SN[" + sn
							+ "] ContactChannel Mismatched! InFile["
							+ rro.getContactChannel() + "] Expect["
							+ payChannel + "]");
					return false;
				}
				if (rechargeType != null) {
					if (rechargeType.trim()
							.equalsIgnoreCase(RECHARGE_TYPE_CASH)) {
						if (rro.getCashAmount() != receFee) {
							LOGGER.info("SN[" + sn
									+ "] Cash Mismatched! InFile["
									+ rro.getCashAmount() + "] Expect["
									+ receFee + "]");
							lpr.setCompareResult(RESULT_FAIL);
							lpr.setErrorMessage("SN[" + sn
									+ "] Cash Mismatched! InFile["
									+ rro.getCashAmount() + "] Expect["
									+ receFee + "]");
							return false;
						} else {
							fileTotalAmount -= receFee;
						}
					} else if (rechargeType.trim().equalsIgnoreCase(
							RECHARGE_TYPE_JINGQUAN)) {
						if (rro.getJqAmount() != receFee) {
							LOGGER.info("SN[" + sn
									+ "] JingQuan Mismatched! InFile["
									+ rro.getJqAmount() + "] Expect[" + receFee
									+ "]");
							lpr.setCompareResult(RESULT_FAIL);
							lpr.setErrorMessage("SN[" + sn
									+ "] JingQuan Mismatched! InFile["
									+ rro.getJqAmount() + "] Expect[" + receFee
									+ "]");
							return false;
						} else {
							fileTotalAmount -= receFee;
						}
					} else if (rechargeType.trim().equalsIgnoreCase(
							RECHARGE_TYPE_JINGDOU)) {
						if (rro.getJdAmount() != receFee) {
							LOGGER.info("SN[" + sn
									+ "] JingDou Mismatched! InFile["
									+ rro.getJdAmount() + "] Expect[" + receFee
									+ "]");
							lpr.setCompareResult(RESULT_FAIL);
							lpr.setErrorMessage("SN[" + sn
									+ "] JingDou Mismatched! InFile["
									+ rro.getJdAmount() + "] Expect[" + receFee
									+ "]");
							return false;
						} else {
							fileTotalAmount -= receFee;
						}
					} else if (rechargeType.trim().equalsIgnoreCase(
							RECHARGE_TYPE_DONGQUAN)) {
						if (rro.getDqAmount() != receFee) {
							LOGGER.info("SN[" + sn
									+ "] DongQuan Mismatched! InFile["
									+ rro.getDqAmount() + "] Expect[" + receFee
									+ "]");
							lpr.setCompareResult(RESULT_FAIL);
							lpr.setErrorMessage("SN[" + sn
									+ "] DongQuan Mismatched! InFile["
									+ rro.getDqAmount() + "] Expect[" + receFee
									+ "]");
							return false;
						} else {
							fileTotalAmount -= receFee;
						}
					} else if (rechargeType.trim().equalsIgnoreCase(
							RECHARGE_TYPE_LIPINKA)) {
						if (rro.getLpkAmount() != receFee) {
							LOGGER.info("SN[" + sn
									+ "] LiPinKa Mismatched! InFile["
									+ rro.getLpkAmount() + "] Expect["
									+ receFee + "]");
							lpr.setCompareResult(RESULT_FAIL);
							lpr.setErrorMessage("SN[" + sn
									+ "] LiPinKa Mismatched! InFile["
									+ rro.getLpkAmount() + "] Expect["
									+ receFee + "]");
							return false;
						} else {
							fileTotalAmount -= receFee;
						}
					} else if (rechargeType.trim().equalsIgnoreCase(
							RECHARGE_TYPE_BAITIAO)) {
						if (rro.getBtAmount() != receFee) {
							LOGGER.info("SN[" + sn
									+ "] BaiTiao Mismatched! InFile["
									+ rro.getBtAmount() + "] Expect[" + receFee
									+ "]");
							lpr.setCompareResult(RESULT_FAIL);
							lpr.setErrorMessage("SN[" + sn
									+ "] BaiTiao Mismatched! InFile["
									+ rro.getBtAmount() + "] Expect[" + receFee
									+ "]");
							return false;
						} else {
							fileTotalAmount -= receFee;
						}
					} else {
						LOGGER.error("RechargeType[" + rechargeType
								+ "] Unsupported!");
						lpr.setCompareResult(RESULT_FAIL);
						lpr.setErrorMessage("SN[" + sn + "]RechargeType["
								+ rechargeType + "] Unsupported!");
						return false;
					}
				} else {
					LOGGER.error("SN[" + sn + "]RechargeType is NULL!");
					lpr.setCompareResult(RESULT_FAIL);
					lpr.setErrorMessage("SN[" + sn + "]RechargeType is NULL!");
					return false;
				}
			}
			if (flag4counts == 0) {
				LOGGER.error("SN[" + rro.getSn() + "] NOT FOUND!");
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("SN[" + rro.getSn() + "] NOT FOUND!");
			} else if (fileTotalAmount != 0L) {
				LOGGER.error("SN[" + rro.getSn() + "] TotalAmount ["
						+ (lpr.getAmount() - fileTotalAmount) + "] Expected["
						+ lpr.getAmount() + "]");
				lpr.setCompareResult(RESULT_FAIL);
				lpr.setErrorMessage("SN[" + rro.getSn() + "] NOT FOUND!");
			}
		} catch (SQLException e) {
			LOGGER.error("DML Failed![" + e.getMessage() + "]");
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.error("DB Closing Failed![" + e.getMessage() + "]");
			}
		}
		return false;
	}

	private boolean compareCountsAndAmounts(String line, long count,
			long totalAmount, LogProcessResult lpr) {
		long total_count = -1L;
		long total_charge = -1L;
		String[] pieces = line.split(",");
		if (pieces.length == 2) {
			for (String piece : pieces) {
				String[] totals = piece.split("=");
				if (totals.length == 2) {
					if (totals[0].trim().equalsIgnoreCase("total_count")) {
						try {
							total_count = Long.parseLong(totals[1]);
						} catch (Exception ex) {
							LOGGER.warn("Parse String total_count to Long error["
									+ totals[1] + "]");
							return false;
						}
					}
					if (totals[0].trim().equalsIgnoreCase("total_charge")) {
						try {
							total_charge = Long.parseLong(totals[1]);
						} catch (Exception ex) {
							LOGGER.warn("Parse String total_charge to Long error["
									+ totals[1] + "]");
							return false;
						}
					}
				}
			}
		}
		if ((total_count == count) && (total_charge == totalAmount)) {
			lpr.setCompareResult(RESULT_OK);
			return true;
		} else {
			lpr.setCompareResult(RESULT_FAIL);
			lpr.setErrorMessage("Processed Count[" + count + "]Charge["
					+ totalAmount + "], File Record Count[" + total_count
					+ "]Charge[" + total_charge + "]");
			return false;
		}
	}

	private void setBasicLogInfo(LogProcessResult lpr, File file, String line) {
		lpr.setFileName(file.getAbsolutePath());
		lpr.setRawRecord(line);
		lpr.setSn(null);
		lpr.setDeviceNumber(null);
		lpr.setCompareResult("OK");
		lpr.setErrorMessage(null);
	}

	private List<File> findMatchedFiles(File directory) {
		if (directory != null) {
			File[] fileList = directory.listFiles();
			if (fileList != null) {
				List<File> files = new ArrayList<File>();
				for (File file : fileList) {
					if (file.isDirectory()) {
						LOGGER.debug(file.getAbsolutePath()
								+ " is a Directory!");
					} else if (file.getName().matches(
							"JDMVNO_POP_Reconciliation_[0-9]{8}")) {
						// 日期非严格匹配
						LOGGER.info(file.getAbsolutePath() + " MATCHED!");
						files.add(file);
					}
				}
				return files;
			}
		}
		return null;
	}

	private void initDBStatement() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conOra = DriverManager.getConnection(urlOra, usernameOra, passwordOra);
		conOra.setAutoCommit(false);
		stmt4QueryLogActPay = conOra.prepareStatement(sqlQueryLogActPay);
		stmt4InsLogRechargeCons = conOra
				.prepareStatement(sqlInsertLogRechargeCons);
		stmt4QueryOnlyInBoss = conOra.prepareStatement(sqlQueryOnlyInBoss);
	}

	private String readProperties() {
		Properties p = new Properties();
		try {
			p.load(RechargeCallbackImpl.class.getClassLoader()
					.getResourceAsStream("rechargerecons.properties"));
			urlOra = p.getProperty("DB_URL");
			usernameOra = p.getProperty("DB_USER");
			passwordOra = p.getProperty("DB_PSWD");
			if (urlOra == null) {
				LOGGER.error("Can't Find DB_URL in rechargerecons.properties");				
			} else if (usernameOra == null) {
				LOGGER.error("Can't Find DB_USER in rechargerecons.properties");
			} else if (passwordOra == null) {
				LOGGER.error("Can't Find DB_PSWD in rechargerecons.propertie");
			} else {
				String scanPath = p.getProperty("SCAN_PATH");
				if (scanPath == null) {
					LOGGER.error("Can't Find SCAN_PATH in rechargerecons.propertie");
				} else {
					return scanPath;
				}
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RechargeReconciliation rr = new RechargeReconciliation();
		String scanPath = rr.readProperties();
		if (scanPath != null) {
			rr.process(scanPath);
		}
	}

}

class RechargeReconsObject {
	private String sn;
	private String deviceNumber;
	private String contactChannel;
	private String jdAcctNbr;
	private String payTime;
	private long cashAmount;
	private long jqAmount;
	private long jdAmount;
	private long dqAmount;
	private long lpkAmount;
	private long btAmount;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getContactChannel() {
		return contactChannel;
	}

	public void setContactChannel(String contactChannel) {
		this.contactChannel = contactChannel;
	}

	public String getJdAcctNbr() {
		return jdAcctNbr;
	}

	public void setJdAcctNbr(String jdAcctNbr) {
		this.jdAcctNbr = jdAcctNbr;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public long getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(long cashAmount) {
		this.cashAmount = cashAmount;
	}

	public long getJqAmount() {
		return jqAmount;
	}

	public void setJqAmount(long jqAmount) {
		this.jqAmount = jqAmount;
	}

	public long getJdAmount() {
		return jdAmount;
	}

	public void setJdAmount(long jdAmount) {
		this.jdAmount = jdAmount;
	}

	public long getDqAmount() {
		return dqAmount;
	}

	public void setDqAmount(long dqAmount) {
		this.dqAmount = dqAmount;
	}

	public long getLpkAmount() {
		return lpkAmount;
	}

	public void setLpkAmount(long lpkAmount) {
		this.lpkAmount = lpkAmount;
	}

	public long getBtAmount() {
		return btAmount;
	}

	public void setBtAmount(long btAmount) {
		this.btAmount = btAmount;
	}
}

class LogProcessResult {
	String fileName;
	String rawRecord;
	String sn;
	String deviceNumber;
	String compareResult;
	String errorMessage;
	long amount;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRawRecord() {
		return rawRecord;
	}

	public void setRawRecord(String rawRecord) {
		this.rawRecord = rawRecord;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getCompareResult() {
		return compareResult;
	}

	public void setCompareResult(String compareResult) {
		this.compareResult = compareResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
}
