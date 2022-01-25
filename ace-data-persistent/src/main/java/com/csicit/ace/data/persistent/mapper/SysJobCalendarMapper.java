package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysJobCalendarDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作日表 数据处理层
 *
 * @author generator
 * @date 2019-08-16 08:12:02
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysJobCalendarMapper extends BaseMapper<SysJobCalendarDO> {

}
