package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysMsgTypeExtendService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/9/9 14:10
 */
@RestController
@RequestMapping("/sysMsgTypeExtends")
public class SysMsgTypeExtendController extends BaseController {

    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;


    /**
     * 根据id获取单个扩展类型
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个扩展类型", httpMethod = "GET", notes = "根据id获取单个扩展类型")
    @AceAuth("获取单个扩展类型")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        return R.ok().put("instance", sysMsgTypeExtendService.getById(id));
    }


    /**
     * 根据条件获取扩展类型列表并分页
     *
     * @param params 请求参数map对象
     * @return 扩展类型列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取扩展类型列表并分页", httpMethod = "GET", notes = "获取扩展类型列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取扩展类型列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String groupId = securityUtils.getCurrentGroupId();
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = sysMsgTypeExtendService.list(MapWrapper.getEqualInstance(params)
                    .inSql(StringUtils.isNotBlank(groupId), "app_id", "select id from sys_group_app where group_id = '" + groupId + "'"));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<SysMsgTypeExtendDO> page = new Page<>(current, size);
        IPage list = sysMsgTypeExtendService.page(page, MapWrapper.getEqualInstance(params)
                .inSql(StringUtils.isNotBlank(groupId), "app_id", "select id from sys_group_app where group_id = '" + groupId + "'"));
        return R.ok().put("page", list);
    }


    /**
     * 新增扩展类型保存
     *
     * @param instance 扩展类型对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存扩展类型")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMsgTypeExtendDO")
    @AceAuth("保存扩展类型对象")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysMsgTypeExtendDO instance) {
        // 判断扩展类型名称是否已存在
        int count =
                sysMsgTypeExtendService.count(
                        new QueryWrapper<SysMsgTypeExtendDO>()
                                .eq("name", instance.getName()));
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL", instance.getName()));
        }

        if (sysMsgTypeExtendService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }


    /**
     * 修改扩展类型保存
     *
     * @param instance 对象
     * @return 修改扩展类型
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改扩展类型", httpMethod = "PUT", notes = "修改扩展类型")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMsgTypeExtendDO")
    @AceAuth("修改扩展类型")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysMsgTypeExtendDO instance) {
        if (sysMsgTypeExtendService.updateById(instance)) {
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
    @AceAuth("删除扩展类型对象")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysMsgTypeExtendService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
