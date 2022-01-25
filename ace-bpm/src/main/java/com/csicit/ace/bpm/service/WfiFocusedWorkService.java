package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.domain.WfiFocusedWorkDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/5/26 17:08
 */
@Transactional
public interface WfiFocusedWorkService extends IService<WfiFocusedWorkDO> {
}
