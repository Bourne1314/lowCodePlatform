package com.csicit.ace.common.utils;

import com.csicit.ace.common.config.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取ip地址
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
public class IpUtils {

    static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    private static final String IP_UNKNOWN = "unknown";

    private static Environment environment = SpringContextUtils.getBean(Environment.class);
    private static String hasGeer = environment.getProperty("ace.config.hasGeer");

    /**
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     *
     * @param request 请求
     * @return java.lang.String
     * @author shanwj
     * @date 2019/4/12 14:55
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            if (hasGeer.equals("yes")) {
                ip = getGeerIp(request);
                if (StringUtils.isNotBlank(ip)) {
                    return ip;
                }
            }
        } catch (Exception e) {

        }
        ip = getRealIp(request);
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String ips[] = ip.split(",");
        for (int i = ips.length - 1; i >= 0; i--) {
            String tmpip = ips[i].trim();
            if (!tmpip.equals("")) return tmpip;
        }
        return ip;
    }

    /**
     * 适配格尔网关 获取 真实客户端ip
     *
     * @param request
     * @return
     */
    private static String getGeerIp(HttpServletRequest request) {
        String ip = null;
        try {
            if (request == null) {
                logger.info("request is null");
            }
            Cookie[] cookies = request.getCookies();
            int count = cookies.length;
            if (count == 0) {
                logger.info("cookie is empty");
                return ip;
            }
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if ("KOAL_CLIENT_IP".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    ip = new String(value.getBytes("ISO-8859-1"), "GBK");
                    ip = ip.replaceAll(" ", "");
                    break;
                }
            }
        } catch (Exception e) {
            logger.info("getGeerIp error");
        }
        return ip;
    }

    /**
     * 联合nginx bff获取真实ip
     *
     * @param request
     * @return
     * @author yansiyang
     * @date 2019/9/28 17:33
     */
    public static String getRealIp(HttpServletRequest request) {

        String ip = request.getHeader("acelocalip");
        if (StringUtils.isNotEmpty(ip) && !Objects.equals(ip, "null") && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("realIp");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("realip");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("REALIP");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("x-real-ip");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        return null;
    }

    /**
     * 获取第一个IP地址
     *
     * @param request 请求
     * @return java.lang.String
     * @author shanwj
     * @date 2019/4/12 14:57
     */
    public static String getFirstIpAddress(HttpServletRequest request) {

        if (StringUtils.isNotBlank(getRealIp(request))) {
            return getRealIp(request);
        }
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("x-real-ip");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
