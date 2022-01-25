package com.csicit.ace.platform.core.enums;

/**
 * @author shanwj
 * @date 2019/6/3 15:24
 */
public enum FKDeleteActionEnum {
    NoAction(0),
    Delete(1),
    SetNull(2);
    private final int type;
    FKDeleteActionEnum(int type) {
        this.type = type;
    }
}
