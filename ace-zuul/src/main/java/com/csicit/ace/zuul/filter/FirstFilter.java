package com.csicit.ace.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.config.ZuulRouteConfig;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.*;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.orgauth.core.service.SysUserService;
import com.csicit.ace.platform.core.service.OrgGroupService;
import com.csicit.ace.platform.core.service.OrgOrganizationService;
import com.csicit.ace.zuul.config.PassProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;

//import com.csicit.ace.license.core.config.ZuulLicenseConfig;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/4/27 11:16
 */
//@ConditionalOnBean(ZuulLicenseConfig.class)
@Component
public class FirstFilter extends ZuulFilter {

    @Autowired
    AceLogger logger;

    /**
     * 是否开启 单点登录
     */
    @Value("${ace.config.sso.openGatewaySso:true}")
    private boolean openGatewaySso;
    /**
     * 是否开启 单点登录
     */
    @Value("${ace.config.sso.open:false}")
    private boolean openSso;

    /**
     * 是否开启 自动创建关联用户
     */
    @Value("${ace.config.sso.openCreateUser:false}")
    private boolean openCreateUser;

    /**
     * 单点登录接口 不包含应用名
     */
    @Value("${ace.config.sso.validateUrl:#{null}}")
    private String validateUrl;

    /**
     * 是否指定单点登录接口对应的APP
     */
    @Value("${ace.config.sso.onlyOneApp:false}")
    private boolean onlyOneApp;

    @Autowired
    ZuulRouteConfig zuulRouteConfig;

    /**
     * 单点登录接口对应的APP
     */
    @Value("${ace.config.sso.appName:#{null}}")
    private String theApp;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    PassProperties passProperties;

    @Autowired
    HttpClient client;

    @Autowired
    private Environment environment;

    @Resource(name = "sysUserServiceO")
    private SysUserService sysUserService;

    @Autowired
    private OrgGroupService orgGroupService;

    @Autowired
    private OrgOrganizationService orgOrganizationService;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
//        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    public String setToken() {
        String tokenStr = "webserviceToken";
        if (cacheUtil.hasKey(tokenStr)) {
            String token = cacheUtil.get(tokenStr);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
                if (cacheUtil.hasKey(token)) {
                    return token;
                }
            }
        }
        try {
            String token = GMBaseUtil.getToken("webservice", "webservice");
            int seconds = 60 * 60 * 24;
            cacheUtil.set("webserviceToken", token);
            SysUserDO user = new SysUserDO();
            user.setUserName("webservice");
            user.setId("webservice");
            user.setRealName("webservice");
            cacheUtil.hset(token, "user", JSONObject.toJSON(user), seconds);
            cacheUtil.set(token + "userid", user.getId(), seconds);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static String[] startStrs = {"/js/", "/magicalcoder/", "/css/", "/img/", "/static/", "/fonts/", "/pages/"};

    static String[] needTokenStrs = {"/webservice/"};

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getServletPath();
        logger.info("REQUEST_URL:" + path + ", REQUEST_METHOD:" + request.getMethod()
                + ", REQUEST_IP:" + IpUtils.getIpAddr(request));

        if (path.contains(theApp + validateUrl)) {
            return null;
        }

        // 放行指定app的静态资源
        if (!CollectionUtils.isEmpty(passProperties.getPassAppList())) {
            for (String appName : passProperties.getPassAppList()) {
                if (StringUtils.isNotBlank(appName)) {
                    if (Objects.equals(path, "/" + appName) || Objects.equals(path, "/" + appName + "/")) {
                        return null;
                    }
                    for (int i = 0; i < startStrs.length; i++) {
                        if (path.startsWith("/" + appName + startStrs[i])) {
                            return null;
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(passProperties.getPassUrlList())) {
            for (int i = 0; i < passProperties.getPassUrlList().size(); i++) {
                if (path.startsWith(passProperties.getPassUrlList().get(i))) {
                    return null;
                }
            }
        }


        for (int i = 0; i < needTokenStrs.length; i++) {
            if (path.contains(needTokenStrs[i])) {
                String token = setToken();
                reflectSetparam(request, "token", token);
                return null;
            }
        }

        String token = null;
        try {
            token = securityUtils.getToken();
        } catch (Exception e) {

        }
        HttpServletResponse response = ctx.getResponse();

        try {
            if (StringUtils.isBlank(token) || (StringUtils.isNotBlank(token) && !cacheUtil.hasKey(token))) {
                if (openGatewaySso && openSso && StringUtils.isNotBlank(validateUrl)) {
                    logger.info("*************************[ SSO ]*******************************");
                    logger.info("SSO-REQUEST-URL: " + path);
                    logger.info("SSO-REQUEST_IP:   " + IpUtils
                            .getIpAddr(request));
                    logger.info("SSO-OPEN: True");
                    //截取appName
                    String appName = null;
                    if (path.length() != 1 && path.split("/").length > 1) {
                        appName = path.split("/")[1];
                    }
                    if (onlyOneApp && StringUtils.isNotBlank(theApp)) {
                        appName = theApp;
                    }

                    String sessionId = request.getSession().getId();
                    if (cacheUtil.hasKey(sessionId)) {
                        token = cacheUtil.get(sessionId);
                        if (StringUtils.isNotBlank(token) && cacheUtil.hasKey(token)) {
                            logger.info("SSO-SESSION-TOKEN: YES");
                            return pass(request, token, response, appName, path, ctx);
                        }
                    }

                    if (StringUtils.isNotBlank(appName)) {
                        JSONObject app = JsonUtils.castObject(zuulRouteConfig.getRoutes().get(appName), JSONObject
                                .class);
                        if (Objects.nonNull(app)) {
                            String addr = app.getString("url");
                            if (StringUtils.isBlank(addr)) {
                                String serviceId = app.getString("serviceId");
                                if (StringUtils.isNotBlank(serviceId)) {
                                    String urls = environment.getProperty(serviceId + ".ribbon.listOfServers");
                                    if (StringUtils.isNotBlank(urls)) {
                                        addr = urls.split(",")[0];
                                    }
                                }
                            }
                            logger.info("SSO-REQUEST-ADDR: " + addr);
                            if (StringUtils.isNotBlank(addr)) {
                                String url = addr + validateUrl;
                                if (addr.endsWith("/") && validateUrl.startsWith("/")) {
                                    url = addr + validateUrl.substring(1);
                                }
                                logger.info("SSO-VALIDATE-URL: " + url + buildUrl(request));
                                // 从http请求获得 用户主键 用户名 或 工号
                                String userKey = client.clientWithCookie(url + buildUrl(request), getCookies(request));
                                /**
                                 * 特殊网关需求 根据用户标识创建用户
                                 */
                                logger.info("SSO-USERKEY: " + userKey);
                                if (openCreateUser && StringUtils.isNotBlank(userKey)) {
                                    String userId = userKey.split("-ace-")[0];
                                    if (sysUserService.count(new QueryWrapper<SysUserDO>()
                                            .eq("id", userId)) == 0) {
                                        SysUserDO sysUserDO = new SysUserDO();
                                        sysUserDO.setId(userId);
                                        sysUserDO.setUserName(userId);
                                        sysUserDO.setStaffNo(userId);
                                        sysUserDO.setRealName(userKey.split("-ace-")[1]);
                                        OrgGroupDO orgGroupDO = orgGroupService.getOne(null);
                                        sysUserDO.setGroupId(orgGroupDO.getId());
                                        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getOne(new
                                                QueryWrapper<OrgOrganizationDO>().eq("group_id", orgGroupDO.getId())
                                                .eq("IS_BUSINESS_UNIT", 1)
                                                .orderByAsc("sort_path"));
                                        sysUserDO.setOrganizationId(orgOrganizationDO.getId());
                                        if (sysUserService.save(sysUserDO)) {
                                            userKey = userId;
                                            logger.info("SSO-ADD-USER: " + userKey);
                                        }
                                    } else {
                                        userKey = userId;
                                    }
                                }
                                if (StringUtils.isNotBlank(userKey)) {
                                    token = sysUserService.getTokenAfterLogin(userKey);
                                    logger.info("SSO-TOKEN: " + token);
                                    if (StringUtils.isNotBlank(token) && cacheUtil.hasKey(token)) {
                                        cacheUtil.set(sessionId, token);
                                        return pass(request, token, response, appName, path, ctx);
                                    }
                                }
                            }
                        }
                    }
                }
                response.setStatus(HttpStatus.SEE_OTHER.value());
                response.setHeader(HttpHeaders.LOCATION, "../");
                ctx.setResponse(response);
                ctx.setSendZuulResponse(false);

            }
        } catch (Exception e) {

        }
        SysUserDO user = getUserByToken(token);
        logger.info("REQUEST_USER: " + user.getUserName() + "(" + user.getRealName() + "), REQUEST_IP:   " + IpUtils
                .getIpAddr(request));
        return null;
    }

    private String pass(HttpServletRequest request, String token, HttpServletResponse response, String appName,
                        String path, RequestContext ctx) {
        try {
            reflectSetparam(request, "token", token);
            Cookie cookie = new Cookie("token", token);
            // tomcat下多应用共享
            cookie.setPath("/");
            // 将Cookie添加到Response中,使之生效
            response.addCookie(cookie);
            Cookie cookie1 = new Cookie("appName", appName);
            // tomcat下多应用共享
            cookie1.setPath("/");
            // 将Cookie添加到Response中,使之生效
            response.addCookie(cookie1);
            ctx.setRequest(request);
            logger.info("SSO-GOTO: " + path);
            response.sendRedirect(path);
            //response.sendRedirect("/" + path.split("/")[1] + "/");
            ctx.setResponse(response);
            ctx.setSendZuulResponse(false);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 根据token获取用户信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public SysUserDO getUserByToken(String token) {
        logger.info("-----token:" +token);
        if (token != null && StringUtils.isNotBlank(token)) {
            SysUserDO sysUserDO = JsonUtils.castObject(cacheUtil.hget(token, "user"), SysUserDO.class);
            logger.info("-----sysUserDO:" +sysUserDO);
            return sysUserDO;
        }
        return null;
    }

    private void reflectSetparam(HttpServletRequest request, String key, String value) {
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field request1 = requestClass.getDeclaredField("request");
            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders) headers.get(o1);
            o2.addValue(key).setString(value);
        } catch (Exception e) {
        }
    }

    public List<String> getCookies(HttpServletRequest request) {
        List<String> cookieList = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        logger.info("修改之前的Cookie: " + JSONObject.toJSONString(cookies));
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookieT : cookies) {
                cookieList.add(cookieT.getName() + "=" + cookieT.getValue());
            }
        }
        logger.info("修改之后的Cookie: " + JSONObject.toJSONString(cookies));
        return cookieList;
    }

    public String buildUrl(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        logger.info("SSO-REQUEST-Params：" + JSONObject.toJSONString(request.getParameterMap()));
        for (String key : request.getParameterMap().keySet()) {
            json.put(key, request.getParameterValues(key)[0]);
        }
        Set<String> keys = json.keySet();
        if (keys.size() == 0) {
            return "";
        }
        String str = "?";
        StringJoiner joiner1 = new StringJoiner("&");
        for (String key : keys) {
            joiner1.add(key + "=" + json.getString(key));
        }
        str = str + joiner1.toString();
        return str;
    }
}
