package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 系统管理-用户-可管理的用户组 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:14:32
 * @version V1.0
 */
@Data
@TableName("SYS_USER_ADMIN_USER_GROUP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserAdminUserGroupDO extends AbstractBaseRecordDomain {

        /**
         * 用户主键
         */
        private String userId;
        /**
         * 授权用户组主键(按规则授权时为空)
         */
        private String userGroupId;
        /**
         * 授权用户组规则主键
         */
        private String ruleId;
        /**
         * 是否按规则授权用户组
         */
        @TableField("IS_RULE")
        private Integer rule;
        /**
         * 规则参数
         */
        private String ruleParams;

}
