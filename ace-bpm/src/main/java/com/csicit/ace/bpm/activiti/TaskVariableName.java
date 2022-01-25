package com.csicit.ace.bpm.activiti;

/**
 * 变量名称
 *
 * @author JonnyJiang
 * @date 2019/10/11 10:24
 */
public enum TaskVariableName {
    /**
     * 办理意见
     */
    OPTION("TaskVariableNameOption"),
//    /**
//     * 节点标识
//     */
//    CODE("TaskVariableNodeCode"),
//    /**
//     * 节点名称
//     */
//    NAME("TaskVariableNodeName"),
    /**
     * 临时变量节点id集合
     */
    NODE_IDS("TaskVariableNodeIds"),
    /**
     * 临时变量节点id集合
     */
    FIRST_ARRIVING_NODE_IDS("TaskVariableFirstArrivingNodeIds"),
    /**
     * 节点转交信息ID
     */
    WFI_DELIVER_ID("TaskVariableDeliverInfoId"),
    /**
     * 上一节点转交信息ID
     */
    WFI_DELIVER_ID_FROM("TaskVariableDeliverInfoIdFrom"),
    /**
     * 父任务ID
     */
    PARENT_TASK_ID("TaskVariableParentTaskId");

    private String name;

    TaskVariableName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
