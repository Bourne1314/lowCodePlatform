package com.csicit.ace.dev.util;


import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisSessionDAO;

import java.util.Collection;
import java.util.Objects;

/**
 * <p> Shiro工具类 </p>
 *
 * @author : zuogang
 * @description :
 * @date : 2019/8/23 12:33
 */
public class ShiroUtils {

    /**
     * 私有构造器
     **/
    private ShiroUtils() {
    }

    private static RedisSessionDAO redisSessionDAO = SpringUtil.getBean(RedisSessionDAO.class);


    /**
     * 获取当前用户Session
     *
     * @Return SysUserEntity 用户信息
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 用户登出
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取当前用户信息
     *
     * @Return SysUserEntity 用户信息
     */
    public static DevUserDO getUserinfo() {
        return (DevUserDO) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 删除用户缓存信息
     *
     * @Param username  用户名称
     * @Param isRemoveSession 是否删除Session，删除后用户需重新登录
     */
    public static void deleteCache(String username, boolean isRemoveSession) {
        //从缓存中获取Session
        Session session = null;
        // 获取当前已登录的用户session列表
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        DevUserDO sysUserEntity;
        Object attribute = null;
        // 遍历Session,找到该用户名称对应的Session
        for (Session sessionInfo : sessions) {
            attribute = sessionInfo.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            sysUserEntity = (DevUserDO) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (sysUserEntity == null) {
                continue;
            }
            if (Objects.equals(sysUserEntity.getUserName(), username)) {
                session = sessionInfo;
                // 清除该用户以前登录时保存的session，强制退出  -> 单用户登录处理
                if (isRemoveSession) {
                    redisSessionDAO.delete(session);
                }
            }
        }

        if (session == null || attribute == null) {
            return;
        }
        //删除session
        if (isRemoveSession) {
            redisSessionDAO.delete(session);
        }
        //删除Cache，再访问受限接口时会重新授权
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
    }

    /**
     * 从缓存中获取指定用户名的Session
     *
     * @param username
     */
    private static Session getSessionByUsername(String username) {
        // 获取当前已登录的用户session列表
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        DevUserDO user;
        Object attribute;
        // 遍历Session,找到该用户名称对应的Session
        for (Session session : sessions) {
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (DevUserDO) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            if (Objects.equals(user.getUserName(), username)) {
                return session;
            }
        }
        return null;
    }

}
