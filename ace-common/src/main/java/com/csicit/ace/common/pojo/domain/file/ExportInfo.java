package com.csicit.ace.common.pojo.domain.file;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/7/7 17:37
 */
@Data
public class ExportInfo implements Serializable {
    private String appId;
    private String configurationKey;
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
