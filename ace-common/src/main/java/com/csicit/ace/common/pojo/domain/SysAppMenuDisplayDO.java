package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 应用下菜单显示在平台管控台的信息表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-12-09 16:06:18
 */
@Data
@TableName("SYS_APP_MENU_DISPLAY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAppMenuDisplayDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 菜单URL
     */
    private String menuUrl;
    /**
     * 菜单图标
     */
    private String menuIcon;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单序号
     */
    private Integer menuSortIndex;

}
