package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 应用绑定数据源 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:31
 */
@Data
@TableName("SYS_GROUP_DATASOURCE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysGroupDatasourceDO extends AbstractBaseDomain {

    /**
     * 数据库类型
     */
    private String type;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 路径
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 是否为主数据源 1是0不是
     */
    @TableField("IS_MAJOR")
    private Integer major;
    /**
     * 数据源名称
     */
    private String name;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;
}
