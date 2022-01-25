package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserTokenDO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.Map;
import java.util.Set;

/**
 * shiro相关接口
 */
public interface ShiroService {
    /**
     * 获取用户所有权限列表
     *
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    Set<String> getUserPermissions(String userId);

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    Set<String> getUserRoles(String userId);

    /**
     * 通过token获取用于token信息
     *
     * @param token
     * @return com.csicit.ace.common.pojo.domain.dev.SysDevUserTokenDO
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    DevUserTokenDO queryByToken(String token);

    /**
     * 通过UserId获取用户信息
     *
     * @param userId
     * @return com.csicit.ace.common.pojo.domain.dev.SysDevUserDO
     * @author zuogang
     * @date 2019/12/5 16:07
     */
    DevUserDO queryUser(String userId);

    /**
     * 初始化权限 -> 拿全部权限
     *
     * @param :
     * @return: java.util.Map
     */
    Map<String, String> loadFilterChainDefinitionMap();

    /**
     * 在对uri权限进行增删改操作时，需要调用此方法进行动态刷新加载数据库中的uri权限
     *
     * @param shiroFilterFactoryBean
     * @param roleId
     * @param isRemoveSession:
     * @return: void
     */
    void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, String roleId, Boolean isRemoveSession);

    /**
     * shiro动态权限加载 -> 原理：删除shiro缓存，重新执行doGetAuthorizationInfo方法授权角色和权限
     *
     * @param roleId
     * @param isRemoveSession:
     * @return: void
     */
    void updatePermissionByRoleId(String roleId, Boolean isRemoveSession);

}
