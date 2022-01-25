package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import lombok.Data;

/**
 * @author JonnyJiang
 * @date 2019/11/8 14:21
 */
@Data
public class NodeVo {
    /**
     * 主键
     */
    private String id;
    /**
     * 标识
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 节点类型
     */
    private String nodeType;

    public NodeVo(Node node) {
        this.id = node.getId();
        this.code = node.getCode();
        this.name = node.getName();
        this.nodeType = node.getNodeType();
    }
}
