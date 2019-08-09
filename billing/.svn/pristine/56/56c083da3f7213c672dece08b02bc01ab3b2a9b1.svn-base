package com.tydic.beijing.billing.account.service.impl;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.RechargeCallbackOps;
import com.tydic.beijing.billing.account.service.RechargeCallbackWriteOffService;
import com.tydic.beijing.billing.dao.QBlock;
import com.tydic.beijing.billing.dao.QBlockUrge;
import com.tydic.beijing.billing.dao.QRechargeCallback;

public class RechargeCallbackWriteOffServiceImpl implements RechargeCallbackWriteOffService {
	private final static Logger LOGGER = Logger
			.getLogger(RechargeCallbackWriteOffServiceImpl.class);

	private RechargeCallbackOps ops;
	private final static int Q_STATE_INIT_WRITEOFF=0;//q表为0开始销账
	private final static int Q_STATE_OK_WRITEOFF=11;//销账成功，等待回调
	private final static int Q_STATE_PROCESSING_WRITEOFF=5;//正在销账
	private final static int Q_STATE_FAILED_WRITEOFF = 9;//销账失败
	private final static int BLOCK_FLAG = 1;
	@Override
	public void process(int mod, int partition) throws ParseException {
		while (true){
			List<QRechargeCallback> qrcs = ops.scanQRechargeCallback(mod, partition);
			for(QRechargeCallback qrc:qrcs){
				String expMessage=qrc.getProcess_failed_desc();
				LOGGER.debug("Processing Q_Recharge_Callback Record.Serial_No["
						+ qrc.getSerial_no() + "]");
				try {
					if (qrc.getState() == Q_STATE_INIT_WRITEOFF) {
						ops.updateQState(qrc, Q_STATE_PROCESSING_WRITEOFF,expMessage);
						ops.writeoffHistory(qrc);
						ops.writeoffSimulate(qrc);
						ops.updateQBlock(assembleQBlock(qrc));
						ops.updateQBlockUrge(assembleQBlockUrge(qrc));
						ops.updateQState(qrc, Q_STATE_OK_WRITEOFF, expMessage);
						LOGGER.debug("Processing Q_Recharge_CallbackWriteOff Record.Serial_No["
								+ qrc.getSerial_no() + "] Biz-Process Done!");
					}
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage());
					expMessage=ex.getMessage();
					ops.updateQState(qrc, Q_STATE_FAILED_WRITEOFF,expMessage);
					continue;
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}
	
	private QBlock assembleQBlock(QRechargeCallback qrc) {
		QBlock qb = new QBlock();
		qb.setBlock_flag(BLOCK_FLAG);
		qb.setUser_id(qrc.getUser_id());
		qb.setPay_id(qrc.getPay_id());
		return qb;
	}

	private QBlockUrge assembleQBlockUrge(QRechargeCallback qrc) {
		QBlockUrge qbu = new QBlockUrge();
		qbu.setBlock_flag(BLOCK_FLAG);
		qbu.setUser_id(qrc.getUser_id());
		qbu.setPay_id(qrc.getPay_id());
		return qbu;
	}

	public RechargeCallbackOps getOps() {
		return ops;
	}

	public void setOps(RechargeCallbackOps ops) {
		this.ops = ops;
	}
	
}
