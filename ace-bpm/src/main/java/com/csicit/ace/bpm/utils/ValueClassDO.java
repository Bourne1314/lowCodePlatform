package com.csicit.ace.bpm.utils;

import lombok.Data;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/27 8:55
 */
@Data
public class ValueClassDO {

    public ValueClassDO(){};

    public ValueClassDO(String value, Class aClass){
        this.value = value;
        this.aClass = aClass;
    };

    private String value;

    private Class aClass;
}
