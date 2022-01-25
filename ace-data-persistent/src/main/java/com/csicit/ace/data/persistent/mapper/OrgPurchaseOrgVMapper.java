package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.OrgPurchaseOrgVDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织-采购组织版本 数据处理层
 *
 * @author generator
 * @date 2019-04-15 17:17:45
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface OrgPurchaseOrgVMapper extends BaseMapper<OrgPurchaseOrgVDO> {

}
