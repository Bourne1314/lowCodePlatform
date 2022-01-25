package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 系统管理-用户有效权限-授权组织 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:34
 */
@Data
@TableName("SYS_AUTH_SCOPE_ORG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthScopeOrgDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 权限主键
     */
    private String authId;
    /**
     * 用户主键
     */
    private String userId;
    /**
     * 授权组织主键(按规则授权时为空)
     */
    private String organizationId;
    /**
     * 授权组织规则主键
     */
    private String ruleId;
    /**
     * 是否按规则授权组织
     */
    @TableField("IS_RULE")
    private Integer rule;
    /**
     * 规则参数
     */
    private String ruleParams;
    /**
     * 是否被激活，0没有，1是
     */
    @TableField("IS_ACTIVATED")
    private Integer activated;
    /**
     * 角色类型（111应用系统管理员,222应用安全保密员,333应用安全审计员）
     */
    private Integer roleType;

}
