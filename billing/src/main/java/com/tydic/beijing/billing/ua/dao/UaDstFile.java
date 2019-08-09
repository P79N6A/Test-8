/**  
 * Project Name:Develop
 * File Name:UaDstFile.java
 * Package Name:com.tydic.beijing.billing.ua.dao
 * Date:2014年7月11日下午4:29:59
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 *  
 */
package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;

/**
 * ClassName: UaDstFile <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年7月11日 下午4:29:59 <br/>
 * 
 * @author Bradish7Y
 * @version
 * @since JDK 1.6
 */
public class UaDstFile implements Serializable {

	private static final long serialVersionUID = 1L;
	public String file_type;
	public int file_serial;
	public String rule_condition;
	public String rule_file_name;
	public String rule_recycle_name;
	public String dst_dir;
	public int out_type;// 0 normal, 1 output cdr from rba format
	public int head_serial;
	public char head_delimit;
	public int has_head_return;
	public int is_head_fixed;
	public int body_serial;
	public int has_body_return;
	public char body_delimit;
	public int is_body_fixed;
	public int tail_serial;
	public char tail_delimit;
	public int has_tail_return;
	public int is_tail_fixed;

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public int getFile_serial() {
		return file_serial;
	}

	public void setFile_serial(int file_serial) {
		this.file_serial = file_serial;
	}

	public String getRule_condition() {
		return rule_condition;
	}

	public void setRule_condition(String rule_condition) {
		this.rule_condition = rule_condition;
	}

	public String getRule_file_name() {
		return rule_file_name;
	}

	public void setRule_file_name(String rule_file_name) {
		this.rule_file_name = rule_file_name;
	}

	public String getRule_recycle_name() {
		return rule_recycle_name;
	}

	public void setRule_recycle_name(String rule_recycle_name) {
		this.rule_recycle_name = rule_recycle_name;
	}

	public String getDst_dir() {
		return dst_dir;
	}

	public void setDst_dir(String dst_dir) {
		this.dst_dir = dst_dir;
	}

	public final int getOut_type() {
		return out_type;
	}

	public final void setOut_type(int out_type) {
		this.out_type = out_type;
	}

	public int getHead_serial() {
		return head_serial;
	}

	public void setHead_serial(int head_serial) {
		this.head_serial = head_serial;
	}

	public char getHead_delimit() {
		return head_delimit;
	}

	public void setHead_delimit(char head_delimit) {
		this.head_delimit = head_delimit;
	}

	public int getHas_head_return() {
		return has_head_return;
	}

	public void setHas_head_return(int has_head_return) {
		this.has_head_return = has_head_return;
	}

	public int getIs_head_fixed() {
		return is_head_fixed;
	}

	public void setIs_head_fixed(int is_head_fixed) {
		this.is_head_fixed = is_head_fixed;
	}

	public int getBody_serial() {
		return body_serial;
	}

	public void setBody_serial(int body_serial) {
		this.body_serial = body_serial;
	}

	public int getHas_body_return() {
		return has_body_return;
	}

	public void setHas_body_return(int has_body_return) {
		this.has_body_return = has_body_return;
	}

	public char getBody_delimit() {
		return body_delimit;
	}

	public void setBody_delimit(char body_delimit) {
		this.body_delimit = body_delimit;
	}

	public int getIs_body_fixed() {
		return is_body_fixed;
	}

	public void setIs_body_fixed(int is_body_fixed) {
		this.is_body_fixed = is_body_fixed;
	}

	public int getTail_serial() {
		return tail_serial;
	}

	public void setTail_serial(int tail_serial) {
		this.tail_serial = tail_serial;
	}

	public char getTail_delimit() {
		return tail_delimit;
	}

	public void setTail_delimit(char tail_delimit) {
		this.tail_delimit = tail_delimit;
	}

	public int getHas_tail_return() {
		return has_tail_return;
	}

	public void setHas_tail_return(int has_tail_return) {
		this.has_tail_return = has_tail_return;
	}

	public int getIs_tail_fixed() {
		return is_tail_fixed;
	}

	public void setIs_tail_fixed(int is_tail_fixed) {
		this.is_tail_fixed = is_tail_fixed;
	}

	@Override
	public String toString() {
		return "UaDstFile [file_type=" + file_type + ", file_serial=" + file_serial
				+ ", rule_condition=" + rule_condition + ", rule_file_name=" + rule_file_name
				+ ", rule_recycle_name=" + rule_recycle_name + ", dst_dir=" + dst_dir
				+ ", is_internal=" + out_type + ", head_serial=" + head_serial + ", head_delimit="
				+ head_delimit + ", has_head_return=" + has_head_return + ", is_head_fixed="
				+ is_head_fixed + ", body_serial=" + body_serial + ", has_body_return="
				+ has_body_return + ", body_delimit=" + body_delimit + ", is_body_fixed="
				+ is_body_fixed + ", tail_serial=" + tail_serial + ", tail_delimit=" + tail_delimit
				+ ", has_tail_return=" + has_tail_return + ", is_tail_fixed=" + is_tail_fixed + "]";
	}

}
