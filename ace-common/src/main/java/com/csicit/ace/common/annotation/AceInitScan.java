package com.csicit.ace.common.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 初始化扫描获取包路径
 *
 * @author shanwj
 * @date 2020/4/14 8:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(InitScannerRegistrar.class)
public @interface AceInitScan {

    /**
     * 扫描包名
     */
    String[] value();
}
