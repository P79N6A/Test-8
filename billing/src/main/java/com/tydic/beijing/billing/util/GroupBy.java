package com.tydic.beijing.billing.util;

import java.util.Collections;
import java.util.List;

import com.tydic.beijing.billing.account.type.BalanceWriteOff;
import com.tydic.beijing.billing.account.type.ComparatorBilActBalanceAddUpByBalance;
import com.tydic.beijing.billing.account.type.ComparatorBilActBalanceAddUpByItem;
import com.tydic.beijing.billing.account.type.ComparatorWriteOffDetailByBalance;
import com.tydic.beijing.billing.account.type.ComparatorWriteOffDetailByItem;
import com.tydic.beijing.billing.account.type.ItemWriteOff;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;

public class GroupBy {

	public void doGroupByBalance(List<WriteOffDetail> list_Detail, List<BalanceWriteOff> list_bal) {
		list_bal.clear();
		//排序
		for(WriteOffDetail d : list_Detail) {
			System.out.println(d);
		}
		System.out.println("-----------");
		Collections.sort(list_Detail, new ComparatorWriteOffDetailByBalance());
		for(WriteOffDetail d : list_Detail) {
			System.out.println(d);
		}
		System.out.println("-----------");
		//合并
		BalanceWriteOff bal = null;
		for(WriteOffDetail iter:list_Detail) {
			if(null == bal || bal.equals(iter) != true) {
				if(null != bal) {
					list_bal.add(bal);
				}
				bal  = new BalanceWriteOff(iter);
			} else if (bal.equals(iter) == true){
				bal.add(iter);
			}
		}
		if(bal != null) {
			list_bal.add(bal);
		}
	}
	
	public void doGroupByItem(List<WriteOffDetail> list_Detail, List<ItemWriteOff> list_item) {
		list_item.clear();
		//排序
		for(WriteOffDetail d : list_Detail) {
			System.out.println(d);
		}
		System.out.println("-----------");
		Collections.sort(list_Detail, new ComparatorWriteOffDetailByItem());
		for(WriteOffDetail d : list_Detail) {
			System.out.println(d);
		}
		System.out.println("-----------");
		//合并
		ItemWriteOff item = null;
		for(WriteOffDetail iter:list_Detail) {
			if(null == item || item.equals(iter) != true) {
				if(null != item) {
					list_item.add(item);
				}
				item  = new ItemWriteOff(iter);
			} else if (item.equals(iter) == true){
				item.add(iter);
			}
		}
		if(item != null) {
			list_item.add(item);
		}
	}
	
	public void doGroupByResourceBalance(List<BilActBalanceAddUp> list_Detail, List<BalanceWriteOff> list_bal) {
		list_bal.clear();
		//排序
		for(BilActBalanceAddUp b : list_Detail) {
			System.out.println(b);
		}
		System.out.println("-----------");
		Collections.sort(list_Detail, new ComparatorBilActBalanceAddUpByBalance());
		for(BilActBalanceAddUp b : list_Detail) {
			System.out.println(b);
		}
		System.out.println("-----------");
		//合并
		BalanceWriteOff bal = null;
		for(BilActBalanceAddUp iter:list_Detail) {
			if(null == bal || bal.equals(iter) != true) {
				if(null != bal) {
					list_bal.add(bal);
				}
				bal  = new BalanceWriteOff(iter);
			} else if (bal.equals(iter) == true){
				bal.add(iter);
			}
		}
		if(bal != null) {
			list_bal.add(bal);
		}
	}
	
	public void doGroupResourceByItem(List<BilActBalanceAddUp> list_Detail, List<ItemWriteOff> list_item) {
		list_item.clear();
		//排序
		for(BilActBalanceAddUp d : list_Detail) {
			System.out.println(d);
		}
		System.out.println("-----------");
		Collections.sort(list_Detail, new ComparatorBilActBalanceAddUpByItem());
		for(BilActBalanceAddUp d : list_Detail) {
			System.out.println(d);
		}
		System.out.println("-----------");
		//合并
		ItemWriteOff item = null;
		for(BilActBalanceAddUp iter:list_Detail) {
			if(null == item || item.equals(iter) != true) {
				if(null != item) {
					list_item.add(item);
				}
				item  = new ItemWriteOff(iter);
			} else if (item.equals(iter) == true){
				item.add(iter);
			}
		}
		if(item != null) {
			list_item.add(item);
		}
		System.out.println("=========");
		for(ItemWriteOff i : list_item) {
			System.out.println(i);
		}
		System.out.println("=========");
	}
}
