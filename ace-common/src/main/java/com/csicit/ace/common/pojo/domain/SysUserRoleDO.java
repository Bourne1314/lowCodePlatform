package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 用户角色关系表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:49
 */
@Data
@TableName("SYS_USER_ROLE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserRoleDO extends AbstractBaseRecordDomain {

    /**
     * 用户主键
     */
    private String userId;
    /**
     * 角色主键
     */
    private String roleId;
    /**
     * 角色类型
     */
    @TableField(exist = false)
    private Integer roleType;
    /**
     * 用户所属集团ID
     */
    @TableField(exist = false)
    private String groupId;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 用户名
     */
    private String realName;
    /**
     * 角色名
     */
    private String roleName;
//    /**
//     * 是否被激活，0没有，1是
//     */
//    @TableField("IS_ACTIVATED")
//    private Integer activated;

}
