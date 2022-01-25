package com.csicit.ace.bpm.utils;

import com.csicit.ace.bpm.activiti.TaskVariableName;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地化工具
 *
 * @author JonnyJiang
 * @date 2019/12/6 14:04
 */
public class LocaleUtils {

    public static String getWfdVFlowNotFoundById() {
        return getMessage("BPM1");
    }

    public static String getWfdVFlowNotFoundByCode() {
        return getMessage("BPM2");
    }

    public static String getWfiFlowNotFoundById() {
        return getMessage("BPM3");
    }

    public static String getWfiFlowNotFoundByCode() {
        return getMessage("BPM4");
    }

    public static String getNoAccessToCreateFlow() {
        return getMessage("BPM5");
    }

    public static String getTaskNotFoundById() {
        return getMessage("BPM6");
    }

    public static String getNodeNotFoundById() {
        return getMessage("BPM7");
    }

    public static String getNodeNotFoundByCode() {
        return getMessage("BPM8");
    }

    public static String getOutLinkNotFound() {
        return getMessage("BPM9");
    }

    public static String getNoAccessToCompleteTask() {
        return getMessage("BPM10");
    }

    public static String getUserTypeNone() {
        return getMessage("BPM11");
    }

    public static String getUserTypeAssistant() {
        return getMessage("BPM12");
    }

    public static String getUnsupportedNodeType(String nodeType) {
        return getMessage("BPM13", nodeType);
    }

    public static String getOpinionIsNull() {
        return getMessage("BPM14");
    }

    public static String getRuleNotFound(String ruleId) {
        return getMessage("BPM15", ruleId);
    }

    public static String getProcessInstanceNotFound() {
        return getMessage("BPM16");
    }

    public static String getFlowInstanceNotFound() {
        return getMessage("BPM17");
    }

    public static String getTaskFirstOpenBeHost() {
        return getMessage("BPM18");
    }

    public static String getNormalStartNodeNotFound() {
        return getMessage("BPM19");
    }

    public static String getFirstManualNodeNotFound() {
        return getMessage("BPM20");
    }

    public static String getFlowOutLinkNotFound(String id) {
        return getMessage("BPM21", id);
    }

    public static String getLinkToNodeNotFound(String id) {
        return getMessage("BPM22", id);
    }

    public static String getWfdVFlowVersionBeginDateIsNull(String id) {
        return getMessage("BPM23", id);
    }

    public static String getWfdVFlowVersionBeginDateError(LocalDateTime date) {
        return getMessage("BPM24", date.getYear(), date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
    }

    public static String getTaskInstanceNotFoundById(String id) {
        return getMessage("BPM25", id);
    }

    public static String getNoAccessToDeleteFlowInstance(String id) {
        return getMessage("BPM26", id);
    }

    public static String getLinkToNodeIdIsNull(String id) {
        return getMessage("BPM27", id);
    }

    public static String getSeqResetRuleNever() {
        return getMessage("BPM28");
    }

    public static String getSeqResetRuleYear() {
        return getMessage("BPM29");
    }

    public static String getSeqResetRuleMonth() {
        return getMessage("BPM30");
    }

    public static String getSeqResetRuleDay() {
        return getMessage("BPM31");
    }

    public static String getBpmnModelValidateError(String bpmn) {
        return getMessage("BPM32", bpmn);
    }

    public static String getNodeWaitForPassUsersIsNull(String id, String name, String code) {
        return getMessage("BPM33", id, name, code);
    }

    public static String getNodeWaitForPassCountIsNull(String id, String name, String code) {
        return getMessage("BPM34", id, name, code);
    }

    public static String getNodeWaitForPassPercentIsNull(String id, String name, String code) {
        return getMessage("BPM35", id, name, code);
    }

    public static String getDeleteCompletedFlowError(String id) {
        return getMessage("BPM36", id);
    }

    public static String getWfdVFlowVersionExist(Integer version) {
        return getMessage("BPM37", version);
    }

    public static String getWfdVFlowBiggerVersionExist(Integer version) {
        return getMessage("BPM38", version);
    }

    public static String getWfdVFlowSmallerVersionExist(Integer version) {
        return getMessage("BPM39", version);
    }

    public static String getWfdFlowNotFound(String id) {
        return getMessage("BPM40", id);
    }

    public static String getNodeCodeIsNull(String id) {
        return getMessage("BPM41", id);
    }

    public static String getNodeSameIdExist(String id) {
        return getMessage("BPM42", id);
    }

    public static String getNodeSameCodeExist(String code) {
        return getMessage("BPM43", code);
    }

    public static String getStartNodeCountError(Integer count) {
        return getMessage("BPM44", count);
    }

    public static String getUnsupportedFlowOutMode(Integer flowOutMode) {
        return getMessage("BPM45", flowOutMode);
    }

    public static String getWfdVFlowUsed(Integer version) {
        return getMessage("BPM46", version);
    }

    public static String getWfdVFlowVersionNotExist(Integer version) {
        return getMessage("BPM47", version);
    }

    public static String getNoAccessToQueryTask(String taskId) {
        return getMessage("BPM48", taskId);
    }

    public static String getTaskAlreadyClaimed(String taskId) {
        return getMessage("BPM49", taskId);
    }

    public static String getNoAccessToClaimTask(String taskId) {
        return getMessage("BPM50", taskId);
    }

    public static String getHostModeNotSupportClaim(Integer hostMode) {
        return getMessage("BPM51", hostMode);
    }

    public static String getHostModeNotSupportRevokeClaim(Integer hostMode) {
        return getMessage("BPM52", hostMode);
    }

    public static String getTaskUnclaimed(String taskId) {
        return getMessage("BPM53", taskId);
    }

    public static String getNoAccessToRevokeClaimTask(String taskId) {
        return getMessage("BPM54", taskId);
    }

    public static String getWfdFlowCategoryNotFound(String categoryId) {
        return getMessage("BPM55", categoryId);
    }

    public static String getWfdFlowSameSortNo(Integer sortNo) {
        return getMessage("BPM56", sortNo);
    }

    public static String getWfdFlowSameCode(String code) {
        return getMessage("BPM57", code);
    }

    public static String getTaskForceComplete() {
        return getMessage("BPM58");
    }

    public static String getTaskNotFoundByFlowInstanceId(String flowInstanceId) {
        return getMessage("BPM59", flowInstanceId);
    }

    public static String getBusinessKeyExist(String businessKey, String code) {
        return getMessage("BPM60", businessKey, code);
    }

    public static String getManualStartNodeNotFound(String code) {
        return getMessage("BPM61", code);
    }

    public static String getSysUserNotFoundById() {
        return getMessage("BPM62");
    }

    public static String getProcessDefinitionNotFoundById(String id) {
        return getMessage("BPM63", id);
    }

    public static String getSuperExecutionNotFound() {
        return getMessage("BPM64");
    }

    public static String getDeliverInfoNotFound() {
        return getMessage("BPM65");
    }

    public static String getNodeUsersNotFound(String nodeName, String nodeCode) {
        return getMessage("BPM66", nodeName, nodeCode);
    }

    public static String getUnsupportedFlowEventType(Integer flowEventType) {
        return getMessage("BPM67", flowEventType);
    }

    public static String getUnsupportedTaskEventType(Integer taskEventType) {
        return getMessage("BPM68", taskEventType);
    }

    public static String getFlowStarterNotFound(String starterId) {
        return getMessage("BPM69", starterId);
    }

    public static String getTaskNameSetFlowResult() {
        return getMessage("BPM70");
    }

    public static String getWfiDeliverNotFoundById(String deliverInfoId) {
        return getMessage("BPM71", deliverInfoId);
    }

    public static String getBpmEarlierThenDb(String bpmVersion, String dbVersion) {
        return getMessage("BPM72", bpmVersion, dbVersion);
    }

    public static String getSettingSyncVariantNameIsNull(String variantId) {
        return getMessage("BPM73", variantId);
    }

    public static String getVariantNotFound(String variantName, String flowId) {
        return getMessage("BPM74", variantName, flowId);
    }

    public static String getFlowVariantNotFound(String variantId, String flowId) {
        return getMessage("BPM75", variantId, flowId);
    }

    public static String getGlobalVariantNotFound(String globalVariantId, String flowId) {
        return getMessage("BPM76", globalVariantId, flowId);
    }

    public static String getUnsupportedVariantDataType(String variantDataType) {
        return getMessage("BPM77", variantDataType);
    }

    public static String getFormFieldNotFoundById(String formFieldId) {
        return getMessage("BPM78", formFieldId);
    }

    public static String getFlowStateRunning() {
        return getMessage("BPM79");
    }

    public static String getFlowStateEnded() {
        return getMessage("BPM80");
    }

    public static String getUnsupportedRevokeClaim(String taskId) {
        return getMessage("BPM81", taskId);
    }

    public static String getFlowCompleted() {
        return getMessage("BPM82");
    }

    public static String getWfdFlowFormDataTableIsNull(String flowInstanceId) {
        return getMessage("BPM83", flowInstanceId);
    }

    public static String getUnsupporttedAutoDeliver(String nodeId) {
        return getMessage("BPM84", nodeId);
    }

    public static String getDeliverUsersNotFound(String nodeName, String nodeCode) {
        return getMessage("BPM85", nodeName, nodeCode);
    }

    public static String getWfdGlobalVariantNotFound(String globalVariantId) {
        return getMessage("BPM86", globalVariantId);
    }

    public static String getHistoricActivitiInstanceNotFoundById(String activitiInstanceId) {
        return getMessage("BPM87", activitiInstanceId);
    }

    public static String getUnsupporttedEndActivityType(String endActivityType) {
        return getMessage("BPM88", endActivityType);
    }

    public static String getFlowOperationCreate() {
        return getMessage("BPM89");
    }

    public static String getFlowOperationCreateLog(String flowCode, String flowInstanceId) {
        return getMessage("BPM90", flowCode, flowInstanceId);
    }

    public static String getFlowOperationDeliver() {
        return getMessage("BPM91");
    }

    public static String getFlowOperationDeliverLog(String flowCode, String nodeId, String nodeName, String taskId) {
        return getMessage("BPM92", flowCode, nodeId, nodeName, taskId);
    }

    public static String getFlowOperationDelete() {
        return getMessage("BPM93");
    }

    public static String getFlowOperationDeleteLog(String flowCode, String flowInstanceId, String deleteReason) {
        return getMessage("BPM94", flowCode, flowInstanceId, deleteReason);
    }

    public static String getFlowOperationDeploy() {
        return getMessage("BPM95");
    }

    public static String getFlowOperationDeployLog(String flowCode, String flowName, Integer version) {
        return getMessage("BPM96", flowCode, flowName, version);
    }

    public static String getFlowOperationRevokeDeploy() {
        return getMessage("BPM97");
    }

    public static String getFlowOperationRevokeDeployLog(String flowCode, String flowName, Integer version) {
        return getMessage("BPM98", flowCode, flowName, version);
    }

    public static String getFlowOperationClaim() {
        return getMessage("BPM99");
    }

    public static String getFlowOperationClaimLog(String flowCode, String flowName, String taskId) {
        return getMessage("BPM100", flowCode, flowName, taskId);
    }

    public static String getFlowOperationRevokeClaim() {
        return getMessage("BPM101");
    }

    public static String getFlowOperationRevokeClaimLog(String flowCode, String flowName, String taskId) {
        return getMessage("BPM102", flowCode, flowName, taskId);
    }

    public static String getHistoricTaskInstanceNotFound(String taskId) {
        return getMessage("BPM103", taskId);
    }

    /**
     * 任务已办结，不能取消接收
     *
     * @param taskId 任务id
     * @return 消息
     * @author JonnyJiang
     * @date 2019/12/6 14:10
     */

    public static String getRevokeClaimTaskEnded(String taskId) {
        return getMessage("BPM104", taskId);
    }

    public static String getUserTypeHost() {
        return getMessage("BPM105");
    }

    public static String getUnsupportedFlowInMode(Integer flowInMode) {
        return getMessage("BPM106", flowInMode);
    }

    public static String getFlowOperation() {
        return getMessage("BPM107");
    }


    public static String getMessage(String key, Object... args) {
        if (args == null || args.length == 0) {
            return InternationUtils.getInternationalMsgOrigin("com.csicit.ace.bpm.resources.locale", key);
        }
        return InternationUtils.getInternationalMsgWithPath("com.csicit.ace.bpm.resources.locale", key, args);
    }

    /**
     * @param nodeId 节点id
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/12/9 17:04
     */

    public static String getFlowElementNotFound(String nodeId) {
        return getMessage("BPM108", nodeId);
    }

    public static String getHistoricTargetNodeTasksNotFound(String processInstanceId, String nodeId) {
        return getMessage("BPM109", processInstanceId, nodeId);
    }

    public static String getTargetNodeDataNotSupportRejectTo(String processInstanceId, String nodeId) {
        return getMessage("BPM110", processInstanceId, nodeId);
    }

    public static String getTargetTaskAssigneeNotFound(String taskId, String assignee) {
        return getMessage("BPM111", taskId, assignee);
    }

    public static String getWfiDeliverNotFound() {
        return getMessage("BPM112");
    }

    public static String getHistoricActivitiInstanceNotFoundByTaskId(String taskId) {
        return getMessage("BPM113", taskId);
    }

    public static String getTaskRejectToNotFound(String taskId) {
        return getMessage("BPM114", taskId);
    }

    public static String getFlowOperationReject() {
        return getMessage("BPM115");
    }

    public static String getFlowOperationRejectLog(String taskId, List<String> rejectToNodeNames) {
        return getMessage("BPM116", taskId, StringUtils.join(rejectToNodeNames, ","));
    }

    public static String getNodeNotEnableReject(String code, String name) {
        return getMessage("BPM117", code, name);
    }

    public static String getNoAccessToRejectTask(String taskId) {
        return getMessage("BPM118", taskId);
    }

    public static String getApiNodeNotFound(String code) {
        return getMessage("BPM119", code);
    }

    public static String getNodeHostOnly(String code, String name, int size) {
        return getMessage("BPM120", code, name, size);
    }

    public static String getDeliverNodeNotFoundByNodeId(String nodeId) {
        return getMessage("BPM121", nodeId);
    }

    public static String getUserTaskNotFoundByNodeId(String code, String nodeId) {
        return getMessage("BPM122", code, nodeId);
    }

    public static String getNodeUnsupportReject(String code, String name) {
        return getMessage("BPM123", code, name);
    }

    public static String getNoAccessToGetNodeInfo(String code, String businessKey) {
        return getMessage("BPM124", code, businessKey);
    }

    public static String getEnableAssistAhd() {
        return getMessage("BPM125");
    }

    public static String getMustClaimThenDeliver() {
        return getMessage("BPM126");
    }

    public static String getTaskAlreadyCompleted(String taskId) {
        return getMessage("BPM127", taskId);
    }

    public static String getWaitingForAssist() {
        return getMessage("BPM128");
    }

    public static String getSpecifiedUserUnfinished(List<SysUserDO> specifiedUsers) {
        String names = StringUtils.join(specifiedUsers.stream().map(SysUserDO::getRealName).collect(Collectors.toList()), "，");
        return getMessage("BPM129", names);
    }

    public static String getPassCountNotMatch(long finishedCnt, long waitForPassCount, String taskId) {
        return getMessage("BPM130", finishedCnt, waitForPassCount, taskId);
    }

    public static String getPassPercentNotMatch(Long finishedPercent, Integer waitForPassPercent, String taskId) {
        return getMessage("BPM131", finishedPercent, waitForPassPercent, taskId);
    }

    public static String getHostCntNotMatch(String nodeCode, String nodeName, long hostCnt, long cnt) {
        return getMessage("BPM132", hostCnt, nodeCode, nodeName, cnt);
    }

    public static String getNoSpecifiedHost(String nodeName, String nodeCode) {
        return getMessage("BPM133", nodeCode, nodeName);
    }

    public static String getSpecifiedHostNoAccess(String nodeName, String nodeCode, String realName) {
        return getMessage("BPM134", nodeCode, nodeName, realName);
    }

    public static String getNoAssistant(String nodeName, String nodeCode) {
        return getMessage("BPM135", nodeName);
    }

    public static String getDeleteInvalid(String flowId) {
        return getMessage("BPM136", flowId);
    }

    public static String getWfdGlobalRuleNotFound(String ruleId) {
        return getMessage("BPM137", ruleId);
    }

    public static String getProcessDefinitionNotFoundByKey(String processDefinitionKey) {
        return getMessage("BPM138", processDefinitionKey);
    }

    public static String getLinkFromNodeIdIsNull(String linkId) {
        return getMessage("BPM139", linkId);
    }

    public static String getFormFieldNotFoundByName(String name) {
        return getMessage("BPM140", name);
    }

    public static String getFirstFlowOutManualNode(String flowId) {
        return getMessage("BPM141", flowId);
    }

    public static String getElFlowInstanceIdNotFound() {
        return getMessage("BPM142");
    }

    public static String getElBusinessKeyNotFound() {
        return getMessage("BPM143");
    }

    public static String getElFlowNotFound() {
        return getMessage("BPM144");
    }

    public static String getDeploymentNotFound(String wfdVFlowId, String flowName) {
        return getMessage("BPM145", wfdVFlowId, flowName);
    }

    public static String getEffectiveWfdVFlowNotFoundByFlowId(String flowId) {
        return getMessage("BPM146", flowId);
    }

    public static String getReProcdefNotFound(String deploymentId, String wfdVFlowId) {
        return getMessage("BPM147", deploymentId, wfdVFlowId);
    }

    public static String getWfiBackupNotFoundByTaskId(String flowId, String taskId) {
        return getMessage("BPM148", flowId, taskId);
    }

    public static String getTaskCompleting(String nodeName) {
        return getMessage("BPM149", nodeName);
    }

    public static String getTaskRejecting(String nodeName) {
        return getMessage("BPM150", nodeName);
    }

    public static String getUnsupportedEngineVersion(String engineVersion) {
        return getMessage("BPM151", engineVersion);
    }

    public static String getOriginalHostNotFound() {
        return getMessage("BPM153");
    }

    public static String getNotMatchOriginalHost(String realName) {
        return getMessage("BPM154", realName);
    }

    public static String getNoHost(String nodeName, String nodeCode) {
        return getMessage("BPM155", nodeName, nodeCode);
    }

    public static String getTaskVariableNotFound(String taskId, TaskVariableName taskVariableName) {
        return getMessage("BPM156", taskId, taskVariableName.getName());
    }

    public static String getDeleteSucces() {
        return getMessage("BPM157");
    }

    public static String getNullOrExcepData() {
        return getMessage("BPM158");
    }

    public static String getDelegateSucces() {
        return getMessage("BPM159");
    }

    public static String getRevokeDeploySucceed() {
        return getMessage("BPM160");
    }

    public static String getWfiCommentNotFoundById() {
        return getMessage("BPM161");
    }

    public static String getWfiFocusWork() {
        return getMessage("BPM162");
    }

    public static String getLinkResultIsNull() {
        return getMessage("BPM163");
    }

    private static String getRealName(List<SysUserDO> users) {
        return StringUtils.join(users.stream().map(SysUserDO::getRealName).collect(Collectors.toList()), ",");
    }

    public static String getWaitForPassUsersUnFinished(List<SysUserDO> unfinishedUsers) {
        return getMessage("BPM164", getRealName(unfinishedUsers));
    }

    public static String getFinishedNodeUnsupportInvite() {
        return getMessage("BPM165");
    }

    public static String getDeliverUserExist(List<SysUserDO> users) {
        return getMessage("BPM166", getRealName(users));
    }

    public static String getWfiCommentIsDeleted() {
        return getMessage("BPM167");
    }

    public static String getTaskCommentCannotBeDeleted() {
        return getMessage("BPM168");
    }

    public static String getNoAccessToInvite() {
        return getMessage("BPM169");
    }

    public static String getNotAllowInvite() {
        return getMessage("BPM170");
    }

    public static String getUnsupportedMultiInstanceMode() {
        return getMessage("BPM171");
    }

    public static String getDeliverUserNotFoundByUserId() {
        return getMessage("BPM172");
    }

    public static String getNoAvailableNextStep() {
        return getMessage("BPM173");
    }

    public static String getHostFinished() {
        return getMessage("BPM174");
    }

    public static String getNotLatestFinishedTask() {
        return getMessage("BPM175");
    }

    public static String getNoAccessToWithdrawWork() {
        return getMessage("BPM176");
    }

    public static String getTaskNotEnded() {
        return getMessage("BPM177");
    }

    public static String getNodeAllowDelegate() {
        return getMessage("BPM178");
    }

    public static String getDeliverUserDuplicate() {
        return getMessage("BPM179");
    }

    public static String getForbiddenOperationType() {
        return getMessage("BPM180");
    }

    public static String getWfiTaskPendingNotFound() {
        return getMessage("BPM181");
    }

    public static String getUnsupportedUserType() {
        return getMessage("BPM182");
    }

    public static String getNoAdminAuth() {
        return getMessage("BPM183");
    }

    public static String getUnsupportedTaskPendingUserType() {
        return getMessage("BPM184");
    }

    public static String getNoTaskPendingException() {
        return getMessage("BPM185");
    }

    public static String getNotAllowHostDelete() {
        return getMessage("BPM186");
    }

    public static String getNotAllowAssitDelete() {
        return getMessage("BPM187");
    }

    public static String getTaskRejectToQuantityNotMatch() {
        return getMessage("BPM188");
    }

    public static String getRejectToNodeIsNotFirstTask() {
        return getMessage("BPM189");
    }

    public static String getFirstTaskNotFound() {
        return getMessage("BPM190");
    }

    public static String getTaskRejectIsNotSpecified() {
        return getMessage("BPM191");
    }

    public static String getNotAllowRejectToTask() {
        return getMessage("BPM192");
    }

    public static String getRejectToStepNotSpecified() {
        return getMessage("BPM193");
    }

    public static String getResultVarNameNotSpecified() {
        return getMessage("BPM194");
    }

    public static String getFlowInModeAll(Node node, Node fromNode) {
        return getMessage("BPM195", node.getName(), fromNode.getName(), node.getName(), node.getName());
    }

    public static String getSyncSettingFieldNameNotSpecified() {
        return getMessage("BPM196");
    }

    public static String getFormSaveOperationNotFound(String formSaveOperate) {
        return getMessage("BPM197", formSaveOperate);
    }

    public static String getUnsupportedVariableId() {
        return getMessage("BPM198");
    }

    public static String getWorkResultNotFound(String nodeCode, String nodeName, String workResult) {
        return getMessage("BPM199", nodeCode, nodeName, workResult);
    }

    public static String getFormSecretLevelIsNull() {
        return getMessage("BPM200");
    }

    public static String getUnsupportedFormSecretLevel() {
        return getMessage("BPM201");
    }

    public static String getDeliverUserSecretLevelNotMatch(String realName) {
        return getMessage("BPM202", realName);
    }

    public static String getWfiVFlowNotFound(Integer flowVersion) {
        return getMessage("BPM203", flowVersion);
    }

    public static String getWfiVFlowVersionObsolete() {
        return getMessage("BPM204");
    }

    public static String getNoAccessToPreset() {
        return getMessage("BPM205");
    }

    public static String getWfiRoutePresetNotFoundByTaskId() {
        return getMessage("BPM206");
    }

    public static String getMustPresetRoute() {
        return getMessage("BPM207");
    }

    public static String getLinkNotFoundById() {
        return getMessage("BPM208");
    }

    public static String getNodeFreeStepNotFoundById() {
        return getMessage("BPM209");
    }

    public static String getNodeFreeStepIdIsNull() {
        return getMessage("BPM210");
    }

    public static String getNodeFreeStepIsNotNextStep() {
        return getMessage("BPM211");
    }

    public static String getNodeTypeNotMatch() {
        return getMessage("BPM212");
    }

    public static String getNextNodeFreeStepNotFound() {
        return getMessage("BPM213");
    }

    public static String getNodeFreeStepNotSet(String nodeName) {
        return getMessage("BPM214", nodeName);
    }

    public static String getDeliverNodeFreeStepIdIsNull(String nodeName) {
        return getMessage("BPM215", nodeName);
    }

    public static String getSpecifiedHostNotMatch(String nodeName, String specifiedHost, String host) {
        return getMessage("BPM216", nodeName, host, specifiedHost);
    }

    public static String getDeliverUsersNotContainsWaitForPassUser(String nodeName, String realNames) {
        return getMessage("BPM217", nodeName, realNames);
    }

    public static String getWaitForPassCountNotEnough(String nodeName, Integer waitForPassCount, int deliverUserCount) {
        return getMessage("BPM218", nodeName, waitForPassCount, deliverUserCount);
    }

    public static String getFoundMoreThenOneLatestWfdVFlow() {
        return getMessage("BPM219");
    }

    public static String getLatestWfdVFlowNotFound() {
        return getMessage("BPM220");
    }

    public static String getFoundMoreThenOneEffectiveWfdVFlow() {
        return getMessage("BPM221");
    }

    public static String getPresetedRouteNodeNotFound() {
        return getMessage("BPM222");
    }

    public static String getAllowModifyPresetRouteByDescendantMustBeTrue(String nodeName) {
        return getMessage("BPM223", nodeName);
    }

    public static String getWfiRoutePresetNotFoundByPresetedNode(String nodeName) {
        return getMessage("BPM224", nodeName);
    }

    public static String getMustPresetRouteUser() {
        return getMessage("BPM225");
    }

    public static String getTargetNodeMustBePresetedNode(String nodeName) {
        return getMessage("BPM226", nodeName);
    }

    public static String getDeliverUserSortIndexIsNull(String userId, String realName) {
        return getMessage("BPM227", realName);
    }

    public static String getNodeNotAllowPresetRoute() {
        return getMessage("BPM228");
    }

    public static String getWfiRoutePresetNotFoundByFlowId() {
        return getMessage("BPM229");
    }

    public static String getFlowListenerNotFound() {
        return getMessage("BPM230");
    }

    public static String getTaskListenerNotFound() {
        return getMessage("BPM230");
    }
}