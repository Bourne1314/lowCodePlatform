package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysUserAdminOrgDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统管理-用户-可管理的组织 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:14:22
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysUserAdminOrgMapper extends BaseMapper<SysUserAdminOrgDO> {

}
