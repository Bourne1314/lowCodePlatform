package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.SysConfigMapper;
import com.csicit.ace.data.persistent.service.SysConfigServiceD;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.OrgGroupService;
import com.csicit.ace.orgauth.core.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 系统配置项 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Service("sysConfigServiceO")
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfigDO> implements SysConfigService {

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    SysConfigServiceD sysConfigServiceD;
    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;

    @Resource(name = "orgGroupServiceO")
    OrgGroupService orgGroupService;

    @Override
    public void saveScanConfig(List<SysConfigDO> configs) {
        sysConfigServiceD.saveScanConfig(configs);
    }

    @Override
    public String getValue(String name) {
        String groupId = null;
        try {
            groupId = securityUtils.getCurrentGroupId();
        } catch (Exception e) {

        }
        SysConfigDO instance = null;
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isNotBlank(groupId)) {
                instance = getConfigByGroupId(name, groupId);
            }
            if (instance == null) {
                instance = getOne(new QueryWrapper<SysConfigDO>().eq("name", name).eq("scope", 1));
            }
            if (instance == null) {
                return null;
            }

            return instance.getValue();
        }
        return null;
    }

    @Override
    public String getValueByApp(String appId, String name) {
        if (StringUtils.isNotBlank(name)) {
            SysConfigDO config = null;
            if (StringUtils.isNotBlank(appId)) {
                //应用级
                String value = (String) cacheUtil.hget(appId, name);
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
                config = getOne(new QueryWrapper<SysConfigDO>().eq("name", name).eq("scope", 3).eq
                        ("app_id", appId));
                if (config != null) {
                    cacheUtil.hset(appId, config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
                    return config.getValue();
                }
            }
            if (config == null) {
                SysGroupAppDO sysGroupApp = sysGroupAppServiceD.getById(appId);
                if (sysGroupApp != null) {
                    //集团级
                    String value = (String) cacheUtil.hget(sysGroupApp.getGroupId(), name);
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                    config = getConfigByGroupId(name, sysGroupApp.getGroupId());
                    if (config != null) {
                        cacheUtil.hset(sysGroupApp.getGroupId(), config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
                        return config.getValue();
                    }
                }
            }
            if (config == null) {
                //租户级
                String value = cacheUtil.get(name);
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
                config = getOne(new QueryWrapper<SysConfigDO>().eq("name", name).eq("scope", 1));
                if (config != null) {
                    cacheUtil.set(name, config.getValue());
                    return config.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 递归获取 配置项
     *
     * @param key
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/11/6 17:51
     */
    private SysConfigDO getConfigByGroupId(String key, String groupId) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(groupId)) {
            SysConfigDO config = getOne(new QueryWrapper<SysConfigDO>().eq("name", key).eq("scope", 2)
                    .eq("group_id",
                            groupId));
            if (config != null) {
                return config;
            } else {
                OrgGroupDO group = orgGroupService.getById(groupId);
                if (group != null && StringUtils.isNotBlank(group.getParentId()) && !Objects.equals(group.getParentId(),
                        "0")) {
                    return getConfigByGroupId(key, group.getParentId());
                }
                return null;
            }
        }
        return null;
    }
}
