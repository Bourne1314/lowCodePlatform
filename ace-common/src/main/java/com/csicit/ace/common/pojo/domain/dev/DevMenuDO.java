package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单表
 *
 * @author zuogang
 * @date Created in 16:51 2019/11/27
 */
@Data
@TableName("SYS_MENU")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevMenuDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 父主键
     */
    private String parentId;
    /**
     * 菜单名
     */
    private String name;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;
    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单标识
     */
    private String code;
    /**
     * 排序号
     */
    private String sortIndex;
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
     * 子菜单列表
     */
    @TableField(exist = false)
    private List<?> children;

    /**
     * 与角色关联：false不关联，true关联
     */
    @TableField(exist = false)
    private boolean relationFlag = false;

    /**
     * 子菜单列表 左侧菜单用
     */
    @TableField(exist = false)
    private List<?> list;


}
