package com.csicit.ace.bpm;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;

import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2019/9/10 10:36
 */
public interface TaskInstance {
    /**
     * 获取流程实例id
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/9/10 10:52
     */

    String getFlowInstanceId();

    /**
     * 获取id
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/9/10 10:52
     */

    String getId();

    /**
     * 获取节点名称
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/9/11 9:12
     */

    String getNodeName();

    /**
     * 获取节点id
     *
     * @return 节点id
     * @author JonnyJiang
     * @date 2019/10/11 10:38
     */

    String getNodeId();

    /**
     * 获取节点标识
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/11/11 20:41
     */

    String getNodeCode();

    /**
     * 签收人或被委托
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/9/27 18:23
     */

    String getAssignee();

    /**
     * 签收人（默认为空，只有在委托时才有值）
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/9/27 18:24
     */

    String getOwner();

    /**
     * 获取开始时间
     *
     * @return java.util.Date
     * @author JonnyJiang
     * @date 2019/12/11 16:10
     */

    Date getStartTime();

    /**
     * 获取接收时间
     *
     * @return java.util.Date
     * @author JonnyJiang
     * @date 2019/12/11 16:09
     */

    Date getClaimTime();

    /**
     * 获取办结时间
     *
     * @return 办结时间
     * @author JonnyJiang
     * @date 2019/11/7 16:41
     */

    Date getEndTime();

    /**
     * 获取流程标识
     *
     * @return 流程标识
     * @author JonnyJiang
     * @date 2019/11/7 17:12
     */

    String getFlowCode();

    /**
     * 获取业务主键
     *
     * @return 业务主键
     * @author JonnyJiang
     * @date 2019/11/7 17:12
     */

    String getFlowBusinessKey();

    /**
     * 获取节点信息
     *
     * @return 节点信息
     * @author JonnyJiang
     * @date 2020/11/25 14:20
     */

    Node getNode();

    /**
     * 获取流程定义
     *
     * @return 流程定义
     * @author JonnyJiang
     * @date 2020/11/25 14:21
     */

    Flow getFlow();

    /**
     * 获取流程实例
     *
     * @return 流程实例
     * @author JonnyJiang
     * @date 2020/12/1 11:41
     */

    WfiFlowDO getWfiFlow();
}
