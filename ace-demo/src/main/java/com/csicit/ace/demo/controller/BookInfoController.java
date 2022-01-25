package com.csicit.ace.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.annotation.AceScheduled;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.demo.domain.BookInfoDO;
import com.csicit.ace.demo.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2019/10/16 15:57
 */
@RestController
@RequestMapping("/bookInfos")
public class BookInfoController extends BaseController {
    @Autowired
    BookInfoService bookInfoService;

    /**
     * 查询列表
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/10/16 15:22
     */
    @RequestMapping(method = RequestMethod.GET)
    @AceScheduled(group = "xxx",name="ttt",url="/bookInfos_get")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String typeId = params.get("type_id").toString();
        if(Objects.equals(typeId,"0")){
            params.remove("type_id");
        }
        Page<BookInfoDO> page = new Page<>(current, size);
        IPage list = bookInfoService.page(page,MapWrapper.getEqualInstance(params));
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
    @AceScheduled(group = "xxx",name="ttt",url="/bookInfos/{id}_get")
    @AceAuth
    public R get(@PathVariable("id") String id) {
        BookInfoDO instance = bookInfoService.getById(id);
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
    public R save(@RequestBody BookInfoDO instance) {
        if (bookInfoService.save(instance)) {
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
    public R update(@RequestBody BookInfoDO instance) {
        if (bookInfoService.updateById(instance)) {
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
        if (bookInfoService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }



}
