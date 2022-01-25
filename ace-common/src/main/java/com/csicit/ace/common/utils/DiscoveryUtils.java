package com.csicit.ace.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 服务管理工具类
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 10:15
 */
@ConditionalOnExpression("'${spring.cloud.nacos.discovery.server-addr:1}'.length() > 6")
@Component
public class DiscoveryUtils {

    @Nullable
    @Autowired
    DiscoveryClient discoveryClient;

    public static final String HEALTHY = "nacos.healthy";

    /**
     * 获取所有的已注册服务
     *
     * @return
     * @author FourLeaves
     * @date 2019/12/24 10:18
     */
    public List<String> getAllServices() {
        if (discoveryClient != null) {
            return discoveryClient.getServices();
        }
        return new ArrayList<>();
    }

    /**
     * 获取所有的已注册的拥有健康实例的服务
     *
     * @return
     * @author FourLeaves
     * @date 20120/11/04 08:43
     */
    public List<String> getHealthyServices() {
        List<String> services = getAllServices();
        List<String> healthyHervices = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(services)) {
            for (String service : services) {
                if (existHealthyInstance(service)) {
                    healthyHervices.add(service);
                }
            }
        }
        return healthyHervices;
    }

    /**
     * 根据服务名获取所有的实例
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 10:18
     */
    public List<ServiceInstance> getAllInstances(String appName) {
        if (StringUtils.isNotBlank(appName) && discoveryClient != null) {
            return discoveryClient.getInstances(appName);
        }
        return new ArrayList<>();
    }

    /**
     * 根据服务名获取 健康 的实例
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 10:18
     */
    public List<ServiceInstance> getHealthyInstances(String appName) {
        List<ServiceInstance> instances = getAllInstances(appName);
        List<ServiceInstance> heaalthyInstances = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            if (Objects.equals(instance.getMetadata().get(HEALTHY), "true")) {
                heaalthyInstances.add(instance);
            }
        }
        return heaalthyInstances;
    }

    /**
     * 根据服务名获取 一个 健康 的实例
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 10:18
     */
    public ServiceInstance getOneHealthyInstance(String appName) {
        List<ServiceInstance> heaalthyInstances = getHealthyInstances(appName);
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(heaalthyInstances)) {
            return heaalthyInstances.get(0);
        }
        return null;
    }

    /**
     * 根据服务名判断是否存在监控实例
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 10:18
     */
    public boolean existHealthyInstance(String appName) {
        return getHealthyInstances(appName).size() > 0;
    }


}
