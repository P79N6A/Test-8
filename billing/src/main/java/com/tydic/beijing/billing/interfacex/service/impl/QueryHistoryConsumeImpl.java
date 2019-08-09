package com.tydic.beijing.billing.interfacex.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dao.HistoryConsumeData;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RemainBalance;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.BillDto;
import com.tydic.beijing.billing.dto.HistoryConsume;
import com.tydic.beijing.billing.dto.QueryHistoryConsumeInfo;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QueryHistoryConsume;

public class QueryHistoryConsumeImpl implements QueryHistoryConsume{
	
	private static Logger log=Logger.getLogger(QueryHistoryConsumeImpl.class);

	@Override
	public HistoryConsume queryHistoryConsume(QueryHistoryConsumeInfo info) {
		String nbr = info.getMSISDN();

		// add by wangtao begin
		nbr = JDNToNewMsisdn.jdnToNewMsisdn(nbr,BasicType.STARTSTR);
		// add by wangtao end

		 String channle=info.getContactChannle();
		 log.debug("服务[QueryHistoryConsume],号码["+nbr+"],渠道["+channle+"]");
		 DbTool db=new DbTool();
		 HistoryConsume  consume=new HistoryConsume();
		 
		List<UserPayInfo> userInfos = db.queryUserInfoByNbr(nbr);
		if (userInfos == null || userInfos.isEmpty()) {
			log.error("错误码[ZSMART-CC-00003]:业务号码不存在");
			consume.setStatus("0");
			consume.setErrorCode("ZSMART-CC-00003");
			consume.setErrorMessage("业务号码不存在");
			return consume;
		}
		  		
		  		
		 int findPay=0;
		 String payId="";
		 for(UserPayInfo iter : userInfos){
			 	if(iter==null)
			 		continue;
		  		payId=iter.getPay_id();
		  		if(payId !=null && !payId.isEmpty()){
		  			findPay=1;
		  		}
		  		String tag=iter.getDefault_tag();
		  		if(tag!=null && tag.equals("0")){
		  			findPay=2;
		  			break;
		  		}
		 }
		 if(findPay==0){
		  		log.debug("账户不存在");
		  		consume.setStatus("0");
		  		consume.setErrorCode("ZSMART-CC-00016");
		  		consume.setErrorMessage("账户不存在");
	  			return consume;
		 }else if(findPay==1){
		  		log.debug("默认账户不存在");
		  		consume.setStatus("0");
		  		consume.setErrorCode("ZSMART-CC-00016");
		  		consume.setErrorMessage("默认账户不存在");
	  			return consume;
		 }
		  

		 int month=Calendar.getInstance().get(Calendar.MONTH);
		 Calendar cal=Calendar.getInstance();
		 cal.set(Calendar.MONTH, month-6);
		 SimpleDateFormat sdf1 =new SimpleDateFormat("yyyyMM");
		 String startMonth=sdf1.format(cal.getTime());
//		 int startMonth=month-6;
		 String monthnow=sdf1.format(Calendar.getInstance().getTime());
		 List<HistoryConsumeData> data=new ArrayList<HistoryConsumeData>();
		 String userId=userInfos.get(0).getUser_id();
		 log.debug("查询历史账期["+startMonth+"]至["+monthnow+"]");
		 data=db.queryHistoryConsume(payId, userId,startMonth, monthnow);
		 if(data==null || data.isEmpty()){
			 log.debug("service QueryHistoryConsume  no data found !");
			  consume.setStatus("1");
//			  consume.setErrorCode("ZSMART-CC-00032");
			  consume.setErrorMessage("没有查找到记录");
	  		  return consume;
		 }
		 List<BillDto> dtos=new ArrayList<BillDto>();
		 long totalFee=0;
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 for(HistoryConsumeData  d  : data){
			  String effDate=sdf.format(d.getAct_eff_date());
			  String expDate=sdf.format(d.getAct_exp_date());
			  long acctMonth=Long.parseLong(effDate.replace("-", "").substring(0,6));
//			  BillDto  bill=new BillDto(d.getAcct_month(),d.getFee(),effDate,expDate);
			  BillDto  bill=new BillDto(acctMonth,d.getFee(),effDate,expDate);
			  dtos.add(bill);
			  totalFee+=d.getFee();
		 }
		 Collections.sort(dtos,new Comparator<BillDto>(){
			 @Override
			public int compare(BillDto o1, BillDto o2) {
				return (int)(o1.getBillCycleID()-o2.getBillCycleID());
				
			}
		 });
		 consume.setBillDtoList(dtos);
		 consume.setTotalFee(totalFee);
		  //LOG
		  log.debug(consume);
		  for(BillDto  d  :dtos){
			  log.debug(d);
		  }
		return consume;
	}

	
	
	
}
