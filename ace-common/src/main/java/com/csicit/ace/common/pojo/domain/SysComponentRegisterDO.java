package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 组件注册 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Data
@TableName("SYS_COMPONENT_REGISTER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysComponentRegisterDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 组件名称
     */
    private String name;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 组件地址路径
     */
    private String componentUrl;
    /**
     * 组件对应的权限ID
     */
    private String authId;
    /**
     * 组件对应的权限名
     */
    @TableField(exist = false)
    private String authName;

    /**
     * 字段1
     */
    private String columnOne;

    /**
     * 字段2
     */
    private String columnTwo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 在首页所占列数
     */
    private Integer colSpan;
    /**
     * 跟踪ID
     */
    private String traceId;
}
