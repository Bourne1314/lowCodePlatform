package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysMicroAppDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2020/4/7 9:35
 */
@DS("ace")
@Mapper
public interface SysMicroAppMapper extends BaseMapper<SysMicroAppDO> {

}
