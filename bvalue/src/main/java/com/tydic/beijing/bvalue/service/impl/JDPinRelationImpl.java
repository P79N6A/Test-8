package com.tydic.beijing.bvalue.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import net.sf.json.JSONObject;

import com.tydic.beijing.bvalue.service.JDPinRelation;
import com.tydic.beijing.bvalue.service.JDPinRelationBiz;

public class JDPinRelationImpl implements JDPinRelation,ApplicationContextAware{

	private ApplicationContext ac;
	
	@Override
	public JSONObject dealRelation(JSONObject request) {
		JDPinRelationBiz  biz=(JDPinRelationBiz)ac.getBean("biz");
		return biz.deal(request);
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ac=applicationContext;
	}
	
	

}
