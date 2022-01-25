package com.csicit.ace.common.pojo.domain.file;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/7/23 16:36
 */
@Data
public class ExceptConfiguration implements Serializable {
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 附件配置标识
     */
    private List<String> configurationKeys;

    public List<String> getConfigurationKeys() {
        if (configurationKeys == null) {
            configurationKeys = new ArrayList<>();
        }
        return configurationKeys;
    }

    public void addConfigurationKey(String configuratoinKey) {
        getConfigurationKeys().add(configuratoinKey);
    }
}
