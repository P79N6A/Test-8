package com.tydic.beijing.billing.rating.dto;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class SpyMemcachedClient {

	private String str ="";  
	  
    public String getStr() {  
        return str;  
    }  
  
    public void setStr(String str) {  
        this.str = str;  
    }  
      
    public MemcachedClient getMClient() throws IOException{  
            return new MemcachedClient(AddrUtil.getAddresses(str));  
    }  
	    
}
