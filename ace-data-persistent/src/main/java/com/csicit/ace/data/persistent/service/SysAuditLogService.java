package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.SysAuditLogDO;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.enums.AuditLogTypeDefault;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 系统审计日志表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-08 16:28:31
 */
@Transactional
public interface SysAuditLogService extends IBaseService<SysAuditLogDO> {

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
     * 保存操作日志 带上用户名 真实姓名
     *
     * @param type      操作类别
     * @param title     操作标题
     * @param opContent 日志内容
     * @param userName
     * @param realName
     */
    boolean saveLogWithUserName(AuditLogTypeDO type, String title, Object opContent, String userName, String realName);

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

    /**
     * 保存登录日志
     *
     * @param opContent 日志内容
     * @param name      用户名
     */
    boolean saveLoginLog(String opContent, String name, String realName);

    /**
     * 同步审计日志
     *
     * @return
     * @author yansiyang
     * @date 2019/6/4 10:06
     */
    boolean synchronize();

    /**
     * 校验日志是否被篡改
     *
     * @return
     * @author yansiyang
     * @date 2019/6/4 10:20
     */
    Map<String, String> validateLogCount();

}
