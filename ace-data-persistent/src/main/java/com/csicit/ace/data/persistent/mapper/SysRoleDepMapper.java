package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysRoleDepDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/4/21 8:52
 */
@DS("ace")
@Mapper
public interface SysRoleDepMapper extends BaseMapper<SysRoleDepDO> {
}
