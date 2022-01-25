package com.csicit.ace.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.demo.domain.Question;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanwj
 * @date 2020/6/16 16:20
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
