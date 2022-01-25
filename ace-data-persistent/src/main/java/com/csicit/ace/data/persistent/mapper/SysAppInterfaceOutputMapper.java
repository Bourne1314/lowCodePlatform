package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceOutputDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口sql出参信息表 数据处理层
 *
 * @author generator
 * @date 2020-06-03 09:05:33
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysAppInterfaceOutputMapper extends BaseMapper<SysAppInterfaceOutputDO> {

}
