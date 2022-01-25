package com.csicit.ace.platform.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/8 14:14
 */
@RestController
@RequestMapping("/nacos")
public class NacosController {

    @Autowired
    HttpClient client;

    @Value("${spring.cloud.nacos.discovery.server-addr:#{null}}")
    String nacosServerAddr;

    /**
     * 获取实例列表
     * @param params
     * @return 
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping("/instance/list")
    public Object listInstances(@RequestParam Map<String, Object> params) {
        params.put("clusterName", "DEFAULT");
        return client("/v1/ns/catalog/instances", HttpMethod.GET, params);
    }

    /**
     * 获取实例详情
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping("/instance")
    public Object getInstance(@RequestParam Map<String, Object> params) {
        return client("/v1/ns/instance", HttpMethod.GET,  params);
    }

    /**
     * 更新实例
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/instance", method = RequestMethod.PUT)
    public Object updateInstance(@RequestParam Map<String, Object> params) {
        return clientSaveOrUpdate("/v1/ns/instance", HttpMethod.PUT, params);

    }

    /**
     * 获取命名空间列表
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping("/namespace/list")
    public Object getNamespaces(@RequestParam Map<String, Object> params) {
        params.put("clusterName", "DEFAULT");
        return client("/v1/console/namespaces", HttpMethod.GET, params);
    }

    /**
     * 获取命名空间
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/namespace", method = RequestMethod.POST)
    public Object saveNamespace(@RequestParam Map<String, Object> params) {
        return clientSaveOrUpdate("/v1/console/namespaces", HttpMethod.POST, params);
    }

    /**
     * 删除命名空间
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/namespace", method = RequestMethod.DELETE)
    public Object removeNamespace(@RequestParam Map<String, Object> params) {
        return clientDelete("/v1/console/namespaces", HttpMethod.DELETE, params);
    }

    /**
     * 获取服务列表
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/service/list")
    public Object listServices(@RequestParam Map<String, Object> params) {
        return client("/v1/ns/catalog/services", HttpMethod.GET, params);
    }

    /**
     * 获取服务详情
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/service")
    public Object getService(@RequestParam Map<String, Object> params) {
        return client("/v1/ns/service", HttpMethod.GET, params);
    }

    /**
     * 获取配置列表
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/config/list")
    public Object listConfigs(@RequestParam Map<String, Object> params) {
        return client("/v1/cs/configs", HttpMethod.GET, params);
    }

    /**
     * 获取配置
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/config")
    public Object getConfig(@RequestParam Map<String, Object> params) {
        return client("/v1/cs/configs", HttpMethod.GET, params);
    }

    /**
     * 更新配置
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public Object saveConfig(@RequestBody Map<String, Object> params) {
        return clientSaveOrUpdate("/v1/cs/configs", HttpMethod.POST, params);
    }

    /**
     * 删除配置
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/config", method = RequestMethod.DELETE)
    public Object deleteConfig(@RequestParam Map<String, Object> params) {
        return clientDelete("/v1/cs/configs", HttpMethod.DELETE, params);
    }

    /**
     * 获取配置历史
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    @RequestMapping(value = "/config/history")
    public Object history(@RequestParam Map<String, Object> params) {
        return client("/v1/cs/history", HttpMethod.GET, params);
    }


    /**
     * 模拟请求
     * @param params
     * @param url 请求路径
     * @param httpMethod 请求方法
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    public JSONObject client(String url, HttpMethod httpMethod, Map<String, Object> params) {
        if (StringUtils.isNotBlank(nacosServerAddr)) {
            url = "http://" + nacosServerAddr + "/nacos" + url;
            try {
                String str = client.client(url, httpMethod, params, loginNacos());
                if (StringUtils.isNotBlank(str)) {
                    return JSONObject.parseObject(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 模拟请求-保存更新
     * @param params
     * @param url 请求路径
     * @param httpMethod 请求方法
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    public String clientSaveOrUpdate(String url, HttpMethod httpMethod, Map<String, Object> params) {
        if (StringUtils.isNotBlank(nacosServerAddr)) {
            url = "http://" + nacosServerAddr + "/nacos" + url;
            try {
                String str = client.clientSaveConfig(url, httpMethod, params, loginNacos());
                if (StringUtils.isNotBlank(str)) {
                    return str;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 请求-删除
     * @param params
     * @param url 请求路径
     * @param httpMethod 请求方法
     * @return
     * @author FourLeaves
     * @date 2020/6/8 14:53
     */
    public String clientDelete(String url, HttpMethod httpMethod, Map<String, Object> params) {
        if (StringUtils.isNotBlank(nacosServerAddr)) {
            url = "http://" + nacosServerAddr + "/nacos" + url;
            try {
                String str = client.client(url, httpMethod, params, loginNacos());
                if (StringUtils.isNotBlank(str)) {
                    return str;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String loginNacos() {
        if (StringUtils.isNotBlank(nacosServerAddr)) {
            String url = "http://" + nacosServerAddr + "/nacos/v1/auth/login";
            Map<String, Object> params = new HashMap<>();
            params.put("username", "nacos");
            params.put("password", "nacos");
            try {
                String str = client.client(url, HttpMethod.POST, params, null);
                if (StringUtils.isNotBlank(str)) {
                    JSONObject jsonObject = JSONObject.parseObject(str);
                    if (StringUtils.isNotBlank((String) jsonObject.get("data"))) {
                        return (String) jsonObject.get("data");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
