package com.tydic.beijing.billing.interfacex.service;

import com.tydic.beijing.billing.dto.ResourceAdjustRequest;
import com.tydic.beijing.billing.dto.ResourceAdjustResponse;

/**
 * 
 * @author Tian
 *
 */
public interface ResourceAdjust {
	public ResourceAdjustResponse doProcess(ResourceAdjustRequest rar);
}
