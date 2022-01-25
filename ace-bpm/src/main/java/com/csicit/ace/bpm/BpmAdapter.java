package com.csicit.ace.bpm;

import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.activiti.WfiDeliverTasks;
import com.csicit.ace.bpm.enums.OperationType;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.*;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.process.ActivityVO;
import com.csicit.ace.bpm.pojo.vo.process.FlowVO;
import com.csicit.ace.bpm.pojo.vo.process.TaskVO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.pojo.vo.wfi.WfiNodeVO;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/12/6 17:24
 */
@Transactional
public interface BpmAdapter {

    /**
     * 获取工作任务对应的流程实例
     *
     * @param task 任务
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/9/10 10:38
     */

    FlowInstance getFlowInstance(TaskInstance task);

    /**
     * 获取指定流程实例
     *
     * @param flowInstanceId 工作流实例id
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/6/24 14:53
     */

    FlowInstance getFlowInstance(String flowInstanceId);

    /**
     * 获取指定流程实例
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/12/3 11:06
     */

    FlowInstance getFlowInstanceByBusinessKey(String code, String businessKey);

    /**
     * 获取办理中任务列表
     *
     * @param flowInstanceId 流程实例ID
     * @return 任务实例列表
     * @author JonnyJiang
     * @date 2019/9/11 9:08
     */

    List<TaskInstance> listTasksByFlowInstanceId(String flowInstanceId);

    /**
     * 获取所有任务列表
     *
     * @param flowInstanceId 流程实例id
     * @return 任务列表
     * @author JonnyJiang
     * @date 2019/12/10 16:05
     */

    List<TaskInstance> listAllTasksByFlowInstanceId(String flowInstanceId);

    /**
     * 获取所有任务列表
     *
     * @param flowInstanceId
     * @return 任务列表
     * @author JonnyJiang
     * @date 2019/12/10 16:05
     */

    List<TaskInstance> listCompletedTasksByFlowInstanceId(String flowInstanceId);

    /**
     * 获取流程实例列表
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 流程实例列表
     * @author JonnyJiang
     * @date 2019/12/3 11:12
     */

    List<FlowInstance> listFlowInstancesByBusinessKey(String code, String businessKey);

    /**
     * 流程实例是否存在
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 流程实例是否存在
     * @author JonnyJiang
     * @date 2019/12/4 8:59
     */

    Boolean flowInstanceExists(String code, String businessKey);

    /**
     * 获取任务实例
     *
     * @param id 任务id
     * @return 任务实例
     * @author JonnyJiang
     * @date 2019/11/14 14:05
     */

    TaskInstance getTaskInstanceById(String id);

    /**
     * 获取步骤信息
     *
     * @param flowCode 流程标识
     * @param nodeId   节点id
     * @return 步骤信息
     * @author JonnyJiang
     * @date 2019/10/14 14:44
     */

    NodeInfo getNodeInfo(String flowCode, String nodeId);

    /**
     * 获取步骤信息
     *
     * @param taskId 任务id
     * @return 步骤信息
     * @author JonnyJiang
     * @date 2019/10/14 14:47
     */

    NodeInfo getNodeInfoByTaskId(String taskId);

    /**
     * 获取步骤信息，不校验权限
     * @param taskId 任务id
     * @return
     * @author FourLeaves
     * @date 2021/5/6 14:10
     */
    NodeInfo getNodeInfoByTaskIdWithoutAuth(String taskId);

    /**
     * 是否有监控权限
     *
     * @param flow        流程定义
     * @param currentUser 用户
     * @return 是否有监控权限
     * @author JonnyJiang
     * @date 2019/10/14 18:01
     */

    Boolean hasAdminAuth(Flow flow, SysUserDO currentUser);

    /**
     * 是否有查询权限
     *
     * @param flow        流程定义
     * @param currentUser 用户
     * @return 是否有查询权限
     * @author JonnyJiang
     * @date 2019/10/14 18:01
     */

    Boolean hasQueryAuth(Flow flow, SysUserDO currentUser);

    /**
     * 是否有查询权限
     *
     * @param flowId      流程定义id
     * @param currentUser 用户
     * @return 是否有查询权限
     * @author JonnyJiang
     * @date 2019/10/14 18:01
     */

    Boolean hasQueryAuth(String flowId, SysUserDO currentUser);

    /**
     * 是否有创建权限
     *
     * @param flow        流程定义
     * @param currentUser 用户
     * @return 是否有创建权限
     * @author JonnyJiang
     * @date 2019/10/14 18:09
     */

    Boolean hasInitAuth(Flow flow, SysUserDO currentUser);

    /**
     * 是否有创建权限
     *
     * @param userId     用户id
     * @param initAuthId 权限id
     * @return 是否有创建权限
     * @author JonnyJiang
     * @date 2019/12/6 17:40
     */

    Boolean hasInitAuth(String userId, String initAuthId);

    /**
     * 获取我的工作列表
     *
     * @param flowCode  流程标识
     * @param current   当前页码
     * @param size      每页大小
     * @param completed 是否完成
     * @return 我的工作列表
     */
    List<TaskMineVO> listTaskMineByFlowCode(String flowCode, Integer current, Integer size, Integer completed);

    /**
     * 获取我的工作列表
     *
     * @param current   当前页码
     * @param size      每页大小
     * @param completed 是否完成
     * @return 我的工作列表
     */
    List<TaskMineVO> listTaskMine(Integer current, Integer size, Integer completed);

    /**
     * 获取我的工作列表
     *
     * @param params 参数列表
     * @return 我的工作列表
     */
    List<TaskMineVO> listTaskMineByParams(Map<String, String> params);

    /**
     * 获取我的工作列表个数
     *
     * @param params 参数列表
     * @return 我的工作列表
     */
    long getTaskMineCountByParams(Map<String, String> params);

    /**
     * 获取我的工作列表
     *
     * @param flowCode  流程标识
     * @param completed 是否完成
     * @return 我的工作列表
     */
    List<TaskMineVO> listTaskMineByFlowCode(String flowCode, Integer completed);

    /**
     * 获取我的工作列表
     *
     * @param completed 是否完成
     * @return 我的工作列表
     */
    List<TaskMineVO> listTaskMine(Integer completed);

    /**
     * 获取我的工作总数
     *
     * @param flowCode  流程标识
     * @param completed 是否完成
     * @return 我的工作总数
     * @author JonnyJiang
     * @date 2019/11/4 11:23
     */

    Long getTaskMineTotalByFlowCode(String flowCode, Integer completed);

    /**
     * 获取我的工作总数
     *
     * @param completed 是否完成
     * @return 我的工作总数
     * @author JonnyJiang
     * @date 2019/11/4 11:23
     */

    Long getTaskMineTotal(Integer completed);

    /**
     * 获取工作监控列表
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param flowId  流程定义ID
     * @return 工作监控列表
     */
    R listTaskMonitor(Integer current, Integer size, String flowId, int completed
            , String searchStr);

    /**
     * 获取工作监控列表
     *
     * @param flowId 流程定义ID
     * @return 工作监控列表
     */
    List<TaskMonitorVO> listTaskMonitor(String flowId);

    /**
     * 获取工作监控总数
     *
     * @return 工作监控总数
     * @author JonnyJiang
     * @date 2019/11/4 11:23
     */

    Long getTaskMonitorTotal();

    /**
     * 获取工作查询列表
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param flowId  流程定义ID
     * @return 工作查询列表
     */
    R listTaskQuery(Integer current, Integer size, String flowId, int completed, String searchStr);

    /**
     * 获取工作查询列表
     *
     * @param flowId 流程定义ID
     * @return 工作查询列表
     */
    List<TaskQueryVO> listTaskQuery(String flowId);

    /**
     * 获取工作查询总数
     *
     * @return 工作查询总数
     * @author JonnyJiang
     * @date 2019/11/4 11:23
     */

    Long getTaskQueryTotal(String flowId, int completed, String searchStr);

    /**
     * 获取工作委托列表
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param params
     * @return 工作委托列表
     */
    List<TaskDelegateVO> listTaskDelegate(Integer current, Integer size, Map<String, String> params);

    /**
     * 获取工作委托列表信息
     *
     * @param current 当前页码
     * @param params
     * @return 工作委托列表信息
     */
    R getTaskDelegateInfo(Integer current, Integer size, Map<String, String> params);

    /**
     * 获取工作委托列表
     *
     * @return 工作委托列表
     */
    List<TaskDelegateVO> listTaskDelegate();

    /**
     * 获取工作委托总数
     *
     * @return 工作委托总数
     * @author JonnyJiang
     * @date 2019/11/4 11:23
     */

    Long getTaskDelegateTotal(Map<String, String> params);

    /**
     * 获取步骤信息
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 步骤信息
     * @author JonnyJiang
     * @date 2019/10/30 8:06
     */

    NodeInfo getNodeInfoByBusinessKey(String code, String businessKey);

    /**
     * 获取步骤信息 针对流程的最新步骤
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 步骤信息
     * @author JonnyJiang
     * @date 2019/10/30 8:06
     */

    NodeInfo getLatestNodeInfoByBusinessKey(String code, String businessKey);

    /**
     * 根据流程标识获取生效的流程版本
     *
     * @param code 流程标识
     * @return 流程版本对象
     * @author JonnyJiang
     * @date 2019/9/23 9:10
     */

    WfdVFlowDO getEffectiveWfdVFlowByCode(String code);

    /**
     * 根据任务经办人列表
     *
     * @param taskId 任务ID
     * @return 经办人列表
     * @author JonnyJiang
     * @date 2019/11/5 8:47
     */

    List<TaskUser> getTaskUsersByTaskId(String taskId);

    /**
     * 获取经办人ID
     *
     * @param flowInstanceId 流程实例ID
     * @param nodeId         节点ID
     * @return 经办人ID
     * @author JonnyJiang
     * @date 2019/11/8 11:19
     */

    List<String> getUserIdsByNodeId(String flowInstanceId, String nodeId);

    /**
     * 获取任务列表
     *
     * @param flowInstanceId 流程实例ID
     * @param nodeId         节点ID
     * @return 任务列表
     * @author JonnyJiang
     * @date 2019/11/8 14:13
     */

    List<TaskInstance> getTasksByNodeId(String flowInstanceId, String nodeId);

    /**
     * 获取任务列表
     *
     * @param flowInstanceId 流程实例ID
     * @param nodeIds        节点ID列表
     * @return 任务列表
     * @author JonnyJiang
     * @date 2019/11/8 14:13
     */

    List<HistoricTaskInstance> getTasksByNodeIds(String flowInstanceId, List<String> nodeIds);

    /**
     * 获取节点列表
     *
     * @param flowCode 流程标识
     * @return 节点列表
     * @author JonnyJiang
     * @date 2019/11/8 14:23
     */

    List<NodeVo> listNodesByFlowCode(String flowCode);

    /**
     * 获取节点列表
     *
     * @param flowCode 流程标识
     * @return 节点列表
     * @author JonnyJiang
     * @date 2019/11/8 14:23
     */

    List<NodeVo> listManualNodesByFlowCode(String flowCode);

    /**
     * 根据id获取流程实例
     *
     * @param id wfiFlowId
     * @return com.csicit.ace.bpm.pojo.domain.WfiFlowDO
     * @author JonnyJiang
     * @date 2019/9/16 15:39
     */

    WfiFlowDO getWfiFlowById(String id);

    /**
     * 获取流程实例
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 流程实例对象
     * @author JonnyJiang
     * @date 2019/11/13 16:37
     */

    WfiFlowDO getWfiFlowByBusinessKey(String code, String businessKey);

    /**
     * 获取步骤办结时间
     *
     * @param flowInstanceId 流程实例ID
     * @param nodeCode       节点标识
     * @return 步骤办结时间
     * @author JonnyJiang
     * @date 2019/11/13 18:07
     */

    Date getNodeEndTime(String flowInstanceId, String nodeCode);

    /**
     * 获取办理过程
     *
     * @param taskId 任务信息
     * @return 办理过程-流程实例
     * @author JonnyJiang
     * @date 2019/11/20 16:13
     */

    FlowVO getHandingProcessByTaskId(String taskId);

    /**
     * 获取办理过程
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return com.csicit.ace.bpm.pojo.vo.process.FlowVO
     * @author JonnyJiang
     * @date 2019/11/20 16:15
     */

    FlowVO getHandingProcessByBusinessKey(String flowCode, String businessKey);

    /**
     * 获取办理过程
     *
     * @param taskId 任务id
     * @return 办理过程
     * @author JonnyJiang
     * @date 2019/11/27 11:31
     */

    FlowVO getProcessFlowByTaskId(String taskId);

    /**
     * 获取办理过程
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 办理过程
     * @author JonnyJiang
     * @date 2019/11/27 11:31
     */

    FlowVO getProcessFlowByBusinessKey(String flowCode, String businessKey);

    /**
     * 获取办理过程的所有任务
     *
     * @param current     当前页码
     * @param size        分页大小
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 办理过程的所有任务
     * @author JonnyJiang
     * @date 2019/12/2 18:11
     */

    List<TaskVO> listAllProcessTasksByBusinessKey(Integer current, Integer size, String flowCode, String businessKey);

    /**
     * 获取办理过程的所有任务总数
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return 办理过程的所有任务总数
     * @author FourLeaves
     * @date 2019/12/19 9:32
     */
    Long getAllProcessTasksCountByBusinessKey(String flowCode, String businessKey);

    /**
     * 获取办理过程的所有任务
     *
     * @param current 当前页码
     * @param size    分页大小
     * @param taskId  任务id
     * @return 办理过程的所有任务
     * @author JonnyJiang
     * @date 2019/12/2 18:12
     */

    List<TaskVO> listAllProcessTasksByTaskId(Integer current, Integer size, String taskId);


    /**
     * 获取办理过程的所有任务总数
     *
     * @param taskId 任务id
     * @return 办理过程的所有任务总数
     * @author FourLeaves
     * @date 2019/12/19 9:28
     */
    Long getAllProcessTasksCountByTaskId(String taskId);


    /**
     * 获取办理过程节点任务总数
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @param nodeId      节点id
     * @return 办理过程节点任务总数
     * @author FourLeaves
     * @date 2019/12/19 9:29
     */
    Long getProcessTasksCountByBusinessKey(String flowCode, String businessKey, String nodeId);

    /**
     * 获取办理过程节点任务
     *
     * @param current     当前页码
     * @param size        分页大小
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @param nodeId      节点id
     * @return 办理过程节点任务
     * @author JonnyJiang
     * @date 2019/12/2 18:12
     */

    List<TaskVO> listProcessTasksByBusinessKey(Integer current, Integer size, String flowCode, String businessKey, String nodeId);


    /**
     * 获取办理过程节点任务总数
     *
     * @param taskId 任务id
     * @param nodeId 节点id
     * @return 办理过程节点任务总数
     * @author FourLeaves
     * @date 2019/12/19 9:31
     */
    Long getProcessTasksCountByTaskId(String taskId, String nodeId);

    /**
     * 获取办理过程节点任务
     *
     * @param current 当前页码
     * @param size    分页大小
     * @param taskId  任务id
     * @param nodeId  节点id
     * @return 办理过程节点任务
     * @author JonnyJiang
     * @date 2019/12/2 18:13
     */

    List<TaskVO> listProcessTasksByTaskId(Integer current, Integer size, String taskId, String nodeId);

    /**
     * 获取后续可跳转步骤
     *
     * @param wfiFlow           流程实例
     * @param nodeId            节点id
     * @param workResults       工作结果
     * @param deliverInfoIdFrom 来自转交信息id
     * @return 后续可跳转步骤
     * @author JonnyJiang
     * @date 2020/6/23 18:23
     */

    List<NextStep> listNextSteps(WfiFlowDO wfiFlow, String nodeId, List<String> workResults, String deliverInfoIdFrom);



    /**
     * @Author zhangzhaojun
     * @Description //TODO获取flowId和nodeId下的userid并制作成部门树
     * @Date 14:59 2021/9/8
     * @Param 根据flowid和nodeid获取过程中的用户的部门并返回树结构
     * @return 并返回树结构
     **/
    List<OrgDepartmentDO> getDepartmentTree(String flowId,String nodeId);


    /**
     * @Author zhangzhaojun
     * @Description //TODO获取flowId和nodeId下的userid并制作成部门树
     * @Date 14:59 2021/9/8
     * @Param 根据flowid和nodeid获取过程中的用户的部门并返回树结构
     * @return 并返回树结构
     **/
    List<OrgDepartmentDO> listdeliverDepartment(String flowId,String nodeId,String taskId);

    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 14:59 2021/9/8
     * @Param 根据departmentId部门id获取部门下用户
     * @return 部门下用户
     **/
    List<SysUserDO> getUsersByDepartmentId(String departmentId);
    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 14:59 2021/9/8
     * @Param 根据departmentId部门id获取部门下用户
     * @return 部门下用户
     **/
    List<SysUserDO> getUsersByUserIds(List<String> userIds);

    /**
     * 获取后续可跳转步骤
     *
     *
     * @param wfiFlow
     * @param flow              流程定义
     * @param node              节点
     * @param workResults       工作结果
     * @param deliverInfoIdFrom 来自转交信息id
     * @return 后续可跳转步骤
     * @author JonnyJiang
     * @date 2020/10/19 10:24
     */

    List<NextStep> listNextSteps(WfiFlowDO wfiFlow, Flow flow, Node node, List<String> workResults, String deliverInfoIdFrom);

    /**
     * 获取节点开始时间
     *
     * @param flowInstanceId 流程实例id
     * @param nodeCode       节点标识
     * @return 节点开始时间
     * @author JonnyJiang
     * @date 2020/3/27 9:13
     */

    Date getNodeStartTime(String flowInstanceId, String nodeCode);

    /**
     * 获取活动列表
     *
     * @param id 流程实例id
     * @return 活动列表
     * @author FourLeaves
     * @date 2019/12/4 11:47
     */
    List<ActivityVO> listActivities(String id);

    /**
     * 获取活动列表
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return
     * @author FourLeaves
     * @date 2019/12/4 11:47
     */
    List<ActivityVO> listActivitiesByBusinessKey(String flowCode, String businessKey);

    /**
     * 获取活动列表
     *
     * @param taskId 任务id
     * @return 活动列表
     * @author FourLeaves
     * @date 2019/12/4 11:47
     */
    List<ActivityVO> listActivitiesByTaskId(String taskId);

    /**
     * 获取上级任务列表
     *
     * @param flow   流程定义
     * @param taskId 任务id
     * @return 上级任务列表
     * @author JonnyJiang
     * @date 2019/12/10 16:27
     */

    List<TaskInstance> listParentTasks(Flow flow, String taskId);

    /**
     * 获取主办人id
     *
     * @param flowId   流程id
     * @param nodeCode 节点标识
     * @return 主办人id
     * @author JonnyJiang
     * @date 2019/12/20 14:32
     */

    String getNodeHostId(String flowId, String nodeCode);

    /**
     * 获取主办人
     *
     * @param flowId   流程id
     * @param nodeCode 节点标识
     * @return 主办人
     * @author JonnyJiang
     * @date 2019/12/20 14:47
     */

    SysUserDO getNodeHost(String flowId, String nodeCode);

    /**
     * 获取主办意见
     *
     * @param flowId   流程id
     * @param nodeCode 节点标识
     * @return 主办意见
     * @author JonnyJiang
     * @date 2019/12/20 14:53
     */

    String getNodeHostOpinion(String flowId, String nodeCode);

    /**
     * 获取产生流程正在办理任务的节点列表
     *
     * @param flowId 任务id
     * @return 被驳回的节点列表
     * @author JonnyJiang
     * @date 2019/12/25 14:12
     */

    List<Node> getRejectFromNode(String flowId);

    /**
     * 获取用户身份类型
     *
     * @param node          节点
     * @param taskId        任务id
     * @param currentUserId 用户id
     * @return 用户身份类型
     * @author JonnyJiang
     * @date 2019/12/27 9:13
     */

    UserType getUserType(Node node, String taskId, String currentUserId);

    /**
     * 获取用户身份类型
     *
     * @param node          节点
     * @param currentUserId 用户id
     * @param deliverNode   转交节点
     * @return 用户身份类型
     * @author JonnyJiang
     * @date 2019/12/27 9:14
     */

    UserType getUserType(Node node, String currentUserId, DeliverNode deliverNode);

    /**
     * 获取表单字段值
     *
     * @param tableName   表名
     * @param columnName  列名
     * @param formIdName  业务标识列名
     * @param businessKey 业务标识
     * @return 表单字段值
     * @author JonnyJiang
     * @date 2020/1/13 16:59
     */

    Object getFormValue(String tableName, String columnName, String formIdName, String businessKey);

    /**
     * 获取生成任务的DeliverInfoId列表
     *
     * @param taskInstances 任务实例列表
     * @return 获取生成任务的DeliverInfoId 列表
     * @author JonnyJiang
     * @date 2020/4/16 15:03
     */

    Map<String, Object> listTaskVariableValues(List<TaskInstance> taskInstances, TaskVariableName taskVariableName);

    /**
     * 获取流程变量
     *
     * @param taskId           任务id
     * @param taskVariableName 变量名称
     * @return 流程值
     * @author JonnyJiang
     * @date 2020/4/16 15:34
     */

    Object getTaskVariable(String taskId, TaskVariableName taskVariableName);

    /**
     * 获取流程实例
     *
     * @param taskId 任务id
     * @return 流程实例
     * @author JonnyJiang
     * @date 2020/5/9 11:25
     */

    WfiFlowDO getWfiFlowByTaskId(String taskId);

    /**
     * 获取用户已关注工作
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/5/26 17:04
     */
    R listTaskFocused(Map<String, String> params);

    /**
     * 判断用户是否关注此工作
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/5/27 9:05
     */
    boolean isFocused(Map<String, String> params);

    /**
     * 获取有查询权限的流程定义列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/5/28 8:12
     */
    List<WfdFlowDO> getFlowsByQueryAuth();

    /**
     * 获取有监控权限的流程定义列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/5/28 8:12
     */
    List<WfdFlowDO> getFlowsByMonitorAuth();

    /**
     * 补充任务评论节点信息
     *
     * @param flowId      流程实例id
     * @param wfiComments 评论列表
     * @author JonnyJiang
     * @date 2020/6/3 10:23
     */

    void appendNodeInfo(String flowId, List<WfiCommentDO> wfiComments);

    /**
     * 获取节点id
     *
     * @param taskId 任务id
     * @return 节点id
     * @author JonnyJiang
     * @date 2020/6/23 18:32
     */

    String getNodeIdByTaskId(String taskId);

    OperationType getOperationType(Boolean finished, UserType userType, Node node, String taskId, String owner, String assignee, WfiDeliverTasks wfiDeliverTasks);

    /**
     * 获取操作类型
     *
     * @param flowId 流程实例id
     * @param taskId 任务id
     * @return 操作类型
     * @author JonnyJiang
     * @date 2020/6/30 10:31
     */

    OperationType getOperationType(String flowId, String taskId);

    /**
     * 获取用户身份
     *
     * @param flowId          流程实例id
     * @param taskId          任务id
     * @param isFinished      流程实例id
     * @param userId          用户id
     * @param node            节点
     * @param deliverNodeFrom 来自转交节点
     * @return 用户身份
     * @author JonnyJiang
     * @date 2020/7/1 19:34
     */

    UserType getUserType(String flowId, String taskId, Boolean isFinished, String userId, Node node, DeliverNode deliverNodeFrom);

    /**
     * 是否存在流程实例
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @return java.lang.Boolean
     * @author JonnyJiang
     * @date 2020/7/9 16:39
     */

    Boolean existFlowInstance(String flowCode, String businessKey);

    TaskRejectTo getTaskRejectToByRejectToFirst(WfiFlowDO wfiFlow, Flow flow, Node node);

    Collection<List<TaskRejectTo>> listTaskRejectToByRejectToSpecified(WfiFlowDO wfiFlow, Flow flow, Node node, String taskId);

    List<TaskRejectTo> listTaskRejectToByRejectToLast(WfiFlowDO wfiFlow, Flow flow, Node node);

    /**
     * 获取可流转的预设路径列表
     *
     * @param taskId 任务ID
     * @return 可流转的预设路径列表
     * @author JonnyJiang
     * @date 2020/9/23 23:09
     */

    List<PresetInfo> listAvailablePresetInfosByTaskId(String taskId);

    /**
     * 获取将指定节点设定为预设节点的最新流程预设信息
     * @param wfiFlow 流程实例
     * @param node 指定节点
     * @return 流程预设信息
     */
    WfiRoutePresetDO getLatestWfiRoutePreset(WfiFlowDO wfiFlow, Node node);

    /**
     * 获取流程实例节点列表，含办理人
     * @param wfiFlowId 流程实例id
     * @return 流程实例节点列表
     */
    List<WfiNodeVO> listWfiNodesByFlowId(String wfiFlowId);

    /**
     * 获取流程实例节点列表，含办理人
     * @param flowCode 流程标识
     * @param businessKey 业务标识
     * @return 流程实例节点列表
     */
    List<WfiNodeVO> listWfiNodesByBusinessKey(String flowCode, String businessKey);

    /**
     * 是否允许预设流程
     * @param wfiFlow 流程实例
     * @param node 当前节点
     * @param taskId 任务id
     * @return 是否允许预设流程
     * @author JonnyJiang
     * @date 2021/7/3 13:49
     */

    boolean allowPresetRoute(WfiFlowDO wfiFlow, Node node, String taskId);
    /** 
     * 流程是否有预设信息
     * @param flowId	流程实例id
     * @return 流程是否有预设信息
     * @author JonnyJiang
     * @date 2021/8/22 17:35
     */
    boolean flowPreseted(String flowId);
    /**
     * 流程是否有预设信息
     * @paramformId	发起流程formId
     * @return 流程是否有预设信息
     * @author 张招君
     * @date 2021/12/3 09:20
     */
    R addReviewFile(String formId);

    /***
     * @description:文件审查流程结束函数
     * @params: 设置文件审查
     * @return: com.csicit.ace.common.utils.server.R
     * @author: Zhangzhaojun
     * @time: 2021/12/3 17:44
     */
    R setReview(String formId);
}