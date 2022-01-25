package com.csicit.ace.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 安全方面公共接口工具类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Component
public class GatewaySecurityUtils {

    private static Logger logger = LoggerFactory.getLogger(GatewaySecurityUtils.class);

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private CacheUtil cacheUtil;

    private static String token;

    /**
     * 获取当前会话
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
//    public HttpSession getSession(ServerHttpRequest request) {
//        return request.getSession();
//    }

    public String getValueFromHeader(ServerHttpRequest request, String key) {
        String value = null;
        List<String> strings = request.getHeaders().get(key);
        if (CollectionUtils.isNotEmpty(strings)) {
            value = strings.get(0);
        }
        return value;
    }

    /**
     * 获取token
     *
     * @param request ServerHttpRequest
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public String getToken(ServerHttpRequest request) throws Exception {
        logger.debug("--------------REQUEST----------------");
        logger.debug(JSONObject.toJSONString(request));
        //从header中获取token
        List<String> strings = request.getHeaders().get("Authorization");
        logger.debug("-------------Authorization------------");
        logger.debug(JSONObject.toJSONString(strings));
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0);
        }
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(authToken)) {
            strings = request.getHeaders().get("token");
            logger.debug("-------------Header------------");
            logger.debug(JSONObject.toJSONString(strings));
            if (strings != null) {
                authToken = strings.get(0);
            }
        }
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(authToken)) {
            strings = request.getQueryParams().get("token");
            logger.debug("-------------Query------------");
            logger.debug(JSONObject.toJSONString(strings));
            if (strings != null) {
                authToken = URLDecoder.decode(strings.get(0), "UTF-8");
            }
        }
        if (StringUtils.isBlank(authToken)) {
            authToken = getTokenByCookie(request);
        }
        if (Objects.equals(authToken, "null")) {
            authToken = null;
        }

        if (StringUtils.isNotBlank(authToken)) {
            try {
                String resultToken = URLDecoder.decode(authToken, "utf-8");
                token = resultToken;
                return resultToken;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
            }
        }
        return authToken;
    }

    public String getTokenByCookie(ServerHttpRequest request) throws UnsupportedEncodingException {
        String token = null;
        List<String> strings = request.getHeaders().get("Cookie");
        logger.debug("-------------Query------------");
        logger.debug(JSONObject.toJSONString(strings));
        if (strings != null) {
            String str = strings.get(0);
            if (str.contains("token=")) {
                token = URLDecoder.decode(str.split("token=")[1].split(";")[0], "utf-8");
            }
        }
        return token;
    }

    /**
     * gateway制作请求参数
     * @param request
     * @return 
     * @author FourLeaves
     * @date 2020/11/12 8:57
     */
    public String buildUrl(ServerHttpRequest request) {
        JSONObject json = new JSONObject();
        logger.info("SSO-REQUEST-Params：" + JSONObject.toJSONString(request.getQueryParams()));
        for (String key : request.getQueryParams().keySet()) {
            json.put(key, request.getQueryParams().get(key).get(0));
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

    /**
     * gateway制作请求header
     * @param request
     * @return
     * @author FourLeaves
     * @date 2020/11/12 8:57
     */
    public Map<String, String> getHeaders(ServerHttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        for (String key : request.getHeaders().keySet()) {
            headers.put(key, request.getHeaders().getFirst(key));
        }
        return headers;
    }

    /**
     * gateway制作cookies
     *
     * @param request
     * @return
     * @author yansiyang
     * @date 2019/9/28 16:50
     */
    public List<String> getCookies(ServerHttpRequest request) {
        List<String> cookies = new ArrayList<>();
        List<String> strings = request.getHeaders().get("Cookie");
        logger.info("修改之前的Cookie: " + JSONObject.toJSONString(strings));
        if (strings != null && strings.size() > 0) {
            for (String str : strings) {
                String[] sttrs = str.split(";");
                if (sttrs != null && sttrs.length > 0) {
                    for (String strrr : sttrs) {
                        if (strrr.contains("=")) {
                            String[] strrrs = strrr.split("=");
                            if (strrrs != null && strrrs.length > 1) {
                                cookies.add(strrr);
                            }
                        }
                    }
                }
            }
        }
        logger.info("修改之后的Cookie: " + JSONObject.toJSONString(cookies));
        return cookies;
    }

    /**
     * 获取前端UI服务名称
     *
     * @param request ServerHttpRequest
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public String getUiName(ServerHttpRequest request) {
        String uiName = null;
        List<String> strings = request.getHeaders().get("Cookie");
        if (strings != null) {
            String str = strings.get(0);
            uiName = str.split("uiName=")[1].split(";")[0];
        }
        return uiName;
    }

    /**
     * 获取当前用户ID
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/16 17:40
     */
    public String getCurrentUserId() {
        if (StringUtils.isNotBlank(token)) {
            try {
                JSONObject json = GMBaseUtil.decryptToken(token);
                return json.getString("userId");
            } catch (Exception e) {

            }
            String userId = cacheUtil.get(token + "userid");
            if (StringUtils.isNotBlank(userId)) {
                return userId;
            }
            SysUserDO user = getCurrentUser();
            if (user == null) {
                return null;
            }
            return user.getId();
        }
        return null;
    }

    /**
     * 获取当前用户
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public SysUserDO getCurrentUser() {
        return getUserByToken();
    }

    /**
     * 根据token获取用户信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public SysUserDO getUserByToken() {
        if (token != null && StringUtils.isNotBlank(token)) {
            SysUserDO sysUserDO = JsonUtils.castObject(cacheUtil.hget(token, "user"), SysUserDO.class);
            return sysUserDO;
        }
        return null;
    }

}
