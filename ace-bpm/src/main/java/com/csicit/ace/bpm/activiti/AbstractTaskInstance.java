package com.csicit.ace.bpm.activiti;

import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.FlowUtils;
import org.activiti.engine.task.TaskInfo;

import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2020/4/16 10:29
 */
public abstract class AbstractTaskInstance implements TaskInstance {
    private TaskInfo taskInfo;
    private WfiFlowDO wfiFlow;
    private Flow flow;
    private Node node;

    public AbstractTaskInstance(TaskInfo taskInfo, WfiFlowDO wfiFlow) {
        this.taskInfo = taskInfo;
        this.wfiFlow = wfiFlow;
        init();
    }

    private void init() {
        this.flow = FlowUtils.getFlow(wfiFlow.getModel());
        this.node = flow.getNodeById(getNodeId());
    }

    @Override
    public String getId() {
        return taskInfo.getId();
    }

    @Override
    public String getNodeId() {
        return taskInfo.getName();
    }

    @Override
    public String getFlowCode() {
        return wfiFlow.getFlowCode();
    }

    @Override
    public String getFlowBusinessKey() {
        return wfiFlow.getBusinessKey();
    }

    @Override
    public String getFlowInstanceId() {
        return taskInfo.getProcessInstanceId();
    }

    @Override
    public String getAssignee() {
        return taskInfo.getAssignee();
    }

    @Override
    public String getOwner() {
        return taskInfo.getOwner();
    }

    @Override
    public Date getStartTime() {
        return taskInfo.getCreateTime();
    }

    @Override
    public Date getClaimTime() {
        return taskInfo.getClaimTime();
    }

    @Override
    public String getNodeName() {
        return node.getName();
    }

    @Override
    public String getNodeCode() {
        return node.getCode();
    }

    @Override
    public Flow getFlow() {
        return flow;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public WfiFlowDO getWfiFlow() {
        return wfiFlow;
    }
}
