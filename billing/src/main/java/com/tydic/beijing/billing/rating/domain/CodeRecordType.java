package com.tydic.beijing.billing.rating.domain;

/**
 * 记录类型代码 code_record_type
 * @author sung
 *
 */
public class CodeRecordType {

	private String record_code; 
	private String  source_type ; 
	private String value_type;
	private String  remarks  ; 
	
	public String getRecord_code() {
		return record_code;
	}
	public void setRecord_code(String record_code) {
		this.record_code = record_code;
	}
	public String getSource_type() {
		return source_type;
	}
	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getValue_type() {
		return value_type;
	}
	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}
	
	
	
}
