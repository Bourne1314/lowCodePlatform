package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 角色用户授予—待激活的用户表
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-05 17:37:22
 */
@Data
@TableName("SYS_WAIT_GRANT_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysWaitGrantUserDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户主键
     */
    private String userId;
    /**
     * 应用ID
     */
    private String appId;

}
