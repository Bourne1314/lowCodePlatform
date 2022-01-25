package com.csicit.ace.zuul.config;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/29 16:01
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    SecurityUtils securityUtils;

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    static String[] passUrls = {"/platform/login",
            "/platform/init",
            "/platform/ssoRequest",
            "/platform/sysConfigs/action/downloadImg/",
            "/platform/logout",
            "/orgauth/orgauth/login",
            "/orgauth/orgauth/logout"};

    @Autowired
    CacheUtil cacheUtil;

    String[] apps = {"platform", "fileserver", "quartz", "report", "orgauth", "push"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (path.length() < 2) {
            return true;
        }

        for (String url : passUrls) {
            if (path.startsWith(url)) {
                for (String app : apps) {
                    if (path.startsWith("/" + app + "/")) {
                        path = path.substring(app.length() + 1);
                        break;
                    }
                }
                request.getRequestDispatcher(path).forward(request, response);
                return false;
            }
        }

        String token = null;
        try {
            token = securityUtils.getToken();
        } catch (Exception e) {

        }
        if (path.startsWith("/platform/sysMenus/action/tree")) {
            // 拦截token失效 返回json
            if (StringUtils.isBlank(token) || (StringUtils.isNotBlank(token) && !cacheUtil.hasKey(token))) {
                JSONObject json = new JSONObject();
                json.put("code", HttpCode.AUTHENTICATION_FAILURE);
                if (StringUtils.isNotBlank(token)) {
                    json.put("msg", InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"));
                }
                //设置返回请求头
                response.setContentType("application/json;charset=utf-8");
                //写出流
                PrintWriter out = response.getWriter();
                out.write(json.toJSONString());
                out.flush();
                out.close();
                return false;
            }
        }

        /**
         * 校验失败 返回登录页
         */
        if (StringUtils.isBlank(token) || (StringUtils.isNotBlank(token) && !cacheUtil.hasKey(token))) {
            request.getRequestDispatcher("/logout").forward(request, response);
            return false;
        }

        List<String> appList = new ArrayList<>();
        appList.add(appName);
        appList.addAll(Arrays.asList(apps));

        for (String app : appList) {
            if (path.startsWith("/" + app + "/")) {
                if (Objects.equals(app, "orgauth") && !path.startsWith("/" + app + "/" + app)) {
                    break;
                }
                if (Objects.equals(app, "report")) {
                    break;
                }
                path = path.substring(app.length() + 1);
                break;
            }
        }

        request.getRequestDispatcher(path).forward(request, response);
        return false;
    }
}
