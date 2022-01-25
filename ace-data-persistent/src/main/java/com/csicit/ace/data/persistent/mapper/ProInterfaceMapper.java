package com.csicit.ace.data.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.dev.ProInterfaceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实体模型 数据处理层
 *
 * @author zuog
 * @date 2019/11/25 11:09
 */
@Mapper
public interface ProInterfaceMapper extends BaseMapper<ProInterfaceDO> {
}
