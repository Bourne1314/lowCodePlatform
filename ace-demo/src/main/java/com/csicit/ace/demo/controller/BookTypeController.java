package com.csicit.ace.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.demo.domain.BookTypeDO;
import com.csicit.ace.demo.service.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/10/16 15:13
 */

@RestController
@RequestMapping("/bookTypes")
public class BookTypeController extends BaseController {

    @Autowired
    BookTypeService bookTypeService;

    /** 
     * 查询列表 
     * @param params	
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/10/16 15:22
     */
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<BookTypeDO> page = new Page<>(current, size);
        IPage list = bookTypeService.page(page, MapWrapper.getEqualInstance(params,"sort",true));
        return R.ok().put("page", list);
    }

    /** 
     * 查询单个
     * @param id	
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/10/16 15:22
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BookTypeDO instance = bookTypeService.getById(id);
        return R.ok().put("instance", instance);
    }

    /** 
     * 保存 
     * @param instance	
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/10/16 15:22
     */
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BookTypeDO instance) {
        if (bookTypeService.saveBookType(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }
    
    /** 
     * 修改 
     * @param instance	
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/10/16 15:23
     */
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BookTypeDO instance) {
        if (bookTypeService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /** 
     * 删除
     * @param ids	
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/10/16 15:26
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (bookTypeService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    @RequestMapping(value = "/query/bookType/tree/{appId}",method = RequestMethod.GET)
    public TreeVO getBookTypeTree(@PathVariable("appId")String appId){
        return bookTypeService.getBookTypeTree(appId);
    }


}
