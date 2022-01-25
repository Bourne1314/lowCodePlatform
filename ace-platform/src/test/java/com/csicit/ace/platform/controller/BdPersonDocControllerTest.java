package com.csicit.ace.platform.controller;

import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.BaseTest;
import com.csicit.ace.platform.core.controller.BdPersonDocController;
//import io.github.swagger2markup.GroupBy;
//import io.github.swagger2markup.Language;
//import io.github.swagger2markup.Swagger2MarkupConfig;
//import io.github.swagger2markup.Swagger2MarkupConverter;
//import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
//import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URL;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BdPersonDocControllerTest extends BaseTest {
    private MockMvc mockMvc;
    @Autowired
    BdPersonDocController bdPersonDocController;

    private void testBefore() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bdPersonDocController).build();
    }


    @Test
    public void getTest() {
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/bdPersonDocs/c17949cab6c441b189718f44cbd833e5")
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
    public void listByDepIdTest() {
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/bdPersonDocs/action/listByDepId")
                    .param("current", "1")
                    .param("size", "10")
                    .param("depId", "8c22d74d433d4828bd59b05d7059a8b7")
                    .param("searchString", "6")
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
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/bdPersonDocs")
                    .param("current", "1")
                    .param("size", "10")
                    .param("organizationId", "b96daa08d59c467a9bd6cca17e46f53f")
                    .param("groupId", "")
                    .param("searchString", "")
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
            mvcResult = mockMvc.perform(get("/bdPersonDocs")
                    .param("current", "1")
                    .param("size", "10")
                    .param("organizationId", "")
                    .param("groupId", "9e92cb17c7bf4af9b6072520d42ea103")
                    .param("searchString", "")
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
        String token = login("dayuanceshiren1");
        SecurityUtils.TEST_TOKEN = token;
        testBefore();
        MvcResult mvcResult = null; // 返回一个MvcResult
        try {
            mvcResult = mockMvc.perform(get("/bdPersonDocs")
                    .param("current", "1")
                    .param("size", "10")
                    .param("organizationId", "")
                    .param("groupId", "")
                    .param("searchString", "")
            )
                    .andDo(print()) // 定义执行行为
                    .andExpect(status().isOk()) // 对请求结果进行验证
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mvcResult.toString());
    }
//    /**
//     * 生成AsciiDocs格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocs() throws Exception {
//        //    输出Ascii格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:2110/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./docs/asciidoc/generated"));
//    }
//
//    /**
//     * 生成Markdown格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocs() throws Exception {
//        //    输出Markdown格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:2110/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./docs/markdown/generated"));
//    }
//    /**
//     * 生成Confluence格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateConfluenceDocs() throws Exception {
//        //    输出Confluence使用的格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.CONFLUENCE_MARKUP)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:2110/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./docs/confluence/generated"));
//    }
//
//    /**
//     * 生成AsciiDocs格式文档,并汇总成一个文件
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocsToFile() throws Exception {
//        //    输出Ascii到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:2110/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("./docs/asciidoc/generated/all"));
//    }
//
//    /**
//     * 生成Markdown格式文档,并汇总成一个文件
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocsToFile() throws Exception {
//        //    输出Markdown到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:2110/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("./docs/markdown/generated/all"));
//    }

}


