package com.csicit.ace.bpm.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.domain.SysReviewFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Component
public interface SysReviewFileService extends IService<SysReviewFile> {
}
