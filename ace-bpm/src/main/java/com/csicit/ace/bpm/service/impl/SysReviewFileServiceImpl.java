package com.csicit.ace.bpm.service.impl;

import com.csicit.ace.bpm.mapper.SysReviewFileMapper;
import com.csicit.ace.bpm.pojo.domain.SysReviewFile;
import com.csicit.ace.bpm.service.SysReviewFileService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("reviewFileService")
@Transactional
public class SysReviewFileServiceImpl extends BaseServiceImpl<SysReviewFileMapper, SysReviewFile> implements SysReviewFileService {


}
