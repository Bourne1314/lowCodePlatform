package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/9/23 11:52
 */
@Transactional
public interface SysAuthService extends IService<SysAuthDO> {
    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId   用户ID
     * @param authCode 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthCodeInCurrentApp(String userId, String authCode, String appId);

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId 用户ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthorityWithUserId(String userId, String authId);

    /**
     * 判断指定角色是否拥有某项权限
     *
     * @param roleId 角色ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthorityWithRoleId(String roleId, String authId);

    /**
     * 获取指定用户的所有权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllAuthIdsByUserId(String userId);

    /**
     * 获取指定用户关于指定应用的所有权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllAuthIdsByUserIdAndApp(String userId, String appId);

    /**
     * 获取指定用户关于指定应用的所有权限标识(code)
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllAuthCodesByUserIdAndApp(String userId, String appId);

    /**
     * 获取指定角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:05 2019/3/28
     */
    List<String> getAuthIdsByRoleId(String roleId);

    /**
     * 判断指定用户是否拥有使用某项api资源的权限
     *
     * @param userId 用户ID
     * @param api    权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasApiByUserId(String userId, String api);


    /**
     * 获取指定用户的所有api资源的权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllApisByUserId(String userId);


    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllApisByUserIdAndApp(String userId, String appId);

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllApisByRoleId(String roleId);
    /**
     * 根据相关条件获取指定用户
     *
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/10/15 8:27
     */
    List<SysAuthDO> getMixAuth(String appId, String userId);

}
