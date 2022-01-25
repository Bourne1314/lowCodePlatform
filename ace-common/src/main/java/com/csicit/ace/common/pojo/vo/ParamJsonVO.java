package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 表格控件基类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/18 11:06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamJsonVO {
    /**
     * 模糊查询字段名(多字段以英文逗号相隔) 例:'bookName,author'
     */
    private String searchField;
    /**
     * 模糊查询值
     */
    private String searchValue;
    /**
     * 排序字段名
     */
    private String orderField;
    /**
     * 排序方式(asc/desc)
     */
    private String orderWay;

    /**
     * 筛选内容
     */
    private List<FilterInfoVO> filterInfo;
}
