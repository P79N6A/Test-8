package com.tydic.beijing.billing.iop.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.LifeNumberIOP;
import com.tydic.beijing.billing.dao.LifeNumberIOPLocal;
import com.tydic.beijing.billing.dao.QLifeNumberIOP;
import com.tydic.beijing.billing.iop.service.IOPInterface;

import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class LifeNumberOps implements IOPInterface{
	

	private static Logger log=Logger.getLogger(LifeNumberOps.class);
	
	public List<String> getkeys(){
		List<QLifeNumberIOP> qlist = S.get(QLifeNumberIOP.class).query(
				Condition.build("queryforSync"));
		
		List<String> retList = new ArrayList<String>();
		for(QLifeNumberIOP tmpq:qlist){
			retList.add(tmpq.getUser_id());
		}
		return retList;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public String dealTables(String key) throws Exception {
		
		//根据uesrid查找life_imsi
		List<LifeNumberIOP> lifenumberList = getLifeNumberbyuserid(key);

		//删除本地的life_imsi表记录
		deleteLifeNumberLocal(key);
		//同步到本地数据库的life_imsi表
		insertLifeNumberLocal(lifenumberList);

		//删除q表
		deleteqtable(key);
		
		return  getValue(lifenumberList);
		
	}

	private String getValue(List<LifeNumberIOP> lifenumberList) {

		String retValue ="";
		int i=0;
		for(LifeNumberIOP tmplii :lifenumberList){
			
			//用户ID:生效时间:失效时间:用户号码
			if(i>0){
				retValue = retValue +"|";
			}
			i++;
			retValue = retValue +tmplii.getUser_id()+":"+tmplii.getStart_time()+":"+
			           tmplii.getEnd_time()+":"+tmplii.getDevice_number();
			
			log.debug("当前信息展示Number=============>"+retValue);
		}
		return retValue;
	}

	private void insertLifeNumberLocal(List<LifeNumberIOP> lifenumberList) {
		for(LifeNumberIOP tmpllt :lifenumberList){
			LifeNumberIOPLocal local = new LifeNumberIOPLocal(tmpllt);
			S.get(LifeNumberIOPLocal.class).create(local);
		}
	}

	private void deleteLifeNumberLocal(String user_id) {
		S.get(LifeNumberIOPLocal.class).batch(
				Condition.build("deleteByUserid").filter("user_id", user_id));		
	}

	private List<LifeNumberIOP> getLifeNumberbyuserid(String user_id) {
		// TODO Auto-generated method stub
		return  S.get(LifeNumberIOP.class).query(
				Condition.build("byUserid").filter("user_id", user_id));
	}

	private void deleteqtable(String user_id) {
		S.get(QLifeNumberIOP.class).batch(
				Condition.build("deleteByUserid").filter("user_id", user_id));		
	}

	@Override
	public String getField() {
		// TODO Auto-generated method stub
		return "life_device";
	}


}
