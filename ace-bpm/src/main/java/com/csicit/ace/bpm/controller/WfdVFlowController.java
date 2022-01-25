package com.csicit.ace.bpm.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.service.WfdVFlowService;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 流程定义 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:43:23
 */

@RestController
@RequestMapping("/wfdVFlows")
@Api("流程定义")
public class WfdVFlowController extends BaseController {

    @Autowired
    private WfdVFlowService wfdVFlowService;


    /**
     * 新增 工作流模板
     *
     * @param json 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdVFlowDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R saveWfdVFlow(@RequestBody JSONObject json) {
        if (wfdVFlowService.saveWorkFlow(json)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        WfdVFlowDO instance = wfdVFlowService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取指定版本流程
     *
     * @param flowId  流程id
     * @param version 指定版本
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/4/27 0:32
     */

    @RequestMapping(value = "/{flowId}/{version}", method = RequestMethod.GET)
    public R getByVersion(@PathVariable("flowId") String flowId, @PathVariable("version") Integer version) {
        return R.ok().put("instance", wfdVFlowService.getByVersion(flowId, version));
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<WfdVFlowDO> page = new Page<>(current, size);
        IPage list = wfdVFlowService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

//    /**
//     * 新增
//     *
//     * @param instance	 对象
//     * @return 保存响应结果
//     * @author generator
//     * @date 2019-08-16 11:43:23
//     */
//    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
//    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdVFlowDO")
//    @AceAuth("保存")
//    @RequestMapping(method = RequestMethod.POST)
//    public R save(@RequestBody WfdVFlowDO instance) {
//        if (wfdVFlowService.save(instance)) {
//            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdVFlowDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody WfdVFlowDO instance) {
        if (wfdVFlowService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (!wfdVFlowService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 通过流程ID获取最新版本号
     *
     * @param params ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiOperation(value = "通过流程ID获取最新版本号", httpMethod = "POST", notes = "通过流程ID获取最新版本号")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过流程ID获取最新版本号")
    @RequestMapping(value = "/getLastVersion", method = RequestMethod.POST)
    public R getLastVersion(@RequestBody Map<String, String> params) {
        String flowId = params.get("flowId");
        WfdVFlowDO wfdVFlowDO = wfdVFlowService.getLatestByFlowId(flowId);
        if (wfdVFlowDO == null) {
            return R.ok().put("lastVersion", 0);
        }
        return R.ok().put("lastVersion", wfdVFlowDO.getFlowVersion());
    }

    /**
     * 通过流程ID和版本号获取该版本数据
     *
     * @param params ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 11:43:23
     */
    @ApiOperation(value = "通过流程ID和版本号获取该版本数据", httpMethod = "GET", notes = "通过流程ID和版本号获取该版本数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过流程ID和版本号获取该版本数据")
    @RequestMapping(value = "/getHistoryVersionFlowData", method = RequestMethod.GET)
    public R getHistoryVersionFlowData(@RequestParam Map<String, String> params) {
        String flowId = params.get("flowId");
        String version = params.get("version");
        WfdVFlowDO wfdVFlowDO = wfdVFlowService.getOne(new QueryWrapper<WfdVFlowDO>()
                .eq("flow_id", flowId).eq("flow_version", Integer.parseInt(version)));
        if (wfdVFlowDO == null) {
            return R.error(InternationUtils.getInternationalMsg("DATA_NOT_EXIST"));
        }
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(wfdVFlowDO.getModel());
        return R.ok().put("jsonObject", jsonObject);
    }

}
