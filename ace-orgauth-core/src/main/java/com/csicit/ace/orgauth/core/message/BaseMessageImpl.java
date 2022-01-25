package com.csicit.ace.orgauth.core.message;

import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.utils.HttpClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/7/5 14:56
 */
@Component
@Order(-1)
public class BaseMessageImpl {

    @Autowired
    protected  HttpClient client;

    @Nullable
    @Autowired
    protected IMsgPush msgPush;

    protected Map<String,Object> getMsgContent(SysMessageDO sysMessageDO,String type){
        Map<String,Object> map = new HashMap<>(16);
        map.put("to",sysMessageDO.getReceiveUsers());
        JSONObject object = new JSONObject();
        object.put("title",sysMessageDO.getTitle());
        object.put("content",sysMessageDO.getContent());
        object.put("url",sysMessageDO.getUrl());
        object.put("type",type);
        map.put("appId",sysMessageDO.getAppId());
        map.put("message",object);
        return map;
    }
}
