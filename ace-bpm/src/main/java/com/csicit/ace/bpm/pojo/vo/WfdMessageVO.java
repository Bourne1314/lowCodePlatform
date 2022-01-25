package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 发送消息对象
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/27 17:53
 */
@Data
public class WfdMessageVO implements Serializable {

    public WfdMessageVO() {

    }

    public WfdMessageVO(List<String> userIds, String channelName, String templateId, Map<String,Object> data) {
        this.userIds = userIds;
        this.channelName = channelName;
        this.templateId = templateId;
        this.data = data;
    }

    /**
     * 接收人主键列表
     */
    private String userId;

    /**
     * 接收人主键列表
     */
    private List<String> userIds;

    /**
     * 频道
     */
    private String channelName;

    /**
     * 模板主键
     */
    private String templateId;

    /**
     * 模板数据
     */
    private Map<String,Object> data;
}
