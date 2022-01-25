package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IMenu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单接口实现类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/6/13 18:39
 */
@Service("menu")
public class MenuImpl extends BaseImpl implements IMenu {

    @Override
    public List<SysMenuDO> list() {
        if (StringUtils.isNotBlank(appName)) {
            return gatewayService.getMenuList(appName);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysMenuDO> listMenuByNoPermission() {
        if (StringUtils.isNotBlank(appName)) {
            return gatewayService.getMenuListByNoPermission(appName);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysMenuDO> listByParentId(String parentId) {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(parentId) && StringUtils.isNotBlank(appName) && StringUtils.isNotBlank(userId)) {
            return gatewayService.listByParentId(appName, parentId, userId);
        }
        return new ArrayList<>();
    }
}
