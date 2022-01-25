package com.csicit.ace.platform.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BdPostService;
import com.csicit.ace.platform.core.service.SysDictValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门岗位表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-14 08:00:27
 */

@RestController
@RequestMapping("/bdPosts")
@Api("部门岗位表")
public class BdPostController extends BaseController {

    @Autowired
    private BdPostService bdPostService;

    @Autowired
    SysDictValueService sysDictValueService;

    /**
     * 获取单个部门岗位信息
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/11/7 19:35
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个部门岗位信息", httpMethod = "GET", notes = "获取单个部门岗位信息")
    //@AceAuth("获取单个部门岗位信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BdPostDO instance = bdPostService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 通过集团ID获取岗位类别
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/11/7 19:35
     */
    @ApiImplicitParam(name = "groupId", value = "groupId", dataType = "String", required = true)
    @ApiOperation(value = "通过集团ID获取岗位类别", httpMethod = "GET", notes = "通过集团ID获取岗位类别")
    @RequestMapping(value = "/action/getPostType/{groupId}", method = RequestMethod.GET)
    public R getPostType(@PathVariable("groupId") String groupId) {
        if (StringUtils.isNotBlank(groupId)) {
            List<SysDictValueDO> valueDOS = sysDictValueService.getDictValueByGroupId("POST_TYPE", groupId);
//                    sysDictValueService.list(new QueryWrapper<SysDictValueDO>().eq
//                    ("group_id", groupId).eq("type", "POST_TYPE"));
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(valueDOS)) {
                return R.ok().put("list", valueDOS);
            }
        }
        return R.error();
    }


    /**
     * 获取基础应用产品库列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取部门岗位信息列表", httpMethod = "GET", notes = "获取部门岗位信息列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    //@AceAuth("获取部门岗位信息列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        String depId = (String) params.get("depId");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr) || StringUtils.isBlank(depId)) {
            if (StringUtils.isNotBlank(depId)) {
                List<BdPostDO> list = bdPostService.list(new QueryWrapper<BdPostDO>().eq("DEPARTMENT_ID", depId)
                        .orderByAsc("sort_index"));
                return R.ok().put("list", list);
            } else {
                List<BdPostDO> list = bdPostService.list(new QueryWrapper<BdPostDO>().orderByAsc("sort_index"));
                return R.ok().put("list", list);
            }
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<BdPostDO> page = new Page<>(current, size);
        IPage list = bdPostService.page(page, new QueryWrapper<BdPostDO>().eq("DEPARTMENT_ID", depId).orderByAsc
                ("sort_index"));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(list.getRecords())) {
            List<BdPostDO> posts = list.getRecords();
            List<String> dictIds = posts.stream().map(BdPostDO::getTypeId).collect(Collectors.toList());
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(dictIds)) {
                List<SysDictValueDO> dictValueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>().in
                        ("id", dictIds));
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(dictValueDOS)) {
                    Map<String, String> typeIdAndName = new HashMap<>();
                    dictValueDOS.forEach(dictValueDO -> {
                        typeIdAndName.put(dictValueDO.getId(), dictValueDO.getDictName());
                    });
                    posts.forEach(post -> {
                        post.setTypeName(typeIdAndName.get(post.getTypeId()));
                    });
                    list.setRecords(posts);
                }
            }
        }
        return R.ok().put("page", list);
    }

    /**
     * 新增部门岗位
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "保存部门岗位", httpMethod = "POST", notes = "保存部门岗位")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BdPostDO")
    //@AceAuth("保存基础应用产品库")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BdPostDO instance) {
        if (bdPostService.count(new QueryWrapper<BdPostDO>()
                .eq("name", instance.getName()).eq("DEPARTMENT_ID",instance.getDepartmentId())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
            ));
        }
        if (bdPostService.savePost(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改部门岗位
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "修改部门岗位", httpMethod = "PUT", notes = "修改部门岗位")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BdPostDO")
    //@AceAuth("修改部门岗位")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BdPostDO instance) {
        BdPostDO old = bdPostService.getById(instance);
        if (!Objects.equals(old.getName(), instance.getName())) {
            if (bdPostService.count(new QueryWrapper<BdPostDO>()
                    .eq("name", instance.getName()).eq("DEPARTMENT_ID",instance.getDepartmentId())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
                ));
            }
        }
        if (bdPostService.updatePost(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除部门岗位
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "删除部门岗位", httpMethod = "DELETE", notes = "删除部门岗位")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    //@AceAuth("删除基础应用产品库")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (bdPostService.deletePost(ids)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
