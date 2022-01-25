package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class FormField implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private String dataType;
    /**
     * 字段标题
     */
    private String caption;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

}
