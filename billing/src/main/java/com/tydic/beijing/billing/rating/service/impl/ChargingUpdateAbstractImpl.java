/**
 * 
 */
package com.tydic.beijing.billing.rating.service.impl;

import com.tydic.beijing.billing.rating.domain.RBConf;
import com.tydic.beijing.billing.rating.domain.RatableCondCheck;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.service.ChargingUpdate;
import com.tydic.beijing.billing.rating.service.DinnerConversion;
import com.tydic.beijing.billing.rating.service.PricingSectionRate;
import com.tydic.beijing.billing.rating.service.RatableResourceExtraction;
import com.tydic.beijing.billing.rating.service.RateDinnerFormula;

/**
 * @author sung
 *
 */
public abstract class ChargingUpdateAbstractImpl implements ChargingUpdate{

	protected RatingMsg ratingMsg;
	protected RatingData ratingData;
	
	protected RBConf conf=new RBConf();
	protected RatableCondCheck m_pRatableCondCheck;
	protected DinnerConversion  dinnerConv;
	
	protected RatableResourceExtraction ratableResource;
	
	protected RateDinnerFormula   formulaRate;
	
	protected PricingSectionRate   pricingSection;
	
	
	
	
	
	
}
