package com.csicit.ace.fileserver.core.service.impl;

import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysConfigServiceD;
import com.csicit.ace.fileserver.core.ServerException;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;

/**
 * 配置名称枚举
 *
 * @author JonnyJiang
 * @date 2019/8/5 15:02
 */
public enum ConfigEnum {

    /**
     * 是否启用文件加密保存
     */
    ENABLE_ENCRYPT("File.EnableEncrypt", "1"),

    /**
     * 是否允许设置文件密级
     */
    ENABLE_SECRET_LEVEL("File.EnableSecretLevel", "1"),

    /**
     * 是否允许上传
     */
    ALLOW_UPLOAD("File.AllowUpload", "1"),

    /**
     * 是否允许删除
     */
    ALLOW_DELETE("File.AllowDelete", "1"),

    /**
     * 是否允许下载
     */
    ALLOW_DOWNLOAD("File.AllowDownload", "1"),

    /**
     * 是否通过临时Token下载
     */
    ENABLE_DOWNLOAD_TOKEN("File.EnableDownloadToken", "1"),

    /**
     * 是否禁止删除他人上传文件
     */
    ENABLE_USER_SEPARATE("File.EnableUserSeparate", "0"),

    /**
     * 是否启用图片压缩
     */
    ENABLE_IMAGE_COMPRESS("File.EnableImageCompress", "0"),

    /**
     * 是否启用文件预览
     */
    ENABLE_PREVIEW("File.EnablePreview", "1"),

    /**
     * 是否启用附件审查
     */
    ENABLE_REVIEW("File.EnableReview", "0"),

    /**
     * 关联数据源id
     */
    GROUP_DATASOURCE_ID("File.GroupDatasourceId", null),

    /**
     * 允许上传的文件类型（例：png,docx）
     */
    ACCEPT("File.Accept", null),

    /**
     * 允许上传文件的最大数量
     */
    FILE_NUM_LIMIT("File.FileNumLimit", null),

    /**
     * 允许上传文件的总大小
     */
    FILE_SIZE_LIMIT("File.FileSizeLimit", null),

    /**
     * 允许上传的最大单个文件大小
     */
    FILE_SINGLE_SIZE_LIMIT("File.FileSingleSizeLimit", null),

    /**
     * 文件存储库id
     */
    FILE_REPOSITORY_ID("File.FileRepositoryId", null),

    /**
     * 存储路径生成规则
     */
    SUB_DIR_FORMAT("File.SubDirFormat", ""),

    /**
     * 是否启用事件文件上传前
     */
    ENABLE_EVT_FILE_UPLOADING("File.EnableEvtFileUploading", "0"),

    /**
     * 是否启用事件文件上传后
     */
    ENABLE_EVT_FILE_UPLOADED("File.EnableEvtFileUploaded", "0"),

    /**
     * 是否启用事件文件下载前
     */
    ENABLE_EVT_FILE_DOWNLOADING("File.EnableEvtFileDownloading", "0"),

    /**
     * 是否启用事件文件下载后
     */
    ENABLE_EVT_FILE_DOWNLOADED("File.EnableEvtFileDownloaded", "0"),

    /**
     * 是否启用事件文件删除前
     */
    ENABLE_EVT_FILE_DELETING("File.EnableEvtFileDeleting", "0"),

    /**
     * 是否允许不登录下载
     */
    ALLOW_DOWNLOAD_WITHOUT_LOGIN("File.AllowDownloadWithoutLogin", "0"),

    /**
     * 块大小
     */
    CHUNK_SIZE("File.ChunkSize", "10485760");

    /**
     * 系统配置项名称
     */
    private String name;
    private String defaultValue;
    private static final SysConfigServiceD sysConfigServiceD = SpringContextUtils.getBean(SysConfigServiceD.class);
    private static final MultipartProperties multipartProperties = SpringContextUtils.getBean(MultipartProperties.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigEnum.class);
    private SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);

    ConfigEnum(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取系统配置项值
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/8/5 15:09
     */

    public String getValue(String appId) {
        if (ConfigEnum.CHUNK_SIZE.equals(this)) {
            Long maxFileSize = multipartProperties.getMaxFileSize().toBytes();
            Long dv = Long.valueOf(getDefaultValue());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("MAX_FILE_SIZE: " + maxFileSize);
                LOGGER.debug("MAX_FILE_SIZE: " + dv);
            }
            if (NumberUtils.compare(maxFileSize, dv) < 0) {
                throw new ServerException(LocaleUtils.getChunkSizeExceeded(dv, maxFileSize));
            }
        }
        String val = sysConfigServiceD.getValue(appId, name);
        if (StringUtils.isEmpty(val)) {
            val = getDefaultValue();
        }
        return val;
    }

    /**
     * 获取缺省值
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/24 8:10
     */

    public String getDefaultValue() {
        return defaultValue;
    }
}