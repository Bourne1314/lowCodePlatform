package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统菜单 接口访问层
 *
 * @author yansiyang
 * @version v1.0
 * @date 2019-04-11 14:18:16
 */

@RestController
@RequestMapping("/sysMenus")
@Api("菜单维护")
public class SysMenuController extends BaseController {

    /**
     * 获取菜单树,加载左侧菜单,加载权限
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:36
     */
    @ApiOperation(value = "获取菜单树", httpMethod = "GET")
    @AceAuth("获取左侧菜单树")
    @RequestMapping(value = "/action/tree/{appId}", method = RequestMethod.GET)
    public R listSideTree(@PathVariable("appId") String appId) {

        // 获取用户所有的权限标识
//        List<SysAuthMixDO> authMixs = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().eq("user_id",
//                securityUtils.getCurrentUserId()).eq("app_id", appId));
//        List<String> authIds = authMixs.stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toList());
//        if (authIds == null || authIds.size() == 0) {
//            return R.ok().put("list", new ArrayList<>()).put("perms", null);
//        }

        List<SysMenuDO> tree = sysMenuService.listSideTree(appId);
        List<SysAuthDO> auths = sysAuthService.list(new QueryWrapper<SysAuthDO>()
//                .and(authIds == null || authIds.size() == 0, i -> i.eq("1", "2"))
//                .in("id", authIds)
                .inSql("id", "select auth_id from sys_auth_mix where app_id ='" + appId + "' and " + "user_id='" +
                        securityUtils.getCurrentUserId() + "'")
        );
        // 加载权限信息
        List<String> perms = auths.stream().map(SysAuthDO::getCode).collect(Collectors.toList());

        return R.ok().put("list", tree).put("perms", perms);
    }

    /**
     * 获取菜单树,加载列表
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:36
     */
    @ApiOperation(value = "获取菜单树", httpMethod = "GET")
    @AceAuth("获取菜单树列表")
    @RequestMapping(value = "/action/menuTree/{appId}", method = RequestMethod.GET)
    public R listMenuTree(@PathVariable("appId") String appId) {
        List<SysMenuDO> tree = sysMenuService.listMenuTree(appId);
        return R.ok().put("list", tree);
    }

    /**
     * @param id 菜单id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个菜单", httpMethod = "GET", notes = "获取单个菜单")
    @AceAuth("获取单个菜单")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        return sysMenuService.getMenuInfo(id);
    }

    /**
     * 获取菜单列表
     *
     * @param params 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 菜单列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取菜单列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取菜单列表并分页")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<SysMenuDO> page = new Page<>(current, size);
        IPage list = sysMenuService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 保存菜单
     *
     * @param menu 菜单对象
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存菜单", httpMethod = "POST")
    @ApiImplicitParam(name = "menu", value = "菜单实体", required = true, dataType = "SysMenuDO")
    @AceAuth("保存菜单")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody SysMenuDO menu) {
        if (sysMenuService.saveMenu(menu)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单对象
     * @return com.csicit.ace.common.utils.server.R 更新结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改菜单", httpMethod = "PUT", notes = "修改菜单")
    @ApiImplicitParam(name = "menu", value = "菜单实体", required = true, dataType = "SysMenuDO")
    @AceAuth("修改菜单")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody SysMenuDO menu) {
        if (sysMenuService.updateMenu(menu)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除菜单
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "删除菜单", httpMethod = "DELETE", notes = "删除菜单")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("删除菜单")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, Object> params) {
        return sysMenuService.delete(params);
    }

    /**
     * 移动菜单
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "移动菜单", httpMethod = "POST", notes = "移动菜单")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("移动菜单")
    @RequestMapping(value = "/action/moveMenu", method = RequestMethod.POST)
    public R moveMenu(@RequestBody Map<String, String> params) {
        if (sysMenuService.moveMenu(params)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

}
