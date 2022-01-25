package com.csicit.ace.fileserver;

import com.csicit.ace.common.utils.server.R;
import net.minidev.json.JSONObject;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author JonnyJiang
 * @date 2019/5/16 14:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileRepositoryControllerTest {
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void allocateSpace() throws Exception {
        String formId = UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", UUID.randomUUID().toString());
        map.put("formId", formId);
        map.put("secretLevel", 0);
        map.put("md5", "");
        map.put("contentType", "ms-word");
        map.put("fileSize", 1000);
        map.put("chunks", 1);
        map.put("yfId", UUID.randomUUID().toString());
        map.put("configurationKey", "1");
        MvcResult result = mockMvc.perform(post("/fileRepository/allocateSpace").content(JSONObject.toJSONString(map))
                .header("token", FileInfoControllerTest.TOKEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果

        String resultStr = result.getResponse().getContentAsString();
        System.out.println(resultStr);
        R r = com.alibaba.fastjson.JSONObject.parseObject(resultStr, R.class);
        assert (Integer) r.get("code") == 40000;
        assert r.get("fileInfo") != null;
    }
}
