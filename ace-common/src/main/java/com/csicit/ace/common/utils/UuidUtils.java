package com.csicit.ace.common.utils;

import java.util.UUID;

public class UuidUtils {
    public static synchronized String createUUID(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
