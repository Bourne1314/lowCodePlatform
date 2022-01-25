package com.csicit.ace.bpm.utils;

import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/27 10:49
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdCollectionDO {
    /**
     * 集合类型
     */
    private String type;

    /**
     * 集合代码
     */
    private String collectionType;

    /**
     * ID数组
     */
    private List<IdName> params;

    /**
     * 集合
     */
    private List<WfdCollectionDO> collections;

    /**
     * 规则id
     */
    private String rule;

    /**
     * 是否满足
     */
    private boolean satisfy;

    /**
     * 条件为真集合
     */
    private WfdCollectionDO ifTrue;

    /**
     * 条件为假集合
     */
    private WfdCollectionDO ifFalse;

}
