package com.csicit.ace.bpm.activiti.impl.v7v1v81;

import com.csicit.ace.bpm.DbUpdateTask;
import com.csicit.ace.bpm.activiti.AbstractDbHelper;
import com.csicit.ace.bpm.activiti.AbstractHistory;
import com.csicit.ace.bpm.activiti.BpmConfiguration;
import com.csicit.ace.bpm.enums.TableName;
import com.csicit.ace.bpm.pojo.domain.WfiBackupDO;
import com.csicit.ace.bpm.pojo.domain.WfiCommentDO;
import com.csicit.ace.common.utils.StringUtils;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author JonnyJiang
 * @date 2020/1/17 11:56
 */
public class History extends AbstractHistory {
    public static final String ENGINE_VERSION = "7.1.81";
    protected static final String[] ACT_GE_BYTEARRAY_COLUMNS = new String[]{"ID_", "REV_", "NAME_", "DEPLOYMENT_ID_", "BYTES_", "GENERATED_"};
    protected static final String[] ACT_HI_ACTINST_COLUMNS = new String[]{"ID_", "PROC_DEF_ID_", "PROC_INST_ID_", "EXECUTION_ID_", "ACT_ID_", "TASK_ID_", "CALL_PROC_INST_ID_", "ACT_NAME_", "ACT_TYPE_", "ASSIGNEE_", "START_TIME_", "END_TIME_", "DURATION_", "DELETE_REASON_", "TENANT_ID_"};
    protected static final String[] ACT_HI_COMMENT_COLUMNS = new String[]{"ID_", "TYPE_", "TIME_", "USER_ID_", "TASK_ID_", "PROC_INST_ID_", "ACTION_", "MESSAGE_", "FULL_MSG_"};
    protected static final String[] ACT_HI_DETAIL_COLUMNS = new String[]{"ID_", "TYPE_", "PROC_INST_ID_", "EXECUTION_ID_", "TASK_ID_", "ACT_INST_ID_", "NAME_", "VAR_TYPE_", "REV_", "TIME_", "BYTEARRAY_ID_", "DOUBLE_", "LONG_", "TEXT_", "TEXT2_"};
    protected static final String[] ACT_HI_IDENTITYLINK_COLUMNS = new String[]{"ID_", "GROUP_ID_", "TYPE_", "USER_ID_", "TASK_ID_", "PROC_INST_ID_"};
    protected static final String[] ACT_HI_PROCINST_COLUMNS = new String[]{"ID_", "PROC_INST_ID_", "BUSINESS_KEY_", "PROC_DEF_ID_", "START_TIME_", "END_TIME_", "DURATION_", "START_USER_ID_", "START_ACT_ID_", "END_ACT_ID_", "SUPER_PROCESS_INSTANCE_ID_", "DELETE_REASON_", "TENANT_ID_", "NAME_"};
    protected static final String[] ACT_HI_TASKINST_COLUMNS = new String[]{"ID_", "PROC_DEF_ID_", "TASK_DEF_KEY_", "PROC_INST_ID_", "EXECUTION_ID_", "PARENT_TASK_ID_", "NAME_", "DESCRIPTION_", "OWNER_", "ASSIGNEE_", "START_TIME_", "CLAIM_TIME_", "END_TIME_", "DURATION_", "DELETE_REASON_", "PRIORITY_", "DUE_DATE_", "FORM_KEY_", "CATEGORY_", "TENANT_ID_"};
    protected static final String[] ACT_HI_VARINST_COLUMNS = new String[]{"ID_", "PROC_INST_ID_", "EXECUTION_ID_", "TASK_ID_", "NAME_", "VAR_TYPE_", "REV_", "BYTEARRAY_ID_", "DOUBLE_", "LONG_", "TEXT_", "TEXT2_", "CREATE_TIME_", "LAST_UPDATED_TIME_"};
    protected static final String[] ACT_RU_EXECUTION_COLUMNS = new String[]{"ID_", "REV_", "PROC_INST_ID_", "BUSINESS_KEY_", "PARENT_ID_", "PROC_DEF_ID_", "SUPER_EXEC_", "ROOT_PROC_INST_ID_", "ACT_ID_", "IS_ACTIVE_", "IS_CONCURRENT_", "IS_SCOPE_", "IS_EVENT_SCOPE_", "IS_MI_ROOT_", "SUSPENSION_STATE_", "CACHED_ENT_STATE_", "TENANT_ID_", "NAME_", "START_TIME_", "START_USER_ID_", "LOCK_TIME_", "IS_COUNT_ENABLED_", "EVT_SUBSCR_COUNT_", "TASK_COUNT_", "JOB_COUNT_", "TIMER_JOB_COUNT_", "SUSP_JOB_COUNT_", "DEADLETTER_JOB_COUNT_", "VAR_COUNT_", "ID_LINK_COUNT_"};
    protected static final String[] ACT_RU_IDENTITYLINK_COLUMNS = new String[]{"ID_", "REV_", "GROUP_ID_", "TYPE_", "USER_ID_", "TASK_ID_", "PROC_INST_ID_", "PROC_DEF_ID_"};
    protected static final String[] ACT_RU_TASK_COLUMNS = new String[]{"ID_", "REV_", "EXECUTION_ID_", "PROC_INST_ID_", "PROC_DEF_ID_", "NAME_", "PARENT_TASK_ID_", "DESCRIPTION_", "TASK_DEF_KEY_", "OWNER_", "ASSIGNEE_", "DELEGATION_", "PRIORITY_", "CREATE_TIME_", "DUE_DATE_", "CATEGORY_", "SUSPENSION_STATE_", "TENANT_ID_", "FORM_KEY_", "CLAIM_TIME_"};
    protected static final String[] ACT_RU_VARIABLE_COLUMNS = new String[]{"ID_", "REV_", "TYPE_", "NAME_", "EXECUTION_ID_", "PROC_INST_ID_", "TASK_ID_", "BYTEARRAY_ID_", "DOUBLE_", "LONG_", "TEXT_", "TEXT2_"};
    protected static final String[] WFI_DELIVER_COLUMNS = new String[]{"ID", "DELIVER_INFO", "FLOW_ID", "USER_ID", "DELIVER_TIME"};
    protected static final String[] WFI_COMMENT_COLUMNS = new String[]{"ID", "COMMENT_USER_ID", "COMMENT_USER", "REPLY_COMMENT_ID", "FLOW_ID", "COMMENT_TIME", "TASK_ID", "COMMENT_TEXT", "APP_ID", "REPLY_USER_ID", "REPLY_USER", "USER_TYPE"};
    protected static final String[] WFI_FLOW_COLUMNS = new String[]{"ID", "V_FLOW_ID", "MODEL", "BUSINESS_KEY", "FLOW_NO", "FLOW_ID", "FLOW_CODE", "APP_ID"};
    protected static final String[] WFI_V_FLOW_COLUMNS = new String[]{"ID", "FLOW_ID", "MODEL", "BPMN", "FLOW_VERSION", "VERSION_END_DATE"};
    protected static final String ACT_RU_EXECUTION_START_TIME = "START_TIME_";
    protected static final String ACT_GE_BYTEARRAY_ID = "ID_";
    protected static final String ACT_GE_BYTEARRAY_REV = "REV_";
    protected static final String ACT_GE_BYTEARRAY_NAME = "NAME_";
    protected static final String ACT_GE_BYTEARRAY_DEPLOYMENT_ID = "DEPLOYMENT_ID_";
    protected static final String ACT_GE_BYTEARRAY_BYTES = "BYTES_";
    protected static final String ACT_GE_BYTEARRAY_GENERATED = "GENERATED_";
    protected static final String ACT_RU_EXECUTION_ID = "ID_";
    protected static final String ACT_RU_EXECUTION_PROC_INST_ID = "PROC_INST_ID_";
    protected static final String ACT_RU_TASK_ID = "ID_";
    protected static final String ACT_RU_IDENTITYLINK_ID = "ID_";
    protected static final String ACT_HI_ACTINST_DELETE_REASON = "DELETE_REASON_";
    protected static final String ACT_HI_COMMENT_ID = "ID_";
    protected static final String ACT_HI_COMMENT_TYPE = "TYPE_";
    protected static final String ACT_HI_COMMENT_TIME = "TIME_";
    protected static final String ACT_HI_COMMENT_USER_ID = "USER_ID_";
    protected static final String ACT_HI_COMMENT_TASK_ID = "TASK_ID_";
    protected static final String ACT_HI_COMMENT_PROC_INST_ID = "PROC_INST_ID_";
    protected static final String ACT_HI_COMMENT_ACTION = "ACTION_";
    protected static final String ACT_HI_COMMENT_MESSAGE = "MESSAGE_";
    protected static final String ACT_HI_COMMENT_FULL_MSG = "FULL_MSG_";
    protected static final String ACT_HI_VARINST_TEXT = "TEXT_";
    protected static final String ACT_HI_VARINST_TEXT2 = "TEXT2_";
    protected static final String ACT_HI_DETAIL_TEXT = "TEXT_";
    protected static final String ACT_HI_DETAIL_TEXT2 = "TEXT2_";
    protected static final String ACT_HI_PROCINST_DELETE_REASON = "DELETE_REASON_";
    protected static final String ACT_HI_TASKINST_DESCRIPTION = "DESCRIPTION_";
    protected static final String ACT_HI_TASKINST_DELETE_REASON = "DELETE_REASON_";
    protected static final String ACT_RU_TASK_DESCRIPTION = "DESCRIPTION_";
    protected static final String ACT_RU_VARIABLE_TEXT = "TEXT_";
    protected static final String ACT_RU_VARIABLE_TEXT2 = "TEXT2_";
    protected static final String WFI_FLOW_FLOW_CODE = "FLOW_CODE";
    protected static final String WFI_FLOW_APP_ID = "APP_ID";
    protected static final String WFI_FLOW_ID = "ID";
    protected static final String WFI_FLOW_V_FLOW_ID = "V_FLOW_ID";
    protected static final String WFI_FLOW_MODEL = "MODEL";
    protected static final String WFI_FLOW_BUSINESS_KEY = "BUSINESS_KEY";
    protected static final String WFI_FLOW_FLOW_NO = "FLOW_NO";
    protected static final String WFI_FLOW_FLOW_ID = "FLOW_ID";
    protected static final String WFI_V_FLOW_ID = "ID";
    protected static final String WFI_V_FLOW_FLOW_ID = "FLOW_ID";
    protected static final String WFI_V_FLOW_MODEL = "MODEL";
    protected static final String WFI_V_FLOW_BPMN = "BPMN";
    protected static final String WFI_V_FLOW_FLOW_VERSION = "FLOW_VERSION";
    protected static final String WFI_V_FLOW_VERSION_END_DATE = "VERSION_END_DATE";
    protected static final String WFI_DELIVER_ID = "ID";
    protected static final String WFI_DELIVER_DELIVER_INFO = "DELIVER_INFO";
    protected static final String WFI_DELIVER_FLOW_ID = "FLOW_ID";
    protected static final String WFI_DELIVER_USER_ID = "USER_ID";
    protected static final String WFI_DELIVER_DELIVER_TIME = "DELIVER_TIME";
    protected static final String WFI_BACKUP_BACKUP_DATA = "BACKUP_DATA";
    protected static final String WFI_BACKUP_ID = "ID";
    protected static final String WFI_BACKUP_FLOW_ID = "FLOW_ID";
    protected static final String WFI_BACKUP_DESCRIPTION = "DESCRIPTION";
    protected static final String WFI_BACKUP_VERSION = "VERSION";
    protected static final String WFI_BACKUP_BACKUP_TIME = "BACKUP_TIME";
    protected static final String WFI_BACKUP_TASK_ID = "TASK_ID";
    protected static final String WFI_BACKUP_APP_ID = "APP_ID";
    protected static final String WFI_BACKUP_ENGINE_VERSION = "ENGINE_VERSION";
    protected static final String WFI_COMMENT_ID = "ID";
    protected static final String WFI_COMMENT_COMMENT_USER_ID = "COMMENT_USER_ID";
    protected static final String WFI_COMMENT_COMMENT_USER = "COMMENT_USER";
    protected static final String WFI_COMMENT_REPLY_COMMENT_ID = "REPLY_COMMENT_ID";
    protected static final String WFI_COMMENT_FLOW_ID = "FLOW_ID";
    protected static final String WFI_COMMENT_COMMENT_TIME = "COMMENT_TIME";
    protected static final String WFI_COMMENT_TASK_ID = "TASK_ID";
    protected static final String WFI_COMMENT_COMMENT_TEXT = "COMMENT_TEXT";
    protected static final String WFI_COMMENT_APP_ID = "APP_ID";
    protected static final String WFI_COMMENT_REPLY_USER_ID = "REPLY_USER_ID";
    protected static final String WFI_COMMENT_REPLY_USER = "REPLY_USER";
    protected static final String WFI_COMMENT_USER_TYPE = "USER_TYPE";

    public History(WfiBackupDO wfiBackup) {
        super(wfiBackup);
    }

    /**
     * 临时变量，避免重复申请内存
     */
    private static com.oscar.jdbc.OscarClob tClob;
    /**
     * 临时变量，避免重复申请内存
     */
    private static Object tClobObj;

    private static void resolveStClob(Map<String, Object> map, String key) {
        tClobObj = map.get(key);
        if (tClobObj == null) {
            map.put(key, "");
        } else {
            if (tClobObj instanceof com.oscar.jdbc.OscarClob) {
                tClob = (com.oscar.jdbc.OscarClob) tClobObj;
                map.put(key, tClob.toString());
            } else {
                map.put(key, tClobObj.toString());
            }
        }
    }

    public static void resolveStActHiDetails(List<Map<String, Object>> hiDetails) {
        for (Map<String, Object> item : hiDetails) {
            resolveStClob(item, ACT_HI_DETAIL_TEXT);
            resolveStClob(item, ACT_HI_DETAIL_TEXT2);
        }
    }

    public static void resolveStActHiVarinsts(List<Map<String, Object>> hiVarinsts) {
        for (Map<String, Object> item : hiVarinsts) {
            resolveStClob(item, ACT_HI_VARINST_TEXT);
            resolveStClob(item, ACT_HI_VARINST_TEXT2);
        }
    }

    public static void resolveStActHiActinsts(List<Map<String, Object>> hiActinsts) {
        for (Map<String, Object> item : hiActinsts) {
            resolveStClob(item, ACT_HI_ACTINST_DELETE_REASON);
        }
    }

    public static void resolveStActHiTaskinsts(List<Map<String, Object>> hiTaskinsts) {
        for (Map<String, Object> item : hiTaskinsts) {
            resolveStClob(item, ACT_HI_TASKINST_DESCRIPTION);
            resolveStClob(item, ACT_HI_TASKINST_DELETE_REASON);
        }
    }

    public static void resolveStActRuTasks(List<Map<String, Object>> ruTasks) {
        for (Map<String, Object> item : ruTasks) {
            resolveStClob(item, ACT_RU_TASK_DESCRIPTION);
        }
    }

    public static void resolveStActRuVariables(List<Map<String, Object>> ruVariables) {
        for (Map<String, Object> item : ruVariables) {
            resolveStClob(item, ACT_RU_VARIABLE_TEXT);
            resolveStClob(item, ACT_RU_VARIABLE_TEXT2);
        }
    }

    public static void resolveStActHiProcinsts(List<Map<String, Object>> hiProcinsts) {
        for (Map<String, Object> item : hiProcinsts) {
            resolveStClob(item, ACT_HI_PROCINST_DELETE_REASON);
        }
    }

    @Override
    protected AbstractDbHelper initDbHelper() {
        return DbHelper.getInstance();
    }

    private Boolean exist(List<String> keyCache, String key) {
        for (String t : keyCache) {
            if (t.equals(key)) {
                return true;
            }
        }
        keyCache.add(key);
        return false;
    }

    @Override
    public void recoverData(Map<String, Object> data) {
        dbHelper.clear(wfiBackup.getFlowId());
//        Map<String, Object> actReDeployment = (Map<String, Object>) data.get(ACT_RE_DEPLOYMENT);
//        if (actReDeployment != null) {
//            wfiBackupService.insertActReDeployment(actReDeployment);
//        }
//        Map<String, Object> actReProcdef = (Map<String, Object>) data.get(ACT_RE_PROCDEF);
//        if (actReProcdef != null) {
//            wfiBackupService.insertActReProcdef(actReProcdef);
//        }
        List<String> keyCache = new ArrayList<>();
        String key;
        List<Map<String, Object>> actGeBytearrays = (List<Map<String, Object>>) data.get(TableName.ACT_GE_BYTEARRAY);
        if (actGeBytearrays != null) {
            for (Map<String, Object> map : actGeBytearrays) {
                key = (String) map.get(ACT_GE_BYTEARRAY_ID);
                if (exist(keyCache, key)) {
                    continue;
                }
                resolveFields(map, ACT_GE_BYTEARRAY_COLUMNS);
                insertActGeBytearray(map);
            }
        }
        keyCache.clear();
        List<Map<String, Object>> actRuExecutions = (List<Map<String, Object>>) data.get(TableName.ACT_RU_EXECUTION);
        if (actRuExecutions != null) {
            actRuExecutions.sort((o1, o2) -> actRuExecutionSort(o1, o2, ACT_RU_EXECUTION_START_TIME));
            for (Map<String, Object> map : actRuExecutions) {
                key = (String) map.get(ACT_RU_EXECUTION_ID);
                if (exist(keyCache, key)) {
                    continue;
                }
                resolveFields(map, ACT_RU_EXECUTION_COLUMNS);
                wfiBackupService.insertActRuExecution(map);
            }
        }
        keyCache.clear();
        List<Map<String, Object>> actRuTasks = (List<Map<String, Object>>) data.get(TableName.ACT_RU_TASK);
        if (actRuTasks != null) {
            for (Map<String, Object> map : actRuTasks) {
                key = (String) map.get(ACT_RU_TASK_ID);
                if (exist(keyCache, key)) {
                    continue;
                }
                resolveFields(map, ACT_RU_TASK_COLUMNS);
                wfiBackupService.insertActRuTask(map);
            }
        }
        List<Map<String, Object>> actRuIdentitylinks = (List<Map<String, Object>>) data.get(TableName.ACT_RU_IDENTITYLINK);
        if (actRuIdentitylinks != null) {
            for (Map<String, Object> map : actRuIdentitylinks) {
                key = (String) map.get(ACT_RU_IDENTITYLINK_ID);
                if (exist(keyCache, key)) {
                    continue;
                }
                resolveFields(map, ACT_RU_IDENTITYLINK_COLUMNS);
                wfiBackupService.insertActRuIdentitylink(map);
            }
        }
        List<Map<String, Object>> actRuVariables = (List<Map<String, Object>>) data.get(TableName.ACT_RU_VARIABLE);
        if (actRuVariables != null) {
            for (Map<String, Object> map : actRuVariables) {
                key = (String) map.get(ACT_RU_IDENTITYLINK_ID);
                if (exist(keyCache, key)) {
                    continue;
                }
                resolveFields(map, ACT_RU_VARIABLE_COLUMNS);
                wfiBackupService.insertActRuVariable(map);
            }
        }
        List<Map<String, Object>> actHiProcinsts = (List<Map<String, Object>>) data.get(TableName.ACT_HI_PROCINST);
        if (actHiProcinsts != null) {
            for (Map<String, Object> map : actHiProcinsts) {
                resolveFields(map, ACT_HI_PROCINST_COLUMNS);
                wfiBackupService.insertActHiProcinst(map);
            }
        }
        List<Map<String, Object>> actHiTaskinsts = (List<Map<String, Object>>) data.get(TableName.ACT_HI_TASKINST);
        if (actHiTaskinsts != null) {
            for (Map<String, Object> map : actHiTaskinsts) {
                resolveFields(map, ACT_HI_TASKINST_COLUMNS);
                wfiBackupService.insertActHiTaskinst(map);
            }
        }
        List<Map<String, Object>> actHiActinsts = (List<Map<String, Object>>) data.get(TableName.ACT_HI_ACTINST);
        if (actHiActinsts != null) {
            for (Map<String, Object> map : actHiActinsts) {
                resolveFields(map, ACT_HI_ACTINST_COLUMNS);
                wfiBackupService.insertActHiActinst(map);
            }
        }
        List<Map<String, Object>> actHiComments = (List<Map<String, Object>>) data.get(TableName.ACT_HI_COMMENT);
        if (actHiComments != null) {
            for (Map<String, Object> map : actHiComments) {
                resolveFields(map, ACT_HI_COMMENT_COLUMNS);
                insertActHiComment(map);
            }
        }
        List<Map<String, Object>> actHiDetails = (List<Map<String, Object>>) data.get(TableName.ACT_HI_DETAIL);
        if (actHiDetails != null) {
            for (Map<String, Object> map : actHiDetails) {
                resolveFields(map, ACT_HI_DETAIL_COLUMNS);
                wfiBackupService.insertActHiDetail(map);
            }
        }
        List<Map<String, Object>> actHiIdentitylinks = (List<Map<String, Object>>) data.get(TableName.ACT_HI_IDENTITYLINK);
        if (actHiIdentitylinks != null) {
            for (Map<String, Object> map : actHiIdentitylinks) {
                resolveFields(map, ACT_HI_IDENTITYLINK_COLUMNS);
                wfiBackupService.insertActHiIdentitylink(map);
            }
        }
        List<Map<String, Object>> actHiVarinsts = (List<Map<String, Object>>) data.get(TableName.ACT_HI_VARINST);
        if (actHiVarinsts != null) {
            for (Map<String, Object> map : actHiVarinsts) {
                resolveFields(map, ACT_HI_VARINST_COLUMNS);
                wfiBackupService.insertActHiVarinst(map);
            }
        }
        List<Map<String, Object>> wfiFlows = (List<Map<String, Object>>) data.get(TableName.WFI_FLOW);
        if (wfiFlows != null) {
            for (Map<String, Object> map : wfiFlows) {
                resolveFields(map, WFI_FLOW_COLUMNS);
                wfiBackupService.insertWfiFlow(map);
            }
        }
        List<Map<String, Object>> wfiVFlows = (List<Map<String, Object>>) data.get(TableName.WFI_V_FLOW);
        if (wfiVFlows != null) {
            for (Map<String, Object> map : wfiVFlows) {
                resolveFields(map, WFI_V_FLOW_COLUMNS);
                wfiBackupService.insertWfiVFlow(map);
            }
        }
        List<Map<String, Object>> wfiDelivers = (List<Map<String, Object>>) data.get(TableName.WFI_DELIVER);
        if (wfiDelivers != null) {
            for (Map<String, Object> map : wfiDelivers) {
                resolveFields(map, WFI_DELIVER_COLUMNS);
                wfiBackupService.insertWfiDeliver(map);
            }
        }
        List<Map<String, Object>> wfiComments = (List<Map<String, Object>>) data.get(TableName.WFI_COMMENT);
        if (wfiComments != null) {
            for (Map<String, Object> map : wfiComments) {
                resolveFields(map, WFI_COMMENT_COLUMNS);
                wfiBackupService.insertWfiComment(map);
            }
        }
    }

    protected void insertActGeBytearray(Map<String, Object> map) {
        wfiBackupService.insertActGeBytearray(map);
    }

    protected void insertActHiComment(Map<String, Object> map) {
        wfiBackupService.insertActHiComment(map);
    }

    private void resolveFields(Map<String, Object> map, String[] columns) {
        for (String column : columns) {
            if (!map.containsKey(column)) {
                map.put(column, null);
            }
        }
    }

    public static Map<String, Object> resolveActHiComment(String id, String type, Date time, String userId, String taskId, String procInstId, String action, String message, Object fullMsg) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(ACT_HI_COMMENT_ID, id);
        map.put(ACT_HI_COMMENT_TYPE, type);
        map.put(ACT_HI_COMMENT_TIME, time);
        map.put(ACT_HI_COMMENT_USER_ID, userId);
        map.put(ACT_HI_COMMENT_TASK_ID, taskId);
        map.put(ACT_HI_COMMENT_PROC_INST_ID, procInstId);
        map.put(ACT_HI_COMMENT_ACTION, action);
        map.put(ACT_HI_COMMENT_MESSAGE, message);
        map.put(ACT_HI_COMMENT_FULL_MSG, fullMsg);
        return map;
    }

    public static Map<String, Object> resolveActGeBytearray(String id, Integer rev, String name, String deploymentId, Object bytes, Integer generated) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(ACT_GE_BYTEARRAY_ID, id);
        map.put(ACT_GE_BYTEARRAY_REV, rev);
        map.put(ACT_GE_BYTEARRAY_NAME, name);
        map.put(ACT_GE_BYTEARRAY_DEPLOYMENT_ID, deploymentId);
        map.put(ACT_GE_BYTEARRAY_BYTES, bytes);
        map.put(ACT_GE_BYTEARRAY_GENERATED, generated);
        return map;
    }
//
//    public static Map<String, Object> resolveActHiDetail(String id, String type, String procInstId, String executionId, String taskId, String actInstId, String name, String varType, Integer rev, LocalDateTime time, String bytearrayId, Double doubleValue, Long longValue, String text, String text2) {
//        Map<String, Object> map = new HashMap<>(16);
//        map.put(ACT_HI_DETAIL_ID, id);
//        map.put(ACT_HI_DETAIL_TYPE, type);
//        map.put(ACT_HI_DETAIL_PROC_INST_ID, procInstId);
//        map.put(ACT_HI_DETAIL_EXECUTION_ID, executionId);
//        map.put(ACT_HI_DETAIL_TASK_ID, taskId);
//        map.put(ACT_HI_DETAIL_ACT_INST_ID, actInstId);
//        map.put(ACT_HI_DETAIL_NAME, name);
//        map.put(ACT_HI_DETAIL_VAR_TYPE, varType);
//        map.put(ACT_HI_DETAIL_REV, rev);
//        map.put(ACT_HI_DETAIL_TIME, time);
//        map.put(ACT_HI_DETAIL_BYTEARRAY_ID, bytearrayId);
//        map.put(ACT_HI_DETAIL_DOUBLE, doubleValue);
//        map.put(ACT_HI_DETAIL_LONG, longValue);
//        map.put(ACT_HI_DETAIL_TEXT, text);
//        map.put(ACT_HI_DETAIL_TEXT2, text2);
//        return map;
//    }

    public static Map<String, Object> resolveWfiFlow(String id, String vFlowId, String model, String businessKey, String flowNo, String flowId, String flowCode, String appId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(WFI_FLOW_ID, id);
        map.put(WFI_FLOW_V_FLOW_ID, vFlowId);
        map.put(WFI_FLOW_MODEL, model);
        map.put(WFI_FLOW_BUSINESS_KEY, businessKey);
        map.put(WFI_FLOW_FLOW_NO, flowNo);
        map.put(WFI_FLOW_FLOW_ID, flowId);
        map.put(WFI_FLOW_FLOW_CODE, flowCode);
        map.put(WFI_FLOW_APP_ID, appId);
        return map;
    }

    public static Object resolveWfiVFlow(String id, String flowId, String model, String bpmn, Integer flowVersion, LocalDateTime versionEndDate) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(WFI_V_FLOW_ID, id);
        map.put(WFI_V_FLOW_FLOW_ID, flowId);
        map.put(WFI_V_FLOW_MODEL, model);
        map.put(WFI_V_FLOW_BPMN, bpmn);
        map.put(WFI_V_FLOW_FLOW_VERSION, flowVersion);
        map.put(WFI_V_FLOW_VERSION_END_DATE, versionEndDate);
        return map;
    }

    public static Map<String, Object> resolveWfiDeliver(String id, String deliverInfo, String flowId, String userId, LocalDateTime deliverTime) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(WFI_DELIVER_ID, id);
        map.put(WFI_DELIVER_DELIVER_INFO, deliverInfo);
        map.put(WFI_DELIVER_FLOW_ID, flowId);
        map.put(WFI_DELIVER_USER_ID, userId);
        map.put(WFI_DELIVER_DELIVER_TIME, deliverTime);
        return map;
    }

    public static Map<String, Object> resolveWfiComment(WfiCommentDO wfiComment) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(WFI_COMMENT_ID, wfiComment.getId());
        map.put(WFI_COMMENT_COMMENT_USER_ID, wfiComment.getCommentUserId());
        map.put(WFI_COMMENT_COMMENT_USER, wfiComment.getCommentUser());
        map.put(WFI_COMMENT_REPLY_COMMENT_ID, wfiComment.getReplyCommentId());
        map.put(WFI_COMMENT_FLOW_ID, wfiComment.getFlowId());
        map.put(WFI_COMMENT_COMMENT_TIME, wfiComment.getCommentTime());
        map.put(WFI_COMMENT_TASK_ID, wfiComment.getTaskId());
        map.put(WFI_COMMENT_COMMENT_TEXT, wfiComment.getCommentText());
        map.put(WFI_COMMENT_APP_ID, wfiComment.getAppId());
        map.put(WFI_COMMENT_REPLY_USER_ID, wfiComment.getReplyUserId());
        map.put(WFI_COMMENT_REPLY_USER, wfiComment.getReplyUser());
        map.put(WFI_COMMENT_USER_TYPE, wfiComment.getUserType());
        return map;
    }

    public static Map<String, Object> resolveWfiBackup(WfiBackupDO wfiBackup) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(WFI_BACKUP_ID, wfiBackup.getId());
        map.put(WFI_BACKUP_FLOW_ID, wfiBackup.getFlowId());
        map.put(WFI_BACKUP_DESCRIPTION, wfiBackup.getDescription());
        map.put(WFI_BACKUP_VERSION, wfiBackup.getVersion());
        map.put(WFI_BACKUP_BACKUP_TIME, wfiBackup.getBackupTime());
        map.put(WFI_BACKUP_BACKUP_DATA, wfiBackup.getBackupData());
        map.put(WFI_BACKUP_TASK_ID, wfiBackup.getTaskId());
        map.put(WFI_BACKUP_APP_ID, wfiBackup.getAppId());
        map.put(WFI_BACKUP_ENGINE_VERSION, wfiBackup.getEngineVersion());
        return map;
    }

    private Integer compare(Timestamp d1, Timestamp d2) {
        if (d1 != null && d2 != null) {
            return d1.compareTo(d2);
        } else if (d2 == null) {
            return 1;
        }
        return -1;
    }

    private Integer compare(oracle.sql.TIMESTAMP d1, oracle.sql.TIMESTAMP d2) {
        if (d1 != null && d2 != null) {
            try {
                return d1.timestampValue().compareTo(d2.timestampValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (d2 == null) {
            return 1;
        }
        return -1;
    }

    protected Integer actRuExecutionSort(Map<String, Object> o1, Map<String, Object> o2, String columnName) {
        if (StringUtils.equals((String) o1.get(ACT_RU_EXECUTION_ID), (String) o1.get(ACT_RU_EXECUTION_PROC_INST_ID))) {
            return -1;
        }
        if (StringUtils.equals((String) o2.get(ACT_RU_EXECUTION_ID), (String) o2.get(ACT_RU_EXECUTION_PROC_INST_ID))) {
            return 1;
        }
        switch (BpmConfiguration.getActualDatabaseType()) {
            case DbUpdateTask.DATABASE_TYPE_DM:
                return compare((Timestamp) o1.get(columnName), (Timestamp) o2.get(columnName));
            case DbUpdateTask.DATABASE_TYPE_ST:
                return compare((Timestamp) o1.get(columnName), (Timestamp) o2.get(columnName));
            case ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE:
                return compare((oracle.sql.TIMESTAMP) o1.get(columnName), (oracle.sql.TIMESTAMP) o2.get(columnName));
            case ProcessEngineConfigurationImpl.DATABASE_TYPE_MYSQL:
                return compare((Timestamp) o1.get(columnName), (Timestamp) o2.get(columnName));
        }
        return -1;
    }
}