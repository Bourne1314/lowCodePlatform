package com.csicit.ace.push.controller;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.utils.JsonUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/11 9:00
 */
@RestController
@RequestMapping("/common-methods")
public class CommonMethodsController {
    /**
     * 获取pom文件的版本号
     *
     * @return
     * @author FourLeaves
     * @date 2020/6/11 9:02
     */
    @RequestMapping("/getPomVersion")
    public JSONObject getPomVersion() {
        JSONObject json = new JSONObject();
        try {
            String version;
            ClassPathResource classPathResource = new ClassPathResource("pom.xml");
            JSONObject projectJson = JsonUtils.xmlToJson(classPathResource.getInputStream());
            if (projectJson.containsKey("version")) {
                version = projectJson.getString("version");
            } else {
                version = projectJson.getJSONObject("parent").getString("version");
            }
            json.put("version", version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
