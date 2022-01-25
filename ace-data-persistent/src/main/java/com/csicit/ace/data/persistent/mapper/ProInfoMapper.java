package com.csicit.ace.data.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.dev.ProInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目管理 数据处理层
 *
 * @author shanwj
 * @date 2019/11/25 11:09
 */
@Mapper
public interface ProInfoMapper extends BaseMapper<ProInfoDO> {
}
