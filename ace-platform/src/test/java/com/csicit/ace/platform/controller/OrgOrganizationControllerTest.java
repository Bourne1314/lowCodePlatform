package com.csicit.ace.platform.controller;

import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.controller.OrgOrganizationController;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrgOrganizationControllerTest extends BaseTest {

    private MockMvc mockMvc;
    @Autowired
    OrgOrganizationController orgOrganizationController;

    private void testBefore() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orgOrganizationController).build();
    }


    @Test
    public void listGroupAndOrgTest1() {
        String token = login("dayuanceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations/action/listGroupAndOrg")
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
    public void listGroupAndOrgTest2() {
        String token = login("dayuanceshiren2");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations/action/listGroupAndOrg")
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
    public void listGroupAndOrgTest3() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations/action/listGroupAndOrg")
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
    public void listTest1() {
        String token = login("dayuanceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
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
        String token = login("dayuanceshiren2");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations")
                    .param("groupId", "")
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
            mvcResult = mockMvc.perform(get("/orgOrganizations")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
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
            mvcResult = mockMvc.perform(get("/orgOrganizations")
                    .param("groupId", "")
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
    public void listAllTest() {
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations/list/all")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
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
    public void listByGroupTest1() {
        String token = login("dayuanceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations/action/listByGroup/9e92cb17c7bf4af9b6072520d42ea103")
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
    public void listByGroupTest2() {
        String token = login("appceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/orgOrganizations/action/listByGroup/9e92cb17c7bf4af9b6072520d42ea103")
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