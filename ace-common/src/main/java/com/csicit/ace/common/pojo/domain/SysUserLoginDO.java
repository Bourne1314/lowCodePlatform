package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 用户登录 实例对象类
 *
 * @Author: yansiyang
 * @Descruption:
 * @Date: Created in 17:34 2019/4/9
 * @Modified By:
 */
@Data
@TableName("SYS_USER_LOGIN")
public class SysUserLoginDO {

    public SysUserLoginDO(String userId,String userName, Integer status) {
        this.userId = userId;
        this.status = status;
        this.userName = userName;
        this.loginTime = LocalDateTime.now();
    }

    public SysUserLoginDO() {
    }

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
     * 用户ID
     */
    private String userId;
    /**
     * 登录状态 0 失败 1成功
     */
    private Integer status;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

}
