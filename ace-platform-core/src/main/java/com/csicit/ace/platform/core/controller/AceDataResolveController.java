package com.csicit.ace.platform.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.core.pojo.vo.AceDataVO;
import com.csicit.ace.platform.core.service.AceDataResolveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * Ace 平台数据处理
 * @author FourLeaves
 * @version V1.0
 * @date 2020/8/27 8:04
 */
@RestController
@RequestMapping("/data")
@Api("平台数据处理")
public class AceDataResolveController {

    @Autowired
    AceDataResolveService aceDataResolveService;

    @Autowired
    SecurityUtils securityUtils;

    /**
     * 根据条件导出平台数据
     * @param response
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/8/27 8:07
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "map", required = true)
    @ApiOperation(value = "根据条件导出平台数据", httpMethod = "GET", notes = "根据条件导出平台数据")
    @AceAuth("根据条件导出平台数据")
    @RequestMapping(value = "/action/export", method = RequestMethod.GET)
    public void exportData(HttpServletResponse response, @RequestParam Map<String, String> params) {
        if (!Objects.equals(securityUtils.getCurrentUserName(), "admin")) {
            throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        String jsonName = "aceData.json";
        try {
            AceDataVO aceDataVO = aceDataResolveService.exportData(params);
            String content = JSONObject.toJSONString(aceDataVO, SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
            File file = new File(jsonName);
            if (file.exists()) {
                file.delete();
            }
            // 写文件
            file.createNewFile();
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(content);
            write.flush();
            write.close();

            // 下载
            InputStream inputStream = new FileInputStream(file);
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            response.setHeader("Context-Type", "application/xmsdownload");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + jsonName);
            OutputStream os = response.getOutputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 100];
            int ch;
            while ((ch = inputStream.read(buffer)) != -1) {
                swapStream.write(buffer, 0, ch);
            }
            os.write(swapStream.toByteArray());
            os.close();
            swapStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
    }

    /**
     * 导入平台数据
     * @param file
     * @return
     * @author FourLeaves
     * @date 2020/8/27 8:07
     */
    @ApiImplicitParam(name = "file", value = "file", dataType = "MultipartFile", required = true)
    @ApiOperation(value = "导入平台数据", httpMethod = "POST", notes = "导入平台数据")
    @AceAuth("导入平台数据")
    @RequestMapping(value = "/action/import", method = RequestMethod.POST)
    public R importData(@RequestParam("file") MultipartFile file) {
        if (!Objects.equals(securityUtils.getCurrentUserName(), "admin")) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        AceDataVO aceDataVO;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            aceDataVO = JSONObject.parseObject(stringBuffer.toString(), AceDataVO.class);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException("文件格式、内容异常！");
        }
        if (aceDataVO != null) {
            if (aceDataResolveService.importData(aceDataVO)) {
                return R.ok();
            }
        }
        return R.error("文件格式、内容异常！");
    }
}
