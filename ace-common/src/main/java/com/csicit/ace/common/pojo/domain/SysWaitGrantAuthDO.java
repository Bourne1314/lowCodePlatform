package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 待激活的用户或角色表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-05 17:37:22
 */
@Data
@TableName("SYS_WAIT_GRANT_AUTH")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysWaitGrantAuthDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户主键或角色主键
     */
    private String activateId;
    /**
     * 0为用户1为角色
     */
    private Integer type;
    /**
     * 应用ID
     */
    private String appId;

}
