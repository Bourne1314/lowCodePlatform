package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.CodeTemplatePartDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 编码部件
 *
 * @author shanwj
 * @date 2020/5/22 14:44
 */
@DS("ace")
@Mapper
public interface CodeTemplatePartMapper extends BaseMapper<CodeTemplatePartDO> {
}
