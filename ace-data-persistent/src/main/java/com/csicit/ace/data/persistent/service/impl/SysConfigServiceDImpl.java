package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.AppUpgradeJaxb.SysConfigDetail;
import com.csicit.ace.common.annotation.AceConfigField;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysConfigMapper;
import com.csicit.ace.data.persistent.service.OrgGroupServiceD;
import com.csicit.ace.data.persistent.service.SysConfigServiceD;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 10:36
 */
@Service
public class SysConfigServiceDImpl extends ServiceImpl<SysConfigMapper, SysConfigDO> implements SysConfigServiceD {

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    OrgGroupServiceD orgGroupServiceD;
    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;

    @Override
    public void saveScanConfig(List<SysConfigDO> configs) {
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }
        SysConfigDO firstConfig = configs.get(0);
        String appId = firstConfig.getAppId();
        String groupIdTT = firstConfig.getGroupId();
        if (StringUtils.isEmpty(groupIdTT)) {
            SysGroupAppDO appDO = sysGroupAppServiceD.getById(appId);
            if (appDO != null) {
                groupIdTT = appDO.getGroupId();
            }
        }
        String groupId = groupIdTT;
        List<SysConfigDO> dbSysConfigDOS = list(new QueryWrapper<SysConfigDO>().eq("scope", 1)
                .or(StringUtils.isNotBlank(groupId), i -> i.eq("scope", 2).eq("group_id", groupId))
                .or(StringUtils.isNotBlank(appId), i -> i.eq("scope", 3).eq("app_id", appId)));
        Set<String> tenantKeys = new HashSet<>();
        Set<String> groupKeys = new HashSet<>();
        Set<String> appKeys = new HashSet<>();
        // ???????????????????????????
        for (SysConfigDO config : dbSysConfigDOS) {
            if (config.getScope() == 1) {
                tenantKeys.add(config.getName());
            } else if (config.getScope() == 2) {
                groupKeys.add(config.getName());
            } else if (config.getScope() == 3) {
                appKeys.add(config.getName());
            }
        }
        int appMaxSortIndex = 10;
        SysConfigDO appMaxConfig = getOne(new QueryWrapper<SysConfigDO>().eq("app_id", appId).isNotNull("sort_index").eq("scope", 3)
                .orderByDesc("sort_index"));
        if (appMaxConfig != null && appMaxConfig.getSortIndex() != null) {
            appMaxSortIndex = appMaxConfig.getSortIndex() + 10;
        }
        int groupMaxSortIndex = 10;
        SysConfigDO groupMaxConfig = getOne(new QueryWrapper<SysConfigDO>().eq("group_id", groupId).isNotNull("sort_index").eq("scope", 2)
                .orderByDesc("sort_index"));
        if (groupMaxConfig != null && groupMaxConfig.getSortIndex() != null) {
            groupMaxSortIndex = groupMaxConfig.getSortIndex() + 10;
        }
        int tenantMaxSortIndex = 10;
        SysConfigDO tenantMaxConfig = getOne(new QueryWrapper<SysConfigDO>().eq("scope", 1)
                .isNotNull("sort_index").orderByDesc("sort_index"));
        if (tenantMaxConfig != null && tenantMaxConfig.getSortIndex() != null) {
            tenantMaxSortIndex = tenantMaxConfig.getSortIndex() + 10;
        }

        List<SysConfigDO> list = new ArrayList<>();
        // ???????????????????????????
        // ??????????????????????????????
        for (SysConfigDO config : configs) {
            String name = config.getName();
            // ???????????????
            Set<Integer> scopes = config.getScopes();
            // ?????????
            if (scopes.contains(3) && !appKeys.contains(name)) {
                SysConfigDO configT = new SysConfigDO();
                configT.setUpdateType(config.getUpdateType());
                configT.setRemark(config.getRemark());
                configT.setValue(config.getValue());
                configT.setType(config.getType());
                configT.setScope(3);
                configT.setAppId(appId);
                configT.setGroupId(groupId);
                configT.setName(name);
                configT.setSortIndex(appMaxSortIndex);

                list.add(configT);
                appMaxSortIndex += 10;
            }
            // ?????????
            if (scopes.contains(2) && !groupKeys.contains(name)) {
                SysConfigDO configT = new SysConfigDO();
                configT.setUpdateType(config.getUpdateType());
                configT.setRemark(config.getRemark());
                configT.setValue(config.getValue());
                configT.setType(config.getType());
                configT.setGroupId(groupId);
                configT.setName(name);

                configT.setScope(2);
                configT.setAppId(null);
                configT.setSortIndex(groupMaxSortIndex);
                list.add(configT);
                groupMaxSortIndex += 10;
            }
            // ?????????
            if (scopes.contains(1) && !tenantKeys.contains(name)) {
                SysConfigDO configT = new SysConfigDO();
                configT.setUpdateType(config.getUpdateType());
                configT.setRemark(config.getRemark());
                configT.setValue(config.getValue());
                configT.setName(name);
                configT.setType(config.getType());
                configT.setScope(1);
                configT.setAppId(null);
                configT.setGroupId(null);
                configT.setSortIndex(tenantMaxSortIndex);
                list.add(configT);
                tenantMaxSortIndex += 10;
            }
        }
        if (!CollectionUtils.isEmpty(list) && saveBatch(list)) {
            // ?????????redis
            for (SysConfigDO config : list) {
                int scope = config.getScope();
                String key = config.getName();
                String value = config.getValue();
                if (scope == 1 && !cacheUtil.hasKey(key)) {
                    cacheUtil.set(key, value, CacheUtil.NOT_EXPIRE);
                } else if (scope == 2) {
                    // ?????????
                    String groupIdT = config.getGroupId();
                    if (StringUtils.isNotBlank(groupIdT) && !cacheUtil.hHasKey(groupIdT, key)) {
                        cacheUtil.hset(groupIdT, key, value, CacheUtil.NOT_EXPIRE);
                    }
                } else if (scope == 3) {
                    // ?????????
                    String appIdT = config.getAppId();
                    if (StringUtils.isNotBlank(appIdT) && !cacheUtil.hHasKey(appIdT, key)) {
                        cacheUtil.hset(appIdT, key, value, CacheUtil.NOT_EXPIRE);
                    }
                }
            }
        }
    }

    private SysConfigDO getAppConfig(String appId, String name) {
        return getOne(new QueryWrapper<SysConfigDO>().eq("name", name).eq("app_id", appId).eq("scope", 3));
    }

    private SysConfigDO getGroupConfig(String groupId, String name) {
        return getOne(new QueryWrapper<SysConfigDO>().eq("name", name).eq("group_id", groupId).eq("scope", 2));
    }

    private SysConfigDO getTenantConfig(String name) {
        return getOne(new QueryWrapper<SysConfigDO>().eq("name", name).eq("scope", 1));
    }

    /**
     * ???????????? ?????????
     *
     * @param key
     * @param groupId
     * @return com.csicit.ace.common.pojo.domain.SysConfigDO
     * @author JonnyJiang
     * @date 2020/7/14 10:16
     */

    private SysConfigDO getConfigByGroupId(String key, String groupId) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(groupId)) {
            SysConfigDO config = getGroupConfig(groupId, key);
            if (config != null) {
                return config;
            } else {
                OrgGroupDO group = orgGroupServiceD.getById(groupId);
                if (group != null && StringUtils.isNotBlank(group.getParentId()) && !Objects.equals(group.getParentId(),
                        "0")) {
                    return getConfigByGroupId(key, group.getParentId());
                }
            }
        }
        return null;
    }

    @Override
    public String getValue(String appId, String name) {
        if (StringUtils.isNotBlank(name)) {
            SysConfigDO config = null;
            if (StringUtils.isNotBlank(appId)) {
                //?????????
                String value = (String) cacheUtil.hget(appId, name);
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
                config = getAppConfig(appId, name);
                if (config != null) {
                    cacheUtil.hset(appId, config.getName(), config.getValue(), CacheUtil.NOT_EXPIRE);
                    return config.getValue();
                }
            }
            if (config == null) {
                SysGroupAppDO sysGroupApp = sysGroupAppServiceD.getById(appId);
                if (sysGroupApp != null) {
                    //?????????
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
                //?????????
                String value = cacheUtil.get(name);
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
                config = getTenantConfig(name);
                if (config != null) {
                    cacheUtil.set(name, config.getValue());
                    return config.getValue();
                }
            }
        }
        return null;
    }

    /**
     * ????????????????????????????????????
     *
     * @param sysConfigDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 15:54
     */
    @Override
    public boolean sysConfigUpdate(List<SysConfigDetail> sysConfigDetails, String appId) {
        List<SysConfigDO> add = new ArrayList<>(16);
        List<SysConfigDO> upd = new ArrayList<>(16);

        sysConfigDetails.stream().forEach(sysConfigDetail -> {
            SysConfigDO item = JsonUtils.castObject(sysConfigDetail, SysConfigDO.class);
            // ??????????????????????????????????????????????????????
            SysConfigDO sysConfigDO = getOne(new
                    QueryWrapper<SysConfigDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysConfigDO == null) {
                add.add(item);
            } else {
                // ????????????????????????????????????????????????????????????????????????
                if (CommonUtils.compareFields(sysConfigDO, item)) {
                    item.setId(sysConfigDO.getId());
                    upd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(add)) {
            if (!saveBatch(add)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }

    /**
     * ????????????
     */
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void initConfig() {
        // ??????????????? ?????? platform ??????????????????
        if (Constants.isZuulApp || Constants.isMonomerApp || Constants.PLATFORM.equals(appName)) {
            List<SysConfigDO> configDOS = list(null);
            // ?????????
            Map<String, Set<KeyValueVO>> groupConfigs = new HashMap<>();
            // ?????????
            Map<String, Set<KeyValueVO>> appConfigs = new HashMap<>();
            for (SysConfigDO configDO : configDOS) {
                // ???????????????????????????????????????
                int scope = configDO.getScope() == null ? 1 : configDO.getScope();
                String key = configDO.getName();
                String value = configDO.getValue();

                if (configDO.getUpdateType() == 1) {
                    // ?????????
                    if (scope == 1) {
                        cacheUtil.set(key, value, CacheUtil.NOT_EXPIRE);
                    } else if (scope == 2) {
                        // ?????????
                        String groupId = configDO.getGroupId();
                        if (groupConfigs.containsKey(groupId)) {
                            groupConfigs.get(groupId).add(new KeyValueVO(key, value));
                        } else {
                            Set<KeyValueVO> keyValueVOS = new HashSet<>();
                            keyValueVOS.add(new KeyValueVO(key, value));
                            groupConfigs.put(groupId, keyValueVOS);
                        }
                    } else if (scope == 3) {
                        // ?????????
                        String appId = configDO.getAppId();
                        if (appConfigs.containsKey(appId)) {
                            appConfigs.get(appId).add(new KeyValueVO(key, value));
                        } else {
                            Set<KeyValueVO> keyValueVOS = new HashSet<>();
                            keyValueVOS.add(new KeyValueVO(key, value));
                            appConfigs.put(appId, keyValueVOS);
                        }
                    }
                } else {
                    // ?????????
                    if (scope == 1 && !cacheUtil.hasKey(key)) {
                        cacheUtil.set(key, value, CacheUtil.NOT_EXPIRE);
                    } else if (scope == 2) {
                        // ?????????
                        String groupId = configDO.getGroupId();
                        if (!cacheUtil.hHasKey(groupId, key)) {
                            if (groupConfigs.containsKey(groupId)) {
                                groupConfigs.get(groupId).add(new KeyValueVO(key, value));
                            } else {
                                Set<KeyValueVO> keyValueVOS = new HashSet<>();
                                keyValueVOS.add(new KeyValueVO(key, value));
                                groupConfigs.put(groupId, keyValueVOS);
                            }
                        }
                    } else if (scope == 3) {
                        // ?????????
                        String appId = configDO.getAppId();
                        if (!cacheUtil.hHasKey(appId, key)) {
                            if (appConfigs.containsKey(appId)) {
                                appConfigs.get(appId).add(new KeyValueVO(key, value));
                            } else {
                                Set<KeyValueVO> keyValueVOS = new HashSet<>();
                                keyValueVOS.add(new KeyValueVO(key, value));
                                // ????????????????????????ID
                                keyValueVOS.add(new KeyValueVO("groupId", configDO.getGroupId()));
                                appConfigs.put(appId, keyValueVOS);
                            }
                        }
                    }
                }
            }
            for (String groupId : groupConfigs.keySet()) {
                Set<KeyValueVO> keyValueVOS = groupConfigs.get(groupId);
                for (KeyValueVO keyValue : keyValueVOS) {
                    cacheUtil.hset(groupId, keyValue.getKey(), keyValue.getValue(), CacheUtil.NOT_EXPIRE);
                }
            }
            for (String appId : appConfigs.keySet()) {
                Set<KeyValueVO> keyValueVOS = appConfigs.get(appId);
                for (KeyValueVO keyValue : keyValueVOS) {
                    cacheUtil.hset(appId, keyValue.getKey(), keyValue.getValue(), CacheUtil.NOT_EXPIRE);
                }
            }
        }
    }

    static String basePkgName = "com.csicit.ace";

    public static String getPkgName(String appName) {
        return basePkgName + "." + appName;
    }

    /**
     * ????????????????????????
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/26 11:50
     */
    @Override
    public void scanConfig(String appName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(getPkgName(appName)))
                .setScanners(new FieldAnnotationsScanner()));
        // ??????????????????AceConfigField???????????????
        Set<Field> fields = reflections.getFieldsAnnotatedWith(AceConfigField.class);
        List<SysConfigDO> configs = new ArrayList<>();

        Map<String, Set<Integer>> nameAndScopes = new HashMap<>();
        // ??????????????????
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                SysConfigDO configDO = new SysConfigDO();
                AceConfigField aceConfigField = field.getAnnotation(AceConfigField.class);
                if (StringUtils.isNotBlank(aceConfigField.name())) {
                    configDO.setName(aceConfigField.name());
                } else {
                    configDO.setName(field.getName());
                }
                // ??????
                // ????????????????????? ???????????????
                Set<Integer> scopeSet = new HashSet<>();
                for (int i : aceConfigField.scopes()) {
                    if (i == 2) {
                        scopeSet.add(i);
                        break;
                    }
                }
                // ??????????????????????????????
                scopeSet.add(1);
                configDO.setScopes(scopeSet);
                if (nameAndScopes.containsKey(configDO.getName())
                        && nameAndScopes.get(configDO.getName()).size() >= configDO.getScopes().size()) {
                    continue;
                }
                nameAndScopes.put(configDO.getName(), configDO.getScopes());
                configDO.setType(field.getType().getSimpleName());
                configDO.setValue(aceConfigField.defaultValue());
                configDO.setRemark(aceConfigField.remark());
                configDO.setUpdateType(aceConfigField.updateType());
                configs.add(configDO);
            }
        }

        // ??????????????????
        List<SysConfigDO> sysConfigDOS = list(new QueryWrapper<SysConfigDO>().eq("scope", 1)
                .or(i -> i.likeRight("name", appName + "-").eq("scope", 2)));
        Set<String> tenantKeys = new HashSet<>();
        Set<String> groupKeys = new HashSet<>();
        // ???????????????????????????
        for (SysConfigDO config : sysConfigDOS) {
            if (config.getScope() == 1) {
                tenantKeys.add(config.getName());
            } else if (config.getScope() == 2) {
                // groupId@@appName-key
                groupKeys.add(config.getGroupId() + "@@" + config.getName());
            }
        }
        // ?????????????????????
        List<SysConfigDO> list = new ArrayList<>();
        List<String> groupIds = orgGroupServiceD.list(new QueryWrapper<OrgGroupDO>().eq("is_delete", 0))
                .stream().map(OrgGroupDO::getId).collect(Collectors.toList());


        int tenantMaxSortIndex = 10;
        SysConfigDO tenantMaxConfig = getOne(new QueryWrapper<SysConfigDO>().eq("scope", 1)
                .isNotNull("sort_index").orderByDesc("sort_index"));
        if (tenantMaxConfig != null && tenantMaxConfig.getSortIndex() != null) {
            tenantMaxSortIndex = tenantMaxConfig.getSortIndex() + 10;
        }

        Map<String, Integer> groupIdAndSortIndex = new HashMap<>();

        // ???????????????????????????
        // ??????????????????????????????
        for (SysConfigDO config : configs) {
            String name = config.getName();
            // ???????????????
            Set<Integer> scopes = config.getScopes();
            // ?????????
            if (scopes.contains(2)) {
                for (String groupId : groupIds) {
                    if (groupKeys.size() == 0 || !groupKeys.contains(groupId + "@@" + appName + "-" + name)) {
                        int groupMaxSortIndex = 10;
                        if (groupIdAndSortIndex.containsKey(groupId)) {
                            groupMaxSortIndex = groupIdAndSortIndex.get(groupId);
                        } else {
                            SysConfigDO groupMaxConfig = getOne(new QueryWrapper<SysConfigDO>().eq("group_id", groupId).eq("scope", 2)
                                    .isNotNull("sort_index").orderByDesc("sort_index"));
                            if (groupMaxConfig != null && groupMaxConfig.getSortIndex() != null) {
                                groupMaxSortIndex = groupMaxConfig.getSortIndex() + 10;
                            }
                        }
                        groupIdAndSortIndex.put(groupId, groupMaxSortIndex + 10);
                        SysConfigDO configT = new SysConfigDO();
                        configT.setUpdateType(config.getUpdateType());
                        configT.setRemark(config.getRemark());
                        configT.setType(config.getType());
                        configT.setValue(config.getValue());
                        configT.setSortIndex(groupMaxSortIndex);

                        configT.setScope(2);
                        configT.setGroupId(groupId);
                        configT.setName(appName + "-" + name);
                        list.add(configT);
                    }
                }
            }
            // ?????????
            if (!tenantKeys.contains(name)) {
                SysConfigDO configT = new SysConfigDO();
                configT.setUpdateType(config.getUpdateType());
                configT.setRemark(config.getRemark());
                configT.setValue(config.getValue());
                configT.setType(config.getType());

                configT.setName(name);
                configT.setGroupId(null);
                configT.setScope(1);
                configT.setSortIndex(tenantMaxSortIndex);
                list.add(configT);
                tenantMaxSortIndex += 10;
            }
        }
        if (!CollectionUtils.isEmpty(list) && saveBatch(list)) {
            // ?????????redis
            for (SysConfigDO config : list) {
                int scope = config.getScope();
                String key = config.getName();
                String value = config.getValue();
                if (scope == 1 && !cacheUtil.hasKey(key)) {
                    cacheUtil.set(key, value, CacheUtil.NOT_EXPIRE);
                } else if (scope == 2) {
                    // ?????????
                    String groupIdT = config.getGroupId();
                    if (StringUtils.isNotBlank(groupIdT) && !cacheUtil.hHasKey(groupIdT, key)) {
                        cacheUtil.hset(groupIdT, key, value, CacheUtil.NOT_EXPIRE);
                    }

                }
            }
        }

    }
}
