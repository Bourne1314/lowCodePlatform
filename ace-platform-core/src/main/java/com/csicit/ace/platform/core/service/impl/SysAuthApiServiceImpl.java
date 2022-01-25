package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysAuthApiDO;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthApiMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysApiMixService;
import com.csicit.ace.platform.core.service.SysAuthApiService;
import com.csicit.ace.platform.core.service.SysAuthService;
import com.csicit.ace.platform.core.service.SysGroupAppService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限api关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthApiService")
public class SysAuthApiServiceImpl extends BaseServiceImpl<SysAuthApiMapper, SysAuthApiDO> implements
        SysAuthApiService {

    @Autowired
    SysApiMixService sysApiMixService;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    SysAuthService sysAuthService;

    @Override
    public boolean initApis() {
        List<SysGroupAppDO> apps = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().select("id"));
        apps.stream().forEach(app -> {
            String appId = app.getId();
            cacheUtil.delete(appId + "-apis");
            List<SysAuthDO> auths = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                    .eq("app_id", appId).select("id"));
            if (CollectionUtils.isNotEmpty(auths)) {
                List<SysAuthApiDO> authApiDOS = list(new QueryWrapper<SysAuthApiDO>().in("auth_id",
                        auths.stream().map(SysAuthDO::getId).collect(Collectors.toList()))
                        .select("api_id", "auth_id"));
                Map<String, Object> apisAndAuthids = new HashMap<>();
                authApiDOS.stream().forEach(authApiDO -> {
                    apisAndAuthids.put(authApiDO.getApiId(), authApiDO.getAuthId());
                });
                cacheUtil.hmset(appId + "-apis", apisAndAuthids, CacheUtil.NOT_EXPIRE);
            }
        });
        return false;
    }

    /**
     * 保存auth-api关系表
     *
     * @param authId 权限id
     * @param apis   api资源集合
     * @author shanwj
     * @date 2019/4/18 16:58
     */
    @Override
    public boolean saveAuthApi(String authId, List<SysApiResourceDO> apis) {
//        if (!remove(new QueryWrapper<SysAuthApiDO>().eq("auth_id", authId))) {
//            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//        }
        SysAuthDO authDO = sysAuthService.getById(authId);
        List<SysAuthApiDO> authApiDOS = list(new QueryWrapper<SysAuthApiDO>().eq("auth_id", authId)
                .select("api_id"));
        authApiDOS.stream().forEach(authApiDO -> {
            cacheUtil.hDelete(authDO.getAppId() + "-apis", authApiDO.getApiId());
        });
        remove(new QueryWrapper<SysAuthApiDO>().eq("auth_id", authId));
        if (apis.size() > 0) {
            apis.forEach(api -> {
                SysAuthApiDO sysAuthApiDO = new SysAuthApiDO();
                String id = UuidUtils.createUUID();
                sysAuthApiDO.setAuthId(authId);
                sysAuthApiDO.setApiId(api.getId());
                sysAuthApiDO.setId(id);
//                try {
//                    sysAuthApiDO.setSign(GMBaseUtil.getSign(authId + id + api));
//                } catch (Exception e) {
//                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//                }
                if (!save(sysAuthApiDO)) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            });
            Map<String, Object> apisAndAuthids = new HashMap<>();
            // 放入缓存
            apis.forEach(api -> {
                apisAndAuthids.put(api.getId(), authId);
            });
            cacheUtil.hmset(authDO.getAppId() + "-apis", apisAndAuthids, CacheUtil.NOT_EXPIRE);
        }
        return sysApiMixService.synchronizeAuthApi(authId);
    }
}
