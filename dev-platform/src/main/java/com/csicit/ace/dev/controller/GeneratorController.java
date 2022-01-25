package com.csicit.ace.dev.controller;

import com.csicit.ace.dev.service.GeneratorService;
import com.csicit.ace.dev.service.ProServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自动生成接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController extends BaseController {

    @Autowired
    GeneratorService generatorService;
    @Autowired
    ProServiceService proServiceService;
//    @Autowired
//    MetaTableService metaTableService;

//    @RequestMapping("/gen/one/{appId}/{tableId}")
//    public void genOne(@PathVariable("appId") String appId,
//                       @PathVariable("tableId") String tableId,
//                       HttpServletResponse response) throws IOException {
//        byte[] data = generatorService.generatorCode(appId,tableId);
//        response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"gen.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());
//    }

//    @RequestMapping(value = "/gen/select/{appId}/{tableIds}", method = RequestMethod.GET)
//    public void genMulti(HttpServletResponse response,
//                         @PathVariable("appId") String appId,
//                         @PathVariable("tableIds") String tableIds) throws IOException {
//        List<String> tableIdList = new ArrayList<>();
//        if (tableIds != null) {
//            if (tableIds.contains(",")) {
//                tableIdList = Arrays.asList(tableIds.split(","));
//            } else {
//                tableIdList.add(tableIds);
//            }
//        }
//        byte[] data = generatorService.generatorCodes(appId, tableIdList);
//        response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"gen.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());
//    }

    @RequestMapping(value = "/gen/all/{serviceId}", method = RequestMethod.GET)
    public void genAll(HttpServletResponse response,
                       @PathVariable("serviceId") String serviceId) throws IOException {
//        byte[] data = generatorService.generatorCodes(serviceId);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"gen.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());

      generatorService.generatorCodes(serviceId,response.getOutputStream());
    }

//    @RequestMapping(value = "/publish/service/{appId}/{modelIds}", method = RequestMethod.GET)
//    public R publishService(@PathVariable("appId") String appId,
//                            @PathVariable("modelIds") String modelIds) {
//        List<String> modelIdList = new ArrayList<>();
//        if (modelIdList != null) {
//            if (modelIds.contains(",")) {
//                modelIdList = Arrays.asList(modelIds.split(","));
//            } else {
//                modelIdList.add(modelIds);
//            }
//        }
//        if (generatorService.publishService(modelIdList, appId)) {
//            return R.ok("服务发布成功");
//        }
//        return R.error("服务发布失败");
//    }

//    @RequestMapping(value = "/gen/service/{appId}/{modelIds}", method = RequestMethod.GET)
//    public void genService(HttpServletResponse response, @PathVariable("appId") String appId, @PathVariable
//            ("modelIds") String modelIds) throws IOException {
//        List<String> modelIdList = new ArrayList<>();
//        if (modelIdList != null) {
//            if (modelIds.contains(",")) {
//                modelIdList = Arrays.asList(modelIds.split(","));
//            } else {
//                modelIdList.add(modelIds);
//            }
//        }
//        byte[] data = generatorService.genService(modelIdList, appId);
//        response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"gen.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());
//    }
}
