package com.csicit.ace.fileserver.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import com.csicit.ace.data.persistent.mapper.FileChunkInfoMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.fileserver.core.service.FileChunkInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Service
public class FileChunkInfoServiceImpl extends BaseServiceImpl<FileChunkInfoMapper, FileChunkInfoDO> implements FileChunkInfoService {
    @Override
    public List<FileChunkInfoDO> listByFileInfoId(String fileInfoId) {
        return baseMapper.selectList(new QueryWrapper<FileChunkInfoDO>().eq("FILE_INFO_ID", fileInfoId).orderByAsc("CHUNK_INDEX"));
    }
}
