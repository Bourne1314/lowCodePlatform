package com.csicit.ace.bpm.pojo.vo.wfd;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.exception.LinkNotFoundByIdException;
import com.csicit.ace.bpm.exception.NodeNotFoundByCodeException;
import com.csicit.ace.bpm.exception.NodeNotFoundByIdException;
import com.csicit.ace.bpm.exception.FlowVariantNotFoundByIdException;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/9/3 11:57
 */
public class Flow implements Serializable {
    private static final String DEFAULT_FORM_ID_NAME = "ID";
    /**
     * 主键
     */
    private String id;

    /**
     * 消息模板标识(待办工作)
     */
    private String msgTemplateCodeWaitJob;

    /**
     * 模板变量(待办工作)
     */
    private List<String> variableFieldWaitJob;

    /**
     * 消息模板标识(流程结束)
     */
    private String msgTemplateCodeOverJob;

    /**
     * 模板变量(流程结束)
     */
    private List<String> variableFieldOverJob;

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
     * 流程类别
     */
    private String categoryId;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 允许设置工作紧急程度
     */
    private Integer enableSetUrgency;
    /**
     * 修订版本号
     */
    private Integer reviseVersion;
    /**
     * 发布后已经修改过, 0没有，1有
     */
    private Integer hasModified;
    /**
     * 文号样式
     */
    private String workNoStyle;
    /**
     * 流水重置
     */
    private Integer workNoSeqResetRule;
    /**
     * 流水位数
     */
    private Integer workNoSeqLength;
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
     * 表单id名称
     */
    private String formIdName;
    /**
     * 级联删除
     */
    private Integer formCascadeDel;
    /**
     * 表单字段
     */
    private List<FormField> formFields = new ArrayList<>();
    /**
     * 密级字段
     */
    private String formSecretLevelField;
    /**
     * 结果字段
     */
    private String formResultField;
    /**
     * 表单操作
     */
    private List<String> formOperations = new ArrayList<>();
    /**
     * 表单保存操作
     */
    private String formSaveOperate;

    /**
     * 流程变量
     */
    private List<Variant> variants = new ArrayList<>();
    /**
     * 业务规则
     */
    private List<Rule> rules = new ArrayList<>();
    /**
     * 流程图JSON
     */
    private String flowChart;
    /**
     * 流程事件
     */
    private List<Event> events = new ArrayList<>();
    /**
     * 新待办通知
     */
    private String msgTemplateNewWork;
    /**
     * 流程结束通知
     */
    private String msgTemplateFinished;
    /**
     * 流程结束通知范围
     */
    private List<String> msgScopeFinished;
    /**
     * 通知消息类别
     */
    private String msgChannel;
    /**
     * 同步设置
     */
    private List<SyncSetting> syncSettings = new ArrayList<>();
    /**
     * 查询权限
     */
    private String queryAuthId;
    /**
     * 监控权限
     */
    private String adminAuthId;
    /**
     * 流程模型
     */
    private String model;
    /**
     * 节点
     */
    private List<Node> nodes = new ArrayList<>();
    /**
     * 流程路径
     */
    private List<Link> links = new ArrayList<>();

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public List<Variant> getVariants() {
        if (variants == null) {
            variants = new ArrayList<>();
        }
        return variants;
    }

    public void setSyncSettings(List<SyncSetting> syncSettings) {
        this.syncSettings = syncSettings;
    }

    public List<SyncSetting> getSyncSettings() {
        if (syncSettings == null) {
            syncSettings = new ArrayList<>();
        }
        return syncSettings;
    }

    public List<Node> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        return nodes;
    }

    /**
     * 获取后续节点
     *
     * @param nodeId 上级节点标识
     * @return java.util.List<com.csicit.ace.bpm.pojo.vo.wfd.Node>
     * @author JonnyJiang
     * @date 2019/9/23 21:36
     */

    public List<Node> getNextNodesByNodeId(String nodeId) {
        return getLinks().stream().filter(o -> StringUtils.equals(o.getFromNode().getId(), nodeId)).map
                (Link::getToNode).collect(Collectors.toList());
    }

    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<>();
        }
        return links;
    }

    /**
     * 获取流程模型
     *
     * @return 流程模型
     */
    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 添加节点
     *
     * @param node
     * @return void
     * @author JonnyJiang
     * @date 2019/9/3 15:12
     */

    public void addNode(Node node) {
        node.setFlow(this);
        this.nodes.add(node);
    }

    /**
     * 添加关联
     *
     * @param link
     * @return void
     * @author JonnyJiang
     * @date 2019/9/3 15:12
     */

    public void addLink(Link link) {
        link.getFromNode().addFlowOutLink(link);
        link.getToNode().addFlowInLink(link);
        link.setFlow(this);
        this.links.add(link);
    }

    /**
     * 转换为平台流程对象
     *
     * @param
     * @return WfdFlowDO
     * @author JonnyJiang
     * @date 2019/9/3 19:49
     */

    public WfdFlowDO toWfdFlow() {
        WfdFlowDO wfdFlow = new WfdFlowDO();
        wfdFlow.setId(getId());
        wfdFlow.setName(getName());
        wfdFlow.setCode(getCode());
        wfdFlow.setSortNo(getSortNo());
        wfdFlow.setCategoryId(getCategoryId());
        wfdFlow.setDescription(getDescription());
        wfdFlow.setAdminAuthId(getAdminAuthId());
        wfdFlow.setQueryAuthId(getQueryAuthId());
        wfdFlow.setHasModified(getHasModified());
        wfdFlow.setReviseVersion(getReviseVersion());
        wfdFlow.setModel(toJson());
        wfdFlow.setFormDataTable(getFormDataTable());
        wfdFlow.setFormDataSourceId(getFormDatasourceId());
        return wfdFlow;
    }

    public String getQueryAuthId() {
        return queryAuthId;
    }

    public void setQueryAuthId(String queryAuthId) {
        this.queryAuthId = queryAuthId;
    }

    public String getAdminAuthId() {
        return adminAuthId;
    }

    public void setAdminAuthId(String adminAuthId) {
        this.adminAuthId = adminAuthId;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public Integer getReviseVersion() {
        return reviseVersion;
    }

    public void setReviseVersion(Integer reviseVersion) {
        this.reviseVersion = reviseVersion;
    }

    public Integer getHasModified() {
        return hasModified;
    }

    public void setHasModified(Integer hasModified) {
        this.hasModified = hasModified;
    }

    public String getWorkNoStyle() {
        return workNoStyle;
    }

    public void setWorkNoStyle(String workNoStyle) {
        this.workNoStyle = workNoStyle;
    }

    public Integer getWorkNoSeqResetRule() {
        return workNoSeqResetRule;
    }

    public void setWorkNoSeqResetRule(Integer workNoSeqResetRule) {
        this.workNoSeqResetRule = workNoSeqResetRule;
    }

    public Integer getWorkNoSeqLength() {
        return workNoSeqLength;
    }

    public void setWorkNoSeqLength(Integer workNoSeqLength) {
        this.workNoSeqLength = workNoSeqLength;
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

    public Integer getFormCascadeDel() {
        return formCascadeDel;
    }

    public void setFormCascadeDel(Integer formCascadeDel) {
        this.formCascadeDel = formCascadeDel;
    }

    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }

    public List<FormField> getFormFields() {
        if (formFields == null) {
            formFields = new ArrayList<>();
        }
        return formFields;
    }

    public FormField getFormFieldById(String id) {
        if (StringUtils.isNotEmpty(id)) {
            for (FormField formField : getFormFields()) {
                if (StringUtils.equals(formField.getId(), id)) {
                    return formField;
                }
            }
        }
        throw new BpmException(LocaleUtils.getFormFieldNotFoundById(id));
    }

    public FormField getFormFieldByName(String name) {
        if (StringUtils.isNotEmpty(name)) {
            for (FormField formField : getFormFields()) {
                if (StringUtils.equals(formField.getName(), name)) {
                    return formField;
                }
            }
        }
        throw new BpmException(LocaleUtils.getFormFieldNotFoundByName(name));
    }

    public String getFormSecretLevelField() {
        return formSecretLevelField;
    }

    public void setFormSecretLevelField(String formSecretLevelField) {
        this.formSecretLevelField = formSecretLevelField;
    }

    public String getFormResultField() {
        return formResultField;
    }

    public void setFormResultField(String formResultField) {
        this.formResultField = formResultField;
    }

    public String getMsgTemplateCodeWaitJob() {
        return msgTemplateCodeWaitJob;
    }

    public void setMsgTemplateCodeWaitJob(String msgTemplateCodeWaitJob) {
        this.msgTemplateCodeWaitJob = msgTemplateCodeWaitJob;
    }

    public List<String> getVariableFieldWaitJob() {
        return variableFieldWaitJob;
    }

    public void setVariableFieldWaitJob(List<String> variableFieldWaitJob) {
        this.variableFieldWaitJob = variableFieldWaitJob;
    }

    public String getMsgTemplateCodeOverJob() {
        return msgTemplateCodeOverJob;
    }

    public void setMsgTemplateCodeOverJob(String msgTemplateCodeOverJob) {
        this.msgTemplateCodeOverJob = msgTemplateCodeOverJob;
    }

    public List<String> getVariableFieldOverJob() {
        return variableFieldOverJob;
    }

    public void setVariableFieldOverJob(List<String> variableFieldOverJob) {
        this.variableFieldOverJob = variableFieldOverJob;
    }

    public void setFormOperations(List<String> formOperations) {
        this.formOperations = formOperations;
    }

    public List<String> getFormOperations() {
        if (formOperations == null) {
            formOperations = new ArrayList<>();
        }
        return formOperations;
    }

    public String getFormSaveOperate() {
        return formSaveOperate;
    }

    public void setFormSaveOperate(String formSaveOperate) {
        this.formSaveOperate = formSaveOperate;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        return rules;
    }

    public String getFlowChart() {
        return flowChart;
    }

    public void setFlowChart(String flowChart) {
        this.flowChart = flowChart;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }

    public String getMsgTemplateNewWork() {
        return msgTemplateNewWork;
    }

    public void setMsgTemplateNewWork(String msgTemplateNewWork) {
        this.msgTemplateNewWork = msgTemplateNewWork;
    }

    public String getMsgTemplateFinished() {
        return msgTemplateFinished;
    }

    public void setMsgTemplateFinished(String msgTemplateFinished) {
        this.msgTemplateFinished = msgTemplateFinished;
    }

    public void setMsgScopeFinished(List<String> msgScopeFinished) {
        this.msgScopeFinished = msgScopeFinished;
    }

    public List<String> getMsgScopeFinished() {
        if (msgScopeFinished == null) {
            msgScopeFinished = new ArrayList<>();
        }
        return msgScopeFinished;
    }

    public String getMsgChannel() {
        return msgChannel;
    }

    public void setMsgChannel(String msgChannel) {
        this.msgChannel = msgChannel;
    }

    public String getFormIdName() {
        if (StringUtils.isEmpty(formIdName)) {
            return DEFAULT_FORM_ID_NAME;
        }
        return formIdName;
    }

    public void setFormIdName(String formIdName) {
        this.formIdName = formIdName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 通过ID获取流程变量
     *
     * @param id 变量id
     * @return 流程变量
     * @author yansiyang
     * @date 2019/9/5 10:11
     */
    public Variant getVariantById(String id) {
        List<Variant> variantList = getVariants();
        for (Variant variant : variantList) {
            if (StringUtils.equals(id, variant.getId())) {
                return variant;
            }
        }
        throw new FlowVariantNotFoundByIdException(id, getId());
    }

    /**
     * 通过ID获取业务规则
     *
     * @param id
     * @return 业务规则
     * @author yansiyang
     * @date 2019/9/5 10:11
     */
    public Rule getRuleById(String id) {
        List<Rule> ruleList = getRules();
        for (Rule rule : ruleList) {
            if (StringUtils.equals(id, rule.getId())) {
                return rule;
            }
        }
        return null;
    }

    /**
     * 获取发起权限id
     *
     * @return 发起权限id
     * @author JonnyJiang
     * @date 2019/11/20 14:19
     */

    public String getInitAuthId() {
        Optional<Node> optional = getNodes().stream().filter(o -> NodeType.Start.isEquals(o.getNodeType()) &&
                IntegerUtils.isTrue(o.getEnableApiStart())).findFirst();
        if (optional.isPresent()) {
            Node startNode = optional.get();
            if (IntegerUtils.isTrue(startNode.getAllowManualStart())) {
                return startNode.getInitAuthId();
            }

        }
        return null;
    }

    /**
     * 根据节点标识获取节点
     *
     * @param nodeId 节点id
     * @return com.csicit.ace.bpm.pojo.vo.wfd.Node
     * @author JonnyJiang
     * @date 2019/9/23 21:37
     */

    public Node getNodeById(String nodeId) {
        for (Node node : getNodes()) {
            if (StringUtils.equals(node.getId(), nodeId)) {
                return node;
            }
        }
        throw new NodeNotFoundByIdException(nodeId, getId());
    }

    /**
     * 获取节点
     *
     * @param code 节点标识
     * @return 节点
     * @author JonnyJiang
     * @date 2019/11/13 17:20
     */

    public Node getNodeByCode(String code) {
        for (Node node : getNodes()) {
            if (StringUtils.isNotEmpty(node.getCode())) {
                if (StringUtils.equals(node.getCode(), code)) {
                    return node;
                }
            }
        }
        throw new NodeNotFoundByCodeException(code, getId());
    }

    /**
     * 获取分支
     *
     * @param id 分支id
     * @return 分支
     * @author JonnyJiang
     * @date 2020/11/12 10:01
     */

    public Link getLinkById(String id) {
        for (Link link : getLinks()) {
            if (StringUtils.isNotEmpty(link.getId())) {
                if (StringUtils.equals(link.getId(), id)) {
                    return link;
                }
            }
        }
        throw new LinkNotFoundByIdException(id, getId());
    }
}