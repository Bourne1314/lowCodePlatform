package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysWaitGrantAuthDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待激活的用户或角色表 数据处理层
 *
 * @author generator
 * @date 2019-07-05 17:37:22
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysWaitGrantAuthMapper extends BaseMapper<SysWaitGrantAuthDO> {

}
