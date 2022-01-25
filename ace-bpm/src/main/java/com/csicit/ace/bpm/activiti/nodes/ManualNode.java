package com.csicit.ace.bpm.activiti.nodes;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.activiti.utils.BpmnUtil;
import com.csicit.ace.bpm.enums.AllowPassMode;
import com.csicit.ace.bpm.enums.FlowInMode;
import com.csicit.ace.bpm.enums.FlowOutMode;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;

/**
 * 人工节点
 *
 * @author JonnyJiang
 * @date 2019/8/26 14:34
 */
public class ManualNode extends AbstractNode {
    public static final String ASSIGNEE_LIST = "assigneeList";
    public static final String ASSIGNEE = "assignee";
    /**
     * 来自以下文件
     * activiti-engine\src\main\java\org\activiti\engine\impl\bpmn\behavior\ParallelMultiInstanceBehavior.java
     */
    public static final String NUMBER_OF_INSTANCES = "nrOfInstances";
    /**
     * 来自以下文件
     * activiti-engine\src\main\java\org\activiti\engine\impl\bpmn\behavior\ParallelMultiInstanceBehavior.java
     */
    public static final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
    /**
     * 来自以下文件
     * activiti-engine\src\main\java\org\activiti\engine\impl\bpmn\behavior\ParallelMultiInstanceBehavior.java
     */
    public static final String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";

    public ManualNode(BpmnModel bpmnModel, Process process, Node nodeManual) {
        super(bpmnModel, process, nodeManual);
    }

    @Override
    public void generateFlowElements() {
        UserTask userTask = new UserTask();
        userTask.setId(generateId());
        userTask.setName(node.getId());
        BpmnUtil.appendTaskListener(userTask);
        analysisAllowPassMode(userTask);
        process.addFlowElement(userTask);
        analysisFlowInMode(userTask);
        analysisFlowOutMode(userTask);
    }

    private void analysisAllowPassMode(UserTask userTask) {
        userTask.setAssignee("${" + ASSIGNEE + "}");
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        multiInstanceLoopCharacteristics.setSequential(false);
        multiInstanceLoopCharacteristics.setInputDataItem(ASSIGNEE_LIST);
        multiInstanceLoopCharacteristics.setElementVariable(ASSIGNEE);
        if (AllowPassMode.None.isEquals(super.node.getAllowPassMode())) {

        } else if (AllowPassMode.AllSign.isEquals(super.node.getAllowPassMode())) {
            multiInstanceLoopCharacteristics.setCompletionCondition("${" + NUMBER_OF_COMPLETED_INSTANCES + "==" + NUMBER_OF_INSTANCES + "}");
        } else if (AllowPassMode.SpecifiedUserAllSign.isEquals(super.node.getAllowPassMode())) {
            if (super.node.getWaitForPassUsers().size() == 0) {
                throw new BpmException(LocaleUtils.getNodeWaitForPassUsersIsNull(node.getId(), node.getName(), node.getCode()));
            } else {
//                multiInstanceLoopCharacteristics.setCompletionCondition("${pass==true}");
            }
        } else if (AllowPassMode.ExceedCount.isEquals(super.node.getAllowPassMode())) {
            if (super.node.getWaitForPassCount() == null) {
                throw new BpmException(LocaleUtils.getNodeWaitForPassCountIsNull(node.getId(), node.getName(), node.getCode()));
            } else {
                multiInstanceLoopCharacteristics.setCompletionCondition("${" + NUMBER_OF_COMPLETED_INSTANCES + ">=" + super.node.getWaitForPassCount() + "}");
            }
        } else if (AllowPassMode.ExceedPercent.isEquals(super.node.getAllowPassMode())) {
            if (super.node.getWaitForPassPercent() == null) {
                throw new BpmException(LocaleUtils.getNodeWaitForPassPercentIsNull(node.getId(), node.getName(), node.getCode()));
            } else {
                multiInstanceLoopCharacteristics.setCompletionCondition("${" + NUMBER_OF_COMPLETED_INSTANCES + "/" + NUMBER_OF_INSTANCES + ">" + super.node.getWaitForPassPercent() + "}");
            }
        }
        userTask.setLoopCharacteristics(multiInstanceLoopCharacteristics);
    }

    private void analysisFlowInMode(UserTask userTask) {
        if (FlowInMode.All.isEquals(super.node.getFlowInMode())) {
            // 等待所有流入分支路径抵达激活步骤实例
            InclusiveGateway inclusiveGateway = new InclusiveGateway();
            inclusiveGateway.setId(generateId());
            inclusiveGateway.setName(FlowInMode.All.name());
            process.addFlowElement(inclusiveGateway);
            addSequenceFlow(process, inclusiveGateway, userTask);
            addStartFlowElement(inclusiveGateway);
//        } else if (FlowInMode.Any.isEquals(super.node.getFlowInMode())) {
//            // 有任一分支抵达就激活次步骤实例
//            List<SequenceFlow> outgoingFlows = new ArrayList<>();
//            ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
//            exclusiveGateway.setId(generateId());
//            exclusiveGateway.setName(FlowInMode.Any.name());
//            process.addFlowElement(exclusiveGateway);
//            addStartFlowElement(exclusiveGateway);
//            SequenceFlow sequenceFlow = addSequenceFlow(process, exclusiveGateway, userTask);
//            outgoingFlows.add(sequenceFlow);
//            String conditionExpression = "${" + TaskVariableName.FIRST_ARRIVING_NODE_IDS.getName() + ".contains(\"" + node.getId() + "\")}";
//            sequenceFlow.setConditionExpression(conditionExpression);
//            EndEvent endEvent = new EndEvent();
//            endEvent.setId(generateId());
//            endEvent.setName("end");
//            process.addFlowElement(endEvent);
//            SequenceFlow sequenceFlowEnd = addSequenceFlow(process, exclusiveGateway, endEvent);
//            outgoingFlows.add(sequenceFlowEnd);
//            exclusiveGateway.setDefaultFlow(sequenceFlowEnd.getId());
//            exclusiveGateway.setOutgoingFlows(outgoingFlows);
        } else if (FlowInMode.Every.isEquals(super.node.getFlowInMode())) {
            // 每一分支抵达都重新激活此步骤的新实例
            addStartFlowElement(userTask);
        } else {
            throw new BpmException(LocaleUtils.getUnsupportedFlowInMode(super.node.getFlowInMode()));
        }
    }

    private void analysisFlowOutMode(UserTask userTask) {
        addEndFlowElement(userTask);
//        if (FlowOutMode.All.isEquals(super.node.getFlowOutMode())) {
//            if (super.node.getFlowOutLinks().size() > 1) {
//                addEndInclusiveGateWay(userTask, FlowOutMode.All);
//            } else {
//                addEndFlowElement(userTask);
//            }
//        } else if (FlowOutMode.Any.isEquals(super.node.getFlowOutMode())) {
//            addEndInclusiveGateWay(userTask, FlowOutMode.Any);
//        } else if (FlowOutMode.Every.isEquals(super.node.getFlowOutMode())) {
//            if (super.node.getFlowOutLinks().size() > 1) {
//                addEndInclusiveGateWay(userTask, FlowOutMode.Every);
//            } else {
//                addEndFlowElement(userTask);
//            }
//        } else {
//            throw new BpmException(LocaleUtils.getUnsupportedFlowOutMode(super.node.getFlowOutMode()));
//        }
    }

    @Override
    public Boolean getAllowDrawBack() {
        return IntegerUtils.isTrue(super.node.getAllowDrawBack());
    }

    private void addEndInclusiveGateWay(UserTask userTask, FlowOutMode flowOutMode) {
        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(generateId());
        inclusiveGateway.setName(flowOutMode.name());
        process.addFlowElement(inclusiveGateway);
        addSequenceFlow(process, userTask, inclusiveGateway);
        addEndFlowElement(inclusiveGateway);
    }
}