package com.tydic.beijing.billing.account.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.RechargeOps;
import com.tydic.beijing.billing.account.service.Recharge;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dto.RechargeInfo;
import com.tydic.beijing.billing.dto.RechargeResult;

public class RechargeImpl implements Recharge {
	private final static Logger LOGGER = Logger.getLogger(RechargeImpl.class);
	private final static String RESULT_STATUS_FAIL = "0"; // 返回失败
	private final static Map<String,String> rechargeP = new HashMap<String,String>();

	RechargeOps ops;
	
	static{
		Properties p = new Properties();
		try {
			p.load(RechargeImpl.class.getClassLoader()
					.getResourceAsStream("recharge.properties"));
		} catch (Exception ex) {
			LOGGER.error("load recharge.properties error!!!");
			LOGGER.error(ex.getMessage());
			System.exit(1);
		}
		Iterator<Entry<Object, Object>> it = p.entrySet().iterator();  
        while (it.hasNext()) {  
        	Entry<Object, Object> entry = it.next();  
        	rechargeP.put(entry.getKey().toString(), entry.getValue().toString());
        }
	}

	@Override
	public RechargeResult recharge(RechargeInfo info) {
		LOGGER.debug("Recharge Input:[" + info + "]");
		// add by wangtao begin
		String nbr = info.getMSISDN();
		nbr = JDNToNewMsisdn.jdnToNewMsisdn(nbr, BasicType.STARTSTR);
		info.setMSISDN(nbr);
		// add by wangtao end
		RechargeResult result = new RechargeResult();
		try {
			ops.recharge(info, result);
		} catch (Exception ex) {
			LOGGER.error("Recharge DeviceNumber[" + info.getMSISDN()
					+ "] Failed!");
			LOGGER.error(ex.toString());
			if (!result.getStatus().trim().equalsIgnoreCase(RESULT_STATUS_FAIL)) {
				result.setStatus(RESULT_STATUS_FAIL);
				result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				result.setErrorMessage("数据库异常");
			}
		}
		LOGGER.debug("Recharge output:[" + result + "]");
		return result;
	}
	
	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		ops.refresh(refreshBatchId, datastoreName, serviceName);
	}

	@Override
	public RechargeResult rechargeOutIntf(RechargeInfo info) {
		LOGGER.debug("Recharge Input:[" + info + "]");
		Set<String> feimaoNumSet = null;
		if(rechargeP.get("feimaoNum")!=null){
			LOGGER.debug("调用飞猫充值的号码段:"+rechargeP.get("feimaoNum"));
			String[] feimaoNums = rechargeP.get("feimaoNum").split(",");
			feimaoNumSet = new HashSet<String>();
			for(int i=0;i<feimaoNums.length;i++){
				feimaoNumSet.add(feimaoNums[i]);
			}
		}
		if(feimaoNumSet!=null && feimaoNumSet.contains(info.getMSISDN().trim().substring(0, 3))){
			LOGGER.debug("Recharge 145号码");
			LOGGER.debug("调用飞猫充值的url:"+rechargeP.get("feimaoUrl"));
			return ops.recharge145(info, rechargeP.get("feimaoUrl"));
		}else{
			return recharge(info);
		}
	}
	
	public RechargeOps getOps() {
		return ops;
	}

	public void setOps(RechargeOps ops) {
		this.ops = ops;
	}

}
