package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统审计日志表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-08 16:28:31
 */
@Data
@TableName("SYS_AUDIT_LOG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuditLogDO extends AbstractBaseDomain {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime opTime;
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
     * 集团ID
     */
    private String groupId;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 用户类别
     */
    private String userType;
    /**
     * 该操作人对应的管理员角色
     */
    private String roleId;
}
