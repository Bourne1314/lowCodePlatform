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
 * 系统管理-用户授权版本控制 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Data
@TableName("SYS_AUTH_USER_LV")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthUserLvDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 版本编号
     */
    private Integer versionNo;
    /**
     * 是否最新版本
     */
    @TableField("IS_LAST_VERSION")
    private Integer lastVersion;
    /**
     * 版本启用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime versionBeginTime;
    /**
     * 版本停用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime versionEndTime;
    /**
     * 版本启用人主键
     */
    private String versionBeginUserId;
    /**
     * 版本停用人主键
     */
    private String versionEndUserId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 应用id
     */
    private String appId;
}
