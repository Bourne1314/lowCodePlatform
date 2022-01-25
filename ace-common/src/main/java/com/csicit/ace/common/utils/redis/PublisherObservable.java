package com.csicit.ace.common.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Observable;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/17 11:27
 */
public class PublisherObservable extends Observable implements Runnable {
    private JedisPool jedisPool;

    private String chanelName;

    private String pubStr;


    public PublisherObservable() {

    }
    public PublisherObservable(JedisPool jedisPool,String chanelName,String pubStr) {
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
