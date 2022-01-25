package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuditLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统审计日志表 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-08 16:28:31
 */
@DS("ace")
@Mapper
public interface SysAuditLogMapper extends BaseMapper<SysAuditLogDO> {

}
