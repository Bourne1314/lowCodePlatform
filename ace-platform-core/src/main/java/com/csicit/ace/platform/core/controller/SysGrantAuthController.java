package com.csicit.ace.platform.core.controller;


import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.GrantAuthVO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.SysGrantAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 授权管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/sysGrantAuths")
@Api("授权管理")
public class SysGrantAuthController extends BaseController {

    @Autowired
    SysGrantAuthService sysGrantAuthService;

    /**
     * 获取用户权限列表
     *
     * @param params 请求参数map对象
     * @return 权限列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取用户权限列表", httpMethod = "GET", notes = "获取用户权限列表")
    @ApiImplicitParam(name = "params", value = "请求参数", required = true, dataType = "Map")
    @AceAuth("获取用户授权列表")
    @RequestMapping(value = "/user/auths", method = RequestMethod.GET)
    public R getUserAuths(@RequestParam Map<String, Object> params) {
        String userId = params.get("userId").toString();
        String appId = params.get("appId").toString();
        List<GrantAuthVO> userAuths = sysGrantAuthService.getUserAuths(userId, appId);
        List<GrantAuthVO> listT = TreeUtils.makeTree(userAuths, GrantAuthVO.class);
        return R.ok().put("list", listT);
    }


    /**
     * 获取角色权限列表
     *
     * @param params 请求参数map对象
     * @return 角色权限列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取角色权限列表", httpMethod = "GET", notes = "获取角色权限列表")
    @ApiImplicitParam(name = "params", value = "请求参数", required = true, dataType = "Map")
    @AceAuth("获取角色权限列表")
    @RequestMapping(value = "/role/auths", method = RequestMethod.GET)
    public R getRoleAuths(@RequestParam Map<String, Object> params) {
        String roleId = params.get("roleId").toString();
        String appId = params.get("appId").toString();
        List<GrantAuthVO> roleAuths = sysGrantAuthService.getRoleAuths(roleId, appId);
        List<GrantAuthVO> listT = TreeUtils.makeTree(roleAuths, GrantAuthVO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 获取权限用户角色列表
     *
     * @param params 请求参数map对象
     * @return 权限列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取用户权限列表", httpMethod = "GET", notes = "获取用户权限列表")
    @ApiImplicitParam(name = "params", value = "请求参数", required = true, dataType = "Map")
    @AceAuth("获取权限授权列表")
    @RequestMapping(value = "/auth/usersAndRoles", method = RequestMethod.GET)
    public R getUsersAndRoles(@RequestParam Map<String, Object> params) {
        String authId = params.get("authId").toString();
        String appId = params.get("appId").toString();
        GrantAuthReciveVO grantAuthReciveVO = sysGrantAuthService.getUsersAndRoles(authId, appId);
        return R.ok().put("instance", grantAuthReciveVO);
    }

    /**
     * 获取该应用管理员所拥有的应用权限列表
     *
     * @param params 请求参数map对象
     * @return 角色权限列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取该应用管理员所拥有的应用权限列表", httpMethod = "GET", notes = "获取该应用管理员所拥有的应用权限列表")
    @ApiImplicitParam(name = "params", value = "请求参数", required = true, dataType = "Map")
    @AceAuth("获取角色权限列表")
    @RequestMapping(value = "/query/appAdmin/appAuths", method = RequestMethod.POST)
    public R getAppAuths(@RequestBody Map<String, Object> params) {
        String appId = params.get("appId").toString();
        List<SysAuthDO> auths = sysGrantAuthService.getAppAuths(appId);
        List<SysAuthDO> listT = TreeUtils.makeTree(auths, SysAuthDO.class);
        return R.ok().put("list", listT);
    }


    /**
     * 角色授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "角色授权(未激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("角色授权(未激活)")
    @RequestMapping(value = "/save/roleAuth", method = RequestMethod.POST)
    public R saveRoleAuth(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysAuthRoleService.saveRoleAuth(grantAuthReciveVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 角色授权(分配并激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "角色授权(分配并激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("角色授权(分配并激活)")
    @RequestMapping(value = "/saveAndActive/roleAuth", method = RequestMethod.POST)
    public R saveAndActiveRoleAuth(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysAuthRoleService.saveRoleAuth(grantAuthReciveVO)) {
            if (sysAuthRoleService.saveRoleAuthActivation(grantAuthReciveVO)) {
                return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
            }
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 角色授权(激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "角色授权(激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("角色授权(激活)")
    @RequestMapping(value = "/roleAuthActivation", method = RequestMethod.POST)
    public R saveRoleAuthActivation(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysAuthRoleService.saveRoleAuthActivation(grantAuthReciveVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 用户授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "用户授权(未激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("用户授权(未激活)")
    @RequestMapping(value = "/save/userAuth", method = RequestMethod.POST)
    public R saveUserAuth(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysAuthUserService.saveUserAuth(grantAuthReciveVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 用户授权(分配并激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "用户授权(分配并激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("用户授权(分配并激活)")
    @RequestMapping(value = "/saveAndActive/userAuth", method = RequestMethod.POST)
    public R saveAndActiveUserAuth(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysAuthUserService.saveUserAuth(grantAuthReciveVO)) {
            if (sysAuthUserService.saveUserAuthActivation(grantAuthReciveVO)) {
                return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
            }
        }

        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 用户授权(激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "用户授权(激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("用户授权(激活)")
    @RequestMapping(value = "/userAuthActivation", method = RequestMethod.POST)
    public R saveUserAuthActivation(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysAuthUserService.saveUserAuthActivation(grantAuthReciveVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 一键权限激活
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "一键权限激活")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("一键权限激活")
    @RequestMapping(value = "/allAuthActivation", method = RequestMethod.POST)
    public R saveAllActivation(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysGrantAuthService.saveAllActivation(grantAuthReciveVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 权限授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "权限授权(未激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("权限授权(未激活)")
    @RequestMapping(value = "/save/userRoleAuth", method = RequestMethod.POST)
    public R saveUserRoleAuth(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        if (sysGrantAuthService.saveUserRoleAuth(grantAuthReciveVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 权限授权(分配并激活)
     *
     * @param grantAuthReciveVO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "权限授权(分配并激活)")
    @ApiImplicitParam(name = "grantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
    @AceAuth("权限授权(分配并激活)")
    @RequestMapping(value = "/saveAndActive/userRoleAuth", method = RequestMethod.POST)
    public R saveAndActiveUserRoleAuth(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
        // 分配
        // 权限角色授权
        Map<String, Object> result1 = sysAuthRoleService.saveAuthRoleGrant(grantAuthReciveVO);
        if (!(Boolean) result1.get("result"))
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        // 激活
        // 发生改变关系的角色ID列表
        List<String> roleIds = (List<String>) result1.get("roleIds");
        if (CollectionUtils.isNotEmpty(roleIds)) {
            roleIds.stream().distinct().forEach(roleId -> {
                if (!sysAuthRoleService.saveRoleAuthHistory(grantAuthReciveVO.getAppId(), roleId, sysRoleService
                        .getById(roleId).getName
                        ())) {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
            });
        }
        if (!sysAuthRoleService.saveRoleActivateForUserValidAuth(roleIds, grantAuthReciveVO.getAppId())) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }

        // 分配
        // 权限用户授权
        Map<String, Object> result2 = sysAuthUserService.saveAuthUserGrant(grantAuthReciveVO);
        if (!(Boolean) result2.get("result"))
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        // 激活
        // 发生改变关系的用户ID列表
        List<String> userIds = (List<String>) result2.get("userIds");
        if (CollectionUtils.isNotEmpty(userIds)) {
            userIds.stream().forEach(userId -> {
                GrantAuthReciveVO userGrantAuthReciveVO = new GrantAuthReciveVO();
                userGrantAuthReciveVO.setUserId(userId);
                userGrantAuthReciveVO.setAppId(grantAuthReciveVO.getAppId());
                if (!sysAuthUserService.saveUserAuthActivation(userGrantAuthReciveVO)) {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
            });
        }

        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 获取待激活的用户列表和角色列表
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "获取待激活的用户列表和角色列表", httpMethod = "POST")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取待激活的用户列表和角色列表")
    @RequestMapping(value = "/waitActivation/userAndRoleList", method = RequestMethod.POST)
    public R waitActivationUserAndRoleList(@RequestBody Map<String, Object> map) {
        String appId = (String) map.get("appId");
        GrantAuthReciveVO grantAuthReciveVO = sysGrantAuthService.waitActivationUserAndRoleList(appId);
        return R.ok().put("instance", grantAuthReciveVO);
    }

    /**
     * 获取当前应用管理员需要激活权限的用户和角色个数
     *
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "获取当前应用管理员需要激活权限的用户和角色个数", httpMethod = "POST")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取待激活的用户列表和角色列表")
    @RequestMapping(value = "/waitActivation/userAndRoleCount", method = RequestMethod.POST)
    public R waitActivationUserAndRoleCount() {
        GrantAuthReciveVO grantAuthReciveVO = sysGrantAuthService.waitActivationUserAndRoleCount();
        return R.ok().put("instance", grantAuthReciveVO);
    }

}
