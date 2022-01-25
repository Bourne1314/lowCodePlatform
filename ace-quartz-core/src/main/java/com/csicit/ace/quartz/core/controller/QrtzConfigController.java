package com.csicit.ace.quartz.core.controller;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.quartz.core.vo.JobDetailVO;
import com.csicit.ace.quartz.core.service.QrtzConfigService;
import com.csicit.ace.quartz.core.utils.JobDetailType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.quartz.CronExpression;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.*;


/**
 * 批处理任务配置 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */

@RestController
@RequestMapping("/qrtzConfigs")
@Api("批处理任务配置")
public class QrtzConfigController {

    @Autowired
    private QrtzConfigService qrtzConfigService;

    /**
     * 获取单个任务列表
     *
     * @param id
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个任务列表", httpMethod = "GET", notes = "获取单个任务列表")
    @AceAuth("获取单个任务列表")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        QrtzConfigDO instance = qrtzConfigService.getById(id);
        List<JobDetailType> jobDetailTypes = qrtzConfigService.getJobDetailType(instance);
        return R.ok().put("instance", instance).put("jobDetailTypes", jobDetailTypes);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<QrtzConfigDO> page = new Page<>(current, size);
        IPage list = qrtzConfigService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "QrtzConfigDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody QrtzConfigDO instance) {
        if (qrtzConfigService.save(instance)) {
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
     * @date 2019-07-16 09:59:40
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "QrtzConfigDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody QrtzConfigDO instance) {
        if (qrtzConfigService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (qrtzConfigService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }


    /**
     * 获取任务运行监控的列表
     *
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "获取任务运行监控的列表", httpMethod = "GET", notes = "获取任务运行监控的列表")
    @AceAuth("获取任务运行监控的列表")
    @RequestMapping(value = "/getOperateData", method = RequestMethod.GET)
    public R getOperateData() {
        List<JobDetailVO> jobDetailVOList = qrtzConfigService.getOperateDataList();


        SchedulerMetaData meta = qrtzConfigService.getSchedulerState();
        String state = "";
        if (meta.isInStandbyMode()) {
            state = "已暂停";
        } else if (meta.isStarted()) {
            state = "运行中";
        } else if (meta.isShutdown()) {
            state = "已停止";
        }
        return R.ok().put("list", jobDetailVOList).put("state", state).put("schedulerInstanceId", meta
                .getSchedulerInstanceId()).put("runTime", meta.getRunningSince().toInstant().atZone(ZoneId
                .systemDefault())
                .toLocalDateTime());
    }

    /**
     * 获取任务配置的列表
     *
     * @param params
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "获取任务运行监控的列表", httpMethod = "POST", notes = "获取任务运行监控的列表")
    @AceAuth("获取任务运行监控的列表")
    @RequestMapping(value = "/getTaskConfigData", method = RequestMethod.POST)
    public R getTaskConfigDataList(@RequestBody Map<String, Object> params) {
        List<JobDetailVO> jobDetailVOList = qrtzConfigService.getTaskConfigDataList((String) params.get("id"));
        return R.ok().put("list", jobDetailVOList);
    }

    /**
     * 获取任务组group下的所有任务列表
     *
     * @param params
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "获取任务组group下的所有任务列表", httpMethod = "POST", notes = "获取任务组group下的所有任务列表")
    @AceAuth("获取任务组group下的所有任务列表")
    @RequestMapping(value = "/query/allJobByGroup", method = RequestMethod.POST)
    public R allJobByGroup(@RequestBody Map<String, Object> params) {
        String group = (String) params.get("group");
        String appId = (String) params.get("appId");
        List<JobDetailType> jobDetailTypes = qrtzConfigService.allJobByGroup(group, appId);
        return R.ok().put("jobDetailTypes", jobDetailTypes);
    }

    /**
     * 暂停任务
     *
     * @param params
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "暂停任务", httpMethod = "POST", notes = "暂停任务")
    @AceAuth("暂停任务")
    @RequestMapping(value = "/pausedJob", method = RequestMethod.POST)
    public R pausedJob(@RequestBody Map<String, Object> params) {
        String group = (String) params.get("group");
        String name = (String) params.get("name");
        if (!qrtzConfigService.pausedJob(group, name)) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 运行任务
     *
     * @param params
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "运行任务", httpMethod = "POST", notes = "运行任务")
    @AceAuth("运行任务")
    @RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
    public R resumeJob(@RequestBody Map<String, Object> params) {
        String group = (String) params.get("group");
        String name = (String) params.get("name");
        if (!qrtzConfigService.resumeJob(group, name)) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 新增任务
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "新增任务", httpMethod = "POST", notes = "新增任务")
    @AceAuth("新增任务")
    @RequestMapping(value = "/addJob", method = RequestMethod.POST)
    public R addJob(@RequestBody Map<String, Object> params) {

        if (!qrtzConfigService.existJob(params)) {
            return R.error("任务组下已经存在同名任务");
        }

        if (!qrtzConfigService.addJob(params)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 更新任务
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "更新任务", httpMethod = "PUT", notes = "更新任务")
    @AceAuth("更新任务")
    @RequestMapping(value = "/updJob", method = RequestMethod.PUT)
    public R updJob(@RequestBody Map<String, Object> params) {

        if (!qrtzConfigService.updJob(params)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 删除任务
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "删除任务", httpMethod = "DELETE", notes = "删除任务")
    @AceAuth("删除任务")
    @RequestMapping(value = "/delJob", method = RequestMethod.DELETE)
    public R delJob(@RequestBody Map<String, Object> params) {

        if (!qrtzConfigService.delJob(params)) {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
    }

    /**
     * 新增触发器
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "新增触发器", httpMethod = "POST", notes = "新增触发器")
    @AceAuth("新增触发器")
    @RequestMapping(value = "/addTrigger", method = RequestMethod.POST)
    public R addTrigger(@RequestBody Map<String, Object> params) {

        // 判断相应任务是否存在
        JobDetailType jobDetailType = qrtzConfigService.getJobDetail(params);

        if (jobDetailType == null) {
            return R.error("保存失败，指定的任务在配置文件中不存在！");
        } else {
            if (!qrtzConfigService.existTrigger(params)) {
                return R.error("任务下已经存在同名触发器");
            }
        }

        String cronExpression = (String) params.get("cronExpression");
        try {
            new CronExpression(cronExpression).getExpressionSummary();
        } catch (ParseException e) {

            return R.error("保存失败，Cron表达式不正确，请检查！");
        }
        if (!CronExpression.isValidExpression(cronExpression)) {
            return R.error("保存失败，Cron表达式不正确，请检查！");
        }

        if (!qrtzConfigService.addTrigger(params)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 更新触发器
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "更新触发器", httpMethod = "PUT", notes = "更新触发器")
    @AceAuth("更新触发器")
    @RequestMapping(value = "/updTrigger", method = RequestMethod.PUT)
    public R updTrigger(@RequestBody Map<String, Object> params) {

        // 判断触发器是否存在
        if (qrtzConfigService.existTrigger(params)) {
            return R.error("保存失败，正在更新的触发器在配置文件中不存在");
        }

        String cronExpression = (String) params.get("cronExpression");
        try {
            new CronExpression(cronExpression).getExpressionSummary();
        } catch (ParseException e) {

            return R.error("保存失败，Cron表达式不正确，请检查！");
        }
        if (!CronExpression.isValidExpression(cronExpression)) {
            return R.error("保存失败，Cron表达式不正确，请检查！");
        }

        if (!qrtzConfigService.updTrigger(params)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 删除触发器
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "删除触发器", httpMethod = "DELETE", notes = "删除触发器")
    @AceAuth("删除触发器")
    @RequestMapping(value = "/delTrigger", method = RequestMethod.DELETE)
    public R delTrigger(@RequestBody Map<String, Object> params) {

        if (!qrtzConfigService.delTrigger(params)) {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
    }

    /**
     * 新增参数
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "新增参数", httpMethod = "POST", notes = "新增参数")
    @AceAuth("新增参数")
    @RequestMapping(value = "/addParam", method = RequestMethod.POST)
    public R addParam(@RequestBody Map<String, Object> params) {

        // 判断相应任务是否存在
        JobDetailType jobDetailType = qrtzConfigService.getJobDetail(params);

        if (jobDetailType == null) {
            return R.error("保存失败，指定的任务在配置文件中不存在！");
        } else {
            if (!qrtzConfigService.existParam(params)) {
                return R.error("任务下已经存在同名参数");
            }
        }

        if (!qrtzConfigService.addParam(params)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 更新参数
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "更新参数", httpMethod = "PUT", notes = "更新参数")
    @AceAuth("更新参数")
    @RequestMapping(value = "/updParam", method = RequestMethod.PUT)
    public R updParam(@RequestBody Map<String, Object> params) {

        // 判断参数是否存在
        if (qrtzConfigService.existParam(params)) {
            return R.error("保存失败，正在更新的参数在配置文件中不存在");
        }

        if (!qrtzConfigService.updParam(params)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 删除参数
     * <p>
     *
     * @param params
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "删除参数", httpMethod = "DELETE", notes = "删除参数")
    @AceAuth("删除参数")
    @RequestMapping(value = "/delParam", method = RequestMethod.DELETE)
    public R delParam(@RequestBody Map<String, Object> params) {

        if (!qrtzConfigService.delParam(params)) {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
    }

    /**
     * 运行所有服务任务
     *
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "运行所有服务任务", httpMethod = "GET", notes = "运行所有服务任务")
    @AceAuth("运行所有服务任务")
    @RequestMapping(value = "/clickAllRun", method = RequestMethod.GET)
    public R clickAllRun() {
        if (!qrtzConfigService.clickAllRun()) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 暂停所有服务任务
     *
     * @return
     * @author generator
     * @date 2019-07-16 09:59:40
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "暂停所有服务任务", httpMethod = "GET", notes = "暂停所有服务任务")
    @AceAuth("暂停所有服务任务")
    @RequestMapping(value = "/clickAllPause", method = RequestMethod.GET)
    public R clickAllPause() {
        if (!qrtzConfigService.clickAllPause()) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }


}
