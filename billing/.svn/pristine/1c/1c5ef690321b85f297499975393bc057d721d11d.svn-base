package com.tydic.beijing.billing.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.FixUser;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class TestDubboAccountProcess {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "sum.xml"});
		
		AccountProcess accountProcess;
		int acct_month = Integer.parseInt(args[0]);
		String partition_no = args[1];
		String user_id;
		int cnt = 0;

		Map<String, Object> filter = new HashMap<String, Object>();
		List<FixUser> user_list = S.get(FixUser.class).query(Condition.build("fixmembilluser"));
		for(FixUser user : user_list) {
			cnt++;
			user_id = user.getUser_id();
			filter.clear();
	
			filter.put("acct_month", acct_month);
			filter.put("partition_no", partition_no);
			filter.put("user_id", user_id);
	
			List<BilActAddUp> baaus = S.get(BilActAddUp.class).query(
					Condition.build("getBilActAddUp").filter(filter));
			
			accountProcess = (AccountProcess) context.getBean("accountProcess");
			System.out.println("===========Account Process Begin==========");
			try {
				accountProcess.accountProcess(user_id, baaus);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("do user:" + user_id + " number:" + cnt);
			System.out.println("===========Account Process End==========");
		}
		System.out.println("do user number:" + cnt);
		context.close();
	}

}
