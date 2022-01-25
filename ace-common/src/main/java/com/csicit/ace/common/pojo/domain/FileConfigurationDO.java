package com.csicit.ace.common.pojo.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 附件管理-附件配置
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Data
@TableName("FILE_CONFIGURATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileConfigurationDO extends AbstractBaseRecordDomain {
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
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer fileNumLimit;

    /**
     * 允许上传的最大单个文件大小
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Long fileSingleSizeLimit;

    /**
     * 允许上传文件的总大小
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Long fileSizeLimit;

    /**
     * 是否启用文件加密保存
     */
    @TableField(value = "IS_ENABLE_ENCRYPT", strategy = FieldStrategy.IGNORED)
    private Integer enableEncrypt;

    /**
     * 是否允许设置文件密级
     */
    @TableField(value = "IS_ENABLE_SECRET_LEVEL", strategy = FieldStrategy.IGNORED)
    private Integer enableSecretLevel;

    /**
     * 是否允许上传
     */
    @TableField(value = "IS_ALLOW_UPLOAD", strategy = FieldStrategy.IGNORED)
    private Integer allowUpload;

    /**
     * 是否允许删除
     */
    @TableField(value = "IS_ALLOW_DELETE", strategy = FieldStrategy.IGNORED)
    private Integer allowDelete;

    /**
     * 是否允许下载
     */
    @TableField(value = "IS_ALLOW_DOWNLOAD", strategy = FieldStrategy.IGNORED)
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
    @TableField(value = "IS_ENABLE_DOWNLOAD_TOKEN", strategy = FieldStrategy.IGNORED)
    private Integer enableDownloadToken;

    /**
     * 是否禁止删除他人上传文件
     */
    @TableField(value = "IS_ENABLE_USER_SEPARATE", strategy = FieldStrategy.IGNORED)
    private Integer enableUserSeparate;

    /**
     * 指定文件存储库id
     */
    private String fileRepositoryId;

    /**
     * 指定文件存储库标识
     */
    @TableField(exist = false)
    private String repositoryKey;

    /**
     * 存储路径生成规则
     */
    private String subDirFormat;

    /**
     * 是否启用图片压缩上传
     */
    @TableField(value = "IS_ENABLE_IMAGE_COMPRESS", strategy = FieldStrategy.IGNORED)
    private Integer enableImageCompress;

    /**
     * 图片压缩宽度
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer compressedImageWidth;

    /**
     * 图片压缩高度
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer compressedImageHeight;

    /**
     * 启用图片压缩的图片大小
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer compressImageSize;

    /**
     * 是否启用文件预览
     */
    @TableField(value = "IS_ENABLE_PREVIEW", strategy = FieldStrategy.IGNORED)
    private Integer enablePreview;

    /**
     * 是否启用附件审查
     */
    @TableField(value = "IS_ENABLE_REVIEW", strategy = FieldStrategy.IGNORED)
    private Integer enableReview;

    /**
     * 数据签名
     */
    private String sign;
    /**
     * 是否启用事件文件删除前
     */
    @TableField(value = "IS_ENABLE_EVT_FILE_DELETING", strategy = FieldStrategy.IGNORED)
    private Integer enableEvtFileDeleting;
    /**
     * 是否启用事件文件下载前
     */
    @TableField(value = "IS_ENABLE_EVT_FILE_DOWNLOADING", strategy = FieldStrategy.IGNORED)
    private Integer enableEvtFileDownloading;
    /**
     * 是否启用事件文件下载后
     */
    @TableField(value = "IS_ENABLE_EVT_FILE_DOWNLOADED", strategy = FieldStrategy.IGNORED)
    private Integer enableEvtFileDownloaded;
    /**
     * 是否启用事件文件上传前
     */
    @TableField(value = "IS_ENABLE_EVT_FILE_UPLOADING", strategy = FieldStrategy.IGNORED)
    private Integer enableEvtFileUploading;
    /**
     * 是否启用事件文件上传后（不影响文件上传到服务器）
     */
    @TableField(value = "IS_ENABLE_EVT_FILE_UPLOADED", strategy = FieldStrategy.IGNORED)
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
    @TableField(value = "IS_ALLOW_DLD_WITHOUT_LOGIN", strategy = FieldStrategy.IGNORED)
    private Integer allowDownloadWithoutLogin;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 块大小
     */
    @TableField(exist = false)
    private Long chunkSize;
    /**
     * 流程标识
     */
    private String flowCode;
    /**
     * 流程名称
     */
    private String flowName;


    public String getFileConfigurationJson() {
        return JSONObject.toJSONString(toFileConfiguration());
    }

    public FileConfiguration toFileConfiguration() {
        FileConfiguration fileConfiguration = new FileConfiguration();
        fileConfiguration.setId(getId());
        fileConfiguration.setConfigurationType(getConfigurationType());
        fileConfiguration.setGroupId(getGroupId());
        fileConfiguration.setAppId(getAppId());
        fileConfiguration.setConfigurationKey(getConfigurationKey());
        fileConfiguration.setGroupDatasourceId(getGroupDatasourceId());
        fileConfiguration.setTableName(getTableName());
        fileConfiguration.setAccept(getAccept());
        fileConfiguration.setFileNumLimit(getFileNumLimit());
        fileConfiguration.setFileSingleSizeLimit(getFileSingleSizeLimit());
        fileConfiguration.setFileSizeLimit(getFileSizeLimit());
        fileConfiguration.setEnableSecretLevel(getEnableSecretLevel());
        fileConfiguration.setAllowUpload(getAllowUpload());
        fileConfiguration.setAllowDelete(getAllowDelete());
        fileConfiguration.setAllowDownload(getAllowDownload());
        fileConfiguration.setUploadAuthCode(getUploadAuthCode());
        fileConfiguration.setDeleteAuthCode(getDeleteAuthCode());
        fileConfiguration.setDownloadAuthCode(getDownloadAuthCode());
        fileConfiguration.setEnableDownloadToken(getEnableDownloadToken());
        fileConfiguration.setEnableUserSeparate(getEnableUserSeparate());
        fileConfiguration.setEnableImageCompress(getEnableImageCompress());
        fileConfiguration.setCompressedImageWidth(getCompressedImageWidth());
        fileConfiguration.setCompressedImageHeight(getCompressedImageHeight());
        fileConfiguration.setCompressImageSize(getCompressImageSize());
        fileConfiguration.setEnablePreview(getEnablePreview());
        fileConfiguration.setEnableReview(getEnableReview());
        fileConfiguration.setUploadOperationKey(getUploadOperationKey());
        fileConfiguration.setDownloadOperationKey(getDownloadOperationKey());
        fileConfiguration.setDeleteOperationKey(getDeleteOperationKey());
        fileConfiguration.setChunkSize(getChunkSize());
        fileConfiguration.setAllowDownloadWithoutLogin(getAllowDownloadWithoutLogin());
        fileConfiguration.setFlowCode(getFlowCode());
        fileConfiguration.setFlowName(getFlowName());
        return fileConfiguration;
    }
}