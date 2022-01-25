package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统管理-角色授权版本历史数据 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:24
 */
@DS("ace")
@Mapper
public interface SysAuthRoleVMapper extends BaseMapper<SysAuthRoleVDO> {

}
