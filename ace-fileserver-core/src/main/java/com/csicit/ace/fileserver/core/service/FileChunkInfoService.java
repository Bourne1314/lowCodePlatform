package com.csicit.ace.fileserver.core.service;

import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import org.springframework.transaction.annotation.Transactional;
import com.csicit.ace.dbplus.service.IBaseService;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Transactional
public interface FileChunkInfoService extends IBaseService<FileChunkInfoDO> {
    /**
     * 根据文件id查询切片
     *
     * @param fileInfoId 文件id
     * @return java.util.List<FileChunkInfoDO> 排序后的切片
     * @author JonnyJiang
     * @date 2019/5/21 20:53
     */
    List<FileChunkInfoDO> listByFileInfoId(String fileInfoId);
}
