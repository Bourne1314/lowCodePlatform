package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.awt.print.PrinterAbortException;
import java.io.Serializable;

/**
 * @author shanwj
 * @date 2020/4/7 9:09
 */
@Data
@TableName("SYS_MICRO_APP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMicroAppDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 主键
     */
    private String name;
    /**
     * 小程序APPID
     */
    private String appId;
    /**
     * 小程序密钥
     */
    private String appSecret;
    /**
     * 小程序token
     */
    private String accessToken;
    /**
     * 小程序类型 2 微信小程序 待扩展
     */
    private String type;
    /**
     * 当前版本
     */
    private String currentVersion;
    /**
     * 审核版本
     */
    private String auditVersion;
    /**
     * 数据版本
     */
    @Version
    private Integer dataVersion;
}
