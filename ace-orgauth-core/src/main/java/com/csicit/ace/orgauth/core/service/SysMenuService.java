package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.dbplus.service.IBaseService;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/6/13 19:05
 */
public interface SysMenuService extends IBaseService<SysMenuDO> {

    /**
     * 获取树结构的菜单 左侧菜单
     *
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:31
     */
    List<SysMenuDO> listSideTree(String appId);

    /**
     * 无权限获取树结构的菜单 左侧菜单
     * @param appId
     * @return
     * @author xulei
     * @date 2019/6/17 9:44
     */
    List<SysMenuDO> listSideTreeByNoPermission(String appId);

    /**
     * 根据父节点获取子节点应用菜单
     *
     * @param parentId
     * @return 菜单列表
     * @author FourLeaves
     * @date 2019/12/11 8:54
     */
    List<SysMenuDO> listByParentId(String appId, String parentId, String userId);

    /**
     * 在应用启动时检查应用菜单和权限是否包含了工作流的菜单和权限，没有的话就新增相应的权限和菜单
     *
     * @param appId
     * @author zuogang
     * @date 2019/12/11 8:54
     */
    boolean setAppFLowMenu(String appId);
}
