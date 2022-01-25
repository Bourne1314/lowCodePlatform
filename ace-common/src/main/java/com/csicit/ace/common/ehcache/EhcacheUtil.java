package com.csicit.ace.common.ehcache;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.google.gson.Gson;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/13 17:03
 */
@ConditionalOnExpression("'${ace.config.cache.type:redis}'.equals('ehcache') || ('${ace.config.cache.type:redis}'.equals('ehcacheHttp') && '${spring.application.name}'.equals('platform'))")
@Component
@EnableCaching
public class EhcacheUtil implements CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(EhcacheUtil.class);

    private final static Gson gson = new Gson();

    /**
     * 缓存名称
     */
    private static final String CACHE_NAME = "ACE_CACHE";


    /**
     * 默认过期时长 1天，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 缓存管理组件
     */
    private static CacheManager ehCacheManager = CacheManager.create();

    /**
     * 缓存实现
     */
    private static Ehcache ehcache;

    /**
     * 缓存数据在失效前的允许闲置时间(单位:秒)
     */
    private static int timeToIdleSeconds = 0;

    /**
     * 缓存数据的总的存活时间（单位：秒）
     */
    private static int timeToLiveSeconds = 0;

    /**
     * 内存中缓存数据的最大数量
     */
    private static long maxEntriesInCacheCount = 1000L;

    /**
     * 硬盘中缓存数据的最大数量
     */
    private static long maxEntriesLocalDiskCount = 100000L;

    @Value("${spring.application.name}")
    private String appName;

    static {
        ehCacheManager.addCache(CACHE_NAME);
        initCacheSettings(ehCacheManager.getCache(CACHE_NAME));
        ehcache = ehCacheManager.getEhcache(CACHE_NAME);
    }

    private static void initCacheSettings(Cache cache) {
        CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
        cacheConfiguration.setMaxEntriesInCache(maxEntriesInCacheCount);
        cacheConfiguration.setMaxEntriesLocalDisk(maxEntriesLocalDiskCount);
        cacheConfiguration.setTimeToIdleSeconds(timeToIdleSeconds);
        cacheConfiguration.setTimeToLiveSeconds(timeToLiveSeconds);
        /**
         * 内存超标往磁盘迁移
         */
        cacheConfiguration.setDiskSpoolBufferSizeMB(100);
        /**
         * 没有时间限制
         */
        cacheConfiguration.setEternal(true);
    }

    @Override
    public Set<String> getKeys(String pattern) {
        List<String> keys = ehcache.getKeys();
        if (StringUtils.isBlank(pattern)) {
            return new HashSet<>(keys);
        }
        Set<String> ketSet = new HashSet<>();
        for (String key : keys) {
            if (key.matches(pattern)) {
                ketSet.add(key);
            }
        }
        return ketSet;
    }

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
            Element element = ehcache.get(oldKey);
            if (delete(oldKey)) {
                long expirationTime = element.getExpirationTime();
                long nowTime = new Date().getTime();
                long time = nowTime - expirationTime;
                int seconds = 1;
                if (time > 1000) {
                    seconds = (int) (time / 1000);
                }
                ehcache.put(new Element(newKey, element.getObjectValue(), false, 0, seconds));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

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

    @Override
    public Map<String, Object> hmget(String key) {
        String value = get(key);
        if (value != null) {
            return fromJson(value, Map.class);
        }
        return new HashMap<>();
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        return set(key, map);
    }

    @Override
    public long getExpire(String key) {
        Element element = ehcache.get(key);
        if (element != null) {
            long expirationTime = element.getExpirationTime();
            long nowTime = new Date().getTime();
            long time = nowTime - expirationTime;
            if (time > 0) {
                return time / 1000;
            }
        }
        return 0;
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
//        return gson.toJson(object);
        return JSONObject.toJSONString(object);
    }


    @Override
    public boolean set(String key, Object value) {
        if (!(value instanceof String)) {
            value = toJson(value);
        }
        if (hasKey(key)) {
            Element element = ehcache.get(key);
            long expirationTime = element.getExpirationTime();
            long nowTime = new Date().getTime();
            long time = expirationTime - nowTime;
            int seconds = 1;
            if (time > 1000) {
                seconds = (int) (time / 1000);
            }
            ehcache.put(new Element(key, value, false, 0, seconds));
        } else {
            ehcache.put(new Element(key, value, false, 0, (int) DEFAULT_EXPIRE));
        }
        return true;
    }

    @Override
    public boolean set(String key, Object value, long expire) {
        if (!(value instanceof String)) {
            value = toJson(value);
        }
        if (expire > 0) {
            ehcache.put(new Element(key, value, false, 0, (int) expire));
        } else {
            ehcache.put(new Element(key, value));
        }
        return true;
    }

    @Override
    public String get(String key) {
        Element element = ehcache.get(key);
        return element == null ? null : (String) element.getObjectValue();
    }

    @Override
    public String get(String key, long expire) {
        expire(key, expire);
        return get(key);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value = get(key);
        return value == null ? null : fromJson(value, clazz);
    }

    @Override
    public <T> T get(String key, Class<T> clazz, long expire) {
        expire(key, expire);
        return get(key, clazz);
    }

    @Override
    public boolean delete(String key) {
        return ehcache.remove(key);
    }

    @Override
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                Element element = ehcache.get(key);
                if (element != null) {
                    element.setTimeToLive((int) time);
                    ehcache.put(element);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException("Ehcache异常");
        }
    }

    @Override
    public boolean hasKey(String key) {
        return ehcache.getQuiet(key) != null;
    }

    @Override
    public Object hget(String key, String item) {
        Map<String, Object> map = get(key, Map.class);
        if (map != null) {
            return map.get(item);
        }
        return null;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        return set(key, map, time);
    }

    @Override
    public boolean hset(String key, String item, Object value) {
        Map<String, Object> map = new HashMap<>();
        if (hasKey(key)) {
            map = hmget(key);
        }
        map.put(item, value);
        return set(key, map);
    }

    @Override
    public boolean hset(String key, String item, Object value, long time) {
        Map<String, Object> map = new HashMap<>();
        if (hasKey(key)) {
            map = hmget(key);
            map.put(item, value);
            return set(key, map, time);
        } else {
            map.put(item, value);
            return hmset(key, map, time);
        }
    }

    @Override
    public boolean hDelete(String key, String item) {
        if (hasKey(key)) {
            Map<String, Object> map = hmget(key);
            map.remove(item);
            return hmset(key, map);
        }
        return true;
    }

    @Override
    public boolean hHasKey(String key, String item) {
        if (hasKey(key)) {
            Map<String, Object> map = hmget(key);
            return map.containsKey(item);
        }
        return false;
    }

    private <T> T fromJson(String json, Class<T> clazz) {
        //return gson.fromJson(json, clazz);
        return JSONObject.parseObject(json, clazz);
    }
}
