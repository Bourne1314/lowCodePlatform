package com.csicit.ace.common.utils.redis;

import com.csicit.ace.common.config.RedisConfig;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/12 14:28
 */
@ConditionalOnBean(RedisConfig.class)
@Component
public class RedisUtils implements CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;


    /**
     * redis地址
     */
    @Value("${spring.redis.host}")
    private String redisHost;

    /**
     * redis地址
     */
    @Value("${spring.redis.port:6379}")
    private int redisPort;

    /**
     * redis密码
     */
    @Value("${spring.redis.password:#{null}}")
    private String redisPassword;

    /**
     * 默认过期时长 1天，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 从redis获取配置项的值  应用->集团->租户
     * ************************************
     * 此接口不适用平台
     * *************************************
     *
     * @param key
     * @return
     * @author FourLeaves
     * @date 2019/12/25 16:44
     */
    @Override
    public String getConfigValue(String key) {
        if (StringUtils.isNotBlank(key)) {
            String value = (String) hget(appName, key);
            if (StringUtils.isBlank(value)) {
                String groupId = (String) hget(appName, "groupId");
                if (StringUtils.isNotBlank(groupId)) {
                    value = (String) hget(groupId, key);
                    if (StringUtils.isBlank(value)) {
                        return get(key);
                    }
                }
                return get(key);
            }
            return value;
        }
        return null;
    }

    /**
     * 增加记录并设置过期时间
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public boolean set(String key, Object value, long expire) {
        try {
            valueOperations.set(key, toJson(value));
            if (expire > 0) {
                return expire(key, expire);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }


    /**
     * 增加记录并设置默认过期时间
     *
     * @param key   键
     * @param value 值
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public boolean set(String key, Object value) {
        long milliseconds = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if (milliseconds > 0) {
            valueOperations.set(key, toJson(value), milliseconds, TimeUnit.MILLISECONDS);
            return true;
        }
        return set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 获取值并转化为对象，以及重新刷新过期时间
     *
     * @param key    键
     * @param clazz  返回值对象类型
     * @param expire 过期时间
     * @return 返回值对象
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire > 0) {
            expire(key, expire);
        }
        return value == null ? null : fromJson(value, clazz);
    }

//    /**
//     * 获取值并转化为List对象，以及重新刷新过期时间
//     *
//     * @param key    键
//     * @param expire 过期时间
//     * @return 返回值对象
//     * @author yansiyang
//     * @date 2019/4/12 14:28
//     */
//    @Override
//    public <T> List<T> getList(String key, long expire) {
//        String value = valueOperations.get(key);
//        if (expire > 0) {
//            expire(key, expire);
//        }
//        return value == null ? null : gson.fromJson(value, new TypeToken<List<T>>() {
//        }.getType());
//    }

    /**
     * JSON数据，转成List
     *
     * @param json 字符串
     * @return 对象
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    private <T> List<T> fromJsonList(String json, Class<T> clazz) {
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }


    /**
     * 获取值并转化为对象
     *
     * @param key   键
     * @param clazz 返回值对象类型
     * @return 返回值对象
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    /**
     * 获取值并更新键过期时间
     *
     * @param key    键
     * @param expire 过期时间（秒）
     * @return 值
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire > 0) {
            expire(key, expire);
        }
        return value;
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return value值
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    /**
     * 删除记录
     *
     * @param key 键
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    @Override
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * Object转成JSON数据
     *
     * @param object 对象
     * @return json 字符串
     * @author yansiyang
     * @date 2019/4/12 14:27
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     *
     * @param json  字符串
     * @param clazz 对象类类型
     * @return 对象
     * @author yansiyang
     * @date 2019/4/12 14:28
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 设置过期时间
     *
     * @param key  键
     * @param time 时间（秒）
     * @return boolean true 成功
     * @author yansiyang
     * @date 2019/4/12 14:29
     */
    @Override
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }


    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:29
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 修改key名称
     *
     * @param oldKey      旧键 不能为null
     * @param newKey      新键 不能为null
     * @param ifHasNewKey 如果新键已存在
     * @return true 成功 false失败
     * @author yansiyang
     * @date 2019/4/12 14:30
     */
    @Override
    public boolean rename(String oldKey, String newKey, boolean ifHasNewKey) {
        if (StringUtils.isBlank(oldKey) || StringUtils.isBlank(newKey)) {
            return false;
        }
        if (!hasKey(oldKey)) {
            return false;
        }
        if (hasKey(newKey) && !ifHasNewKey) {
            return false;
        }
        try {
            redisTemplate.rename(oldKey, newKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 判断值是否存在
     *
     * @param key 键 不能为null
     * @return true 存在 false不存在
     * @author yansiyang
     * @date 2019/4/12 14:30
     */
    @Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 获取hashMap的值
     *
     * @param key  记录的键
     * @param item hashMap的键
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:30
     */
    @Override
    public Object hget(String key, String item) {
        return hashOperations.get(key, item);
    }

    /**
     * 获取hashMap的所有键值对
     *
     * @param key
     * @return Map<String   ,       Object>                               ,                                                               Object>
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public Map<String, Object> hmget(String key) {
        return hashOperations.entries(key);
    }

    /**
     * 保存hashMap
     *
     * @param key 键
     * @param map hashMap
     * @return true 保存成功 false 保存失败
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        long milliseconds = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if (milliseconds > 0) {
            redisTemplate.opsForHash().putAll(key, map);
            return redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);

        }
        return hmset(key, map, DEFAULT_EXPIRE);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                return expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 向一张hash表中放入数据, 如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public boolean hset(String key, String item, Object value) {
        long milliseconds = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        if (milliseconds > 0) {
            hashOperations.put(key, item, value);
            return redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);

        }
        return hset(key, item, value, DEFAULT_EXPIRE);
    }


    /**
     * 向一张hash表中放入数据, 如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public boolean hset(String key, String item, Object value, long time) {
        try {
            hashOperations.put(key, item, value);
            if (time > 0) {
                return expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     * @return
     * @date 2019/4/12 14:31
     * @author yansiyang
     */
    public boolean hdelByItem(String key, Object... item) {
        try {
            hashOperations.delete(key, item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 删除hash表
     *
     * @param key 键 不能为null
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public boolean hDelete(String key, String item) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(item) || !hasKey(key) || !hHasKey(key, item)) {
            return true;
        }
        try {
            hashOperations.delete(key, item);
            return hHasKey(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     * @author yansiyang
     * @date 2019/4/12 14:31
     */
    @Override
    public boolean hHasKey(String key, String item) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(item)) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    public void publish(String chanelName, String pubStr) {
        JedisPool jedisPool = StringUtils.isNotBlank(redisPassword) ? new JedisPool(new JedisPoolConfig(), redisHost,
                redisPort, 60000, redisPassword
        ) : new
                JedisPool(new JedisPoolConfig(),
                redisHost,
                redisPort);
        //发布者
        PublisherObservable publisherObservable = new PublisherObservable(jedisPool, chanelName, pubStr);
        PublisherObserver publisherObserver = new PublisherObserver(jedisPool, chanelName, pubStr);
        publisherObservable.addObserver(publisherObserver);
        publisherObservable.run();
    }
}
