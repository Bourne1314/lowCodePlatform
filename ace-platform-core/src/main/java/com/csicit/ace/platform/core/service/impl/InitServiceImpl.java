package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/31 14:56
 */
@Service
public class InitServiceImpl implements InitService {

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysUserRoleLvService sysUserRoleLvService;

    @Autowired
    SysUserRoleVService sysUserRoleVService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysPasswordPolicyService sysPasswordPolicyService;

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SecurityUtils securityUtils;

    @Override
    public boolean updateSecretKey(String str) {
        if (StringUtils.isNotBlank(str)) {
            SysConfigDO sysConfigDO = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                    "platform_secret_key"));
            if (sysConfigDO == null) {
                sysConfigDO = new SysConfigDO();
                sysConfigDO.setSortIndex(10);
                sysConfigDO.setType("String");
                sysConfigDO.setScope(1);
                sysConfigDO.setName("platform_secret_key");
                sysConfigDO.setValue(str);
                sysConfigDO.setCreateTime(LocalDateTime.now());
                sysConfigDO.setUpdateTime(LocalDateTime.now());
                if (sysConfigService.save(sysConfigDO)) {
                    if (sysAuditLogService.saveLogWithUserName(new AuditLogTypeDO(AuditLogType.warning, "更新"),
                            "更新授权文件", "更新授权文件", "admin", "租户系统管理员")) {
                        cacheUtil.set("platform_secret_key", str, CacheUtil.NOT_EXPIRE);
                        return true;
                    }
                }
            } else {
                // 只有租户系统管理员拥有此项权限
                if (!Objects.equals(securityUtils.getCurrentUserName(), "admin")) {
                    throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
                }
                // 替换旧的配置项
                SysConfigDO oldConfig = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                        "platform_secret_key_old"));
                if (oldConfig == null) {
                    oldConfig = JsonUtils.castObjectForSetIdNull(sysConfigDO, SysConfigDO.class);
                    oldConfig.setName("platform_secret_key_old");
                    oldConfig.setCreateTime(LocalDateTime.now());
                    oldConfig.setUpdateTime(LocalDateTime.now());
                    oldConfig.setCreateUser(securityUtils.getCurrentUserId());
                } else {
                    oldConfig.setValue(sysConfigDO.getValue());
                    oldConfig.setUpdateTime(LocalDateTime.now());
                }
                if (sysConfigService.saveOrUpdate(oldConfig)) {
                    sysConfigDO.setUpdateTime(LocalDateTime.now());
                    sysConfigDO.setValue(str);
                    if (sysConfigService.updateById(sysConfigDO)) {
                        if (sysAuditLogService.saveLogWithUserName(new AuditLogTypeDO(AuditLogType.warning, "更新"),
                                "更新授权文件", "更新授权文件", "admin", "租户系统管理员")) {
                            cacheUtil.set("platform_secret_key", str, CacheUtil.NOT_EXPIRE);
                            return true;
                        }
                    }
                }
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    @Override
    public boolean recallSecretKey() {
        SysConfigDO oldConfig = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                "platform_secret_key_old"));
        if (oldConfig == null) {
            if (sysConfigService.remove(new QueryWrapper<SysConfigDO>().eq("name", "platform_secret_key"))) {
                if (sysAuditLogService.saveLogWithUserName(new AuditLogTypeDO(AuditLogType.warning, "更新"), "撤回授权文件",
                        "撤回授权文件", "admin", "租户系统管理员")) {
                    cacheUtil.delete("platform_secret_key");
                    return true;
                }
            }
        } else {
            // 只有租户系统管理员拥有此项权限
            if (!Objects.equals(securityUtils.getCurrentUserName(), "admin")) {
                throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
            SysConfigDO config = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                    "platform_secret_key"));
            config.setValue(oldConfig.getValue());
            config.setUpdateTime(LocalDateTime.now());
            if (sysConfigService.updateById(config)) {
                if (sysAuditLogService.saveLogWithUserName(new AuditLogTypeDO(AuditLogType.warning, "更新"), "撤回授权文件",
                        "撤回授权文件", "admin", "租户系统管理员")) {
                    cacheUtil.set("platform_secret_key", config.getValue(), CacheUtil.NOT_EXPIRE);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public R init(Map<String, String> map) {
        // 判断是否已初始化
        int count = sysUserService.count(null);
        if (count > 1) {
            return R.error(InternationUtils.getInternationalMsg("INIT_HAS_OVER"));
        }
        // 密级选择
        if (StringUtils.isBlank(map.get("secretLevel"))) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        Integer defaultSecretLevel = Integer.parseInt(map.get("secretLevel"));
        // 语言选择
        String defaultLanguage = map.get("language");
        // 初始密码
        String defaultPassword = map.get("defaultPassword");
        // 免登陆天数
        String defaultTokenExpireDay = map.get("tokenExpireDay");
        // adminIP地址
        String adminIpAddress = map.get("adminIpAddress");
        // secIP地址
        String secIpAddress = map.get("secIpAddress");
        // auditorIp地址
        String auditorIpAddress = map.get("auditorIpAddress");
        // 平台管理员组成  "1"：集团应用三员；"2"：集团单一管理员、应用单一管理员；"3"：单一业务管理员
        String retainGroupAndAppAdmin = map.get("retainGroupAndAppAdmin");


        //平台名称
        String platformName = "企业管理平台";
        if (StringUtils.isNotBlank(map.get("platformName"))) {
            platformName = map.get("platformName");
        }

        // 加密密码
        String cipherDefaultPassword;
        try {
            cipherDefaultPassword = GMBaseUtil.encryptString("Cipher" + defaultPassword);
        } catch (Exception e) {
            return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        if (StringUtils.isBlank(defaultPassword) || StringUtils.isBlank(defaultLanguage) || StringUtils.isBlank
                (defaultTokenExpireDay)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }

        // mybatis plus 方法执行失败只能撤回当前方法的的事务
        // 须抛出异常撤回所有事务
        if (sysConfigService.initSaveOrUpdateConfigByKey("adminIpAddressCheck", "stop", "管理员IP地址校验")) {
            if (sysConfigService.initSaveOrUpdateConfigByKey("retainGroupAndAppAdmin", retainGroupAndAppAdmin,
                    "平台管理员组成模式 \"1\"：集团应用三员；\"2\"：集团单一管理员、应用单一管理员；\"3\"：单一业务管理员")) {
                if (sysConfigService.initSaveOrUpdateConfigByKey("platformName", platformName, "平台名称")) {
                    if (sysConfigService.initSaveOrUpdateConfigByKey("defaultPassword", cipherDefaultPassword,
                            "默认密码")) {
                        if (sysConfigService.initSaveOrUpdateConfigByKey("defaultLanguage", defaultLanguage, "默认语言")) {
                            if (sysConfigService.initSaveOrUpdateConfigByKey("defaultSecretLevel", defaultSecretLevel
                                            .toString(),

                                    "租户密级"
                            )) {
                                if (sysConfigService.initSaveOrUpdateConfigByKey("defaultTokenExpireDay",
                                        defaultTokenExpireDay,
                                        "token无效时间（单位：分钟）;值为int类型"
                                )) {
                                    /**
                                     * 根据密级设置默认的密码策略
                                     */
                                    String passwordPolicyId = "bdpolicy" + "-" + defaultSecretLevel;
                                    SysPasswordPolicyDO passwordPolicy = sysPasswordPolicyService.getById
                                            (passwordPolicyId);
                                    if (passwordPolicy != null) {
                                        passwordPolicy.setId("bdpolicy");
                                        sysPasswordPolicyService.save(passwordPolicy);
                                    }
                                    // 存储到redis
                                    cacheUtil.set("platformName", platformName, CacheUtil.NOT_EXPIRE);
                                    cacheUtil.set("platform_internationalization", defaultLanguage, CacheUtil
                                            .NOT_EXPIRE);
                                    cacheUtil.set("defaultSecretLevel", defaultSecretLevel, CacheUtil.NOT_EXPIRE);
                                    cacheUtil.set("defaultLanguage", defaultLanguage, CacheUtil.NOT_EXPIRE);
                                    cacheUtil.set("defaultTokenExpireDay", Integer.parseInt(defaultTokenExpireDay),
                                            CacheUtil
                                            .NOT_EXPIRE);
                                    // 设置启动管理员登录IP地址校验
                                    cacheUtil.set("adminIpAddressCheck", "stop", CacheUtil.NOT_EXPIRE);
                                    // 设置是否保留集团应用三员
                                    cacheUtil.set("retainGroupAndAppAdmin", retainGroupAndAppAdmin, CacheUtil.NOT_EXPIRE);
                                    // 新建租户管理员用户
                                    // 系统管理员
                                    SysUserDO systemManager = new SysUserDO();
                                    String adminId = UuidUtils.createUUID();
                                    systemManager.setId(adminId);
                                    systemManager.setUserType(0);
                                    systemManager.setUserName("admin");
                                    systemManager.setRealName("租户系统管理员");
                                    systemManager.setPassword(defaultPassword);
                                    systemManager.setSecretLevel(5);
                                    systemManager.setIpBind(1);
                                    systemManager.setIpAddress(adminIpAddress);
                                    sysUserService.initUser(systemManager);
                                    // 安全管理员
                                    SysUserDO securityManager = new SysUserDO();
                                    String secId = UuidUtils.createUUID();
                                    securityManager.setId(secId);
                                    securityManager.setUserType(0);
                                    securityManager.setUserName("sec");
                                    securityManager.setRealName("租户安全保密员");
                                    securityManager.setPassword(defaultPassword);
                                    securityManager.setSecretLevel(5);
                                    securityManager.setIpBind(1);
                                    securityManager.setIpAddress(secIpAddress);
                                    sysUserService.initUser(securityManager);
                                    // 审计员
                                    SysUserDO auditor = new SysUserDO();
                                    String auditorId = UuidUtils.createUUID();
                                    auditor.setId(auditorId);
                                    auditor.setUserType(0);
                                    auditor.setUserName("auditor");
                                    auditor.setRealName("租户安全审计员");
                                    auditor.setPassword(defaultPassword);
                                    auditor.setSecretLevel(5);
                                    auditor.setIpBind(1);
                                    auditor.setIpAddress(auditorIpAddress);
                                    sysUserService.initUser(auditor);


                                    // 关联角色
                                    SysUserRoleDO admin = new SysUserRoleDO();
                                    admin.setRoleId("admin");
                                    admin.setUserId(adminId);
                                    admin.setRoleType(1);
                                    admin.setAppId("platform");
//                            admin.setActivated(1);
                                    if (!sysUserRoleService.save(admin)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleLvDO adminLv = new SysUserRoleLvDO();
                                    adminLv.setId(UuidUtils.createUUID());
                                    adminLv.setUserId(adminId);
                                    adminLv.setLatest(1);
                                    adminLv.setVersion(0);
                                    adminLv.setAppId("platform");
                                    if (!sysUserRoleLvService.save(adminLv)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleVDO adminV = new SysUserRoleVDO();
                                    adminV.setId(UuidUtils.createUUID());
                                    adminV.setLvId(adminLv.getId());
                                    adminV.setRoleId("admin");
                                    if (!sysUserRoleVService.save(adminV)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleDO sec = new SysUserRoleDO();
                                    sec.setRoleId("sec");
                                    sec.setUserId(secId);
                                    sec.setRoleType(2);
                                    sec.setAppId("platform");
//                            sec.setActivated(1);
                                    if (!sysUserRoleService.save(sec)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleLvDO secLv = new SysUserRoleLvDO();
                                    secLv.setId(UuidUtils.createUUID());
                                    secLv.setUserId(secId);
                                    secLv.setLatest(1);
                                    secLv.setVersion(0);
                                    secLv.setAppId("platform");
                                    if (!sysUserRoleLvService.save(secLv)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleVDO secV = new SysUserRoleVDO();
                                    secV.setId(UuidUtils.createUUID());
                                    secV.setLvId(secLv.getId());
                                    secV.setRoleId("sec");
                                    if (!sysUserRoleVService.save(secV)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleDO auditorUserRole = new SysUserRoleDO();
                                    auditorUserRole.setRoleId("auditor");
                                    auditorUserRole.setUserId(auditorId);
                                    auditorUserRole.setRoleType(3);
                                    auditorUserRole.setAppId("platform");
//                            auditorUserRole.setActivated(1);
                                    if (!sysUserRoleService.save(auditorUserRole)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleLvDO auditorLv = new SysUserRoleLvDO();
                                    auditorLv.setId(UuidUtils.createUUID());
                                    auditorLv.setUserId(auditorId);
                                    auditorLv.setLatest(1);
                                    auditorLv.setVersion(0);
                                    auditorLv.setAppId("platform");
                                    if (!sysUserRoleLvService.save(auditorLv)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    SysUserRoleVDO auditorV = new SysUserRoleVDO();
                                    auditorV.setId(UuidUtils.createUUID());
                                    auditorV.setLvId(auditorLv.getId());
                                    auditorV.setRoleId("auditor");
                                    if (!sysUserRoleVService.save(auditorV)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }


                                    // 关联权限  Auth_Mix
                                    if (!sysAuthMixService.saveAuthMix(adminId)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }
                                    if (!sysAuthMixService.saveAuthMix(secId)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }
                                    if (!sysAuthMixService.saveAuthMix(auditorId)) {
                                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                                    }

                                    return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
                                }
                            }
                        }
                    }
                }
            }
        }

        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }
}
