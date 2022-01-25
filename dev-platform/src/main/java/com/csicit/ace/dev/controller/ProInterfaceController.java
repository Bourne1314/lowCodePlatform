package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.dev.ProInputParamsDO;
import com.csicit.ace.common.pojo.domain.dev.ProInterfaceDO;
import com.csicit.ace.common.pojo.domain.dev.ProOutputParamsDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.ProDatasourceService;
import com.csicit.ace.dev.service.ProInputParamsService;
import com.csicit.ace.dev.service.ProInterfaceService;
import com.csicit.ace.dev.service.ProOutputParamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
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
@RequestMapping("/proInterfaces")
@Api("接口")
public class ProInterfaceController extends BaseController {

    @Autowired
    private ProInterfaceService proInterfaceService;

    @Autowired
    private ProInputParamsService proInputParamsService;

    @Autowired
    private ProOutputParamsService proOutputParamsService;

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
    @RequiresPermissions("sys:interface:view")
    public R get(@PathVariable("id") String id) {
        ProInterfaceDO instance = proInterfaceService.getById(id);
        if (instance != null) {
            instance.setInputParams(proInputParamsService.list(new QueryWrapper<ProInputParamsDO>()
                    .eq("interface_id", instance.getId())));
            instance.setOutPutParams(proOutputParamsService.list(new QueryWrapper<ProOutputParamsDO>()
                    .eq("interface_id", instance.getId())));
            if (StringUtils.isNotBlank(instance.getDsId())) {
                instance.setDsName(proDatasourceService.getById(instance.getDsId()).getName());
            }
        }
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
        String categoryId = params.get("categoryId").toString();
        List<ProInterfaceDO> list = proInterfaceService.list(new QueryWrapper<ProInterfaceDO>()
                .eq("category_id", categoryId).orderByDesc("create_time"));
        list.stream().forEach(item -> {
            item.setInputParams(proInputParamsService.list(new QueryWrapper<ProInputParamsDO>()
                    .eq("interface_id", item.getId())));
            item.setOutPutParams(proOutputParamsService.list(new QueryWrapper<ProOutputParamsDO>()
                    .eq("interface_id", item.getId())));
        });
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
    @RequiresPermissions("sys:interface:list")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String searchStr = (String) params.get("searching");
        String categoryId = (String) params.get("categoryId");
        Page<ProInterfaceDO> page = new Page<>(current, size);
        IPage list = proInterfaceService.page(page, new QueryWrapper<ProInterfaceDO>()
                .and(StringUtils.isNotBlank(searchStr)
                        , i -> i.like("name", searchStr))
                .eq("category_id", categoryId).orderByDesc("create_time"));
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProInterfaceDO")
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions("sys:interface:add")
    public R save(@RequestBody ProInterfaceDO instance) {

        int count = proInterfaceService.count(new QueryWrapper<ProInterfaceDO>()
                .eq("category_id", instance.getCategoryId()).eq("name", instance.getName()));
        if (count > 0) {
            return R.error("在同一类别下，已存在相同接口名称");
        }
        if (proInterfaceService.count(new QueryWrapper<ProInterfaceDO>().eq("code", instance.getCode())) > 0) {
            return R.error("在该服务下，已存在相同接口标识");
        }
        if (proInterfaceService.addInterface(instance)) {
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
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProInterfaceDO")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions("sys:interface:edit")
    public R update(@RequestBody ProInterfaceDO instance) {
        ProInterfaceDO oldModel = proInterfaceService.getById(instance.getId());
        if (!Objects.equals(instance.getName(), oldModel
                .getName())) {
            int count = proInterfaceService.count(new QueryWrapper<ProInterfaceDO>()
                    .eq("category_id", instance.getCategoryId()).eq("name", instance.getName()));
            if (count > 0) {
                return R.error("在同一类别下，已存在相同接口名称");
            }
        }
        if (!Objects.equals(instance.getCode(), oldModel.getCode())) {
            if (proInterfaceService.count(new QueryWrapper<ProInterfaceDO>().eq("code", instance.getCode())) > 0) {
                return R.error("在该服务下，已存在相同接口标识");
            }
        }

        if (proInterfaceService.updInterface(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }

        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }


    /**
     * @param ids
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions("sys:interface:del")
    public R delete(@RequestBody String[] ids) {
        if (proInterfaceService.delInterface(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * sql代码测试检查
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ProInterfaceDO")
    @RequestMapping(value = "/action/sqlCode/check", method = RequestMethod.POST)
    public R sqlCodeCheck(@RequestBody ProInterfaceDO instance) {
        return proInterfaceService.sqlCodeCheck(instance);
    }

    /**
     * @param sqlCode
     * @param httpRequest
     * @return
     * @author zuogang
     * @date 2020/6/15 10:20
     */
    @RequestMapping(value = "/commonResult/{sqlCode}", method = RequestMethod.GET)
    public R getSqlResult(@PathVariable("sqlCode") String sqlCode, HttpServletRequest httpRequest) throws
            UnsupportedEncodingException {
        String queryString = httpRequest.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            queryString = URLDecoder.decode(queryString, "utf-8");
        }

        return proInterfaceService.getSqlResult(sqlCode, queryString);
    }
}
