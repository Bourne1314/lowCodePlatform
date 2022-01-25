package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * 配置项注解
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 15:49
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AceConfigMethod {
    String configName();
}
