package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
/**
 * 用户角色历史版本数据 实例对象类
 *
 * @author generator
 * @date 2019-10-22 19:08:38
 * @version V1.0
 */
@Data
@TableName("SYS_USER_ROLE_V")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserRoleVDO  {
        /**
         * 主键
         */
        @Id
        @TableId(type = IdType.UUID)
        private String id;
        /**
         * 角色ID
         */
        private String roleId;
        /**
         * 角色名称
         */
        private String roleName;
        /**
         * 主表ID
         */
        private String lvId;

}
