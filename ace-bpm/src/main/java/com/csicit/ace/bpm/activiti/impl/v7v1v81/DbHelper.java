package com.csicit.ace.bpm.activiti.impl.v7v1v81;

import com.csicit.ace.bpm.activiti.AbstractDbHelper;
import com.csicit.ace.bpm.activiti.BpmConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/4/13 15:24
 */
public class DbHelper extends AbstractDbHelper {
    public static DbHelper getInstance() {
        if (BpmConfiguration.getActualDatabaseType().equals(ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL)) {
            return new com.csicit.ace.bpm.activiti.impl.v7v1v81.mysql.DbHelper();
        }
        return new DbHelper();
    }

    @Override
    public void clear(String flowInstanceId) {
//        WfiFlowDO wfiFlow = queryUtils.getWfiFlowById(flowInstanceId);
//        WfdVFlowDO wfdVFlow = queryUtils.getWfdVFlowById(wfiFlow.getVFlowId());
//        Map<String, Object> deployment = wfiBackupService.getActReDeploymentByKey(wfdVFlow.getId(), wfdVFlow.getName());
//        if (deployment == null) {
//            throw new BpmException(LocaleUtils.getDeploymentNotFound(wfdVFlow.getId(), wfdVFlow.getName()));
//        }
//        String deploymentId = (String) deployment.get("ID_");
//        Map<String, Object> procdef = wfiBackupService.getActReProcdef(deploymentId, wfdVFlow.getFlowId());
//        if (procdef == null) {
//            throw new BpmException(LocaleUtils.getReProcdefNotFound(deploymentId, wfdVFlow.getId()));
//        }
        deleteWfiFlow(flowInstanceId);
        deleteWfiVFlow(flowInstanceId);
        deleteWfiDeliver(flowInstanceId);
        deleteWfiComment(flowInstanceId);
        List<String> actGeBytearrayIds = wfiBackupService.listActGeByteArrayIdsByRootProcInstId(flowInstanceId);
        deleteActRuVariable(flowInstanceId);
        actGeBytearrayService.deleteByIds(actGeBytearrayIds);
        deleteActRuIdentitylink(flowInstanceId);
        deleteActRuTask(flowInstanceId);
        deleteActRuExecution(flowInstanceId);
        deleteActHiVarinst(flowInstanceId);
        deleteActHiIdentitylink(flowInstanceId);
        deleteActHiComment(flowInstanceId);
        deleteActHiDetail(flowInstanceId);
        deleteActHiTaskinst(flowInstanceId);
        deleteActHiActinst(flowInstanceId);
        deleteActHiProcinst(flowInstanceId);
//        wfiBackupService.deleteActReProcdef(deploymentId, wfdVFlow.getFlowId());
//        wfiBackupService.deleteActReDeploymentByKey(wfdVFlow.getId(), wfdVFlow.getName());

    }

    protected void deleteWfiFlow(String rootProcInstId) {
        wfiBackupService.deleteWfiFlowByRootProcInstId(rootProcInstId);
    }

    protected void deleteWfiVFlow(String rootProcInstId) {
        wfiBackupService.deleteWfiVFlowByRootProcInstId(rootProcInstId);
    }

    protected void deleteWfiDeliver(String rootProcInstId) {
        wfiBackupService.deleteWfiDeliverByRootProcInstId(rootProcInstId);
    }

    private void deleteWfiComment(String rootProcInstId) {
        wfiBackupService.deleteWfiCommentByRootProcInstId(rootProcInstId);
    }

    protected void deleteActRuVariable(String rootProcInstId) {
        wfiBackupService.deleteActRuVariableByRootProcInstId(rootProcInstId);
    }

    protected void deleteActRuIdentitylink(String rootProcInstId) {
        wfiBackupService.deleteActRuIdentitylinkByRootProcInstId(rootProcInstId);
    }

    protected void deleteActRuTask(String rootProcInstId) {
        wfiBackupService.deleteActRuTaskByRootProcInstId(rootProcInstId);
    }

    protected void deleteActRuExecution(String rootProcInstId) {
        wfiBackupService.deleteActRuExecutionByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiVarinst(String rootProcInstId) {
        wfiBackupService.deleteActHiVarinstByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiIdentitylink(String rootProcInstId) {
        wfiBackupService.deleteActHiIdentitylinkByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiComment(String rootProcInstId) {
        wfiBackupService.deleteActHiCommentByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiDetail(String rootProcInstId) {
        wfiBackupService.deleteActHiDetailByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiTaskinst(String rootProcInstId) {
        wfiBackupService.deleteActHiTaskinstByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiActinst(String rootProcInstId) {
        wfiBackupService.deleteActHiActinstByRootProcInstId(rootProcInstId);
    }

    protected void deleteActHiProcinst(String rootProcInstId) {
        wfiBackupService.deleteActHiProcinstByRootProcInstId(rootProcInstId);
    }
}