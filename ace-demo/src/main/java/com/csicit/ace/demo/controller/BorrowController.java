package com.csicit.ace.demo.controller;

import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.demo.domain.BorrowDO;
import com.csicit.ace.demo.service.BorrowService;
import com.csicit.ace.interfaces.service.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author shanwj
 * @date 2019/5/28 8:16
 */
@Controller
@RequestMapping("/borrows")
public class BorrowController extends BaseController {
    @Autowired
    private BorrowService borrowService;
    @Value("ace.message.type")
    private String messageType;

    @Autowired
    private IMessage message;
    private static final String BORROW_FORM_PATH_NAME = "borrow/borrowForm";
    private static final String BORROW_LIST_PATH_NAME = "borrow/borrowList";
    private static final String REDIRECT_TO_BORROW_URL = "redirect:/borrows";

    /**
     * 获取 Book 列表
     * 处理 "/book" 的 GET 请求，用来获取 Book 列表
     */
    @RequestMapping(method = RequestMethod.GET)
//    @AceAuth("获取书籍列表")
    public String getBorrowList(ModelMap map) {
        map.addAttribute("borrowList",borrowService.list(null));
        SysMessageDO sysMessageDO = new SysMessageDO();
        sysMessageDO.setUrl("xxxx");
        sysMessageDO.setTitle("title");
        sysMessageDO.setContent("content");
        sysMessageDO.setReceiveUsers("94ab7c5f84184615b3518d9a9246161c,a9dc4b429efa486a9e17dc0a5cabe36a,315f0f2a9939435e9d8f66c496d286ad");
        sysMessageDO.setType(messageType);
        sysMessageDO.setBusinessType("通知消息");
//        message.sendMessage(sysMessageDO,"tt","zz",null);
        return BORROW_LIST_PATH_NAME;
    }
    /**
     * 获取创建 Book 表单
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
//    @AceAuth("新增书籍界面")
    public String createBookForm(ModelMap map) {
        map.addAttribute("borrow", new BorrowDO());
        map.addAttribute("action", "create");
        return BORROW_FORM_PATH_NAME;
    }

    /**
     * 创建 Book
     * 处理 "/book/create" 的 POST 请求，用来新建 Book 信息
     * 通过 @ModelAttribute 绑定表单实体参数，也通过 @RequestParam 传递参数
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    @AceAuth("新增书籍")
    public String postBook(@ModelAttribute BorrowDO book) {
        borrowService.save(book);
        return REDIRECT_TO_BORROW_URL;
    }

    /**
     * 获取更新 Book 表单
     *    处理 "/book/update/{id}" 的 GET 请求，通过 URL 中的 id 值获取 Book 信息
     *    URL 中的 id ，通过 @PathVariable 绑定参数
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
//    @AceAuth("编辑书籍界面")
    public String getUser(@PathVariable String id, ModelMap map) {
        map.addAttribute("borrow", borrowService.getById(id));
        map.addAttribute("action", "update");
        return BORROW_FORM_PATH_NAME;
    }

    /**
     * 更新 Book
     * 处理 "/update" 的 PUT 请求，用来更新 Book 信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @AceAuth("更新书籍")
    public String putBook(@ModelAttribute BorrowDO borrow) {
        borrowService.updateById(borrow);
        return REDIRECT_TO_BORROW_URL;
    }

    /**
     * 删除 Book
     * 处理 "/book/{id}" 的 GET 请求，用来删除 Book 信息
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
//    @AceAuth("删除书籍")
    public String deleteBook(@PathVariable String id) {
        borrowService.removeById(id);
        return REDIRECT_TO_BORROW_URL;
    }

}
