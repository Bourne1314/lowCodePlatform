package com.csicit.ace.bpmtest.test;

import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.enums.FlowInMode;
import com.csicit.ace.bpm.enums.FlowOutMode;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.service.WfdVFlowService;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2019/9/23 18:58
 */
@Component
public class SubflowTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTests.class);
    @Autowired
    private BpmManager bpmManager;
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    private WfdFlowService wfdFlowService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private WfdVFlowService wfdVFlowService;
    @Autowired
    private WfiFlowService wfiFlowService;

    private static final String TEST_A = "SubflowTest_ID_A";
    private static final String TEST_B = "SubflowTest_ID_B";
    private static final String TEST_C = "SubflowTest_ID_C";

    private static final String CODE_A = "SubflowTest_CODE_A";
    private static final String CODE_B = "SubflowTest_CODE_B";
    private static final String CODE_C = "SubflowTest_CODE_C";

    /**
     * 测试调用式子流程
     * 测试主流程是否能够直接提交到子流程的某一个任务
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/9/23 19:05
     */

    public void testA() {
        wfdFlowService.removeById(TEST_A);
        String subflowId = TEST_A + "_Subflow";
        String subflowCode = CODE_A + "_Subflow";
        wfdFlowService.removeById(subflowId);
        // 创建子流程
        Flow subflow = new Flow();
        subflow.setId(subflowId);
        subflow.setCode(subflowCode);
        subflow.setName("TEST A SUBFLOW");

        Node subflowNodeStart = FlowUtils.getNode(subflow.getId() + "_begin", "10", "begin", NodeType.Start);
        subflowNodeStart.setEnableApiStart(1);
        subflow.addNode(subflowNodeStart);

        Node subflowNodeApply = FlowUtils.getNode(subflow.getId() + "_apply", "20", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        subflow.addNode(subflowNodeApply);

        Node subflowNodeEnd = FlowUtils.getNode(subflow.getId() + "_end", "30", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        subflow.addNode(subflowNodeEnd);

        Link subflowLink0 = new Link();
        subflowLink0.setId(subflow.getId() + "_LINK0");
        subflowLink0.setFromNode(subflowNodeStart);
        subflowLink0.setToNode(subflowNodeApply);
        subflow.addLink(subflowLink0);

        Link subflowLink1 = new Link();
        subflowLink1.setId(subflow.getId() + "_LINK1");
        subflowLink1.setFromNode(subflowNodeApply);
        subflowLink1.setToNode(subflowNodeEnd);
        subflow.addLink(subflowLink1);

        wfdFlowService.save(subflow.toWfdFlow());
        bpmManager.deploy(subflow.getId());

        LOGGER.info("subflow deployed");

        Flow flow = new Flow();
        flow.setId(TEST_A);
        flow.setCode(CODE_A);
        flow.setName("TEST A");

        Node nodeStart = FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        nodeStart.setEnableApiStart(1);
        flow.addNode(nodeStart);

        Node nodeApply = FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(nodeApply);

        Node nodeSubflow = FlowUtils.getNode(flow.getId() + "_subflow", "30", "subflow", NodeType.Subflow, FlowInMode.Any, FlowOutMode.Mode0);
        nodeSubflow.setSubFlowId(subflowId);
        flow.addNode(nodeSubflow);

        Node nodeEnd = FlowUtils.getNode(flow.getId() + "_end", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(nodeEnd);

        Link link0 = new Link();
        link0.setId(flow.getId() + "_LINK0");
        link0.setFromNode(nodeStart);
        link0.setToNode(nodeApply);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(flow.getId() + "_LINK1");
        link1.setFromNode(nodeApply);
        link1.setToNode(nodeSubflow);
        flow.addLink(link1);

        Link link2 = new Link();
        link2.setId(flow.getId() + "_LINK2");
        link2.setFromNode(nodeSubflow);
        link2.setToNode(nodeEnd);
        flow.addLink(link2);

        wfdFlowService.save(flow.toWfdFlow());
        LOGGER.info("flow deploying");
        bpmManager.deploy(flow.getId());
        LOGGER.info("flow deployed");

        FlowInstance flowInstance = bpmManager.createFlowInstanceById(flow.getId(), UUID.randomUUID().toString());
        LOGGER.info("flow instance id: " + flowInstance.getId());

        List<TaskInstance> tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
        LOGGER.info("tasks size: " + tasks.size());
        for (TaskInstance task :
                tasks) {
            LOGGER.info("*********************");
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getNodeName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
            bpmManager.deliverWork(task);
        }
        List<ProcessInstance> subProcessInstances = runtimeService.createProcessInstanceQuery().superProcessInstanceId(flowInstance.getId()).list();
        LOGGER.info("subProcessInstances size: " + subProcessInstances.size());
        for (ProcessInstance subProcessInstance :
                subProcessInstances) {
            LOGGER.info("*********************");
            LOGGER.info("id: " + subProcessInstance.getId());
        }
        tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
        LOGGER.info("tasks size: " + tasks.size());
        for (TaskInstance task :
                tasks) {
            LOGGER.info("*********************");
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getNodeName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
        }
    }
}