package com.csicit.ace.dev.shiro;

import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserTokenDO;
import com.csicit.ace.dev.service.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * 认证
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        DevUserDO user = (DevUserDO) principals.getPrimaryPrincipal();
        String userId = user.getId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);
        //用户角色列表

        Set<String> rolesSet = shiroService.getUserRoles(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        info.setRoles(rolesSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        DevUserTokenDO tokenEntity = shiroService.queryByToken(accessToken);
        //token失效
        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        DevUserDO user = shiroService.queryUser(tokenEntity.getUserId());
        // 如果用户不存在，抛此异常
        if (user == null) {
            throw new UnknownAccountException("用户名密码错误！");
        }
        // 判断用户是否启用
        if (Objects.equals(0, user.getApplyStatus())) {
            throw new DisabledAccountException("帐号被停用！");
        }
        // 锁定
        if (Objects.equals(1, user.getLockStatus())) {
            throw new LockedAccountException("帐号已被锁定！");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,accessToken , getName());
        return info;
    }
}
