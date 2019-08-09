package com.tydic.beijing.billing.account.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.type.BilActUserRealTimeBill;
import com.tydic.beijing.billing.account.type.BilActUserRealTimeBillForFile;
import com.tydic.beijing.billing.dao.BilActRealTimeBill;
import com.tydic.beijing.billing.dao.BilActRealTimeBillForFile;
import com.tydic.beijing.billing.dao.LogActPreWriteoff;
import com.tydic.beijing.billing.dao.LogActPreWriteoffForFile;

public class Dao2File extends Thread {
	private final static Logger LOGGER = Logger.getLogger(Dao2File.class);
	private String fileName;
	private String daoName;
	private int line_num;
	private long time;
	private int LINELIMIT;
	private int TIMELIMIT;
	private String path;
	private int seq;
	private SimpleDateFormat df;
	private File fileIns;
	private BufferedOutputStream writeBuff;
	private String data;
	private long channel_credit;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getLINELIMIT() {
		return LINELIMIT;
	}
	public void setLINELIMIT(int lINELIMIT) {
		LINELIMIT = lINELIMIT;
	}
	public int getTIMELIMIT() {
		return TIMELIMIT;
	}
	public void setTIMELIMIT(int tIMELIMIT) {
		TIMELIMIT = tIMELIMIT;
	}
	public long getChannel_credit() {
		return channel_credit;
	}
	public void setChannel_credit(long channel_credit) {
		this.channel_credit = channel_credit;
	}
	public Dao2File() {
		df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
		if (this instanceof BilActRealTimeBill) {
			daoName = "BilActRealTimeBill";
		} else if (this instanceof LogActPreWriteoff) {
			daoName = "LogActPreWriteoff";
		} else if (this instanceof BilActUserRealTimeBill) {
			daoName = "BilActUserRealTimeBill";
		}
		init();
	}
	

	public synchronized void writeFile(Object obj, int size) throws IOException {
		if(fileName == null) {
			createFile();
		}
		
		if(line_num+size > LINELIMIT) {
			//关闭文件，move到指定目录
			LOGGER.debug("Dao2File size " + size + " line " + line_num + " change file");
			writeBuff.flush();
			writeBuff.close();
			LOGGER.debug("create file " + fileName + " close");
			//更改文件名tmp
			changeName();
			createFile();
		}
		if(obj != null) {
			if (obj instanceof BilActRealTimeBillForFile) {
				BilActRealTimeBillForFile new_obj = (BilActRealTimeBillForFile) obj;
				List<BilActRealTimeBill> list = new_obj.getList();
				for(BilActRealTimeBill bill : list) {
					data = bill.dao2Line();
					writeBuff.write(data.getBytes(), 0, data.length());
					line_num ++;
				}

			} else if (obj instanceof LogActPreWriteoffForFile) {
				LogActPreWriteoffForFile new_obj = (LogActPreWriteoffForFile) obj;
				List<LogActPreWriteoff> list = new_obj.getList();
				for(LogActPreWriteoff log : list) {
					data = log.dao2Line();
					writeBuff.write(data.getBytes(), 0, data.length());
					line_num ++;
				}
			} else if (obj instanceof BilActUserRealTimeBillForFile) {
				BilActUserRealTimeBillForFile new_obj = (BilActUserRealTimeBillForFile) obj;
				List<BilActUserRealTimeBill> list = new_obj.getList();
				for(BilActUserRealTimeBill ubill : list) {
					data = ubill.dao2Line();
					writeBuff.write(data.getBytes(), 0, data.length());
					line_num ++;
				}
			}
		}
		
		if(line_num >= LINELIMIT || System.currentTimeMillis()-time >= TIMELIMIT) {
			if(line_num == 0) {
				//文件为空，继续使用该文件
				LOGGER.debug("Dao2File line 0 don't change file");
			} else {
				//关闭文件，move到指定目录
				LOGGER.debug("Dao2File line " + line_num + " change file");
				writeBuff.flush();
				writeBuff.close();
				LOGGER.debug("create file " + fileName + " close");
				//更改文件名tmp
				changeName();
				createFile();
			}
		}
	}
	
	public void run() {
		while(true) {
			LOGGER.debug("Dao2File time " + TIMELIMIT + " done change file");
			try {
				writeFile(null, 0);
				Thread.sleep(TIMELIMIT);
			} catch (Exception e) {
				LOGGER.error("Dat2File run function error:" + e.toString());
			} 
			
		}
	}
	
	private File createFile() {
		while(true) {
			String nameTime = df.format(new Date());// new Date()为获取当前系统时间
			String processName = java.lang.management.ManagementFactory
					.getRuntimeMXBean().getName();
			String processID = processName.substring(0, processName.indexOf('@'));
			seq = (++seq)%1000;
			fileName = daoName + "_" + processID + "_" + String.format("%03d", channel_credit) + "_" + nameTime + "_" + String.format("%03d", seq) + ".r.tmp";
			LOGGER.debug("create file " + fileName + " open");
			fileIns = new File(path, fileName);
			if(fileIns.exists() == false) {
				try {
					//打开一个文件
					fileIns.createNewFile();
					writeBuff = new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(fileIns)));
					line_num = 0;
					time = System.currentTimeMillis();
				} catch (IOException e) {
					LOGGER.error("Dao2File createFile erro: " + e.toString());
				}
				break;
			}
		}
		return null;
	}
	
	private void changeName() {
		int dest = fileName.lastIndexOf(".");
		String nameNoTmp = fileName.substring(0, dest);
		fileIns.renameTo(new File(path, nameNoTmp));
	}
	
	private void init() {
		InputStream is = Dao2File.class.getResourceAsStream("/Dao2File.properties");
		Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			LOGGER.error("Dao2File config error ", e);
		}
		path = prop.getProperty("path");
		String time_pro = prop.getProperty("timelimit");
		String line_pro = prop.getProperty("linelimit");
		LINELIMIT = Integer.parseInt(line_pro);
		TIMELIMIT = Integer.parseInt(time_pro);
		
		fileName = null;
		path = path + daoName;
		line_num = 0;
		time = System.currentTimeMillis();
		seq = 0;
		writeBuff = null;
		data = null;	
	}
}
