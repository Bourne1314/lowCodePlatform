package com.csicit.ace.bpm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.el.WfdFlowElService;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO;
import com.csicit.ace.bpm.pojo.vo.free.FreeStep;
import com.csicit.ace.bpm.pojo.vo.free.FreeStepInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetRoute;
import com.csicit.ace.bpm.pojo.vo.preset.PresetUser;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.FormField;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.NodeFreeStep;
import com.csicit.ace.bpm.pojo.vo.wfi.WfiFlowInfo;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.service.WfiTaskPendingService;
import com.csicit.ace.bpm.service.WfiVFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.WfdCollectionUtils;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.IUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/8/3 14:50
 */
@RestController
@RequestMapping("/wfiFlow")
public class WfiFlowController {
    private static final String KEY_WORD = "keyWord";
    private static final Integer SIZE_SORT_BY_PINYIN = 30;
    @Autowired
    private WfiFlowService wfiFlowService;
    @Autowired
    private WfiVFlowService wfiVFlowService;
    @Autowired
    private WfdCollectionUtils wfdCollectionUtils;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    private WfiTaskPendingService wfiTaskPendingService;
    @Autowired
    private IUser iUser;
    @Autowired
    private WfdFlowElService wfdFlowElService;
    @Autowired
    private QueryUtil queryUtil;

    /**
     * 获取流程实例模型
     *
     * @param id 流程实例ID
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/8/3 14:52
     */

    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @CrossOrigin
    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable("id") String id) {
        WfiFlowDO wfiFlow = wfiFlowService.getById(id);
        return R.ok().put("instance", wfiFlow).put("flowVersion", wfiVFlowService.getFlowVersionByFlowId(id));
    }

    @ApiOperation(value = "获取未办结列表", httpMethod = "GET", notes = "获取未办结列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R listPending(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<WfiFlowDO> page = new Page<>(current, size);
        QueryWrapper<WfiFlowDO> query;
        if (params.containsKey(KEY_WORD)) {
            String keyWord = (String) params.get(KEY_WORD);
            query = new QueryWrapper<WfiFlowDO>().like("FLOW_NO", "%" + keyWord + "%");
        } else {
            query = new QueryWrapper<>();
        }
        IPage list = wfiFlowService.page(page, query);
        return R.ok().put("page", list);
    }

    @CrossOrigin
    @RequestMapping(value = "/updateByNew", method = RequestMethod.POST)
    public R updateByNew(@RequestBody WfiFlowInfo wfiFlowInfo) {
        if (wfiFlowInfo.getFlow() == null) {
            throw new BpmException("flow is null");
        }
        wfiFlowService.updateByNew(wfiFlowInfo.getFlow(), wfiFlowInfo.getFlowVersion());
        return R.ok().put("flowVersion", wfiVFlowService.getFlowVersionByFlowId(wfiFlowInfo.getFlow().getId()));
    }

    /**
     * 预设流转路径
     *
     * @param presetInfo 流转信息
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/8/25 17:07
     */

    @CrossOrigin
    @RequestMapping(value = "/presetRoute", method = RequestMethod.POST)
    public R presetRoute(@RequestBody PresetInfo presetInfo) {
        // TODO: 2020/12/27 多次预设，预设信息没有清理干净
        WfiFlowDO wfiFlow = wfiFlowService.getById(presetInfo.getFlowId());
        if (wfiFlow == null) {
            throw new WfiFlowNotFoundByIdException(presetInfo.getFlowId());
        }
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        String currentUserId = securityUtils.getCurrentUserId();
        WfiTaskPendingDO wfiTaskPending = wfiTaskPendingService.getByTaskId(presetInfo.getFlowId(), presetInfo.getTaskId(), currentUserId);
        if (wfiTaskPending == null) {
            throw new WfiTaskPendingNotFoundException(presetInfo.getFlowId(), presetInfo.getTaskId(), currentUserId);
        }
        if (!UserType.Host.isEquals(wfiTaskPending.getUserType())) {
            throw new NoAccessToPresetException(presetInfo.getFlowId(), presetInfo.getTaskId(), currentUserId);
        }
        // 填充预设用户姓名，用于回显
        appendRealName(presetInfo);
        Node node = flow.getNodeById(wfiTaskPending.getNodeId());
        if(!bpmAdapter.allowPresetRoute(wfiFlow, node, presetInfo.getTaskId()))
        {
            throw new NodeNotAllowPresetRouteException(node);
        }
        wfiFlowService.presetRoute(presetInfo, wfiFlow, node);
        return R.ok().put("flowVersion", wfiVFlowService.getFlowVersionByFlowId(presetInfo.getFlowId()));
    }

    private void appendRealName(PresetInfo presetInfo) {
        List<String> presetUserIds = new ArrayList<>();
        for (PresetRoute presetRoute : presetInfo.getPresetRoutes()) {
            for (PresetUser presetUser : presetRoute.getPresetUsers()) {
                if (!presetUserIds.contains(presetUser.getUserId())) {
                    presetUserIds.add(presetUser.getUserId());
                }
            }
        }
        if (presetUserIds.size() > 0) {
            List<SysUserDO> sysUsers = iUser.getUsersByIds(presetUserIds);
            SysUserDO sysUser;
            for (PresetRoute presetRoute : presetInfo.getPresetRoutes()) {
                for (PresetUser presetUser : presetRoute.getPresetUsers()) {
                    sysUser = sysUsers.stream().filter(o -> StringUtils.equals(presetUser.getUserId(), o.getId())).findFirst().orElse(null);
                    if (sysUser == null) {
                        throw new SysUserNotFoundByIdException(presetUser.getUserId());
                    } else {
                        presetUser.setRealName(sysUser.getRealName());
                    }
                }
            }
        }
    }

    /**
     * 获取节点经办人列表
     *
     * @param flowId 流程实例id
     * @param nodeId 节点id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/8/25 17:05
     */

    @CrossOrigin
    @RequestMapping(value = "/listUsersForPresetByNodeId", method = RequestMethod.GET)
    public R listUsersForPresetByNodeId(@RequestParam("flowId") String flowId, @RequestParam("nodeId") String nodeId) {
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        SessionAttribute.initElSession(securityUtils.getSession(), flowId, flow, wfiFlow.getBusinessKey());
        Node node = flow.getNodeById(nodeId);
        Integer secretLevel = FlowUtils.getSecretLevel(flow, wfdFlowElService);
        List<SysUserDO> sysUsers = wfdCollectionUtils.getUsers(node, secretLevel);
        if(sysUsers.size() > SIZE_SORT_BY_PINYIN)
        {
            sysUsers.sort(((o1, o2) -> StringUtils.compare(o1.getPinyin(), o2.getPinyin())));
        }
        List<String> userIds = new ArrayList<>();
        for(SysUserDO sysUserDO:sysUsers){
            userIds.add(sysUserDO.getId());
        }
        List<OrgDepartmentDO> departmentTree = bpmAdapter.getDepartmentTree(flowId,nodeId);
        return R.ok().put("users", sysUsers).put("departmentTree",departmentTree);
    }

    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 15:22 2021/9/15
     * @Param 转交根据节点人员获取部门树
     * @return departmentTree
     **/

    @CrossOrigin
    @RequestMapping(value = "/listdeliverDepartment", method = RequestMethod.GET)
    public R listdeliverDepartment(@RequestParam("flowId") String flowId, @RequestParam("nodeId") String nodeId,@RequestParam("taskId") String taskId) {
        List<OrgDepartmentDO> departmentTree = bpmAdapter.listdeliverDepartment(flowId,nodeId,taskId);
        return R.ok().put("departmentTree",departmentTree);
    }

    @CrossOrigin
    @RequestMapping(value = "/updateNodeFreeSteps", method = RequestMethod.POST)
    public R updateNodeFreeSteps(@RequestBody FreeStepInfo freeStepInfo) {
        TaskInstance task = bpmAdapter.getTaskInstanceById(freeStepInfo.getTaskId());
        wfiFlowService.updateNodeFreeSteps(task, freeStepInfo);
        return getFreeStepInfoByNodeId(freeStepInfo.getTaskId(), freeStepInfo.getNodeId());
    }

    @CrossOrigin
    @RequestMapping(value = "/getFreeStepInfoByNodeId", method = RequestMethod.GET)
    public R getFreeStepInfoByNodeId(@RequestParam("taskId") String taskId, @RequestParam("nodeId") String nodeId) {
        FreeStepInfo freeStepInfo = new FreeStepInfo();
        freeStepInfo.setTaskId(taskId);
        freeStepInfo.setNodeId(nodeId);
        TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
        Flow flow = task.getFlow();
        Node node = flow.getNodeById(nodeId);
        if (!NodeType.Free.isEquals(node.getNodeType())) {
            throw new NodeTypeNotMatchException(NodeType.Free, node.getNodeType());
        }
        NodeFreeStep nodeFreeStep;
        for (int i = 0; i < node.getNodeFreeSteps().size(); i++) {
            nodeFreeStep = node.getNodeFreeSteps().get(i);
            FreeStep freeStep = new FreeStep();
            freeStep.setId(nodeFreeStep.getId());
            freeStep.setStepNo(nodeFreeStep.getStepNo());
            freeStep.setHostMode(nodeFreeStep.getHostMode());
            freeStep.setDeliverUsers(nodeFreeStep.getDeliverUsers());
            freeStepInfo.addFreeStep(freeStep);
        }
        freeStepInfo.setFlowVersion(wfiVFlowService.getFlowVersionByFlowId(task.getFlowInstanceId()));
        return R.ok().put("freeStepInfo", freeStepInfo);
    }

    /**
     * 获取流程步骤列表，含办理人
     *
     * @param flowId 流程实例id
     * @return 流程办理步骤
     */
    @CrossOrigin
    @RequestMapping(value = "/listWfiNodesByFlowId/{flowId}", method = RequestMethod.GET)
    public R listWfiNodesByFlowId(@PathVariable("flowId") String flowId) {
        return R.ok().put("wfiNodes", bpmAdapter.listWfiNodesByFlowId(flowId));
    }

    /**
     * 获取流程步骤列表，含办理人
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 流程办理步骤
     */
    @CrossOrigin
    @RequestMapping(value = "/listWfiNodesByBusinessKey", method = RequestMethod.GET)
    public R listWfiNodesByBusinessKey(@RequestParam("flowCode") String flowCode, @RequestParam("businessKey") String businessKey) {
        return R.ok().put("wfiNodes", bpmAdapter.listWfiNodesByBusinessKey(flowCode, businessKey));
    }
}