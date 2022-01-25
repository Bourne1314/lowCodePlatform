package com.csicit.ace.common.annotation;

import com.csicit.ace.common.constant.Constants;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 初始化扫描包
 *
 * @author shanwj
 * @date 2020/4/14 9:52
 */
public class InitScannerRegistrar implements ImportBeanDefinitionRegistrar{

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = metadata
                .getAnnotationAttributes(AceInitScan.class.getName(), true);
        Map<String, Object> nacosAttr = metadata
                .getAnnotationAttributes(EnableDiscoveryClient.class.getName(), true);
        String []names = (String[]) attrs.get("value");
        if (Objects.isNull(nacosAttr) && !Constants.isDevPlatform){
            Constants.isMonomerApp = true;
        }
        StringJoiner sj = new StringJoiner(",");
        for (String name:names){
            sj.add(name);
        }
        Constants.BasePackages = sj.toString();
        if (Constants.isMonomerApp || Constants.isZuulApp){
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("----------单体版应用启动-------------");
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
        }else{
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("----------云版应用启动-------------");
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
        }

    }
}
