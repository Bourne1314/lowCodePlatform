package com.csicit.ace.common.enums;

import com.csicit.ace.common.utils.StringUtils;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/6 9:36
 */
public class AuditLogTypeDO {
    /**
     * 类型值
     */
    private AuditLogType type;
    /**
     * 类型名称
     */
    private String text;

    public AuditLogTypeDO(String text) {
        this.type = AuditLogType.primary;
        this.text = text;
    }

    public AuditLogTypeDO() {
        this.type = AuditLogType.primary;
        this.text = "其他操作";
    }


    public AuditLogTypeDO(AuditLogType type, String text) {
        this.type = type;
        this.text = text;
    }

    public AuditLogType getType() {
        return type;
    }

    public String getTag() {
        return type.getTag();
    }

    public void setType(AuditLogType type) {
        this.type = type;
    }

    public String getText() {
        return StringUtils.isNotBlank(text) ? text : "未知操作";
    }

    public void setText(String text) {
        this.text = text;
    }
}
