package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysApiMixDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 有效API权限 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:05:05
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysApiMixMapper extends BaseMapper<SysApiMixDO> {

}
