package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysPasswordPolicyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 密码安全策略表 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:12:41
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysPasswordPolicyMapper extends BaseMapper<SysPasswordPolicyDO> {

}
