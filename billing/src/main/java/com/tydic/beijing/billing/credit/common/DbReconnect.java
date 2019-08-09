package com.tydic.beijing.billing.credit.common;

/**
 * Created by lenovo on 2018/6/1.
 */
public  final class DbReconnect {
    public  static  final int DB_RECONNECT_LIMIT_TIMES = 3; //数据库异常情况下，重连次数
    public  static  final int DB_RECONNECT_SLEEP = 6 ;//每次重连休眠时间
}
