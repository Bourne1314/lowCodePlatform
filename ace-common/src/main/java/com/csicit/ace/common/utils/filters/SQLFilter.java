package com.csicit.ace.common.utils.filters;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.exception.RException;
import org.apache.commons.lang3.StringUtils;

/**
 * SQL过滤器
 *
 * @author yansiyang
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
public class SQLFilter {

    /**
     *  SQL注入过滤
     * @param str	待验证的字符串
     * @return java.lang.String
     * @author yansiyang
     * @date 2019-03-29 10:37:46
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new RException("包含非法字符");
            }
        }

        return str;
    }
}
