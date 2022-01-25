package com.csicit.ace.bpm.utils;

import com.csicit.ace.bpm.pojo.vo.wfd.Variant;
import com.csicit.ace.common.config.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 14:10
 */
@Component
public class ExpressionUtils {

    /**
     * 解析表达式  支持表达式嵌套
     * json为对象
     *
     * @param str
     * @return String
     * @author yansiyang
     * @date 2019/8/23 14:14
     */
    public String parseExpression(String str) {
        EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
        List<String> serviceNames = getServiceNames(str);
        serviceNames.stream().forEach(serviceName -> {
            context.setVariable(serviceName, SpringContextUtils.getBean
                    (serviceName));
        });

        ExpressionParser parser = new SpelExpressionParser();
        String result = parser.parseExpression(str).getValue(context, String.class);
        return result;
    }


    /**
     * 获取表达式中所有的bean名称
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/11/27 14:52
     */
    private List<String> getServiceNames(String str) {
        List<String> serviceNames = new ArrayList<>();

        while (str.indexOf("#") >= 0) {
            int begin = str.indexOf("#");
            int end = str.indexOf("Service") + 7;
            String serviceName = str.substring(begin + 1, end);
            serviceNames.add(serviceName);
            str = str.substring(end);
        }

        return serviceNames;
    }

    /**
     * 去除所有空格 换行符等
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/28 15:07
     */
    public static String replaceAllBlank(String str) {
        String s = "";
        if (str != null) {
            //Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Pattern p = Pattern.compile("\t|\r|\n");
            /**\n 回车(\u000a)
             \t 水平制表符(\u0009)
             \s 空格(\u0008)     去除
             \r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }

    /**
     * 获取表达式的值
     * @param variant
     * @return 
     * @author FourLeaves
     * @date 2020/4/27 16:23
     */
    public String getValue(Variant variant) {
        String el = variant.getValueExpression();
        if (StringUtils.isBlank(el)) {
            return variant.getDefaultValue();
        }
        return parseExpression(el);
    }
}
