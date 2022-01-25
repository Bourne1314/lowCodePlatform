package com.csicit.ace.dev.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  <p> Spring上下文工具类 </p>
 *
 * @description :
 * @author : zuogang
 * @date : 2019/8/23 12:32
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 设置上下文
     *
     * @param applicationContext 上下文
     * @author shanwj
     * @date 2019/4/12 17:16
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     *
     * @return org.springframework.context.ApplicationContext
     * @author shanwj
     * @date 2019/4/12 14:28
     */
    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    /**
     * 通过name获取 Bean.
     *
     * @param name 类名
     * @return Bean对象
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz 类对象
     * @param <T>   类泛型
     * @return 类
     * @author shanwj
     * @date 2019/4/12 14:29
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  类名
     * @param clazz 类对象
     * @param <T>   类泛型
     * @return 类
     * @author shanwj
     * @date 2019/4/12 14:29
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 返回满足指定对象类型的Bean
     *
     * @param clazz 类对象
     * @return java.util.Map<java.lang.String, T> 对象集合
     * @author JonnyJiang
     * @date 2019/10/15 17:23
     */

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

}
