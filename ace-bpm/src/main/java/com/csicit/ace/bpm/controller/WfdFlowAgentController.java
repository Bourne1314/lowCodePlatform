package com.csicit.ace.bpm.controller;

import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.pojo.domain.WfdFlowAgentDO;
import com.csicit.ace.bpm.pojo.vo.FlowAgentDO;
import com.csicit.ace.bpm.service.WfdFlowAgentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 工作代办规则 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */

@RestController
@RequestMapping("/wfdFlowAgents")
@Api("工作代办规则")
public class WfdFlowAgentController extends BaseController {
    @Autowired
    private WfdFlowAgentService wfdFlowAgentService;

    @Autowired
    private WfdFlowService wfdFlowService;

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
        WfdFlowAgentDO instance = wfdFlowAgentService.getById(id);
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
        String currentUserId = securityUtils.getCurrentUserId();
        Page<WfdFlowAgentDO> page = new Page<>(current, size);
        IPage list = wfdFlowAgentService.page(page, new QueryWrapper<WfdFlowAgentDO>()
                .eq("origin_user_id", currentUserId));
        if (list != null) {
            List<WfdFlowAgentDO> agentDOS = list.getRecords();
            if (agentDOS != null && agentDOS.size() > 0) {
                agentDOS.stream().forEach(agentDO -> {
                    agentDO.setFlowName(wfdFlowService.getById(agentDO.getFlowId()).getName());
                });
            }
            list.setRecords(agentDOS);
        }
        return R.ok().put("page", list);
    }

    /**
     * 保存代办规则
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "保存代办规则", httpMethod = "POST", notes = "保存代办规则")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowAgentDO")
    @AceAuth("保存代办规则")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody WfdFlowAgentDO instance) {
        if (wfdFlowAgentService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 保存代办规则
     *
     * @param flowAgentDO 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "保存代办规则", httpMethod = "POST", notes = "保存代办规则")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "FlowAgentDO")
    @AceAuth("保存代办规则")
    @RequestMapping(value = "/save/flowAgent", method = RequestMethod.POST)
    public R saveAgent(@RequestBody FlowAgentDO flowAgentDO) {
        if (wfdFlowAgentService.saveAgent(flowAgentDO)) {
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowAgentDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody WfdFlowAgentDO instance) {
        if (wfdFlowAgentService.updateById(instance)) {
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
        if (wfdFlowAgentService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 启用委托规则
     *
     * @param ids 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "启用委托规则", httpMethod = "POST", notes = "启用委托规则")
    @ApiImplicitParam(name = "ids", value = "实体", required = true, dataType = "String[]")
    @AceAuth("启用委托规则")
    @RequestMapping(value = "/enableRule", method = RequestMethod.POST)
    public R enableRule(@RequestBody String[] ids) {
        if (wfdFlowAgentService.enableRule(ids)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 禁用委托规则
     *
     * @param ids 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "禁用委托规则", httpMethod = "POST", notes = "禁用委托规则")
    @ApiImplicitParam(name = "ids", value = "实体", required = true, dataType = "String[]")
    @AceAuth("禁用委托规则")
    @RequestMapping(value = "/disableRule", method = RequestMethod.POST)
    public R disableRule(@RequestBody String[] ids) {
        if (wfdFlowAgentService.disableRule(ids)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

}
