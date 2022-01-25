package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.FileReviewDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@DS("ace")
@Mapper
public interface FileReviewMapper extends BaseMapper<FileReviewDO> {
}
