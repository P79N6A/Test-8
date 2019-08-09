package com.tydic.beijing.billing.branch;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dto.Res2AcctProductInfo;
import com.tydic.beijing.billing.dto.Resource2AccountInfo;
import com.tydic.beijing.billing.dto.Resource2AccountResult;
import com.tydic.beijing.billing.interfacex.service.Resource2Account;

public class BatchRe2AcctOps implements Runnable{
	private static Logger LOGGER = Logger.getLogger(BatchRe2AcctOps.class);

	private batchRe2AtOps ops;
	private Resource2Account resource2AccountService;
	private int mod_id;
	
	public int getMod_id() {
		return mod_id;
	}

	public void setMod_id(int mod_id) {
		this.mod_id = mod_id;
	}



	
	
	public void run() {
		try {
			for(int i=0; i<10; i++){
				LOGGER.debug("#############  run start ############");
			    List<InfoUser> lInfoUser = ops.selectInfoUser(mod_id);
			    if(lInfoUser != null){
			    	for(InfoUser iu : lInfoUser){
			    	    Resource2AccountInfo info = new Resource2AccountInfo();
			    	    info.setContactChannel("10");
				    	info.setUserId(iu.getUser_id());				    
				    	List<Res2AcctProductInfo> lr2aProductInfo = new ArrayList<Res2AcctProductInfo>();
				    	List<LifeUserProduct> lupl = ops.selectLifeUserProduct(iu.getUser_id());
						
				    	
				    	if(lupl == null || lupl.isEmpty()){
				    		LOGGER.debug("---用户：" + iu.getUser_id() + "订购的产品，没有需要资源到账的----");
				    		continue;
				    	}
				    	LOGGER.debug("--用户：" + iu.toString());
				    	resetProductInfo(lr2aProductInfo,lupl);
				    	LOGGER.debug("---size = " + lr2aProductInfo.size() + ",lr2aProductInfo[" + lr2aProductInfo.toString());
				    	info.setlProductInfo(lr2aProductInfo);
				    	Resource2AccountResult result = resource2AccountService.doRes2Acct(info);
				    	if(result.getStatus().trim().equals("0")){
				    		LOGGER.error("--用户 :" + iu.getUser_id() + "," + result.toString());
				    	}
			    	}
			    }
			    mod_id++;
			
		}
	} catch (Exception e) {
		LOGGER.error("---" + e.toString());
		e.printStackTrace();
		return;
	}

}

	private void resetProductInfo(List<Res2AcctProductInfo> lr2aProductInfo,
			List<LifeUserProduct> lupl) {
		
		for(LifeUserProduct lup : lupl){
			Res2AcctProductInfo r2a = new Res2AcctProductInfo();
			r2a.setProductId(lup.getProduct_id());
			r2a.setUserProductId(lup.getUser_product_id());
			lr2aProductInfo.add(r2a);
		}
	}

	public batchRe2AtOps getOps() {
		return ops;
	}

	public void setOps(batchRe2AtOps ops) {
		this.ops = ops;
	}

	public Resource2Account getResource2AccountService() {
		return resource2AccountService;
	}

	public void setResource2AccountService(Resource2Account resource2AccountService) {
		this.resource2AccountService = resource2AccountService;
	}
	
}
