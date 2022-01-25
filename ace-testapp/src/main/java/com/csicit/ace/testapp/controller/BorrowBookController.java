package com.csicit.ace.testapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.interfaces.service.IFile;
import com.csicit.ace.testapp.pojo.Book;
import com.csicit.ace.testapp.pojo.BorrowBook;
import com.csicit.ace.testapp.service.BookService;
import com.csicit.ace.testapp.service.BorrowBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrowBooks")
@Api("借书申请")
public class BorrowBookController {

    @Autowired
    private BorrowBookService borrowBookService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BpmManager bpmManager;

    @CrossOrigin
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "借书信息", httpMethod = "GET", notes = "借书信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BorrowBook instance = borrowBookService.getById(id);
        if (StringUtils.isNotBlank(instance.getBookIds())) {
            List<String> bookIds = Arrays.asList((instance.getBookIds().split(",")));
            instance.setBooks(bookService.list(new QueryWrapper<Book>()
                    .in("id", bookIds)));
        }
        return R.ok().put("instance", instance);
    }

    /**
     * 获取借书信息列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @CrossOrigin
    @ApiOperation(value = "获取借书信息列表", httpMethod = "GET", notes = "获取借书信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取借书信息列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<BorrowBook> page = new Page<>(current, size);
        IPage list = borrowBookService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }


    /**
     * 获取借书信息列表(通过排序筛选模糊查询等等)
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @CrossOrigin
    @ApiOperation(value = "获取借书信息列表", httpMethod = "POST", notes = "获取借书信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取借书信息列表")
    @RequestMapping(value = "/list/paramJson", method = RequestMethod.POST)
    public R listByParamJson(@RequestBody Map<String, Object> params) {
        int current = (Integer) params.get("current");
        int size = (Integer) params.get("size");
        Page<BorrowBook> page = new Page<>(current, size);

        IPage list = borrowBookService.page(page, MapWrapper.getInstanceByParamJson(params));
        return R.ok().put("page", list);
    }


    /**
     * 新增借书信息
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @CrossOrigin
    @ApiOperation(value = "新增借书信息", httpMethod = "POST", notes = "新增借书信息")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Book")
    @AceAuth("新增借书信息")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BorrowBook instance) {
        if (borrowBookService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改借书信息
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @CrossOrigin
    @ApiOperation(value = "修改借书信息", httpMethod = "PUT", notes = "修改借书信息")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Book")
    @AceAuth("修改借书信息")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BorrowBook instance) {
        if (borrowBookService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除借书信息
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @CrossOrigin
    @ApiOperation(value = "删除借书信息", httpMethod = "DELETE", notes = "删除借书信息")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除借书信息")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (borrowBookService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));

    }

    /**
     * 新建借书信息
     *
     * @param borrowBook
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @CrossOrigin
    @ApiOperation(value = "新建借书信息", httpMethod = "POST")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("新建借书信息")
    @RequestMapping(value = "/add/borrowBook", method = RequestMethod.POST)
    public R addBorrowBook(@RequestBody BorrowBook borrowBook) {
        if (borrowBookService.addBorrowBook(borrowBook)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @CrossOrigin
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R upload(@RequestParam("path") String path) {
        IFile iFile = SpringContextUtils.getBean(IFile.class);
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            iFile.upload("upload", "xyz", file.getName(), file.length(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok();
    }


    @CrossOrigin
    @RequestMapping(path = "/deleteTest", method = RequestMethod.POST)
    public R deleteTest(@RequestParam("code") String code, @RequestParam("businessKey") String businessKey) {
        bpmManager.deleteFlowInstanceByBusinessKey(code, businessKey, "");
        return R.ok();
    }
}
