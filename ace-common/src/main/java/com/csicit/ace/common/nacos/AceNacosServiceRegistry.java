package com.csicit.ace.common.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.StringUtils;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/1/6 9:57
 */
public class AceNacosServiceRegistry implements ServiceRegistry<Registration> {

    private static final Logger log = LoggerFactory.getLogger(AceNacosServiceRegistry.class);

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private final NamingService namingService;

    public AceNacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.namingService = nacosDiscoveryProperties.namingServiceInstance();
    }

    @Override
    public void register(Registration registration) {

        if (StringUtils.isEmpty(registration.getServiceId())) {
            log.warn("No service to register for nacos client...");
            return;
        }

        String serviceId = registration.getServiceId();

        Instance instance = new Instance();
        instance.setIp(registration.getHost());
        instance.setPort(registration.getPort());
        instance.setWeight(nacosDiscoveryProperties.getWeight());
        instance.setClusterName(nacosDiscoveryProperties.getClusterName());
        instance.setMetadata(registration.getMetadata());

        try {
            namingService.registerInstance(serviceId, instance);
            log.info("nacos registry, {} {}:{} register finished", serviceId,
                    instance.getIp(), instance.getPort());
        } catch (Exception e) {
            log.error("nacos registry, {} register failed...{},", serviceId,
                    registration.toString(), e);
            /**
             * 退出
             */
            System.exit(-1);
        }
    }

    @Override
    public void deregister(Registration registration) {

        log.info("De-registering from Nacos Server now...");

        if (StringUtils.isEmpty(registration.getServiceId())) {
            log.warn("No dom to de-register for nacos client...");
            return;
        }

        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        String serviceId = registration.getServiceId();

        try {
            namingService.deregisterInstance(serviceId, registration.getHost(),
                    registration.getPort(), nacosDiscoveryProperties.getClusterName());
        } catch (Exception e) {
            log.error("ERR_NACOS_DEREGISTER, de-register failed...{},",
                    registration.toString(), e);
        }

        log.info("De-registration finished.");
        /**
         * 退出
         */
        System.exit(-1);
    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(Registration registration, String status) {
        // nacos doesn't support set status of a particular registration.
    }

    @Override
    public <T> T getStatus(Registration registration) {
        // nacos doesn't support query status of a particular registration.
        return null;
    }

}

