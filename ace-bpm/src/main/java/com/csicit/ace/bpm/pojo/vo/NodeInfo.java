package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.common.utils.IntegerUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点信息
 *
 * @author JonnyJiang
 * @date 2019/10/8 19:58
 */
@Data
public abstract class NodeInfo implements Serializable {
    @JsonIgnore
    protected transient Node node;
    @JsonIgnore
    protected transient WfiFlowDO wfiFlow;
    @JsonIgnore
    protected transient TaskInstance task;
    @JsonIgnore
    protected transient DeliverInfo deliverInfoFrom;

    /**
     * 主键
     */
    private String id;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 步骤名称
     */
    private String name;
    /**
     * 节点标识
     */
    private String code;
    /**
     * 是否允许手动选择其他人员代办工作
     */
    private Integer allowPassToAgent;
    /**
     * 选择工作结果的提示语
     */
    private String workResultPrompt;
    /**
     * 是否允许选择工作结果
     */
    private Integer allowSelectWorkResult;
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
     * 流程信息
     */
    private FlowInfo flow;
    /**
     * 当前状态
     */
    private Integer state;
    /**
     * 用户身份
     */
    private Integer userType;
    /**
     * 流出模式
     */
    private Integer flowOutMode;
    /**
     * 流出模式为2时，允许多选 0不多选，1多选
     */
    private Integer allowMultipleSelection;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 主办模式
     */
    private Integer hostMode;
    /**
     * 是否允许撤销接收
     */
    private Integer allowRevokeClaim;
    /**
     * 是否允许接收
     */
    private Integer allowClaim;
    /**
     * 是否允许驳回
     */
    private Integer allowReject;
    /**
     * 是否启用驳回
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
     * 允许的操作
     */
    private List<String> availableOperations;
    /**
     * 允许加签
     */
    private Integer allowInvite;

    /**
     * 是否允许预设流转路径
     */
    private Integer allowPresetRoute;

    /**
     * 是否必须预设流转路径
     */
    private Integer mustPresetRoute;


    /**
     * 是否有权限
     */
    private boolean withAuth;

    /**
     * 构造函数（创建工作时使用）
     *
     * @param node 流程定义节点
     *             17:47thor JonnyJiang
     * @date 15:52
     */

    public NodeInfo(Node node) {
        this(node, null, null, true);
    }

    /**
     * 构造函数
     *
     * @param node    流程定义节点
     * @param wfiFlow 流程实例
     * @param task    任务
     * @author JonnyJiang
     * @date 2019/10/8 20:12
     */

    public NodeInfo(Node node, WfiFlowDO wfiFlow, TaskInstance task, boolean withAuth) {
        this.node = node;
        this.wfiFlow = wfiFlow;
        this.task = task;
        this.id = node.getId();
        this.name = node.getName();
        this.code = node.getCode();
        this.allowPassToAgent = node.getAllowPassToAgent();
        this.workResultPrompt = node.getWorkResultPrompt();
        this.allowSelectWorkResult = IntegerUtils.FALSE_VALUE;
        this.workResultOptions = node.getWorkResultOptions();
        this.minSelResult = node.getMinSelResult();
        this.maxSelResult = node.getMaxSelResult();
        this.flow = new FlowInfo(node.getFlow(), wfiFlow);
        this.flowOutMode = node.getFlowOutMode();
        this.allowMultipleSelection = node.getAllowMultipleSelection();
        this.hostMode = node.getHostMode();
        this.userType = UserType.None.getValue();
        this.enableReject = node.getEnableReject();
        this.rejectTo = node.getRejectTo();
        this.rejectToStep = node.getRejectToStep();
        this.enableRejectHaltBranches = node.getEnableRejectHaltBranches();
        this.enableRejectedReflow = node.getEnableRejectedReflow();
        this.allowConfigReject = node.getAllowConfigReject();
        this.allowInvite = IntegerUtils.FALSE_VALUE;
        this.allowPresetRoute = node.getAllowPresetRoute();
        this.mustPresetRoute = node.getMustPresetRoute();
        this.withAuth = withAuth;
        if (task != null) {
            this.taskId = task.getId();
        }
        init();
    }

    /**
     * 初始化流程状态、用户身份、操作类型、是否允许取消接收
     *
     * @author JonnyJiang
     * @date 2019/12/5 10:45
     */

    protected abstract void init();

    public List<String> getAvailableOperations() {
        if (availableOperations == null) {
            availableOperations = new ArrayList<>();
        }
        return availableOperations;
    }

    protected void addAvailableOperations(List<String> operations) {
        if (operations != null && operations.size() > 0) {
            getAvailableOperations().addAll(operations);
        }
    }

    public DeliverInfo getDeliverInfoFrom() {
        return deliverInfoFrom;
    }
}