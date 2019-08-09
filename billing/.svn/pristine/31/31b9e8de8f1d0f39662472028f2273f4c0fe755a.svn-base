package com.tydic.beijing.billing.branch;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Resource2AccountBatchMain {
	private static Logger LOGGER = Logger.getLogger(Resource2AccountBatchMain.class);
	
		public static void main(String[] args) {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"resbatch.xml"});
			LOGGER.debug("##################################");
		
			String str_thread_num = args[0];
			String str_mod_no = args[1];
			int mod_id = Integer.parseInt(str_mod_no);
			int thread_num = Integer.parseInt(str_thread_num);

			for(int i=0; i<thread_num; i++){
				BatchRe2AcctOps ops = (BatchRe2AcctOps)context.getBean("batchRe2AcctOps");
				ops.setMod_id(mod_id);
				new Thread(ops).start();
				mod_id += 10;
			}
			LOGGER.debug("-----end -------------------");
		}


}
