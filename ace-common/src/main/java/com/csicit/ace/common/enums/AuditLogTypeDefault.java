package com.csicit.ace.common.enums;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/6 15:51
 */
public enum AuditLogTypeDefault {

    ADD("success", "新增"),
    SEARCH("primary", "查询"),
    UPDATE("warning", "更新"),
    DELETE("danger", "删除");


    AuditLogTypeDefault(String tag, String type) {
        this.type = type;
        this.tag = tag;
    }

    private String type;
    private String tag;

    public String getType() {
        return type;
    }

    public String getTag() {
        return tag;
    }
}
