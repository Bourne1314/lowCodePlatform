package com.csicit.ace.common.utils;

/**
 * Integer工具类
 *
 * @author JonnyJiang
 * @date 2019/5/21 17:55
 */
public class IntegerUtils {
    /**
     * true值
     */
    public static final int TRUE_VALUE = 1;
    /**
     * false值
     */
    public static final int FALSE_VALUE = 0;

    /**
     * 判断是否为true
     *
     * @param value 要判断的数值
     * @return 判断结果
     * @author JonnyJiang
     * @date 2019/03/30 19:55
     */
    public static boolean isTrue(Integer value) {
        if (value == null) {
            return false;
        }
        return value.equals(TRUE_VALUE);
    }

    /**
     * 判断是否为false
     *
     * @param value
     * @return boolean
     * @author JonnyJiang
     * @date 2019/10/17 19:55
     */

    public static boolean isFalse(Integer value) {
        if (value == null) {
            return true;
        }
        return value.equals(FALSE_VALUE);
    }

    public static boolean equals(Integer value1, Integer value2) {
        return value1 == null ? value2 == null : value1.equals(value2);
    }
}
