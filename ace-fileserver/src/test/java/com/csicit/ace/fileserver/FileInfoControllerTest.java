package com.csicit.ace.fileserver;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author JonnyJiang
 * @date 2019/6/5 8:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileInfoControllerTest {
    private MockMvc mockMvc;
    protected static final String TOKEN = "Y577pVtfuZmFCLqdR6z7hLRstrFgTo6KSIgz4o1HuUOYm_JUSyVr_5eUsxzfU5RepU2Rs24D8fNgTCUitFh4HtfPw_tNGQat2D3sbn7Y4pmRd19n9dCqmpJXjQHVL0Nz";

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    FileInfoService fileInfoService;
    @Autowired
    FileConfigurationService fileConfigurationService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shareByFileId() throws Exception {
        List<FileInfoDO> fileInfoS = fileInfoService.list(new QueryWrapper<>());
        assert fileInfoS.size() > 0;
        if (fileInfoS.size() > 0) {
            FileInfoDO fileInfo = fileInfoS.get(0);
            FileConfigurationDO configuration = fileConfigurationService.getById(fileInfo.getFileConfigurationId());
            Map<String, Object> map = new HashMap<>();
            map.put("configurationKey", configuration.getConfigurationKey());
            map.put("fileId", fileInfo.getId());
            map.put("desConfigurationKey", configuration.getConfigurationKey());
            String formId = UUID.randomUUID().toString();
            map.put("desFormId", formId);

            MvcResult result = mockMvc.perform(post("/fileInfo/shareByFileId")
                    .header("token", TOKEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();// 返回执行请求的结果
            String resultStr = result.getResponse().getContentAsString();
            System.out.println(resultStr);
            R r = JSONObject.parseObject(resultStr, R.class);
            assert (Integer) r.get("code") == 40000;
            map.clear();
            map.put("FILE_CONFIGURATION_ID", configuration.getId());
            map.put("FORM_ID", formId);
            Integer count = fileInfoService.count(new QueryWrapper<FileInfoDO>().allEq(map));
            assert count == 1;
        }
    }

    @Test
    public void shareByFormId() throws Exception {
        List<FileInfoDO> fileInfoS = fileInfoService.list(new QueryWrapper<>());
        assert fileInfoS.size() > 0;
        if (fileInfoS.size() > 0) {
            FileInfoDO fileInfo = fileInfoS.get(0);
            FileConfigurationDO configuration = fileConfigurationService.getById(fileInfo.getFileConfigurationId());
            Map<String, Object> map = new HashMap<>();
            map.put("configurationKey", configuration.getConfigurationKey());
            String formId = fileInfo.getFormId();
            map.put("formId", formId);
            map.put("desConfigurationKey", configuration.getConfigurationKey());
            String desFormId = UUID.randomUUID().toString();
            map.put("desFormId", desFormId);

            MvcResult result = mockMvc.perform(post("/fileInfo/shareByFormId")
                    .header("token", TOKEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();// 返回执行请求的结果
            String resultStr = result.getResponse().getContentAsString();
            System.out.println(resultStr);
            R r = JSONObject.parseObject(resultStr, R.class);
            assert (Integer) r.get("code") == 40000;
            map.clear();
            map.put("FILE_CONFIGURATION_ID", configuration.getId());
            map.put("FORM_ID", formId);
            Integer count = fileInfoService.count(new QueryWrapper<FileInfoDO>().allEq(map));
            System.out.println("源文件数量：" + count);
            map.put("FORM_ID", desFormId);
            Integer desCount = fileInfoService.count(new QueryWrapper<FileInfoDO>().allEq(map));
            System.out.println("目标文件数量：" + desCount);
            assert count == desCount;
        }
    }

    @Test
    public void deleteByFileId() throws Exception {
        List<FileInfoDO> fileInfoS = fileInfoService.list(new QueryWrapper<>());
        assert fileInfoS.size() > 0;
        if (fileInfoS.size() > 0) {
            FileInfoDO fileInfo = fileInfoS.get(0);
            FileConfigurationDO configuration = fileConfigurationService.getById(fileInfo.getFileConfigurationId());
            Map<String, Object> map = new HashMap<>();
            map.put("configurationKey", configuration.getConfigurationKey());
            map.put("fileId", fileInfo.getId());

            MvcResult result = mockMvc.perform(post("/fileInfo/deleteByFileId")
                    .header("token", TOKEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();// 返回执行请求的结果
            String resultStr = result.getResponse().getContentAsString();
            System.out.println(resultStr);
            R r = JSONObject.parseObject(resultStr, R.class);
            assert (Integer) r.get("code") == 40000;
            FileInfoDO file = fileInfoService.getById(fileInfo.getId());
            assert file == null;
        }
    }

    @Test
    public void deleteFileByFormId() throws Exception {
        List<FileInfoDO> fileInfoS = fileInfoService.list(new QueryWrapper<>());
        assert fileInfoS.size() > 0;
        if (fileInfoS.size() > 0) {
            FileInfoDO fileInfo = fileInfoS.get(0);
            FileConfigurationDO configuration = fileConfigurationService.getById(fileInfo.getFileConfigurationId());
            Map<String, Object> map = new HashMap<>();
            map.put("configurationKey", configuration.getConfigurationKey());
            map.put("formId", fileInfo.getFormId());

            MvcResult result = mockMvc.perform(post("/fileInfo/deleteFileByFormId")
                    .header("token", TOKEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();// 返回执行请求的结果
            String resultStr = result.getResponse().getContentAsString();
            System.out.println(resultStr);
            R r = JSONObject.parseObject(resultStr, R.class);
            assert (Integer) r.get("code") == 40000;
            map.clear();
            map.put("FILE_CONFIGURATION_ID", fileInfo.getFileConfigurationId());
            map.put("FORM_ID", fileInfo.getFormId());
            Integer count = fileInfoService.count(new QueryWrapper<FileInfoDO>().allEq(map));
            assert count == 0;
        }
    }

    @Test
    public void listByFormId() throws Exception {
        List<FileInfoDO> fileInfoS = fileInfoService.list(new QueryWrapper<>());
        assert fileInfoS.size() > 0;
        if (fileInfoS.size() > 0) {
            FileInfoDO fileInfo = fileInfoS.get(0);
            FileConfigurationDO configuration = fileConfigurationService.getById(fileInfo.getFileConfigurationId());
            Map<String, Object> map = new HashMap<>();
            map.put("configurationKey", configuration.getConfigurationKey());
            map.put("formId", fileInfo.getFormId());

            MvcResult result = mockMvc.perform(post("/fileInfo/listByFormId")
                    .header("token", TOKEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();// 返回执行请求的结果
            String resultStr = result.getResponse().getContentAsString();
            System.out.println(resultStr);
            R r = JSONObject.parseObject(resultStr, R.class);
            assert (Integer) r.get("code") == 40000;
            List<FileInfoDO> fileInfos = (List<FileInfoDO>) r.get("fileInfos");
            map.clear();
            map.put("FILE_CONFIGURATION_ID", fileInfo.getFileConfigurationId());
            map.put("FORM_ID", fileInfo.getFormId());
            Integer count = fileInfoService.count(new QueryWrapper<FileInfoDO>().allEq(map));
            assert count == fileInfos.size();
        }
    }

    @Test
    public void getByFileId() throws Exception {
        List<FileInfoDO> fileInfoS = fileInfoService.list(new QueryWrapper<>());
        assert fileInfoS.size() > 0;
        if (fileInfoS.size() > 0) {
            FileInfoDO fileInfo = fileInfoS.get(0);
            FileConfigurationDO configuration = fileConfigurationService.getById(fileInfo.getFileConfigurationId());
            Map<String, Object> map = new HashMap<>();
            map.put("configurationKey", configuration.getConfigurationKey());
            map.put("fileId", fileInfo.getId());

            MvcResult result = mockMvc.perform(post("/fileInfo/getByFileId")
                    .header("token", TOKEN)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();// 返回执行请求的结果
            String resultStr = result.getResponse().getContentAsString();
            System.out.println(resultStr);
            R r = JSONObject.parseObject(resultStr, R.class);
            assert (Integer) r.get("code") == 40000;
            FileInfoDO file = JSONObject.toJavaObject((JSONObject) r.get("fileInfo"), FileInfoDO.class);
            assert file != null;
            assert StringUtils.equals(fileInfo.getId(), fileInfoService.getActualFileInfo(file.getId()).getId());
        }
    }
}
