package com.csicit.ace.common.ehcache;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/10/13 14:38
 */
@ConditionalOnBean(DiscoveryUtils.class)
@ConditionalOnExpression("'${ace.config.cache.type:redis}'.equals('ehcacheHttp') && !'${spring.application.name}'.equals('platform')")
@Component
public class EhcacheHttpUtils implements CacheUtil {

    @Autowired
    HttpClient client;

    @Override
    public Set<String> getKeys(String pattern) {
        Map<String, Object> params = new HashMap<>();
        params.put("pattern", pattern);
        String result = client.getForEhcache("getKeys", params);
        return JSONObject.parseObject(result, Set.class);
    }

    @Override
    public String getConfigValue(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        String result = client.getForEhcache("getConfigValue", params);
        return result;
    }

    @Override
    public boolean rename(String oldKey, String newKey, boolean ifHasNewKey) {
        Map<String, Object> params = new HashMap<>();
        params.put("oldKey", oldKey);
        params.put("newKey", newKey);
        params.put("ifHasNewKey", ifHasNewKey);
        String result = client.getForEhcache("rename", params);
        return Boolean.parseBoolean(result);
    }

    @Override
    public boolean set(String key, Object value) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        String result = client.postForEhcache("set", params, value);
        return Boolean.parseBoolean(result);
    }

    @Override
    public boolean set(String key, Object value, long expire) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("expire", expire);
        String result = client.postForEhcache("setExpire", params, value);
        return Boolean.parseBoolean(result);
    }

    @Override
    public long getExpire(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        String result = client.getForEhcache("getExpire", params);
        return Long.parseLong(result);
    }

    @Override
    public String get(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        String result = client.getForEhcache("get", params);
        return result;
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
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        String result = client.getForEhcache("delete", params);
        return Boolean.parseBoolean(result);
    }

    @Override
    public boolean expire(String key, long time) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("time", time);
        String result = client.getForEhcache("expire", params);
        return Boolean.parseBoolean(result);
    }

    @Override
    public boolean hasKey(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        String result = client.getForEhcache("hasKey", params);
        return Boolean.parseBoolean(result);
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
    public boolean hHasKey(String key, String item) {
        if (hasKey(key)) {
            Map<String, Object> map = hmget(key);
            return map.containsKey(item);
        }
        return false;
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

    private <T> T fromJson(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }
}
