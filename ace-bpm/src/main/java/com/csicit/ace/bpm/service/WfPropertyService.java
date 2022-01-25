package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfPropertyDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2019/9/5 17:59
 */
@Transactional
public interface WfPropertyService extends IBaseService<WfPropertyDO> {
}
