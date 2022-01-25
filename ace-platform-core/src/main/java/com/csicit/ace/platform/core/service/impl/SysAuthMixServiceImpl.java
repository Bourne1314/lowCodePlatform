package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthMixMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.JDBCUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限管理 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service
public class SysAuthMixServiceImpl
        extends BaseServiceImpl<SysAuthMixMapper, SysAuthMixDO> implements SysAuthMixService {

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleRelationService sysRoleRelationService;

    @Autowired
    SysAuthRoleService sysAuthRoleService;

    @Autowired
    SysAuthUserService sysAuthUserService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysApiMixService sysApiMixService;

    @Autowired
    SysAuthApiService sysAuthApiService;

    @Autowired
    SysApiResourceService sysApiResourceService;

    @Autowired
    SysUserRoleVService sysUserRoleVService;

    @Autowired
    SysUserRoleLvService sysUserRoleLvService;


    /**
     * 查询指定用户的所有权限并根据app分类
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:49
     */
    @Override
    public Map<String, List> getAuthWithAppByUserId(String userId) {
        List<SysAuthMixDO> list = this.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", userId));
        Map<String, List> map = new HashMap<>();
        list.stream().forEach(auth -> {
            String appId = sysAuthService.getById(auth.getAuthId()).getAppId();
            if (!map.containsKey(appId)) {
                List<String> authList = new ArrayList<>();
                authList.add(auth.getAuthId());
                map.put(appId, authList);
            } else {
                map.get(appId).add(auth.getAuthId());
            }
        });
        return map;
    }

    /**
     * 计算普通用户在某应用下的有效权限
     *
     * @param userId
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2019/12/19 8:12
     */
    @Override
    public boolean saveAuthMixForApp(String userId, String appId) {
        //删除以前的有限权限及有效api
        remove(new QueryWrapper<SysAuthMixDO>().eq("app_id", appId).eq("user_id", userId));
        sysApiMixService.remove(new QueryWrapper<SysApiMixDO>().eq("app_id", appId).eq("user_id", userId));
        //获取用户角色id集合
        List<String> roleIds = sysUserRoleService.getEffectiveRoleData(userId, appId).stream()
                .map(AbstractBaseDomain::getId).collect(Collectors.toList());
        //获取角色所有的上级角色集合
        List<String> allRoleIds = sysRoleRelationService.getAllSuperRoleIds(roleIds);
        allRoleIds.addAll(roleIds);
        //激活角色权限集合
        List<SysAuthRoleVDO> allActiveAuthRoles = new ArrayList<>(16);
        allActiveAuthRoles.addAll(sysAuthRoleService.getActiveAuthRolesForAppId(allRoleIds, appId));
        //用户存放权限唯一对应及授予/禁用关系
        Map<String, Integer> roleAuthMap = new HashMap<>(16);
        allActiveAuthRoles.stream().forEach(authRole -> {
            String authId = authRole.getAuthId();
            Integer revoker = authRole.getRevoker();
            //如果存在相同的权限，禁用权限优先级高于授予权限
            if (roleAuthMap.containsKey(authId)) {
                int existRoveke = roleAuthMap.get(authId).intValue();
                if (revoker > existRoveke) {
                    roleAuthMap.put(authId, revoker);
                }
            } else {
                roleAuthMap.put(authId, revoker);
            }
        });
        //当前用户所有的激活授权记录
        List<SysAuthUserVDO> activeAuthUser = sysAuthUserService.getActiveAuthUserForApp(userId, appId);
        activeAuthUser.stream().forEach(authUser -> {
            String authId = authUser.getAuthId();
            Integer revoker = authUser.getRevoker();
            //如果存在相同的权限，用户授予的权限优先级高于角色授予的权限
            roleAuthMap.put(authId, revoker);
        });
        List<String> authIds = new ArrayList<>();
        //迭代map保存用户有效权限
        roleAuthMap.entrySet().stream().forEach(es -> {
            String authId = es.getKey();
            Integer revoker = es.getValue();
            if (revoker == 0) {
                authIds.add(authId);
            }
        });
        saveAuthMixs(userId, authIds, appId);
        setUserMixApis(userId, authIds, appId);
        return true;
    }

    /**
     * 计算管理员用户有效权限
     * @param userId 授权用户id
     * @author shanwj
     * @date 2019/4/19 15:19
     */
//    或者 计算普通用户在所有应用下的有效权限
    @Override
    public boolean saveAuthMix(String userId) {
        //删除以前的有限权限及有效api
        remove(new QueryWrapper<SysAuthMixDO>().eq("user_id", userId));
        sysApiMixService.remove(new QueryWrapper<SysApiMixDO>().eq("user_id", userId));
        //获取用户角色id集合
        List<String> roleIds = sysUserRoleService.getEffectiveRoleData(userId, null).stream()
                .map(AbstractBaseDomain::getId).collect(Collectors.toList());
        //获取角色所有的上级角色集合
        List<String> allRoleIds = sysRoleRelationService.getAllSuperRoleIds(roleIds);
        allRoleIds.addAll(roleIds);
        //激活角色权限集合
        List<SysAuthRoleVDO> allActiveAuthRoles = new ArrayList<>(16);
//        allRoleIds.stream().forEach(roleId -> {
//            allActiveAuthRoles.addAll(sysAuthRoleService.getActiveAuthRole(roleId));
//        });
        allActiveAuthRoles.addAll(sysAuthRoleService.getActiveAuthRoles(allRoleIds));
        //用户存放权限唯一对应及授予/禁用关系
        Map<String, Integer> roleAuthMap = new HashMap<>(16);
        allActiveAuthRoles.stream().forEach(authRole -> {
            String authId = authRole.getAuthId();
            Integer revoker = authRole.getRevoker();
            //如果存在相同的权限，禁用权限优先级高于授予权限
            if (roleAuthMap.containsKey(authId)) {
                int existRoveke = roleAuthMap.get(authId).intValue();
                if (revoker > existRoveke) {
                    roleAuthMap.put(authId, revoker);
                }
            } else {
                roleAuthMap.put(authId, revoker);
            }
        });


        //当前用户所有的激活授权记录
        List<SysAuthUserVDO> activeAuthUser = sysAuthUserService.getActiveAuthUser(userId);
        activeAuthUser.stream().forEach(authUser -> {
            String authId = authUser.getAuthId();
            Integer revoker = authUser.getRevoker();
            //如果存在相同的权限，用户授予的权限优先级高于角色授予的权限
            roleAuthMap.put(authId, revoker);
        });
        List<String> authIds = new ArrayList<>();
        //迭代map保存用户有效权限
        roleAuthMap.entrySet().stream().forEach(es -> {
            String authId = es.getKey();
            Integer revoker = es.getValue();
            if (revoker == 0) {
                authIds.add(authId);
            }
        });

        saveAuthMixs(userId, authIds, "platform");
        setUserMixApis(userId, authIds, "platform");

        return true;
    }

    /**
     * 设置用户有效api
     *
     * @param userId  用户id
     * @param authIds 权限id
     * @param appId   应用ID
     * @author shanwj
     * @date 2019/7/4 20:19
     */
    private void setUserMixApis(String userId, List<String> authIds, String appId) {
        //设置用户有效api资源
        List<List<String>> list = JDBCUtil.getListGroupBy(authIds);
        List<SysAuthApiDO> authApis = new ArrayList<>(16);
        list.stream().forEach(ids -> {
            authApis.addAll(sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                    .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                    .in("auth_id", ids)));
        });
        if (authApis.size() > 0) {
            List<SysApiMixDO> apiMixs = new ArrayList<>();
            authApis.forEach(authApi -> {
                SysApiResourceDO apiResourceDO = sysApiResourceService.getById(authApi.getApiId());
                if (apiResourceDO != null) {
                    SysApiMixDO sysApiMixDO = new SysApiMixDO();
                    sysApiMixDO.setApiId(authApi.getApiId());
                    sysApiMixDO.setUserId(userId);
                    sysApiMixDO.setApiMethod(apiResourceDO.getApiMethod());
                    sysApiMixDO.setApiUrl(apiResourceDO.getApiUrl());
                    sysApiMixDO.setAuthId(authApi.getAuthId());
                    sysApiMixDO.setAppId(appId);
                    String id = UuidUtils.createUUID();
                    sysApiMixDO.setId(id);
//                    try {
//                        sysApiMixDO.setSign(GMBaseUtil.getSign(userId + id + authApi.getApiId()));
//                    } catch (Exception e) {
//                        throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//                    }
                    apiMixs.add(sysApiMixDO);
                }
            });
            if (CollectionUtils.isNotEmpty(apiMixs)) {
                sysApiMixService.saveBatch(apiMixs);
            }
        }
    }

    /**
     * 设置用户有效api
     *
     * @param userId 用户id
     * @param authId 权限id
     * @author shanwj
     * @date 2019/7/4 20:19
     */
//    private void setUserMixApi(String userId, String authId) {
//        //设置用户有效api资源
//        List<SysAuthApiDO> authApis =
//                sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>().eq("auth_id", authId));
//        if (authApis.size() > 0) {
//            List<SysApiMixDO> apiMixs = new ArrayList<>();
//            authApis.forEach(authApi -> {
//                SysApiResourceDO apiResourceDO = sysApiResourceService.getById(authApi.getApiId());
//                SysApiMixDO sysApiMixDO = new SysApiMixDO();
//                sysApiMixDO.setApiId(authApi.getApiId());
//                sysApiMixDO.setUserId(userId);
//                sysApiMixDO.setApiMethod(apiResourceDO.getApiMethod());
//                sysApiMixDO.setApiUrl(apiResourceDO.getApiUrl());
//                sysApiMixDO.setAuthId(authApi.getAuthId());
//                sysApiMixDO.setAppId(sysAuthService.getById(authApi.getAuthId()).getAppId());
//                String id = UuidUtils.createUUID();
//                sysApiMixDO.setId(id);
//                try {
//                    sysApiMixDO.setSign(GMBaseUtil.getSign(userId + id + authApi.getApiId()));
//                } catch (Exception e) {
//                    throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//                }
//                apiMixs.add(sysApiMixDO);
//            });
//            sysApiMixService.saveBatch(apiMixs);
//        }
//    }


    /**
     * 保存用户有效权限
     *
     * @param userId  用户id
     * @param authIds 权限id
     * @param appId   应用ID
     */
    private void saveAuthMixs(String userId, List<String> authIds, String appId) {
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(authIds)) {
            List<SysAuthMixDO> sysAuthMixDOS = new ArrayList<>(16);
            if (CollectionUtils.isNotEmpty(authIds)) {
                authIds.stream().forEach(authId -> {
                    SysAuthMixDO sysAuthMixDO = new SysAuthMixDO();
//                    String id = UuidUtils.createUUID();
//                    sysAuthMixDO.setId(id);
                    sysAuthMixDO.setUserId(userId);
                    sysAuthMixDO.setAuthId(authId);
                    sysAuthMixDO.setAppId(appId);
//                    try {
//                        sysAuthMixDO.setSign(GMBaseUtil.getSign(userId + id + authId));
//                    } catch (Exception e) {
//                        throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//                    }
                    sysAuthMixDOS.add(sysAuthMixDO);
                });
            }
            if (CollectionUtils.isNotEmpty(sysAuthMixDOS)) {
                if (!saveBatch(sysAuthMixDOS)) {
                    throw new RException("GRANT_PRIVILEGE");
                }
            }
        }
    }

    /**
     * 保存用户有效权限
     *
     * @param userId 用户id
     * @param authId 权限id
     */
//    private void saveAuthMix(String userId, String authId) {
//        SysAuthMixDO sysAuthMixDO = new SysAuthMixDO();
//        String id = UuidUtils.createUUID();
//        sysAuthMixDO.setId(id);
//        sysAuthMixDO.setUserId(userId);
//        sysAuthMixDO.setAuthId(authId);
//        sysAuthMixDO.setAppId(sysAuthService.getById(authId).getAppId());
//        try {
//            sysAuthMixDO.setSign(GMBaseUtil.getSign(userId + id + authId));
//        } catch (Exception e) {
//            throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//        }
//        if (!save(sysAuthMixDO)) {
//            throw new RException("GRANT_PRIVILEGE");
//        }
//    }
}
