package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysAuthScopeAppDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统管理-有效权限-授权应用 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:15:23
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysAuthScopeAppMapper extends BaseMapper<SysAuthScopeAppDO> {

}
