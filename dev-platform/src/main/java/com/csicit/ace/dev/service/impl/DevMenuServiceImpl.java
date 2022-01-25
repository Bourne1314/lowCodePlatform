package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.DevMenuDO;
import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserRoleDO;
import com.csicit.ace.data.persistent.mapper.DevMenuMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.DevMenuRoleService;
import com.csicit.ace.dev.service.DevMenuService;
import com.csicit.ace.dev.service.DevUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("devMenuService")
public class DevMenuServiceImpl extends BaseServiceImpl<DevMenuMapper, DevMenuDO> implements
        DevMenuService {


    @Autowired
    DevUserRoleService devUserRoleService;

    @Autowired
    DevMenuRoleService devMenuRoleService;

    /**
     * 删除菜单
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public boolean deleteMenu(List<String> ids) {
        String pid = ids.get(0);
        List<String> cids = new ArrayList<>(16);
        this.getCids(cids,pid);
        cids.add(pid);
        if (!removeByIds(cids)) {
            return false;
        }
        return true;
    }

    /**
     * 根据ID获取子菜单ID列表
     *
     * @param cids
     * @param pid
     * @return java.util.List<java.lang.String>
     * @author zuogang
     * @date 2020/1/19 14:23
     */
    private void getCids(List<String> cids, String pid) {
        List<String> ids = list(new QueryWrapper<DevMenuDO>().eq("parent_id", pid)).stream().map(DevMenuDO::getId)
                .collect(Collectors.toList());
        if (ids != null && ids.size() > 0) {
            cids.addAll(ids);
            ids.stream().forEach(id -> {
                getCids(cids, id);
            });
        }
    }


    /**
     * 左侧菜单树
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/5/15 9:00
     */
    @Override
    public List<DevMenuDO> listSideTree(String userId) {
        List<DevMenuDO> list = new ArrayList<>();
        devUserRoleService.list(new QueryWrapper<DevUserRoleDO>()
                .eq("user_id", userId)).stream().map(DevUserRoleDO::getRoleId)
                .forEach(roleId -> {
                    devMenuRoleService.list(new QueryWrapper<DevMenuRoleDO>()
                            .eq("role_id", roleId)).stream().map(DevMenuRoleDO::getMenuId)
                            .forEach(menuId -> {
                                DevMenuDO DevMenuDO = getById(menuId);
                                if (!Objects.equals(2, DevMenuDO.getType())) {
                                    list.add(DevMenuDO);
                                }
                            });
                });
        List<DevMenuDO> listOrder = list.stream().sorted(Comparator.comparing(DevMenuDO::getSortIndex)).collect
                (Collectors.toList());

        List<DevMenuDO> subMenuList = new ArrayList<>();
        for (DevMenuDO menu : listOrder) {
            //目录
            if (Objects.equals(menu.getParentId(), "0")) {
                List<DevMenuDO> cidmenuList = list(new QueryWrapper<DevMenuDO>()
                        .eq("parent_id", menu.getId()).orderByAsc("sort_index"));
                menu.setList(makeTree(cidmenuList, new HashSet<>()));
                subMenuList.add(menu);
            }
            menu.setChildren(menu.getList());
        }
        return subMenuList;
    }

    /**
     * 递归把菜单列表转化为菜单树   左侧菜单
     *
     * @param menuList
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:54
     */
    public List<DevMenuDO> makeTree(List<DevMenuDO> menuList, HashSet<String> set) {
        List<DevMenuDO> subMenuList = new ArrayList<>();

        for (DevMenuDO menu : menuList) {
            //目录
            if (menu.getType() == 0) {
                List<DevMenuDO> cidmenuList = list(new QueryWrapper<DevMenuDO>()
                        .eq("parent_id", menu.getId()).orderByAsc("sort_index"));
                menu.setList(makeTree(cidmenuList, new HashSet<>()));
            }
            // 防止重复添加菜单
            if (!set.contains(menu.getId())) {
                subMenuList.add(menu);
                set.add(menu.getId());
            }
        }
        return subMenuList;
    }
}
