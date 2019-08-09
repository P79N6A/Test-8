package com.tydic.beijing.billing.rating.domain;

import java.util.Map;

import org.apache.log4j.Logger;
/**
 * 批价配置数据
 * @author dongxuanyi
 *
 */
public class Configure {

	private static Logger log = Logger.getLogger(Configure.class);

	String pInstance;

	String m_strSvcName;
	int nWhich; // 当前在用的是哪份数据
	Map<String, String> iParams1;
	Map<String, String> iParams2;
	Map<String, String> pParams;

	String instance() {
		return pInstance;
	}

	void Configure(String conn_, String name_)// 数据库连接和初始化
	{

	}

	void reload() {
		load();
	}

	void load() {
		log.debug("Configure-load:");

		Map<String, String> pParams;
		if (nWhich == 1)
			pParams = iParams2;
		else
			pParams = iParams1;

		String strParam;
		String strValue;

		if (nWhich == 1)
			nWhich = 2;
		else
			nWhich = 1;
	}

	boolean getParam(String group_, String param_, String value_) {

		String iter = pParams.get(param_);
		if (iter != pParams.get(pParams.size())) {
			value_ = iter;
			return true;
		}

		return false;
	}

	boolean getParam(String group_, String param_, int value_) {
		String iter = pParams.get(param_);
		if (iter != pParams.get(pParams.size())) {
			value_ = Integer.parseInt(iter);
			return true;
		}

		return false;
	}

	boolean getParam(String group_, String param_, long value_) {

		String iter = pParams.get(param_);
		if (iter != pParams.get(pParams.size())) {
			value_ = Long.parseLong(iter);
			return true;
		}

		return false;
	}

}
