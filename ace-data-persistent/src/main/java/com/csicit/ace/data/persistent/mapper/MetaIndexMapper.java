package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.dev.MetaIndexDO;
import org.apache.ibatis.annotations.Mapper;

@DS("ace")
@Mapper
public interface MetaIndexMapper extends BaseMapper<MetaIndexDO> {
}
