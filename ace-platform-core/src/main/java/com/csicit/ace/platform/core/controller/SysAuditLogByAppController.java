package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.AceDBHelperMapper;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.common.pojo.domain.SysAuditLogDO;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/6/3 18:13
 */
@RestController
@RequestMapping("/sysAuditLogsByApp")
@Api("应用系统日志管理")
public class SysAuditLogByAppController {

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    SysAuditLogService sysAuditLogService;

    /**
     * 获取审计日志列表
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "获取单条审计日志", httpMethod = "GET", notes = "获取单条审计日志")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "String")
    @AceAuth("获取单条审计日志")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        return R.ok().put("log", sysAuditLogService.getById(id));
    }

    @Autowired
    AceDBHelperMapper dbHelperMapper;

    public static final String  TYPE_ALL = "全部";

    /**
     * 获取审计日志的类别列表
     *
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "获取审计日志的类别列表", httpMethod = "GET", notes = "获取审计日志的类别列表")
    @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String")
    @AceAuth("获取审计日志的类别列表")
    @RequestMapping(value = "/action/getTypeList/{appId}", method = RequestMethod.GET)
    public R getTypeList(@PathVariable("appId") String appId) {
        List<String> types = new ArrayList<>();
        types.add(TYPE_ALL);
        types.addAll(dbHelperMapper.getStringsWithParams("select distinct type from sys_audit_log where app_id=''{0}''", appId));
        return R.ok().put("types",types);
    }


    /**
     * 获取审计日志列表
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "获取审计日志列表并分页", httpMethod = "GET", notes = "获取审计日志列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取审计日志列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        if (securityUtils.getCurrentUser().getUserType() == 3) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));

        String appId = (String)params.get("appId");

        String type = (String)params.get("type");

        if (StringUtils.isBlank(appId)) {
            return R.error(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }

        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");

        boolean beginTimeValid = StringUtils.isNotBlank(beginTime);
        boolean endTimeValid = StringUtils.isNotBlank(endTime);

        // 排除应用管理员 防止应用业务表与平台建在统一表空间下

        Page<SysAuditLogDO> page = new Page<>(current, size);
        IPage list;
        list = sysAuditLogService.page(page,
                new QueryWrapper<SysAuditLogDO>()
                        .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                        .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                        .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL,type)
                                ,"type", type)
                        .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                , i -> i.like("op_userName", params.get("op_userName"))
                                        .or().like("op_name", params.get("op_name"))
                                        .or().like("ip_address", params.get("ip_address"))
                                        .or().like("title", params.get("title")))
//                                        .or().like("op_content", params.get("op_content")))
                        .eq("app_id", appId)
                        .eq("user_type", 3)
                        .orderByDesc("op_time")
        );
        return R.ok().put("page", list);
    }
}
