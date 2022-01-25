package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysCacheDataDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/11/16 14:59
 */
@DS("ace")
@Mapper
public interface SysCacheDataMapper extends BaseMapper<SysCacheDataDO> {
}
