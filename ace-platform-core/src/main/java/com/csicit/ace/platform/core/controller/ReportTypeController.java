package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.ReportTypeService;
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
 * 报表类别 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-07 08:52:49
 */

@RestController
@RequestMapping("/reportTypes")
@Api("报表类别")
public class ReportTypeController extends BaseController {

    @Autowired
    private ReportTypeService reportTypeService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-08-07 08:52:49
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ReportTypeDO instance = reportTypeService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-07 08:52:49
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
//        int current = Integer.parseInt((String) params.get("current"));
//        int size = Integer.parseInt((String) params.get("size"));
//        Page<ReportTypeDO> page = new Page<>(current, size);
//        IPage list = reportTypeService.page(page, MapWrapper.getEqualInstance(params,"sort",true));
//        return R.ok().put("page", list);
        List<ReportTypeDO> list = reportTypeService.list(MapWrapper.getEqualInstance(params, "sort", true));
        List<ReportTypeDO> listT = TreeUtils.makeTree(list, ReportTypeDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-07 08:52:49
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ReportTypeDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody ReportTypeDO instance) {
        if (reportTypeService.count(new QueryWrapper<ReportTypeDO>().eq("name", instance.getName()).eq("app_id",
                instance.getAppId()).eq("parent_id",instance.getParentId())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
            ));
        }
        if (reportTypeService.count(new QueryWrapper<ReportTypeDO>().eq("sort", instance.getSort()).eq("app_id",
                instance.getAppId()).eq("parent_id",instance.getParentId())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("SORT_NO"), instance.getSort().toString()}
            ));
        }
        if (reportTypeService.saveReportType(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-08-07 08:52:49
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ReportTypeDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody ReportTypeDO instance) {
        ReportTypeDO old = reportTypeService.getById(instance.getId());
        if(!Objects.equals(old.getName(),instance.getName())){
            if (reportTypeService.count(new QueryWrapper<ReportTypeDO>().eq("name", instance.getName()).eq("app_id",
                    instance.getAppId()).eq("parent_id",instance.getParentId())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
                ));
            }
        }
        if(!Objects.equals(old.getSort(),instance.getSort())){
            if (reportTypeService.count(new QueryWrapper<ReportTypeDO>().eq("sort", instance.getSort()).eq("app_id",
                    instance.getAppId()).eq("parent_id",instance.getParentId())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("SORT_NO"), instance.getSort().toString()}
                ));
            }
        }
        if (reportTypeService.updateReportType(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-07 08:52:49
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (reportTypeService.removeReportType(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
