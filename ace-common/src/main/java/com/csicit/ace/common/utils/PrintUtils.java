package com.csicit.ace.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 调试打印用工具
 * @author yansiyang
 * @version V1.0
 * @date 2019/10/24 16:01
 */
public class PrintUtils {
    /**
     *  调试打印
     * @param o
     * @return
     * @author yansiyang
     * @date 2019/10/24 16:03
     */
    public static void print(Object o) {
        System.out.println("[PrintUtils] ******************************************************");
        System.out.println(JSONObject.toJSONString(o));
        System.out.println("[PrintUtils] ******************************************************");
    }
}
