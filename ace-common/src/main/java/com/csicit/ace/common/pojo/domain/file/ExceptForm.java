package com.csicit.ace.common.pojo.domain.file;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/7/23 16:18
 */
@Data
public class ExceptForm implements Serializable {
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 附件配置标识
     */
    private String configurationKey;

    /**
     * 是否需要匹配配置标识
     */
    private Boolean needMatchConfigurationKey = true;

    /**
     * 表单id列表
     */
    private List<String> formIds;

    public List<String> getFormIds() {
        if (formIds == null) {
            formIds = new ArrayList<>();
        }
        return formIds;
    }

    public void addFormId(String formId) {
        getFormIds().add(formId);
    }
}
