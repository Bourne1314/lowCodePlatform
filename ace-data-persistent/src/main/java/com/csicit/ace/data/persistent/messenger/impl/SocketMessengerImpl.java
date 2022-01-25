package com.csicit.ace.data.persistent.messenger.impl;


import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.utils.DateUtils;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.messenger.AbstractMessengerImpl;
import com.csicit.ace.data.persistent.messenger.IMessenger;
import com.csicit.ace.data.persistent.messenger.config.SocketServerRunner;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 站内信信使实现
 *
 * @author shanwj
 * @date 2020/4/13 18:37
 */
@Service
public class SocketMessengerImpl extends AbstractMessengerImpl implements IMessenger {

//    @Value("${ace.socket.sendAppType:0}")
//    private Integer sendAppType;
//
//    @Value("${ace.socket.sendApps:#{null}}")
//    private String sendApps;

    /**
     * 单机版使用 socketIo服务对象
     */
    @Autowired
    @Nullable
    SocketIOServer socketIOServer;

    @Nullable
    @Autowired
    DiscoveryUtils discoveryUtils;


    @Override
    public String getAnnotationValue() {
        return getAnnotationValue(this.getClass());
    }

    @Override
    public String getType() {
        return "站内信";
    }

    @Override
    public void sendMsg(SysMessageDO sysMessageDO) {
        SysMessageDO message = replaceTempplateByData(sysMessageDO, Constants.SocketMessenger);
        if (Constants.isMonomerApp || Constants.isZuulApp) {
            monoSend(message);
        } else {
            cloudSend(message);
        }
    }

    private void monoSend(SysMessageDO sysMessageDO) {
        String receiveUsers = sysMessageDO.getReceiveUsers();
        if (StringUtils.isNotEmpty(receiveUsers) && !Objects.isNull(socketIOServer)) {
            String[] userIds = receiveUsers.split(",");
            Map<String, Object> msgSendData = getMonoMsgSendData(sysMessageDO);
            SocketIONamespace namespace = socketIOServer.getNamespace("/" + sysMessageDO.getAppId());
            if (Objects.nonNull(namespace)) {
                for (String userId : userIds) {
                    namespace.getRoomOperations(userId).sendEvent(SocketServerRunner.SOCKET_EVENT_NAME, msgSendData);
                }
            }
//            List<SocketIONamespace> namespaces = new ArrayList<>();
//            if (sendAppType == 0) {
//                namespaces.add(socketIOServer.getNamespace("/" + sysMessageDO.getAppId()));
//            } else if (sendAppType == 1) {
//                if (StringUtils.isNotBlank(sendApps)) {
//                    for (String app : sendApps.split(",")) {
//                        namespaces.add(socketIOServer.getNamespace("/" + app));
//                    }
//                }
//            } else {
//                namespaces.add(socketIOServer.getNamespace("/" + sysMessageDO.getAppId()));
//                if (StringUtils.isNotBlank(sendApps)) {
//                    for (String app : sendApps.split(",")) {
//                        namespaces.add(socketIOServer.getNamespace("/" + app));
//                    }
//                }
//            }
//            Map<String, Object> msgSendData = getMonoMsgSendData(sysMessageDO);
//            for (SocketIONamespace namespace : namespaces) {
//                if (Objects.nonNull(namespace)) {
//                    for (String userId : userIds) {
//                        namespace.getRoomOperations(userId).sendEvent(SocketServerRunner.SOCKET_EVENT_NAME, msgSendData);
//                    }
//                }
//            }
        }
    }

    private void cloudSend(SysMessageDO sysMessageDO) {
        Map<String, Object> map = getMsgSendData(sysMessageDO);
        if (discoveryUtils != null) {
            ServiceInstance push = discoveryUtils.getOneHealthyInstance(Constants.PUSHSERVER);
            if (Objects.nonNull(push)) {
                httpClient.postReturnString("http://" + push.getHost() + ":" + push.getPort() + "/", map);
            }
        }
    }

    private Map<String, Object> getMonoMsgSendData(SysMessageDO sysMessageDO) {
        JSONObject object = new JSONObject();
        object.put("title", sysMessageDO.getTitle());
        object.put("content", sysMessageDO.getContent());
        object.put("url", sysMessageDO.getUrl());
        object.put("eventName", "aceNotifyEvent");
        object.put("datetime", DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN).format(LocalDateTime.now()));
        object.put("id", sysMessageDO.getAppId());
        return object;
    }

    private Map<String, Object> getMsgSendData(SysMessageDO sysMessageDO) {
        Map<String, Object> map = new HashMap<>(16);
        JSONObject object = new JSONObject();
        object.put("title", sysMessageDO.getTitle());
        object.put("content", sysMessageDO.getContent());
        object.put("url", sysMessageDO.getUrl());
        object.put("eventName", "aceNotifyEvent");
        object.put("datetime", DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN).format(LocalDateTime.now()));
        object.put("id", sysMessageDO.getAppId());
        map.put("to", sysMessageDO.getReceiveUsers());
        map.put("appId", sysMessageDO.getAppId());
        map.put("data", object);
        return map;
    }

}
