package com.csicit.ace.platform.core.controller;

import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.form.LoginForm;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.platform.core.service.SysUserLoginService;
import com.csicit.ace.platform.core.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/11 10:40
 */
@RestController
@Api("登录管理")
public class LoginController {

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserLoginService sysUserLoginService;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    CacheUtil cacheUtil;
    /**
     * 登录
     *
     * @param loginForm
     * @return
     * @author yansiyang
     * @date 2019/4/22 9:00
     */
    @ApiOperation(value = "登录", httpMethod = "POST")
    @ApiImplicitParam(name = "loginForm", value = "登陆表单", required = true, dataType = "LoginForm")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public R login(@RequestBody LoginForm loginForm) {
        String userName = loginForm.getUserName();
        String password = loginForm.getPassword();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_USERNAME_OR_PASSWORD"));
        }
        return sysUserService.login(userName, password);
    }

    /**
     * 用户登出
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/22 9:00
     */
    @ApiOperation(value = "登出", httpMethod = "GET")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public R logout() {
        String token = securityUtils.getToken();
        if (!StringUtils.isBlank(token)) {
            sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger,"退出"),"退出","退出成功!",null,null);
            cacheUtil.delete(token);
            cacheUtil.delete(token + "userid");
            cacheUtil.delete(token + "aceuserid");
        }
        return R.ok(InternationUtils.getInternationalMsg("LOGOUT_SUCCESS"));
    }

    /**
     * 管理员登录平台，首页显示内容 
     * @param
     * @return 
     * @author zuogang
     * @date 2020/9/11 15:26
     */
    @ApiOperation(value = "管理员登录平台，首页显示内容 ", httpMethod = "GET")
    @RequestMapping(value = "/getLoginInfo", method = RequestMethod.GET)
    public R getLoginInfo() {
        return sysUserLoginService.getLoginInfo();
    }


}
