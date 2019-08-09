package com.tydic.beijing.billing.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;


/**
 * 判断是否以beginStr开头的字符串，
 * 如果是拼接"JDN_"并返回，
 * 如果不是将入参返回出去
 * @author WANGTAO
 *
 */
public class JDNToNewMsisdn {
	
	private static final Logger LOGGER = Logger.getLogger(JDNToNewMsisdn.class);
	/**
	 * @param msisdn  必填，业务号码
	 * @param beginStr  必填，否则会将业务号码赋值为空
	 */
	public static String jdnToNewMsisdn(String msisdn,String beginStr){
		
		if(msisdn==null || msisdn.trim().isEmpty() || beginStr==null || beginStr.trim().isEmpty()){
			return null;
		}
		LOGGER.debug("msisdn:"+msisdn.toString()+",beginStr:"+beginStr.toString());
		
		if(msisdn.startsWith(beginStr)){
			return DigestUtils.md5Hex(("JDN_"+msisdn).getBytes());
		}else{
			return msisdn;
		}
	}
}
