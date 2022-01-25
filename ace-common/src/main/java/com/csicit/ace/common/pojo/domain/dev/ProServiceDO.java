package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * 应用管理
 *
 * @author shanwj
 * @date 2019/11/25 11:05
 */
@Data
@TableName("PRO_SERVICE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProServiceDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 是否启用权限 1启用0不启用
     */
    @TableField("IS_ENABLE_AUTH")
    private Integer enableAuth;
    /**
     * 是否启用日志 1启用0不启用
     */
    @TableField("IS_ENABLE_LOG")
    private Integer enableLog;
    /**
     * 是否生成过服务，0否，1是
     */
    private Integer createServiceFlg;
    /**
     * 应用发布服务端口
     */
    private String ipPort;
    /**
     * Nacos服务地址
     */
    private String nacosServerAddr;
    /**
     * Redis数据库索引
     */
    private Integer redisDataBase;
    /**
     * Redis服务器地址
     */
    private String redisHost;
    /**
     * Redis连接端口
     */
    private String redisPort;
    /**
     * Redis连接密码
     */
    private String redisPassword;
    /**
     * 项目ID
     */
    private String proInfoId;
    /**
     * 是否删除，0否，1是
     */
    @TableField("IS_DELETE")
    private Integer isDelete;
    /**
     * 该服务下对应的数据模型
     */
    @TableField(exist = false)
    private List<ProModelDO> proModelDOS;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 项目维护人员ID(多人使用;分割)
     */
    private String maintainStaffs;
}
