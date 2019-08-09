package com.tydic.beijing.billing.interfacex.service;

import com.tydic.beijing.billing.dto.ResourceChargeParaIn;
import com.tydic.beijing.billing.dto.ResourceChargeParaOut;

public interface ResourceChargeService {
	public ResourceChargeParaOut charge(ResourceChargeParaIn resourceChargeParaIn) throws Exception;
}

