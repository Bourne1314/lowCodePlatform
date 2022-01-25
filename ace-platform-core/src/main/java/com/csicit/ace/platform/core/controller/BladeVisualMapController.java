package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BladeVisualMapDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BladeVisualMapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csicit.ace.common.utils.internationalization.InternationUtils;


import java.util.Map;


/**
 * 可视化地图配置表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 10:05:48
 */

@RestController
@RequestMapping("/bladeVisualMaps")
@Api("可视化地图配置表")
public class BladeVisualMapController extends BaseController {

    @Autowired
    private BladeVisualMapService bladeVisualMapService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2020-06-05 10:05:48
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BladeVisualMapDO instance = bladeVisualMapService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2020-06-05 10:05:48
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<BladeVisualMapDO> page = new Page<>(current, size);
        IPage list = bladeVisualMapService.page(page, new QueryWrapper<BladeVisualMapDO>().select("id", "name"));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2020-06-05 10:05:48
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BladeVisualMapDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BladeVisualMapDO instance) {
        if (bladeVisualMapService.saveBladeVisualMap(instance)) {
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
     * @date 2020-06-05 10:05:48
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BladeVisualMapDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BladeVisualMapDO instance) {
        if (bladeVisualMapService.updateBladeVisualMap(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param map map
     * @return 删除响应结果
     * @author generator
     * @date 2020-06-05 10:05:48
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "map", value = "map", required = true, allowMultiple = true, dataType = "map")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, String> map) {
        if (bladeVisualMapService.deleteBladeVisualMap(map.get("id"))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
