package com.tydic.beijing.billing.memcache.service.impl;

import com.tydic.beijing.billing.common.BasicType;

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

import com.tydic.beijing.billing.dao.BilActUserRealTimeBill;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBillForMemcached;
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
/**
 * 全量
 *
 */
public class DB2MemImpl extends MyApplicationContextUtil implements DB2Mem {

	private final static Logger LOGGER = Logger.getLogger(DB2MemImpl.class);
	
	private final static String EFF_FLAG="0";
	private final static String CORP_EFF_FLAG="0";
//	private final static String MANAGE_TYPE="01";//管理员角色
//	private final static String COMMON_TYPE="00";//普通成员角色
//	private final static String OUTER_NET_TYPE="2";//外网用户类型
	private final static String INNER_NET_TYPE="1";//内网用户类型
	
	private final static String CODE_JDN_ATTR ="JDNATTR001";
	SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
	
	private String g_imsi;
	
	public String getG_imsi() {
		return g_imsi;
	}

	public void setG_imsi(String g_imsi) {
		this.g_imsi = g_imsi;
	}
	private Connection conn;
	private Statement stmt_InfoUser;
	private Statement stmt_CodeAcctMonth;
	
	private PreparedStatement ps_LifeUserProduct;
	private PreparedStatement ps_PayUserRel;
	private PreparedStatement ps_InfoPay;
	private PreparedStatement ps_BilActUserRealTimeBill;
//	private PreparedStatement ps_Number;
	//UserCorpInfo
	private PreparedStatement ps_UserCorpInfo;
	private PreparedStatement ps_imsi;
//	private PreparedStatement ps_UserCorpInfo_Outer;
	
	private String sql_InfoUser;
	private String sql_LifeUserProduct;
	private String sql_PayUserRel;
	private String sql_InfoPay;
	private String sql_BilActUserRealTimeBill;
	private String sql_CodeAcctMonth;
	private String sql_imsi;
	//UserCorpInfo
	private String sql_UserCorpInfo;

	private UserInfoForMemCached uifm;
	private PayUserRelForMemCached lupfm;
	private BilActUserRealTimeBillForMemcached baurtbfm;

	// 局部标量
	private InfoUser iu;
	List<LifeUserProduct> l_lup;
	LifeUserProduct lup;
	ResultSet rs_LifeUserProduct;
	ResultSet rs_Number;
	List<PayUserRel> l_lupay;
	List<String> s_payId;
	PayUserRel pyrel;
	ResultSet rs_PayUserRel;
	List<InfoPay> l_ip;
	InfoPay ip;
	ResultSet rs_InfoPay;
	BilActUserRealTimeBill baurtb;
	List<BilActUserRealTimeBill> l_baurtb;
	ResultSet rs_BilActUserRealTimeBill;
	//UserCorpInfo
	UserCorpInfo uci;
	List<UserCorpInfo> l_uci;
	ResultSet rs_UserCorpInfo;
	List<String> l_Imsi;
	ResultSet rs_Imsi;
	ResultSet rs_Imsi_up;

	public DB2MemImpl() {
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
		baurtb = new BilActUserRealTimeBill();
		l_baurtb = new ArrayList<BilActUserRealTimeBill>();
		rs_BilActUserRealTimeBill = null;
		l_Imsi=new ArrayList<String>();
		//UserCorpInfo
		uci=new UserCorpInfo();
		l_uci=new ArrayList<UserCorpInfo>();
		rs_UserCorpInfo=null;
		rs_Imsi=null;
		rs_Imsi_up=null;
	}

	@Override
	public void load() throws BasicException {
		if (init() == false) {
			throw new BasicException(ErrorCode.ERR_MEM_CONN_DB,
					"memcached init DB error");
		}

		upload(LOAD_FLAG);

		if (terminal() == false) {
			throw new BasicException(ErrorCode.ERR_MEM_CONN_DB,
					"memcached terminal DB error");
		}
	}

	@Override
	public void load(int mod, int partition) throws BasicException {
		if (init() == false) {
			throw new BasicException(ErrorCode.ERR_MEM_CONN_DB,
					"memcached init DB error");
		}
		
		upload(THREAD_LOAD_FLAG, null, mod, partition);

		if (terminal() == false) {
			throw new BasicException(ErrorCode.ERR_MEM_CONN_DB,
					"memcached terminal DB error");
		}
	}
	
	@Override
	public Boolean update(String device_number) throws BasicException {
		if (init() == false) {
			throw new BasicException(ErrorCode.ERR_MEM_CONN_DB,
					"memcached init DB error");
		}

		upload(RELOAD_FLAG, device_number);

		if (terminal() == false) {
			throw new BasicException(ErrorCode.ERR_MEM_CONN_DB,
					"memcached terminal DB error");
		}
		return true;
	}

	private Boolean init() {
		DBInfo db_info = (DBInfo) mycontext.getBean("DBInfo");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(db_info.getDb_url(),
					db_info.getUsername(), db_info.getPassword());
			stmt_CodeAcctMonth = conn.createStatement();
			stmt_InfoUser = conn.createStatement();

			sql_CodeAcctMonth = "select acct_month, to_char(act_eff_date, 'YYYYMM'), partition_no from code_acct_month where use_tag = 1 ";
			//获取加载账期 加载账期use_tag=1 如果配置文件给出账期则加载配置文件中的，否则加载账期定义表中acct_month最大的账期
			if(db_info.getAcct_month().isEmpty() != true) {
				sql_CodeAcctMonth = sql_CodeAcctMonth + " and acct_month = " + db_info.getAcct_month();
			} else {
				sql_CodeAcctMonth = sql_CodeAcctMonth + " order by acct_month desc";
			}
			LOGGER.info("sql_CodeAcctMonth:[" + sql_CodeAcctMonth + "]");
			ResultSet rs = stmt_CodeAcctMonth.executeQuery(sql_CodeAcctMonth);
			int acct_month = 0;
			String date_month = null;
			String partition_no = null;
			if(rs.next()) {
				acct_month = rs.getInt(1);
				date_month = rs.getString(2);
				partition_no = rs.getString(3);
			}
			if(acct_month == 0 && date_month == null && partition_no == null) {
				return false;
			}
			sql_InfoUser = "select USER_ID, TELE_TYPE, DEVICE_NUMBER, USER_PWD, USER_STATUS, CREATE_DATE, ACTIVE_DATE, USER_TYPE, PREPAY_FLAG, LOCAL_NET, DEVELOP_CHANNEL_ID, PRODUCT_ID, PROTO_FLAG, SUB_USER_STATUS, STOP_DATE, VALID_FLAG,main_ofr_id from info_user where   (valid_flag = 0 or (valid_flag = 1 and user_status not in (403, 404) and nvl(to_char(stop_date, 'YYYYMM'), '209912') >= '" + date_month + "'))";
			sql_LifeUserProduct = "select USER_PRODUCT_ID, USER_ID, OFR_ID, PRODUCT_ID, to_char(eff_date,'YYYYMMDDHH24MISS') EFF_DATE,to_char(exp_date,'YYYYMMDDHH24MISS') EXP_DATE, EFF_FLAG, CREATE_DATE from life_user_product where user_id = ?";
			sql_PayUserRel = "select PAY_USER_ID, USER_ID, PAY_ID, LATN_ID, DEFAULT_TAG, PAYITEM_CODE, PAYBALANCE_CODE, PRIORITY, EFF_DATE, EXP_DATE, EFF_FLAG, LIMIT_TYPE, LIMIT_VALUEA, LIMIT_VALUEB from pay_user_rel where user_id = ?";
			sql_InfoPay = "select PAY_ID, PAY_NAME, CUST_ID, CREATE_DATE, EXP_DATE, PREPAY_FLAG, LOCAL_NET from info_pay where pay_id = ?";
			sql_BilActUserRealTimeBill = "select USER_ID, ACCT_MONTH, PARTITION_NUM, ACCT_ITEM_CODE, UNIT_TYPE_ID, FEE, DISCOUNT_FEE, ADJUST_BEFORE, INSERT_DATE, ORG_FEE from bil_act_user_real_time_bill_" + partition_no + " where user_id = ? and acct_month = " + acct_month;
			sql_UserCorpInfo="select a.CORP_ID,a.CORP_TYPE,b.MEM_TYPE,b.EFF_DATE,b.EXP_DATE,b.EFF_FLAG,a.EFF_FLAG from crm_user.info_corp a,crm_user.life_corp_member b where b.mem_dev_number=? and a.corp_id=b.corp_id and a.corp_user_id=b.corp_user_id and a.eff_flag=? and b.eff_flag=? order by b.MEM_TYPE desc ";
			sql_imsi="select b.imsi,b.lte_imsi,b."+g_imsi
					+" from info_user a,crm_user.life_imsi b  where a.user_id =b.user_id and a.valid_flag=0 and a.device_number= ? and b.eff_flag=0";
			LOGGER.debug("sql_BilActUserRealTimeBill[" + sql_BilActUserRealTimeBill + "]");
			ps_LifeUserProduct = conn.prepareStatement(sql_LifeUserProduct);
			ps_PayUserRel = conn.prepareStatement(sql_PayUserRel);
			ps_InfoPay = conn.prepareStatement(sql_InfoPay);
			ps_BilActUserRealTimeBill = conn.prepareStatement(sql_BilActUserRealTimeBill);
			ps_UserCorpInfo=conn.prepareStatement(sql_UserCorpInfo);
			ps_imsi=conn.prepareStatement(sql_imsi);
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.error("DB init error" + e.toString());
			return false;
		}
		return true;

	}

	private Boolean terminal() {
		try {
			stmt_InfoUser.close();
			ps_LifeUserProduct.close();
			ps_PayUserRel.close();
			ps_InfoPay.close();
			ps_UserCorpInfo.close();//UserCorpInfo
			ps_imsi.close();
			stmt_CodeAcctMonth.close();
			ps_BilActUserRealTimeBill.close();
			stmt_InfoUser = null;
			ps_LifeUserProduct = null;
			ps_PayUserRel = null;
			ps_InfoPay = null;
			ps_UserCorpInfo=null;
			stmt_CodeAcctMonth = null;
			ps_BilActUserRealTimeBill = null;
			ps_imsi=null;
			conn.close();
			conn = null;
		} catch (SQLException e) {
			LOGGER.error("DB terminal error" + e.toString());
			return false;
		}
		return true;

	}

	//for LOAD_FLAG
	private void upload(int flag) {
		upload(flag, null);
	}

	//for UPDATE_FLAG
	private void upload(int flag, String device_number) {
		upload(flag, device_number, 0, 0);
	}
	
	//for THREAD_LOAD_FLAG
	private void upload(int flag, String device_number, int mod, int partition) {
		
		PreparedStatement ps_LifeServiceAttr;
		String sql_LifeServiceAttr; //京牛要查询用户服务属性
		
		LOGGER.debug("test--------");
		uifm = new UserInfoForMemCached();
		lupfm = new PayUserRelForMemCached();
		baurtbfm = new BilActUserRealTimeBillForMemcached();
		
		String user_id;
		List<String> s_pay_id = null;
		int sum = 0;
		try {
			stmt_InfoUser.setFetchSize(1000);
			ResultSet rs = null;

			long runtime1 = System.currentTimeMillis();
			if (flag == RELOAD_FLAG) {
				sql_InfoUser = sql_InfoUser + " and device_number = '"
						+ device_number + "'";
			}
			if (flag == THREAD_LOAD_FLAG) {
				sql_InfoUser = sql_InfoUser + " and mod(user_id, " + mod
						+ ") = " + partition;
			}
			
			
			LOGGER.debug("sql_InfoUser :" + sql_InfoUser);
			rs = stmt_InfoUser.executeQuery(sql_InfoUser);
			while (rs.next()) {
				//UserInfoForMemCached
				user_id = addInfoUser(rs);
				device_number=rs.getString(3);
				uifm.setKeyString(device_number);
				uifm.setTeleUserType(INNER_NET_TYPE);
				addUserCorpInfo(device_number);//添加UserCorpInfo
				addDeviceNumber(rs);
				addLifeUserProduct(user_id);
				s_pay_id = addPayUserRel(user_id);
				addInfoPay(s_pay_id);
				// memcached set
				S.get(UserInfoForMemCached.class).create(uifm);

				//**********得到imsi******************************
				l_Imsi=getImsiByDeviceNumber(device_number);
				
					for(String imsiString:l_Imsi){
						uifm.setKeyString(imsiString);
						S.get(UserInfoForMemCached.class).create(uifm);
					}
				
				//PayUserRelForMemCached
				lupfm.setUser_id(BasicType.MEMCACHED_CLUSTER_TAG_1 + user_id);
				lupfm.setUserPayUserRels(l_lupay);
				// memcached set
				S.get(PayUserRelForMemCached.class).create(lupfm);
				
				//BilActUserRealTimeBillForMemcached
				baurtbfm.setUser_id(BasicType.MEMCACHED_CLUSTER_TAG_1 + BasicType.MEMCACHED_BILL_PREFIX + user_id);
				addUserBill(user_id);
				// memcached set
				S.get(BilActUserRealTimeBillForMemcached.class).create(baurtbfm);
				sum++;
				LOGGER.debug("=========" + rs.getRow());
				
				//同步京牛用户关联手机信息轨迹
				//device_number 此时是jdpin+前缀后的md5值
				if (device_number != null && device_number.length() > 11 && user_id!=null) {

					sql_LifeServiceAttr = "select USER_ID,SERV_ATTR_CYCLE_ID,SERV_ATTR,SERV_ATTR_VALUE,to_char(EFF_DATE,'YYYYMMDDHH24MISS'),to_char(EXP_DATE,'YYYYMMDDHH24MISS') from crm_user.life_service_attr where user_id ='" + user_id + "' and serv_attr= '" + DB2MemImpl.CODE_JDN_ATTR + "'";
					ps_LifeServiceAttr = conn.prepareStatement(sql_LifeServiceAttr);
					ResultSet attrResultSet = ps_LifeServiceAttr.executeQuery();

					while (attrResultSet.next()) {
						LOGGER.debug("attrResultSet:"+attrResultSet.getRow());
						// 开始遍历
						String servAttrValue = attrResultSet.getString(4);
						String servAttrCycleId = attrResultSet.getString(2);
						String effDate = attrResultSet.getString(5);
						String expDate = attrResultSet.getString(6);
						
						LifeServiceAttrForMemcache lifeMem = S.get(LifeServiceAttrForMemcache.class).get(DB2MemImpl.CODE_JDN_ATTR+servAttrValue);

						if (lifeMem == null) {
							// memcached查不到这个key
							// 新建这个LifeServiceAttrMem对象
							
							lifeMem = new LifeServiceAttrForMemcache();
							lifeMem.setJdnDeviceNumber(DB2MemImpl.CODE_JDN_ATTR+servAttrValue);
							SynchronizeInfo synchronizeInfo = new SynchronizeInfo();
							synchronizeInfo.setDevice_number(device_number);
							synchronizeInfo.setEff_date(effDate);
							synchronizeInfo.setExp_date(expDate);
							synchronizeInfo.setService_attr_cycle_id(servAttrCycleId);
							synchronizeInfo.setUserid(user_id);
							List<SynchronizeInfo> synchronizeInfoList = new ArrayList<SynchronizeInfo>();
							synchronizeInfoList.add(synchronizeInfo);
							lifeMem.setSynchronizeInfoList(synchronizeInfoList);
							S.get(LifeServiceAttrForMemcache.class).create(lifeMem);
							LOGGER.debug("--- create lifeMem:"+lifeMem.toString());

						} else {
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
										lifeMemList.get(i).setUserid(user_id);
										break;
									}
								}
								
								lifeMem.setSynchronizeInfoList(lifeMemList);
								S.get(LifeServiceAttrForMemcache.class).update(lifeMem);
								LOGGER.debug("1 --- update lifeMem:"+lifeMem.toString());
								
							} else {
								// 说明list里面没有这个同步信息，本次是新做同步
								// 把本次要同步的记录attrResultSet.next() add 到这个对象的list成员变量里
								SynchronizeInfo synchronizeInfo = new SynchronizeInfo();
								synchronizeInfo.setDevice_number(device_number);
								synchronizeInfo.setEff_date(effDate);
								synchronizeInfo.setExp_date(expDate);
								synchronizeInfo.setService_attr_cycle_id(servAttrCycleId);
								synchronizeInfo.setUserid(user_id);
								lifeMemList.add(synchronizeInfo);
								lifeMem.setSynchronizeInfoList(lifeMemList);
								S.get(LifeServiceAttrForMemcache.class).update(lifeMem);
								LOGGER.debug("2 --- update lifeMem:"+lifeMem.toString());
							}
						}
						// 准备好mem对象后，更新到memcached
					}
					ps_LifeServiceAttr.close();
					ps_LifeServiceAttr = null;

				}
			}
			rs.close();
			long runtime2 = System.currentTimeMillis();
			LOGGER.debug("完成，run: " + sum + " time: " + (runtime2 - runtime1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		LOGGER.debug("==========" + sum + "=============");
	}

	private void addDeviceNumber(ResultSet rs) {
		String device_number = null;
		try {
			device_number = rs.getString(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		uifm.setDevice_number(device_number);

	}
	
	//加载关联的网外的用户信息
//	private void addOuterNumberInfo(String number){
//	    String sql_Number="select mem_dev_number from life_corp_member where corp_id in(select corp_id from life_corp_member where mem_dev_number=?) and mem_dev_number not in(?)";
//	    l_uci.clear();
//	    try {
//			ps_Number=conn.prepareStatement(sql_Number);
//			ps_Number.setString(1, number);
//			ps_Number.setString(2, number);
//			rs_Number=ps_Number.executeQuery();
//			while(rs_Number.next()){
//				String other_number=rs_Number.getString(1);
//				String sql_InfoUser_Two="select USER_ID, TELE_TYPE, DEVICE_NUMBER, USER_PWD, USER_STATUS, CREATE_DATE, ACTIVE_DATE, USER_TYPE, PREPAY_FLAG, LOCAL_NET, DEVELOP_CHANNEL_ID, PRODUCT_ID, PROTO_FLAG, SUB_USER_STATUS, STOP_DATE, VALID_FLAG from info_user where device_number = '"
//						+ other_number + "' and valid_flag = 0";
//				Statement stmt_InfoUser=conn.createStatement();
//				ResultSet rs_Two=stmt_InfoUser.executeQuery(sql_InfoUser_Two);
//				uifm_outer=new UserInfoForMemCached();
//				if (!rs_Two.next()) {
//					uifm_outer.setTeleUserType(OUTER_NET_TYPE);
//					uifm_outer.setDevice_number(other_number);
//					ps_UserCorpInfo_Outer=conn.prepareStatement(sql_UserCorpInfo);
//					ps_UserCorpInfo_Outer.setString(1, other_number);
//					ResultSet rs_UserCorpInfoTwo=ps_UserCorpInfo_Outer.executeQuery();
//					
//					while (rs_UserCorpInfoTwo.next()) {
//						uci=new UserCorpInfo();
//						uci.setCorp_id(rs_UserCorpInfo.getString(1));
//						uci.setCorp_type(rs_UserCorpInfo.getString(2));
//						uci.setMem_type(rs_UserCorpInfo.getString(3));
//						uci.setEff_date(rs_UserCorpInfo.getString(4));
//						uci.setExp_date(rs_UserCorpInfo.getString(5));
//						uci.setEff_flag(rs_UserCorpInfo.getString(6));
//						uci.setCorp_eff_flag(rs_UserCorpInfo.getString(7));
//						l_uci.add(uci);
//					}
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    uifm_outer.setUserCorpInfos(l_uci);
//		//外网号码信息
//		S.get(UserInfoForMemCached.class).create(uifm_outer);
//	}
	
	
	//***************通过device_number得到imsi系列
	private List<String> getImsiByDeviceNumber(String devicenumber){
		l_Imsi.clear();
			try {
				ps_imsi.setString(1, devicenumber);
				rs_Imsi_up=ps_imsi.executeQuery();
				String key_String="IMSI";
				String keyString="";
				if (!rs_Imsi_up.next()) {
					LOGGER.error("============DeviceNumber:"+devicenumber+",没有找到Imsi的相关信息！");
					return l_Imsi;
				}
				rs_Imsi_up.close();
				LOGGER.debug("===============开始加载Imsi=========");
				rs_Imsi=ps_imsi.executeQuery();
				while (rs_Imsi.next()) {
					String imsi=rs_Imsi.getString(1);
					String lte_imsi=rs_Imsi.getString(2);
					String imsig=rs_Imsi.getString(3);
					if (imsi!=null&& imsi.length()>0) {
						keyString=key_String+imsi;
						l_Imsi.add(keyString);
					}
					if (lte_imsi!=null&& lte_imsi.length()>0) {
						keyString=key_String+lte_imsi;
						l_Imsi.add(keyString);
					}
					if (imsig!=null&& imsig.length()>0) {
						keyString=key_String+imsig;
						l_Imsi.add(keyString);
					}
				}
				rs_Imsi.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return l_Imsi;
	}
	
	
	
	//UserCorpInfo
	private void addUserCorpInfo(String number) {
		l_uci.clear();
		try {
				ps_UserCorpInfo.setString(1, number);
				ps_UserCorpInfo.setString(2,EFF_FLAG);
				ps_UserCorpInfo.setString(3,CORP_EFF_FLAG);
				rs_UserCorpInfo = ps_UserCorpInfo.executeQuery();
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		uifm.setUserCorpInfos(l_uci);
	}
	

	private void addInfoPay(List<String> s_pay_id) {
		l_ip.clear();
		try {
			for (String pay_id : s_pay_id) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		uifm.setUserInfoPays(l_ip);
	}

	private List<String> addPayUserRel(String user_id) {
		l_lupay.clear();
		s_payId.clear();
		try {
			ps_PayUserRel.setString(1, user_id);
			rs_PayUserRel = ps_PayUserRel.executeQuery();
			while (rs_PayUserRel.next()) {
				// select USER_PAY_ID, USER_ID, PAY_ID, LATN_ID, DEFAULT_TAG,
				// PAYITEM_CODE, PAYBALANCE_CODE, PRIORITY, EFF_DATE, EXP_DATE,
				// LIMIT_TYPE, LIMIT_VALUEA, LIMIT_VALUEB from life_user_pay
				// where user_id = ?
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		uifm.setPayUserRels(l_lupay);
		return s_payId;
	}

	private void addLifeUserProduct(String user_id) {
		l_lup.clear();
		try {
			ps_LifeUserProduct.setString(1, user_id);
			rs_LifeUserProduct = ps_LifeUserProduct.executeQuery();
			while (rs_LifeUserProduct.next()) {
				lup = new LifeUserProduct();
				lup.setUser_product_id(rs_LifeUserProduct.getString(1));
				lup.setUser_id(rs_LifeUserProduct.getString(2));
				lup.setOfr_id(rs_LifeUserProduct.getString(3));
				lup.setProduct_id(rs_LifeUserProduct.getString(4));
				//需要加时分秒
				lup.setEff_date(sdf.parse(rs_LifeUserProduct.getString(5)));
				lup.setExp_date(sdf.parse(rs_LifeUserProduct.getString(6)));
				lup.setEff_flag(rs_LifeUserProduct.getString(7));
				lup.setCreate_date(rs_LifeUserProduct.getDate(8));
				LOGGER.debug(lup);
				l_lup.add(lup);
			}
			rs_LifeUserProduct.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		uifm.setUserProducts(l_lup);
	}

	private String addInfoUser(ResultSet rs) {
		try {
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
			uifm.setInfoUser(iu);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iu.getUser_id();
	}
	
	public void addUserBill(String user_id) {
		l_baurtb.clear();
		try {
			ps_BilActUserRealTimeBill.setString(1, user_id);
			rs_BilActUserRealTimeBill = ps_BilActUserRealTimeBill.executeQuery();
			while(rs_BilActUserRealTimeBill.next()) {
				baurtb = new BilActUserRealTimeBill();
				baurtb.setUser_id(rs_BilActUserRealTimeBill.getString(1));
				baurtb.setAcct_month(rs_BilActUserRealTimeBill.getInt(2));
				baurtb.setPartition_num(rs_BilActUserRealTimeBill.getString(3));
				baurtb.setAcct_item_code(rs_BilActUserRealTimeBill.getInt(4));
				baurtb.setUnit_type_id(rs_BilActUserRealTimeBill.getInt(5));
				baurtb.setFee(rs_BilActUserRealTimeBill.getLong(6));
				baurtb.setDiscount_fee(rs_BilActUserRealTimeBill.getLong(7));
				baurtb.setAdjust_before(rs_BilActUserRealTimeBill.getLong(8));
				baurtb.setInsert_date(rs_BilActUserRealTimeBill.getString(9));
				baurtb.setOrg_fee(rs_BilActUserRealTimeBill.getLong(10));
				LOGGER.debug(baurtb);
				l_baurtb.add(baurtb);
			}
			rs_BilActUserRealTimeBill.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		baurtbfm.setL_userbill(l_baurtb);
	}

	@Override
	public Boolean update(String serviceNumber, String userId)
			throws BasicException {
		LOGGER.error("invoke error function DB2MemUpdateDubbo::load(int mod, int partition)");
		throw new BasicException(ErrorCode.ERR_MEM_INVOKE,"invoke error function DB2MemUpdateDubbo::update(String serviceNumber, String userId)");
	}

}
