package com.csicit.ace.data.persistent.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.enums.AuditLogTypeDefault;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.HttpContextUtils;
import com.csicit.ace.common.utils.IpUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.SysAuditLogMapper;
import com.csicit.ace.data.persistent.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统审计日志表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-08 16:28:31
 */
@Service
public class SysAuditLogServiceImpl extends BaseServiceImpl<SysAuditLogMapper, SysAuditLogDO> implements
        SysAuditLogService {

    private static Logger logger = LoggerFactory.getLogger(SysAuditLogServiceImpl.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private SysAuditLogBackupService sysAuditLogBackupService;

    @Autowired
    private SysAuditLogCountService sysAuditLogCountService;

    @Autowired
    SysUserRoleServiceD sysUserRoleServiceD;

    @Autowired
    SysUserServiceD sysUserServiceD;


    @Override
    public boolean synchronize() {
        if (Integer.parseInt(validateLogCount().get("errorCode")) != HttpCode.AUDIT_LOG_UNKNOWN) {
            List<SysAuditLogDO> logList = list(null);
            List<SysAuditLogBackupDO> backList = sysAuditLogBackupService.list(null);
            // 日志的ID
            Set<String> logIds = logList.stream().map(SysAuditLogDO::getId).collect(Collectors.toSet());
            // 备份的日志对应的 日志的ID
            Set<String> backLogIds = new HashSet<>();
            // 备份的日志ID
            Map<String, String> backIds = new HashMap<>();
            backList.forEach(back -> {
                try {
                    String logId = GMBaseUtil.decryptString(back.getLid()).split(back.getId())[1];
                    backLogIds.add(logId);
                    backIds.put(logId, back.getId());
                } catch (Exception e) {
                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                }
            });
            // 日志 比 备份 多出来的 记录
            List<String> exceedLogIds = new ArrayList<>();
            logIds.forEach(id -> {
                if (!backLogIds.contains(id)) {
                    exceedLogIds.add(id);
                }
            });
            // 备份 比 日志 多出来的 记录
            Map<String, String> exceedBackIds = new HashMap<>();
            backLogIds.forEach(id -> {
                if (!logIds.contains(id)) {
                    // 键值对   备份的ID  log的ID
                    exceedBackIds.put(backIds.get(id), id);
                }
            });
            // 将日志往备份表里插数据
            if (exceedLogIds.size() > 0) {
                List<SysAuditLogDO> exceedLogs = list(new QueryWrapper<SysAuditLogDO>()
                        .and(exceedLogIds == null || exceedLogIds.size() == 0, i -> i.eq("1", "2"))
                        .in("id", exceedLogIds));
                exceedLogs.forEach(log -> {
                    String id = log.getId();
                    String nid = UuidUtils.createUUID();
                    SysAuditLogBackupDO sysAuditLogBackupDO = new SysAuditLogBackupDO();
                    sysAuditLogBackupDO.setId(nid);
                    try {
                        sysAuditLogBackupDO.setOpContent(GMBaseUtil.encryptString(nid + log.getOpContent()));
                        sysAuditLogBackupDO.setLid(GMBaseUtil.encryptString(nid + id));
                        sysAuditLogBackupDO.setIpAddress(GMBaseUtil.encryptString(nid + log.getIpAddress()));
                        sysAuditLogBackupDO.setOpName(GMBaseUtil.encryptString(nid + log.getOpName()));
                        sysAuditLogBackupDO.setOpUsername(GMBaseUtil.encryptString(nid + log.getOpUsername()));
                        if (StringUtils.isNotBlank(log.getUserType())) {
                            sysAuditLogBackupDO.setUserType(GMBaseUtil.encryptString(nid + log.getUserType()));
                        }
                        if (StringUtils.isNotBlank(log.getRoleId())) {
                            sysAuditLogBackupDO.setRoleId(GMBaseUtil.encryptString(nid + log.getRoleId()));
                        }
                        if (StringUtils.isNotBlank(log.getGroupId())) {
                            sysAuditLogBackupDO.setGroupId(GMBaseUtil.encryptString(nid + log.getGroupId()));
                        }
                        if (StringUtils.isNotBlank(log.getAppId())) {
                            sysAuditLogBackupDO.setAppId(GMBaseUtil.encryptString(nid + log.getAppId()));
                        }
                        Long opTime = log.getOpTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                        sysAuditLogBackupDO.setOpTime(GMBaseUtil.encryptString(nid + opTime));
                    } catch (Exception e) {
                        throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
                    }
                    if (!sysAuditLogBackupService.save(sysAuditLogBackupDO)) {
                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                    }
                });
            }
            // 将备份日志往日志表里插数据
            if (exceedBackIds.size() > 0) {
                List<SysAuditLogBackupDO> exceedBacks = sysAuditLogBackupService.list(new
                        QueryWrapper<SysAuditLogBackupDO>().in
                        ("id", exceedBackIds.keySet()));
                exceedBacks.forEach(back -> {
                    try {
                        SysAuditLogDO log = new SysAuditLogDO();
                        String logId = exceedBackIds.get(back.getId());
                        log.setId(logId);
                        log.setIpAddress(GMBaseUtil.decryptString(back.getIpAddress()).split(back.getId())[1]);
                        log.setOpName(GMBaseUtil.decryptString(back.getOpName()).split(back.getId())[1]);
                        log.setOpUsername(GMBaseUtil.decryptString(back.getOpUsername()).split(back.getId())[1]);
                        log.setOpContent(GMBaseUtil.decryptString(back.getOpContent()).split(back.getId())[1]);
                        if (StringUtils.isNotBlank(back.getGroupId())) {
                            log.setGroupId(GMBaseUtil.decryptString(back.getGroupId()).split(back.getId())[1]);
                        }
                        if (StringUtils.isNotBlank(back.getAppId())) {
                            log.setAppId(GMBaseUtil.decryptString(back.getAppId()).split(back.getId())[1]);
                        }
                        if (StringUtils.isNotBlank(back.getUserType())) {
                            log.setUserType(GMBaseUtil.decryptString(back.getUserType()).split(back.getId())[1]);
                        }
                        if (StringUtils.isNotBlank(back.getRoleId())) {
                            log.setRoleId(GMBaseUtil.decryptString(back.getRoleId()).split(back.getId())[1]);
                        }
                        // 获取操作时间
                        String opTimeStr = GMBaseUtil.decryptString(back.getOpTime());
                        Long opTimeLong = Long.parseLong(opTimeStr.split(back.getId())[1]);
                        Instant instant = Instant.ofEpochMilli(opTimeLong);
                        ZoneId zone = ZoneOffset.of("+8");
                        LocalDateTime opTime = LocalDateTime.ofInstant(instant, zone);
                        log.setOpTime(opTime);
                        if (!save(log)) {
                            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                        }
                    } catch (Exception e) {
                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                    }
                });
            }
        } else {
            throw new RException(InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN"));
        }
        return true;
    }

    @Override
    public Map<String, String> validateLogCount() {
        Map<String, String> map = new HashMap<>();
        // 日志的数量
        Integer logCount = count(null);
        // 备份的数量
        Integer backCount = sysAuditLogBackupService.count(null);
        // 数量的加密字符串
        SysAuditLogCountDO countDo = sysAuditLogCountService.getOne(null);
        String countStr = countDo == null ? null : countDo.getCount();
        // 是否被篡改
        boolean edited = false;
        String errorMsg = "";
        int code = HttpCode.SUCCESS;
        try {
            Integer count = 0;
            if (StringUtils.isNotBlank(countStr)) {
                String cipherCount = GMBaseUtil.decryptString(countStr);
                cipherCount = cipherCount.replace(Constants.REPLACE, "");
                count = Integer.parseInt(cipherCount);
            }
            if (Objects.equals(count, backCount) && Objects.equals(count, logCount)) {
                // 暂时无法确认日志是否被篡改
                // 利用计划任务去遍历检查
            } else if (Objects.equals(count, backCount) && !Objects.equals(count, logCount)) {
                // 审计日志被删除 或 添加
                // 可同步 将备份的日志复原到日志表 或 删除多余的日志
                edited = true;
                code = HttpCode.AUDIT_LOG_EDITED;
                errorMsg = InternationUtils.getInternationalMsg("AUDIT_LOG_EDITED");
            } else if (Objects.equals(count, logCount) && !Objects.equals(backCount, logCount)) {
                // 备份的审计日志被删除 或 添加
                // 可同步 将日志重新备份到备份日志表
                edited = true;
                code = HttpCode.AUDIT_LOG_BACKUP_EDITED;
                errorMsg = InternationUtils.getInternationalMsg("AUDIT_LOG_BACKUP_EDITED");
            } else {
                // 日志被篡改 且 无法复原
                edited = true;
                code = HttpCode.AUDIT_LOG_UNKNOWN;
                errorMsg = InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN");
            }
        } catch (Exception e) {
            edited = true;
            code = HttpCode.AUDIT_LOG_UNKNOWN;
            errorMsg = InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN") + "[ERROR:count]";
        }

        map.put("errorCode", code + "");
        map.put("msg", errorMsg);
        map.put("edited", edited + "");
        return map;
    }

    /**
     * 保存登录日志
     *
     * @param opContent 日志内容
     */
    @Override
    public boolean saveLoginLog(String opContent, String name, String realName) {
        SysAuditLogDO sysAuditLogDO = new SysAuditLogDO();
        String ipAddress = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        sysAuditLogDO.setTypeTag("success");
        sysAuditLogDO.setType("登录");
        sysAuditLogDO.setTitle("登录");
        sysAuditLogDO.setOpUsername(name);
        sysAuditLogDO.setOpName(realName);
        sysAuditLogDO.setOpTime(LocalDateTime.now());
        sysAuditLogDO.setOpContent(opContent);
        sysAuditLogDO.setIpAddress(ipAddress);
        sysAuditLogDO.setGroupId(null);
        sysAuditLogDO.setAppId(securityUtils.getAppName());
        SysUserDO user = sysUserServiceD.getOne(new QueryWrapper<SysUserDO>().eq("user_name", name).eq("is_delete", 0));
        if (user != null) {
            Integer userType = user.getUserType();
            sysAuditLogDO.setUserType(userType + "");

            if (!Objects.equals("3", sysAuditLogDO.getUserType())) {
                // 管理员用户
//                List<String> roleIds = securityUtils.getRoleIds();
                SysUserRoleDO sysUserRoleDO = sysUserRoleServiceD.getOne(new QueryWrapper<SysUserRoleDO>()
                        .eq("user_id", user.getId()));
                if (sysUserRoleDO != null) {
                    sysAuditLogDO.setRoleId(sysUserRoleDO.getRoleId());
                }
            }

        }
        String id = UuidUtils.createUUID();
        sysAuditLogDO.setId(id);
        try {
            sysAuditLogDO.setSign(GMBaseUtil.getSign(name + id + IpUtils
                    .getIpAddr(HttpContextUtils.getHttpServletRequest())));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
        }
        if (!save(sysAuditLogDO)) {
            return false;
        }
        String nid = UuidUtils.createUUID();
        SysAuditLogBackupDO sysAuditLogBackupDO = new SysAuditLogBackupDO();
        sysAuditLogBackupDO.setId(nid);
        try {
            setLogBackup(new AuditLogTypeDO(AuditLogType.success, "登录"), "登录", opContent, sysAuditLogDO, ipAddress,
                    id, nid,
                    sysAuditLogBackupDO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
        }
        if (sysAuditLogBackupService.save(sysAuditLogBackupDO)) {
            return updateLogCount();
        }
        return true;
    }

    private void setLogBackup(AuditLogTypeDO type, String title, String opContent, SysAuditLogDO sysAuditLogDO,
                              String ipAddress,
                              String id, String nid,
                              SysAuditLogBackupDO sysAuditLogBackupDO) throws Exception {
        sysAuditLogBackupDO.setTitle(GMBaseUtil.encryptString(nid + title));
        sysAuditLogBackupDO.setTypeTag(type.getTag());
        sysAuditLogBackupDO.setType(GMBaseUtil.encryptString(nid + type.getText()));
        sysAuditLogBackupDO.setOpContent(GMBaseUtil.encryptString(nid + opContent));
        sysAuditLogBackupDO.setLid(GMBaseUtil.encryptString(nid + id));
        sysAuditLogBackupDO.setIpAddress(GMBaseUtil.encryptString(nid + ipAddress));
        sysAuditLogBackupDO.setOpName(GMBaseUtil.encryptString(nid + sysAuditLogDO.getOpName()));
        sysAuditLogBackupDO.setOpUsername(GMBaseUtil.encryptString(nid + sysAuditLogDO.getOpUsername()));
        if (StringUtils.isNotBlank(sysAuditLogDO.getUserType())) {
            sysAuditLogBackupDO.setUserType(GMBaseUtil.encryptString(nid + sysAuditLogDO.getUserType()));
        }
        if (StringUtils.isNotBlank(sysAuditLogDO.getRoleId())) {
            sysAuditLogBackupDO.setRoleId(GMBaseUtil.encryptString(nid + sysAuditLogDO.getRoleId()));
        }
        if (StringUtils.isNotBlank(sysAuditLogDO.getGroupId())) {
            sysAuditLogBackupDO.setGroupId(GMBaseUtil.encryptString(nid + sysAuditLogDO.getGroupId()));
        }
        if (StringUtils.isNotBlank(sysAuditLogDO.getAppId())) {
            sysAuditLogBackupDO.setAppId(GMBaseUtil.encryptString(nid + sysAuditLogDO.getAppId()));
        }
        Long opTime = sysAuditLogDO.getOpTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        sysAuditLogBackupDO.setOpTime(GMBaseUtil.encryptString(nid + opTime));
    }

    @Override
    public boolean saveLogWithUserName(AuditLogTypeDO type, String title, Object opContent, String userName, String
            realName) {
        SysAuditLogDO sysAuditLogDO = new SysAuditLogDO();
        String ipAddress = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        sysAuditLogDO.setTitle(title);
        sysAuditLogDO.setTypeTag(type.getTag());
        sysAuditLogDO.setType(type.getText());
        sysAuditLogDO.setOpUsername(userName);
        sysAuditLogDO.setOpName(realName);
        sysAuditLogDO.setOpTime(LocalDateTime.now());
        sysAuditLogDO.setOpContent(JSONObject.toJSONString(opContent));
        sysAuditLogDO.setIpAddress(ipAddress);
        sysAuditLogDO.setGroupId(null);
        sysAuditLogDO.setAppId(securityUtils.getAppName());
        SysUserDO user = sysUserServiceD.getOne(new QueryWrapper<SysUserDO>().eq("user_name", userName).eq
                ("is_delete", 0));
        if (user != null) {
            Integer userType = user.getUserType();
            sysAuditLogDO.setUserType(userType + "");

            if (!Objects.equals("3", sysAuditLogDO.getUserType())) {
                // 管理员用户
                List<String> roleIds = securityUtils.getRoleIds();
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(roleIds)) {
                    sysAuditLogDO.setRoleId(roleIds.get(0));
                }
            }
        }
        String id = UuidUtils.createUUID();
        sysAuditLogDO.setId(id);
        try {
            sysAuditLogDO.setSign(GMBaseUtil.getSign(userName + id + ipAddress));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
        }
        if (!save(sysAuditLogDO)) {
            return false;
        }

        String nid = UuidUtils.createUUID();
        SysAuditLogBackupDO sysAuditLogBackupDO = new SysAuditLogBackupDO();
        sysAuditLogBackupDO.setId(nid);
        try {
            setLogBackup(type, title, JSONObject.toJSONString(opContent), sysAuditLogDO, ipAddress, id,
                    nid,
                    sysAuditLogBackupDO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
        }
        if (sysAuditLogBackupService.save(sysAuditLogBackupDO)) {
            return updateLogCount();
        }
        return true;
    }

    @Override
    public boolean saveLog(String title, Object opContent) {
        return saveLog(new AuditLogTypeDO(), title, opContent);
    }

    @Override
    public boolean saveLog(String title, Object opContent, String type) {
        return saveLog(new AuditLogTypeDO(type), title, opContent);
    }

    @Override
    public boolean saveLog(String title, Object opContent, String type, AuditLogType tag) {
        return saveLog(new AuditLogTypeDO(tag, type), title, opContent);
    }

    @Override
    public boolean saveLog(AuditLogTypeDefault type, String title, Object opContent) {
        return saveLog(new AuditLogTypeDO(AuditLogType.getTypeByTag(type.getTag()), type.getType()), title, opContent);
    }

    /**
     * 保存操作日志
     *
     * @param opContent 日志内容
     */
    @Override
    public boolean saveLog(AuditLogTypeDO type, String title, Object opContent) {
        return saveLog(type, title, opContent, securityUtils.getCurrentGroupId(), securityUtils.getAppName());
    }

    /**
     * 保存集团管理员或应用管理员操作日志
     *
     * @param opContent 日志内容
     */
    @Override
    public boolean saveLog(AuditLogTypeDO type, String title, Object opContent, String groupId, String appId) {
        SysAuditLogDO sysAuditLogDO = new SysAuditLogDO();
        SysUserDO user = securityUtils.getCurrentUser();
        String ipAddress = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        sysAuditLogDO.setTitle(title);
        sysAuditLogDO.setTypeTag(type.getType().getTag());
        sysAuditLogDO.setType(type.getText());
        sysAuditLogDO.setOpTime(LocalDateTime.now());
        if (opContent != null) {
            sysAuditLogDO.setOpContent(JSONObject.toJSONString(opContent));
        }
        sysAuditLogDO.setIpAddress(ipAddress);
        if (!StringUtils.isBlank(groupId)) {
            sysAuditLogDO.setGroupId(groupId);
        } else {
            sysAuditLogDO.setGroupId(null);
        }
        if (!StringUtils.isBlank(appId)) {
            sysAuditLogDO.setAppId(appId);
        } else {
            sysAuditLogDO.setAppId(securityUtils.getAppName());
        }
        if (user != null) {
            sysAuditLogDO.setOpUsername(user.getUserName());
            sysAuditLogDO.setOpName(user.getRealName());
            Integer userType = user.getUserType();
            sysAuditLogDO.setUserType(userType.toString());

            if (!Objects.equals("3", sysAuditLogDO.getUserType())) {
                // 管理员用户
                List<String> roleIds = securityUtils.getRoleIds();
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(roleIds)) {
                    sysAuditLogDO.setRoleId(roleIds.get(0));
                }
            }
        }
        String id = UuidUtils.createUUID();
        sysAuditLogDO.setId(id);
        try {
            sysAuditLogDO.setSign(GMBaseUtil.getSign(user.getUserName() + id + ipAddress));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RException("设置日志签名错误：" + e.getLocalizedMessage());
        }
        if (!save(sysAuditLogDO)) {
            return false;
        }

        String nid = UuidUtils.createUUID();
        SysAuditLogBackupDO sysAuditLogBackupDO = new SysAuditLogBackupDO();
        sysAuditLogBackupDO.setId(nid);
        try {
            setLogBackup(type, title, JSONObject.toJSONString(opContent), sysAuditLogDO, ipAddress, id,
                    nid,
                    sysAuditLogBackupDO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RException("设置日志备份错误：" + e.getLocalizedMessage());
        }
        if (sysAuditLogBackupService.save(sysAuditLogBackupDO)) {
            return updateLogCount();
        }
        return true;
    }


    /**
     * 更新日志数量
     *
     * @return
     * @author FourLeaves
     * @date 2020/1/16 8:11
     */
    private boolean updateLogCount() {
        int count = sysAuditLogCountService.count(null);
        if (count == 0) {
            String cid = UuidUtils.createUUID();
            SysAuditLogCountDO sysAuditLogCountDO = new SysAuditLogCountDO();
            sysAuditLogCountDO.setId(cid);
            try {
                sysAuditLogCountDO.setCount(GMBaseUtil.encryptString(Constants.REPLACE + 1));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RException("设置日志数量错误：" + e.getLocalizedMessage());
            }
            return sysAuditLogCountService.save(sysAuditLogCountDO);
        } else {
            SysAuditLogCountDO one = sysAuditLogCountService.getOne(null);
            try {
                String cipherCount = GMBaseUtil.decryptString(one.getCount());
                cipherCount = cipherCount.replace(Constants.REPLACE, "");
                one.setCount(GMBaseUtil.encryptString(
                        Constants.REPLACE + (Integer.parseInt(cipherCount) + 1)));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RException("更新日志数量错误：" + e.getLocalizedMessage());
            }
            return sysAuditLogCountService.updateById(one);
        }
    }
}
