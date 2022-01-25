package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.orgauth.core.service.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/9/23 11:44
 */
@RequestMapping("/orgauth/sysAuths")
@RestController
public class SysAuthControllerO {

    @Resource(name = "sysAuthServiceO")
    SysAuthService sysAuthService;


    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId   用户ID
     * @param authCode 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/action/hasAuthCodeInCurrentApp/{userId}/{authCode}/{appId}", method = RequestMethod
            .GET)
    boolean hasAuthCodeInCurrentApp(@PathVariable("userId") String userId, @PathVariable("authCode") String authCode,
                                    @PathVariable("appId") String appId) {
        return sysAuthService.hasAuthCodeInCurrentApp(userId, authCode, appId);
    }

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId 用户ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = {"/action/hasAuthorityWithUserId/{userId}/{authId}",
            "/action/hasAuthorityWithUserId/{userId}/"}, method = RequestMethod.GET)
    boolean hasAuthorityWithUserId(@PathVariable("userId") String userId, @PathVariable("authId") String authId) {
        return sysAuthService.hasAuthorityWithUserId(userId, authId);
    }

    /**
     * 判断指定角色是否拥有某项权限
     *
     * @param roleId 角色ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/action/hasAuthorityWithRoleId/{roleId}/{authId}", method = RequestMethod.GET)
    boolean hasAuthorityWithRoleId(@PathVariable("roleId") String roleId, @PathVariable("authId") String authId) {
        return sysAuthService.hasAuthorityWithRoleId(roleId, authId);
    }

    /**
     * 获取指定用户的所有权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getAllAuthIdsByUserId/{userId}", method = RequestMethod.GET)
    List<String> getAllAuthIdsByUserId(@PathVariable("userId") String userId) {
        return sysAuthService.getAllAuthIdsByUserId(userId);
    }

    /**
     * 获取指定用户关于指定应用的所有权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getAllAuthIdsByUserIdAndApp/{userId}/{appId}", method = RequestMethod.GET)
    List<String> getAllAuthIdsByUserIdAndApp(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId) {
        return sysAuthService.getAllAuthIdsByUserIdAndApp(userId, appId);
    }

    /**
     * 获取指定用户关于指定应用的所有权限标识(code)
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getAllAuthCodesByUserIdAndApp/{userId}/{appId}", method = RequestMethod.GET)
    List<String> getAllAuthCodesByUserIdAndApp(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId) {
        return sysAuthService.getAllAuthCodesByUserIdAndApp(userId, appId);
    }

    /**
     * 获取指定角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:05 2019/3/28
     */
    @RequestMapping(value = "/action/getAuthIdsByRoleId/{roleId}", method = RequestMethod.GET)
    List<String> getAuthIdsByRoleId(@PathVariable("roleId") String roleId) {
        return sysAuthService.getAuthIdsByRoleId(roleId);
    }

    /**
     * 判断指定用户是否拥有使用某项api资源的权限
     *
     * @param userId 用户ID
     * @param api    权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/action/hasApiByUserId/{userId}/{api}", method = RequestMethod.GET)
    boolean hasApiByUserId(@PathVariable("userId") String userId, @PathVariable("api") String api) {
        return sysAuthService.hasApiByUserId(userId, api);
    }

    /**
     * 获取指定用户的所有api资源的权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getAllApisByUserId/{userId}", method = RequestMethod.GET)
    List<String> getAllApisByUserId(@PathVariable("userId") String userId) {
        return sysAuthService.getAllApisByUserId(userId);
    }

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getAllApisByUserIdAndApp/{userId}/{appId}", method = RequestMethod.GET)
    List<String> getAllApisByUserIdAndApp(@PathVariable("userId") String userId, @PathVariable("appId") String appId) {
        return sysAuthService.getAllApisByUserIdAndApp(userId, appId);
    }

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getAllApisByRoleId/{roleId}", method = RequestMethod.GET)
    List<String> getAllApisByRoleId(@PathVariable("roleId") String roleId) {
        return sysAuthService.getAllApisByRoleId(roleId);
    }

    /**
     * 获取登录用户在该应用下的有效权限列表
     *
     * @param appId  应用ID
     * @param userId 用户iD
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/action/getMixAuth/{appId}/{userId}", method = RequestMethod.GET)
    List<SysAuthDO> getMixAuth(@PathVariable("appId") String appId, @PathVariable("userId") String userId) {
        return sysAuthService.getMixAuth(appId, userId);
    }
}
