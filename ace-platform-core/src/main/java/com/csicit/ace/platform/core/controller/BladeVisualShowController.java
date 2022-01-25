package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.pojo.domain.BladeVisualMapDO;
import com.csicit.ace.common.pojo.domain.BladeVisualShowDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BladeVisualMapService;
import com.csicit.ace.platform.core.service.BladeVisualShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 可视化地图配置表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 10:05:48
 */

@RestController
@RequestMapping("/bladeVisualShows")
@Api("大屏展示")
public class BladeVisualShowController extends BaseController {

    @Autowired
    private BladeVisualShowService bladeVisualShowService;

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
        BladeVisualShowDO instance = bladeVisualShowService.getInfo(id);
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
        Page<BladeVisualShowDO> page = new Page<>(current, size);
        IPage list = bladeVisualShowService.page(page, new QueryWrapper<BladeVisualShowDO>().select("id", "name"));
        return R.ok().put("page", list);
    }

    /**
     * 获取树列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2020-06-05 10:05:48
     */
    @ApiOperation(value = "获取树列表", httpMethod = "GET", notes = "获取树列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取树列表")
    @RequestMapping(value = "/query/listTree", method = RequestMethod.GET)
    public R listTree(@RequestParam Map<String, Object> params) {
        List<BladeVisualShowDO> list = bladeVisualShowService.getListTree((String) params.get("appId"));
        return R.ok().put("list", list);
    }

    /**
     * 获取看版列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2020-06-05 10:05:48
     */
    @ApiOperation(value = "获取看版列表", httpMethod = "GET", notes = "获取看版列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取看版列表")
    @RequestMapping(value = "/query/lookEdit/list", method = RequestMethod.GET)
    public R lookEditList(@RequestParam Map<String, Object> params) {
        List<BladeVisualShowDO> list = bladeVisualShowService.list(new QueryWrapper<BladeVisualShowDO>()
        .eq("type","0").eq("app_id",params.get("appId")).select("id","name"));
        return R.ok().put("list", list);
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
    public R save(@RequestBody BladeVisualShowDO instance) {
        if (bladeVisualShowService.count(new QueryWrapper<BladeVisualShowDO>()
                .eq("app_id", instance.getAppId()).eq("name", instance.getName())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
            ));
        }
        if (bladeVisualShowService.saveBladeVisualShow(instance)) {
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
    public R update(@RequestBody BladeVisualShowDO instance) {
        BladeVisualShowDO bladeVisualShowDO = bladeVisualShowService.getById(instance.getId());
        if (!Objects.equals(bladeVisualShowDO.getName(), instance.getName())) {
            if (bladeVisualShowService.count(new QueryWrapper<BladeVisualShowDO>()
                    .eq("app_id", instance.getAppId()).eq("name", instance.getName())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
                ));
            }
        }
        if (bladeVisualShowService.updateBladeVisualShow(instance)) {
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
        if (bladeVisualShowService.deleteBladeVisualShow(map.get("id"))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
