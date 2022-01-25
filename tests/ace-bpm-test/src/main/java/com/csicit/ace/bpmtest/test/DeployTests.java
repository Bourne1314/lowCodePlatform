package com.csicit.ace.bpmtest.test;

import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.enums.ConditionMode;
import com.csicit.ace.bpm.enums.FlowInMode;
import com.csicit.ace.bpm.enums.FlowOutMode;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2019/9/6 16:03
 */
@Component
public class DeployTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployTests.class);
    public static final String TEST_A = "DeployTests_TEST_A";
    public static final String TEST_B = "DeployTests_TEST_B";
    public static final String TEST_C = "DeployTests_TEST_C";
    public static final String TEST_D = "DeployTests_TEST_D";
    public static final String TEST_E = "DeployTests_TEST_E";
    public static final String TEST_F = "DeployTests_TEST_F";
    public static final String TEST_G = "DeployTests_TEST_G";
    public static final String TEST_H = "DeployTests_TEST_H";
    public static final String TEST_I = "DeployTests_TEST_I";
    public static final String TEST_J = "DeployTests_TEST_J";

    public static final String CODE_A = "DeployTests_CODE_A";
    public static final String CODE_B = "DeployTests_CODE_B";
    public static final String CODE_C = "DeployTests_CODE_C";
    public static final String CODE_D = "DeployTests_CODE_D";
    public static final String CODE_E = "DeployTests_CODE_E";
    public static final String CODE_F = "DeployTests_CODE_F";
    public static final String CODE_G = "DeployTests_CODE_G";
    public static final String CODE_H = "DeployTests_CODE_H";
    public static final String CODE_I = "DeployTests_CODE_I";
    public static final String CODE_J = "DeployTests_CODE_J";
    @Autowired
    private BpmManager bpmManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private WfdFlowService wfdFlowService;

    /**
     * startEvent-userTask-userTask-endEvent
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:46
     */

    public void testA() {
        wfdFlowService.removeById(TEST_A);
        Flow flow = new Flow();
        flow.setId(TEST_A);
        flow.setCode(flow.getId());
        flow.setName("test a");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_Begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_Apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_Audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_End", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        link0.setFlow(flow);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setFlow(flow);
        flow.addLink(link1);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeEnd);
        link2.setFlow(flow);
        flow.addLink(link2);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * startEvent-userTask-[userTask,userTask]-endEvent
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    public void testB() {
        wfdFlowService.removeById(TEST_B);
        Flow flow = new Flow();
        flow.setId(TEST_B);
        flow.setCode(flow.getId());
        flow.setName("test b");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_end", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setSortIndex(0);
        flow.addLink(link1);

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setSortIndex(1);
        flow.addLink(link3);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeEnd);
        flow.addLink(link2);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeEnd);
        flow.addLink(link4);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * startEvent-userTask-[userTask,userTask]-endEvent
     * 流入模式为0
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    public void testC() {
        deployTestC(wfdFlowService, bpmManager);
    }

    public void deployTestC(WfdFlowService wfdFlowService, BpmManager bpmManager) {
        wfdFlowService.removeById(TEST_C);
        Flow flow = new Flow();
        flow.setId(TEST_C);
        flow.setCode(CODE_C);
        flow.setName("test c");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_end", "40", "end", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setSortIndex(0);
        flow.addLink(link1);

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setSortIndex(1);
        flow.addLink(link3);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeEnd);
        flow.addLink(link2);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeEnd);
        flow.addLink(link4);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * startEvent-userTask-[userTask,userTask]-endEvent
     * 带转出条件
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    public void testD() {
        wfdFlowService.removeById(TEST_D);
        Flow flow = new Flow();
        flow.setId(TEST_D);
        flow.setCode(flow.getId());
        flow.setName("test d");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_end", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setSortIndex(0);
        flow.addLink(link1);

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setSortIndex(1);
        flow.addLink(link3);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeEnd);
        flow.addLink(link2);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeEnd);
        flow.addLink(link4);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * startEvent-userTask-[userTask,userTask]-userTask(flowInMode0)-endEvent
     * 流入模式为0
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    public void testE() {
        wfdFlowService.removeById(TEST_E);
        Flow flow = new Flow();
        flow.setId(TEST_E);
        flow.setCode(flow.getId());
        flow.setName("test e");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeManager =FlowUtils.getNode(flow.getId() + "_manager", "40", "manager", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeManager);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_end", "50", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setSortIndex(0);
        flow.addLink(link1);

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setSortIndex(1);
        flow.addLink(link3);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeManager);
        flow.addLink(link2);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeManager);
        flow.addLink(link4);

        Link link5 = new Link();
        link5.setId(UUID.randomUUID().toString());
        link5.setFromNode(wfdNodeManager);
        link5.setToNode(wfdNodeEnd);
        flow.addLink(link5);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * startEvent-userTask-[userTask,userTask]-userTask(flowInMode1)-endEvent
     * 流入模式为1
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/30 8:03
     */

    public void testF() {
        wfdFlowService.removeById(TEST_F);

        Flow flow = new Flow();
        flow.setId(TEST_F);
        flow.setCode(flow.getId());
        flow.setName("test F");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeManager =FlowUtils.getNode(flow.getId() + "_manager", "40", "manager", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeManager);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_end", "50", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setSortIndex(0);
        flow.addLink(link1);

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setSortIndex(1);
        flow.addLink(link3);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeManager);
        flow.addLink(link2);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeManager);
        flow.addLink(link4);

        Link link5 = new Link();
        link5.setId(UUID.randomUUID().toString());
        link5.setFromNode(wfdNodeManager);
        link5.setToNode(wfdNodeEnd);
        flow.addLink(link5);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * startEvent-userTask(flowOutMode0)-[userTask,userTask]-userTask(flowInMode1)-endEvent
     * 流出模式为0
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/30 8:03
     */

    public void testG() {
        wfdFlowService.removeById(TEST_G);
        String flowId = TEST_G;
        wfdFlowService.removeById(TEST_G);

        Flow flow = new Flow();
        flow.setId(flowId);
        flow.setCode(flow.getId());
        flow.setName("test G");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_money", "30", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeManager =FlowUtils.getNode(flow.getId() + "_manager", "40", "manager", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeManager);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_", "50", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        link0.setConditionMode(ConditionMode.None.getValue());
        flow.addLink(link0);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setSortIndex(0);
        link1.setConditionMode(ConditionMode.None.getValue());
        flow.addLink(link1);

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setSortIndex(1);
        link3.setConditionMode(ConditionMode.None.getValue());
        flow.addLink(link3);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeManager);
        link2.setConditionMode(ConditionMode.None.getValue());
        flow.addLink(link2);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeManager);
        link4.setConditionMode(ConditionMode.None.getValue());
        flow.addLink(link4);

        Link link5 = new Link();
        link5.setId(UUID.randomUUID().toString());

        link5.setFromNode(wfdNodeManager);
        link5.setToNode(wfdNodeEnd);
        link5.setConditionMode(ConditionMode.None.getValue());
        flow.addLink(link5);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * 测试撤回
     * startEvent-userTask(flowOutMode0)-[userTask,userTask(允许撤回)]-userTask(flowInMode1)-endEvent
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/9/5 10:49
     */

    public void testH() {
        wfdFlowService.removeById(TEST_H);
        String flowId = TEST_H;
        String startId = TEST_H + "_Start";
        String applyId = TEST_H + "_Apply";
        String auditId = TEST_H + "_Audit";
        String moneyId = TEST_H + "_Money";
        String managerId = TEST_H + "_Manager";
        String endId = TEST_H + "_end";
        wfdFlowService.removeById(TEST_H);

        Flow flow = new Flow();
        flow.setId(flowId);
        flow.setCode(flow.getId());
        flow.setName("test H");

        Node wfdNodeStart =FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        flow.addNode(wfdNodeStart);

        Node wfdNodeApply =FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit =FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeMoney =FlowUtils.getNode(flow.getId() + "_money", "31", "money", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        // 设置允许撤回
        wfdNodeMoney.setAllowDrawBack(1);
        flow.addNode(wfdNodeMoney);

        Node wfdNodeManager =FlowUtils.getNode(flow.getId() + "_manager", "40", "manager", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeManager);

        Node wfdNodeEnd =FlowUtils.getNode(flow.getId() + "_end", "50", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        link0.setFlow(flow);
        link0.setConditionMode(ConditionMode.None.getValue());

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setFlow(flow);
        link1.setSortIndex(0);
        link1.setConditionMode(ConditionMode.None.getValue());

        Link link3 = new Link();
        link3.setId(UUID.randomUUID().toString());
        link3.setFromNode(wfdNodeApply);
        link3.setToNode(wfdNodeMoney);
        link3.setFlow(flow);
        link3.setSortIndex(1);
        link3.setConditionMode(ConditionMode.None.getValue());

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeManager);
        link2.setFlow(flow);
        link2.setConditionMode(ConditionMode.None.getValue());

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeMoney);
        link4.setToNode(wfdNodeManager);
        link4.setFlow(flow);
        link4.setConditionMode(ConditionMode.None.getValue());

        Link link5 = new Link();
        link5.setId(UUID.randomUUID().toString());

        link5.setFromNode(wfdNodeManager);
        link5.setToNode(wfdNodeEnd);
        link5.setFlow(flow);
        link5.setConditionMode(ConditionMode.None.getValue());

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * 测试三种开始事件
     */
    public void testI() {
        wfdFlowService.removeById(TEST_I);
        Flow flow = new Flow();
        flow.setId(TEST_I);
        flow.setCode(flow.getId());
        flow.setName("test i");

        Node wfdNodeStart = FlowUtils.getNode(flow.getId() + "_begin", "10", "begin", NodeType.Start);
        wfdNodeStart.setEnableApiStart(1);
        flow.addNode(wfdNodeStart);

        Node wfdNodeStartTimer = FlowUtils.getNode(flow.getId() + "_beginByTimer", "11", "begin by timer", NodeType.Start);
        wfdNodeStartTimer.setEnableTimerStart(1);
        wfdNodeStartTimer.setStartTimerCron("* * 10 ? 1,2,3,4,5");
        flow.addNode(wfdNodeStartTimer);

        Node wfdNodeStartMessage = FlowUtils.getNode(flow.getId() + "_beginByMessage", "12", "begin by message", NodeType.Start);
        wfdNodeStartMessage.setEnableMessageStart(1);
        wfdNodeStartMessage.setMessageName("message");
        wfdNodeStartMessage.setMessageProcessingClass("com.csicit.ace.messageListener");
        flow.addNode(wfdNodeStartMessage);

        Node wfdNodeApply = FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit = FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeEnd = FlowUtils.getNode(flow.getId() + "_end", "40", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStart);
        link0.setToNode(wfdNodeApply);
        link0.setFlow(flow);
        flow.addLink(link0);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeStartTimer);
        link4.setToNode(wfdNodeApply);
        link4.setFlow(flow);
        flow.addLink(link4);

        Link link5 = new Link();
        link5.setId(UUID.randomUUID().toString());
        link5.setFromNode(wfdNodeStartMessage);
        link5.setToNode(wfdNodeApply);
        link5.setFlow(flow);
        flow.addLink(link5);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setFlow(flow);
        flow.addLink(link1);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeAudit);
        link2.setToNode(wfdNodeEnd);
        link2.setFlow(flow);
        flow.addLink(link2);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }

    /**
     * 测试多个消息事件触发不同节点
     */
    public void testJ() {
        wfdFlowService.removeById(TEST_I);
        Flow flow = new Flow();
        flow.setId(TEST_I);
        flow.setCode(flow.getId());
        flow.setName("test j");

        Node wfdNodeStartMessage0 = FlowUtils.getNode(flow.getId() + "_message0", "10", "message0", NodeType.Start);
        wfdNodeStartMessage0.setEnableMessageStart(1);
        wfdNodeStartMessage0.setMessageName("message0");
        wfdNodeStartMessage0.setMessageProcessingClass("com.csicit.ace.messageListener");
        flow.addNode(wfdNodeStartMessage0);

        Node wfdNodeStartMessage1 = FlowUtils.getNode(flow.getId() + "_message1", "11", "message1", NodeType.Start);
        wfdNodeStartMessage1.setEnableMessageStart(1);
        wfdNodeStartMessage1.setMessageName("message1");
        wfdNodeStartMessage1.setMessageProcessingClass("com.csicit.ace.messageListener");
        flow.addNode(wfdNodeStartMessage1);

        Node wfdNodeStartMessage2 = FlowUtils.getNode(flow.getId() + "_message2", "12", "message2", NodeType.Start);
        wfdNodeStartMessage2.setEnableMessageStart(1);
        wfdNodeStartMessage2.setMessageName("message2");
        wfdNodeStartMessage2.setMessageProcessingClass("com.csicit.ace.messageListener");
        flow.addNode(wfdNodeStartMessage2);

        Node wfdNodeApply = FlowUtils.getNode(flow.getId() + "_apply", "20", "apply", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeApply);

        Node wfdNodeAudit = FlowUtils.getNode(flow.getId() + "_audit", "30", "audit", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeAudit);

        Node wfdNodeManager = FlowUtils.getNode(flow.getId() + "_manager", "40", "manager", NodeType.Manual, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeManager);

        Node wfdNodeEnd = FlowUtils.getNode(flow.getId() + "_end", "50", "end", NodeType.End, FlowInMode.Any, FlowOutMode.Mode0);
        flow.addNode(wfdNodeEnd);

        Link link0 = new Link();
        link0.setId(UUID.randomUUID().toString());
        link0.setFromNode(wfdNodeStartMessage0);
        link0.setToNode(wfdNodeApply);
        link0.setFlow(flow);
        flow.addLink(link0);

        Link link4 = new Link();
        link4.setId(UUID.randomUUID().toString());
        link4.setFromNode(wfdNodeStartMessage1);
        link4.setToNode(wfdNodeAudit);
        link4.setFlow(flow);
        flow.addLink(link4);

        Link link5 = new Link();
        link5.setId(UUID.randomUUID().toString());
        link5.setFromNode(wfdNodeStartMessage2);
        link5.setToNode(wfdNodeManager);
        link5.setFlow(flow);
        flow.addLink(link5);

        Link link6 = new Link();
        link6.setId(UUID.randomUUID().toString());
        link6.setFromNode(wfdNodeAudit);
        link6.setToNode(wfdNodeManager);
        link6.setFlow(flow);
        flow.addLink(link6);

        Link link1 = new Link();
        link1.setId(UUID.randomUUID().toString());
        link1.setFromNode(wfdNodeApply);
        link1.setToNode(wfdNodeAudit);
        link1.setFlow(flow);
        flow.addLink(link1);

        Link link2 = new Link();
        link2.setId(UUID.randomUUID().toString());
        link2.setFromNode(wfdNodeManager);
        link2.setToNode(wfdNodeEnd);
        link2.setFlow(flow);
        flow.addLink(link2);

        wfdFlowService.save(flow.toWfdFlow());

        bpmManager.deploy(flow.getId());
    }
}
