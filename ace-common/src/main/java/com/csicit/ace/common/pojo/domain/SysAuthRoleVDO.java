package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 系统管理-角色授权版本历史数据 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:24
 */
@Data
@TableName("SYS_AUTH_ROLE_V")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthRoleVDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 权限主键
     */
    private String authId;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 角色主键
     */
    private String roleId;
    /**
     * 签名
     */
    private String sign;
    /**
     * 是否禁用
     */
    @TableField("IS_REVOKE")
    private Integer revoker;
    /**
     * 角色授权版本控制主键
     */
    private String lvId;
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
     * 最后一次修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 跟踪ID
     */
    private String traceId;
}
