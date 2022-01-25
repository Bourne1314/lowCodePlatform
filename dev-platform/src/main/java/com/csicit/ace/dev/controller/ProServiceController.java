package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.ProDatasourceService;
import com.csicit.ace.dev.service.ProServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 服务管理 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/proServices")
@Api("服务管理")
public class ProServiceController extends BaseController {

    @Autowired
    private ProServiceService proServiceService;

//    @Autowired
//    private ProDatasourceService proDatasourceService;

    /**
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:proService:view")
    public R get(@PathVariable("id") String id) {
        ProServiceDO instance = proServiceService.getById(id);
//        if (instance.getDsId() != null) {
//            ProDatasourceDO proDatasourceDO = proDatasourceService.getById(instance.getDsId());
//            if (proDatasourceDO != null) {
//                instance.setDsName(proDatasourceDO.getName());
//            }
//        }
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
    @RequiresPermissions("sys:proService:list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<ProServiceDO> page = new Page<>(current, size);
        IPage list = proServiceService.page(page, new QueryWrapper<ProServiceDO>()
                .eq("is_delete", 0).eq("pro_info_id", params.get("pro_info_id")).orderByAsc("name"));
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
    @RequestMapping(value = "/query/list/{infoId}", method = RequestMethod.GET)
    @RequiresPermissions("sys:proService:listNoPage")
    public R listAll(@PathVariable("infoId") String infoId) {
        List<ProServiceDO> list = proServiceService.list(new QueryWrapper<ProServiceDO>()
                .eq("is_delete", 0).eq("pro_info_id",infoId).orderByAsc("name"));
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions("sys:proService:add")
    public R save(@RequestBody ProServiceDO instance) {
        Integer count = proServiceService.count(new QueryWrapper<ProServiceDO>()
                .eq("name", instance.getName()));
        if (count > 0) {
            return R.error("已存在相同的服务名称");
        }
        Integer count1 = proServiceService.count(new QueryWrapper<ProServiceDO>()
                .eq("ip_port", instance.getIpPort()));
        if (count1 > 0) {
            return R.error("已存在相同端号的服务");
        }
        if (proServiceService.count(new QueryWrapper<ProServiceDO>()
                .eq("app_id", instance.getAppId())) > 0) {
            return R.error("已存在相同的服务标识");
        }
        instance.setCreateServiceFlg(0);
        if (proServiceService.saveProService(instance)) {
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions("sys:proService:edit")
    public R update(@RequestBody ProServiceDO instance) {
        ProServiceDO oldApp = proServiceService.getById(instance.getId());
        if (!Objects.equals(oldApp.getName(), instance.getName())) {
            Integer count = proServiceService.count(new QueryWrapper<ProServiceDO>()
                    .eq("name", instance.getName()));
            if (count > 0) {
                return R.error("已存在相同的服务名称");
            }
        }
        if (!Objects.equals(oldApp.getIpPort(), instance.getIpPort())) {
            Integer count1 = proServiceService.count(new QueryWrapper<ProServiceDO>()
                    .eq("ip_port", instance.getIpPort()));
            if (count1 > 0) {
                return R.error("已存在相同端号的服务");
            }
        }
        if (!Objects.equals(oldApp.getAppId(), instance.getAppId())) {
            if (proServiceService.count(new QueryWrapper<ProServiceDO>()
                    .eq("app_id", instance.getAppId())) > 0) {
                return R.error("已存在相同的服务标识");
            }
        }
        if (proServiceService.updateById(instance)) {
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
    @RequiresPermissions("sys:proService:del")
    public R delete(@RequestBody String[] ids) {
        if (proServiceService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 判断当前应用的服务是否启动中
     *
     * @param params
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "判断当前应用的服务是否启动中", httpMethod = "POST", notes = "判断当前应用的服务是否启动中")
    @ApiImplicitParam(name = "params", value = "Map", required = true, allowMultiple = true, dataType = "Map")
    @RequestMapping(value = "/server/runJudge", method = RequestMethod.POST)
    public R serverRunJudge(@RequestBody Map<String, Object> params) {
        String appId = params.get("appId").toString();

        return R.ok().put("serverRunFlg", proServiceService.serverRunJudge(appId));
    }

    /**
     * 关闭服务
     *
     * @param params
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "关闭服务", httpMethod = "POST", notes = "关闭服务")
    @ApiImplicitParam(name = "params", value = "Map", required = true, allowMultiple = true, dataType = "Map")
    @RequestMapping(value = "/server/close", method = RequestMethod.POST)
    public R closeServer(@RequestBody Map<String, Object> params) {
        String appId = params.get("appId").toString();
        if (proServiceService.closeServer(appId)) {
            return R.ok("服务关闭成功！");
        }
        return R.error("服务关闭失败！");
    }

    /**
     * 开启服务
     *
     * @param params
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "开启服务", httpMethod = "POST", notes = "开启服务")
    @ApiImplicitParam(name = "params", value = "Map", required = true, allowMultiple = true, dataType = "Map")
    @RequestMapping(value = "/server/run", method = RequestMethod.POST)
    public R runServer(@RequestBody Map<String, Object> params) {
        String appId = params.get("appId").toString();
        if (proServiceService.runServer(appId)) {
            return R.ok("服务开启成功！");
        }
        return R.error("服务开启失败！");
    }

}
