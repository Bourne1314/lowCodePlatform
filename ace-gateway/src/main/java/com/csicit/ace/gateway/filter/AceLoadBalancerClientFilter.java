package com.csicit.ace.gateway.filter;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.gateway.utils.GatewaySecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * @author shanwj
 * @date 2020/6/16 8:33
 */
@Component
public class AceLoadBalancerClientFilter extends LoadBalancerClientFilter {

    private static Map<String,String> pushIpMap = new HashMap<>(16);

    public AceLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    GatewaySecurityUtils gatewaySecurityUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        //preserve the original url
        addOriginalRequestUrl(exchange, url);
        final ServiceInstance instance = choose(exchange);

        if (instance == null) {
            String msg = "Unable to find instance for " + url.getHost();
            throw new RException(msg);
        }
        URI uri = exchange.getRequest().getURI();
        String overrideScheme = instance.isSecure() ? "https" : "http";
        if (schemePrefix != null) {
            overrideScheme = url.getScheme();
        }
        URI requestUrl = loadBalancer.reconstructURI(new DelegatingServiceInstance(instance, overrideScheme), uri);
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
        return chain.filter(exchange);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        if(Objects.equals(path,"/socket.io/") &&this.loadBalancer instanceof RibbonLoadBalancerClient){
            String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost();
            String token = null;
            try {
                token = gatewaySecurityUtils.getTokenByCookie(exchange.getRequest());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(Objects.isNull(token)){
                return super.choose(exchange);
            }
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
            if(org.apache.commons.collections.CollectionUtils.isEmpty(serviceInstances)){
                throw new RException("消息推送服务未启动");
            }
            String ip = pushIpMap.computeIfAbsent(token,
                    key-> serviceInstances.get(new Random().nextInt(serviceInstances.size())).getHost());
            List<ServiceInstance> collects =
                    serviceInstances.stream()
                            .filter(serviceInstance -> Objects.equals(serviceInstance.getHost(), ip))
                            .collect(Collectors.toList());
            if (org.apache.commons.collections.CollectionUtils.isEmpty(serviceInstances)){
                return super.choose(exchange);
            }
            return collects.get(0);
        }
        return super.choose(exchange);
    }

    class DelegatingServiceInstance implements ServiceInstance {
        final ServiceInstance delegate;
        private String overrideScheme;

        DelegatingServiceInstance(ServiceInstance delegate, String overrideScheme) {
            this.delegate = delegate;
            this.overrideScheme = overrideScheme;
        }

        @Override
        public String getServiceId() {
            return delegate.getServiceId();
        }

        @Override
        public String getHost() {
            return delegate.getHost();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public boolean isSecure() {
            return delegate.isSecure();
        }

        @Override
        public URI getUri() {
            return delegate.getUri();
        }

        @Override
        public Map<String, String> getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public String getScheme() {
            String scheme = delegate.getScheme();
            if (scheme != null) {
                return scheme;
            }
            return this.overrideScheme;
        }

    }
}
