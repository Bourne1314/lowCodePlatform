package com.csicit.ace.common.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 事件处理对象
 *
 * @author shanwj
 * @date 2020/4/24 15:39
 */
@Data
public class SocketEventVO {

    /**
     * 接收人id集合
     */
    private List<String> userIds;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 处理数据
     */
    private Map<String,Object> data;
    /**
     * 应用标识
     */
    private String appId;
    /**
     * 接收人字符串
     */
    private String receivers;

    public SocketEventVO(){

    }

    public SocketEventVO(List<String> userIds,String eventName,Map<String,Object> data,String appId){
        this.userIds = userIds;
        this.eventName = eventName;
        this.data = data;
        this.appId = appId;
        StringJoiner sj = new StringJoiner(",");
        userIds.forEach(userId -> {
            sj.add(userId);
        });
        this.receivers = sj.toString();
    }

}
