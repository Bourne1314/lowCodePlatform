package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 附件管理-切片信息
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Data
@TableName("FILE_CHUNK_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileChunkInfoDO {
    /**
     * 切片id
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 所属文件id
     */
    private String fileInfoId;

    /**
     * 切片路径
     */
    private String chunkPath;

    /**
     * 切片大小
     */
    private Long chunkSize;

    /**
     * 切片序号
     */
    private Integer chunkIndex;

    /**
     * 是否加密
     */
    @TableField(value = "IS_ENCRYPTED")
    private Integer encrypted;

    /**
     * 加密后大小
     */
    private Long encryptedSize;
}