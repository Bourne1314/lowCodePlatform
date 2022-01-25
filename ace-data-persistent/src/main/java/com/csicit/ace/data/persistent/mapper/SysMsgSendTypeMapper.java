package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysMsgSendTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2019/8/26 10:08
 */
@DS("ace")
@Mapper
public interface SysMsgSendTypeMapper extends BaseMapper<SysMsgSendTypeDO> {
}
