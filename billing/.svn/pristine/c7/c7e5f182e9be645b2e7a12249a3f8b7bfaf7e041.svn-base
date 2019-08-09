package com.tydic.beijing.billing.branch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.branch.DBKit;
import com.tydic.beijing.billing.branch.dao.InfoUser;
import com.tydic.beijing.billing.branch.dao.LogQBlock;
import com.tydic.beijing.billing.branch.dao.QBlock4Branch;
import com.tydic.beijing.billing.branch.dao.QUserReasonSend;
import com.tydic.beijing.billing.branch.dto.BranchDb;
import com.tydic.beijing.billing.branch.service.BranchService;


public class BranchServiceImpl implements BranchService {
	private final static Logger log = Logger
	.getLogger(BranchServiceImpl.class);
	private BranchDb branchDb;


	public BranchDb getBranchDb() {
		return branchDb;
	}

	public void setBranchDb(BranchDb branchDb) {
		this.branchDb = branchDb;
	}


	public void run() throws Exception {
		

			log.debug("--run begin -----");
			long startTime = System.currentTimeMillis();
		
			log.debug("--startTime :" + startTime);
			
			List<LogQBlock> listLogQBlock = new ArrayList<LogQBlock>();
			List<QUserReasonSend> listQUserReasonSend = new ArrayList<QUserReasonSend>();
			
			log.debug("--select QBlock table------");
			log.info("---select QBblock table begin---");
			List<QBlock4Branch> listQBlock4Branch = this.branchDb.getQblockInfo();
		
			log.debug("---QBlock num：" + listQBlock4Branch.size());
			log.info("---select QBlock table end ---");
			
			if(listQBlock4Branch != null && !listQBlock4Branch.isEmpty()){
				
				
//				for(int i=0; i<listQBlock4Branch.size(); i++){
				for(QBlock4Branch qBlock : listQBlock4Branch){
					log.debug("---for QBlock data --- ");
					LogQBlock logQBlock = new LogQBlock();
					QUserReasonSend qUserReasonSend = new QUserReasonSend();
					
//					QBlock4Branch qBlock = listQBlock4Branch.get(i);
	
					log.debug("----userId :" + qBlock.getUser_id());
					
					log.debug("---select InfoUser table By QBlock.userId begin---");
					InfoUser infoUser = this.branchDb.getInfoUser(qBlock.getUser_id());
					log.debug("---select InfoUser table By QBlock.userId end---");
					if(infoUser == null || infoUser.equals("")){
						System.out.println("infoUser null userId = " + qBlock.getUser_id());
						log.debug("infoUser null userId = " + qBlock.getUser_id());
						continue;
					}
					qUserReasonSend.setUser_no(qBlock.getUser_id());
					qUserReasonSend.setTele_type(infoUser.getTele_type());
					qUserReasonSend.setLocal_net(infoUser.getLocal_net());
					qUserReasonSend.setReason_code("71");
					qUserReasonSend.setActive_type("");
					qUserReasonSend.setCharge_id("");
					
					long sequence = DBKit.getFileSn("SEQ_Q_REASON_SN");
					
					//qUserReasonSend.setEnqueue_date("");//取oracle的时间
					qUserReasonSend.setSerial_num(sequence);//序列：CREDIT_REASON_SEQUENCE.Nextval，自动生成
					
					log.debug("--qUserReasonSend.sequence :" +qUserReasonSend.getSerial_num());
					
					listQUserReasonSend.add(qUserReasonSend);
					
					logQBlock.setUser_id(qBlock.getUser_id());
					logQBlock.setSource("1");
					logQBlock.setPay_id(qBlock.getPay_id());
					logQBlock.setBlock_flag(qBlock.getBlock_flag());
					logQBlock.setSerial_num(qBlock.getSerial_num());
					logQBlock.setEnqueue_date(qBlock.getEnqueue_date());
					//logQBlock.setACTION_TIME("");//取oracle时间
					
					listLogQBlock.add(logQBlock);
					
					System.out.println("logQBlock:" + logQBlock.toString());
					System.out.println("qUserReasonSend:" + qUserReasonSend.toString());
					log.debug("logQBlock:" + logQBlock.toString());
					log.debug("qUserReasonSend:" + qUserReasonSend.toString());
				
				}
		
				if(listLogQBlock.size()>0){
					this.branchDb.insLogQblock(listLogQBlock);	
				}
				if(listQUserReasonSend.size()>0){
					this.branchDb.insQUserReasonSend(listQUserReasonSend);	
				}
				if(listQBlock4Branch.size()>0){
					log.debug("-----delete from qblock---rowid :" + listQBlock4Branch.get(0).getRow_id());
					//System.out.println("-----delete from qblock");
					//System.out.println("-----delete from qblock ---rowid :" + listQBlock4Branch.get(0).getRow_id());
					this.branchDb.delQblockInfo(listQBlock4Branch);	
				}
		
				long endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				System.out.println("条数：" + listQBlock4Branch.size() + ",时间：" + String.valueOf(time));

			}
			log.debug("---QBlock null ---");
	}

}
