package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表类别 数据处理层
 *
 * @author generator
 * @date 2019-08-07 08:52:49
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface ReportTypeMapper extends BaseMapper<ReportTypeDO> {

}
