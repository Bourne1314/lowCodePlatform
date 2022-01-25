package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAppMenuDisplayDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用下菜单显示在平台管控台的信息表 数据处理层
 *
 * @author generator
 * @date 2019-12-09 16:06:18
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysAppMenuDisplayMapper extends BaseMapper<SysAppMenuDisplayDO> {

}
