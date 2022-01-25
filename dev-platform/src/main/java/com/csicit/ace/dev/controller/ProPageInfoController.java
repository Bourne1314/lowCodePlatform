package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProPageInfoDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.ProPageInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 页面基本信息 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/proPageInfos")
@Api("页面基本信息")
public class ProPageInfoController extends BaseController {

    @Autowired
    private ProPageInfoService pageInfoService;

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ProPageInfoDO instance = pageInfoService.getProPageInfoDO(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String serviceId = (String) params.get("serviceId");
        String searchStr = (String) params.get("searching");
        Page<ProPageInfoDO> page = new Page<>(current, size);
        IPage list = pageInfoService.page(page, new QueryWrapper<ProPageInfoDO>().eq("service_id", serviceId)
                .and(StringUtils.isNotBlank(searchStr)
                        , i -> i.like("name", searchStr)).orderByAsc
                        ("name"));
        return R.ok().put("page", list);
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
        String serviceId = (String) params.get("serviceId");
        List<ProPageInfoDO> list = pageInfoService.list(new QueryWrapper<ProPageInfoDO>()
                .eq("service_id", serviceId).orderByAsc("name"));
        return R.ok().put("list", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "proInfo")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody ProPageInfoDO instance) {
        Integer count = pageInfoService.count(new QueryWrapper<ProPageInfoDO>()
                .eq("name", instance.getName()).eq("service_id", instance.getServiceId()));
        if (count > 0) {
            return R.error("已存在相同的页面名称");
        }
        instance.setCreateTime(LocalDateTime.now());
        if (pageInfoService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "proInfo")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody ProPageInfoDO instance) {
        ProPageInfoDO oldProject = pageInfoService.getById(instance.getId());
        if (!Objects.equals(oldProject.getName(), instance.getName())) {
            Integer count = pageInfoService.count(new QueryWrapper<ProPageInfoDO>()
                    .eq("name", instance.getName()).eq("service_id", instance.getServiceId()));
            if (count > 0) {
                return R.error("已存在相同的页面名称");
            }
        }
        instance.setJsCode(oldProject.getJsCode());
        instance.setHtmlCode(oldProject.getHtmlCode());
        if (pageInfoService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (pageInfoService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 编辑页面
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "编辑页面", httpMethod = "POST", notes = "编辑页面")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "proInfo")
    @RequestMapping(value = "/action/edit", method = RequestMethod.POST)
    public R editPageInfo(@RequestBody ProPageInfoDO instance) {
        ProPageInfoDO proPageInfoDO = pageInfoService.getById(instance.getId());
        proPageInfoDO.setJsCode(instance.getJsCode());
        proPageInfoDO.setHtmlCode(instance.getHtmlCode());
        if (pageInfoService.updateById(proPageInfoDO)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }
}
