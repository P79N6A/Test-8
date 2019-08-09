/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.service;

import net.sf.json.JSONObject;

/**
 * B值兑换接口<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public interface ITradeExchange {
	public JSONObject trade(JSONObject in);
}
