package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统管理-用户授权 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:45
 */
@Data
@TableName("SYS_AUTH_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthUserDO extends AbstractBaseDomain {

    /**
     * 权限主键
     */
    private String authId;
    /**
     * 用户主键
     */
    private String userId;
    /**
     * 签名
     */
    private String sign;
    /**
     * 是否禁用0不1禁用
     */
    @TableField("IS_REVOKE")
    private Integer revoker;
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
     * 用户名
     */
    private String realName;
    /**
     * 权限名
     */
    private String authName;
    /**
     * 应用ID
     */
    private String appId;
}
