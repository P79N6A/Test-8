package com.tydic.beijing.bvalue.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchangeDao;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountAttrHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeRelChg;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.dto.AttrDto;
import com.tydic.beijing.bvalue.service.JDPinRelationBiz;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

/**
 * @author sung
 *
 */
public class JDPinRelationBizImpl implements JDPinRelationBiz{

	private static Logger log =Logger.getLogger(JDPinRelationBizImpl.class);
	
	private DbTool db;
//	private static InfoPayBalanceSync sync = new InfoPayBalanceSync();
	
	private String expDate;  //加关联时的失效日期    2050/12/31  23:59:59
	
	private static final String ADD_BALANCE="0";
	private static final String DEL_BALANCE="1";
	
	
	public String getExpDate() {
		return expDate;
	}


	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}


	@Override
	public JSONObject deal(JSONObject req) {
		
		log.debug("请求参数>>>>>>>>>>>>:"+req.toString());
		
		List<AttrDto>  attrDtos = new ArrayList<AttrDto>();
		String jdpin="";
		String mobileNumber="";
		String mode="";
		boolean flag_legou=false;
		boolean flag_noheyue=true;
		boolean flag_frist=false;
		boolean release=false;
		long giveBalance=0;
		try{
			jdpin=req.getString("JDPin");
			mobileNumber=req.getString("MobileNumber");
			mode=req.getString("Mode");
			if( !mode.equals(Constants.JDPinRelation_Mode_Add) && !mode.equals(Constants.JDPinRelation_Mode_Release)){
				log.debug(">>>>>>>>请求参数错误[mode]");
				JSONObject error =new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误");
				log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>>"+error.toString());
				return error;
			}
			String dtos="";
			if(mode.equals(Constants.JDPinRelation_Mode_Add)){
				dtos=req.getString("AttrDtoList");
				
//				ObjectMapper mapper=new ObjectMapper();
//				List<LinkedHashMap<String,String>> dtoList=mapper.readValue(dtos, List.class);
				
				JSONArray attrdtoArray = req.getJSONArray("AttrDtoList");
				//attrdtoArray.size()
				
				System.out.println("size>>>>>>>>>>>>:"+attrdtoArray.size());
				for(int i=0 ; i< attrdtoArray.size() ; ++i){
					//Map<String,String> map= dtoList.get(i);
					JSONObject map = attrdtoArray.getJSONObject(i);
					String attrcode=map.getString("AttrCode");
					String attrvalue=map.getString("AttrValue");
					String effdate=map.getString("EffDate");
					String expdate=map.getString("ExpDate");
					if(attrcode.isEmpty() || attrvalue.isEmpty() || effdate.isEmpty() || expdate.isEmpty()){
						log.debug(">>>>>>>>请求参数错误[attrCode]");
						JSONObject error =new JSONObject();
						error.put("Status", Constants.Result_Error);
						error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
						error.put("ErrorMessage", "请求参数错误");
						log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>"+error.toString());
						
						return error;
					}
					AttrDto d=new AttrDto(attrcode,attrvalue,effdate,expdate);
					attrDtos.add(d);
					
				}
				if(attrDtos.isEmpty()){
					log.debug(">>>>>>>>请求参数错误[attrdtoList]");
					JSONObject error =new JSONObject();
					error.put("Status", Constants.Result_Error);
					error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
					error.put("ErrorMessage", "请求参数错误");
					log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>>>>>"+error.toString());
					return error;
				}
				
			}
			
			
			if(jdpin.isEmpty() || mobileNumber.isEmpty() || mode.isEmpty() || !Common.isMobileNumber(mobileNumber) ){
				log.debug(">>>>>>>>请求参数错误[请检查必填项]");
				JSONObject error =new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误");
				return error;
			}
		}catch(Exception e){
			log.error(e.getMessage());
			JSONObject error =new JSONObject();
			error.put("Status", Constants.Result_Error);
			error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
			error.put("ErrorMessage", "请求参数错误");
			return error;
			
		}
		

		
		String userId=Common.md5(jdpin);
//		synchronized (this) {
			
		
		InfoUserExternalAccount infoUser=hasRelationNumber(userId);	//查有效的第一条绑定关系 
		
		if(infoUser != null){
			String num=infoUser.getExternal_account_code();
			if(mode.equals(Constants.JDPinRelation_Mode_Add)){
				JSONObject error =new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.JDPinRelation_Exists);
				error.put("ErrorMessage", "已存在关联号码:"+num);
				log.error("ERR"+jdpin+"|"+userId+">>>>>>>>>>>>>>>>"+error.toString());
				return error;
				
				
				
			}else if(mode.equals(Constants.JDPinRelation_Mode_Release)){
				if(!num.equals(mobileNumber)){
					JSONObject jerror =new JSONObject();
					jerror.put("Status", Constants.Result_Error);
					jerror.put("ErrorCode", BValueErrorCode.JDPinRelation_Not_Exists);
					jerror.put("ErrorMessage", "不存在关联号码:"+mobileNumber);
					log.debug("ERR>>>>>>>>>>>>>>>>>>"+jerror.toString());
					return jerror;
				}
				 release=true;
			}
			
		}else{ //无关联号码
			
			
			if(mode.equals(Constants.JDPinRelation_Mode_Release)){
				JSONObject jerror =new JSONObject();
				jerror.put("Status", Constants.Result_Error);
				jerror.put("ErrorMessage", "不存在关联号码:"+mobileNumber);
				jerror.put("ErrorCode",BValueErrorCode.JDPinRelation_Not_Exists);
				log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>>"+jerror.toString());
				return jerror;
			}
		
		}
		
		
		String tradeId=Common.getUUID();
		long partitionId=Calendar.getInstance().get(Calendar.MONTH)+1;
		
		String remark="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String currDate=sdf.format(new Date());
		String processTime=currDate;
		//判断用户是否为乐购卡 非合约用户 首次关联
		//首次关联
//		boolean flag_mainproduct=false;//是否有主产品
		if(mode.equals(Constants.JDPinRelation_Mode_Add)){
		for(AttrDto attrDato:attrDtos){
			//此处对时间进行格式统一
			String tmpEffDate = attrDato.getEffDate().replace("-", "").replace(":", "").replace(" ", "");
			String tmpExpDate = attrDato.getExpDate().replace("-", "").replace(":", "").replace(" ", "");

			if(attrDato.getAttrCode().equals("1001")&&attrDato.getAttrValue()!=null
					&&tmpExpDate.compareTo(currDate)>0
					&&tmpEffDate.compareTo(tmpExpDate)<0){
					flag_noheyue=false;
			}
			if(attrDato.getAttrCode().equals("1002")&&attrDato.getAttrValue().equals("02")
					&&tmpEffDate.compareTo(currDate)<0
					&&tmpExpDate.compareTo(currDate)>0
					&&tmpEffDate.compareTo(tmpExpDate)<0){
					flag_legou=true;
			}
			if(attrDato.getAttrCode().equals("1004")&&attrDato.getAttrValue().equals("1")){
					flag_frist=true;
			}
		}
		
		log.debug("是否为乐购卡=========="+flag_legou);
		log.debug("是否为非合约用户==========="+flag_noheyue);
		log.debug("是否为首次关联==========="+flag_frist);
		if(flag_legou&&flag_noheyue&&flag_frist){
			String domainCode="2000";
			String tradeTypeCode="104";
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("domain_code", domainCode);
			filter.put("trade_type_code", tradeTypeCode);
			List<RuleParameters> ruleParameters=S.get(RuleParameters.class).query(Condition.build("byattrValue").filter(filter));
			if(ruleParameters==null ){
			//zhanghbadd20151202,不能在这里抛异常，因为有可能是自由行用户关联走到这里了，自由行是不关心这个配置的，要么这里加条件，要么抛异常往后放
				JSONObject jerror=new JSONObject();
				jerror.put("Status", Constants.STATUS_FAILURE);
				jerror.put("ErrorCode", Constants.NOT_FIND_PRODUCT);
				jerror.put("ErrorMessage", "系统配置异常");
				log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>>"+jerror.toString());
				return jerror;
			}
			String attrValue_1003=null;
			AttrDto ad=new AttrDto();
			for(AttrDto a:attrDtos){
				if(a.getAttrCode().equals("1003")){
					ad.setAttrCode(a.getAttrCode());
					ad.setAttrValue(a.getAttrValue());
					ad.setEffDate(a.getEffDate());
					ad.setExpDate(a.getExpDate());
				}
			}
			log.debug("ad========"+ad.toString());
			attrValue_1003 = ad.getAttrValue();
			log.debug("主产品=========="+attrValue_1003);
			if(attrValue_1003==null){
				giveBalance=0;
			}else{
				for(RuleParameters rule:ruleParameters){
					if(rule.getPara_name().equals(attrValue_1003)){
						if(rule.getPara_char3()==null ||rule.getPara_char3().length()==0 ){
							giveBalance=0;
						}else{
							giveBalance=Long.parseLong(rule.getPara_char3());
						}
						break;
					}
				}
			}
					log.debug("主套餐对应的赠送的B值为："+giveBalance);
			}
		}
		LogTradeRelChg logTrade=new LogTradeRelChg();
		logTrade.setTrade_id(tradeId);
		logTrade.setTrade_type_code("502");
		logTrade.setExternal_system_code(Constants.JDPinRelation_ExternalSysCode);
		logTrade.setChannel_type(Constants.JDPinRelation_ChannelType);
		logTrade.setUser_id(userId);
		logTrade.setPartition_id(partitionId);
		logTrade.setProcess_tag(2);
		logTrade.setProcess_time(processTime);
		logTrade.setTrade_time(currDate);
		logTrade.setRemark(remark);
		

		
		// 写资料
		List<InfoPayBalance> infoPayBalance=db.getEffectiveInfoPayBalanceByUserId(userId,currDate);
		if(mode.equals(Constants.JDPinRelation_Mode_Add)){
			try{
			
			addRelationInfo(logTrade,jdpin,mobileNumber,attrDtos,mode,userId,flag_frist,flag_noheyue,flag_legou,giveBalance);
			
			}catch(Exception e){
				log.error(e.getMessage());
				JSONObject error =new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
				error.put("ErrorMessage", "用户关联失败");
				return error;
			}
		}else{
			List<InfoUserExternalAccountAttr>  attrs=db.getExternalAccountAttrbyUserIdAndExternal(infoUser.getUser_id(), infoUser.getExternal_account_id());
			if(attrs== null || attrs.isEmpty()){
				JSONObject error=new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorMessage", "此账号关联属性异常,绑定关系:"+infoUser.getExternal_account_id());
				error.put("ErrorCode",Constants.DataError);
				return error;
			}
			try{
			updateRelationInfo(logTrade,infoUser ,attrs,userId,infoPayBalance,release);
			}catch(Exception e){
			log.error(e.getMessage());
			JSONObject error =new JSONObject();
			error.put("Status", Constants.Result_Error);
			error.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
			error.put("ErrorMessage", "用户解关联失败");
			return error;
		}
	}
//		try {
//			
//			dealRelation(logTrade ,mode, jdpin,attrDtos,mobileNumber);
//			
//			
			
			// 写台账状态
//			db.updateProcessTag(2,logTrade.getTrade_id() ,"",logTrade.getUser_id());
//		db.addLogTradeRelChg(logTrade);
			
//		}
		
		// 同步redis
//		if(this.syncRedis!=null && this.syncRedis.equals("Y")){
//			for (InfoPayBalance pay : delInfoPayBalance) {
//				
//				log.debug("同步redis, " + pay.toString());
//				int retCode = paySync.sync(pay);
//				if(retCode !=0){
//					//0成功  -1失败
//					log.debug("同步Redis失败！");
//				}
//			}
//		}
		

		
		
		JSONObject jresult=new JSONObject();
		jresult.put("Status", Constants.Result_Succ);
		jresult.put("ErrorCode",""  );
		jresult.put("ErrorMessage", "");
		log.debug("RETURN>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+jresult.toString());
		
		return jresult;
		
		
	}

	
	public  InfoUserExternalAccount hasRelationNumber(String userId){
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		InfoUserExternalAccount rel=db.isRelationExists(userId,currentTime);

		return rel;
	}

	
	public  void dealLogTradeHis(LogTradeRelChg log) throws BValueException{
		LogTradeRelChg logTrade=db.queryProcess(log.getUser_id());
		if(logTrade==null){
			db.addLogTradeRelChg(log);
		}else{
			throw new BValueException(BValueErrorCode.ERR_JDRELCHG_DEALING,"业务处理中");
		}
//		db.addLogTradeRelChg(log);
		
	}
	
	
	//加关联
	private void  addRelationInfo(LogTradeRelChg logTrade,String jdpin ,String mobileNumber,List<AttrDto> attrDtos ,String mode,String userId,
			boolean flag_frist,boolean flag_noheyue,boolean flag_legou,long giveBalance) throws Exception{
	
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime=sdf2.format(Calendar.getInstance().getTime());
		InfoUserExternalAccount external=new InfoUserExternalAccount();
		String external_account_id=Common.getUUID();
		external.setExternal_account_id(external_account_id);
		external.setUser_id(logTrade.getUser_id());
		external.setJd_pin(jdpin);
		external.setExternal_system_code(Constants.JDPinRelation_ExternalSysCode);
		external.setExternal_account_code(mobileNumber);
		external.setExp_date(expDate);
//		db.addExternalInfoUser(external);  //写关联表
		List<InfoUserExternalAccountAttr> attrs=new ArrayList<InfoUserExternalAccountAttr>();
		for(AttrDto dto :attrDtos){
			InfoUserExternalAccountAttr attr=new InfoUserExternalAccountAttr(external);
			attr.setAttr_code(dto.getAttrCode());
			attr.setAttr_value(dto.getAttrValue());
			attr.setEff_date(dto.getEffDate().replace("-", "").replace(" ", "").replace(":", ""));
			attr.setExp_date(dto.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));
			attrs.add(attr);
		}
		
//		db.addExternalUserAttr(attrs);	//写关联属性表
		LogTradeExternalAccountHis his=new LogTradeExternalAccountHis(logTrade,external);
		
		his.setOperation_type(mode);
//		db.addExternalUserHis(his);  		//写关联历史表
		
		List<LogTradeExternalAccountAttrHis> attrHis=new ArrayList<LogTradeExternalAccountAttrHis>();
		
		for(InfoUserExternalAccountAttr  iter: attrs){
			LogTradeExternalAccountAttrHis attr=new LogTradeExternalAccountAttrHis(iter,logTrade);
			attr.setOperation_type(mode);
			attrHis.add(attr);
			
		}
//		db.addExternalUserAttrHis(attrHis);		//写关联属性历史表
		InfoUser  user=db.queryInfoUserByUserId(userId);
		
		//写自动兑换设置
		if(flag_legou){
			//增加乐购卡的特有属性
			LifeUserAutoExchange exchange=new LifeUserAutoExchange();
			String exchangeId=Common.getUUID();
			exchange.setExchange_id(exchangeId);
			exchange.setUser_id(logTrade.getUser_id());
			exchange.setCycle_type("COM");	//按月兑换
			exchange.setPurchase_mode("001");	//全部兑换
			exchange.setTop_b_value(500);
			exchange.setExchange_mode("2");	//按百分比
			String resourceListId=Common.getUUID();
			exchange.setResource_list_id(resourceListId);
			exchange.setEff_date(currentTime);
			exchange.setExp_date(expDate);
			List<LifeResourceList> resourceList=new ArrayList<LifeResourceList>();
			LifeResourceList moneyRes=new LifeResourceList();
			moneyRes.setResource_list_id(resourceListId);
			moneyRes.setUser_id(logTrade.getUser_id());
			moneyRes.setOrder_number(0);
			moneyRes.setResource_type_code("ROM");	//金钱
			moneyRes.setResource_value("100");
			resourceList.add(moneyRes);
			if(flag_frist&&flag_noheyue){
						if(user==null){
						//zhanghbadd20151202已经判断newuser了，为什么还要再加一个if判断null？
							//判断是否送b
						InfoUser infouser=new InfoUser();
						List<InfoPayBalance> balance=new ArrayList<InfoPayBalance>();
						LogTradeHis tradeHis=new LogTradeHis();
						LogTradeCreateUserHis createUserHis=new LogTradeCreateUserHis();
						BalanceAccessLog insertBalanceAccessLog=new BalanceAccessLog();
						creatInfoPayBalance( logTrade,jdpin,flag_frist,giveBalance,currentTime,mobileNumber,
								infouser, balance,tradeHis,createUserHis,insertBalanceAccessLog);
						db.insertNewInfoUser(external, attrs, his, attrHis,exchange,resourceList,infouser, balance,tradeHis,createUserHis,
								insertBalanceAccessLog);
						//redies更新
//						for(InfoPayBalance bal:balance){
//							int rel=sync.sync(bal);
//							if(rel==0){
//								log.debug("redies更新成功");
//							}else{
//								throw new Exception("redies更新失败！");
//							}
//						}
					}else{
						//首次关联有用户信息
						LogTradeHis tradeHis=new LogTradeHis();
						tradeHis.setTrade_id(logTrade.getTrade_id());
						tradeHis.setTrade_type_code("502");//zhanghbadd20151202 501是开户，这里已经有用户了，你要写的是关联订单类型502吧？
						tradeHis.setExternal_system_code("10000");
						tradeHis.setUser_id(userId);
						tradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
						tradeHis.setChannel_type("121");
						tradeHis.setProcess_tag(0);
						tradeHis.setTrade_time(currentTime);
						tradeHis.setBalance(giveBalance);
						tradeHis.setRemark("京东账号首次关联号码"+mobileNumber+"赠送"+giveBalance+"个通信B");
						int type=3;
						int changeType=1;
						InfoPayBalanceManager infoPayBalanceManage=new InfoPayBalanceManager();
						List<InfoPayBalance> updateInfoPayBalance=new ArrayList<InfoPayBalance>();
						List<InfoPayBalance> insertInfoPayBalance=new ArrayList<InfoPayBalance>();
						List<BalanceAccessLog> insertBalanceAccessLog=new ArrayList<BalanceAccessLog>();
						log.debug("giveBalance======="+giveBalance);
						int code=infoPayBalanceManage.manage(userId, type, changeType, giveBalance, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
						log.debug("updateInfoPayBalance========="+updateInfoPayBalance.toString());
						if(code==1){
							log.debug("========B值扣减成功========");
						}else{	
							throw new Exception("扣减用户账本失败！");
						}
						for(BalanceAccessLog iba:insertBalanceAccessLog){
							iba.setTrade_Id(logTrade.getTrade_id());
							iba.setTrade_Type_Code(tradeHis.getTrade_type_code());
						}
						log.debug("updateInfoPayBalance========"+updateInfoPayBalance.toString());
						log.debug("insertInfoPayBalance=========="+insertInfoPayBalance.toString());
						log.debug("insertBalanceAccessLog=========="+insertBalanceAccessLog.toString());
						db.insertInfoUser(external, attrs, his, attrHis,exchange,resourceList,tradeHis,updateInfoPayBalance,insertInfoPayBalance,insertBalanceAccessLog);
//						for(InfoPayBalance up:updateInfoPayBalance){
//							int rel=sync.sync(up);
//							if(rel==0){
//								log.debug("redies更新成功");
//							}else{
//								throw new Exception("redies更新失败！");
//							}
//						}
//						for(InfoPayBalance in:insertInfoPayBalance){
//							int rel=sync.sync(in);
//							if(rel==0){
//								log.debug("redies更新成功");
//							}else{
//								throw new Exception("redies更新失败！");
//							}
//						}
					}
			}else{	
						//非首次关联没有用户信息创建0账本
							if(user==null){
								//判断是否送b
								//zhanghbadd20151202上面也有开户的代码，这里又重复出现，为什么不写成一个方法，分别在两个地方调用这个方法呢？这样分开写后期维护会很崩溃
							InfoUser infouser=new InfoUser();
							List<InfoPayBalance> balance=new ArrayList<InfoPayBalance>();
							LogTradeHis tradeHis=new LogTradeHis();
							LogTradeCreateUserHis createUserHis=new LogTradeCreateUserHis();
							BalanceAccessLog insertBalanceAccessLog=new BalanceAccessLog();
							creatInfoPayBalance( logTrade,jdpin,flag_frist,giveBalance,currentTime,mobileNumber,
									infouser, balance,tradeHis,createUserHis,insertBalanceAccessLog);
							db.insertNoFristCreateInfoUser(external, attrs, his, attrHis,exchange,resourceList,infouser, balance,tradeHis,createUserHis);
//							for(InfoPayBalance in:balance){
//								int rel=sync.sync(in);
//								if(rel==0){
//									log.debug("redies更新成功");
//								}else{
//									throw new Exception("redies更新失败！");
//								}
//							}
						
							}else{
							//用户首次关联有用户信息
							LogTradeHis tradeHis=new LogTradeHis();
							tradeHis.setTrade_id(logTrade.getTrade_id());
							tradeHis.setTrade_type_code("502");
							tradeHis.setExternal_system_code("10000");
							tradeHis.setUser_id(userId);
							tradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
							tradeHis.setChannel_type("121");
							tradeHis.setProcess_tag(0);
							tradeHis.setTrade_time(currentTime);
							db.addJdpinRelation(external, attrs, his, attrHis,exchange,resourceList,tradeHis);
						}
					}
			}else{
			//非乐购用户
				
				if(user==null){
					InfoUser infouser=new InfoUser();
					List<InfoPayBalance> balance=new ArrayList<InfoPayBalance>();
					LogTradeHis tradeHis=new LogTradeHis();
					LogTradeCreateUserHis createUserHis=new LogTradeCreateUserHis();
					BalanceAccessLog insertBalanceAccessLog=new BalanceAccessLog();
					creatInfoPayBalance( logTrade,jdpin,flag_frist,giveBalance,currentTime,mobileNumber,
							infouser, balance,tradeHis,createUserHis,insertBalanceAccessLog);
					 
					db.insertuserbyRelation(infouser,tradeHis,createUserHis);
					
				}
				
			LogTradeHis tradeHis=new LogTradeHis();
			tradeHis.setTrade_id(logTrade.getTrade_id());
			tradeHis.setTrade_type_code("502");
			tradeHis.setExternal_system_code("10000");
			tradeHis.setUser_id(userId);
			tradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
			tradeHis.setChannel_type("121");
			tradeHis.setProcess_tag(0);
			tradeHis.setTrade_time(currentTime);
			LifeUserAutoExchange exchange=new LifeUserAutoExchange();
			String exchangeId=Common.getUUID();
			exchange.setExchange_id(exchangeId);
			exchange.setUser_id(logTrade.getUser_id());
			exchange.setCycle_type("COM");	//按月兑换
			exchange.setPurchase_mode("002");	//最高封顶
			exchange.setTop_b_value(500);
			exchange.setExchange_mode("2");	//按百分比
			String resourceListId=Common.getUUID();
			exchange.setResource_list_id(resourceListId);
			exchange.setEff_date(currentTime);
			exchange.setExp_date(expDate);
			List<LifeResourceList> resourceList=new ArrayList<LifeResourceList>();
			LifeResourceList resource=new LifeResourceList();
			resource.setResource_list_id(resourceListId);
			resource.setUser_id(logTrade.getUser_id());
			resource.setOrder_number(0);
			resource.setResource_type_code("ROV");	//语音
			resource.setResource_value("50");
			LifeResourceList  volumeRes=new LifeResourceList();

			volumeRes.setResource_list_id(resource.getResource_list_id());
			volumeRes.setUser_id(resource.getUser_id());
			volumeRes.setOrder_number(1);
			volumeRes.setResource_type_code("ROF");	//流量
			volumeRes.setResource_value("50");
			
			resourceList.add(resource);
			resourceList.add(volumeRes);
			db.addJdpinRelation(external, attrs, his, attrHis,exchange,resourceList,tradeHis);
		
		}
		
	}
	
		
	
	
//	public  synchronized void dealRelation(LogTradeRelChg logTrade ,String mode,String jdpin,List<AttrDto>attrDtos,String mobileNumber) throws BValueException{
//		
//		InfoUserExternalAccount  infoUser=	hasRelationNumber(logTrade.getUser_id());
//
//		if(mode.equals(Constants.JDPinRelation_Mode_Add)){
//			
//			if(infoUser != null){
//				throw new BValueException(BValueErrorCode.ERR_JDREL_EXISTS,"已存在关联号码:"+infoUser.getExternal_account_code());
//			}
//			
//			addRelationInfo(logTrade,jdpin,mobileNumber,attrDtos,mode);
//			
//		}else{
//			if(infoUser== null){
//				throw new BValueException(BValueErrorCode.ERR_JDREL_NOT_EXISTS, "不存在关联号码");
//			}else if( !infoUser.getExternal_account_code().equals(mobileNumber)){
//				throw new BValueException(BValueErrorCode.ERR_JDREL_NOT_EXISTS, "不存在关联号码:"+mobileNumber);
//			}
//			
//			updateRelationInfo(logTrade,infoUser);
//			
//		}
//		
//		
//	}
	
	
	//解关联
	//合约属性编码  1001
	//产品属性编码  1002  取值范围（01自由行  02乐购卡）
	private boolean islegouandheyue(List<InfoUserExternalAccountAttr> attrs,String currentTime) {
		
		boolean islegou =false;
//	boolean isheyue = false;
		
		for(InfoUserExternalAccountAttr tmpattr:attrs){
			
            if(tmpattr.getAttr_code().equals("1002")){
            	if(tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("02")
						&& tmpattr.getEff_date().compareTo(currentTime) <=0 && tmpattr.getExp_date().compareTo(currentTime) >=0){
            		islegou = true;
            	}
				
			}
		}
		
		
//		if(islegou && isheyue){
//			return true;
//		}
		
		return islegou;
	}


	public void updateRelationInfo(LogTradeRelChg logTrade,InfoUserExternalAccount infoUser ,List<InfoUserExternalAccountAttr>  attrs,String userId,List<InfoPayBalance> infoPayBalance,boolean release) throws Exception{
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		InfoUserExternalAccount external=new InfoUserExternalAccount();
		external.setUser_id(logTrade.getUser_id());
		external.setExternal_account_code(infoUser.getExternal_account_code());
		external.setExternal_account_id(infoUser.getExternal_account_id());
		external.setExp_date(currentTime);
//		db.updatePinRelation(external);		//更新关联表
		
		
		InfoUserExternalAccountAttr attr=new InfoUserExternalAccountAttr();
		attr.setUser_id(logTrade.getUser_id());
		attr.setExp_date(currentTime);
		attr.setExternal_account_id(infoUser.getExternal_account_id());
//		db.updateExternalAccountAttr(attr);		//更新关联属性表
		
		LogTradeExternalAccountHis accountHis=new LogTradeExternalAccountHis();
		accountHis.setTrade_id(logTrade.getTrade_id());
		accountHis.setTrade_type_code("502");
		accountHis.setUser_id(logTrade.getUser_id());
		accountHis.setPartition_id(logTrade.getPartition_id());
		accountHis.setExternal_account_id(infoUser.getExternal_account_id());
		accountHis.setExternal_system_code(infoUser.getExternal_system_code());
		accountHis.setOperation_type(Constants.JDPinRelation_Mode_Release);
		accountHis.setExternal_account_code(infoUser.getExternal_account_code());
		accountHis.setEff_date(infoUser.getEff_date().replace("-", "").replace(":", "").replace(" ", "").substring(0,14));
		accountHis.setExp_date(currentTime);
//		db.updateExternalUserHis(accountHis); 	//写关联历史表
		
		List<LogTradeExternalAccountAttrHis> attrHisList=new ArrayList<LogTradeExternalAccountAttrHis>();
		for(InfoUserExternalAccountAttr iter : attrs){
			LogTradeExternalAccountAttrHis  attrHis=new LogTradeExternalAccountAttrHis();
			attrHis.setTrade_id(logTrade.getTrade_id());
			attrHis.setTrade_type_code("502");
			attrHis.setUser_id(logTrade.getUser_id());
			attrHis.setPartition_id(logTrade.getPartition_id());
			attrHis.setExternal_account_id(infoUser.getExternal_account_id());
			attrHis.setExternal_system_code(infoUser.getExternal_system_code());
			attrHis.setOperation_type("2");
			attrHis.setAttr_code(iter.getAttr_code());
			attrHis.setAttr_value(iter.getAttr_value());
			log.debug(">>>>>effDate:"+iter.getEff_date());
			log.debug(">>>>>>ExpDate>>>"+iter.getExp_date());
			attrHis.setEff_date(iter.getEff_date());
			attrHis.setExp_date(currentTime);
			attrHisList.add(attrHis);
		}
//		db.updateExternalUserAttrHis(attrHis);
		
		//失效自动兑换关系
		List<InfoPayBalance> infopay=db.getInfoPayBalanceByUserId(userId);
		LifeUserAutoExchangeDao exchange=new LifeUserAutoExchangeDao();
		exchange.setUser_id(infoUser.getUser_id());
		exchange.setExp_date(currentTime);
	
		if(infopay==null&&infopay.isEmpty()){	
			log.debug("infoPayBalance是空的不需要进行账本扣减");
			LogTradeHis trade=new LogTradeHis();
			trade.setTrade_id(logTrade.getTrade_id());
			trade.setTrade_type_code("502");
			trade.setExternal_system_code("10000");
			trade.setUser_id(userId);
			trade.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
			trade.setChannel_type("121");
			trade.setProcess_tag(0);
			trade.setTrade_time(currentTime);
			trade.setBalance(0);
			trade.setRemark("账户解关联通信B清零");
				
			db.delJdpinRelation(external,attr,accountHis,attrHisList,exchange,trade);
			}else{
			List<BalanceAccessLog> insertBalanceAccess=new ArrayList<BalanceAccessLog>();
			List<InfoPayBalance> delInfoPayBalance =new ArrayList<InfoPayBalance>();
			long delBalance=0;
			long allDelBalance=0;
			long delOverBalance=0;
			for(InfoPayBalance info:infopay){
				if(info.getBalance()<=0){
					continue;
				}
			 delBalance=-info.getBalance();
			 if(info.getBalance_type_id()==0||info.getBalance_type_id()==3){
				 allDelBalance=allDelBalance+delBalance; 
			 }
			 if(info.getBalance_type_id()!=0&&info.getBalance_type_id()!=3){
				 delOverBalance=delOverBalance+delBalance; 
			 }
			 
			info.setBalance(delBalance);
			delInfoPayBalance.add(info);
				//更新异动表
			BalanceAccessLog insertBalanceAccessLog=new BalanceAccessLog();
			insertBalanceAccessLog.setTrade_Id(logTrade.getTrade_id());
			insertBalanceAccessLog.setTrade_Type_Code("502");
			insertBalanceAccessLog.setUser_Id(userId);
			insertBalanceAccessLog.setPartition_Id(Calendar.getInstance().get(Calendar.MONTH)+1);
			insertBalanceAccessLog.setBalance_Id(info.getBalance_id());
			insertBalanceAccessLog.setBalance_Type_Id(info.getBalance_type_id());
			insertBalanceAccessLog.setAccess_Tag(DEL_BALANCE);//取款还是存款
			insertBalanceAccessLog.setMoney(-info.getBalance());
			insertBalanceAccessLog.setNew_Balance(0);
			insertBalanceAccessLog.setOld_Balance(info.getBalance());
			insertBalanceAccessLog.setOperate_Time(currentTime);
			insertBalanceAccess.add(insertBalanceAccessLog);
			}
			log.debug("正常账本的失效值========="+allDelBalance);
			log.debug("溢出账本的失效值========="+delOverBalance);
			LogTradeHis trade=new LogTradeHis();
			trade.setTrade_id(logTrade.getTrade_id());
			trade.setTrade_type_code("502");
			trade.setExternal_system_code("10000");
			trade.setUser_id(userId);
			trade.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
			trade.setChannel_type("121");
			trade.setProcess_tag(0);
			trade.setTrade_time(currentTime);
			trade.setBalance(allDelBalance);
			trade.setOvertop_value(delOverBalance);
			trade.setRemark("账户解关联通信B清零");
			
//			db.delJdpinRelation(external,attr,accountHis,attrHisList,exchange,delInfoPayBalance,insertBalanceAccess,tradeHis);
			db.registUserDisengagement(external,attr,accountHis,attrHisList,exchange,delInfoPayBalance,insertBalanceAccess,trade);
//			for(InfoPayBalance in:delInfoPayBalance){
//				int rel=sync.sync(in);
//				if(rel==0){
//					log.debug("redies更新成功");
//				}else{
//					throw new Exception("redies更新失败！");
//				}
//			}
			}
		}	

	//用户创建方法
	public void creatInfoPayBalance(LogTradeRelChg logTrade,String jdpin,boolean flag_frist,long giveBalance,String currentTime,String mobileNumber
			,InfoUser infouser,List<InfoPayBalance> balance,LogTradeHis tradeHis,LogTradeCreateUserHis createUserHis,BalanceAccessLog insertBalanceAccessLog){
		String userId=logTrade.getUser_id();
		infouser.setUser_id(userId);
		infouser.setJd_pin(jdpin);
		infouser.setCreate_channel("124");
		infouser.setCreate_date(currentTime);
		InfoPayBalance payBalance_0=new InfoPayBalance();
		String balanceId_0=Common.getUUID();
		payBalance_0.setBalance_id(balanceId_0);
		payBalance_0.setUser_id(userId);
		payBalance_0.setBalance_type_id(0);
		payBalance_0.setBalance(0);
		Calendar now1=Calendar.getInstance();
		String eff_date="";
		String exp_date="";
		now1.set(Calendar.MILLISECOND, 0);
		Date now=now1.getTime();
		
		String createTime = currentTime;
		String createYear = createTime.substring(0,4);
		
		if(createTime.compareTo(createYear+"0701000000") >=0 ){
			eff_date = createYear+"0701000000";
			exp_date = (Integer.parseInt(createYear)+1) +"0630235959";
		} else {
			eff_date = createYear+"0101000000";
			exp_date = createYear+"1231235959";
		}
		
		
		payBalance_0.setEff_date(eff_date);
		payBalance_0.setExp_date(exp_date);
		if(flag_frist){
			InfoPayBalance payBalance_3=new InfoPayBalance();
			String balanceId_3=Common.getUUID();
			payBalance_3.setBalance_id(balanceId_3);
			payBalance_3.setBalance(giveBalance);
			payBalance_3.setUser_id(userId);
			payBalance_3.setBalance_type_id(3);
			payBalance_3.setEff_date(eff_date);
			payBalance_3.setExp_date(exp_date);
			balance.add(payBalance_3);
			
			
			insertBalanceAccessLog.setTrade_Id(logTrade.getTrade_id());
			insertBalanceAccessLog.setTrade_Type_Code("501");
			insertBalanceAccessLog.setUser_Id(userId);
			insertBalanceAccessLog.setPartition_Id(Calendar.getInstance().get(Calendar.MONTH)+1);
			insertBalanceAccessLog.setBalance_Id(payBalance_3.getBalance_id());
			insertBalanceAccessLog.setBalance_Type_Id(payBalance_3.getBalance_type_id());
			insertBalanceAccessLog.setAccess_Tag(ADD_BALANCE);//取款还是存款
			insertBalanceAccessLog.setMoney(giveBalance);
			insertBalanceAccessLog.setNew_Balance(giveBalance);
			insertBalanceAccessLog.setOld_Balance(0);				
			insertBalanceAccessLog.setOperate_Time(currentTime);
			}
			balance.add(payBalance_0);
			String tradeid501= Common.getUUID();
			tradeHis.setTrade_id(tradeid501);
			tradeHis.setTrade_type_code("501");
			tradeHis.setExternal_system_code("10000");
			tradeHis.setUser_id(userId);
			tradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
			tradeHis.setChannel_type("121");
			tradeHis.setProcess_tag(0);
			tradeHis.setTrade_time(currentTime);
			tradeHis.setBalance(giveBalance);
			String remark=null;
			if(flag_frist){
				remark="京东账号首次关联号码"+mobileNumber+"赠送"+giveBalance+"个通信B";
			}
			tradeHis.setRemark(remark);
			createUserHis.setTrade_id(tradeHis.getTrade_id());
			createUserHis.setJd_pin(jdpin);
			createUserHis.setUser_id(userId);
			createUserHis.setPartition_id(tradeHis.getPartition_id());
			createUserHis.setProcess_tag(2);
		}

	
	


	public void setDb(DbTool db) {
		this.db = db;
	}
	
	
	
	
	
}
