package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProInfoDO;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dev.service.ProInfoService;
import com.csicit.ace.dev.service.ProServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 项目管理 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/proInfos")
@Api("项目管理")
public class ProInfoController extends BaseController {

    @Autowired
    private ProInfoService proInfoService;

    @Autowired
    private ProServiceService proServiceService;

    @Autowired
    private SecurityUtils securityUtils;

    /**
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:proInfo:view")
    public R get(@PathVariable("id") String id) {
        ProInfoDO instance = proInfoService.getProInfoDO(id);
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
    @RequiresPermissions("sys:proInfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<ProInfoDO> page = new Page<>(current, size);
        IPage list = null;
        List<String> roleIds = securityUtils.getRoleIds();
        // 项目开发人员
        if(CollectionUtils.isEmpty(roleIds)){
            return R.ok().put("page", list);
        }
        if (roleIds.contains("devdeveloper")) {
            List<String> infoIds = proServiceService.list(new QueryWrapper<ProServiceDO>()
                    .eq("IS_DELETE", 0).like("MAINTAIN_STAFFS", securityUtils.getCurrentUserId()))
                    .stream().map(ProServiceDO::getProInfoId).collect(Collectors.toList());

            list = proInfoService.page(page, new QueryWrapper<ProInfoDO>()
                    .and(infoIds == null || infoIds.size() == 0, i -> i.eq("1", "2"))
                    .in("id", infoIds)
                    .eq("is_delete", 0).orderByAsc("name"));
        } else if (roleIds.contains("devmaintainer")) {
            // 项目管理人员
            list = proInfoService.page(page, new QueryWrapper<ProInfoDO>().eq("create_user", securityUtils
                    .getCurrentUserId()).eq("is_delete", 0).orderByAsc("name"));
        } else {
            list = proInfoService.page(page, new QueryWrapper<ProInfoDO>()
                    .eq("is_delete", 0).orderByAsc("name"));
        }

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
    @RequiresPermissions("sys:proInfo:listNoPage")
    public R listAll() {
        List<String> roleIds = securityUtils.getRoleIds();
        List<ProInfoDO> list = null;
        // 开发人员
        if (roleIds.contains("devdeveloper")) {
            List<String> infoIds = proServiceService.list(new QueryWrapper<ProServiceDO>()
                    .eq("IS_DELETE", 0).like("MAINTAIN_STAFFS", securityUtils.getCurrentUserId()))
                    .stream().map(ProServiceDO::getProInfoId).collect(Collectors.toList());
            list = proInfoService.list(new QueryWrapper<ProInfoDO>()
                    .eq("is_delete", 0)
                    .and(infoIds == null || infoIds.size() == 0, i -> i.eq("1", "2"))
                    .in("id", infoIds).orderByAsc("name"));
        } else if (roleIds.contains("devmaintainer")) {
            list = proInfoService.list(new QueryWrapper<ProInfoDO>().eq("create_user", securityUtils
                    .getCurrentUserId()).eq("is_delete", 0).orderByAsc("name"));
        } else {
            list = proInfoService.list(new QueryWrapper<ProInfoDO>()
                    .eq("is_delete", 0).orderByAsc("name"));
        }
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
    @RequiresPermissions("sys:proInfo:add")
    public R save(@RequestBody ProInfoDO instance) {
        Integer count = proInfoService.count(new QueryWrapper<ProInfoDO>()
                .eq("name", instance.getName()));
        if (count > 0) {
            return R.error("已存在相同的项目名称");
        }
        if (proInfoService.saveInfo(instance)) {
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
    @RequiresPermissions("sys:proInfo:edit")
    public R update(@RequestBody ProInfoDO instance) {
        ProInfoDO oldProject = proInfoService.getById(instance.getId());
        if (!Objects.equals(oldProject.getName(), instance.getName())) {
            Integer count = proInfoService.count(new QueryWrapper<ProInfoDO>()
                    .eq("name", instance.getName()));
            if (count > 0) {
                return R.error("已存在相同的项目名称");
            }
        }
        if (proInfoService.updateById(instance)) {
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
    @RequiresPermissions("sys:proInfo:del")
    public R delete(@RequestBody String[] ids) {
        if (proInfoService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
