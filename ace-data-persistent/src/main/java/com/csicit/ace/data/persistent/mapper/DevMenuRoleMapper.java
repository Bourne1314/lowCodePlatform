package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单角色关系管理 数据处理层
 *
 * @author zuog
 * @date 2019/11/25 11:09
 */
@DS("ace")
@Mapper
public interface DevMenuRoleMapper extends BaseMapper<DevMenuRoleDO> {
}
