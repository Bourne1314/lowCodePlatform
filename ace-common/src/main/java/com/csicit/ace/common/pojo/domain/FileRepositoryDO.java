package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 附件管理-文件存储库
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Data
@TableName("FILE_REPOSITORY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileRepositoryDO extends AbstractBaseRecordDomain {
    /**
     * 存储库标识
     */
    private String repositoryKey;

    /**
     * 存储库类型（0-文件，1-OSS，2-FTP）
     */
    private Integer repositoryType;

    /**
     * 存储库最大容量
     */
    private Long maxSize;

    /**
     * 已占用空间
     */
    private Long usedSize;

    /**
     * 存储空间不足预警阀值
     */
    private Long warningSize;

    /**
     * 存储根路径
     */
    private String storageBaseDirectory;

    /**
     * OSS提供程序
     */
    private String ossProvider;

    /**
     * FTP服务器
     */
    private String ftpServerAddress;

    /**
     * FTP登录用户名
     */
    private String ftpUserName;

    /**
     * FTP登录密码
     */
    private String ftpPassword;
}
