package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthUserMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限用户管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthUserService")
public class SysAuthUserServiceImpl extends BaseServiceImpl<SysAuthUserMapper, SysAuthUserDO> implements
        SysAuthUserService {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysAuthUserLvService sysAuthUserLvService;

    @Autowired
    SysAuthUserVService sysAuthUserVService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysAuthUserService sysAuthUserService;

    @Autowired
    SysWaitGrantAuthService sysWaitGrantAuthService;

    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    /**
     * 用户授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 17:03
     */
    @Override
    public boolean saveUserAuth(GrantAuthReciveVO grantAuthReciveVO) {
        String userId = grantAuthReciveVO.getUserId();
        String realName = grantAuthReciveVO.getRealName();
        // 删除老用户权限
        this.remove(new QueryWrapper<SysAuthUserDO>().eq("app_id", grantAuthReciveVO.getAppId())
                .eq("user_id", grantAuthReciveVO.getUserId()));
        // 添加该用户的待激活表
        if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>().eq("ACTIVATE_ID", grantAuthReciveVO
                .getUserId())
                .eq("TYPE", 0).eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
            sysWaitGrantAuthService.saveSysWaitGrantAuth(grantAuthReciveVO.getUserId(), 0, grantAuthReciveVO.getAppId
                    ());
        }
        StringBuffer stringBuffer = new StringBuffer();
        List<SysAuthUserDO> sysAuthUserDOList = new ArrayList<>(16);
        grantAuthReciveVO.getGrantAuthVOList().forEach(grantAuth -> {
            stringBuffer.append(grantAuth.getName());
            stringBuffer.append(",");
            // 添加新用户权限
            sysAuthUserDOList.add(this.addSysAuthUser(grantAuthReciveVO.getAppId(), userId, realName, grantAuth
                    .getName(), grantAuth.getId(), grantAuth.getRevoker()));
        });
        if (CollectionUtils.isNotEmpty(sysAuthUserDOList)) {
            if (!this.saveBatch(sysAuthUserDOList)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }
        if (grantAuthReciveVO.getGrantAuthVOList().size() == 0) {
            return sysAuditLogService.
                    saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "清空用户权限", "(清空)授权用户【" + realName + "】的权限",
                            securityUtils.getCurrentGroupId(), grantAuthReciveVO.getAppId());
        }
        return sysAuditLogService.
                saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存用户权限", "(保存)授权用户【" + realName + "】的权限：" +
                                stringBuffer
                                        .substring(0, stringBuffer
                                                .length() - 1),
                        securityUtils.getCurrentGroupId(), grantAuthReciveVO.getAppId());
    }

    /**
     * 用户授权(激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 17:03
     */
    @Override
    public boolean saveUserAuthActivation(GrantAuthReciveVO grantAuthReciveVO) {
        // 用户权限激活
        if (!this.authUserActivation(grantAuthReciveVO.getUserId(), grantAuthReciveVO.getAppId()))
            return false;

        // 删除该角色的待激活表
        sysWaitGrantAuthService.remove(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 0).eq("app_id", grantAuthReciveVO.getAppId())
                .eq("activate_id", grantAuthReciveVO.getUserId()));

        // 添加新有效权限
        sysAuthMixService.saveAuthMixForApp(grantAuthReciveVO.getUserId(), grantAuthReciveVO.getAppId());
        String userId = grantAuthReciveVO.getUserId();
        String realName = grantAuthReciveVO.getRealName();

        List<SysAuthUserDO> sysAuthUserDOS = sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                .eq("user_id", userId));

        StringBuffer stringBuffer = new StringBuffer();
        if (sysAuthUserDOS != null && sysAuthUserDOS.size() > 0) {
            sysAuthUserDOS.stream().forEach(sysAuthUserDO -> {
                stringBuffer.append(sysAuthUserDO.getAuthName());
                stringBuffer.append(",");
            });
        }

        return sysAuditLogService.
                saveLog(new AuditLogTypeDO(AuditLogType.success, "激活"), "激活用户权限", "(激活)授权用户【" + realName + "】的权限：" +
                                (stringBuffer.length() > 0 ?
                                        stringBuffer
                                                .substring(0,
                                                        stringBuffer.length() - 1) : null),
                        securityUtils.getCurrentGroupId(),
                        grantAuthReciveVO.getAppId());
    }

    /**
     * 权限用户授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 17:03
     */
    @Override
    public Map<String, Object> saveAuthUserGrant(GrantAuthReciveVO grantAuthReciveVO) {
        Map<String, Object> result = new HashMap<>();
        // 发生变化关系的用户ID列表
        List<String> userIds = new ArrayList<>(16);

        // 获取该权限以及该权限的所有上级权限
        List<SysAuthDO> parentAuthList = new ArrayList<>(16);
        parentAuthList.add(sysAuthService.getById(grantAuthReciveVO.getAuthId()));
        sysAuthService.getParentAuths(parentAuthList, grantAuthReciveVO.getAuthId());

        //获取该权限以及该权限的所有下级权限
        List<SysAuthDO> childAuthList = new ArrayList<>(16);
        childAuthList.add(sysAuthService.getById(grantAuthReciveVO.getAuthId()));
        sysAuthService.getChildAuths(grantAuthReciveVO.getAuthId(), childAuthList);


        // 获取要授权的用户列表
        List<SysUserDO> grantUsers = new ArrayList<>(16);
        // 获取要禁用的用户列表
        List<SysUserDO> unUsedUsers = new ArrayList<>(16);
        if (grantAuthReciveVO.getSysAuthUserDOList() != null && grantAuthReciveVO.getSysAuthUserDOList().size() > 0) {
            grantAuthReciveVO.getSysAuthUserDOList().stream().forEach(authUser -> {
                SysUserDO sysUserDO = new SysUserDO();
                sysUserDO.setId(authUser.getUserId());
                sysUserDO.setRealName(authUser.getRealName());
                if (Objects.equals(authUser.getRevoker(), 0)) {
                    // 授予
                    grantUsers.add(sysUserDO);
                } else {
                    // 禁用
                    unUsedUsers.add(sysUserDO);
                }
            });
        }
        StringBuffer grantStr = new StringBuffer();
        // 获取原有的该权限对应的userIds，通过判断获取被删除的用户ID
        List<String> grantUserIds = grantUsers.stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
        List<String> oldGrantUserIds = sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                .eq("auth_id", grantAuthReciveVO.getAuthId()).eq("is_revoke", 0)).stream().map
                (SysAuthUserDO::getUserId).collect(Collectors.toList());
        // 判断权限用户新授予关系与旧授予关系是否发生改变
        if (!(grantUserIds.size() == oldGrantUserIds.size() && grantUserIds.containsAll(oldGrantUserIds))) {
            // 有变化

            // 删除的用户权限关系
            List<String> deletedUserIds = oldGrantUserIds.stream().filter(it -> !grantUserIds.contains(it)).collect
                    (Collectors.toList());
            deletedUserIds.stream().forEach(userId -> {
                // 针对被删除的用户Id
                // 删除用户Id对应的该权限及以下权限的关系表
                childAuthList.stream().forEach(sysAuthDO -> {
                    this.remove(new QueryWrapper<SysAuthUserDO>()
                            .eq("is_revoke", 0)
                            .eq("auth_id", sysAuthDO.getId())
                            .eq("user_id", userId));
                });
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>().eq("ACTIVATE_ID", userId)
                        .eq("TYPE", 0).eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    userIds.add(userId);
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(userId, 0, grantAuthReciveVO.getAppId());
                }
            });
            // 新增的用户权限关系
            List<String> addUserIds = grantUserIds.stream().filter(it -> !oldGrantUserIds.contains(it)).collect
                    (Collectors.toList());
            addUserIds.stream().forEach(userId -> {
                SysUserDO sysUserDO = sysUserService.getById(userId);
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>().eq("ACTIVATE_ID", userId)
                        .eq("TYPE", 0).eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    userIds.add(userId);
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(userId, 0, grantAuthReciveVO.getAppId());
                }
                List<SysAuthUserDO> sysAuthUserDOList = new ArrayList<>(16);
                if (CollectionUtils.isNotEmpty(parentAuthList)) {
                    parentAuthList.stream().forEach(sysAuthDO -> {
                        sysAuthUserDOList.add(this.addSysAuthUser(grantAuthReciveVO.getAppId(), userId,
                                sysUserDO.getRealName(), sysAuthDO.getName
                                        (), sysAuthDO.getId()
                                , 0));
                    });
                    this.remove(new QueryWrapper<SysAuthUserDO>().eq("user_id", userId).in("auth_id",
                            parentAuthList
                                    .stream().map(AbstractBaseDomain::getId).collect(Collectors.toList())));
                }

                // 查询该用户所有的权限再去重
                List<SysAuthUserDO> arrayList = new ArrayList<>(16);
                List<String> authIds = new ArrayList<>(16);
                sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                        .eq("user_id", userId).eq("is_revoke", 0))
                        .stream().forEach(sysAuthUserDO -> {
                    if (!authIds.contains(sysAuthUserDO.getAuthId())) {
                        authIds.add(sysAuthUserDO.getAuthId());
                        arrayList.add(sysAuthUserDO);
                    }
                });
                // 删除该用户所有的权限
                this.remove(new QueryWrapper<SysAuthUserDO>().eq("app_id", grantAuthReciveVO.getAppId()).eq("user_id",
                        userId).eq("is_revoke", 0));
                // 添加新的用户权限
                arrayList.stream().forEach(auth -> {
                    sysAuthUserDOList.add(this.addSysAuthUser(grantAuthReciveVO.getAppId(), userId, sysUserDO
                            .getRealName(), auth.getAuthName(), auth.getAuthId(), 0));
                });
                if (CollectionUtils.isNotEmpty(sysAuthUserDOList)) {
                    if (!this.saveBatch(sysAuthUserDOList)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                }

                grantStr.append(sysUserDO.getRealName());
                grantStr.append(",");
            });
        }

        StringBuffer unUsedStr = new StringBuffer();

        List<String> unUsedUserIds = unUsedUsers.stream().map(AbstractBaseDomain::getId).collect(Collectors
                .toList());
        // 获取原有的该权限对应的userIds，通过判断获取被删除的用户ID
        List<String> oldUnUserUserIds = sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                .eq("auth_id", grantAuthReciveVO.getAuthId()).eq("is_revoke", 1)).stream().map(SysAuthUserDO::getUserId)
                .collect(Collectors.toList());

        // 判断权限用户新禁用关系与旧禁用关系是否发生改变
        if (!(unUsedUserIds.size() == oldUnUserUserIds.size() && unUsedUserIds.containsAll(oldUnUserUserIds))) {
            // 有变化
            // 删除的
            List<String> deletedUserIds = oldUnUserUserIds.stream().filter(it -> !unUsedUserIds.contains(it)).collect
                    (Collectors.toList());
            deletedUserIds.stream().forEach(userId -> {
                // 针对被删除的用户Id
                // 删除用户Id对应的该权限及以下权限的关系表
                this.remove(new QueryWrapper<SysAuthUserDO>()
                        .eq("auth_id", grantAuthReciveVO.getAuthId())
                        .eq("user_id", userId));
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>().eq("ACTIVATE_ID", userId)
                        .eq("TYPE", 0).eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    userIds.add(userId);
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(userId, 0, grantAuthReciveVO.getAppId());
                }

            });
            // 新建的
            List<String> addUserIds = unUsedUserIds.stream().filter(it -> !oldUnUserUserIds.contains(it)).collect
                    (Collectors
                            .toList());
            addUserIds.stream().forEach(userId -> {
                SysUserDO sysUserDO = sysUserService.getById(userId);
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>().eq("ACTIVATE_ID", userId)
                        .eq("TYPE", 0).eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    userIds.add(userId);
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(userId, 0, grantAuthReciveVO.getAppId());
                }
                List<SysAuthUserDO> sysAuthUserDOList = new ArrayList<>(16);
                if (CollectionUtils.isNotEmpty(childAuthList)) {
                    childAuthList.stream().forEach(sysAuthDO -> {
                        sysAuthUserDOList.add(this.addSysAuthUser(grantAuthReciveVO.getAppId(), userId, sysUserDO
                                        .getRealName(), sysAuthDO.getName(), sysAuthDO.getId()
                                , 1));
                    });
                    this.remove(new QueryWrapper<SysAuthUserDO>()
                            .eq("user_id", sysUserDO.getId()).in("auth_id", childAuthList.stream().map
                                    (AbstractBaseDomain::getId)
                                    .collect(Collectors.toList())));
                }

                // 查询该用户所有的权限再去重
                List<String> authIds = new ArrayList<>(16);
                List<SysAuthUserDO> arrayList = new ArrayList<>(16);
                sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                        .eq("user_id", sysUserDO.getId()).eq("is_revoke", 1))
                        .stream().forEach(sysAuthUserDO -> {
                    if (!authIds.contains(sysAuthUserDO.getAuthId())) {
                        authIds.add(sysAuthUserDO.getAuthId());
                        arrayList.add(sysAuthUserDO);
                    }
                });
                // 删除该用户所有的权限
                this.remove(new QueryWrapper<SysAuthUserDO>().eq("app_id", grantAuthReciveVO.getAppId()).eq("user_id",
                        sysUserDO.getId()).eq("is_revoke", 1));
                // 添加新的用户权限

                arrayList.stream().forEach(auth -> {
                    sysAuthUserDOList.add(this.addSysAuthUser(grantAuthReciveVO.getAppId(), sysUserDO.getId(), sysUserDO
                                    .getRealName(),
                            auth.getAuthName(), auth.getAuthId(), 1));
                });
                if (CollectionUtils.isNotEmpty(sysAuthUserDOList)) {
                    if (!this.saveBatch(sysAuthUserDOList)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                }

                unUsedStr.append(sysUserDO.getRealName());
                unUsedStr.append(",");

            });

        }
        result.put("result", sysAuditLogService.
                saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "权限角色保存", "权限角色授权(保存):权限【" + grantAuthReciveVO
                                .getAuthName() + "】的授权用户：" + (grantStr.length() > 0 ? grantStr.substring(0, grantStr
                                .length
                                        () - 1) : null) +
                                "；禁用用户：" + (unUsedStr.length() > 0 ? unUsedStr.substring(0, unUsedStr.length() - 1) :
                                null),
                        securityUtils.getCurrentGroupId(), grantAuthReciveVO.getAppId()));
        result.put("userIds", userIds);
        return result;

    }

    /**
     * 获取 管理员用户/普通用户在所有应用下的 激活权限
     *
     * @param userId
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthUserVDO>
     * @author zuogang
     * @date 2019/7/5 14:18
     */
    @Override
    public List<SysAuthUserVDO> getActiveAuthUser(String userId) {
        return sysAuthUserVService.list(new QueryWrapper<SysAuthUserVDO>()
                .inSql("lv_id", "select id from sys_auth_user_lv where IS_LAST_VERSION=1 and user_id='" + userId
                        + "'")
        );
    }

    /**
     * 获取 普通用户在某应用下的 激活权限
     *
     * @param userId
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthUserVDO>
     * @author zuogang
     * @date 2019/7/5 14:18
     */
    @Override
    public List<SysAuthUserVDO> getActiveAuthUserForApp(String userId, String appId) {
        return sysAuthUserVService.list(new QueryWrapper<SysAuthUserVDO>()
                .inSql("lv_id", "select id from sys_auth_user_lv where IS_LAST_VERSION=1 and app_id ='" + appId +
                        "' and user_id ='" + userId + "'").inSql("auth_id", "select id from sys_auth")
        );
    }


    /**
     * 用户权限激活
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/5/5 14:51
     */
    @Override
    public boolean authUserActivation(String userId, String appId) {
        //用户权限表数据 激活
//        sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
//                .eq("user_id", userId))
//                .stream().forEach(sysAuthUserDO -> {
//            sysAuthUserService.updateById(sysAuthUserDO);
//        });

        // 获取用户授权版本控制表的版本编号
        Integer oldVersionNo = -1;

        SysAuthUserLvDO sysAuthUserLvDO1 = sysAuthUserLvService.getOne(new QueryWrapper<SysAuthUserLvDO>()
                .eq("is_last_version", 1).eq("user_id", userId).eq("app_id", appId));
        if (sysAuthUserLvDO1 != null) {
            oldVersionNo = sysAuthUserLvDO1.getVersionNo();
        }

        // 更改用户授权版本控制表数据
        sysAuthUserLvService.update(new SysAuthUserLvDO(), new UpdateWrapper<SysAuthUserLvDO>()
                .eq("is_last_version", 1).eq("user_id", userId).eq("app_id", appId)
                .set("is_last_version", 0)
                .set("version_end_time", LocalDateTime.now())
                .set("version_end_user_id", securityUtils.getCurrentUser().getId()));
        // 添加用户授权版本控制表数据
        SysAuthUserLvDO sysAuthUserLvDO = new SysAuthUserLvDO();
        String id = UuidUtils.createUUID();
        sysAuthUserLvDO.setId(id);
        sysAuthUserLvDO.setUserId(userId);
        sysAuthUserLvDO.setAppId(appId);
        sysAuthUserLvDO.setVersionNo(oldVersionNo + 1);
        sysAuthUserLvDO.setLastVersion(1);
        sysAuthUserLvDO.setVersionBeginTime(LocalDateTime.now());
        sysAuthUserLvDO.setVersionBeginUserId(securityUtils.getCurrentUser().getId());
        if (!sysAuthUserLvService.save(sysAuthUserLvDO))
            return false;
        //  添加用户授权版本历史数据表数据
        List<SysAuthUserVDO> sysAuthUserVDOS = new ArrayList<>(16);
        List<SysAuthUserDO> userAuthes = sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                .eq("app_id", appId).eq("user_id", userId));
        if (CollectionUtils.isNotEmpty(userAuthes)) {
            userAuthes.stream().forEach(sysAuthUserDO -> {
                SysAuthUserVDO sysAuthUserVDO = JsonUtils.castObjectForSetIdNull(sysAuthUserDO, SysAuthUserVDO.class);
                sysAuthUserVDO.setId(UuidUtils.createUUID());
                sysAuthUserVDO.setLvId(id);
                sysAuthUserVDO.setAuthName(sysAuthUserDO.getAuthName());
                sysAuthUserVDOS.add(sysAuthUserVDO);
            });
        }
        if (CollectionUtils.isNotEmpty(sysAuthUserVDOS)) {
            sysAuthUserVService.saveBatch(sysAuthUserVDOS);
        }
        return true;
    }

    /**
     * 追加权限用户表数据
     *
     * @param appId
     * @param userId
     * @param authId
     * @param revoker
     * @return
     * @author zuogang
     * @date 2019/4/17 17:16
     */
    private SysAuthUserDO addSysAuthUser(String appId, String userId, String realName, String authName, String
            authId, Integer revoker) {
        SysAuthUserDO sysAuthUserDO = new SysAuthUserDO();
        String id = UuidUtils.createUUID();
        sysAuthUserDO.setId(id);
        sysAuthUserDO.setUserId(userId);
        sysAuthUserDO.setAuthId(authId);
        sysAuthUserDO.setAppId(appId);
        sysAuthUserDO.setRevoker(revoker);
        sysAuthUserDO.setAuthName(authName);
        sysAuthUserDO.setRealName(realName);
        sysAuthUserDO.setCreateUser(securityUtils.getCurrentUser().getId());
        sysAuthUserDO.setCreateTime(LocalDateTime.now());
//        try {
//            sysAuthUserDO.setSign(
//                    GMBaseUtil.getSign(userId + id + authId));
//        } catch (Exception e) {
//            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//        }
        return sysAuthUserDO;
//        if (!this.save(sysAuthUserDO)) {
//            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//        }
    }

    /**
     * 计算当前时间段下的变更过权限的用户列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public List<SysUserDO> getChangeUserList(Map<String, Object> map) {
        String beginTime = (String) map.get("beginTime");
        String endTime = (String) map.get("endTime");
        String appId = (String) map.get("appId");

        // 单一业务管理员
        Integer userType = securityUtils.getCurrentUser().getUserType();
        if (userType == 5) {
            if (StringUtils.isNotBlank(beginTime)) {
                // 获取当前时间段修改过的用户ID
                return sysUserService.list(new QueryWrapper<SysUserDO>()
                        .eq("is_delete", 0)
                        .inSql("id", "select id from sys_user where id in (select user_id from sys_auth_user_lv " +
                                "where app_id='" + appId + "' and version_begin_time between '" + beginTime + "' and '" +
                                endTime +
                                "') ")
                );
            }
            return sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("is_delete", 0)
                    .inSql("id", "select id from sys_user where id in (select user_id from sys_auth_user_lv " +
                            "where app_id='" + appId + "') ")
            );
        }

        if (StringUtils.isNotBlank(beginTime)) {
            // 获取当前时间段修改过的用户ID
            return sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("is_delete", 0)
                    .inSql("id", "select id from sys_user where id in (select user_id from sys_auth_user_lv " +
                            "where app_id='" + appId + "' and version_begin_time between '" + beginTime + "' and '" +
                            endTime +
                            "') and  " +
                            "organization_id in (select ORGANIZATION_ID from SYS_AUTH_SCOPE_ORG where user_id ='" +
                            securityUtils.getCurrentUserId() + "')")
            );
        }
        return sysUserService.list(new QueryWrapper<SysUserDO>()
                .eq("is_delete", 0)
                .inSql("id", "select id from sys_user where id in (select user_id from sys_auth_user_lv " +
                        "where app_id='" + appId + "') and  " +
                        "organization_id in (select ORGANIZATION_ID from SYS_AUTH_SCOPE_ORG where user_id ='" +
                        securityUtils.getCurrentUserId() + "')")
        );

        // 计算当前审计员的组织管控域
//        List<String> orgIds = new ArrayList<>(16);
//        sysAuthScopeOrgService.getOrgIdsByUserId(securityUtils.getCurrentUserId()).stream().forEach(orgId1 -> {
//            OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById(orgId1);
//            if (orgOrganizationDO != null && Objects.equals(0, orgOrganizationDO.getBeDeleted())) {
//                if (!orgIds.contains(orgId1)) {
//                    orgIds.add(orgId1);
//                }
//            }
//        });
//
//        // 获取组织管控域下的所有用户ID信息
//        List<String> userIds = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
//                .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
//                .in("organization_id", orgIds)).stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
//
//
//        // 获取当前时间段修改过的用户ID
//        List<String> updUserIds = sysAuthUserLvService.list(new QueryWrapper<SysAuthUserLvDO>()
//                        .ge(StringUtils.isNotBlank(beginTime), "version_begin_time", MapWrapper.formDate(beginTime))
//                        .le(StringUtils.isNotBlank(endTime), "version_begin_time", MapWrapper.formDate(endTime))
//                        .eq("app_id", appId)
////                .exists("select from sys_user where organization_id in (select ORGANIZATION_ID from " +
////                        "SYS_AUTH_SCOPE_ORG where user_id='" + securityUtils.getCurrentUserId() + "')")
////                .and(userIds == null || userIds.size() == 0, i -> i.eq("1", "2"))
////                .in("user_id", userIds)
//        ).stream().distinct().map(SysAuthUserLvDO::getUserId).collect(Collectors
//                .toList());
//        if (CollectionUtils.isNotEmpty(updUserIds)) {
//            List<String> newUserIds = new ArrayList<>(16);
//            updUserIds.forEach(userId -> {
//                if (userIds.contains(userId)) {
//                    newUserIds.add(userId);
//                }
//            });
//            if (CollectionUtils.isNotEmpty(newUserIds)) {
//                return (ArrayList) sysUserService.listByIds(newUserIds);
//            }
//            return new ArrayList<>(16);
//        }
//        return new ArrayList<>(16);
    }
}
