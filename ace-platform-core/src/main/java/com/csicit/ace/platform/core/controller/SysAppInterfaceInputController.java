package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.SysAppInterfaceInputService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csicit.ace.common.utils.internationalization.InternationUtils;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 接口sql入参信息表 接口访问层
 *
 * @author generator
 * @date 2020-06-03 09:05:20
 * @version V1.0
 */
 
@RestController
@RequestMapping("/sysAppInterfaceInputs")
@Api("接口sql入参信息表")
public class SysAppInterfaceInputController extends BaseController {

	@Autowired
	private SysAppInterfaceInputService sysAppInterfaceInputService;

    /**
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysAppInterfaceInputDO instance = sysAppInterfaceInputService.getById(id);
        return R.ok().put("instance", instance);
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
    public R listAll(@RequestParam Map<String, Object> params) {
        String interface_id = params.get("interface_id").toString();
        List<SysAppInterfaceInputDO> list = sysAppInterfaceInputService.list(new QueryWrapper<SysAppInterfaceInputDO>()
                .eq("interface_id", interface_id));
        return R.ok().put("list", list);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String interface_id = (String) params.get("interface_id");
        Page<SysAppInterfaceInputDO> page = new Page<>(current, size);
        IPage list = sysAppInterfaceInputService.page(page, new QueryWrapper<SysAppInterfaceInputDO>()
                .eq("interface_id", interface_id));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaViewDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysAppInterfaceInputDO instance) {
        int count = sysAppInterfaceInputService.count(new QueryWrapper<SysAppInterfaceInputDO>()
                .eq("interface_id", instance.getInterfaceId()).eq("param_key", instance.getParamKey()));
        if (count > 0) {
            return R.error("在该接口下，已存在相同入参键");
        }
        if (sysAppInterfaceInputService.addInputParams(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaViewDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysAppInterfaceInputDO instance) {
        SysAppInterfaceInputDO oldModel = sysAppInterfaceInputService.getById(instance.getId());
        if (!Objects.equals(instance.getParamKey(), oldModel
                .getParamKey())) {
            int count = sysAppInterfaceInputService.count(new QueryWrapper<SysAppInterfaceInputDO>()
                    .eq("interface_id", instance.getInterfaceId()).eq("param_key", instance.getParamKey()));
            if (count > 0) {
                return R.error("在该接口下，已存在相同入参键");
            }
        }

        if (sysAppInterfaceInputService.updInputParams(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }

        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }


    /**
     * @param ids
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysAppInterfaceInputService.delInputParams(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
