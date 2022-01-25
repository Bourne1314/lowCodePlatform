package com.csicit.ace.zuul.service;

import com.csicit.ace.common.annotation.AceConfigField;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IConfig;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/7 17:01
 */
@Service("config")
public class ConfigImpl extends BaseImpl implements IConfig {

    @Override
    public void scanConfig() {
        SysGroupAppDO app = clientService.getAppById(securityUtils.getAppName());
        if (app != null) {
            // 不允许平台服务利用此接口
            if (StringUtils.isNotBlank(Constants.BasePackages) && !Constants.AppNames.contains(securityUtils.getAppName())) {
                Reflections reflections = new Reflections(new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(Constants.BasePackages))
                        .setScanners(new FieldAnnotationsScanner()));
                // 获取所有的注解
                Set<Field> fields = reflections.getFieldsAnnotatedWith(AceConfigField.class);
                Set<SysConfigDO> configDOS = new HashSet<>();
                Map<String, Set<Integer>> nameAndScopes = new HashMap<>();
                // 扫描生成对象
                for (Field field : fields) {
                    // 公共静态常量
                    if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                        SysConfigDO configDO = new SysConfigDO();
                        AceConfigField aceConfigField = field.getAnnotation(AceConfigField.class);
                        if (StringUtils.isNotBlank(aceConfigField.name())) {
                            configDO.setName(aceConfigField.name());
                        } else {
                            configDO.setName(field.getName());
                        }
                        // 注意
                        // 相同名称的配置 取范围大的
                        Set<Integer> scopeSet = new HashSet<>();
                        for (int i : aceConfigField.scopes()) {
                            if (i == 1 || i == 2) {
                                scopeSet.add(i);
                            }
                        }
                        // 非平台应用自动添加应用级
                        scopeSet.add(3);
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
                        configDO.setAppId(app.getId());
                        configDO.setGroupId(app.getGroupId());

                        configDOS.add(configDO);
                    }
                }
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(configDOS)) {
                    clientService.scanConfig(configDOS);
                }
            }
        }

    }

    @Override
    public String getConfig(String key) {
        return getConfigByCurrentApp(key);
    }

    @Override
    public String getValue(String key, String defaultValue) {
        String config = getConfig(key);
        if (StringUtils.isEmpty(config)) {
            return defaultValue;
        }
        return config;

    }

    @Override
    public String getConfigByCurrentApp(String key) {
        return getConfigByApp(key, securityUtils.getAppName());
    }

    @Override
    public String getConfigByApp(String key, String appId) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(appId)) {
            String value = (String) cacheUtil.hget(appId, key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
            return clientService.getConfigValueByApp(appId, key);
        }
        return null;
    }
}
