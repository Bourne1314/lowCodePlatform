package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.dev.MetaTableColDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据列 数据处理层
 *
 * @author shanwj
 * @date 2019-11-04 14:49:22
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface MetaTableColMapper extends BaseMapper<MetaTableColDO> {

}
