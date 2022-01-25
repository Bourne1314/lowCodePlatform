package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.OrgCorporationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织-公司组织 数据处理层
 *
 * @author generator
 * @date 2019-04-16 15:31:10
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface OrgCorporationMapper extends BaseMapper<OrgCorporationDO> {

}
