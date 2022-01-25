package com.csicit.ace.common.utils;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import java.util.Date;

public class JsonToBeanUtils {
    public static <T> T jsonToBean(Class<T> clazz, String JsonString) {
        JSONUtils.getMorpherRegistry().registerMorpher(
                new DateMorpherEx(new String[]{"yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd", "yyyy-MM-dd't'HH:mm:ss"}, (Date) null));//调用DateMorpherEx，defaultValue为null
        JSONObject jsonObject = JSONObject.fromObject(JsonString);
        T entity = (T) JSONObject.toBean(jsonObject, clazz);
        return entity;
    }
}
