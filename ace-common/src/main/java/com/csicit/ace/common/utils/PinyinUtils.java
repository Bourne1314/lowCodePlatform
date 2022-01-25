package com.csicit.ace.common.utils;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.exception.RException;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.StringJoiner;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/14 17:22
 */
public class PinyinUtils {

    /**
     * 将文字转为汉语拼音
     *
     * @param chineseLanguage 要转成拼音的中文
     * @return
     * @author yansiyang
     * @date 2019/5/14 17:28
     */
    public static String toHanyuPinyin(String chineseLanguage) {
        char[] cl_chars = chineseLanguage.trim().toCharArray();
        StringJoiner joiner = new StringJoiner("");
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音
                    joiner.add(PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0]);
                } else {
                    // 如果字符不是中文,则不转换
                    joiner.add(String.valueOf(cl_chars[i]));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new RException("转化汉语拼音失败");
        }
        return joiner.toString();
    }

    public static String getFirstLettersUp(String ChineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String ChineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.LOWERCASE);
    }

    public static String getFirstLetters(String ChineseLanguage, HanyuPinyinCaseType caseType) throws
            BadHanyuPinyinOutputFormatCombination {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        StringJoiner joiner = new StringJoiner("");
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(caseType);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        for (int i = 0; i < cl_chars.length; i++) {
            String str = String.valueOf(cl_chars[i]);
            if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                joiner.add(PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1));
            } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                joiner.add(String.valueOf(cl_chars[i]));
            } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
                joiner.add(String.valueOf(cl_chars[i]));
            } else {// 否则不转换
                joiner.add(String.valueOf(cl_chars[i]));//如果是标点符号的话，带着
            }
        }

        return joiner.toString();
    }

    public static String getPinyinString(String ChineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        StringJoiner joiner = new StringJoiner("");
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        for (int i = 0; i < cl_chars.length; i++) {
            String str = String.valueOf(cl_chars[i]);
            if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                joiner.add(PinyinHelper.toHanyuPinyinStringArray(
                        cl_chars[i], defaultFormat)[0]);
            } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                joiner.add(String.valueOf(cl_chars[i]));
            } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
                joiner.add(String.valueOf(cl_chars[i]));
            } else {// 否则不转换
            }
        }
        return joiner.toString();
    }

    /**
     * 取第一个汉字的第一个字符
     *
     * @return String
     * @throws
     */
    public static String getFirstLetter(String ChineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        String str = String.valueOf(cl_chars[0]);
        if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
            hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
                    cl_chars[0], defaultFormat)[0].substring(0, 1);
            ;
        } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
            hanyupinyin += cl_chars[0];
        } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母

            hanyupinyin += cl_chars[0];
        } else {// 否则不转换

        }
        return hanyupinyin;
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        System.out.println(toHanyuPinyin("闫四洋"));
    }
}
