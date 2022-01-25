package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.SysAuditLogBackupDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/5/31 15:00
 */
@Transactional
public interface SysAuditLogBackupService extends IBaseService<SysAuditLogBackupDO> {
}
