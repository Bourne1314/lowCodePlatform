package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "appUpgrade")
public class AppUpgrade {
    // 应用ID
    private String appId;
    // 版本号
    private String version;
    // 数据源
    private List<GroupDatasourceDetail> groupDatasource;
    // 组件注册
    private List<ComponentRegisterDetail> componentRegister;
    // 权限
    private List<AuthDetail> auth;
    // API
    private List<ApiResourceDetail> apiResource;
    // 权限API
    private List<AuthApiDetail> authApi;
    // 菜单定义
    private List<MenuDetail> menu;
    // 角色
    private List<RoleDetail> role;
    // 上下级角色
    private List<RoleRelationDetail> roleRelation;
    // 互斥角色
    private List<RoleMutexDetail> roleMutex;
    // 角色授权
    private List<AuthRoleDetail> authRole;
    private List<AuthRoleLvDetail> authRoleLv;
    private List<AuthRoleVDetail> authRoleV;
    // 附件配置
    private List<FileConfigDetail> fileConfiguration;
    // 定时任务
    private List<QrtzConfigDetail> qrtzConfig;
    // 消息通道
    private List<MsgSendTypeDetail> msgSendType;
    // 消息模板
    private List<MsgTemplateDetail> msgTemplate;
    // 信使消息模板
    private List<MsgTemplateConfigDetail> msgTemplateConfig;
    // 消息拓展
    private List<MsgTypeExtendDetail> msgTypeExtend;
    // 数据字典
    private List<DictDetail> dict;
    private List<DictValueDetail> dictValue;
    // 数据模型
    private List<ServiceDetail> services;
    private List<ModelDetail> model;
    private List<ModelColDetail> modelCol;
    private List<ModelIndexDetail> modelIndex;
    private List<ModelAssDetail> modelAss;
    // 系统配置
    private List<SysConfigDetail> sysConfig;
    // 业务类型
    private List<ReportTypeDetail> reportType;
    // 报表/仪表盘信息
    private List<ReportInfoDetail> reportInfo;
    // 接口
    private List<AppInterfaceDetail> appInterface;
    private List<AppInterfaceInDetail> appInterfaceIn;
    private List<AppInterfaceOutDetail> appInterfaceOut;
    // 大屏
    private List<BladeVisualDetail> bladeVisual;
    // 大屏展示
    private List<BladeVisualShowDetail> bladeVisualShow;
    // 大屏消息
    private List<BladeVisualMsgDetail> bladeVisualMsg;


    // 数字序列
    private List<CodeSeqDetail> codeSeq;
    //编码模板
    private List<CodeTemplateDetail> codeTemplate;
    private List<CodeTemplatePartDetail> codeTemplatePart;

    public AppUpgrade() {
        super();
    }

    public AppUpgrade(String appId, String version, List<GroupDatasourceDetail> groupDatasource,
                      List<ComponentRegisterDetail> componentRegister, List<AuthDetail> auth, List<ApiResourceDetail>
                              apiResource, List<AuthApiDetail> authApi, List<MenuDetail> menu, List<RoleDetail> role,
                      List<RoleRelationDetail> roleRelation, List<RoleMutexDetail> roleMutex, List<AuthRoleDetail>
                              authRole, List<AuthRoleLvDetail> authRoleLv, List<AuthRoleVDetail> authRoleV,
                      List<FileConfigDetail> fileConfiguration, List<QrtzConfigDetail> qrtzConfig,
                      List<MsgSendTypeDetail> msgSendType, List<MsgTemplateDetail> msgTemplate,
                      List<MsgTemplateConfigDetail> msgTemplateConfig, List<MsgTypeExtendDetail> msgTypeExtend,
                      List<DictDetail> dict, List<DictValueDetail> dictValue, List<ServiceDetail> services,
                      List<ModelDetail> model, List<ModelColDetail> modelCol, List<ModelIndexDetail> modelIndex,
                      List<ModelAssDetail> modelAss, List<SysConfigDetail> sysConfig, List<ReportTypeDetail>
                              reportType, List<ReportInfoDetail> reportInfo, List<AppInterfaceDetail> appInterface,
                      List<AppInterfaceInDetail> appInterfaceIn, List<AppInterfaceOutDetail> appInterfaceOut,
                      List<BladeVisualDetail> bladeVisual, List<BladeVisualShowDetail> bladeVisualShow,
                      List<BladeVisualMsgDetail> bladeVisualMsg, List<CodeSeqDetail> codeSeq,
                      List<CodeTemplateDetail> codeTemplate, List<CodeTemplatePartDetail> codeTemplatePart) {
        this.appId = appId;
        this.version = version;
        this.groupDatasource = groupDatasource;
        this.componentRegister = componentRegister;
        this.auth = auth;
        this.apiResource = apiResource;
        this.authApi = authApi;
        this.menu = menu;
        this.role = role;
        this.roleRelation = roleRelation;
        this.roleMutex = roleMutex;
        this.authRole = authRole;
        this.authRoleLv = authRoleLv;
        this.authRoleV = authRoleV;
        this.fileConfiguration = fileConfiguration;
        this.qrtzConfig = qrtzConfig;
        this.msgSendType = msgSendType;
        this.msgTemplate = msgTemplate;
        this.msgTemplateConfig = msgTemplateConfig;
        this.msgTypeExtend = msgTypeExtend;
        this.dict = dict;
        this.dictValue = dictValue;
        this.services = services;
        this.model = model;
        this.modelCol = modelCol;
        this.modelIndex = modelIndex;
        this.modelAss = modelAss;
        this.sysConfig = sysConfig;
        this.reportType = reportType;
        this.reportInfo = reportInfo;
        this.appInterface = appInterface;
        this.appInterfaceIn = appInterfaceIn;
        this.appInterfaceOut = appInterfaceOut;
        this.bladeVisual = bladeVisual;
        this.bladeVisualShow = bladeVisualShow;
        this.bladeVisualMsg = bladeVisualMsg;
        this.codeSeq = codeSeq;
        this.codeTemplate = codeTemplate;
        this.codeTemplatePart = codeTemplatePart;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<GroupDatasourceDetail> getGroupDatasource() {
        return groupDatasource;
    }

    public void setGroupDatasource(List<GroupDatasourceDetail> groupDatasource) {
        this.groupDatasource = groupDatasource;
    }

    public List<ComponentRegisterDetail> getComponentRegister() {
        return componentRegister;
    }

    public void setComponentRegister(List<ComponentRegisterDetail> componentRegister) {
        this.componentRegister = componentRegister;
    }

    public List<AuthDetail> getAuth() {
        return auth;
    }

    public void setAuth(List<AuthDetail> auth) {
        this.auth = auth;
    }

    public List<ApiResourceDetail> getApiResource() {
        return apiResource;
    }

    public void setApiResource(List<ApiResourceDetail> apiResource) {
        this.apiResource = apiResource;
    }

    public List<AuthApiDetail> getAuthApi() {
        return authApi;
    }

    public void setAuthApi(List<AuthApiDetail> authApi) {
        this.authApi = authApi;
    }

    public List<MenuDetail> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuDetail> menu) {
        this.menu = menu;
    }

    public List<RoleDetail> getRole() {
        return role;
    }

    public void setRole(List<RoleDetail> role) {
        this.role = role;
    }

    public List<RoleRelationDetail> getRoleRelation() {
        return roleRelation;
    }

    public void setRoleRelation(List<RoleRelationDetail> roleRelation) {
        this.roleRelation = roleRelation;
    }

    public List<RoleMutexDetail> getRoleMutex() {
        return roleMutex;
    }

    public void setRoleMutex(List<RoleMutexDetail> roleMutex) {
        this.roleMutex = roleMutex;
    }

    public List<AuthRoleDetail> getAuthRole() {
        return authRole;
    }

    public void setAuthRole(List<AuthRoleDetail> authRole) {
        this.authRole = authRole;
    }

    public List<AuthRoleLvDetail> getAuthRoleLv() {
        return authRoleLv;
    }

    public void setAuthRoleLv(List<AuthRoleLvDetail> authRoleLv) {
        this.authRoleLv = authRoleLv;
    }

    public List<AuthRoleVDetail> getAuthRoleV() {
        return authRoleV;
    }

    public void setAuthRoleV(List<AuthRoleVDetail> authRoleV) {
        this.authRoleV = authRoleV;
    }

    public List<FileConfigDetail> getFileConfiguration() {
        return fileConfiguration;
    }

    public void setFileConfiguration(List<FileConfigDetail> fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public List<QrtzConfigDetail> getQrtzConfig() {
        return qrtzConfig;
    }

    public void setQrtzConfig(List<QrtzConfigDetail> qrtzConfig) {
        this.qrtzConfig = qrtzConfig;
    }

    public List<MsgSendTypeDetail> getMsgSendType() {
        return msgSendType;
    }

    public void setMsgSendType(List<MsgSendTypeDetail> msgSendType) {
        this.msgSendType = msgSendType;
    }

    public List<MsgTemplateDetail> getMsgTemplate() {
        return msgTemplate;
    }

    public void setMsgTemplate(List<MsgTemplateDetail> msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    public List<MsgTemplateConfigDetail> getMsgTemplateConfig() {
        return msgTemplateConfig;
    }

    public void setMsgTemplateConfig(List<MsgTemplateConfigDetail> msgTemplateConfig) {
        this.msgTemplateConfig = msgTemplateConfig;
    }

    public List<MsgTypeExtendDetail> getMsgTypeExtend() {
        return msgTypeExtend;
    }

    public void setMsgTypeExtend(List<MsgTypeExtendDetail> msgTypeExtend) {
        this.msgTypeExtend = msgTypeExtend;
    }

    public List<DictDetail> getDict() {
        return dict;
    }

    public void setDict(List<DictDetail> dict) {
        this.dict = dict;
    }

    public List<DictValueDetail> getDictValue() {
        return dictValue;
    }

    public void setDictValue(List<DictValueDetail> dictValue) {
        this.dictValue = dictValue;
    }

    public List<ServiceDetail> getServices() {
        return services;
    }

    public void setServices(List<ServiceDetail> services) {
        this.services = services;
    }

    public List<ModelDetail> getModel() {
        return model;
    }

    public void setModel(List<ModelDetail> model) {
        this.model = model;
    }

    public List<ModelColDetail> getModelCol() {
        return modelCol;
    }

    public void setModelCol(List<ModelColDetail> modelCol) {
        this.modelCol = modelCol;
    }

    public List<ModelIndexDetail> getModelIndex() {
        return modelIndex;
    }

    public void setModelIndex(List<ModelIndexDetail> modelIndex) {
        this.modelIndex = modelIndex;
    }

    public List<ModelAssDetail> getModelAss() {
        return modelAss;
    }

    public void setModelAss(List<ModelAssDetail> modelAss) {
        this.modelAss = modelAss;
    }

    public List<SysConfigDetail> getSysConfig() {
        return sysConfig;
    }

    public void setSysConfig(List<SysConfigDetail> sysConfig) {
        this.sysConfig = sysConfig;
    }

    public List<ReportTypeDetail> getReportType() {
        return reportType;
    }

    public void setReportType(List<ReportTypeDetail> reportType) {
        this.reportType = reportType;
    }

    public List<ReportInfoDetail> getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(List<ReportInfoDetail> reportInfo) {
        this.reportInfo = reportInfo;
    }

    public List<AppInterfaceDetail> getAppInterface() {
        return appInterface;
    }

    public void setAppInterface(List<AppInterfaceDetail> appInterface) {
        this.appInterface = appInterface;
    }

    public List<AppInterfaceInDetail> getAppInterfaceIn() {
        return appInterfaceIn;
    }

    public void setAppInterfaceIn(List<AppInterfaceInDetail> appInterfaceIn) {
        this.appInterfaceIn = appInterfaceIn;
    }

    public List<AppInterfaceOutDetail> getAppInterfaceOut() {
        return appInterfaceOut;
    }

    public void setAppInterfaceOut(List<AppInterfaceOutDetail> appInterfaceOut) {
        this.appInterfaceOut = appInterfaceOut;
    }

    public List<BladeVisualDetail> getBladeVisual() {
        return bladeVisual;
    }

    public void setBladeVisual(List<BladeVisualDetail> bladeVisual) {
        this.bladeVisual = bladeVisual;
    }

    public List<BladeVisualShowDetail> getBladeVisualShow() {
        return bladeVisualShow;
    }

    public void setBladeVisualShow(List<BladeVisualShowDetail> bladeVisualShow) {
        this.bladeVisualShow = bladeVisualShow;
    }

    public List<BladeVisualMsgDetail> getBladeVisualMsg() {
        return bladeVisualMsg;
    }

    public void setBladeVisualMsg(List<BladeVisualMsgDetail> bladeVisualMsg) {
        this.bladeVisualMsg = bladeVisualMsg;
    }

    public List<CodeSeqDetail> getCodeSeq() {
        return codeSeq;
    }

    public void setCodeSeq(List<CodeSeqDetail> codeSeq) {
        this.codeSeq = codeSeq;
    }

    public List<CodeTemplateDetail> getCodeTemplate() {
        return codeTemplate;
    }

    public void setCodeTemplate(List<CodeTemplateDetail> codeTemplate) {
        this.codeTemplate = codeTemplate;
    }

    public List<CodeTemplatePartDetail> getCodeTemplatePart() {
        return codeTemplatePart;
    }

    public void setCodeTemplatePart(List<CodeTemplatePartDetail> codeTemplatePart) {
        this.codeTemplatePart = codeTemplatePart;
    }
}
