package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 消息信息配置
 *
 * @author shanwj
 * @date 2020/4/7 9:07
 */
@Data
@TableName("SYS_MSG_TEMPLATE_CONFIG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMsgTemplateConfigDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 小程序消息模板id
     */
    private String templateId;
    /**
     * 是否启用 0不启用1启用
     */
    @TableField("IS_OPEN")
    private Integer open;
    /**
     * 链接路径 如果启用并且有值得情况下使用当前路径否则使用平台默认路径
     */
    private String url;
    /**
     * 平台消息模板id
     */
    private String tid;
    /**
     * 小程序消息模板标题
     */
    private String title;
    /**
     * 小程序消息模板内容
     */
    private String content;
    /**
     * 消息信使类型
     */
    private String type;
    /**
     * 小程序名称
     */
    private String microAppName;
    /**
     * 小程序标识
     */
    private String microAppId;
    /**
     * 小程序密钥
     */
    @TableField(exist = false)
    private String microAppSecret;
    /**
     * 跟踪ID
     */
    private String traceId;
}
