package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.ProDatasourceService;
import com.csicit.ace.dev.service.ProModelService;
import com.csicit.ace.dev.service.ProServiceService;
import com.csicit.ace.dev.util.DBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 实体模型 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:31:19
 */

@RestController
@RequestMapping("/proModels")
@Api("实体模型")
public class ProModelController extends BaseController {

    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProServiceService proServiceService;
    @Autowired
    private ProDatasourceService proDatasourceService;

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
        ProModelDO instance = proModelService.getById(id);
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
        String appId = params.get("appId").toString();
        String searchStr = (String) params.get("searching");
        List<ProModelDO> list = proModelService.list(new QueryWrapper<ProModelDO>()
                .eq("service_id", appId).and(StringUtils.isNotBlank(searchStr)
                        , i -> i.like("model_name", searchStr).or().like("table_name", searchStr).or().like
                                ("object_name", searchStr)).orderByDesc("create_time"));
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
        Page<ProModelDO> page = new Page<>(current, size);
        IPage list = proModelService.page(page, new QueryWrapper<ProModelDO>()
                .eq("service_id", params.get("appId")).and(StringUtils.isNotBlank(searchStr)
                        , i -> i.like("model_name", searchStr).or().like("table_name", searchStr).or().like
                                ("object_name", searchStr)).orderByDesc("create_time"));
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaViewDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody ProModelDO instance) {
        int count = proModelService.count(new QueryWrapper<ProModelDO>()
                .eq("service_id", instance.getServiceId()).eq("model_name", instance.getModelName()));
        if (count > 0) {
            return R.error("在该服务下，已存在相同模型名称");
        }

        int count1 = proModelService.count(new QueryWrapper<ProModelDO>()
                .eq("service_id", instance.getServiceId()).eq("object_name", instance.getObjectName()));
        if (count1 > 0) {
            return R.error("在该服务下，已存在相同表实体名");
        }
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("SERVICE_ID", instance.getServiceId()));
        if (DBUtil.validateTableNameExist(instance.getTableName(), proDatasourceDO)) {
            return R.error("在该服务对应的数据源下，已存在相同表名");
        }


        if (proModelService.addModel(instance)) {
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaViewDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody ProModelDO instance) {
        ProModelDO oldModel = proModelService.getById(instance.getId());
        if (!Objects.equals(instance.getModelName(), oldModel
                .getModelName())) {
            int count = proModelService.count(new QueryWrapper<ProModelDO>()
                    .eq("service_id", instance.getServiceId()).eq("model_name", instance.getModelName()));
            if (count > 0) {
                return R.error("在同一服务下，已存在相同模型名称");
            }
        }
        if (!Objects.equals(instance.getObjectName(), oldModel
                .getObjectName()) && !Objects.equals(instance.getObjectName().toLowerCase(), oldModel
                .getObjectName().toLowerCase())) {
            int count = proModelService.count(new QueryWrapper<ProModelDO>()
                    .eq("service_id", instance.getServiceId()).eq("object_name", instance.getObjectName()));
            if (count > 0) {
                return R.error("在同一服务下，已存在相同表实体名");
            }
        }
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("SERVICE_ID", instance.getServiceId()));
        if (!Objects.equals(instance.getTableName(), oldModel
                .getTableName())) {
            if (DBUtil.validateTableNameExist(instance.getTableName(), proDatasourceDO)) {
                return R.error("在该服务对应的数据源下，已存在相同表名");
            }
        }

        if (proModelService.updModel(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }

        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }


    /**
     * @param id
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "id", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestParam String id) {
        if (proModelService.delModel(id)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }


//    /**
//     * 发布服务
//     *
//     * @param params ID数组
//     * @return 删除响应结果
//     * @author zuog
//     * @date 2019-11-07 10:30:31
//     */
//    @ApiOperation(value = "发布服务", httpMethod = "POST", notes = "发布服务")
//    @ApiImplicitParam(name = "Map", value = "params", required = true, allowMultiple = true, dataType = "Map")
//    @RequestMapping(value = "/publishService", method = RequestMethod.POST)
//    public R publishService(@RequestBody Map<String, String> params) {
//
//        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
//    }
}
