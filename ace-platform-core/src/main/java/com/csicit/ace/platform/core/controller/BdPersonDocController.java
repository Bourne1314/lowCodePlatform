package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BdPersonDocService;
import com.csicit.ace.platform.core.service.BdPersonIdTypeService;
import com.csicit.ace.platform.core.service.BdPersonJobService;
import com.csicit.ace.platform.core.service.SysUserAdminOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 人员档案接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/bdPersonDocs")
@Api("个人档案维护管理")
public class BdPersonDocController extends BaseController {
    @Autowired
    BdPersonDocService bdPersonDocService;

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    BdPersonIdTypeService bdPersonIdTypeService;

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;

    /**
     * 获取单个个人档案
     *
     * @param id 个人档案id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个个人档案", httpMethod = "GET", notes = "获取单个个人档案")
    @AceAuth("获取单个个人档案")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BdPersonDocDO instance = bdPersonDocService.getById(id);
        // 所在集团 业务单元名称
        Set<String> orgIds = new HashSet<>();
        orgIds.add(instance.getGroupId());
        orgIds.add(instance.getOrganizationId());
        List<OrgOrganizationDO> orgList = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                ("is_delete", 0)
                .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", orgIds).select
                        ("id", "name"));
        Map<String, String> orgMap = new HashMap<>();
        orgList.parallelStream().forEach(org -> {
            orgMap.put(org.getId(), org.getName());
        });
        instance.setGroupName(orgMap.get(instance.getGroupId()));
        instance.setOrganizationName(orgMap.get(instance.getOrganizationId()));
        // 所拥有的职能
        List<BdPersonJobDO> jobList = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
                .eq("person_doc_id", id));
        instance.setJobList(jobList);
        if (StringUtils.isNotBlank(instance.getPersonIdTypeId())) {
            BdPersonIdTypeDO bdPersonIdTypeDO = bdPersonIdTypeService.getById(instance.getPersonIdTypeId());
            if (bdPersonIdTypeDO != null) {
                instance.setPersonIdTypeName(bdPersonIdTypeDO.getName());
            }
        }
        return R.ok().put("instance", instance);
    }

    /**
     * 根据部门主键获人档案列表并分页
     *
     * @param params 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 个人档案列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "根据部门主键获人档案列表并分页", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("根据部门主键获人档案列表并分页")
    @RequestMapping(value = "/action/listByDepId", method = RequestMethod.GET)
    public R listByDepId(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        // 检索条件
        String searchStr = (String) params.get("searchString");
        String depId = (String) params.get("depId");
        String orgId = (String) params.get("orgId");
        Page<BdPersonDocDO> page = new Page<>(current, size);
        IPage list = null;

        if (StringUtils.isNotBlank(depId) && StringUtils.isNotBlank(orgId)) {
            if (Objects.equals(depId, "allParentId")) {
                // 获取该业务单元下所有人员信息
                list = bdPersonDocService.page(page, new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0)
                        .orderByAsc
                                ("sort_index")
                        .and(a -> a.eq("organization_id", orgId) // 在当前组织下的人员
                                .or()
                                .inSql("id", "select PERSON_DOC_ID from BD_PERSON_JOB where " +
                                        "organization_id =" +
                                        " '" + orgId
                                        + "'")  // 在当前组织下有职务关系的人员
                        ).and(StringUtils.isNotBlank
                                        (searchStr)
                                , i -> i.like("name", searchStr)
                                        .or().like("code", searchStr)
                                        .or().like("birth_Date", searchStr)
                                        .or().like("join_Work_Date", searchStr)
                                        .or().like("used_name", searchStr)
                                        .or().like("cell_phone", searchStr)));
            } else {
                // 获取该部门的人员信息(不包含下级部门)
                list = bdPersonDocService.page(page, new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0)
                        .orderByAsc
                                ("sort_index")
                                .inSql("id", "select PERSON_DOC_ID from BD_PERSON_JOB where department_id =" +
                                        " '" + depId
                                        + "'")  // 在当前部门下有职务关系的人员
                        .and(StringUtils.isNotBlank
                                        (searchStr)
                                , i -> i.like("name", searchStr)
                                        .or().like("code", searchStr)
                                        .or().like("birth_Date", searchStr)
                                        .or().like("join_Work_Date", searchStr)
                                        .or().like("used_name", searchStr)
                                        .or().like("cell_phone", searchStr)));
            }
        }
        return R.ok().put("page", list);
    }

    /**
     * 获取个人档案列表并分页
     *
     * @param params 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 个人档案列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取个人档案列表并分页", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取个人档案列表并分页")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        // 检索条件
        String searchStr = (String) params.get("searchString");
        String orgId = (String) params.get("organizationId");
        String depId = (String) params.get("depId");
        String groupId = (String) params.get("groupId");
        Page<BdPersonDocDO> page = new Page<>(current, size);
        IPage list = null;
        if (StringUtils.isNotBlank(depId)) {
            list = bdPersonDocService.page(page, new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0).orderByAsc
                    ("sort_index")
                    .inSql("id", "select person_doc_id " +
                            "from bd_person_job where department_id='" + depId + "'")
                    .and(StringUtils.isNotBlank
                                    (searchStr)
                            , i -> i.like("name", searchStr)
                                    .or().like("pin_yin", searchStr)
                                    .or().like("used_name", searchStr)
                                    .or().like("cell_phone", searchStr)
                                    .or().like("code", searchStr)));
        } else if (StringUtils.isNotBlank(orgId)) {
            // 递归获取 子业务单元
            List<String> orgIds = orgOrganizationService.getSonIdsOrgsByOrgId(orgId, 1, true);
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(orgIds)) {
                list = bdPersonDocService.page(page, new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0).orderByAsc
                        ("sort_index")
                        .in("ORGANIZATION_ID", orgIds).and(StringUtils.isNotBlank
                                        (searchStr)
                                , i -> i.like("name", searchStr)
                                        .or().like("pin_yin", searchStr)
                                        .or().like("used_name", searchStr)
                                        .or().like("cell_phone", searchStr)
                                        .or().like("code", searchStr)));
            }

        } else if (StringUtils.isNotBlank(groupId)) {
            List<String> authOrgIds = sysAuthScopeOrgService.getOrgIdsByUserId(getCurrentUserId());
            list = bdPersonDocService.page(page, new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0)
                    .eq("group_id", groupId).orderByAsc("sort_index")
                    .and(authOrgIds == null || authOrgIds.size() == 0, i -> i.eq("1", "2"))
                    .in("ORGANIZATION_ID", authOrgIds)
                    .and(StringUtils.isNotBlank
                                    (searchStr)
                            , i -> i.like("name", searchStr)
                                    .or().like("pin_yin", searchStr)
                                    .or().like("used_name", searchStr)
                                    .or().like("cell_phone", searchStr)
                                    .or().like("code", searchStr)));
        } else {
            List<String> authOrgIds = sysAuthScopeOrgService.getOrgIdsByUserId(getCurrentUserId());
            list = bdPersonDocService.page(page, new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0).orderByAsc
                    ("sort_index")
                    .and(authOrgIds == null || authOrgIds.size() == 0, i -> i.eq("1", "2"))
                    .in("ORGANIZATION_ID", authOrgIds)
                    .and(StringUtils
                                    .isNotBlank
                                            (searchStr)
                            , i -> i.like("name", searchStr)
                                    .or().like("pin_yin", searchStr)
                                    .or().like("used_name", searchStr)
                                    .or().like("cell_phone", searchStr)
                                    .or().like("code", searchStr)));
        }

        if (list != null) {
            List<BdPersonDocDO> persons = list.getRecords();
            if (persons != null && persons.size() > 0) {
                // 所在集团 业务单元名称
                Set<String> groupIds = persons.stream().map(BdPersonDocDO::getGroupId).collect(Collectors.toSet
                        ());

                Set<String> orgIds = persons.stream().map(BdPersonDocDO::getOrganizationId).collect(Collectors.toSet());

                orgIds.addAll(groupIds);
                List<OrgOrganizationDO> orgList = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                        .eq("is_delete", 0)
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("id", orgIds).select("id", "name"));
                Map<String, String> orgMap = new HashMap<>();
                orgList.parallelStream().forEach(org -> {
                    orgMap.put(org.getId(), org.getName());
                });

                persons.stream().forEach(person -> {
                    person.setGroupName(orgMap.get(person.getGroupId()));
                    person.setOrganizationName(orgMap.get(person.getOrganizationId()));
                });
                list.setRecords(persons);
            }
        }
        return R.ok().put("page", list);
    }

    /**
     * 保存个人档案
     *
     * @param personDoc 个人档案对象
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存个人档案", httpMethod = "POST")
    @ApiImplicitParam(name = "personDoc", value = "个人档案实体", required = true, dataType = "BdPersonDocDO")
    @AceAuth("保存个人档案")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody BdPersonDocDO personDoc) {
//        if (bdPersonDocService.count(new QueryWrapper<BdPersonDocDO>().eq("code", personDoc.getCode())) > 0) {
//            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
//                    new String[]{InternationUtils.getInternationalMsg("CODE"), personDoc.getCode()}
//            ));
//        }
//        if (bdPersonDocService.save(personDoc)) {
//            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        return bdPersonDocService.insert(personDoc);
    }

    /**
     * 修改个人档案
     *
     * @param personDoc 个人档案对象
     * @return com.csicit.ace.common.utils.server.R 更新结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改个人档案", httpMethod = "PUT")
    @ApiImplicitParam(name = "personDoc", value = "个人档案实体", required = true, dataType = "BdPersonDocDO")
    @AceAuth("修改个人档案")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody BdPersonDocDO personDoc) {
//        if (bdPersonDocService.count(new QueryWrapper<BdPersonDocDO>()
//                .ne("id", personDoc.getId())
//                .eq("code", personDoc.getCode())) > 0) {
//            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
//                    new String[]{InternationUtils.getInternationalMsg("CODE"), personDoc.getCode()}
//            ));
//        }
//        if (bdPersonDocService.updateById(personDoc)) {
//            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        return bdPersonDocService.update(personDoc);

    }

    /**
     * 删除个人档案
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "删除个人档案", httpMethod = "DELETE")
    @ApiImplicitParam(name = "personDocIds", value = "个人档案ID数组", required = true, allowMultiple = true, dataType =
            "String")
    @AceAuth("删除个人档案")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String,Object> map) {
        return bdPersonDocService.delete(map);
    }


}
