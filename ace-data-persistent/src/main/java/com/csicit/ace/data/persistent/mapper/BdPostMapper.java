package com.csicit.ace.data.persistent.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门岗位表 数据处理层
 *
 * @author generator
 * @date 2019-09-23 17:24:50
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface BdPostMapper extends BaseMapper<BdPostDO> {
}
