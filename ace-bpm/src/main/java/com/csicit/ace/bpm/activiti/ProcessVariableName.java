package com.csicit.ace.bpm.activiti;

/**
 * @author JonnyJiang
 * @date 2019/9/24 9:15
 */
public enum ProcessVariableName {
    /**
     * 流程变量流程发布id
     */
    VFlowId("ProcessVariableVFlowId"),
    /**
     * 工作文号
     */
    FLOW_NO("ProcessVariableFlowNo"),
    /**
     * 表单URL
     */
    FLOW_FORM_URL("ProcessVariableFlowFormUrl"),
    /**
     * 是否主流程
     */
    IS_MAIN("ProcessVariableFlowIsMain"),
    /**
     * 流程结果
     */
    Result("ProcessVariableFlowResult"),
    /**
     * 无效
     */
    Invalid("ProcessVariableInvalid");

    private String name;

    ProcessVariableName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
