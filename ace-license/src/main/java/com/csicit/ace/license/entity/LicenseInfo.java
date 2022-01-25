package com.csicit.ace.license.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shanwj
 * @date 2019/6/27 16:31
 */
@Data
public class LicenseInfo implements Serializable {
    /**
     * 主键
     */
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
     * 授权类型 dev/prod
     */
    private String type;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 电脑主板序列号
     */
    private String motherBoardNum;
    /**
     * MAC地址
     */
    private String macAddress;
}
