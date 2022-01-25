package com.csicit.ace.bpm.pojo.vo.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 办理过程-办理活动
 *
 * @author JonnyJiang
 * @date 2019/11/26 8:48
 */
@Data
public class ActivityVO implements Serializable {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 序号
     */
    private Integer sn;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 驳回到的节点id
     */
    private List<String> rejectToNodeIds;
    /**
     * 驳回到的节点名称
     */
    private List<String> rejectToNodeNames;

    public List<String> getRejectToNodeIds() {
        if (rejectToNodeIds == null) {
            rejectToNodeIds = new ArrayList<>();
        }
        return rejectToNodeIds;
    }

    public List<String> getRejectToNodeNames() {
        if (rejectToNodeNames == null) {
            rejectToNodeNames = new ArrayList<>();
        }
        return rejectToNodeNames;
    }

    public void addRejectToNodeId(String nodeId) {
        getRejectToNodeIds().add(nodeId);
    }

    public void addRejectToNodeName(String name) {
        getRejectToNodeNames().add(name);
    }
}
