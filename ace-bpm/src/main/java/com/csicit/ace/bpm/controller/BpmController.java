package com.csicit.ace.bpm.controller;

import com.alibaba.fastjson.JSONArray;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.el.WfdFlowElService;
import com.csicit.ace.bpm.service.*;
import com.csicit.ace.bpm.utils.WfdCollectionUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import net.sf.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.utils.QueryUtil;
import com.csicit.ace.bpm.enums.NodeRejectTo;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.exception.WfiTaskPendingNotFoundException;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.*;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.commonUtils.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.text.ParseException;

import static com.csicit.ace.bpm.utils.FlowUtils.getSecretLevel;

/**
 * @author JonnyJiang
 * @date 2019/7/10 14:16
 */
@RestController
@RequestMapping("/bpm")
public class BpmController {
    private static final Integer SIZE_SORT_BY_PINYIN = 30;
    private BpmManager bpmManager;

    private BpmAdapter bpmAdapter;

    @Autowired
    WfiFlowService wfiFlowService;
    @Autowired
    WfdFlowElService wfdFlowElService;
    @Autowired
    WfdCollectionUtils wfdCollectionUtils;

    @Autowired
    WfiTaskPendingService wfiTaskPendingService;

    private WfdDelegateRuleService wfdDelegateRuleService;

    @Autowired
    WfdUserPageService wfdUserPageService;

    @Autowired
    QueryUtil queryUtil;

    private static int ADD_INDEX = 10;

    @Autowired
    SqlUtils sqlUtils;

    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    SysReviewFileService sysReviewFileService;

    @Autowired
    public void setBpmManager(BpmManager bpmManager) {
        this.bpmManager = bpmManager;
    }

    @Autowired
    public void setBpmAdapter(BpmAdapter bpmAdapter) {
        this.bpmAdapter = bpmAdapter;
    }

    @Autowired
    public void setWfdDelegateRuleService(WfdDelegateRuleService wfdDelegateRuleService) {
        this.wfdDelegateRuleService = wfdDelegateRuleService;
    }

    /**
     * 创建流程实例
     *
     * @param params code/businessKey
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/14 9:33
     */

    @CrossOrigin
    @RequestMapping(value = "/createFlowIntanceByCode", method = RequestMethod.POST)
    public R createFlowIntanceByCode(@RequestBody Map<String, Object> params) {
        String code = String.valueOf(params.get("code"));
        String businessKey = String.valueOf(params.get("businessKey"));
        FlowInstance flowInstance = bpmManager.createFlowInstanceByCode(code, businessKey);
        List<TaskInstance> tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
        return R.ok().put("flow", flowInstance).put("tasks", tasks);
    }

    /**
     * 获取我的工作列表
     *
     * @param current   当前页码
     * @param size      分页大小
     * @param completed 是否办结
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:25
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMine")
    public R listTaskMine(@RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam("completed") Integer completed) {
        return listTaskMineByFlowCode(QueryUtil.FILTER_ALL, current, size, completed);
    }

    /**
     * 通过参数获取我的工作列表
     *
     * @param params 参数
     * @return com.csicit.ace.common.utils.server.R
     * @author FourLeaves
     * @date 2020/5/31 9:25
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMineByParams")
    public R listTaskMineByParams(@RequestParam Map<String, String> params) {
        return R.ok().put("tasks", bpmAdapter.listTaskMineByParams(params))
                .put("total", bpmAdapter.getTaskMineCountByParams(params));
    }




    /**
     * @Author zhangzhaojun
     * @Description //转交点击部门树获取人员
     * @Date 17:48 2021/9/10
     * @Param 预测流程根据部门树的部门ids获取用户列表
     * @return userIds
     **/
    @CrossOrigin
    @RequestMapping("/getAllUsersByDepartmentIds")
    public R getAllUsersByDepartmentId(@RequestParam("departmentId") String departmentId,@RequestParam("flowId") String flowId,
                                       @RequestParam("nodeId") String nodeId,@RequestParam("taskId") String taskId){
        TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
        task.getFlowInstanceId();
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(task.getFlowInstanceId());
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        List<String> workResults = new ArrayList<>();
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(task.getNodeId());
        queryUtil.checkPresetRoute(node, taskId);
        SessionAttribute.initElSession(securityUtils.getSession(), wfiFlow.getId(), node.getFlow(), wfiFlow.getBusinessKey());
        WfdFlowElService wfdFlowElService = SpringContextUtils.getBean(WfdFlowElService.class);
        Integer secretLevel = getSecretLevel(node.getFlow(), wfdFlowElService);
        List<NextStep> nextSteps = bpmAdapter.listNextSteps(wfiFlow, task.getNodeId(), workResults, wfiDeliverIdFrom);
        List<StepUser> sysUserDOS1 = new ArrayList<>();
        for(NextStep nextStep:nextSteps){
            if(nextStep.getNodeId().equals(nodeId)){
                sysUserDOS1 = nextStep.getNextStepUsers();
            }
        }
        List<SysUserDO> sysUserDOS2 = bpmAdapter.getUsersByDepartmentId(departmentId);
        List<SysUserDO> list = new ArrayList<>();
        for(StepUser stepUser:sysUserDOS1){
            for(SysUserDO sysUserDO1:sysUserDOS2){
                if(stepUser.getUserId().equals(sysUserDO1.getId()))
                    list.add(sysUserDO1);
            }
        }
        List<SysUserDO> sysUserDOList = list.stream().filter(s->s.getSecretLevel()<=secretLevel).collect(Collectors.toList());
        return R.ok().put("SysUserList",sysUserDOList);
    }

    /**
     * 获取我的工作列表
     *
     * @param flowCode  流程标识
     * @param current   当前页码
     * @param size      分页大小
     * @param completed 是否办结
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:25
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMineByFlowCode")
    public R listTaskMineByFlowCode(@RequestParam("flowCode") String flowCode, @RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam("completed") Integer completed) {
        return R.ok().put("tasks", bpmAdapter.listTaskMineByFlowCode(flowCode, current, size, completed))
                .put("total", bpmAdapter.getTaskMineTotalByFlowCode(flowCode, completed));
    }

    /**
     * 获取我的工作列表
     *
     * @param completed 是否办结
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:25
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMineAll")
    public R listTaskMineAll(@RequestParam("completed") Integer completed) {
        return listTaskMineAllByFlowCode(QueryUtil.FILTER_ALL, completed);
    }

    /**
     * 获取我的工作列表
     *
     * @param flowCode  流程标识
     * @param completed 是否办结
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:25
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMineAllByFlowCode")
    public R listTaskMineAllByFlowCode(@RequestParam("flowCode") String flowCode, @RequestParam("completed") Integer completed) {
        return R.ok().put("tasks", bpmAdapter.listTaskMineByFlowCode(flowCode, completed));
    }

    /**
     * 获取工作监控列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @param flowId  流程定义id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:23
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMonitor")
    public R listTaskMonitor(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                             @RequestParam("flowId") String flowId, @RequestParam("completed") int completed
            , @RequestParam("searchStr") String searchStr) {
        return bpmAdapter.listTaskMonitor(current, size, flowId, completed, searchStr);
    }

    /**
     * 获取工作监控列表
     *
     * @param flowId 流程定义id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:23
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskMonitorAll")
    public R listTaskMonitorAll(@RequestParam("flowId") String flowId) {
        return R.ok().put("tasks", bpmAdapter.listTaskMonitor(flowId));
    }

    /**
     * 获取有查询权限的流程定义列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/5/28 8:18
     */
    @CrossOrigin
    @RequestMapping(value = "/listFlowsWithQueryAuth")
    public R listFlowsWithQueryAuth() {
        return R.ok().put("list", bpmAdapter.getFlowsByQueryAuth());
    }

    /**
     * 获取有监控权限的流程定义列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/5/28 8:18
     */
    @CrossOrigin
    @RequestMapping(value = "/listFlowsWithAdminAuth")
    public R listFlowsWithAdminAuth() {
        return R.ok().put("list", bpmAdapter.getFlowsByMonitorAuth());
    }

    /**
     * 获取工作查询列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @param flowId  流程定义id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:52
     */
    @CrossOrigin
    @RequestMapping(value = "/listTaskQuery")
    public R listTaskQuery(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                           @RequestParam("flowId") String flowId, @RequestParam("completed") int completed
            , @RequestParam("searchStr") String searchStr) {
        return bpmAdapter.listTaskQuery(current, size, flowId, completed, searchStr);
    }

    /**
     * 获取工作查询列表
     *
     * @param flowId 流程定义id
     * @return 可查询的工作列表
     * @author JonnyJiang
     * @date 2019/10/31 9:52
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskQueryAll")
    public R listTaskQueryAll(@RequestParam("flowId") String flowId) {
        return R.ok().put("tasks", bpmAdapter.listTaskQuery(flowId));
    }


    /**
     * 手动委托工作
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/2/25 14:46
     */
    @CrossOrigin
    @RequestMapping(value = "/delegateWork", method = RequestMethod.POST)
    public R delegateTask(@RequestBody Map<String, String> params) {
        String taskId = params.get("taskId");
        String userId = params.get("userId");
        bpmManager.delegateWork(taskId, userId);
        return R.ok(LocaleUtils.getDelegateSucces());
    }


    /**
     * 获取工作委托列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:53
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskDelegate")
    public R listTaskDelegate(@RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam Map<String, String> params) {
        return bpmAdapter.getTaskDelegateInfo(current, size, params);
//        return R.ok().put("tasks", bpmAdapter.listTaskDelegate(current, size, params)).put("total", bpmAdapter
//                .getTaskDelegateTotal(params));
    }

    /**
     * 获取工作委托列表
     *
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/31 9:53
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskDelegateAll")
    public R listTaskDelegateAll() {
        return R.ok().put("tasks", bpmAdapter.listTaskDelegate());
    }

    /**
     * 获取流程实例
     *
     * @param flowInstanceId 流程实例id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:21
     */

    @CrossOrigin
    @RequestMapping("/getFlowInstance/{flowInstanceId}")
    public R getFlowInstance(@PathVariable("flowInstanceId") String flowInstanceId) {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(flowInstanceId);
        return R.ok().put("flowInstance", flowInstance);
    }


    /**
     * 转交工作
     *
     * @param deliverInfo 转交信息
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:22
     */

    @CrossOrigin
    @RequestMapping(value = "/deliverWork", method = RequestMethod.POST)
    public R deliverWork(@RequestBody DeliverInfo deliverInfo) {
        bpmManager.deliverWork(deliverInfo);
        return R.ok();
    }

    /**
     * @Author zhangzhaojun
     * @Description //TODO 预设流程根据部门获取用户
     * @Date 7:39 2021/9/15
     * @Param departmentId
     * @return user
     **/
    @CrossOrigin
    @RequestMapping(value = "/getPresetUserByDepartmentId", method =  RequestMethod.GET)
    public R getPresetUserByDepartmentId(@RequestParam("departmentId") String departmentId,@RequestParam("flowId") String flowId,@RequestParam("nodeId")String nodeId){
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        SessionAttribute.initElSession(securityUtils.getSession(), flowId, flow, wfiFlow.getBusinessKey());
        Node node = flow.getNodeById(nodeId);
        Integer secretLevel = getSecretLevel(flow, wfdFlowElService);
        List<SysUserDO> sysUsers = wfdCollectionUtils.getUsers(node, secretLevel);
        if(sysUsers.size() > SIZE_SORT_BY_PINYIN)
        {
            sysUsers.sort(((o1, o2) -> StringUtils.compare(o1.getPinyin(), o2.getPinyin())));
        }
        List<SysUserDO> sysUserDOS = bpmAdapter.getUsersByDepartmentId(departmentId);
        List<SysUserDO> list = new ArrayList<>();
        for(SysUserDO sysUserDO:sysUsers){
            for(SysUserDO sysUserDO1:sysUserDOS){
                if(sysUserDO.getUserName().equals(sysUserDO1.getUserName()))
                    list.add(sysUserDO1);
            }
        }
        return R.ok().put("userList",list);
    }

    /**
     * 转交工作
     *
     * @param taskId 任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/7/15 8:43
     */

    @CrossOrigin
    @RequestMapping(value = "/deliverWorkByTaskId/{taskId}", method = RequestMethod.POST)
    public R deliverWorkByTaskId(@PathVariable("taskId") String taskId) {
        bpmManager.deliverWork(taskId);
        return R.ok();
    }

    /**
     * 流程发布
     *
     * @param params id/version/versionStartDate
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:22
     */

    @CrossOrigin
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public R deploy(@RequestBody Map<String, Object> params) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        bpmManager.deploy((String) params.get("id"), (Integer) params.get("version"), LocalDateTime.parse((String)
                params.get("startDate"), df));
        return R.ok();
    }

    /**
     * 获取节点任务信息
     *
     * @param params flowCode/nodeId
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:23
     */

    @CrossOrigin
    @RequestMapping(value = "/getNodeInfo")
    public R getNodeInfo(@RequestParam Map<String, Object> params) {
        String flowCode = (String) params.get("flowCode");
        String nodeId = (String) params.get("nodeId");
        NodeInfo nodeInfo = bpmAdapter.getNodeInfo(flowCode, nodeId);
        return resolveGetNodeInfo(nodeInfo);
    }

    /**
     * 获取任务节点信息
     *
     * @param taskId 任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:24
     */

    @CrossOrigin
    @RequestMapping(value = "/getNodeInfoByTaskId/{taskId}")
    public R getNodeInfoByTaskId(@PathVariable("taskId") String taskId) {
        NodeInfo nodeInfo = bpmAdapter.getNodeInfoByTaskId(taskId);
        return resolveGetNodeInfo(nodeInfo);
    }

    /**
     * 获取任务节点信息，不校验权限
     *
     * @param taskId 任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author Fourleaves
     * @date 2021/05/06 14:08
     */

    @CrossOrigin
    @RequestMapping(value = "/getNodeInfoByTaskIdWithoutAuth/{taskId}")
    public R getNodeInfoByTaskIdWithoutAuth(@PathVariable("taskId") String taskId) {
        NodeInfo nodeInfo = bpmAdapter.getNodeInfoByTaskIdWithoutAuth(taskId);
        return resolveGetNodeInfo(nodeInfo);
    }

    /**
     * 获取任务节点信息
     *
     * @param code        节点标识
     * @param businessKey 业务标识
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:24
     */

    @CrossOrigin
    @RequestMapping(value = "/getNodeInfoByBusinessKey")
    public R getNodeInfoByBusinessKey(@RequestParam("code") String code, @RequestParam("businessKey") String
            businessKey) {
        NodeInfo nodeInfo = bpmAdapter.getNodeInfoByBusinessKey(code, businessKey);
        return resolveGetNodeInfo(nodeInfo);
    }

    private R resolveGetNodeInfo(NodeInfo nodeInfo) {
        if (UserType.None.isEquals(nodeInfo.getUserType())) {
            if (!bpmAdapter.hasQueryAuth(nodeInfo.getFlow().getId(), securityUtils.getCurrentUser())) {
                throw new BpmException(LocaleUtils.getNoAccessToQueryTask(nodeInfo.getTaskId()));
            }
        }
        String[] workResults = null;
        if (nodeInfo.getDeliverInfoFrom() != null) {
            workResults = nodeInfo.getDeliverInfoFrom().getWorkResultOptions();
        }
        if (workResults == null) {
            workResults = new String[0];
        }
        Integer operationType = nodeInfo.getOperationType();
        Integer userType = nodeInfo.getUserType();
        Integer flowPreseted = IntegerUtils.FALSE_VALUE;
        if (StringUtils.isNotEmpty(nodeInfo.getFlow().getFlowInstanceId())) {
            if (bpmAdapter.flowPreseted(nodeInfo.getFlow().getFlowInstanceId())) {
                flowPreseted = IntegerUtils.TRUE_VALUE;
            }
        }
        System.out.println("nodeInfo");
        System.out.println(nodeInfo);
        System.out.println(nodeInfo);
        return R.ok()
                .put("nodeInfo", nodeInfo)
                .put("workResults", workResults)
                .put("operationType", operationType)
                .put("userType", userType)
                .put("flowPreseted", flowPreseted);
    }

    /**
     * 获取后续步骤列表
     *
     * @param taskId 任务ID
     * @return 后续步骤列表
     * @author JonnyJiang
     * @date 2019/11/27 17:25
     */

    @CrossOrigin
    @RequestMapping(value = "/listNextStepsByTaskId/{taskId}")
    public R listNextStepsByTaskId(@PathVariable("taskId") String taskId) {
        TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(task.getFlowInstanceId());
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        return resolveListNextSteps(wfiFlow, task.getNodeId(), null, wfiDeliverIdFrom, task);
    }

    /**
     * 获取后续步骤列表
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/24 14:55
     */

    @CrossOrigin
    @RequestMapping(value = "/listNextStepsByTaskIdWithWorkResults", method = RequestMethod.POST)
    public R listNextStepsByTaskIdWithWorkResults(@RequestBody Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(task.getFlowInstanceId());
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        List<String> workResults = (List<String>) params.get("workResults");
        return resolveListNextSteps(wfiFlow, task.getNodeId(), workResults, wfiDeliverIdFrom, task);
    }

    /**
     * 获取后续步骤列表
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @param nodeId      节点id
     * @return 后续步骤列表
     * @author JonnyJiang
     * @date 2020/5/27 19:13
     */

    @CrossOrigin
    @RequestMapping(value = "/listNextStepsByNodeId")
    public R listNextStepsByNodeId(@RequestParam("code") String code, @RequestParam("businessKey") String
            businessKey, @RequestParam("nodeId") String nodeId) {
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByBusinessKey(code, businessKey);
        return resolveListNextSteps(wfiFlow, nodeId, null, null, null);
    }

    /**
     * 获取后续步骤列表
     *
     * @param params
     * @return 后续步骤列表
     * @author JonnyJiang
     * @date 2020/6/24 14:54
     */

    @CrossOrigin
    @RequestMapping(value = "/listNextStepsByNodeIdWithWorkResults", method = RequestMethod.POST)
    public R listNextStepsByNodeIdWithWorkResults(@RequestBody Map<String, Object> params) {
        String flowCode = (String) params.get("code");
        String businessKey = (String) params.get("businessKey");
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByBusinessKey(flowCode, businessKey);
        String nodeId = (String) params.get("nodeId");
        List<String> workResults = (List<String>) params.get("workResults");
        return resolveListNextSteps(wfiFlow, nodeId, workResults, null, null);
    }

    @CrossOrigin
    @RequestMapping(value = "/getInfoForDeliver", method = RequestMethod.POST)
    public R getInfoForDeliver(@RequestBody Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
        task.getFlowInstanceId();
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(task.getFlowInstanceId());
        String wfiDeliverIdFrom = (String) bpmAdapter.getTaskVariable(taskId, TaskVariableName.WFI_DELIVER_ID_FROM);
        List<String> workResults = (List<String>) params.get("workResults");
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(task.getNodeId());
        queryUtil.checkPresetRoute(node, taskId);
        List<NextStep> nextSteps = bpmAdapter.listNextSteps(wfiFlow, task.getNodeId(), workResults, wfiDeliverIdFrom);
        return R.ok().put("nextSteps", nextSteps);
    }

    @CrossOrigin
    @RequestMapping(value = "/addUrgeTaskDO", method = RequestMethod.GET)
    public R addUrgeTaskDO(String flowId,String nodeId,String taskId) throws ParseException {
        bpmManager.addUrgeTaskDO(flowId,nodeId,taskId);
        return R.ok();
    }

    @CrossOrigin
    @RequestMapping(value = "/reviewFile", method = RequestMethod.GET)
    public R reviewFile(@RequestParam String flowCode,@RequestParam String oldFormId,@RequestParam String newFormId) {
        //getFlowModel
        String flowModel, flowInstanceId, flowNo = "";
        String businessKey = "";
        if (StringUtils.isEmpty(businessKey)) {
            WfdVFlowDO wfdVFlow = bpmAdapter.getEffectiveWfdVFlowByCode(flowCode);
            flowModel = wfdVFlow.getModel();
            flowInstanceId = UUID.randomUUID().toString();
        } else {
            WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByBusinessKey(flowCode, businessKey);
            flowModel = wfiFlow.getModel();
            flowInstanceId = wfiFlow.getId();
            flowNo = wfiFlow.getFlowNo();
        }
        resolveGetFlowModelResult(flowModel, flowNo, flowInstanceId);
        //getNodeInfo
        String nodeId = "";
        NodeInfo nodeInfo = bpmAdapter.getNodeInfo(flowCode, nodeId);
        resolveGetNodeInfo(nodeInfo);

        return R.ok();
    }

    private R resolveListNextSteps(WfiFlowDO wfiFlow, String nodeId, List<String> workResults, String wfiDeliverIdFrom, TaskInstance task) {
        List<NextStep> nextSteps = bpmAdapter.listNextSteps(wfiFlow, nodeId, workResults, wfiDeliverIdFrom);
//        List<OrgDepartmentDO> listT = bpmAdapter.getDepartmentTree(nextSteps);
//        HashSet<String> userIdSet = new HashSet<>();
//        for(NextStep nextStep:nextSteps){
//            userIdSet.add(nextStep.getNextStepUsers().get(0).getUserId());
//        }
//        for(String useridset:userIdSet){
//            SysUserService.
//        }
//        System.out.println(userIdSet);


//        String DepartmentTree = bpmAdapter.getDepartmentTree(nextStepuserId);



//        SysUserDO userDO = sysUserService.getById(userId);

        // TODO: 2021/9/8 添加根据stepUser中的userId获取部门树形结构
//        System.out.println("");
//
//        return R.ok().put("nextSteps", nextSteps).put("depts", depts);
        // TODO: 2021/9/8 添加根据stepUser中的userId获取部门树形结构


        return R.ok().put("nextSteps", nextSteps);
    }

    /**
     * 删除实例
     *
     * @param ids 流程实例集合
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:24
     */

    @CrossOrigin
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public R delete(@RequestBody String[] ids) {
        JSONArray array = new JSONArray();
        String deleteReason = "";
        if (ids != null && ids.length > 0) {
            List<WfiFlowDO> wfiFlowDOS = wfiFlowService.list(new QueryWrapper<WfiFlowDO>()
                    .in("id", (Object[]) ids).select("ID", "FLOW_NO"));
            for (int i = 0; i < ids.length; i++) {
                String id = ids[i];
                String message = LocaleUtils.getDeleteSucces();
                int success = 1;
                try {
                    bpmManager.deleteFlowInstanceById(id, deleteReason);
                } catch (BpmException e) {
                    e.printStackTrace();
                    message = e.getMessage();
                    success = 0;
                }
                JSONObject result = new JSONObject();
                result.put("id", id);
                result.put("success", success);
                result.put("message", message);
                result.put("flowNo", wfiFlowDOS.stream().filter(w -> Objects.equals(id, w.getId())).findFirst().get().getFlowNo());
                array.add(result);
            }
            return R.ok().put("results", array);
        }
        return R.error(LocaleUtils.getNullOrExcepData());
    }

    @CrossOrigin
    @RequestMapping(value = "/deleteFlowInstanceByTaskIds", method = RequestMethod.POST)
    public R deleteFlowInstanceByTaskIds(@RequestBody String[] ids) {
        JSONArray array = new JSONArray();
        String deleteReason = "";
        if (ids != null || ids.length > 0) {
            Integer success = 1;
            String message = LocaleUtils.getDeleteSucces();
            List<WfiTaskPendingDO> wfiTaskPendings = wfiTaskPendingService.listByTaskIds(ids);
            Collection<WfiFlowDO> wfiFlows = wfiFlowService.listByIds(wfiTaskPendings.stream().map(WfiTaskPendingDO::getFlowId).distinct().collect(Collectors.toList()));
            List<String> taskIds = Arrays.asList(ids);
            String taskId;
            WfiTaskPendingDO wfiTaskPending;
            WfiTaskPendingDO wfiTaskPendingTemp;
            Boolean pending;
            SysUserDO currentUser = securityUtils.getCurrentUser();
            String currentUserId = currentUser.getId();
            Boolean hasAdminAuth;
            for (WfiFlowDO wfiFlow : wfiFlows) {
                Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                hasAdminAuth = bpmAdapter.hasAdminAuth(flow, currentUser);
                pending = true;
                for (int i = 0; i < taskIds.size(); i++) {
                    taskId = taskIds.get(i);
                    success = 1;
                    try {
                        if (pending) {
                            wfiTaskPending = null;
                            for (int j = 0; j < wfiTaskPendings.size(); j++) {
                                wfiTaskPendingTemp = wfiTaskPendings.get(j);
                                if (StringUtils.equals(taskId, wfiTaskPendingTemp.getTaskId())) {
                                    wfiTaskPending = wfiTaskPendingTemp;
                                    break;
                                }
                            }
                            if (wfiTaskPending == null) {
                                throw new WfiTaskPendingNotFoundException(wfiFlow.getId(), taskId, securityUtils.getCurrentUserId());
                            } else {
                                bpmManager.deleteFlowInstanceByTaskPending(wfiFlow, flow, wfiTaskPending, hasAdminAuth, currentUserId, deleteReason);
                            }
                            pending = false;
                        }
                    } catch (BpmException e) {
                        e.printStackTrace();
                        message = e.getMessage();
                        success = 0;
                    }
                    JSONObject result = new JSONObject();
                    result.put("id", taskId);
                    result.put("success", success);
                    result.put("message", message);
                    result.put("flowNo", wfiFlow.getFlowNo());
                    array.add(result);
                }
            }
            return R.ok().put("results", array);
        }
        return R.error(LocaleUtils.getNullOrExcepData());
    }

    /**
     * 接收任务
     *
     * @param taskId 任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:25
     */

    @CrossOrigin
    @RequestMapping(value = "/claimTask/{taskId}")
    public R claimTask(@PathVariable("taskId") String taskId) {
        bpmManager.claim(taskId);
        return R.ok();
    }

    /**
     * 撤销接收任务
     *
     * @param taskId 任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/10/14 20:25
     */

    @CrossOrigin
    @RequestMapping(value = "/revokeClaimTask/{taskId}")
    public R revokeClaimTask(@PathVariable("taskId") String taskId) {
        bpmManager.revokeClaim(taskId);
        return R.ok();
    }

    /**
     * 获取流程监听列表
     *
     * @param flowEventType 流程监听类型
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/14 9:36
     */

    @CrossOrigin
    @RequestMapping(value = "/listFlowListeners/{flowEventType}")
    public R listFlowListeners(@PathVariable("flowEventType") Integer flowEventType) {
        return R.ok().put("listeners", ListenerScanner.getFlowListeners().get(com.csicit.ace.bpm.delegate
                .FlowEventType.getFlowEventType(flowEventType)));
    }

    /**
     * 获取步骤监听列表
     *
     * @param taskEventType 步骤监听类型
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/14 9:35
     */

    @CrossOrigin
    @RequestMapping(value = "/listTaskListeners/{taskEventType}")
    public R listTaskListeners(@PathVariable("taskEventType") Integer taskEventType) {
        return R.ok().put("listeners", ListenerScanner.getTaskListeners().get(com.csicit.ace.bpm.delegate
                .TaskEventType.getTaskEventType(taskEventType)));
    }

    /**
     * 获取所有节点
     *
     * @param flowCode 流程标识
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/14 9:35
     */

    @CrossOrigin
    @RequestMapping(value = "/listNodesByFlowCode/{flowCode}")
    public R listNodesByFlowCode(@PathVariable("flowCode") String flowCode) {
        return R.ok().put("nodes", bpmAdapter.listNodesByFlowCode(flowCode));
    }

    /**
     * 获取人工节点
     *
     * @param flowCode 流程标识
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/14 9:35
     */

    @CrossOrigin
    @RequestMapping(value = "/listManualNodesByFlowCode/{flowCode}")
    public R listManualNodesByFlowCode(@PathVariable("flowCode") String flowCode) {
        return R.ok().put("nodes", bpmAdapter.listManualNodesByFlowCode(flowCode));
    }

    /**
     * 获取办理过程
     *
     * @param taskId 任务ID
     * @return 办理过程
     * @author JonnyJiang
     * @date 2019/11/20 16:07
     */

    @CrossOrigin
    @RequestMapping(value = "/getHandingProcessByTaskId/{taskId}")
    public R getHandingProcessByTaskId(@PathVariable("taskId") String taskId) {
        return R.ok().put("flow", bpmAdapter.getHandingProcessByTaskId(taskId));
    }

    /**
     * 获取办理过程
     *
     * @param taskId 任务ID
     * @return 办理过程
     * @author JonnyJiang
     * @date 2019/11/27 11:29
     */

    @CrossOrigin
    @RequestMapping(value = "/getProcessFlowByTaskId/{taskId}")
    public R getProcessFlowByTaskId(@PathVariable("taskId") String taskId) {
        return R.ok().put("flow", bpmAdapter.getProcessFlowByTaskId(taskId));
    }

    /**
     * 办理办理过程
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 办理过程
     * @author JonnyJiang
     * @date 2019/11/26 9:04
     */

    @CrossOrigin
    @RequestMapping(value = "/getHandingProcessByBusinessKey")
    public R getHandingProcessByBusinessKey(@RequestParam("flowCode") String flowCode, @RequestParam("businessKey")
            String businessKey) {
        return R.ok().put("flow", bpmAdapter.getHandingProcessByBusinessKey(flowCode, businessKey));
    }

    /**
     * 获取办理过程
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 办理过程
     * @author JonnyJiang
     * @date 2019/11/27 11:30
     */

    @CrossOrigin
    @RequestMapping(value = "/getProcessFlowByBusinessKey")
    public R getProcessFlowByBusinessKey(@RequestParam("flowCode") String flowCode, @RequestParam("businessKey")
            String businessKey) {
        return R.ok().put("flow", bpmAdapter.getProcessFlowByBusinessKey(flowCode, businessKey));
    }

    /**
     * 获取最大序列号
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author Zuo Gang
     * @date 2019/11/14 9:33
     */

    @CrossOrigin
    @RequestMapping(value = "/getMaxSortNo", method = RequestMethod.POST)
    public R getMaxSortNo(@RequestBody Map<String, Object> params) {
        return R.ok().put("maxSortNo", ADD_INDEX + sqlUtils.getMaxSort(params));
    }

    /**
     * 获取活动列表
     *
     * @param id 流程实例id
     * @return 活动列表
     * @author JonnyJiang
     * @date 2019/11/26 9:01
     */

    @CrossOrigin
    @RequestMapping(value = "/listActivities/{id}")
    public R listActivities(@PathVariable("id") String id) {
        return R.ok().put("list", bpmAdapter.listActivities(id));
    }

    /**
     * 获取活动列表
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 活动列表
     * @author JonnyJiang
     * @date 2019/11/26 9:02
     */

    @CrossOrigin
    @RequestMapping(value = "/listActivitiesByBusinessKey")
    public R listActivitiesByBusinessKey(@RequestParam("flowCode") String flowCode, @RequestParam("businessKey")
            String businessKey) {
        return R.ok().put("list", bpmAdapter.listActivitiesByBusinessKey(flowCode, businessKey));
    }

    /**
     * 获取活动列表
     *
     * @param taskId 任务id
     * @return 活动列表
     * @author JonnyJiang
     * @date 2019/11/26 9:03
     */

    @CrossOrigin
    @RequestMapping(value = "/listActivitiesByTaskId/{taskId}")
    public R listActivitiesByTaskId(@PathVariable("taskId") String taskId) {
        return R.ok().put("list", bpmAdapter.listActivitiesByTaskId(taskId));
    }

    /**
     * 获取办理过程-任务列表
     *
     * @param current     当前页码
     * @param size        分页大小
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 任务列表
     * @author JonnyJiang
     * @date 2019/11/27 14:39
     */

    @CrossOrigin
    @RequestMapping(value = "/listAllProcessTasksByBusinessKey")
    public R listAllProcessTasksByBusinessKey(@RequestParam("current") Integer current, @RequestParam("size") Integer
            size, @RequestParam("flowCode") String flowCode, @RequestParam("businessKey") String businessKey) {
        return R.ok()
                .put("tasks", bpmAdapter.listAllProcessTasksByBusinessKey(current, size, flowCode, businessKey))
                .put("total", bpmAdapter.getAllProcessTasksCountByBusinessKey(flowCode, businessKey));
    }

    /**
     * 获取办理过程-任务列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @param taskId  任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/27 14:41
     */

    @CrossOrigin
    @RequestMapping(value = "/listAllProcessTasksByTaskId")
    public R listAllProcessTasksByTaskId(@RequestParam("current") Integer current, @RequestParam("size") Integer
            size, @RequestParam("taskId") String taskId) {
        return R.ok().put("tasks", bpmAdapter.listAllProcessTasksByTaskId(current, size, taskId))
                .put("total", bpmAdapter.getAllProcessTasksCountByTaskId(taskId));
    }

    /**
     * 获取办理过程-任务列表
     *
     * @param current     当前页码
     * @param size        分页大小
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @param nodeId      节点id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/27 14:42
     */

    @CrossOrigin
    @RequestMapping(value = "/listProcessTasksByBusinessKey")
    public R listProcessTasksByBusinessKey(@RequestParam("current") Integer current, @RequestParam("size") Integer
            size, @RequestParam("flowCode") String flowCode, @RequestParam("businessKey") String businessKey,
                                           @RequestParam("nodeId") String nodeId) {
        return R.ok().put("tasks", bpmAdapter.listProcessTasksByBusinessKey(current, size, flowCode, businessKey,
                nodeId))
                .put("total", bpmAdapter.getProcessTasksCountByBusinessKey(flowCode, businessKey, nodeId));
    }

    /**
     * 获取办理过程-任务列表
     *
     * @param current 当前页码
     * @param size    分页大小
     * @param taskId  任务id
     * @param nodeId  节点id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/11/27 14:43
     */

    @CrossOrigin
    @RequestMapping(value = "/listProcessTasksByTaskId")
    public R listProcessTasksByTaskId(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                      @RequestParam("taskId") String taskId, @RequestParam("nodeId") String nodeId) {
        return R.ok().put("tasks", bpmAdapter.listProcessTasksByTaskId(current, size, taskId, nodeId))
                .put("total", bpmAdapter.getProcessTasksCountByTaskId(taskId, nodeId));
    }

    /**
     * 获取可以驳回到的任务列表
     *
     * @param taskId 任务id
     * @return 任务列表
     * @author yansiyang
     * @date 2019/12/10 17:58
     */

    @CrossOrigin
    @RequestMapping("/listTasksRejectTo/{taskId}")
    public R listTaskRejectTo(@PathVariable("taskId") String taskId) {
        TaskInstance taskInstance = bpmAdapter.getTaskInstanceById(taskId);
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(taskInstance.getFlowInstanceId());
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        Node node = flow.getNodeById(taskInstance.getNodeId());
        if (IntegerUtils.isTrue(node.getEnableReject())) {
            // 驳回到第一步
            if (NodeRejectTo.First.isEquals(node.getRejectTo())) {
                TaskRejectTo taskRejectTo = bpmAdapter.getTaskRejectToByRejectToFirst(wfiFlow, flow, node);
                if (taskRejectTo != null) {
                    return R.ok().put("switchTasks", taskRejectTo).put("rejectTo", node.getRejectTo());
                }
            } else if (NodeRejectTo.Specific.isEquals(node.getRejectTo())) {
                // 是否驳回到指定步骤
                return R.ok().put("switchTasks", bpmAdapter.listTaskRejectToByRejectToSpecified(wfiFlow, flow, node, taskId)).put("rejectTo", node.getRejectTo());
            } else if (NodeRejectTo.Last.isEquals(node.getRejectTo())) {
                List<TaskRejectTo> taskRejectTosT = bpmAdapter.listTaskRejectToByRejectToLast(wfiFlow, flow, node);
                if (taskRejectTosT.size() > 0) {
                    return R.ok().put("switchTasks", taskRejectTosT).put("rejectTo", node.getRejectTo());
                }
            }
        }
        return R.error(LocaleUtils.getNodeUnsupportReject(node.getCode(), node.getName()));
    }


    @CrossOrigin
    @RequestMapping(value = "/rejectWork", method = RequestMethod.POST)
    public R rejectWork(@RequestBody RejectInfo rejectInfo) {
        bpmManager.rejectWork(rejectInfo);
        return R.ok();
    }

    /**
     * @Author zhangzhaojun
     * @Description //TODO 催办工作
     * @Date 17:12 2021/8/31
     * @Param taskId
     * @return
     **/
//
//    @AceAuth("催办提醒")
    @CrossOrigin
    @RequestMapping(value = "/urgeTask", method = RequestMethod.POST)
    public R urgeTask(@RequestParam String flowId,String nodeId) {
          bpmManager.urgeTask();
        return R.ok();
    }


    /**
     * @Author zhangzhaojun
     * @Description //TODO 催办工作
     * @Date 17:12 2021/8/31
     * @Param taskId
     * @return
     **/

    @CrossOrigin
    @RequestMapping(value = "/removeUrgeTask", method = RequestMethod.POST)
    public R remove(@RequestParam String taskId) {
        bpmManager.removeUrgeTask(taskId);
        return R.ok();
    }

    /**
     * 撤回
     *
     * @param flowInstanceId 实例ID
     * @param taskId         撤回目标节点
     * @return
     * @author FourLeaves
     * @date 2020/2/10 17:10
     */
    @CrossOrigin
    @RequestMapping(value = "/recoverFlowInstance/{flowInstanceId}/{taskId}")
    public R recoverFlowInstance(@PathVariable("flowInstanceId") String flowInstanceId, @PathVariable("taskId") String taskId) {
        if (StringUtils.isBlank(flowInstanceId) || StringUtils.isBlank(taskId)) {
            return R.error();
        }
        bpmManager.recoverFlowInstance(flowInstanceId, taskId);
        return R.ok();
    }

    /**
     * 获取委托规则
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return
     * @author FourLeaves
     * @date 2020/2/19 15:33
     */
    @CrossOrigin
    @RequestMapping(value = "/delegateRules")
    public R getDelegateRuleList(@RequestParam("current") Integer current, @RequestParam("size") Integer size) {
        return wfdDelegateRuleService.getDelegateRuleList(current, size);
    }

    /**
     * 获取别人委托给自己的委托规则
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return
     * @author FourLeaves
     * @date 2020/2/19 15:33
     */
    @CrossOrigin
    @RequestMapping(value = "/delegateRulesOfMe")
    public R getDelegateRuleListOfMe(@RequestParam("current") Integer current, @RequestParam("size") Integer size) {
        return wfdDelegateRuleService.getDelegateRuleListOfMe(current, size);
    }

    /**
     * 保存委托规则
     *
     * @param wfdDelegateRuleDO
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:10
     */
    @CrossOrigin
    @RequestMapping(value = "/delegateRules", method = RequestMethod.POST)
    public R saveDelegateRule(@RequestBody WfdDelegateRuleDO wfdDelegateRuleDO) {
        if (wfdDelegateRuleService.saveDelegateRule(wfdDelegateRuleDO)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除委托规则
     *
     * @param ids
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:11
     */
    @CrossOrigin
    @RequestMapping(value = "/delegateRules", method = RequestMethod.DELETE)
    public R removeDelegateRules(@RequestBody List<String> ids) {
        if (wfdDelegateRuleService.removeDelegateRules(ids)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 启用委托规则
     *
     * @param ids
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:11
     */
    @CrossOrigin
    @RequestMapping(value = "/startDelegateRules", method = RequestMethod.POST)
    public R startDelegateRules(@RequestBody List<String> ids) {
        if (wfdDelegateRuleService.startDelegateRules(ids)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 暂停委托规则
     *
     * @param ids
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:11
     */
    @CrossOrigin
    @RequestMapping(value = "/stopDelegateRules", method = RequestMethod.POST)
    public R stopDelegateRules(@RequestBody List<String> ids) {
        if (wfdDelegateRuleService.stopDelegateRules(ids)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 获取用户默认显示页面
     *
     * @param flowCode
     * @return
     * @author FourLeaves
     * @date 2020/4/10 08:47
     */
    @CrossOrigin
    @RequestMapping(value = "/wfdUserPage/{flowCode}", method = RequestMethod.GET)
    public R getUserPage(@PathVariable("flowCode") String flowCode) {
        if (StringUtils.isNotBlank(flowCode)) {
            String userId = securityUtils.getCurrentUserId();
            WfdUserPageDO wfdUserPageDO = wfdUserPageService.getOne(new QueryWrapper<WfdUserPageDO>()
                    .eq("user_id", userId).eq("wfd_id", flowCode));
            if (wfdUserPageDO != null) {
                return R.ok().put("userPage", wfdUserPageDO);
            }
            wfdUserPageDO = new WfdUserPageDO();
            wfdUserPageDO.setPageCode("tab1");
            wfdUserPageDO.setPageSize(10);
            wfdUserPageDO.setUserId(userId);
            wfdUserPageDO.setWfdId(flowCode);
            return R.ok().put("userPage", wfdUserPageDO);
        }
        WfdUserPageDO wfdUserPageDO = new WfdUserPageDO();
        wfdUserPageDO.setPageCode("tab1");
        wfdUserPageDO.setPageSize(10);
        wfdUserPageDO.setUserId(securityUtils.getCurrentUserId());
        return R.ok().put("userPage", wfdUserPageDO);
    }

    /**
     * 修改用户默认显示页面
     *
     * @param wfdUserPageDO
     * @param flowCode
     * @return
     * @author FourLeaves
     * @date 2020/4/10 08:47
     */
    @CrossOrigin
    @RequestMapping(value = "/wfdUserPage/{flowCode}", method = RequestMethod.PUT)
    public R updateUserPage(@RequestBody WfdUserPageDO wfdUserPageDO, @PathVariable("flowCode") String flowCode) {
        if (StringUtils.isBlank(wfdUserPageDO.getWfdId()) && StringUtils.isNotBlank(flowCode)) {
            wfdUserPageDO.setWfdId(flowCode);
        }
        if (wfdUserPageService.saveOrUpdate(wfdUserPageDO)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 获取已关注工作并分页
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/5/27 8:51
     */
    @CrossOrigin
    @RequestMapping(value = "/listFocusedWorks", method = RequestMethod.GET)
    public R listFocusedWorks(@RequestParam Map<String, String> params) {
        return bpmAdapter.listTaskFocused(params);
    }

    /**
     * 关注 或 取消关注工作
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/5/27 8:51
     */
    @CrossOrigin
    @RequestMapping(value = "/focusWork", method = RequestMethod.POST)
    public R focusWork(@RequestBody Map<String, Object> params) {
        if (bpmManager.focusWork(params)) {
            return R.ok();
        }
        return R.error(LocaleUtils.getWfiFocusWork());
    }

    /**
     * 更新用户关注的工作的最新一次阅读时间
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/16 10:38
     */
    @CrossOrigin
    @RequestMapping(value = "/updateFocusWorkReadTime", method = RequestMethod.GET)
    public R updateFocusWorkReadTime(@RequestParam Map<String, String> params) {
        bpmManager.updateFocusWorkReadTime(params);
        return R.ok();
    }

    /**
     * 是否关注此流程
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/5/27 8:51
     */
    @CrossOrigin
    @RequestMapping(value = "/isFocusWork", method = RequestMethod.GET)
    public R isFocusWork(@RequestParam Map<String, String> params) {
        return R.ok().put("isFocus", bpmAdapter.isFocused(params));
    }


    /**
     * 获取流程定义
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/5/6 19:00
     */

    @CrossOrigin
    @RequestMapping("/getFlowModelByCode")
    public R getFlowModelByCode(@RequestParam("flowCode") String flowCode, @RequestParam(value = "businessKey", required = false) String businessKey) {
        String flowModel, flowInstanceId, flowNo = "";
        if (StringUtils.isEmpty(businessKey)) {
            WfdVFlowDO wfdVFlow = bpmAdapter.getEffectiveWfdVFlowByCode(flowCode);
            flowModel = wfdVFlow.getModel();
            flowInstanceId = UUID.randomUUID().toString();
        } else {
            WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByBusinessKey(flowCode, businessKey);
            flowModel = wfiFlow.getModel();
            flowInstanceId = wfiFlow.getId();
            flowNo = wfiFlow.getFlowNo();
        }
        return resolveGetFlowModelResult(flowModel, flowNo, flowInstanceId);
    }

    @CrossOrigin
    @RequestMapping("/getFlowModelByTaskId")
    public R getFlowModelByTaskId(@RequestParam("taskId") String taskId) {
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByTaskId(taskId);
        return resolveGetFlowModelResult(wfiFlow.getModel(), wfiFlow.getFlowNo(), wfiFlow.getId());
    }

    private R resolveGetFlowModelResult(String flowModel, String flowNo, String flowInstanceId) {
        return R.ok().put("flowModel", flowModel).put("flowInstanceId", flowInstanceId).put("flowNo", flowNo);
    }

    /**
     * 加签
     *
     * @param params taskId/userIds
     * @return 加签结果
     * @author JonnyJiang
     * @date 2020/6/2 14:53
     */

    @CrossOrigin
    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public R invite(@RequestBody Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        List<String> userIds = (List<String>) params.get("userIds");
        bpmManager.invite(taskId, userIds);
        return R.ok();
    }
    /***
     * @description: 附件审查流程
     * @params: @RequestBody HashMap<String,String> params 添加附件流程的信息
     * @return: com.csicit.ace.common.utils.server.R
     * @author: Zhangzhaojun
     * @time: 2021/12/6 11:05
     */
    @CrossOrigin
    @RequestMapping(value = "/createReviewFile", method = RequestMethod.POST)
    public R createReviewFile(@RequestBody HashMap<String, String> params) {
        bpmManager.createReviewFile(params);
        return R.ok();
    }

    /***
     * @description:  获取附件审查信息
     * @params: 获取附件审查信息
     * @return: com.csicit.ace.common.utils.server.R
     * @author: Zhangzhaojun
     * @time: 2021/12/6 14:53
     */
    @RequestMapping(value = "/FileReview/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysReviewFile instance = sysReviewFileService.getById(id);
        return R.ok().put("instance", instance);
    }
}