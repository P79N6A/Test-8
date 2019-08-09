package com.tydic.beijing.billing.rating.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHelper implements ApplicationContextAware{

	private static ApplicationContext appCtx; 
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		appCtx = arg0; 
		
	}
	  /** 
     * 这是一个便利的方法，帮助我们快速得到一个BEAN 
     * @param beanName bean的名字 
     * @return 返回一个bean对象 
     */  
    public static Object getBean( String beanName ) {  
        return appCtx.getBean( beanName );  
    }  
}
