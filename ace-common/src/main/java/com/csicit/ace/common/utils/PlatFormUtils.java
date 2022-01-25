package com.csicit.ace.common.utils;

import com.csicit.ace.common.pojo.domain.SysMenuDO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shanwj
 * @date 2019/6/13 19:30
 */
public class PlatFormUtils {
    /**
     * 递归把菜单列表转化为菜单树   左侧菜单
     *
     * @param menuList
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:54
     */
    public static List<SysMenuDO> makeTree(List<SysMenuDO> menuList, HashSet<String> set) {
        List<SysMenuDO> subMenuList = new ArrayList<>();

        for (SysMenuDO menu : menuList) {
            //目录
            if (menu.getType() == 0) {
                menu.setList(makeTree(menuList.parallelStream()
                        .filter(mu -> mu.getSortPath().startsWith(menu.getSortPath())
                                && mu.getSortPath().length() > menu.getSortPath().length())
                        .collect(Collectors.toList()), set));
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
