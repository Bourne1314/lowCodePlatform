package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 批处理任务配置 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */
@DS("ace")
@Mapper
public interface QrtzConfigMapper extends BaseMapper<QrtzConfigDO> {

}
