package com.csicit.ace.bpm.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.activiti.nodes.*;
import com.csicit.ace.bpm.el.WfdFlowElService;
import com.csicit.ace.bpm.enums.*;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.domain.WfdGlobalRuleDO;
import com.csicit.ace.bpm.pojo.domain.WfdGlobalVariantDO;
import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.vo.*;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.*;
import com.csicit.ace.bpm.service.WfdGlobalRuleService;
import com.csicit.ace.bpm.service.WfdGlobalVariantService;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.IUser;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/9/5 10:10
 */
public class FlowUtils {
    public static final String GLOBAL_VARIABLE_ID_PREFIX = "G_";
    public static final String FLOW_VARIABLE_ID_PREFIX = "F_";
    public static final String GLOBAL_RULE_ID_PREFIX = "G_";
    public static final String FLOW_RULE_ID_PREFIX = "F_";

    /**
     * 生成流程实体
     *
     * @param model 流程模型
     * @return Flow
     * @author JonnyJiang
     * @date 2019/9/9 19:28
     */

    public static Flow getFlow(String model) {
        return getFlow(model, false);
    }

    /**
     * 获取开始节点
     *
     * @param flow
     * @return com.csicit.ace.bpm.pojo.vo.wfd.Node
     * @author JonnyJiang
     * @date 2020/1/13 16:43
     */

    private static Node getStartNode(Flow flow) {
        Optional<Node> startNodeOpt = flow.getNodes().stream().filter(o -> NodeType.Start.isEquals(o.getNodeType())).findFirst();
        if (startNodeOpt.isPresent()) {
            return startNodeOpt.get();
        } else {
            throw new NormalStartNodeNotFoundException(flow.getId());
        }
    }

    /**
     * 获取所有的第一步人工节点
     * 必须使用静态方法，否则序列化会有问题
     *
     * @return
     */
    public static List<Node> getFirstManualNodes(Flow flow) {
        List<Node> nodes = new ArrayList<>();
        Node startNode = getStartNode(flow);
        if (startNode.getFlowOutLinks().size() > 0) {
            for (int i = 0; i < startNode.getFlowOutLinks().size(); i++) {
                Node toNode = startNode.getFlowOutLinks().get(i).getToNode();
                if (NodeType.Manual.isEquals(toNode.getNodeType()) || NodeType.Free.isEquals(toNode.getNodeType())) {
                    nodes.add(toNode);
                }
            }
            return nodes;
        }
        throw new BpmException(LocaleUtils.getFlowOutLinkNotFound(flow.getId()));
    }

    /**
     * 获取第一个人工节点
     * 必须使用静态方法，否则序列化会有问题
     *
     * @return
     */
    public static Node getFirstManualNode(Flow flow) {
        Node startNode = getStartNode(flow);
        if (startNode.getFlowOutLinks().size() > 0) {
            Node firstManualNode = null;
            for (int i = 0; i < startNode.getFlowOutLinks().size(); i++) {
                Node toNode = startNode.getFlowOutLinks().get(i).getToNode();
                if (NodeType.Manual.isEquals(toNode.getNodeType()) || NodeType.Free.isEquals(toNode.getNodeType())) {
                    firstManualNode = toNode;
                    break;
                }
            }
            if (firstManualNode == null) {
                throw new FirstManualNodeNotFoundException(flow.getId());
            }
            return firstManualNode;
        }
        throw new BpmException(LocaleUtils.getFlowOutLinkNotFound(flow.getId()));
    }

    public static Node getFirstFlowOutManualNode(Flow flow) {
        Node startNode = getStartNode(flow);
        if (startNode.getFlowOutLinks().size() > 0) {
            Node toNode = null;
            RuleUtils ruleUtils = SpringContextUtils.getBean(RuleUtils.class);
            for (Link link : startNode.getFlowOutLinks()) {
                Boolean val = ruleUtils.calculateRule(link.getFlow(), link.getRuleExpression());
                if (val) {
                    toNode = link.getToNode();
                    break;
                }
            }
            if (toNode == null) {
                throw new BpmException(LocaleUtils.getFirstFlowOutManualNode(flow.getId()));
            }
            return toNode;
        } else {
            throw new BpmException(LocaleUtils.getFlowOutLinkNotFound(flow.getId()));
        }
    }

    /**
     * 生成流程实体
     *
     * @param model    流程模型
     * @param validate 是否校验
     * @return 流程实体
     * @author JonnyJiang
     * @date 2020/1/13 16:45
     */

    public static Flow getFlow(String model, Boolean validate) {
        Flow flow = JSONObject.parseObject(model, Flow.class);
        flow.setModel(model);
        for (FormField formField : flow.getFormFields()) {
            formField.setFlow(flow);
        }
        for (Variant variant : flow.getVariants()) {
            variant.setFlow(flow);
        }
        for (Rule rule : flow.getRules()) {
            rule.setFlow(flow);
        }
        for (Event event : flow.getEvents()) {
            event.setFlow(flow);
        }
        for (SyncSetting syncSetting : flow.getSyncSettings()) {
            syncSetting.setFlow(flow);
        }
        for (Node node : flow.getNodes()) {
            node.fitOut(flow);
        }
        for (Link link : flow.getLinks()) {
            link.setFlow(flow);
            combineNodeInfo(link);
        }
        if (validate) {
            validate(flow);
        }
        return flow;
    }

    /**
     * 获取扩展后的节点
     *
     * @param process
     * @return 返回具体类型节点
     */
    public static AbstractNode getNode(BpmnModel bpmnModel, Process process, Node node) {
        if (NodeType.Start.isEquals(node.getNodeType())) {
            return new StartNode(bpmnModel, process, node);
        } else if (NodeType.Manual.isEquals(node.getNodeType())) {
            return new ManualNode(bpmnModel, process, node);
        } else if (NodeType.Free.isEquals(node.getNodeType())) {
            return new ManualNode(bpmnModel, process, node);
        } else if (NodeType.Subflow.isEquals(node.getNodeType())) {
            return new SubflowNode(bpmnModel, process, node);
        } else if (NodeType.End.isEquals(node.getNodeType())) {
            return new EndNode(bpmnModel, process, node);
        } else {
            throw new UnsupportedNodeTypeException(node.getNodeType());
        }
    }

    /**
     * 流程定义校验
     *
     * @param flow 流程定义
     * @return void
     * @author JonnyJiang
     * @date 2019/11/20 14:17
     */

    private static void validate(Flow flow) {
        List<String> codes = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        Integer flag = 0;

        for (Node node :
                flow.getNodes()) {
            // 节点标识不能为空
            if (StringUtils.isEmpty(node.getCode())) {
                throw new BpmException(LocaleUtils.getNodeCodeIsNull(node.getCode()));
            }
            // 节点id不能重复
            if (ids.contains(node.getId())) {
                throw new BpmException(LocaleUtils.getNodeSameIdExist(node.getCode()));
            }
            ids.add(node.getId());
            // 节点标识不能重复
            if (codes.contains(node.getCode())) {
                throw new BpmException(LocaleUtils.getNodeSameCodeExist(node.getCode()));
            }
            codes.add(node.getCode());
            if (NodeType.Start.isEquals(node.getNodeType())) {
                if (IntegerUtils.isTrue(node.getEnableApiStart())) {
                    flag++;
                }
            }
        }
        if (flag > 1) {
            // 无指定开始事件最多只能有一个
            throw new BpmException(LocaleUtils.getStartNodeCountError(flag));
        }
    }

    /**
     * StartNode使用
     *
     * @param id
     * @param code
     * @param name
     * @param nodeType
     * @author JonnyJiang
     * @date 2019/9/9 17:35
     */

    public static Node getNode(String id, String code, String name, NodeType nodeType) {
        Node node = new Node();
        if (StringUtils.isEmpty(id)) {
            node.setId("tid-" + UUID.randomUUID().toString());
        } else {
            node.setId(id);
        }
        node.setCode(code);
        node.setName(name);
        node.setNodeType(nodeType.getValue());
        node.setEnableApiStart(1);
        node.setAllowManualStart(1);
        node.setEnableTimerStart(0);
        node.setEnableMessageStart(0);
        return node;
    }

    /**
     * 非StartNode使用
     *
     * @param id
     * @param code
     * @param name
     * @param nodeType
     * @param flowInMode
     * @param flowOutMode
     * @author JonnyJiang
     * @date 2019/9/9 17:35
     */
    public static Node getNode(String id, String code, String name, NodeType nodeType, FlowInMode flowInMode, FlowOutMode flowOutMode) {
        Node node = getNode(id, code, name, nodeType);
        node.setFlowInMode(flowInMode.getValue());
        node.setFlowOutMode(flowOutMode.getValue());
        return node;
    }

    public static String getVariantId(String variantId, String prefix) {
        return variantId.substring(prefix.length());
    }

    public static Variant getVariant(String variantId, Flow flow) {
        if (FlowUtils.isGlobalVariant(variantId)) {
            return FlowUtils.getGlobalVariantById(FlowUtils.getGlobalVariantId(variantId));
        } else if (FlowUtils.isFlowVariant(variantId)) {
            return flow.getVariantById(getFlowVariantId(variantId));
        }
        throw new UnsupportedVariableIdException(variantId);
    }

    public static String getVariantId(String variantId) {
        if (StringUtils.isNotBlank(variantId)) {
            if (isGlobalVariant(variantId)) {
                return getVariantId(variantId, GLOBAL_VARIABLE_ID_PREFIX);
            } else if (isFlowVariant(variantId)) {
                return getVariantId(variantId, FLOW_VARIABLE_ID_PREFIX);
            }
        }
        return variantId;
    }

    public static Boolean isGlobalRule(String ruleId) {
        return ruleId.startsWith(GLOBAL_VARIABLE_ID_PREFIX);
    }

    public static Boolean isFlowRule(String ruleId) {
        return ruleId.startsWith(FLOW_VARIABLE_ID_PREFIX);
    }

    public static Boolean isGlobalVariant(String variantId) {
        return variantId.startsWith(GLOBAL_VARIABLE_ID_PREFIX);
    }

    public static Boolean isFlowVariant(String variantId) {
        return variantId.startsWith(FLOW_VARIABLE_ID_PREFIX);
    }

    public static String getFlowVariantId(String variantId) {
        return getVariantId(variantId, FLOW_VARIABLE_ID_PREFIX);
    }

    public static String getGlobalVariantId(String variantId) {
        return getVariantId(variantId, GLOBAL_VARIABLE_ID_PREFIX);
    }

    public static String getRuleId(String ruleId, String prefix) {
        return ruleId.substring(prefix.length());
    }

    public static String getGlobalRuleId(String ruleId) {
        return getRuleId(ruleId, GLOBAL_RULE_ID_PREFIX);
    }

    public static String getFlowRuleId(String ruleId) {
        return getRuleId(ruleId, FLOW_RULE_ID_PREFIX);
    }

    public static Variant getGlobalVariantById(List<WfdGlobalVariantDO> wfdGlobalVariants, String variantId) {
        for (WfdGlobalVariantDO wfdGlobalVariant : wfdGlobalVariants) {
            if (variantId.equals(wfdGlobalVariant.getId())) {
                return getGlobalVariant(wfdGlobalVariant);
            }
        }
        throw new WfdGlobalVariantNotFoundException(variantId);
    }

    private static Variant getGlobalVariant(WfdGlobalVariantDO wfdGlobalVariant) {
        Variant variant = new Variant();
        variant.setId(wfdGlobalVariant.getId());
        variant.setName(wfdGlobalVariant.getName());
        variant.setCaption(wfdGlobalVariant.getCaption());
        variant.setDataType(wfdGlobalVariant.getDataType());
        variant.setDefaultValue(wfdGlobalVariant.getDefaultValue());
        variant.setValueExpression(wfdGlobalVariant.getValueExpression());
        return variant;
    }

    public static Variant getGlobalVariantById(String variantId) {
        WfdGlobalVariantService wfdGlobalVariantService = SpringContextUtils.getBean(WfdGlobalVariantService.class);
        WfdGlobalVariantDO wfdGlobalVariant = wfdGlobalVariantService.getById(variantId);
        if (wfdGlobalVariant == null) {
            throw new BpmException(LocaleUtils.getWfdGlobalVariantNotFound(variantId));
        }
        return getGlobalVariant(wfdGlobalVariant);
    }

    public static Rule getGlobalRuleById(String ruleId) {
        WfdGlobalRuleService wfdGlobalRuleService = SpringContextUtils.getBean(WfdGlobalRuleService.class);
        WfdGlobalRuleDO wfdGlobalRule = wfdGlobalRuleService.getById(ruleId);
        if (wfdGlobalRule == null) {
            throw new BpmException(LocaleUtils.getWfdGlobalRuleNotFound(ruleId));
        }
        Rule rule = new Rule();
        rule.setId(wfdGlobalRule.getId());
        rule.setDescription(wfdGlobalRule.getDescription());
        rule.setExpression(wfdGlobalRule.getExpression());
        return rule;
    }

    public static Integer getSecretLevel(Flow flow, WfdFlowElService wfdFlowElService)
    {
        Integer secretLevel = null;
        if (StringUtils.isNotEmpty(flow.getFormSecretLevelField())) {
            // 如果设置了密级字段，则仅返回符合密级的用户
            FormField formField = flow.getFormFieldByName(flow.getFormSecretLevelField());
            String secretLevelStr = wfdFlowElService.getFormValue(formField.getName());
            if (StringUtils.isNotEmpty(secretLevelStr)) {
                // 如果表单密级不为空
                try {
                    if (StringUtils.isNumeric(secretLevelStr)) {
                        secretLevel = Integer.valueOf(secretLevelStr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (secretLevel == null) {
                    throw new UnsupportedFormSecretLevelException(secretLevelStr);
                }
            }
        }
return secretLevel;
    }

    /**
     * 获取后续步骤
     *
     * @param session        http会话
     * @param flowInstanceId 流程实例id
     * @param businessKey    业务标识
     * @param node           流出节点
     * @param workResults    流出节点选择的办理结果
     * @param deliverInfos   当前节点已办结的转交信息
     * @param nodeFreeStepId 自由流步骤id
     */
    public static List<NextStep> listNextSteps(HttpSession session, String flowInstanceId, String businessKey, Node node, List<String> workResults, List<DeliverInfo> deliverInfos, String nodeFreeStepId) {
        SessionAttribute.initElSession(session, flowInstanceId, node.getFlow(), businessKey);
        WfdFlowElService wfdFlowElService = SpringContextUtils.getBean(WfdFlowElService.class);
        Integer secretLevel = getSecretLevel(node.getFlow(), wfdFlowElService);

        RuleUtils ruleUtils = SpringContextUtils.getBean(RuleUtils.class);
        List<NextStep> nextSteps = new ArrayList<>();
        if (NodeType.Free.isEquals(node.getNodeType())) {
            // 如果当前节点是自由流节点，需要判断是否需要添加当前节点自身
            // TODO: 2020/11/23
            /**
             * 如果当前节点是自由流节点，则需要判断是否需要添加自身
             * 如果当前节点不是自由流节点，则需要判断目标节点是不是自由流节点，如果是自由流节点，则需要设置nodeFreeStepId
             * 如果是自由流节点，需要判断自由流节点是否流转完了，如果流转完了，则根据流出分支生成后续步骤，否则，继续办理当前自由流节点
             */
            NodeFreeStep nodeFreeStep = node.getNextNodeFreeStepById(nodeFreeStepId);
            if (nodeFreeStep != null) {
                // 如果存在自由流节点存在后续步骤，则必须先办完后续步骤
                nextSteps.add(new NextStep(nodeFreeStep, secretLevel));
                return nextSteps;
            }
        }
        // 已办结的任务各工作结果被选择的次数
        Map<String, Integer> resultMap = getWorkResultMap(node, workResults, deliverInfos);
        for (Link link : node.getFlowOutLinks()) {
            if (nextSteps.stream().anyMatch(o -> StringUtils.equals(o.getNodeId(), link.getToNodeId()))) {
                // 如果下一点相同，则仅保存一个，当前分支跳过
                continue;
            }
            if (IntegerUtils.isFalse(link.getPresetedLink())) {
                // 如果当前路径不是预设路径，则判断前序节点中是否存在预设流转路径的节点
                if (parentNodeHasPresetedRoute(link.getFromNode())) {
                    // 如果当前节点不是预设的节点，则当前分支跳过
                    continue;
                }
            }
            if (linkAvailable(ruleUtils, link, node.getFlow(), resultMap)) {
                nextSteps.add(new NextStep(link, secretLevel));
            }
        }
        return nextSteps;
    }

    /**
     * 获取工作结果Map
     *
     * @param node         节点
     * @param workResults  当前选择的工作结果
     * @param deliverInfos 当前节点已办结的转交信息
     * @return 工作结果Map
     * @author JonnyJiang
     * @date 2020/9/28 21:35
     */

    public static Map<String, Integer> getWorkResultMap(Node node, List<String> workResults, List<DeliverInfo> deliverInfos) {
        Map<String, Integer> resultMap = new HashMap<>(16);
        if (node.getWorkResultOptions().length > 0) {
            // 如果当前任务节点设置了工作结果，则计算各工作结果被选择的次数
            String[] workResultOptions = node.getWorkResultOptions().clone();
            // 初始化各工作结果被选择的次数
            for (String workResult : workResultOptions) {
                resultMap.put(workResult, 0);
            }
            // 首先计入本次提交所选择的工作结果
            if (workResults != null) {
                for (String workResult : workResults) {
                    if (resultMap.containsKey(workResult)) {
                        resultMap.put(workResult, resultMap.get(workResult) + 1);
                    }
                }
            }
            if (deliverInfos != null) {
                for (DeliverInfo deliverInfo : deliverInfos) {
                    if (deliverInfo.getWorkResultOptions() != null) {
                        for (String workResult : deliverInfo.getWorkResultOptions()) {
                            if (resultMap.containsKey(workResult)) {
                                resultMap.put(workResult, resultMap.get(workResult) + 1);
                            }
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * 分支是否可用
     *
     * @param ruleUtils 规则工具
     * @param link      分支
     * @param flow      流程定义
     * @param resultMap 工作结果
     * @return 分支是否可用
     * @author JonnyJiang
     * @date 2020/9/28 21:34
     */

    public static boolean linkAvailable(RuleUtils ruleUtils, Link link, Flow flow, Map<String, Integer> resultMap) {
        if (ConditionMode.None.isEquals(link.getConditionMode())) {
            return true;
        } else if (ConditionMode.WorkResult.isEquals(link.getConditionMode())) {
            if (StringUtils.isEmpty(link.getResult())) {
                throw new LinkResultIsNullException();
            } else {
                if (LinkOperator.isTrue(link.getCountMode(), link.getOperator(), link.getResult(), link.getCount(), resultMap)) {
                    return true;
                }
            }
        } else if (ConditionMode.Rule.isEquals(link.getConditionMode())) {
            if (ruleUtils.calculateRule(flow, link.getRuleExpression())) {
                return true;
            }
        }
        return false;
    }

    private static boolean parentNodeHasPresetedRoute(Node parentNode) {
        if (IntegerUtils.isTrue(parentNode.getPresetedRoute())) {
            return true;
        } else {
            for (Link link : parentNode.getFlowInLinks()) {
                if (parentNodeHasPresetedRoute(link.getFromNode())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void combineNodeInfo(Link link) {
        if (StringUtils.isEmpty(link.getFromNodeId())) {
            throw new BpmException(LocaleUtils.getLinkFromNodeIdIsNull(link.getId()));
        }
        link.setFromNode(link.getFlow().getNodeById(link.getFromNodeId()));
        link.getFromNode().addFlowOutLink(link);
        if (StringUtils.isEmpty(link.getToNodeId())) {
            throw new BpmException(LocaleUtils.getLinkToNodeIdIsNull(link.getId()));
        }
        link.setToNode(link.getFlow().getNodeById(link.getToNodeId()));
        link.getToNode().addFlowInLink(link);
    }

    public static List<String> getFirstMultiInstanceUserIds(Node node, DeliverNode deliverNode) {
        List<String> userIds;
        if (MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
            userIds = deliverNode.getDeliverUsers().stream().map(DeliverUser::getUserId).collect(Collectors.toList());
        } else {
            DeliverUser deliverUser = deliverNode.getDeliverUsers().get(0);
            if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
                userIds = deliverNode.getDeliverUsers().stream().filter(o -> StringUtils.equals(o.getDepartmentId(), deliverUser.getDepartmentId())).map(DeliverUser::getUserId).collect(Collectors.toList());
            } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
                userIds = deliverNode.getDeliverUsers().stream().filter(o -> StringUtils.equals(o.getOrganizationId(), deliverUser.getOrganizationId())).map(DeliverUser::getUserId).collect(Collectors.toList());
            } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
                userIds = deliverNode.getDeliverUsers().stream().filter(o -> StringUtils.equals(o.getGroupId(), deliverUser.getGroupId())).map(DeliverUser::getUserId).collect(Collectors.toList());
            } else {
                throw new UnsupportedMultiInstanceModeException(node);
            }
        }
        return userIds;
    }

    public static Boolean allowClaim(Node node, DeliverNode deliverNode, String currentUserId) {
        List<DeliverUser> deliverUsers = new ArrayList<>();
        if (MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
            deliverUsers.addAll(deliverNode.getDeliverUsers());
        } else {
            DeliverUser currentDeliverUser = deliverNode.getDeliverUserByUserId(currentUserId);
            if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
                for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                    if (StringUtils.equals(currentDeliverUser.getDepartmentId(), deliverUser.getDepartmentId())) {
                        deliverUsers.add(deliverUser);
                    }
                }
            } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
                for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                    if (StringUtils.equals(currentDeliverUser.getOrganizationId(), deliverUser.getOrganizationId())) {
                        deliverUsers.add(deliverUser);
                    }
                }
            } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
                for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                    if (StringUtils.equals(currentDeliverUser.getGroupId(), deliverUser.getGroupId())) {
                        deliverUsers.add(deliverUser);
                    }
                }
            }
        }
        if (deliverUsers.size() > 0) {
            if (deliverUsers.stream().allMatch(o -> o.getClaimTime() == null)) {
                // 如果未接收，则允许接收
                return true;
            }
        }
        return false;
    }

    private static void mapAdd(Map<String, List<DeliverUser>> map, String key, DeliverUser value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<DeliverUser> l = new ArrayList<>();
            l.add(value);
            map.put(key, l);
        }
    }

    public static void initDeliverNodes(Flow flow, DeliverInfo deliverInfo, String businessKey) {
        IUser iUser = SpringContextUtils.getBean(IUser.class);
        Set<String> userIds = new HashSet<>();
        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
            userIds.addAll(deliverNode.getDeliverUsers().stream().map(DeliverUser::getUserId).collect(Collectors.toSet()));
            //for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
            //if (!userIds.contains(deliverUser.getUserId())) {
            //   userIds.add(deliverUser.getUserId());
            //}
            //}
        }
        List<SysUserDO> users = new ArrayList<>();
        if (userIds.size() > 0) {
            users = iUser.getUsersByIds(new ArrayList<>(userIds));
        }
        if (StringUtils.isNotEmpty(flow.getFormSecretLevelField())) {
            // 如果密级字段不为空，则判断用户密级是否满足密级要求
            WfdFlowElService wfdFlowElService = SpringContextUtils.getBean(WfdFlowElService.class);
            FormField formField = flow.getFormFieldByName(flow.getFormSecretLevelField());
            String secretLevelStr = wfdFlowElService.getFormValue(formField.getName());
            if (StringUtils.isEmpty(secretLevelStr)) {
                throw new FormSecretLevelIsNullException(flow.getFormDataTable(), formField.getName(), businessKey);
            } else {
                Integer secretLevel = null;
                try {
                    if (StringUtils.isNumeric(secretLevelStr)) {
                        secretLevel = Integer.valueOf(secretLevelStr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (secretLevel == null) {
                    throw new UnsupportedFormSecretLevelException(secretLevelStr);
                }
                for (SysUserDO sysUser : users) {
                    if (SecurityUtils.compareSecretLevel(secretLevel, sysUser.getSecretLevel()) > 0) {
                        throw new DeliverUserSecretLevelNotMatchException(secretLevel, sysUser);
                    }
                }
            }
        }
        for (DeliverNode deliverNode : deliverInfo.getDeliverNodes()) {
            Node node = flow.getNodeById(deliverNode.getNodeId());
            Map<String, List<DeliverUser>> map = new HashMap<>(16);
            if (MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
                map.put(null, deliverNode.getDeliverUsers());
            } else {
                for (DeliverUser deliverUser : deliverNode.getDeliverUsers()) {
                    Optional<SysUserDO> userOpt = users.stream().filter(o -> StringUtils.equals(o.getId(), deliverUser.getUserId())).findFirst();
                    if (userOpt.isPresent()) {
                        SysUserDO user = userOpt.get();
                        deliverUser.setDepartmentId(user.getDepartmentId());
                        deliverUser.setOrganizationId(user.getOrganizationId());
                        deliverUser.setGroupId(user.getGroupId());
                        if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
                            mapAdd(map, deliverUser.getDepartmentId(), deliverUser);
                        } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
                            mapAdd(map, deliverUser.getOrganizationId(), deliverUser);
                        } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
                            mapAdd(map, deliverUser.getGroupId(), deliverUser);
                        }
                    } else {
                        throw new SysUserNotFoundByIdException(deliverUser.getUserId());
                    }
                }
            }
            map.forEach((k, deliverUsers) -> {
                if (!NodeType.End.isEquals(node.getNodeType())) {
                    if (HostMode.FirstClaim.isEquals(node.getHostMode())) {
                        // 如果是谁先接收谁主办
                        if (deliverUsers.size() == 0) {
                            // 如果未选择经办人，则提示
                            throw new NoAssistantException(node.getName(), node.getCode());
                        } else if (deliverUsers.size() == 1) {
                            // 如果只有一个办理人，则自动接收
                            DeliverUser deliverUser = deliverUsers.get(0);
                            deliverUser.setUserType(UserType.Host.getValue());
                            deliverUser.setClaimTime(LocalDateTime.now());
                        }
                    } else {
                        if (deliverUsers.size() == 0) {
                            if (HostMode.Specified.isEquals(node.getHostMode())) {
                                // 如果是指定主办人
                                throw new BpmException(LocaleUtils.getNoHost(node.getName(), node.getCode()));
                            } else if (HostMode.Everybody.isEquals(node.getHostMode())) {
                                // 如果是所有人都可以主办
                                throw new NoAssistantException(node.getName(), node.getCode());
                            } else if (HostMode.AllowDeliver.isEquals(node.getHostMode())) {
                                // 如果是在允许结转之后办理的办理人主办
                                throw new NoAssistantException(node.getName(), node.getCode());
                            }
                        } else if (deliverUsers.size() == 1) {
                            // 如果只有一个办理人，则自动指定为主办人
                            DeliverUser deliverUser = deliverUsers.get(0);
                            deliverUser.setUserType(UserType.Host.getValue());
                        }
                    }
                }
            });
        }
    }

    public static DeliverInfo getDeliverInfo(WfiDeliverDO wfiDeliver) {
        return JSONObject.parseObject(wfiDeliver.getDeliverInfo(), DeliverInfo.class);
    }

    /**
     * 获取接收人id
     *
     * @param node        节点
     * @param deliverNode 转交节点
     * @param userId      用户id
     * @return 接收人id
     * @author JonnyJiang
     * @date 2020/6/28 16:46
     */

    public static String getClaimerId(Node node, DeliverNode deliverNode, String userId) {
        DeliverUser deliverUser = deliverNode.getDeliverUserByUserId(userId);
        if (deliverUser.getClaimTime() != null) {
            return userId;
        }
        if (MultiInstanceMode.Single.isEquals(node.getMultiInstanceMode())) {
            for (DeliverUser du : deliverNode.getDeliverUsers()) {
                if (du.getClaimTime() != null) {
                    return du.getUserId();
                }
            }
        } else if (MultiInstanceMode.Department.isEquals(node.getMultiInstanceMode())) {
            for (DeliverUser du : deliverNode.getDeliverUsers()) {
                if (StringUtils.equals(deliverUser.getDepartmentId(), du.getDepartmentId())) {
                    if (du.getClaimTime() != null) {
                        return du.getUserId();
                    }
                }
            }
        } else if (MultiInstanceMode.Organization.isEquals(node.getMultiInstanceMode())) {
            for (DeliverUser du : deliverNode.getDeliverUsers()) {
                if (StringUtils.equals(deliverUser.getOrganizationId(), du.getOrganizationId())) {
                    if (du.getClaimTime() != null) {
                        return du.getUserId();
                    }
                }
            }
        } else if (MultiInstanceMode.Group.isEquals(node.getMultiInstanceMode())) {
            for (DeliverUser du : deliverNode.getDeliverUsers()) {
                if (StringUtils.equals(deliverUser.getGroupId(), du.getGroupId())) {
                    if (du.getClaimTime() != null) {
                        return du.getUserId();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 将预设信息JSON字符串转换为预设信息对象
     *
     * @param presetInfo 预设信息JSON字符串
     * @return 预设信息对象
     */
    public static PresetInfo getPresetInfo(String presetInfo) {
        PresetInfo pInfo = JSONObject.parseObject(presetInfo, PresetInfo.class);
        pInfo.getPresetRoutes().sort(((o1, o2) -> ObjectUtils.compare(o1.getSortIndex(), o2.getSortIndex())));
        return pInfo;
    }

    /**
     * 获取节点经办人列表
     *
     * @param wfdCollectionUtils 集合计算工具
     * @param node               节点
     * @param validate           是否校验
     * @param secretLevel 表单密级
     * @return 节点经办人列表
     */
    public static List<StepUser> listStepUsersByNode(WfdCollectionUtils wfdCollectionUtils, Node node, Boolean validate, Integer secretLevel) {
        List<StepUser> nextStepUsers = new ArrayList<>();
        if (!NodeType.End.isEquals(node.getNodeType())) {
            List<SysUserDO> sysUsers = wfdCollectionUtils.getUsers(node, secretLevel);
            String hostId = null;
            if (HostMode.Specified.isEquals(node.getHostMode())) {
                hostId = node.getHostId();
            }
            if (IntegerUtils.isTrue(node.getPresetedNode())) {
                // 如果当前节点是预设节点
                NodePresetUser nodePresetUser = node.getNodePresetUsers().stream().filter(o -> UserType.Host.isEquals(o.getUserType())).findFirst().orElse(null);
                if (nodePresetUser != null) {
                    // 如果预设经办人中有主办人
                    hostId = nodePresetUser.getUserId();
                }
            }
            for (SysUserDO sysUser : sysUsers) {
                NodePresetUser nodePresetUser = null;
                if (IntegerUtils.isTrue(node.getPresetedNode())) {
                    // 如果当前节点是预设节点
                    if (node.getNodePresetUsers().size() > 0) {
                        // 如果当前节点已预设经办人
                        nodePresetUser = node.getNodePresetUsers().stream().filter(o->StringUtils.equals(o.getUserId(), sysUser.getId())).findFirst().orElse(null);
                        if(nodePresetUser == null)
                        {
                            // 如果当前用户不在预设经办人列表中，则跳过
                            continue;
                        }
                    }
                }
                StepUser stepUser = new StepUser(sysUser, hostId);
                if(nodePresetUser != null)
                {
                    stepUser.setSortIndex(nodePresetUser.getSortIndex());
                }
                nextStepUsers.add(stepUser);
            }
            if (IntegerUtils.isFalse(node.getEnableManualSelParticipant())) {
                if (HostMode.Specified.isEquals(node.getHostMode())) {
                    if (StringUtils.isEmpty(hostId)) {
                        if (validate) {
                            throw new NoSpecifiedHostException(node.getName(), node.getCode());
                        }
                    } else {
                        if (nextStepUsers.stream().noneMatch(o -> UserType.Host.isEquals(o.getUserType()))) {
                            IUser iUser = SpringContextUtils.getBean(IUser.class);
                            SysUserDO sysUser = iUser.getUserById(hostId);
                            if (validate) {
                                if (sysUser == null) {
                                    throw new SysUserNotFoundByIdException(hostId);
                                } else {
                                    throw new SpecifiedHostNoAccessException(node.getName(), node.getCode(), sysUser.getRealName());
                                }
                            }
                        }
                    }
                } else {
                    if (nextStepUsers.size() == 0) {
                        if (validate) {
                            throw new NoAssistantException(node.getCode(), node.getName());
                        }
                    }
                }
            }
        }
        // 对办理人进行排序
        nextStepUsers.sort((o1, o2) -> {
            if(o1.getSysUser() != null && o2.getSysUser() != null)
            {
                if(StringUtils.compare(o1.getSysUser().getDepartmentSortPath(), o2.getSysUser().getDepartmentSortPath()) <= 0) {
                    return -1;
                }
                return ObjectUtils.compare(o1.getSysUser().getSortIndex(), o1.getSysUser().getSortIndex());
            } else if(o1.getSysUser() == null){
                return -1;
            } else if(o2.getSysUser() == null) {
                return 1;
            } else {
                return 0;
            }
        });
        return nextStepUsers;
    }
}