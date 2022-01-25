package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysWaitGrantUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色用户授予—待激活的用户表 数据处理层
 *
 * @author generator
 * @date 2019-12-12 15:06:28
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysWaitGrantUserMapper extends BaseMapper<SysWaitGrantUserDO> {

}
