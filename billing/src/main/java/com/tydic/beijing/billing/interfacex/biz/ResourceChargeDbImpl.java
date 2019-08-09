package com.tydic.beijing.billing.interfacex.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.interfacex.biz.ResourceChargeDb;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dto.ResourceChargeParaIn;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;
import com.tydic.beijing.billing.dto.ResourceChargeParaOut;
import com.tydic.beijing.billing.dto.ResourceChargeParaOutList;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;



public class ResourceChargeDbImpl implements ResourceChargeDb {
	private final static Logger log = Logger.getLogger(ResourceChargeDbImpl.class);
	
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
/*
	//--插入info_pay_balance
	@Override
	public void InsInfoPayBalance(InfoPayBalance infopaybalance) throws Exception {
		// TODO Auto-generated method stub
		log.debug("info_pay_balance pay_id :" + infopaybalance.getPay_id() + ", balance_id:" + infopaybalance.getBalance_id());
		S.get(InfoPayBalance.class).create(infopaybalance);
		//S.get(BilActAccesslog.class).create(bilactaccesslog);
	}
	
	//--查询相同账本类型和生失效时间的balance_id
	@Override
	//select balance_id from info_pay_balance where pay_id = ? and balance_type_id = ? and eff_date = to_date(?, 'YYYYMMDDHH24MISS') and exp_date = to_date(?, 'YYYYMMDDHH24MISS')
	public long getBalanceId(long BalanceTypeId, String PayId, String EffDate, String ExpDate) throws Exception{
		InfoPayBalance info_pay_balance = S.get(InfoPayBalance.class).queryFirst(Condition.build("isBalanceIdExist").filter("pay_id", PayId).filter("balance_type_id", BalanceTypeId).filter("eff_date",EffDate).filter("exp_date", ExpDate));
		if(null==info_pay_balance){
			return -1;
		}
		return info_pay_balance.getBalance_id();
	}

	//--插入存取款日志
	@Override
	public void InsBilActAccessLog(BilActAccesslog bilactaccesslog) throws Exception {
		log.debug("--in InsBilActAccessLog");
		S.get(BilActAccesslog.class).create(bilactaccesslog);
	}

	//--查询balance_type_id账本是否可以累加

	//select addup_flag from CODE_BIL_BALANCE_TYPE where balance_type_id = ?
	public CodeBilBalanceType getBalanceTypeAddFalg(long BalanceTypeId) throws Exception {
		log.debug("in getBalanceTypeAddFalg------------"+BalanceTypeId);
//		CodeBilBalanceType result = S.get(CodeBilBalanceType.class).queryFirst(Condition.build("").filter("balance_type_id", BalanceTypeId));
		CodeBilBalanceType result = S.get(CodeBilBalanceType.class).queryFirst(Condition.build("").filter("balance_type_id", (int)BalanceTypeId));
		log.debug("----addup_flag :"+result.getAddup_falg());
		return result;
	}
	
	@Override
	//--select to_char(Seq_balance_id.Nextval) from dual
	public long getBalanceIdNextVal() throws Exception{
		Sequences s = S.get(Sequences.class).queryFirst(Condition.build("getBalanceIdNextVal"));
		return s.getSeq();
	}

	@Override
	public void UpdInfoPayBalance(InfoPayBalance infopaybalance) throws Exception {
		// TODO Auto-generated method stub
		S.get(InfoPayBalance.class).update(infopaybalance);
	}


	@Override
	public long getOldBalance(String pay_id, long balance_type_id) throws Exception {
		log.debug("in info_pay_balance");
		InfoPayBalance info_pay_balance = S.get(InfoPayBalance.class).queryFirst(Condition.build("getOldBalance").filter("pay_id", pay_id).filter("balance_type_id", balance_type_id));
		if(info_pay_balance==null){
			log.debug("info_pay_balance is null");
			return 0;
		}
		return info_pay_balance.getBalance();
	}

	@Override
	public String getOperateId() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLatnId(String PayId) throws Exception {
		PayUserRel pay_user_rel = S.get(PayUserRel.class).queryFirst(Condition.build("getLatnId").filter("pay_id", PayId));
		return (int)pay_user_rel.getLatn_id();
	}
*/
	@Override
	public ResourceChargeParaOut process(
			ResourceChargeParaIn resourceChargeParaIn) throws Exception {
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
		long old_balance = 0;
		ResourceChargeParaOut resourceChargeParaOut = new ResourceChargeParaOut();
		List<ResourceChargeParaOutList> resourceChargeParaOutList = new ArrayList<ResourceChargeParaOutList>();
		pay_id = resourceChargeParaIn.getPayId();
		operate_type = resourceChargeParaIn.getOperateType();
		SN = resourceChargeParaIn.getSN();
		log.debug("pay_id:"+pay_id+", operate_type:"+operate_type+", SN"+SN);
		
		for(ResourceChargeParaInList ParaInList : resourceChargeParaIn.getResourceChargeParaInList()){
			ResourceChargeParaOutList paraOutList = new ResourceChargeParaOutList();
			
			old_balance = 0;
			balance_type_id = ParaInList.getBalanceTypeId();
			charge_value = ParaInList.getChargeValue();
			eff_date = formatDate(ParaInList.getEffDate().substring(0, 8));
			exp_date = formatDate(ParaInList.getExpDate().substring(0, 8));
			PayUserRel pay_user_rel = S.get(PayUserRel.class).queryFirst(Condition.build("getLatnId").filter("pay_id", pay_id));
			latn_id = (int)pay_user_rel.getLatn_id();
			log.debug("balance_type_id:"+balance_type_id+", charge_value:"+charge_value+", eff_date"+eff_date);
			
			//--查询balance_type_id是否可以合并账本
			CodeBilBalanceType code_bil_balance_type = S.get(CodeBilBalanceType.class).queryFirst(Condition.build("getBalanceTypeAddFalg").filter("balance_type_id", (int)balance_type_id));
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
			
//			InfoPayBalance ipb_oldBalance = S.get(InfoPayBalance.class).queryFirst(Condition.build("getOldBalance").filter("pay_id", pay_id).filter("balance_type_id", balance_type_id));
//			if(ipb_oldBalance==null){
//				log.debug("info_pay_balance is null");
//				old_balance = 0;
//			}else{old_balance = ipb_oldBalance.getReal_balance();}
//			
			
			//--不可累加
			if(addup_flag.equals("0")){
				//--创建新帐本
				log.debug("in resource charge, balance_type_id="+balance_type_id+", addup_flag="+addup_flag+", insert info_pay_balance");
				Sequences s = S.get(Sequences.class).queryFirst(Condition.build("getBalanceIdNextVal"));
				balance_id = s.getSeq();
				info_pay_balance.setBalance_id(balance_id);	
				S.get(InfoPayBalance.class).create(info_pay_balance);
			}else if(addup_flag.equals("1")){
				//--查询是否存在balance_id
				log.debug("--can add up balance_type_id"+eff_date+", "+exp_date);
				InfoPayBalance ipb_BalanceId = S.get(InfoPayBalance.class).queryFirst(Condition.build("isBalanceIdExist").filter("pay_id", pay_id).filter("balance_type_id", balance_type_id).filter("eff_date",eff_date).filter("exp_date", exp_date));
				if(null==ipb_BalanceId){
					log.debug("--no find balance_id");
					Sequences s = S.get(Sequences.class).queryFirst(Condition.build("getBalanceIdNextVal"));
					balance_id = s.getSeq();
					log.debug("--nextval balance_id"+balance_id);
					info_pay_balance.setBalance_id(balance_id);
					S.get(InfoPayBalance.class).create(info_pay_balance);
				}else{
					balance_id = ipb_BalanceId.getBalance_id();
					log.debug("--find balance_id:"+balance_id);
					old_balance=ipb_BalanceId.getReal_balance();
					info_pay_balance.setBalance_id(balance_id);
					S.get(InfoPayBalance.class).update(info_pay_balance);
				}
				
			}else{
				log.error("in resource charge, balance_type_id="+balance_type_id+", addup_flag="+addup_flag+"error!");
				resourceChargeParaOut.setErrorMessage("addup_flag is not 0 or 1");
				resourceChargeParaOut.setStatus("0");
				return resourceChargeParaOut;
			}
			log.debug("---InfoPayBalance deal done");
			
			long new_balance = old_balance + charge_value;
			
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH )+1;
			log.debug("--old_balance:"+old_balance);
			BilActAccesslog bil_act_accesslog = new BilActAccesslog();
			bil_act_accesslog.setOperate_id(SN);
			bil_act_accesslog.setOperate_type(operate_type);
			bil_act_accesslog.setPay_id(pay_id);
			bil_act_accesslog.setBalance_id(balance_id);
			bil_act_accesslog.setBalance_type_id((int)balance_type_id);
			bil_act_accesslog.setAccess_tag("0");
			bil_act_accesslog.setMoney(charge_value);
			bil_act_accesslog.setOld_balance(old_balance);
			bil_act_accesslog.setNew_balance(new_balance);
			bil_act_accesslog.setPartition_id(month);
			S.get(BilActAccesslog.class).create(bil_act_accesslog);
			log.debug("---InsBilActAccessLog done");
			
			paraOutList.setBalanceTypeId(balance_type_id);
			paraOutList.setEffDate(ParaInList.getEffDate());
			paraOutList.setExpDate(ParaInList.getExpDate());
			paraOutList.setNewBalance(new_balance);
			paraOutList.setOldBalance(old_balance);
			paraOutList.setBalanceId(Long.toString(balance_id));
			resourceChargeParaOutList.add(paraOutList);
			resourceChargeParaOut.setResourceChargeParaOutList(resourceChargeParaOutList);
		}
		status = "1";
		resourceChargeParaOut.setStatus(status);
		
		return resourceChargeParaOut;

	}
}