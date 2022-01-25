package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.DevRoleDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.DevRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 角色管理 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/devRoles")
@Api("角色管理")
public class DevRoleController extends BaseController {

    @Autowired
    private DevRoleService devRoleService;

    /**
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:role:view")
    public R get(@PathVariable("id") String id) {
        DevRoleDO instance = devRoleService.getRoleInfo(id);
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
    @RequiresPermissions("sys:role:list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<DevRoleDO> page = new Page<>(current, size);
        IPage list = devRoleService.page(page, new QueryWrapper<DevRoleDO>().orderByAsc("role_name"));
        return R.ok().put("page", list);
    }

    /**
     * 不分页获取列表
     *
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "不分页获取列表", httpMethod = "GET", notes = "不分页获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public R listAll() {
        List<DevRoleDO> list = devRoleService.list(new QueryWrapper<DevRoleDO>().orderByAsc("role_name"));
        return R.ok().put("list", list);
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
    @RequiresPermissions("sys:role:add")
    public R save(@RequestBody DevRoleDO instance) {
        Integer count = devRoleService.count(new QueryWrapper<DevRoleDO>()
                .eq("role_name", instance.getRoleName()));
        if (count > 0) {
            return R.error("已存在相同的角色名称");
        }
        if (devRoleService.saveRole(instance)) {
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
    @RequiresPermissions("sys:role:edit")
    public R update(@RequestBody DevRoleDO instance) {
        DevRoleDO oldRole = devRoleService.getById(instance.getId());
        if (!Objects.equals(oldRole.getRoleName(), instance.getRoleName())) {
            Integer count = devRoleService.count(new QueryWrapper<DevRoleDO>()
                    .eq("role_name", instance.getRoleName()));
            if (count > 0) {
                return R.error("已存在相同的角色名称");
            }
        }
        if (devRoleService.updateRole(instance)) {
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
    @RequiresPermissions("sys:role:del")
    public R delete(@RequestBody String[] ids) {
        if (devRoleService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
