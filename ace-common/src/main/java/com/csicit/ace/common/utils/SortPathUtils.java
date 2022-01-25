package com.csicit.ace.common.utils;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.internationalization.InternationUtils;

import java.text.DecimalFormat;

/**
 * 排序路径工具类
 *
 * @version V1.0
 * @Author: yansiyang
 * @date 2019/4/15 14:06
 */
public class SortPathUtils {

    public static final String pattern = "000000";
    /**
     * 根据父路径及排序号获取路径
     *
     * @param parentPath 父排序路径
     * @param sortIndex  子排序号
     * @return
     * @author yansiyang
     * @date 19:47 2019/4/10
     */
    public static String getSortPath(String parentPath, int sortIndex) {
        if ((sortIndex + "").length() > pattern.length()) {
            Long largestNumber = Long.parseLong("1" + pattern) - 1;
            throw new RException(String.format(InternationUtils.getInternationalMsg("SORT_INDEX_HAS_EXCEED"), largestNumber));
        }
        DecimalFormat df = new DecimalFormat(pattern);
        String sortPath = df.format(Integer.valueOf(sortIndex));
        return parentPath + sortPath;
    }
}
