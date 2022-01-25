package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/** 用户角色关系表
 * @author zuogang
 * @date Created in 16:51 2019/11/27
 */
@Data
@TableName("SYS_USER_ROLE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevUserRoleDO {

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
     * 用户ID
     */
    private String userId;
}
