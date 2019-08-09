package com.tydic.beijing.billing.account.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.CreateAccountBookOps;
import com.tydic.beijing.billing.account.service.CreateAccountBook;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.dao.InfoPayBalance;

public class CreateAccountBookImpl implements CreateAccountBook {

	private final static Logger LOGGER = Logger
			.getLogger(CreateAccountBookImpl.class);
	private final static long DATE_20501231 = 2556097843923L;
	public final static int DEFAULT_BALANCE_TYPE_ID = 1;
	private CreateAccountBookOps ops;

	@Override
	public int create(String localNet, String payId, Date effDate) {
		InfoPayBalance ipb = new InfoPayBalance();
		ipb.clear();
		ipb.setLocal_net(localNet);
		ipb.setPay_id(payId);
		ipb.setBalance_type_id(DEFAULT_BALANCE_TYPE_ID);
		ipb.setEff_date(new java.sql.Date(effDate.getTime()));
		ipb.setExp_date(new java.sql.Date(DATE_20501231));
		return process(ipb);
	}

	@Override
	public int create(String localNet, String payId, int balanceTypeId,
			Date effDate, Date expDate) {
		InfoPayBalance ipb = new InfoPayBalance();
		ipb.clear();
		ipb.setLocal_net(localNet);
		ipb.setPay_id(payId);
		ipb.setBalance_type_id(balanceTypeId);
		ipb.setEff_date(new java.sql.Date(effDate.getTime()));
		ipb.setExp_date(new java.sql.Date(expDate.getTime()));
		return process(ipb);
	}

	@Override
	public InfoPayBalance createNew(String localNet, String payId,
			int balanceTypeId, Date effDate, Date expDate) {
		InfoPayBalance ipb = new InfoPayBalance();
		ipb.clear();
		ipb.setLocal_net(localNet);
		ipb.setPay_id(payId);
		ipb.setBalance_type_id(balanceTypeId);
		ipb.setEff_date(new java.sql.Date(effDate.getTime()));
		ipb.setExp_date(new java.sql.Date(expDate.getTime()));
		return processNew(ipb, null, null);
	}

	private InfoPayBalance processNew(InfoPayBalance ipb, String attrName,
			String attrValue) {
		LOGGER.debug("CreateAccountBookImpl.processNew input[" + ipb + "]");
		long beginProcess = System.currentTimeMillis();
		ipb.setBalance(0);
		ipb.setReal_balance(0);
		ipb.setUsed_balance(0);
		try {
			InfoPayBalance retIpb = ops.process(ipb, attrName, attrValue);
			LOGGER.debug("CreateAccountBookImpl.processNew output[" + retIpb
					+ "]");
			long endProcess = System.currentTimeMillis();
			LOGGER.trace("[STAT][CreateAcctBook-New]Total["
					+ (endProcess - beginProcess) + "]ms");
			return retIpb;
		} catch (BasicException ex) {
			LOGGER.error(ex.toString());
			return null;
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
			LOGGER.error("Create Account Book for PayId[" + ipb.getPay_id()
					+ "] Failed!");
			return null;
		}
	}

	private int process(InfoPayBalance ipb) {
		LOGGER.debug("CreateAccountBookImpl.process input[" + ipb + "]");
		long beginProcess = System.currentTimeMillis();
		ipb.setBalance(0);
		ipb.setReal_balance(0);
		ipb.setUsed_balance(0);
		try {
			ops.process(ipb, null, null);
			LOGGER.debug("CreateAccountBookImpl.process output[0]");
			long endProcess = System.currentTimeMillis();
			LOGGER.trace("[STAT][CreateAcctBook]Total["
					+ (endProcess - beginProcess) + "]ms");
			return 0;
		} catch (BasicException ex) {
			LOGGER.error(ex.toString());
			return ex.getCode();
		} catch (Exception ex) {
			LOGGER.error("Create Account Book for PayId[" + ipb.getPay_id()
					+ "] Failed!");
			return -1;
		}
	}

	public CreateAccountBookOps getOps() {
		return ops;
	}

	public void setOps(CreateAccountBookOps ops) {
		this.ops = ops;
	}

	@Override
	public InfoPayBalance createNew(String localNet, String payId,
			int balanceTypeId, Date effDate, Date expDate, String attrName,
			String attrValue) {
		InfoPayBalance ipb = new InfoPayBalance();
		ipb.clear();
		ipb.setLocal_net(localNet);
		ipb.setPay_id(payId);
		ipb.setBalance_type_id(balanceTypeId);
		ipb.setEff_date(new java.sql.Date(effDate.getTime()));
		ipb.setExp_date(new java.sql.Date(expDate.getTime()));
		return processNew(ipb, attrName, attrValue);
	}
}
