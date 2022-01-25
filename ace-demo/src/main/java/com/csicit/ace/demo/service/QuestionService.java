package com.csicit.ace.demo.service;

import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.demo.domain.Question;

/**
 * @author shanwj
 * @date 2020/6/16 16:21
 */
public interface QuestionService extends IBaseService<Question> {

    void hadlerQuestion();

    void styleQuestion();
}
