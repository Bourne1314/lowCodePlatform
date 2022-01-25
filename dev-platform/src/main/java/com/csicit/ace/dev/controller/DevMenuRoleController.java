package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.dev.service.DevMenuRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 菜单角色管理 接口访问层
 *
 * @author zuog
 * @date 2019-11-07 10:30:31
 * @version V1.0
 */
 
@RestController
@RequestMapping("/devMenuRoles")
@Api("菜单角色管理")
public class DevMenuRoleController extends BaseController {

	@Autowired
	private DevMenuRoleService devMenuRoleService;

    /**
     *
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        DevMenuRoleDO instance = devMenuRoleService.getById(id);
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
    @ApiImplicitParam(name = "params", value = "参数", required = true,dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<DevMenuRoleDO> page = new Page<>(current, size);
        IPage list = devMenuRoleService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance	 对象
     * @return 保存响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody DevMenuRoleDO instance) {
        if (devMenuRoleService.save(instance)) {
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
    public R update(@RequestBody DevMenuRoleDO instance) {
        if (devMenuRoleService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple=true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (devMenuRoleService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
