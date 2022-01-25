package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 流程变量
 *
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class Variant implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 变量名
     */
    private String name;
    /**
     * 变量标题
     */
    private String caption;
    /**
     * 类型
     */
    private String dataType;
    /**
     * 初始值
     */
    private String defaultValue;
    /**
     * 取值表达式
     */
    private String valueExpression;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

}
