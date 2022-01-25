package com.csicit.ace.common.AppUpgradeJaxb;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder = {"configurationType", "groupId", "appId", "configurationKey", "groupDatasourceId", "tableName",
        "accept", "fileNumLimit", "fileSingleSizeLimit", "fileSizeLimit", "enableEncrypt", "enableSecretLevel",
        "allowUpload", "allowDelete", "allowDownload", "uploadAuthCode", "deleteAuthCode", "downloadAuthCode",
        "enableDownloadToken", "enableUserSeparate", "fileRepositoryId", "subDirFormat", "enableImageCompress",
        "compressedImageWidth", "compressedImageHeight", "compressImageSize", "enablePreview", "enableReview",
        "sign", "enableEvtFileDeleting", "enableEvtFileDownloading", "enableEvtFileDownloaded",
        "enableEvtFileUploading", "enableEvtFileUploaded", "uploadOperationKey", "downloadOperationKey",
        "deleteOperationKey", "allowDownloadWithoutLogin", "traceId", "dataVersion", "createUser", "createTime",
        "updateTime", "remark"})
public class FileConfigDetail {

    /**
     * 附件配置类型（0-租户级1-集团级2-应用级3-表单级）
     */
    private Integer configurationType;

    /**
     * 集团id
     */
    private String groupId;

    /**
     * 集团应用id
     */
    private String appId;

    /**
     * 配置标识
     */
    private String configurationKey;

    /**
     * 关联数据源id
     */
    private String groupDatasourceId;

    /**
     * 关联表名
     */
    private String tableName;

    /**
     * 允许上传的文件类型
     */
    private String accept;

    /**
     * 允许上传文件的最大数量
     */
    private Integer fileNumLimit;

    /**
     * 允许上传的最大单个文件大小
     */
    private Long fileSingleSizeLimit;

    /**
     * 允许上传文件的总大小
     */
    private Long fileSizeLimit;

    /**
     * 是否启用文件加密保存
     */
    private Integer enableEncrypt;

    /**
     * 是否允许设置文件密级
     */
    private Integer enableSecretLevel;

    /**
     * 是否允许上传
     */
    private Integer allowUpload;

    /**
     * 是否允许删除
     */
    private Integer allowDelete;

    /**
     * 是否允许下载
     */
    private Integer allowDownload;

    /**
     * 允许上传权限标识
     */
    private String uploadAuthCode;

    /**
     * 允许删除权限标识
     */
    private String deleteAuthCode;

    /**
     * 允许下载权限标识
     */
    private String downloadAuthCode;

    /**
     * 是否通过临时Token下载
     */
    private Integer enableDownloadToken;

    /**
     * 是否禁止删除他人上传文件
     */
    private Integer enableUserSeparate;

    /**
     * 指定文件存储库id
     */
    private String fileRepositoryId;


    /**
     * 存储路径生成规则
     */
    private String subDirFormat;

    /**
     * 是否启用图片压缩上传
     */
    private Integer enableImageCompress;

    /**
     * 图片压缩宽度
     */
    private Integer compressedImageWidth;

    /**
     * 图片压缩高度
     */
    private Integer compressedImageHeight;

    /**
     * 启用图片压缩的图片大小
     */
    private Integer compressImageSize;

    /**
     * 是否启用文件预览
     */
    private Integer enablePreview;

    /**
     * 是否启用附件审查
     */
    private Integer enableReview;

    /**
     * 数据签名
     */
    private String sign;
    /**
     * 是否启用事件文件删除前
     */
    private Integer enableEvtFileDeleting;
    /**
     * 是否启用事件文件下载前
     */
    private Integer enableEvtFileDownloading;
    /**
     * 是否启用事件文件下载后
     */
    private Integer enableEvtFileDownloaded;
    /**
     * 是否启用事件文件上传前
     */
    private Integer enableEvtFileUploading;
    /**
     * 是否启用事件文件上传后（不影响文件上传到服务器）
     */
    private Integer enableEvtFileUploaded;
    /**
     * 上传操作标识
     */
    private String uploadOperationKey;
    /**
     * 下载操作标识
     */
    private String downloadOperationKey;
    /**
     * 删除操作标识
     */
    private String deleteOperationKey;

    /**
     * 是否允许不登录下载
     */
    private Integer allowDownloadWithoutLogin;
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

    public FileConfigDetail() {
        super();
    }

    public FileConfigDetail(Integer configurationType, String groupId, String appId, String configurationKey, String
            groupDatasourceId, String tableName, String accept, Integer fileNumLimit, Long fileSingleSizeLimit, Long
            fileSizeLimit, Integer enableEncrypt, Integer enableSecretLevel, Integer allowUpload, Integer
            allowDelete, Integer allowDownload, String uploadAuthCode, String deleteAuthCode, String
            downloadAuthCode, Integer enableDownloadToken, Integer enableUserSeparate, String fileRepositoryId,
                            String subDirFormat, Integer enableImageCompress, Integer compressedImageWidth, Integer
                                    compressedImageHeight, Integer compressImageSize, Integer enablePreview, Integer
                                    enableReview, String sign, Integer enableEvtFileDeleting, Integer
                                    enableEvtFileDownloading, Integer enableEvtFileDownloaded, Integer
                                    enableEvtFileUploading, Integer enableEvtFileUploaded, String uploadOperationKey,
                            String downloadOperationKey, String deleteOperationKey, Integer
                                    allowDownloadWithoutLogin, String traceId, Integer dataVersion, String
                                    createUser, XMLGregorianCalendar createTime, XMLGregorianCalendar updateTime,
                            String remark) {
        this.configurationType = configurationType;
        this.groupId = groupId;
        this.appId = appId;
        this.configurationKey = configurationKey;
        this.groupDatasourceId = groupDatasourceId;
        this.tableName = tableName;
        this.accept = accept;
        this.fileNumLimit = fileNumLimit;
        this.fileSingleSizeLimit = fileSingleSizeLimit;
        this.fileSizeLimit = fileSizeLimit;
        this.enableEncrypt = enableEncrypt;
        this.enableSecretLevel = enableSecretLevel;
        this.allowUpload = allowUpload;
        this.allowDelete = allowDelete;
        this.allowDownload = allowDownload;
        this.uploadAuthCode = uploadAuthCode;
        this.deleteAuthCode = deleteAuthCode;
        this.downloadAuthCode = downloadAuthCode;
        this.enableDownloadToken = enableDownloadToken;
        this.enableUserSeparate = enableUserSeparate;
        this.fileRepositoryId = fileRepositoryId;
        this.subDirFormat = subDirFormat;
        this.enableImageCompress = enableImageCompress;
        this.compressedImageWidth = compressedImageWidth;
        this.compressedImageHeight = compressedImageHeight;
        this.compressImageSize = compressImageSize;
        this.enablePreview = enablePreview;
        this.enableReview = enableReview;
        this.sign = sign;
        this.enableEvtFileDeleting = enableEvtFileDeleting;
        this.enableEvtFileDownloading = enableEvtFileDownloading;
        this.enableEvtFileDownloaded = enableEvtFileDownloaded;
        this.enableEvtFileUploading = enableEvtFileUploading;
        this.enableEvtFileUploaded = enableEvtFileUploaded;
        this.uploadOperationKey = uploadOperationKey;
        this.downloadOperationKey = downloadOperationKey;
        this.deleteOperationKey = deleteOperationKey;
        this.allowDownloadWithoutLogin = allowDownloadWithoutLogin;
        this.traceId = traceId;
        this.dataVersion = dataVersion;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
    }

    public Integer getConfigurationType() {
        return configurationType;
    }

    public void setConfigurationType(Integer configurationType) {
        this.configurationType = configurationType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(String configurationKey) {
        this.configurationKey = configurationKey;
    }

    public String getGroupDatasourceId() {
        return groupDatasourceId;
    }

    public void setGroupDatasourceId(String groupDatasourceId) {
        this.groupDatasourceId = groupDatasourceId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public Integer getFileNumLimit() {
        return fileNumLimit;
    }

    public void setFileNumLimit(Integer fileNumLimit) {
        this.fileNumLimit = fileNumLimit;
    }

    public Long getFileSingleSizeLimit() {
        return fileSingleSizeLimit;
    }

    public void setFileSingleSizeLimit(Long fileSingleSizeLimit) {
        this.fileSingleSizeLimit = fileSingleSizeLimit;
    }

    public Long getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(Long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }

    public Integer getEnableEncrypt() {
        return enableEncrypt;
    }

    public void setEnableEncrypt(Integer enableEncrypt) {
        this.enableEncrypt = enableEncrypt;
    }

    public Integer getEnableSecretLevel() {
        return enableSecretLevel;
    }

    public void setEnableSecretLevel(Integer enableSecretLevel) {
        this.enableSecretLevel = enableSecretLevel;
    }

    public Integer getAllowUpload() {
        return allowUpload;
    }

    public void setAllowUpload(Integer allowUpload) {
        this.allowUpload = allowUpload;
    }

    public Integer getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Integer allowDelete) {
        this.allowDelete = allowDelete;
    }

    public Integer getAllowDownload() {
        return allowDownload;
    }

    public void setAllowDownload(Integer allowDownload) {
        this.allowDownload = allowDownload;
    }

    public String getUploadAuthCode() {
        return uploadAuthCode;
    }

    public void setUploadAuthCode(String uploadAuthCode) {
        this.uploadAuthCode = uploadAuthCode;
    }

    public String getDeleteAuthCode() {
        return deleteAuthCode;
    }

    public void setDeleteAuthCode(String deleteAuthCode) {
        this.deleteAuthCode = deleteAuthCode;
    }

    public String getDownloadAuthCode() {
        return downloadAuthCode;
    }

    public void setDownloadAuthCode(String downloadAuthCode) {
        this.downloadAuthCode = downloadAuthCode;
    }

    public Integer getEnableDownloadToken() {
        return enableDownloadToken;
    }

    public void setEnableDownloadToken(Integer enableDownloadToken) {
        this.enableDownloadToken = enableDownloadToken;
    }

    public Integer getEnableUserSeparate() {
        return enableUserSeparate;
    }

    public void setEnableUserSeparate(Integer enableUserSeparate) {
        this.enableUserSeparate = enableUserSeparate;
    }

    public String getFileRepositoryId() {
        return fileRepositoryId;
    }

    public void setFileRepositoryId(String fileRepositoryId) {
        this.fileRepositoryId = fileRepositoryId;
    }

    public String getSubDirFormat() {
        return subDirFormat;
    }

    public void setSubDirFormat(String subDirFormat) {
        this.subDirFormat = subDirFormat;
    }

    public Integer getEnableImageCompress() {
        return enableImageCompress;
    }

    public void setEnableImageCompress(Integer enableImageCompress) {
        this.enableImageCompress = enableImageCompress;
    }

    public Integer getCompressedImageWidth() {
        return compressedImageWidth;
    }

    public void setCompressedImageWidth(Integer compressedImageWidth) {
        this.compressedImageWidth = compressedImageWidth;
    }

    public Integer getCompressedImageHeight() {
        return compressedImageHeight;
    }

    public void setCompressedImageHeight(Integer compressedImageHeight) {
        this.compressedImageHeight = compressedImageHeight;
    }

    public Integer getCompressImageSize() {
        return compressImageSize;
    }

    public void setCompressImageSize(Integer compressImageSize) {
        this.compressImageSize = compressImageSize;
    }

    public Integer getEnablePreview() {
        return enablePreview;
    }

    public void setEnablePreview(Integer enablePreview) {
        this.enablePreview = enablePreview;
    }

    public Integer getEnableReview() {
        return enableReview;
    }

    public void setEnableReview(Integer enableReview) {
        this.enableReview = enableReview;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getEnableEvtFileDeleting() {
        return enableEvtFileDeleting;
    }

    public void setEnableEvtFileDeleting(Integer enableEvtFileDeleting) {
        this.enableEvtFileDeleting = enableEvtFileDeleting;
    }

    public Integer getEnableEvtFileDownloading() {
        return enableEvtFileDownloading;
    }

    public void setEnableEvtFileDownloading(Integer enableEvtFileDownloading) {
        this.enableEvtFileDownloading = enableEvtFileDownloading;
    }

    public Integer getEnableEvtFileDownloaded() {
        return enableEvtFileDownloaded;
    }

    public void setEnableEvtFileDownloaded(Integer enableEvtFileDownloaded) {
        this.enableEvtFileDownloaded = enableEvtFileDownloaded;
    }

    public Integer getEnableEvtFileUploading() {
        return enableEvtFileUploading;
    }

    public void setEnableEvtFileUploading(Integer enableEvtFileUploading) {
        this.enableEvtFileUploading = enableEvtFileUploading;
    }

    public Integer getEnableEvtFileUploaded() {
        return enableEvtFileUploaded;
    }

    public void setEnableEvtFileUploaded(Integer enableEvtFileUploaded) {
        this.enableEvtFileUploaded = enableEvtFileUploaded;
    }

    public String getUploadOperationKey() {
        return uploadOperationKey;
    }

    public void setUploadOperationKey(String uploadOperationKey) {
        this.uploadOperationKey = uploadOperationKey;
    }

    public String getDownloadOperationKey() {
        return downloadOperationKey;
    }

    public void setDownloadOperationKey(String downloadOperationKey) {
        this.downloadOperationKey = downloadOperationKey;
    }

    public String getDeleteOperationKey() {
        return deleteOperationKey;
    }

    public void setDeleteOperationKey(String deleteOperationKey) {
        this.deleteOperationKey = deleteOperationKey;
    }

    public Integer getAllowDownloadWithoutLogin() {
        return allowDownloadWithoutLogin;
    }

    public void setAllowDownloadWithoutLogin(Integer allowDownloadWithoutLogin) {
        this.allowDownloadWithoutLogin = allowDownloadWithoutLogin;
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
