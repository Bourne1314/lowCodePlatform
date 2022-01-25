package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.CodeSequenceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2020/5/22 15:16
 */
@DS("ace")
@Mapper
public interface CodeSequenceMapper extends BaseMapper<CodeSequenceDO> {
}
