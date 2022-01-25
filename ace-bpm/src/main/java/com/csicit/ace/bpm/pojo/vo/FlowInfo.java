package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;

import java.io.Serializable;

/**
 * 流程信息
 *
 * @author JonnyJiang
 * @date 2019/10/8 19:58
 */
public class FlowInfo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程标识
     */
    private String code;
    /**
     * 流程序号
     */
    private Integer sortNo;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 允许设置工作紧急程度
     */
    private Integer enableSetUrgency;
    /**
     * 页面URL
     */
    private String formUrl;
    /**
     * 数据源
     */
    private String formDatasourceId;
    /**
     * 数据表
     */
    private String formDataTable;
    /**
     * 流程模型
     */
    private String model;
    /**
     * 流程图JSON
     */
    private String flowChart;
    /**
     * 工作文号
     */
    private String flowNo;

    /**
     * 业务标识
     */
    private String businessKey;

    /**
     * 表单保存操作
     */
    private String formSaveOperate;

    /**
     * 流程实例id
     */
    private String flowInstanceId;

    /**
     * 构造函数
     *
     * @param flow    流程定义
     * @param wfiFlow 流程实例
     * @author JonnyJiang
     * @date 2019/10/8 20:12
     */

    public FlowInfo(Flow flow, WfiFlowDO wfiFlow) {
        this.id = flow.getId();
        this.name = flow.getName();
        this.code = flow.getCode();
        this.sortNo = flow.getSortNo();
        this.description = flow.getDescription();
        this.enableSetUrgency = flow.getEnableSetUrgency();
        this.formUrl = flow.getFormUrl();
        this.formDatasourceId = flow.getFormDatasourceId();
        this.formDataTable = flow.getFormDataTable();
        this.model = flow.getModel();
        this.flowChart = flow.getFlowChart();
        this.formSaveOperate = flow.getFormSaveOperate();
        if (wfiFlow != null) {
            this.flowNo = wfiFlow.getFlowNo();
            this.businessKey = wfiFlow.getBusinessKey();
            this.flowInstanceId = wfiFlow.getId();
        }
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnableSetUrgency() {
        return enableSetUrgency;
    }

    public void setEnableSetUrgency(Integer enableSetUrgency) {
        this.enableSetUrgency = enableSetUrgency;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getFormDatasourceId() {
        return formDatasourceId;
    }

    public void setFormDatasourceId(String formDatasourceId) {
        this.formDatasourceId = formDatasourceId;
    }

    public String getFormDataTable() {
        return formDataTable;
    }

    public void setFormDataTable(String formDataTable) {
        this.formDataTable = formDataTable;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFlowChart() {
        return flowChart;
    }

    public void setFlowChart(String flowChart) {
        this.flowChart = flowChart;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getFormSaveOperate() {
        return formSaveOperate;
    }

    public void setFormSaveOperate(String formSaveOperate) {
        this.formSaveOperate = formSaveOperate;
    }

    public String getFlowInstanceId() {
        return flowInstanceId;
    }

    public void setFlowInstanceId(String flowInstanceId) {
        this.flowInstanceId = flowInstanceId;
    }
}