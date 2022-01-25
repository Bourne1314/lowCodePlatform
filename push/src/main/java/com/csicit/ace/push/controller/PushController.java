package com.csicit.ace.push.controller;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.push.config.SocketServerRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/6/9 14:44
 */
@RestController
@RequestMapping("/")
public class PushController {

    @Autowired
    AceLogger logger;

    @Autowired
    SocketIOServer socketIOServer;

    @Autowired
    SocketServerRunner socketServerRunner;

    @PostMapping(value = "")
    public String pushSocketIo(@RequestBody Map<String,Object> map) {
        String appName = map.get("appId").toString();
        String userIdStr = map.get("to").toString();
        HashMap<String,Object> data = (HashMap)map.get("data");
        SocketIONamespace namespace = socketIOServer.getNamespace("/" + appName);
        if(Objects.nonNull(namespace)&&StringUtils.isNotEmpty(userIdStr)){
            String []userIds = userIdStr.split(",");
            for (String userId:userIds){
                namespace.getRoomOperations(userId).sendEvent(SocketServerRunner.SOCKET_EVENT_NAME, data);
            }
        }
        return "ok";
    }

    /**
     * 创建应用名空间
     * @param appName
     * @return 
     * @author FourLeaves
     * @date 2020/10/19 16:28
     */
    @GetMapping("/action/createNamespace/{appName}")
    public String createNamespace(@PathVariable("appName") String appName) {
        logger.info("服务注册：" + appName);
        socketServerRunner.httpCreateNamespace(appName);
        return "ok";
    }


}
