package com.csicit.ace.dev.controller;

import com.csicit.ace.common.form.LoginForm;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.DevUserService;
import com.csicit.ace.dev.service.DevUserTokenService;
import com.csicit.ace.dev.util.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关
 *
 * @author zuogang
 * @date Created in 8:34 2019/12/3
 */
@RestController
@RequestMapping("/logins")
@Api("登录管理")
public class SysLoginController {

    @Autowired
    DevUserService devUserService;

    @Autowired
    DevUserTokenService devUserTokenService;

    /**
     * 登录
     *
     * @param loginForm
     * @return
     * @author zuog
     * @date 2019/4/22 9:00
     */
    @ApiOperation(value = "登录", httpMethod = "POST")
    @ApiImplicitParam(name = "loginForm", value = "登陆表单", required = true, dataType = "LoginForm")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public R login(@RequestBody LoginForm loginForm) {
        return devUserService.login(loginForm);
    }

    /**
     * 退出系统
     *
     * @return
     * @author zuog
     * @date 2019/4/22 9:00
     */
    @ApiOperation(value = "退出系统", httpMethod = "POST")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public R logout() {
        devUserTokenService.logout(ShiroUtils.getUserinfo().getId());
        return R.ok();
    }

}
