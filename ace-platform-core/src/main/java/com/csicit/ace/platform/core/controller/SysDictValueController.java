package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 数据字典数据管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/5/21 8:22
 */
@RestController
@RequestMapping("/sysDictValues")
@Api("数据字典数据管理")
public class SysDictValueController extends BaseController {
    /**
     * 根据id获取单个字典数据
     *
     * @param id 单个字典id
     * @return 字典数据对象
     * @author shanwj
     * @date 2019/5/21 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个字典数据", httpMethod = "GET", notes = "根据id获取单个字典数据")
    @AceAuth("获取单个字典数据")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysDictValueDO SysDictValueDO = sysDictValueService.getById(id);
        return R.ok().put("instance", SysDictValueDO);
    }

    /**
     * 根据条件获取字典数据列表并分页
     *
     * @param params 请求参数map对象
     * @return 字典数据列表
     * @author shanwj
     * @date 2019/5/11 18:21
     */
    @ApiOperation(value = "获取字典数据列表并分页", httpMethod = "GET", notes = "获取字典数据列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取字典数据列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String typeId = params.get("typeId").toString();
        List<SysDictValueDO> list =
                sysDictValueService.list(new QueryWrapper<SysDictValueDO>().and(StringUtils.isNotBlank
                                ((String) params.get("name"))
                        , i -> i.like("dict_name", params.get("name")).or().like("type", params.get("name")))
                        .eq("type_id", typeId).orderByAsc("sort_path"));
        //生成树结构的数据
        List<SysDictValueDO> listT = TreeUtils.makeTree(list, SysDictValueDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 新增字典数据保存
     *
     * @param instance 字典数据对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存字典数据")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysDictValueDO")
    @AceAuth("保存字典数据")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysDictValueDO instance) {
        //判断父节点是否存在
        if (!Objects.equals("0", instance.getParentId()) &&
                sysDictValueService.count(new QueryWrapper<SysDictValueDO>().eq("id", instance.getParentId())) == 0) {
            return R.error(InternationUtils.getInternationalMsg("PARENT_DELETE"));
        }
        int count = sysDictValueService.count(
                new QueryWrapper<SysDictValueDO>()
                        .eq("sort_index", instance.getSortIndex())
                        .eq("type_id", instance.getTypeId())
                        .eq("parent_id", instance.getParentId()));
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("SHORT_INDEX"), instance.getSortIndex()
                            .toString()}
            ));
        }

        if (sysDictValueService.saveDictValue(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改字典数据对象
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改字典数据")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysDictValueDO")
    @AceAuth("修改字典数据")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysDictValueDO instance) {
        if (!Objects.equals(instance.getSortIndex(), sysDictValueService.getById(instance.getId()).getSortIndex())) {
            int count = sysDictValueService.count(
                    new QueryWrapper<SysDictValueDO>()
                            .eq("sort_index", instance.getSortIndex())
                            .eq("type_id", instance.getTypeId())
                            .eq("parent_id", instance.getParentId()));
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("SHORT_INDEX"), instance.getSortIndex()
                                .toString()}
                ));
            }
        }
        if (sysDictValueService.updateDictValue(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 根据id数组删除数据
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除字典数据")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @AceAuth("删除字典数据")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysDictValueService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
