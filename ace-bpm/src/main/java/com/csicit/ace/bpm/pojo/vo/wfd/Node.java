package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.csicit.ace.bpm.exception.NodeFreeStepNotFoundByIdException;
import com.csicit.ace.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class Node implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 紧急类型
     */
    private Integer urgentType;
    /**
     * 是否继承流程催办时间
     */
    private Integer extendsFlowUrge;
    /**
     * 步骤名称
     */
    private String name;

    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 节点标识
     */
    private String code;

    /**
     * 事件监听器
     */
    private String eventListener;

    /********startNode start********/
    /**
     * 发起权限id
     */
    private String initAuthId;
    /**
     * 是否启用API发起
     */
    private Integer enableApiStart;
    /**
     * 是否允许手动发起
     */
    private Integer allowManualStart;
    /**
     * 是否启用计时器发起
     */
    private Integer enableTimerStart;
    /**
     * 是否启用消息发起
     */
    private Integer enableMessageStart;
    /**
     * 定时发起Cron表达式
     */
    private String startTimerCron;
    /**
     * 消息名称
     */
    private String messageName;
    /**
     * 消息处理类
     */
    private String messageProcessingClass;

    /********startNode end********/

    /********manualNode start********/

    /**
     * 步骤说明
     */
    private String remark;
    /**
     * 主办模式
     */
    private Integer hostMode;
    /**
     * 强制办理顺序(主办模式为任何人都可以主办时生效)
     */
    private Integer forceSequence;
    /**
     * 默认主办人
     */
    private String hostId;
    /**
     * 继续办理
     * 确定主办人后，其他人可以作为协办人员继续办理
     */
    private Integer enableAssistAhd;
    /**
     * 允许结转
     */
    private Integer allowPassMode;
    /**
     * 额外允许结转规则
     */
    private String allowPassRuleId;
    /**
     * 额外允许结转规则修饰符
     */
    private Integer allowPassRuleModifier;
    /**
     * 主办人办结后，是否允许协办人继续办理
     */
    private Integer allowPassAfterHostCompleted;
    /**
     * 前序签署人(曾用名:指定签署人)
     */
    private List<String> waitForPassUsers;
    /**
     * 等待已办理用户达到指定数量
     */
    private Integer waitForPassCount;
    /**
     * 等待已办理用户达到指定百分比
     */
    private Integer waitForPassPercent;
    /**
     * 选择工作结果的提示语
     */
    private String workResultPrompt;
    /**
     * 工作结果选项
     */
    private String[] workResultOptions;
    /**
     * 最少选择结果
     */
    private Integer minSelResult;
    /**
     * 最多选择结果
     */
    private Integer maxSelResult;
    /**
     * 启用会签
     */
    private Integer enableVote;
    /**
     * 匿名会签
     */
    private Integer voteViewMode;
    /**
     * 允许主办人加签
     */
    private Integer allowHostInvite;
    /**
     * 允许协办人加签
     */
    private Integer allowAssistInvite;
    /**
     * 签署意见
     * 0：允许签署（可签可不签） 1：必须签署  2：禁止签署
     */
    private Integer remarkMode;
    /**
     * 是否允许主办人发送查看
     */
    private Integer allowHostAuthorizeViewer;
    /**
     * 是否允许协办人发送查看
     */
    private Integer allowAssitAuthorizeViewer;
    /**
     * 是否允许主办人删除
     */
    private Integer allowHostDelete;
    /**
     * 是否允许协办人删除
     */
    private Integer allowAssitDelete;
    /**
     * 允许将此工作委托给其他人员办理  0 否 1 是
     */
    private Integer allowPassToAgent;
    /**
     * 是否多实例并行
     */
    private Integer multiInstanceMode;
    /**
     * 参与人生成规则
     */
    private String participateRule;
    /**
     * 是否允许手动选人
     */
    private Integer enableManualSelParticipant;
    /**
     * 是否自动选中满足条件的人
     */
    private Integer enableAutoSelParticipant;
    /**
     * 单人办理
     */
    private Integer enableHostOnly;
    /**
     * 是否允许驳回
     */
    private Integer enableReject;
    /**
     * 默认驳回到
     */
    private Integer rejectTo;
    /**
     * 默认驳回到指定步骤
     */
    private List<String> rejectToStep = new ArrayList<>();
    /**
     * 分支处理（驳回）
     */
    private Integer enableRejectHaltBranches;
    /**
     * 重新提交（驳回）
     */
    private Integer enableRejectedReflow;
    /**
     * 允许修改驳回设置
     */
    private Integer allowConfigReject;
    /**
     * 允许撤回
     */
    private Integer allowDrawBack;
    /**
     * 主办人办结前可执行的表单操作
     */
    private List<String> operationsHostBd;
    /**
     * 主办人办结后可执行的表单操作
     */
    private List<String> operationsHostAd;
    /**
     * 协办人办结前可执行的表单操作
     */
    private List<String> operationsAssistBd;
    /**
     * 协办人办结后可执行的表单操作
     */
    private List<String> operationsAssistAd;
    /**
     * 一般工作办理时限
     */
    private Integer timeLimitGeneral;
    /**
     * 加急工作办理时限
     */
    private Integer timeLimitUrgent;
    /**
     * 特急工作办理时限
     */
    private Integer timeLimitExtraUrgent;
    /**
     * 一般工作办理时限单位
     */
    private String timeLimitUnitG;
    /**
     * 加急工作办理时限单位
     */
    private String timeLimitUnitU;
    /**
     * 特急工作办理时限单位
     */
    private String timeLimitUnitEu;
    /**
     * 超时处理方式
     */
    private String overTimeMode;
    /**
     * 催办次数
     */
    private Integer overTimeRemindTime;
    /**
     * 催办间隔
     */
    private Integer overTimeRemindIntv;
    /**
     * 催办消息类别
     */
    private String overTimeMsgChannel;
    /**
     * 催办处理频道
     */
    private String overTimeMsgType;
    /**
     * 催办信息模板
     */
    private String overTimeMsgTemplateCode;
    /**
     * 流入消息模板
     */
    private String flowInMsgTemplate;
    /**
     * 流出消息模板
     */
    private String flowOutMsgTemplate;
    /**
     * 流入消息发送范围
     */
    private List<String> flowInMsgSendTo;
    /**
     * 流出消息发送范围
     */
    private String flowOutMsgSendTo;
    /**
     * 流入消息类别
     */
    private String flowInMsgChannel;
    /**
     * 流出消息类别
     */
    private String flowOutMsgChannel;
    /**
     * 手动选人-分支流入
     */
    private Integer flowInSelParticipantMode;
    /**
     * 手动选人-指定步骤
     */
    private String flowInNodeSelParticipant;
    /**
     * 流入前消息
     */
    private String flowInBeforeMessage;
    /**
     * 转存参数
     */
    private String flowInUnloadingParam;
    /**
     * 消息处理类
     */
    private String flowInMessageProcessingClass;
    /**
     * 流出后消息
     */
    private String flowOutAfterMessage;
    /**
     * 转存参数
     */
    private String flowOutUnloadingParam;
    /**
     * 消息处理类
     */
    private String flowOutMessageProcessingClass;

    /**
     * 任务节点消息模板标识
     */
    private String flowInMsgTemplateCode;

    /**
     * 消息节点模板参数
     */
    private List<String> variableFieldInMsg;

    /**
     * 是否允许预设流转路径
     */
    private Integer allowPresetRoute;

    /**
     * 必须预设流转路径
     */
    private Integer mustPresetRoute;
    /**
     * 必须预设流转路径办理人
     */
    private Integer mustPresetRouteUser;

    /**
     * 是否允许后续节点修改流转路径
     */
    private Integer allowModifyPresetRouteByDescendant;

    /********freeNode start********/

    /**
     * 是否允许预先设置自由流步骤
     */
    private Integer allowPresetFreeRoute;

    /**
     * 是否允许中途变更预设自由流步骤
     */
    private Integer allowModifyPresetedFreeRoute;

    /**
     * 自由流步骤
     */
    private List<NodeFreeStep> nodeFreeSteps = new ArrayList<>();

    /********freeNode end********/

    /********manualNode end********/

    /********subflowNode start********/

    /**
     * 子流程主键
     */
    private String subFlowId;
    /**
     * 结果变量名
     */
    private String resultVarName;

    /********subflowNode end********/

    /********endNode start********/

    /**
     * 结果值
     */
    private String resultValue;

    /**
     * 是否保存结果值
     */
    private Integer saveResultValue;

    /********endNode end********/


    /********Node start********/

    /**
     * 流入模式
     * 仅限subFlowNode/manualNode
     */
    private Integer flowInMode;
    /**
     * 流出模式
     * 仅限subFlowNode/manualNode
     */
    private Integer flowOutMode;
    /**
     * 流出模式为2时，允许多选 0不多选，1多选
     */
    private Integer allowMultipleSelection;

    /********Node end********/

    /**
     * 流程节点事件
     */
    private List<NodeEvent> events = new ArrayList<>();

    /**
     * 是否预设了流转路径
     */
    private Integer presetedRoute;

    /**
     * 是否是预设的流转路径上的节点
     */
    private Integer presetedNode;

    /**
     * 预设的经办人列表
     */
    private List<NodePresetUser> nodePresetUsers;

    /**
     * 所属流程
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Flow flow;

    /**
     * 流入节点的关联
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient List<Link> flowInLinks = new ArrayList<>();

    /**
     * 流出节点的关联
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient List<Link> flowOutLinks = new ArrayList<>();

    public List<Link> getFlowInLinks() {
        return flowInLinks;
    }

    public List<Link> getFlowOutLinks() {
        return flowOutLinks;
    }

    public String getId() {
        return id;
    }

    public void setUrgentType(Integer urgentType){
        this.urgentType = urgentType;
    }

    public Integer getUrgentType() {return urgentType;}

    public void setExtendsFlowUrge(Integer extendsFlowUrge){
        this.extendsFlowUrge = extendsFlowUrge;
    }

    public Integer getExtendsFlowUrge() {return extendsFlowUrge;}

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getHostMode() {
        return hostMode;
    }

    public void setHostMode(Integer hostMode) {
        this.hostMode = hostMode;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Integer getEnableAssistAhd() {
        return enableAssistAhd;
    }

    public void setEnableAssistAhd(Integer enableAssistAhd) {
        this.enableAssistAhd = enableAssistAhd;
    }

    public Integer getAllowPassMode() {
        return allowPassMode;
    }

    public void setAllowPassMode(Integer allowPassMode) {
        this.allowPassMode = allowPassMode;
    }

    public String getAllowPassRuleId() {
        return allowPassRuleId;
    }

    public void setAllowPassRuleId(String allowPassRuleId) {
        this.allowPassRuleId = allowPassRuleId;
    }

    public Integer getAllowPassRuleModifier() {
        return allowPassRuleModifier;
    }

    public void setAllowPassRuleModifier(Integer allowPassRuleModifier) {
        this.allowPassRuleModifier = allowPassRuleModifier;
    }

    public List<String> getWaitForPassUsers() {
        if (waitForPassUsers == null) {
            waitForPassUsers = new ArrayList<>();
        }
        return waitForPassUsers;
    }

    public void setWaitForPassUsers(List<String> waitForPassUsers) {
        this.waitForPassUsers = waitForPassUsers;
    }

    public Integer getWaitForPassCount() {
        return waitForPassCount;
    }

    public void setWaitForPassCount(Integer waitForPassCount) {
        this.waitForPassCount = waitForPassCount;
    }

    public Integer getWaitForPassPercent() {
        return waitForPassPercent;
    }

    public void setWaitForPassPercent(Integer waitForPassPercent) {
        this.waitForPassPercent = waitForPassPercent;
    }

    public String getWorkResultPrompt() {
        return workResultPrompt;
    }

    public void setWorkResultPrompt(String workResultPrompt) {
        this.workResultPrompt = workResultPrompt;
    }

    public String[] getWorkResultOptions() {
        if (workResultOptions == null) {
            workResultOptions = new String[0];
        }
        return workResultOptions;
    }

    public String getOverTimeMsgType() {
        return overTimeMsgType;
    }

    public void setOverTimeMsgType(String overTimeMsgType) {
        this.overTimeMsgType = overTimeMsgType;
    }

    public String getOverTimeMsgTemplateCode() {
        return overTimeMsgTemplateCode;
    }

    public void setOverTimeMsgTemplateCode(String overTimeMsgTemplateCode) {
        this.overTimeMsgTemplateCode = overTimeMsgTemplateCode;
    }

    public void setWorkResultOptions(String[] workResultOptions) {
        this.workResultOptions = workResultOptions;
    }

    public Integer getMinSelResult() {
        return minSelResult;
    }

    public void setMinSelResult(Integer minSelResult) {
        this.minSelResult = minSelResult;
    }

    public Integer getMaxSelResult() {
        return maxSelResult;
    }

    public void setMaxSelResult(Integer maxSelResult) {
        this.maxSelResult = maxSelResult;
    }

    public Integer getEnableVote() {
        return enableVote;
    }

    public void setEnableVote(Integer enableVote) {
        this.enableVote = enableVote;
    }

    public Integer getVoteViewMode() {
        return voteViewMode;
    }

    public void setVoteViewMode(Integer voteViewMode) {
        this.voteViewMode = voteViewMode;
    }

    public Integer getAllowHostInvite() {
        return allowHostInvite;
    }

    public void setAllowHostInvite(Integer allowHostInvite) {
        this.allowHostInvite = allowHostInvite;
    }

    public Integer getAllowAssistInvite() {
        return allowAssistInvite;
    }

    public void setAllowAssistInvite(Integer allowAssistInvite) {
        this.allowAssistInvite = allowAssistInvite;
    }

    public Integer getRemarkMode() {
        return remarkMode;
    }

    public void setRemarkMode(Integer remarkMode) {
        this.remarkMode = remarkMode;
    }

    public Integer getAllowHostAuthorizeViewer() {
        return allowHostAuthorizeViewer;
    }

    public void setAllowHostAuthorizeViewer(Integer allowHostAuthorizeViewer) {
        this.allowHostAuthorizeViewer = allowHostAuthorizeViewer;
    }

    public Integer getAllowAssitAuthorizeViewer() {
        return allowAssitAuthorizeViewer;
    }

    public void setAllowAssitAuthorizeViewer(Integer allowAssitAuthorizeViewer) {
        this.allowAssitAuthorizeViewer = allowAssitAuthorizeViewer;
    }

    public Integer getAllowHostDelete() {
        return allowHostDelete;
    }

    public void setAllowHostDelete(Integer allowHostDelete) {
        this.allowHostDelete = allowHostDelete;
    }

    public Integer getAllowAssitDelete() {
        return allowAssitDelete;
    }

    public void setAllowAssitDelete(Integer allowAssitDelete) {
        this.allowAssitDelete = allowAssitDelete;
    }

    public Integer getAllowPassToAgent() {
        return allowPassToAgent;
    }

    public void setAllowPassToAgent(Integer allowPassToAgent) {
        this.allowPassToAgent = allowPassToAgent;
    }

    public Integer getMultiInstanceMode() {
        return multiInstanceMode;
    }

    public void setMultiInstanceMode(Integer multiInstanceMode) {
        this.multiInstanceMode = multiInstanceMode;
    }

    public String getParticipateRule() {
        return participateRule;
    }

    public void setParticipateRule(String participateRule) {
        this.participateRule = participateRule;
    }

    public Integer getEnableManualSelParticipant() {
        return enableManualSelParticipant;
    }

    public void setEnableManualSelParticipant(Integer enableManualSelParticipant) {
        this.enableManualSelParticipant = enableManualSelParticipant;
    }

    public Integer getEnableHostOnly() {
        return enableHostOnly;
    }

    public void setEnableHostOnly(Integer enableHostOnly) {
        this.enableHostOnly = enableHostOnly;
    }

    public Integer getEnableReject() {
        return enableReject;
    }

    public void setEnableReject(Integer enableReject) {
        this.enableReject = enableReject;
    }

    public Integer getRejectTo() {
        return rejectTo;
    }

    public void setRejectTo(Integer rejectTo) {
        this.rejectTo = rejectTo;
    }

    public void setEvents(List<NodeEvent> events) {
        this.events = events;
    }

    public List<NodeEvent> getEvents() {
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }

    public void setRejectToStep(List<String> rejectToStep) {
        this.rejectToStep = rejectToStep;
    }

    public List<String> getRejectToStep() {
        if (rejectToStep == null) {
            rejectToStep = new ArrayList<>();
        }
        return rejectToStep;
    }

    public Integer getEnableRejectHaltBranches() {
        return enableRejectHaltBranches;
    }

    public void setEnableRejectHaltBranches(Integer enableRejectHaltBranches) {
        this.enableRejectHaltBranches = enableRejectHaltBranches;
    }

    public Integer getEnableRejectedReflow() {
        return enableRejectedReflow;
    }

    public void setEnableRejectedReflow(Integer enableRejectedReflow) {
        this.enableRejectedReflow = enableRejectedReflow;
    }

    public Integer getAllowConfigReject() {
        return allowConfigReject;
    }

    public void setAllowConfigReject(Integer allowConfigReject) {
        this.allowConfigReject = allowConfigReject;
    }

    public Integer getAllowDrawBack() {
        return allowDrawBack;
    }

    public void setAllowDrawBack(Integer allowDrawBack) {
        this.allowDrawBack = allowDrawBack;
    }

    public List<String> getOperationsHostBd() {
        return operationsHostBd;
    }

    public void setOperationsHostBd(List<String> operationsHostBd) {
        this.operationsHostBd = operationsHostBd;
    }

    public List<String> getOperationsHostAd() {
        return operationsHostAd;
    }

    public void setOperationsHostAd(List<String> operationsHostAd) {
        this.operationsHostAd = operationsHostAd;
    }

    public List<String> getOperationsAssistBd() {
        return operationsAssistBd;
    }

    public void setOperationsAssistBd(List<String> operationsAssistBd) {
        this.operationsAssistBd = operationsAssistBd;
    }

    public List<String> getOperationsAssistAd() {
        return operationsAssistAd;
    }

    public void setOperationsAssistAd(List<String> operationsAssistAd) {
        this.operationsAssistAd = operationsAssistAd;
    }

    public Integer getTimeLimitGeneral() {
        return timeLimitGeneral;
    }

    public void setTimeLimitGeneral(Integer timeLimitGeneral) {
        this.timeLimitGeneral = timeLimitGeneral;
    }

    public Integer getTimeLimitUrgent() {
        return timeLimitUrgent;
    }

    public void setTimeLimitUrgent(Integer timeLimitUrgent) {
        this.timeLimitUrgent = timeLimitUrgent;
    }

    public Integer getTimeLimitExtraUrgent() {
        return timeLimitExtraUrgent;
    }

    public void setTimeLimitExtraUrgent(Integer timeLimitExtraUrgent) {
        this.timeLimitExtraUrgent = timeLimitExtraUrgent;
    }

    public String getTimeLimitUnitG() {
        return timeLimitUnitG;
    }

    public void setTimeLimitUnitG(String timeLimitUnitG) {
        this.timeLimitUnitG = timeLimitUnitG;
    }

    public String getTimeLimitUnitU() {
        return timeLimitUnitU;
    }

    public void setTimeLimitUnitU(String timeLimitUnitU) {
        this.timeLimitUnitU = timeLimitUnitU;
    }

    public String getTimeLimtUnitEu() {
        return timeLimitUnitEu;
    }

    public void setTimeLimtUnitEu(String timeLimtUnitEu) {
        this.timeLimitUnitEu = timeLimtUnitEu;
    }

    public String getOverTimeMode() {
        return overTimeMode;
    }

    public void setOverTimeMode(String overTimeMode) {
        this.overTimeMode = overTimeMode;
    }

    public Integer getOverTimeRemindTime() {
        return overTimeRemindTime;
    }

    public void setOverTimeRemindTime(Integer overTimeRemindTime) {
        this.overTimeRemindTime = overTimeRemindTime;
    }

    public Integer getOverTimeRemindIntv() {
        return overTimeRemindIntv;
    }

    public void setOverTimeRemindIntv(Integer overTimeRemindIntv) {
        this.overTimeRemindIntv = overTimeRemindIntv;
    }

    public String getOverTimeMsgChannel() {
        return overTimeMsgChannel;
    }

    public void setOverTimeMsgChannel(String overTimeMsgChannel) {
        this.overTimeMsgChannel = overTimeMsgChannel;
    }

    public String getFlowInMsgTemplate() {
        return flowInMsgTemplate;
    }

    public void setFlowInMsgTemplate(String flowInMsgTemplate) {
        this.flowInMsgTemplate = flowInMsgTemplate;
    }

    public String getFlowOutMsgTemplate() {
        return flowOutMsgTemplate;
    }

    public void setFlowOutMsgTemplate(String flowOutMsgTemplate) {
        this.flowOutMsgTemplate = flowOutMsgTemplate;
    }

    public List<String> getFlowInMsgSendTo() {
        return flowInMsgSendTo;
    }

    public void setFlowInMsgSendTo(List<String> flowInMsgSendTo) {
        this.flowInMsgSendTo = flowInMsgSendTo;
    }

    public String getFlowOutMsgSendTo() {
        return flowOutMsgSendTo;
    }

    public void setFlowOutMsgSendTo(String flowOutMsgSendTo) {
        this.flowOutMsgSendTo = flowOutMsgSendTo;
    }

    public String getFlowInMsgChannel() {
        return flowInMsgChannel;
    }

    public void setFlowInMsgChannel(String flowInMsgChannel) {
        this.flowInMsgChannel = flowInMsgChannel;
    }

    public String getFlowOutMsgChannel() {
        return flowOutMsgChannel;
    }

    public void setFlowOutMsgChannel(String flowOutMsgChannel) {
        this.flowOutMsgChannel = flowOutMsgChannel;
    }

    public Integer getFlowInSelParticipantMode() {
        return flowInSelParticipantMode;
    }

    public void setFlowInSelParticipantMode(Integer flowInSelParticipantMode) {
        this.flowInSelParticipantMode = flowInSelParticipantMode;
    }

    public String getFlowInNodeSelParticipant() {
        return flowInNodeSelParticipant;
    }

    public void setFlowInNodeSelParticipant(String flowInNodeSelParticipant) {
        this.flowInNodeSelParticipant = flowInNodeSelParticipant;
    }

    public String getSubFlowId() {
        return subFlowId;
    }

    public void setSubFlowId(String subFlowId) {
        this.subFlowId = subFlowId;
    }

    public String getResultVarName() {
        return resultVarName;
    }

    public void setResultVarName(String resultVarName) {
        this.resultVarName = resultVarName;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getEventListener() {
        return eventListener;
    }

    public void setEventListener(String eventListener) {
        this.eventListener = eventListener;
    }

    public Integer getFlowInMode() {
        return flowInMode;
    }

    public void setFlowInMode(Integer flowInMode) {
        this.flowInMode = flowInMode;
    }

    public Integer getFlowOutMode() {
        return flowOutMode;
    }

    public void setFlowOutMode(Integer flowOutMode) {
        this.flowOutMode = flowOutMode;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public String getStartTimerCron() {
        return startTimerCron;
    }

    public void setStartTimerCron(String startTimerCron) {
        this.startTimerCron = startTimerCron;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public Integer getEnableApiStart() {
        return enableApiStart;
    }

    public void setEnableApiStart(Integer enableApiStart) {
        this.enableApiStart = enableApiStart;
    }

    public Integer getAllowManualStart() {
        return allowManualStart;
    }

    public void setAllowManualStart(Integer allowManualStart) {
        this.allowManualStart = allowManualStart;
    }

    public Integer getEnableTimerStart() {
        return enableTimerStart;
    }

    public void setEnableTimerStart(Integer enableTimerStart) {
        this.enableTimerStart = enableTimerStart;
    }

    public Integer getEnableMessageStart() {
        return enableMessageStart;
    }

    public void setEnableMessageStart(Integer enableMessageStart) {
        this.enableMessageStart = enableMessageStart;
    }

    public String getMessageProcessingClass() {
        return messageProcessingClass;
    }

    public void setMessageProcessingClass(String messageProcessingClass) {
        this.messageProcessingClass = messageProcessingClass;
    }

    public String getInitAuthId() {
        return initAuthId;
    }

    public void setInitAuthId(String initAuthId) {
        this.initAuthId = initAuthId;
    }

    public Integer getAllowMultipleSelection() {
        return allowMultipleSelection;
    }

    public void setAllowMultipleSelection(Integer allowMultipleSelection) {
        this.allowMultipleSelection = allowMultipleSelection;
    }

    public Integer getAllowPassAfterHostCompleted() {
        return allowPassAfterHostCompleted;
    }

    public void setAllowPassAfterHostCompleted(Integer allowPassAfterHostCompleted) {
        this.allowPassAfterHostCompleted = allowPassAfterHostCompleted;
    }

    public String getFlowInMsgTemplateCode() {
        return flowInMsgTemplateCode;
    }

    public void setFlowInMsgTemplateCode(String flowInMsgTemplateCode) {
        this.flowInMsgTemplateCode = flowInMsgTemplateCode;
    }

    public List<String> getVariableFieldInMsg() {
        return variableFieldInMsg;
    }

    public void setVariableFieldInMsg(List<String> variableFieldInMsg) {
        this.variableFieldInMsg = variableFieldInMsg;
    }

    public void addFlowOutLink(Link link) {
        this.flowOutLinks.add(link);
    }

    public void addFlowInLink(Link link) {
        this.flowInLinks.add(link);
    }

    public void addEvent(NodeEvent nodeEvent) {
        nodeEvent.setNode(this);
        this.events.add(nodeEvent);
    }

    public void fitOut(Flow flow) {
        setFlow(flow);
        for (NodeEvent nodeEvent : getEvents()) {
            nodeEvent.setNode(this);
        }
        for (NodePresetUser nodePresetUser : getNodePresetUsers()) {
            nodePresetUser.setNode(this);
        }
        for (NodeFreeStep nodeFreeStep : getNodeFreeSteps()) {
            if (nodeFreeStep != null) {
                nodeFreeStep.setNode(this);
            }
        }
    }

    public Integer getSaveResultValue() {
        return saveResultValue;
    }

    public void setSaveResultValue(Integer saveResultValue) {
        this.saveResultValue = saveResultValue;
    }

    public Integer getAllowPresetRoute() {
        return allowPresetRoute;
    }

    public void setAllowPresetRoute(Integer allowPresetRoute) {
        this.allowPresetRoute = allowPresetRoute;
    }

    public Integer getPresetedRoute() {
        return presetedRoute;
    }

    public void setPresetedRoute(Integer presetedRoute) {
        this.presetedRoute = presetedRoute;
    }

    public Integer getPresetedNode() {
        return presetedNode;
    }

    public void setPresetedNode(Integer presetedNode) {
        this.presetedNode = presetedNode;
    }

    public List<NodePresetUser> getNodePresetUsers() {
        if (nodePresetUsers == null) {
            nodePresetUsers = new ArrayList<>();
        }
        return nodePresetUsers;
    }

    public void setNodePresetUsers(List<NodePresetUser> nodePresetUsers) {
        this.nodePresetUsers = nodePresetUsers;
    }

    public void clearPresetedUsers() {
        if (nodePresetUsers != null) {
            nodePresetUsers.clear();
        }
    }

    public void addPresetedUser(NodePresetUser nodePresetUser) {
        getNodePresetUsers().add(nodePresetUser);
    }

    public Integer getMustPresetRoute() {
        return mustPresetRoute;
    }

    public void setMustPresetRoute(Integer mustPresetRoute) {
        this.mustPresetRoute = mustPresetRoute;
    }

    public Integer getAllowPresetFreeRoute() {
        return allowPresetFreeRoute;
    }

    public void setAllowPresetFreeRoute(Integer allowPresetFreeRoute) {
        this.allowPresetFreeRoute = allowPresetFreeRoute;
    }

    public Integer getAllowModifyPresetedFreeRoute() {
        return allowModifyPresetedFreeRoute;
    }

    public void setAllowModifyPresetedFreeRoute(Integer allowModifyPresetedFreeRoute) {
        this.allowModifyPresetedFreeRoute = allowModifyPresetedFreeRoute;
    }

    public List<NodeFreeStep> getNodeFreeSteps() {
        if (nodeFreeSteps == null) {
            nodeFreeSteps = new ArrayList<>();
        }
        return nodeFreeSteps;
    }

    public void addNodeFreeStep(NodeFreeStep nodeFreeStep) {
        nodeFreeStep.setNode(this);
        getNodeFreeSteps().add(nodeFreeStep);
    }

    public void setNodeFreeSteps(List<NodeFreeStep> nodeFreeSteps) {
        this.nodeFreeSteps = nodeFreeSteps;
    }

    /**
     * 获取自由流步骤
     *
     * @param nodeFreeStepId 自由流步骤id
     * @return 自由流步骤
     * @author JonnyJiang
     * @date 2020/11/23 18:15
     */

    public NodeFreeStep getNodeFreeStepById(String nodeFreeStepId) {
        for (NodeFreeStep nodeFreeStep : getNodeFreeSteps()) {
            if (StringUtils.equals(nodeFreeStep.getId(), nodeFreeStepId)) {
                return nodeFreeStep;
            }
        }
        throw new NodeFreeStepNotFoundByIdException(nodeFreeStepId, getId());
    }

    /**
     * 获取自由流下一步骤
     *
     * @param nodeFreeStepId 自由流步骤id
     * @return 自由流下一步骤
     * @author JonnyJiang
     * @date 2020/11/23 18:15
     */

    public NodeFreeStep getNextNodeFreeStepById(String nodeFreeStepId) {
        NodeFreeStep nextNodeFreeStep = null;
        NodeFreeStep nodeFreeStep = getNodeFreeStepById(nodeFreeStepId);
        for (NodeFreeStep freeStep : getNodeFreeSteps()) {
            if (nodeFreeStep != freeStep) {
                if (nextNodeFreeStep == null) {
                    nextNodeFreeStep = freeStep;
                } else {
                    if (freeStep.getStepNo() > nodeFreeStep.getStepNo()) {
                        if (freeStep.getStepNo() < nextNodeFreeStep.getStepNo()) {
                            nextNodeFreeStep = freeStep;
                        }
                    }
                }
            }
        }
        return nextNodeFreeStep;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public NodeFreeStep getFirstNodeFreeStep() {
        if (getNodeFreeSteps().size() > 0) {
            return getNodeFreeSteps().get(0);
        }
        return null;
    }

    public Integer getAllowModifyPresetRouteByDescendant() {
        return allowModifyPresetRouteByDescendant;
    }

    public void setAllowModifyPresetRouteByDescendant(Integer allowModifyPresetRouteByDescendant) {
        this.allowModifyPresetRouteByDescendant = allowModifyPresetRouteByDescendant;
    }

    public Integer getForceSequence() {
        return forceSequence;
    }

    public void setForceSequence(Integer forceSequence) {
        this.forceSequence = forceSequence;
    }

    public Integer getMustPresetRouteUser() {
        return mustPresetRouteUser;
    }

    public void setMustPresetRouteUser(Integer mustPresetRouteUser) {
        this.mustPresetRouteUser = mustPresetRouteUser;
    }

    public Integer getEnableAutoSelParticipant() {
        return enableAutoSelParticipant;
    }

    public void setEnableAutoSelParticipant(Integer enableAutoSelParticipant) {
        this.enableAutoSelParticipant = enableAutoSelParticipant;
    }
}