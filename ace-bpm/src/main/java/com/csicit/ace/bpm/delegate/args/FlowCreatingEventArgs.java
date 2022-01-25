package com.csicit.ace.bpm.delegate.args;

import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/11/7 20:08
 */
public class FlowCreatingEventArgs {
    private WfdVFlowDO wfdVFlow;
    private Flow flow;
    private String businessKey;
    private SysUserDO starter;
    private Map<String, Object> variables;

    /**
     * 构造函数
     *
     * @param wfdVFlow    流程定义版20:17  * @param flow        流程定义
     * @param businessKey 业务标识
     * @param starter     发起人
     * @param variables   流程变量
     * @author JonnyJiang
     * @date 2019/11/7 19:24
     */

    public FlowCreatingEventArgs(WfdVFlowDO wfdVFlow, Flow flow, String businessKey, SysUserDO starter, Map<String, Object> variables) {
        this.wfdVFlow = wfdVFlow;
        this.flow = flow;
        this.businessKey = businessKey;
        this.starter = starter;
        this.variables = variables;
    }

    public WfdVFlowDO getWfdVFlow() {
        return wfdVFlow;
    }

    public Flow getFlow() {
        return flow;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public SysUserDO getStarter() {
        return starter;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
