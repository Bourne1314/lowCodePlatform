package com.csicit.ace.bpm.pojo.vo.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 办理过程-节点信息
 *
 * @author JonnyJiang
 * @date 2019/11/20 9:24
 */
@Data
public class NodeVO implements Serializable {
    /**
     * 节点主键
     */
    private String id;
    /**
     * 流程实例id
     */
    private String flowId;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点标识
     */
    private String code;
    /**
     * 是否已办结
     */
    private Integer completed;
    /**
     * 是否当前办理节点 0 否 1是
     */
    private Integer pending;
    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 驳回到本节点的节点id
     */
    private String rejectFromNodeId;
    /**
     * 流出分支列表
     */
    private List<LinkVO> flowOutLinks;
    /**
     * 是否与当前任务相关
     */
    private Integer relevant;
    /**
     * 驳回到的节点id列表
     */
    private List<String> rejectToNodeIds;

    public List<String> getRejectToNodeIds() {
        if (rejectToNodeIds == null) {
            rejectToNodeIds = new ArrayList<>();
        }
        return rejectToNodeIds;
    }

    public void addFlowOutLinks(LinkVO link) {
        getFlowOutLinks().add(link);
    }

    public List<LinkVO> getFlowOutLinks() {
        if (flowOutLinks == null) {
            flowOutLinks = new ArrayList<>();
        }
        return flowOutLinks;
    }

    public void addRejectToNodeId(String nodeId) {
        getRejectToNodeIds().add(nodeId);
    }
}