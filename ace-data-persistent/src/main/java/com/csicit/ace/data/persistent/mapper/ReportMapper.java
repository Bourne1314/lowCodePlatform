package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2019/12/12 11:47
 */
@DS("ace")
@Mapper
public interface ReportMapper extends BaseMapper<ReportInfoDO> {
}
