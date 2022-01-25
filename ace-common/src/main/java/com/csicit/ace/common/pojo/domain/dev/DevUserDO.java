package com.csicit.ace.common.pojo.domain.dev;

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
 * 用户表
 *
 * @author zuogang
 * @date Created in 16:51 2019/11/27
 */
@Data
@TableName("SYS_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevUserDO implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户姓名
     */
    private String realName;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态：0禁用，1正常
     */
    private Integer applyStatus;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 创建人ID
     */
    private String createUserId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 密级
     */
    private Integer secLevel;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 锁定状态：0未锁定，1锁定
     */
    private Integer lockStatus;
    /**
     * 是否激活IP校验，0否，1是
     */
    @TableField("IS_IP_BIND")
    private Integer ipBind;
    /**
     * 用户排序
     */
    private Integer sortIndex;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;
    /**
     * 上次登录失败时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastFailTime;
    /**
     * 失败次数
     */
    private Integer failTimes;
    /**
     * 用户拥有的角色关系列表
     */
    @TableField(exist = false)
    private List<String> roleIds;

    /**
     * 与项目是否关联：false不关联，true关联
     */
    @TableField(exist = false)
    private boolean relationFlag = false;
}
