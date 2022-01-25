package com.csicit.ace.bpm.activiti.cmd;

import com.csicit.ace.bpm.activiti.utils.BpmnUtil;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/6/8 20:26
 */
public class MultiInstanceCmd implements Command<Void> {
    private final UserTask userTask;
    private final Node node;
    private final List<String> userIds;
    private final Map<String, Object> variables;
    private final Task task;

    public MultiInstanceCmd(UserTask userTask, Node node, List<String> userIds, Map<String, Object> variables, Task task) {
        this.userTask = userTask;
        this.node = node;
        this.userIds = userIds;
        this.variables = variables;
        this.task = task;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntityImpl taskEntity = (TaskEntityImpl) task;
        ExecutionEntity execution = taskEntity.getExecution();
        // 任务创建后，会在原执行流的基础上再创建一个执行流，此处的parent即为原来的执行流
        ExecutionEntity parentExecutionEntity = execution.getParent();
        // 新创建的任务，应在原来的执行流的基础上创建，因此此处再次获取parent
        ExecutionEntity originalExecutionEntity = parentExecutionEntity.getParent();
        Map<String, Object> m = new HashMap<>(16);
        BpmnUtil.initUserTask(userTask, node, userIds, m);
        m.forEach((k, v) -> {
            if (!variables.containsKey(k)) {
                execution.setVariable(k, v);
            }
        });
        ExecutionEntityManager executionEntityManager = Context.getProcessEngineConfiguration().getExecutionEntityManager();
        /**
         * 以下来自activiti-engine\org\activiti\engine\impl\agenda\TakeOutgoingSequenceFlowsOperation.java
         */
        ExecutionEntity outgoingExecutionEntity = executionEntityManager.createChildExecution(originalExecutionEntity);
        outgoingExecutionEntity.setCurrentFlowElement(userTask);
        executionEntityManager.insert(outgoingExecutionEntity);
        Context.getAgenda().planContinueProcessOperation(outgoingExecutionEntity);
        return null;
    }
}