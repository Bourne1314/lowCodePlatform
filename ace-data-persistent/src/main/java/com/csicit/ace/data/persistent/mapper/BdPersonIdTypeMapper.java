package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础数据-人员证件类型 数据处理层
 *
 * @author generator
 * @date 2019-04-15 17:27:06
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface BdPersonIdTypeMapper extends BaseMapper<BdPersonIdTypeDO> {

}
