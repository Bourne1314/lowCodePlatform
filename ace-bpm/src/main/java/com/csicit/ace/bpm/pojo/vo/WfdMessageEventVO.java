package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/27 19:12
 */
@Data
public class WfdMessageEventVO {

    public WfdMessageEventVO() {

    }

    public WfdMessageEventVO(List<String> userIds, String eventName, Map<String, Object> data) {
        this.userIds = userIds;
        this.eventName = eventName;
        this.data = data;
    }

    private String userId;

    /**
     * 接收人主键列表
     */
    private List<String> userIds;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 数据
     */
    private Map<String, Object> data;
}
