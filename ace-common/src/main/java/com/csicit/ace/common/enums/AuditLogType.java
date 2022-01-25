package com.csicit.ace.common.enums;

import java.util.Objects;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/5 9:29
 */
public enum AuditLogType {
    success(0, "success"),
    primary(1, "primary"),
    warning(2, "warning"),
    danger(3, "danger");

    AuditLogType(int type, String tag) {
        this.type = type;
        this.tag = tag;
    }

    public static AuditLogType getTypeByTag(String tagT) {
        if (Objects.equals(tagT, "success")) {
            return success;
        }
        if (Objects.equals(tagT, "primary")) {
            return primary;
        }
        if (Objects.equals(tagT, "warning")) {
            return warning;
        }
        if (Objects.equals(tagT, "danger")) {
            return danger;
        }
        return primary;
    }

    private int type;
    private String tag;

    public Integer getValue() {
        return type;
    }

    public String getTag() {
        return tag;
    }
}
