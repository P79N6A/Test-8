/**
 * 
 */
package com.tydic.beijing.billing.interfacex.sendEdm.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONStringer;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.interfacex.sendEdm.Result;

//import com.jd.fsp.application.saf.service.Payable4SafService;

import com.tydic.beijing.billing.interfacex.sendEdm.SendEdm;
import com.tydic.beijing.billing.interfacex.sendEdm.Service;


/**
 * @author dongxuanyi
 * 
 */
/*
 * 
 * 
 * 
 * 语音 2 1 国内语音通话费 0 11 2014/10/24 6:56:56 9 91 语音呼转长途费 0 11 2014/10/24 6:56:56
 * 10 94 漫游有条件呼转费 0 21 2014/10/24 6:56:56 8 84 语音呼转基本费 0 11 2014/10/24 6:56:56
 * 11 97 国际漫游语音费 0 11 2014/10/24 6:56:56 12 100 国际长途及港澳台语音费 0 11 2014/10/24
 * 6:56:56 25 137 国内语音被叫费 0 11 2014/10/24 6:56:56 28 140 10193电话 0 21 2014/10/24
 * 6:56:56 29 143 IP电话 0 21 2014/10/24 6:56:56
 * 
 * 流量 13 103 国内流量费 0 21 2014/10/24 6:56:56 14 106 国际漫游流量费 0 21 2014/10/24
 * 6:56:56 30 146 搜狗流量卡结算费 0 21 2014/10/24 6:56:56
 * 
 * 短信 15 109 国内短信费 0 21 2014/10/24 6:56:56 16 112 国际漫游短信费 0 21 2014/10/24
 * 6:56:56 17 115 国际长途短信费 0 21 2014/10/24 6:56:56 24 136 国内短信被叫费 0 21 2014/10/24
 * 6:56:56
 * 
 * 彩信 18 118 国内彩信费 0 21 2014/10/24 6:56:56 19 121 国际漫游彩信费 0 21 2014/10/24
 * 6:56:56 20 124 国际长途彩信费 0 21 2014/10/24 6:56:56
 * 
 * 代收 21 127 京东图书代收费 0 1 2014/10/24 6:56:56
 * 
 * 会员服务费 22 130 会员服务费 0 21 2014/10/24 6:56:56
 * 
 * 低消差额 31 147 最低消费差额 0 21 2014/10/24 6:56:56
 * 
 * 
 * 23 133 资源包订购费 0 21 2014/10/24 6:56:56
 */
public class SendEdmImpl implements SendEdm {

	private static Logger log = Logger.getLogger(SendEdmImpl.class);
	private String strCons;
	private String drivers;
	private String userName;
	private String password;
	private String addmail;
	private String mailtype;
	private String tokens;

	private String user_mail="";
	private String username = "";// -用户名
	private String USER_ID = "";
	private String mobile = "";// -号码
	private String zhangqi = "";// -账期
	private BigDecimal yuefei = new BigDecimal("0.00");// -月费会员服务费
	private BigDecimal tonghuafei = new BigDecimal("0.00");// -语音通话费
	private BigDecimal caixinfei = new BigDecimal("0.00");// -彩信费
	private BigDecimal liuliangfei = new BigDecimal("0.00");// -流量费
	private BigDecimal duanxinfei = new BigDecimal("0.00");// -短信费
	private BigDecimal daishoufei = new BigDecimal("0.00");// -代收费
	private BigDecimal zuidichae = new BigDecimal("0.00");// --低消差额
	private BigDecimal feiyongheji = new BigDecimal("0.00");// -费用合计
	private BigDecimal yan = new BigDecimal("100");
	
	public boolean sendmailsState() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.MONTH, -1);// 日期加3个月
		String reStrmon = sdf.format(rightNow.getTime());
		try {
			Class.forName(drivers);
			java.sql.Connection conn = DriverManager.getConnection(strCons,userName, password);
			if (conn.isClosed())
				System.out.println("数据库连接失败！");
			Statement statement = conn.createStatement();
			String info_use ="update info_edm_user"+reStrmon+" set State=1";
			try {
				statement.executeUpdate(info_use);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				log.error("statfee数据库查询错误！！"+e.getMessage());
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			log.error("数据库查询错误！！" + e.getMessage());
		}
		
		return true;
	}
	
	
	
	public boolean saveUserinfo() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.MONTH, -1);// 日期加3个月
		String reStrmon = sdf.format(rightNow.getTime());
		try {
			Class.forName(drivers);
			java.sql.Connection conn = DriverManager.getConnection(strCons,userName, password);
			if (conn.isClosed())
				System.out.println("数据库连接失败！");
			Statement statement = conn.createStatement();
			Statement infouses = conn.createStatement();
			Statement infouses1 = conn.createStatement();
			ResultSet rs = null;
			String mailssql = "select t.user_id,t.tele_type,t.user_type,t.create_date,t.prepay_flag,t.local_net,t.develop_channel_id,t.operators_type,'JD',t.flag_4g,t.product_id,t.device_number from CRM_USER.INFO_USER t  where  t.tele_type ='CTC' ";// where rownum <=10 
			//log.debug("发送sql=" + mailssql);
			System.out.println("mailssql=" + mailssql);
			try {
				rs = statement.executeQuery(mailssql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				log.error("statement数据库查询错误！！" + e.getMessage());
			}
			int is = 1;
			JSONObject jsonObjectall = new JSONObject(); 
			JSONArray resultListall = new JSONArray();  
			JSONObject jsonObject = new JSONObject();
		    FileWriter fileWritter;
			try {
				fileWritter = new FileWriter("userFile.dat",true);

            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			
			while (rs.next()) {
				JSONObject jsonObjects = new JSONObject();
				JSONObject jsonObject1 = new JSONObject();
		        jsonObject.put("user_id", rs.getString("user_id"));
		        jsonObject.put("tele_type", rs.getString("tele_type"));
		        jsonObject.put("user_type", rs.getString("user_type"));
		        jsonObject.put("create_date", rs.getString("create_date"));
		        jsonObject.put("prepay_flag", rs.getString("prepay_flag"));
		        jsonObject.put("local_net", rs.getString("local_net"));
		        jsonObject.put("develop_channel_id", rs.getString("develop_channel_id"));
		        jsonObject.put("operators_type", rs.getString("operators_type"));
		        jsonObject.put("flag_4g", rs.getString("flag_4g"));
		        jsonObject.put("product_id", rs.getString("product_id"));
		        jsonObject.put("device_number", rs.getString("device_number"));
		        jsonObject.put("resale_operators_type", "JD");
		        
		        ResultSet bs = infouses.executeQuery("SELECT a.* FROM  pys_product_resource a, crm_user.life_ofr b WHERE a.product_id = b.ofr_id and b.user_id='"+rs.getString("user_id")+"'");
		        JSONArray resultList = new JSONArray();  
		        while (bs.next()) {
		        	
		        	jsonObjects.put("product_id", bs.getString("product_id"));
		        	jsonObjects.put("product_type", bs.getString("product_type"));
		        	jsonObjects.put("product_name", bs.getString("product_name"));
		        	jsonObjects.put("resource_type", bs.getString("resource_type"));
		        	jsonObjects.put("resource_value", bs.getString("resource_value"));
		        	jsonObjects.put("amount", bs.getString("amount"));
		        	jsonObjects.put("cycle_type", bs.getString("cycle_type"));
		        	jsonObjects.put("cycle_value", bs.getString("cycle_value"));
		        	resultList.add(jsonObjects);
		        	
		        }
		        
		        ResultSet bc = infouses1.executeQuery("SELECT * FROM CRM_USER.LIFE_IMSI where user_id='"+rs.getString("user_id")+"'");
		        JSONArray resultList1 = new JSONArray();  
		        while (bc.next()) {
		        
		        	jsonObject1.put("imsi", bc.getString("IMSI"));		 
		        	resultList1.add(jsonObject1);
		        	
		        }
		        
		        jsonObject.element("products", resultList);  
		        jsonObject.element("imsis", resultList1);  
		      		          
		        
		        if(rs.getRow()!=1)
		        {
		        	bufferWritter.newLine();
		        }
		        bufferWritter.write(jsonObject.toString());
		        
		        // resultListall.add(jsonObject);
		        //resultListall.element(jsonObject);
		        
		        
			}
		
			

           // bufferWritter.write(resultListall.toString());
            bufferWritter.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			//System.out.println("添加属性后的对象：" + jsonObject);
			statement.close();
			infouses.close();
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			System.out.println("sandpysus=!!!!!!!!!!!!!!!!!!!!!!!"+e.getMessage());
		}
		return true;
	
	}
	
	
	public boolean sendmails() {

		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.MONTH, -1);// 日期加3个月
		String reStrmon = sdf.format(rightNow.getTime());
		try {
			Class.forName(drivers);
			java.sql.Connection conn = DriverManager.getConnection(strCons,userName, password);
			if (conn.isClosed())
				System.out.println("数据库连接失败！");
			Statement statement = conn.createStatement();
			Statement infouses = conn.createStatement();
			ResultSet rs = null;
			String mailssql = "select * from info_edm_user" + reStrmon;
			log.debug("发送sql=" + mailssql);
			System.out.println("mailssql=" + mailssql);
			try {
				rs = statement.executeQuery(mailssql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				log.error("statement数据库查询错误！！" + e.getMessage());
			}
			int is = 1;
			while (rs.next()) {

				String payables = "{\"emailAddress\":\""
						+ rs.getString("user_mail") + "\",\"keyValues\":["
						+ "{\"key\":\"username\",\"value\":\""
						+ rs.getString("username") + "\"},"
						+ "{\"key\":\"yuefei\",\"value\":\""
						+ rs.getString("yuefei") + "\"},"
						+ "{\"key\":\"tonghuafei\",\"value\":\""
						+ rs.getString("tonghuafei") + "\"},"
						+ "{\"key\":\"caixinfei\",\"value\":\""
						+ rs.getString("caixinfei") + "\"},"
						+ "{\"key\":\"liuliangfei\",\"value\":\""
						+ rs.getString("liuliangfei") + "\"},"
						+ "{\"key\":\"duanxinfei\",\"value\":\""
						+ rs.getString("duanxinfei") + "\"},"
						+ "{\"key\":\"daishoufei\",\"value\":\""
						+ rs.getString("daishoufei") + "\"},"
						+ "{\"key\":\"mobile\",\"value\":\""
						+ rs.getString("mobile") + "\"},"
						+ "{\"key\":\"zhangqi\",\"value\":\""
						+ rs.getString("zhangqi") + "月\"},"
						+ "{\"key\":\"zuidichae\",\"value\":\""
						+ rs.getString("zuidichae") + "\"},"
						+ "{\"key\":\"xiaofeichae\",\"value\":\""
						+ rs.getString("zuidichae") + "\"},"
						+ "{\"key\":\"feiyongheji\",\"value\":\""
						+ rs.getString("feiyongheji") + "\"}],"
						+ "\"pin\":\" \"," + "\"skuId\":\" \"," + "\"type\":"
						+ mailtype + "}";

				Service service = new Service();
				Service.setTOKEN(tokens);
				Service.setUrl(addmail);
				int tesmp = service.sendEmailByJson(payables);
				String zhuangtais = "发送" + Result.getRusult(tesmp).toString();
				String info_use ="update info_edm_user"+reStrmon+" set ret_info='"+zhuangtais+"' where USER_ID='"+rs.getString("USER_ID")+"' and user_mail = '"+rs.getString("user_mail")+"' and username= '"+rs.getString("username")+"'";
				try {
					infouses.executeUpdate(info_use);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					log.error("statfee数据库查询错误！！"+e.getMessage());
				}
				
				log.debug("payable=" + payables);
				log.debug(zhuangtais);

			}

			statement.close();
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			log.error("数据库查询错误！！" + e.getMessage());
		}
		return true;
	}
	
	
	public boolean sendmail() throws ClassNotFoundException, SQLException {
		// TODO 自动生成的方法存根
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.MONTH, -1);// 日期加3个月
		String reStrmon = sdf.format(rightNow.getTime());
		Class.forName(drivers);
		java.sql.Connection conn = DriverManager.getConnection(strCons,
				userName, password);
		if (conn.isClosed())
			System.out.println("数据库连接失败！");
		Statement statement = conn.createStatement();
		
		ResultSet  mails=null, fees = null;
		try {
			String mailssql="select a.user_id,a.email,b.customer_name,a.device_number from crm_user.info_post_jd a,crm_cust.info_person b where a.customer_id=b.customer_id";
			log.debug("mailssql=" + mailssql);
			System.out.println("mailssql="+mailssql);
			try {
			mails = statement.executeQuery(mailssql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				log.error("statement数据库查询错误！！"+e.getMessage());
			}
			int is=1;
			while (mails.next()) {
				
				user_mail = mails.getString("email");
				if (user_mail != "" || user_mail != null) {
					 username = mails.getString("customer_name");;// -用户名
					 yuefei = new BigDecimal("0.00");// -月费会员服务费
					 tonghuafei = new BigDecimal("0.00");// -语音通话费
					 caixinfei = new BigDecimal("0.00");// -彩信费
					 liuliangfei = new BigDecimal("0.00");// -流量费
					 duanxinfei = new BigDecimal("0.00");// -短信费
					 daishoufei = new BigDecimal("0.00");// -代收费
					 mobile = mails.getString("device_number");// -号码
					 zhangqi = reStrmon;// -账期
					 zuidichae = new BigDecimal("0.00");// --低消差额
					 feiyongheji = new BigDecimal("0.00");// -费用合计
					 yan = new BigDecimal("100");
					 USER_ID = mails.getString("user_id");
					 //USER_ID="494480";
					boolean feesboolsbs=false; 
					String freesql = "select sum(t.fee) as fee,sum(t.owe_fee) owe_fee,t.acct_item_code as acct_item_code from BIL_ACT_BILL t,code_acct_month b "
							+ "where t.acct_month=b.acct_month and b.partition_no= "+zhangqi+" and t.user_id='"
							+ USER_ID
							+ "'  group by t.user_id,t.acct_item_code";
					log.debug("freesql=" + freesql);
					System.out.println("freesql="+freesql);
					Statement statfee = conn.createStatement();
					try {
						fees = statfee.executeQuery(freesql);
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						log.error("statfee数据库查询错误！！"+e.getMessage());
					}
					while (fees.next()) {
						if (mailtype.equals("2079")) {
							feesboolsbs=true;
							String acct_item_name = fees
									.getString("acct_item_code");
							if (acct_item_name.equals("1")
									|| acct_item_name.equals("91")
									|| acct_item_name.equals("94")
									|| acct_item_name.equals("84")
									|| acct_item_name.equals("97")
									|| acct_item_name.equals("100")
									|| acct_item_name.equals("137")
									|| acct_item_name.equals("140")
									|| acct_item_name.equals("143")) { // 语音

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									tonghuafei = tonghuafei.add(new BigDecimal(
											fee).divide(yan));
								// yuan = new BigDecimal(fen).divide(new
								// BigDecimal(MULTIPLIER)).setScale(2).toString
								
							}
							if (acct_item_name.equals("103")
									|| acct_item_name.equals("106")
									|| acct_item_name.equals("146")) {// 流量

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									liuliangfei = liuliangfei
											.add(new BigDecimal(fee)
													.divide(yan));
							}
							if (acct_item_name.equals("109")
									|| acct_item_name.equals("112")
									|| acct_item_name.equals("115")
									|| acct_item_name.equals("136")) {// 短信

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									duanxinfei = duanxinfei.add(new BigDecimal(
											fee).divide(yan));
							}
							if (acct_item_name.equals("118")
									|| acct_item_name.equals("121")
									|| acct_item_name.equals("124")) {// 彩信

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									caixinfei = caixinfei.add(new BigDecimal(
											fee).divide(yan));
							}
							if (acct_item_name.equals("127")) {// 代收

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									daishoufei = daishoufei.add(new BigDecimal(
											fee).divide(yan));
							}
							if (acct_item_name.equals("130")) {// 会员服务费

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									yuefei = yuefei.add(new BigDecimal(fee)
											.divide(yan));
							}
							if (acct_item_name.equals("147")) {// 低消差额

								String fee = fees.getString("fee");
								if (!fee.equals("") && !fee.equals(null))
									zuidichae = zuidichae.add(new BigDecimal(
											fee).divide(yan));
							}

						}

					}

					
					if (feesboolsbs==true) {
						feiyongheji = feiyongheji.add(yuefei);
						feiyongheji = feiyongheji.add(tonghuafei);
						feiyongheji = feiyongheji.add(caixinfei);
						feiyongheji = feiyongheji.add(liuliangfei);
						feiyongheji = feiyongheji.add(duanxinfei);
						feiyongheji = feiyongheji.add(daishoufei);
						feiyongheji = feiyongheji.add(zuidichae);
						
					
						String info_edm_user = "insert into info_edm_user"+reStrmon+" (user_mail ,username ,USER_ID,mobile,zhangqi,yuefei,tonghuafei,caixinfei,liuliangfei,duanxinfei,daishoufei,zuidichae,feiyongheji,yan,State,ret_info)values('"
								+ user_mail+"','"
								+ username+"','"
				                + USER_ID+"','"
					            + mobile+"','"   
					            + zhangqi+"','" 
					            + yuefei+"','"  
					            + tonghuafei+"','"  
					            + caixinfei+"','" 
					            + liuliangfei+"','"  
					            + duanxinfei+"','"  
					            + daishoufei+"','"  
					            + zuidichae+"','" 
					            + feiyongheji+"','"
					            + yan+"',"
					            + "0,"
					            + "'0'"
								+ ")";
						log.debug("info_edm_user=" + info_edm_user);
						System.out.println("info_edm_user="+info_edm_user);
						try {
							 statfee.executeUpdate(info_edm_user);
						} catch (SQLException e) {
							// TODO 自动生成的 catch 块
							log.error("statfee数据库查询错误！！"+e.getMessage());
						}
					}
					fees.close();
					statfee.close();
						
					

					/*
					 * System.out.println("USER_ID="+USER_ID);
					 * System.out.println("帐期="+zhangqi);
					 * System.out.println("电话="+mobile);
					 * System.out.println("会员服务费="+yuefei.toString());
					 * System.out.println("语音通话费="+tonghuafei.toString());
					 * System.out.println("彩信费="+caixinfei.toString());
					 * System.out.println("流量费="+liuliangfei.toString());
					 * System.out.println("短信费="+duanxinfei.toString());
					 * System.out.println("代收费="+daishoufei.toString());
					 * System.out.println("低消差额="+zuidichae.toString());
					 * System.out.println("费用合计 ="+feiyongheji.toString());
					 */
				}
				is++;
				log.debug("user:" +is );
			}

			
			mails.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			log.error("数据库查询错误！！"+e.getMessage());
		}

		return true;
	}

	public void setDrivers(String drivers) {
		this.drivers = drivers;
	}

	public void setStrCons(String strCons) {
		this.strCons = strCons;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddmail(String addmail) {
		this.addmail = addmail;
	}

	public void setMailtype(String mailtype) {
		this.mailtype = mailtype;
	}

	public void setTokens(String tokens) {
		this.tokens = tokens;
	}
}
