package com.csicit.ace.platform.core.utils;

import java.util.UUID;

/**
 * 表主键Id自动获取
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/17 10:35
 */
public class UuidUtil {
    public static synchronized String createUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
