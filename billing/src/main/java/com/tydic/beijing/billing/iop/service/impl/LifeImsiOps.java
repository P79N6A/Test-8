package com.tydic.beijing.billing.iop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.LifeImsiIOP;
import com.tydic.beijing.billing.dao.LifeImsiIOPLocal;
import com.tydic.beijing.billing.dao.QLifeImsiIOP;
import com.tydic.beijing.billing.iop.service.IOPInterface;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class LifeImsiOps implements IOPInterface {
	
	private static Logger log=Logger.getLogger(LifeImsiOps.class);

	@Override
	public List<String> getkeys() {
	    List<QLifeImsiIOP> qlist= S.get(QLifeImsiIOP.class).query(
					Condition.build("queryforSync"));
	 
	    List<String> retList = new ArrayList<String>();
	    for(QLifeImsiIOP tmpq:qlist){
	    	retList.add(tmpq.getImsi());
	    }
		return retList;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public String dealTables(String key) throws Exception {
		
		//根据imsi查找life_imsi
		List<LifeImsiIOP> lifeimsiList = getLifeImsibyImsi(key);

		//删除本地的life_imsi表记录
		deleteLifeImsiLocal(key);
		//同步到本地数据库的life_imsi表
		insertLifeImsiLocal(lifeimsiList);

		//删除q表
		deleteqtable(key);
		
		return  getValue(lifeimsiList);
		
	}

	private void deleteqtable(String imsi) {
		S.get(QLifeImsiIOP.class).batch(
				Condition.build("deleteByImsi").filter("imsi", imsi));		
	}


	private String getValue(List<LifeImsiIOP> lifeimsiList) {
		
		String retValue ="";
		int i=0;
		for(LifeImsiIOP tmplii :lifeimsiList){
			
			//imsi:生效时间:失效时间:用户id
			if(i>0){
				retValue = retValue +"|";
			}
			i++;
			retValue = retValue +tmplii.getImsi()+":"+tmplii.getStart_time()+":"+
			           tmplii.getEnd_time()+":"+tmplii.getUser_id();
		
			log.debug("当前信息展示Imsi=============>"+retValue);
		}
		return retValue;
	}

	private void insertLifeImsiLocal(List<LifeImsiIOP> lifeimsiList) {

		for(LifeImsiIOP tmpllt :lifeimsiList){
			LifeImsiIOPLocal local = new LifeImsiIOPLocal(tmpllt);
			S.get(LifeImsiIOPLocal.class).create(local);
		}
		
	}

	private void deleteLifeImsiLocal(String imsi) {
		S.get(LifeImsiIOPLocal.class).batch(
				Condition.build("deleteByImsi").filter("imsi", imsi));
	}

	private List<LifeImsiIOP> getLifeImsibyImsi(String imsi) {
		
		return  S.get(LifeImsiIOP.class).query(
				Condition.build("byImsi").filter("imsi", imsi));
	}

	@Override
	public String getField() {
		return "life_imsi";
	}

}
