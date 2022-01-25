package com.csicit.ace.demo.controller;

import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.demo.domain.BorrowDO;
import com.csicit.ace.demo.domain.Question;
import com.csicit.ace.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shanwj
 * @date 2020/6/16 16:24
 */
@RestController
@RequestMapping("/question")
public class QuestionController {


    @Autowired
    QuestionService questionService;
    @PostMapping("/read/question")
    public void readQuestion() throws Exception {
        FileInputStream fis = new FileInputStream("d://txt//file.txt");
        InputStreamReader isr = new InputStreamReader(fis,"GB2312");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        Question question =  null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (StringUtils.isEmpty(line)){
                continue;
            }
            if (Character.isDigit(line.charAt(0))){
                question = new Question();
                int num = getNum(line);
                line = line.replace(num+".","");
                question.setNum(num);
                question.setTitle(line);
            }else if(line.startsWith("A")){
                question.setAn1(line);
            }else if(line.startsWith("B")){
                question.setAn2(line);
            }else if(line.startsWith("C")){
                question.setAn3(line);
            }else if(line.startsWith("D")){
                question.setAn4(line);
            }else if(line.startsWith("答")){
                question.setAn(line);
                questionService.save(question);
            }
        }
        br.close();
        isr.close();
        fis.close();
    }
  private static int  getNum(String str){
      int end = str.indexOf(".");
      str = str.substring(0,end);
      return Integer.parseInt(str);
  }

    @PostMapping("/handler/question")
    public void hadlerQuestion() throws Exception {
        questionService.hadlerQuestion();
    }

    @PostMapping("/style/question")
    public void styleQuestion() throws Exception {
        questionService.styleQuestion();
    }


}
