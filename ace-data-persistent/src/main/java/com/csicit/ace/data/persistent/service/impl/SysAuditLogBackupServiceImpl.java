package com.csicit.ace.data.persistent.service.impl;


import com.csicit.ace.common.pojo.domain.SysAuditLogBackupDO;
import com.csicit.ace.data.persistent.mapper.SysAuditLogBackupMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogBackupService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/5/31 15:00
 */
@Service
public class SysAuditLogBackupServiceImpl extends BaseServiceImpl<SysAuditLogBackupMapper,SysAuditLogBackupDO>
        implements SysAuditLogBackupService {
}
