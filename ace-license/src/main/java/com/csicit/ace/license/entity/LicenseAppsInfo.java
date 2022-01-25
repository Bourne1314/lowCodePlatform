package com.csicit.ace.license.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("LICENSE_APPS_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicenseAppsInfo {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 授权信息主键
     */
    private String licenseInfoId;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 授权截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 授权数量
     */
    private Integer licenseNum;
    /**
     * 序号
     */
    private Integer sortIndex;
}
