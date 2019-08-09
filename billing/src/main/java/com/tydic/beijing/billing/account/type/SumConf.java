package com.tydic.beijing.billing.account.type;

public class SumConf {
	/*
	 * 1001:100;2002:200
	 * 第一分割符；
	 * 第二分割符：
	 * 费用代码：1001 2002
	 * 费用：		100 200
	 */
	private String tariff_split1;//第一分割符
	private String tariff_split2;//第二分割符
	/*
	 * 1001：1，1，10；2002：2，2，20
	 * 第三分割符，
	 * 账本ID：1001 2002
	 * 账本类型：1    2
	 * 费用：	   10   20
	 */
	private String balance_split1;//第一分割符
	private String balance_split2;//第二分割符
	private String balance_split3;//第三分割符
	
	/*
	 * 1000:1;1001:2
	 * resouce_id: 1000 1001
	 * resouce_value:  1    2
	 */
	private String resource_split1;//第一分割符
	private String resource_split2;//第二分割符
	
	public String getTariff_split1() {
		return tariff_split1;
	}
	public void setTariff_split1(String tariff_split1) {
		this.tariff_split1 = tariff_split1;
	}
	public String getTariff_split2() {
		return tariff_split2;
	}
	public void setTariff_split2(String tariff_split2) {
		this.tariff_split2 = tariff_split2;
	}
	public String getBalance_split1() {
		return balance_split1;
	}
	public void setBalance_split1(String balance_split1) {
		this.balance_split1 = balance_split1;
	}
	public String getBalance_split2() {
		return balance_split2;
	}
	public void setBalance_split2(String balance_split2) {
		this.balance_split2 = balance_split2;
	}
	public String getBalance_split3() {
		return balance_split3;
	}
	public void setBalance_split3(String balance_split3) {
		this.balance_split3 = balance_split3;
	}
	public String getResource_split1() {
		return resource_split1;
	}
	public void setResource_split1(String resource_split1) {
		this.resource_split1 = resource_split1;
	}
	public String getResource_split2() {
		return resource_split2;
	}
	public void setResource_split2(String resource_split2) {
		this.resource_split2 = resource_split2;
	}
}
