package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.OrgSalesOrgDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织-销售组织 数据处理层
 *
 * @author generator
 * @date 2019-04-15 17:17:51
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface OrgSalesOrgMapper extends BaseMapper<OrgSalesOrgDO> {

}
