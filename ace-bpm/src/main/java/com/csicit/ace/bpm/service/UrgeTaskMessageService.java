package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.vo.UrgeTaskMessageVO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UrgeTaskMessageService extends IService<UrgeTaskMessageVO> {
}