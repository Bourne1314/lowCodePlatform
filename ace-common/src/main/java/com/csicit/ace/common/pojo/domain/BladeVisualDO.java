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
 * 大屏信息数据表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 09:59:54
 */
@Data
@TableName("BLADE_VISUAL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BladeVisualDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 大屏标题
     */
    private String title;
    /**
     * 大屏背景
     */
    private String backgroundInfo;
    /**
     * 业务类型ID
     */
    private String category;
    /**
     * 发布密码
     */
    private String password;
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
     * 修改人
     */
    private String updateUser;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 逻辑删除 0 否 1是
     */
    @TableField(value = "IS_DELETED")
    private Integer beDeleted;
    /**
     * 配置json
     */
    private String detail;
    /**
     * 组件json
     */
    private String component;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 权限标识
     */
    private String authId;
    /**
     * 关联权限
     */
    @TableField(exist = false)
    private String authName;
    /**
     * 跟踪ID
     */
    private String traceId;
}
