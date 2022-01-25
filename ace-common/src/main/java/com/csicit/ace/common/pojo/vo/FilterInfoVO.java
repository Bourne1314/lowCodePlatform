package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 表格筛选内容基类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/18 11:06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterInfoVO {
    /**
     * 字段类型 :'num','date','string'
     */
    private String type;
    /**
     * 字段名
     */
    private String field;
    /**
     * 筛选具体内容
     */
    private List<FilterItemVO> dynamicItem;
}
