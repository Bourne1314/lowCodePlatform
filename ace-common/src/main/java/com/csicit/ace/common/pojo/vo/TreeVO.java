package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/21 9:18
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeVO extends AbstractTreeVO {
    private String label;
    private String type;
    private List<TreeVO> children;
}

