package com.csicit.ace.data.persistent.service.impl;

import com.csicit.ace.common.pojo.domain.SysAuditLogCountDO;
import com.csicit.ace.data.persistent.mapper.SysAuditLogCountMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogCountService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/5/31 15:59
 */
@Service
public class SysAuditLogCountServiceImpl extends BaseServiceImpl<SysAuditLogCountMapper, SysAuditLogCountDO>
        implements SysAuditLogCountService {
}
