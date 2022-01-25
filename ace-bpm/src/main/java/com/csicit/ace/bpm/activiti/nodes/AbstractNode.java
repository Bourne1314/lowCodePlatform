package com.csicit.ace.bpm.activiti.nodes;

import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;

import java.util.*;

/**
 * @author JonnyJiang
 * @date 2019/8/26 14:31
 */
public abstract class AbstractNode {
    protected BpmnModel bpmnModel;
    protected Process process;
    protected Node node;
    /**
     * 开始流程元素
     */
    private List<FlowElement> startFlowElements = new ArrayList<>();
    /**
     * 结束流程元素
     */
    private List<FlowElement> endFlowElements = new ArrayList<>();

    /**
     * 前一个节点
     */
    private Map<Link, List<AbstractNode>> previousNodes = new HashMap<>();
    /**
     * 后一个节点
     */
    private Map<Link, List<AbstractNode>> nextNodes = new HashMap<>();

    public AbstractNode(BpmnModel bpmnModel, Process process, Node node) {
        this.bpmnModel = bpmnModel;
        this.process = process;
        this.node = node;
        generateFlowElements();
    }

    public abstract void generateFlowElements();

    public Map<Link, List<AbstractNode>> getPreviousNodes() {
        return previousNodes;
    }

    public Map<Link, List<AbstractNode>> getNextNodes() {
        return nextNodes;
    }

    public String getId() {
        return node.getId();
    }

    public String getCode() {
        return node.getCode();
    }

    public Boolean getAllowDrawBack() {
        return false;
    }

    public static String generateId() {
        return "fid-" + UUID.randomUUID().toString();
    }

    public static SequenceFlow addSequenceFlow(Process process, FlowElement sourceFlowElement, FlowElement targetFlowElement) {
        return addSequenceFlow(process, sourceFlowElement, targetFlowElement, null);
    }

    public static SequenceFlow addSequenceFlow(Process process, FlowElement sourceFlowElement, FlowElement targetFlowElement, Link link) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(generateId());
        sequenceFlow.setSourceRef(sourceFlowElement.getId());
        sequenceFlow.setSourceFlowElement(sourceFlowElement);
        sequenceFlow.setTargetRef(targetFlowElement.getId());
        sequenceFlow.setTargetFlowElement(targetFlowElement);
        if (link != null) {

        }
        process.addFlowElement(sequenceFlow);
        return sequenceFlow;
    }

    private void addFlowElement(Link link, FlowElement flowElement, Map<Link, List<FlowElement>> map) {
        if (map.containsKey(link)) {
            map.get(link).add(flowElement);
        } else {
            List<FlowElement> flowElements = new ArrayList<>();
            flowElements.add(flowElement);
            map.put(link, flowElements);
        }
    }

    protected void addStartFlowElement(FlowElement flowElement) {
        startFlowElements.add(flowElement);
    }

    protected void addEndFlowElement(FlowElement flowElement) {
        endFlowElements.add(flowElement);
    }

    public List<FlowElement> getStartFlowElements() {
        return startFlowElements;
    }

    public List<FlowElement> getEndFlowElements() {
        return endFlowElements;
    }

    private void addNode(Link link, AbstractNode previousNode, Map<Link, List<AbstractNode>> map) {
        if (map.containsKey(link)) {
            map.get(link).add(previousNode);
        } else {
            List<AbstractNode> flowElements = new ArrayList<>();
            flowElements.add(previousNode);
            map.put(link, flowElements);
        }
    }

    public void addPreviousNode(Link link, AbstractNode previousNode) {
        addNode(link, previousNode, previousNodes);
    }

    public void addNextNode(Link link, AbstractNode nextNode) {
        addNode(link, nextNode, nextNodes);
    }

    public String getNodeType() {
        return node.getNodeType();
    }

    public Node getNode() {
        return node;
    }
}