package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.jdbc.JDBCUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.dev.service.MetaDatasourceService;
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
 * 数据源 接口访问层
 *
 * @author shanwj
 * @date 2019-11-04 14:48:24
 * @version V1.0
 */
 
@RestController
@RequestMapping("/metaDatasources")
@Api("数据源")
public class MetaDatasourceController extends BaseController {

	@Autowired
	private MetaDatasourceService metaDatasourceService;

    /**
     *
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:datasource:view")
    public R get(@PathVariable("id") String id) {
        MetaDatasourceDO instance = metaDatasourceService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true,dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("sys:datasource:list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<MetaDatasourceDO> page = new Page<>(current, size);
        IPage list = metaDatasourceService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public R listUserOrgApp() {
        List<MetaDatasourceDO> list = metaDatasourceService.list(null);
        return R.ok().put("list", list);
    }

    /**
     * 新增
     *
     * @param instance	 对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaDatasourceDO")
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions("sys:datasource:add")
    public R save(@RequestBody MetaDatasourceDO instance) {
        List<MetaDatasourceDO> dsList =
                metaDatasourceService.list(new QueryWrapper<MetaDatasourceDO>().eq("name", instance.getName()));
        if(dsList!=null&&dsList.size()>0){
            return R.error("已存在相同的数据源名称");
        }
        if(!instance.getUrl().toLowerCase().startsWith("jdbc:")){
            return R.error("连接路径格式不对");
        }
        instance.setType(JDBCUtils.getDriverType(instance.getUrl().toLowerCase()));
        instance.setScheme(instance.getUserName());
        if (metaDatasourceService.save(instance)) {
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
     * @date 2019-11-04 14:48:24
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaDatasourceDO")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions("sys:datasource:edit")
    public R update(@RequestBody MetaDatasourceDO instance) {
        String oldName = metaDatasourceService.getById(instance.getId()).getName();
        if(!Objects.equals(oldName,instance.getName())){
            List<MetaDatasourceDO> dsList =
                    metaDatasourceService.list(new QueryWrapper<MetaDatasourceDO>().eq("name", instance.getName()));
            if(dsList!=null&&dsList.size()>0){
                return R.error("已存在相同的数据源名称");
            }
        }
        if(!instance.getUrl().toLowerCase().startsWith("jdbc:")){
            return R.error("连接路径格式不对");
        }
        instance.setType(JDBCUtils.getDriverType(instance.getUrl().toLowerCase()));
        instance.setScheme(instance.getUserName());
        if (metaDatasourceService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple=true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions("sys:datasource:del")
    public R delete(@RequestBody String[] ids) {
        if (metaDatasourceService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }


    @RequestMapping(value = "/synDataModel",method = RequestMethod.POST)
    public R synDataModel(@RequestParam String id) {
        if (metaDatasourceService.synDataModel(id)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

}
