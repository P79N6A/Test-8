package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LogTradeAdjustHis;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeSetHis;
import com.tydic.beijing.bvalue.dao.LogTradeExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountAttrHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeRewardHis;
import com.tydic.beijing.bvalue.dao.QueryTradeReq;
import com.tydic.beijing.bvalue.dao.QueryTradeResp;
import com.tydic.beijing.bvalue.dao.TradeLogDto;
import com.tydic.beijing.bvalue.service.QueryTrade;

/**
 * @author zhanghengbo
 *
 */
public class QueryTradeImpl implements QueryTrade {

	@Autowired
	private DbTool dbTool;
	
	private String desc502ChangeTime;
	
	//private HashMap<String,String> attrOperateTypeMap;
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



	private static Logger log = Logger.getLogger(QueryTradeImpl.class);
	
	@Override
	public QueryTradeResp query(QueryTradeReq queryTradeReq) {
		// 台账记录查询
		log.debug("输入的参数为========="+queryTradeReq.toString());
		QueryTradeResp queryTradeResp = checkReq(queryTradeReq);
		if(queryTradeResp.getStatus().equals("0")){
			return queryTradeResp;
		}
		
		try {			
			String jdPin = queryTradeReq.getJDPin();
			String userId = Common.md5(jdPin);
			int pagecount =0;
			
			int pageIndex = (int) queryTradeReq.getPageIndex(); 
			int rowPerPage = (int) queryTradeReq.getRowPerPage();
			int minNum = (pageIndex-1) * rowPerPage;
			int maxNum = pageIndex * rowPerPage;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			String strEndTime = sdf.format(queryTradeReq.getEndTime());
			String strStartTime = sdf.format(queryTradeReq.getStartTime());
			strEndTime = strEndTime + " 23-59-59";
			
			List<LogTradeHis> listLogTradeHis = dbTool.getLogTradeHisByUserIdAndTime(userId,strStartTime, strEndTime);
			log.debug("get all balanceaccesslog ,size=>"+listLogTradeHis.size());
			
			List<LogTradeHis> resultLogList = new ArrayList<LogTradeHis>();
			int index=0;
			for(LogTradeHis tmpa: listLogTradeHis){
				String tmpat = getAccessTag(tmpa.getBalance());
				if(queryTradeReq.getAccessTag()!= null && !queryTradeReq.getAccessTag().equals(tmpat)){
					continue;
				}
				
				if(queryTradeReq.getTradeTypeCode()!=null && 
						!queryTradeReq.getTradeTypeCode().equals(tmpa.getTrade_type_code())){
					continue;
				}
				//分页改为在这里实现
				index++;
				if(index > minNum && index <=maxNum){
					resultLogList.add(tmpa);
				}
				//分页结束
			}
			
			log.debug("the final listsize===>"+resultLogList.size());
			
			//获得了过滤后的订单列表，以后都根据该列表做处理
			//如果pageindex=1,需要查询页数
			if(pageIndex == 1){
				pagecount = index / rowPerPage;
				if(index > rowPerPage * pagecount){
					pagecount++;
				}
				log.debug("get pagecount===>"+pagecount);
				queryTradeResp.setPageCount(pagecount);
			}
			
			List<TradeLogDto> listTradeLogDto = new ArrayList<TradeLogDto>();
			for(LogTradeHis tmpbal:resultLogList){
				TradeLogDto tradeLogDto = new TradeLogDto();
				tradeLogDto.setTradeTypeCode(tmpbal.getTrade_type_code());
				tradeLogDto.setBValue(tmpbal.getBalance() );
				tradeLogDto.setOrderNo(tmpbal.getOrder_no());
				tradeLogDto.setOrderAmount(tmpbal.getOrder_amount());
				tradeLogDto.setOperateTime(tmpbal.getProcess_time() == null?null: sdfhms.parse(tmpbal.getProcess_time()));//TODO 格式问题
				String desc =getDesc(tmpbal);
				tradeLogDto.setDesc(desc);
			    
				listTradeLogDto.add(tradeLogDto);
			}
			
			queryTradeResp.setStatus("1");
			queryTradeResp.setErrorCode("");
			queryTradeResp.setErrorMessage("");
			queryTradeResp.setJDPin(jdPin);
			queryTradeResp.setTradeLogDtoList(listTradeLogDto);
			log.debug("return status="+queryTradeResp.getStatus());
			log.debug("return logtradedto="+queryTradeResp.getTradeLogDtoList().size());
		} catch (Exception e) {
			e.printStackTrace();
			queryTradeResp.setStatus("0");
			queryTradeResp.setErrorCode("-10011");
			queryTradeResp.setErrorMessage(e.getMessage());
			log.debug("return status="+queryTradeResp.getStatus());
		}

		
//		log.debug("return logtradedto="+queryTradeResp.getTradeLogDtoList().size());
		return queryTradeResp;
	}

	private String getDesc(LogTradeHis tmpbal) throws Exception {
		
	       String retDesc ="";
			if(tmpbal.getTrade_type_code().equals("101")){
				//SKU赠送
				if(getAccessTag(tmpbal.getBalance()).equals("0") ) {//收入
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
					retDesc= ""+tmpbal.getPartition_id()+"月,购物奖励"+tmpbal.getBalance()+"B";//TODO 怎么获取月份？？
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
				//退货  退货应该不存在正值，这里不再判断access_tag
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
				
			    //对于资源类型相同且结束时间相同的账本，做合并
				listExchangeHis = addUpExchangeHis(listExchangeHis);
				
				List<String> listExpDate = getAllExpDate(listExchangeHis);
			 
				for(String tmpexpdate :listExpDate){
					for(LogTradeExchangeHis tmphis:listExchangeHis){
						String unitName="";
						String resourceValue =""; 
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
						}
						retDesc = retDesc + resourceValue +unitName+"";
						
						expDateStr = tmphis.getExp_date();
					}
					
					retDesc = retDesc + (isMoney == true ?"话费":"资源") + "有效期至"+expDateStr.substring(0,4)+"年"+expDateStr.substring(4,6)+"月"+expDateStr.substring(6,8)+"日；";
					
				}
				
				
	            
				
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
			
			} else if  (tmpbal.getTrade_type_code().equals("505")){ //自动兑换设置及变更
				
				//LOG_TRADE_AUTO_EXCHANGE_SET_HIS
				List<LogTradeAutoExchangeSetHis> listSetHis = dbTool.getLogTradeAutoExchangeSetHisByTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id());
				if(listSetHis.size()==0) {
					throw new BValueException(-10015,"没有找到自动兑换设置明细");
				}
				LogTradeAutoExchangeSetHis setHisOne = listSetHis.get(0);
				
				//COD：按天兑换 COW：按周兑换	COM：按月兑换 COS：按季兑换 	COY：按年兑换
				if(setHisOne.getCycle_type().equals("COM") ){ //目前只有按月兑换
					if(setHisOne.getExchange_mode().equals("1")){ //0：按固定值	1：按百分比 目前只有按百分比
						//改为到life_resource_list表获取设置明细
						List<LifeResourceList> listLifeResourceList = dbTool.getLifeResourceListByResourceId(setHisOne.getResource_list_id(),setHisOne.getUser_id());
						for(LifeResourceList tmplrl:listLifeResourceList){
							String unitName="";
							if(tmplrl.getResource_type_code().equals("ROM")){
								unitName="金钱";
							}else if (tmplrl.getResource_type_code().equals("ROV")){
								unitName="语音";
							}else if (tmplrl.getResource_type_code().equals("ROF")){
								unitName="流量";
							}else if (tmplrl.getResource_type_code().equals("ROS")){
								unitName="短信";
							}
							
							retDesc = retDesc + unitName +"百分比" + tmplrl.getResource_value() +"%,";
			
						}
						
						//001：余量全部兑换  002：最高封顶
						if(setHisOne.getPurchase_mode().equals("002") && setHisOne.getTop_b_value() >0){
							retDesc = retDesc + "每月兑换封顶：【" + setHisOne.getTop_b_value() +"B】";
						}else{
							retDesc = retDesc + "每月兑换封顶：【不封顶】";
						}
					 
					}else if (setHisOne.getExchange_mode().equals("0")){
						//固定值 
						//TODO 后续补充
					}
	 
				}else{
				 log.debug("cycle_type不是COM,是["+setHisOne.getCycle_type()+"]");
				}
			
			} else if  (tmpbal.getTrade_type_code().equals("501")){ //开户
				retDesc = "开户";  
				
			} else if  (tmpbal.getTrade_type_code().equals("502")){  //外部账号绑定
				//绑定
				List<LogTradeExternalAccountHis> listLogAccountHis = dbTool.getLogTradeExternalAccountHisByTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id());
				List<LogTradeExternalAccountAttrHis> listLogAccountAttrHis = dbTool.getLogTradeExternalAccountAttrHis(tmpbal.getTrade_id(),tmpbal.getUser_id());
				
				if(listLogAccountHis.size()==0 || listLogAccountAttrHis.size()==0){
					throw new BValueException(-10016,"没有找到账号关联明细");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss ");
				String currDate=sdf.format(new Date());
				String opertype = listLogAccountAttrHis.get(0).getOperation_type();
				String opername = opertype.equals("1")?"关联":"解关联";
				if(opername.equals("关联")){
					if(tmpbal.getBalance()==0){
						retDesc = opername+"京东通信手机号"+ listLogAccountHis.get(0).getExternal_account_code()+";";
						List<LogTradeExternalAccountAttrHis> listattrs = dbTool.getAttrHisbyUserIdAndExternal(tmpbal.getUser_id(),tmpbal.getTrade_id());
		                boolean isheyueExists = checkheyueattr(listattrs); //默认是false，有合约，就返回true,没合约就返回false
						for(LogTradeExternalAccountAttrHis tmpattrhis :listattrs){
							if(isheyueExists&&tmpattrhis.getAttr_code().contains("1003")){//如果有合约，并且遍历到了主产品，则continue
								continue;	
							}
							if(tmpattrhis.getAttr_code().contains("1004")){
								continue;
							}
			                String attrdesc = getAttrDesc(tmpattrhis,"");
			                
							retDesc = retDesc + attrdesc;
						}
					}else{
						retDesc="京东账号首次关联号码"+listLogAccountHis.get(0).getExternal_account_code()+";赠送"+tmpbal.getBalance()+"个通信B;";
					}		
				}else if(opername.equals("解关联")){
					if(desc502ChangeTime.compareTo(currDate)>=0){
						retDesc = opername+"京东通信手机号"+ listLogAccountHis.get(0).getExternal_account_code()+";";
						List<LogTradeExternalAccountAttrHis> listattrs = dbTool.getAttrHisbyUserIdAndExternal(tmpbal.getUser_id(),tmpbal.getTrade_id());
						boolean isheyueExists = checkheyueattr(listattrs); //默认是false，有合约，就返回true,没合约就返回false
						for(LogTradeExternalAccountAttrHis tmpattrhis :listattrs){
							if(isheyueExists&&tmpattrhis.getAttr_code().contains("1003")){//如果有合约，并且遍历到了主产品，则continue
								continue;	
							}
							if(tmpattrhis.getAttr_code().contains("1004")){
								continue;
							}
							String attrdesc = getAttrDesc(tmpattrhis,"");
		                
							retDesc = retDesc + attrdesc;
						}
					}else{
						retDesc="账户解关联通信B清零";
					}
				}
				
				log.debug("retDesc==========="+retDesc);
			} 
//			else if  (tmpbal.getTrade_type_code().equals("503")){  //用户属性变更
//				 List<LogTradeExternalAccountAttrHis> listLogAccountAttrHis = dbTool.getLogTradeExternalAccountAttrHis(tmpbal.getTrade_id(),tmpbal.getUser_id());
//				
//				if( listLogAccountAttrHis.size()==0){
//					throw new BValueException(-10016,"没有找到账号关联明细");
//				}
//				
//				for(LogTradeExternalAccountAttrHis tmpaah :listLogAccountAttrHis) {
//					if(retDesc.length()>0){
//						retDesc = retDesc +"；";
//					}
//					retDesc = retDesc + "更改用户" +getAttrName(tmpaah.getAttr_code()) +"属性，变更为" + getAttrValueName(tmpaah.getAttr_value()) +"";
//				}
//	 
//			} 
			else if  (tmpbal.getTrade_type_code().equals("506")){  //B值调整
				List<LogTradeAdjustHis> listLogTradeAdjustHis = dbTool.getLogTradeAdjustHisByTradeId(tmpbal.getTrade_id(),tmpbal.getUser_id());
				
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
			}else if(tmpbal.getTrade_type_code().equals("504")){
				retDesc ="";
				List<LogTradeExternalAccountAttrHis> listattrs = dbTool.getAttrHisbyUserIdAndExternal(tmpbal.getUser_id(),tmpbal.getTrade_id());
				
				boolean isheyueExists = checkheyueattr(listattrs); //默认是false，有合约，就返回true,没合约就返回false
				
				for(LogTradeExternalAccountAttrHis tmpattrhis :listattrs){
					
					if(isheyueExists && tmpattrhis.getAttr_code().contains("1003")){//如果有合约，并且遍历到了主产品，则continue
						continue;	
					}
					
	                String attrdesc = getAttrDesc(tmpattrhis,"变更");
	                
					retDesc = retDesc + attrdesc;
				}
				log.debug("retDesc==========="+retDesc);
			}
			else if(tmpbal.getTrade_type_code().equals("604")){
			//增加B值发放的
//			retDesc="";
//			long Bbalance=0;
//			
//			List<LogTradeHis> listlogtrade=dbTool.getBGrantByTradeTypeCode(tmpbal.getTrade_id(),tmpbal.getUser_id());
//			 
//			for(LogTradeHis tmptrade:listlogtrade){
//				long Bblance=((LogTradeHis) listlogtrade).getBalance();
//			}
			String userId=tmpbal.getUser_id();
			String tradeId=tmpbal.getTrade_id();
			String platname="";
			List<LogTradeRewardHis> logTradeRewardHis=dbTool.queryLogTradeRewardHis(userId,tradeId);
			for(LogTradeRewardHis ltrh:logTradeRewardHis){
		    platname=ltrh.getPlat_name();
			log.debug(ltrh.getPlat_name()+" 获赠 "+tmpbal.getBalance()+" B");
			}
			retDesc=platname+" 获赠 "+tmpbal.getBalance()+" B";
			}else {
				throw new BValueException(BValueErrorCode.ERR_TRADE_TYPE_CODE,"交易类型编码错误");
			}

			return retDesc;
		
		
		
		
	}

	//变更[品牌]为[乐购卡]；变更[合约]为[XXX]，有效期yyyymmdd至yyyymmdd
	private String getAttrDesc(LogTradeExternalAccountAttrHis attrhis,String prefix) {
		
		String attrname= attrCodeMap.get(attrhis.getAttr_code());
		String tradeTypeCode=attrhis.getTrade_type_code();
		String attrDesc=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss ");
		String currDate=sdf.format(new Date());
	//	String attrOperateTypename = attrOperateTypeMap.get(attrhis.getOperation_type());
		//1001 合约 1002 品牌 1003 主产品

			 attrDesc = prefix+"["+attrname+"]为[";
			 if(attrhis.getAttr_code()!=null){
				 if( attrhis.getAttr_code().equals("1002")){
						attrDesc = attrDesc + pinpaiMap.get(attrhis.getAttr_value()) +"];";
						
					}else if (attrhis.getAttr_code().equals("1001")){
						attrDesc = attrDesc + attrhis.getAttr_value() +"],有效期"+attrhis.getEff_date().substring(0,8)+"至"+attrhis.getExp_date().substring(0,8)+";";
						
					}else if (attrhis.getAttr_code().equals("1003")) {
						attrDesc = attrDesc +attrhis.getAttr_value() +"],有效期"+attrhis.getEff_date().substring(0,8)+"至"+attrhis.getExp_date().substring(0,8)+";";
						
					}else if(attrhis.getAttr_code().equals("1004")){
						attrDesc=attrDesc;
					}
			 } 
			
			 log.debug("attrDesc:"+attrDesc);
		return attrDesc;
	}
	//用于判断合约和主产品是否同时存在
	private boolean checkheyueattr(List<LogTradeExternalAccountAttrHis> listattrs){
		for(LogTradeExternalAccountAttrHis list:listattrs)
		if(list.getAttr_code().equals("1001")&& list.getAttr_code()!=null){
			return true;
		}
		return false;
		
		
	}
	
	private String getAttrValueName(String attr_value) {
		//TODO 这个地方需要配置啊 大哥
		return attr_value;
	}

	private String getAttrName(String attr_code) {
		
		if(attr_code.equals("PRODUCT")){
			return "主产品";
		}
		
		return "";
	}

	private String getAccessTag(long balance) {
		if(balance >0){
			return "0";
		} else if (balance <0){
			return "1";
		} else {
			return "";
		}
		
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



private List<String> getAllAutoExpDate(List<LogTradeAutoExchangeHis> listExchangeHis) {
	 List<String> listExpDate = new ArrayList<String>();
	 
	 for( LogTradeAutoExchangeHis tmphis :listExchangeHis){
		 if(!listExpDate.contains(tmphis.getExp_date())){
			 listExpDate.add(tmphis.getExp_date());
		 }
	 }
 
		return listExpDate;
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



private List<String> getAllExpDate(List<LogTradeExchangeHis> listExchangeHis) {
	 List<String> listExpDate = new ArrayList<String>();
	 for( LogTradeExchangeHis tmphis :listExchangeHis){
		 if(!listExpDate.contains(tmphis.getExp_date())){
			 listExpDate.add(tmphis.getExp_date());
		 }
	 }
		return listExpDate;
	}

	private QueryTradeResp checkReq(QueryTradeReq queryTradeReq) {

		QueryTradeResp queryTradeResp = new QueryTradeResp();
		queryTradeResp.setStatus("1");
		
		log.debug("queryTradeReq.accesstag="+queryTradeReq.getAccessTag()+",jdpin="+queryTradeReq.getJDPin()+",pageindex="+queryTradeReq.getPageIndex()+",rowperpage="
				+queryTradeReq.getRowPerPage()+",tradetypecode="+queryTradeReq.getTradeTypeCode()+",");
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String jdPin = queryTradeReq.getJDPin();
			if(jdPin == null || jdPin.length() ==0){
				throw new Exception ("jdpin不能为空");
			}
			String st = sdf.format(queryTradeReq.getStartTime());
			String et = sdf.format(queryTradeReq.getEndTime());
			if(queryTradeReq.getPageIndex() >999 || queryTradeReq.getRowPerPage() >999){
				throw new Exception ("页码或每页显示数量太大");
			}
			
		} catch (Exception e) {
			queryTradeResp.setStatus("0");
			queryTradeResp.setErrorCode("-10013");
			queryTradeResp.setErrorMessage("格式错误:"+e.getMessage());
		}

		return queryTradeResp;
	}

}
