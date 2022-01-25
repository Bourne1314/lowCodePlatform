package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;

/**
 * 任务删除事件参数
 *
 * @author JonnyJiang
 * @date 2019/11/7 20:09
 */
public class TaskDeleteEventArgs {
    private final Node node;
    private final String taskId;
    private final String assignee;
    private final String owner;

    /**
     * 构造函数
     *
     * @param node     节点
     * @param taskId   任务ID
     * @param assignee 办理人
     * @param owner    拥有人
     * @author JonnyJiang
     * @date 21:20
     */

    public TaskDeleteEventArgs(Node node, String taskId, String assignee, String owner) {
        this.node = node;
        this.taskId = taskId;
        this.assignee = assignee;
        this.owner = owner;
    }

    public Node getNode() {
        return node;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getOwner() {
        return owner;
    }
}
