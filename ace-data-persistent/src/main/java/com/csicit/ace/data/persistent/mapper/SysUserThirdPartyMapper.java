package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysUserThirdPartyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/25 15:25
 */
@DS("ace")
@Mapper
public interface SysUserThirdPartyMapper extends BaseMapper<SysUserThirdPartyDO> {
}
