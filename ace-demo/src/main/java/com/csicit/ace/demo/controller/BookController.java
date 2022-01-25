package com.csicit.ace.demo.controller;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.demo.domain.BookDO;
import com.csicit.ace.demo.service.BookService;
import com.csicit.ace.interfaces.service.IConfig;
import com.csicit.ace.interfaces.service.IDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author shanwj
 * @date 2019/5/27 17:23
 */
@Controller
@RequestMapping("/books")
@Api("书籍管理")
public class BookController extends BaseController {
    @Autowired
    BookService bookService;
    @Autowired
    IConfig config;
    @Autowired
    IDict dict;
    private static final String BOOK_FORM_PATH_NAME = "book/bookForm";
    private static final String BOOK_LIST_PATH_NAME = "book/bookList";
    private static final String REDIRECT_TO_BOOK_URL = "redirect:/books";

    /**
     * 获取 Book 列表
     * 处理 "/book" 的 GET 请求，用来获取 Book 列表
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个公司", httpMethod = "GET", notes = "获取单个")
//    @AceAuth("获取书籍列表")
    public String getBookList(ModelMap map) {
//        String init = config.getConfig("platform_init");
//        List<SysDictValueDO> citys = dict.getValue("city");
//        map.addAttribute("init", init);
//        map.addAttribute("citys", citys);
//        map.addAttribute("user", securityUtils.getCurrentUser().toString());
        map.addAttribute("bookList", bookService.list(null));
        return BOOK_LIST_PATH_NAME;
    }

    /**
     * 获取创建 Book 表单
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
//    @AceAuth("新增书籍界面")
    public String createBookForm(ModelMap map) {
        BookDO book = new BookDO();
        book.setId(UUID.randomUUID().toString());
        map.addAttribute("book", book);
        map.addAttribute("action", "create");
        return BOOK_FORM_PATH_NAME;
    }

    /**
     * 创建 Book
     * 处理 "/book/create" 的 POST 请求，用来新建 Book 信息
     * 通过 @ModelAttribute 绑定表单实体参数，也通过 @RequestParam 传递参数
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "保存书籍", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BookDO")
    @AceAuth("新增书籍")
    public String postBook(@ModelAttribute BookDO book) {
        bookService.saveBook(book);
        return REDIRECT_TO_BOOK_URL;
    }

    /**
     * 获取更新 Book 表单
     * 处理 "/book/update/{id}" 的 GET 请求，通过 URL 中的 id 值获取 Book 信息
     * URL 中的 id ，通过 @PathVariable 绑定参数
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
//    @AceAuth("编辑书籍界面")
    public String getUser(@PathVariable String id, ModelMap map) {
        map.addAttribute("book", bookService.getById(id));
        map.addAttribute("action", "update");
        return BOOK_FORM_PATH_NAME;
    }

    /**
     * 更新 Book
     * 处理 "/update" 的 PUT 请求，用来更新 Book 信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @AceAuth("更新书籍")
    public String putBook(@ModelAttribute BookDO book) {
        bookService.updateById(book);
        return REDIRECT_TO_BOOK_URL;
    }

    /**
     * 删除 Book
     * 处理 "/book/{id}" 的 GET 请求，用来删除 Book 信息
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
//    @AceAuth("删除书籍")
    public String deleteBook(@PathVariable String id) {
        bookService.removeById(id);
        return REDIRECT_TO_BOOK_URL;
    }

    @RequestMapping(value = "/action/send",method = RequestMethod.POST)
    @ResponseBody
    public String send(@RequestBody SysMessageDO sysMessageDO){
        System.out.println(sysMessageDO.toString());
        return sysMessageDO.toString();
    }


}
