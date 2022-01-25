package com.csicit.ace.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/17 10:57
 */
public class AceCollectionUtils {
    /**
     * 复制列表 新的内存对象
     * @param list
     * @return 
     * @author FourLeaves
     * @date 2020/6/17 10:59
     */
    public  static <T> List<T> copyList(List<T> list) {
        List<T> cpList = new ArrayList<>();
        org.apache.commons.collections.CollectionUtils.addAll(cpList, new Object[list.size()]);
        Collections.copy(cpList, list);
        return cpList;
    }
}
