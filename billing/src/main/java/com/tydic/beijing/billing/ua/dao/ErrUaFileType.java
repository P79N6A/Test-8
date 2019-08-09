package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;

public class ErrUaFileType implements Serializable {

	private static final long serialVersionUID = 1L;
	public String file_type;// 给vds用，动态表，占位符
	public String raw_file_name;
	public int error_code;
	public int error_pos;
	public String record;
	public int redo_flag;
	public String row_id;

	public final String getFile_type() {
		return file_type;
	}

	public final void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public final String getRaw_file_name() {
		return raw_file_name;
	}

	public final void setRaw_file_name(String raw_file_name) {
		this.raw_file_name = raw_file_name;
	}

	public final int getError_code() {
		return error_code;
	}

	public final void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public final int getError_pos() {
		return error_pos;
	}

	public final void setError_pos(int error_pos) {
		this.error_pos = error_pos;
	}

	public final String getRecord() {
		return record;
	}

	public final void setRecord(String record) {
		this.record = record;
	}

	public final int getRedo_flag() {
		return redo_flag;
	}

	public final void setRedo_flag(int redo_flag) {
		this.redo_flag = redo_flag;
	}

	public final String getRow_id() {
		return row_id;
	}

	public final void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	@Override
	public String toString() {
		return "ErrUaFileType [file_type=" + file_type + ", raw_file_name=" + raw_file_name
				+ ", error_code=" + error_code + ", error_pos=" + error_pos + ", record=" + record
				+ ", redo_flag=" + redo_flag + ", row_id=" + row_id + "]";
	}

	public void clear() {
		file_type = "";
		raw_file_name = "";
		error_code = 0;
		error_pos = 0;
		record = "";
		redo_flag = 0;
		row_id = "";
	}
}
