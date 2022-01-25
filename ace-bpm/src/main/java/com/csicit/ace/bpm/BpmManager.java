package com.csicit.ace.bpm;

import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.RejectInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 流程管理
 *
 * @author JonnyJiang
 * @date 2019/6/24 8:10
 */
@Transactional
public interface BpmManager {
    String DB_VERSION = "1.0.15";
    String ENGINE_VERSION = "7.1.81";

    /**
     * 创建流程实例
     *
     * @param flowId      流程id
     * @param businessKey 业务标识
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/9/5 15:52
     */

    FlowInstance createFlowInstanceById(String flowId, String businessKey);

    /**
     * 创建流程实例
     *
     * @param flowId      流程id
     * @param businessKey 业务标识
     * @param variables   变量
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/10/24 8:30
     */

    FlowInstance createFlowInstanceById(String flowId, String businessKey, Map<String, Object> variables);

    /**
     * 创建流程实例
     *
     * @param flowId      流程id
     * @param businessKey 业务标识
     * @param initiator   创建人
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/6/24 14:10
     */

    FlowInstance createFlowInstanceById(String flowId, String businessKey, SysUserDO initiator);

    /**
     * 创建流程实例
     *
     * @param flowId      流程id
     * @param businessKey 业务标识
     * @param initiator   创建人
     * @param variables   变量
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/10/24 8:30
     */

    FlowInstance createFlowInstanceById(String flowId, String businessKey, SysUserDO initiator, Map<String, Object> variables);

    /**
     * 创建流程实例
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/6/24 14:10
     */

    FlowInstance createFlowInstanceByCode(String code, String businessKey);

    /**
     * 创建流程实例
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @param variables   变量
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/10/24 8:31
     */

    FlowInstance createFlowInstanceByCode(String code, String businessKey, Map<String, Object> variables);

    /**
     * 创建流程实例
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @param initiator   创建人
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/6/24 14:10
     */

    FlowInstance createFlowInstanceByCode(String code, String businessKey, SysUserDO initiator);

    /**
     * 创建流程实例
     *
     * @param code        流程标识
     * @param businessKey 业务标识
     * @param initiator   创建人
     * @param variables   变量
     * @return 流程实例
     * @author JonnyJiang
     * @date 2019/10/24 8:32
     */

    FlowInstance createFlowInstanceByCode(String code, String businessKey, SysUserDO initiator, Map<String, Object> variables);

    /**
     * 转交工作
     *
     * @param task 任务
     * @author JonnyJiang
     * @date 2019/9/10 10:40
     */

    void deliverWork(TaskInstance task);

    /**
     * 转交工作
     *
     * @param task        任务
     * @param currentUser 当前用户
     * @author JonnyJiang
     * @date 2019/9/10 10:39
     */

    void deliverWork(TaskInstance task, SysUserDO currentUser);

    /**
     * 转交工作
     *
     * @param taskId 任务id
     * @author JonnyJiang
     * @date 2019/9/10 10:40
     */

    void deliverWork(String taskId);

    /**
     * 转交工作
     *
     * @param taskId      任务id
     * @param currentUser 操作用户
     * @author JonnyJiang
     * @date 2019/9/10 10:35
     */

    void deliverWork(String taskId, SysUserDO currentUser);

    /**
     * 转交工作
     *
     * @param task      任务实例
     * @param variables 变量
     * @author JonnyJiang
     * @date 2019/9/10 10:40
     */

    void deliverWork(TaskInstance task, Map<String, Object> variables);

    /**
     * 转交工作
     *
     * @param task        任务实例
     * @param variables   变量
     * @param currentUser 操作用户
     * @author JonnyJiang
     * @date 2019/9/10 10:39
     */

    void deliverWork(TaskInstance task, Map<String, Object> variables, SysUserDO currentUser);

    /**
     * 转交工作
     *
     * @param taskId    任务id
     * @param variables 变量
     * @author JonnyJiang
     * @date 2019/9/10 10:40
     */

    void deliverWork(String taskId, Map<String, Object> variables);

    /**
     * 转交工作
     *
     * @param taskId      任务id
     * @param variables   变量
     * @param currentUser 操作用户
     * @author JonnyJiang
     * @date 2019/9/10 10:39
     */

    void deliverWork(String taskId, Map<String, Object> variables, SysUserDO currentUser);

    /**
     * 转交工作
     *
     * @param deliverInfo 转交信息
     * @author JonnyJiang
     * @date 2019/9/25 8:07
     */

    void deliverWork(DeliverInfo deliverInfo);

    /**
     * 删除流程实例
     *
     * @param flowId       流程id
     * @param businessKeys 业务标识
     * @param deleteReason 删除原因
     * @author JonnyJiang
     * @date 2019/9/5 16:58
     */

    void deleteFlowInstanceByFlowId(String flowId, List<String> businessKeys, String deleteReason);

    /**
     * 删除流程实例
     *
     * @param code         流程id
     * @param businessKeys 业务标识
     * @param deleteReason 删除原因
     * @author JonnyJiang
     * @date 2019/9/5 16:58
     */

    void deleteFlowInstanceByCode(String code, List<String> businessKeys, String deleteReason);

    /**
     * 删除流程实例
     *
     * @param flowInstanceId 流程实例id
     * @param deleteReason   删除原因
     * @author JonnyJiang
     * @date 2019/9/5 17:02
     */

    void deleteFlowInstanceById(String flowInstanceId, String deleteReason);

    /**
     * 删除流程实例
     *
     * @param code         流程标识
     * @param businessKey  业务标识
     * @param deleteReason 删除原因
     * @author JonnyJiang
     * @date 2019/11/4 8:59
     */

    void deleteFlowInstanceByBusinessKey(String code, String businessKey, String deleteReason);

    /**
     * 强制删除流程实例
     *
     * @param flowCode     流程标识
     * @param businessKeys 业务标识列表
     * @param deleteReason 删除原因
     * @author JonnyJiang
     * @date 2020/6/9 9:03
     */

    void forceDeleteFlowInstanceByBusinessKey(String flowCode, List<String> businessKeys, String deleteReason);

    /**
     * 强制删除流程实例
     *
     * @param flowCode     流程标识
     * @param businessKeys 业务标识列表
     * @param deleteReason 删除原因
     * @author JonnyJiang
     * @date 2020/6/9 9:03
     */

    void forceDeleteFlowInstanceByBusinessKey(String flowCode, String[] businessKeys, String deleteReason);

    /**
     * 发布流程
     * 版本号默认为1，版本生效日期默认为后一天
     *
     * @param id 流程定义id
     * @author JonnyJiang
     * @date 2019/9/16 20:08
     */

    void deploy(String id);

    /**
     * 发布流程
     *
     * @param id        流程定义id
     * @param startDate 版本生效日期
     * @author JonnyJiang
     * @date 2019/9/16 20:08
     */

    void deploy(String id, LocalDateTime startDate);

    /**
     * 发布流程
     *
     * @param id        流程定义id
     * @param version   流程定义版本号
     * @param startDate 版本生效日期
     * @author JonnyJiang
     * @date 2019/7/15 9:59
     */

    void deploy(String id, Integer version, LocalDateTime startDate);

    /**
     * 撤销发布流程
     *
     * @param id      流程定义id
     * @param version 流程定义版本号
     * @author JonnyJiang
     * @date 2019/9/16 19:53
     */

    void revokeDeploy(String id, Integer version);

    /**
     * 接收任务
     *
     * @param taskId 任务id
     * @author JonnyJiang
     * @date 2019/9/11 10:02
     */

    void claim(String taskId);

    /**
     * 接收任务
     *
     * @param taskId   任务id
     * @param userId   接收人
     * @param realName 用户名称
     * @author JonnyJiang
     * @date 2019/9/11 10:03
     */

    void claim(String taskId, String userId, String realName);

    /**
     * 撤销接收任务
     *
     * @param taskId 任务id
     * @author JonnyJiang
     * @date 2019/10/14 20:02
     */

    void revokeClaim(String taskId);

    /**
     * 委托工作
     *
     * @param taskId 任务id
     * @param userId 用户id
     * @author JonnyJiang
     * @date 2019/9/16 18:48
     */

    void delegateWork(String taskId, String userId);

    /**
     * 获取流程定义xml
     *
     * @param flow 流程
     * @return 流程定义xml
     * @author JonnyJiang
     * @date 2019/9/17 20:43
     */

    String getBpmnXml(Flow flow);

    /**
     * 驳回工作
     *
     * @param rejectInfo 驳回信息
     * @author JonnyJiang
     * @date 2019/12/6 11:39
     */

    void rejectWork(RejectInfo rejectInfo);

    
    /**
     * @Author zhangzhaojun
     * @Description //TODO 根据编制人编制成任务后进行根据taskId创建待办提醒到数据表已备扫描
     * @Date 10:29 2021/9/22
     * @Param 
     * @return 
     **/

    void addUrgeTaskDO(String flowId,String nodeId,String taskId) throws ParseException;


    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 17:18 2021/8/31
     * @Param UrgeTaskVO
     **/
    void urgeTask();


    /**
     * @Author zhangzhaojun
     * @Description //TODO 待办完成后或达到指定提醒次数后，进行删除待办提醒表的操作
     * @Date 10:36 2021/9/22
     * @Param
     * @return
     **/

    void removeUrgeTask(String taskId);
    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:30 2021/9/22
     * @Param
     * @return
     **/
    WfiFlowDO getWfiFlowById(String id);

    /**
     * @Author zhangzhaojun
     * @Description //TODO 判断当前时间是否在该待办任务的提醒时间内
     * @Date 10:36 2021/9/24
     * @Param
     * @return
     **/

    boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime);
    /**
     * 更新工作文号
     *
     * @param flowCode    流程标识
     * @param businessKey 业务标识
     * @param flowNo      工作文号
     * @author JonnyJiang
     * @date 2019/12/31 10:08
     */

    void updateFlowNo(String flowCode, String businessKey, String flowNo);

    /**
     * 删除无效流程实例
     *
     * @author JonnyJiang
     * @date 2020/1/2 16:36
     */

    void deleteInvalidFlowInstances();

    /**
     * 还原实例
     *
     * @param flowInstanceId 任务id
     * @param taskId         流程实例id
     * @author JonnyJiang
     * @date 2020/1/20 15:10
     */

    void recoverFlowInstance(String flowInstanceId, String taskId);

    /**
     * 关注工作 或 取消关注
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/5/27 8:53
     */
    boolean focusWork(Map<String, Object> params);

    /**
     * 更新用户关注的工作的最新一次阅读时间、标志
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/16 10:38
     */
    boolean updateFocusWorkReadTime(Map<String, String> params);

    /**
     * 加签
     *
     * @param taskId  任务id
     * @param userIds 用户id列表
     * @author JonnyJiang
     * @date 2020/6/2 14:54
     */

    void invite(String taskId, List<String> userIds);

    /**
     * 检查是否可以办结
     *
     * @param userType 用户身份
     * @param node     节点
     * @param task     任务
     * @author JonnyJiang
     * @date 2020/6/24 14:17
     */

    void checkAllowDeliver(UserType userType, Node node, TaskInstance task);

    /**
     * 刷新待办任务
     *
     * @param flowInstanceId 流程实例id
     * @param flow           流程定义
     * @author JonnyJiang
     * @date 2020/6/28 14:31
     */

    void refreshTaskPending(String flowInstanceId, Flow flow);

    /**
     * 刷新待办任务
     *
     * @param flowInstanceId 流程实例id
     * @param flow           流程定义
     * @author JonnyJiang
     * @date 2020/6/28 14:31
     */

    void resolveTaskPending(Collection<WfiTaskPendingDO> wfiTaskPendings, String flowInstanceId, Flow flow);

    /**
     * 删除待办任务对应的流程实例
     *
     * @param wfiFlow        流程实例
     * @param flow           流程
     * @param wfiTaskPending 待办任务
     * @param hasAdminAuth   是否有监控权限
     * @param currentUserId  当前用户
     * @param deleteReason   删除原因
     * @return void
     * @author JonnyJiang
     * @date 2020/7/2 10:01
     */

    void deleteFlowInstanceByTaskPending(WfiFlowDO wfiFlow, Flow flow, WfiTaskPendingDO wfiTaskPending, Boolean hasAdminAuth, String currentUserId, String deleteReason);
    /***
     * @description: 创建审查流程并将数据存入到表中
     * @params: SysReviewFile
     * @return: void
     * @author: Zhangzhaojun
     * @time: 2021/12/6 11:08
     */
    void createReviewFile(HashMap<String,String> params);
}