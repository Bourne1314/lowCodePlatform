package com.csicit.ace.bpm.controller;

import com.csicit.ace.bpm.service.WfdFlowCategoryService;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.bpm.pojo.domain.WfdFlowCategoryDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 流程类别 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */

@RestController
@RequestMapping("/wfdFlowCategorys")
@Api("流程类别")
public class WfdFlowCategoryController extends BaseController {
    @Autowired
    WfdFlowCategoryService wfdFlowCategoryService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        WfdFlowCategoryDO instance = wfdFlowCategoryService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String appId = (String) params.get("app_id");
        Page<WfdFlowCategoryDO> page = new Page<>(current, size);
        IPage list = wfdFlowCategoryService.page(page, new QueryWrapper<WfdFlowCategoryDO>()
                .orderByAsc("sort_no").eq("app_id", appId));
        return R.ok().put("page", list);
    }

    /**
     * 获取流程类别列表
     *
     * @param params 请求参数
     * @return 流程类别集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取流程类别列表", httpMethod = "GET", notes = "根据请求参数获取流程类别列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取流程类别列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        String appId = (String) params.get("app_id");
        List<WfdFlowCategoryDO> list = wfdFlowCategoryService.list(new QueryWrapper<WfdFlowCategoryDO>()
                .orderByAsc("sort_no").eq("app_id", appId));
        return R.ok().put("list", list);
    }

    /**
     * 保存流程类别
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "保存流程类别", httpMethod = "POST", notes = "保存流程类别")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowCategoryDO")
    @AceAuth("保存流程类别")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody WfdFlowCategoryDO instance) {
        // 存在判断
        if (StringUtils.isNotBlank(wfdFlowCategoryService.existCheck(instance))) {
            return R.error(wfdFlowCategoryService.existCheck(instance));
        }
        if (wfdFlowCategoryService.save(instance)) {
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
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowCategoryDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody WfdFlowCategoryDO instance) {
        WfdFlowCategoryDO oldCategoryDO = wfdFlowCategoryService.getById(instance.getId());
        if (!Objects.equals(oldCategoryDO.getName(), instance.getName())) {
            // 判断流程类别是否已存在
            int count = wfdFlowCategoryService.count(new QueryWrapper<WfdFlowCategoryDO>()
                    .eq("app_id", instance.getAppId()).eq("name", instance.getName()));
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new Object[]{"FLOW_CATEGORY", instance.getName()}
                ));
            }
        }
        if (!Objects.equals(oldCategoryDO.getSortNo(), instance.getSortNo())) {
            // 判断序号是否存在
            int count2 = wfdFlowCategoryService.count(new QueryWrapper<WfdFlowCategoryDO>()
                    .eq("app_id", instance.getAppId()).eq("sort_no", instance.getSortNo()));
            if (count2 > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new Object[]{InternationUtils.getInternationalMsg("SORT_NO"), instance.getSortNo().toString()}
                ));
            }
        }
        if (wfdFlowCategoryService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (wfdFlowCategoryService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
