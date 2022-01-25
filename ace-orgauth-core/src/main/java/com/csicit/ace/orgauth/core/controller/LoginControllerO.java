package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.orgauth.core.service.SysUserService;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录请求接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/22 9:28
 */
@RestController
@Api("登录管理")
public class LoginControllerO {

    @Resource(name = "sysUserServiceO")
    private SysUserService sysUserService;

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    CacheUtil cacheUtil;

    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回用户信息
     *
     * @param userKey
     * @return
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    @RequestMapping(value = "/orgauth/login/action/getUserAfterLogin/{userKey}", method = RequestMethod.GET)
    public R getUserAfterLogin(@PathVariable("userKey") String userKey) {
        return sysUserService.getUserInfoAfterLogin(userKey);
    }

    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回用户信息 token
     *
     * @param userKey
     * @return
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    @RequestMapping(value = "/orgauth/login/action/getTokenAfterLogin/{userKey}", method = RequestMethod.GET)
    public String getTokenAfterLogin(@PathVariable("userKey") String userKey) {
        return sysUserService.getTokenAfterLogin(userKey);
    }

//    /**
//     * 登录
//     *
//     * @param loginForm
//     * @return
//     * @author yansiyang
//     * @date 2019/4/22 9:00
//     */
//    @ApiOperation(value = "登录", httpMethod = "POST")
//    @ApiImplicitParam(name = "loginForm", value = "登陆表单", required = true, dataType = "LoginForm")
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public R login(@RequestBody LoginForm loginForm) {
//        String userName = loginForm.getUserName();
//        String password = loginForm.getPassword();
//        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
//            return R.error(InternationUtils.getInternationalMsg("EMPTY_USERNAME_OR_PASSWORD"));
//        }
//        return sysUserService.login(userName, password);
//    }

//    /**
//     * 用户登出
//     *
//     * @param
//     * @return
//     * @author yansiyang
//     * @date 2019/4/22 9:00
//     */
//    @ApiOperation(value = "登出", httpMethod = "GET")
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public R logout() {
//        String token = securityUtils.getToken();
//        if (!StringUtils.isBlank(token)) {
//            sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger,"退出"),"退出","退出成功!",null,null);
//            cacheUtil.delete(token);
//            cacheUtil.delete(token + "userid");
//            cacheUtil.delete(token + "aceuserid");
//        }
//        return R.ok(InternationUtils.getInternationalMsg("LOGOUT_SUCCESS"));
//    }
}
