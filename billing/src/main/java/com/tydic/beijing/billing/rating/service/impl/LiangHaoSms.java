package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class LiangHaoSms {

	@Autowired
	private LiangHaoSendMsg senmsg;

	public static final Logger log = LoggerFactory.getLogger(LiangHaoSms.class);

	public void LiangHaoSendSms() throws Exception {
		log.debug("==============出账完成后进行新靓号预存用户的短信提醒================");
		// 查找当前有靓号预存的用户
		List<PayUserRel> listPayUserRel = new ArrayList<PayUserRel>();
		List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
		List<InfoPayBalance> listInfoPayBalances = new ArrayList<InfoPayBalance>();
		List<InfoUser> listLiangHaoInfoUser = S.get(InfoUser.class).query(Condition.build("queryLiangHaoInfoUser"));
		if (listLiangHaoInfoUser == null || listLiangHaoInfoUser.size() == 0) {
			log.debug("==========目前没有靓号预存用户！！！");
		} else if (listLiangHaoInfoUser != null && listLiangHaoInfoUser.size() > 0) {
			for (InfoUser info : listLiangHaoInfoUser) {
				long allbalance = 0;
				long allrealbalance = 0;
				listPayUserRel = S.get(PayUserRel.class)
						.query(Condition.build("queryPayUserRelByUserId").filter("userid", info.getUser_id()));
				if (listPayUserRel == null || listPayUserRel.size() == 0) {
					log.error("====用户:" + info.getUser_id() + " 没有主账户！！！====");
				} else {
					PayUserRel payuserrel = listPayUserRel.get(0);
					String payId = payuserrel.getPay_id();
					listInfoPayBalance = S.get(InfoPayBalance.class)
							.query(Condition.build("queryLiangHaoBalance").filter("payid", payId));
					if (listInfoPayBalance == null || listInfoPayBalance.size() == 0) {
						log.debug("用户：" + info.getUser_id() + "不是新靓号预存用户！");
					} else if (listInfoPayBalance != null && listInfoPayBalance.size() > 0) {
						int flag = 0;
						listInfoPayBalances.clear();
						log.debug("将余额大于0的账本单独列出来");
						for (InfoPayBalance infopaybalance : listInfoPayBalance) {
							if (infopaybalance.getReal_balance() > 0) {
								log.debug("余额是：" + infopaybalance.getReal_balance());
								listInfoPayBalances.add(infopaybalance);
							}
						}
						Date expdate = null;
						String expdateStr_date = "";
						if (listInfoPayBalances != null && listInfoPayBalances.size() > 0) {
							log.debug("新靓号用户账户有余额账本" + listInfoPayBalances.size() + "个");
							expdate = listInfoPayBalances.get(0).getExp_date();// 最小的有效期
							expdateStr_date = expStrTime(expdate);
							log.debug("将要比较有效期==============" + expdate + "转换后:" + expdateStr_date);
							for (InfoPayBalance infopaybalance : listInfoPayBalances) {
								allbalance += infopaybalance.getReal_balance();
								Date expdatebal = infopaybalance.getExp_date();
								String expdateStr1_date = expStrTime(expdatebal);
								log.debug("比较有效期==============" + expdatebal + "转换后:" + expdateStr1_date);
								if (!expdateStr_date.equals(expdateStr1_date)) {
									log.debug("用户新靓号账本有效期不同");
									flag = 1;
									break;
								}
							}
						}
						if (allbalance <= 0) {
							log.debug("新靓号用户没有余额了");
							continue;
						}
						if (flag == 0) {
							log.debug("有效期相同");
							allrealbalance = allbalance;
						} else if (flag == 1) {
							log.debug("有效期不相同，得到有效期最小的账本总余额=========");
							expdate = listInfoPayBalances.get(0).getExp_date();// 最小的有效期
							expdateStr_date = expStrTime(expdate);
							for (InfoPayBalance infoPayBalance : listInfoPayBalances) {
								Date expdatebal = infoPayBalance.getExp_date();
								String expdateStr1_date = expStrTime(expdatebal);
								if (expdateStr_date.equals(expdateStr1_date)) {
									allrealbalance += infoPayBalance.getReal_balance();
								}
							}
						}
						// 像用户发送短信
						log.debug("==========开始发送短信=============");

						senmsg.senSmsToLiangHao(info, allrealbalance, expdate);
					}
				}

			}
		}
	}

	// 日期转换
	private String expStrTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String expdateStr = dateFormat.format(date);
		String expdateStr_date = expdateStr.substring(0, 8);
		return expdateStr_date;
	}

}
