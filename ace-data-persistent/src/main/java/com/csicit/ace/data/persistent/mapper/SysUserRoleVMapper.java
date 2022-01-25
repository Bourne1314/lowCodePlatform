package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysUserRoleVDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色历史版本数据 数据处理层
 *
 * @author generator
 * @date 2019-10-22 19:17:41
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysUserRoleVMapper extends BaseMapper<SysUserRoleVDO> {

}
