package com.csicit.ace.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/10/19 10:07
 */
@RequestMapping("/ehcache")
@RestController
public class EhcacheHttpController {

    @Autowired
    CacheUtil cacheUtil;

    /**
     * 获取符合条件的key
     * @param pattern 表达式
     * @author FourLeaves
     * @date 2020/10/19 10:24
     */
    @RequestMapping("/action/getKeys")
    public String getKeys(@RequestParam("pattern") String pattern) {
        try {
            Set<String> keys = cacheUtil.getKeys(pattern);
            return JSONObject.toJSONString(keys);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 修改key名称
     *
     * @param oldKey      旧键 不能为null
     * @param newKey      新键 不能为null
     * @param ifHasNewKey 如果新键已存在
     * @return true 成功 false失败
     * @author FourLeaves
     * @date 2020/10/19 10:24
     */
    @RequestMapping("/action/rename")
    public String rename(@RequestParam("oldKey") String oldKey,
                          @RequestParam("newKey") String newKey,
                          @RequestParam("ifHasNewKey") Boolean ifHasNewKey) {
        try {
            boolean result = cacheUtil.rename(oldKey, newKey, ifHasNewKey);
            return result + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 从缓存获取配置项的值  应用->集团->租户
     * ************************************
     * 此接口不适用平台
     * *************************************
     * @param key
     * @return
     * @author FourLeaves
     * @date 2020/10/19 10:24
     */
    @RequestMapping("/action/getConfigValue")
    public String getConfigValue(@RequestParam("key") String key) {
        try {
            return cacheUtil.getConfigValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键,不能为null
     * @return 
     * @author FourLeaves
     * @date 2020/10/19 10:30
     */
    @RequestMapping("/action/getExpire")
    public String getExpire(@RequestParam("key") String key) {
        try {
            return cacheUtil.getExpire(key) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 增加记录
     *
     * @param key   键
     * @param value 值
     * @return
     * @author FourLeaves
     * @date 2020/10/19 10:41
     */
    @RequestMapping(value = "/action/set", method = RequestMethod.POST)
    public String set(@RequestParam("key") String key,
                       @RequestBody String value) {
        try {
            return cacheUtil.set(key, value) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 增加记录并设置过期时间
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return boolean
     * @author FourLeaves
     * @date 2020/10/19 10:41
     */
    @RequestMapping(value = "/action/setExpire", method = RequestMethod.POST)
    public String set(@RequestParam("key") String key,
                       @RequestBody String value,
                       @RequestParam("expire") Long expire) {
        try {
            return cacheUtil.set(key, value, expire) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 根据key获取String类型值
     *
     * @param key 键,不能为null
     * @return
     * @author FourLeaves
     * @date 2020/10/19 10:41
     */
    @RequestMapping("/action/get")
    public String get(@RequestParam("key") String key) {
        try {
            return cacheUtil.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 删除记录
     *
     * @param key 键
     * @return
     * @author FourLeaves
     * @date 2020/10/19 10:41
     */
    @RequestMapping("/action/delete")
    public String delete(@RequestParam("key") String key) {
        try {
            return cacheUtil.delete(key) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 重置记录过期时间
     *
     * @param key 键
     * @return
     * @author FourLeaves
     * @date 2020/10/19 10:41
     */
    @RequestMapping("/action/expire")
    public String expire(@RequestParam("key") String key,
                          @RequestParam("time") Long time) {
        try {
            return cacheUtil.expire(key, time) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

    /**
     * 判断值是否存在
     *
     * @param key 键 不能为null
     * @return true 存在 false不存在
     * @author FourLeaves
     * @date 2020/10/19 10:41
     */
    @RequestMapping("/action/hasKey")
    public String hasKey(@RequestParam("key") String key) {
        try {
            return cacheUtil.hasKey(key) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClient.EhcacheException;
        }
    }

}
