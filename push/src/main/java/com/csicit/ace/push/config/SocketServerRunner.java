package com.csicit.ace.push.config;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.DiscoveryUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author shanwj
 * @date 2020/6/9 14:41
 */
@Component
public class SocketServerRunner implements CommandLineRunner {

    @Autowired
    AceLogger aceLogger;

    private static final String SOCKET_LOG = "socketLog_";

    private static final String NEW_APP_EVENT_NAME = "newAceAppEvent";

    public static final String SOCKET_EVENT_NAME = "aceSocketEvent";

    private final SocketIOServer server;

    @Nullable
    @Autowired
    DiscoveryUtils discoveryUtils;

    @Autowired
    CacheUtil cacheUtil;

    @Value("${ace.socket.port:5070}")
    private int port;

    @Value("${ace.socket.name:push}")
    private String name;

    @Value("${spring.cloud.nacos.discovery.ip:123}")
    private String ip;

    @Value("${spring.cloud.nacos.discovery.server-addr:#{null}}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.discovery.namespace:public}")
    private String namespace;

    @Nullable
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    public SocketServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) {
        server.start();
        aceLogger.info(SOCKET_LOG + "消息服务已启动!");
        registerNacos();
        initNameSpace();
        subNewInstanceStart();
    }

    /**
     * 订阅新应用实例启动通知事件
     */
    private void subNewInstanceStart() {
        if (Objects.nonNull(redissonClient)) {
            RTopic newAceAppEvent = redissonClient.getTopic(NEW_APP_EVENT_NAME);
            newAceAppEvent.addListener(String.class, new MessageListener<String>() {
                @Override
                public void onMessage(CharSequence charSequence, String appName) {
                    createNameSpace(appName);
                }
            });
        }
    }

    /**
     * 注册到nacos
     */
    private void registerNacos() {
        NamingService naming = null;
        try {
            Properties properties = new Properties();
            if (StringUtils.isNotBlank(serverAddr)) {
                properties.setProperty("serverAddr", serverAddr);
                properties.setProperty("namespace", namespace);
                naming = NamingFactory.createNamingService(properties);
                naming.registerInstance(name, (Objects.equals(ip, "123") || StringUtils.isBlank(ip)) ? InetAddress.getLocalHost().getHostAddress() : ip, port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建命名空间
     */
    private void initNameSpace() {
        if (Objects.nonNull(discoveryUtils)) {
            List<String> services = discoveryUtils.getAllServices();
            for (String service : services) {
                createNameSpace(service);
            }
        }
    }

    /**
     * 创建服务命名空间
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2020/10/19 16:27
     */
    public void httpCreateNamespace(String appName) {
        createNameSpace(appName);
    }

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
                                            String userId;
                                            if (Objects.nonNull(redissonClient)) {
                                                try {
                                                    RBucket<String> bucket = redissonClient.getBucket(token + "userid", StringCodec.INSTANCE);
                                                    userId = bucket.get();
                                                } catch (Exception e) {
                                                    aceLogger.error("redissonClient获取userId时发生异常，key=" + token + "userid", e);
                                                    userId = cacheUtil.get(token + "userid");
                                                }
                                            } else {
                                                userId = cacheUtil.get(token + "userid");
                                            }
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
}
