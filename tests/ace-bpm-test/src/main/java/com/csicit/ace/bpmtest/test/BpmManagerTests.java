package com.csicit.ace.bpmtest.test;

import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2019/9/6 16:03
 */
@Component
public class BpmManagerTests {
    @Autowired
    BpmManager bpmManager;
    @Autowired
    BpmAdapter bpmAdapter;
    @Autowired
    WfdFlowService wfdFlowService;
    @Autowired
    DeployTests deployTests;

    private FlowInstance createFlowInstanceById(String id, SysUserDO initiator) {
        deployTests.deployTestC(wfdFlowService, bpmManager);
        FlowInstance flowInstance = bpmManager.createFlowInstanceById(id, UUID.randomUUID().toString(), initiator);
        return flowInstance;
    }

    private FlowInstance createFlowInstanceByCode(String code, SysUserDO initiator) {
        deployTests.deployTestC(wfdFlowService, bpmManager);
        FlowInstance flowInstance = bpmManager.createFlowInstanceByCode(code, UUID.randomUUID().toString(), initiator);
        return flowInstance;
    }

    public void testCreateFlowInstanceById() {
        FlowInstance flowInstance = createFlowInstanceById(DeployTests.TEST_C, new SysUserDO());
        assert DeployTests.TEST_C.equals(flowInstance.getCode());
    }

    public void testCreateFlowInstanceByCode() {
        FlowInstance flowInstance = createFlowInstanceByCode(DeployTests.CODE_C, new SysUserDO());
        assert DeployTests.TEST_C.equals(flowInstance.getCode());
    }

    public void testDeleteFlowInstanceById() {
        FlowInstance flowInstance = createFlowInstanceById(DeployTests.TEST_C, new SysUserDO());
        List<String> businessKeys = new ArrayList<>();
        businessKeys.add(flowInstance.getBusinessKey());
        businessKeys.add(UUID.randomUUID().toString());
        String deleteReason = "testDeleteFlowInstanceById";
        bpmManager.deleteFlowInstanceByFlowId(DeployTests.TEST_C, businessKeys, deleteReason);
    }

    public void testDeleteFlowInstanceByCode() {
        FlowInstance flowInstance = createFlowInstanceByCode(DeployTests.CODE_C, new SysUserDO());
        List<String> businessKeys = new ArrayList<>();
        businessKeys.add(flowInstance.getBusinessKey());
        businessKeys.add(UUID.randomUUID().toString());
        String deleteReason = "testDeleteFlowInstanceById";
        bpmManager.deleteFlowInstanceByCode(DeployTests.CODE_C, businessKeys, deleteReason);
    }

    public FlowInstance createTestPurchaseApply() {
        return createTest("Test.PurchaseApply", UUID.randomUUID().toString());
    }

    public FlowInstance createTest(String flowCode, String businessKey) {
        return bpmManager.createFlowInstanceByCode(flowCode, businessKey);
    }

    public void deliverWorkByFlowId(String flowId) {
        List<TaskInstance> taskInstances = bpmAdapter.listTasksByFlowInstanceId(flowId);
        for (TaskInstance task : taskInstances) {
            bpmManager.deliverWork(task.getId());
        }
    }

    public void showTasks(String flowId) {
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowId);
        Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
        List<TaskInstance> taskInstances = getTasks(flowId);
        for (TaskInstance task : taskInstances) {
            System.out.println("*******************TASK*******************");
            System.out.println("Id: " + task.getId());
            Node node = flow.getNodeById(task.getNodeId());
            System.out.println("Name: " + node.getName());
            System.out.println("Owner: " + task.getOwner());
            System.out.println("Assignee: " + task.getAssignee());
        }
    }

    public List<TaskInstance> getTasks(String flowId) {
        List<TaskInstance> taskInstances = bpmAdapter.listTasksByFlowInstanceId(flowId);
        return taskInstances;
    }

    public void claimTasksByFlowId(String flowId) {
        List<TaskInstance> taskInstances = bpmAdapter.listTasksByFlowInstanceId(flowId);
        for (TaskInstance taskInstance : taskInstances) {
            bpmManager.claim(taskInstance.getId());
        }
    }

    public void showTaskUsers(String taskId) {
        List<TaskUser> taskUsers = bpmAdapter.getTaskUsersByTaskId(taskId);

        for (int i = 0; i < taskUsers.size(); i++) {
            TaskUser taskUser = taskUsers.get(i);
            System.out.println(i + ">UserId: " + taskUser.getUserId());
            System.out.println(i + ">UserType: " + taskUser.getUserType());
        }
    }

    public void revokeClaimTasksByFlowId(String flowId) {
        List<TaskInstance> taskInstances = bpmAdapter.listTasksByFlowInstanceId(flowId);
        for (TaskInstance taskInstance : taskInstances) {
            bpmManager.revokeClaim(taskInstance.getId());
        }
    }
}