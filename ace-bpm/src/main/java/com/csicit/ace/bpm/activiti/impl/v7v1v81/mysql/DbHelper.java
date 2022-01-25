package com.csicit.ace.bpm.activiti.impl.v7v1v81.mysql;

/**
 * @author JonnyJiang
 * @date 2020/5/6 9:28
 */
public class DbHelper extends com.csicit.ace.bpm.activiti.impl.v7v1v81.DbHelper {

    @Override
    protected void deleteWfiFlow(String rootProcInstId) {
        wfiBackupService.deleteMySqlWfiFlowByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteWfiDeliver(String rootProcInstId) {
        wfiBackupService.deleteMySqlWfiDeliverByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActRuVariable(String rootProcInstId) {
        wfiBackupService.deleteMySqlActRuVariableByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActRuIdentitylink(String rootProcInstId) {
        wfiBackupService.deleteMySqlActRuIdentitylinkByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActRuTask(String rootProcInstId) {
        wfiBackupService.deleteMySqlActRuTaskByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiVarinst(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiVarinstByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiIdentitylink(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiIdentitylinkByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiComment(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiCommentByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiDetail(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiDetailByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiTaskinst(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiTaskinstByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiActinst(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiActinstByRootProcInstId(rootProcInstId);
    }

    @Override
    protected void deleteActHiProcinst(String rootProcInstId) {
        wfiBackupService.deleteMySqlActHiProcinstByRootProcInstId(rootProcInstId);
    }
}
