package com.csicit.ace.bpm.pojo.vo.wfi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WfiNodeVO implements Serializable {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点标识
     */
    private String nodeCode;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 经办人列表
     */
    private List<WfiNodeUserVO> nodeUsers;
    /**
     * 自由流步骤id
     */
    private String nodeFreeStepId;
}