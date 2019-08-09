package com.tydic.beijing.billing.branch.dao;

import java.io.Serializable;

public class CreditSequenceUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	public long duckduckgo;

	public long getDuckduckgo() {
		return duckduckgo;
	}

	public void setDuckduckgo(long duckduckgo) {
		this.duckduckgo = duckduckgo;
	}

	@Override
	public String toString() {
		return "CreditSequenceUtils [duckduckgo=" + duckduckgo + "]";
	}

}
