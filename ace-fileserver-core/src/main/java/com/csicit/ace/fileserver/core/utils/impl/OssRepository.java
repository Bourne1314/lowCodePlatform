package com.csicit.ace.fileserver.core.utils.impl;

import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.fileserver.core.UploadExpcetion;
import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.utils.AbstractFileRepository;
import com.csicit.ace.fileserver.core.utils.OssProvider;

import java.io.InputStream;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
public class OssRepository extends AbstractFileRepository {

    private OssProvider ossProvider;

    public OssRepository(FileRepositoryService fileRepositoryService, String fileRepositoryId, String ossProvider, String providerName, String ossEndpoint, String ossSecret, String ossKey, String buket) {
        super(fileRepositoryService, fileRepositoryId);
        switch (ossProvider) {
            case "Aliyun":
                this.ossProvider = new AliyunOssProvider(providerName, ossEndpoint, ossSecret, ossKey, buket);
                break;
            case "Huawei":
                this.ossProvider = new HuaweiOssProvider(providerName, ossEndpoint, ossSecret, ossKey, buket);
                break;
            case "Tencent":
                this.ossProvider = new TencentOssProvider(providerName, ossEndpoint, ossSecret, ossKey, buket);
                break;
            default:
                throw new UploadExpcetion("不支持的存储类型！");
        }
    }

    @Override
    public String transformFilePath(String filePath) {
        return null;
    }

    @Override
    public Boolean checkPhysicalStorage(Long size) {
        return false;
    }

    @Override
    public Long encryptSave(byte[] key, String chunkPath, InputStream inputStream) {
        return 0L;
    }

    @Override
    public void normalSave(String chunkPath, InputStream fileStream) {

    }

    @Override
    protected InputStream getChunkInputStream(FileChunkInfoDO chunkInfo) {
        return null;
    }

    @Override
    protected void createDirectory(String path) {

    }

    @Override
    protected void deleteChunk(FileChunkInfoDO chunkInfo) {

    }
}
