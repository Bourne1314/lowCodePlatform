package com.csicit.ace.webservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职务称谓表 数据处理层
 * @author wangzimin
 * @version V1.0
 * @date 2019/9/28 8:59
 */
@Mapper
public interface BdJobMappper extends BaseMapper<BdJobDO> {
}
