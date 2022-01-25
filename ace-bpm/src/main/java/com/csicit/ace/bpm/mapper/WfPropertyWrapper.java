package com.csicit.ace.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.bpm.pojo.domain.WfPropertyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程实例
 *
 * @author JonnyJiang
 * @date 2019/9/5 19:01
 */
@Mapper
public interface WfPropertyWrapper extends BaseMapper<WfPropertyDO> {
}