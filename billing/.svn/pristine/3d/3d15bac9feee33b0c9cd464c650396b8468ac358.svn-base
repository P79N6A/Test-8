package com.tydic.beijing.billing.interfacex.service.impl;


import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.tydic.beijing.billing.interfacex.service.ResourceChargeService;
import com.tydic.beijing.billing.dto.ResourceChargeParaIn;
import com.tydic.beijing.billing.dto.ResourceChargeParaOut;
import com.tydic.beijing.billing.interfacex.biz.ResourceChargeDb;


public class ResourceChargeServiceImpl implements ResourceChargeService {

	private final static Logger log = Logger.getLogger(ResourceChargeServiceImpl.class);

	private ResourceChargeDb chargeDb;
	
	public ResourceChargeDb getChargeDb() {
		return chargeDb;
	}

	public void setChargeDb(ResourceChargeDb ChargeDb) {
		this.chargeDb = ChargeDb;
	}
	
	public static String formatDate(String str){
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
		String sfstr = "";
		try {
			sfstr = sf2.format(sf1.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sfstr;
	}

	@Override
	public ResourceChargeParaOut charge(ResourceChargeParaIn resourceChargeParaIn) throws Exception {
		log.debug("--ResourceCharge run begin -----");
		/*
		long balance_type_id = 0;
		long charge_value = 0;
		String eff_date = null;
		String exp_date = null;
		String pay_id = null;
		String operate_type = null;
		String SN = null;
		long balance_id = 0;
		String status = null;
		int latn_id = 0;
		ResourceChargeParaOut resourceChargeParaOut = new ResourceChargeParaOut();
		List<ResourceChargeParaOutList> resourceChargeParaOutList = new ArrayList<ResourceChargeParaOutList>();
		
		try{
		pay_id = resourceChargeParaIn.getPayId();
		operate_type = resourceChargeParaIn.getOperateType();
		SN = resourceChargeParaIn.getSN();
		log.debug("pay_id:"+pay_id+", operate_type:"+operate_type+", SN"+SN);
		
		for(ResourceChargeParaInList ParaInList : resourceChargeParaIn.getResourceChargeParaInList()){
			ResourceChargeParaOutList paraOutList = new ResourceChargeParaOutList();
			
			balance_type_id = ParaInList.getBalanceTypeId();
			charge_value = ParaInList.getChargeValue();
			eff_date = formatDate(ParaInList.getEffDate().substring(0, 8));
			exp_date = formatDate(ParaInList.getExpDate().substring(0, 8));
			latn_id = chargeDb.getLatnId(pay_id);
			log.debug("balance_type_id:"+balance_type_id+", charge_value:"+charge_value+", eff_date"+eff_date);
			
			//--查询balance_type_id是否可以合并账本
			CodeBilBalanceType code_bil_balance_type = chargeDb.getBalanceTypeAddFalg(balance_type_id);
			String addup_flag = code_bil_balance_type.getAddup_falg();//0:不可累加，1：可累加
			
			InfoPayBalance info_pay_balance = new InfoPayBalance();
			info_pay_balance.setBalance(charge_value);
			info_pay_balance.setBalance_type_id((int)balance_type_id);
			info_pay_balance.setEff_date(java.sql.Date.valueOf(eff_date));
			info_pay_balance.setExp_date(java.sql.Date.valueOf(exp_date));
			info_pay_balance.setPay_id(pay_id);
			info_pay_balance.setReal_balance(charge_value);
			info_pay_balance.setLatn_id(latn_id);
			log.debug("--addup_flag:"+addup_flag+", setBalance_type_id:"+info_pay_balance.getBalance_type_id());
			
			long old_balance = chargeDb.getOldBalance(pay_id, balance_type_id);
			log.debug("--old_balance:"+old_balance);
			
			//--不可累加
			if(addup_flag.equals("0")){
				//--创建新帐本
				log.debug("in resource charge, balance_type_id="+balance_type_id+", addup_flag="+addup_flag+", insert info_pay_balance");
				balance_id = chargeDb.getBalanceIdNextVal();
				info_pay_balance.setBalance_id(balance_id);
				chargeDb.InsInfoPayBalance(info_pay_balance);				
			}else if(addup_flag.equals("1")){
				//--查询是否存在balance_id
				log.debug("--can add up balance_type_id"+eff_date+", "+exp_date);
				long BalanceId = chargeDb.getBalanceId(balance_type_id, pay_id, eff_date, exp_date);
				if(BalanceId == -1){
					log.debug("--no find balance_id");
					balance_id = chargeDb.getBalanceIdNextVal();
					log.debug("--nextval balance_id"+balance_id);
					info_pay_balance.setBalance_id(balance_id);
					chargeDb.InsInfoPayBalance(info_pay_balance);
				}else{
					log.debug("--find balance_id:"+BalanceId);
					balance_id = BalanceId;
					info_pay_balance.setBalance_id(balance_id);
					chargeDb.UpdInfoPayBalance(info_pay_balance);
				}
				
			}else{
				log.error("in resource charge, balance_type_id="+balance_type_id+", addup_flag="+addup_flag+"error!");
			}
			log.debug("---InfoPayBalance deal done");
			
			long new_balance = old_balance + charge_value;
			
			BilActAccesslog bil_act_accesslog = new BilActAccesslog();
			//bil_act_accesslog.setOperate_id(ChargeDb.getOperateId());
			bil_act_accesslog.setOperate_id(SN);
			bil_act_accesslog.setOperate_type(operate_type);
			bil_act_accesslog.setPay_id(pay_id);
			bil_act_accesslog.setBalance_id(balance_id);
			bil_act_accesslog.setBalance_type_id((int)balance_type_id);
			bil_act_accesslog.setAccess_tag("0");
			bil_act_accesslog.setMoney(charge_value);
			bil_act_accesslog.setOld_balance(old_balance);
			bil_act_accesslog.setNew_balance(new_balance);
			chargeDb.InsBilActAccessLog(bil_act_accesslog);
			log.debug("---InsBilActAccessLog done");
			
			paraOutList.setBalanceTypeId(balance_type_id);
			paraOutList.setEffDate(ParaInList.getEffDate());
			paraOutList.setExpDate(ParaInList.getExpDate());
			paraOutList.setNewBalance(new_balance);
			paraOutList.setOldBalance(old_balance);
			paraOutList.setBalanceId(Long.toString(balance_id));
			resourceChargeParaOutList.add(paraOutList);
			//resourceChargeParaOut.addResourceChargeParaOutList(paraOutList);
			resourceChargeParaOut.setResourceChargeParaOutList(resourceChargeParaOutList);
		}
		
		status = "1";
		resourceChargeParaOut.setStatus(status);
		*/
		ResourceChargeParaOut resourceChargeParaOut = new ResourceChargeParaOut();
		try{
			if(resourceChargeParaIn.getPayId().equals("") || resourceChargeParaIn.getSN().equals("") || resourceChargeParaIn.getOperateType().equals("")){
				log.error("####paraIn pay_id or SN is null");
				resourceChargeParaOut.setErrorMessage("pay_id或者SN,operate_type是空！");
				resourceChargeParaOut.setStatus("0");
				return resourceChargeParaOut;
			}
			resourceChargeParaOut = chargeDb.process(resourceChargeParaIn);
			//return resourceChargeParaOut;
		}catch(Exception e){
			log.error("#####" + e.toString());
			resourceChargeParaOut.setErrorMessage(e.toString());
			resourceChargeParaOut.setStatus("0");
			return resourceChargeParaOut;
		}
		return resourceChargeParaOut;
	}
	
}