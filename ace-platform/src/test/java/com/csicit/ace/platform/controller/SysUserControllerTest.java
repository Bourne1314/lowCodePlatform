package com.csicit.ace.platform.controller;

import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.controller.SysUserController;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SysUserControllerTest extends BaseTest {

    private MockMvc mockMvc;
    @Autowired
    SysUserController sysUserController;

    private void testBefore() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sysUserController).build();
    }

    @Test
    public void listTest1() {
        String token = login("dayuanceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers")
                    .param("current", "1")
                    .param("size", "10")
                    .param("searchString", "")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
                    .param("roleId", "")
                    .param("organizationId", "b96daa08d59c467a9bd6cca17e46f53f")
                    .param("type", "2")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listTest2() {
        String token = login("dayuanceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers")
                    .param("current", "1")
                    .param("size", "10")
                    .param("searchString", "")
                    .param("groupId", "")
                    .param("roleId", "")
                    .param("organizationId", "b96daa08d59c467a9bd6cca17e46f53f")
                    .param("type", "2")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listTest3() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers")
                    .param("current", "1")
                    .param("size", "10")
                    .param("searchString", "")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
                    .param("roleId", "3aca40d1a6594410bc599b6d3f2821f7")
                    .param("organizationId", "")
                    .param("type", "2")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listTest4() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers")
                    .param("current", "1")
                    .param("size", "10")
                    .param("searchString", "")
                    .param("groupId", "")
                    .param("roleId", "3aca40d1a6594410bc599b6d3f2821f7")
                    .param("organizationId", "")
                    .param("type", "2")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listTest5() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers")
                    .param("current", "1")
                    .param("size", "10")
                    .param("searchString", "")
                    .param("groupId", "")
                    .param("roleId", "")
                    .param("organizationId", "")
                    .param("type", "2")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listTest6() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers")
                    .param("current", "1")
                    .param("size", "10")
                    .param("searchString", "")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
                    .param("roleId", "")
                    .param("organizationId", "")
                    .param("type", "2")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listForFlowAgentTest1() {
        String token = login("ceshiuser2");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        Map<String, String> map = new HashMap<>();
        map.put("current", "1");
        map.put("size", "10");
        map.put("searchString", "");
        map.put("roleId", "");
        map.put("depId", "");
        try {
            mvcResult = mockMvc.perform(post("/sysUsers/listForFlowAgent")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(com.alibaba.fastjson.JSONObject.toJSONString(map))
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listForFlowAgentTest2() {
        String token = login("ceshiuser1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        Map<String, String> map = new HashMap<>();
        map.put("current", "1");
        map.put("size", "10");
        map.put("searchString", "");
        map.put("roleId", "3aca40d1a6594410bc599b6d3f2821f7");
        map.put("depId", "");
        try {
            mvcResult = mockMvc.perform(post("/sysUsers/listForFlowAgent")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(com.alibaba.fastjson.JSONObject.toJSONString(map))
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listForFlowAgentTest3() {
        String token = login("ceshiuser3");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        Map<String, String> map = new HashMap<>();
        map.put("current", "1");
        map.put("size", "10");
        map.put("searchString", "");
        map.put("roleId", "");
        map.put("depId", "8c22d74d433d4828bd59b05d7059a8b7");
        try {
            mvcResult = mockMvc.perform(post("/sysUsers/listForFlowAgent")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(com.alibaba.fastjson.JSONObject.toJSONString(map))
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listForDepTest1() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        Map<String, String> map = new HashMap<>();
        map.put("current", "1");
        map.put("size", "10");
        map.put("searchString", "");
        map.put("selectValueId", "groupSel");
        map.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        map.put("orgId", "");
        map.put("depId", "");
        try {
            mvcResult = mockMvc.perform(post("/sysUsers/list/forGroupOrOrgOrDep")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(com.alibaba.fastjson.JSONObject.toJSONString(map))
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listForDepTest2() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        Map<String, String> map = new HashMap<>();
        map.put("current", "1");
        map.put("size", "10");
        map.put("searchString", "");
        map.put("selectValueId", "orgSel");
        map.put("groupId", "");
        map.put("orgId", "b96daa08d59c467a9bd6cca17e46f53f");
        map.put("depId", "");
        try {
            mvcResult = mockMvc.perform(post("/sysUsers/list/forGroupOrOrgOrDep")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(com.alibaba.fastjson.JSONObject.toJSONString(map))
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void listForDepTest3() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        Map<String, String> map = new HashMap<>();
        map.put("current", "1");
        map.put("size", "10");
        map.put("searchString", "");
        map.put("selectValueId", "depSel");
        map.put("groupId", "");
        map.put("orgId", "");
        map.put("depId", "8c22d74d433d4828bd59b05d7059a8b7");
        try {
            mvcResult = mockMvc.perform(post("/sysUsers/list/forGroupOrOrgOrDep")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(com.alibaba.fastjson.JSONObject.toJSONString(map))
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

    @Test
    public void infoTest() {
        String token = login("ceshiuser1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/sysUsers/action/info")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }

}