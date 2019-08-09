package com.tydic.beijing.billing.interfacex.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserContract;
import com.tydic.beijing.billing.dao.LifeUserReleaseCal;
import com.tydic.beijing.billing.dao.RuleContract;
import com.tydic.beijing.billing.dao.RuleResourceRelease;
import com.tydic.beijing.billing.dto.QryPromDetailResponse;
import com.tydic.beijing.billing.dto.ReleaseFeeDto;
import com.tydic.beijing.billing.interfacex.service.QryPromDetailService;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;


public class QryPromDetailServiceImpl implements QryPromDetailService {
	private final static Logger LOGGER = Logger.getLogger(QryPromDetailServiceImpl.class);
	@Override
	public QryPromDetailResponse qryPromDetail(String ContactChannle, String MSISDN) {
		QryPromDetailResponse response=new QryPromDetailResponse();
		try {
			Map<Integer, CodeBilBalanceType> store =new HashMap<Integer, CodeBilBalanceType>();
			Map<String, String> contractIds=new HashMap<String, String>();
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			store = load();
			
			List<RuleResourceRelease> RuleResourceReleases=S.get(RuleResourceRelease.class).query(Condition.build("getAllRuleResourceRelease"));
			
			response.setStatus("1");
			response=checkInputParam(response, ContactChannle, MSISDN);
			if (response.getStatus().equals("0")) {
				return response;
			}
			
			InfoUser infoUser=S.get(InfoUser.class).queryFirst(Condition.build("getUserInfoByDeviceNumber").filter("device_number",MSISDN));
			if (infoUser==null) {
				response.setStatus("0");
				response.setErrorMessage("没有找到该号码["+MSISDN+"]的用户信息");
				return response;
			}
			List<ReleaseFeeDto> releaseFeeDtos=new ArrayList<ReleaseFeeDto>();
			String userId=infoUser.getUser_id();
			contractIds=getContractId(userId);
			List<LifeUserReleaseCal> lifeUserReleaseCals=S.get(LifeUserReleaseCal.class).query(Condition.build("getLifeUserReleaseCalByUserId").filter("user_id",userId));
			
			if (lifeUserReleaseCals==null || lifeUserReleaseCals.size()==0) {
				LOGGER.debug("该用户的lifeUserReleaseCals不存在，UserId:["+"userId"+"]");
				response.setReleaseFeeDtoList(releaseFeeDtos);
			}else {
				for(LifeUserReleaseCal lifeUserReleaseCal:lifeUserReleaseCals){
					ReleaseFeeDto releaseFeeDto=new ReleaseFeeDto();
					RuleResourceRelease ruleResourceRelease=null;
					int balance_type_id=(int)lifeUserReleaseCal.getBalance_type_id();
					CodeBilBalanceType cBalanceType=store.get(balance_type_id);
//					LOGGER.debug("账本类型表:"+cBalanceType.toString());
					if (cBalanceType==null) {
						LOGGER.debug("无该账本类型["+lifeUserReleaseCal.getBalance_type_id()+"]");
						continue;
					}
					String contractId="";
					if (contractIds.containsKey(lifeUserReleaseCal.getContract_inst_id())) {
						contractId=contractIds.get(lifeUserReleaseCal.getContract_inst_id());
					}
					for(RuleResourceRelease release:RuleResourceReleases){
						if (contractId.equals(release.getContract_id())) { 
							ruleResourceRelease=release;
							break;
						}
					}
					
					if (ruleResourceRelease==null) {
						releaseFeeDto.setReturnType(null);
					}else {
						long releaseCycle= ruleResourceRelease.getRelease_cycle();
						if (releaseCycle==1L) {
							releaseFeeDto.setReturnType(1+"");
						}else {
							releaseFeeDto.setReturnType(2+"");
						}
					}
					
					
					String BalanceTypeName =cBalanceType.getBalance_type_name();
					LOGGER.debug("账本类型名称是：["+cBalanceType.getBalance_type_name()+"]");
					String ReleaseFee=String.valueOf(lifeUserReleaseCal.getBalance());
					String GiveStatus=lifeUserReleaseCal.getProcess_state();
					Date EffDate=lifeUserReleaseCal.getEff_date();
					Date ExpDate=lifeUserReleaseCal.getExp_date();
					Calendar cal=Calendar.getInstance();
					cal.setTime(ExpDate);
					cal.add(Calendar.DATE, 1);
					cal.add(Calendar.SECOND, -1);
					ExpDate=(Date) cal.getTime();
					String effDate=dateformat.format(EffDate);
					String expDate=dateformat.format(ExpDate);
					releaseFeeDto.setBalanceTypeName(BalanceTypeName);
					releaseFeeDto.setEffDate(effDate);
					releaseFeeDto.setExpDate(expDate);
					releaseFeeDto.setGiveStatus(GiveStatus);
					releaseFeeDto.setReleaseFee(ReleaseFee);
					releaseFeeDtos.add(releaseFeeDto);
				}
				response.setReleaseFeeDtoList(releaseFeeDtos);
			}
		} catch (Exception e) {
			response.setStatus("0");
			response.setErrorMessage(e.getMessage());
		}
		LOGGER.debug(response.toString());
		return response;
	}
	
	public QryPromDetailResponse checkInputParam(QryPromDetailResponse response,String ContactChannle, String MSISDN){
		if ( ContactChannle==null || ContactChannle.equals("")) {
			response.setStatus("0");
			response.setErrorMessage("没有ContactChannle入参");
		}
		if ( MSISDN==null || MSISDN.equals("")) {
			response.setStatus("0");
			response.setErrorMessage("没有MSISDN入参");
		}
		return response;
	}
	
	public Map<Integer, CodeBilBalanceType> load() throws Exception {
		Map<Integer, CodeBilBalanceType> store = null;
		if (store == null) {
			List<CodeBilBalanceType> bts = S.get(CodeBilBalanceType.class)
					.query(Condition.empty());
			if (bts == null) {
				LOGGER.error("TABLE[CODE_BIL_BALANCE_TYPE] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_BIL_BALANCE_TYPE] Shouldn't Be Empty!");
			}
			store = new HashMap<Integer, CodeBilBalanceType>();
			for (CodeBilBalanceType bt : bts) {
				store.put(bt.getBalance_type_id(), bt);
			}
		}
		LOGGER.debug("账本类型Map是:"+store.size());
		return store;
	}

	public Map<String, String> getContractId(String userid) throws Exception {
		Map<String, String> contractIds = new HashMap<String, String>();
		
			List<LifeUserContract> lucs = S.get(LifeUserContract.class)
					.query(Condition.build("getLifeUserContractByUserId").filter("user_id",userid));
			
			for (LifeUserContract luc : lucs) {
				contractIds.put(luc.getContract_inst_id(), luc.getContract_id());
			}
			LOGGER.debug("绑定分月返还协议个数:"+contractIds.size());
		return contractIds;
	}

}
