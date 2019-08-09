package com.tydic.beijing.billing.branch.dto;

import java.util.List;

import com.tydic.beijing.billing.branch.dao.InfoUser;
import com.tydic.beijing.billing.branch.dao.LogQBlock;
import com.tydic.beijing.billing.branch.dao.QBlock4Branch;
import com.tydic.beijing.billing.branch.dao.QUserReasonSend;

public interface BranchDb {
	
	public List<QBlock4Branch> getQblockInfo() throws Exception;
	
	public InfoUser getInfoUser(String userId)  throws Exception;
	
	public void delQblockInfo(List<QBlock4Branch> one)  throws Exception;
	
	public void insLogQblock(List<LogQBlock> listLogQblock)  throws Exception;
	
	public void insQUserReasonSend(List<QUserReasonSend> listQUserReasonSend)  throws Exception;
	
	

}
