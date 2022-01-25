package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单 实例对象访问接口
 * @author yansiyang
 * @date 2019-04-11 14:18:16
 * @version v1.0
 */
@Transactional
public interface SysMenuService extends IBaseService<SysMenuDO> {

    /**
     * 获取树结构的菜单
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:31
     */
    List<SysMenuDO> listMenuTree(String appId);

    /**
     * 获取树结构的菜单 左侧菜单
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:31
     */
    List<SysMenuDO> listSideTree(String appId);

    /**
     * 增加菜单
     * @param menu
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:29
     */
    boolean saveMenu(SysMenuDO menu);

    /**
     * 更新菜单
     * @param menu
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:29
     */
    boolean updateMenu(SysMenuDO menu);

    /**
     * 删除菜单
     * @param params
     * @return 
     * @author yansiyang
     * @date 2019/5/15 8:58
     */
    R delete(Map<String, Object> params);

    /**
     * 移动菜单
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/5/15 8:58
     */
    boolean moveMenu(Map<String, String> params);

    /**
     * 获取单个菜单信息
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:31
     */
    R getMenuInfo(String id);
}
