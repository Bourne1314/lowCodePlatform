package com.csicit.ace.testapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.annotation.AceScheduled;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.interfaces.service.IFile;
import com.csicit.ace.interfaces.service.IUser;
import com.csicit.ace.testapp.pojo.Book;
import com.csicit.ace.testapp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/books")
@Api("书籍")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private IFile iFile;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private IUser iUser;

    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个书籍信息", httpMethod = "GET", notes = "获取单个书籍信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @AceScheduled(name = "xxx", url = "/books_GET", group = "tt")
//    @AceConfigField(n)
    public R get(@PathVariable("id") String id) {
        Book instance = bookService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取书籍信息列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取书籍信息列表", httpMethod = "GET", notes = "获取书籍信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取书籍信息列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<Book> page = new Page<>(current, size);
        IPage list = bookService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }


    /**
     * 获取书籍信息列表(通过排序筛选模糊查询等等)
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取书籍信息列表", httpMethod = "POST", notes = "获取书籍信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取书籍信息列表")
    @RequestMapping(value = "/list/paramJson", method = RequestMethod.POST)
    public R listByParamJson(@RequestBody Map<String, Object> params) {
        System.out.println(params.toString());
        int current = (Integer) params.get("current");
        int size = (Integer) params.get("size");
        Page<Book> page = new Page<>(current, size);

        IPage list = bookService.page(page, MapWrapper.getInstanceByParamJson(params));
        return R.ok().put("page", list);
    }

    /**
     * 获取书籍信息列表(不分页)
     *
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取书籍信息列表", httpMethod = "GET", notes = "获取书籍信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取书籍信息列表")
    @RequestMapping(value = "/list/noPage", method = RequestMethod.GET)
    public R listByNoPage() {
        List<Book> list = bookService.list(new QueryWrapper<>(null));
        return R.ok().put("list", list);
    }


    /**
     * 新增书籍信息
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "新增书籍信息", httpMethod = "POST", notes = "新增书籍信息")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Book")
    @AceAuth("新增书籍信息")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody Book instance) {
        if (bookService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改书籍信息
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "修改书籍信息", httpMethod = "PUT", notes = "修改书籍信息")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Book")
    @AceAuth("修改书籍信息")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody Book instance) {
        if (bookService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除书籍信息
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "删除书籍信息", httpMethod = "DELETE", notes = "删除书籍信息")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除书籍信息")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (bookService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    @CrossOrigin
    @RequestMapping("/exportZip")
    public void exportZip(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssSSS");
        try (FileOutputStream fileOutputStream = new FileOutputStream(simpleDateFormat.format(new Date()) + ".zip")) {
            InputStream inputStream = iFile.exportZip(configurationKey, formId);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/exportZipBatch", method = RequestMethod.POST)
    public void exportZipBatch(@RequestBody Map<String, Object> params) {
//        @RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId
        String configurationKey = (String) params.get("configurationKey");
        List<String> formIds = (List<String>) params.get("formIds");
        List<ExportInfo> exportInfos = new ArrayList<>();
        for (String formId : formIds) {
            ExportInfo exportInfo = new ExportInfo();
            exportInfo.setConfigurationKey(configurationKey);
            exportInfo.setAppId(securityUtils.getAppName());
            exportInfo.addFormId(formId);
            exportInfos.add(exportInfo);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssSSS");
        try (FileOutputStream fileOutputStream = new FileOutputStream(simpleDateFormat.format(new Date()) + ".zip")) {
            InputStream inputStream = iFile.exportZipBatch(exportInfos);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping("/importZip")
    public void importZip(@RequestParam("fileName") String fileName) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            iFile.importZip(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @CrossOrigin
    @RequestMapping("/importFile")
    public void importZip(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId) throws Exception {
        List<FileInfoDO> fileInfos = iFile.listFileByFormId(configurationKey, formId);
        if (fileInfos.size() > 0) {
            InputStream inputStream = iFile.download(configurationKey, fileInfos.get(0).getId());
            iFile.importZip(inputStream);
        }
    }
}
