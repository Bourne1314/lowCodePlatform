package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthMapper;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.orgauth.core.service.SysApiMixService;
import com.csicit.ace.orgauth.core.service.SysAuthApiService;
import com.csicit.ace.orgauth.core.service.SysAuthRoleVService;
import com.csicit.ace.orgauth.core.service.SysAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/9/23 11:52
 */
@Service("sysAuthServiceO")
public class SysAuthServiceImpl extends ServiceImpl<SysAuthMapper, SysAuthDO> implements SysAuthService {

    @Resource(name = "sysAuthMixServiceO")
    SysAuthMixService sysAuthMixService;

    @Resource(name = "sysAuthRoleVServiceO")
    SysAuthRoleVService sysAuthRoleVService;

    @Resource(name = "sysApiMixServiceO")
    SysApiMixService sysApiMixService;

    @Resource(name = "sysAuthApiServiceO")
    SysAuthApiService sysAuthApiService;

    @Override
    public boolean hasAuthCodeInCurrentApp(String userId, String authCode, String appId) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(authCode) && StringUtils.isNotBlank(appId)) {
            List<SysAuthDO> authDOS = list(new QueryWrapper<SysAuthDO>().eq("app_id", appId).eq
                    ("code", authCode));
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(authDOS)) {
                List<String> authIds = authDOS.stream().map(SysAuthDO::getId).collect(Collectors.toList());
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(authIds)) {
                    int count = sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>().eq("user_id", userId).in
                            ("auth_id", authIds));
                    return count > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasAuthorityWithUserId(String userId, String authId) {
        int count = sysAuthMixService.count(new QueryWrapper<SysAuthMixDO>().eq("user_id", userId).eq("auth_id",
                authId));
        return count == 1;
    }

    @Override
    public boolean hasAuthorityWithRoleId(String roleId, String authId) {
        int count = sysAuthRoleVService.count(new QueryWrapper<SysAuthRoleVDO>().eq("role_id", roleId).eq("auth_id",
                authId));
        return count == 1;
    }

    @Override
    public List<String> getAllAuthIdsByUserId(String userId) {
        List<SysAuthMixDO> authMixDOS = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().select("auth_id").eq
                ("user_id", userId));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(authMixDOS)) {
            return authMixDOS.stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllAuthIdsByUserIdAndApp(String userId, String appId) {
        //        List<SysAuthDO> authDOS = sysAuthService.list(new QueryWrapper<SysAuthDO>().select("id").eq
        // ("app_id", appId));
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(authDOS)) {
//            List<String> authIds = authDOS.stream().map(SysAuthDO::getId).collect(Collectors.toList());
        List<SysAuthMixDO> authMixDOS = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().select("auth_id").eq
                ("user_id", userId).eq("app_id", appId));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(authMixDOS)) {
            return authMixDOS.stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toList());
        }
//        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllAuthCodesByUserIdAndApp(String userId, String appId) {
        List<String> authMixIdS = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().select("auth_id").eq
                ("user_id", userId).eq("app_id", appId))
                .stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toList());
        if (authMixIdS != null && authMixIdS.size() > 0) {
            List<SysAuthDO> auths = list(new QueryWrapper<SysAuthDO>().select("id", "code").isNotNull("code").in("id", authMixIdS));
            return auths.stream().map(SysAuthDO::getCode).collect(Collectors
                    .toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAuthIdsByRoleId(String roleId) {
        List<SysAuthRoleVDO> authRoleVDOS = sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>().select
                ("auth_id").eq
                ("role_id", roleId));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(authRoleVDOS)) {
            return authRoleVDOS.stream().map(SysAuthRoleVDO::getAuthId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean hasApiByUserId(String userId, String api) {
        int count = sysApiMixService.count(new QueryWrapper<SysApiMixDO>().eq("user_id", userId).eq("api_id", api));
        return count == 1;
    }

    @Override
    public List<String> getAllApisByUserId(String userId) {
        List<SysApiMixDO> sysApiMixDOS = sysApiMixService.list(new QueryWrapper<SysApiMixDO>().select("id").eq
                ("user_id", userId));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(sysApiMixDOS)) {
            return sysApiMixDOS.stream().map(SysApiMixDO::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllApisByUserIdAndApp(String userId, String appId) {
        //        List<SysApiResourceDO> apiResourceDOS = sysApiResourceService.list(new
        // QueryWrapper<SysApiResourceDO>()
//                .select("id").eq
//                        ("app_id", appId));
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(apiResourceDOS)) {
//            List<String> apiIds = apiResourceDOS.stream().map(SysApiResourceDO::getId).collect(Collectors.toList());
        List<SysApiMixDO> apiMixDOS = sysApiMixService.list(new QueryWrapper<SysApiMixDO>().select("api_id")
                .eq("app_id", appId).eq("user_id", userId));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(apiMixDOS)) {
            return apiMixDOS.stream().map(SysApiMixDO::getApiId).collect(Collectors.toList());
        }
//        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllApisByRoleId(String roleId) {
        List<SysAuthRoleVDO> authRoleVDOS = sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>().select
                ("auth_id").eq
                ("role_id", roleId));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(authRoleVDOS)) {
            List<String> authIds = authRoleVDOS.stream().map(SysAuthRoleVDO::getAuthId).collect(Collectors.toList());
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(authIds)) {
                List<SysAuthApiDO> authApiDOS = sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>().in("auth_id",
                        authIds).select("api_id"));
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(authApiDOS)) {
                    return authApiDOS.stream().map(SysAuthApiDO::getApiId).collect(Collectors.toList());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysAuthDO> getMixAuth(String appId, String userId) {
        List<SysAuthDO> sysAuthDOS = new ArrayList<>(16);
        List<String> authIds = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("app_id", appId).eq("user_id", userId)).stream().map(SysAuthMixDO::getAuthId)
                .collect(Collectors.toList());
        authIds.stream().forEach(authId -> {
            sysAuthDOS.add(getById(authId));
        });
        return sysAuthDOS;
    }
}
