package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProInterfaceCategoryDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.ProInterfaceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 接口类别 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:31:19
 */

@RestController
@RequestMapping("/proInterfaceCategorys")
@Api("接口类别")
public class ProInterfaceCategoryController extends BaseController {

    @Autowired
    private ProInterfaceCategoryService proInterfaceCategoryService;

    /**
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ProInterfaceCategoryDO instance = proInterfaceCategoryService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 不分页获取列表
     *
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "不分页获取列表", httpMethod = "GET", notes = "不分页获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        String appId = params.get("service_id").toString();
        List<ProInterfaceCategoryDO> list = proInterfaceCategoryService.list(new QueryWrapper<ProInterfaceCategoryDO>()
                .eq("service_id", appId).orderByDesc("create_time"));
        return R.ok().put("list", list);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String appId = (String) params.get("service_id");
        Page<ProInterfaceCategoryDO> page = new Page<>(current, size);
        IPage list = proInterfaceCategoryService.page(page, new QueryWrapper<ProInterfaceCategoryDO>()
                .eq("service_id", appId).orderByDesc("create_time"));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProInterfaceCategoryDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody ProInterfaceCategoryDO instance) {
        int count = proInterfaceCategoryService.count(new QueryWrapper<ProInterfaceCategoryDO>()
                .eq("service_id", instance.getServiceId()).eq("name", instance.getName()));
        if (count > 0) {
            return R.error("在该服务下，已存在相同接口类别");
        }
        if (proInterfaceCategoryService.addInterfaceCategory(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProInterfaceCategoryDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody ProInterfaceCategoryDO instance) {
        ProInterfaceCategoryDO oldModel = proInterfaceCategoryService.getById(instance.getId());
        if (!Objects.equals(instance.getName(), oldModel
                .getName())) {
            int count = proInterfaceCategoryService.count(new QueryWrapper<ProInterfaceCategoryDO>()
                    .eq("service_id", instance.getServiceId()).eq("name", instance.getName()));
            if (count > 0) {
                return R.error("在同一服务下，已存在相同接口类别");
            }
        }

        if (proInterfaceCategoryService.updInterfaceCategory(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }

        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }


    /**
     * @param ids
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (proInterfaceCategoryService.delInterfaceCategory(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
