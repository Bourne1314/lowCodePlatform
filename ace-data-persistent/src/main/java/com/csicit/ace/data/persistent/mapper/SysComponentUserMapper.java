package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysComponentUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 普通用户登录应用首页组件排版信息 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:44
 */

@DS("ace")
@Mapper
public interface SysComponentUserMapper extends BaseMapper<SysComponentUserDO> {

}
