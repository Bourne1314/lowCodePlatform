package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口sql入参信息表 数据处理层
 *
 * @author generator
 * @date 2020-06-03 09:05:20
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysAppInterfaceInputMapper extends BaseMapper<SysAppInterfaceInputDO> {

}
