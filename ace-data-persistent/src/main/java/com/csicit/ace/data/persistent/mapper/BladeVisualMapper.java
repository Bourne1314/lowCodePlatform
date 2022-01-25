package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 大屏信息数据表 数据处理层
 *
 * @author generator
 * @date 2020-06-05 09:59:54
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface BladeVisualMapper extends BaseMapper<BladeVisualDO> {

}
