package com.tydic.beijing.billing.util;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.RpcContext;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;

/**
 * 
 * @author Tian
 *
 */
public class RecordAssembler {
	// 组装log_refresh_trigger记录
	public static LogRefreshTrigger assembleLogRefreshTrigger(
			int refreshBatchId, String datastoreName, String serviceName,
			String refreshStatus, String memo) {
		LogRefreshTrigger lrt = new LogRefreshTrigger();
		lrt.setRefresh_batch_id(refreshBatchId);
		lrt.setService_name(serviceName);
		URL url = RpcContext.getContext().getUrl();
		if (url != null) {
			lrt.setService_ip(url.getIp());
			lrt.setService_port(Integer.toString(url.getPort()));
			lrt.setService_pid(url.getParameter("pid"));
		} else {
			lrt.setService_ip("Block");
			lrt.setService_port("Block");
			lrt.setService_pid("Block");
		}
		lrt.setDatastore_name(datastoreName);
		lrt.setRefresh_status(refreshStatus);
		lrt.setMemo(memo);
		return lrt;
	}
}
