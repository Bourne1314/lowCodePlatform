package com.csicit.ace.common.pojo.domain;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.IntegerUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 附件管理-附件配置
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Data
@TableName("FILE_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfoDO extends AbstractBaseDomain {
    /**
     * 附件配置id
     */
    private String fileConfigurationId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 关联表记录
     */
    private String formId;

    /**
     * 上传人id
     */
    private String uploaderId;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTime;

    /**
     * 文件密级
     */
    private Integer secretLevel;

    /**
     * 文件md5
     */
    private String md5;

    /**
     * 文件内容类型MIME
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件加密后大小
     */
    private Long encryptedSize;

    /**
     * 文件切片数量
     */
    private Long chunks;

    /**
     * 共享文件id
     */
    private String sharedFileInfoId;

    /**
     * 图片旋转角度
     */
    private Integer imageRotationAngle;

    /**
     * 是否需要审查
     */
    @TableField(value = "IS_NEED_REVIEW")
    private Integer needReview;

    /**
     * 附件存储库id
     */
    private String fileRepositoryId;

    /**
     * 是否在回收站
     */
    @TableField(value = "IS_IN_RECYCLE_BIN")
    private Integer inRecycleBin;

    /**
     * 回收时间
     */
    private LocalDateTime recycleTime;

    /**
     * 回收人
     */
    private String recyclerId;

    /**
     * 旧表单的Id
     */
    private String oldFormId;


    /**
     * 获取文件信息JSON
     * @param fileConfiguration 附件配置
     * @param includeActualFileId 是否包含真实文件ID
     * @return 文件信息JSON
     * @author JonnyJiang
     * @date 2021/5/24 16:23
     */

    public String getFileInfoJson(FileConfigurationDO fileConfiguration, Boolean includeActualFileId) {
        FileInfo fileInfo = toFileInfo(fileConfiguration);
        if(includeActualFileId)
        {
            fileInfo.setActualFileId(getId());
        }
        return JSONObject.toJSONString(fileInfo);
    }

    /**
     * 获取文件信息
     * @param fileConfiguration 附件配置
     * @return 文件信息
     * @author JonnyJiang
     * @date 2021/5/24 16:23
     */

    public FileInfo toFileInfo(FileConfigurationDO fileConfiguration) {
        FileInfo fileInfo = new FileInfo();
        if (IntegerUtils.isTrue(fileConfiguration.getEnableDownloadToken())) {
            CacheUtil cacheUtil = SpringContextUtils.getBean(CacheUtil.class);
            String fileToken = UUID.randomUUID().toString();
            cacheUtil.set(fileToken, getId());
            fileInfo.setId(fileToken);
        } else {
            fileInfo.setId(getId());
        }
        fileInfo.setFileConfigurationId(getFileConfigurationId());
        fileInfo.setFileName(getFileName());
        fileInfo.setFormId(getFormId());
        fileInfo.setUploaderId(getUploaderId());
        fileInfo.setUploadTime(getUploadTime());
        fileInfo.setSecretLevel(getSecretLevel());
        fileInfo.setContentType(getContentType());
        fileInfo.setFileSize(getFileSize());
        fileInfo.setNeedReview(getNeedReview());
        fileInfo.setOldFormId(getOldFormId());
        return fileInfo;
    }
}