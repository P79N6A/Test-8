package com.tydic.beijing.billing.account.biz;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.Log;
import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.QBlock;
import com.tydic.beijing.billing.dao.QBlockUrge;
import com.tydic.beijing.billing.dao.QRechargeCallback;
import com.tydic.beijing.billing.dao.QRechargeCallbackLog;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

import net.sf.jsqlparser.statement.select.Select;

public class RechargeCallbackOps extends MyApplicationContextUtil /*implements
		InitializingBean*/ {
	//Dao2File d2f_item;
	//Dao2File d2f_log;
	
	private AccountProcess accountProcess;
	
	private final static Logger LOGGER = Logger
			.getLogger(RechargeCallbackOps.class);

	public RechargeCallbackOps() {
		//d2f_item = new BilActRealTimeBill();
		//d2f_log = new LogActPreWriteoff();
	}

	public void writeoffHistory(QRechargeCallback qrc) throws Exception {
		HistoryWriteOff hisWriteOff = (HistoryWriteOff) mycontext
				.getBean("hisWriteOff");
		hisWriteOff.process(qrc.getSerial_no(), qrc.getUser_id(), false);
//		SimulateWriteOffService simulateWriteOff = (SimulateWriteOffService) mycontext
//				.getBean("SimulateWriteOffImpl");
//		simulateWriteOff.doSimulation(qrc.getUser_id(), d2f_item, d2f_log,
//				true);
		//zhanghb modify 20150817 改为调模拟销账接口accountProcess
		
	}

	public void writeoffSimulate(QRechargeCallback qrc) throws Exception {
		LOGGER.debug("开始模拟销账===");
		String userId = qrc.getUser_id();
		List<BilActAddUp> listBilActAddUp = queryBilActAddUpbyUserId(userId);
		accountProcess.accountProcess(userId,listBilActAddUp);
		LOGGER.debug("模拟销账结束===");
	}
	
	private List<BilActAddUp> queryBilActAddUpbyUserId(String userId) throws Exception {
		
		CodeAcctMonth codeAcctMonth = S.get(CodeAcctMonth.class).queryFirst(Condition.build("queryBySysdate"));
		if(codeAcctMonth == null) {
			LOGGER.error("CodeAcctMonth error");
			throw new Exception("账期异常");
		}
		int acctMonth = codeAcctMonth.getAcct_month();
		String partitionNo = codeAcctMonth.getPartition_no();

		return S.get(BilActAddUp.class).query(Condition.build("getBilActAddUp").filter("partition_no",partitionNo)
				.filter("user_id",userId).filter("acct_month",acctMonth));

	}

	public List<QRechargeCallback> scanQRechargeCallback(int mod,int partition) {
		return S.get(QRechargeCallback.class).query(Condition.empty().filter("mod",mod).filter("partition",partition));
	}
	public List<QRechargeCallback> scanQRechargeCallbackUpdate(int mod,int partition) {
		return S.get(QRechargeCallback.class).query(Condition.build("queryQRechargeCallback").filter("mod",mod).filter("partition",partition));
	}

	public void updateQState(QRechargeCallback qrc, int state,String memsage) {
		qrc.setState(state);
		qrc.setProcess_failed_desc(memsage);
		S.get(QRechargeCallback.class).update(qrc);
	}

	public void updateQBlock(QBlock qb) {
		Sequences seq = S.get(Sequences.class).queryFirst(
				Condition.build("querySeqQBlock"));
		qb.setSerial_num((int) seq.getSeq());
		S.get(QBlock.class).create(qb);
	}

	public void updateQBlockUrge(QBlockUrge qbu) {
		Sequences seq = S.get(Sequences.class).queryFirst(
				Condition.build("querySeqQBlock"));
		qbu.setSerial_num((int) seq.getSeq());
		S.get(QBlockUrge.class).create(qbu);
	}

	public void insertQRechargeCallbackLog(QRechargeCallback qrc){
			QRechargeCallbackLog qrcl=new QRechargeCallbackLog();
			qrcl.setSerial_no(qrc.getSerial_no());
			qrcl.setUser_id(qrc.getUser_id());
			qrcl.setCallbackurl(qrc.getCallbackurl());
			qrcl.setDevice_number(qrc.getDevice_number());
			qrcl.setPay_id(qrc.getPay_id());
			qrcl.setPay_time(qrc.getPay_time());
//			qrcl.setProcess_time(processTime);
			qrcl.setRecharge_detail(qrc.getRecharge_detail());
			qrcl.setState(qrc.getState());
			S.get(QRechargeCallbackLog.class).create(qrcl);
	}
	
	
	public void deleteQRechargeCallback(QRechargeCallback qrc){
			S.get(QRechargeCallback.class).batch(Condition.build("deleteQRechargeCallback"), qrc);
	}
	
//	public void insertDetail(QRechargeCallback qrc,String exceptionMessage){
//		qrc.setProcess_failed_desc(exceptionMessage);
//		S.get(QRechargeCallback.class).update(qrc);
//	}
	
	/*
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(d2f_item).start();
		new Thread(d2f_log).start();
	}
	*/

	public AccountProcess getAccountProcess() {
		return accountProcess;
	}

	public void setAccountProcess(AccountProcess accountProcess) {
		this.accountProcess = accountProcess;
	}
	
	
}
