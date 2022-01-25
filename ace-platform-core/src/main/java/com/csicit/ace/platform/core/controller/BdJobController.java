package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BdJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 职务称谓表 接口访问层
 * @author wangzimin
 * @version V1.0
 * @date 2019/9/28 9:36
 */

@RestController
@RequestMapping("/bdJobs")
@Api("职务称谓表")
public class BdJobController extends BaseController{
    @Autowired
    private BdJobService bdJobService;

    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个职务称谓信息", httpMethod = "GET", notes = "获取单个职务称谓信息")
    //@AceAuth("获取单个部门岗位信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BdJobDO instance = bdJobService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取职务称谓库列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取职务称谓信息列表", httpMethod = "GET", notes = "获取职务称谓信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    //@AceAuth("获取部门岗位信息列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        String groupId = (String) params.get("groupId");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr) || StringUtils.isBlank(groupId)) {
            if (StringUtils.isNotBlank(groupId)) {
                List<BdJobDO> list = bdJobService.list(new QueryWrapper<BdJobDO>().eq("GROUP_ID", groupId).orderByAsc("SORT_INDEX"));
                return R.ok().put("list", list);
            } else {
                List<BdJobDO> list = bdJobService.list(new QueryWrapper<BdJobDO>().orderByAsc("sort_index"));
                return R.ok().put("list", list);
            }
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<BdJobDO> page = new Page<>(current, size);
        IPage list = bdJobService.page(page, new QueryWrapper<BdJobDO>().eq("GROUP_ID", groupId).orderByAsc("sort_index"));
        return R.ok().put("page", list);
    }

    /**
     * 新增职务称谓
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "保存职务称谓", httpMethod = "POST", notes = "保存职务称谓")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BdJobDO")
    //@AceAuth("保存基础应用产品库")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BdJobDO instance) {
        if (bdJobService.count(new QueryWrapper<BdJobDO>()
                .eq("name", instance.getName()).eq("group_id",instance.getGroupId())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
            ));
        }
        if (bdJobService.saveJob(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改职务称谓
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "修改职务称谓", httpMethod = "PUT", notes = "修改职务称谓")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BdJobDO")
    //@AceAuth("修改部门岗位")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BdJobDO instance) {
        BdJobDO old = bdJobService.getById(instance);
        if (!Objects.equals(old.getName(), instance.getName())) {
            if (bdJobService.count(new QueryWrapper<BdJobDO>()
                    .eq("name", instance.getName()).eq("group_id",instance.getGroupId())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
                ));
            }
        }
        if (bdJobService.updateJob(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除职务称谓
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "删除职务称谓", httpMethod = "DELETE", notes = "删除职务称谓")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    //@AceAuth("删除基础应用产品库")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (bdJobService.deleteJob(ids)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
