package com.csicit.ace.bpm.utils;

import com.csicit.ace.bpm.enums.*;
import com.csicit.ace.bpm.exception.*;
import com.csicit.ace.bpm.pojo.vo.wfd.*;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/7/17 9:07
 */
public class FlowValidator {
    private Flow flow;

    public FlowValidator(Flow flow) {
        this.flow = flow;
    }

    public static void validate(Flow flow) {
        new FlowValidator(flow).validate();
    }

    public void validate() {
        validateFlow();
        validateLinks();
        validateNodes();
    }

    private void validateSyncSettings() {
        for (SyncSetting syncSetting : flow.getSyncSettings()) {
            if (StringUtils.isEmpty(syncSetting.getFieldName())) {
                throw new SyncSettingFieldNameNotSpecifiedException(syncSetting);
            } else {
                flow.getFormFieldById(syncSetting.getFieldName());
            }
        }
    }

    private void validateFlow() {
        if (StringUtils.isNotEmpty(flow.getFormSecretLevelField())) {
            flow.getFormFieldByName(flow.getFormSecretLevelField());
        }
        if (StringUtils.isNotEmpty(flow.getFormResultField())) {
            flow.getFormFieldByName(flow.getFormResultField());
        }
        if (StringUtils.isNotEmpty(flow.getFormSaveOperate())) {
            if (!flow.getFormOperations().contains(flow.getFormSaveOperate())) {
                throw new FormSaveOperationNotFoundException(flow.getFormOperations(), flow.getFormSaveOperate());
            }
        }
        validateRules();
        validateSyncSettings();
    }

    private void validateRule(RuleDO rule) {
        if (RuleType.Expr.isEquals(rule.getType())) {
            if (StringUtils.isNotEmpty(rule.getVar1())) {
                if (OperandClass.Variant.isEquals(rule.getOperandClass())) {
                    FlowUtils.getVariant(rule.getVar2(), flow);
                }
            }
            String operator = rule.getOperator();
            if (Operator.IS_NULL.equals(operator) || Operator.IS_NOT_NULL.equals(operator)) {
            } else {
                if (OperandClass.Variant.isEquals(rule.getOperandClass())) {
                    FlowUtils.getVariant(rule.getVar2(), flow);
                }
            }
        } else {
            for (RuleDO ruleDO : rule.getParts()) {
                validateRule(ruleDO);
            }
        }
    }

    private void validateRules() {
        for (Rule rule : flow.getRules()) {
            if (StringUtils.isNotEmpty(rule.getExpression())) {
                validateRule(JsonUtils.castObject(rule.getExpression(), RuleDO.class));
            }
        }
    }

    private void validateNodes() {
        for (Node node : flow.getNodes()) {
            validateNode(node);
        }
    }

    private void validateNode(Node node) {
        if (NodeType.End.isEquals(node.getNodeType())) {
            if (IntegerUtils.isTrue(node.getSaveResultValue())) {
                if (StringUtils.isEmpty(node.getFlow().getFormResultField())) {
                    // 如果设置了工作流结果，未指定结果字段，则抛出异常
                    throw new FormResultFieldNotSpecifiedException();
                }
            }
        } else {
            if (NodeRejectTo.Specific.isEquals(node.getRejectTo())) {
                if (CollectionUtils.isEmpty(node.getRejectToStep())) {
                    throw new RejectToStepNotSpecifiedException();
                }
            }
            if (FlowInMode.All.isEquals(node.getFlowInMode())) {
                // 如果等待所有分支都抵达，当流入分支数量大于1时，需要验证每个上级节点的流出分支最终都流入当前节点
                if (node.getFlowInLinks().size() > 1) {
                    for (Link flowInLink : node.getFlowInLinks()) {
                        for (Link flowOutLink : flowInLink.getFromNode().getFlowOutLinks()) {
                            if (!flowOutLink.getToNode().equals(node)) {
                                List<Node> analysedNodes = new ArrayList<>();
                                // 如果上级节点的流出分支，不是流入当前节点，则验证流出分支是否最终流入上级节点
                                if (!linkResultToTargetNode(analysedNodes, flowOutLink.getToNode(), flowOutLink.getFromNode())) {
                                    throw new FlowInModeAllException(node, flowOutLink.getFromNode());
                                }
                            }
                        }
                    }
                }
            }
            if (IntegerUtils.isTrue(node.getAllowPresetRoute())) {
                // 如果允许预设后续流转路径
                if (IntegerUtils.isFalse(node.getAllowModifyPresetRouteByDescendant())) {
                    // 如果不允许后续节点修改预设的流转路径，则后续节点都应该设置为不允许后续节点修改预设的流转路径
                    // 已发现过的节点，避免死循环查找
                    List<Node> foundNodes = new ArrayList<>();
                    foundNodes.add(node);
                    checkAllowModifyPresetRouteByDescendant(foundNodes, node);
                }
            }
        }
    }

    /**
     * 校验是否允许后续节点是否设置为false
     *
     * @param foundNodes 已发现过的节点，避免死循环查找
     * @param node       当前节点
     * @author JonnyJiang
     * @date 2020/12/18 0:52
     */

    private void checkAllowModifyPresetRouteByDescendant(List<Node> foundNodes, Node node) {
        for (Link flowOutLink : node.getFlowOutLinks()) {
            if (!foundNodes.contains(flowOutLink.getToNode())) {
                foundNodes.add(flowOutLink.getToNode());
                if (IntegerUtils.isTrue(flowOutLink.getToNode().getAllowPresetRoute())) {
                    // 如果后续节点允许预设流转路径
                    if (IntegerUtils.isTrue(flowOutLink.getToNode().getAllowModifyPresetRouteByDescendant())) {
                        // 后续节点必须设置为不允许修改后续流转步骤，如果后续节点允许修改，则抛出异常
                        throw new AllowModifyPresetRouteByDescendantMustBeTrueException(flowOutLink.getToNode());
                    }
                } else {
                    checkAllowModifyPresetRouteByDescendant(foundNodes, flowOutLink.getToNode());
                }
            }
        }
    }

    private void validateLinks() {
        for (Link link : flow.getLinks()) {
            validateLink(link);
        }
    }

    private void validateLink(Link link) {
        if (ConditionMode.WorkResult.isEquals(link.getConditionMode())) {
            if (!Arrays.asList(link.getFromNode().getWorkResultOptions()).contains(link.getResult())) {
                throw new WorkResultNotFoundException(link.getFromNode(), link.getResult());
            }
        } else if (ConditionMode.Rule.isEquals(link.getConditionMode())) {
            if (StringUtils.isNotEmpty(link.getRuleExpression())) {
                validateRule(JsonUtils.castObject(link.getRuleExpression(), RuleDO.class));
            }
        }
    }

    /**
     * 分析是否最终流入目标节点
     *
     * @param analysedNodes 已分析过的节点
     * @param analysingNode 正在分析的节点
     * @param targetNode    目标节点
     * @return boolean
     * @author JonnyJiang
     * @date 2020/7/17 10:44
     */

    private Boolean linkResultToTargetNode(List<Node> analysedNodes, Node analysingNode, Node targetNode) {
        // 重复出现的节点，最终根据未出现的节点判断是否可以返回目标节点，因此此处作为可返回目标节点处理
        Boolean linkToTargetNode = true;
        if (!analysedNodes.contains(analysingNode)) {
            analysedNodes.add(analysingNode);
            for (Link flowOutLink : analysingNode.getFlowOutLinks()) {
                if (!flowOutLink.getToNode().equals(targetNode)) {
                    // 如果流出节点不是目标节点
                    if (NodeType.End.isEquals(flowOutLink.getToNode().getNodeType())) {
                        // 如果流出节点是结束节点
                        linkToTargetNode = false;
                        break;
                    } else {
                        if (!linkResultToTargetNode(analysedNodes, flowOutLink.getToNode(), targetNode)) {
                            linkToTargetNode = false;
                            break;
                        }
                    }
                }
            }
        }
        return linkToTargetNode;
    }
}
