package com.tydic.beijing.billing.branch.dto.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.branch.dao.InfoUser;
import com.tydic.beijing.billing.branch.dao.LogQBlock;
import com.tydic.beijing.billing.branch.dao.QBlock4Branch;
import com.tydic.beijing.billing.branch.dao.QUserReasonSend;
import com.tydic.beijing.billing.branch.dto.BranchDb;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class BranchDbImpl implements BranchDb {
	private final static Logger log = Logger.getLogger(BranchDbImpl.class);
	
	private static final int MAX_IN = 500;
//	@Override
//	public void delQblockInfo(List<QBlock4Branch> one)  throws Exception{
//		StringBuffer sb = new StringBuffer();
//		int len = one.size();
//
//		for (int i = 0; i < len; i++) {
//			sb.append("'").append(one.get(i).getRow_id()).append("'");
//			sb.append(",");
//		}
//		
////			if (i != 0 && i % MAX_IN == 0) {
//			
//					
//		String rowid = sb.toString();
//		rowid = rowid.substring(0, rowid.length()-1);
//		System.out.println("---rowid:" + sb.toString());
//		log.info("---rowid:" + rowid);
//					
//					S.get(QBlock4Branch.class).batch(
//						Condition.build("branch.qblock.delete").filter("row_id", rowid),
//						new QBlock4Branch());
//
//				//sb.delete(0, sb.length());
////			} else {
////				if (i != len - 1) {
////					sb.append(",");
////				}
////			}
////		}
////		if(!(sb.toString().equals("") || sb.toString() == null)){
////			System.out.println("--222-rowid:" + sb.toString());
////			S.get(QBlock4Branch.class).batch(
////				Condition.build("branch.qblock.delete").filter("row_id", sb.toString()), new QBlock4Branch());
////		}
//
//	}
	
	public void delQblockInfo(List<QBlock4Branch> delList){
		StringBuffer sb = new StringBuffer();
		int len = delList.size();

		log.debug("###>step in deleteQBlock().delList.size=" + len);
		for (int i = 0; i < len; i++) {
			sb.append("'").append(delList.get(i).getRow_id()).append("'");
			if (i != 0 && i % MAX_IN == 0) {
				S.get(QBlock4Branch.class).batch(
						Condition.build("branch.qblock.delete").filter("row_id", sb.toString()),
						new QBlock4Branch());
				sb.delete(0, sb.length());
			} else {
				if (i != len - 1) {
					sb.append(",");
				}
			}
		}

		log.debug("###>step in deleteQBlock().sb.length=" + sb.length());
		if (sb.length() != 0) {
			S.get(QBlock4Branch.class).batch(
					Condition.build("branch.qblock.delete").filter("row_id", sb.toString()),
					new QBlock4Branch());
		}
	}
	

	@Override
	public InfoUser getInfoUser(String userId) throws Exception {
		InfoUser infoUser = new InfoUser();
		infoUser = S.get(InfoUser.class).queryFirst(Condition.build("getInfoUser").filter("user_id", userId));
		return infoUser;
	}

	@Override
	public List<QBlock4Branch> getQblockInfo()  throws Exception {
		List<QBlock4Branch> listQBlock4Branch = new ArrayList<QBlock4Branch>();
		listQBlock4Branch = S.get(QBlock4Branch.class).query(Condition.build("getQBlockInfo"));
		
		return listQBlock4Branch;
	}

	@Override
	public void insLogQblock(List<LogQBlock> listLogQblock)  throws Exception{
		System.out.println("LogQBlock条数：" + listLogQblock.size());
		for(int i=0; i<listLogQblock.size(); i++){
			log.debug("logqblock userid :" + listLogQblock.get(i).getUser_id());
			S.get(LogQBlock.class).create(listLogQblock.get(i));
		}
	}

	@Override
	public void insQUserReasonSend(List<QUserReasonSend> listQUserReasonSend)  throws Exception{
		System.out.println("QUserReasonSend条数：" + listQUserReasonSend.size());
		
		for(int i=0; i<listQUserReasonSend.size(); i++){
			log.debug("--quserreasonsed userno :" + listQUserReasonSend.get(i).getUser_no());
			S.get(QUserReasonSend.class).create(listQUserReasonSend.get(i));
		}
	}


}
