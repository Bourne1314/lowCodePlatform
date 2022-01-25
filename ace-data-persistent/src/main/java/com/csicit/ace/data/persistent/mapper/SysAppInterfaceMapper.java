package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用接口信息表 数据处理层
 *
 * @author generator
 * @date 2020-06-03 09:03:59
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysAppInterfaceMapper extends BaseMapper<SysAppInterfaceDO> {

}
