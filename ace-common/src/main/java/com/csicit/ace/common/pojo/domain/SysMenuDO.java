package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 系统菜单 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:36
 */
@Data
@TableName("SYS_MENU")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuDO extends AbstractBaseRecordDomain {

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 父级菜单id
     */
    private String parentId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 父级菜单名称
     */
    private String parentName;
    /**
     * 菜单路径
     */
    private String url;
    /**
     * 菜单类型-1目录0菜单1按钮
     */
    private Integer type;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 权限id
     */
    private String authId;
    /**
     * 权限标识
     */
    private String btnAuth;
    /**
     * 是否是叶子节点0不是1是
     */
    @TableField("IS_LEAF")
    private Integer isLeaf;
    /**
     * 当前菜单下排序
     */
    private Integer sortIndex;
    /**
     * 全局排序
     */
    private Integer allOrder;
    /**
     * 路径供排序时候使用
     */
    private String sortPath;
    /**
     * 打开方式
     */
    private String openStyle;
    /**
     * 关闭提示0不提示1提示
     */
    private Integer closeNotice;
    /**
     * 叶子数量
     */
    private Integer leaf;
    /**
     * 菜单所属权限0平台1应用
     */
    private Integer menuAuth;
    /**
     * 是否为嵌套iframe 0不是 1是
     */
    @TableField(value = "is_iframe")
    private Integer iframe;
    /**
     * 子菜单列表 左侧菜单用
     */
    @TableField(exist = false)
    private List<?> list;

    /**
     * 子菜单列表
     */
    @TableField(exist = false)
    private List<?> children;

    /**
     * 是否自动创建关联权限（0关联，1不关联）
     */
    @TableField(exist = false)
    private Integer addRelationFlag;

    /**
     * 是否自动更新关联权限名称（0关联，1不关联）
     */
    @TableField(exist = false)
    private Integer updRelationFlag;

    /**
     * 关联权限的名称
     */
    @TableField(exist = false)
    private String authName;
    /**
     * 跟踪ID
     */
    private String traceId;
}
