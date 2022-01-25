package com.csicit.ace.platform.core.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 对象转化工具类
 * @Author: yansiyang
 * @Descruption:
 * @Date: Created in 14:36 2019/4/11
 * @Modified By:
 */
public class JsonUtils {

    /**
     * 转化对象
     * @param object
     * @param t 目标类
     * @return
     * @author yansiyang
     * @date 2019/4/18 20:15
     */
    public static <T> T castObject(Object object, Class<T> t) {
        String jsonStr = JSONObject.toJSONString(object);
        return JSONObject.parseObject(jsonStr, t);
    }

    /**
     * 转化对象 id置空
     * @param object
     * @param t 目标类
     * @return 
     * @author yansiyang
     * @date 2019/4/18 20:15
     */
    public static <T> T castObjectForSetIdNull(Object object, Class<T> t) {
        String jsonStr = JSONObject.toJSONString(object);
        JSONObject tempJsonObject = JSONObject.parseObject(jsonStr);
        if (tempJsonObject.containsKey("id")) {
            tempJsonObject.put("id", UuidUtil.createUUID());
            jsonStr = JSONObject.toJSONString(tempJsonObject);
        }
        return JSONObject.parseObject(jsonStr, t);
    }
}
