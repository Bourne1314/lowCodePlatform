package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shanwj
 * @date 2020/4/7 9:08
 */
@Data
@TableName("SYS_MSG_TEMPLATE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMsgTemplateDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 模板标题
     */
    private String templateTitle;
    /**
     * 使用权限
     */
    private String auth;
    /**
     * 模板内容
     */
    private String templateContent;
    /**
     * 消息连接
     */
    private String url;
    /**
     * 创建人id
     */
    private String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 数据版本
     */
    @Version
    private Integer dataVersion;

    /**
     * 应用ID
     */
    private String appId;
    /**
     * 调用权限名称
     */
    @TableField(exist = false)
    private String authName;
    /**
     * 小程序类型 小程序模板导入时使用
     */
    @TableField(exist = false)
    private String type;
    /**
     * 消息信使模板列表
     */
    @TableField(exist = false)
    List<SysMsgTemplateConfigDO> templateConfigs;
    /**
     * 信使模板
     */
    @TableField(exist = false)
    SysMsgTemplateConfigDO templateConfig;
    /**
     * 跟踪ID
     */
    private String traceId;
}
