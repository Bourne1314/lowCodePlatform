package com.csicit.ace.bpm.service.impl;

import com.csicit.ace.bpm.mapper.WfiBackupMapper;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.service.WfiBackupService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/1/17 10:10
 */
@Service
public class WfiBackupServiceImpl extends BaseServiceImpl<WfiBackupMapper, WfiBackupDO> implements WfiBackupService {
//    @Autowired
//    private ActGeBytearrayService actGeBytearrayService;

    @Override
    public Map<String, Object> getActReDeploymentByKey(String wfvFlowId, String flowName) {
        return baseMapper.getActReDeploymentByKey(wfvFlowId, flowName);
    }
//
//    @Override
//    public List<Map<String, Object>> listActGeByteArrayByDeploymentId(String deploymentId) {
//        return resolveActGeBytearrays(baseMapper.listActGeByteArrayByDeploymentId(deploymentId));
//    }

    @Override
    public Map<String, Object> getActReProcdef(String deploymentId, String flowId) {

        return baseMapper.getActReProcdef(deploymentId, flowId);
    }

    @Override
    public List<Map<String, Object>> listActRuExecutionByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActRuExecutionByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActRuTaskByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActRuTaskByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActHiVarinstByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActHiVarinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActHiTaskinstByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActHiTaskinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActHiProcinstByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActHiProcinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActHiIdentityLinkByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActHiIdentityLinkByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActHiDetailsByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActHiDetailsByRootProcInstId(rootProcInstId);
    }
//
//    @Override
//    public List<Map<String, Object>> listStActHiDetailsByRootProcInstId(String rootProcInstId) {
//        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActHiDetailDO> list = baseMapper.listStActHiDetailsByRootProcInstId(rootProcInstId);
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActHiDetailDO item : list) {
//            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActHiDetail(item.getId(), item.getType(), item.getProcInstId(), item.getExecutionId(), item.getTaskId(), item.getActInstId(), item.getName(), item.getVarType(), item.getRev(), item.getTime(), item.getBytearrayId(), item.getDoubleValue(), item.getLongValue(), item.getText(), item.getText2()));
//        }
//        return result;
//    }

    @Override
    public List<Map<String, Object>> listActHiCommentByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActHiCommentDO> list = baseMapper.listActHiCommentByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActHiCommentDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActHiComment(item.getId(), item.getType(), item.getTime(), item.getUserId(), item.getTaskId(), item.getProcInstId(), item.getAction(), item.getMessage(), item.getFullMsg()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listMySqlActHiCommentByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql.ActHiCommentDO> list = baseMapper.listMySqlActHiCommentByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql.ActHiCommentDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActHiComment(item.getId(), item.getType(), item.getTime(), item.getUserId(), item.getTaskId(), item.getProcInstId(), item.getAction(), item.getMessage(), item.getFullMsg()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listOracleActHiCommentByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.oracle.ActHiCommentDO> list = baseMapper.listOracleActHiCommentByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.oracle.ActHiCommentDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActHiComment(item.getId(), item.getType(), item.getTime(), item.getUserId(), item.getTaskId(), item.getProcInstId(), item.getAction(), item.getMessage(), item.getFullMsg()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listStActHiCommentByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActHiCommentDO> list = baseMapper.listStActHiCommentByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActHiCommentDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActHiComment(item.getId(), item.getType(), item.getTime(), item.getUserId(), item.getTaskId(), item.getProcInstId(), item.getAction(), item.getMessage(), item.getFullMsg()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listActHiActinstByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActHiActinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActRuVariableByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActRuVariableByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActRuIdentityLinksByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActRuIdentityLinksByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<Map<String, Object>> listActGeByteArrayByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActGeBytearrayDO> list = baseMapper.listActGeBytearraysByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActGeBytearrayDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActGeBytearray(item.getId(), item.getRev(), item.getName(), item.getDeploymentId(), item.getBytes(), item.getGenerated()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listOracleActGeByteArrayByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.oracle.ActGeBytearrayDO> list = baseMapper.listOracleActGeBytearraysByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.oracle.ActGeBytearrayDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActGeBytearray(item.getId(), item.getRev(), item.getName(), item.getDeploymentId(), item.getBytes(), item.getGenerated()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listStActGeByteArrayByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActGeBytearrayDO> list = baseMapper.listStActGeBytearraysByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.st.ActGeBytearrayDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActGeBytearray(item.getId(), item.getRev(), item.getName(), item.getDeploymentId(), item.getBytes(), item.getGenerated()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listMySqlActGeByteArrayByRootProcInstId(String rootProcInstId) {
        List<com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql.ActGeBytearrayDO> list = baseMapper.listMySqlActGeBytearraysByRootProcInstId(rootProcInstId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql.ActGeBytearrayDO item : list) {
            result.add(com.csicit.ace.bpm.activiti.impl.v7v1v81.History.resolveActGeBytearray(item.getId(), item.getRev(), item.getName(), item.getDeploymentId(), item.getBytes(), item.getGenerated()));
        }
        return result;
    }

    @Override
    public List<String> listActGeByteArrayIdsByRootProcInstId(String rootProcInstId) {
        return baseMapper.listActGeBytearrayIdsByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<WfiFlowDO> listWfiFlow(String rootProcInstId) {
        return baseMapper.listWfiFlowByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<WfiVFlowDO> listWfiVFlow(String rootProcInstId) {
        return baseMapper.listWfiVFlowByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<WfiDeliverDO> listWfiDeliver(String rootProcInstId) {
        return baseMapper.listWfiDeliverByRootProcInstId(rootProcInstId);
    }

    @Override
    public List<WfiCommentDO> listWfiComment(String rootProcInstId) {
        return baseMapper.listWfiCommentByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActReDeploymentByKey(String wfvFlowId, String flowName) {
        baseMapper.deleteActReDeploymentByKey(wfvFlowId, flowName);
    }

    @Override
    public void deleteActReProcdef(String deploymentId, String flowId) {
        baseMapper.deleteActReProcdef(deploymentId, flowId);
    }

    @Override
    public void deleteActRuExecutionByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActRuExecutionByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActRuTaskByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActRuTaskByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActRuIdentitylinkByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActRuIdentitylinkByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActRuVariableByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActRuVariableByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiProcinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiProcinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiTaskinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiTaskinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiActinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiActinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiCommentByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiCommentByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiDetailByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiDetailByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiIdentitylinkByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiIdentitylinkByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteActHiVarinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteActHiVarinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteWfiFlowByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteWfiFlowByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteWfiVFlowByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteWfiVFlowByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteWfiDeliverByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteWfiDeliverByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteWfiCommentByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteWfiCommentByRootProcInstId(rootProcInstId);
    }

    @Override
    public void insertActReDeployment(Map<String, Object> actReDeployment) {
        baseMapper.insertActReDeployment(actReDeployment);
    }

    @Override
    public void insertActReProcdef(Map<String, Object> actReProcdef) {
        baseMapper.insertActReProcdef(actReProcdef);
    }

    @Override
    public void insertActGeBytearray(Map<String, Object> actGeBytearray) {
        baseMapper.insertActGeBytearray(actGeBytearray);
    }

    @Override
    public void insertOracleActGeBytearray(Map<String, Object> map) {
        baseMapper.insertOracleActGeBytearray(map);
    }

    @Override
    public void insertMysqlActGeBytearray(Map<String, Object> map) {
        baseMapper.insertMysqlActGeBytearray(map);
    }

    @Override
    public void insertActRuExecution(Map<String, Object> actRuExecution) {
        baseMapper.insertActRuExecution(actRuExecution);
    }

    @Override
    public void insertActRuTask(Map<String, Object> actRuTask) {
        baseMapper.insertActRuTask(actRuTask);
    }

    @Override
    public void insertActRuIdentitylink(Map<String, Object> actRuIdentitylink) {
        baseMapper.insertActRuIdentitylink(actRuIdentitylink);
    }

    @Override
    public void insertActRuVariable(Map<String, Object> actRuVariable) {
        baseMapper.insertActRuVariable(actRuVariable);
    }

    @Override
    public void insertActHiProcinst(Map<String, Object> actHiProcinst) {
        baseMapper.insertActHiProcinst(actHiProcinst);
    }

    @Override
    public void insertActHiTaskinst(Map<String, Object> actHiTaskinst) {
        baseMapper.insertActHiTaskinst(actHiTaskinst);
    }

    @Override
    public void insertActHiActinst(Map<String, Object> actHiActinst) {
        baseMapper.insertActHiActinst(actHiActinst);
    }

    @Override
    public void insertActHiComment(Map<String, Object> actHiComment) {
        baseMapper.insertActHiComment(actHiComment);
    }

    @Override
    public void insertOracleActHiComment(Map<String, Object> map) {
        baseMapper.insertOracleActHiComment(map);
    }

    @Override
    public void insertMysqlActHiCOmment(Map<String, Object> map) {
        baseMapper.insertMysqlActHiComment(map);
    }

    @Override
    public void insertActHiDetail(Map<String, Object> actHiDetail) {
        baseMapper.insertActHiDetail(actHiDetail);
    }

    @Override
    public void insertActHiIdentitylink(Map<String, Object> actHiIdentitylink) {
        baseMapper.insertActHiIdentitylink(actHiIdentitylink);
    }

    @Override
    public void insertActHiVarinst(Map<String, Object> actHiVarinst) {
        baseMapper.insertActHiVarinst(actHiVarinst);
    }

    @Override
    public void insertWfiFlow(Map<String, Object> wfiFlow) {
        baseMapper.insertWfiFlow(wfiFlow);
    }

    @Override
    public void insertWfiVFlow(Map<String, Object> wfiVFlow) {
        baseMapper.insertWfiVFlow(wfiVFlow);
    }

    @Override
    public void insertWfiDeliver(Map<String, Object> wfiDeliver) {
        baseMapper.insertWfiDeliver(wfiDeliver);
    }

    @Override
    public void insertWfiComment(Map<String, Object> wfiComment) {
        baseMapper.insertWfiComment(wfiComment);
    }

    @Override
    public void insertWfiBackup(Map<String, Object> map) {
        baseMapper.insertWfiBackup(map);
    }

    @Override
    public void deleteMySqlWfiFlowByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlWfiFlowByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlWfiDeliverByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlWfiDeliverByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActRuVariableByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActRuVariableByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActRuIdentitylinkByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActRuIdentitylinkByRootProcInstId(rootProcInstId);

    }

    @Override
    public void deleteMySqlActRuTaskByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActRuTaskByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiVarinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiVarinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiIdentitylinkByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiIdentitylinkByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiCommentByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiCommentByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiDetailByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiDetailByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiTaskinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiTaskinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiActinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiActinstByRootProcInstId(rootProcInstId);
    }

    @Override
    public void deleteMySqlActHiProcinstByRootProcInstId(String rootProcInstId) {
        baseMapper.deleteMySqlActHiProcinstByRootProcInstId(rootProcInstId);
    }

//
//    @Override
//    public void deleteActGeBytearray(List<ActGeBytearrayDO> actGeBytearrays) {
//        actGeBytearrayService.removeByIds(actGeBytearrays.stream().map(ActGeBytearrayDO::getId).collect(Collectors.toList()));
//    }
//
//    private List<Map<String, Object>> resolveActGeBytearrays(List<Map<String, Object>> list) {
//        for (Map<String, Object> map : list) {
//            resolveBlob(map, History.ACT_GE_BYTEARRAY_BYTES);
//        }
//        return list;
//    }

//    private List<Map<String, Object>> resolveActHiComment(List<Map<String, Object>> list) {
//        for (Map<String, Object> map : list) {
//            resolveBlob(map, History.ACT_HI_COMMENT_FULL_MSG);
//        }
//        return list;
//    }
//
//    private List<Map<String, Object>> resolveWfifLOW(List<Map<String, Object>> list) {
//        for (Map<String, Object> map : list) {
//            resolveClob(map, History.WFI_FLOW_MODEL);
//        }
//        return list;
//    }

//    private void resolveBlob(Map<String, Object> map, String... keys) {
//        for (String key : keys) {
//            Object obj = map.get(key);
//            if (obj != null) {
//                if (obj instanceof Blob) {
//                    Blob val = (Blob) obj;
//                    try {
//                        byte[] bytes = val.getBytes(1, (int) val.length());
//                        map.put(key, new String(bytes));
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    private void resolveClob(Map<String, Object> map, String... keys) {
//        for (String key : keys) {
//            Object obj = map.get(key);
//            if (obj != null) {
//                if (obj instanceof Clob) {
//                    Clob val = (Clob) obj;
//                    try {
//                        map.put(key, val.getSubString(1, (int) val.length()));
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}