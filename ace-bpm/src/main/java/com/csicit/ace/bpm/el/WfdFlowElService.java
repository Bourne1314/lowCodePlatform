package com.csicit.ace.bpm.el;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/27 8:15
 */
@Transactional
public interface WfdFlowElService {
    String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    String getFormValue(String var);

    /**
     * 根据用户主键获取岗位名称
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/14 15:40
     */
    String getPostName(String userId);

    /**
     * 根据表单字段中的用户ID获取岗位名称
     *
     * @param columnName 表单字段
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/11/20 8:09
     */

    String getPostNameByUserIdFromFormValue(String columnName);

    /**
     * 获取当前步骤名称列表
     *
     * @return 当前步骤名称列表
     * @author JonnyJiang
     * @date 2019/10/23 18:55
     */

    String getNodeNames();

    /**
     * 获取当前步骤标识列表
     *
     * @return 当前步骤标识列表
     * @author JonnyJiang
     * @date 2019/10/23 18:56
     */

    String getNodeCodes();

    /**
     * 获取当前步骤序号列表
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/23 18:56
     */

    String getNodeSns();

    /**
     * 获取流程是否已完成
     *
     * @return 流程是否已完成
     * @author JonnyJiang
     * @date 2019/10/23 18:56
     */

    String getCompleted();

    /**
     * 获取流程状态
     *
     * @return 流程状态
     * @author JonnyJiang
     * @date 2019/10/23 18:56
     */

    String getFlowState();

    /**
     * 获取主办人ID
     *
     * @param nodeCode 节点标识
     * @return 主办人ID
     * @author JonnyJiang
     * @date 2019/10/23 18:56
     */

    String getNodeHostId(String nodeCode);


    /**
     * 获取主办人姓名
     *
     * @param nodeCode 节点标识
     * @return 主办人姓名
     * @author JonnyJiang
     * @date 2019/10/23 18:57
     */

    String getNodeHostRealName(String nodeCode);

    /**
     * 获取主办意见
     *
     * @param nodeCode 节点标识
     * @return 主办意见
     * @author JonnyJiang
     * @date 2019/10/23 18:58
     */

    String getNodeHostOpinion(String nodeCode);

    /**
     * 获取发起时间
     *
     * @return 发起时间
     * @author JonnyJiang
     * @date 2019/11/11 20:38
     */

    String getStartTime();

    /**
     * 获取指定节点的办结时间
     *
     * @param nodeCode
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/12/2 10:51
     */

    String getNodeStartTime(String nodeCode);

    /**
     * 获取办结时间
     *
     * @return java.util.Date
     * @author JonnyJiang
     * @date 2019/10/23 18:58
     */

    String getEndTime();

    /**
     * 获取指定节点的办结时间
     *
     * @param nodeCode 节点标识
     * @return
     */
    String getNodeEndTime(String nodeCode);

    /**
     * 获取主办人主职部门id
     *
     * @param nodeCode 节点标识
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/23 18:58
     */

    String getHostDeptId(String nodeCode);

    /**
     * 获取主办人主职部门
     *
     * @param nodeCode 节点标识
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/23 18:59
     */

    String getHostDeptName(String nodeCode);

    /**
     * 获取当前登录用户id
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/23 19:00
     */

    String getCurrentUserId();

    /**
     * 获取当前登录用户名称
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/23 19:00
     */

    String getCurrentUserRealName();

    /**
     * 获取产生流程正在办理任务的驳回节点标识
     *
     * @return 节点标识
     * @author JonnyJiang
     * @date 2019/12/25 14:06
     */

    String getRejectFromNodeCode();

    /**
     * 获取产生流程正在办理任务的驳回节点名称
     *
     * @return 节点名称
     * @author JonnyJiang
     * @date 2019/12/25 14:06
     */

    String getRejectFromNodeName();
}