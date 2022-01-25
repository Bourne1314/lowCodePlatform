package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.internationalization.InternationUtils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.GrantAuthVO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.mapper.SysGrantAuthMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 授权管理 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:26
 */
@Service("sysGrantAuthService")
public class SysGrantAuthServiceImpl extends BaseServiceImpl<SysGrantAuthMapper, GrantAuthReciveVO> implements
        SysGrantAuthService {

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysAuthRoleService sysAuthRoleService;

    @Autowired
    SysAuthUserService sysAuthUserService;

    @Autowired
    SysAuthScopeUserGroupService sysAuthScopeUserGroupService;

    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    SysWaitGrantAuthService sysWaitGrantAuthService;

    @Autowired
    SysWaitGrantUserService sysWaitGrantUserService;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    SysUserGroupUserService sysUserGroupUserService;

    @Autowired
    SysMessageService sysMessageService;

    /**
     * 权限授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:15
     */
    @Override
    public boolean saveUserRoleAuth(GrantAuthReciveVO grantAuthReciveVO) {
        // 权限角色授权
        Map<String, Object> result1=sysAuthRoleService.saveAuthRoleGrant(grantAuthReciveVO);
        if (!(Boolean) result1.get("result"))
            return false;

        // 权限用户授权
        Map<String, Object> result2=sysAuthUserService.saveAuthUserGrant(grantAuthReciveVO);
        if (!(Boolean)result2.get("result"))
            return false;

        return true;
    }


    /**
     * 获取用户授权信息
     *
     * @param userId 用户id
     * @return java.util.List<com.csicit.ace.common.pojo.vo.GrantAuthVO>
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public List<GrantAuthVO> getUserAuths(String userId, String appId) {

        List<SysAuthDO> sortAuths = sysAuthService.list(new QueryWrapper<SysAuthDO>().eq("app_id", appId).orderByAsc
                ("sort_path"));

        List<SysAuthUserDO> userAuths =
                sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>().eq("user_id", userId));
        List<GrantAuthVO> grantAuthVOS = new ArrayList<>();
        sortAuths.stream().forEach(auth -> {
            GrantAuthVO grantAuthVO = JsonUtils.castObjectForSetIdNull(auth, GrantAuthVO.class);
            grantAuthVO.setId(auth.getId());
            grantAuthVO.setRevoker(-1);
            setUserAuth(userAuths, grantAuthVO, auth);
            grantAuthVO.setGrantFlag(false);
            grantAuthVO.setRevokeFlag(false);
            if (grantAuthVO.getRevoker() == 1) {
                grantAuthVO.setRevokeFlag(true);
            } else if (grantAuthVO.getRevoker() == 0) {
                grantAuthVO.setGrantFlag(true);
            }
            grantAuthVOS.add(grantAuthVO);
        });
        return grantAuthVOS;
    }

    /**
     * 获取角色授权信息
     *
     * @param roleId 用户id
     * @return java.util.List<com.csicit.ace.common.pojo.vo.GrantAuthVO>
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public List<GrantAuthVO> getRoleAuths(String roleId, String appId) {

        List<SysAuthDO> sortAuths = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", appId).orderByAsc("sort_path"));

        List<SysAuthRoleDO> roleAuths =
                sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>().eq("role_id", roleId));
        List<GrantAuthVO> grantAuthVOS = new ArrayList<>();
        sortAuths.stream().forEach(auth -> {
            GrantAuthVO grantAuthVO = JsonUtils.castObjectForSetIdNull(auth, GrantAuthVO.class);
            grantAuthVO.setId(auth.getId());
            grantAuthVO.setRevoker(-1);
            grantAuthVO.setSortPath(auth.getSortPath());
            setRoleAuth(roleAuths, grantAuthVO, auth);
            grantAuthVO.setGrantFlag(false);
            grantAuthVO.setRevokeFlag(false);
            if (grantAuthVO.getRevoker() == 1) {
                grantAuthVO.setRevokeFlag(true);
            } else if (grantAuthVO.getRevoker() == 0) {
                grantAuthVO.setGrantFlag(true);
            }
            grantAuthVOS.add(grantAuthVO);
        });
        return grantAuthVOS;
    }

    /**
     * 获取该应用管理员所拥有的应用权限列表
     *
     * @param appId
     * @return
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public List<SysAuthDO> getAppAuths(String appId) {
        List<SysAuthDO> sortAuths = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", appId).orderByAsc("sort_path"));
        return sortAuths;
    }


    /**
     * 获取权限用户角色列表
     *
     * @param authId 权限id
     * @param appId  应用ID
     * @return com.csicit.ace.common.pojo.vo.GrantAuthReciveVO
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public GrantAuthReciveVO getUsersAndRoles(String authId, String appId) {
        // 权限用户角色列表
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();

        List<SysAuthUserDO> sysAuthUserDOList = sysAuthUserService
                .list(new QueryWrapper<SysAuthUserDO>()
                        .eq("auth_id", authId).inSql("user_id","select id from sys_user where is_delete=0"));
        if (CollectionUtils.isNotEmpty(sysAuthUserDOList)) {
            List<String> userIds = sysAuthUserDOList.stream().map(SysAuthUserDO::getUserId).collect(Collectors.toList());
            List<SysUserDO> users = sysUserService.list(new QueryWrapper<SysUserDO>().in("id", userIds).select("id", "real_name"));
            Map<String, String> userIdAndRealNames = new HashMap<>();
            users.stream().forEach(userDO -> {
                userIdAndRealNames.put(userDO.getId(), userDO.getRealName());
            });
            sysAuthUserDOList.stream().forEach(sysAuthUserDO -> {
                sysAuthUserDO.setRealName(userIdAndRealNames.get(sysAuthUserDO.getUserId()));
            });
        }

        List<SysAuthRoleDO> sysAuthRoleDOList = sysAuthRoleService
                .list(new QueryWrapper<SysAuthRoleDO>()
                        .eq("auth_id", authId));
        List<SysAuthRoleDO> realList = new ArrayList<>(16);
        if (CollectionUtils.isNotEmpty(sysAuthRoleDOList)) {
            List<String> roleIds = sysAuthRoleDOList.stream().map(SysAuthRoleDO::getRoleId).collect(Collectors.toList());
            List<SysRoleDO> roles = sysRoleService.list(new QueryWrapper<SysRoleDO>().in("id", roleIds).select("id", "name"));
//                    .eq("app_id", appId)
            Map<String, String> roleIdAndNames = new HashMap<>();
            roles.stream().forEach(roleDO -> {
                roleIdAndNames.put(roleDO.getId(), roleDO.getName());
            });
            sysAuthRoleDOList.stream().forEach(sysAuthRoleDO -> {
                if (roleIdAndNames.containsKey(sysAuthRoleDO.getRoleId())) {
                    sysAuthRoleDO.setRoleName(roleIdAndNames.get(sysAuthRoleDO.getRoleId()));
                    realList.add(sysAuthRoleDO);
                }
            });
        }
        grantAuthReciveVO.setSysAuthUserDOList(sysAuthUserDOList);
        grantAuthReciveVO.setSysAuthRoleDOList(realList);
        return grantAuthReciveVO;

    }

    /**
     * 设置角色权限
     *
     * @param roleAuths
     * @param grantAuthVO
     * @param auth
     * @return void
     * @author shanwj
     * @date 2019/5/17 11:56
     */
    private void setRoleAuth(List<SysAuthRoleDO> roleAuths, GrantAuthVO grantAuthVO, SysAuthDO auth) {
        roleAuths.stream().forEach(roleAuth -> {
            if (Objects.equals(auth.getId(), roleAuth.getAuthId())) {
                grantAuthVO.setRevoker(roleAuth.getRevoker());
            }
        });
    }

    /**
     * 设置用户权限
     *
     * @param userAuths
     * @param grantAuthVO
     * @param auth
     * @return void
     * @author shanwj
     * @date 2019/5/17 11:56
     */
    private void setUserAuth(List<SysAuthUserDO> userAuths, GrantAuthVO grantAuthVO, SysAuthDO auth) {
        userAuths.stream().forEach(userAuth -> {
            if (Objects.equals(auth.getId(), userAuth.getAuthId())) {
                grantAuthVO.setRevoker(userAuth.getRevoker());
            }
        });
    }

    /**
     * 获取待激活的用户列表和角色列表
     *
     * @param appId 应用ID
     * @return com.csicit.ace.common.pojo.vo.GrantAuthReciveVO
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public GrantAuthReciveVO waitActivationUserAndRoleList(String appId) {
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();

        // 获取当前集团的ID
        String groupId = securityUtils.getCurrentUser().getGroupId();

        // 获得当前管理员的UserID
        String userId = securityUtils.getCurrentUserId();

        // 获得当前管理员组织管控域
        List<String> orgIds = sysAuthScopeOrgService.getOrgIdsByUserId(userId);

        // 待激活的用户列表
        List<SysUserDO> sysUserDOList = new ArrayList<>(16);

        List<String> userIds = sysWaitGrantAuthService.list(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 0).eq("app_id", appId))
                .stream().map(SysWaitGrantAuthDO::getActivateId).collect(Collectors.toList());

        // 获取当前集团的所有用户
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgIds)) {
            sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("group_id", groupId)
                    .in("organization_id", orgIds))
                    .stream().forEach(sysUserDO -> {
                if (Objects.equals(0, sysUserDO.getBeDeleted())) {
                    if (userIds.contains(sysUserDO.getId())) {
                        sysUserDOList.add(sysUserDO);
                    }
                }
            });
        }


        // 待激活的角色列表
        List<SysRoleDO> sysRoleDOList = new ArrayList<>(16);

        List<String> roleIds = sysWaitGrantAuthService.list(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 1).eq("app_id", appId))
                .stream().map(SysWaitGrantAuthDO::getActivateId).collect(Collectors.toList());

        // 获取当前appID的所有角色
        sysRoleService.list(new QueryWrapper<SysRoleDO>()
                .eq("app_id", appId).or().isNull("app_id"))
                .stream().forEach(sysRoleDO -> {
            if (roleIds.contains(sysRoleDO.getId())) {
                sysRoleDOList.add(sysRoleDO);
            }

        });
        grantAuthReciveVO.setSysUserList(sysUserDOList);
        grantAuthReciveVO.setSysRoleList(sysRoleDOList);
        return grantAuthReciveVO;
    }

    /**
     * 获取当前应用管理员需要激活权限的用户和角色个数
     *
     * @return com.csicit.ace.common.pojo.vo.GrantAuthReciveVO
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public GrantAuthReciveVO waitActivationUserAndRoleCount() {
        GrantAuthReciveVO grantAuthReciveVO = new GrantAuthReciveVO();

        // 获取当前集团的ID
        String groupId = securityUtils.getCurrentUser().getGroupId();

        // 获得当前管理员的UserID
        String userId = securityUtils.getCurrentUserId();

        // 获得当前管理员组织管控域
        List<String> orgIds = sysAuthScopeOrgService.getOrgIdsByUserId(userId);

        // 获取当前管理员应用管控域
        List<String> appIds =
                sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>().eq("is_activated", 1).eq("user_id",
                        userId))
                        .stream().map(SysAuthScopeAppDO::getAppId).distinct().collect(Collectors.toList());

        int userCount = 0;

        List<String> userIds = sysWaitGrantAuthService.list(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 0)
                .and(appIds == null || appIds.size() == 0, i -> i.eq("1", "2"))
                .in("app_id", appIds))
                .stream().map(SysWaitGrantAuthDO::getActivateId).collect(Collectors.toList());

        // 获取当前应用管理员能管控的所有用户


        for (SysUserDO sysUserDO : sysUserService.list(new QueryWrapper<SysUserDO>()
                .eq("group_id", groupId)
                .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                .in("organization_id", orgIds))) {
            if (Objects.equals(0, sysUserDO.getBeDeleted())) {
                if (userIds.contains(sysUserDO.getId())) {
                    userCount = userCount + 1;
                }
            }

        }

        // 带激活的用户个数
        grantAuthReciveVO.setUserCount(userCount);

        // 获取该应用管理员管控的应用域
//        List<SysGroupAppDO> apps = sysGroupAppService.listUserOrgApp();

        int roleCount = 0;

        List<String> roleIds = sysWaitGrantAuthService.list(new QueryWrapper<SysWaitGrantAuthDO>()
                .eq("type", 1)
                .and(appIds == null || appIds.size() == 0, i -> i.eq("1", "2"))
                .in("app_id", appIds))
                .stream().map(SysWaitGrantAuthDO::getActivateId).collect(Collectors.toList());

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(appIds)) {
            for (String appId : appIds) {
                // 获取当前appID的所有角色
                for (SysRoleDO sysRoleDO : sysRoleService.list(new QueryWrapper<SysRoleDO>()
                        .eq("app_id", appId))) {
                    if (roleIds.contains(sysRoleDO.getId())) {
                        roleCount = roleCount + 1;
                    }
                }
            }
        }

        // 带激活的角色个数
        grantAuthReciveVO.setRoleCount(roleCount);

        int roleUserCount = 0;

        List<String> userIds2 = sysWaitGrantUserService.list(new QueryWrapper<SysWaitGrantUserDO>()
                .and(appIds == null || appIds.size() == 0, i -> i.eq("1", "2"))
                .in("app_id", appIds))
                .stream().map(SysWaitGrantUserDO::getUserId).collect(Collectors.toList());

        // 获取当前应用管理员能管控的所有用户

        for (SysUserDO sysUserDO : sysUserService.list(new QueryWrapper<SysUserDO>()
                .eq("group_id", groupId)
                .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                .in("organization_id", orgIds))) {
            if (Objects.equals(0, sysUserDO.getBeDeleted())) {
                if (userIds2.contains(sysUserDO.getId())) {
                    roleUserCount = roleUserCount + 1;
                }
            }

        }
        grantAuthReciveVO.setRoleUserCount(roleUserCount);

        return grantAuthReciveVO;
    }

    /**
     * 一键权限激活
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 17:03
     */
    @Override
    public boolean saveAllActivation(GrantAuthReciveVO grantAuthReciveVO) {
        String appId = grantAuthReciveVO.getAppId();
        Integer type = grantAuthReciveVO.getType();
        // 一键激活用户
        if (type == 0) {
            // 获取当前应用需要激活的用户列表和角色列表
            List<String> userIds = sysWaitGrantAuthService.list(new QueryWrapper<SysWaitGrantAuthDO>()
                    .eq("app_id", appId).eq("type", 0)).stream().map(SysWaitGrantAuthDO::getActivateId).distinct()
                    .collect(Collectors.toList());
            // 激活用户权限
            if (CollectionUtils.isNotEmpty(userIds)) {
                StringBuffer sb = new StringBuffer(16);
                userIds.stream().forEach(userId -> {
                    GrantAuthReciveVO userGrantAuthReciveVO = new GrantAuthReciveVO();
                    userGrantAuthReciveVO.setUserId(userId);
                    userGrantAuthReciveVO.setAppId(appId);
                    if (!sysAuthUserService.saveUserAuthActivation(userGrantAuthReciveVO)) {
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
                        sysMessageService.fireSocketEvent(new SocketEventVO(Arrays.asList(securityUtils
                                .getCurrentUserId()),
                                Constants.OneClickActivateEvent, map, appName));
                    }
                });
            }
        } else if (type == 1) {
            List<String> roleIds = sysWaitGrantAuthService.list(new QueryWrapper<SysWaitGrantAuthDO>()
                    .eq("app_id", appId).eq("type", 1)).stream().distinct().map(SysWaitGrantAuthDO::getActivateId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roleIds)) {
                roleIds.stream().distinct().forEach(roleId -> {
                    if (!sysAuthRoleService.saveRoleAuthHistory(appId, roleId, sysRoleService.getById(roleId).getName
                            ())) {
                        throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                    }
                });
            }
            if (!sysAuthRoleService.saveRoleActivateForUserValidAuth(roleIds, appId)) {
                return false;
            }
        }

        return true;
    }

}
