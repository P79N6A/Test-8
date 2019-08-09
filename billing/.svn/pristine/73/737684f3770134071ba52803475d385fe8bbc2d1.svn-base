package com.tydic.beijing.billing.rating.service.impl;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.rating.domain.RatingData;
//import com.tydic.beijing.billing.rating.domain.RatingException;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.service.Rating;


/**
 *  计费方法入口
 * @author zhanghengbo
 *
 */
public class RatingImpl implements Rating{
	
	private static  Logger log = Logger.getLogger(RatingImpl.class);
    
//	private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"rating.xml"});


	private  RatingMsg ratingMsg ;
	
	private  MsgParsingImpl msgParsing;
	
	private RatingData ratingData;
	
	private DinnerConversionImpl dinnerConversion;
//	@Autowired
//	BalanceData balanceData;

	DeductResourceAcct deductResourceAcct;
	
	
   /**
    * 提供给统一接入的计费方法
    * @param strRequestMsg
    * @return
    */
//	@Transactional(rollbackFor=Exception.class)
	public String deal( String strRequestMsg ){
//		ratingMsg=new RatingMsg();
//		ratingData=new RatingData();
//		strRequestMsg=strRequestMsg.trim();
//		long starttime = System.currentTimeMillis();
//		long endtime =0L;
//		
//		
//		//MsgParsing msgParsing = new MsgParsingImpl();
//		
//	    log.info("接收的统一接入消息:"+strRequestMsg);
//	    System.out.println("接收的统一接入消息:"+strRequestMsg);
//	    
//	    msgParsing=new MsgParsingImpl();
	    
//	    String strReturnMsg ="";
//	    String returnmsg="";
//		try {
			//ratingMsg = msgParsing.getRatingMsgFromRequestMsg(strRequestMsg);
//			msgParsing.setRatingMsg(ratingMsg);
//			
//			ratingMsg = msgParsing.getRatingMsgFromRequestMsg(strRequestMsg);
//
//			DirectRating directRating=new DirectRating(ratingMsg,ratingData);
//			
//			RateData result=null;
//			
//			long startRate = System.currentTimeMillis();
//			result=directRating.executeRating();
//			long endRate = System.currentTimeMillis();
//			System.out.println("executeRating :["+(endRate-startRate)+"]milliseconds");
//			ratingMsg=directRating.getRatingMsg();
//			ratingData=directRating.getRatingData();
//			strReturnMsg=msgParsing.createMergeOutMsg(ratingMsg,ratingData);
//					
//		}catch(RatingException re){
//			log.debug(re.printError());
//			System.out.println(re.printError());
//			returnmsg=msgParsing.createErrorOutMsg(ratingMsg,ratingData,re.getErrorCode(),re.getMessage(), strRequestMsg);
//		}catch (Exception e) {
//			log.info(e.getLocalizedMessage());
////			e.printStackTrace();
//			returnmsg=msgParsing.createErrorOutMsg(ratingMsg,ratingData,-1,e.getLocalizedMessage(), strRequestMsg);
//		}
		
	
//		log.debug("返回数据:"+strReturnMsg);
//		System.out.println("返回数据:"+strReturnMsg);
//		
		return "";

	}
	
	/**
	 * 非批价消息处理，包括查询和直扣等
	 * @param ratingMsg
	 * @return
	 */
	public String directBalance(RatingMsg ratingMsg){
		
	   
		
		return "";
	}
	
	/**
	 * 批价消息处理
	 * @param ratingMsg
	 * @return
	 */
   public String rating(){
	   	//会话消息  
//	   	ratingData.init(ratingMsg);
//		//充值 
//		
//		//释放
//		
//		//获取余额
//		
//		ChargingImpl charge=new ChargingImpl(ratingMsg,ratingData);
//		charge.setMsgParsing((MsgParsingImpl)msgParsing);
//		if(ratingData.getnFeeType()==ParamData.FEECMD_REQ){//只有预占   init消息
//			//return charge.ratingInit();
//		}else if(ratingData.getnFeeType()==ParamData.FEECMD_REALREQ){//实扣+预占
//			charge.ratingUpdate();
//		}else if(ratingData.getnFeeType()==ParamData.FEECMD_REAL ){//只有实扣  terminal消息
//			charge.ratingTerm();
//		}else if(ratingData.getnFeeType()==ParamData.FEECMD_EVENTBACK ){//事件返还
//			charge.ratingEventBack();
//		}
//		
//		
		return "";
	}
	
   
   
}
