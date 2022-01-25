package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.orgauth.core.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 菜单接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/6/13 18:54
 */
@RestController
@RequestMapping("/orgauth/sysMenus")
public class SysMenuControllerO {

    @Resource(name = "sysMenuServiceO")
    SysMenuService sysMenuService;


    /**
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2019/12/11 8:54
     */
    @RequestMapping(value = "/{appId}", method = RequestMethod.GET)
    public List<SysMenuDO> list(@PathVariable("appId") String appId) {
        return sysMenuService.listSideTree(appId);

    }

    /**
     * @param appId
     * @return
     * @author  xulei
     * @date 2020/6/17 10:05
     */
    @RequestMapping(value = "/noPermission/{appId}", method = RequestMethod.GET)
    public List<SysMenuDO> listByNoPermission(@PathVariable("appId") String appId) {
        return sysMenuService.listSideTreeByNoPermission(appId);

    }

    /**
     * 根据父节点获取子节点应用菜单
     *
     * @param parentId
     * @return 菜单列表
     * @author FourLeaves
     * @date 2019/12/11 8:54
     */
    @RequestMapping(value = "/action/listByParentId/{appId}/{parentId}/{userId}", method = RequestMethod.GET)
    public List<SysMenuDO> listByParentId(@PathVariable("appId") String appId, @PathVariable("parentId") String parentId
            , @PathVariable("userId") String userId) {
        return sysMenuService.listByParentId(appId, parentId, userId);
    }

    /**
     * 在应用启动时检查应用菜单和权限是否包含了工作流的菜单和权限，没有的话就新增相应的权限和菜单
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/12/11 8:54
     */
    @RequestMapping(method = RequestMethod.POST)
    @AceAuth
    public boolean setAppFLowMenu(@RequestBody Map<String, String> params) {
        return sysMenuService.setAppFLowMenu(params.get("appId"));
    }
}
