package com.tydic.beijing.bvalue.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.common.InfoBpoolErrorCode;
import com.tydic.beijing.bvalue.dao.InfoBpool;
import com.tydic.beijing.bvalue.dao.LogTradeBpoolHis;
import com.tydic.beijing.bvalue.dao.LogTradeBpoolHisDtl;
import com.tydic.beijing.bvalue.service.ICreateBValuePoolAccess;


public class CreateBValuePoolAccessImpl implements ICreateBValuePoolAccess {

	private static Logger log = Logger.getLogger(CreateBValuePoolAccessImpl.class);
	
	/**
	 * B池新增活动、修改
	 * author：卞森
	 */
	@Transactional(rollbackFor=Exception.class)
	public JSONObject createAccess(JSONObject inputJson){
		JSONObject ret = new JSONObject();
		DbTool dbTool = new DbTool();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		SimpleDateFormat mysqlSdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		try{
			log.debug("inputJson==>"+inputJson.toString());
			//根据opertype进行活动管理
			String opertype = inputJson.getString("OperType");
			log.debug("get OperType=>"+opertype);
			
			//新增操作
			if ("1".equals(opertype)) {

				//String bpoolid =  Common.getUUID();
				String bpoolid = inputJson.containsKey("ActivityId") ? inputJson.getString("ActivityId") : Common.getUUID();
				String bpoolname =   inputJson.getString("ActivityName");// 活动名称
				String departMenttype =  inputJson.getString("DepartMentType");// 部门活动类型	
				if ("1".equals(departMenttype))// 本部门活动
					log.debug("\t本部门活动[" + departMenttype + "]");
				else if("2".equals(departMenttype))// 合作活动						
					log.debug("\t合作活动[" + departMenttype + "]");
				else {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "入参格式错误");
					log.error("新增操作添加活动类型时必须为1或2");
					return ret;
				} 
				String activitytype = inputJson.getString("ActivityType");//活动分类
				if ("1".equals(activitytype))// 发放类
					log.debug("\f发放场类[" + activitytype + "]");
				else if("2".equals(activitytype))// 扣减类
					log.debug("\t扣取类[" + activitytype + "]");
				else{			
						ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
						ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
						ret.put("ErrorMessage", "入参格式错误");
						log.error("新增操作添加活动分类时必须为1、2");
						return ret;
				} 
				String effdate =   inputJson.getString("StartTime");// 活动生效时间

				if (effdate == null || effdate.isEmpty()) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "新增操作时，活动生效时间必填");
					log.error("新增操作时，活动生效时间必填");
					return ret;
				}
				
				effdate = effdate.substring(0,10)+" 00-00-00";

				try {
					effdate = DateUtil.changeToInternalFormat(effdate);
				} catch (ParseException e) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "日期格式不正确");
					log.error("日期格式不正确");
					return ret;
				}
				String expdate =   inputJson.getString("EndTime");// 活动失效时间
				log.debug("\t活动失效时间[" + expdate + "]");
				if (expdate == null || expdate.isEmpty()) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "新增操作时，活动失效时间必填");
					log.error("新增操作时，活动失效时间必填");
					return ret;
				}
				
				expdate = expdate.substring(0,10)+" 23-59-59";

				try {
					expdate = DateUtil.changeToInternalFormat(expdate);
				} catch (ParseException e) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "日期格式不正确");
					log.error("日期格式不正确");
					return ret;
				}
				String pwd =   inputJson.getString("Pwd");//活动密钥
				Long bPool =  inputJson.getLong("BPool");//B池
				Long costvalue =  inputJson.getLong("CostValue");//相应成本（元）
//				Long usedbalance = (Long) inputJson.getLong("used_balance");//已发放B值
//				log.debug("\t已发放B值[" + usedbalance + "]");
				Long balance = bPool;			
											
				Long threshold = inputJson.getLong("Threshold");//阀值
				if(threshold>=1 && threshold<=99)
					log.debug("\t阀值[" + threshold + "]");
				else{
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "入参格式错误");
					log.error("新增操作阀值为1-99");
					return ret;
				}
				
				Long threstatus =  inputJson.getLong("ThreStatus");// 告警状态
				if (threstatus==1)// 正常
					log.debug("\t正常[" + threstatus + "]");
				else if(threstatus==2)// 告警
					log.debug("\t告警[" + threstatus + "]");
				else if(threstatus==3)// 告警已处理
					log.debug("\t告警已处理[" + threstatus + "]");
				else			
					log.debug("\t告警状态[" + threstatus + "]");
				
				Long activitystatus =  inputJson.getLong("Status");// 活动状态	
				if (activitystatus==1)// 开
					log.debug("\t开[" + activitystatus + "]");
				else if(activitystatus==0)// 关
					log.debug("\t关[" + activitystatus + "]");
				else{
				
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "入参格式错误");
					log.error("新增操作添加活动状态时必须为1或2");
					return ret;
				} 

				String createtime = mysqlSdf.format(new Date());
				
				String createstaff =   inputJson.getString("CreateStaff");//活动创建人
				String contactphone =  inputJson.getString("ContactPhone");//紧急电话
				if (contactphone == null || contactphone.isEmpty()) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "新增操作时，紧急电话必填");
					log.error("新增操作时，紧急电话必填");
					return ret;
				}	
				String contactemail = inputJson.getString("ContactEmail");//紧急邮箱
				String note =   inputJson.getString("Note");//备注
				
				//log_trade_bpool_his_dtl

				//String activityid =   inputJson.getString("activity_id");//活动平台侧的活动ID
				//String jdpin =   inputJson.getString("jdpin");//接收B值的jdpin
				Long bvalue = 0L;
//				Long oldbpool =  inputJson.getLong("old_bpool");//发放前B池余额
//				Long newbpool =   inputJson.getLong("new_bpool");//发放后B池余额
				
				
				//log_trade_bpool_his
		
				String trade_type_code = "601";//订单类型:新增
				Long partition_id =  (long) (cal.get(Calendar.MONTH) + 1);//按月分区
//				String order_no =  inputJson.getString("ORDER_NO");
//				String process_time =   inputJson.getString("PROCESS_TIME");

				Long process_tag = 0L;
				String reserve_c1 ="";
				String reserve_c2 ="";
				String reserve_c3 =""; 
				String reserve_c4 = "";

				//insert info_bpool
				
				InfoBpool infoBpool = new InfoBpool();
				infoBpool.setbpool_id(bpoolid);
				infoBpool.setbpool_name(bpoolname);
				infoBpool.setdepartMent_type(departMenttype);
				infoBpool.setactivity_type(activitytype);
				infoBpool.seteff_date(effdate);
				infoBpool.setexp_date(expdate);
				infoBpool.setpwd(pwd);
				infoBpool.setbpool(bPool);
				infoBpool.setcost_value(costvalue);
				infoBpool.setused_balance(0);
				infoBpool.setbalance(balance);
				infoBpool.setthreshold(threshold);
				infoBpool.setthre_status(threstatus);
				infoBpool.setactivity_status(activitystatus);
				infoBpool.setcreate_time(createtime);
				infoBpool.setcreate_staff(createstaff);
				infoBpool.setcontact_phone(contactphone);
				infoBpool.setcontact_email(contactemail);
				infoBpool.setnote(note);
				dbTool.insertInfoBpool(infoBpool);

				
				//insert log_trade_bpool_his_dtl

				LogTradeBpoolHisDtl logTradeBpoolHisDtl = new LogTradeBpoolHisDtl();
				String tradeId  = Common.getUUID();
				logTradeBpoolHisDtl.settrade_id(tradeId);
				logTradeBpoolHisDtl.setbpool_id(bpoolid);
				logTradeBpoolHisDtl.setbpool_name(bpoolname);
				logTradeBpoolHisDtl.setdepartMent_type(departMenttype);
				logTradeBpoolHisDtl.setactivity_type(activitytype);
				logTradeBpoolHisDtl.seteff_date(effdate);
				logTradeBpoolHisDtl.setexp_date(expdate);
				logTradeBpoolHisDtl.setpwd(pwd);
				logTradeBpoolHisDtl.setbpool(bPool);
				logTradeBpoolHisDtl.setcost_value(costvalue);
				logTradeBpoolHisDtl.setused_balance(0);
				logTradeBpoolHisDtl.setbalance(balance);
				logTradeBpoolHisDtl.setthreshold(threshold);
				logTradeBpoolHisDtl.setthre_status(threstatus);
				logTradeBpoolHisDtl.setactivity_status(activitystatus);
				logTradeBpoolHisDtl.setcreate_time(createtime);
				logTradeBpoolHisDtl.setcreate_staff(createstaff);
				logTradeBpoolHisDtl.setcontact_phone(contactphone);
				logTradeBpoolHisDtl.setcontact_email(contactemail);
				logTradeBpoolHisDtl.setactivity_id("");
				logTradeBpoolHisDtl.setjdpin("");
//				logTradeBpoolHisDtl.setbvalue(bvalue);
//				logTradeBpoolHisDtl.setold_bpool(bPool);
//				logTradeBpoolHisDtl.setnew_bpool(bPool);
				logTradeBpoolHisDtl.setnote(note);
				
				dbTool.insertLogTradeBpoolHisDtl(logTradeBpoolHisDtl);

				//insert log_trade_bpool_his
			
				LogTradeBpoolHis logTradeBpoolHis = new LogTradeBpoolHis();
				String trade_Id  = Common.getUUID();
				logTradeBpoolHis.settrade_id(trade_Id);
				logTradeBpoolHis.setTRADE_TYPE_CODE(trade_type_code);
				logTradeBpoolHis.setbpool_id(bpoolid);
				logTradeBpoolHis.setPARTITION_ID(partition_id);
				logTradeBpoolHis.setORDER_NO("");
				logTradeBpoolHis.setPROCESS_TIME(sdf.format(new Date()));
				logTradeBpoolHis.setPROCESS_TAG(process_tag);
				logTradeBpoolHis.setRESERVE_C1(reserve_c1);
				logTradeBpoolHis.setRESERVE_C2(reserve_c2);
				logTradeBpoolHis.setRESERVE_C3(reserve_c3);
				logTradeBpoolHis.setRESERVE_C4(reserve_c4);
				
				dbTool.insertLogTradeBpoolHis(logTradeBpoolHis);
				ret.put("Status", InfoBpoolErrorCode.STATUS_ON);
			}

			//修改操作
			else if("2".equals(opertype)){
				
				if(!inputJson.containsKey("ActivityId")){
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "修改时活动Id不能为空");
					log.error("修改时活动Id不能为空");
					return ret;
				}
				
				String bpoolid =   inputJson.getString("ActivityId");// 活动ID
				
				InfoBpool infoBpool = dbTool.queryInfoBpoolbyId(bpoolid);
				if(infoBpool == null ){
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "活动Id"+infoBpool+"不存在");
					log.error("活动Id"+infoBpool+"不存在");
					return ret;
				}
				
				long oldusedbalance = infoBpool.getused_balance();
				long oldbalance = infoBpool.getbalance();
				 
				String bpoolname =  getFromInputJson(inputJson,"ActivityName");
				if(bpoolname!=null){
					infoBpool.setbpool_name(bpoolname);
				}
				String departMenttype =  getFromInputJson(inputJson,"DepartMentType"); // 部门活动类型	
				if (departMenttype !=null && !"1".equals(departMenttype) && !"2".equals(departMenttype) ){
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "部门类型格式错误");
					log.error("修改操作修改活动类型时必须为1或2");
					return ret;
				} 
				
				if(departMenttype!=null){
					infoBpool.setdepartMent_type(departMenttype);
				}
				
				String activitytype =  getFromInputJson(inputJson,"ActivityType");  //活动分类
				if (activitytype !=null && !"1".equals(activitytype) && !"2".equals(activitytype)){			
						ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
						ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
						ret.put("ErrorMessage", "活动类型错误");
						log.error("修改操作修改活动分类时必须为1、2、3");
						return ret;
				} 
				
				if(activitytype!=null){
					infoBpool.setactivity_type(activitytype);
				}
				
				String effdate =  getFromInputJson(inputJson,"StartTime");   // 活动生效时间 
//				if (effdate == null || effdate.isEmpty()) {
//					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
//					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
//					ret.put("ErrorMessage", "新增操作时，活动生效时间必填");
//					log.error("修改操作时，活动生效时间必填");
//					return ret;
//				}
				
				

				try {
					if(effdate !=null ){
						effdate = effdate.substring(0,10)+" 00-00-00";
						effdate = DateUtil.changeToInternalFormat(effdate);
					}
					
				} catch (ParseException e) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "日期格式不正确");
					log.error("日期格式不正确");
					return ret;
				}
				
				if(effdate!=null){
					infoBpool.seteff_date(effdate);
				}
				
				String expdate =  getFromInputJson(inputJson,"EndTime");   // 活动失效时间 
//				if (expdate == null || expdate.isEmpty()) {
//					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
//					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
//					ret.put("ErrorMessage", "修改操作时，活动失效时间必填");
//					log.error("活动失效时间必填");
//					return ret;
//				}
				try {
					if(expdate !=null ){
						expdate = expdate.substring(0,10)+" 23-59-59";
						expdate = DateUtil.changeToInternalFormat(expdate);
					}
					
				} catch (ParseException e) {
					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
					ret.put("ErrorMessage", "日期格式不正确");
					log.error("日期格式不正确");
					return ret;
				}
				
				String costvalue =  getFromInputJson(inputJson,"CostValue"); //相应成本（元）
				
				if(costvalue!=null){
					infoBpool.setcost_value(Float.parseFloat(costvalue));
				}
				
				String threshold = getFromInputJson(inputJson,"Threshold");//阀值
				if(threshold!=null){
					infoBpool.setthreshold(Long.parseLong(threshold));
				}
				
				if(expdate !=null){
					infoBpool.setexp_date(expdate);
				}
				
				
				
				String contactphone = getFromInputJson(inputJson,"ContactPhone"); //紧急电话 
				String contactemail = getFromInputJson(inputJson,"ContactEmail"); //紧急邮箱
				String activitystatus = getFromInputJson(inputJson,"Status");  // 活动状态	
				
				if(contactphone!=null){
					infoBpool.setcontact_phone(contactphone);
				}
				if(contactemail!=null){
					infoBpool.setcontact_email(contactemail);
				}
				if(activitystatus!=null){
					infoBpool.setactivity_status(Long.parseLong(activitystatus));
				}
				
				
				String bPool =  getFromInputJson(inputJson,"BPool");  //B池
				
				
				if(bPool !=null){
					chkAndSendMsg(infoBpool,bPool);
					//infoBpool.setbpool(Long.parseLong(bPool));
				}
				
				infoBpool.setused_balance(0L); //B池修改不会修改useBalance
				
				
//				if(threshold>=1 && threshold<=99)
//					log.debug("\t阀值[" + threshold + "]");
//				else{
//					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
//					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
//					ret.put("ErrorMessage", "入参格式错误");
//					log.error("修改操作阀值为1-99");
//					return ret;
//				}

				
				
				
				
//				if (activitystatus==1)// 开
//					log.debug("\t开[" + activitystatus + "]");
//				else if(activitystatus==0)// 关
//					log.debug("\t关[" + activitystatus + "]");
//				else{
//				
//					ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
//					ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
//					ret.put("ErrorMessage", "入参格式错误");
//					log.error("修改操作时活动状态时必须为1或2");
//					return ret;
//				} 
			 
				String note =   getFromInputJson(inputJson,"Note"); //备注
				if(note!=null){
					infoBpool.setnote(note);
				}
				
				//insert log_trade_bpool_his_dtl
				
//				String activityid = (String) inputJson.get("activity_id");//活动平台侧的活动ID
//				log.debug("\t活动平台侧的活动ID[" + activityid + "]");
//				String jdpin = (String) inputJson.get("jdpin");//接收B值的jdpin
//				log.debug("\t接收B值的jdpin[" + jdpin + "]");
//				Long bvalue = (Long) inputJson.getLong("bvalue");//发放B值
//				log.debug("\t发放B值[" + bvalue + "]");
//				Long oldbpool = (Long) inputJson.getLong("old_bpool");//发放前B池余额
//				log.debug("\t发放前B池余额[" + oldbpool + "]");
//				Long newbpool = (Long) inputJson.getLong("new_bpool");//发放后B池余额
//				log.debug("\t发放后B池余额[" + newbpool + "]");
				
				//insert log_trade_bpool_his
				
				String trade_type_code = "602";//订单类型 
				Long partition_id = (long) (cal.get(Calendar.MONTH) + 1);//按月分区 
				String order_no =  ""; 
				String process_time = sdf.format(new Date()); 
				Long process_tag = 2L;//是否处理
				
				String reserve_c1 = ""; 
				String reserve_c2 = "";
				String reserve_c3 = "";
				String reserve_c4 ="";
		
				//update info_bpool
				
				dbTool.updateInfoBpool(infoBpool);
		
				//insert log_trade_bpool_his_dtl
				
				LogTradeBpoolHisDtl logTradeBpoolHisDtl = new LogTradeBpoolHisDtl();
				String tradeId  = Common.getUUID();
				logTradeBpoolHisDtl.settrade_id(tradeId);
				logTradeBpoolHisDtl.setbpool_id(bpoolid);
				logTradeBpoolHisDtl.setbpool_name(bpoolname);
				logTradeBpoolHisDtl.setdepartMent_type(departMenttype);
				logTradeBpoolHisDtl.setactivity_type(activitytype);
				logTradeBpoolHisDtl.seteff_date(effdate);
				logTradeBpoolHisDtl.setexp_date(expdate);
				logTradeBpoolHisDtl.setpwd(infoBpool.getpwd());
				logTradeBpoolHisDtl.setbpool(infoBpool.getbpool());
				logTradeBpoolHisDtl.setcost_value(infoBpool.getcost_value());
				logTradeBpoolHisDtl.setused_balance(infoBpool.getused_balance()+oldusedbalance);
				logTradeBpoolHisDtl.setbalance(infoBpool.getbalance()+oldbalance);
				logTradeBpoolHisDtl.setthreshold(infoBpool.getthreshold());
				logTradeBpoolHisDtl.setthre_status(infoBpool.getthre_status());
				logTradeBpoolHisDtl.setactivity_status(infoBpool.getactivity_status());
				logTradeBpoolHisDtl.setcreate_time(mysqlSdf.format(new Date()));
//				logTradeBpoolHisDtl.setcreate_staff(createstaff);
				logTradeBpoolHisDtl.setcontact_phone(infoBpool.getcontact_phone());
				logTradeBpoolHisDtl.setcontact_email(infoBpool.getcontact_email());
//				logTradeBpoolHisDtl.setactivity_id(activityid);
//				logTradeBpoolHisDtl.setjdpin(jdpin);
//				logTradeBpoolHisDtl.setbvalue(bvalue);
//				logTradeBpoolHisDtl.setold_bpool(oldbpool);
//				logTradeBpoolHisDtl.setnew_bpool(newbpool);
				logTradeBpoolHisDtl.setnote(note);
				
				dbTool.insertLogTradeBpoolHisDtl(logTradeBpoolHisDtl);
		
				// insert log_trade_bpool_his
				 
				LogTradeBpoolHis logTradeBpoolHis = new LogTradeBpoolHis();
				String trade_Id  = Common.getUUID();
				logTradeBpoolHis.settrade_id(trade_Id);
				logTradeBpoolHis.setTRADE_TYPE_CODE(trade_type_code);
				logTradeBpoolHis.setbpool_id(bpoolid);
				logTradeBpoolHis.setPARTITION_ID(partition_id);
				logTradeBpoolHis.setORDER_NO(order_no);
				logTradeBpoolHis.setPROCESS_TIME(process_time);
				logTradeBpoolHis.setPROCESS_TAG(process_tag);
				logTradeBpoolHis.setRESERVE_C1(reserve_c1);
				logTradeBpoolHis.setRESERVE_C2(reserve_c2);
				logTradeBpoolHis.setRESERVE_C3(reserve_c3);
				logTradeBpoolHis.setRESERVE_C4(reserve_c4);
				
				dbTool.insertLogTradeBpoolHis(logTradeBpoolHis);
				ret.put("Status", InfoBpoolErrorCode.STATUS_ON);
			}
			else {
				ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
				ret.put("ErrorCode", InfoBpoolErrorCode.ERR_TRADE_TYPE_CODE);
				ret.put("ErrorMessage", "操作类型编码错误");
			}

			log.info("RESULT:" + ret.toString());
		} catch (Exception e) {							//异常处理
			ret.put("Status", InfoBpoolErrorCode.STATUS_OFF);
			ret.put("ErrorCode", InfoBpoolErrorCode.ERR_INPUT_FORMAT);
			ret.put("ErrorMessage", "接口异常");
			log.error(e.getMessage());
			e.printStackTrace();
			return ret;
		}
		return ret;
	}

	private void chkAndSendMsg(InfoBpool infoBpool, String bPool) throws Exception {
		//如果是B池调增，重新计算是否达到阀值
		//如果是B池调减，判断余额是否足够调减，并且重新计算告警阀值
		Long oldBpool = infoBpool.getbpool();
		Long oldBalance = infoBpool.getbalance();
		Long newBpool = Long.parseLong(bPool);
		
		if(newBpool >oldBpool){
			infoBpool.setbpool(newBpool);
			infoBpool.setbalance((newBpool-oldBpool));
			sendChkMsg(infoBpool);
		}else if (newBpool <oldBpool){
			Long deductValue = oldBpool - newBpool;
			if(oldBalance<deductValue){
				log.error("余额不足，不能做调减");
				throw new Exception("余额不足，不能做调减");
			}
			
			infoBpool.setbpool(newBpool);
			infoBpool.setbalance(newBpool-oldBpool);
			
			sendChkMsg(infoBpool);
		}else{
			//如果B池没有变化
			infoBpool.setbpool(newBpool);
			infoBpool.setbalance(0L);
		}
		
	}

	private void sendChkMsg(InfoBpool infoBpool) {

		Long newpool = infoBpool.getbpool();
		Long usedBalance = infoBpool.getused_balance();
		Long threshold = infoBpool.getthreshold();
		
		if((1.0*(newpool-usedBalance)/newpool) < 1.0*threshold/100     ) {
			
			//【京东通信】{0}总计{1}B，已使用{2}%，活动结束时间{3}，请及时关注/追加通信B。
			String sendMsg ="|aoc.dic.bactivityalarm|"+infoBpool.getbpool_name()+"|"+infoBpool.getbpool()+"|"+(infoBpool.getused_balance()*100/infoBpool.getbpool())+"|"+infoBpool.getexp_date().substring(0,8);
			
			String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			
			HlpSmsSend sendHis=new HlpSmsSend();
			sendHis.setMsisdn_send("10023");
			sendHis.setMsisdn_receive(infoBpool.getcontact_phone());
			sendHis.setMessage_text(sendMsg);
//			sendHis.setPara_key(paraKey);
//			sendHis.setSend_time(currentTime);
			sendHis.setCreate_time(currentTime);
			
			DbTool dbTool = new DbTool();
			dbTool.sendMsg(sendHis);
			
			infoBpool.setthre_status(2); //已告警
			
		} else{
			long oldstatus = infoBpool.getthre_status();
			long newstatus =1;
			if(oldstatus ==1){
				newstatus =1;
			}else if (oldstatus==2){
				newstatus =3;
			}else if (oldstatus==3){
				newstatus =3;
			}
			
			infoBpool.setthre_status(newstatus);
			
		}
	}

	private String getFromInputJson(JSONObject inputJson, String keyStr) {
		
		if(inputJson.containsKey(keyStr)){
			return inputJson.getString(keyStr);
		}
		
		return null;
	}
}


//String currDate = DateUtil.getSystemTime();
