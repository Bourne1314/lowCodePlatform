package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/25 15:49
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AceReqMock {
    /**
     *  切面指定的用户
     */
    String[] userNames();

    /**
     *  返回的数据
     */
    String data() default "";
}
