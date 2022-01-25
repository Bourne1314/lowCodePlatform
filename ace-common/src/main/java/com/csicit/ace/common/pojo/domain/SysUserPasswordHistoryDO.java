package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 密码修正历史 实例对象类
 *
 * @Author: yansiyang
 * @Descruption:
 * @Date: Created in 16:23 2019/4/4
 * @Modified By:
 */
@Data
@TableName("SYS_USER_PASSWORD_HISTORY")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SysUserPasswordHistoryDO {

    public SysUserPasswordHistoryDO(String password, String userId) {
        this.password = password;
        this.userId = userId;
        this.createTime = LocalDateTime.now();
    }

    public SysUserPasswordHistoryDO() {

    }

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户主键
     */
    private String userId;
    /**
     * 记录创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;
}
