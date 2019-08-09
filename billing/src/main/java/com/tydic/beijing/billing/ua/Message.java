/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * RBA消息解析、组装<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class Message {
	/**
	 * 
	 * inner class, represents of a group of rba message<br/>
	 * 
	 * @author Bradish7Y
	 * @since JDK 1.7
	 *
	 */
	public class Fields {
		public String fieldName = null;
		public String fieldValue = null;
		// 判断末尾是']' or ',' or ';'
		public int tailSign = -1;// 0 ']', 1 ',' 2 ';'

		@Override
		public String toString() {
			return "Fields [fieldName=" + fieldName + ", fieldValue=" + fieldValue + ", tailSign="
					+ tailSign + "]";
		}
	}

	private LinkedHashMap<String, Vector<Fields>> message = null;

	public Message() {
		message = new LinkedHashMap<String, Vector<Fields>>();
	}

	/**
	 * 
	 * addValue:加入非组的. <br/>
	 * 
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            值
	 * @param type
	 *            判断末尾是']' or ',' or ';'，正确配置是：0 ']', 1 ',' 2 ';'
	 */
	public void addValue(String fieldName, String fieldValue, int type) {
		Vector<Fields> v = message.get(fieldName);
		if (v == null) {
			v = new Vector<Fields>();
			message.put(fieldName, v);
		}
		Fields f = new Fields();
		f.fieldName = fieldName;
		f.fieldValue = fieldValue;
		f.tailSign = type;
		v.add(f);
	}

	/**
	 * 
	 * p:加入非组的. <br/>
	 * 
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            值
	 * @param type
	 *            判断末尾是']' or ',' or ';'，正确配置是：0 ']', 1 ',' 2 ';'
	 */
	public void p(String fieldName, String fieldValue, int type) {
		Vector<Fields> v = message.get(fieldName);
		if (v == null) {
			v = new Vector<Fields>();
			message.put(fieldName, v);
		}
		Fields f = new Fields();
		f.fieldName = fieldName;
		f.fieldValue = fieldValue;
		f.tailSign = type;
		v.add(f);
	}

	/**
	 * 
	 * addValue:加入组的. <br/>
	 * 
	 * @param groupName
	 *            组名
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            值
	 * @param type
	 *            判断末尾是']' or ',' or ';'，正确配置是：0 ']', 1 ',' 2 ';'
	 */
	public void addValue(String groupName, String fieldName, String fieldValue, int type) {
		Vector<Fields> v = message.get(groupName);
		if (v == null) {
			v = new Vector<Fields>();
			message.put(groupName, v);
		}
		Fields f = new Fields();
		f.fieldName = fieldName;
		f.fieldValue = fieldValue;
		f.tailSign = type;
		v.add(f);
	}

	/**
	 * 
	 * p:加入组的. <br/>
	 * 
	 * @param groupName
	 *            组名
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            值
	 * @param type
	 *            判断末尾是']' or ',' or ';'，正确配置是：0 ']', 1 ',' 2 ';'
	 */
	public void p(String groupName, String fieldName, String fieldValue, int type) {
		Vector<Fields> v = message.get(groupName);

		if (v == null) {
			v = new Vector<Fields>();
			message.put(groupName, v);
		}
		Fields f = new Fields();
		f.fieldName = fieldName;
		f.fieldValue = fieldValue;
		f.tailSign = type;
		v.add(f);
	}

	private int curPos = 0;// 当前位置
	private int length = 0;// 长度

	/**
	 * 
	 * getFieldName:返回'='的左值.<br/>
	 * 
	 * @param msg
	 *            要解析的消息
	 * @return 返回'='的左值
	 */
	private String getFieldName(String msg) {
		StringBuffer sb = new StringBuffer();
		while (curPos < length - 1) {
			char chr = msg.charAt(++curPos);
			if (chr == '=') {
				break;
			}
			sb.append(chr);
		}
		return sb.toString();
	}

	/**
	 * 
	 * getFieldValue:返回'='的右值.<br/>
	 * 
	 * @param msg
	 *            要解析的消息
	 * @return 返回'='的右值
	 */
	private String getFieldValue(String msg) {
		StringBuffer sb = new StringBuffer();
		while (curPos < length) {
			char chr = msg.charAt(++curPos);
			if (chr == ']') {
				break;
			} else if (chr == ',' || chr == ';') {
				break;
			}
			sb.append(chr);
		}
		return sb.toString();
	}

	private void skipSgin() {
		if (curPos < length) {
			++curPos;
		}
	}

	/**
	 * 
	 * convert:vector to map.<br/>
	 * 
	 * @param vec
	 */
	private void convert(Vector<Vector<Fields>> fieldVector) {
		for (Vector<Fields> v : fieldVector) {
			message.put(v.get(0).fieldName, v);
		}
	}

	/**
	 * 
	 * parse:解析RBA消息. <br/>
	 * 
	 * @param msg
	 *            rba消息
	 * @return true ok <br/>
	 *         false error，the sign not exits in list of ('[],;=')
	 */
	public boolean parse(String msg) {
		length = msg.length();
		Fields f = null;
		Vector<Vector<Fields>> fieldVector = new Vector<Vector<Fields>>();
		Vector<Fields> field = new Vector<Fields>();

		while (curPos < length) {
			char chr = msg.charAt(curPos);
			switch (chr) {
			case '[':
				f = new Fields();
				f.fieldName = getFieldName(msg);
				break;
			case ']':
				f.tailSign = 0;
				field.add(f);
				fieldVector.add(field);
				skipSgin();
				field = new Vector<Fields>();
				break;
			case ',':
				f.tailSign = 1;
				field.add(f);
				f = new Fields();
				f.fieldName = getFieldName(msg);
				break;
			case ';':
				f.tailSign = 2;
				field.add(f);
				f = new Fields();
				f.fieldName = getFieldName(msg);
				break;
			case '=':
				f.fieldValue = getFieldValue(msg);
				break;
			default:
				return false;
			}
		}// end while

		// 转换容器
		convert(fieldVector);
		return true;
	}

	/**
	 * 
	 * toData:组装消息<br/>
	 * 
	 * @return 返回RBA消息的字符串格式
	 */
	public String toData2() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Vector<Fields>> e : message.entrySet()) {
			sb.append("[");
			for (Fields f : e.getValue()) {
				sb.append(f.fieldName);
				sb.append('=');
				sb.append(f.fieldValue);
				if (f.tailSign == 2) {
					sb.append(';');
				} else if (f.tailSign == 1) {
					sb.append(',');
				} else {
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}

	public String toData() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Vector<Fields>> e : message.entrySet()) {
			sb.append("[");
			Vector<Fields> f = e.getValue();
			int length = f.size();
			for (int i = 0; i < length; i++) {
				sb.append(f.get(i).fieldName);
				sb.append('=');
				sb.append(f.get(i).fieldValue);
				if (f.get(i).tailSign == 2) {
					if (i == length - 1) {
						sb.append("]");
					} else {
						sb.append(';');
					}
				} else if (f.get(i).tailSign == 1) {
					if (i == length - 1) {
						sb.append("]");
					} else {
						sb.append(',');
					}
				} else {
					if (i == length - 1) {
						sb.append("]");
					}
				}
			}

		}
		return sb.toString();
	}

	/**
	 * 
	 * clear:清空sorted map.<br/>
	 *
	 */
	public void clear() {
		message.clear();
		curPos = 0;
		length = 0;
	}

	/**
	 * 
	 * getValue:返回指定消息.<br/>
	 * 
	 * @param fieldName
	 *            key值
	 * @return 返回指定消息数组给javascript处理
	 */
	public Fields[] getValue2(String fieldName) {
		Vector<Fields> v = message.get(fieldName);
		Fields[] fs = new Fields[v.size()];
		for (int i = 0; i < v.size(); i++) {
			fs[i] = v.get(i);
		}
		return fs;
	}

	public MapWrapper[] getValue(String fieldName) {
		ArrayList<MapWrapper> ret = new ArrayList<MapWrapper>();
		Vector<Fields> v = message.get(fieldName);
		if (v == null) {
			return null;
		}
		HashMap<String, String> tmp = new HashMap<String, String>();
		MapWrapper mw = new MapWrapper(tmp);

		for (Fields e : v) {
			mw.p(e.fieldName, e.fieldValue);

			if (e.tailSign == 2 || e.tailSign == 0) {
				ret.add(mw);
				tmp = new HashMap<String, String>();
				mw = new MapWrapper(tmp);
			}
		}
		return ret.toArray(new MapWrapper[ret.size()]);
	}
}
