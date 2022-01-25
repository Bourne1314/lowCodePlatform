package com.csicit.ace.bpm;

import com.csicit.ace.bpm.pojo.vo.wfd.Flow;

import javax.servlet.http.HttpSession;

/**
 * @author JonnyJiang
 * @date 2019/12/10 9:35
 */
public class SessionAttribute {
    public static final String TASK_WFD_DELIVER = "TASK_WFD_DELIVER";
    public static final String TASK_DELIVER_INFO = "TASK_DELIVER_INFO";
    /**
     * 流程实例id
     */
    public static final String WFD_FLOW_EL_FLOW_ID = "WFD_FLOW_EL_FLOW_ID";
    /**
     * 流程定义
     */
    public static final String WFD_FLOW_EL_FLOW = "WFD_FLOW_EL_FLOW";
    /**
     * 业务标识
     */
    public static final String WFD_FLOW_EL_BUSINESS_KEY = "WFD_FLOW_EL_BUSINESS_KEY";

    public static void initElSession(HttpSession session, String flowInstanceId, Flow flow, String businessKey) {
        session.setAttribute(WFD_FLOW_EL_FLOW_ID, flowInstanceId);
        session.setAttribute(WFD_FLOW_EL_FLOW, flow);
        session.setAttribute(WFD_FLOW_EL_BUSINESS_KEY, businessKey);
    }
}
