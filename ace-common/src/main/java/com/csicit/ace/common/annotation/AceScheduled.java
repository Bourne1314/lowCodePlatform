package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * 计划任务注解
 *
 * @author shanwj
 * @date 2020/4/13 18:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  AceScheduled {

    /**
     * 任务名称
     * @return
     */
    String name();

    /**
     * 执行任务路径
     * @return
     */
    String url();

    /**
     * 任务组
     * @return
     */
    String group();

}
