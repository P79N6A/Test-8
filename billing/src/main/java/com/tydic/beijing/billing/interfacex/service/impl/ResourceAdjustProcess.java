package com.tydic.beijing.billing.interfacex.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.dao.BalanceAdjustLog;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.ResourceAdjustRequest;
import com.tydic.beijing.billing.dto.ResourceAdjustResponse;
import com.tydic.beijing.billing.dto.ResourceDto;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
//import com.tydic.beijing.billing.util.SftpClient;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class ResourceAdjustProcess {

	
	private static Logger log = Logger.getLogger(ResourceAdjustProcess.class);

//	private String serverAddress;  //ftp地址
//	private int serverPort;
//	private String filePath;        //上传到ftp的路径
//	private String user;           //ftp用户名
//	private String pwd;            //ftp密码
//	private String localPathforBack ; //本地备份地址
//	private String split;
	
	/**
	 * @author zhanghengbo
	 * 接口说明参考svn-资源调整接口设计文档.doc
	 */
	
//	@Transactional(rollbackFor=Exception.class)
//	public   ResourceAdjustResponse dealFtp(ResourceAdjustRequest rar)  throws Exception{
//        
//		log.debug("ftp地址-------------->"+serverAddress);
//	
//		ResourceAdjustResponse resourceAdjustResponse = new ResourceAdjustResponse();
//		DbTool dbTool = new DbTool();
//		String payId ="";
//		
//		FileWriter writer = null;  
//		FTPClient ftpclient = new FTPClient(); 
//		//FileOutputStream fos =null; 
//		
//		
//	try{
//		String  deviceNumber = rar.getMSISDN();
//		log.debug("获取到输入号码："+deviceNumber);
//		List<UserPayInfo> userPayInfoList = dbTool.queryUserInfoByNbr(deviceNumber);
//		log.debug("根据输入号码：["+deviceNumber+"]获取到userpayinfo数量"+userPayInfoList.size());
//		
//		//获取用户默认的账户，也就是default_tag=0的
//		UserPayInfo userPayInfo = null;
//		for(UserPayInfo tmpupi:userPayInfoList){
//			if(tmpupi.getDefault_tag().equals("0")){
//				userPayInfo = tmpupi;
//			}
//		}
//		
//		if(userPayInfo == null){//没有找到用户默认账户
//			resourceAdjustResponse.setStatus("0");
//			resourceAdjustResponse.setErrorCode("ZSMART-CC-00017");
//			resourceAdjustResponse.setErrorMessage("账户不能为空");
//			
//			return resourceAdjustResponse;
//		}
//		
//		payId = userPayInfo.getPay_id();
//	
//		 List<ResourceDto> resourceDtoList = rar.getResourceDtoList();
//		log.debug("本次请求涉及资源调整数量"+resourceDtoList.size());
//		for(ResourceDto tmprd:resourceDtoList){
//			//遍历传入的资源调整明细
//			String resourceType =  tmprd.getResourceType();
//			int resourceNumber = tmprd.getResourceNumber();
//			 
//			List<RuleParameters> ruleParametersList = dbTool.queryRuleParameters(5000, "ResourceAdjust", resourceType);
//			
//			
//			if(ruleParametersList == null || ruleParametersList.size()== 0 ){
//				//如果没有找到资源编码映射，则返回错误
//				resourceAdjustResponse.setStatus("0");
//				resourceAdjustResponse.setErrorCode("ZSMART-CC-00021");
//				resourceAdjustResponse.setErrorMessage("没有找到"+resourceType+"对应的资源账本类型编码");
//				
//				return resourceAdjustResponse;
//			}
//			
//			RuleParameters ruleParameters =  ruleParametersList.get(0);
//			int balanceTypeId = Integer.parseInt(ruleParameters.getPara_char2());
//			
//			if(resourceNumber > 0){//如果是调增
//				
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//				java.sql.Date  effDate =  new java.sql.Date(sdf.parse(tmprd.getEffDate()).getTime());
//				java.sql.Date  expDate =  new java.sql.Date(sdf.parse(tmprd.getExpDate()).getTime());
//				
//				List<InfoPayBalance> infoPayBalanceList = dbTool.queryInfoPayBalancebyPayIdandBalanceType(payId, balanceTypeId);
//				InfoPayBalance infoPayBalance = null;
//				for(InfoPayBalance tmpipb:infoPayBalanceList){
//					
//					if( tmpipb.getEff_date().compareTo(effDate) <=0  && tmpipb.getExp_date().compareTo(expDate)==0
//							){//如果开始时间<=报文中的EffDate，并且结束时间=报文中的ExpDate
//						infoPayBalance = tmpipb;
//						break;
//					}
//				}
//				
//				long balanceIdforLog =0L;
//				long valueforLog =0L;
//				long oldBalanceforLog =0L;////经和田甜确认，这里是用的balance
//				long newBalanceforLog =0L;
//				
//				if(infoPayBalance ==null){//没有找到合适的账本
//					balanceIdforLog =  getBalanceId();  //如果没有找到合适的账本，就新获取一个balanceid
//					oldBalanceforLog = 0L;
//					if(resourceType.equals("3")){///k
//						valueforLog = resourceNumber;
//						newBalanceforLog = oldBalanceforLog+valueforLog;
//					} else if (resourceType.equals("1")){//分钟
//						valueforLog = resourceNumber;
//						newBalanceforLog = oldBalanceforLog+valueforLog;
//					} else{
//						resourceAdjustResponse.setStatus("0");
//						resourceAdjustResponse.setErrorCode("ZSMART-CC-00021");
//						resourceAdjustResponse.setErrorMessage("没有找到"+resourceType+"对应的资源账本类型编码");
//						
//						return resourceAdjustResponse;
//					}
//					
//					
//					infoPayBalance = new InfoPayBalance();
//					infoPayBalance.setPay_id(payId);
//					infoPayBalance.setBalance_id(balanceIdforLog);
//					infoPayBalance.setBalance_type_id(balanceTypeId);
//					infoPayBalance.setBalance(newBalanceforLog);
//					infoPayBalance.setReal_balance(newBalanceforLog);
//					infoPayBalance.setLatn_id(954);//这个没用吧
//					infoPayBalance.setEff_date(effDate);
//					infoPayBalance.setExp_date(expDate);
//					
//					dbTool.createInfoPayBalance(infoPayBalance);
//	
//				}else{//找到了合适的账本
//					balanceIdforLog = infoPayBalance.getBalance_id();//如果找到合适的账本，日志表就记录已存在的balanceid
//					oldBalanceforLog = infoPayBalance.getBalance();
//					if(resourceType.equals("3")){///k
//						valueforLog = resourceNumber;
//						newBalanceforLog = oldBalanceforLog+valueforLog;
//					} else if (resourceType.equals("1")){//分钟
//						valueforLog = resourceNumber;
//						newBalanceforLog = oldBalanceforLog+valueforLog;
//					} else{
//						resourceAdjustResponse.setStatus("0");
//						resourceAdjustResponse.setErrorCode("ZSMART-CC-00021");
//						resourceAdjustResponse.setErrorMessage("没有找到"+resourceType+"对应的资源账本类型编码");
//						
//						return resourceAdjustResponse;
//					}
//
//					infoPayBalance.setBalance(newBalanceforLog);
//					infoPayBalance.setReal_balance(infoPayBalance.getReal_balance()+valueforLog);
//
//					dbTool.updateInfoPayBalance(infoPayBalance);
//					
//				}
//				
//				//writelog start
//				
//				BalanceAdjustLog balanceAdjustLog = new BalanceAdjustLog();
//				balanceAdjustLog.setSession_id(rar.getSessionId());
//				balanceAdjustLog.setJdpin(rar.getJdPin());
//				balanceAdjustLog.setMsisdn(rar.getMSISDN());
//				balanceAdjustLog.setChannel_no(rar.getChannelNo());
//				balanceAdjustLog.setRequest_time(new java.sql.Date(sdf.parse(rar.getRequestTime()).getTime()));
//				balanceAdjustLog.setResource_type(resourceType);
//				balanceAdjustLog.setResource_number(resourceNumber);
//				balanceAdjustLog.setEff_date(effDate);
//				balanceAdjustLog.setExp_date(expDate);
//				dbTool.createBalanceAdjustLog(balanceAdjustLog);
//				
//				Calendar cal = Calendar.getInstance();
//			    int month = cal.get(Calendar.MONTH) + 1;
//			    log.debug("获取到当前月份："+month);
//			    
//				BilActAccesslog bilActAccesslog = new BilActAccesslog();
//				bilActAccesslog.setOperate_id(rar.getSessionId());
//				bilActAccesslog.setOperate_type("8");
//				bilActAccesslog.setPartition_id(month);
//				bilActAccesslog.setPay_id(payId);
//				bilActAccesslog.setBalance_id(balanceIdforLog);
//				bilActAccesslog.setBalance_type_id(balanceTypeId);
//				bilActAccesslog.setAccess_tag("0");
//				bilActAccesslog.setMoney(valueforLog);
//				bilActAccesslog.setOld_balance(oldBalanceforLog);
//				bilActAccesslog.setNew_balance(newBalanceforLog);
//				bilActAccesslog.setLocal_net(userPayInfo.getLocal_net());
//				//bilActAccesslog.setOperate_time(); 默认sysdate
//				dbTool.createBilActAccessLog(bilActAccesslog);
//				
//				
//				//writelog end
//	
//			}else if(resourceNumber < 0){//如果是调减,就生成一条话单文件，放到服务器指定地址
//				
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
////				java.sql.Date  effDate =  new java.sql.Date(sdf.parse(tmprd.getEffDate()).getTime());
////				java.sql.Date  expDate =  new java.sql.Date(sdf.parse(tmprd.getExpDate()).getTime());
//				
//				
//				//记录账本调整记录
//				BalanceAdjustLog balanceAdjustLog = new BalanceAdjustLog();
//				balanceAdjustLog.setSession_id(rar.getSessionId());
//				balanceAdjustLog.setJdpin(rar.getJdPin());
//				balanceAdjustLog.setMsisdn(rar.getMSISDN());
//				balanceAdjustLog.setChannel_no(rar.getChannelNo());
//				balanceAdjustLog.setRequest_time(new java.sql.Date(sdf.parse(rar.getRequestTime()).getTime()));
//				balanceAdjustLog.setResource_type(resourceType);
//				balanceAdjustLog.setResource_number(resourceNumber);
////				balanceAdjustLog.setEff_date();
////				balanceAdjustLog.setExp_date();
//				dbTool.createBalanceAdjustLog(balanceAdjustLog);
//				
//				//写话单文件 文件格式参照 VOP-Interface-Specification-File-Interface-Volume-0.5.7.pdf
//				
//				if(!ftpclient.isConnected()){
//					//ftpclient.connect(this.serverAddress); 
//					ftpclient.connect(this.serverAddress, this.serverPort);
//					ftpclient.login(this.user, this.pwd); 
//					ftpclient.changeWorkingDirectory(this.filePath); // /home/billing_dev/ua/data1/in
//				}
//
//				String businessType="";
//				String fileDetail ="";
//				SimpleDateFormat tmpsdf = new SimpleDateFormat("yyyyMMddHHmmss");
//				
//				if(resourceType.equals("1")){//语音 分钟
//					businessType="YY";
//					
//					//拼话单 
//					  fileDetail ="2001"+ //话单类型标识
//					               ",123456789"+//文件ID 随便起的
//							       ",00"+//主叫呼出话单
//					               ","+userPayInfo.getLocal_net()+ //主叫归属区号
//							       ","+userPayInfo.getDevice_number()+//主叫号码
//							       ","+userPayInfo.getLocal_net()+//被叫归属区号
//							       ","+userPayInfo.getDevice_number()+//被叫号码
//							       ","+//连接号码
//							       ","+tmpsdf.format(new java.util.Date())+//起始时间
//							       ","+resourceNumber*60*-1+ //使用量 因为resourceNumber<0，所以*-1 ua接收的话单单位是秒，所以要*60
//							       ","+ //被计费移动号码imsi
//							       ","+ //被计费移动设备imei
//							       ","+userPayInfo.getLocal_net()+ //国内漫游地区号
//							       ","+//国际漫游所在运营公司编码
//							       ",0"+ //漫游类型 非漫游
//							       ","+ //标准费用
//							       ",0"+ //长途类型  非长途
//							       ","+//前转话单类型
//							       ","+//预留字段1
//							       ","+//预留字段2
//							       ","+ //预留字段3
//							       ","+ //预留字段4
//							       ", "  //预留字段5
//							       ; 
//					
//				}else if (resourceType.equals("3")) {//数据 kb
//					
//					businessType="SJ";
//					fileDetail ="2003"+ //话单类型标识
//					            ",123456789"+ //文件ID 随便起的
//							    ",1"+//话单类型
//					            ","+userPayInfo.getDevice_number()+//计费号码
//							    ","+tmpsdf.format(new java.util.Date())+//起始时间
//							    ","+tmpsdf.format(new java.util.Date())+//终止时间
//							    ",0"+//上行流量
//							    ","+resourceNumber*1024*-1+ //下行流量 ua接收的话单单位是byte，所以要*1024
//							    ","+ //手机用户的imsi
//							    ","+ //手机设备的imei
//							    ","+userPayInfo.getLocal_net()+ //归属区号
//							    ","+userPayInfo.getLocal_net()+ //漫游省份编码
//							    ","+//国际漫游费用
//							    ","+ //业务代码
//							    ",3gnet"+ //apn的网络标识
//							    ","+ //记录使用的ggsn plmn标识
//							    ",0"+ //漫游类型
//							    ","+//预留字段1
//							    ","+//预留字段2
//							    ","+ //预留字段3
//							    ","+ //预留字段4
//							    ", "  //预留字段5
//							    ;
//				}
//				
//				
//				long fileseq =1000000000L;
//				String preFileName="JIDO"+"010"+"0000"+businessType+"00" //userPayInfo.getLocal_net() 暂用011代替
//			                      +tmpsdf.format(new java.util.Date());
//				String sufFileName=".dat";
//
//				//log.debug("ftp连接成功了？"+ftpclient.isConnected());
//				FTPFile[] fileList = ftpclient.listFiles();
//				//log.debug(this.filePath+"下的文件数量"+fileList.length);
//				String fileName="";
//
//				while(true){
//					fileName=preFileName+fileseq+sufFileName;
//					boolean existFlag = false;
//					for(FTPFile tmpfile:fileList){
//						if(tmpfile.getName().equals(fileName)){
//							//如果该文件名已经存在了，就退出for循环重新生成一个新的文件名
//							existFlag = true;
//							break;
//						}
//					}
//					
//					if(existFlag){//如果文件已经存在
//						log.debug("文件"+fileName+"已存在");
//						fileseq++;
//					}else{//如果该文件不存在，则文件名可用
//						log.debug("文件"+fileName+"不存在，可以使用");
//					   break;	
//					}
//					
//				}
//
//				log.debug("获取到最终的文件名："+fileName);
//				log.debug("生成的话单"+fileDetail);
//				
//				//确认ResourceAdjust文件夹是否已存在，如果不存在，创建一个
//				
//				File localdirFile = new File(localPathforBack);
//				if(!localdirFile.exists()){
//					localdirFile.mkdir();
//				}
//				
////				File bakFile = new File(localPathforBack+"/"+fileName);
////				if(!bakFile.exists()){
////					bakFile.createNewFile();
////				}
//				
//				 
//		        // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
//		        writer = new FileWriter(localPathforBack+"/"+fileName, true);     
//		        writer.write(fileDetail+"\r\n");   
//		        writer.flush();
//		        
//		        FileInputStream  fis = new FileInputStream(localPathforBack+"/"+fileName); 
//		        ftpclient.storeFile(fileName, fis);
//		     
//				log.debug("上传话单文件结束");
//				
//				
//				
//				
//			}
//			
//			
//			
//		}
//		
//		resourceAdjustResponse.setStatus("1");
//		resourceAdjustResponse.setErrorCode("");
//		resourceAdjustResponse.setErrorMessage("");
//		
//		return resourceAdjustResponse;
//		
//	}catch (Exception e){
//		e.printStackTrace();
//		throw e;
//	} finally{
//		 try {     
//             if(writer != null){  
//                 writer.close();     
//             }  
//             
//             ftpclient.disconnect();
//         } catch (IOException e) {     
//             e.printStackTrace();     
//         } 
//		 
//		 
//		
//	}
//		
//}
	

	/**
	 * sftp
	 * @param rar
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public   ResourceAdjustResponse dealSftp(ResourceAdjustRequest rar)  throws Exception{
        
	//	log.debug("ftp地址-------------->"+serverAddress);
	
		ResourceAdjustResponse resourceAdjustResponse = new ResourceAdjustResponse();
		DbTool dbTool = new DbTool();
		String payId ="";
		
		log.debug("获得资源调整staffid"+rar.getStaffId());
		log.debug("获得资源调整时间requesttime"+rar.getRequestTime());
		
		
//		SftpClient sftpClient = new SftpClient();
//		sftpClient.setHost(this.serverAddress);
//		sftpClient.setPort(this.serverPort);
//		sftpClient.setUsername(this.user);
//		sftpClient.setPassword(this.pwd);
		
	try{
		
		//增加判断sessionid是否已经存在了
		List<BalanceAdjustLog> listBalanceAdjustLog = dbTool.getBalanceAdjustLogBySessionIdAndChannelNo(rar.getSessionId(),rar.getChannelNo());
		if(listBalanceAdjustLog !=null && listBalanceAdjustLog.size()>0){
			
			log.debug("getrequesttime="+listBalanceAdjustLog.get(0).getRequest_time());
			
			resourceAdjustResponse.setStatus("0");
			resourceAdjustResponse.setErrorCode("ZSMART-CC-00000");
			resourceAdjustResponse.setErrorMessage("该sessionid已进行过调整，不能重复");
			
			return resourceAdjustResponse;
		}
		 
		String  deviceNumber = rar.getMSISDN();
		log.debug("获取到输入号码："+deviceNumber);
		List<UserPayInfo> userPayInfoList = dbTool.queryUserInfoByNbr(deviceNumber);
		log.debug("根据输入号码：["+deviceNumber+"]获取到userpayinfo数量"+userPayInfoList.size());
		
		//获取用户默认的账户，也就是default_tag=0的
		UserPayInfo userPayInfo = null;
		for(UserPayInfo tmpupi:userPayInfoList){
			if(tmpupi.getDefault_tag().equals("0")){
				userPayInfo = tmpupi;
			}
		}
		
		if(userPayInfo == null){//没有找到用户默认账户
			resourceAdjustResponse.setStatus("0");
			resourceAdjustResponse.setErrorCode("ZSMART-CC-00017");
			resourceAdjustResponse.setErrorMessage("账户不能为空");
			
			return resourceAdjustResponse;
		}
		
		payId = userPayInfo.getPay_id();
	
		 List<ResourceDto> resourceDtoList = rar.getResourceDtoList();
		log.debug("本次请求涉及资源调整数量"+resourceDtoList.size());
		for(ResourceDto tmprd:resourceDtoList){
			//遍历传入的资源调整明细
			String resourceType =  tmprd.getResourceType();
			int resourceNumber = tmprd.getResourceNumber();
			 
			List<RuleParameters> ruleParametersList = dbTool.queryRuleParameters(5000, "ResourceAdjust", resourceType);
			
			
			if(ruleParametersList == null || ruleParametersList.size()== 0 ){
				//如果没有找到资源编码映射，则返回错误
				resourceAdjustResponse.setStatus("0");
				resourceAdjustResponse.setErrorCode("ZSMART-CC-00021");
				resourceAdjustResponse.setErrorMessage("没有找到"+resourceType+"对应的资源账本类型编码");
				
				return resourceAdjustResponse;
			}
			
			RuleParameters ruleParameters =  ruleParametersList.get(0);
			int balanceTypeId = Integer.parseInt(ruleParameters.getPara_char2());
			
			if(resourceNumber > 0){//如果是调增
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //yyyy-MM-dd HH-mm-ss  生效时间判断也改为判断年月日
				SimpleDateFormat expsdf = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date  effDate =  new java.sql.Date(sdf.parse(tmprd.getEffDate()).getTime());
				java.sql.Date  expDate =  new java.sql.Date(expsdf.parse(tmprd.getExpDate()).getTime());
				
				if(expsdf.parse(tmprd.getExpDate()).getTime() <= new Date().getTime()){
					//如果赠送的资源失效时间早于当前时间，则认为是无效的，返回错误
					resourceAdjustResponse.setStatus("0");
					resourceAdjustResponse.setErrorCode("ZSMART-CC-00039");
					resourceAdjustResponse.setErrorMessage("失效时间早于当前时间，异常");
					
					return resourceAdjustResponse;
				}
					
				
				List<InfoPayBalance> infoPayBalanceList = dbTool.queryInfoPayBalancebyPayIdandBalanceType(payId, balanceTypeId);
				InfoPayBalance infoPayBalance = null;
				for(InfoPayBalance tmpipb:infoPayBalanceList){
					
					if( tmpipb.getEff_date().compareTo(effDate) ==0  && tmpipb.getExp_date().compareTo(expDate)==0
							){//如果开始时间=报文中的EffDate，并且结束时间=报文中的ExpDate
						infoPayBalance = tmpipb;
						break;
					}
				}
				
				long balanceIdforLog =0L;
				long valueforLog =0L;
				long oldBalanceforLog =0L;////经和田甜确认，这里是用的balance
				long newBalanceforLog =0L;
				
				if(infoPayBalance ==null){//没有找到合适的账本
					balanceIdforLog =  getBalanceId();  //如果没有找到合适的账本，就新获取一个balanceid
					oldBalanceforLog = 0L;
					if(resourceType.equals("3")){///k
						valueforLog = resourceNumber;
						newBalanceforLog = oldBalanceforLog+valueforLog;
					} else if (resourceType.equals("1")){//分钟
						valueforLog = resourceNumber;
						newBalanceforLog = oldBalanceforLog+valueforLog;
					}  else if (resourceType.equals("2")){//条
						valueforLog = resourceNumber;
						newBalanceforLog = oldBalanceforLog+valueforLog;
					}else if (resourceType.equals("4")){//分钟（长市话）
						valueforLog = resourceNumber;
						newBalanceforLog = oldBalanceforLog+valueforLog;
					} else{
						resourceAdjustResponse.setStatus("0");
						resourceAdjustResponse.setErrorCode("ZSMART-CC-00021");
						resourceAdjustResponse.setErrorMessage("没有找到"+resourceType+"对应的资源账本类型编码");
						
						return resourceAdjustResponse;
					}
					
					
					infoPayBalance = new InfoPayBalance();
					infoPayBalance.setPay_id(payId);
					infoPayBalance.setBalance_id(balanceIdforLog);
					infoPayBalance.setBalance_type_id(balanceTypeId);
					infoPayBalance.setBalance(newBalanceforLog);
					infoPayBalance.setReal_balance(newBalanceforLog);
					infoPayBalance.setLatn_id(954);//这个没用吧
					infoPayBalance.setEff_date(effDate);
					infoPayBalance.setExp_date(expDate);
					
					dbTool.createInfoPayBalance(infoPayBalance);
	
				}else{//找到了合适的账本
					balanceIdforLog = infoPayBalance.getBalance_id();//如果找到合适的账本，日志表就记录已存在的balanceid
					oldBalanceforLog = infoPayBalance.getBalance();
					if(resourceType.equals("3") || resourceType.equals("2") || resourceType.equals("1") || resourceType.equals("4") ){
						valueforLog = resourceNumber;
						newBalanceforLog = oldBalanceforLog+valueforLog;
					}  else{
						resourceAdjustResponse.setStatus("0");
						resourceAdjustResponse.setErrorCode("ZSMART-CC-00021");
						resourceAdjustResponse.setErrorMessage("没有找到"+resourceType+"对应的资源账本类型编码");
						
						return resourceAdjustResponse;
					}

//					infoPayBalance.setBalance(newBalanceforLog);
//					infoPayBalance.setReal_balance(infoPayBalance.getReal_balance()+valueforLog);
					
					infoPayBalance.setBalance(valueforLog);
					infoPayBalance.setReal_balance(valueforLog);

					dbTool.updateInfoPayBalance(infoPayBalance);
					
				}
				
				//writelog start
				
				BalanceAdjustLog balanceAdjustLog = new BalanceAdjustLog();
				balanceAdjustLog.setSession_id(rar.getSessionId());
				balanceAdjustLog.setJdpin(rar.getJdPin());
				balanceAdjustLog.setMsisdn(rar.getMSISDN());
				balanceAdjustLog.setChannel_no(rar.getChannelNo());
				balanceAdjustLog.setRequest_time(rar.getRequestTime());   //sdf.parse(rar.getRequestTime()) 
				balanceAdjustLog.setResource_type(resourceType);
				balanceAdjustLog.setResource_number(resourceNumber);
				balanceAdjustLog.setEff_date(effDate);
				balanceAdjustLog.setExp_date(expDate);
				balanceAdjustLog.setChange_time(new Date());
				balanceAdjustLog.setUser_id(userPayInfo.getUser_id());  //增加userid
				balanceAdjustLog.setStaff_id(rar.getStaffId() == null?"":rar.getStaffId());
				balanceAdjustLog.setReal_deduct_info("调整了10B");
				balanceAdjustLog.setActivity_type(rar.getActivityType());
				balanceAdjustLog.setReason(rar.getReason());
				dbTool.createBalanceAdjustLog(balanceAdjustLog);
				
				Calendar cal = Calendar.getInstance();
			    int month = cal.get(Calendar.MONTH) + 1;
			    log.debug("获取到当前月份："+month);
			    
				BilActAccesslog bilActAccesslog = new BilActAccesslog();
				bilActAccesslog.setOperate_id(rar.getSessionId());
				bilActAccesslog.setOperate_type("8");
				bilActAccesslog.setPartition_id(month);
				bilActAccesslog.setPay_id(payId);
				bilActAccesslog.setBalance_id(balanceIdforLog);
				bilActAccesslog.setBalance_type_id(balanceTypeId);
				bilActAccesslog.setAccess_tag("0");
				bilActAccesslog.setMoney(valueforLog);
				bilActAccesslog.setOld_balance(oldBalanceforLog);
				bilActAccesslog.setNew_balance(newBalanceforLog);
				bilActAccesslog.setLocal_net(userPayInfo.getLocal_net());
				//bilActAccesslog.setOperate_time(); 默认sysdate
				dbTool.createBilActAccessLog(bilActAccesslog);

				//writelog end
	
			}else if(resourceNumber < 0){//如果是调减,改为直接扣减账本
				
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

				//先查到用户当前都有哪些符合该资源的账本是有效或者未来生效的，倒序  balanceTypeId
				List<InfoPayBalance> infoPayBalanceList = dbTool.getResourceBalanceForAdjust(payId,balanceTypeId);

				//依次扣减
				String deductInfo ="";
				long resnum = (long) resourceNumber*-1;
				
				for(InfoPayBalance tmpipb:infoPayBalanceList){
					long todeduct =0L;
					long oldRealBalance = tmpipb.getReal_balance() ;
				    if(tmpipb.getReal_balance() >= resnum ){	
				    	todeduct = resnum;
				    	resnum = 0L;
				    }else{
				    	todeduct = tmpipb.getReal_balance() ;
				    	resnum = resnum - tmpipb.getReal_balance();
				    	
				    }
				    
				    log.debug("sleep start...zzZZ..");
				   // Thread.sleep(20000);
				    
				    //dbTool.deductResourceBalanceForAdjust(tmpipb.getBalance_id(),todeduct);
				    tmpipb.setReal_balance(-1*todeduct);
				    tmpipb.setBalance(-1*todeduct);
				    dbTool.updateInfoPayBalance(tmpipb);
				    
				    ///直接扣减账本可能扣成负，这里要增加判断，如果扣成了负，返回异常，重新再走一次
				    
				    InfoPayBalance  resultIPB = dbTool.getResourceBalanceForCheck(tmpipb.getBalance_id()+"");
				    if(resultIPB.getReal_balance() <0){
				    	throw new BasicException(1001,"realbalance is minus!");
				    }
				    
				    
				    
			    	deductInfo = deductInfo + tmpipb.getBalance_id()+":"+ todeduct +";";
			    	
			    	Calendar cal = Calendar.getInstance();
				    int month = cal.get(Calendar.MONTH) + 1;
				    log.debug("获取到当前月份："+month);
				    
					BilActAccesslog bilActAccesslog = new BilActAccesslog();
					bilActAccesslog.setOperate_id(rar.getSessionId());
					bilActAccesslog.setOperate_type("8");
					bilActAccesslog.setPartition_id(month);
					bilActAccesslog.setPay_id(payId);
					bilActAccesslog.setBalance_id(tmpipb.getBalance_id());
					bilActAccesslog.setBalance_type_id(balanceTypeId);
					bilActAccesslog.setAccess_tag("1");          //1扣减  0调增
					bilActAccesslog.setMoney(todeduct);
					bilActAccesslog.setOld_balance(oldRealBalance);
					bilActAccesslog.setNew_balance(oldRealBalance - todeduct);
					bilActAccesslog.setLocal_net(userPayInfo.getLocal_net());
					//bilActAccesslog.setOperate_time(); 默认sysdate
					dbTool.createBilActAccessLog(bilActAccesslog);
					
					
					
					
			    	if(resnum <=0 ){
			    		break;
			    	}

				}
				
				if(resnum >0){
					log.debug("not deduct completely!!,use for free"+resnum);	
				}
			
				//记录账本调整记录
				BalanceAdjustLog balanceAdjustLog = new BalanceAdjustLog();
				balanceAdjustLog.setSession_id(rar.getSessionId());
				balanceAdjustLog.setJdpin(rar.getJdPin());
				balanceAdjustLog.setMsisdn(rar.getMSISDN());
				balanceAdjustLog.setChannel_no(rar.getChannelNo());
				balanceAdjustLog.setRequest_time(rar.getRequestTime());
				balanceAdjustLog.setResource_type(resourceType);
				balanceAdjustLog.setResource_number(resourceNumber);
//				balanceAdjustLog.setEff_date();
//				balanceAdjustLog.setExp_date();
				balanceAdjustLog.setUser_id(userPayInfo.getUser_id());  //增加userid
				balanceAdjustLog.setStaff_id(rar.getStaffId() == null?"":rar.getStaffId());   //增加操作工号
				balanceAdjustLog.setReal_deduct_info(deductInfo);
				
				balanceAdjustLog.setActivity_type(rar.getActivityType());
				balanceAdjustLog.setReason(rar.getReason());
				
				dbTool.createBalanceAdjustLog(balanceAdjustLog);


				
		
			}
			
			
			
		}
		
		resourceAdjustResponse.setStatus("1");
		resourceAdjustResponse.setErrorCode("");
		resourceAdjustResponse.setErrorMessage("");
		
		return resourceAdjustResponse;
		
	}catch (Exception e){
		e.printStackTrace();
		throw e;
	}
		
}

	 

	private long getBalanceId() {
		Sequences s = S.get(Sequences.class).queryFirst(
				Condition.build("queryBalanceId"));
		return s.getSeq();
	}





	
}
