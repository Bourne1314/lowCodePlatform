package com.csicit.ace.common.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Observable;
import java.util.Observer;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/17 11:22
 */
public class PublisherObserver implements Observer {

    private JedisPool jedisPool;

    private String chanelName;

    private String pubStr;

    public PublisherObserver(JedisPool jedisPool,String chanelName,String pubStr){
        this.jedisPool = jedisPool;
        this.chanelName = chanelName;
        this.pubStr = pubStr;
    }
    @Override
    public void update(Observable o, Object arg) {
        Jedis jedis = jedisPool.getResource();   //连接池中取出一个连接
        jedis.publish(chanelName, pubStr);
    }
}
