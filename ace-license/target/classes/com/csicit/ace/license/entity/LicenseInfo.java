package com.csicit.ace.license.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/6/27 16:31
 */
@Data
@TableName("LICENSE_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicenseInfo implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 授权单位
     */
    private String unitName;
    /**
     * 授权信息
     */
    private String licenseInfo;
    /**
     * 授权截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
//    /**
//     * 授权类型 dev/prod
//     */
//    @TableField(exist = false)
//    private String type;
    /**
     * 签名 加密后MD5密文
     */
    private String cipher;
    /**
     * 授权应用
     */
    @TableField(exist = false)
    private List<LicenseAppsInfo> apps;

    /**
     * 电脑主板序列号
     */
    private String mbNum;
    /**
     * MAC地址
     */
    private String macAddress;
    /**
     * IP地址
     */
    private String localIp;
    /**
     * 硬盘序列号
     */
    private String diskNum;
    /**
     * CPU编号
     */
    private String cpuNum;
}
