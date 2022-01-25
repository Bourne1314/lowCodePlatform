package com.csicit.ace.fileserver.core.service.impl;

import com.csicit.ace.common.pojo.domain.FileReviewDO;
import com.csicit.ace.data.persistent.mapper.FileReviewMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.fileserver.core.service.FileReviewService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author JR-zhangzhaojun
 * @DATE 2021/11/19
 * @Param
 * @return
 * @Version 1.0
 */
@Service
public class FileReviewServiceImpl extends BaseServiceImpl<FileReviewMapper, FileReviewDO> implements FileReviewService {
}
