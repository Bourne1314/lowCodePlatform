package com.csicit.ace.data.persistent.messenger.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.csicit.ace.common.config.ZuulRouteConfig;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.utils.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * socket服务运行类
 *
 * @author shanwj
 * @date 2020/3/31 18:09
 */
@Component
public class SocketServerRunner implements CommandLineRunner {
    private final SocketIOServer server;
    @Autowired
    AceLogger aceLogger;
    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    ZuulRouteConfig zuulRouteConfig;

    private static final String SOCKET_LOG = "socketLog_";

    public static final String SOCKET_EVENT_NAME = "aceSocketEvent";

    @Autowired
    public SocketServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        if (Constants.isMonomerApp || Constants.isZuulApp) {
            server.start();
            aceLogger.info(SOCKET_LOG + "消息服务已启动!");
            initNameSpace();
        }
    }

    /**
     * 创建命名空间
     */
    private void initNameSpace() {
        Set<String> appIds = zuulRouteConfig.getRoutes().keySet();
        for (String service : appIds) {
            createNameSpace(service);
        }
    }

//    /**
//     * 添加@onConnect，客户端连接时调用，将客户端信息加入room
//     *
//     * @param socketIOClient
//     */
//    @OnConnect
//    public void onConnect(SocketIOClient socketIOClient) {
//        aceLogger.info(SOCKET_LOG + "建立连接：" + socketIOClient.getSessionId());
//        List<Map.Entry<String, String>> entries =
//                socketIOClient.getHandshakeData().getHttpHeaders().entries();
//        String cookStr = "";
//        for (Map.Entry<String, String> map : entries) {
//            if (Objects.equals("Cookie", map.getKey())) {
//                cookStr = map.getValue();
//            }
//        }
//        if (!StringUtils.isEmpty(cookStr)) {
//            String[] cookStrs = cookStr.split(";");
//            for (String str : cookStrs) {
//                if (str.contains("token=")) {
//                    String token = str.split("token=")[1];
//                    try {
//                        token = URLDecoder.decode(token, "utf-8");
//                        UUID sessionId = socketIOClient.getSessionId();
//                        String userId = cacheUtil.get(token + "userid");
//                        if (StringUtils.isNotEmpty(userId)) {
//                            socketIOClient.joinRoom(userId);
//                            aceLogger.info(SOCKET_LOG + "建立连接用户：{userId:" + userId + ",sessionId:" + sessionId + "}");
//                        }
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

    private void createNameSpace(String appName) {
        if (Objects.isNull(server.getNamespace("/" + appName))) {
            aceLogger.info(SOCKET_LOG + "创建命名空间:" + appName);
            SocketIONamespace serviceNamespace = server.addNamespace("/" + appName);
            serviceNamespace.addConnectListener(
                    new ConnectListener() {
                        @Override
                        public void onConnect(SocketIOClient socketIOClient) {
                            aceLogger.info(SOCKET_LOG + "建立连接：" + socketIOClient.getSessionId());
                            List<Map.Entry<String, String>> entries =
                                    socketIOClient.getHandshakeData().getHttpHeaders().entries();
                            String cookStr = "";
                            for (Map.Entry<String, String> map : entries) {
                                if (Objects.equals("Cookie", map.getKey())) {
                                    cookStr = map.getValue();
                                }
                            }
                            if (!StringUtils.isEmpty(cookStr)) {
                                String[] cookStrs = cookStr.split(";");
                                for (String str : cookStrs) {
                                    if (str.contains("token=")) {
                                        String token = str.split("token=")[1];
                                        try {
                                            token = URLDecoder.decode(token, "utf-8");
                                            String userId = cacheUtil.get(token + "userid");
                                            if (StringUtils.isNotEmpty(userId)) {
                                                socketIOClient.joinRoom(userId);
                                                aceLogger.info(SOCKET_LOG + "建立连接用户：" + userId);
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    });
            serviceNamespace.addDisconnectListener(new DisconnectListener() {
                @Override
                public void onDisconnect(SocketIOClient socketIOClient) {
                    UUID sessionId = socketIOClient.getSessionId();
                    aceLogger.info(SOCKET_LOG + "断开连接：" + sessionId);

                }
            });
        }
    }

//    /**
//     * 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
//     *
//     * @param client
//     */
//    @OnDisconnect
//    public void onDisconnect(SocketIOClient client) {
//        UUID sessionId = client.getSessionId();
//        aceLogger.info(SOCKET_LOG + "断开连接：" + sessionId);
//    }

}
