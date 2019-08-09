package com.tydic.beijing.billing.rating.service;

import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;


public interface PricingSectionRate {

	public void init();
	public OfrRateData rateOfr(PlanDisct plan  ,OfrRateData rateData) throws RatingException;
}
