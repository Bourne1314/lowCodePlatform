package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 步骤办理人
 *
 * @author JonnyJiang
 * @date 2019/9/24 18:03
 */
public class StepUser implements Serializable {
    /**
     * 排序号
     */
    private Integer sortIndex;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String realName;
    /**
     * 身份类型（0:主办人,1:协办人）
     */
    private Integer userType;
    /**
     * 身份类型（0:主办人,1:协办人）
     */
    private Integer secretLevel;
    /**
     * 用户
     */
    @JsonIgnore
    private transient SysUserDO sysUser;


    public StepUser(SysUserDO sysUser, String hostId) {
        this.userId = sysUser.getId();
        this.realName = sysUser.getRealName();
        this.secretLevel = sysUser.getSecretLevel();
        Integer userType = UserType.Assistant.getValue();
        if (sysUser.getId().equals(hostId)) {
            userType = UserType.Host.getValue();
        }
        this.userType = userType;
        this.sysUser = sysUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(Integer secretLevel) {
        this.secretLevel = secretLevel;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public SysUserDO getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUserDO sysUser) {
        this.sysUser = sysUser;
    }
}