package com.tydic.beijing.billing.rating.service;

import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMsg;

public interface Charging {

	//批价
	public String charge();
	//反算
	public String inverseCharge();
	
	public RateData calcFee ();
	
	public int getIVPNPricingPlanId();
	
	public boolean isNeedCtrl ();
	
	public int checkForbid(RatingData data_, RatingMsg msg_, int nVocFlag_);
	
	public int checkForbid(String strRefType_, int nRefID_, int nVocFlag_);
	
	public int getOfrGroupAndPrior();
	
	public int getOfrGroupFav();
	
	public int getFormulaAndAdjust();
	
	public int rate( RateData result_ );
	
	public String ratingInit() throws Exception;
	
		

	
	public int ratingTerm();
		

	
	public void ratingEventBack();
		

	
	
}
