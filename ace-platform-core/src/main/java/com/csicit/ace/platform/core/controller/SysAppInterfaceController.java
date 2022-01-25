package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceOutputDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.platform.core.service.SysAppInterfaceInputService;
import com.csicit.ace.platform.core.service.SysAppInterfaceOutputService;
import com.csicit.ace.platform.core.service.SysAppInterfaceService;
import com.csicit.ace.platform.core.service.SysAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csicit.ace.common.utils.internationalization.InternationUtils;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 应用接口信息表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-03 09:03:59
 */

@RestController
@RequestMapping("/sysAppInterfaces")
@Api("应用接口信息表")
public class SysAppInterfaceController extends BaseController {

    @Autowired
    private SysAppInterfaceService sysAppInterfaceService;

    @Autowired
    private SysAppInterfaceInputService sysAppInterfaceInputService;

    @Autowired
    private SysAppInterfaceOutputService sysAppInterfaceOutputService;

    @Autowired
    private SysAuthService sysAuthService;

    @Autowired
    private SysGroupDatasourceService sysGroupDatasourceService;

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
        SysAppInterfaceDO instance = sysAppInterfaceService.getById(id);
        if (instance != null) {
            if (StringUtils.isNotBlank(instance.getAuthId())) {
                instance.setAuthName(sysAuthService.getById(instance.getAuthId()).getName());
            }
            if (StringUtils.isNotBlank(instance.getDsId())) {
                instance.setDsName(sysGroupDatasourceService.getById(instance.getDsId()).getName());
            }
            instance.setInputParams(sysAppInterfaceInputService.list(new QueryWrapper<SysAppInterfaceInputDO>()
                    .eq("interface_id", instance.getId())));
            instance.setOutPutParams(sysAppInterfaceOutputService.list(new QueryWrapper<SysAppInterfaceOutputDO>()
                    .eq("interface_id", instance.getId())));
        }
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
        String appId = params.get("app_id").toString();
        List<SysAppInterfaceDO> list = sysAppInterfaceService.list(new QueryWrapper<SysAppInterfaceDO>()
                .eq("app_id", appId).orderByDesc("create_time"));
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
        String searchStr = (String) params.get("searching");
        String appId = (String) params.get("app_id");
        Page<SysAppInterfaceDO> page = new Page<>(current, size);
        IPage list = sysAppInterfaceService.page(page, new QueryWrapper<SysAppInterfaceDO>()
                .and(StringUtils.isNotBlank(searchStr)
                        , i -> i.like("name", searchStr))
                .eq("app_id", appId).orderByDesc("create_time"));
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysAppInterfaceDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysAppInterfaceDO instance) {
        if (sysAppInterfaceService.count(new QueryWrapper<SysAppInterfaceDO>()
                .eq("app_id", instance.getAppId()).eq("name", instance.getName())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
            ));
        }
        if (sysAppInterfaceService.count(new QueryWrapper<SysAppInterfaceDO>()
                .eq("app_id", instance.getAppId()).eq("code", instance.getCode())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("CODE"), instance.getCode()}
            ));
        }
        if (sysAppInterfaceService.addInterface(instance)) {
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysAppInterfaceDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysAppInterfaceDO instance) {
        SysAppInterfaceDO oldModel = sysAppInterfaceService.getById(instance.getId());
        if (!Objects.equals(instance.getName(), oldModel.getName())) {
            if (sysAppInterfaceService.count(new QueryWrapper<SysAppInterfaceDO>()
                    .eq("app_id", instance.getAppId()).eq("name", instance.getName())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
                ));
            }
        }
        if (!Objects.equals(instance.getCode(), oldModel.getCode())) {
            if (sysAppInterfaceService.count(new QueryWrapper<SysAppInterfaceDO>()
                    .eq("app_id", instance.getAppId()).eq("code", instance.getCode())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("CODE"), instance.getCode()}
                ));
            }
        }

        if (sysAppInterfaceService.updInterface(instance)) {
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
        if (sysAppInterfaceService.delInterface(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * sql代码测试检查
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysAppInterfaceDO")
    @RequestMapping(value = "/action/sqlCode/check", method = RequestMethod.POST)
    public R sqlCodeCheck(@RequestBody SysAppInterfaceDO instance) {
        return sysAppInterfaceService.sqlCodeCheck(instance);
    }
}
