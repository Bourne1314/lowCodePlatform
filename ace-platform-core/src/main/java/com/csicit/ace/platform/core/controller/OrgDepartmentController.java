package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.OrgDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 组织-部门 接口访问层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:38
 */

@RestController
@RequestMapping("/orgDepartments")
@Api("组织-部门管理")
public class OrgDepartmentController extends BaseController {

    @Autowired
    private OrgDepartmentService orgDepartmentService;

    /**
     * 移动部门
     *
     * @param params {mvToTop,id,organizationId,depId}
     * @return
     * @author yansiyang
     * @date 2019/4/18 19:30
     */
    @ApiOperation(value = "移动部门", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Map")
    @AceAuth("移动部门")
    @RequestMapping(value = "/action/mvDep", method = RequestMethod.POST)
    public R mvDep(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String targetDepId = params.get("depId");
        String orgId = params.get("organizationId");
        String mvToTop = params.get("mvToTop");
        if (orgDepartmentService.mvDep(Objects.equals(mvToTop, "1"), id, orgId, targetDepId)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }

        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 版本化部门
     *
     * @param params {versionName,versionNo,depId}
     * @return
     * @author yansiyang
     * @date 2019/4/18 19:30
     */
    @ApiOperation(value = "版本化部门", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgDepartmentDO")
    @AceAuth("版本化部门")
    @RequestMapping(value = "/action/version", method = RequestMethod.POST)
    public R version(@RequestBody Map<String, String> params) {
        if (orgDepartmentService.versionOrg(params)) {
            return R.ok(InternationUtils.getInternationalMsg("VERSION_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("VERSION_FAILED"));
    }

    /**
     * 获取单个部门
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个部门", httpMethod = "GET", notes = "获取单个部门")
    @AceAuth("获取单个部门")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        OrgDepartmentDO instance = orgDepartmentService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取部门列表
     *
     * @param params 请求参数map对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "获取部门列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取部门列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String search = (String) params.get("search");
        String orgId = (String) params.get("organizationId");
        boolean withSon = params.get("withSon") == null ? false : Boolean.parseBoolean((String) params.get("withSon"));
        List<OrgDepartmentDO> list = new ArrayList<>();
        // 获取指定业务单元 及其 子业务单元 下的所有部门
        if (StringUtils.isNotBlank(orgId)) {
            if (withSon) {
                List<String> orgIds = orgOrganizationService.getSonIdsOrgsByOrgId(orgId, 1, true);
                list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().and(StringUtils.isNotBlank
                        (search), i -> i.like("name", search).or().like("code", search)).eq("is_delete", 0)
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("organization_Id", orgIds).orderByAsc("sort_path"));

            } else {
                list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().and(StringUtils.isNotBlank
                        (search), i -> i.like("name", search).or().like("code", search)).eq("is_delete", 0)
                        .eq("organization_Id", orgId).orderByAsc("sort_path"));
            }
        } else {
            list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().and(StringUtils.isNotBlank
                    (search), i -> i.like("name", search).or().like("code", search)).eq("is_delete", 0)
                    .orderByAsc("sort_path"));
        }

        Map<String, String> idAndName = new HashMap<>();
        list.forEach(dep -> {
            idAndName.put(dep.getId(), dep.getName());
        });
        list.forEach(dep -> {
            // 如果 上级部门存在 且 不存在于结果中  则从数据库中查询
            if (!Objects.equals(dep.getParentId(), "0") && !idAndName.containsKey(dep.getParentId())) {
                OrgDepartmentDO tempDep = orgDepartmentService.getById(dep.getParentId());
                idAndName.put(tempDep.getId(), tempDep.getName());
            }
            dep.setParentName(idAndName.get(dep.getParentId()));
        });
        List<OrgDepartmentDO> listT = TreeUtils.makeTree(list, OrgDepartmentDO.class);
        return R.ok().put("list", listT).put("dataList", list);
    }

    /**
     * 获取部门列表
     *
     * @param params 请求参数map对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "获取部门列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取部门列表")
    @RequestMapping(value = "/action/list/forPersonnelMenu", method = RequestMethod.GET)
    public R listForPersonnelMenu(@RequestParam Map<String, Object> params) {
        String orgId = (String) params.get("organizationId");
        OrgDepartmentDO parentDepartment = new OrgDepartmentDO();
        List<OrgDepartmentDO> list = new ArrayList<>();
        if (StringUtils.isNotBlank(orgId)) {
            list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0)
                    .eq("organization_Id", orgId).orderByAsc("sort_path"));
        } else {
            list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0)
                    .orderByAsc("sort_path"));
        }
        Map<String, String> idAndName = new HashMap<>();
        list.forEach(dep -> {
            idAndName.put(dep.getId(), dep.getName());
        });
        list.forEach(dep -> {
            // 如果 上级部门存在 且 不存在于结果中  则从数据库中查询
            if (!Objects.equals(dep.getParentId(), "0") && !idAndName.containsKey(dep.getParentId())) {
                OrgDepartmentDO tempDep = orgDepartmentService.getById(dep.getParentId());
                idAndName.put(tempDep.getId(), tempDep.getName());
            }
            dep.setParentName(idAndName.get(dep.getParentId()));
        });
        List<OrgDepartmentDO> listT = TreeUtils.makeTree(list, OrgDepartmentDO.class);
        if (CollectionUtils.isNotEmpty(listT)) {
            parentDepartment.setId("allParentId");
            parentDepartment.setChildren(listT);
            parentDepartment.setName("所有部门");
            parentDepartment.setGroupId(listT.get(0).getGroupId());
            parentDepartment.setOrganizationId(listT.get(0).getOrganizationId());
        }
        return R.ok().put("parentDepartment", parentDepartment);
    }

    /**
     * 根据业务单元ID获取所有部门列表（不判断管理员权限）
     *
     * @return 集团集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "根据业务单元ID获取所有部门列表", httpMethod = "GET", notes = "根据业务单元ID获取所有部门列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("根据业务单元ID获取所有部门列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        String orgId = (String) params.get("organizationId");
        List<OrgDepartmentDO> list = new ArrayList<>();
        if (StringUtils.isNotBlank(orgId)) {
            list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0)
                    .eq("organization_Id", orgId).orderByAsc("sort_path"));
        }
        Map<String, String> idAndName = new HashMap<>();
        list.forEach(dep -> {
            idAndName.put(dep.getId(), dep.getName());
        });
        list.forEach(dep -> {
            // 如果 上级部门存在 且 不存在于结果中  则从数据库中查询
            if (!Objects.equals(dep.getParentId(), "0") && !idAndName.containsKey(dep.getParentId())) {
                OrgDepartmentDO tempDep = orgDepartmentService.getById(dep.getParentId());
                idAndName.put(tempDep.getId(), tempDep.getName());
            }
            dep.setParentName(idAndName.get(dep.getParentId()));
        });
        List<OrgDepartmentDO> listT = TreeUtils.makeTree(list, OrgDepartmentDO.class);
        return R.ok().put("list", listT);

    }


    /**
     * 保存部门
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "保存部门", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgDepartmentDO")
    @AceAuth("保存部门")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody OrgDepartmentDO instance) {
        if (orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0).eq("code", instance.getCode())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("CODE"), instance.getCode()}
            ));
        }
        if (orgDepartmentService.saveDep(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改部门
     *
     * @param instance 对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "修改部门", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgDepartmentDO")
    @AceAuth("修改部门")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody OrgDepartmentDO instance) {
        OrgDepartmentDO old=orgDepartmentService.getById(instance.getId());
        if(!Objects.equals(old.getCode(),instance.getCode())){
            if (orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0).eq("code", instance.getCode())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("CODE"), instance.getCode()}
                ));
            }
        }
        if (orgDepartmentService.updateDep(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除部门
     *
     * @param params ID数组
     * @return com.csicit.ace.common.utils.server.R
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "删除部门", httpMethod = "DELETE")
    @ApiImplicitParam(name = "params", value = "ID数组", required = true, dataType = "Map")
    @AceAuth("删除部门")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, Object> params) {
        return orgDepartmentService.deleteDep(params);
    }


}
