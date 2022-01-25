package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.SysMenuDO;

import java.util.List;

/**
 * 菜单接口
 *
 * @author shanwj
 * @date 2019/6/13 18:38
 * @version V1.0
 */
public interface IMenu {
    /** 
     * 获取应用菜单
     *
     * @return 菜单列表
     * @author shanwj
     * @date 2019/6/18 18:11
     */
    List<SysMenuDO> list();

    /**
     * 无权限获取应用菜单
     * @return 无权限显示菜单列表
     * @author xulei
     * @date 2020/6/17 9:40
     */
    List<SysMenuDO> listMenuByNoPermission();


    /**
     * 根据父节点获取子节点应用菜单
     *
     * @param parentId 父节点id
     * @return 菜单列表
     * @author shanwj
     * @date 2019/6/18 18:11
     */
    List<SysMenuDO> listByParentId(String parentId);
}
