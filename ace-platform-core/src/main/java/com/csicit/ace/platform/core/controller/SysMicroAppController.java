package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysMicroAppDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysMicroAppService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shanwj
 * @date 2020/4/7 11:16
 */
@RestController
@RequestMapping("/sysMicroApps")
public class SysMicroAppController extends BaseController{

    @Autowired
    SysMicroAppService sysMicroAppService;

    /**
     * 根据id获取单个小程序
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个小程序", httpMethod = "GET", notes = "根据id获取单个小程序" )
    @AceAuth("获取单个小程序")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysMicroAppDO SysMicroAppDO = sysMicroAppService.getById(id);
        return R.ok().put("instance", SysMicroAppDO);
    }


    /**
     * 根据条件获取小程序列表并分页
     *
     * @param params 请求参数map对象
     * @return 小程序列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取小程序列表并分页", httpMethod = "GET", notes = "获取小程序列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取小程序列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = sysMicroAppService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<SysMicroAppDO> page = new Page<>(current, size);
        IPage list = sysMicroAppService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增小程序保存
     *
     * @param instance 小程序对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存小程序")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMicroAppDO")
    @AceAuth("保存小程序对象")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysMicroAppDO instance) {
        return sysMicroAppService.saveSysMicroApp(instance);
    }


    /**
     * 修改小程序保存
     *
     * @param instance 对象
     * @return 修改小程序
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改小程序", httpMethod = "PUT", notes = "修改小程序")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMicroAppDO")
    @AceAuth("修改小程序")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysMicroAppDO instance) {
        return sysMicroAppService.updateSysMicroApp(instance);
    }

    /**
     * 根据id数组删除数据
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除小程序对象")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @AceAuth("删除小程序对象")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysMicroAppService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
