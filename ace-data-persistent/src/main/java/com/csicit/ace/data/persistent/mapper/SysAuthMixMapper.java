package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统管理-用户有效权限 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:16
 */
@DS("ace")
@Mapper
public interface SysAuthMixMapper extends BaseMapper<SysAuthMixDO> {


}
