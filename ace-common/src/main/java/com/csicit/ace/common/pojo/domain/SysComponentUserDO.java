package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 普通用户登录应用首页组件排版信息 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Data
@TableName("SYS_COMPONENT_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysComponentUserDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 组件信息
     */
    private String componentInfo;
    /**
     * 应用ID
     */
    private String appId;

    /**
     * 用户ID
     */
    private String userId;


}
