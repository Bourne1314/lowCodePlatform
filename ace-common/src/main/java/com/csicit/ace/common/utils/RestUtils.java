package com.csicit.ace.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Objects;

/**
 * API工具类
 *
 * @author JonnyJiang
 * @date 2019/6/6 9:27
 */
public class RestUtils {
    /**
     * 通过API验证
     *
     * @param api    验证API
     * @param object 参数
     * @return java.lang.Boolean 验证通过/失败
     * @author JonnyJiang
     * @date 2019/6/6 9:26
     */

    public static Boolean check(String api, Object object) {
        if (StringUtils.isNotEmpty(api)) {
            SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(api);
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(object), "UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            httpPost.addHeader("content-type", "application/json;charset=UTF-8");
            httpPost.addHeader("token", securityUtils.getToken());
            try {
                HttpResponse response = httpClient.execute(httpPost);
                response.addHeader("content-type", "application/json;charset=UTF-8");
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    return JSONObject.parseObject(result, Boolean.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 通知API
     *
     * @param api    验证API
     * @param object 参数
     * @author JonnyJiang
     * @date 2019/6/6 9:26
     */

    public static R notify(String api, Object object) {
        if (StringUtils.isNotEmpty(api)) {
            try {
                SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(api);
                StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(object), "UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                httpPost.addHeader("content-type", "application/json;charset=UTF-8");
                httpPost.addHeader("token", securityUtils.getToken());
                HttpResponse response = httpClient.execute(httpPost);
                response.addHeader("content-type", "application/json;charset=UTF-8");
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    return JSONObject.parseObject(result, R.class);
                }
            } catch (Exception e) {
                return R.error(e.getMessage());
            }
        }
        return R.ok();
    }
}
