package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dto.ResourcePara;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleParameters {
	private final static Logger LOGGER = Logger
			.getLogger(DSRuleParameters.class);
	private List<RuleParameters> lstore;
	private Map<String,Integer> mstore;
	private ResourcePara mrp = new ResourcePara();
	private static final int RESOURCE_PARA_DOMAIN_CODE = 8000; // 资源域
	private static final String AGREEMENT_TYPE = "AGREEMENT_TYPE";
	private static final String CYCLE_TRIAL = "CycleTrial";

	public DSRuleParameters() {

	}

	public synchronized void load() throws Exception {
		if (lstore == null) {
			List<RuleParameters> rps = S.get(RuleParameters.class).query(
					Condition.build("queryAll"));
			if (rps == null || rps.isEmpty()) {
				LOGGER.error("TABLE[RULE_PARAMETERS] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[RULE_PARAMETERS] Shouldn't Be Empty!");
			}
			lstore = rps;
			mstore = new HashMap<String,Integer>();
			for(RuleParameters mrps: rps){
				if(mrps.getPara_name().trim().equals(AGREEMENT_TYPE)){
					mstore.put(mrps.getPara_char1(), mrps.getPara_num1());
				}
			}
		}
	}


	public synchronized ResourcePara get(String domain_code) {
		for (RuleParameters rp : lstore) {
			if (rp.getDomain_code() == RESOURCE_PARA_DOMAIN_CODE
					&& domain_code.equals(BasicType.RESOURCE_CHANGE)) {
				if (rp.getPara_name().trim().equals("StaffId")) {
					mrp.setStaff_id(rp.getPara_char1());
				} else if (rp.getPara_name().trim().equals("ChannelId")) {
					mrp.setChannel_id(rp.getPara_char1());
				} else if (rp.getPara_name().trim().equals("ExternalSystemId")) {
					mrp.setExternal_system_id(rp.getPara_char1());
				} else if (rp.getPara_name().trim().equals("MsisdnSend")){
					mrp.setMsisnd_send(rp.getPara_char1());
				}
			}
			if (rp.getDomain_code() == RESOURCE_PARA_DOMAIN_CODE
					&& domain_code.equals(BasicType.CYCLE_RENT)) {
				if (rp.getPara_name().trim().equals("cdrPath")) {
					mrp.setPath(rp.getPara_char1());;
				}
				if(rp.getPara_name().equals("CHANNEL_NO")){
					mrp.setChannel_no(rp.getPara_num1());
				}
			}
			if (rp.getDomain_code() == RESOURCE_PARA_DOMAIN_CODE
					&& domain_code.equals(BasicType.RESOURCE_PRESENT)) {
				if (rp.getPara_name().trim().equals("StaffId")) {
					mrp.setStaff_id(rp.getPara_char3());
				} else if (rp.getPara_name().trim().equals("ChannelId")) {
					mrp.setChannel_id(rp.getPara_char3());
				} else if (rp.getPara_name().trim().equals("ExternalSystemId")) {
					mrp.setExternal_system_id(rp.getPara_char3());
				} else if (rp.getPara_name().trim().equals("MsisdnSend")){
					mrp.setMsisnd_send(rp.getPara_char1());
				}
			}
		}

		return mrp;
	}
	
	public synchronized int getArgeementType(String argeement_type) {
		LOGGER.debug("-argeement_type="+argeement_type+"--mstore.get(argeement_type):"+mstore.get(argeement_type));
		return mstore.get(argeement_type);
	}
	
	public synchronized String getSmsTemp(String product_id){
		for (RuleParameters rp : lstore) {
			if(rp.getDomain_code() == RESOURCE_PARA_DOMAIN_CODE && rp.getPara_name().equals(CYCLE_TRIAL)){
				if(product_id.equals(rp.getPara_char1())){
					return rp.getPara_char2();
				}
			}
		}
		return null;
	}
	
	
	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh RuleParameters Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleParameters Size[" + lstore.size()
					+ "]");
		}
		List<RuleParameters> rps = S.get(RuleParameters.class).query(
				Condition.build("queryAll"));
		if (rps == null || rps.isEmpty()) {
			LOGGER.error("TABLE[RULE_PARAMETERS] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RULE_PARAMETERS] Shouldn't Be Empty!");
		}
		lstore = rps;
		LOGGER.info("After Refresh RULE_PARAMETERS Size[" + lstore.size() + "]");
	}

}
