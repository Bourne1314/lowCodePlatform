package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysJobCalendarDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.SysJobCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 工作日表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 08:12:02
 */

@RestController
@RequestMapping("/sysJobCalendars")
@Api("工作日表")
public class SysJobCalendarController extends BaseController {

    @Autowired
    private SysJobCalendarService sysJobCalendarService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-08-16 08:12:02
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysJobCalendarDO instance = sysJobCalendarService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-16 08:12:02
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<SysJobCalendarDO> page = new Page<>(current, size);
        IPage list = sysJobCalendarService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 08:12:02
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysJobCalendarDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysJobCalendarDO instance) {
        if (sysJobCalendarService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-08-16 08:12:02
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysJobCalendarDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysJobCalendarDO instance) {
        if (sysJobCalendarService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 08:12:02
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (!sysJobCalendarService.removeByIds(Arrays.asList(ids))) {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));

    }

    /**
     * 获取编辑过的工作日的年份列表
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:45
     */
    @ApiOperation(value = "获取编辑过的工作日的年份列表", httpMethod = "GET", notes = "获取编辑过的工作日的年份列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取编辑过的工作日的年份列表")
    @RequestMapping(value = "/query/hasWorkDay/yearList", method = RequestMethod.GET)
    public R getHasWorkYearList(@RequestParam Map<String, Object> params) {
        String orgId = (String) params.get("orgId");
        List<String> years = sysJobCalendarService.getHasWorkYearList(orgId);
        return R.ok().put("workYearList", years);
    }

    /**
     * 求出当前年月及上下月的工作日
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:45
     */
    @ApiOperation(value = "求出当前年月及上下月的工作日", httpMethod = "POST", notes = "求出当前年月及上下月的工作日")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("求出当前年月及上下月的工作日")
    @RequestMapping(value = "/getWorkDayDataList", method = RequestMethod.POST)
    public R getWorkDayDataList(@RequestBody Map<String, Object> params) {
        return sysJobCalendarService.getWorkDayDataList(params);
    }

    /**
     * 当前日期为工作日时，点击设置为休息日，反之，休息日点击设置为工作日
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:45
     */
    @ApiOperation(value = "工作日休息日点击设置", httpMethod = "POST", notes = "工作日休息日点击设置")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("工作日休息日点击设置")
    @RequestMapping(value = "/setWorkState", method = RequestMethod.POST)
    public R setWorkState(@RequestBody Map<String, Object> params) {
        if (!sysJobCalendarService.setWorkState(params)) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 为该年份设置工作日
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:45
     */
    @ApiOperation(value = "为该年份设置工作日", httpMethod = "POST", notes = "为该年份设置工作日")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("为该年份设置工作日")
    @RequestMapping(value = "/setWorkDay", method = RequestMethod.POST)
    public R setWorkDay(@RequestBody Map<String, Object> params) {
        if (!sysJobCalendarService.setWorkDay(params)) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }
}
