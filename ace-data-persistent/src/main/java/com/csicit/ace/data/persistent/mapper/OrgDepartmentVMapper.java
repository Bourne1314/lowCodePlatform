package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.OrgDepartmentVDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织-部门版本 数据处理层
 *
 * @author yansiyang
 * @date 2019-04-15 15:20:43
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface OrgDepartmentVMapper extends BaseMapper<OrgDepartmentVDO> {

}
