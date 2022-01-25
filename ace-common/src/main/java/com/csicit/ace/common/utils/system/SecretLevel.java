package com.csicit.ace.common.utils.system;

import java.io.Serializable;

/**
 * 密级
 *
 * @author JonnyJiang
 * @date 2019/9/27 8:48
 */
public class SecretLevel implements Serializable {
    /**
     * 密级值
     */
    private Integer value;
    /**
     * 密级名称
     */
    private String name;

    /**
     * @param value 密级值
     * @param name  密级名称
     */
    public SecretLevel(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 判断密级是否相同
     *
     * @param secretLevel 密级
     * @return
     */
    public Boolean equals(Integer secretLevel) {
        return value.equals(secretLevel);
    }

    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }
}
