package com.tydic.beijing.billing.cyclerent.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.dao.*;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 各种查询，非事务处理
 *
 * @author Tian
 */
public final class QueryInfo {

    /**
     * 按用户查询用户支付关系
     *
     * @param userId
     * @return
     */
    public final static List<PayUserRel> getPayUserRelByUserId(String userId) {
        return S.get(PayUserRel.class).query(
                Condition.build("queryByUserId").filter("user_id", userId));
    }

    /**
     * 按用户查询用户实时账单
     *
     * @param userId
     * @param acctMonth
     * @param partitionNo
     * @return
     */
    public final static List<BilActUserRealTimeBill> getRealTimeBillByUserId(
            String userId, int acctMonth, String partitionNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("acct_month", acctMonth);
        map.put("partition_num", partitionNo);
        map.put("user_id", userId);
        return S.get(BilActUserRealTimeBill.class).query(
                Condition.build("queryByUserId").filter(map));
    }

    /**
     * 按账户查询账本
     *
     * @param payId
     * @return
     */
    public final static List<InfoPayBalance> getBalanceByPayId(String payId) {
        return S.get(InfoPayBalance.class).query(
                Condition.build("queryByPayId").filter("pay_id", payId));

    }

    /**
     * 按用户查询出账账单
     *
     * @param userId
     * @return
     */
    public final static List<BilActBill> getActBillByUserId(String userId) {
        return S.get(BilActBill.class).query(
                Condition.build("queryByUserId").filter("user_id", userId));
    }

    /**
     * 查询账务月起始时间
     *
     * @param acctMonth
     * @return
     */
    public final static CodeAcctMonth getAcctMonth(int acctMonth) {
        return S.get(CodeAcctMonth.class).queryFirst(
                Condition.build("queryByAcctMonth").filter("acct_month",
                        acctMonth));
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    public final static InfoUser getUserInfoByUserId(String userId) {
        return S.get(InfoUser.class).queryFirst(
                Condition.build("queryByDeviceNo").filter("device_number",
                        userId));
    }

    /**
     * 获取数据库系统时间/时间戳
     *
     * @return
     */
    public final static SystemTime getDBSystemTimeIssue() {
        return S.get(SystemTime.class).queryFirst(
                Condition.build("getTimestamp"));
    }

   
}
