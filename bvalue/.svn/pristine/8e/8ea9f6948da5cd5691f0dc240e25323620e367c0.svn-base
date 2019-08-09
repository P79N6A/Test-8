package com.tydic.beijing.bvalue.service.impl;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.LogTradeAdjustHis;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountAttrHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeRewardHis;
import com.tydic.beijing.bvalue.service.IQueryBAcctAccessLog;

public class QueryBAcctAccessLogImpl implements IQueryBAcctAccessLog {

	private static Logger log = Logger.getLogger(QueryBAcctAccessLogImpl.class);
	@Autowired
	private  DbTool dbTool;
	private String desc502ChangeTime;
	private HashMap<String,String>  pinpaiMap;
	private HashMap<String,String>  attrCodeMap;
	
	
	


	public HashMap<String, String> getPinpaiMap() {
		return pinpaiMap;
	}


	public void setPinpaiMap(HashMap<String, String> pinpaiMap) {
		this.pinpaiMap = pinpaiMap;
	}


	public HashMap<String, String> getAttrCodeMap() {
		return attrCodeMap;
	}


	public void setAttrCodeMap(HashMap<String, String> attrCodeMap) {
		this.attrCodeMap = attrCodeMap;
	}


	public String getDesc502ChangeTime() {
		return desc502ChangeTime;
	}


	public void setDesc502ChangeTime(String desc502ChangeTime) {
		this.desc502ChangeTime = desc502ChangeTime;
	}


	/**
	 * 查询异动明细
	 * @author zhanghengbo
	 */
	@Override
	public JSONObject queryAccessLog(JSONObject inputJson) {
		
		JSONObject retJson = new JSONObject();
//		DbTool dbTool = new DbTool();
		int pagecount =0;

		try {
			//校验入参
			log.debug("inputJson==>"+inputJson.toString());
			checkFormat(inputJson);
			//根据jdpin获取用户信息
			String jdpin = inputJson.getString("JDPin");
			log.debug("get jdpin =>"+jdpin);
			String userId = Common.md5(jdpin);
			log.debug("get userid =>"+userId);
			
			int pageIndex = inputJson.getInt("PageIndex");
			int rowPerPage =  inputJson.getInt("RowPerPage");
			int minNum = (pageIndex-1) * rowPerPage;
			int maxNum = pageIndex * rowPerPage;
			
			String strEndTime = inputJson.getString("EndTime");
			strEndTime = strEndTime + " 23-59-59";
			
//			List<BalanceAccessLog> listBalanceAccessLog = dbTool.getBalanceAccessLogByUserIdandTime(userId,//不再传accesstag和tradetype
//					inputJson.getString("StartTime"), strEndTime); //分页不在这里实现了。。。
			List<LogTradeHis> listLogTradeHis = dbTool.getLogTradeHisByUserIdAndTime(userId,inputJson.getString("StartTime"), strEndTime);
			log.debug("get all balanceaccesslog ,size=>"+listLogTradeHis.size());
			
			List<LogTradeHis> resultLogList = new ArrayList<LogTradeHis>();
			int index=0;
			for(LogTradeHis tmpa: listLogTradeHis){
//				if(inputJson.containsKey("AccessTag") && 
//						!checkAccessTag(tmpa.getTrade_type_code(),inputJson.getString("AccessTag")) ){//如果入参传了accestag，不满足就continue
//					continue;
//				}
				
				String tmpat = getAccessTag(tmpa.getBalance());
				if(inputJson.containsKey("AccessTag") &&
						!inputJson.getString("AccessTag").equals(tmpat)){
					log.debug("accesstag continue tmpat==>"+tmpat);
					continue;
				}
				
				if(inputJson.containsKey("TradeTypeCode") && 
						!inputJson.getString("TradeTypeCode").equals(tmpa.getTrade_type_code())){
					log.debug("accesstag continue tmpat==>"+tmpat);
					continue;
				}
				
				if(tmpa.getBalance() ==0){  //没有balance变化的，continue
					continue;
				}
				
				//分页改为在这里实现
				index++;
				if(index > minNum &&
						index <=maxNum){
					resultLogList.add(tmpa);
				}
				//分页结束

			}
			
			log.debug("the final listsize===>"+resultLogList.size());
			
			
			
			//如果pageindex=1,需要查询页数
			if(inputJson.getLong("PageIndex") == 1L){
				pagecount = index / rowPerPage;
				if(index > rowPerPage * pagecount){
					pagecount++;
				}
				log.debug("get pagecount===>"+pagecount);
				retJson.put("PageCount", pagecount);
			}
			
			//根据异动表明细查找相关订单，拼出订单明细返回
			JSONArray jsonArray = new JSONArray (); //AccessLogDtoList
			
			for(LogTradeHis tmpbal:resultLogList){
//				if( (tmpbal.getMoney() < 0 && inputJson.get("AccessTag").equals("0"))
//				|| (tmpbal.getMoney() > 0 && inputJson.get("AccessTag").equals("1"))){//0收入  1支出
//					continue;
//				}

				JSONObject accessLogDto = new JSONObject();  //accessLogDto
				String tmpaccesstag = getAccessTag(tmpbal.getBalance());
				accessLogDto.put("AccessTag", tmpaccesstag);
				accessLogDto.put("TradeTypeCode", tmpbal.getTrade_type_code());
				accessLogDto.put("Money", tmpbal.getBalance());
				accessLogDto.put("OperateTime",tmpbal.getTrade_time());
				String desc =getDesc(tmpbal);
				
				
				accessLogDto.put("Desc", desc);
				
				jsonArray.add(accessLogDto);
			}
			
			log.debug("jsonArray.size===>"+jsonArray.size());
			retJson.put("Status", "1");
			retJson.put("ErrorCode", "");
			retJson.put("ErrorMessage", "");
			retJson.put("JDPin", inputJson.getString("JDPin"));
			
			retJson.put("AccessLogDtoList", jsonArray);
			
			log.debug("retJson==>"+retJson.toString());
			
		}catch (BValueException be) {
			be.printStackTrace();
			retJson.put("Status", "0"); //失败
			retJson.put("ErrorCode", be.getErrCode());
			retJson.put("ErrorMessage", be.getErrMsg());			
		}catch (Exception e) {
			e.printStackTrace();
			retJson.put("Status", "0"); //失败
			retJson.put("ErrorCode", -99999);
			retJson.put("ErrorMessage", "查询异常");			
		}
		
		return retJson;

	}


	/**
	 * 根据异动表获取异动明细的描述
	 * @param tmpbal
	 * @return
	 */
	private String getDesc(LogTradeHis tmpbal) throws Exception{
		String retDesc ="";
		DbTool dbTool = new DbTool();
		if(tmpbal.getTrade_type_code().equals("101")){
			//SKU赠送
			if(getAccessTag(tmpbal.getBalance()).equals("0")){//收入
				retDesc="赠送"+tmpbal.getBalance()+"B,订单号"+tmpbal.getOrder_no();
			}else if (getAccessTag(tmpbal.getBalance()).equals("1")){//支出
				retDesc = "扣除"+tmpbal.getBalance()+"B,退货单号"+tmpbal.getOrder_no();
			}
		}else if (tmpbal.getTrade_type_code().equals("102")){ //102改为全民盛宴赠送类型，2月以后的购物赠使用104类型
			//购物赠送
			if(getAccessTag(tmpbal.getBalance()).equals("0")){//收入
				retDesc= "全民盛宴一月送"+tmpbal.getBalance()+"B";
			}
		}else if (tmpbal.getTrade_type_code().equals("104")){ 
			//购物赠送
			if(getAccessTag(tmpbal.getBalance()).equals("0")){//收入
				retDesc= ""+tmpbal.getPartition_id()+"月,购物奖励"+tmpbal.getBalance()+"B";//怎么获取月份？？
			}else if(getAccessTag(tmpbal.getBalance()).equals("1")){//支出
				retDesc ="退货,扣除"+tmpbal.getBalance()+"B";
			}
		} else if(tmpbal.getTrade_type_code().equals("103")){
			//开户赠送  开户赠送不存在负值，所以不再判断access_tag
			retDesc ="入网赠送"+tmpbal.getBalance()+"B";				
		}else if(tmpbal.getTrade_type_code().equals("201")){
			//退货  退货应该不存在正值，这里不再判断access_tag
			//retDesc = "退货,扣除"+tmpbal.getBalance()+"B";
			retDesc = "扣除"+tmpbal.getBalance()+"B,退货单号"+tmpbal.getOrder_no();
		}else if (tmpbal.getTrade_type_code().equals("202")){
			//换货
			if(getAccessTag(tmpbal.getBalance()).equals("0")){//收入
				retDesc= "换货,奖励"+tmpbal.getBalance()+"B";
			}else if(getAccessTag(tmpbal.getBalance()).equals("1")){//支出
				retDesc ="换货,扣除"+tmpbal.getBalance()+"B";
			}
		}else if(tmpbal.getTrade_type_code().equals("203")){
			//购物退
			retDesc = "退货,扣除"+tmpbal.getBalance()+"B";
		}else if(tmpbal.getTrade_type_code().equals("301")){
			//B值兑换 需要查询兑换明细
			List<LogTradeExchangeHis> listExchangeHis = new DbTool().getLogTradeExchangeHisbyTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id()); 
			
			if(listExchangeHis.size() ==0){//如果找不到兑换明细，返回异常
				return "";
				//throw new BValueException(BValueErrorCode.ERR_NO_EXCHANGE_HIS,"异常，没有找到兑换历史记录");
			}
			
			retDesc =""+(tmpbal.getBalance()<0?tmpbal.getBalance()*-1:tmpbal.getBalance())+"B已手动兑换";
			String expDateStr = "";
			boolean isMoney = false;
			boolean isDenomination = false;
			
		    //对于资源类型相同且结束时间相同的账本，做合并
			listExchangeHis = addUpExchangeHis(listExchangeHis);
			
			List<String> listExpDate = getAllExpDate(listExchangeHis);
		 
			for(String tmpexpdate :listExpDate){
				for(LogTradeExchangeHis tmphis:listExchangeHis){
					String unitName="";
					String resourceValue ="";
					String denomination="";
//					if(listExpDate.size()>1){
//						throw new BValueException(BValueErrorCode.ERR_TRADE_TYPE_CODE,"交易类型编码错误");
//					}else{

						if(!tmphis.getExp_date().equals(tmpexpdate)){
							continue;
						}
						
						if(tmphis.getResource_type_code().equals("ROV")){
							unitName="分钟语音，";
							resourceValue = ""+tmphis.getResource_value();
						}else if (tmphis.getResource_type_code().equals("ROF")){
							unitName="M流量，";
							resourceValue = ""+tmphis.getResource_value();
						}else if  (tmphis.getResource_type_code().equals("ROS")){
							unitName="条短信，";
							resourceValue = ""+tmphis.getResource_value();
						}else if  (tmphis.getResource_type_code().equals("ROM")){  
							unitName="元话费，";
							isMoney =true;
							resourceValue = ""+ (1.0*tmphis.getResource_value()) /10 ;
						}else if (tmphis.getResource_type_code().equals("ROD")){
							    denomination=tmphis.getReserve_1();//面额
								unitName=denomination+"东券；";
								resourceValue=tmphis.getResource_value()+"张";
								isDenomination = true;
							
						}	
						
//					}
					
					retDesc = retDesc + resourceValue +unitName+"";

					expDateStr = tmphis.getExp_date();
				}
				
				retDesc = retDesc + (isDenomination == true? "":(isMoney == true ?"话费":"资源") +"有效期至"+expDateStr.substring(0,4)+"年"+expDateStr.substring(4,6)+"月"+expDateStr.substring(6,8)+"日；");
				
			}
			
			
			//String expDateStr = new SimpleDateFormat("YYYYMMDD").format(expDate);
//			if(!isMoney){
//				retDesc = retDesc + "资源有效期至"+expDateStr.substring(0,4)+"年"+expDateStr.substring(4,6)+"月"+expDateStr.substring(6,8)+"日";
//			}
			
            
			
		}else if (tmpbal.getTrade_type_code().equals("302")){
			//B值自动兑换
            List<LogTradeAutoExchangeHis> listExchangeHis = new DbTool().getLogTradeAutoExchangeHisbyTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id()); 
            if(listExchangeHis.size() ==0){//如果找不到兑换明细，返回异常
				return "";
				//throw new BValueException(BValueErrorCode.ERR_NO_EXCHANGE_HIS,"异常，没有找到兑换历史记录");
			}
			
			retDesc =""+(tmpbal.getBalance()<0?tmpbal.getBalance()*-1:tmpbal.getBalance())+"B已自动兑换";
			String expDateStr = "";
			String unitDetail ="";
			boolean isMoney = false;
			
			listExchangeHis = addUpAutoExchangeHis(listExchangeHis);
			List<String> listExpDate = getAllAutoExpDate(listExchangeHis);
			
			for(String tmpexpDate : listExpDate){
				
				for(LogTradeAutoExchangeHis tmphis:listExchangeHis){
					String unitName="";
					String resourceValue = "";
					
					if(!tmphis.getExp_date().equals(tmpexpDate)){
						continue;
					}
					
					if(tmphis.getResource_type_code().equals("ROV")){ 
						unitName="分钟语音，";
						resourceValue = ""+tmphis.getResource_value();
					}else if (tmphis.getResource_type_code().equals("ROF")){
						unitName="M流量，";
						resourceValue = ""+tmphis.getResource_value();
					}else if  (tmphis.getResource_type_code().equals("ROS")){
						unitName="条短信，";
						resourceValue = ""+tmphis.getResource_value();
					}
					else if  (tmphis.getResource_type_code().equals("ROM")){  
						unitName="元话费，";
						isMoney =true;
					//	resourceValue = ""+ (1.0*tmphis.getResource_value()) /10;
						resourceValue = ""+ (1.0* Long.parseLong( tmphis.getResource_value() )) /10 ;
					}
					//unitDetail = unitDetail + resourceValue +unitName+"";
					retDesc = retDesc + resourceValue +unitName+"";
					expDateStr  =tmphis.getExp_date();
				}
				
				retDesc = retDesc + (isMoney == true ?"话费":"资源") + "有效期至"+expDateStr.substring(0,4)+"年"+expDateStr.substring(4,6)+"月"+expDateStr.substring(6,8)+"日；";
				
			}
			

			
//			if(!isMoney){
//				retDesc = retDesc +unitDetail + "资源有效期至"+expDateStr.substring(0,4)+"年"+expDateStr.substring(4,6)+"月"+expDateStr.substring(6,8)+"日";
//			}
			
		
		}else if  (tmpbal.getTrade_type_code().equals("506")){  //B值调整
			List<LogTradeAdjustHis> listLogTradeAdjustHis = new DbTool().getLogTradeAdjustHisByTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id());
			
			if(listLogTradeAdjustHis.size()==0){
				throw new BValueException(-10018,"没有找到B值调整明细记录");
			}
			
			for(LogTradeAdjustHis tmplah :listLogTradeAdjustHis){
				
				String adjustType ="";
				if(tmplah.getAdjust_mode().equals("1")){
					adjustType = "调增";
				}else if (tmplah.getAdjust_mode().equals("2")){
					adjustType = "调减";
				}
				 
				retDesc = retDesc  +  adjustType + "用户"+tmplah.getAdjust_fee()+"B值，调整后B值为"+tmplah.getNew_balance()+"";
			}
		}else if(tmpbal.getTrade_type_code().equals("503")){
			retDesc ="";
//			List<LogTradeExternalAccountAttrHis> listattrs = new DbTool().getAttrHisbyUserIdAndExternal(tmpbal.getUser_id(),tmpbal.getTrade_id());
//			
//			for(LogTradeExternalAccountAttrHis tmpattrhis :listattrs){
//                String attrdesc = getAttrDesc(tmpattrhis);
//                
//				retDesc = retDesc + attrdesc;
//			}
			
		}else if(tmpbal.getTrade_type_code().equals("604")){
			String userId=tmpbal.getUser_id();
			String tradeId=tmpbal.getTrade_id();
			String platname="";
			List<LogTradeRewardHis> logTradeRewardHis=dbTool.queryLogTradeRewardHis(userId,tradeId);
			for(LogTradeRewardHis his:logTradeRewardHis){
		    platname=his.getPlat_name();
			log.debug(his.getPlat_name()+"获赠"+tmpbal.getBalance()+"B"+"");
			}
			retDesc=platname+"获赠"+tmpbal.getBalance()+"B"+"";
		}else if(tmpbal.getTrade_type_code().equals("502")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss ");
			String currDate=sdf.format(new Date());
			if(desc502ChangeTime.compareTo(currDate)<=0){
			List<LogTradeExternalAccountHis> listLogAccountHis = dbTool.getLogTradeExternalAccountHisByTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id());
			List<LogTradeExternalAccountAttrHis> listLogAccountAttrHis = dbTool.getLogTradeExternalAccountAttrHis(tmpbal.getTrade_id(),tmpbal.getUser_id());
			if(listLogAccountHis.size()==0 || listLogAccountAttrHis.size()==0){
				throw new BValueException(-10016,"没有找到账号关联明细");
			}
			String opertype = listLogAccountAttrHis.get(0).getOperation_type();
			String opername = opertype.equals("1")?"关联":"解关联";
			if(tmpbal.getBalance()==0){
				retDesc= null;
			}else{
				if(opername.equals("关联")){
					if(tmpbal.getBalance()>0){
						
						retDesc="京东账号首次关联号码"+listLogAccountHis.get(0).getExternal_account_code()+";赠送"+tmpbal.getBalance()+"个通信B;";	
					}else{
						retDesc=null;
					}
				}else if(opername.equals("解关联")){
					if(tmpbal.getBalance()<0){
						retDesc="账户解关联通信B清零";
					}else{
						retDesc= null;
					}
				}
			}
			
		  }else{
			  retDesc= null;  
		  }
			log.debug("retDesc==========="+retDesc); 
		}else{
			//throw new BValueException(BValueErrorCode.ERR_TRADE_TYPE_CODE,"交易类型编码错误");
			retDesc= null;  
		}

		return retDesc;
	}

private List<String> getAllExpDate(List<LogTradeExchangeHis> listExchangeHis) {
	 List<String> listExpDate = new ArrayList<String>();
	 for( LogTradeExchangeHis tmphis :listExchangeHis){
		 if(!listExpDate.contains(tmphis.getExp_date())){
			 listExpDate.add(tmphis.getExp_date());
		 }
	 }
		return listExpDate;
	}

private List<String> getAllAutoExpDate(List<LogTradeAutoExchangeHis> listExchangeHis) {
	 List<String> listExpDate = new ArrayList<String>();
	 
	 for( LogTradeAutoExchangeHis tmphis :listExchangeHis){
		 if(!listExpDate.contains(tmphis.getExp_date())){
			 listExpDate.add(tmphis.getExp_date());
		 }
	 }
 
		return listExpDate;
	}

private List<LogTradeExchangeHis> addUpExchangeHis(List<LogTradeExchangeHis> listExchangeHis) {
		List<LogTradeExchangeHis> newlistExchangeHis = new ArrayList<LogTradeExchangeHis>();
		for(LogTradeExchangeHis tmphis:listExchangeHis){
			//如果list里已经有了，并且有效期相同，则累加
				addUptoHis(newlistExchangeHis,tmphis);
		}
		
		log.debug("合并后兑换明细数量："+newlistExchangeHis.size());
		return newlistExchangeHis;
	}


private void addUptoHis(List<LogTradeExchangeHis> newlistExchangeHis,
		LogTradeExchangeHis tmphis) {
	
	boolean isExist = false;
 	for(int i = 0;i<newlistExchangeHis.size();i++){
		if(newlistExchangeHis.get(i).getResource_type_code().equals(tmphis.getResource_type_code()) &&
				newlistExchangeHis.get(i).getExp_date().equals(tmphis.getExp_date())){
			long oldResourceValue = newlistExchangeHis.get(i).getResource_value();
			newlistExchangeHis.get(i).setResource_value(oldResourceValue+tmphis.getResource_value());
			isExist = true;
		}
	}
	
	if(isExist == false){
		newlistExchangeHis.add(tmphis);
	}
 
}

private List<LogTradeAutoExchangeHis> addUpAutoExchangeHis(List<LogTradeAutoExchangeHis> listExchangeHis) {
	List<LogTradeAutoExchangeHis> newlistExchangeHis = new ArrayList<LogTradeAutoExchangeHis>();
	for(LogTradeAutoExchangeHis tmphis:listExchangeHis){
		//如果list里已经有了，并且有效期相同，则累加
			addUptoAutoHis(newlistExchangeHis,tmphis);
	}
	
	log.debug("合并后兑换明细数量："+newlistExchangeHis.size());
	return newlistExchangeHis;
}


private void addUptoAutoHis(List<LogTradeAutoExchangeHis> newlistExchangeHis,
		LogTradeAutoExchangeHis tmphis) {

	boolean isExist = false;
	for(int i = 0;i<newlistExchangeHis.size();i++){
	if(newlistExchangeHis.get(i).getResource_type_code().equals(tmphis.getResource_type_code()) &&
			newlistExchangeHis.get(i).getExp_date().equals(tmphis.getExp_date())){
		long oldResourceValue =  Long.parseLong( newlistExchangeHis.get(i).getResource_value() );
		long newResourceValue = oldResourceValue+Long.parseLong(tmphis.getResource_value());
		newlistExchangeHis.get(i).setResource_value(""+newResourceValue);
		isExist = true;
	   }
    }
	if(!isExist){
		newlistExchangeHis.add(tmphis);
	}

}


//	private String getAttrDesc(LogTradeExternalAccountAttrHis attrhis) {
//		
//		String attrname= attrCodeMap.get(attrhis.getAttr_code());
//		String attrOperateTypename = attrOperateTypeMap.get(attrhis.getOperation_type());
//		
//		String attrDesc = attrOperateTypename +"属性["+attrname+"]的值[";
//		if(attrhis.getAttr_code()!=null && attrhis.getAttr_code().equals("1002")){
//			attrDesc = attrDesc + pinpaiMap.get(attrhis.getAttr_value());
//		}else{
//			attrDesc = attrDesc + attrhis.getAttr_value();
//		}
//		
//		attrDesc = attrDesc +"],有效期"+attrhis.getEff_date().substring(0,8)+"至"+attrhis.getExp_date().substring(0,8)+";";
//       
//		return attrDesc;
//	}


	private String getAccessTag(long balance) {
		
		if(balance >0){
			return "0";
		}else if (balance <0){
			return "1";
		}
		
		return "";
	}

	//入参校验
	/*JDPin	String	1-100	京东pin
	AccessTag	String	1	操作类型
	0:收入
	1:支出
	TradeTypeCode	String	1-5	交易类型
	101：SKU赠送
	102：购物赠送
	103：开户赠送
	201：退货
	202：换货
	301：B值兑换
	302：B值自动兑换
	StartTime	Date	14	开始时间
	EndTime	Date	14	结束时间
	PageIndex	Long	3	页码，首页为1
	RowPerPage	Long	3	每页记录数
	*/
	private void checkFormat(JSONObject string) throws Exception {
		
		if(!string.containsKey("JDPin") || !string.containsKey("StartTime") ||!string.containsKey("EndTime") 
				||!string.containsKey("PageIndex") || !string.containsKey("RowPerPage")){
			throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"必填字段不能为空");
		}
		
		if(string.containsKey("AccessTag") && 
				(!string.get("AccessTag").equals("0") && !string.get("AccessTag").equals("1"))){
			throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"操作类型编码错误");
		}
		
		//不要再读数据库配置了吧？？
//		if(string.containsKey("TradeTypeCode") && 
//				(!string.get("TradeTypeCode").equals("101") && !string.get("TradeTypeCode").equals("102") && !string.get("TradeTypeCode").equals("103")
//				&& !string.get("TradeTypeCode").equals("201") && !string.get("TradeTypeCode").equals("202")
//				&& !string.get("TradeTypeCode").equals("301") && !string.get("TradeTypeCode").equals("302")
//						)){
//			throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"交易类型编码错误");
//		}
		
		if(string.containsKey("RowPerPage") && string.getInt("RowPerPage") <=0 ){
			throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"每页数量必须大于0");
		}
		
		if(string.containsKey("PageIndex") && string.getInt("PageIndex") <=0 ){
			throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"页码必须大于0");
		}

	}
	
	private boolean checkheyueattr(List<LogTradeExternalAccountAttrHis> listattrs){
		for(LogTradeExternalAccountAttrHis list:listattrs)
		if(list.getAttr_code().equals("1001")&& list.getAttr_code()!=null){
			return true;
		}
		return false;
		
		
	}
 
	//变更[品牌]为[乐购卡]；变更[合约]为[XXX]，有效期yyyymmdd至yyyymmdd
	private String getAttrDesc(LogTradeExternalAccountAttrHis attrhis,String prefix) {
		
		String attrname= attrCodeMap.get(attrhis.getAttr_code());
		String tradeTypeCode=attrhis.getTrade_type_code();
		String attrDesc;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss ");
		String currDate=sdf.format(new Date());
	//	String attrOperateTypename = attrOperateTypeMap.get(attrhis.getOperation_type());
		//1001 合约 1002 品牌 1003 主产品

			 attrDesc = prefix+"["+attrname+"]为[";
			 if(attrhis.getAttr_code()!=null){
				 if( attrhis.getAttr_code().equals("1002")){
						attrDesc = attrDesc + pinpaiMap.get(attrhis.getAttr_value()) +"];";
						log.debug("attrDesc:"+attrDesc);
					}else if (attrhis.getAttr_code().equals("1001")){
						attrDesc = attrDesc + attrhis.getAttr_value() +"],有效期"+attrhis.getEff_date().substring(0,8)+"至"+attrhis.getExp_date().substring(0,8)+";";
						log.debug("attrDesc:"+attrDesc);
					}else if (attrhis.getAttr_code().equals("1003")) {
						attrDesc = attrDesc +attrhis.getAttr_value() +"],有效期"+attrhis.getEff_date().substring(0,8)+"至"+attrhis.getExp_date().substring(0,8)+";";
						log.debug("attrDesc:"+attrDesc);
					}else if(attrhis.getAttr_code().equals("1004")){
						attrDesc=attrDesc+null;
					}
			 } 
			
			 log.debug("attrDesc:"+attrDesc);
		return attrDesc;
	}
}
