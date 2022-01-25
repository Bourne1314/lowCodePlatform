package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.enums.AuditLogTypeDefault;


/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/10 17:15
 */
public interface IAuditLog {
    /**
     * 保存操作日志
     *
     * @param title     操作标题  '新增人员'
     * @param opContent 日志内容
     */
    boolean saveLog(String title, Object opContent);

    /**
     * 保存操作日志
     *
     * @param title     操作标题 '新增人员'
     * @param opContent 日志内容 '人员信息'
     * @param type      操作类别 '新增'
     */
    boolean saveLog(String title, Object opContent, String type);

    /**
     * 保存操作日志
     * @param title     操作标题 '新增人员'
     * @param opContent 日志内容 '人员信息'
     * @param type      操作类别 '新增'
     * @param tag       操作类别标签
     */
    boolean saveLog(String title, Object opContent, String type, AuditLogType tag);

    /**
     * 保存操作日志
     * @param type      操作类别
     * @param title     操作标题
     * @param opContent 日志内容
     */
    boolean saveLog(AuditLogTypeDefault type, String title, Object opContent);


    /**
     * 保存操作日志
     * @param type      操作类别
     * @param title     操作标题
     * @param opContent 日志内容
     */
    boolean saveLog(AuditLogTypeDO type, String title, Object opContent);

    /**
     * 保存集团管理员或应用管理员操作日志
     *
     * @param type      操作类别
     * @param title     操作标题
     * @param opContent 日志内容
     * @param groupId   集团ID
     * @param appId     应用ID
     */
    boolean saveLog(AuditLogTypeDO type, String title, Object opContent, String groupId, String appId);

}
