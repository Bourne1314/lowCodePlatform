package com.csicit.ace.bpm.test;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.activiti.impl.NodeInfoImpl;
import com.csicit.ace.bpm.enums.FlowInMode;
import com.csicit.ace.bpm.enums.FlowOutMode;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.pojo.vo.*;
import com.csicit.ace.bpm.pojo.vo.process.ActivityVO;
import com.csicit.ace.bpm.pojo.vo.process.FlowVO;
import com.csicit.ace.bpm.pojo.vo.process.LinkVO;
import com.csicit.ace.bpm.pojo.vo.process.NodeVO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfd.NodeEvent;
import com.csicit.ace.bpm.utils.FlowUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2019/9/3 16:02
 */
public class FlowTest {
    @Autowired
    RepositoryService repositoryService;
    private Flow getFlow() {
        Flow flow = new Flow();
        flow.setId(UUID.randomUUID().toString());
        flow.setName("name123");
        flow.setCode("code123");
        flow.setSortNo(10);
        flow.setDescription("描述");
        flow.setEnableSetUrgency(1);
        flow.setFormUrl("xxx/views/yyy");
        flow.setFormDatasourceId("xxxxxxx");
        flow.setFormDataTable("sys_app");
        flow.setModel("model");
        flow.setFlowChart("flowChart");

        System.out.println("==================flow initing===============");

        Node startNode = FlowUtils.getNode(flow.getId() + "_start", "10", "start", NodeType.Start);
        flow.addNode(startNode);
        Node manualNode = FlowUtils.getNode(flow.getId() + "_manual", "20", "manual", NodeType.Manual, FlowInMode.All, FlowOutMode.Mode0);
        manualNode.setAllowPassToAgent(1);
        manualNode.setWorkResultPrompt("请选择办理结果");
        manualNode.setWorkResultOptions(new String[]{"同意", "不同意"});
        manualNode.setMinSelResult(1);
        manualNode.setMaxSelResult(2);
        System.out.println("================manualNode initing===============");
        flow.addNode(manualNode);
        NodeEvent nodeEvent = new NodeEvent();
        nodeEvent.setId(UUID.randomUUID().toString());
        nodeEvent.setSortIndex(0);
        manualNode.addEvent(nodeEvent);
        Node endNode = FlowUtils.getNode(flow.getId() + "_end", "30", "end", NodeType.End, FlowInMode.All, FlowOutMode.Mode0);
        System.out.println("==================endNode initing================");
        flow.addNode(endNode);
        Link link = new Link();
        link.setId(UUID.randomUUID().toString());
        link.setSortIndex(1);
        link.setFromNode(startNode);
        link.setToNode(manualNode);
        flow.addLink(link);
        System.out.println("================flow initing===============");
        return flow;
    }

    private String getFlowJson() {
        String json = JSONObject.toJSONString(getFlow());
        System.out.println(json);
        return json;
    }

    @Test
    public void testSerialize() {
        getFlowJson();
    }


//    @Test
//    public void urgeTask(){
//        String processDefId = "process:1:7";
//        //当前流程节点Id,在任务表里面对应TASK_DEF_KEY_字段，可以根据taskId获取该字段数据
//        String flowElemetId = "leader";
//        BpmnModel bpmnModel = repositoryService.getBpmnModel("51cea0cb-72ea-4d01-b1ae-3193bad64260");
//        Process process = bpmnModel.getProcesses().get(0);
//        //获取所有普通任务节点
//        List<UserTask> UserTaskList = process.findFlowElementsOfType(UserTask.class);
//        for (UserTask userTask : UserTaskList) {
//            //获取当前任务节点Id
//            String id = userTask.getId();
//
//        }
//    }
    @Test
    public void testDeserialize() {
        String str = getFlowJson();
        Flow flow = FlowUtils.getFlow(str);
        System.out.println("=========================");
        System.out.println(flow);
        System.out.println("=========================");
        System.out.println(flow.toJson());
    }

    @Test
    public void testDeliver() {
        DeliverInfo deliverInfo = new DeliverInfo();
        deliverInfo.setTaskId(UUID.randomUUID().toString());
        deliverInfo.setOpinion("同意");
        DeliverNode deliverNode = new DeliverNode();
        deliverNode.setNodeId(UUID.randomUUID().toString());
        DeliverUser deliverUser = new DeliverUser();
        deliverUser.setUserId(UUID.randomUUID().toString());
        deliverUser.setRealName("张三");
        deliverUser.setUserType(0);
        deliverNode.addDeliverUser(deliverUser);
        deliverInfo.addDeliverNode(deliverNode);
        deliverInfo.setWorkResultOptions(new String[]{"A", "B"});
        System.out.println(JSONObject.toJSONString(deliverInfo));
//
//        public String toJson() {
//            return JSONObject.toJSONString(this);
//        }

    }

    private void getTaskMine(){
        TaskMineVO taskMineVO = new TaskMineVO();

    }

    private String getNodeInfoJson() {
        Flow flow = getFlow();
        Node node = FlowUtils.getFirstManualNode(flow);
        String json = JSONObject.toJSONString(new NodeInfoImpl(node));
        System.out.println(json);
        return json;
    }


    @Test
    public void testNodeInfoSerialize() {
        System.out.println(getNodeInfoJson());
    }

    @Test
    public void testActivityVo() {
        ActivityVO activity = new ActivityVO();
        activity.setNodeId("节点id");
        activity.setTaskId("任务id");
        activity.setSn(1);
        activity.setStartTime(new Date());
        activity.setEndTime(new Date());
        activity.addRejectToNodeId("驳回到的节点id");
        activity.addRejectToNodeName("驳回到的节点名称");
        System.out.println(JSONObject.toJSONString(activity));
    }

    @Test
    public void testFlowVO() {
        FlowVO flow = new FlowVO();
        flow.setId("流程id");
        flow.setFlowInstanceId("流程实例id");
        flow.setCode("流程标识");
        flow.setName("流程名称");
        flow.setResult("流程结果");
        flow.setEndTime(new Date());
        flow.setCompleted(1);

        for (int i = 0; i < 2; i++) {
            NodeVO node = new NodeVO();
            node.setId("节点id");
            node.setFlowId("流程id");
            node.setName("节点名称");
            node.setCompleted(1);
            node.setCode("节点标识");
            node.setNodeType("manual");
            node.setEndTime(LocalDateTime.now());
            node.setRelevant(1);
            node.setRejectFromNodeId("驳回来自节点id");
            node.addRejectToNodeId("驳回到节点id");
            for (int j = 0; j < 2; j++) {
                LinkVO link = new LinkVO();
                link.setId(String.valueOf(j));
                link.setRelevant(1);
                node.addFlowOutLinks(link);
            }

            flow.addNode(node);
        }

        System.out.println(JSONObject.toJSONString(flow));
    }


    @Test
    public void testRejectInfo() {
        RejectInfo rejectInfo = new RejectInfo();
        rejectInfo.setTaskId("任务id");
        rejectInfo.setRejectReason("驳回原因");
        List<TaskRejectTo> taskRejectTos = new ArrayList<>();
        TaskRejectTo taskRejectTo = new TaskRejectTo();
        taskRejectTo.setTaskId("任务id");
        taskRejectTo.setNodeName("任务名称");
        taskRejectTos.add(taskRejectTo);
        rejectInfo.setTaskRejectTos(taskRejectTos);
        System.out.println(JSONObject.toJSONString(rejectInfo));
    }
//    @Test
//    public void TestShowInfo(){
//        Flow flow = new FlowVO;
//
//    }
}
