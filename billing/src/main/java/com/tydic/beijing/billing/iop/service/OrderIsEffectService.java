package com.tydic.beijing.billing.iop.service;

import net.sf.json.JSONObject;
/**
 * 用户对订单发起退订请求，营业系统到计费系统查询用户订单是否有话单，计费系统返回结果
 * @author yanhongxia
 *
 */
public interface OrderIsEffectService {
	public JSONObject getIsEffect(JSONObject input);
}
