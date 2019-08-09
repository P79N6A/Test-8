package com.tydic.beijing.billing.dto;

import java.io.Serializable;

public class Res2AcctProductInfo  implements Serializable {

	private static final long serialVersionUID = 1L;

	private String UserProductId;
	
	private String ProductId;

	public String getUserProductId() {
		return UserProductId;
	}

	public void setUserProductId(String userProductId) {
		UserProductId = userProductId;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}
	
	public String toString(){
		return "Res2AcctProductInfo [productId = " + ProductId + ",UserProductId=" + UserProductId + "]";
	}
	
}
