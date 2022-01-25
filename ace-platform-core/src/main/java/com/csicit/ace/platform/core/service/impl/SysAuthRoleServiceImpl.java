package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthRoleMapper;
import com.csicit.ace.data.persistent.service.SysMessageService;
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
 * 权限角色关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthRoleService")
public class SysAuthRoleServiceImpl extends BaseServiceImpl<SysAuthRoleMapper, SysAuthRoleDO> implements
        SysAuthRoleService {

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysAuthRoleVService sysAuthRoleVService;

    @Autowired
    SysAuthRoleLvService sysAuthRoleLvService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysAuthRoleService sysAuthRoleService;

    @Autowired
    SysWaitGrantAuthService sysWaitGrantAuthService;

    @Autowired
    SysMessageService sysMessageService;

    /**
     * 角色授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:15
     */
    @Override
    public boolean saveRoleAuth(GrantAuthReciveVO grantAuthReciveVO) {
        // 删除老角色权限
        this.remove(new QueryWrapper<SysAuthRoleDO>()
                .eq("role_id", grantAuthReciveVO.getRoleId()).inSql("auth_id", "select id from sys_auth where " +
                        "app_id='" + grantAuthReciveVO.getAppId() + "'"));

        // 添加该角色的待激活表
        if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>().eq("ACTIVATE_ID", grantAuthReciveVO
                .getRoleId())
                .eq("TYPE", 1).eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
            sysWaitGrantAuthService.saveSysWaitGrantAuth(grantAuthReciveVO.getRoleId(), 1, grantAuthReciveVO.getAppId
                    ());
        }

        StringBuffer stringBuffer = new StringBuffer();
        List<SysAuthRoleDO> sysAuthRoleDOS = new ArrayList<>(16);
        grantAuthReciveVO.getGrantAuthVOList().forEach(grantAuth -> {
            stringBuffer.append(grantAuth.getName());
            stringBuffer.append(",");
            // 添加新角色权限
            sysAuthRoleDOS.add(this.addSysAuthRole(grantAuthReciveVO.getRoleId(), grantAuthReciveVO.getRoleName(),
                    grantAuth.getId(), grantAuth.getName(), grantAuth.getRevoker()));
        });
        if (CollectionUtils.isNotEmpty(sysAuthRoleDOS)) {
            if (!this.saveBatch(sysAuthRoleDOS)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }

        if (grantAuthReciveVO.getGrantAuthVOList().size() == 0) {
            return sysAuditLogService.
                    saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "清空角色权限", "(清空)授权角色【" + grantAuthReciveVO
                                    .getRoleName() + "】的权限",
                            securityUtils.getCurrentGroupId(), grantAuthReciveVO.getAppId());
        }

        return sysAuditLogService.
                saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存角色权限","(保存)授权角色【" + grantAuthReciveVO.getRoleName()
                                + "】的权限："+ stringBuffer
                                .substring(0, stringBuffer
                                        .length() - 1),
                        securityUtils.getCurrentGroupId(), grantAuthReciveVO.getAppId());
    }

    /**
     * 角色授权(激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:15
     */
    @Override
    public boolean saveRoleAuthActivation(GrantAuthReciveVO grantAuthReciveVO) {

        if (!this.saveRoleAuthHistory(grantAuthReciveVO.getAppId(), grantAuthReciveVO.getRoleId(), grantAuthReciveVO
                .getRoleName())) {
            return false;
        }

        if (!this.saveRoleActivateForUserValidAuth(Arrays.asList(grantAuthReciveVO.getRoleId()), grantAuthReciveVO
                .getAppId())) {
            return false;
        }
        return true;
    }

    /**
     * 角色授权(激活)--角色权限历史数据更新
     *
     * @param appId
     * @param roleId
     * @param roleName
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:15
     */
    @Override
    public boolean saveRoleAuthHistory(String appId, String roleId, String roleName) {
        // 角色权限激活
        if (!this.authRoleActivation(roleId, appId))
            return false;

        // 删除该角色的待激活表
        sysWaitGrantAuthService.remove(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 1).eq("app_id", appId)
                .eq("activate_id", roleId));

        StringBuffer stringBuffer = new StringBuffer();
        List<SysAuthRoleDO> sysAuthRoleDOS = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                .eq("role_id", roleId));
        if (sysAuthRoleDOS != null && sysAuthRoleDOS.size() > 0) {
            sysAuthRoleDOS.stream().forEach(sysAuthRoleDO -> {
                stringBuffer.append(sysAuthRoleDO.getAuthName());
                stringBuffer.append(",");
            });
        }
        return sysAuditLogService.
                saveLog(new AuditLogTypeDO(AuditLogType.success, "激活"), "激活角色权限","(激活)授权角色【" + roleName + "】的权限："+
                        (stringBuffer.length() > 0 ?
                                stringBuffer.substring(0,
                                        stringBuffer.length() - 1) : null),
                        securityUtils.getCurrentGroupId(), appId);
    }

    /**
     * 角色授权(激活)--相关用户有效权限更新计算
     *
     * @param roleIds
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:15
     */
    @Override
    public boolean saveRoleActivateForUserValidAuth(List<String> roleIds, String appId) {
        // 添加有效权限表
        // 根据roleId获取UserIds
        List<String> userIds = sysUserRoleService.getEffectiveUserDatas(roleIds);
        if (CollectionUtils.isNotEmpty(userIds)) {
            StringBuffer sb = new StringBuffer(16);
            userIds.stream().distinct().forEach(userId -> {
                // 添加新有效权限
                if (!sysAuthMixService.saveAuthMixForApp(userId, appId)) {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
                sb.append("S");
                if (sb.length() * 100 / userIds.size() > 1) {
                    // 前台推送
                    Map<String, Object> map = new HashMap<>();
                    map.put("count", userIds.size());
                    map.put("currentNum", sb.length());
                    map.put("eventName", Constants.OneClickActivateEvent);
                    // 单体版无效，因为单体版启动的appName是ace-zuul,但是前端的appName是platform
                    sysMessageService.fireSocketEvent(new SocketEventVO(Arrays.asList(securityUtils.getCurrentUserId()),
                            Constants.OneClickActivateEvent, map, appName));
                }
            });
        }

        return true;
    }

    /**
     * 权限角色授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:15
     */
    @Override
    public Map<String, Object> saveAuthRoleGrant(GrantAuthReciveVO grantAuthReciveVO) {

        Map<String, Object> result = new HashMap<>();
        // 发生变化关系的角色ID列表
        List<String> roleIds = new ArrayList<>(16);

        // 获取该权限以及该权限的所有上级权限
        List<SysAuthDO> parentAuthList = new ArrayList<>(16);
        parentAuthList.add(sysAuthService.getById(grantAuthReciveVO.getAuthId()));
        sysAuthService.getParentAuths(parentAuthList, grantAuthReciveVO.getAuthId());

        //获取该权限以及该权限的所有下级权限
        List<SysAuthDO> childAuthList = new ArrayList<>(16);
        childAuthList.add(sysAuthService.getById(grantAuthReciveVO.getAuthId()));
        sysAuthService.getChildAuths(grantAuthReciveVO.getAuthId(), childAuthList);

        // 获取要授权的角色列表
        List<SysRoleDO> grantRoles = new ArrayList<>(16);
        // 获取要禁用的角色列表
        List<SysRoleDO> unUsedRoles = new ArrayList<>(16);
        if (grantAuthReciveVO.getSysAuthRoleDOList() != null && grantAuthReciveVO.getSysAuthRoleDOList().size() > 0) {
            grantAuthReciveVO.getSysAuthRoleDOList().stream().forEach(authRole -> {
                SysRoleDO sysRoleDO = new SysRoleDO();
                sysRoleDO.setId(authRole.getRoleId());
                sysRoleDO.setName(authRole.getRoleName());
                if (Objects.equals(authRole.getRevoker(), 0)) {
                    // 授予
                    grantRoles.add(sysRoleDO);
                } else {
                    // 禁用
                    unUsedRoles.add(sysRoleDO);

                }
            });
        }


        StringBuffer grantStr = new StringBuffer();

        // 获取原有的该权限对应的roleIds，通过判断获取被删除的角色ID
        List<String> grantRoleIds = grantRoles.stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
        List<String> oldGrantRoleIds = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                .eq("auth_id", grantAuthReciveVO.getAuthId()).eq("is_revoke", 0))
                .stream().map(SysAuthRoleDO::getRoleId).collect(Collectors.toList());

        // 判断权限角色新授予关系与旧授予关系是否发生改变
        if (!(grantRoleIds.size() == oldGrantRoleIds.size() &&
                grantRoleIds.containsAll(oldGrantRoleIds))) {
            // 有变化
            // 删除的权限角色
            List<String> deletedRoleIds = oldGrantRoleIds.stream().filter(it -> !grantRoleIds.contains(it)).collect
                    (Collectors.toList());
            deletedRoleIds.stream().forEach(oldRoleId -> {
                // 针对被删除的角色Id
                // 删除角色Id对应的该权限及以下权限的关系表
                childAuthList.stream().forEach(sysAuthDO -> {
                    this.remove(new QueryWrapper<SysAuthRoleDO>()
                            .eq("is_revoke", 0)
                            .eq("auth_id", sysAuthDO.getId())
                            .eq("role_id", oldRoleId));
                });
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                        .eq("ACTIVATE_ID", oldRoleId).eq("type", 1)
                        .eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    roleIds.add(oldRoleId);
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(oldRoleId, 1, grantAuthReciveVO
                            .getAppId());
                }

            });
            // 添加新的权限角色
            List<String> addRoleIds = grantRoleIds.stream().filter(it -> !oldGrantRoleIds.contains(it)).collect
                    (Collectors.toList());
            addRoleIds.stream().forEach(roleId -> {
                SysRoleDO sysRoleDO = sysRoleService.getById(roleId);
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                        .eq("ACTIVATE_ID", sysRoleDO.getId()).eq("type", 1)
                        .eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    roleIds.add(sysRoleDO.getId());
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(sysRoleDO.getId(), 1, grantAuthReciveVO.getAppId());
                }
                List<SysAuthRoleDO> sysAuthRoleDOS = new ArrayList<>(16);
                parentAuthList.stream().forEach(sysAuthDO -> {
                    this.remove(new QueryWrapper<SysAuthRoleDO>()
                            .eq("role_id", sysRoleDO.getId()).eq("auth_id", sysAuthDO.getId()));
                    sysAuthRoleDOS.add(this.addSysAuthRole(sysRoleDO.getId(), sysRoleDO.getName(), sysAuthDO.getId(),
                            sysAuthDO
                                    .getName(), 0));
                });
                // 查询该角色所有的权限再去重
                List<SysAuthRoleDO> sysAuthRoleDOS1 = new ArrayList<>(16);
                List<String> authIds = new ArrayList<>(16);
                sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>().inSql("auth_id", "select id from sys_auth " +
                        "where app_id='" + grantAuthReciveVO.getAppId() +
                        "'").eq("role_id", sysRoleDO.getId()).eq("is_revoke", 0))
                        .stream().forEach(sysAuthRoleDO -> {
                    if (!authIds.contains(sysAuthRoleDO.getAuthId())) {
                        authIds.add(sysAuthRoleDO.getAuthId());
                        sysAuthRoleDOS1.add(sysAuthRoleDO);
                    }
                });
                // 删除该角色所有的权限
                this.remove(new QueryWrapper<SysAuthRoleDO>().eq("role_id", sysRoleDO.getId())
                        .inSql("auth_id", "select id from sys_auth where app_id='" + grantAuthReciveVO.getAppId() +
                                "'").eq("is_revoke", 0));
                // 添加新的角色权限

                sysAuthRoleDOS1.stream().forEach(sysAuthRoleDO -> {
                    sysAuthRoleDOS.add(this.addSysAuthRole(sysRoleDO.getId(), sysRoleDO.getName(), sysAuthRoleDO
                                    .getAuthId(),
                            sysAuthRoleDO.getAuthName(), 0));
                });

                if (CollectionUtils.isNotEmpty(sysAuthRoleDOS)) {
                    if (!this.saveBatch(sysAuthRoleDOS)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                }

                grantStr.append(sysRoleDO.getName());
                grantStr.append(",");

            });
        }

        StringBuffer unUsedStr = new StringBuffer();

        List<String> unUsedRoleIds = unUsedRoles.stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
        List<String> oldUnUserdRoleIds = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                .eq("auth_id", grantAuthReciveVO.getAuthId()).eq("is_revoke", 1))
                .stream().map(SysAuthRoleDO::getRoleId).collect(Collectors.toList());
        // 判断权限角色新禁用关系与旧禁用关系是否发生改变
        if (!(unUsedRoleIds.size() == oldUnUserdRoleIds.size() && unUsedRoleIds.containsAll(oldUnUserdRoleIds))) {
            //有变化
            // 删除的权限角色
            List<String> deleteRoleIds = oldUnUserdRoleIds.stream().filter(it -> !unUsedRoleIds.contains(it)).collect
                    (Collectors.toList());
            deleteRoleIds.stream().forEach(oldRoleId -> {
                // 针对被删除的角色Id
                // 删除角色Id对应的该权限及以下权限的关系表
                this.remove(new QueryWrapper<SysAuthRoleDO>()
                        .eq("auth_id", grantAuthReciveVO.getAuthId())
                        .eq("role_id", oldRoleId));
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                        .eq("ACTIVATE_ID", oldRoleId).eq("type", 1)
                        .eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    roleIds.add(oldRoleId);
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(oldRoleId, 1, grantAuthReciveVO
                            .getAppId());
                }
            });
            // 新的权限角色
            List<String> addRoleIds = unUsedRoleIds.stream().filter(it -> !oldUnUserdRoleIds.contains(it)).collect
                    (Collectors.toList());
            addRoleIds.stream().forEach(roleId -> {
                SysRoleDO sysRoleDO = sysRoleService.getById(roleId);
                if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                        .eq("ACTIVATE_ID", sysRoleDO.getId()).eq("type", 1)
                        .eq("APP_ID", grantAuthReciveVO.getAppId())) == 0) {
                    roleIds.add(sysRoleDO.getId());
                    // 添加新的待激活表数据
                    sysWaitGrantAuthService.saveSysWaitGrantAuth(sysRoleDO.getId(), 1, grantAuthReciveVO.getAppId());
                }
                List<SysAuthRoleDO> sysAuthRoleDOS = new ArrayList<>(16);
                childAuthList.stream().forEach(sysAuthDO -> {
                    this.remove(new QueryWrapper<SysAuthRoleDO>()
                            .eq("role_id", sysRoleDO.getId()).eq("auth_id", sysAuthDO.getId()));
                    sysAuthRoleDOS.add(this.addSysAuthRole(sysRoleDO.getId(), sysRoleDO.getName(), sysAuthDO.getId(),
                            sysAuthDO
                                    .getName(), 1));
                });
                // 查询该角色所有的权限再去重
                List<SysAuthRoleDO> sysAuthRoleDOS1 = new ArrayList<>(16);
                List<String> authIds = new ArrayList<>(16);
                sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>().inSql("auth_id", "select id from sys_auth " +
                        "where app_id='" + grantAuthReciveVO.getAppId() +
                        "'")
                        .eq("role_id", sysRoleDO.getId()).eq("is_revoke", 1))
                        .stream().forEach(sysAuthRoleDO -> {
                    if (!authIds.contains(sysAuthRoleDO.getAuthId())) {
                        authIds.add(sysAuthRoleDO.getAuthId());
                        sysAuthRoleDOS1.add(sysAuthRoleDO);
                    }
                });
                // 删除该角色所有的权限
                this.remove(new QueryWrapper<SysAuthRoleDO>().eq("role_id", sysRoleDO.getId())
                        .eq("is_revoke", 1).inSql("auth_id", "select id from sys_auth where app_id='" +
                                grantAuthReciveVO.getAppId() + "'"));
                // 添加新的角色权限
                sysAuthRoleDOS1.stream().forEach(sysAuthRoleDO -> {
                    sysAuthRoleDOS.add(this.addSysAuthRole(sysRoleDO.getId(), sysRoleDO.getName(), sysAuthRoleDO
                                    .getAuthId(),
                            sysAuthRoleDO.getAuthName(), 1));
                });

                if (CollectionUtils.isNotEmpty(sysAuthRoleDOS)) {
                    if (!this.saveBatch(sysAuthRoleDOS)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                }
                unUsedStr.append(sysRoleDO.getName());
                unUsedStr.append(",");

            });
        }
        result.put("result", sysAuditLogService.
                saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"),"权限角色保存" ,"权限角色授权(保存):权限【" + grantAuthReciveVO
                                .getAuthName() +
                                "】的授权角色："+ (grantStr.length() > 0 ? grantStr.substring(0, grantStr.length() - 1) :
                                null) +
                                "；禁用角色：" + (unUsedStr.length() > 0 ? unUsedStr.substring(0, unUsedStr.length() - 1) :
                                null),
                        securityUtils.getCurrentGroupId(), grantAuthReciveVO.getAppId()));
        result.put("roleIds", roleIds);
        return result;
    }

    @Override
    public List<SysAuthRoleVDO> getActiveAuthRolesForAppId(List<String> roleIds, String appId) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>(16);
        }
        StringJoiner roleIdsStr = new StringJoiner(",");
        roleIds.stream().forEach(roleId -> {
            roleIdsStr.add("'" + roleId + "'");
        });
        return sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                .inSql("lv_id", "select id from sys_auth_role_lv where IS_LAST_VERSION=1 and app_id='" + appId +
                        "' and role_id in (" + roleIdsStr.toString() + ")").inSql("auth_id", "select id from sys_auth")
        );
    }

    @Override
    public List<SysAuthRoleVDO> getActiveAuthRoles(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>(16);
        }
        StringJoiner roleIdsStr = new StringJoiner(",");
        roleIds.stream().forEach(roleId -> {
            roleIdsStr.add("'" + roleId + "'");
        });
        return sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                .inSql("lv_id", "select id from sys_auth_role_lv where IS_LAST_VERSION=1 and role_id in (" +
                        roleIdsStr.toString() + ")")
        );
    }

    /**
     * 获取当前角色的激活权限
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<SysAuthRoleVDO> getActiveAuthRole(String roleId) {
        List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("IS_LAST_VERSION", 1).eq("role_id", roleId));
        if (sysAuthRoleLvDOS != null && sysAuthRoleLvDOS.size() > 0) {
            List<SysAuthRoleVDO> actAuthRoles = new ArrayList<>(16);
            sysAuthRoleLvDOS.stream().forEach(sysAuthRoleLvDO -> {
//                actAuthRoles.addAll(sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
//                        .eq("role_id", roleId).eq("lv_id",
//                                sysAuthRoleLvDO.getId())));
                sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                        .eq("role_id", roleId).eq("lv_id",
                                sysAuthRoleLvDO.getId())).stream().forEach(sysAuthRoleVDO -> {
                    if (sysAuthService.count(new QueryWrapper<SysAuthDO>()
                            .eq("id", sysAuthRoleVDO.getAuthId())) > 0) {
                        actAuthRoles.add(sysAuthRoleVDO);
                    }
                });

            });
            return actAuthRoles;
        }

//        if (sysAuthRoleLvService.getOne(
//                new QueryWrapper<SysAuthRoleLvDO>()
//                        .eq("is_last_version", 1).eq("role_id", roleId)) != null) {
//            String lvId = sysAuthRoleLvService.getOne(
//                    new QueryWrapper<SysAuthRoleLvDO>()
//                            .eq("is_last_version", 1).eq("role_id", roleId)).getId();
//
//        }
        return new ArrayList<>(16);
    }

    /**
     * 角色权限激活
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/5/5 14:58
     */
    @Override
    public boolean authRoleActivation(String roleId, String appId) {
        //角色权限表数据 激活
//        sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
//                .eq("role_id", roleId))
//                .stream().forEach(sysAuthRoleDO -> {
//            sysAuthRoleService.updateById(sysAuthRoleDO);
//        });

//        String appId = sysRoleService.getById(roleId).getAppId();

        // 获取角色授权版本控制表的版本编号
        Integer oldVersionNo = -1;
        SysAuthRoleLvDO sysAuthRoleLvDO1 = sysAuthRoleLvService.getOne(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("is_last_version", 1).eq("role_id", roleId).eq("app_id", appId));
        if (sysAuthRoleLvDO1 != null) {
            oldVersionNo = sysAuthRoleLvDO1.getVersionNo();
        }

        // 更改角色授权版本控制表数据
        sysAuthRoleLvService.update(new SysAuthRoleLvDO(), new UpdateWrapper<SysAuthRoleLvDO>()
                .eq("is_last_version", 1).eq("role_id", roleId).eq("app_id", appId)
                .set("is_last_version", 0)
                .set("version_end_time", LocalDateTime.now())
                .set("version_end_user_id", securityUtils.getCurrentUser().getId())
        );

        // 添加角色授权版本控制表数据
        SysAuthRoleLvDO sysAuthRoleLvDO = new SysAuthRoleLvDO();
        String id = UuidUtils.createUUID();
        sysAuthRoleLvDO.setId(id);
        sysAuthRoleLvDO.setRoleId(roleId);
        sysAuthRoleLvDO.setAppId(appId);
        sysAuthRoleLvDO.setVersionNo(oldVersionNo + 1);
        sysAuthRoleLvDO.setLastVersion(1);
        sysAuthRoleLvDO.setVersionBeginTime(LocalDateTime.now());
        sysAuthRoleLvDO.setVersionBeginUserId(securityUtils.getCurrentUser().getId());
        if (!sysAuthRoleLvService.save(sysAuthRoleLvDO))
            return false;

        // 添加角色授权版本历史数据表数据
        List<SysAuthRoleVDO> sysAuthRoleVDOS = new ArrayList<>(16);
        sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>().inSql("auth_id", "select id from sys_auth where " +
                "app_id='" + appId + "'")
                .eq("role_id", roleId))
                .stream().forEach(sysAuthRoleDO -> {
            SysAuthRoleVDO sysAuthRoleVDO = JsonUtils.castObjectForSetIdNull(sysAuthRoleDO, SysAuthRoleVDO.class);
            sysAuthRoleVDO.setId(UuidUtils.createUUID());
            sysAuthRoleVDO.setLvId(id);
            sysAuthRoleVDO.setAuthName(sysAuthRoleDO.getAuthName());
            sysAuthRoleVDOS.add(sysAuthRoleVDO);
//            sysAuthRoleVService.save(sysAuthRoleVDO);
        });
        if (CollectionUtils.isNotEmpty(sysAuthRoleVDOS)) {
            sysAuthRoleVService.saveBatch(sysAuthRoleVDOS);
        }
//        sysAuthRoleVService.saveSysAuthRoleV(id);
        return true;
    }

    /**
     * 追加权限角色表数据
     *
     * @param roleId
     * @param authId
     * @param revoker
     * @return void
     * @author zuogang
     * @date 2019/4/17 17:16
     */
    private SysAuthRoleDO addSysAuthRole(String roleId, String roleName, String authId, String authName, Integer
            revoker) {
        SysAuthRoleDO sysAuthRoleDO = new SysAuthRoleDO();
        String id = UuidUtils.createUUID();
        sysAuthRoleDO.setId(id);
        sysAuthRoleDO.setAuthId(authId);
        sysAuthRoleDO.setAuthName(authName);
        sysAuthRoleDO.setRoleId(roleId);
        sysAuthRoleDO.setRoleName(roleName);
        sysAuthRoleDO.setRevoker(revoker);
        sysAuthRoleDO.setCreateUser(securityUtils.getCurrentUser().getId());
        sysAuthRoleDO.setCreateTime(LocalDateTime.now());
//        try {
//            sysAuthRoleDO.setSign(GMBaseUtil.getSign(roleId + id + authId));
//        } catch (Exception e) {
//            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//        }
        return sysAuthRoleDO;
//        if (!this.save(sysAuthRoleDO)) {
//            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//        }
    }

    /**
     * 计算当前时间段下的变更过权限的角色列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public List<SysRoleDO> getChangeRoleList(Map<String, Object> map) {
        String beginTime = (String) map.get("beginTime");
        String endTime = (String) map.get("endTime");
        String appId = (String) map.get("appId");

        // 获取当前时间段修改过的角色ID
        List<String> updRoleIds = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                .ge(StringUtils.isNotBlank(beginTime), "version_begin_time", MapWrapper.formDate(beginTime))
                .le(StringUtils.isNotBlank(endTime), "version_begin_time", MapWrapper.formDate(endTime))
                .eq("app_id", appId)).stream().distinct().map(SysAuthRoleLvDO::getRoleId).collect(Collectors
                .toList());

        if (updRoleIds.size() > 0) {
            return sysRoleService.list(new QueryWrapper<SysRoleDO>()
                    .in("id", updRoleIds));
        }
        return new ArrayList<>(16);
    }

}
