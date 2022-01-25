package com.csicit.ace.bpm.activiti.nodes;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.common.utils.IntegerUtils;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;

/**
 * 开始节点
 *
 * @author JonnyJiang
 * @date 2019/8/26 15:18
 */
public class StartNode extends AbstractNode {
    public StartNode(BpmnModel bpmnModel, Process process, Node nodeStart) {
        super(bpmnModel, process, nodeStart);
    }

    @Override
    public void generateFlowElements() {
        // 创建开始事件
        if (IntegerUtils.isTrue(node.getEnableApiStart())) {
            StartEvent startEvent = new StartEvent();
            startEvent.setId(generateId());
            startEvent.setName(node.getCode());
            process.addFlowElement(startEvent);
            addStartFlowElement(startEvent);
            addEndFlowElement(startEvent);
        }
        if (IntegerUtils.isTrue(node.getEnableTimerStart())) {
            StartEvent startEventTimer = new StartEvent();
            TimerEventDefinition timerEventDefinition = new TimerEventDefinition();
            timerEventDefinition.setTimeDuration(node.getStartTimerCron());
            startEventTimer.addEventDefinition(timerEventDefinition);
            startEventTimer.setId(generateId());
            startEventTimer.setName(node.getName());
            process.addFlowElement(startEventTimer);
            addStartFlowElement(startEventTimer);
            addEndFlowElement(startEventTimer);
        }
        if (IntegerUtils.isTrue(node.getEnableMessageStart())) {
            StartEvent startEventMessage = new StartEvent();
            MessageEventDefinition messageEventDefinition = new MessageEventDefinition();
            Message message = new Message();
            message.setId(generateId());
            message.setName(node.getMessageName());
            bpmnModel.addMessage(message);
            messageEventDefinition.setMessageRef(message.getId());
            startEventMessage.addEventDefinition(messageEventDefinition);
            startEventMessage.setId(generateId());
            startEventMessage.setName(node.getName());
            process.addFlowElement(startEventMessage);
            addStartFlowElement(startEventMessage);
            addEndFlowElement(startEventMessage);
        }
    }
}