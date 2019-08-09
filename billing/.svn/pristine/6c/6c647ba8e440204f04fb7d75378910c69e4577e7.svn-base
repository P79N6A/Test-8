package com.tydic.beijing.billing.interfacex.sendEdm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: cd-zhanglin
 * Date: 12-12-27
 * Time: 下午6:26
 * To change this template use File | Settings | File Templates.
 */
public enum Result {
    成功(1),
    未知失败(101),
    反序列化失败(102),
    去重失败(103),
    获取模板失败(104),
    获取订单信息失败(105),
    获取用户信息失败(106),
    邮箱地址验证失败(107),
    获取KeyValue失败(108),
    获取ChannelType失败(109),
    插入数据库失败(110),
    获取MessageType失败(111),
    订单过滤失败(112),
    订阅退订过滤失败(113),
    设置mailMessageDomain信息失败(114),
    强验证数据失败(115),
    管道过滤失败(116),
    TOKEN错误(201),
    连接错误(202);
    
    private static ConcurrentHashMap<Integer,Result> map=new ConcurrentHashMap<Integer, Result>(255);

    public int getValue() {
        return value;
    }

    private void setValue(int value){
        this.value=value;
    }

    public static Result getRusult(int value){
        Result result;

        result=map.get(value);
        if(result==null){
            for(Result item :Result.values()){
                if(value==item.getValue()){
                    map.put(item.getValue(),item);
                    result=item;
                    break;
                }
            }
        }

        if(result==null){
            result=Result.未知失败;
        }

        return result;
    }

    private Result(int value) {
        this.value = value;
    }

    private int value;
}
