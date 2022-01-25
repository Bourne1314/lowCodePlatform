package com.csicit.ace.demo.service.impl;

import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.demo.domain.Question;
import com.csicit.ace.demo.mapper.QuestionMapper;
import com.csicit.ace.demo.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanwj
 * @date 2020/6/16 16:22
 */
@Service
public class QuestionServiceImpl extends BaseServiceImpl<QuestionMapper,Question> implements QuestionService {
    @Override
    public void hadlerQuestion() {
        List<Question> questions = list(null);
        for (Question question:questions){
            if(StringUtils.isNotEmpty(question.getAn1())&&StringUtils.isNotEmpty(question.getAn2())
                    &&StringUtils.isNotEmpty(question.getAn3())&&StringUtils.isNotEmpty(question.getAn4())){
                continue;
            }
            String an1 = question.getAn1();
            if (StringUtils.isEmpty(question.getAn2())){
                if(StringUtils.isEmpty(question.getAn3())&&StringUtils.isEmpty(question.getAn4())){
                    String an4 = "D"+an1.split("D")[1];
                    String an3 = an1.split("C")[1];
                    an3 = "C"+an3.replace(an4,"");
                    String an2 = "B"+an1.split("B")[1];
                    an2 = an2.replace(an3+an4,"");
                    an1 = an1.replace(an2+an3+an4,"");
                    an1 = an1.trim();
                    an2 = an2.trim();
                    an3 = an3.trim();
                    an4 = an4.trim();
                    question.setAn1(an1);
                    question.setAn2(an2);
                    question.setAn3(an3);
                    question.setAn4(an4);
                }
                if (StringUtils.isNotEmpty(question.getAn3())
                        &&StringUtils.isEmpty(question.getAn2())&&StringUtils.isEmpty(question.getAn4())){

                    String an2 = "B"+an1.split("B")[1];
                    an1 = an1.replace(an2,"");

                    String an3 = question.getAn3();
          System.out.println("num:"+question.getNum());
                    String an4 = "D"+an3.split("D")[1];
                    an3 = an3.replace(an4,"");
                    an1 = an1.trim();
                    an2 = an2.trim();
                    an3 = an3.trim();
                    an4 = an4.trim();
                    question.setAn1(an1);
                    question.setAn2(an2);
                    question.setAn3(an3);
                    question.setAn4(an4);
                }
                updateById(question);
            }
        }
    }

    @Override
    public void styleQuestion() {
        List<Question> questions = list(null);
        for (Question question:questions){
            question.setTitle("["+question.getNum()+"]"+question.getTitle());
            String an1 = question.getAn1();
            String an2 = question.getAn2();
            String an3 = question.getAn3();
            String an4 = question.getAn4();
            String an = question.getAn();
            an1 = an1.startsWith("A.")|| an1.startsWith("A、")?an1.substring(2,an1.length()):an1.substring(1,an1.length());
            an2 = an2.startsWith("B.")|| an1.startsWith("B、")?an2.substring(2,an2.length()):an2.substring(1,an2.length());
            an3 = an3.startsWith("C.")|| an1.startsWith("C、")?an3.substring(2,an3.length()):an3.substring(1,an3.length());
            an4 = an4.startsWith("D.")|| an1.startsWith("D、")?an4.substring(2,an4.length()):an4.substring(1,an4.length());
            if(an.contains("A")){
                an1 = "[YES]"+an1;
            }else if (an.contains("B")){
                an2 = "[YES]"+an2;
            }else if (an.contains("C")){
                an3 = "[YES]"+an3;
            }else if (an.contains("D")){
                an4 = "[YES]"+an4;
            }
            question.setAn1(an1);
            question.setAn2(an2);
            question.setAn3(an3);
            question.setAn4(an4);
            updateById(question);
        }
    }

}
