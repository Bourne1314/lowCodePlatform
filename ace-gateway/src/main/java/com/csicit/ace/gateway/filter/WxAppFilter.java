package com.csicit.ace.gateway.filter;

import com.csicit.ace.common.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/25 14:57
 */
@Configuration
public class WxAppFilter implements GlobalFilter, Ordered {

    @Autowired
    CacheUtil redisUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (path.contains("/acewx/")) {
            String openWxApp = redisUtils.get("openWxApp");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
