package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/16 8:23
 */
@DS("ace")
@Mapper
public interface SysAuthMixMapperF extends BaseMapper<SysAuthMixDO> {
}
