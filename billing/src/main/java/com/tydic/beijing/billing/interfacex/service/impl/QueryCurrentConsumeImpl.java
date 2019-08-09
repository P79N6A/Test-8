/**
 * 
 */
package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dao.BalanceConsumeData;
import com.tydic.beijing.billing.dao.BalanceConsumeResource;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.BalanceConsumeDto;
import com.tydic.beijing.billing.dto.CurrentConsume;
import com.tydic.beijing.billing.dto.CurrentConsumeQueryInfo;
import com.tydic.beijing.billing.dto.FeeItemDto;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QueryCurrentConsume;

/**
 * @author sung
 *
 */
public class QueryCurrentConsumeImpl implements QueryCurrentConsume{

	
	private static Logger log = Logger.getLogger(QueryCurrentConsumeImpl.class);
	/**
	 * @author sung
	 *
	 * @param nbr
	 * @param contactChannle
	 */
	@Override
	public CurrentConsume query(CurrentConsumeQueryInfo info) {
		String nbr=info.getMSISDN();
		String channle=info.getContactChannle();
		log.debug("服务[QueryCurrentConsume],号码["+nbr+"],渠道["+channle+"]");
		
		CurrentConsume  consume=new CurrentConsume();
		DbTool db=new DbTool();

		//add by wangtao begin
		nbr = JDNToNewMsisdn.jdnToNewMsisdn(nbr,BasicType.STARTSTR);
		//add by wangtao end

		List<UserPayInfo>  userInfos=db.queryUserInfoByNbr(nbr);
	  	if(userInfos == null || userInfos.isEmpty()){
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
	  	
	  	int month=Calendar.getInstance().get(Calendar.MONTH)+1;

	  	String acctMonth=month>9?""+month:"0"+month; 
	  	String userId=userInfos.get(0).getUser_id();
	  	List<BalanceConsumeData>  bill=db.queryBalanceFeeConsume(payId, userId,acctMonth);
//	  	if(bill==null ||bill.isEmpty()){
//	  			log.debug("没有找到记录");
//	  			consume.setStatus("0");
//	  			consume.setErrorCode("ZSMART-CC-00032");
//	  			consume.setErrorMessage("没有查找到记录");
//	  			return consume;
//	  	}
	  	
	  	long totalFee=0;
	  	List<FeeItemDto> feeItems=new ArrayList<FeeItemDto>();
	  	if(bill !=null && !bill.isEmpty()){
	  		
		  	for(BalanceConsumeData data : bill){
		  			FeeItemDto item=new FeeItemDto(data.getFeeType(),data.getFee());
		  			feeItems.add(item);
		  			totalFee+=data.getFee();
		  	}
		  	
	  	}
	  	consume.setTotalFee(totalFee);
	  	consume.setFeeItemDtoList(feeItems);
	  	List<BalanceConsumeResource> resources=db.queryBalanceConsume(payId);
	  	List<BalanceConsumeDto> balanceConsumeDto=new ArrayList<BalanceConsumeDto>();
	  	Map<Long,BalanceConsumeDto> fees=new HashMap<Long,BalanceConsumeDto>();
	  	if(resources !=null){
	  			for(BalanceConsumeResource  resource : resources){

	  				long balanceType=resource.getPara_char1();
	  				if(fees.get(balanceType)==null){
	  					BalanceConsumeDto dto=new BalanceConsumeDto(balanceType,resource.getFee());
	  					fees.put(balanceType, dto);
	  				}else{
	  					long value=fees.get(balanceType).getConsume();
	  					value+=resource.getFee();
	  					fees.get(balanceType).setConsume(value);
	  				}
//	  				BalanceConsumeDto dto=new BalanceConsumeDto(resource.getPara_char1(),resource.getFee());
//	  				balanceConsumeDto.add(dto);
	  			}
	  	}
	  	Set<Long> keys=fees.keySet();
	  	if(keys !=null && keys.size()!=0){
	  		Iterator<Long> iter=keys.iterator();
		  	while(iter.hasNext()){
		  		long key=iter.next();
		  		BalanceConsumeDto value=fees.get(key);

		  		balanceConsumeDto.add(value);
		  	}
	  	}
	  	
	  	consume.setBalanceConsumeDtoList(balanceConsumeDto);
	  		
	  	//LOG
	  	log.debug(consume);
	  	for(FeeItemDto dto: feeItems){
	  		log.debug(dto);
	  	}
	  	for(BalanceConsumeDto  dto : balanceConsumeDto){
	  		log.debug(dto);
	  	}
		return consume;
	}

	
}
