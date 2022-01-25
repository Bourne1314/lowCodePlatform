package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class SyncSetting implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 变量名
     */
    private String variantName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 所属流程
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private transient Flow flow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }
}
