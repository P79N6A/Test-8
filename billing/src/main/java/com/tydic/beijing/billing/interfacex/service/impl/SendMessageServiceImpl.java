package com.tydic.beijing.billing.interfacex.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.jd.jsf.gd.error.NoAliveProviderException;
import com.jd.mobilePhoneMsg.sender.client.request.SignatureMobileNum;
import com.jd.mobilePhoneMsg.sender.client.request.SmsBaseInfo;
import com.jd.mobilePhoneMsg.sender.client.request.SmsMessage;
import com.jd.mobilePhoneMsg.sender.client.response.BaseInfoValidationStatus;
import com.jd.mobilePhoneMsg.sender.client.response.SmsResponse;
import com.jd.mobilePhoneMsg.sender.client.service.SmsMessageRpcService;
import com.jd.mobilePhoneMsg.sender.client.util.SignatureUtil;
//import com.jiexun.sms.sv.SendMsg;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.TbSmsSend;
import com.tydic.beijing.billing.dao.TbSmsSendHis;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.SendMessageService;


public class SendMessageServiceImpl implements SendMessageService {

	private int time;
	private int amount;
	private ExecutorService executorService ;
//	private SendMsg service;
	private DbTool db;
	private Map<String,String> templates =null;
	private Logger log =Logger.getLogger(SendMessageServiceImpl.class);
	final private BlockingQueue<TbSmsSendHis> smsQue= new ArrayBlockingQueue<TbSmsSendHis>(5000);
	final private AtomicLong pendingSmsVisits = new AtomicLong();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String DEAL_FLAG_SUCC="F";
	private int poolSize;
	private String instanceId;
	private final String STOP="0";
	private boolean noProvider=false; 
	
	private SmsMessageRpcService smsMessageRpcService;
	private String accountCommon;
	private String account170;
	
	public void startSendMsg(final List<TbSmsSendHis> smsList){
		
		pendingSmsVisits.incrementAndGet();
		executorService.execute(new Runnable(){

		@Override
		public void run() {
			
			perform(smsList);
			
		}
		});
		
	}
	
	

	

	/**
	 * 	1  0000  成功 
		3  0001  短信网关操作失败
		4  0002  充值平台操作超时
		5  1001  找不到发送号码归属
		6  1002  输入参数错误
		   1003  待扩展
	 * @param smsList
	 */
	public void perform(List<TbSmsSendHis> smsList){
		
		for(TbSmsSendHis iter : smsList){
			log.debug(">>>>>>>>>threadName:"+Thread.currentThread().getName()+"get task : "+iter.getMsg_id());
			JSONObject jsonObject = initJson(iter);
			
			log.debug(">>>>>>send sms message:"+jsonObject.toString());
			try{
				long start=System.nanoTime();
//				String result=service.get(jsonObject.toString());
//				long end = System.nanoTime();
//				JSONObject jo=JSONObject.fromObject(result);
//				String status=jo.getString("result");
//				String responseId=jo.getString("responseId");
//				String responseTime=jo.getString("responseTime");
//				log.debug(">>>>>>return(time taken :"+(end-start)/1.0e9+") service status:"+status+",responseId:"+responseId+",responseTime:"+responseTime+",msgId:"+iter.getMsg_id()+",deviceNumber:"+iter.getMsisdn_receive());
				
				
				//新网关
				String smsAccount = accountCommon;
				if(iter.getMsisdn_receive().startsWith("170")){
					smsAccount = account170;
				}
				
				SmsBaseInfo smsBaseInfo = new SmsBaseInfo();
				smsBaseInfo.setExtension("");//
				smsBaseInfo.setSenderNum(smsAccount); //测试环境senderNum:mms.public.test.senderNum  下行发送者编号、账号,测试环境无需申请，正式环境需要申请
				
				SignatureMobileNum signatureMobileNum =  new SignatureMobileNum();
				signatureMobileNum.setMobileNum(iter.getMsisdn_receive());//短信接收方
				//signatureMobileNum.setSignature(signature); java调用时可不用该参数改为在发送前调用SignatureUtil.generateSignature(smsMessage);
				
				SmsMessage smsMessage = new SmsMessage();
				smsMessage.setSmsBaseInfo(smsBaseInfo);
				smsMessage.setSignatureMobileNum(signatureMobileNum);
				smsMessage.setMsgContent(iter.getMessage_text());
				
				SignatureUtil.generateSignature(smsMessage);
				SmsResponse smsResponse = smsMessageRpcService.sendSmsMessage(smsMessage);
				BaseInfoValidationStatus baseInfoValidationStatus =  smsResponse.getBaseInfoValidationStatus();
				//检查短信发送是否成功
				if(BaseInfoValidationStatus.ALL_PASS.equals(baseInfoValidationStatus)){
					//发送成功
					iter.setProcess_tag(BasicType.SENDSMSMSG_PROCESSTAG_SUCCESS);
				}else{//发送失败
					log.error("send msg error,error info ====>"+baseInfoValidationStatus);
					iter.setProcess_tag(BasicType.SENDSMSMSG_PROCESSTAG_FAIL);
				}
				
//				String returnStatus = initSmsReturnCode(status);	
//				iter.setProcess_tag(returnStatus);
				//使用原始返回状态  配合挪表程序0000-》1
//				if(status ==null || status.isEmpty()){
//					iter.setProcess_tag(BasicType.SENDSMSMSG_PROCESSTAG_FAIL);
//				}else if(status.equals(BasicType.SENDSMSMSG_PROCESSTAG_SUCC)){
//					iter.setProcess_tag(BasicType.SENDSMSMSG_PROCESSTAG_SUCCESS);
//				}else{
//					iter.setProcess_tag(status);
//				}
				
				iter.setComplete_time(sdf.format(new Date()));
				iter.setDeal_flag(DEAL_FLAG_SUCC+"|"+instanceId);
				if(smsQue.size()<200){
					smsQue.put(iter);
				}else{
					db.updateProcessTag(iter);
				}
			}catch(NoAliveProviderException e){
				e.printStackTrace();
				noProvider=true;
				break;
			}catch(Exception ex){
				
				log.error(">>>>>call get method exception:::msgId:"+iter.getMsg_id()+","+"deviceNumber:"+iter.getMsisdn_receive()+">>>>>>>>"+ex.getMessage());
				iter.setProcess_tag(BasicType.SENDSMSMSG_PROCESSTAG_0002);
				iter.setComplete_time(sdf.format(new Date()));
				iter.setDeal_flag(DEAL_FLAG_SUCC+"|"+instanceId);
				db.updateProcessTag(iter);
				
			}
						
		}
		pendingSmsVisits.decrementAndGet();
		log.debug(">>>>>>>>>>>>>>>>>>pendingSmsVisits decrement>>>>"+pendingSmsVisits.get());
	}
	
	
	
	@Override
	public void send() {
		
//		if(isInstanceExists(instanceId)){
//			
//			log.error(">>>>>>>>>>>>instance:"+instanceId+"  exists !!!");
//			return;
//		}
		recovery(instanceId);
		executorService = Executors.newFixedThreadPool(poolSize);
		
		if(!initParameter()){
			return ;
		}
		log.debug("service SendMessageService["+instanceId+"] start successful.......");
		
		List<TbSmsSendHis> smsList=new ArrayList<TbSmsSendHis>();
		TbSmsSend preSms = new TbSmsSend();
		preSms.setDeal_flag(instanceId);
		preSms.setAmount(amount);
		while(pendingSmsVisits.get()==0){

			smsList.clear();
			db.updateDealFlag(preSms);
			
			smsList=db.querySmsSendHis(instanceId,amount*2);
			
			if(smsList !=null && smsList.size()>0 ){		
				startSendMsg(smsList);
				TbSmsSendHis item =null;
				while(pendingSmsVisits.get()>0 || smsQue.size()>0){	
					try {
						item = smsQue.poll(2,TimeUnit.SECONDS);
						if(item != null ){
							db.updateProcessTag(item);
						}
					} catch (InterruptedException e) {
						if(item != null)
							log.error(">>>>>exception:msgId"+item.getMsg_id());
						log.error(">>>>>>"+e.getMessage());
					}
				}
				
			}else{
				
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String runflag=db.queryRunFlag();
			if(!StringUtils.isBlank(runflag) && runflag.equals(STOP))
				break;
			
			if(noProvider){
				break;
			}
		}
		executorService.shutdown();
		
	}


	
	public boolean isInstanceExists(String instanceId){
		TbSmsSendHis sms = db.checkInstance(instanceId);
		if(sms !=null && sms.getMsg_prior()>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void recovery(String instanceId){
		db.recovery(instanceId);
	}
	
	public  boolean initParameter(){
		if(templates==null){
			templates=new HashMap<String,String>();
			List<RuleParameters> param=db.getSmsTemplateParam();
			if(param !=null && param.size() !=0){
				for(RuleParameters iter : param){
					
					templates.put(iter.getPara_char2().trim(), iter.getPara_char4());
					
				}
			}else{
				log.error("短信模版参数异常!");
				return false;
			}
		}
		return true;
	}
	
	
	public JSONObject initJson(TbSmsSendHis sms){
		JSONObject jsonObject=new JSONObject();
		String msgId=sms.getMsg_id();
		Date requestTime=Calendar.getInstance().getTime();
		String date=sdf.format(requestTime);
		String tradeId=date+msgId;
		String requestStr=sdf2.format(requestTime);
		String systemId=BasicType.SENDSMSMSG_SYSTEMID;

		String parakey=sms.getPara_key();
		if(parakey==null || parakey.isEmpty())
			parakey="other";
		String servid=	templates.get(parakey);
		if(servid==null || servid.isEmpty())
			servid=BasicType.SENDSMSMSG_NOTEMPLATE_SERVICE;
		String phoneNo=sms.getMsisdn_receive();
		String content=sms.getMessage_text();
		jsonObject.put("requestId", tradeId);
		jsonObject.put("requestTime", requestStr);
		jsonObject.put("systemId", systemId);
		jsonObject.put("serveId", servid);
		jsonObject.put("phoneNo", phoneNo);
		jsonObject.put("content", content);
		
		return jsonObject ;
		
	}
	
	
	public String initSmsReturnCode(String status){
		if(status == null || status.isEmpty())
			return BasicType.SENDSMSMSG_PROCESSTAG_FAIL;
		
		if(status.equals(BasicType.SENDSMSMSG_PROCESSTAG_SUCC)){
			return BasicType.SENDSMSMSG_PROCESSTAG_SUCCESS;
		}else if(status.equals(BasicType.SENDSMSMSG_PROCESSTAG_ERR1)){
			return BasicType.SENDSMSMSG_PROCESSTAG_0001;
		}else if(status.equals(BasicType.SENDSMSMSG_PROCESSTAG_ERR2)){
			return BasicType.SENDSMSMSG_PROCESSTAG_0002;
		}else if(status.equals(BasicType.SENDSMSMSG_PROCESSTAG_ERR3)){
			return BasicType.SENDSMSMSG_PROCESSTAG_1001;
		}else if(status.equals(BasicType.SENDSMSMSG_PROCESSTAG_ERR4)){
			return BasicType.SENDSMSMSG_PROCESSTAG_1002;
		}else{
			return BasicType.SENDSMSMSG_PROCESSTAG_FAIL;
		}
		
	}
	
	
	
//	public void setService(SendMsg service) {
//		this.service = service;
//	}

	public void setDb(DbTool db) {
		this.db = db;
		
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}





	public String getAccountCommon() {
		return accountCommon;
	}





	public void setAccountCommon(String accountCommon) {
		this.accountCommon = accountCommon;
	}





	public String getAccount170() {
		return account170;
	}





	public void setAccount170(String account170) {
		this.account170 = account170;
	}





	public SmsMessageRpcService getSmsMessageRpcService() {
		return smsMessageRpcService;
	}





	public void setSmsMessageRpcService(SmsMessageRpcService smsMessageRpcService) {
		this.smsMessageRpcService = smsMessageRpcService;
	}
	
	
}
