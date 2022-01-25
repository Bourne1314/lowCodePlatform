package com.csicit.ace.common.pojo.domain.file;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2019/10/15 18:17
 */
@Data
public class FileConfiguration implements Serializable {
    /**
     * 主键
     */
    private String id;

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
     * 块大小
     */
    private Long chunkSize;

    /**
     * 是否允许不登录下载
     */
    private Integer allowDownloadWithoutLogin;
    /**
     * 流程标识
     */
    private String flowCode;
    /**
     * 流程名称
     */
    private String flowName;
}
