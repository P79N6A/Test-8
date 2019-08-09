package com.tydic.beijing.billing.interfacex.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.dao.*;
import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dto.CDRDto;
import com.tydic.beijing.billing.dto.QuerySubsCDRInfo;
import com.tydic.beijing.billing.dto.SubsCDR;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QuerySubsCDR;

public class QuerySubsCDRImpl implements QuerySubsCDR{

	private static Logger log=Logger.getLogger(QuerySubsCDRImpl.class);
	
	private static final String FLAG_STARTDATE = "START";
	private static final String FLAG_ENDDATE = "END";
	
	private static Map<String,String> cityCodeMap = new HashMap<String,String>();
	private List<String> lists;
	private String ftpUrl;
	
	
//	@Override
//	public SubsCDR query(QuerySubsCDRInfo info) {
//		
//		SubsCDR cdr=new SubsCDR();
//		DbTool db=new DbTool();
//		
//		List<RuleParameters> ruleParametersList = new ArrayList<RuleParameters>();// 用于漫游等参数转换 zhb add
//		List<CodeActAcctItem> codeActAcctItemList = new ArrayList<CodeActAcctItem>(); //用于转换费用，资源类账本的返回金额都置成0
//		
//		String serviceType=info.getServiceType();
//		if(serviceType==null || serviceType.isEmpty()){
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00119");
//			cdr.setErrorMessage("业务类型不能为空");
//			return cdr;
//		}
//		String nbr=info.getMSISDN();
//		if(nbr==null || nbr.isEmpty()){
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00001");
//			cdr.setErrorMessage("业务号码不能为空");
//			return cdr;
//		}
//		String channle=info.getContactChannle();
//		long billCycle=info.getBillCycleID();
//		if(billCycle==0 ||(""+billCycle).length()<6 ){
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00123");
//			cdr.setErrorMessage("账期标识错误");
//			return cdr;
//		}
//		String startTime=info.getStartTime();
//		String endTime=info.getEndTime();
//		if(startTime==null){
//			startTime = assembleDate(billCycle, FLAG_STARTDATE);
//			if (startTime == null) {
//				cdr.setStatus("0");
//				cdr.setErrorCode("ZSMART-CC-00123");
//				cdr.setErrorMessage("账期标识错误，日期非法");
//				return cdr;
//			}
//		}
//		if(endTime==null){
//			endTime = assembleDate(billCycle, FLAG_ENDDATE);
//			if (endTime == null) {
//				cdr.setStatus("0");
//				cdr.setErrorCode("ZSMART-CC-00123");
//				cdr.setErrorMessage("账期标识错误，日期非法");
//				return cdr;
//			}
//		}
//		long pageIndex=info.getPageIndex();
//		if(pageIndex==0){
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00120");
//			cdr.setErrorMessage("页码不能为空");
//			return cdr;
//		}
//		long row=info.getRowPerPage();
//		if(row==0){
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00121");
//			cdr.setErrorMessage("每页记录数不能为空或者为零");
//			return cdr;
//		}
//		log.debug("服务[QuerySubsCDR],号码["+nbr+"],业务类型["+serviceType+"],开始日期["+startTime+"],结束日期["+endTime+"],页码["+pageIndex+"],页大小["+row+"]");
//		
//		String tableType="";
//		if(serviceType.equals("1")){
//			tableType="100";
//		}else if(serviceType.equals("2")){
//			tableType="200";
//		}else if(serviceType.equals("3")){
//			tableType="300";
////		}else if(serviceType.equals("4")){
////			tableType="400";
//		}else{
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00122");
//			cdr.setErrorMessage("业务类型错误");
//			return cdr;
//		}
//		
////		List<UserPayInfo> upi=db.queryUserInfoByNbr(nbr);
////		String userId=upi.get(0).getUser_id();
//		
//		
//		
//		List<InfoUser> infoUserList=  db.getUserInfoByDeviceNumber(nbr);
//		
//		if(infoUserList == null || infoUserList.size() !=1){
//			//没有找到用户
//			cdr.setStatus("0");
//			cdr.setErrorCode("ZSMART-CC-00003");
//			cdr.setErrorMessage("没有找到用户信息");
//			return cdr;
//		}
//		
//		String userId=infoUserList.get(0).getUser_id();
////		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
////		String startDate=sdf.format(startTime);
////  		String endDate=sdf.format(endTime);
//		
//		String startDate=startTime.replace("-", "").replace(" ", "").replace(":", "");
//		String endDate=endTime.replace("-", "").replace(" ", "").replace(":", "");
//		if(startDate.length()>8){
//			startDate=startDate.substring(0,8)+"000000";
//		}else{
//			startDate+="000000";
//		}
//		
//		if(endDate.length()>8){
//			endDate=endDate.substring(0,8)+"235959";
//		}else{
//			endDate+="235959";
//		}
//		long monthsys=Calendar.getInstance().get(Calendar.MONTH)+1;
//		log.debug("当前月:"+monthsys);
//		
//  		long cycle=billCycle%100;
//  		String month=cycle<10?"0"+cycle:""+cycle;
//  		String sysMonth=monthsys<10?"0"+monthsys:""+monthsys;
//  		boolean  hisMonth=false;
//  		long count=0;
////  		if(cycle<monthsys){
////  			count=db.queryHisCDRCallingCount(tableType, userId, month, sysMonth, startDate, endDate);
////  			count=seq.getSeq();
////  			hisMonth=true;
////  			log.debug(">>>往月详单查询");
////  		}else{
//  			count=db.queryCDRCallingCount(tableType,userId,month,startDate,endDate);
////  		}
//  		
//        if(count==0){
//			cdr.setStatus("1");
////			cdr.setErrorCode("ZSMART-CC-00032");
//			cdr.setErrorMessage("没有查询到记录");
//			return cdr;
//		}
//        log.debug("记录数共:"+count);
//        
//		if(pageIndex==1){
//			
//			cdr.setPageCount((long)Math.ceil((double)count/row));
//			
//		}
////		log.debug("pageCount:"+cdr.getPageCount());
//		
//	  	List<CDRCalling> callingCDRs=new ArrayList<CDRCalling>();
//	  	List<CDRSms>  sms=new ArrayList<CDRSms>();
//	  	List<CDRNet>  nets=new ArrayList<CDRNet>();
//	  	
//	  	List<CDRDto> cdrDtos=new ArrayList<CDRDto>();
//	  	SimpleDateFormat dateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	  	long startRow=(pageIndex-1)*row+1;
//	  	long endRow=pageIndex*row;
//	  	
//	  	///先获取所有的参数配置 start zhb
//	  	
//	  	ruleParametersList = DbTool.getRuleParametersofCdrConfig("5020","QuerySubsCdr");
//	  	log.debug("详单查询获取参数转换参数配置数量："+ruleParametersList.size());     
//	  	
//	  	codeActAcctItemList = DbTool.getAllCodeActAcctItem();
//	  	
//	  	//获取所有的参数配置 end zhb
//	  	
//	  	if(serviceType.equals("1")){//语音业务
////	  		if(hisMonth){
////	  			callingCDRs=db.queryHisCDRCalling(tableType, userId, month, sysMonth, startDate, endDate, startRow, endRow);
////	  		}else{
//	  			callingCDRs=db.queryCDRCalling(tableType,userId,month, startDate, endDate,startRow,endRow);
////	  		}
//	  		long payFlag=-1;
//	  		for(CDRCalling iter : callingCDRs){
//	  			
//	  			String calledNumber="";
//	  			payFlag=iter.getPayFlag();
//	  			if(payFlag==0){	//主叫
//
//	  				calledNumber=iter.getCalledparty();
//	  			}else if(payFlag==1){	//被叫
//
//	  				calledNumber=iter.getCallingparty();
//	  			}else {
//
//	  				calledNumber="";
//	  			}
//	  			CDRDto dto=new CDRDto();
//	  			String call=iter.getCall_type();
//	  			if(call==null){
//	  				call="";
//	  			}
//	  			
//	  			//callType编码需要转换
//	  			String terminalCallType = getValueFromCdrConfig(serviceType,"CallType",call,ruleParametersList);
//	  			
//	  			dto.setCallType(terminalCallType);
//	  			
//	  			dto.setCalledNumber(calledNumber);
//	  			dto.setServiceType(serviceType);
//	  			dto.setStartTime(dateTimeFormat.format(iter.getStartTime()));
//	  			dto.setDuration(iter.getCallduration());
//	  			String roam=iter.getRoamingType();
////	  			if(roam !=null && !roam.equals("5")){
////	  				roam="0";
////	  			}else if(roam !=null && roam.equals("5")){
////	  				roam="1";
////	  			}
//	  			
//	  			//漫游类型改为从数据库读配置
//	  			String terminalRoam = getValueFromCdrConfig(serviceType,"RoamType",roam,ruleParametersList);
//	  			dto.setRomaType(terminalRoam);
//	  			String fee=iter.getTariffinfo();
//	  			String[] fees=fee.split(";");
//	  			long charge=0;
//	  			for(String str:fees){
//	  			//	long tmpfee = Long.parseLong(str.split(":")[1]);
//	  				long tmpfee = getTerminalFee(codeActAcctItemList,str);
//	  				charge+= tmpfee;
//	  				
//	  			}
//	  			dto.setCharge(charge);
//	  			cdrDtos.add(dto);
//	  			
//	  		}
//	  		cdr.setCDRDtoList(cdrDtos);
//	  		
//	  	}else if(serviceType.equals("2")){   //上网
//	  		
////	  		if(hisMonth){
////	  			nets=db.queryHisCDRNet(tableType, userId, month, sysMonth, startDate, endDate, startRow, endRow);
////	  		}else{
//	  			nets=db.queryCDRNet(tableType, userId, month,startDate, endDate, startRow, endRow);
////	  		}
//	  		
//	  		
//	  		for(CDRNet   net : nets){
//	  			
//	  			CDRDto dto=new CDRDto();
//	  			double trafic=Double.parseDouble(net.getTotal_flow())/BasicType.SUBCDR_NET_UNIT;
//	  			
//	  			dto.setDataTrafic(trafic>Math.round(trafic)? Math.round(trafic)+1:Math.round(trafic));//不足1K算1K
//	  			dto.setStartTime(dateTimeFormat.format(net.getStartTime()));
//	  			String roam=net.getRoamingtype();
////	  			if(roam !=null && !roam.equals("5")){
////	  				roam="0";
////	  			}else if(roam !=null && roam.equals("5")){
////	  				roam="1";
////	  			}
//	  			
//	  		//漫游类型改为从数据库读配置
//	  			String terminalRoam = getValueFromCdrConfig(serviceType,"RoamType",roam,ruleParametersList);
//	  			
//	  			dto.setRomaType(terminalRoam);
//	  			String fee=net.getTariffinfo();
//	  			String[] fees=fee.split(";");
//	  			long charge=0;
//	  			for(String str:fees){
//	  				//charge+=Long.parseLong(str.split(":")[1]);
//	  				long tmpfee = getTerminalFee(codeActAcctItemList,str);
//	  				charge += tmpfee;
//	  				
//	  			}
//	  			dto.setCharge(charge);
//	  			dto.setServiceType(serviceType);
//	  			cdrDtos.add(dto);
//	  		
//	  		}
//	  		cdr.setCDRDtoList(cdrDtos);
//	  		
//	  	}else if(serviceType.equals("3")){	//短信
////	  		if(hisMonth){
////	  			sms=db.queryHisCDRSms(tableType, userId, month, sysMonth, startDate, endDate, startRow, endRow);
////	  		}else{
//	  			sms=db.queryCDRSms(tableType, userId, month,startDate, endDate, startRow, endRow);
////	  		}
//	  		long payflag=-1;
//	  		for(CDRSms   s : sms){
//	  			String calledNumber="";
//	  			payflag=s.getPayFlag();
//	  			if(payflag==0){
//	  				calledNumber=s.getCalledparty();
//	  			}else if(payflag==1){
//	  				calledNumber=s.getCallingparty();
//	  			}
//	  			
//	  			CDRDto dto=new CDRDto();
//	  			String call=s.getCall_type();
//	  			if(call==null){
//	  				call="";
//	  			}
//	  			
//	  		//callType编码需要转换
//	  			String terminalCallType = getValueFromCdrConfig(serviceType,"CallType",call,ruleParametersList);
//	  			
//	  			dto.setCallType(terminalCallType);
//	  			dto.setCalledNumber(calledNumber);
//	  			dto.setServiceType(serviceType);
//	  			dto.setStartTime(dateTimeFormat.format(s.getStartTime()));
//	  			String roam=s.getRoamingtype();
////	  			if(roam !=null && !roam.equals("5")){
////	  				roam="0";
////	  			}else if(roam !=null && roam.equals("5")){
////	  				roam="1";
////	  			}
//	  			
//	  		//漫游类型改为从数据库读配置
//	  			String terminalRoam = getValueFromCdrConfig(serviceType,"RoamType",roam,ruleParametersList);
//	  			
//	  			dto.setRomaType(terminalRoam);
//	  			String fee=s.getTariffinfo();
//	  			String[] fees=fee.split(";");
//	  			long charge=0;
//	  			for(String str:fees){
//	  				//charge+=Long.parseLong(str.split(":")[1]);
//	  				long tmpfee = getTerminalFee(codeActAcctItemList,str);
//	  				charge += tmpfee;
//	  			}
//	  			dto.setCharge(charge);
//	  			cdrDtos.add(dto);
//	  		}
//	  		cdr.setCDRDtoList(cdrDtos);
//	  	}
//	  	
//	  	
//		return cdr;
//		
//	}

	//获取最终的金额，过滤掉资源类账本额
	private long getTerminalFee(List<CodeActAcctItem> codeActAcctItemList,
			String str) {
		
		String[] infos = str.split(":");
		int acctItemCode = Integer.parseInt(infos[0]);
		long value = Long.parseLong(infos[1]);
		
		for(CodeActAcctItem tmpcaai:codeActAcctItemList){
			if(tmpcaai.getAcct_item_code() == acctItemCode && tmpcaai.getUnit_type_id() !=0){///0的是金钱，非0的是资源账本
				value =0;
				break;
			}
		}
		
		
		return value;
	}

	private String getValueFromCdrConfig(String char1, String char2,
			String char3,List<RuleParameters> configList) {
       
		String retValue="";
        for(RuleParameters tmprp:configList){
        	if(tmprp.getPara_char1().equals(char1) && tmprp.getPara_char2().equals(char2) && tmprp.getPara_char3().equals(char3)){
        		retValue = tmprp.getPara_char4();
        		break;
        	}
        }
		
		return retValue;
	}

	private String assembleDate(long billingCycleId, String flag) {
		String billingCycle = Long.toString(billingCycleId);
		String year = billingCycle.substring(0, 4);
		String month = billingCycle.substring(4, 6);
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
		
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDate.parse(year + "/" + month));
		} catch (ParseException e) {
			log.error("BillingCycleId[" + billingCycle + "] is Invalid!");
			return null;
		}
		int day = 0;
		if (flag.equals(FLAG_STARTDATE)) {
			day = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		} else if (flag.equals(FLAG_ENDDATE)) {
			day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return (year + "-" + month + "-" + (day < 10 ? "0" + day: day));
	}

	@Override
	public SubsCDR query(QuerySubsCDRInfo info) {
		
		SubsCDR cdr=new SubsCDR();
		
		
		DbTool db=new DbTool();
		
		List<RuleParameters> ruleParametersList = new ArrayList<RuleParameters>();// 用于漫游等参数转换 zhb add
		List<CodeActAcctItem> codeActAcctItemList = new ArrayList<CodeActAcctItem>(); //用于转换费用，资源类账本的返回金额都置成0
		
		String serviceType=info.getServiceType();
		if(serviceType==null || serviceType.isEmpty()){
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00119");
			cdr.setErrorMessage("业务类型不能为空");
			return cdr;
		}
		String queryType="1";
		
		String queryCallType=info.getQueryCallType();
		
		String callingNbr ="";
		String calledNbr="";
		
		if(queryCallType == null || queryCallType.isEmpty()){
			queryType="1";	//计费号码
			//add by wangtao begin
			callingNbr=info.getQueryNumber();
			callingNbr = JDNToNewMsisdn.jdnToNewMsisdn(callingNbr,BasicType.STARTSTR);
			//add by wangtao end
		}else if(queryCallType.equals("1")){
			queryType="2"; 	// 计费 + 主叫
		}else if(queryCallType.equals("2")){
			queryType="3";	// 计费 + 被叫
		}else{
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00119");
			cdr.setErrorMessage("查询方式取值错误："+queryCallType);
			return cdr;
		}
		
		if(queryType.equals("2")){
			callingNbr=info.getQueryNumber();
			//add by wangtao begin
			callingNbr = JDNToNewMsisdn.jdnToNewMsisdn(callingNbr,BasicType.STARTSTR);
			//add by wangtao end
			if(callingNbr == null || callingNbr.isEmpty()){
				cdr.setStatus("0");
				cdr.setErrorCode("ZSMART-CC-00119");
				cdr.setErrorMessage("查询方式:"+queryCallType+",缺少主叫号码");
				return cdr;
			}
		}
		
		if(queryType.equals("3")){
			calledNbr=info.getQueryNumber();
			//add by wangtao begin
			calledNbr = JDNToNewMsisdn.jdnToNewMsisdn(calledNbr,BasicType.STARTSTR);
			//add by wangtao end
			if(calledNbr == null || calledNbr.isEmpty()){
				cdr.setStatus("0");
				cdr.setErrorCode("ZSMART-CC-00119");
				cdr.setErrorMessage("查询方式:"+queryCallType+",缺少被叫号码");
				return cdr;
			}
		}
		
		
//		if(calledNbr!=null && !calledNbr.isEmpty()){
//			queryType="2";
//		}
		
		String nbr=info.getMSISDN();
		
		if(nbr==null || nbr.isEmpty()){
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00001");
			cdr.setErrorMessage("业务号码不能为空");
			return cdr;
		}
		//add by wangtao begin
		nbr = JDNToNewMsisdn.jdnToNewMsisdn(nbr,BasicType.STARTSTR);
		//add by wangtao end
		
		long billCycle=info.getBillCycleID();
		if(billCycle==0 ||(""+billCycle).length()<6 ){
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00123");
			cdr.setErrorMessage("账期标识错误");
			
			return cdr;
		}
		String startTime=info.getStartTime();
		String endTime=info.getEndTime();
		if(startTime==null){
			startTime = assembleDate(billCycle, FLAG_STARTDATE);
			if (startTime == null) {
				cdr.setStatus("0");
				cdr.setErrorCode("ZSMART-CC-00123");
				cdr.setErrorMessage("账期标识错误，日期非法");
				
				return cdr;
			}
		}
		if(endTime==null){
			endTime = assembleDate(billCycle, FLAG_ENDDATE);
			if (endTime == null) {
				cdr.setStatus("0");
				cdr.setErrorCode("ZSMART-CC-00123");
				cdr.setErrorMessage("账期标识错误，日期非法");
				
				return cdr;
			}
		}
		long pageIndex=info.getPageIndex();
		if(pageIndex==0){
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00120");
			cdr.setErrorMessage("页码不能为空");
			
			return cdr;
		}
		long row=info.getRowPerPage();
		if(row==0){
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00121");
			cdr.setErrorMessage("每页记录数不能为空或者为零");
			
			return cdr;
		}
		log.debug("服务[QuerySubsCDR],号码["+nbr+"],业务类型["+serviceType+"],开始日期["+startTime+"],结束日期["+endTime+"],页码["+pageIndex+"],页大小["+row+"]"
				+"查询方式["+queryCallType+"],主/被叫号码["+info.getQueryNumber()+"]");
		
		
		String tableType="";
		if(serviceType.equals("1")){
			tableType="100";
		}else if(serviceType.equals("2")){
			tableType="200";
		}else if(serviceType.equals("3")){
			tableType="300";
//		}else if(serviceType.equals("4")){
//			tableType="400";
		}else{
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00122");
			cdr.setErrorMessage("业务类型错误");
			
			return cdr;
		}
		
		
		List<InfoUser> infoUserList=  db.getUserInfoByDeviceNumber(nbr);
		
		if(infoUserList == null || infoUserList.size() !=1){
			//没有找到用户
			cdr.setStatus("0");
			cdr.setErrorCode("ZSMART-CC-00003");
			cdr.setErrorMessage("没有找到用户信息");
			
			return cdr;
		}
		
		String userId=infoUserList.get(0).getUser_id();

		String productId=infoUserList.get(0).getMain_ofr_id();

		boolean isCarHomeProduct=checkProduct(productId);

		
		String startDate=startTime.replace("-", "").replace(" ", "").replace(":", "");
		String endDate=endTime.replace("-", "").replace(" ", "").replace(":", "");
		if(startDate.length()>8){
			startDate=startDate.substring(0,8)+"000000";
		}else{
			startDate+="000000";
		}
		
		if(endDate.length()>8){
			endDate=endDate.substring(0,8)+"235959";
		}else{
			endDate+="235959";
		}
//		long monthsys=Calendar.getInstance().get(Calendar.MONTH)+1;
//		log.debug("当前月:"+monthsys);
		
  		long cycle=billCycle%100;
  		String month=cycle<10?"0"+cycle:""+cycle;
  		
		
	  	List<CDRCalling> callingCDRs=new ArrayList<CDRCalling>();
	  	List<CDRSms>  sms=new ArrayList<CDRSms>();
	  	List<CDRNet>  nets=new ArrayList<CDRNet>();
	  	
	  	List<CDRDto> cdrDtos=new ArrayList<CDRDto>();
	  	SimpleDateFormat dateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	long startRow=(pageIndex-1)*row+1;
	  	long endRow=pageIndex*row;
	  	
	  	///先获取所有的参数配置 start zhb
	  	
	  	ruleParametersList = DbTool.getRuleParametersofCdrConfig("5020","QuerySubsCdr");
	  	log.debug("详单查询获取参数转换参数配置数量："+ruleParametersList.size());     
	  	
	  	codeActAcctItemList = DbTool.getAllCodeActAcctItem();
	  	
	  	//获取所有的参数配置 end zhb
	  	
	  	if(cityCodeMap.size()==0){
	  		cityCodeMap = DbTool.initCityCode();
	  	}
	  	if(serviceType.equals("1")){//语音业务
	  				    
			
	  		if(queryType.equals("1")){ // 计费号码
	  			
	  			List<VoiceCdr> totalTariff=db.getTotalTariffInfo(userId,tableType,month ,startDate , endDate );
		  		
	  		  	long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  		  	
	  		  	int	count=(int)db.queryCDRCallingCount(tableType,userId,month,startDate,endDate);
	  		    log.debug("count>>>>"+count);
				if(count==0){
					cdr.setStatus("1");
					cdr.setErrorMessage("没有查询到记录");
					
					return cdr;
				}
				cdr.setTotalRecord(count);
				
				
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)count/row));
	  				
	  			}
	  		  	
	  			log.debug("查询日期范围内详单量："+count);  	
	  			callingCDRs=db.queryCDRCalling(tableType,userId,month, startDate, endDate,startRow,endRow);
	  			
	  			
	  		}else if(queryType.equals("3")){  //被叫
	  			
	  			List<VoiceCdr> totalTariff=db.getTotalTariffInfoWithCalledNbr(userId,tableType,month ,startDate , endDate ,calledNbr);
	  			long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  			
	  			int queryCount=(int)db.queryCdrCountWithCalledNbr(tableType, userId, month, startDate, endDate, calledNbr);
	  			
	  			if(queryCount==0){
	  				cdr.setStatus("1");
	  				cdr.setErrorMessage("没有查询到记录");
	  				
	  				return cdr;
	  			}
	  			
	  			cdr.setTotalRecord(queryCount);
	  			
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)queryCount/row));
	  				
	  			}
	  			log.debug("查询日期范围内详单量："+queryCount);  
	  			callingCDRs=db.queryCDRCallingWithCalledNbr(tableType,userId,month,startDate,endDate,startRow,endRow,calledNbr);
	  			
	  		}else{	//主叫  
	  			
	  			List<VoiceCdr> totalTariff=db.getTotalTariffInfoWithCallingNbr(userId,tableType,month ,startDate , endDate ,callingNbr);
	  			long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  		  	
	  		  	
	  			QueryLong result = db.queryCdrCountWithCallingNbr(tableType, userId, month, startDate, endDate, callingNbr);
	  			
	  			if(result == null || result.getResult() ==0){
	  				cdr.setStatus("1");
	  				cdr.setErrorMessage("没有查询到记录");
	  				
	  				return cdr;
	  			}
	  			long queryCount=result.getResult();
	  			
	  			cdr.setTotalRecord(queryCount);
	  			
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)queryCount/row));
	  				
	  			}
	  			
	  			log.debug("查询日期范围内详单量："+queryCount);  
	  			
	  			callingCDRs=db.queryCDRCallingWithCallingNbr(tableType,userId,month,startDate,endDate,startRow,endRow,callingNbr);
	  		}
	  		long payFlag=-1;
	  		for(CDRCalling iter : callingCDRs){
	  			
	  			String calledNumber="";
	  			payFlag=iter.getPayFlag();
	  			if(payFlag==0){	//主叫

	  				calledNumber=iter.getCalledparty();
	  			}else if(payFlag==1){	//被叫

	  				calledNumber=iter.getCallingparty();
	  			}else {

	  				calledNumber="";
	  			}
	  			CDRDto dto=new CDRDto();
	  			String call=iter.getCall_type();
	  			if(call==null){
	  				call="";
	  			}
	  			
	  			//callType编码需要转换
	  			String terminalCallType = getValueFromCdrConfig(serviceType,"CallType",call,ruleParametersList);
	  			
	  			dto.setCallType(terminalCallType);
	  			
	  			dto.setCalledNumber(calledNumber);
	  			dto.setServiceType(serviceType);
	  			dto.setStartTime(dateTimeFormat.format(iter.getStartTime()));
	  			dto.setDuration(iter.getCallduration());
	  			String roam=iter.getRoamingType();

	  			dto.setLongDistanceType(iter.getLongdistancetype());
	  			dto.setVisitedCity(cityCodeMap.get(iter.getCallingpartyvisitedcity()));
	  			
	  			//漫游类型改为从数据库读配置
	  			String terminalRoam = getValueFromCdrConfig(serviceType,"RoamType",roam,ruleParametersList);
	  			dto.setRomaType(terminalRoam);
	  			String fee=iter.getTariffinfo();
	  			String[] fees=fee.split(";");
	  			long charge=0;
	  			for(String str:fees){
	  			//	long tmpfee = Long.parseLong(str.split(":")[1]);
	  				long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  				charge+= tmpfee;
	  				
	  			}
	  			dto.setCharge(charge);
				if(isCarHomeProduct){
					if(iter.getCallId()!=null && !"".equals(iter.getCallId())){
						String callId=iter.getCallId();
						dto.setCallId(callId);
						log.debug("call==="+callId);
						PrivnumMessReceiveHis receiveHis=db.getLoadUrl(month,callId);
						if(receiveHis!=null){
							if(receiveHis.getVideoFileName()!=null && !"".equals(receiveHis.getVideoFileName())){
								dto.setRecordUrl(ftpUrl+receiveHis.getVideoFileName());
							}
						}
					}
				}
	  			cdrDtos.add(dto);
	  			
	  		}
	  		cdr.setCDRDtoList(cdrDtos);

	  		
	  	}else if(serviceType.equals("2")){   //上网
	  		
	  		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
  		  	String begin="";
  		  	String end="";
  		  	try {
				Date date=sdf.parse(startDate);
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
				begin=sdf.format(cal.getTime());
				cal.clear();
				cal.setTime(date);
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				end=sdf.format(cal.getTime()).substring(0,8)+"235959";
			} catch (ParseException e) {
				log.debug("date parse error !");
			}
  		  	log.debug("to query totalRecord > begin :"+begin +",end : "+end);
  		  	
  		  	
//  		  	long totalRows=db.queryCDRCallingCount(tableType, userId, month, startDate, endDate);
	  		
	  		
	  			int	count=(int)db.queryCDRCallingCount(tableType,userId,month,startDate,endDate);
	  			if(count==0){
	  				cdr.setStatus("1");
	  				cdr.setErrorMessage("没有查询到记录");
	  				
	  				return cdr;
	  			}
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)count/row));
	  				
	  			}
	  			
	  			log.debug("查询日期范围内详单总量："+count);
	  		  	List<VoiceCdr> totalTariff=db.getTotalTariffInfo(userId,tableType,month ,startDate, endDate);
	  		  	long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  		  	cdr.setTotalRecord(count);
	  			
	  			NetCdr dataFlow=db.getTotalDataFlow(userId, tableType, month ,startDate ,endDate );
	  			
	  			cdr.setTotalFlow(dataFlow.getTotalDataFlow());
	  			
	  			nets=db.queryCDRNet(tableType, userId, month,startDate, endDate, startRow, endRow);
	  		
	  		
	  		
	  		for(CDRNet   net : nets){	
	  			
	  			CDRDto dto=new CDRDto();
	  			double trafic=Double.parseDouble(net.getTotal_flow())/BasicType.SUBCDR_NET_UNIT;
	  			
	  			dto.setDataTrafic(trafic>Math.round(trafic)? Math.round(trafic)+1:Math.round(trafic));//不足1K算1K
	  			dto.setStartTime(dateTimeFormat.format(net.getStartTime()));
	  			String roam=net.getRoamingtype();
	  			
	  			
	  		//漫游类型改为从数据库读配置
	  			String terminalRoam = getValueFromCdrConfig(serviceType,"RoamType",roam,ruleParametersList);
	  			
	  			dto.setRomaType(terminalRoam);
	  			String fee=net.getTariffinfo();
	  			String[] fees=fee.split(";");
	  			long charge=0;
	  			for(String str:fees){
	  				//charge+=Long.parseLong(str.split(":")[1]);
	  				long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  				charge += tmpfee;
	  				
	  			}
	  			dto.setCharge(charge);
	  			dto.setServiceType(serviceType);
	  			cdrDtos.add(dto);
	  			
	  		}
	  		
	  		cdr.setCDRDtoList(cdrDtos);
	  		
	  		
	  		
	  	}else if(serviceType.equals("3")){	//短信
	  		
			
	  		if(queryType.equals("1")){	//计费号
	  			
	  			List<VoiceCdr> totalTariff=db.getTotalTariffInfo(userId,tableType,month ,startDate ,endDate);
	  		  	long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  		  	
	  			int	count=(int)db.queryCDRCallingCount(tableType,userId,month,startDate,endDate);
				if(count==0){
					cdr.setStatus("1");
					cdr.setErrorMessage("没有查询到记录");
					
					return cdr;
				}
				
				cdr.setTotalRecord(count);
				
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)count/row));
	  				
	  			}
	  			
	  			log.debug("查询日期范围内详单总量："+count);
	  			sms=db.queryCDRSms(tableType, userId, month,startDate, endDate, startRow, endRow);
	  			
	  		}else if(queryType.equals("3")){	//被叫
	  			
	  			List<VoiceCdr> totalTariff = db.getTotalTariffInfoWithCalledNbr(userId ,tableType,month,startDate,endDate,calledNbr);
	  			long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  		  	
	  		  	
	  			int queryCount=(int)db.queryCdrCountWithCalledNbr(tableType, userId, month, startDate, endDate, calledNbr);
	  			if(queryCount==0){
	  				cdr.setStatus("1");
	  				cdr.setErrorMessage("没有查询到记录");
	  				
	  				return cdr;
	  			}
	  			cdr.setTotalRecord(queryCount);
	  			
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)queryCount/row));
	  				
	  			}
	  			
	  			log.debug("查询日期范围内详单总量："+queryCount);
	  			sms=db.queryCDRSmsWithCalledNbr(tableType, userId, month,startDate, endDate, startRow, endRow ,calledNbr);
	  			
	  		}else{	//主叫
	  			
	  			List<VoiceCdr> totalTariff = db.getTotalTariffInfoWithCallingNbr(userId ,tableType,month,startDate,endDate,callingNbr);
	  			long tariffFee=0;
	  		  	for(VoiceCdr iter : totalTariff){
	  		  		String fee=iter.getTariffinfo();
	  		  		String[] fees=fee.split(";");
	  		  		for(String str:fees){
	  			
	  		  			long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  		  			tariffFee+= tmpfee;
	  				
	  		  		}
	  		  	}
	  		  	cdr.setTotalCharge(tariffFee);
	  		  	
	  		  	
	  			QueryLong result = db.queryCdrCountWithCallingNbr(tableType, userId, month, startDate, endDate, callingNbr);
	  			
	  			if(result == null || result.getResult() ==0){
	  				
	  				cdr.setStatus("1");
	  				cdr.setErrorMessage("没有查询到记录");
	  				
	  				return cdr;
	  			}
	  			long queryCount=result.getResult();
	  			
	  			cdr.setTotalRecord(queryCount);
	  			
	  			if(pageIndex==1){
	  				
	  				cdr.setPageCount((long)Math.ceil((double)queryCount/row));
	  				
	  			}
	  			
	  			
	  			sms=db.queryCDRSmsWithCallingNbr(tableType, userId, month,startDate, endDate, startRow, endRow ,callingNbr);
	  		}
	  		long payflag=-1;
	  		for(CDRSms   s : sms){
	  			String calledNumber="";
	  			payflag=s.getPayFlag();
	  			if(payflag==0){
	  				calledNumber=s.getCalledparty();
	  			}else if(payflag==1){
	  				calledNumber=s.getCallingparty();
	  			}
	  			
	  			CDRDto dto=new CDRDto();
	  			String call=s.getCall_type();
	  			if(call==null){
	  				call="";
	  			}
	  			
	  		//callType编码需要转换
	  			String terminalCallType = getValueFromCdrConfig(serviceType,"CallType",call,ruleParametersList);
	  			
	  			dto.setCallType(terminalCallType);
	  			dto.setCalledNumber(calledNumber);
	  			dto.setServiceType(serviceType);
	  			dto.setStartTime(dateTimeFormat.format(s.getStartTime()));
	  			String roam=s.getRoamingtype();

	  			
	  		//漫游类型改为从数据库读配置
	  			String terminalRoam = getValueFromCdrConfig(serviceType,"RoamType",roam,ruleParametersList);
	  			
	  			dto.setRomaType(terminalRoam);
	  			String fee=s.getTariffinfo();
	  			String[] fees=fee.split(";");
	  			long charge=0;
	  			for(String str:fees){
	  				//charge+=Long.parseLong(str.split(":")[1]);
	  				long tmpfee = getTerminalFee(codeActAcctItemList,str);
	  				charge += tmpfee;
	  			}
	  			dto.setCharge(charge);
	  			cdrDtos.add(dto);
	  		}
	  		cdr.setCDRDtoList(cdrDtos);
	  		
	  		
	  	}
	  	
		return cdr;
		
	}

	private boolean checkProduct(String productId) {
		if(lists.contains(productId)){
			return true;
		}
		return false;
	}

	public List<String> getLists() {
		return lists;
	}

	public void setLists(List<String> lists) {
		this.lists = lists;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}
}
