package com.csicit.ace.bpmtest.test;

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.enums.FlowInMode;
import com.csicit.ace.bpm.enums.FlowOutMode;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.service.WfdVFlowService;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.service.WfiVFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2019/9/10 20:56
 */
@Component
public class CustomTests {
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

    private static final String TEST_A = "CUSTOM_TESTS_A";
    private static final String TEST_B = "CUSTOM_TESTS_B";
    private static final String TEST_C = "CUSTOM_TESTS_C";

    private static final String CODE_A = "CUSTOM_TESTS_CODE_A";
    private static final String CODE_B = "CUSTOM_TESTS_CODE_B";
    private static final String CODE_C = "CUSTOM_TESTS_CODE_C";

    /**
     * 测试条件分支是否支持多个分支同时满足条件
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/9/10 20:58
     */

    public void testA() {
        wfdFlowService.removeById(TEST_A);
        Flow flow = new Flow();
        flow.setId(TEST_A);
        flow.setCode(CODE_A);
        flow.setName("testA");

        Node startNode = FlowUtils.getNode(TEST_A + "_start", "10", "start", NodeType.Start);
        flow.addNode(startNode);

        Node applyNode = FlowUtils.getNode(TEST_A + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(applyNode);

        Node auditNode0 = FlowUtils.getNode(TEST_A + "_audit0", "30", "audit0", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(auditNode0);

        Node auditNode1 = FlowUtils.getNode(TEST_A + "_audit1", "31", "audit1", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(auditNode1);

        Node auditNode2 = FlowUtils.getNode(TEST_A + "_audit2", "32", "audit2", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(auditNode2);

        Node auditNode3 = FlowUtils.getNode(TEST_A + "_audit3", "33", "audit3", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(auditNode3);

        Node endNode = FlowUtils.getNode(TEST_A + "_end", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(endNode);

        Link link0 = new Link();
        link0.setId(TEST_A + "link0");
        link0.setFromNode(startNode);
        link0.setToNode(applyNode);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(TEST_A + "link1");
        link1.setFromNode(applyNode);
        link1.setToNode(auditNode0);
        flow.addLink(link1);

        Link link2 = new Link();
        link2.setId(TEST_A + "link2");
        link2.setFromNode(applyNode);
        link2.setToNode(auditNode1);
        flow.addLink(link2);

        Link link3 = new Link();
        link3.setId(TEST_A + "link3");
        link3.setFromNode(applyNode);
        link3.setToNode(auditNode2);
        flow.addLink(link3);

        Link link4 = new Link();
        link4.setId(TEST_A + "link4");
        link4.setFromNode(applyNode);
        link4.setToNode(auditNode3);
        flow.addLink(link4);

        Link link5 = new Link();
        link5.setId(TEST_A + "link5");
        link5.setFromNode(auditNode0);
        link5.setToNode(endNode);
        flow.addLink(link5);

        Link link6 = new Link();
        link6.setId(TEST_A + "link6");
        link6.setFromNode(auditNode1);
        link6.setToNode(endNode);
        flow.addLink(link6);

        Link link7 = new Link();
        link7.setId(TEST_A + "link7");
        link7.setFromNode(auditNode2);
        link7.setToNode(endNode);
        flow.addLink(link7);

        Link link8 = new Link();
        link8.setId(TEST_A + "link8");
        link8.setFromNode(auditNode3);
        link8.setToNode(endNode);
        flow.addLink(link8);

        WfdFlowDO wfdFlow = flow.toWfdFlow();
        LOGGER.info("flow saving");
        wfdFlowService.save(wfdFlow);
        LOGGER.info("flow saved");
        LOGGER.info("flow deploying");
        bpmManager.deploy(wfdFlow.getId());
        LOGGER.info("flow deployed");
        LOGGER.info("flow instance creating");
        FlowInstance flowInstance = bpmManager.createFlowInstanceById(TEST_A, UUID.randomUUID().toString());
        LOGGER.info("flow instance created: " + flowInstance.getId());
        List<TaskInstance> tasks = listTasks(flowInstance.getId());
        TaskInstance taskApply = tasks.get(0);
        List<String> nodeIds = new ArrayList<>();
        nodeIds.add(link1.getToNodeId());
        nodeIds.add(link3.getToNodeId());
        LOGGER.info("work delivering: " + taskApply.getNodeName());
//        bpmManager.deliverWork(taskApply, nodeIds);
        LOGGER.info("work delivered: " + taskApply.getNodeName());
        listTasks(flowInstance.getId());
    }

    private List<TaskInstance> listTasks(String flowInstanceId) {
        List<TaskInstance> tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstanceId);
        LOGGER.info("tasks count: " + tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            LOGGER.info("task " + i + ": " + tasks.get(i).getNodeName());
        }
        return tasks;
    }

    public void testB() {
        wfdFlowService.removeById(TEST_B);
        Flow flow = new Flow();
        flow.setId(TEST_B);
        flow.setCode(CODE_B);
        flow.setName("testB");

        Node startNode = FlowUtils.getNode(TEST_B + "start", "10", "start", NodeType.Start);
        flow.addNode(startNode);

        Node applyNode = FlowUtils.getNode(TEST_B + "apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(applyNode);

        Node auditNode = FlowUtils.getNode(TEST_B + "audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(auditNode);

        Node endNode = FlowUtils.getNode(TEST_B + "end", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(endNode);

        Link link0 = new Link();
        link0.setId(TEST_B + "link0");
        link0.setFromNode(startNode);
        link0.setToNode(applyNode);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(TEST_B + "link1");
        link1.setFromNode(applyNode);
        link1.setToNode(auditNode);
        flow.addLink(link1);

        Link link2 = new Link();
        link2.setId(TEST_B + "link2");
        link2.setFromNode(auditNode);
        link2.setToNode(endNode);
        flow.addLink(link2);
        wfdFlowService.save(flow.toWfdFlow());
        bpmManager.deploy(flow.getId());

        FlowInstance flowInstance = bpmManager.createFlowInstanceById(TEST_B, UUID.randomUUID().toString());
        printHistoricTaskInstances(flowInstance.getId());
        List<TaskInstance> tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
        for (TaskInstance task :
                tasks) {
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getNodeName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
            bpmManager.delegateWork(task.getId(), "Jonny");
        }
        printHistoricTaskInstances(flowInstance.getId());
        tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
        for (TaskInstance task :
                tasks) {
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getNodeName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
            LOGGER.info("setVariableLocal");
            taskService.setVariableLocal(task.getId(), "TASK_HOST", task.getAssignee());
            Map<String, Object> localVariables = taskService.getVariablesLocal(task.getId());
            localVariables.forEach((key, value) -> {
                LOGGER.info("key: " + key + " value: " + value);
            });
            LOGGER.info("resolveTask");
            taskService.resolveTask(task.getId());
            LOGGER.info("complete");
            taskService.complete(task.getId());
        }
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(flowInstance.getId()).list();
        for (HistoricTaskInstance task :
                historicTaskInstances) {
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
            List<HistoricVariableInstance> xxx = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
            xxx.forEach(o -> {
                LOGGER.info("key: " + o.getVariableName() + " value: " + o.getValue());
            });
        }
        tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
        for (TaskInstance task :
                tasks) {
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getNodeName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
            LOGGER.info("claim");
            taskService.claim(task.getId(), "Jack");
        }
        printHistoricTaskInstances(flowInstance.getId());
    }

    private void printHistoricTaskInstances(String flowInstanceId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(flowInstanceId).list();
        for (HistoricTaskInstance task :
                historicTaskInstances) {
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
        }
    }

//    public void testC() {
//        wfdFlowService.removeById(TEST_C);
//        Flow flow = new Flow();
//        flow.setId(TEST_C);
//        flow.setCode(CODE_C);
//        flow.setName("testC");
//
//        Node startNode = FlowUtils.getNode(TEST_C + "start", "10", "start", NodeType.Start);
//        flow.addNode(startNode);
//
//        Node applyNode = FlowUtils.getNode(TEST_C + "apply", "20", "apply", NodeType.Manual, FlowInMode.None, FlowOutMode.None);
//        flow.addNode(applyNode);
//
//        Node auditNode = FlowUtils.getNode(TEST_C + "audit", "30", "audit", NodeType.Manual, FlowInMode.None, FlowOutMode.None);
//        flow.addNode(auditNode);
//
//        Node endNode = FlowUtils.getNode(TEST_C + "end", "40", "end", NodeType.End, FlowInMode.None, FlowOutMode.None);
//        flow.addNode(endNode);
//
//        Link link0 = new Link();
//        link0.setId(TEST_C + "link0");
//        link0.setFromNode(startNode);
//        link0.setToNode(applyNode);
//        flow.addLink(link0);
//
//        Link link1 = new Link();
//        link1.setId(TEST_C + "link1");
//        link1.setFromNode(applyNode);
//        link1.setToNode(auditNode);
//        flow.addLink(link1);
//
//        Link link2 = new Link();
//        link2.setId(TEST_C + "link2");
//        link2.setFromNode(auditNode);
//        link2.setToNode(endNode);
//        flow.addLink(link2);
//        wfdFlowService.save(flow.toWfdFlow());
//        bpmManager.deploy(flow.getId());
//        LOGGER.info("flow deployed");
//        FlowInstance flowInstance = bpmManager.createFlowInstanceById(TEST_C, UUID.randomUUID().toString());
//        LOGGER.info("flow instance created");
//
//        flow = FlowUtils.getFlow(wfdFlowService.getById(flow.getId()).getModel());
//        Node auditNode1 = FlowUtils.getNode(TEST_C + "audit_1", "31", "audit_1", NodeType.Manual, FlowInMode.None, FlowOutMode.None);
//        flow.addNode(auditNode1);
//
//        Link link3 = new Link();
//        link3.setId(TEST_C + "link3");
//        link3.setFromNode(applyNode);
//        link3.setToNode(auditNode1);
//        flow.addLink(link3);
//
//        Link link4 = new Link();
//        link4.setId(TEST_C + "link4");
//        link4.setFromNode(auditNode1);
//        link4.setToNode(endNode);
//        flow.addLink(link4);
//
//        // 更新实例中的流程模型
//        wfiFlowService.updateModel(flowInstance.getId(), flow.getModel(), WfiVFlowService.FIRST_FLOW_VERSION);
//
//
//        List<TaskInstance> tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
//        for (TaskInstance task :
//                tasks) {
//            LOGGER.info("id: " + task.getId());
//            LOGGER.info("name: " + task.getNodeName());
//            LOGGER.info("assignee: " + task.getAssignee());
//            LOGGER.info("owner: " + task.getOwner());
//            bpmManager.deliverWork(task);
//        }
//        LOGGER.info("task delivered");
//        tasks = bpmAdapter.listTasksByFlowInstanceId(flowInstance.getId());
//        for (TaskInstance task :
//                tasks) {
//            LOGGER.info("id: " + task.getId());
//            LOGGER.info("name: " + task.getNodeName());
//            LOGGER.info("assignee: " + task.getAssignee());
//            LOGGER.info("owner: " + task.getOwner());
//        }
//
////        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowInstance.getId()).singleResult();
////        InputStream resourceAsStream = repositoryService.getResourceAsStream(processInstance.getDeploymentId(), flow.getCode() + ".bpmn");
////
////        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
////
////        XMLStreamReader xmlStreamReader = XMLStreamReaderFactory.create(UUID.randomUUID().toString(), resourceAsStream, false);
////
////        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(xmlStreamReader);
////
////        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
////
////        String bpmn = new String(convertToXML);
////
////        LOGGER.debug(bpmn);
//    }
}