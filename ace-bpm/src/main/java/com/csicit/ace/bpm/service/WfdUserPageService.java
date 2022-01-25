package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.domain.WfdUserPageDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 8:42
 */
@Transactional
public interface WfdUserPageService extends IService<WfdUserPageDO> {
}
