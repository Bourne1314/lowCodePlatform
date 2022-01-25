package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 集团绑定数据源访问接口层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/17 17:07
 */
@RestController
@RequestMapping("/sysGroupDatasources")
@Api("集团绑定数据源管理")
public class SysGroupDatasourceController extends BaseController {

    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;

    /**
     * 获取单个数据源
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个数据源", httpMethod = "GET", notes = "获取单个数据源")
    @AceAuth("获取单个数据源")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysGroupDatasourceDO instance = sysGroupDatasourceService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取数据源列表并分页
     *
     * @param params 请求参数map对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "获取数据源列表并分页", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取数据源列表并分页")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List<SysGroupDatasourceDO> list = sysGroupDatasourceService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<SysGroupDatasourceDO> page = new Page<>(current, size);
        IPage list = sysGroupDatasourceService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 保存数据源
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "保存数据源", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysGroupDatasourceDO")
    @AceAuth("保存数据源")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody SysGroupDatasourceDO instance) {
        return sysGroupDatasourceService.insert(instance);
    }

    /**
     * 修改数据源
     *
     * @param instance 对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "修改数据源", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysGroupDatasourceDO")
    @AceAuth("修改数据源")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody SysGroupDatasourceDO instance) {
        return sysGroupDatasourceService.update(instance);
    }

    /**
     * 删除数据源
     *
     * @param ids ID数组
     * @return com.csicit.ace.common.utils.server.R
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "删除数据源", httpMethod = "DELETE")
    @ApiImplicitParam(name = "groupIds", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除数据源")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        return sysGroupDatasourceService.delete(ids);
    }

}
