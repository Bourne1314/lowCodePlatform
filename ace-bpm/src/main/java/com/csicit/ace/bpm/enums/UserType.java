package com.csicit.ace.bpm.enums;

import com.csicit.ace.bpm.exception.UnsupportedUserTypeException;

/**
 * 人员类型
 *
 * @author JonnyJiang
 * @date 2019/9/17 10:33
 */
public enum UserType {
    /**
     * 无关人员
     */
    None(-1),
    /**
     * 主办人
     */
    Host(0),
    /**
     * 协办人
     */
    Assistant(1);

    private Integer value;

    UserType(Integer value) {
        this.value = value;
    }

    public static UserType getByValue(Integer userType) {
        if (None.isEquals(userType)) {
            return None;
        } else if (Host.isEquals(userType)) {
            return Host;
        } else if (Assistant.isEquals(userType)) {
            return Assistant;
        }
        throw new UnsupportedUserTypeException(userType);
    }

    public Integer getValue() {
        return this.value;
    }

    public Boolean isEquals(Integer userType) {
        return this.value.equals(userType);
    }
}