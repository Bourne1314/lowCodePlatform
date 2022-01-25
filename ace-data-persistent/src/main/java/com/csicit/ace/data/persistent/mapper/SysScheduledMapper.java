package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 11:08
 */
@DS("ace")
@Mapper
public interface SysScheduledMapper extends BaseMapper<QrtzConfigDO> {
}
