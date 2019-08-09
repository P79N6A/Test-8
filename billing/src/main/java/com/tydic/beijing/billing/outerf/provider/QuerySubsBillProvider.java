package com.tydic.beijing.billing.outerf.provider;


import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dto.QuerySubsBillRequest;
import com.tydic.beijing.billing.dto.QuerySubsBillResponse;
import com.tydic.beijing.billing.outerf.api.QuerySubsBill;
import com.tydic.beijing.billing.outerf.architecture.Disposable;
import com.tydic.beijing.billing.outerf.architecture.LogAble;
import com.tydic.beijing.billing.outerf.busi.QuerySubsBillBusi;


public class QuerySubsBillProvider extends LogAble implements QuerySubsBill{
	 private Disposable busi;

	    public void setBusi(QuerySubsBillBusi busi) {
	        this.busi = busi;
	    }
	    
	    public QuerySubsBillProvider() {
	        super();
	    }

	    @Override
	    public QuerySubsBillResponse querySubsBill(QuerySubsBillRequest req) {
	        logger.info( "querySubsBill() in" );
	        logger.info( String.format( "querySubsBill() req:%s", req ) );
	        long beginTime = System.currentTimeMillis();
	        
	        //add by wangtao begin
	        String nbr = req.getMSISDN();
			nbr = JDNToNewMsisdn.jdnToNewMsisdn(nbr,BasicType.STARTSTR);
			req.setMSISDN(nbr);
			//add by wangtao end
			
	        QuerySubsBillResponse resp =  (QuerySubsBillResponse) busi.dispose( req );
	        long endTime = System.currentTimeMillis();
	        logger.info( String.format( "querySubsBill() resp:%s", resp ) );
	        logger.info( "querySubsBill() done Elapsed time["
				+ (endTime - beginTime) + "ms]");
	        return resp;
	    }
	    
	}