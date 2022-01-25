package com.csicit.ace.gateway.dynamic;

//import com.csicit.ace.common.utils.JsonUtils;
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.config.listener.Listener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Executor;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/24 16:48
 */
//@Component
public class DynamicRouteServiceImplByNacos {
//    @Autowired
//    private DynamicRouteServiceImpl dynamicRouteService;
//
//    public DynamicRouteServiceImplByNacos() {
//
//        dynamicRouteByNacosListener("sc-gateway", "xujin_test");
//    }
//
//    /**
//     * 监听Nacos Server下发的动态路由配置
//     *
//     * @param dataId
//     * @param group
//     */
//    public void dynamicRouteByNacosListener(String dataId, String group) {
//        try {
//            ConfigService configService = NacosFactory.createConfigService("127.0.0.1:8848");
//            String content = configService.getConfig(dataId, group, 5000);
//            System.out.println(content);
//            configService.addListener(dataId, group, new Listener() {
//                @Override
//                public void receiveConfigInfo(String configInfo) {
//                    RouteDefinition definition = JsonUtils.castObject(configInfo, RouteDefinition.class);
//                    dynamicRouteService.update(definition);
//                }
//
//                @Override
//                public Executor getExecutor() {
//                    return null;
//                }
//            });
//        } catch (Exception e) {
//            //todo 提醒:异常自行处理此处省略
//        }
//    }

}
