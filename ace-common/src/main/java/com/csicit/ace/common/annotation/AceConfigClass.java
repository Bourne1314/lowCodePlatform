package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 16:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AceConfigClass {
    String value() default "";
}
