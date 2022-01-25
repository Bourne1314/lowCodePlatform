package com.csicit.ace.report.core.h5;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author shanwj
 * @date 2020/3/31 10:01
 */
@Component
public class RequestUtils {


    private static String appName;

    @Value("${spring.application.name}")
    private String name;

    @PostConstruct
    public void getAppName() {
        appName = this.name;
    }

    public static URL getReportURL(HttpServletRequest request) throws MalformedURLException {
        String queryString = request.getQueryString();
        String httpStr = queryString.split("&")[1];
        httpStr = httpStr.split("=")[1];
        String scheme = httpStr.split("//")[0];
        String host = httpStr.split("//")[1];
        scheme = scheme.substring(0, scheme.length() - 1);
        String newHost = host.contains(":") ? host.split(":")[0] : host;
        int port = host.contains(":") ? Integer.parseInt(host.split(":")[1]) : 80;
        URL contextURL = new URL(scheme, newHost, port, "/" + appName);
        return contextURL;
    }
}
