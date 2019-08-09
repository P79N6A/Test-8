package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dao.ValueAddedDual;
import com.tydic.beijing.billing.dao.ValueAddedRemainFee;
import com.tydic.beijing.billing.dao.ValueAddedTradeHistory;
import com.tydic.beijing.billing.dto.ValueAddedChargeRequest;
import com.tydic.beijing.billing.dto.ValueAddedChargeResponse;
import com.tydic.beijing.billing.dto.ValueAddedFileFormat;
import com.tydic.beijing.billing.dto.ValueAddedFileObj;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.ValueAddedCharge;
public class ValueAddedChargeImpl implements ValueAddedCharge{

	private Logger log = Logger.getLogger(ValueAddedChargeImpl.class);
	private ValueAddedFileFormat listFormat;
	private DbTool db;
	private List<String> useTag;
	private List<String> actTag;
	
	@Override
	public ValueAddedChargeResponse doProcess(ValueAddedChargeRequest info) {

		String nbr=info.getMSISDN();
		
		log.debug("服务[ValueAddedCharge],"+info);
		
		ValueAddedChargeResponse status=new ValueAddedChargeResponse();

//		DbTool db=new DbTool();
 		
		List<UserPayInfo>  userInfos=db.queryUserInfoByNbr(nbr);
		
	  	if(userInfos == null || userInfos.isEmpty()){
	  			log.error("错误码[ZSMART-CC-00003]:业务号码不存在");
	  			status.setStatus("0");
	  			status.setErrorCode("ZSMART-CC-00003");
	  			status.setErrorMessage("业务号码不存在");
	  			return status;
	  	}
	  		
	  	String userId=userInfos.get(0).getUser_id();
	  	String localNet=userInfos.get(0).getLocal_net();
	  	
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
	  		status.setStatus("0");
	  		status.setErrorCode("ZSMART-CC-00016");
	  		status.setErrorMessage("账户不存在");
  			return status;
	  	}else if(findPay==1){
	  		log.debug("默认账户不存在");
	  		status.setStatus("0");
	  		status.setErrorCode("ZSMART-CC-00016");
	  		status.setErrorMessage("默认账户不存在");
  			return status;
	  	}
		String feeType=info.getFeeType();
		if(feeType==null || feeType.isEmpty()){
			feeType="-1";
		}
		long chargeFee = info.getValueAddedChargeFee();
		String orderTime=info.getOrderTime().replace(" " , "").replace("-", "").replace(":", "");
		
	  	List<ValueAddedRemainFee> fees=db.queryRealFeeForActItem(payId, feeType);
	  	CodeAcctMonth cam=db.getCurrentCodeAcctMonth(orderTime.substring(0,8));
	  	String aUseTag=cam.getUse_tag();
	  	String aActTag=cam.getAct_tag();
	  	
	  	String partition_no=orderTime.substring(4,6);
	  	int acct_month=cam.getAcct_month();
	  	
	  	if(useTag.indexOf(aUseTag)==-1 || actTag.indexOf(aActTag)==-1){
	  		status.setStatus("0");
	  		status.setErrorCode("ZSMART-CC-00000");
	  		status.setErrorMessage("系统内部错误");
	  		ValueAddedTradeHistory  his=new ValueAddedTradeHistory(info);
		  	his.fillData(status);
		  	his.setError_message("账期错误,useTag["+aUseTag+"],actTag["+aActTag+"]");
		  	
		  	his.setUser_id(userId);
		  	his.setService_scenarious(BasicType.VALUEADDED_SCENARIOUS); 
		  	his.setPay_flag("1");
		  	his.setLocal_net(localNet);
		  	his.setPartition_no(partition_no);
		  	his.setAcct_month(acct_month);
		  	
		  	db.addHistory(his);
	  		return status;
	  		
	  		
	  	}
	  			
	  	
	  	
	  	long realBalance=0;
	  	String tariffInfo="";
	  	long acctitem=0;
//	  	long feetype=0;
	  	
	  	if(fees==null || fees.isEmpty()){
	  		status.setStatus("0");
	  		status.setErrorCode("ZSMART-CC-00221");
	  		status.setErrorMessage("剩余金额不够扣费或无有效账本");
	  		ValueAddedTradeHistory  his=new ValueAddedTradeHistory(info);
		  	his.fillData(status);
		  	his.setError_message("info_pay_balance表无有效记录");
		  	his.setTariff_info(tariffInfo);
		  	his.setRemain_fee(realBalance);
		  	his.setUser_id(userId);
		  	his.setService_scenarious(BasicType.VALUEADDED_SCENARIOUS); 
		  	his.setPay_flag("1");
		  	his.setLocal_net(localNet);
		  	his.setPartition_no(partition_no);
		  	his.setAcct_month(acct_month);
		  	
		  	db.addHistory(his);
	  		return status;
	  	}
	  	for(ValueAddedRemainFee  iter : fees){
	  		
	  		realBalance+=iter.getReal_balance();
	  		long acctItemCode=iter.getAcctItemCode();

	  		if(acctitem!=acctItemCode ){
	  			acctitem=acctItemCode;
	  			tariffInfo=tariffInfo+acctitem+":"+chargeFee+";";
	  		}
	  	}
	  	if(realBalance<chargeFee){
	  		status.setStatus("0");
	  		status.setErrorCode("ZSMART-CC-00221");
	  		status.setErrorMessage("剩余金额不够扣费");
	  		ValueAddedTradeHistory  his=new ValueAddedTradeHistory(info);
		  	his.fillData(status);
		  	his.setTariff_info(tariffInfo);
		  	his.setRemain_fee(realBalance);
		  	his.setUser_id(userId);
		  	his.setService_scenarious(BasicType.VALUEADDED_SCENARIOUS); 
		  	his.setPay_flag("1");
		  	his.setLocal_net(localNet);
		  	his.setPartition_no(partition_no);
		  	his.setAcct_month(acct_month);
		  	
		  	db.addHistory(his);
	  		return status;
	  	}
//	  	System.out.println("fee:"+chargeFee);
//	  	System.out.println("realBalance:"+realBalance);
	  	String serialNo=db.getSerialNumber();
	  	log.debug("serialNo:"+serialNo);
	  	ValueAddedFileObj obj=new ValueAddedFileObj();
	  	obj.fillData(info);
	  	obj.setServID(userId);
	  	obj.setSerialNo(serialNo);
	  	obj.setTariffInfo(tariffInfo);
	  	obj.setPartition_no(partition_no);
	  	
	  	Date now =Calendar.getInstance().getTime();
	  	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	  	String fileDate=sdf.format(now);
	  	String fileName="";
	  	String filePath=db.getFileDir().getPara_char2().replace('/', File.separatorChar);
	  	filePath=filePath.replace('\\', File.separatorChar);
	  	if(filePath.endsWith(""+File.separatorChar)){
	  		filePath=filePath.substring(0,filePath.length()-1);
	  	}
//	  	File file=new File(filePath);
	  	log.debug("清单输出目路:"+filePath);
	  	fileName=getFileName(filePath,fileDate);
	  	log.debug("写入文件:"+fileName);
	  	StringBuffer buffer=new StringBuffer();
	  	
	  	try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(fileName));
			Map<Integer,String> format=listFormat.getListFormat();
			int size=format.size();
			for(int i=0;i<size;++i){
				String value=format.get(i);
				String str=obj.getValue(value);
				buffer.append(str+"|");
			}
			buffer.deleteCharAt(buffer.length()-1);
			buffer.append("\n");
			bw.write(buffer.toString());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  	
	  	ValueAddedTradeHistory  his=new ValueAddedTradeHistory(info);
	  	his.fillData(status);
	  	his.fillData(obj);
	  	his.setRemain_fee(realBalance);
	  	his.setLocal_net(localNet);
	  	his.setPartition_no(partition_no);
	  	his.setAcct_month(acct_month);
	  	
	  	db.addHistory(his);
	  	
	  	QAcctProcess process=new QAcctProcess();
	  	RuleParameters rule=db.getChannelNoMod();
	  	int mod=1;
	  	if(rule!=null && rule.getPara_char2()!=null && !rule.getPara_char2().isEmpty()){
	  		mod=Integer.parseInt(rule.getPara_char2());
	  	}
	  	
	  	int channel =(int)(Long.parseLong(userId)%mod);
	  	process.setChannel_no(channel);
	  	process.setSession_id(info.getSessionid());
	  	process.setCharged_party(info.getMSISDN());
	  	process.setCalling_party(info.getMSISDN());
	  	process.setUser_id(userId);
	  	process.setSession_begin_time(orderTime);
	  	process.setSession_terminated_time(orderTime);
	  	process.setTariff_info(tariffInfo);
	  	process.setProcess_tag(0);

	  	process.setService_scenarious(400);
	  	
	  	
	  	process.setAcct_month(acct_month);
	  	
	  	db.addQAcctProcess(process);
	  	
	  	
		return status;
	}


	
	public String getFileName(String path,String date){
		File file=new File(path);
		if(!file.exists() && !file.isDirectory()){
			if(!file.mkdir()){
				log.debug("创建目路失败!");
				
			}
			log.debug("新建目路:"+path);
		}
		String fileName="";
		ValueAddedDual dual=db.getFileSerialNumber();
		String serialNumber=dual.getId();
		serialNumber=getFileSerial(serialNumber);
		fileName=path+File.separatorChar+BasicType.FILEHEADER+date+serialNumber+".dat";
		return fileName;
	}
	
	public String getFileSerial(String serialNumber){
		String serial=serialNumber;
		int length=serialNumber.length();
		while(length<BasicType.FILE_SERIAL_LENGTH){
			serial="0"+serial;
			length=serial.length();
		}
		return serial;
	}
	
	public void setListFormat(ValueAddedFileFormat listFormat) {
		this.listFormat = listFormat;
	}
	
	public void setDb(DbTool db) {
		this.db = db;
	}
	
	public void setActTag(List<String> actTag) {
		this.actTag = actTag;
	}
	public void setUseTag(List<String> useTag) {
		this.useTag = useTag;
	}
	
	
	
}
