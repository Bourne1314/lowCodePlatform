package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关系表 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:49
 */
@DS("ace")
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleDO> {

}
