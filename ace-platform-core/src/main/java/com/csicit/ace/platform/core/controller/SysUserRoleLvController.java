package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysUserRoleLvDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.SysUserRoleLvService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 用户角色历史数据主表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-10-22 19:11:13
 */

@RestController
@RequestMapping("/sysUserRoleLvs")
@Api("用户角色历史数据主表")
public class SysUserRoleLvController extends BaseController {

    @Autowired
    private SysUserRoleLvService sysUserRoleLvService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-10-22 19:11:13
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysUserRoleLvDO instance = sysUserRoleLvService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-10-22 19:11:13
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<SysUserRoleLvDO> page = new Page<>(current, size);
        IPage list = sysUserRoleLvService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-10-22 19:11:13
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysUserRoleLvDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysUserRoleLvDO instance) {
        if (sysUserRoleLvService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-10-22 19:11:13
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysUserRoleLvDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysUserRoleLvDO instance) {
        if (sysUserRoleLvService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-10-22 19:11:13
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysUserRoleLvService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
