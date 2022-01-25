package com.csicit.ace.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.vo.HttpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

/**
 * http请求
 *
 * @author shanwj
 * @date 2019/7/2 15:59
 */
@Service("client")
public class HttpClient {

    @Nullable
    @Autowired(required = false)
    DiscoveryUtils discoveryUtils;

    @Value("${ace.config.zuul.ip:127.0.0.1}")
    String zuulIp;


    @Value("${ace.config.zuul.port:2100}")
    String zuulPort;

    /**
     * 特殊情况，服务器无法运行Redis，使用Ehcache做内存，
     * 只启动platform保证数据一致行，其余服务通过远程url调用
     */
    /*******************Ehcache Http*******************/

    public final static String EhcacheException = "ace-ehcache-exception";

    /**
     * platform app 的 ip地址
     */
    public static String platformAppIp = "";

    /**
     * platform app 的 端口
     */
    public static int platformAppPort = 0;

    public String getPlatformUrl() {
        if (StringUtils.isBlank(platformAppIp) || platformAppPort == 0) {
            ServiceInstance serviceInstance =
                    discoveryUtils.getOneHealthyInstance("platform");
            platformAppIp = serviceInstance.getHost();
            platformAppPort = serviceInstance.getPort();
        }
        return "http://" + platformAppIp + ":" + platformAppPort;
    }

    public String getForEhcacheForZuul(String url, Map<String, Object> params) {
        url = "http://" + zuulIp + ":" + zuulPort + "/ehcache/action/" + url + buildUrl(params);
        String result = client(url);
        if (StringUtils.isBlank(result)) {
            return result;
        }
        if (result.equals(EhcacheException)) {
            throw new RException("Ehcache获取数据异常！");
        }
        if (result.equals("null")) {
            return null;
        }
        return result;
    }

    public String getForEhcache(String url, Map<String, Object> params) {
        url = getPlatformUrl() + "/ehcache/action/" + url + buildUrl(params);
        String result = client(url);
        if (StringUtils.isBlank(result)) {
            return result;
        }
        if (result.equals(EhcacheException)) {
            throw new RException("Ehcache获取数据异常！");
        }
        if (result.equals("null")) {
            return null;
        }
        return result;
    }
    public String postForEhcache(String url, Map<String, Object> params, Object value) {
        url = "http://" + zuulIp + ":" + zuulPort + "/ehcache/action/" + url + buildUrl(params);
        String result = postReturnString(url, value);
        if (result.equals(EhcacheException)) {
            throw new RException("Ehcache获取数据异常！");
        }
        if (result.equals("null")) {
            return null;
        }
        return result;
    }
    /**************************************************/

    public String clientWithCookie(String url, List<String> cookies) {
       return clientWithCookieAndHeader(url, new HashMap<>(), cookies);
    }

    public String clientWithCookieAndHeader(String url,Map<String, String> headers, List<String> cookies) {
        RestTemplate client = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put("Cookie", cookies);
        for (String key : headers.keySet()) {
            requestHeaders.set(key, headers.get(key));
        }
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        //  执行HTTP请求
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }

    public String client(String url) {
        RestTemplate client = new RestTemplate();
        //  执行HTTP请求
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        return response.getBody();
    }

    public RestTemplate getInstance(String charset) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName(charset)));
        return restTemplate;
    }

    public String postReturnString(String url, Object request) {
        RestTemplate client = new RestTemplate();
        return client.postForObject(url, request, String.class);
    }

    public String clientSaveConfig(String url, HttpMethod method, Map<String, Object> params, String token) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (StringUtils.isNotBlank(token)) {
            headers.setBearerAuth(token);
            headers.set("Authorization", token);
        }
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        params.keySet().forEach(key -> {
            postParameters.set(key, params.get(key));
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(postParameters, headers);

        ResponseEntity<String> response = client.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }

    public String client(String url, HttpMethod method, Map<String, Object> params, String token) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.isNotBlank(token)) {
            headers.setBearerAuth(token);
            headers.set("Authorization", token);
        }
        HttpEntity<Map<String, Object>> requestEntity =
                new HttpEntity<>(params, headers);
//        //  执行HTTP请求
//        if (Objects.equals(method, HttpMethod.GET)) {
//            url += buildUrl(params);
//        }
        url += buildUrl(params);

        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    public String client(String token, String url, HttpMethod method, Map<String, Object> params) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity =
                new HttpEntity<>(params, headers);
        //  执行HTTP请求
        if (Objects.equals(method, HttpMethod.GET)) {
            url += buildUrl(params);
        }

        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    public String buildUrl(Map<String, Object> map) {
        JSONObject json = JsonUtils.castObject(map, JSONObject.class);
        Set<String> keys = json.keySet();
        String str = "?";
        StringJoiner joiner1 = new StringJoiner("&");
        for (String key : keys) {
            joiner1.add(key + "=" + json.getString(key));
        }
        str = str + joiner1.toString();
        return str;
    }

    public String getAppAddr(String appName) {
        if (discoveryUtils != null) {
            ServiceInstance instance = discoveryUtils.getOneHealthyInstance(appName);
            if (instance != null) {
                return instance.getUri().toString();
            }
        }
        return null;
    }

    public HttpVO getAppHttpVo(String appName) {
        HttpVO httpVO = new HttpVO();
        if (discoveryUtils != null) {
            ServiceInstance instance = discoveryUtils.getOneHealthyInstance(appName);
            if (instance != null) {
                httpVO.setScheme("http");
                httpVO.setHost(instance.getHost());
                httpVO.setPort(instance.getPort());
                return httpVO;
            }
        }
        return httpVO;
    }

}
