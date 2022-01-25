package com.csicit.ace.bpm.activiti.nodes;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;

/**
 * 子流程节点
 *
 * @author JonnyJiang
 * @date 2019/8/26 14:35
 */
public class SubflowNode extends AbstractNode {
    public SubflowNode(BpmnModel bpmnModel, Process process, Node nodeSubflow) {
        super(bpmnModel, process, nodeSubflow);
    }

    @Override
    public void generateFlowElements() {
        // 是否是调用式子流程
        Boolean isCallActivity = true;
        if (isCallActivity) {
            CallActivity callActivity = new CallActivity();
            callActivity.setId(generateId());
            callActivity.setName(node.getCode());
            callActivity.setCalledElement(node.getSubFlowId());
            callActivity.setExclusive(true);
            process.addFlowElement(callActivity);
            addStartFlowElement(callActivity);
            addEndFlowElement(callActivity);
        } else {
            // 如果是嵌入式子流程
            SubProcess subProcess = new SubProcess();
            subProcess.setId(generateId());
            subProcess.setName(node.getCode());

            process.addFlowElement(subProcess);
            addStartFlowElement(subProcess);
            addEndFlowElement(subProcess);
        }
    }
}