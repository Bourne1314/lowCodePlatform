package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysMsgReadDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2019/7/8 11:03
 */
@DS("ace")
@Mapper
public interface SysMsgReadMapper extends BaseMapper<SysMsgReadDO> {
}
