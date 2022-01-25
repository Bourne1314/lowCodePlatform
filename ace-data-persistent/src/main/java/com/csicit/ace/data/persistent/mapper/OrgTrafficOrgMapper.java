package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.OrgTrafficOrgDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织-物流组织 数据处理层
 *
 * @author generator
 * @date 2019-04-15 17:18:12
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface OrgTrafficOrgMapper extends BaseMapper<OrgTrafficOrgDO> {

}
