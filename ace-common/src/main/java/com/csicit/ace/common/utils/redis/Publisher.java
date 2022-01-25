package com.csicit.ace.common.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author shanwj
 * @date 2019/7/10 18:12
 */
public class Publisher extends Thread {

    private JedisPool jedisPool;

    private String chanelName;

    private String pubStr;

    public Publisher(JedisPool jedisPool,String chanelName,String pubStr) {
        this.jedisPool = jedisPool;
        this.chanelName = chanelName;
        this.pubStr = pubStr;
    }

    @Override
    public void run() {
        Jedis jedis = jedisPool.getResource();   //连接池中取出一个连接
        jedis.publish(chanelName, pubStr);
    }

}
