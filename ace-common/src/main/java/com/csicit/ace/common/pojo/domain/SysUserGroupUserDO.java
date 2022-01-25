package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 系统管理-用户组与用户关系 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:41
 */
@Data
@TableName("SYS_USER_GROUP_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserGroupUserDO extends AbstractBaseDomain {

    /**
     * 用户主键
     */
    private String userId;
    /**
     * 用户组主键
     */
    private String userGroupId;

}
