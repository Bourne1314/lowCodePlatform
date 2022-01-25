package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2020/4/7 9:36
 */
@DS("ace")
@Mapper
public interface SysMsgTemplateConfigMapper extends BaseMapper<SysMsgTemplateConfigDO> {
}
