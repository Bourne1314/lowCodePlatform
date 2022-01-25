package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuditLogBackupDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2019/5/31 14:59
 */
@DS("ace")
@Mapper
public interface SysAuditLogBackupMapper extends BaseMapper<SysAuditLogBackupDO> {
}
