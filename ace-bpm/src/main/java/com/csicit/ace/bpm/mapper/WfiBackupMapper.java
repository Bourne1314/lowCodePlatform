package com.csicit.ace.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActGeBytearrayDO;
import com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActHiCommentDO;
import com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActHiDetailDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/1/17 9:57
 */
@Mapper
public interface WfiBackupMapper extends BaseMapper<WfiBackupDO> {
    /**
     * 获取流程发布
     *
     * @param wfdVFlowId 流程定义版本id
     * @param flowName   流程名称
     * @return 流程发布
     */
    @Select("SELECT * FROM ACT_RE_DEPLOYMENT WHERE KEY_=#{wfdVFlowId} AND NAME_=#{flowName}")
    Map<String, Object> getActReDeploymentByKey(@Param("wfdVFlowId") String wfdVFlowId, @Param("flowName") String flowName);

    @Delete("DELETE FROM ACT_RE_DEPLOYMENT WHERE KEY_=#{wfdVFlowId} AND NAME_=#{flowName}")
    void deleteActReDeploymentByKey(@Param("wfdVFlowId") String wfdVFlowId, @Param("flowName") String flowName);

    /**
     * 获取流程定义
     *
     * @param deploymentId 流程发布id
     * @param flowId       流程id
     * @return 流程定义                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     * @author JonnyJiang
     * @date 2020/1/19 15:35
     */
    @Select("SELECT * FROM ACT_RE_PROCDEF WHERE DEPLOYMENT_ID_=#{deploymentId} AND KEY_=#{flowId}")
    Map<String, Object> getActReProcdef(@Param("deploymentId") String deploymentId, @Param("flowId") String flowId);

    @Select("DELETE FROM ACT_RE_PROCDEF WHERE DEPLOYMENT_ID_=#{deploymentId} AND KEY_=#{flowId}")
    void deleteActReProcdef(@Param("deploymentId") String deploymentId, @Param("flowId") String flowId);

    /**
     * 获取执行流列表
     *
     * @param rootProcInstId 根实例id
     * @return 执行流列表
     * @author JonnyJiang
     * @date 2020/1/19 16:40
     */

    @Select("SELECT * FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId} OR PROC_INST_ID_=#{rootProcInstId} ORDER BY START_TIME_")
    List<Map<String, Object>> listActRuExecutionByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId} OR PROC_INST_ID_=#{rootProcInstId}")
    void deleteActRuExecutionByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_RU_TASK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId}) ORDER BY CREATE_TIME_")
    List<Map<String, Object>> listActRuTaskByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_TASK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})")
    void deleteActRuTaskByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_TASK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActRuTaskByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_RU_IDENTITYLINK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})")
    List<Map<String, Object>> listActRuIdentityLinksByRootProcInstId(String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_IDENTITYLINK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})")
    void deleteActRuIdentitylinkByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_IDENTITYLINK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActRuIdentitylinkByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})")
    List<Map<String, Object>> listActRuVariableByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})")
    void deleteActRuVariableByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActRuVariableByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_ACTINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY START_TIME_")
    List<Map<String, Object>> listActHiActinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_ACTINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiActinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_ACTINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiActinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_COMMENT WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY TIME_")
    List<ActHiCommentDO> listActHiCommentByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_COMMENT WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY TIME_")
    List<com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql.ActHiCommentDO> listMySqlActHiCommentByRootProcInstId(String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_COMMENT WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY TIME_")
    List<com.csicit.ace.bpm.pojo.vo.v7v1v81.oracle.ActHiCommentDO> listOracleActHiCommentByRootProcInstId(String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_COMMENT WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY TIME_")
    List<com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActHiCommentDO> listStActHiCommentByRootProcInstId(String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_COMMENT WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiCommentByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_COMMENT WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiCommentByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY TIME_")
    List<Map<String, Object>> listActHiDetailsByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);
//
//    @Select("SELECT * FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY TIME_")
//    List<ActHiDetailDO> listStActHiDetailsByRootProcInstId(String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiDetailByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiDetailByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY CREATE_TIME_")
    List<Map<String, Object>> listActHiVarinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiVarinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiVarinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_TASKINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY START_TIME_")
    List<Map<String, Object>> listActHiTaskinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_TASKINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiTaskinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_TASKINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiTaskinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) ORDER BY START_TIME_")
    List<Map<String, Object>> listActHiProcinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiProcinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiProcinstByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_HI_IDENTITYLINK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    List<Map<String, Object>> listActHiIdentityLinkByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_IDENTITYLINK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteActHiIdentitylinkByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_HI_IDENTITYLINK WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlActHiIdentitylinkByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM ACT_GE_BYTEARRAY WHERE ID_=ANY((SELECT BYTEARRAY_ID_ FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})))")
    void deleteActGeBytearrayByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_GE_BYTEARRAY WHERE DEPLOYMENT_ID_=#{deploymentId}")
    List<Map<String, Object>> listActGeByteArrayByDeploymentId(@Param("deploymentId") String deploymentId);

    @Select("SELECT * FROM WFI_FLOW WHERE ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}))")
    List<WfiFlowDO> listWfiFlowByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM WFI_V_FLOW WHERE FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}))")
    List<WfiVFlowDO> listWfiVFlowByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM WFI_FLOW WHERE ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}))")
    void deleteWfiFlowByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM WFI_V_FLOW WHERE FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}))")
    void deleteWfiVFlowByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM WFI_FLOW WHERE ID=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) T)")
    void deleteMySqlWfiFlowByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM WFI_DELIVER WHERE FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) ORDER BY DELIVER_TIME")
    List<WfiDeliverDO> listWfiDeliverByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM WFI_COMMENT WHERE FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) ORDER BY COMMENT_TIME")
    List<WfiCommentDO> listWfiCommentByRootProcInstId(String rootProcInstId);

    @Delete("DELETE FROM WFI_COMMENT WHERE ID=ID AND FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteWfiCommentByRootProcInstId(String rootProcInstId);

    @Select("SELECT * FROM ACT_GE_BYTEARRAY WHERE ID_=ANY((SELECT BYTEARRAY_ID_ FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})))")
    List<ActGeBytearrayDO> listActGeBytearraysByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_GE_BYTEARRAY WHERE ID_=ANY((SELECT BYTEARRAY_ID_ FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})))")
    List<com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql.ActGeBytearrayDO> listMySqlActGeBytearraysByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_GE_BYTEARRAY WHERE ID_=ANY((SELECT BYTEARRAY_ID_ FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})))")
    List<com.csicit.ace.bpm.pojo.vo.v7v1v81.oracle.ActGeBytearrayDO> listOracleActGeBytearraysByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT * FROM ACT_GE_BYTEARRAY WHERE ID_=ANY((SELECT BYTEARRAY_ID_ FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId})))")
    List<com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActGeBytearrayDO> listStActGeBytearraysByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Select("SELECT X.BYTEARRAY_ID_ FROM ((SELECT BYTEARRAY_ID_ FROM ACT_HI_DETAIL WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_HI_VARINST WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})) union (SELECT BYTEARRAY_ID_ FROM ACT_RU_VARIABLE WHERE PROC_INST_ID_=ANY(SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE ROOT_PROC_INST_ID_=#{rootProcInstId}))) X WHERE X.BYTEARRAY_ID_ IS NOT NULL")
    List<String> listActGeBytearrayIdsByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM WFI_DELIVER WHERE ID=ID AND FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId})")
    void deleteWfiDeliverByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Delete("DELETE FROM WFI_DELIVER WHERE ID=ID AND FLOW_ID=ANY(SELECT PROC_INST_ID_ FROM (SELECT PROC_INST_ID_ FROM ACT_HI_PROCINST WHERE PROC_INST_ID_=#{rootProcInstId} OR SUPER_PROCESS_INSTANCE_ID_=#{rootProcInstId}) T)")
    void deleteMySqlWfiDeliverByRootProcInstId(@Param("rootProcInstId") String rootProcInstId);

    @Insert("INSERT INTO ACT_RE_DEPLOYMENT(ID_,NAME_,CATEGORY_,KEY_,TENANT_ID_,DEPLOY_TIME_,ENGINE_VERSION_) VALUES(#{ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{CATEGORY_,jdbcType=VARCHAR},#{KEY_,jdbcType=VARCHAR},#{TENANT_ID_,jdbcType=VARCHAR},#{DEPLOY_TIME_,jdbcType=TIMESTAMP},#{ENGINE_VERSION_,jdbcType=VARCHAR})")
    void insertActReDeployment(Map<String, Object> deployment);

    @Insert("INSERT INTO ACT_RE_PROCDEF(ID_,REV_,CATEGORY_,NAME_,KEY_,VERSION_,DEPLOYMENT_ID_,RESOURCE_NAME_,DGRM_RESOURCE_NAME_,DESCRIPTION_,HAS_START_FORM_KEY_,HAS_GRAPHICAL_NOTATION_,SUSPENSION_STATE_,TENANT_ID_,ENGINE_VERSION_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{CATEGORY_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{KEY_,jdbcType=VARCHAR},#{VERSION_,jdbcType=INTEGER},#{DEPLOYMENT_ID_,jdbcType=VARCHAR},#{RESOURCE_NAME_,jdbcType=VARCHAR},#{DGRM_RESOURCE_NAME_,jdbcType=VARCHAR},#{DESCRIPTION_,jdbcType=VARCHAR},#{HAS_START_FORM_KEY_,jdbcType=INTEGER},#{HAS_GRAPHICAL_NOTATION_,jdbcType=INTEGER},#{SUSPENSION_STATE_,jdbcType=INTEGER},#{TENANT_ID_,jdbcType=VARCHAR},#{ENGINE_VERSION_,jdbcType=VARCHAR})")
    void insertActReProcdef(Map<String, Object> actReProcdef);

    @Insert("INSERT INTO ACT_GE_BYTEARRAY(ID_,REV_,NAME_,DEPLOYMENT_ID_,BYTES_,GENERATED_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{NAME_,jdbcType=VARCHAR},#{DEPLOYMENT_ID_,jdbcType=VARCHAR},#{BYTES_,jdbcType=VARCHAR},#{GENERATED_,jdbcType=INTEGER})")
    void insertActGeBytearray(Map<String, Object> actGeBytearray);

    @Insert("INSERT INTO ACT_GE_BYTEARRAY(ID_,REV_,NAME_,DEPLOYMENT_ID_,BYTES_,GENERATED_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{NAME_,jdbcType=VARCHAR},#{DEPLOYMENT_ID_,jdbcType=VARCHAR},#{BYTES_,jdbcType=BLOB},#{GENERATED_,jdbcType=INTEGER})")
    void insertMysqlActGeBytearray(Map<String, Object> map);

    @Insert("INSERT INTO ACT_GE_BYTEARRAY(ID_,REV_,NAME_,DEPLOYMENT_ID_,BYTES_,GENERATED_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{NAME_,jdbcType=VARCHAR},#{DEPLOYMENT_ID_,jdbcType=VARCHAR},#{BYTES_,jdbcType=BLOB},#{GENERATED_,jdbcType=INTEGER})")
    void insertOracleActGeBytearray(Map<String, Object> map);

    @Insert("INSERT INTO ACT_RU_EXECUTION(ID_,REV_,PROC_INST_ID_,BUSINESS_KEY_,PARENT_ID_,PROC_DEF_ID_,SUPER_EXEC_,ROOT_PROC_INST_ID_,ACT_ID_,IS_ACTIVE_,IS_CONCURRENT_,IS_SCOPE_,IS_EVENT_SCOPE_,IS_MI_ROOT_,SUSPENSION_STATE_,CACHED_ENT_STATE_,TENANT_ID_,NAME_,START_TIME_,START_USER_ID_,LOCK_TIME_,IS_COUNT_ENABLED_,EVT_SUBSCR_COUNT_,TASK_COUNT_,TIMER_JOB_COUNT_,SUSP_JOB_COUNT_,DEADLETTER_JOB_COUNT_,VAR_COUNT_,ID_LINK_COUNT_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{PROC_INST_ID_,jdbcType=VARCHAR},#{BUSINESS_KEY_,jdbcType=VARCHAR},#{PARENT_ID_,jdbcType=VARCHAR},#{PROC_DEF_ID_,jdbcType=VARCHAR},#{SUPER_EXEC_,jdbcType=VARCHAR},#{ROOT_PROC_INST_ID_,jdbcType=VARCHAR},#{ACT_ID_,jdbcType=VARCHAR},#{IS_ACTIVE_,jdbcType=INTEGER},#{IS_CONCURRENT_,jdbcType=INTEGER},#{IS_SCOPE_,jdbcType=INTEGER},#{IS_EVENT_SCOPE_,jdbcType=INTEGER},#{IS_MI_ROOT_,jdbcType=INTEGER},#{SUSPENSION_STATE_,jdbcType=INTEGER},#{CACHED_ENT_STATE_,jdbcType=INTEGER},#{TENANT_ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{START_TIME_,jdbcType=TIMESTAMP},#{START_USER_ID_,jdbcType=VARCHAR},#{LOCK_TIME_,jdbcType=TIMESTAMP},#{IS_COUNT_ENABLED_,jdbcType=INTEGER},#{EVT_SUBSCR_COUNT_,jdbcType=INTEGER},#{TASK_COUNT_,jdbcType=INTEGER},#{TIMER_JOB_COUNT_,jdbcType=INTEGER},#{SUSP_JOB_COUNT_,jdbcType=INTEGER},#{DEADLETTER_JOB_COUNT_,jdbcType=INTEGER},#{VAR_COUNT_,jdbcType=INTEGER},#{ID_LINK_COUNT_,jdbcType=INTEGER})")
    void insertActRuExecution(Map<String, Object> actRuExecution);

    @Insert("insert into ACT_RU_TASK(ID_, REV_, EXECUTION_ID_, PROC_INST_ID_, PROC_DEF_ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, TASK_DEF_KEY_, OWNER_, ASSIGNEE_, DELEGATION_, PRIORITY_, CREATE_TIME_, DUE_DATE_, CATEGORY_, SUSPENSION_STATE_, TENANT_ID_, FORM_KEY_, CLAIM_TIME_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{EXECUTION_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{PROC_DEF_ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{PARENT_TASK_ID_,jdbcType=VARCHAR},#{DESCRIPTION_,jdbcType=VARCHAR},#{TASK_DEF_KEY_,jdbcType=VARCHAR},#{OWNER_,jdbcType=VARCHAR},#{ASSIGNEE_,jdbcType=VARCHAR},#{DELEGATION_,jdbcType=VARCHAR},#{PRIORITY_,jdbcType=INTEGER},#{CREATE_TIME_,jdbcType=TIMESTAMP},#{DUE_DATE_,jdbcType=TIMESTAMP},#{CATEGORY_,jdbcType=VARCHAR},#{SUSPENSION_STATE_,jdbcType=INTEGER},#{TENANT_ID_,jdbcType=VARCHAR},#{FORM_KEY_,jdbcType=VARCHAR},#{CLAIM_TIME_,jdbcType=TIMESTAMP})")
    void insertActRuTask(Map<String, Object> actRuTask);

    @Insert("insert into ACT_RU_IDENTITYLINK(ID_, REV_, GROUP_ID_, TYPE_, USER_ID_, TASK_ID_, PROC_INST_ID_, PROC_DEF_ID_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{GROUP_ID_,jdbcType=VARCHAR},#{TYPE_,jdbcType=VARCHAR},#{USER_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{PROC_DEF_ID_,jdbcType=VARCHAR})")
    void insertActRuIdentitylink(Map<String, Object> actRuIdentitylink);

    @Insert("insert into ACT_RU_VARIABLE(ID_, REV_, TYPE_, NAME_, EXECUTION_ID_, PROC_INST_ID_, TASK_ID_, BYTEARRAY_ID_, DOUBLE_, LONG_, TEXT_, TEXT2_) VALUES(#{ID_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{TYPE_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{EXECUTION_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{BYTEARRAY_ID_,jdbcType=VARCHAR},#{DOUBLE_,jdbcType=DOUBLE},#{LONG_,jdbcType=DOUBLE},#{TEXT_,jdbcType=VARCHAR},#{TEXT2_,jdbcType=VARCHAR})")
    void insertActRuVariable(Map<String, Object> actRuVariable);

    @Insert("insert into ACT_HI_PROCINST(ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, START_TIME_, END_TIME_, DURATION_, START_USER_ID_, START_ACT_ID_, END_ACT_ID_, SUPER_PROCESS_INSTANCE_ID_, DELETE_REASON_, TENANT_ID_, NAME_) VALUES(#{ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{BUSINESS_KEY_,jdbcType=VARCHAR},#{PROC_DEF_ID_,jdbcType=VARCHAR},#{START_TIME_,jdbcType=TIMESTAMP},#{END_TIME_,jdbcType=TIMESTAMP},#{DURATION_,jdbcType=DOUBLE},#{START_USER_ID_,jdbcType=VARCHAR},#{START_ACT_ID_,jdbcType=VARCHAR},#{END_ACT_ID_,jdbcType=VARCHAR},#{SUPER_PROCESS_INSTANCE_ID_,jdbcType=VARCHAR},#{DELETE_REASON_,jdbcType=VARCHAR},#{TENANT_ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR})")
    void insertActHiProcinst(Map<String, Object> actHiProcinst);

    @Insert("insert into ACT_HI_TASKINST(ID_, PROC_DEF_ID_, TASK_DEF_KEY_, PROC_INST_ID_, EXECUTION_ID_, PARENT_TASK_ID_, NAME_, DESCRIPTION_, OWNER_, ASSIGNEE_, START_TIME_, CLAIM_TIME_, END_TIME_, DURATION_, DELETE_REASON_, PRIORITY_, DUE_DATE_, FORM_KEY_, CATEGORY_, TENANT_ID_) VALUES(#{ID_,jdbcType=VARCHAR},#{PROC_DEF_ID_,jdbcType=VARCHAR},#{TASK_DEF_KEY_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{EXECUTION_ID_,jdbcType=VARCHAR},#{PARENT_TASK_ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{DESCRIPTION_,jdbcType=VARCHAR},#{OWNER_,jdbcType=VARCHAR},#{ASSIGNEE_,jdbcType=VARCHAR},#{START_TIME_,jdbcType=TIMESTAMP},#{CLAIM_TIME_,jdbcType=TIMESTAMP},#{END_TIME_,jdbcType=TIMESTAMP},#{DURATION_,jdbcType=DOUBLE},#{DELETE_REASON_,jdbcType=VARCHAR},#{PRIORITY_,jdbcType=DOUBLE},#{DUE_DATE_,jdbcType=TIMESTAMP},#{FORM_KEY_,jdbcType=VARCHAR},#{CATEGORY_,jdbcType=VARCHAR},#{TENANT_ID_,jdbcType=VARCHAR})")
    void insertActHiTaskinst(Map<String, Object> actHiTaskinst);

    @Insert("insert into ACT_HI_ACTINST(ID_, PROC_DEF_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, DELETE_REASON_, TENANT_ID_) VALUES(#{ID_,jdbcType=VARCHAR},#{PROC_DEF_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{EXECUTION_ID_,jdbcType=VARCHAR},#{ACT_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{CALL_PROC_INST_ID_,jdbcType=VARCHAR},#{ACT_NAME_,jdbcType=VARCHAR},#{ACT_TYPE_,jdbcType=VARCHAR},#{ASSIGNEE_,jdbcType=VARCHAR},#{START_TIME_,jdbcType=TIMESTAMP},#{END_TIME_,jdbcType=TIMESTAMP},#{DURATION_,jdbcType=DOUBLE},#{DELETE_REASON_,jdbcType=VARCHAR},#{TENANT_ID_,jdbcType=VARCHAR})")
    void insertActHiActinst(Map<String, Object> actHiActinst);

    @Insert("insert into ACT_HI_COMMENT(ID_, TYPE_, TIME_, USER_ID_, TASK_ID_, PROC_INST_ID_, ACTION_, MESSAGE_, FULL_MSG_) VALUES(#{ID_,jdbcType=VARCHAR},#{TYPE_,jdbcType=VARCHAR},#{TIME_,jdbcType=TIMESTAMP},#{USER_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{ACTION_,jdbcType=VARCHAR},#{MESSAGE_,jdbcType=VARCHAR},#{FULL_MSG_,jdbcType=BLOB})")
    void insertActHiComment(Map<String, Object> actHiComment);

    @Insert("insert into ACT_HI_COMMENT(ID_, TYPE_, TIME_, USER_ID_, TASK_ID_, PROC_INST_ID_, ACTION_, MESSAGE_, FULL_MSG_) VALUES(#{ID_,jdbcType=VARCHAR},#{TYPE_,jdbcType=VARCHAR},#{TIME_,jdbcType=TIMESTAMP},#{USER_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{ACTION_,jdbcType=VARCHAR},#{MESSAGE_,jdbcType=VARCHAR},#{FULL_MSG_,jdbcType=BLOB})")
    void insertOracleActHiComment(Map<String, Object> map);

    @Insert("insert into ACT_HI_COMMENT(ID_, TYPE_, TIME_, USER_ID_, TASK_ID_, PROC_INST_ID_, ACTION_, MESSAGE_, FULL_MSG_) VALUES(#{ID_,jdbcType=VARCHAR},#{TYPE_,jdbcType=VARCHAR},#{TIME_,jdbcType=TIMESTAMP},#{USER_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{ACTION_,jdbcType=VARCHAR},#{MESSAGE_,jdbcType=VARCHAR},#{FULL_MSG_,jdbcType=BLOB})")
    void insertMysqlActHiComment(Map<String, Object> map);

    @Insert("insert into ACT_HI_DETAIL(ID_, TYPE_, PROC_INST_ID_, EXECUTION_ID_, TASK_ID_, ACT_INST_ID_, NAME_, VAR_TYPE_, REV_, TIME_, BYTEARRAY_ID_, DOUBLE_, LONG_, TEXT_, TEXT2_)  VALUES(#{ID_,jdbcType=VARCHAR},#{TYPE_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{EXECUTION_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{ACT_INST_ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{VAR_TYPE_,jdbcType=VARCHAR},#{REV_,jdbcType=VARCHAR},#{TIME_,jdbcType=VARCHAR},#{BYTEARRAY_ID_,jdbcType=VARCHAR},#{DOUBLE_,jdbcType=VARCHAR},#{LONG_,jdbcType=VARCHAR},#{TEXT_,jdbcType=VARCHAR},#{TEXT2_,jdbcType=VARCHAR})")
    void insertActHiDetail(Map<String, Object> actHiDetail);

    @Insert("insert into ACT_HI_IDENTITYLINK(ID_, GROUP_ID_, TYPE_, USER_ID_, TASK_ID_, PROC_INST_ID_) VALUES(#{ID_,jdbcType=VARCHAR},#{GROUP_ID_,jdbcType=VARCHAR},#{TYPE_,jdbcType=VARCHAR},#{USER_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR})")
    void insertActHiIdentitylink(Map<String, Object> actHiIdentitylink);

    @Insert("insert into ACT_HI_VARINST(ID_, PROC_INST_ID_, EXECUTION_ID_, TASK_ID_, NAME_, VAR_TYPE_, REV_, BYTEARRAY_ID_, DOUBLE_, LONG_, TEXT_, TEXT2_, CREATE_TIME_, LAST_UPDATED_TIME_) VALUES(#{ID_,jdbcType=VARCHAR},#{PROC_INST_ID_,jdbcType=VARCHAR},#{EXECUTION_ID_,jdbcType=VARCHAR},#{TASK_ID_,jdbcType=VARCHAR},#{NAME_,jdbcType=VARCHAR},#{VAR_TYPE_,jdbcType=VARCHAR},#{REV_,jdbcType=INTEGER},#{BYTEARRAY_ID_,jdbcType=VARCHAR},#{DOUBLE_,jdbcType=DOUBLE},#{LONG_,jdbcType=DOUBLE},#{TEXT_,jdbcType=VARCHAR},#{TEXT2_,jdbcType=VARCHAR},#{CREATE_TIME_,jdbcType=TIMESTAMP},#{LAST_UPDATED_TIME_,jdbcType=TIMESTAMP})")
    void insertActHiVarinst(Map<String, Object> actHiVarinst);

    @Insert("insert into WFI_FLOW(ID, V_FLOW_ID, MODEL, BUSINESS_KEY, FLOW_NO, FLOW_ID, FLOW_CODE, APP_ID) VALUES(#{ID,jdbcType=VARCHAR},#{V_FLOW_ID,jdbcType=VARCHAR},#{MODEL,jdbcType=CLOB},#{BUSINESS_KEY,jdbcType=VARCHAR},#{FLOW_NO,jdbcType=VARCHAR},#{FLOW_ID,jdbcType=VARCHAR},#{FLOW_CODE,jdbcType=VARCHAR},#{APP_ID,jdbcType=VARCHAR})")
    void insertWfiFlow(Map<String, Object> wfiFlow);

    @Insert("insert into WFI_V_FLOW(ID, FLOW_ID, MODEL, BPMN, FLOW_VERSION, VERSION_END_DATE) VALUES(#{ID,jdbcType=VARCHAR},#{FLOW_ID,jdbcType=VARCHAR},#{MODEL,jdbcType=CLOB},#{BPMN,jdbcType=CLOB},#{FLOW_VERSION,jdbcType=INTEGER},#{VERSION_END_DATE,jdbcType=TIMESTAMP})")
    void insertWfiVFlow(Map<String, Object> wfiFlow);

    @Insert("insert into WFI_DELIVER(ID, DELIVER_INFO, FLOW_ID, USER_ID, DELIVER_TIME) VALUES(#{ID,jdbcType=VARCHAR},#{DELIVER_INFO,jdbcType=CLOB},#{FLOW_ID,jdbcType=VARCHAR},#{USER_ID,jdbcType=VARCHAR},#{DELIVER_TIME,jdbcType=TIMESTAMP})")
    void insertWfiDeliver(Map<String, Object> wfiDeliver);

    @Insert("insert into WFI_COMMENT(ID, COMMENT_USER_ID, COMMENT_USER, REPLY_COMMENT_ID, FLOW_ID, COMMENT_TIME, TASK_ID, COMMENT_TEXT, APP_ID, REPLY_USER_ID, REPLY_USER, USER_TYPE) VALUES(#{ID,jdbcType=VARCHAR}, #{COMMENT_USER_ID,jdbcType=VARCHAR}, #{COMMENT_USER,jdbcType=VARCHAR}, #{REPLY_COMMENT_ID,jdbcType=VARCHAR}, #{FLOW_ID,jdbcType=VARCHAR}, #{COMMENT_TIME,jdbcType=TIMESTAMP}, #{TASK_ID,jdbcType=VARCHAR}, #{COMMENT_TEXT,jdbcType=VARCHAR}, #{APP_ID,jdbcType=VARCHAR}, #{REPLY_USER_ID,jdbcType=VARCHAR}, #{REPLY_USER,jdbcType=VARCHAR}, #{USER_TYPE,jdbcType=INTEGER})")
    void insertWfiComment(Map<String, Object> wfiComment);

    @Insert("INSERT INTO WFI_BACKUP(ID,FLOW_ID,DESCRIPTION,VERSION,BACKUP_TIME,BACKUP_DATA,TASK_ID,APP_ID,ENGINE_VERSION) VALUES(#{ID,jdbcType=VARCHAR},#{FLOW_ID,jdbcType=VARCHAR},#{DESCRIPTION,jdbcType=VARCHAR},#{VERSION,jdbcType=INTEGER},#{BACKUP_TIME,jdbcType=TIMESTAMP},#{BACKUP_DATA,jdbcType=CLOB},#{TASK_ID,jdbcType=VARCHAR},#{APP_ID,jdbcType=VARCHAR},#{ENGINE_VERSION,jdbcType=VARCHAR})")
    void insertWfiBackup(Map<String, Object> map);


//    @Insert("DELETE FROM ACT_GE_BYTEARRAY WHERE ID_ IN (${ids})")
//    void deleteActGeBytearrayByIds(@Param("ids") String ids);
}