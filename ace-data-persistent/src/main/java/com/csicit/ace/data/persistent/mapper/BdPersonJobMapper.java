package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础数据-人员工作信息 数据处理层
 *
 * @author generator
 * @date 2019-04-15 17:27:00
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface BdPersonJobMapper extends BaseMapper<BdPersonJobDO> {

}
