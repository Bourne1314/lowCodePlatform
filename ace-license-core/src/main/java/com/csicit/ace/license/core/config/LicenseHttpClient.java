package com.csicit.ace.license.core.config;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author FourLeaves
 * @date 2021/9/18 9:42
 */
@Service("licenseHttpClient")
public class LicenseHttpClient {

    public String client(String url) {
        RestTemplate client = new RestTemplate();
        //  执行HTTP请求
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        return response.getBody();
    }


    public String buildUrl(Map<String, String> map) {
        Set<String> keys = map.keySet();
        String str = "?";
        StringJoiner joiner1 = new StringJoiner("&");
        for (String key : keys) {
            joiner1.add(key + "=" + map.get(key));
        }
        str = str + joiner1.toString();
        return str;
    }

}
