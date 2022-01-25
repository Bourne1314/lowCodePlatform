package com.csicit.ace.bpm.collection;


import com.csicit.ace.bpm.pojo.vo.wfd.IdName;

import java.util.List;
import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/19 16:13
 */
public interface CollectionFormUtil {

    /**
     * 公共方法
     * 获取表单字段指定的值
     * @param idNames
     * @param variables
     * @return 
     * @author FourLeaves
     * @date 2020/3/19 16:14
     */
    String getIdFromForm(List<IdName> idNames, Map<String, Object> variables);
}
