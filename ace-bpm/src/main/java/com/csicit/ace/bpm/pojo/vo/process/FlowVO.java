package com.csicit.ace.bpm.pojo.vo.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 办理过程-流程实例
 *
 * @author JonnyJiang
 * @date 2019/11/20 9:24
 */
@Data
public class FlowVO implements Serializable {
    /**
     * 流程ID
     */
    private String id;
    /**
     * 流程实例id
     */
    private String flowInstanceId;
    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程标识
     */
    private String code;
    /**
     * 是否已办结
     */
    private Integer completed;
    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 办理结果
     */
    private String result;
    /**
     * 节点列表
     */
    private List<NodeVO> nodes;

    public void addNode(NodeVO node) {
        getNodes().add(node);
    }

    public List<NodeVO> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        return nodes;
    }
}
