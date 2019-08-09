package com.tydic.beijing.billing.iop.service;
/**
 * 营业系统每天凌晨1点生成文件，向计费系统查询用户过期可退订单（指用户订购关系失效48小时到15天内
 * （包括15天），未产生话单的订单），计费系统生成回执文件
 * @author yanhongxia
 *
 */
public interface OrderIsEffectFileService {
	public void getIsEffectFile();
}
