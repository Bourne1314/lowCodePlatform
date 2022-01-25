package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 集团应用库 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:12:24
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysGroupAppMapper extends BaseMapper<SysGroupAppDO> {

}
