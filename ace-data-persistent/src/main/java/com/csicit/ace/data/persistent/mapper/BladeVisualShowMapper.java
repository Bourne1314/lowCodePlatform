package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BladeVisualShowDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 大屏展示 数据处理层
 *
 * @author generator
 * @date 2020-06-05 10:05:48
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface BladeVisualShowMapper extends BaseMapper<BladeVisualShowDO> {

}
