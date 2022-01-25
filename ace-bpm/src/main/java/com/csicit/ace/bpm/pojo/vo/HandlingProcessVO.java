package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JonnyJiang
 * @date 2019/11/13 16:06
 */
@Data
public class HandlingProcessVO implements Serializable {
    /**
     * 序号
     */
    private Integer sn;
    /**
     * 办理人
     */
    private String realName;
    /**
     * 是否办结
     */
    private Integer completed;
    /**
     * 办结时间
     */
    private Date endTime;
    /**
     * 节点标识
     */
    private String nodeId;
    /**
     * 转交信息
     */
    private DeliverInfo deliverInfo;
}
