package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysConfigMapper;
import com.csicit.ace.data.persistent.service.SysConfigServiceD;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgGroupService;
import com.csicit.ace.platform.core.service.SysConfigService;
import com.csicit.ace.platform.core.service.SysGroupAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统配置项 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Service("sysConfigService")
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfigDO> implements SysConfigService {

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    OrgGroupService orgGroupService;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    SysConfigServiceD sysConfigServiceD;

    @Override
    public void initConfig() {
        sysConfigServiceD.initConfig();
    }

    @Override
    public boolean initSaveOrUpdateConfigByKey(String key, String value, String remark) {
        SysConfigDO config = getOne(new QueryWrapper<SysConfigDO>().eq("name", key));
        if (config == null) {
            // 保存
            int count = count(null);
            config = new SysConfigDO();
            config.setName(key);
            config.setScope(1);
            config.setValue(value);
            config.setSortIndex(count + 1);
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(config.getCreateTime());
            config.setCreateUser("init");
            config.setRemark(remark);
            config.setAppId("platform");
            return save(config);
        } else {
            // 更新
            config.setValue(value);
            config.setRemark(remark);
            config.setUpdateTime(LocalDateTime.now());
            return updateById(config);
        }
    }

    @Override
    public String getValueByApp(String appId, String key) {
        String value = (String) cacheUtil.hget(appId, key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        SysConfigDO config = getOne(new QueryWrapper<SysConfigDO>().eq("name", key).eq("scope", 3).eq
                ("app_id", appId));
        if (config != null) {
            cacheUtil.hset(appId, config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
            return config.getValue();
        }
        SysGroupAppDO appDO = sysGroupAppService.getById(appId);
        return getValueByGroup(appDO.getGroupId(), key);
    }

    @Override
    public String getValueByGroup(String groupId, String key) {
        String value = (String) cacheUtil.hget(groupId, key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        SysConfigDO config = getConfigByGroupId(key, groupId);
        if (config != null) {
            cacheUtil.hset(groupId, config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
            return config.getValue();
        }
        return getValue(key);
    }

    /**
     * 租户级
     *
     * @param key
     * @return
     */
    @Override
    public String getValue(String key) {
        if (StringUtils.isNotBlank(key)) {
            //租户级
            String value = cacheUtil.get(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
            SysConfigDO config = getOne(new QueryWrapper<SysConfigDO>().eq("name", key).eq("scope", 1));
            if (config != null) {
                cacheUtil.set(key, config.getValue());
                return config.getValue();
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
            SysConfigDO config = getOne(new QueryWrapper<SysConfigDO>().eq("name", key).eq("scope", 2).eq("group_id",
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


    /**
     * 验证 config name 唯一性 各层级
     * 验证 排序号 sortIndex
     *
     * @param config
     * @return
     * @author yansiyang
     * @date 2019/8/22 15:49
     */
    private void validateConfigName(SysConfigDO config, boolean save) {
        int scope = config.getScope();
        String name = config.getName();
        int sortIndex = config.getSortIndex();
        boolean success = true;
        // 租户级
        if (scope == 1) {
            if (save) {
                int count = count(new QueryWrapper<SysConfigDO>().and(i -> i.eq("name", name).or()
                        .eq("sort_index", sortIndex))
                        .eq("scope", 1));
                success = count == 0;
            } else {
                int count = count(new QueryWrapper<SysConfigDO>().and(i -> i.eq("name", name).or()
                        .eq("sort_index", sortIndex)).ne("id", config.getId()).eq("scope", 1));
                success = count == 0;
            }
        } else if (scope == 2) {
            // 集团级
            if (StringUtils.isBlank(config.getGroupId())) {
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
            }
            if (save) {
                int count = count(new QueryWrapper<SysConfigDO>().and(i -> i.eq("name", name).or()
                        .eq("sort_index", sortIndex)).eq("scope", 2).eq("group_id",
                        config.getGroupId()));
                success = count == 0;
            } else {
                int count = count(new QueryWrapper<SysConfigDO>().and(i -> i.eq("name", name).or()
                        .eq("sort_index", sortIndex)).ne("id", config.getId()).eq("scope", 2).eq("group_id", config
                        .getGroupId()));
                success = count == 0;
            }
        } else {
            // 应用级
            if (StringUtils.isBlank(config.getGroupId()) || StringUtils.isBlank(config.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
            }
            if (save) {
                int count = count(new QueryWrapper<SysConfigDO>().and(i -> i.eq("name", name).or()
                        .eq("sort_index", sortIndex)).eq("app_id", config.getAppId()).eq
                        ("group_id",
                                config.getGroupId()));
                success = count == 0;
            } else {
                int count = count(new QueryWrapper<SysConfigDO>().and(i -> i.eq("name", name).or()
                        .eq("sort_index", sortIndex)).ne("id", config.getId()).eq
                        ("app_id", config.getAppId()).eq("group_id", config.getGroupId()));
                success = count == 0;
            }
        }
        if (!success) {
            throw new RException(InternationUtils.getInternationalMsg("SAME_CONFIG_NAME"));
        }
    }


    @Override
    public R saveConfig(SysConfigDO config) {
        int count = 0;

        if (Objects.equals(1, config.getScope())) {
            // 租户级
            count = count(new QueryWrapper<SysConfigDO>().eq("name", config.getName()));
        } else if (Objects.equals(2, config.getScope())) {
            // 集团级
            count = count(new QueryWrapper<SysConfigDO>().eq("group_id", config.getGroupId())
                    .eq("name", config.getName()));
        } else if (Objects.equals(3, config.getScope())) {
            // 应用级
            count = count(new QueryWrapper<SysConfigDO>().eq("app_id", config.getAppId())
                    .eq("name", config.getName()));
        }
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("CONFIG_NAME_EXIST"));
        }
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(config.getCreateTime());
        config.setCreateUser(securityUtils.getCurrentUserId());
        validateConfigName(config, true);
        if (save(config)) {
            updateRedisConfig(config);
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存配置项", config.getName()
                    , config.getGroupId(),
                    config.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * redis缓存更新配置项
     *
     * @param config
     * @return
     * @author FourLeaves
     * @date 2019/12/25 9:24
     */
    public void updateRedisConfig(SysConfigDO config) {
        if (config.getUpdateType() == 0) {
            if (config.getScope() == 1) {
                cacheUtil.set(config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
            } else if (config.getScope() == 2) {
                cacheUtil.hset(config.getGroupId(), config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
            } else if (config.getScope() == 3) {
                cacheUtil.hset(config.getAppId(), config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
            }
        }
    }

    @Override
    public R updateConfig(SysConfigDO config) {
        config.setUpdateTime(LocalDateTime.now());
        SysConfigDO oldConfig = getById(config.getId());
        if (Objects.equals(oldConfig.getName(), config.getName())) {
            validateConfigName(config, false);
        }

        if (updateById(config)) {
            // 判断更新策略
            updateRedisConfig(config);
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改配置项", config, config
                            .getGroupId(),
                    config.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R delete(List<String> configIds) {
        List<SysConfigDO> list = list(new QueryWrapper<SysConfigDO>()
                .and(configIds == null || configIds.size() == 0, i -> i.eq("1", "2")).in("id", configIds));
        if (removeByIds(configIds)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除配置项", list
                    .parallelStream().map
                            (SysConfigDO::getName)
                    .collect(Collectors.toList()), list.get(0).getGroupId(), list.get(0).getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        } else {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
    }
}
