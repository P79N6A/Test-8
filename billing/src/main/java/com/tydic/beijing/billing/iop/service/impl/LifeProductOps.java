package com.tydic.beijing.billing.iop.service.impl;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.LifeProductIOP;
import com.tydic.beijing.billing.dao.LifeProductIOPLocal;
import com.tydic.beijing.billing.dao.QLifeProductIOP;
import com.tydic.beijing.billing.iop.service.IOPInterface;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class LifeProductOps implements IOPInterface{
	
//	private static final Logger log = LoggerFactory.getLogger(LifeProductOps.class);
	private static Logger log=Logger.getLogger(LifeProductOps.class);
	public List<String> getkeys(){
		List<QLifeProductIOP> qlist = S.get(QLifeProductIOP.class).query(
				Condition.build("queryforSync"));
		
		List<String> retList = new ArrayList<String>();
		for(QLifeProductIOP tmpq:qlist){
			retList.add(tmpq.getUser_id());
		}
		return retList;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public String dealTables(String key) throws Exception {
		
		//根据user_id查找
		List<LifeProductIOP> lifeproductList = getLifeProductbyUserid(key);
		
		//删除本地原有的life_product_local表
		deleteLifeProductLocal(key);
		
		//同步到本地数据库的life_product表
		insertLifeProductLocal(lifeproductList);
		
		//删除q表
		deleteqtable(key);
		
		return getValue(lifeproductList);
	}


    //在本地数据库中备份life_product_local表

	private void insertLifeProductLocal(List<LifeProductIOP> lifeproductList) {
		
		for(LifeProductIOP tmplpi:lifeproductList){
			LifeProductIOPLocal local = new LifeProductIOPLocal(tmplpi);
			S.get(LifeProductIOPLocal.class).create(local);
		}
	}

	private void deleteqtable(String user_id) {
		S.get(QLifeProductIOP.class).batch(
				Condition.build("deleteByUserid").filter("user_id", user_id));	
		
	}

    //返回值拼装
	private String getValue(List<LifeProductIOP> lifeproductList) {
		// TODO Auto-generated method stub
		String retValue = "";
		int i=0;
		for(LifeProductIOP tmplpi:lifeproductList){
			
			//用户ID:生效时间:失效时间:产品代码
			if(i>0){
				retValue = retValue + "|";
			}
			i++;
			
			retValue = retValue + tmplpi.getUser_id()+":"+tmplpi.getStart_time()+":"+
			tmplpi.getEnd_time()+":"+tmplpi.getProduct_id();
			
			log.debug("当前信息展示Product=============>"+retValue);
		}
		return retValue;
	}


    //删除该值原有数据
	private void deleteLifeProductLocal(String user_id) {
		// TODO Auto-generated method stub
		S.get(LifeProductIOPLocal.class).batch(
				Condition.build("deleteByUserid").filter("user_id",user_id));
	}

    //通过user_id从life_product中取值
	private List<LifeProductIOP> getLifeProductbyUserid(String user_id) {
		// TODO Auto-generated method stub
		
		//获取当月的前两个月的1号
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat month = new SimpleDateFormat("yyyyMMdd");
		    cal.add(Calendar.MONTH, -2);
		    cal.set(Calendar.DAY_OF_MONTH, 1);
			String data = month.format(cal.getTime());
			log.debug("获取的失效比较日期=============>"+data);
		
		
		return S.get(LifeProductIOP.class).query(
				Condition.build("byUserid").filter("user_id",user_id).filter("data", data));
		
		
	}

	@Override
	public String getField() {
		// TODO Auto-generated method stub
		return "life_product";
	}

	
	
	
	
}
