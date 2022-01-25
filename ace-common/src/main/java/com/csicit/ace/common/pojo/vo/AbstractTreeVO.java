package com.csicit.ace.common.pojo.vo;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 树模型基类
 *
 * @author shanwj
 * @date 2019/4/18 11:06
 * @version V1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractTreeVO extends AbstractBaseDomain {

    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 叶子节点
     */
    private boolean leaf;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点描述
     */
    private String remark ;
    /**
     * 节点全局路径
     */
    private String sortPath;
    /**
     * 当前节点排序号
     */
    private Integer sortIndex;


}
