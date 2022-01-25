package com.csicit.ace.bpm.activiti.nodes;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;

/**
 * 结束节点
 *
 * @author JonnyJiang
 * @date 2019/8/26 14:35
 */
public class EndNode extends AbstractNode {
    public EndNode(BpmnModel bpmnModel, Process process, Node nodeEnd) {
        super(bpmnModel, process, nodeEnd);
    }

    @Override
    public void generateFlowElements() {
//        // 创建设置流程结果的任务
//        ServiceTask serviceTask = new ServiceTask();
//        serviceTask.setId(generateId());
//        serviceTask.setName(InternationKey.getMessage(InternationKey.TASK_NAME_SET_FLOW_RESULT));
//        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
//        serviceTask.setImplementation(ResultServiceTaskListener.class.getName());
//
//        List<FieldExtension> fieldExtensions = new ArrayList<>();
//
//        FieldExtension fieldExtension = new FieldExtension();
//        fieldExtension.setFieldName("result");
//        fieldExtension.setExpression(node.getResultValue());
//        ExtensionElement fieldElement = BpmnUtils.getExtensionElement(BpmnXMLConstants.ELEMENT_FIELD);
//        fieldElement.setName(ProcessVariableName.Result.getName());
//        fieldExtensions.add(fieldExtension);
//
//        serviceTask.setFieldExtensions(fieldExtensions);
//
//        process.addFlowElement(serviceTask);

        EndEvent endEvent = new EndEvent();
        endEvent.setId(generateId());
        endEvent.setName(node.getId());
        process.addFlowElement(endEvent);

        addStartFlowElement(endEvent);
        addEndFlowElement(endEvent);
//        addSequenceFlow(process, serviceTask, endEvent);
    }
}
