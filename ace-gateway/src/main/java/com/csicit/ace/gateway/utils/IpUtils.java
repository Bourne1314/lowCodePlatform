package com.csicit.ace.gateway.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.Objects;

/**
 * 获取ip地址
 *
 * @author shanwj
 * @date 2019-04-10 18:57:46
 * @version V1.0
 */
public class IpUtils {

    static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    private static final String IP_UNKNOWN = "unknown";

    private static String getValue(ServerHttpRequest request, String key) {
        List<String> list = request.getHeaders().get(key);
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;

    }

    /**
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     * @param request 请求
     * @return java.lang.String
     * @author shanwj
     * @date 2019/4/12 14:55
     */
    public static String getIpAddr(ServerHttpRequest request) {

        if (StringUtils.isNotBlank(getRealIp(request))) {
            return getRealIp(request);
        }
        String ip = getValue(request, "X-Real-IP");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip =  getValue(request,"x-forwarded-for");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip =  getValue(request,"x-real-ip");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip =  getValue(request,"Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip =  getValue(request,"WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getHostString();
        }
        String ips[] = ip.split(",");
        for (int i=ips.length-1;i>=0;i--){
            String tmpip = ips[i].trim();
            if (StringUtils.isNotBlank(tmpip)) return tmpip;
        }
        return ip;
    }
    
    /**
     * 联合nginx bff获取真实ip
     * @param request
     * @return 
     * @author yansiyang
     * @date 2019/9/28 17:33
     */
    public static String getRealIp(ServerHttpRequest request) {

        String ip = getValue(request,"acelocalip");
        if (StringUtils.isNotEmpty(ip) && !Objects.equals(ip, "null") && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip =  getValue(request,"realIp");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = getValue(request,"realip");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip =  getValue(request,"REALIP");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = getValue(request,"X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = getValue(request,"x-real-ip");
        if (StringUtils.isNotEmpty(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        return null;
    }
}
