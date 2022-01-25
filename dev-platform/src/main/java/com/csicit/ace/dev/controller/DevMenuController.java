package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.dev.DevMenuDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.dev.service.DevMenuService;
import com.csicit.ace.dev.service.ShiroService;
import com.csicit.ace.dev.util.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 菜单管理 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/devMenus")
@Api("菜单管理")
public class DevMenuController extends BaseController {

    private static int ADD_INDEX = 10;

    @Autowired
    AceSqlUtils sqlUtils;

//    @Autowired
//    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private DevMenuService devMenuService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 获取菜单树,加载左侧菜单,加载权限
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:36
     */
    @ApiOperation(value = "获取菜单树", httpMethod = "GET")
    @RequestMapping(value = "/action/tree", method = RequestMethod.GET)
    public R listSideTree() {
       DevUserDO SysUserDO = ShiroUtils.getUserinfo();
        List<DevMenuDO> tree = devMenuService.listSideTree(SysUserDO.getId());
        Set<String> permissions = shiroService.getUserPermissions(SysUserDO.getId());
        return R.ok().put("list", tree).put("perms", permissions);
    }

    /**
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:menu:view")
    public R get(@PathVariable("id") String id) {
        DevMenuDO instance = devMenuService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("sys:menu:list")
    public R list(@RequestParam Map<String, Object> params) {
        List<DevMenuDO> menus = devMenuService.list(new QueryWrapper<DevMenuDO>()
                .like("name", params.get("name")).orderByAsc("sort_index"));
        //生成树结构的数据
        List<DevMenuDO> listT = TreeUtils.makeTree(menus, DevMenuDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions("sys:menu:add")
    public R save(@RequestBody DevMenuDO instance) {
        Integer count = devMenuService.count(new QueryWrapper<DevMenuDO>()
                .eq("parent_id", instance.getParentId()).eq("name", instance.getName()));
        if (count > 0) {
            return R.error("已存在相同的菜单名称");
        }
        Integer count1 = devMenuService.count(new QueryWrapper<DevMenuDO>()
                .eq("parent_id", instance.getParentId()).eq("sort_index", instance.getSortIndex()));
        if (count1 > 0) {
            return R.error("已存在相同的排序号");
        }
        if (devMenuService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions("sys:menu:edit")
    public R update(@RequestBody DevMenuDO instance) {
        DevMenuDO oldmenu = devMenuService.getById(instance.getId());
        if (!Objects.equals(oldmenu.getName(), instance.getName())) {
            Integer count = devMenuService.count(new QueryWrapper<DevMenuDO>()
                    .eq("parent_id", instance.getParentId()).eq("name", instance.getName()));
            if (count > 0) {
                return R.error("已存在相同的菜单名称");
            }
        }
        if (!Objects.equals(oldmenu.getSortIndex(), instance.getSortIndex())) {
            Integer count1 = devMenuService.count(new QueryWrapper<DevMenuDO>()
                    .eq("parent_id", instance.getParentId()).eq("sort_index", instance.getSortIndex()));
            if (count1 > 0) {
                return R.error("已存在相同的排序号");
            }
        }
        if (devMenuService.updateById(instance)) {
            // 更新权限
//            shiroService.updatePermission(shiroFilterFactoryBean, null, false);
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions("sys:menu:del")
    public R delete(@RequestBody String[] ids) {
        if (devMenuService.deleteMenu(Arrays.asList(ids))) {
            // 更新权限
//            shiroService.updatePermission(shiroFilterFactoryBean, null, false);
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 根据参数获取当前数据下的最大排序号
     *
     * @param params 参数
     * @return 单个权限
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiOperation(value = "根据参数获取当前数据下的最大排序号", httpMethod = "POST", notes = "根据参数获取当前数据下的最大排序号")
    @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "Map")
    @AceAuth("根据参数获取当前数据下的最大排序号")
    @RequestMapping(value = "/getMaxSortNo", method = RequestMethod.POST)
    public R getMaxSortNo(@RequestBody Map<String, Object> params) {
        return R.ok().put("maxSortNo", ADD_INDEX + sqlUtils.getDevMaxSort(params));
    }

}
