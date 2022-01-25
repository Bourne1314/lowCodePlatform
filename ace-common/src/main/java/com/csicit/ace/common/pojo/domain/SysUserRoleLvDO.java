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
 * 用户角色历史数据主表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-10-22 19:11:13
 */
@Data
@TableName("SYS_USER_ROLE_LV")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserRoleLvDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 是否最新
     */
    @TableField("IS_LATEST")
    private Integer latest;
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
     * 应用ID
     */
    private String appId;
}
