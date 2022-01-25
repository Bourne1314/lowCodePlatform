package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysFormItemTipDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表单字段提示信息表 数据处理层
 *
 * @author generator
 * @date 2019-04-15 20:12:28
 * @version V1.0
 */
@DS("ace")
@Mapper
public interface SysFormItemTipMapper extends BaseMapper<SysFormItemTipDO> {

}
