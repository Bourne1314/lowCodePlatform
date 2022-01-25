package com.csicit.ace.common.form;

import lombok.Data;

/**
 * 登录表单
 *
 * @author yansiyang
 * @date 2019-04-10 10:37:46
 * @version V1.0
 */
@Data
public class LoginForm {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 随机编码
     */
    private String uuid;
}
