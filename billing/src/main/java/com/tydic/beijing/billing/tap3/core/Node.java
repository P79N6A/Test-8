package com.tydic.beijing.billing.tap3.core;

public class Node {
	public String nodeName;
	public boolean isNode = false;// default
	public int tag = 0X00;// default
	public Type fieldType = Type.ASCIISTRING;// default
	public String errorCode = "0";// default
	public boolean isSequenceOf = false;
	public boolean isOP = false;// output flag
	/**
	 * sequence multipValue
	 */
	public boolean isM = false;

	@Override
	public String toString() {
		return "Node [nodeName=" + nodeName + ", isNode=" + isNode + ", tag=" + tag
				+ ", fieldType=" + fieldType + ", errorCode=" + errorCode + ", isSequenceOf="
				+ isSequenceOf + ", isOP=" + isOP + "]";
	}

}
