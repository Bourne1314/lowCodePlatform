package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.SysAuditLogCountDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/5/31 15:59
 */
@Transactional
public interface SysAuditLogCountService extends IBaseService<SysAuditLogCountDO> {
}
