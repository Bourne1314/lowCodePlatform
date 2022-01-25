package com.csicit.ace.push.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.csicit.ace.common.config.RedisConfig;
import com.csicit.ace.common.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.Objects;


/**
 * @author shanwj
 * @date 2020/6/9 14:40
 */
@Configuration
public class SocketIoConifg {

    @Value("${ace.socket.port:5070}")
    private int port;
    @Value("${spring.redis.database:0}")
    private int redisDB;
    @Value("${spring.redis.port:6379}")
    private int redisPort;
    @Value("${spring.redis.host:#{null}}")
    private String redisHost;
    @Value("${spring.redis.password:#{null}}")
    private String redisPw;
    @Value("${ace.config.cache.type:redis}")
    private String cacheType;

    @Bean
    public SocketIOServer socketIOServer(@Nullable RedissonClient redissonClient){
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        if (Objects.nonNull(redissonClient)) {
            RedissonStoreFactory redissonStoreFactory = new RedissonStoreFactory(redissonClient);
            config.setStoreFactory(redissonStoreFactory);
        }
        config.setRandomSession(true);
        // 设置监听端口
        config.setPort(port);
        // 协议升级超时时间（毫秒），默认10000。HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(10000);
        // Ping消息间隔（毫秒），默认25000。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(60000);
        // Ping消息超时时间（毫秒），默认60000，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(180000);
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient() {
        if (cacheType.equals("redis")) {
            Config config = new Config();
            config.setCodec(new org.redisson.codec.JsonJacksonCodec());
            config.useSingleServer()
                    .setAddress("redis://" + redisHost + ":" + redisPort)
                    .setTimeout(3000)
                    .setConnectionPoolSize(250)
                    .setConnectionMinimumIdleSize(2)
                    .setDatabase(redisDB);
            if (StringUtils.isNotEmpty(redisPw)) {
                config.useSingleServer().setPassword(redisPw);
            }
            return Redisson.create(config);
        }
        return null;
    }

}
