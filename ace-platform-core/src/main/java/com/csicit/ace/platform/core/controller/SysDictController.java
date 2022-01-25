package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysDictDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 数据字典类型管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/5/21 8:22
 */
@RestController
@RequestMapping("/sysDicts")
@Api("数据字典类型管理")
public class SysDictController extends BaseController {

    /**
     * 根据id获取单个字典类型
     *
     * @param id 单个字典id
     * @return 字典类型对象
     * @author shanwj
     * @date 2019/5/21 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个字典类型", httpMethod = "GET", notes = "根据id获取单个字典类型")
    @AceAuth("获取单个字典类型")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysDictDO sysDictDO = sysDictService.getById(id);
        return R.ok().put("instance", sysDictDO);
    }

    /**
     * 根据条件获取字典类型列表并分页
     *
     * @param params 请求参数map对象
     * @return 字典类型列表
     * @author shanwj
     * @date 2019/5/11 18:21
     */
    @ApiOperation(value = "获取字典类型列表并分页", httpMethod = "GET", notes = "获取字典类型列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取字典类型列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt(params.get("current").toString());
        int size = Integer.parseInt(params.get("size").toString());
        String appId = (String) params.get("appId");
        String groupId = (String) params.get("groupId");
        String name = (String) params.get("name");
        Integer scope = Integer.parseInt(params.get("scope").toString());
        Page<SysDictDO> page = new Page<>(current, size);
        IPage list = null;

        if (Objects.equals(scope, 1)) {
            // 租户级字典列表
            list = sysDictService.page(page, new QueryWrapper<SysDictDO>().eq("scope",
                    1).and(StringUtils.isNotBlank
                    (name), i -> i.like("name", name).or().like("type", name
            )));
        } else if (Objects.equals(scope, 2) && StringUtils.isNotBlank(groupId)) {
            // 集团级字典列表
            list = sysDictService.page(page, new QueryWrapper<SysDictDO>().eq("scope",
                    2).eq("group_id", groupId).and(StringUtils.isNotBlank
                    (name), i -> i.like("name", name).or().like("type", name
            )));
        } else if (Objects.equals(scope, 3) && StringUtils.isNotBlank(appId)) {
            // 应用级字典列表
            list = sysDictService.page(page, new QueryWrapper<SysDictDO>().eq("scope",
                    3).eq("app_id", appId).and(StringUtils.isNotBlank
                    (name), i -> i.like("name", name).or().like("type", name
            )));
        }
        return R.ok().put("page", list);
    }

    /**
     * 新增字典类型保存
     *
     * @param instance 字典类型对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存字典类型")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysDictDO")
    @AceAuth("保存字典类型对象")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysDictDO instance) {
        // 判断字典类型是否已存在
        int count = 0;
        if (Objects.equals(instance.getScope(), 1)) {
            count = sysDictService.count(new QueryWrapper<SysDictDO>()
                    .eq("group_id", "").eq("app_id", "").eq("type", instance.getType()));
        } else if (Objects.equals(instance.getScope(), 2)) {
            count = sysDictService.count(new QueryWrapper<SysDictDO>()
                    .eq("app_id", "").eq("group_id", instance.getGroupId()).eq("type",
                            instance.getType()));
        } else if (Objects.equals(instance.getScope(), 3)) {
            count = sysDictService.count(new QueryWrapper<SysDictDO>().eq("app_id", instance.getAppId()).eq("type",
                    instance.getType()));
        }
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{"字典类型", instance.getType()}
            ));
        }

        if (sysDictService.saveDict(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改字典类型对象
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改字典类型对象", httpMethod = "PUT", notes = "修改字典类型")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysDictDO")
    @AceAuth("修改字典类型对象")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysDictDO instance) {
        // 字典类型变更后，
        if (!Objects.equals(instance.getType(), sysDictService.getById(instance.getId()).getType())) {
            // 判断字典类型是否已存在
            int count = 0;
            if (Objects.equals(instance.getScope(), 1)) {
                count = sysDictService.count(new QueryWrapper<SysDictDO>()
                        .eq("group_id", "").eq("app_id", "").eq("type", instance.getType()));
            } else if (Objects.equals(instance.getScope(), 2)) {
                count = sysDictService.count(new QueryWrapper<SysDictDO>()
                        .eq("app_id", "").eq("group_id", instance.getGroupId()).eq("type",
                                instance.getType()));
            } else if (Objects.equals(instance.getScope(), 3)) {
                count = sysDictService.count(new QueryWrapper<SysDictDO>().eq("app_id", instance.getAppId()).eq("type",
                        instance.getType()));
            }
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{"字典类型", instance.getType()}
                ));
            }
        }
        if (sysDictService.updateDict(instance)) {
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
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除字典类型")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @AceAuth("删除字典类型对象")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysDictService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }


}
