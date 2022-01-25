package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * 方法权限标识
 *
 * @author shanwj
 * @date 2019-04-10 18:57:46
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AceAuth {
    /**
     * 权限名称
     * @return
     */
    String value() default "";
}
