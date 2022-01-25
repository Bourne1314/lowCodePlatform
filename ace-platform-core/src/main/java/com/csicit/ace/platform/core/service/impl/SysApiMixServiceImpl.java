package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysApiMixDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysApiMixMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 有效API权限 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:05:05
 */
@Service
public class SysApiMixServiceImpl extends BaseServiceImpl<SysApiMixMapper, SysApiMixDO> implements SysApiMixService {

    @Autowired
    SysAuthApiService sysAuthApiService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysApiResourceService sysApiResourceService;

    @Override
    public boolean synchronizeAuthApi(String authId) {
        if (StringUtils.isNotBlank(authId)) {
            String appId = sysAuthService.getById(authId).getAppId();
            // 获取有此权限的人
            List<String> userIds = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().eq("auth_id",
                    authId)).stream().distinct().map(SysAuthMixDO::getUserId).collect(Collectors.toList());
            // 重新计算这些用户的有效权限
            if (userIds != null && userIds.size() > 0) {
                userIds.stream().forEach(userId -> {
                    sysAuthMixService.saveAuthMixForApp(userId, appId);
                });
            }

//            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(authMixDOS)) {
//                List<SysAuthApiDO> authApiDOS = sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>().eq("auth_id",
//                        authId).select("api_id"));
//                // 权限是否关联API
//
//                // 需要 先清除 所有的 apiMix
//                if (remove(new QueryWrapper<SysApiMixDO>().in("user_id", authMixDOS.stream().map
//                        (SysAuthMixDO::getUserId).collect(Collectors.toList())))) {
//                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(authApiDOS)) {
//                        // 权限API 用户 多对多
//                        List<SysApiResourceDO> apiResourceDOS = sysApiResourceService.list(new
//                                QueryWrapper<SysApiResourceDO>().in("id", authApiDOS.stream().map
//                                (SysAuthApiDO::getApiId).collect(Collectors.toList())));
//
//                        List<SysApiMixDO> apiMixs = new ArrayList<>();
//                        for (int i = 0; i < authMixDOS.size(); i++) {
//                            String userId = authMixDOS.get(i).getUserId();
//                            apiResourceDOS.forEach(apiResource -> {
//                                SysApiMixDO sysApiMixDO = new SysApiMixDO();
//                                sysApiMixDO.setApiId(apiResource.getId());
//                                sysApiMixDO.setUserId(userId);
//                                sysApiMixDO.setApiMethod(apiResource.getApiMethod());
//                                sysApiMixDO.setApiUrl(apiResource.getApiUrl());
//                                String id = UuidUtils.createUUID();
//                                sysApiMixDO.setId(id);
//                                try {
//                                    sysApiMixDO.setSign(GMBaseUtil.getSign(userId + id + apiResource.getId()));
//                                } catch (Exception e) {
//                                    throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//                                }
//                                apiMixs.add(sysApiMixDO);
//                            });
//                        }
//                        if (!saveBatch(apiMixs)) {
//                            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                        }
//                    }
//                    return true;
//                } else {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
//            } else {
//                return true;
//            }
        }
        return true;
//        throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
    }
}
