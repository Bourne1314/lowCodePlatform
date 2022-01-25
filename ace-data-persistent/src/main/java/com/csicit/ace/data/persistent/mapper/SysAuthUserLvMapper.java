package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuthUserLvDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统管理-用户授权版本控制 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-05 14:16:35
 */
@DS("ace")
@Mapper
public interface SysAuthUserLvMapper extends BaseMapper<SysAuthUserLvDO> {

}
