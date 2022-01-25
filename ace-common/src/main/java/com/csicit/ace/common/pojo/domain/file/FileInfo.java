package com.csicit.ace.common.pojo.domain.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息
 *
 * @author JonnyJiang
 * @date 2019/10/15 18:17
 */
@Data
public class FileInfo implements Serializable {
    /**
     * 主键
     */
    private String id;
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
     * 文件内容类型MIME
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 是否需要审查
     */
    private Integer needReview;
    /**
     * 真实文件ID，仅在流程事件中有值
     */
    private String actualFileId;
    /**
     * 流程标识
     */
    private String oldFormId;
}
