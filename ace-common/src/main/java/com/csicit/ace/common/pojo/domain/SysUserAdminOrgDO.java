package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 系统管理-用户-可管理的组织 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:14:22
 * @version V1.0
 */
@Data
@TableName("SYS_USER_ADMIN_ORG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserAdminOrgDO extends AbstractBaseDomain {

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
         * 规则参数
         */
        private String ruleParams;
        /**
         * 是否按规则授权组织
         */
        @TableField("IS_RULE")
        private Integer rule;
        /**
         * 是否被激活，0没有，1是
         */
        @TableField("IS_ACTIVATED")
        private Integer activated;
        /**
         * 角色类型（11集团系统管理员,22集团安全保密员,33集团安全审计员）
         */
        private Integer roleType;



}
