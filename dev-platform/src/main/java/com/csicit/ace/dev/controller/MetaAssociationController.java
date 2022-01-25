package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.MetaAssociationDO;
import com.csicit.ace.common.pojo.domain.dev.MetaIndexDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 数据关联定义 接口访问层
 *
 * @author shanwj
 * @date 2019-11-07 10:31:39
 * @version V1.0
 */
 
@RestController
@RequestMapping("/metaAssociations")
@Api("数据关联定义")
public class MetaAssociationController extends BaseController {

    /**
     *
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        MetaAssociationDO instance = metaAssociationService.getById(id);
        MetaTableDO tableDO = metaTableService.getById(instance.getRefTableId());
        MetaIndexDO index = metaIndexService.getById(instance.getRefIndexId());
        instance.setRefTableName(tableDO!=null?tableDO.getTableName():"");
        instance.setRefIndexName(index!=null?index.getName():"");
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
    @ApiImplicitParam(name = "params", value = "参数", required = true,dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<MetaAssociationDO> page = new Page<>(current, size);
        IPage list = metaAssociationService.page(page, MapWrapper.getEqualInstance(params));
        List<MetaAssociationDO> records = (List<MetaAssociationDO>) list.getRecords();
        records.forEach(record->{
            MetaTableDO tableDO = metaTableService.getById(record.getRefTableId());
            MetaIndexDO index = metaIndexService.getById(record.getRefIndexId());
            record.setRefTableName(tableDO!=null?tableDO.getTableName():"");
            record.setRefIndexName(index!=null?index.getName():"");
        });
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance	 对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaAssociationDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody MetaAssociationDO instance) {
        if (metaAssociationService.saveAssociation(instance)) {
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
    public R update(@RequestBody MetaAssociationDO instance) {
        if (metaAssociationService.updateAssociation(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     *
     * @param id id
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-11-04 14:49:11
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "id", value = "id", required = true,dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestParam String id) {
        if (metaAssociationService.deleteAssociation(id)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:39
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple=true, dataType = "String")
    @RequestMapping(value = "/delete/multi",method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (metaAssociationService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
