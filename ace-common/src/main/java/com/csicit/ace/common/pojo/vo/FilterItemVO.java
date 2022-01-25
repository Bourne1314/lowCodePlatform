package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 表格筛选具体内容基类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/18 11:06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterItemVO {
    /**
     * 筛选方式
     */
    private String mode;
    /**
     * 筛选内容
     */
    private Object value;
}
