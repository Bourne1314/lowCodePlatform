package com.csicit.ace.bpmtest.controller;

import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.pojo.vo.RejectInfo;
import com.csicit.ace.bpm.pojo.vo.TaskRejectTo;
import com.csicit.ace.bpmtest.service.StudentService;
import com.csicit.ace.bpmtest.test.BpmManagerTests;
import com.csicit.ace.common.utils.server.R;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BpmManagerTest
 *
 * @author JonnyJiang
 * @date 2019/9/6 15:36
 */
@RestController
@RequestMapping("/bpmManagerTest")
public class BpmManagerTestController {
    @Autowired
    BpmManagerTests bpmManagerTests;
    @Autowired
    BpmManager bpmManager;
    @Autowired
    BpmAdapter bpmAdapter;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    private StudentService studentService;

    @RequestMapping("/testCreateFlowInstanceById")
    public void testCreateFlowInstanceById() {
        bpmManagerTests.testCreateFlowInstanceById();
    }

    @RequestMapping("/testCreateFlowInstanceByCode")
    public void testCreateFlowInstanceByCode() {
        bpmManagerTests.testCreateFlowInstanceByCode();
    }

    @RequestMapping("/testDeleteFlowInstanceById")
    public void testDeleteFlowInstanceById() {
        bpmManagerTests.testDeleteFlowInstanceById();
    }

    @RequestMapping("/testDeleteFlowInstanceByCode")
    public void testDeleteFlowInstanceByCode() {
        bpmManagerTests.testDeleteFlowInstanceByCode();
    }

    @RequestMapping("/deleteFlowInstances")
    public R deleteFlowInstances() {
        List<String> businessKeys = new ArrayList<>();
        businessKeys.add("1111111");
        businessKeys.add("22222");
        bpmManager.deleteFlowInstanceByCode("bpm-test.01", businessKeys, "");
        return R.ok();
    }

    @RequestMapping("/deleteFlowInstance/{id}")
    public R deleteFlowInstance(@PathVariable("id") String id) {
        bpmManager.deleteFlowInstanceById(id, "");
        return R.ok();
    }

    @RequestMapping("/createTestPurchaseApply")
    public R createTestPurchaseApply() {
        try {
            FlowInstance flowInstance = bpmManagerTests.createTestPurchaseApply();
            return R.ok().put("flow", flowInstance);
        } catch (BpmException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/createTest")
    public R createTest(@RequestParam("flowCode") String flowCode, @RequestParam("businessKey") String businessKey) {
        try {
            return R.ok().put("flow", studentService.createInstance(flowCode, businessKey));
        } catch (BpmException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/testFlowNo")
    public R testFlowNo(@RequestParam("flowCode") String flowCode, @RequestParam("variableName") String variableName) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put(variableName, "自定义参数");
            return R.ok().put("flow", bpmManager.createFlowInstanceByCode(flowCode, UUID.randomUUID().toString(), map));
        } catch (BpmException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/deliverWorkByFlowId", method = RequestMethod.POST)
    public R deliverWorkByFlowId(@RequestBody Map<String, Object> params) {
        String flowId = (String) params.get("flowId");
        try {
            bpmManagerTests.deliverWorkByFlowId(flowId);
            return R.ok();
        } catch (BpmException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/showTasks/{flowId}")
    public R showTasks(@PathVariable("flowId") String flowId) {
        return R.ok().put("tasks", bpmManagerTests.getTasks(flowId));
    }

    @RequestMapping(value = "/showTaskUsers/{flowId}")
    public R showTaskUsers(@PathVariable("flowId") String flowId) {
        List<TaskInstance> tasks = bpmAdapter.listTasksByFlowInstanceId(flowId);
        for (TaskInstance task : tasks) {
            bpmManagerTests.showTaskUsers(task.getId());
        }
        return R.ok();
    }

    @RequestMapping(value = "/claimTasksByFlowId", method = RequestMethod.POST)
    public R claimTasksByFlowId(@RequestBody Map<String, Object> params) {
        String flowId = (String) params.get("flowId");
        bpmManagerTests.claimTasksByFlowId(flowId);
        return R.ok();
    }

    @RequestMapping(value = "/revokeClaimTasksByFlowId", method = RequestMethod.POST)
    public R revokeClaimTasksByFlowId(@RequestBody Map<String, Object> params) {
        String flowId = (String) params.get("flowId");
        bpmManagerTests.revokeClaimTasksByFlowId(flowId);
        return R.ok();
    }

    @RequestMapping(value = "/revokeDeploy", method = RequestMethod.POST)
    public R revokeDeploy(@RequestBody Map<String, Object> params) {
        String flowId = (String) params.get("flowId");
        Integer version = (Integer) params.get("version");
        try {
            bpmManager.revokeDeploy(flowId, version);
            return R.ok();
        } catch (BpmException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/deployBpmn", method = RequestMethod.GET)
    public R deployBpmn() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("xxx.bpmn").deploy();
        return R.ok();
    }

    @RequestMapping("/rejectWork")
    public R rejectWork(@RequestParam("currentTaskId") String currentTaskId, @RequestParam("targetNodeCode") String targetNodeCode) {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(bpmAdapter.getTaskInstanceById(currentTaskId));
        List<TaskInstance> taskInstances = bpmAdapter.listCompletedTasksByFlowInstanceId(flowInstance.getId());
        if (taskInstances.size() == 0) {
            throw new BpmException("没有任务，流程实例id：" + flowInstance.getId());
        }
        List<TaskInstance> targetTaskInstances = taskInstances.stream().filter(o -> targetNodeCode.equals(o.getNodeCode())).collect(Collectors.toList());
        if (targetTaskInstances.size() == 0) {
            throw new BpmException("没有任务");
        }
        List<TaskRejectTo> taskRejectTos = new ArrayList<>();
        TaskRejectTo taskRejectTo = new TaskRejectTo();
        taskRejectTo.setTaskId(targetTaskInstances.get(targetTaskInstances.size() - 1).getId());
        taskRejectTos.add(taskRejectTo);
        RejectInfo rejectInfo = new RejectInfo();
        rejectInfo.setTaskId(currentTaskId);
        rejectInfo.setTaskRejectTos(taskRejectTos);
        rejectInfo.setRejectReason("测试");
        bpmManager.rejectWork(rejectInfo);
        return R.ok();
    }

    @RequestMapping("/deliverWork/{taskId}")
    public R deliverWork(@PathVariable("taskId") String taskId) {
        bpmManager.deliverWork(taskId);
        return R.ok();
    }

    @RequestMapping("/claimWork/{taskId}")
    public R claimWork(@PathVariable("taskId") String taskId) {
        bpmManager.claim(taskId);
        return R.ok();
    }

    @RequestMapping("/revokeClaimWork/{taskId}")
    public R revokeClaimWork(@PathVariable("taskId") String taskId) {
        bpmManager.revokeClaim(taskId);
        return R.ok();
    }

    @RequestMapping("/deleteInvalid")
    public R deleteInvalid() {
        bpmManager.deleteInvalidFlowInstances();
        return R.ok();
    }
//
//    @CrossOrigin
//    @RequestMapping("/backup/{flowId}/{taskId}")
//    public R backup(@PathVariable("flowId") String flowId, @PathVariable("taskId") String taskId) {
//        bpmManager.backupFlowInstance(flowId, taskId, "测试");
//        return R.ok();
//    }

    @CrossOrigin
    @RequestMapping("/recover/{flowId}/{taskId}")
    public R recover(@PathVariable("flowId") String flowId, @PathVariable("taskId") String taskId) {
        bpmManager.recoverFlowInstance(flowId, taskId);
        return R.ok();
    }
}
