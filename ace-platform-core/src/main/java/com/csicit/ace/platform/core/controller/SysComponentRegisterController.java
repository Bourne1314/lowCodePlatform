package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysComponentRegisterDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.SysAuthService;
import com.csicit.ace.platform.core.service.SysComponentRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 组件注册 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-14 08:00:27
 */

@RestController
@RequestMapping("/sysComponentRegisters")
@Api("组件注册")
public class SysComponentRegisterController extends BaseController {

    @Autowired
    private SysComponentRegisterService sysComponentRegisterService;
    @Autowired
    private SysAuthService sysAuthService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个组件注册信息", httpMethod = "GET", notes = "获取单个组件注册信息")
    @AceAuth("获取单个组件注册信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysComponentRegisterDO instance = sysComponentRegisterService.getById(id);
        if (StringUtils.isNotBlank(instance.getAuthId())) {
            instance.setAuthName(sysAuthService.getById(instance.getAuthId()).getName());
        }
        return R.ok().put("instance", instance);
    }

    /**
     * 获取组件注册信息列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取组件注册信息列表", httpMethod = "GET", notes = "获取组件注册信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取组件注册信息列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        Integer currentStr = Integer.parseInt((String) params.get("current"));
        Integer sizeStr = Integer.parseInt((String) params.get("size"));
        Map<String, Object> equalMap = new HashMap<>();
        equalMap.put("app_id", params.get("app_id"));
        Map<String, Object> likeMap = new HashMap<>();
        likeMap.put("name", params.get("name"));
        Page<SysComponentRegisterDO> page = new Page<>(currentStr, sizeStr);
        IPage list = sysComponentRegisterService.page(page, MapWrapper.getEqualAndLikeInstance(equalMap, likeMap));
        return R.ok().put("page", list);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author shanwj
     * @date 2019-11-04 14:49:22
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public R queryList(@RequestParam Map<String, Object> params) {
        List<SysComponentRegisterDO> list = sysComponentRegisterService.list(new QueryWrapper<SysComponentRegisterDO>()
                .eq("app_id", params.get("app_id")).orderByAsc("name"));
        return R.ok().put("list", list);
    }


    /**
     * 新增组件注册信息
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "新增组件注册信息", httpMethod = "POST", notes = "新增组件注册信息")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysComponentRegisterDO")
    @AceAuth("新增组件注册信息")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysComponentRegisterDO instance) {
        if (sysComponentRegisterService.count(new QueryWrapper<SysComponentRegisterDO>()
                .eq("app_id", instance.getAppId()).eq("name", instance.getName())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{"NAME", instance.getName()}));
        }
        if (sysComponentRegisterService.addComponent(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改组件注册信息
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "修改组件注册信息", httpMethod = "PUT", notes = "修改组件注册信息")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysComponentRegisterDO")
    @AceAuth("修改组件注册信息")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysComponentRegisterDO instance) {
        if (!Objects.equals(instance.getName(), sysComponentRegisterService.getById(instance.getId()).getName())) {
            if (sysComponentRegisterService.count(new QueryWrapper<SysComponentRegisterDO>()
                    .eq("app_id", instance.getAppId()).eq("name", instance.getName())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{"NAME", instance.getName()}));
            }
        }
        if (sysComponentRegisterService.editComponent(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除组件注册信息
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "删除组件注册信息", httpMethod = "DELETE", notes = "删除组件注册信息")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除组件注册信息")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysComponentRegisterService.delComponent(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));


    }
}
