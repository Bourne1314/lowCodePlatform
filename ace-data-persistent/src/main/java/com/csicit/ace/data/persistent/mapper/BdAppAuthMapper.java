package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BdAppAuthDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础应用权限 数据处理层
 *
 * @author generator
 * @date 2019-04-15 17:24:44
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface BdAppAuthMapper extends BaseMapper<BdAppAuthDO> {

}
