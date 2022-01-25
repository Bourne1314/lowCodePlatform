package com.csicit.ace.zuul.service;

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
        String appId = securityUtils.getAppName();
        if (StringUtils.isNotBlank(appId)) {
            return clientService.getMenuList(appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysMenuDO> listMenuByNoPermission() {
        String appId = securityUtils.getAppName();
        if (StringUtils.isNotBlank(appId)) {
            return clientService.getMenuListByNoPermission(appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysMenuDO> listByParentId(String parentId) {
        String appId = securityUtils.getAppName();
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(parentId) && StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(userId)) {
            return clientService.listByParentId(appId, parentId, userId);
        }
        return new ArrayList<>();
    }
}
