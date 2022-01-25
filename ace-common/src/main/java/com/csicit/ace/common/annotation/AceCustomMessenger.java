package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义消息发送信使
 *
 * @author shanwj
 * @date 2020/4/13 18:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AceCustomMessenger {

    /**
     * 自定义信使名称
     * eg. 订单消息发送
     */
    String name();

    /**
     * 请求路径
     * eg. /message/send/order
     */
    String url();

}
