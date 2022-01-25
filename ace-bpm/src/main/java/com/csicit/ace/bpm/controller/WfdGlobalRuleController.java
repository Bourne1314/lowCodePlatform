package com.csicit.ace.bpm.controller;

import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.bpm.pojo.domain.WfdGlobalRuleDO;
import com.csicit.ace.bpm.service.WfdGlobalRuleService;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 全局规则 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:46:06
 */

@RestController
@RequestMapping("/wfdGlobalRules")
@Api("全局规则")
public class WfdGlobalRuleController extends BaseController {

    @Autowired
    private WfdGlobalRuleService wfdGlobalRuleService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-08-16 11:46:06
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        WfdGlobalRuleDO instance = wfdGlobalRuleService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-16 11:46:06
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<WfdGlobalRuleDO> page = new Page<>(current, size);
        Map<String, Object> equalMap = new HashMap<>();
        equalMap.put("app_id", params.get("app_id"));
        Map<String, Object> likeMap = new HashMap<>();
        likeMap.put("name", params.get("name"));
        IPage list = wfdGlobalRuleService.page(page, MapWrapper.getEqualAndLikeInstance(equalMap, likeMap));
        return R.ok().put("page", list);
    }


    /**
     * 获取全局规则列表
     *
     * @param params 请求参数
     * @return 流程类别集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取全局规则列表", httpMethod = "GET", notes = "获取全局规则列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取全局规则列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        List<WfdGlobalRuleDO> list = wfdGlobalRuleService.list(MapWrapper.getEqualInstance(params));
        return R.ok().put("list", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 11:46:06
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowRuleDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody WfdGlobalRuleDO instance) {
        int count = wfdGlobalRuleService.count(new QueryWrapper<WfdGlobalRuleDO>()
                .eq("name", instance.getName()).eq("app_id", instance.getAppId()));
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new Object[]{"NAME", instance.getName()}
            ));
        }
        if (wfdGlobalRuleService.save(instance)) {
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
     * @date 2019-08-16 11:46:06
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowRuleDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody WfdGlobalRuleDO instance) {
        if (wfdGlobalRuleService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 11:46:06
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (wfdGlobalRuleService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
