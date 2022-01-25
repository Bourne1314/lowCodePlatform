package com.csicit.ace.bpm.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 获取用户新建工作的常用流程列表，最近使用流程列表
 *
 * @author zuogang
 * @return
 * @date 2020/4/26 17:12
 */
@Data
public class NewJobFlowVO {
    /**
     * 流程ID
     */
    private String id;
    /**
     * 流程标识
     */
    private String code;
    /**
     * 流程名
     */
    private String name;
    /**
     * 流程发起次数
     */
    private Integer count;
    /**
     * 流程最近发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latelyUserTime;
}
