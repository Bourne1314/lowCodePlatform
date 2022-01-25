package com.csicit.ace.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/7/24 13:45
 */
@Data
public class FileVO implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 平台生成的文件id
     */
    private String yfId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 关联表记录
     */
    private String formId;

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
     * 文件切片数量
     */
    private Integer chunks;

    /**
     * 附件配置标识
     */
    private String configurationKey;

    /**
     * 引用标识
     */
    private String appName;
}
