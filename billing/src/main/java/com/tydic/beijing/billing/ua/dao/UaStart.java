package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;

/**
 * 
 * ClassName: UaStart <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年7月11日 下午3:36:18 <br/>
 * 
 * @author Bradish7Y
 * @version
 * @since JDK 1.6
 */
public final class UaStart implements Serializable {
	private static final long serialVersionUID = 1L;
	public String file_type;
	public String pattern;
	public int on_off;

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public int getOn_off() {
		return on_off;
	}

	public void setOn_off(int on_off) {
		this.on_off = on_off;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "UaStart [file_type=" + file_type + ", pattern=" + pattern + ", on_off=" + on_off
				+ "]";
	}
}
