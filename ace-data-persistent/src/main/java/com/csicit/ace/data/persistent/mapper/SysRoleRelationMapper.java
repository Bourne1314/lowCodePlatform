package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色上下级关系 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:22
 */
@DS("ace")
@Mapper
public interface SysRoleRelationMapper extends BaseMapper<SysRoleRelationDO> {

}
