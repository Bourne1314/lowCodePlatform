package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;

/**
 * @author JonnyJiang
 * @date 2019/11/7 20:09
 */
public class TaskAssignmentEventArgs {
    private String taskId;
    private Node node;
    private String assignee;
    private String originalAssignee;

    public TaskAssignmentEventArgs(String taskId, Node node, String originalAssignee, String assignee) {
        this.taskId = taskId;
        this.node = node;
        this.originalAssignee = originalAssignee;
        this.assignee = assignee;
    }

    public String getTaskId() {
        return taskId;
    }

    public Node getNode() {
        return node;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getOriginalAssignee() {
        return originalAssignee;
    }
}
