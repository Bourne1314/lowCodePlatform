package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.SysMsgReadDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/7/8 11:05
 */
@Transactional
public interface SysMsgReadService extends IBaseService<SysMsgReadDO> {
}
