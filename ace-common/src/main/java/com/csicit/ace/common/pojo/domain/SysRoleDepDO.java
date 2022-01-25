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
 * 角色-部门关联表
 * @author FourLeaves
 * @version V1.0
 * @date 2021/4/21 8:49
 */
@Data
@TableName("SYS_ROLE_DEP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleDepDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 部门主键
     */
    private String depId;

    /**
     * 部门主键
     */
    @TableField(exist = false)
    private String depName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
