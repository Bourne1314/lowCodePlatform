package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.data.persistent.service.SysUserThirdPartyService;
import com.csicit.ace.orgauth.core.service.SysAuthService;
import com.csicit.ace.orgauth.core.service.SysRoleService;
import com.csicit.ace.orgauth.core.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/22 11:26
 */
@RestController
@RequestMapping("/orgauth/sysUsers")
public class SysUserControllerO {

    @Resource(name = "sysUserServiceO")
    SysUserService sysUserService;

    @Resource(name = "sysRoleServiceO")
    SysRoleService sysRoleService;

    @Resource(name = "sysAuthServiceO")
    SysAuthService sysAuthService;

    @Resource(name = "sysAuthMixServiceO")
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysUserThirdPartyService sysUserThirdPartyService;

    /**
     * 绑定用户ip
     *
     * @param userId
     * @param ip
     * @return
     * @author FourLeaves
     * @date 2021/9/1 8:07
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/bingUserIp/{userId}/{ip}", method = RequestMethod.GET)
    Boolean bingUserIp(@PathVariable("userId") String userId, @PathVariable("ip") String ip) {
        return sysUserService.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", userId).set("IS_IP_BIND", 1).set("ip_Address", ip));
    }

    /**
     * 获取用户绑定的用户列表
     *
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2021/7/29 8:09
     */
    @RequestMapping(value = "/action/getBindUsers/{userId}", method = RequestMethod.GET)
    List<SysUserDO> getBindUsers(@PathVariable("userId") String userId) {
        SysUserDO userDO = sysUserService.getById(userId);
        if (Objects.nonNull(userDO)) {
            List<String> userIDs = new ArrayList<>();
            List<String> userIDss = new ArrayList<>();
            userIDss.add(userId);
            userIDss.add(userDO.getUserName());
            if (CollectionUtils.isNotEmpty(userIDss)) {
                List<SysUserThirdPartyDO> sysUserThirdPartyDOS = sysUserThirdPartyService
                        .list(new QueryWrapper<SysUserThirdPartyDO>()
                                .eq("type", "ace")
                                .and(i -> i
                                        .in("user_id", userIDss)
                                        .or().in("account", userIDss)));
                if (CollectionUtils.isNotEmpty(sysUserThirdPartyDOS)) {
                    List<String> userIds = sysUserThirdPartyDOS.stream().map(SysUserThirdPartyDO::getUserId)
                            .collect(Collectors.toList());
                    userIds.addAll(sysUserThirdPartyDOS.stream().map(SysUserThirdPartyDO::getAccount)
                            .collect(Collectors.toList()));
                    sysUserThirdPartyDOS = sysUserThirdPartyService
                            .list(new QueryWrapper<SysUserThirdPartyDO>()
                                    .eq("type", "ace")
                                    .and(i -> i
                                            .in("user_id", userIds).or().in("account", userIds)));
                    Set<String> userSets = sysUserThirdPartyDOS.stream().map(SysUserThirdPartyDO::getUserId)
                            .collect(Collectors.toSet());
                    userSets.addAll(sysUserThirdPartyDOS.stream().map(SysUserThirdPartyDO::getAccount)
                            .collect(Collectors.toSet()));
                    userIDs = new ArrayList<>(userSets);
                }
            }
            if (CollectionUtils.isNotEmpty(userIDs)) {
                return sysUserService.list(new QueryWrapper<SysUserDO>()
                        .in("id", userIDs).or().in("user_name", userIDs).select("id", "staff_no", "user_name",
                                "real_name"
                                , "user_type", "secret_level"));
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取用户绑定的用户主键列表
     *
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2021/7/29 8:09
     */
    @RequestMapping(value = "/action/getBindUserIds/{userId}", method = RequestMethod.GET)
    List<String> getBindUserIds(@PathVariable("userId") String userId) {
        List<SysUserDO> userDOS = getBindUsers(userId);
        if (CollectionUtils.isNotEmpty(userDOS)) {
            return userDOS.stream().map(SysUserDO::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 通过角色编码 判断用户是否有此角色
     *
     * @param users
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    @RequestMapping(value = "/action/addUsers", method = RequestMethod.POST)
    boolean addUsers(@RequestBody List<SysUserDO> users) {
        return sysUserService.addUsers(users);
    }

    /**
     * 根据曲线标识获取用户列表
     *
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    @RequestMapping(value = "/action/getUserListByAuthCode", method =
            RequestMethod.GET)
    List<SysUserDO> getUserListByAuthCode(@RequestParam("code") String code, @RequestParam("appId") String appId) {
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(appId)) {
            SysAuthDO authDO = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                    .eq("code", code)
                    .eq("app_id", appId));
            if (Objects.nonNull(authDO)) {
                List<SysAuthMixDO> authMixDOS = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().select("id",
                        "user_id").eq
                        ("auth_id", authDO.getId()));
                if (CollectionUtils.isNotEmpty(authMixDOS)) {
                    List<String> userIds = authMixDOS.stream().map(SysAuthMixDO::getUserId).collect(Collectors.toList
                            ());
                    return new ArrayList<>(sysUserService.listByIds(userIds));
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据角色标识获取用户列表
     *
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    @RequestMapping(value = "/action/getUserListByRoleCode", method =
            RequestMethod.GET)
    List<SysUserDO> getUserListByRoleCode(@RequestParam("code") String code, @RequestParam("appId") String appId) {
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(appId)) {
            SysRoleDO roleDO = sysRoleService.getOne(new QueryWrapper<SysRoleDO>()
                    .eq("role_code", code).and(i->i
                    .eq("app_id", appId).or().isNull("app_id")));
            if (roleDO != null) {
                return sysUserService.getUsersByRoleId(roleDO.getId());
            }
        }
        return new ArrayList<>();
    }


    /**
     * 根据工号获取用户
     *
     * @param staffNo
     * @return
     * @author FourLeaves
     * @date 2020/11/30 11:16
     */
    @RequestMapping(value = "/action/getUserByStaffNo", method =
            RequestMethod.GET)
    SysUserDO getUserByStaffNo(@RequestParam("staffNo") String staffNo) {
        return sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("staff_no", staffNo)
                .eq("is_delete", 0));
    }

    /**
     * 根据工号列表获取用户列表
     *
     * @param staffNos
     * @return
     * @author FourLeaves
     * @date 2020/11/30 11:16
     */
    @RequestMapping(value = "/action/getUserListByStaffNos", method =
            RequestMethod.POST)
    List<SysUserDO> getUserListByStaffNos(@RequestBody List<String> staffNos) {
        return sysUserService.list(new QueryWrapper<SysUserDO>()
                .in("staff_no", staffNos)
                .eq("is_delete", 0));
    }

    /**
     * 判断用户重名
     *
     * @param userName
     * @return Integer
     * @author xulei
     * @date 2020/7/8 15:38
     */
    @RequestMapping(value = "/action/userRepeat", method = RequestMethod.POST)
    Integer userRepeat(@RequestBody String userName) {
        return sysUserService.userRepeat(userName);
    }

    /**
     * 通过权限ID获取用户ID
     *
     * @param appId
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    @RequestMapping(value = "/action/getUserIdsByAuthId/{appId}/{authId}", method = RequestMethod.GET)
    List<String> getUserIdsByAuthId(@PathVariable("appId") String appId, @PathVariable("authId") String authId) {
        return sysUserService.getUserIdsByAuthId(appId, authId);
    }

    /**
     * 通过权限ID获取用户
     *
     * @param appId
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    @RequestMapping(value = "/action/getUsersByAuthId/{appId}/{authId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByAuthId(@PathVariable("appId") String appId, @PathVariable("authId") String authId) {
        return sysUserService.getUsersByAuthId(appId, authId);
    }


    /**
     * 根据密级、角色或组织信息获取用户并分页
     *
     * @param params type 类型 0 业务单元  1 角色
     *               id 主键
     *               secretLevel 密级
     *               searchStr 查询参数
     * @return
     * @author FourLeaves
     * @date 2020/2/21 10:46
     */
    @RequestMapping(value = "/action/listUsersBySecretLevelAndRoleOrOrg", method = RequestMethod.GET)
    R listUsersBySecretLevelAndRoleOrOrg(@RequestParam Map<String, String> params) {
        return sysUserService.listUsersBySecretLevelAndRoleOrOrg(params);
    }

    /**
     * 修改当前用户密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/11/6 10:42
     */
    @RequestMapping(value = "/action/updateCurrentUserPassword", method = RequestMethod.POST)
    public R updateCurrentUserPassword(@RequestBody Map<String, String> map) {
        return sysUserService.updateCurrentUserPassword(map);
    }

    /**
     * 模糊查询用户列表 用户名 ID
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/action/getUsersByMatchNameOrID/{str}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByMatchNameOrID(@PathVariable("str") String str) {
        return sysUserService.getUsersByMatchNameOrID(str);
    }

    /**
     * 模糊查询用户列表 用户名 ID
     *
     * @param groupId
     * @param orgId
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/action/getUseAppUsers/{groupId}/{orgId}/{appId}", method = RequestMethod.GET)
    List<SysUserDO> getUseAppUsers(@PathVariable("groupId") String groupId, @PathVariable("orgId") String orgId,
                                   @PathVariable("appId") String appId) {
        return sysUserService.getUseAppUsers(groupId, orgId, appId);
    }

    /**
     * 根据业务单元ID获取 对应业务单元及其子业务单元下所有用户
     *
     * @param organizationId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/action/getAllUsersByOrgAndSon/{organizationId}", method = RequestMethod.GET)
    List<SysUserDO> getAllUsersByOrgAndSon(@PathVariable("organizationId") String organizationId) {
        return sysUserService.getAllUsersByOrgAndSon(organizationId);
    }


    /**
     * 根据部门ID获取 对应部门及其子部门下所有用户
     *
     * @param depId 部门ID
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/action/getAllUsersByDepAndSon/{depId}", method = RequestMethod.GET)
    List<SysUserDO> getAllUsersByDepAndSon(@PathVariable("depId") String depId) {
        return sysUserService.getAllUsersByDepAndSon(depId);
    }

    /**
     * 根据相关条件获取指定用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/10/15 8:27
     */
    @RequestMapping(value = "/action/getSomeUsers", method = RequestMethod.GET)
    List<SysUserDO> getSomeUsers(@RequestParam Map<String, Object> map) {
        return sysUserService.getSomeUsers(map);
    }

    /**
     * 模糊查询用户列表 用户名 角色名  部门名
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @ApiImplicitParam(name = "roleId", value = "roleId", dataType = "String", required = true)
    @ApiOperation(value = "模糊查询用户列表", httpMethod = "GET", notes = "模糊查询用户列表")
    @RequestMapping(value = "/action/getUsersByMatch/{str}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByMatch(@PathVariable("str") String str) {
        return sysUserService.getUsersByMatch(str);
    }

    /**
     * 根据角色ID获取用户列表
     *
     * @param roleId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @ApiImplicitParam(name = "roleId", value = "roleId", dataType = "String", required = true)
    @ApiOperation(value = "根据角色ID获取用户列表", httpMethod = "GET", notes = "根据角色ID获取用户列表")
    @RequestMapping(value = "/action/getUsersByRoleId/{roleId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByRoleId(@PathVariable("roleId") String roleId) {
        return sysUserService.getUsersByRoleId(roleId);
    }


    /**
     * 根据部门ID获取用户列表 depId为0 获取当前用户业务单元下所有用户   depId若为业务单元ID 则查询业务单元下的所有用户
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @ApiImplicitParam(name = "depId", value = "depId", dataType = "String", required = true)
    @ApiOperation(value = "根据部门ID获取用户列表", httpMethod = "GET", notes = "根据部门ID获取用户列表")
    @RequestMapping(value = "/action/getUsersDepId/{depId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByDepId(@PathVariable("depId") String depId) {
        return sysUserService.getUsersByDepId(depId);
    }

    /**
     * 根据部门ID获取用户列表
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @ApiImplicitParam(name = "depId", value = "depId", dataType = "String", required = true)
    @ApiOperation(value = "根据部门ID获取用户列表", httpMethod = "GET", notes = "根据部门ID获取部门用户列表")
    @RequestMapping(value = "/action/getUsersOnlyByDepId/{depId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersOnlyByDepId(@PathVariable("depId") String depId) {
        return sysUserService.getUsersOnlyByDepId(depId);
    }

    /**
     * 根据密级获取用户列表
     *
     * @param level
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @ApiImplicitParam(name = "level", value = "密级", dataType = "Integer", required = true)
    @ApiOperation(value = "根据密级获取用户列表", httpMethod = "GET", notes = "根据密级获取用户列表")
    @RequestMapping(value = "/action/getUsersBySecretLevel/{level}", method = RequestMethod.GET)
    List<SysUserDO> getUsersBySecretLevel(@PathVariable("level") Integer level) {
        return sysUserService.getUsersBySecretLevel(level);
    }

    /**
     * 根据密级获取用户列表
     *
     * @param level
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @ApiImplicitParam(name = "level", value = "密级", dataType = "Integer", required = true)
    @ApiOperation(value = "根据密级获取用户列表", httpMethod = "GET", notes = "根据密级获取用户列表")
    @RequestMapping(value = "/action/getUsersBySecretLevel/{level}/{le}", method = RequestMethod.GET)
    List<SysUserDO> getUsersBySecretLevel(@PathVariable("level") Integer level, @PathVariable("le") Integer le) {
        return sysUserService.getUsersBySecretLevel(level, le);
    }


    /**
     * 查询指定用户信息
     *
     * @param userId 用户主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "获取单个用户", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path")
    @AceAuth("获取单个用户")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public SysUserDO infoById(@PathVariable("userId") String userId) {
        return sysUserService.infoById(userId);
    }

    /**
     * 通过用户名称查询指定用户信息
     *
     * @param userName 用户名称
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "通过用户名称获取单个用户", httpMethod = "GET")
    @ApiImplicitParam(name = "userName", value = "用户名称", required = true, paramType = "path")
    @RequestMapping(value = "/userName/{userName}", method = RequestMethod.GET)
    public SysUserDO infoByUserName(@PathVariable("userName") String userName) {
        return sysUserService.infoByUserName(userName);
    }


    /**
     * 查询当前用户信息
     *
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "查询当前用户信息", httpMethod = "GET")
    @RequestMapping(value = "/action/current", method = RequestMethod.GET)
    public SysUserDO getCurrentUser() {
        return sysUserService.getCurrentUser();
    }
}
