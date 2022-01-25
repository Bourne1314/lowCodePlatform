package com.csicit.ace.fileserver.core.service;

import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.vo.FileVO;
import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Transactional
public interface FileRepositoryService extends IBaseService<FileRepositoryDO> {
    /**
     * 分配存储空间
     *
     * @param fileConfiguration 附件配置
     * @param file              文件信息
     * @return FileInfoDO
     * @author JonnyJiang
     * @date 2019/5/22 19:05
     */

    FileInfoDO allocateSpace(FileConfigurationDO fileConfiguration, FileVO file);

    /**
     * 更新已使用空间
     *
     * @param fileRepositoryId 存储库id
     * @param size             使用空间大小
     * @return
     */
    Boolean updateUsedSizeById(String fileRepositoryId, Long size);

    /**
     * 根据文件信息获取文件存储库
     *
     * @param fileInfo 文件信息
     * @return 文件存储库
     */
    FileRepositoryDO getByFileInfo(FileInfoDO fileInfo);

    /**
     * 释放已使用空间
     *
     * @param fileRepositoryId 存储库id
     * @param size             释放空间大小
     * @return void
     * @author JonnyJiang
     * @date 2019/6/5 8:19
     */

    void releaseSpace(String fileRepositoryId, Long size);

    /**
     * 保存文件存储库
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(FileRepositoryDO instance);

    /**
     * 更新文件存储库
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(FileRepositoryDO instance);

    /**
     * 删除文件存储库
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(String[] ids);

    /**
     * 根据指定文件大小，获取加密后的文件大小和分片数
     *
     * @param enableEncrypt 是否加密
     * @param size          原始大小
     * @param chunkSize     块大小
     * @return java.lang.Long[]  索引0值为encryptedSize，索引1值为chunks
     * @author JonnyJiang
     * @date 2019/8/5 11:08
     */

    Long[] getEncryptedSizeAndChunks(Integer enableEncrypt, Long size, Long chunkSize);
}
