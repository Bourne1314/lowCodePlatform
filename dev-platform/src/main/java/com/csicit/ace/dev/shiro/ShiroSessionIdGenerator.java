package com.csicit.ace.dev.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 *  <p> 自定义SessionId生成器 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/23 15:47
 */
public class ShiroSessionIdGenerator implements SessionIdGenerator {
    /**
     * TOKEN前缀
     */
    String REDIS_PREFIX_LOGIN = "code-generator_token_%s";
    /**
     * 实现SessionId生成
     */
    @Override
    public Serializable generateId(Session session) {
        Serializable sessionId = new JavaUuidSessionIdGenerator().generateId(session);
        return String.format(REDIS_PREFIX_LOGIN, sessionId);
    }
}
