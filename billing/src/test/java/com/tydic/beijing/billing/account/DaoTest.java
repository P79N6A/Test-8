package com.tydic.beijing.billing.account;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DaoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml", "uda.xml" });
		context.start();
		PayUserRel lup = S.get(PayUserRel.class).get("1");
		System.out.println(lup.getDefault_tag());

		PayUserRel lup1 = S.get(PayUserRel.class).queryFirst(Condition.build("queryByUserId").filter("user_id", "10001"));
		System.out.println(lup1.getDefault_tag());
	}

}
