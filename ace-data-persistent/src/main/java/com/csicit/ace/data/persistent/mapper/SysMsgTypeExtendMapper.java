package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2019/9/5 11:13
 */
@DS("ace")
@Mapper
public interface SysMsgTypeExtendMapper extends BaseMapper<SysMsgTypeExtendDO> {
}
