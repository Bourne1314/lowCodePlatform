package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserRoleDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserTokenDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dev.service.*;
import com.csicit.ace.dev.util.ShiroUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private DevUserService devUserService;

    @Autowired
    private DevUserRoleService devUserRoleService;

    @Autowired
    private DevRoleService devRoleService;

    @Autowired
    private DevUserTokenService devUserTokenService;

    /**
     * 获取用户所有权限列表
     *
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    @Override
    public Set<String> getUserPermissions(String userId) {
        List<String> permsList = devUserService.queryAllPerms(userId);
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    @Override
    public Set<String> getUserRoles(String userId) {
        List<String> roleNames = new ArrayList<>(16);
        List<String> roleIds =devUserRoleService.list(new QueryWrapper<DevUserRoleDO>()
                .eq("user_id", userId)).stream().map(DevUserRoleDO::getRoleId).collect(Collectors.toList());
        roleIds.stream().forEach(roleId -> {
            roleNames.add(devRoleService.getById(roleId).getRoleName());
        });
        Set<String> rolesSet = new HashSet<>();
        for (String role : roleNames) {
            if (StringUtils.isBlank(role)) {
                continue;
            }
            rolesSet.add(role.trim());
        }
        return rolesSet;
    }

    /**
     * 通过token获取用于token信息
     *
     * @param token
     * @return com.csicit.ace.common.pojo.domain.dev.SysUserTokenDO
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    @Override
    public DevUserTokenDO queryByToken(String token) {
        DevUserTokenDO sysUserTokenDO = devUserTokenService.getOne(new QueryWrapper<DevUserTokenDO>()
                .eq("token", token));
        //token失效
//        if (SysUserTokenDO == null || SysUserTokenDO.getExpireTime().getTime() < System.currentTimeMillis()) {
//            throw new IncorrectCredentialsException("token失效，请重新登录");
//        }
        return sysUserTokenDO;
    }

    /**
     * 通过token获取用于token信息
     *
     * @param userId
     * @return com.csicit.ace.common.pojo.domain.dev.SysUserTokenDO
     * @author zuogang
     * @date 2019/12/5 16:06
     */
    @Override
    public DevUserDO queryUser(String userId) {
        return devUserService.getById(userId);
    }

    /**
     * 初始化权限 -> 拿全部权限
     *
     * @param
     * @return Map
     * @author zuogang
     * @date 2019/12/6 16:35
     */
    @Override
    public Map<String, String> loadFilterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
         /* 过滤链定义，从上向下顺序执行，一般将 / ** 放在最为下边:这是一个坑呢，一不小心代码就不好使了;
          authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问 */
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");

        filterChainDefinitionMap.put("/sockjs-node/**", "anon");
        filterChainDefinitionMap.put("/index.js", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");

        // 登陆
        filterChainDefinitionMap.put("/logins/login", "anon");

//        filterChainDefinitionMap.put("/#/Home", "anon");

//        filterChainDefinitionMap.put("/SysMenus/action/tree/**", "anon");
//        filterChainDefinitionMap.put("/SysUsers/action/info/**", "anon");
        // 生成代码
        filterChainDefinitionMap.put("/generator/**", "anon");
        filterChainDefinitionMap.put("/liquibase/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/**", "oauth2");
        return filterChainDefinitionMap;
    }

    /**
     * 在对uri权限进行增删改操作时，需要调用此方法进行动态刷新加载数据库中的uri权限
     *
     * @param shiroFilterFactoryBean
     * @param roleId
     * @param isRemoveSession
     * @return void
     * @author zuogang
     * @date 2019/12/6 16:31
     */
    @Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, String roleId, Boolean
            isRemoveSession) {
        synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            // 清空拦截管理器中的存储
            manager.getFilterChains().clear();
            // 清空拦截工厂中的存储,如果不清空这里,还会把之前的带进去
            //            ps:如果仅仅是更新的话,可以根据这里的 map 遍历数据修改,重新整理好权限再一起添加
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            // 动态查询数据库中所有权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitionMap());
            // 重新构建生成拦截
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                manager.createChain(entry.getKey(), entry.getValue());
            }
            // 动态更新该角色相关联的用户shiro权限
            if (roleId != null) {
                updatePermissionByRoleId(roleId, isRemoveSession);
            }
        }
    }

    @Override
    public void updatePermissionByRoleId(String roleId, Boolean isRemoveSession) {
        // 查询当前角色的用户shiro缓存信息 -> 实现动态权限
        List<String> userIds = devUserRoleService.list(new QueryWrapper<DevUserRoleDO>()
                .eq("role_id", roleId)).stream().map(DevUserRoleDO::getUserId).collect(Collectors.toList());
        List<DevUserDO> userList =devUserService.list(new QueryWrapper<DevUserDO>()
                .and(userIds == null || userIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", userIds));
        // 删除当前角色关联的用户缓存信息,用户再次访问接口时会重新授权 ; isRemoveSession为true时删除Session -> 即强制用户退出
        if (!CollectionUtils.isEmpty(userList)) {
            for (DevUserDO user : userList) {
                ShiroUtils.deleteCache(user.getUserName(), isRemoveSession);
            }
        }
    }
}
