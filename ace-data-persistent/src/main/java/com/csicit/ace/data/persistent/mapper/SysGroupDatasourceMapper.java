package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用绑定数据源 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:12:31
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysGroupDatasourceMapper extends BaseMapper<SysGroupDatasourceDO> {

}
