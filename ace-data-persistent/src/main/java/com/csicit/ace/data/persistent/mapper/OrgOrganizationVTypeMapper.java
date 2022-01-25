package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.OrgOrganizationVTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织-组织-职能 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:16:45
 */
@DS("ace")
@Mapper
public interface OrgOrganizationVTypeMapper extends BaseMapper<OrgOrganizationVTypeDO> {

}
