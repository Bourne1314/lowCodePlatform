package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"name", "parentId", "appId", "parentName", "url", "type", "icon", "authId",
        "btnAuth", "isLeaf", "sortIndex", "allOrder", "sortPath", "openStyle", "closeNotice", "leaf",
        "menuAuth", "iframe", "traceId", "dataVersion", "createUser", "createTime", "updateTime", "remark"})
public class MenuDetail {

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 父级菜单id
     */
    private String parentId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 父级菜单名称
     */
    private String parentName;
    /**
     * 菜单路径
     */
    private String url;
    /**
     * 菜单类型-1目录0菜单1按钮
     */
    private Integer type;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 权限id
     */
    private String authId;
    /**
     * 权限标识
     */
    private String btnAuth;
    /**
     * 是否是叶子节点0不是1是
     */
    private Integer isLeaf;
    /**
     * 当前菜单下排序
     */
    private Integer sortIndex;
    /**
     * 全局排序
     */
    private Integer allOrder;
    /**
     * 路径供排序时候使用
     */
    private String sortPath;
    /**
     * 打开方式
     */
    private String openStyle;
    /**
     * 关闭提示0不提示1提示
     */
    private Integer closeNotice;
    /**
     * 叶子数量
     */
    private Integer leaf;
    /**
     * 菜单所属权限0平台1应用
     */
    private Integer menuAuth;
    /**
     * 是否为嵌套iframe 0不是 1是
     */
    private Integer iframe;
    /**
     * 跟踪ID
     */
    private String traceId;

    /**
     * 数据版本
     */
    private Integer dataVersion;

    /**
     * 创建人id
     */
    private String createUser;
    /**
     * 创建时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar createTime;
    /**
     * 最后一次修改时间
     */
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updateTime;

    /**
     * 备注
     */
    private String remark;

    public MenuDetail() {
        super();
    }

    public MenuDetail(String name, String parentId, String appId, String parentName, String url, Integer
            type, String icon, String authId, String btnAuth, Integer isLeaf, Integer sortIndex,
                      Integer allOrder, String sortPath, String openStyle, Integer closeNotice, Integer leaf,
                      Integer menuAuth, Integer iframe, String traceId, Integer dataVersion, String createUser,
                      XMLGregorianCalendar createTime
            , XMLGregorianCalendar updateTime, String remark) {
        this.name = name;
        this.parentId = parentId;
        this.appId = appId;
        this.parentName = parentName;
        this.url = url;
        this.type = type;
        this.icon = icon;
        this.authId = authId;
        this.btnAuth = btnAuth;
        this.isLeaf = isLeaf;
        this.sortIndex = sortIndex;
        this.allOrder = allOrder;
        this.sortPath = sortPath;
        this.openStyle = openStyle;
        this.closeNotice = closeNotice;
        this.leaf = leaf;
        this.menuAuth = menuAuth;
        this.iframe = iframe;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getBtnAuth() {
        return btnAuth;
    }

    public void setBtnAuth(String btnAuth) {
        this.btnAuth = btnAuth;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Integer getAllOrder() {
        return allOrder;
    }

    public void setAllOrder(Integer allOrder) {
        this.allOrder = allOrder;
    }

    public String getSortPath() {
        return sortPath;
    }

    public void setSortPath(String sortPath) {
        this.sortPath = sortPath;
    }

    public String getOpenStyle() {
        return openStyle;
    }

    public void setOpenStyle(String openStyle) {
        this.openStyle = openStyle;
    }

    public Integer getCloseNotice() {
        return closeNotice;
    }

    public void setCloseNotice(Integer closeNotice) {
        this.closeNotice = closeNotice;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getMenuAuth() {
        return menuAuth;
    }

    public void setMenuAuth(Integer menuAuth) {
        this.menuAuth = menuAuth;
    }

    public Integer getIframe() {
        return iframe;
    }

    public void setIframe(Integer iframe) {
        this.iframe = iframe;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    @XmlTransient
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }
    @XmlTransient
    public XMLGregorianCalendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(XMLGregorianCalendar updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
