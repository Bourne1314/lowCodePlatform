package com.csicit.ace.testapp.controller;

import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IRole;
import com.csicit.ace.interfaces.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/12/2 8:19
 */
@RestController
public class SsoController {

    @Autowired
    IUser user;

    @Autowired
    IRole role;

    @RequestMapping("/sso/getUserName")
    public String getUserName() {
        return "zgTest";
    }

    @RequestMapping("/feign")
    public R feign() {
        return R.ok()
                .put("role", role.getRoleByCode("aa"))
                .put("o", user.getUserListByRoleCode("aa"));
    }


}
