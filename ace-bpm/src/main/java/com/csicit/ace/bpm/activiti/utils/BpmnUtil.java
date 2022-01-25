package com.csicit.ace.bpm.activiti.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.activiti.NodeEventType;
import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.listeners.*;
import com.csicit.ace.bpm.activiti.nodes.AbstractNode;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.DeliverNode;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.BaseExecutionListener;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * BPMN流程模型助手
 *
 * @author JonnyJiang
 * @date 2019/10/10 15:27
 */
public class BpmnUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BpmnUtil.class);

    private Flow flow;

    private BpmnUtil(Flow flow) {
        this.flow = flow;
    }

    private BpmnModel generate() {
        // 生成流程模型
        BpmnModel model = new BpmnModel();
        model.setTargetNamespace(flow.getId());
        Process process = new Process();
        process.setId(flow.getId());
        process.setName(flow.getCode());

        appendProcessExecutionListener(process);

        model.addProcess(process);

        List<AbstractNode> nodes = new ArrayList<>();
        // 创建关联关系
        for (Link link : flow.getLinks()) {
            AbstractNode sourceNode = getNode(nodes, link.getFromNode());
            if (sourceNode == null) {
                sourceNode = FlowUtils.getNode(model, process, link.getFromNode());
                nodes.add(sourceNode);
            }
            AbstractNode targetNode = getNode(nodes, link.getToNode());
            if (targetNode == null) {
                targetNode = FlowUtils.getNode(model, process, link.getToNode());
                nodes.add(targetNode);
            }
            sourceNode.addNextNode(link, targetNode);
            targetNode.addPreviousNode(link, sourceNode);
        }
        for (AbstractNode sourceNode : nodes) {
            List<FlowElement> sourceFlowElements = sourceNode.getEndFlowElements();
            if (NodeType.Free.isEquals(sourceNode.getNodeType())) {
                // 如果当前节点是自由流节点，则需要增加跳转到自身的分支
                String conditionExpression = getConditionExpression(sourceNode.getId());
                for (FlowElement sourceFlowElement : sourceFlowElements) {
                    linkToTargetElement(sourceNode.getStartFlowElements(), sourceNode.getNode(), process, sourceFlowElement, conditionExpression);
                }
            }
            for (Link link : sourceNode.getNextNodes().keySet()) {
                List<AbstractNode> nextNodes = sourceNode.getNextNodes().get(link);
                for (AbstractNode targetNode : nextNodes) {
                    List<FlowElement> targetFlowElements = targetNode.getStartFlowElements();
                    // 处理跳转条件
                    String conditionExpression = getConditionExpression(targetNode.getId());
                    if (sourceNode.getNextNodes().size() == 1) {
                        // 如果只有一个后续节点，则分支条件不生效
                        conditionExpression = "";
                    }
                    for (FlowElement sourceFlowElement : sourceFlowElements) {
                        linkToTargetElement(targetFlowElements, link.getToNode(), process, sourceFlowElement, conditionExpression);
                    }
                }
            }
        }
        LOGGER.debug("auto layout begin");
        LOGGER.debug(JSONObject.toJSONString(model));
        new BpmnAutoLayout(model).execute();
        LOGGER.debug("auto layout completed");
        LOGGER.debug("validating model");
        validateModel(model);
        LOGGER.debug("model validate completed");
        return model;
    }

    public static String getConditionExpression(String toNodeId) {
        toNodeId = toNodeId.replace("\\", "\\\\").replace("\"", "\\\"");
        return "${" + TaskVariableName.NODE_IDS.getName() + ".contains(\"" + toNodeId + "\")}";
    }

    /**
     * 将流程对象转为bpmn模型
     *
     * @param flow 流程对象
     * @return BPMN模型
     */
    public static BpmnModel generate(Flow flow) {
        return new BpmnUtil(flow).generate();
    }

    private void linkToTargetElement(List<FlowElement> targetFlowElements, Node toNode, Process process, FlowElement sourceFlowElement, String conditionExpression) {
        for (FlowElement targetFlowElement : targetFlowElements) {
            linkToTargetElement(targetFlowElement, toNode, process, sourceFlowElement, conditionExpression);
        }
    }

    private void linkToTargetElement(FlowElement targetFlowElement, Node toNode, Process process, FlowElement sourceFlowElement, String conditionExpression) {
        SequenceFlow sequenceFlow = AbstractNode.addSequenceFlow(process, sourceFlowElement, targetFlowElement);
        sequenceFlow.setName(toNode.getName());
        if (StringUtils.isNotEmpty(conditionExpression)) {
            sequenceFlow.setConditionExpression(conditionExpression);
        }
        if (sourceFlowElement instanceof ExclusiveGateway) {
            ExclusiveGateway exclusiveGateway = (ExclusiveGateway) sourceFlowElement;
            exclusiveGateway.getOutgoingFlows().add(sequenceFlow);
        } else if (sourceFlowElement instanceof InclusiveGateway) {
            InclusiveGateway inclusiveGateway = (InclusiveGateway) sourceFlowElement;
            inclusiveGateway.getOutgoingFlows().add(sequenceFlow);
        }
    }

    /**
     * 获取扩展元素
     *
     * @param name 元素名称
     * @return org.activiti.bpmn.model.ExtensionElement
     * @author JonnyJiang
     * @date 2019/10/10 17:11
     */

    public static ExtensionElement getExtensionElement(String name) {
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setNamespacePrefix(BpmnXMLConstants.ACTIVITI_EXTENSIONS_PREFIX);
        extensionElement.setNamespace(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE);
        extensionElement.setName(name);
        return extensionElement;
    }

    /**
     * 获取class元素
     *
     * @param className class名称
     * @return 扩展元素
     */
    public static ExtensionAttribute getClassAttribute(String className) {
        ExtensionAttribute classAttribute = new ExtensionAttribute(BpmnXMLConstants.ATTRIBUTE_LISTENER_CLASS);
        classAttribute.setValue(className);
        return classAttribute;
    }

    /**
     * 获取event元素
     *
     * @param event event
     * @return org.activiti.bpmn.model.ExtensionAttribute 扩展元素
     * @author JonnyJiang
     * @date 2019/10/10 17:12
     */

    private static ExtensionAttribute getEventAttribute(String event) {
        ExtensionAttribute eventAttribute = new ExtensionAttribute(BpmnXMLConstants.ATTRIBUTE_LISTENER_EVENT);
        eventAttribute.setValue(event);
        return eventAttribute;
    }

    /**
     * 组装流程事件
     *
     * @param process   流程
     * @param className 处理类名称
     * @param event     时间类型
     * @author JonnyJiang
     * @date 2019/10/10 17:11
     */

    private static void appendProcessExecutionListener(Process process, String className, String event) {
        ExtensionElement extensionElement = getExtensionElement(BpmnXMLConstants.ELEMENT_EXECUTION_LISTENER);
        extensionElement.addAttribute(getClassAttribute(className));
        extensionElement.addAttribute(getEventAttribute(event));
        process.addExtensionElement(extensionElement);
    }

    /**
     * 组装流程事件
     *
     * @param process 流程
     * @author JonnyJiang
     * @date 2019/10/10 17:11
     */

    private static void appendProcessExecutionListener(Process process) {
        appendProcessExecutionListener(process, ProcessStartListener.class.getName(), BaseExecutionListener.EVENTNAME_START);
        appendProcessExecutionListener(process, ProcessEndListener.class.getName(), BaseExecutionListener.EVENTNAME_END);
//        for (Event event : flow.getEvents()) {
//            if (ProcessEventType.START.isEquals(event.getEventType())) {
//                appendProcessExecutionListener(process, event.getClassName(), ProcessEventType.START.getEventName());
//            } else if (ProcessEventType.END.isEquals(event.getEventType())) {
//                appendProcessExecutionListener(process, event.getClassName(), ProcessEventType.END.getEventName());
//            }
//        }
    }

    /**
     * 组装任务事件
     *
     * @param userTask 用户任务
     * @author JonnyJiang
     * @date 2019/10/10 16:15
     */

    public static void appendTaskListener(UserTask userTask) {
        appendTaskListener(userTask, TaskCreateListener.class.getName(), NodeEventType.CREATE.getEventName());
        appendTaskListener(userTask, TaskAssignmentListener.class.getName(), NodeEventType.ASSIGNMENT.getEventName());
        appendTaskListener(userTask, TaskCompleteListener.class.getName(), NodeEventType.COMPLETE.getEventName());
        appendTaskListener(userTask, TaskDeleteListener.class.getName(), NodeEventType.DELETE.getEventName());
        appendTaskListener(userTask, TaskAllEventListener.class.getName(), NodeEventType.ALL_EVENT.getEventName());
//        if (HostMode.AllowDeliver.isEquals(node.getHostMode())) {
//            appendTaskListener(userTask, TaskCompletedListener.class.getName(), NodeEventType.COMPLETE.getEventName());
//        }
//        for (NodeEvent nodeEvent : node.getEvents()) {
//            if (NodeEventType.CREATE.isEquals(nodeEvent.getEventType())) {
//                appendTaskListener(userTask, nodeEvent, NodeEventType.CREATE);
//            } else if (NodeEventType.ASSIGNMENT.isEquals(nodeEvent.getEventType())) {
//                appendTaskListener(userTask, nodeEvent, NodeEventType.ASSIGNMENT);
//            } else if (NodeEventType.COMPLETE.isEquals(nodeEvent.getEventType())) {
//                appendTaskListener(userTask, nodeEvent, NodeEventType.COMPLETE);
//            } else if (NodeEventType.DELETE.isEquals(nodeEvent.getEventType())) {
//                appendTaskListener(userTask, nodeEvent, NodeEventType.DELETE);
//            } else if (NodeEventType.ALL_EVENT.isEquals(nodeEvent.getEventType())) {
//                appendTaskListener(userTask, nodeEvent, NodeEventType.ALL_EVENT);
//            }
//        }
    }

    /**
     * 拼接节点事件
     *
     * @param flowElement 任务
     * @param className   类名
     * @param eventName   事件名
     */
    public static void appendTaskListener(FlowElement flowElement, String className, String eventName) {
        ExtensionElement extensionElement = getExtensionElement(BpmnXMLConstants.ELEMENT_TASK_LISTENER);
        extensionElement.addAttribute(getClassAttribute(className));
        extensionElement.addAttribute(getEventAttribute(eventName));
        flowElement.addExtensionElement(extensionElement);
    }

    private static AbstractNode getNode(List<AbstractNode> nodes, Node wfdNode) {
        for (AbstractNode node : nodes) {
            if (wfdNode.getId().equals(node.getId())) {
                return node;
            }
        }
        return null;
    }

    /**
     * 校验模型
     *
     * @param model 模型
     * @author JonnyJiang
     * @date 2019/10/10 17:14
     */

    private static void validateModel(BpmnModel model) {
        RepositoryService repositoryService = SpringContextUtils.getBean(RepositoryService.class);
        List<ValidationError> validationErrors = repositoryService.validateProcess(model);
        if (validationErrors.size() > 0) {
            System.out.println(validationErrors);
            String bpmn = null;
            try {
                bpmn = new String(new BpmnXMLConverter().convertToXML(model), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            throw new BpmException(LocaleUtils.getBpmnModelValidateError(bpmn));
        }
    }

    public static void initUserTasks(Flow flow, BpmnModel bpmnModel, DeliverInfo deliverInfo, Map<String, Object> variables) {
        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
            Node node = flow.getNodeById(deliverNode.getNodeId());
            if (NodeType.Manual.isEquals(node.getNodeType()) || NodeType.Free.isEquals(node.getNodeType())) {
                if (deliverNode.getDeliverUsers().size() > 0) {
                    UserTask userTask = BpmnUtil.getUserTaskByNodeId(flow, bpmnModel, node);
                    List<String> userIds = FlowUtils.getFirstMultiInstanceUserIds(node, deliverNode);
                    initUserTask(userTask, node, userIds, variables);
                } else {
                    throw new BpmException(LocaleUtils.getDeliverUsersNotFound(node.getName(), node.getCode()));
                }
            }
        }
    }

    public static UserTask getUserTaskByNodeId(Flow flow, BpmnModel bpmnModel, Node node) {
        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (node.getId().equals(flowElement.getName())) {
                if (flowElement instanceof UserTask) {
                    return (UserTask) flowElement;
                }
            }
        }
        throw new BpmException(LocaleUtils.getUserTaskNotFoundByNodeId(flow.getCode(), node.getId()));
    }

    public static void initUserTask(UserTask userTask, Node node, List<String> userIds, Map<String, Object> variables) {
        if (IntegerUtils.isTrue(node.getEnableManualSelParticipant()) && IntegerUtils.isTrue(node.getEnableHostOnly())) {
            // 如果启用单人办理
            if (userIds.size() != 1) {
                throw new BpmException(LocaleUtils.getNodeHostOnly(node.getCode(), node.getName(), userIds.size()));
            }
        }
        if (userIds.size() > 0) {
            resetCollectionVariable(userTask, userIds, variables);
        } else {
            throw new BpmException(LocaleUtils.getDeliverUsersNotFound(node.getName(), node.getCode()));
        }
    }

    private static void resetCollectionVariable(UserTask userTask, List<String> userIds, Map<String, Object> variables) {
        String assigneeListVariableName = UUID.randomUUID().toString();
        variables.put(assigneeListVariableName, userIds);
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = userTask.getLoopCharacteristics();
        multiInstanceLoopCharacteristics.setInputDataItem(assigneeListVariableName);
        if (userTask.getBehavior() instanceof MultiInstanceActivityBehavior) {
            MultiInstanceActivityBehavior behavior = (MultiInstanceActivityBehavior) userTask.getBehavior();
            behavior.setCollectionVariable(assigneeListVariableName);
            userTask.setBehavior(behavior);
        }
    }
}
