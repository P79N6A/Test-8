package com.tydic.beijing.billing.memcache.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.codec.digest.DigestUtils;

import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.InfoPay;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeServiceAttrForMemcache;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.PayUserRelForMemCached;
import com.tydic.beijing.billing.dao.SynchronizeInfo;
import com.tydic.beijing.billing.dao.UserCorpInfo;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.memcache.service.DB2Mem;
import com.tydic.beijing.billing.memcache.type.DBInfo;
import com.tydic.uda.service.S;

public class DB2MemUpdateDubbo extends MyApplicationContextUtil implements
		DB2Mem {
	private final static Logger LOGGER = Logger.getLogger(DB2MemImpl.class);
//	private final static String OUTER_NET_TYPE="2";//外网用户类型
	private final static String INNER_NET_TYPE="1";//内网用户类型
	private final static String EFF_FLAG="0";//成员有效标识
	private final static String CORP_EFF_FLAG="0";//集团有效标识
	private final static String CODE_JDN_ATTR ="JDNATTR001";
	
	private String g_imsi;
	
	public String getG_imsi() {
		return g_imsi;
	}

	public void setG_imsi(String g_imsi) {
		this.g_imsi = g_imsi;
	}

	@Override
	public Boolean update(String device_number, String userId)
			throws BasicException {
		Connection conn;

		Statement stmt_InfoUser;
		PreparedStatement ps_LifeUserProduct;
		PreparedStatement ps_PayUserRel;
		PreparedStatement ps_InfoPay;
		PreparedStatement ps_UserCorpInfo;
		PreparedStatement ps_imsi;
		PreparedStatement ps_LifeServiceAttr;
		
		String sql_InfoUser;
		String sql_LifeUserProduct;
		String sql_PayUserRel;
		String sql_InfoPay;
		String sql_UserCorpInfo;
		String sql_imsi;
		String sql_LifeServiceAttr; //京牛要查询用户服务属性
		
		// 局部标量
		// memcached 对象
		UserInfoForMemCached uifm;
		PayUserRelForMemCached lupfm;
		// 资料
		InfoUser iu;
		List<LifeUserProduct> l_lup;
		LifeUserProduct lup;
		ResultSet rs_LifeUserProduct;
		List<PayUserRel> l_lupay;
		List<String> s_payId;
		PayUserRel pyrel;
		ResultSet rs_PayUserRel;
		List<InfoPay> l_ip;
		InfoPay ip;
		ResultSet rs_InfoPay;
		UserCorpInfo uci;
		List<UserCorpInfo> l_uci;
		List<String> l_Imsi;
		ResultSet rs_UserCorpInfo;
		ResultSet rs_Imsi;
		ResultSet rs_Imsi_up;
		String user_id;

		// 构造
		iu = new InfoUser();
		l_lup = new ArrayList<LifeUserProduct>();
		lup = new LifeUserProduct();
		rs_LifeUserProduct = null;
		l_lupay = new ArrayList<PayUserRel>();
		s_payId = new ArrayList<String>();
		pyrel = new PayUserRel();
		rs_PayUserRel = null;
		l_ip = new ArrayList<InfoPay>();
		ip = new InfoPay();
		rs_InfoPay = null;
		uci=new UserCorpInfo();
		l_uci=new ArrayList<UserCorpInfo>();
		l_Imsi=new ArrayList<String>();
		rs_UserCorpInfo=null;
		rs_Imsi=null;
		rs_Imsi_up=null;

		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
		
		// INIT
		DBInfo db_info = (DBInfo) mycontext.getBean("DBInfo");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(db_info.getDb_url(),
					db_info.getUsername(), db_info.getPassword());
			stmt_InfoUser = conn.createStatement();

			// sql_InfoUser =
			// "select USER_ID, TELE_TYPE, DEVICE_NUMBER, USER_PWD, USER_STATUS, CREATE_DATE, ACTIVE_DATE, USER_TYPE, PREPAY_FLAG, LOCAL_NET, DEVELOP_CHANNEL_ID, PRODUCT_ID, PROTO_FLAG, SUB_USER_STATUS, STOP_DATE, VALID_FLAG from info_user";
			sql_LifeUserProduct = "select USER_PRODUCT_ID, USER_ID, OFR_ID, PRODUCT_ID, to_char(eff_date,'YYYYMMDDHH24MISS') EFF_DATE, to_char(exp_date,'YYYYMMDDHH24MISS') EXP_DATE, EFF_FLAG, CREATE_DATE from life_user_product where user_id = ?";
			sql_PayUserRel = "select PAY_USER_ID, USER_ID, PAY_ID, LATN_ID, DEFAULT_TAG, PAYITEM_CODE, PAYBALANCE_CODE, PRIORITY, EFF_DATE, EXP_DATE, EFF_FLAG, LIMIT_TYPE, LIMIT_VALUEA, LIMIT_VALUEB from pay_user_rel where user_id = ?";
			sql_InfoPay = "select PAY_ID, PAY_NAME, CUST_ID, CREATE_DATE, EXP_DATE, PREPAY_FLAG, LOCAL_NET from info_pay where pay_id = ?";
			sql_UserCorpInfo="select a.CORP_ID,a.CORP_TYPE,b.MEM_TYPE,b.EFF_DATE,b.EXP_DATE,b.EFF_FLAG,a.EFF_FLAG from crm_user.info_corp a,crm_user.life_corp_member b where b.mem_dev_number=? and a.corp_id=b.corp_id and a.corp_user_id=b.corp_user_id and a.eff_flag=? and b.eff_flag=? "
					+ " order by b.MEM_TYPE desc ";
			
			sql_imsi="select b.imsi,b.lte_imsi,b."+g_imsi
					+" from info_user a,crm_user.life_imsi b where a.user_id =b.user_id and a.valid_flag=0 and a.device_number= ? and b.eff_flag=0";
			
			
			ps_LifeUserProduct = conn.prepareStatement(sql_LifeUserProduct);
			ps_PayUserRel = conn.prepareStatement(sql_PayUserRel);
			ps_InfoPay = conn.prepareStatement(sql_InfoPay);
			ps_UserCorpInfo=conn.prepareStatement(sql_UserCorpInfo);
			ps_imsi=conn.prepareStatement(sql_imsi);
			
			LOGGER.debug("===============MEMCACHED UPDATE BEGIN===============");

			uifm = new UserInfoForMemCached();
			lupfm = new PayUserRelForMemCached();

			int sum = 0;
			stmt_InfoUser.setFetchSize(1000);
			ResultSet rs = null;

			long runtime1 = System.currentTimeMillis();
			if (userId == null) {
				sql_InfoUser = "select USER_ID, TELE_TYPE, DEVICE_NUMBER, USER_PWD, USER_STATUS, CREATE_DATE, ACTIVE_DATE, USER_TYPE, PREPAY_FLAG, LOCAL_NET, DEVELOP_CHANNEL_ID, PRODUCT_ID, PROTO_FLAG, SUB_USER_STATUS, STOP_DATE, VALID_FLAG,main_ofr_id from info_user where device_number = '"
						+ device_number + "' and valid_flag = 0";
			} else {
				sql_InfoUser = "select USER_ID, TELE_TYPE, DEVICE_NUMBER, USER_PWD, USER_STATUS, CREATE_DATE, ACTIVE_DATE, USER_TYPE, PREPAY_FLAG, LOCAL_NET, DEVELOP_CHANNEL_ID, PRODUCT_ID, PROTO_FLAG, SUB_USER_STATUS, STOP_DATE, VALID_FLAG,main_ofr_id from info_user where device_number = '"
						+ device_number + "' and user_id = '" + userId + "'";
			}
			LOGGER.debug("sql_InfoUser :" + sql_InfoUser);
			rs = stmt_InfoUser.executeQuery(sql_InfoUser);
			while (rs.next()) {
				// UserInfoForMemCached
				// INFO_USER
				iu.setUser_id(rs.getString(1));
				iu.setTele_type(rs.getString(2));
				iu.setDevice_number(rs.getString(3));
				iu.setUser_pwd(rs.getString(4));
				iu.setUser_status(rs.getString(5));
				iu.setCreate_date(rs.getDate(6));
				iu.setActive_date(rs.getDate(7));
				iu.setUser_type(rs.getString(8));
				iu.setPrepay_flag(rs.getString(9));
				iu.setLocal_net(rs.getString(10));
				iu.setDevelop_channel_id(rs.getString(11));
				iu.setProduct_id(rs.getString(12));
				iu.setProto_flag(rs.getString(13));
				iu.setSub_user_status(rs.getString(14));
				iu.setStop_date(rs.getDate(15));
				iu.setMain_ofr_id(rs.getString(17));
				uifm.setInfoUser(iu);
				user_id = rs.getString(1);
				// DEVICENUMBER
				uifm.setDevice_number(device_number);
				//KEYSTRING
				uifm.setKeyString(device_number);
				// LIFE_USER_PRODUCT
				l_lup.clear();
				ps_LifeUserProduct.setString(1, user_id);
				rs_LifeUserProduct = ps_LifeUserProduct.executeQuery();
				while (rs_LifeUserProduct.next()) {
					lup = new LifeUserProduct();
					lup.setUser_product_id(rs_LifeUserProduct.getString(1));
					lup.setUser_id(rs_LifeUserProduct.getString(2));
					lup.setOfr_id(rs_LifeUserProduct.getString(3));
					lup.setProduct_id(rs_LifeUserProduct.getString(4));
					// 带时分秒
					lup.setEff_date(sdf.parse(rs_LifeUserProduct.getString(5)));
					//结束时间加时分秒
					lup.setExp_date(sdf.parse(rs_LifeUserProduct.getString(6)));
					lup.setEff_flag(rs_LifeUserProduct.getString(7));
					lup.setCreate_date(rs_LifeUserProduct.getDate(8));
					l_lup.add(lup);
				}
				rs_LifeUserProduct.close();
				uifm.setUserProducts(l_lup);
				// PAY_USER_REL
				l_lupay.clear();
				s_payId.clear();
				ps_PayUserRel.setString(1, user_id);
				rs_PayUserRel = ps_PayUserRel.executeQuery();
				while (rs_PayUserRel.next()) {
					pyrel = new PayUserRel();
					pyrel.setPay_user_id(rs_PayUserRel.getString(1));
					pyrel.setUser_id(rs_PayUserRel.getString(2));
					pyrel.setPay_id(rs_PayUserRel.getString(3));
					pyrel.setLatn_id(rs_PayUserRel.getLong(4));
					pyrel.setDefault_tag(rs_PayUserRel.getString(5));
					pyrel.setPayitem_code(rs_PayUserRel.getInt(6));
					pyrel.setPaybalance_code(rs_PayUserRel.getInt(7));
					pyrel.setPriority(rs_PayUserRel.getInt(8));
					pyrel.setEff_date(rs_PayUserRel.getDate(9));
					pyrel.setExp_date(rs_PayUserRel.getDate(10));
					pyrel.setEff_flag(rs_PayUserRel.getString(11));
					pyrel.setLimit_type(rs_PayUserRel.getString(12));
					pyrel.setLimit_valuea(rs_PayUserRel.getLong(13));
					pyrel.setLimit_valueb(rs_PayUserRel.getLong(14));

					l_lupay.add(pyrel);
					s_payId.add(pyrel.getPay_id());
				}
				rs_PayUserRel.close();
				uifm.setPayUserRels(l_lupay);
				// INFO_PAY
				l_ip.clear();
				for (String pay_id : s_payId) {
					ps_InfoPay.setString(1, pay_id);
					rs_InfoPay = ps_InfoPay.executeQuery();
					while (rs_InfoPay.next()) {
						ip = new InfoPay();
						ip.setPay_id(rs_InfoPay.getString(1));
						ip.setPay_name(rs_InfoPay.getString(2));
						ip.setCust_id(rs_InfoPay.getString(3));
						ip.setCreate_date(rs_InfoPay.getDate(4));
						ip.setExp_date(rs_InfoPay.getDate(5));
						ip.setPrepay_flag(rs_InfoPay.getString(6));
						ip.setLocal_net(rs_InfoPay.getString(7));

						l_ip.add(ip);
					}
					rs_InfoPay.close();
				}
				uifm.setUserInfoPays(l_ip);
				
				//UserCorpInfo
				l_uci.clear();
				ps_UserCorpInfo.setString(1,device_number);
				ps_UserCorpInfo.setString(2,EFF_FLAG);
				ps_UserCorpInfo.setString(3,CORP_EFF_FLAG);
				rs_UserCorpInfo=ps_UserCorpInfo.executeQuery();
				while (rs_UserCorpInfo.next()) {
					uci=new UserCorpInfo();
					uci.setCorp_id(rs_UserCorpInfo.getString(1));
					uci.setCorp_type(rs_UserCorpInfo.getString(2));
					uci.setMem_type(rs_UserCorpInfo.getString(3));
					uci.setEff_date(rs_UserCorpInfo.getString(4));
					uci.setExp_date(rs_UserCorpInfo.getString(5));
					uci.setEff_flag(rs_UserCorpInfo.getString(6));
					uci.setCorp_eff_flag(rs_UserCorpInfo.getString(7));
					l_uci.add(uci);
				}
				rs_UserCorpInfo.close();
				uifm.setUserCorpInfos(l_uci);
				uifm.setTeleUserType(INNER_NET_TYPE);//内网
				
				// memcached set
				S.get(UserInfoForMemCached.class).create(uifm);
				
				//***************通过device_number得到imsi系列
				l_Imsi.clear();
				LOGGER.debug("====开始通过device_number得到imsi系列");
					ps_imsi.setString(1, device_number);
					rs_Imsi_up=ps_imsi.executeQuery();
					String key_String="IMSI";
					String keyString="";
					if (!rs_Imsi_up.next()) {
						LOGGER.error("============DeviceNumber:"+device_number+",没有找到Imsi的相关信息！");
						rs_Imsi_up.close();
					}else {
						LOGGER.debug("====可以得到imsi了");
						rs_Imsi=ps_imsi.executeQuery();
						LOGGER.debug("====开始通过device_number得到imsi系列");
						while (rs_Imsi.next()) {
							LOGGER.debug("=====判断是哪种imsi=====");
							String imsi=rs_Imsi.getString(1);
							String lte_imsi=rs_Imsi.getString(2);
							String imsig=rs_Imsi.getString(3);
							if (imsi!=null) {
								keyString=key_String+imsi;
								l_Imsi.add(keyString);
							}
							if (lte_imsi!=null) {
								keyString=key_String+lte_imsi;
								l_Imsi.add(keyString);
							}
							if (imsig!=null) {
								keyString=key_String+imsig;
								l_Imsi.add(keyString);
							}
						}
						rs_Imsi.close();
					//Key为Imsi
						if (l_Imsi.size()>0) {
							for(String imsiString:l_Imsi){
								LOGGER.debug("====加入memchache===");
								uifm.setKeyString(imsiString);
								S.get(UserInfoForMemCached.class).create(uifm);
							}
						}else {
							LOGGER.debug("============DeviceNumber:"+device_number+",没有与其对应的Imsi信息！");
						}
					}
					
				
				// PayUserRelForMemCached
				lupfm.setUser_id(BasicType.MEMCACHED_CLUSTER_TAG_1 + user_id);
				lupfm.setUserPayUserRels(l_lupay);
				// memcached set
				S.get(PayUserRelForMemCached.class).create(lupfm);

				sum++;
			}
			
			
			//同步京牛用户关联手机信息轨迹
			//device_number 此时是jdpin+前缀后的md5值
			if (device_number != null && device_number.length() > 11 && userId!=null) {

				sql_LifeServiceAttr = "select USER_ID,SERV_ATTR_CYCLE_ID,SERV_ATTR,SERV_ATTR_VALUE,to_char(EFF_DATE,'YYYYMMDDHH24MISS'),to_char(EXP_DATE,'YYYYMMDDHH24MISS') from crm_user.life_service_attr where user_id ='" + userId + "' and serv_attr= '" + DB2MemUpdateDubbo.CODE_JDN_ATTR + "'";
				ps_LifeServiceAttr = conn.prepareStatement(sql_LifeServiceAttr);
				ResultSet attrResultSet = ps_LifeServiceAttr.executeQuery();

				while (attrResultSet.next()) {
					LOGGER.debug("attrResultSet:"+attrResultSet.getRow());
					// 开始遍历
					String servAttrValue = attrResultSet.getString(4);
					String servAttrCycleId = attrResultSet.getString(2);
					String effDate = attrResultSet.getString(5);
					String expDate = attrResultSet.getString(6);
					
					LifeServiceAttrForMemcache lifeMem = S.get(LifeServiceAttrForMemcache.class).get(DB2MemUpdateDubbo.CODE_JDN_ATTR+servAttrValue);
					
					if (lifeMem == null) {
						
						// memcached查不到这个key
						// 新建这个LifeServiceAttrMem对象
						
						lifeMem = new LifeServiceAttrForMemcache();
						lifeMem.setJdnDeviceNumber(DB2MemUpdateDubbo.CODE_JDN_ATTR+servAttrValue);
						SynchronizeInfo synchronizeInfo = new SynchronizeInfo();
						synchronizeInfo.setDevice_number(device_number);
						synchronizeInfo.setEff_date(effDate);
						synchronizeInfo.setExp_date(expDate);
						synchronizeInfo.setService_attr_cycle_id(servAttrCycleId);
						synchronizeInfo.setUserid(userId);
						List<SynchronizeInfo> synchronizeInfoList = new ArrayList<SynchronizeInfo>();
						synchronizeInfoList.add(synchronizeInfo);
						lifeMem.setSynchronizeInfoList(synchronizeInfoList);
						LOGGER.debug("--- lifeMem:"+lifeMem.toString());
						S.get(LifeServiceAttrForMemcache.class).create(lifeMem);

					} else {
						LOGGER.debug("--- lifeMem:"+lifeMem.toString());
						// 从memcached能查到这个key
						// LifeServiceAttrMem 里应该有一个list变量，包含了这个key所有的关联轨迹
						List<SynchronizeInfo> lifeMemList = lifeMem.getSynchronizeInfoList();
						List<String> serviceAttrCycleIdList = new ArrayList<String>();
						for(SynchronizeInfo sl : lifeMemList){
							serviceAttrCycleIdList.add(sl.getService_attr_cycle_id());
						}
						if (serviceAttrCycleIdList.contains(servAttrCycleId)) {
							// memcache里这个key对应的value里已经包含了这条关联信息，本次有可能是要做变更
							// 把memcached对象的这个list变量里这条记录取出来，并且更新成刚刚从数据库获取的这个attrResultSet.next()
							// 放回memcached
							
							for(int i=0;i<lifeMemList.size();i++){
								if(lifeMemList.get(i).getService_attr_cycle_id().equals(servAttrCycleId)){
									lifeMemList.get(i).setDevice_number(device_number);
									lifeMemList.get(i).setEff_date(effDate);
									lifeMemList.get(i).setExp_date(expDate);
									lifeMemList.get(i).setService_attr_cycle_id(servAttrCycleId);
									lifeMemList.get(i).setUserid(userId);
									break;
								}
							}
							
							lifeMem.setSynchronizeInfoList(lifeMemList);
							S.get(LifeServiceAttrForMemcache.class).update(lifeMem);
							
						} else {
							// 说明list里面没有这个同步信息，本次是新做同步
							// 把本次要同步的记录attrResultSet.next() add 到这个对象的list成员变量里
							SynchronizeInfo synchronizeInfo = new SynchronizeInfo();
							synchronizeInfo.setDevice_number(device_number);
							synchronizeInfo.setEff_date(effDate);
							synchronizeInfo.setExp_date(expDate);
							synchronizeInfo.setService_attr_cycle_id(servAttrCycleId);
							synchronizeInfo.setUserid(userId);
							lifeMemList.add(synchronizeInfo);
							lifeMem.setSynchronizeInfoList(lifeMemList);
							S.get(LifeServiceAttrForMemcache.class).update(lifeMem);
						}
					}
					// 准备好mem对象后，更新到memcached
				}
				ps_LifeServiceAttr.close();
				ps_LifeServiceAttr = null;

			}
			
			rs.close();
			long runtime2 = System.currentTimeMillis();
			LOGGER.debug("run: " + sum + " time: " + (runtime2 - runtime1));
			
			//TERMINAL
			stmt_InfoUser.close();
			ps_LifeUserProduct.close();
			ps_PayUserRel.close();
			ps_InfoPay.close();
			ps_UserCorpInfo.close();
			ps_imsi.close();

			stmt_InfoUser = null;
			ps_LifeUserProduct = null;
			ps_PayUserRel = null;
			ps_InfoPay = null;
			ps_UserCorpInfo=null;
			ps_imsi=null;

			conn.close();
			conn = null;
		} catch (SQLException e) {
			LOGGER.error("SQLException:" + e.toString(),e);
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			LOGGER.error("Exception:" + e.toString(),e);
			System.exit(-1);
		}

		LOGGER.debug("===============MEMCACHED UPDATE END===============");
		return true;
	}

	@Override
	public void load() throws BasicException {
		LOGGER.error("invoke error function DB2MemUpdateDubbo::load()");
		throw new BasicException(ErrorCode.ERR_MEM_INVOKE,
				"invoke error function DB2MemUpdateDubbo::load()");
	}

	@Override
	public void load(int mod, int partition) throws BasicException {
		LOGGER.error("invoke error function DB2MemUpdateDubbo::load(int mod, int partition)");
		throw new BasicException(ErrorCode.ERR_MEM_INVOKE,
				"invoke error function DB2MemUpdateDubbo::load(int mod, int partition)");
	}

	@Override
	public Boolean update(String serviceNumber) throws BasicException {
		return update(serviceNumber, null);
	}
	
	public static String md5(String m) {

		return DigestUtils.md5Hex(m.getBytes());
	}
}
