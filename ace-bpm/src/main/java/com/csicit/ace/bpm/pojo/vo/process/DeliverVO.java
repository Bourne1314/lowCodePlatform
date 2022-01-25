package com.csicit.ace.bpm.pojo.vo.process;

import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 办理过程-转交信息
 *
 * @author JonnyJiang
 * @date 2019/11/20 9:24
 */
@Data
public class DeliverVO implements Serializable {
    /**
     * 序号
     */
    private Integer sn;
    /**
     * 办理人id
     */
    private String userId;
    /**
     * 办理人
     */
    private String realName;
    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 节点标识
     */
    private String nodeId;
    /**
     * 转交信息
     */
    private DeliverInfo deliverInfo;
}
