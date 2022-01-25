package com.csicit.ace.zuul.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/13 9:38
 */
@Configuration
@ConfigurationProperties(prefix = "ace.config")
public class PassProperties {
    /**
     * 允许没有token就请求的url
     */
    private List<String> passUrlList = new ArrayList<>();

    /**
     * 允许没有token就指定app的静态资源
     */
    private List<String> passAppList = new ArrayList<>();

    public List<String> getPassUrlList() {
        return passUrlList;
    }

    public void setPassUrlList(List<String> passUrlList) {
        this.passUrlList = passUrlList;
    }

    public List<String> getPassAppList() {
        return passAppList;
    }

    public void setPassAppList(List<String> passAppList) {
        this.passAppList = passAppList;
    }
}
