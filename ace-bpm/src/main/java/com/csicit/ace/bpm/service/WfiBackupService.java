package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/1/17 10:08
 */
@Transactional
public interface WfiBackupService extends IBaseService<WfiBackupDO> {
    Map<String, Object> getActReDeploymentByKey(String wfvFlowId, String flowName);
//
//    List<Map<String, Object>> listActGeByteArrayByDeploymentId(String deploymentId);

    Map<String, Object> getActReProcdef(String deploymentId, String flowId);

    List<Map<String, Object>> listActRuExecutionByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActRuTaskByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiVarinstByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiTaskinstByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiProcinstByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiIdentityLinkByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiDetailsByRootProcInstId(String rootProcInstId);

//    List<Map<String,Object>> listStActHiDetailsByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiCommentByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listOracleActHiCommentByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listStActHiCommentByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listMySqlActHiCommentByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActHiActinstByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActRuVariableByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActRuIdentityLinksByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listActGeByteArrayByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listOracleActGeByteArrayByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listStActGeByteArrayByRootProcInstId(String rootProcInstId);

    List<Map<String, Object>> listMySqlActGeByteArrayByRootProcInstId(String rootProcInstId);

    List<String> listActGeByteArrayIdsByRootProcInstId(String rootProcInstId);

    List<WfiFlowDO> listWfiFlow(String rootProcInstId);

    List<WfiVFlowDO> listWfiVFlow(String rootProcInstId);

    List<WfiDeliverDO> listWfiDeliver(String rootProcInstId);

    List<WfiCommentDO> listWfiComment(String rootProcInstId);

    void deleteActReDeploymentByKey(String wfvFlowId, String flowName);

    void deleteActReProcdef(String deploymentId, String flowId);

    void deleteActRuExecutionByRootProcInstId(String rootProcInstId);

    void deleteActRuTaskByRootProcInstId(String rootProcInstId);

    void deleteActRuIdentitylinkByRootProcInstId(String rootProcInstId);

    void deleteActRuVariableByRootProcInstId(String rootProcInstId);

    void deleteActHiProcinstByRootProcInstId(String rootProcInstId);

    void deleteActHiTaskinstByRootProcInstId(String rootProcInstId);

    void deleteActHiActinstByRootProcInstId(String rootProcInstId);

    void deleteActHiCommentByRootProcInstId(String rootProcInstId);

    void deleteActHiDetailByRootProcInstId(String rootProcInstId);

    void deleteActHiIdentitylinkByRootProcInstId(String rootProcInstId);

    void deleteActHiVarinstByRootProcInstId(String rootProcInstId);

    void deleteWfiFlowByRootProcInstId(String rootProcInstId);

    void deleteWfiVFlowByRootProcInstId(String rootProcInstId);

    void deleteWfiDeliverByRootProcInstId(String rootProcInstId);

    void insertActReDeployment(Map<String, Object> actReDeployment);

    void insertActReProcdef(Map<String, Object> actReProcdef);

    void insertActGeBytearray(Map<String, Object> actGeBytearray);

    void insertOracleActGeBytearray(Map<String, Object> map);

    void insertMysqlActGeBytearray(Map<String, Object> map);

    void insertActRuExecution(Map<String, Object> actRuExecution);

    void insertActRuTask(Map<String, Object> actRuTask);

    void insertActRuIdentitylink(Map<String, Object> actRuIdentitylink);

    void insertActRuVariable(Map<String, Object> actRuVariable);

    void insertActHiProcinst(Map<String, Object> actHiProcinst);

    void insertActHiTaskinst(Map<String, Object> actHiTaskinst);

    void insertActHiActinst(Map<String, Object> actHiActinst);

    void insertActHiComment(Map<String, Object> actHiComment);

    void insertOracleActHiComment(Map<String, Object> map);

    void insertMysqlActHiCOmment(Map<String, Object> map);

    void insertActHiDetail(Map<String, Object> actHiDetail);

    void insertActHiIdentitylink(Map<String, Object> actHiIdentitylink);

    void insertActHiVarinst(Map<String, Object> actHiVarinst);

    void insertWfiFlow(Map<String, Object> wfiFlow);

    void insertWfiVFlow(Map<String, Object> wfiVFlow);

    void insertWfiDeliver(Map<String, Object> wfiDeliver);

    void insertWfiComment(Map<String, Object> wfiComment);

    void insertWfiBackup(Map<String, Object> map);

    void deleteMySqlWfiFlowByRootProcInstId(String rootProcInstId);

    void deleteMySqlWfiDeliverByRootProcInstId(String rootProcInstId);

    void deleteWfiCommentByRootProcInstId(String rootProcInstId);

    void deleteMySqlActRuVariableByRootProcInstId(String rootProcInstId);

    void deleteMySqlActRuIdentitylinkByRootProcInstId(String rootProcInstId);

    void deleteMySqlActRuTaskByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiVarinstByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiIdentitylinkByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiCommentByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiDetailByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiTaskinstByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiActinstByRootProcInstId(String rootProcInstId);

    void deleteMySqlActHiProcinstByRootProcInstId(String rootProcInstId);

//    void deleteActGeBytearray(List<ActGeBytearrayDO> actGeBytearrays);
}