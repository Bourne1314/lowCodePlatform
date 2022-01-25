package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/11/7 20:09
 */
public class TaskCompletingEventArgs {
    private WfiFlowDO wfiFlow;
    private Flow flow;
    private Node node;
    private TaskInstance taskInstance;
    private Map<String, Object> variables;
    private DeliverInfo deliverInfo;

    /**
     * 构造函数
     *
     * @param wfiFlow      流程实例
     * @param flow         流程定义
     * @param node         节点
     * @param taskInstance 任务实例
     * @param variables    变量
     * @param deliverInfo  转交信息
     * @return void
     * @author JonnyJiang
     * @date 2019/11/7 19:48
     */

    public TaskCompletingEventArgs(WfiFlowDO wfiFlow, Flow flow, Node node, TaskInstance taskInstance, Map<String, Object> variables, DeliverInfo deliverInfo) {
        this.wfiFlow = wfiFlow;
        this.flow = flow;
        this.node = node;
        this.taskInstance = taskInstance;
        this.variables = variables;
        this.deliverInfo = deliverInfo;
    }

    public WfiFlowDO getWfiFlow() {
        return wfiFlow;
    }

    public Flow getFlow() {
        return flow;
    }

    public Node getNode() {
        return node;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public DeliverInfo getDeliverInfo() {
        return deliverInfo;
    }
}
