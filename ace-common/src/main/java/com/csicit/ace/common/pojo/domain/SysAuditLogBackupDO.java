package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * 审计日志备份表
 *
 * @author shanwj
 * @date 2019/5/31 14:56
 * @date 2019-05-08 16:28:31
 */
@Data
@TableName("SYS_AUDIT_LOG_BACKUP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuditLogBackupDO extends AbstractBaseDomain {

    /**
     * 类别标签
     */
    private String typeTag;

    /**
     * 类别名称
     */
    private String type;

    /**
     * 标题
     */
    private String title;
    /**
     * 操作员姓名
     */
    private String opName;
    /**
     * 操作员用户名
     */
    private String opUsername;
    /**
     * 操作时间
     */
    private String opTime;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 操作内容
     */
    private String opContent;
    /**
     * 签名
     */
    private String sign;
    /**
     * 员日志id
     */
    private String lid;
    /**
     * 用户类别
     */
    private String userType;
    /**
     * 该操作人对应的管理员角色
     */
    private String roleId;
    /**
     * 集团ID
     */
    private String groupId;
    /**
     * 应用ID
     */
    private String appId;
}
