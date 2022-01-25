package com.csicit.ace.common.utils;

import java.util.Objects;
import java.util.Random;

/**
 * @author shanwj
 * @date 2019/6/3 19:55
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 删除字符串头尾的某个字符
     * @param str 字符串
     * @param c 字符
     * @return java.lang.String
     * @author shanwj
     * @date 2019/11/5 8:22
     */
    public static String trimChar(String str,char c){
        char[] chars = str.toCharArray();
        int sIndex = 0;
        int eIndex = chars.length-1;
        while (sIndex<chars.length){
            if(!Objects.equals(chars[sIndex],c)){
                break;
            }
            sIndex++;
        }

        while(eIndex >= 0){
            if (!Objects.equals(chars[eIndex],c)) {
                break;
            }
            eIndex--;
        }
        return str.substring(sIndex,eIndex+1);
    }

    /**
     * 字符串超出指定长度后在中间截取字符串替换为随机字符串
     *
     * @param str
     * @param length
     * @return
     */
    public static String replaceStrMid(String str,int length){
        if(str.length()>length){
            int size = str.length() - length + 1;
            int begin = (str.length()-size)/ 2;
            int end = (str.length()-size) - begin;
            int num = new Random().nextInt(10);
            str = str.replaceAll("(?<=.{"+begin+"}).*(?=.{"+end+"})",num+"");
            str = str.replace(num+""+num,num+"");
        }
        return str;
    }
}
