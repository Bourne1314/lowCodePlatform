package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.DevMenuDO;
import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import com.csicit.ace.common.pojo.domain.dev.DevRoleDO;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.DevRoleMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.DevMenuRoleService;
import com.csicit.ace.dev.service.DevMenuService;
import com.csicit.ace.dev.service.DevRoleService;
import com.csicit.ace.dev.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("devRoleService")
public class DevRoleServiceImpl extends BaseServiceImpl<DevRoleMapper, DevRoleDO> implements
        DevRoleService {
    @Autowired
    DevMenuRoleService devMenuRoleService;

    @Autowired
    DevMenuService devMenuService;
    @Autowired
    ShiroService shiroService;
    /**
     * 获取角色信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public DevRoleDO getRoleInfo(String id) {
        DevRoleDO roleDO = getById(id);
        List<DevMenuDO> sysMenuDOS = devMenuService.list(new QueryWrapper<DevMenuDO>()
                .orderByAsc("sort_index"));
        sysMenuDOS.stream().forEach(menuDO -> {
            if (devMenuRoleService.count(new QueryWrapper<DevMenuRoleDO>()
                    .eq("role_id", id).eq("menu_id", menuDO.getId())) > 0) {
                menuDO.setRelationFlag(true);
            } else {
                menuDO.setRelationFlag(false);
            }
        });
        List<DevMenuDO> listT = TreeUtils.makeTree(sysMenuDOS, DevMenuDO.class);
        roleDO.setMenus(listT);
        return roleDO;
    }

    /**
     * 保存角色
     *
     * @param sysRoleDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public boolean saveRole(DevRoleDO sysRoleDO) {
        sysRoleDO.setCreateTime(LocalDateTime.now());
        if (!save(sysRoleDO)) {
            return false;
        }

        return saveMenuRole(sysRoleDO);
    }

    /**
     * 更新角色菜单关系
     *
     * @param sysRoleDO
     * @return
     * @author zuogang
     * @date 2019/11/28 8:01
     */
    private boolean saveMenuRole(DevRoleDO sysRoleDO) {
        devMenuRoleService.remove(new QueryWrapper<DevMenuRoleDO>()
                .eq("role_id", sysRoleDO.getId()));
        if (sysRoleDO.getMenus() != null && sysRoleDO.getMenus().size() > 0) {
            List<DevMenuRoleDO> sysMenuRoleDOS = new ArrayList<>(16);
            sysRoleDO.getMenus().stream().forEach(menuDO -> {
                DevMenuRoleDO sysMenuRoleDO = new DevMenuRoleDO();
                sysMenuRoleDO.setId(UuidUtils.createUUID());
                sysMenuRoleDO.setMenuId(menuDO.getId());
                sysMenuRoleDO.setRoleId(sysRoleDO.getId());
                sysMenuRoleDOS.add(sysMenuRoleDO);
            });
            if (!devMenuRoleService.saveBatch(sysMenuRoleDOS)) {
                return false;
            }
        }
        // 更新shiro权限
        shiroService.updatePermissionByRoleId(sysRoleDO.getId(), false);
        return true;
    }

    /**
     * 更新角色
     *
     * @param sysRoleDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public boolean updateRole(DevRoleDO sysRoleDO) {
        sysRoleDO.setUpdateTime(LocalDateTime.now());
        if (!updateById(sysRoleDO)) {
            return false;
        }

        return saveMenuRole(sysRoleDO);
    }
}
