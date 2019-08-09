package com.tydic.beijing.bvalue.common;

import org.codehaus.jackson.map.PropertyNamingStrategy.PropertyNamingStrategyBase;

public class CapitalizedPropertyNamingStrategy extends PropertyNamingStrategyBase {

	@Override
	public String translate(String propertyName) {
		String name = propertyName.replaceAll("^\\w", propertyName.toUpperCase().substring(0,1));
        return name;

	}

}
