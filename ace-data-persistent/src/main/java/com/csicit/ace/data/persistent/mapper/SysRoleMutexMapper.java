package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysRoleMutexDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色互斥关系 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:18
 */
@DS("ace")
@Mapper
public interface SysRoleMutexMapper extends BaseMapper<SysRoleMutexDO> {

}
