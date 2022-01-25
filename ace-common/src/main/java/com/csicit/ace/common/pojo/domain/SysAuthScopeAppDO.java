package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 系统管理-有效权限-授权应用 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:15:23
 */
@Data
@TableName("SYS_AUTH_SCOPE_APP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthScopeAppDO {
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
         * 集团应用主键
         */
        private String appId;
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
