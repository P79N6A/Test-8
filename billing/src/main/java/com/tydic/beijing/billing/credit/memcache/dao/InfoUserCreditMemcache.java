package com.tydic.beijing.billing.credit.memcache.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.tydic.beijing.billing.credit.dao.InfoUserCredit;
import com.tydic.uda.UdaAnnotationSetKey;

public class InfoUserCreditMemcache implements Serializable {
	public static final String KEY_PREFIX = InfoUserCreditMemcache.class.getSimpleName();
	private static final long serialVersionUID = 1L;

	public String mem_key;
	public List<InfoUserCredit> infoUserCreditList;

	@Id
	public String getMem_key() {
		return mem_key;
	}

	@UdaAnnotationSetKey
	public void setMem_key(String mem_key) {
		this.mem_key = mem_key;
	}

	public List<InfoUserCredit> getInfoUserCreditList() {
		return infoUserCreditList;
	}

	public void setInfoUserCreditList(List<InfoUserCredit> infoUserCreditList) {
		this.infoUserCreditList = infoUserCreditList;
	}

	public void clear() {
		mem_key = "";
		infoUserCreditList = null;
	}

}
