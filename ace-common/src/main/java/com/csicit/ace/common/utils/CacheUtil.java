package com.csicit.ace.common.utils;

import java.util.Map;
import java.util.Set;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/13 17:03
 */
public interface CacheUtil {

    /**
     * 默认过期时长 1天，单位：秒
     */
    long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    long NOT_EXPIRE = -1;

    /**
     * 获取符合条件的key
     * @param pattern 表达式
     * @return 
     * @author FourLeaves
     * @date 2020/4/15 9:41
     */
    Set<String> getKeys(String pattern);

    /**
     * 从redis获取配置项的值  应用->集团->租户
     * ************************************
     * 此接口不适用平台
     * *************************************
     * @param key
     * @return
     * @author FourLeaves
     * @date 2020/4/14 17:54
     */
    String getConfigValue(String key);

    /**
     * 修改key名称
     *
     * @param oldKey      旧键 不能为null
     * @param newKey      新键 不能为null
     * @param ifHasNewKey 如果新键已存在
     * @return true 成功 false失败
     * @author FourLeaves
     * @date 2020/4/15 8:37
     */
    boolean rename(String oldKey, String newKey, boolean ifHasNewKey);

    /**
     * 增加记录
     *
     * @param key   键
     * @param value 值
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:02
     */
    boolean set(String key, Object value);

    /**
     * 增加记录并设置过期时间
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:02
     */
    boolean set(String key, Object value, long expire);

    /**
     * 根据key 获取过期时间
     *
     * @param key 键,不能为null
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:02
     */
    long getExpire(String key);

    /**
     * 根据key获取String类型值
     *
     * @param key 键,不能为null
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:02
     */
    String get(String key);

    /**
     * 根据key获取String类型值并重置过期时间
     *
     * @param key    键,不能为null
     * @param expire 过期时间
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:02
     */
    String get(String key, long expire);

    /**
     * 获取值并转化为对象
     *
     * @param key   键
     * @param clazz 返回值对象类型
     * @return 返回值对象
     * @author FourLeaves
     * @date 2020/4/14 8:02
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取值并转化为对象，以及重新刷新过期时间
     *
     * @param key   键
     * @param clazz 返回值对象类型
     * @return 返回值对象
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    <T> T get(String key, Class<T> clazz, long expire);

//    /**
//     * 获取值并转化为List对象，以及重新刷新过期时间
//     *
//     * @param key    键
//     * @param expire 过期时间
//     * @return 返回值对象
//     * @author FourLeaves
//     * @date 2020/4/14 8:02
//     */
//    <T> List<T> getList(String key, long expire);

    /**
     * 删除记录
     *
     * @param key 键
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean delete(String key);

    /**
     * 重置记录过期时间
     *
     * @param key 键
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean expire(String key, long time);

    /**
     * 判断值是否存在
     *
     * @param key 键 不能为null
     * @return true 存在 false不存在
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean hasKey(String key);

    /**
     * 获取hashMap的值
     *
     * @param key  记录的键
     * @param item hashMap的键
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    Object hget(String key, String item);

    /**
     * 获取hashMap的所有键值对
     *
     * @param key
     * @return Map<String , Object>
     * @author FourLeaves
     * @date 2020/4/14 8:12
     */
    Map<String, Object> hmget(String key);

    /**
     * 保存hashMap
     *
     * @param key 键
     * @param map hashMap
     * @return
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean hmset(String key, Map<String, Object> map);

    /**
     * 保存hashMap并设置过期时间
     *
     * @param key  键
     * @param map  hashMap
     * @param time 过期时间
     * @return true 保存成功 false 保存失败
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean hmset(String key, Map<String, Object> map, long time);

    /**
     * 向一张hash表中放入数据, 如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean hset(String key, String item, Object value);

    /**
     * 向一张hash表中放入数据, 如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean hset(String key, String item, Object value, long time);

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     * @author FourLeaves
     * @date 2020/4/14 8:03
     */
    boolean hHasKey(String key, String item);

    /**
     * 删除hash表中该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     * @author FourLeaves
     * @date 2020/6/23 8:03
     */
    boolean hDelete(String key, String item);
}
