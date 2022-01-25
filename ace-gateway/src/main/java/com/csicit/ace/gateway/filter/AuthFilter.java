package com.csicit.ace.gateway.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.cache.SerializeUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.cipher.SM2Util;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.gateway.properties.PassProperties;
import com.csicit.ace.gateway.utils.GatewaySecurityUtils;
import com.csicit.ace.gateway.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 入口过滤器
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/26 16:49
 */
@Configuration
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    AceLogger logger;
    /**
     * 网关安全工具对象
     */
    @Autowired
    GatewaySecurityUtils gatewaySecurityUtils;

    @Autowired
    HttpClient client;

    @Autowired
    PassProperties passProperties;

    /**
     * 是否允许同时登录
     */
    @Value("${ace.config.multipleLogin:true}")
    private boolean multipleLogin;

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
     * 单点登录接口 不包含应用名
     */
    @Value("${ace.config.sso.validateUrl}")
    private String validateUrl;

    /**
     * 是否指定单点登录接口对应的APP
     */
    @Value("${ace.config.sso.onlyOneApp:false}")
    private boolean onlyOneApp;

    /**
     * 单点登录接口对应的APP
     */
    @Value("${ace.config.sso.appName}")
    private String theApp;

    /**
     * nacos 地址
     */
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosAddress;

    /**
     * redis访问工具对象
     */
    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    private static Map<String, String> pushIpMap = new HashMap<>(16);
    /**
     * url匹配器
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private static String PublicStr = "rO0ABXNyADxvcmcuYm91bmN5Y2FzdGxlLmpjYWpjZS5wcm92aWRlci5hc3ltbWV0cm" +
            "ljLmVjLkJDRUNQdW" +
            "JsaWNLZXkhn3qKo-pIJAMAAloAD3dpdGhDb21wcmVzc2lvbkwACWFsZ29yaXRobXQAEkxqYXZhL2xhbmcvU3RyaW5nO3hw" +
            "AHQAAkVDdXIAAltCrPMX-AYIVOACAAB4cAAAATcwggEzMIHsBgcqhkjOPQIBMIHgAgEBMCwGByqGSM49AQECIQD____-" +
            "_____________________wAAAAD__________zBEBCD____-_____________________wAAAAD__________AQgKOn6np" +
            "2fXjRNWp5Lz2UJp_OXifUVq4-S3by9QU2UDpMEQQQyxK4sHxmBGV-ZBEZqOcmUj-MLv_JmC-FxWkWJM0x0x7w3NqL09nec" +
            "Wb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA_____v_______________3ID32shxgUrU7v0CTnVQSMCAQEDQgAEY7yAGa" +
            "P0izmHvg2AUuJHaU2gNxz0PRTJSPN4eluAaxu2N8tkA9f_eFt_w4oMNYjO8xBBjX6gPAvm3zJ0l2f7J3g=";

    static String[] approveAppUrls = {"/api/", "/platform/", "/orgauth/", "/quartz/", "/report/", "/dashboards/",
            "/fileserver/", "/push/"};

    /**
     * 主过滤器
     *
     * @param exchange
     * @param chain
     * @return
     * @author yansiyang
     * @date 2019/5/5 14:22
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 验证url是否可放行
        String path = exchange.getRequest().getPath().value();
        // 获取jar的版本号
        if (path.startsWith("/common-methods/getPomVersion")) {
            JSONObject map = new JSONObject();
            String version = null;
            try {
                ClassPathResource classPathResource = new ClassPathResource("pom.xml");
                JSONObject projectJson = JsonUtils.xmlToJson(classPathResource.getInputStream());
                if (projectJson.containsKey("version")) {
                    version = projectJson.getString("version");
                } else {
                    version = projectJson.getJSONObject("parent").getString("version");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("version", version);
            return chain.filter(exchange.mutate().response(makeReturnJson(exchange, map)).build());
        }

        if (matchUrl(exchange, path)) {
            return chain.filter(exchange);
        }
        String accessToken = null;
        try {
            accessToken = gatewaySecurityUtils.getToken(exchange.getRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(accessToken)) {
            // 判断是否同时登陆
            if (cacheUtil.hasKey(accessToken)) {
                if (!multipleLogin) {
                    String userId = gatewaySecurityUtils.getCurrentUserId();
                    if (StringUtils.isNotBlank(userId)) {
                        String latestToken = cacheUtil.get(userId);
                        // 比较最新登录的用户的token
                        if (StringUtils.isNotBlank(latestToken) && !Objects.equals(accessToken, latestToken)) {
                            JSONObject map = new JSONObject();
                            map.put("code", HttpCode.AUTHENTICATION_FAILURE);
                            map.put("msg", "您的账号在别处登录，请确认！");
                            return chain.filter(exchange.mutate().response(makeReturnJson(exchange, map)).build());
                        }
                    }
                }
            } else {
                // redis上token过期
                // 返回token失效错误提示
                boolean approve = false;
                for (int i = 0; i < approveAppUrls.length; i++) {
                    if (path.contains(approveAppUrls[i])) {
                        approve = true;
                        break;
                    }
                }
                if (approve) {
                    JSONObject map = new JSONObject();
                    map.put("code", HttpCode.AUTHENTICATION_FAILURE);
                    map.put("msg", "Token失效，请重新登录!");
                    map.put("showMsg", notshowTokenError(exchange));
                    return chain.filter(exchange.mutate().response(makeReturnJson(exchange, map)).build());
                }
            }
        }

        if (StringUtils.isBlank(accessToken) || !cacheUtil.hasKey(accessToken)) {
            // 校验feign请求
            String aceFeignKey = gatewaySecurityUtils.getValueFromHeader(exchange.getRequest(), "aceFeignKey");
            if (StringUtils.isNotBlank(aceFeignKey)) {
                try {
                    aceFeignKey = GMBaseUtil.decryptString(aceFeignKey);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    JSONObject map = new JSONObject();
                    map.put("code", HttpCode.SM4_KEY_EXCEPTION);
                    map.put("msg", "密钥异常！");
                    return chain.filter(exchange.mutate().response(makeReturnJson(exchange, map)).build());
                }
                JSONObject jsonObject = JSONObject.parseObject(aceFeignKey);
                logger.info("REQUEST_APP: " + jsonObject.get("appName") + "， REQUEST_IP:   " + IpUtils
                        .getIpAddr(exchange.getRequest()));

                Mono<Void> filter = chain.filter(exchange);
                return filter;
            } else {
                String value = exchange.getRequest().getPath().value();
                // 判断是否开启单点登录
                boolean approve = false;
                for (int i = 0; i < approveAppUrls.length; i++) {
                    if (value.startsWith(approveAppUrls[i])) {
                        approve = true;
                        break;
                    }
                }
                if (openGatewaySso && openSso && StringUtils.isNotBlank(validateUrl) && !approve) {
                    logger.info("*************************[ SSO ]*******************************");
                    logger.info("SSO-REQUEST-URL: " + value);
                    logger.info("SSO-REQUEST_IP:   " + IpUtils
                            .getIpAddr(exchange.getRequest()));
                    logger.info("SSO-OPEN: True");
                    //截取appName
                    String appName = null;
                    if (value.length() != 1 && value.split("/").length > 1) {
                        appName = value.split("/")[1];
                    }
                    if (onlyOneApp && StringUtils.isNotBlank(theApp)) {
                        appName = theApp;
                    }
                    if (StringUtils.isNotBlank(appName)) {
                        //从nacos获取对应的地址
                        String addr = client.getAppAddr(appName);
                        if (StringUtils.isNotBlank(addr)) {
                            String url = addr + validateUrl;
                            logger.info("*************************[ SSO ]*******************************");
                            logger.info("SSO-VALIDATE-URL: " + url + gatewaySecurityUtils.buildUrl(exchange.getRequest()));
                            //logger.info("SSO-VALIDATE-HEADER: " + url + gatewaySecurityUtils.getHeaders(exchange.getRequest()));
                            //logger.info("SSO-VALIDATE-COOKIE: " + url + gatewaySecurityUtils.getCookies(exchange.getRequest()));
                            // 从http请求获得 用户主键 用户名 或 工号
                            String userKey = client.clientWithCookie(url + gatewaySecurityUtils.buildUrl(exchange.getRequest()), gatewaySecurityUtils.getCookies(exchange
                                    .getRequest()));
                            logger.info("*************************[ SSO ]*******************************");
                            logger.info("SSO-USERKEY: " + userKey);
                            if (StringUtils.isNotBlank(userKey)) {
                                //从nacos获取 orgauth 对应的地址
                                String authAddr = client.getAppAddr("orgauth");
                                if (StringUtils.isNotBlank(authAddr)) {
                                    url = authAddr + "/orgauth/login/action/getTokenAfterLogin/" + userKey;
                                    logger.info("*************************[ SSO ]*******************************");
                                    logger.info("SSO-ORGAUTH-URL: " + url);
                                    // 从 auth服务 请求获得 token
                                    String token = this.client.client(url);
                                    logger.info("*************************[ SSO ]*******************************");
                                    logger.info("SSO-TOKEN: " + token);
                                    if (StringUtils.isNotBlank(token) && cacheUtil.hasKey(token)) {
                                        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().header
                                                ("token", token)
                                                .build();
                                        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
                                        //exchange.mutate().request(serverHttpRequest).build();
                                        ResponseCookie cookie = ResponseCookie.from("token", token).build();
                                        ServerHttpResponse response = exchange.getResponse();
                                        response.getCookies().add("token", cookie);
                                        ResponseCookie appCookie = ResponseCookie.from("appName", appName).build();
                                        response.getCookies().add("appName", appCookie);
                                        ServerWebExchange buildT = exchange.mutate().response(response).build();
                                        Mono<Void> filter = chain.filter(buildT);
                                        return filter;
                                    }
                                }
                            }
                        }
                    }
                }
                String url = "/" +
                        "?originUrl=" + value;
                ServerHttpResponse response = exchange.getResponse();
                //303状态码表示由于请求对应的资源存在着另一个URI，应使用GET方法定向获取请求的资源
                response.setStatusCode(HttpStatus.SEE_OTHER);
                response.getHeaders().set(HttpHeaders.LOCATION, url);
                response.getHeaders().set("originUrl", exchange.getRequest().getPath().value());
                return response.setComplete();
            }
        }

        SysUserDO user = gatewaySecurityUtils.getCurrentUser();
        logger.info("REQUEST_USER: " + user.getUserName() + "(" + user.getRealName() + "), REQUEST_IP:   " + IpUtils
                .getIpAddr(exchange.getRequest()));

        Mono<Void> filter = chain.filter(exchange);
        return filter;
    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return -1;
    }


    /**
     * 返回40001，是否显示错误信息
     *
     * @param null
     * @return
     * @author FourLeaves
     * @date 2019/12/16 9:16
     */
    static String[] notShowTokenErrorStrs = {"/tree"};

    private int notshowTokenError(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        for (int i = 0; i < notShowTokenErrorStrs.length; i++) {
            if (path.contains(notShowTokenErrorStrs[i])) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 验证url是否可放行
     *
     * @param exchange
     * @return
     * @author yansiyang
     * @date 2019/4/12 11:33
     */

    static String[] urls = {"/api/**", "/platform/login", "/platform/logout", "/orgauth/login", "/orgauth/logout"};
    static String[] startStrs = {"/js/", "/magicalcoder/", "/css/", "/img/", "/static/", "/fonts/", "/pages/", "/nacos"};
    static String[] containStrs = {"favicon", "/init", "/ssoRequest", "/static/", "/downloadImg/", "/acewx/", "/common-methods/getPomVersion"};
    static String[] needTokenStrs = {"/webservice/"};

    private boolean matchUrl(ServerWebExchange exchange, String path) {
        logger.info("REQUEST_URL:" + path + ", REQUEST_METHOD:" + exchange.getRequest().getMethodValue()
                + ", REQUEST_IP:"+ IpUtils.getIpAddr(exchange.getRequest()));
        if (path.length() == 1) {
            return true;
        }
        for (int i = 0; i < startStrs.length; i++) {
            if (path.startsWith(startStrs[i])) {
                return true;
            }
        }

        // 放行指定app的静态资源
        if (!CollectionUtils.isEmpty(passProperties.getPassAppList())) {
            for (String appNameT : passProperties.getPassAppList()) {
                if (StringUtils.isNotBlank(appNameT)) {
                    if (Objects.equals(path, "/" + appNameT) || Objects.equals(path, "/" + appNameT + "/")) {
                        return true;
                    }
                    for (int i = 0; i < startStrs.length; i++) {
                        if (path.startsWith("/" + appNameT + startStrs[i])) {
                            return true;
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(passProperties.getPassUrlList())) {
            for (int i = 0; i < passProperties.getPassUrlList().size(); i++) {
                if (path.startsWith(passProperties.getPassUrlList().get(i))) {
                    return true;
                }
            }
        }
        for (int i = 0; i < containStrs.length; i++) {
            if (path.contains(containStrs[i])) {
                return true;
            }
        }
        for (int i = 0; i < urls.length; i++) {
            if (pathMatcher.match(urls[i], path)) {
                return true;
            }
        }
//        //对单点登录url进行放行
//        if (openSso) {
//            if (StringUtils.isNotBlank(completedValidateUrl)) {
//                if (Objects.equals(path, completedValidateUrl)) {
//                    return true;
//                }
//            }
//        }

        for (int i = 0; i < needTokenStrs.length; i++) {
            if (path.contains(needTokenStrs[i])) {
                String token = setToken();
                ServerHttpRequest request = exchange.getRequest().mutate().header("token", token).build();
                exchange.mutate().request(request).build();
                return true;
            }
        }
        return false;
    }


    public String setToken() {
        String tokenStr = "webserviceToken";
        if (cacheUtil.hasKey(tokenStr)) {
            String token = cacheUtil.get(tokenStr);
            if (StringUtils.isNotBlank(token)) {
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

    private R checkLicense(ServerWebExchange exchange) throws Exception {
        String license = cacheUtil.get("platform_secret_key");
        String message = "";
        int code = 200;
        if (StringUtils.isEmpty(license)) {
            message = "无授权信息!";
            code = 500;
            return new R().put("code", code).put("msg", message);
        }

        //授权文件信息进行验签
        JSONObject jsonObject = JSONObject.parseObject(license);
        String sign = jsonObject.getString("sign");
        jsonObject.remove("sign");
        String licenseStr = jsonObject.toString();
        byte[] publicBytes = Base64Utils.decodeFromUrlSafeString(PublicStr);
        BCECPublicKey publicKeyPair = (BCECPublicKey) SerializeUtils.toObject(publicBytes);
        boolean verify = SM2Util.verify(publicKeyPair, null,
                licenseStr.getBytes("UTF-8"), Base64Utils.decodeFromUrlSafeString(sign));
        if (!verify) {
            message = "授权信息验证失败!";
            code = 500;
            return new R().put("code", code).put("msg", message);
        }
        String type = jsonObject.getString("type");
        //平台应用不需要授权验证
        String value = exchange.getRequest().getPath().value();
        String[] values = value.split("/");
        if (values.length == 0) {
            return R.ok();
        }
        String appName = values[1];
        if (!appName.contains("-")) {
            return R.ok();
        }
        //验收授权应用数量是否超标
        appName = appName.substring(3, appName.length());
        JSONArray array = jsonObject.getJSONArray("apps");
        String endDate = null;
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String appId = object.getString("appId").toLowerCase();
            if (Objects.equals(appName, appId)) {
                endDate = object.getString("endDate");
                break;
            }
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate licenseDt = LocalDate.parse(endDate, df);
        LocalDate now = LocalDate.now();
        if (Objects.equals(type, "prod") && now.isAfter(licenseDt)) {
            logger.error("当前应用已超授权时间!");
            System.exit(-1);
        }
        return new R().put("code", code).put("msg", message);
    }

    /**
     * 实现网关返回json的方法
     *
     * @param exchange
     * @param json
     * @return
     * @author yansiyang
     * @date 2019/5/6 8:42
     */
    private ServerHttpResponseDecorator makeReturnJson(ServerWebExchange exchange, JSONObject json) {
        /**
         * 返回json
         */
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator serverHttpResponseDecorator =
                new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        List<Integer> times = new ArrayList<>();
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            /**
                             * 避免缓存未清除  写入两次
                             */
                            if (times.size() > 0) {
                                return bufferFactory.wrap(new String("").getBytes(Charset.forName("UTF-8")));
                            } else {
                                times.add(1);
                                /**
                                 * 释放内存
                                 */
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                /**
                                 * 返回错误码及提示
                                 */
                                return bufferFactory.wrap(json.toJSONString().getBytes(Charset.forName("UTF-8")));
                            }
                        }));
                    }
                };
        return serverHttpResponseDecorator;
    }

    class DelegatingServiceInstance implements ServiceInstance {
        final ServiceInstance delegate;
        private String overrideScheme;

        DelegatingServiceInstance(ServiceInstance delegate, String overrideScheme) {
            this.delegate = delegate;
            this.overrideScheme = overrideScheme;
        }

        @Override
        public String getServiceId() {
            return delegate.getServiceId();
        }

        @Override
        public String getHost() {
            return delegate.getHost();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public boolean isSecure() {
            return delegate.isSecure();
        }

        @Override
        public URI getUri() {
            return delegate.getUri();
        }

        @Override
        public Map<String, String> getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public String getScheme() {
            String scheme = delegate.getScheme();
            if (scheme != null) {
                return scheme;
            }
            return this.overrideScheme;
        }

    }
}
