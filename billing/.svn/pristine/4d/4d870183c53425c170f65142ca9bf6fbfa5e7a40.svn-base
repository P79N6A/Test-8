package com.tydic.beijing.billing.account.service.impl;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;


import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;



import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserContract;
import com.tydic.beijing.billing.dao.LifeUserProto;
import com.tydic.beijing.billing.dao.LifeUserReleaseCal;
import com.tydic.beijing.billing.dao.LogActTradeRelease;
import com.tydic.beijing.billing.dao.RuleResourceRelease;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;



public class SynchroniseExpdateImpl{
private final static Logger LOGGER=Logger.getLogger(SynchroniseExpdateImpl.class);
	@Transactional(rollbackFor=Exception.class)
	public void SynchroniseExpdate() throws Exception  {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString=sdf.format(new java.util.Date());
		String yesterday=sdf.format(new java.util.Date().getTime()-24*60*60*1000);
		LOGGER.debug(dateString+"开始对前一天激活办理协议的用户做时间同步操作");
		
		//获取前一天激活并且办理了协议的用户
		List<InfoUser> infoUser=S.get(InfoUser.class).query(Condition.build("queryYesterdayInfoUser"));
		if(infoUser != null&&(!infoUser.isEmpty())){
			for(InfoUser singleiInfoUser:infoUser){
					dealInfoUser(singleiInfoUser);
					
				
			}
			LOGGER.debug(yesterday+"同步信息完毕 ");
		}else{
			LOGGER.debug(yesterday+" 没有需要同步的用户 ");
		}
	}
	//处理单个的用户
	public void dealInfoUser(InfoUser infoUser) throws Exception{
			LOGGER.debug("=========用户"+infoUser.getUser_id()+"开始同步=========");
			String user_id=infoUser.getUser_id();
			//判断用户协议的返款有效期是否长期
			List<RuleResourceRelease> ruleResourceRelease=S.get(RuleResourceRelease.class).query(Condition.build("queryLongTerm"));
			if(ruleResourceRelease==null&&ruleResourceRelease.size()==0){
				throw new Exception("协议配置表中未找到非长期协议");
			}
			//找到用户对应的协议
			List<LifeUserProto> lifeUserProto=S.get(LifeUserProto.class).query(Condition.build("queryByUserId").filter("user_id", user_id));
			if(lifeUserProto==null&&lifeUserProto.size()==0){
				throw new Exception("未找到用户对应的协议");
			}
			List<InfoPayBalance> newInfoPayBalance=new ArrayList<InfoPayBalance>();
			List<LifeUserReleaseCal> newLifeUserReleaseCal=new ArrayList<LifeUserReleaseCal>();
			for(LifeUserProto lup:lifeUserProto){
				String elementId=lup.getElement_id();
				for(RuleResourceRelease singleRuleResourceRelease:ruleResourceRelease){
					if(!singleRuleResourceRelease.getContract_id().equals(elementId)){
						//如果是长协议跳过
						continue;
					}else{
						Date exp_date=lup.getExp_date();
						LOGGER.debug("协议的结束日期为："+exp_date);
						String contract_id=singleRuleResourceRelease.getContract_id();
						//根据userid和协议号找到返还实例号
						LifeUserContract lifeUserContract=S.get(LifeUserContract.class).queryFirst(Condition.build("queryByUIDAndCID").
								filter("user_id",user_id).filter("contract_id", contract_id));
						if(lifeUserContract==null || lifeUserContract.getContract_inst_id().isEmpty()){
							continue;
						}else{
							LOGGER.debug("contract_inst_id="+lifeUserContract.getContract_inst_id());
						}
						String contract_inst_id=lifeUserContract.getContract_inst_id();
						//根据返还实例号找到 frozen_id
						List<LifeUserReleaseCal> lifeUserReleaseCal=
								S.get(LifeUserReleaseCal.class).query(Condition.build("queryByContractInstId")
								.filter("user_id",user_id).filter("contract_inst_id",contract_inst_id));
						if(lifeUserReleaseCal==null){
							throw new Exception("通过contract_inst_id未找到返还实例号frozen_id");
						}else{
							for(LifeUserReleaseCal singleLifeUserReleaseCal:lifeUserReleaseCal){
								//需要更新未返还明细的结束时间
								if(singleLifeUserReleaseCal.getProcess_state().equals("0")){
									singleLifeUserReleaseCal.setExp_date(exp_date);
									newLifeUserReleaseCal.add(singleLifeUserReleaseCal);
								}
								//需要更新账本结束时间
								if(singleLifeUserReleaseCal.getProcess_state().equals("1")){
								LOGGER.debug("frozen_id="+singleLifeUserReleaseCal.getFrozen_id());
								String frozen_id=singleLifeUserReleaseCal.getFrozen_id();
								//根据user_id，frozen_id 找到修改的balance_id
								List<LogActTradeRelease> logActTradeRelease=S.get(LogActTradeRelease.class).query(Condition.build("queryByUIDAndFID")
										.filter("user_id", user_id).filter("frozen_id", frozen_id));
								if(logActTradeRelease==null){
									throw new Exception("通过user_id和frozen_id未找到返还实例号balance_id");
								}
								for(LogActTradeRelease singlLogActTradeRelease:logActTradeRelease){
									LOGGER.debug("balance_id="+singlLogActTradeRelease.getBalance_id());
									long balance_id=singlLogActTradeRelease.getBalance_id();
									InfoPayBalance singleInfoPayBalance=new InfoPayBalance();
									singleInfoPayBalance.setBalance_id(balance_id);
									singleInfoPayBalance.setExp_date(exp_date);
									newInfoPayBalance.add(singleInfoPayBalance);
								}
								}
							}
						}
					}
				}
			}
			updateTable(newLifeUserReleaseCal,newInfoPayBalance);
			LOGGER.debug("========用户"+user_id+"同步信息完毕========");
			
		}
	private void updateTable(List<LifeUserReleaseCal> lifeUserReleaseCal,List<InfoPayBalance> singleInfoPayBalance) {
		for(InfoPayBalance ipb:singleInfoPayBalance){
			LOGGER.debug("账本ID为："+ipb.getBalance_id()+"修改的日期为："+ipb.getExp_date());
		S.get(InfoPayBalance.class).batch(Condition.build("UpdateNewInfoPayBalance").
				filter("exp_date",ipb.getExp_date()).
				filter("balance_id",ipb.getBalance_id()));
		
		}
		for(LifeUserReleaseCal lifeURC:lifeUserReleaseCal){
			if(lifeURC.getProcess_state()!=null&&lifeURC.getProcess_state().equals("0")){
				S.get(LifeUserReleaseCal.class).batch(Condition.build("UpdateNewLifeUserReleaseCal").
						filter("exp_date",lifeURC.getExp_date()).
						filter("contract_inst_id", lifeURC.getContract_inst_id()));
				}
		}
		
	}
	
}
