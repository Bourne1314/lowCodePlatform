package com.csicit.ace.fileserver;

import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author JonnyJiang
 * @date 2019/5/16 10:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileConfigurationControllerTest {
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    FileConfigurationService fileConfigurationService;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void load() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", "");
        map.put("appId", "");
        map.put("configurationKey", "");
        map.put("formId", "");

        MvcResult result = mockMvc.perform(post("/fileConfiguration/load")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(com.alibaba.fastjson.JSONObject.toJSONString(map)))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果

        System.out.println(result.getResponse().getContentAsString());
    }
}
