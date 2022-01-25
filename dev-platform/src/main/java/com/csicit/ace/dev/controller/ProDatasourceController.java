package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.jdbc.JDBCUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.ProDatasourceService;
import com.csicit.ace.dev.util.DBUtil;
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
 * @version V1.0
 * @date 2019-11-04 14:48:24
 */

@RestController
@RequestMapping("/proDatasources")
@Api("数据源")
public class ProDatasourceController extends BaseController {

    @Autowired
    private ProDatasourceService proDatasourceService;

    /**
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:proDatasource:view")
    public R get(@PathVariable("id") String id) {
        ProDatasourceDO instance = proDatasourceService.getById(id);
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
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("sys:proDatasource:list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<ProDatasourceDO> page = new Page<>(current, size);
        IPage list = proDatasourceService.page(page, new QueryWrapper<ProDatasourceDO>()
                .eq("service_id", params.get("service_id")));
        return R.ok().put("page", list);
    }

    @RequestMapping(value = "/query/list/{serviceId}", method = RequestMethod.GET)
    public R listUserOrgApp(@PathVariable("serviceId") String serviceId) {
        List<ProDatasourceDO> list = proDatasourceService.list(new QueryWrapper<ProDatasourceDO>()
                .eq("service_id", serviceId));
        return R.ok().put("list", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProDatasourceDO")
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions("sys:proDatasource:add")
    public R save(@RequestBody ProDatasourceDO instance) {
        List<ProDatasourceDO> dsList =
                proDatasourceService.list(new QueryWrapper<ProDatasourceDO>().eq("name", instance.getName()));
        if (dsList != null && dsList.size() > 0) {
            return R.error("已存在相同的数据源名称");
        }
        if (instance.getDriver().contains("dm")) {
            instance.setType("dm");
        } else if (instance.getDriver().contains("mysql")) {
            instance.setType("mysql");
        } else if (instance.getDriver().contains("oracle")) {
            instance.setType("oracle");
        }else if(instance.getDriver().contains("oscar")){
            instance.setType("oscar");
        }
//        instance.setType(JDBCUtils.getDriverType(instance.getUrl().toLowerCase()));
        instance.setScheme(instance.getUserName());
        // 已存在
        if (Objects.equals(0, instance.getExistFlg())) {
            if (!DBUtil.checkConnection(instance.getDriver(), instance.getUrl(), instance.getUserName(), instance
                    .getPassword())) {
                return R.error("数据库测试连接出错");
            }
            if (!instance.getUrl().toLowerCase().startsWith("jdbc:")) {
                return R.error("连接路径格式不对");
            }
        } else {
            // 需创建新数据库
            if (Objects.equals("oracle", instance.getType())) {
                instance.setUrl("jdbc:oracle:thin:@//" + instance.getDbIpAddress() + ":" + instance.getDbPort() + "/"
                        + instance.getDbName());
            }
            if (Objects.equals("dm", instance.getType())) {
                instance.setUrl("jdbc:dm://" + instance.getDbIpAddress() + ":" + instance.getDbPort() + ":"
                        + instance.getDbName());
            }
            if (Objects.equals("mysql", instance.getType())) {
                instance.setUrl("jdbc:mysql://" + instance.getDbIpAddress() + ":" + instance.getDbPort() + "/"
                        + instance.getDbName() + "?serverTimezone=UTC&characterEncoding=utf-8");
                instance.setUserName(instance.getDbUser());
                instance.setPassword(instance.getDbPwd());
            }
            if(Objects.equals("oscar",instance.getType())){
                instance.setUrl("jdbc:oscar://" + instance.getDbIpAddress() + ":" + instance.getDbPort() + "/"
                        + instance.getDbName());
            }
//            instance.setUrl();
            if (!DBUtil.createDbAndUser(instance)) {
                return R.error("连接数据库报错！");
            }
        }

        if (proDatasourceService.saveDatasource(instance)) {
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProDatasourceDO")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions("sys:proDatasource:edit")
    public R update(@RequestBody ProDatasourceDO instance) {
        ProDatasourceDO old = proDatasourceService.getById(instance.getId());
        if (!Objects.equals(old.getName(), instance.getName())) {
            List<ProDatasourceDO> dsList =
                    proDatasourceService.list(new QueryWrapper<ProDatasourceDO>().eq("name", instance.getName()));
            if (dsList != null && dsList.size() > 0) {
                return R.error("已存在相同的数据源名称");
            }
        }
        if (!DBUtil.checkConnection(instance.getDriver(), instance.getUrl(), instance.getUserName(), instance
                .getPassword())) {
            return R.error("数据库测试连接出错");
        }
        if (!instance.getUrl().toLowerCase().startsWith("jdbc:")) {
            return R.error("连接路径格式不对");
        }
        instance.setType(JDBCUtils.getDriverType(instance.getUrl().toLowerCase()));
        instance.setScheme(instance.getUserName());
        if (proDatasourceService.updateDatasouce(instance, old.getMajor())) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-11-04 14:48:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions("sys:proDatasource:del")
    public R delete(@RequestBody String[] ids) {
        if (proDatasourceService.deleteDatasouce(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
