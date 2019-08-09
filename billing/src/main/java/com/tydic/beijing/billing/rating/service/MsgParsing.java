package com.tydic.beijing.billing.rating.service;

import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMsg;

/**
 * 消息解析 用于和统一接入的消息交互，以及相关数据转换等
 * @author Administrator
 *
 */
public interface MsgParsing {

	//由string转换为对象
	public RatingMsg getRatingMsgFromRequestMsg(RatingMsg ratingMsg,String strRequestMsg) throws Exception;
	//返回错误信息
	public String createErrorOutMsg(int errorCode,  String errormsg,String strRequestMsg) ;

	public String createMergeOutMsg(RatingMsg ratingMsg,RatingData ratingData) throws Exception;
 
	

}

	 