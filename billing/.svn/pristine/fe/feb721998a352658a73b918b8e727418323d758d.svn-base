package com.tydic.beijing.billing.iop.service;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

public class JedisUtil {

    public static Jedis createJedis() {
        Jedis jedis = new Jedis("127.0.0.1");
        return jedis;
    }

    public static Jedis createJedis(String host, int port) {
        Jedis jedis = new Jedis(host, port);

        return jedis;
    }

    public static Jedis createJedis(String host, int port, String passwrod) {
        Jedis jedis = new Jedis(host, port);

        if (!StringUtils.isNotBlank(passwrod))
            jedis.auth(passwrod);
        
        return jedis;
    }
}
