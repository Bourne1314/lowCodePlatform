package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置项 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:09:53
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfigDO> {

}
