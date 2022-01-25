package com.csicit.ace.bpm.enums;

import java.util.Map;

/**
 * 分支结果比较运算符
 *
 * @author JonnyJiang
 * @date 2020/5/28 17:15
 */
public enum LinkOperator {
    /**
     * 大于等于
     */
    GREATER_THAN_OR_EQUAL(0),
    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL(1),
    /**
     * 大于
     */
    GREATER_THAN(2),
    /**
     * 小于
     */
    LESS_THAN(3),
    /**
     * 等于
     */
    EQUAL(4),
    /**
     * 最多
     */
    MAXIMUM(5),
    /**
     * 最少
     */
    MINIMUM(6);

    private Integer value;

    LinkOperator(Integer value) {
        this.value = value;
    }

    public Boolean isEquals(Integer linkCountMode) {
        return this.value.equals(linkCountMode);
    }

    public static boolean isTrue(Integer linkCountMode, Integer operator, String result, Integer count, Map<String, Integer> resultMap) {
        if (result != null && count != null) {
            Double r;
            // 指定结果被选择次数
            Integer cnt;
            if (resultMap.containsKey(result)) {
                cnt = resultMap.get(result);
            } else {
                cnt = 0;
            }
            if (LinkCountMode.Proportion.isEquals(linkCountMode)) {
                r = Double.valueOf(cnt * 100) / resultMap.values().stream().mapToInt(o -> o).sum();
            } else if (LinkCountMode.Quantity.isEquals(linkCountMode)) {
                r = Double.valueOf(cnt);
            } else {
                r = 0d;
            }
            if (GREATER_THAN_OR_EQUAL.isEquals(operator)) {
                return r >= count;
            } else if (LESS_THAN_OR_EQUAL.isEquals(operator)) {
                return r <= count;
            } else if (GREATER_THAN.isEquals(operator)) {
                return r > count;
            } else if (LESS_THAN.isEquals(operator)) {
                return r < count;
            } else if (EQUAL.isEquals(operator)) {
                return r.equals(result);
            } else if (MAXIMUM.isEquals(operator)) {
                Boolean isMaximum = true;
                for (String key : resultMap.keySet()) {
                    if (!key.equals(result)) {
                        if (resultMap.get(key) >= cnt) {
                            isMaximum = false;
                            break;
                        }
                    }
                }
                return isMaximum;
            } else if (MINIMUM.isEquals(operator)) {
                Boolean isMinimum = true;
                for (String key : resultMap.keySet()) {
                    if (!key.equals(result)) {
                        if (resultMap.get(key) <= cnt) {
                            isMinimum = false;
                            break;
                        }
                    }
                }
                return isMinimum;
            }
        }
        return false;
    }
}
