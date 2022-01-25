package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProModelAssociationDO;
import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.dev.service.ProModelAssociationService;
import com.csicit.ace.dev.service.ProModelService;
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
 * 数据关联定义 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:31:39
 */

@RestController
@RequestMapping("/proModelAssociations")
@Api("数据关联定义")
public class ProModelAssociationController extends BaseController {
    @Autowired
    protected ProModelAssociationService proModelAssociationService;
    @Autowired
    protected ProModelService proModelService;

    /**
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ProModelAssociationDO instance = proModelAssociationService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<ProModelAssociationDO> page = new Page<>(current, size);
        IPage list = proModelAssociationService.page(page, MapWrapper.getEqualInstance(params));
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
        List<ProModelAssociationDO> list = proModelAssociationService.list(new QueryWrapper<ProModelAssociationDO>()
                .eq("model_id", params.get("model_id")).orderByAsc("create_time"));
        list.stream().forEach(proModelAssociationDO -> {
            ProModelDO proModelDO = proModelService.getById(proModelAssociationDO.getRefModelId());
            if (proModelDO != null && StringUtils.isNotBlank(proModelDO.getModelName())) {
                proModelAssociationDO.setRefModelName(proModelDO.getModelName());
            }
        });
        return R.ok().put("list", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaAssociationDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody ProModelAssociationDO instance) {
        if (proModelAssociationService.count(new QueryWrapper<ProModelAssociationDO>()
                .eq("model_id", instance.getModelId()).eq("name", instance.getName())) > 0) {
            return R.error("已存在相同关联外键名");
        }
        if (Objects.equals(instance.getModelId(), instance.getRefModelId())) {
            return R.error("参照表选择出错！");
        }
        if (proModelAssociationService.saveAssociation(instance)) {
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
     * @date 2019-11-07 10:31:39
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaAssociationDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody ProModelAssociationDO instance) {
        if (!Objects.equals(instance.getName(), proModelAssociationService.getById(instance.getId()).getName())) {
            if (proModelAssociationService.count(new QueryWrapper<ProModelAssociationDO>()
                    .eq("model_id", instance.getModelId()).eq("name", instance.getName())) > 0) {
                return R.error("已存在相同关联外键名");
            }
        }
        if (Objects.equals(instance.getModelId(), instance.getRefModelId())) {
            return R.error("参照表选择出错！");
        }
        if (proModelAssociationService.updateAssociation(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param id id
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-11-04 14:49:11
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestParam String id) {
        if (proModelAssociationService.deleteAssociation(id)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(value = "/delete/multi", method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (proModelAssociationService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
