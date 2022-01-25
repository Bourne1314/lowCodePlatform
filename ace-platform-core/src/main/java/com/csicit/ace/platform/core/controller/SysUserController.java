package com.csicit.ace.platform.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysUserThirdPartyService;
import com.csicit.ace.platform.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户管理 接口访问层
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@RequestMapping("/sysUsers")
@RestController
@Api("用户管理")
public class SysUserController extends BaseController {

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;

    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    SysUserThirdPartyService sysUserThirdPartyService;

    @Autowired
    SysPasswordPolicyService sysPasswordPolicyService;


    /**
     * 查询指定用户第三方账号信息列表
     *
     * @param userId 用户主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "查询指定用户第三方账号信息列表", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "path")
    @AceAuth("查询指定用户第三方账号信息列表")
    @RequestMapping(value = "/action/userThirdPartyList/{userId}", method = RequestMethod.GET)
    public R getUserThirdPartyList(@PathVariable("userId") String userId) {
        if (StringUtils.isNotBlank(userId)) {
            List<SysUserThirdPartyDO> list = sysUserThirdPartyService.list(new QueryWrapper<SysUserThirdPartyDO>().eq
                    ("user_id", userId).orderByAsc("update_time"));
            return R.ok().put("list", list);
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "userId"));

    }

    /**
     * 获取指定用户第三方账号信息
     *
     * @param id 主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "获取指定用户第三方账号信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "path")
    @AceAuth("获取指定用户第三方账号信息")
    @RequestMapping(value = "/action/userThirdParty/{id}", method = RequestMethod.GET)
    public R getUserThirdParty(@PathVariable("id") String id) {
        if (StringUtils.isNotBlank(id)) {
            SysUserThirdPartyDO sysUserThirdPartyDO = sysUserThirdPartyService.getById(id);
            return R.ok().put("instance", sysUserThirdPartyDO);
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "id"));
    }

    /**
     * 删除指定用户第三方账号信息
     *
     * @param id 主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "删除指定用户第三方账号信息", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "path")
    @AceAuth("删除指定用户第三方账号信息")
    @RequestMapping(value = "/action/userThirdParty/{id}", method = RequestMethod.DELETE)
    public R deleteUserThirdParty(@PathVariable("id") String id) {
        if (StringUtils.isNotBlank(id)) {
            if (sysUserThirdPartyService.removeById(id)) {
                return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
            }
            return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "id"));
    }


    /**
     * 保存指定用户第三方账号信息
     *
     * @param sysUserThirdPartyDO 对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "保存指定用户第三方账号信息", httpMethod = "POST")
    @ApiImplicitParam(name = "sysUserThirdPartyDO", value = "sysUserThirdPartyDO", required = true, dataType =
            "SysUserThirdPartyDO", paramType = "path")
    @AceAuth("保存指定用户第三方账号信息")
    @RequestMapping(value = "/action/userThirdParty", method = RequestMethod.POST)
    public R saveUserThirdParty(@RequestBody SysUserThirdPartyDO sysUserThirdPartyDO) {
        if (StringUtils.isBlank(sysUserThirdPartyDO.getAccount())) {
            return R.error(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        if (Objects.equals(sysUserThirdPartyDO.getType(), "ace")) {
            String account = sysUserThirdPartyDO.getAccount();
            int count = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("user_name", account));
            if (count == 0) {
                return R.error("平台不存在此用户：" + account + "，请检查用户名正确性！");
            }
        } else if (sysUserThirdPartyService.count(new QueryWrapper<SysUserThirdPartyDO>()
                .eq("type", sysUserThirdPartyDO.getType()).eq("account", sysUserThirdPartyDO.getAccount())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("ACCOUNT"), sysUserThirdPartyDO.getAccount()}
            ));
        }
        sysUserThirdPartyDO.setId(UuidUtils.createUUID());
        sysUserThirdPartyDO.setCreateUser(securityUtils.getCurrentUserId());
        sysUserThirdPartyDO.setCreateTime(LocalDateTime.now());
        sysUserThirdPartyDO.setUpdateTime(LocalDateTime.now());
        if (sysUserThirdPartyService.save(sysUserThirdPartyDO)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }


    /**
     * 更新指定用户第三方账号信息
     *
     * @param sysUserThirdPartyDO 对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "更新指定用户第三方账号信息", httpMethod = "PUT")
    @ApiImplicitParam(name = "sysUserThirdPartyDO", value = "sysUserThirdPartyDO", required = true, dataType =
            "SysUserThirdPartyDO")
    @AceAuth("更新指定用户第三方账号信息")
    @RequestMapping(value = "/action/userThirdParty", method = RequestMethod.PUT)
    public R updateUserThirdParty(@RequestBody SysUserThirdPartyDO sysUserThirdPartyDO) {
        SysUserThirdPartyDO old = sysUserThirdPartyService.getById(sysUserThirdPartyDO.getId());
        if (!Objects.equals(old.getAccount(), sysUserThirdPartyDO.getAccount())) {
            if (sysUserThirdPartyService.count(new QueryWrapper<SysUserThirdPartyDO>()
                    .eq("type", sysUserThirdPartyDO.getType()).eq("account", sysUserThirdPartyDO.getAccount())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("ACCOUNT"), sysUserThirdPartyDO.getAccount()}
                ));
            }
        }
        sysUserThirdPartyDO.setUpdateTime(LocalDateTime.now());
        if (sysUserThirdPartyService.updateById(sysUserThirdPartyDO)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 获取用户列表并分页
     *
     * @param params 分页参数及查询参数
     * @return R
     * @author yansiyang
     * @date 2019/4/12 8:36
     */
    @ApiOperation(value = "获取所有用户", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取所有用户")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {

//        List<SysUserDO> all = sysUserService.list(new QueryWrapper<SysUserDO>().eq("user_type", 3));
//        all.forEach(user -> {
//            OrgOrganizationDO org  = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>().eq
//                    ("group_id", user.getGroupId()).eq("is_delete", 0).eq("is_business_unit", 1).orderByAsc
//                    ("sort_path"));
//            if (org != null) {
//                user.setOrganizationId(org.getId());
//            }
//        });
//        sysUserService.updateBatchById(all);

        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        /**
         * 注意 管理员不可见
         */
        // 获取当前集团ID
        String groupId = (String) params.get("groupId");
        // 检索条件
        String searchStr = (String) params.get("searchString");
        Page<SysUserDO> page = new Page<>(current, size);
        /**
         * 用户管理左侧 组织/角色
         */
        String roleId = (String) params.get("roleId");
        String depId = (String) params.get("depId");
        String orgId = (String) params.get("organizationId");
        boolean checkFromGroup = Boolean.valueOf((String) params.get("checkFromGroup"));
        IPage list = null;
        if (StringUtils.isNotBlank(depId)) {
            list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .eq("user_type", 3)
                    .inSql("person_doc_id", "select person_doc_id " +
                            "from bd_person_job where department_id='" + depId + "'")
                    .and(StringUtils.isNotBlank(searchStr)
                            , i -> i.like("user_name", searchStr)
                                    .or().like("pinyin", searchStr)
                                    .or().like("real_name", searchStr)
                                    .or().like("email", searchStr)
                                    .or().like("phone_number", searchStr)
                                    .or().like("staff_no", searchStr))
                    .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                            ("sort_index"));
        } else if (checkFromGroup && StringUtils.isNotBlank(searchStr)) {
            if (StringUtils.isNotBlank(groupId)) {
                list = sysUserService.page(page, new QueryWrapper<SysUserDO>()
                        .eq("is_delete", 0)
                        .eq("group_id", groupId)
                        .and(StringUtils.isNotBlank(searchStr)
                                , i -> i.like("user_name", searchStr)
                                        .or().like("pinyin", searchStr)
                                        .or().like("real_name", searchStr)
                                        .or().like("email", searchStr)
                                        .or().like("phone_number", searchStr)
                                        .or().like("staff_no", searchStr))
                        .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                ("sort_index"));
            }
        } else if (StringUtils.isNotBlank(orgId)) {
            // 递归获取子业务单元
            List<String> orgIds = orgOrganizationService.getSonIdsOrgsByOrgId(orgId, 1, true);
            if (StringUtils.isNotBlank(groupId)) {
                list = sysUserService.page(page, new QueryWrapper<SysUserDO>()
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("organization_id", orgIds).eq
                                ("is_delete", 0)
                        .eq("group_id", groupId)
                        .and(StringUtils.isNotBlank(searchStr)
                                , i -> i.like("user_name", searchStr)
                                        .or().like("pinyin", searchStr)
                                        .or().like("real_name", searchStr)
                                        .or().like("email", searchStr)
                                        .or().like("phone_number", searchStr)
                                        .or().like("staff_no", searchStr))
                        .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                ("sort_index"));
            } else {
                list = sysUserService.page(page, new QueryWrapper<SysUserDO>()
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("organization_id", orgIds).eq
                                ("is_delete", 0)
                        .and(StringUtils.isNotBlank(searchStr)
                                , i -> i.like("user_name", searchStr)
                                        .or().like("pinyin", searchStr)
                                        .or().like("real_name", searchStr)
                                        .or().like("email", searchStr)
                                        .or().like("phone_number", searchStr)
                                        .or().like("staff_no", searchStr))
                        .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                ("sort_index"));
            }

        } else if (StringUtils.isBlank(roleId)) {
            String userId = securityUtils.getCurrentUserId();
            // 集团系统管理员 安全保密员  集团超级管理员  单一业务管理员
            // 应用管理员 获得其业务单元管控域
            List<String> orgIds = new ArrayList<>(16);
            sysAuthScopeOrgService.getOrgIdsByUserId(userId).stream().forEach(orgId1 -> {
                OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById(orgId1);
                if (orgOrganizationDO != null && Objects.equals(0, orgOrganizationDO.getBeDeleted())) {
                    if (!orgIds.contains(orgId1)) {
                        orgIds.add(orgId1);
                    }
                }
            });
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgIds)) {
                // 用户没有业务单元ID  通过业务单元ID获取用户需要 通过人员表获取
                if (StringUtils.isBlank(groupId)) {
                    list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                            .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                            .in("organization_id", orgIds).and(StringUtils
                                            .isNotBlank
                                                    (searchStr)
                                    , i -> i.like("user_name", searchStr)
                                            .or().like("pinyin", searchStr)
                                            .or().like("real_name", searchStr)
                                            .or().like("email", searchStr)
                                            .or().like("phone_number", searchStr)
                                            .or().like("staff_no", searchStr))
                            .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                    ("sort_index"));
                } else {
                    list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                            .eq("user_type", 3)
                            .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                            .in("organization_id", orgIds)
                            .eq("group_id", groupId)
                            .and(StringUtils.isNotBlank(searchStr)
                                    , i -> i.like("user_name", searchStr)
                                            .or().like("pinyin", searchStr)
                                            .or().like("real_name", searchStr)
                                            .or().like("email", searchStr)
                                            .or().like("phone_number", searchStr)
                                            .or().like("staff_no", searchStr))
                            .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                    ("sort_index"));
                }
            }
        } else if (StringUtils.isNotBlank(roleId)) {
            /**
             * 获取所有拥有此角色的用户ID
             */
            // 应用管理员 获得其业务单元管控域
            List<String> orgIds = new ArrayList<>(16);
            sysAuthScopeOrgService.getOrgIdsByUserId(securityUtils.getCurrentUserId()).stream().forEach(orgId1 -> {
                OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById(orgId1);
                if (orgOrganizationDO != null && Objects.equals(0, orgOrganizationDO.getBeDeleted())) {
                    if (!orgIds.contains(orgId1)) {
                        orgIds.add(orgId1);
                    }
                }
            });
            if (StringUtils.isBlank(groupId)) {
                list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                        .eq("user_type", 3)
                        .inSql("id", "select user_id from sys_user_role_lv where is_latest=1 and id in (select lv_id " +
                                "from" +
                                " sys_user_role_v  where role_id='" + roleId + "')")
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("organization_id", orgIds)
                        .and(StringUtils.isNotBlank(searchStr)
                                , i -> i.like("user_name", searchStr)
                                        .or().like("pinyin", searchStr)
                                        .or().like("real_name", searchStr)
                                        .or().like("email", searchStr)
                                        .or().like("phone_number", searchStr)
                                        .or().like("staff_no", searchStr))
                        .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                ("sort_index"));

            } else {
                list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                        .eq("user_type", 3)
                        .eq("group_id", groupId)
                        .inSql("id", "select user_id from sys_user_role_lv where is_latest=1 and id in (select lv_id " +
                                "from" +
                                " sys_user_role_v  where role_id='" + roleId + "')")
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("organization_id", orgIds)
                        .and(StringUtils.isNotBlank(searchStr)
                                , i -> i.like("user_name", searchStr)
                                        .or().like("pinyin", searchStr)
                                        .or().like("real_name", searchStr)
                                        .or().like("email", searchStr)
                                        .or().like("phone_number", searchStr)
                                        .or().like("staff_no", searchStr))
                        .and(Objects.equals("2", params.get("type")), i -> i.eq("start_flag", "1")).orderByAsc
                                ("sort_index"));
            }
        }
        // 给用户添加 所属集团名称  所属业务单元名称
        if (list != null) {
            List<SysUserDO> users = list.getRecords();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(users)) {
                users = sysUserService.fillUserOrgAndGroup(null, users);
                list.setRecords(users);
            }
        }
        return R.ok().put("page", list);
    }

    /**
     * 查询指定用户信息
     *
     * @param userId 用户主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "获取单个用户", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")
    @AceAuth("获取单个用户")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public R infoById(@PathVariable("userId") String userId) {
        SysUserDO user = sysUserService.getById(userId);
        // 人员档案
        String personId = user.getPersonDocId();
        if (StringUtils.isNotBlank(personId)) {
            BdPersonDocDO person = bdPersonDocService.getById(personId);
            user.setPersonDoc(person);
            BdPersonJobDO personJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    personId).eq("is_main_job", 1));
            user.setBdPersonJobDO(personJob);
        }
        return R.ok().put("instance", user);
    }

    /**
     * 根据部门和角色条件进行查询用户列表
     * 应用程序普通用户中调用
     *
     * @param map 参数
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "根据部门和角色条件进行查询用户列表", httpMethod = "POST")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("根据部门和角色条件进行查询用户列表")
    @RequestMapping(value = "/listForDepOrRole", method = RequestMethod.POST)
    public R listForDepOrRole(@RequestBody Map<String, String> map) {
        return sysUserService.getUserListForDepOrRole(map, 0);
    }

    /**
     * 获取委托规则中可委托的受委托人
     *
     * @param map 参数
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "获取委托规则中可委托的受委托人", httpMethod = "POST")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取委托规则中可委托的受委托人")
    @RequestMapping(value = "/listForFlowAgent", method = RequestMethod.POST)
    public R listForFlowAgent(@RequestBody Map<String, String> map) {
        return sysUserService.getUserListForDepOrRole(map, 1);
    }

    /**
     * 通过部门ID获取用户列表
     *
     * @param map 参数
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "通过部门ID获取用户列表", httpMethod = "POST")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过部门ID获取用户列表")
    @RequestMapping(value = "/list/forGroupOrOrgOrDep", method = RequestMethod.POST)
    public R listForDep(@RequestBody Map<String, String> map) {
        int current = Integer.parseInt(map.get("current"));
        int size = Integer.parseInt(map.get("size"));
        // 检索条件
        String searchStr = map.get("searchString");

        // 筛选条件
        String selectValueId = map.get("selectValueId");

        IPage list = null;
        Page<SysUserDO> page = new Page<>(current, size);

        // 根据选择的集团ID筛选
        if (Objects.equals("groupSel", selectValueId)) {
            String groupId = map.get("groupId");
            // 同时查询该集团下级部门的所有成员
            List<String> groupIds = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                    .select("id").eq("parent_id", groupId)).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            groupIds.add(groupId);

            list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .eq("user_type", 3)
                    .in("group_id", groupIds)
                    .eq("start_flag", "1")
                    .and(StringUtils.isNotBlank(searchStr)
                            , i -> i.like("user_name", searchStr)
                                    .or().like("pinyin", searchStr)
                                    .or().like("real_name", searchStr)
                                    .or().like("email", searchStr)
                                    .or().like("phone_number", searchStr))
                    .orderByAsc("sort_index"));
        }
        if (Objects.equals("orgSel", selectValueId)) {
            // 根据选择的组织ID筛选
            String orgId = map.get("orgId");
            // 同时查询该组织下级部门的所有成员
            List<String> orgIds = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                    .select("id").eq("parent_id", orgId).eq("is_business_unit", 1)).stream().map
                    (AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            orgIds.add(orgId);

            list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .eq("user_type", 3)
                    .in("ORGANIZATION_ID", orgIds)
                    .eq("start_flag", "1")
                    .and(StringUtils.isNotBlank(searchStr)
                            , i -> i.like("user_name", searchStr)
                                    .or().like("pinyin", searchStr)
                                    .or().like("real_name", searchStr)
                                    .or().like("email", searchStr)
                                    .or().like("phone_number", searchStr))
                    .orderByAsc("sort_index"));
        }
        if (Objects.equals("depSel", selectValueId)) {
            // 根据选择的部门ID筛选

            // 用户管理左侧 部门
            String depId = map.get("depId");
            OrgDepartmentDO departmentDO = orgDepartmentService.getById(depId);
            // 同时查询该部门下级部门的所有成员

            list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .eq("user_type", 3)
                    .inSql("person_doc_id", "select PERSON_DOC_ID from BD_PERSON_JOB where department_id in (" +
                            "select id from ORG_DEPARTMENT where SORT_PATH like '" + departmentDO.getSortPath() + "%'" +
                            " and is_delete=0 and group_id='" + departmentDO.getGroupId() + "')")
                    .eq("start_flag", "1")
                    .and(StringUtils.isNotBlank(searchStr)
                            , i -> i.like("user_name", searchStr)
                                    .or().like("pinyin", searchStr)
                                    .or().like("real_name", searchStr)
                                    .or().like("email", searchStr)
                                    .or().like("phone_number", searchStr))
                    .orderByAsc("sort_index"));
        }
        return R.ok().put("page", list);
    }

    /**
     * 通过用户名称查询指定用户信息
     *
     * @param userName 用户名称
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "通过用户名称获取单个用户", httpMethod = "GET")
    @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "String")
    @AceAuth("通过用户名称获取单个用户")
    @RequestMapping(value = "/action/userName/{userName}", method = RequestMethod.GET)
    public R infoByUserName(@PathVariable("userName") String userName) {
        SysUserDO user = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("user_type", 3).eq("is_delete", 0).eq
                ("user_name",
                        userName));
        // 人员档案
        String personId = user.getPersonDocId();
        if (StringUtils.isNotBlank(personId)) {
            BdPersonDocDO person = bdPersonDocService.getById(personId);
            user.setPersonDoc(person);
            BdPersonJobDO personJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    personId).eq("is_main_job", 1));
            user.setBdPersonJobDO(personJob);
        }
        return R.ok().put("instance", user);
    }


    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    /**
     * 获取当前登录用户信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:39
     */
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "GET")
    @AceAuth("获取当前登录用户信息")
    @RequestMapping(value = "/action/info", method = RequestMethod.GET)
    public R info() {

        SysUserDO user = securityUtils.getCurrentUser();
        /**
         * 统计用户可管理集团的数量
         */
        List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(user.getId());
        cacheUtil.set(securityUtils.getToken() + "groups", JSONObject.toJSON(groups), CacheUtil.NOT_EXPIRE);
        // 人员档案
        String personId = user.getPersonDocId();
        OrgDepartmentDO dep = new OrgDepartmentDO();
        if (StringUtils.isNotBlank(personId)) {
            BdPersonDocDO person = bdPersonDocService.getById(personId);
            user.setPersonDoc(person);
            BdPersonJobDO personJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    personId).eq("is_main_job", 1));
            user.setBdPersonJobDO(personJob);
            dep = orgDepartmentService.getById(personJob.getDepartmentId());
        }
        String defaultSecretLevel = cacheUtil.get("defaultSecretLevel");

//        List<SysRoleDO> roles = new ArrayList<>(16);
//        List<SysUserRoleDO> sysUserRoleDOS = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
//                .eq("user_id", user.getId()).select("role_id"));
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysUserRoleDOS)) {
//            Set<String> roleIds = sysUserRoleDOS.stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toSet());
//            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(roleIds)) {
//                roles.addAll(sysRoleService.listByIds(roleIds));
//            }
//        }
        List<SysRoleDO> roles = sysUserRoleService.getEffectiveRoleData(user.getId(), null);
//        if (sysUserService.unlockUser(user.getId())) {
//            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
//        }
        return R.ok()
                .put("dep", dep).put("user", user).put("groups", groups).put("defaultSecretLevel", Integer.parseInt
                        (defaultSecretLevel)).put("groupNum", groups.size()).put("roles", roles);
    }

    /**
     * 管理员解锁用户
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:39
     */
    @ApiOperation(value = "管理员解锁用户", httpMethod = "GET")
    @AceAuth("管理员解锁用户")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String")
    @RequestMapping(value = "/action/unlockUser/{userId}", method = RequestMethod.GET)
    public R unlockUser(@PathVariable("userId") String userId) {
        if (sysUserService.unlockUser(userId)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.ok(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 保存用户
     *
     * @param user user
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:40
     */
    @ApiOperation(value = "保存用户", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "SysUserDO")
    @AceAuth("保存用户")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody SysUserDO user) {
        return sysUserService.saveUser(user);
    }

    /**
     * 修改用户
     *
     * @param user 用户对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:43
     */
    @ApiOperation(value = "修改用户", httpMethod = "PUT")
    @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "SysUserDO")
    @AceAuth("修改用户")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody SysUserDO user) {
        return sysUserService.updateUser(user);
    }

    /**
     * 删除一个或多个用户
     *
     * @param userIds 用户ID数组
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:42
     */
    @ApiOperation(value = "删除一个或多个用户", httpMethod = "DELETE")
    @AceAuth("删除一个或多个用户")
    @ApiImplicitParam(name = "userIds", value = "用户id集合", required = true, dataType = "String[]")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] userIds) {
        return sysUserService.deleteUser(userIds);
    }

    /**
     * 关联人员档案
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/19 8:12
     */
    @ApiOperation(value = "关联人员档案", httpMethod = "POST")
    @AceAuth("关联人员档案")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @RequestMapping(value = "/action/persondoc", method = RequestMethod.POST)
    public R persondoc(@RequestBody Map<String, String> map) {
        return sysUserService.persondoc(map);
    }

    /**
     * 只有租户三员才可以修改密码
     * 更新租户三员密码
     *
     * @return
     * @author yansiyang
     * @date 2021/6/28 11:43
     */
    @ApiOperation(value = "手动获取密码", httpMethod = "GET")
    @RequestMapping(value = "/cipherPassword/{userName}/{password}", method = RequestMethod.GET)
    public String pwToCipherPassword(@PathVariable("userName") String userName, @PathVariable("password") String
            password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_USERNAME_OR_PASSWORD"));
        }

        SysPasswordPolicyDO passwordPolicy = sysPasswordPolicyService.getPasswordPolicy();

        /**
         * 校验密码长度
         * 非密、秘密至少8位
         * 机密至少10位
         */
        int length = passwordPolicy.getLen() > 8 ? passwordPolicy.getLen() : 8;
        /**
         * 校验密码必须同时包含大写字母、小写字母、数字、特殊符号
         */
        String useEl = cacheUtil.get("ace-password-policy-useEl");
        if (Objects.equals(useEl, "yes")) {
            /**
             * 校验密码必须同时包含大写字母、小写字母、数字、特殊符号
             */
            String pwPattern = String.format("^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)" +
                    "[a-zA-Z0-9\\W]{%d,}$", length);
            if (!password.matches(pwPattern)) {
                throw new RException(String.format(InternationUtils.getInternationalMsg("ERROR_PASSWORD_LENGTH"),
                        length));
            }
        } else {
            if (password.length() < length) {
                throw new RException(String.format(InternationUtils.getInternationalMsg("ERROR_PASSWORD_LENGTH"),
                        length));
            }
        }

        /**
         * 加密保存密码
         */
        try {
            password = GMBaseUtil.pwToCipherPassword(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }
        return password;
    }

    /**
     * 只有租户三员才可以修改密码
     * 更新租户三员密码
     *
     * @return
     * @author yansiyang
     * @date 2021/6/28 11:43
     */
    @ApiOperation(value = "更新租户三员密码", httpMethod = "GET")
    @RequestMapping(value = "/updateTenantPassword/{userName}/{password}", method = RequestMethod.GET)
    public R updateTenantPassword(@PathVariable("userName") String userName, @PathVariable("password") String
            password) {
        String userId = securityUtils.getCurrentUserId();
        String roleId = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId)).getRoleId();

        List<String> roleIdList = new ArrayList<>(16);
        roleIdList.add("admin");
        roleIdList.add("sec");
        roleIdList.add("auditor");

        if (!roleIdList.contains(roleId)) {
            return R.error();
        }

        /**
         * https 传输明文密码
         */
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return R.error();
        }
        return sysUserService.updatePassword(userName, password, false);
    }

    /**
     * 更新密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:43
     */
    @ApiOperation(value = "更新密码", httpMethod = "POST")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public R updatePassword(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        /**
         * https 传输明文密码
         */
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return R.error(InternationUtils.getInternationalMsg("INPUT_OLD_OR_NEW_PASSWORD"));
        }

        /**
         * 验证旧的密码是否正确
         */
        if (!sysUserService.authenticate(userName, oldPassword)) {
            return R.error(InternationUtils.getInternationalMsg("INPUT_CORRECT_OLD_PASSWORD"));
        }
        return sysUserService.updatePassword(userName, newPassword, false);
    }

    /**
     * 租户管理员修改默认密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/24 11:45
     */
    @ApiOperation(value = "租户管理员修改默认密码")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @AceAuth("租户管理员修改默认密码")
    @RequestMapping(value = "/action/resetDefaultPassword", method = RequestMethod.POST)
    public R resetDefaultPassword(@RequestBody Map<String, String> map) {
        return sysUserService.resetDefaultPassword(map);
    }

    /**
     * 管理员重置用户密码
     *
     * @param userName
     * @return
     * @author yansiyang
     * @date 2019/4/24 11:45
     */
    @ApiOperation(value = "管理员重置用户密码")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String")
    @AceAuth("管理员重置用户密码")
    @RequestMapping(value = "/action/resetUserPassword/{userName}", method = RequestMethod.GET)
    public R resetUserPassword(@PathVariable("userName") String userName) {
        return sysUserService.resetUserPassword(userName);
    }

    /**
     * 修改用户集团授控域
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "修改用户集团授控域")
    @ApiImplicitParam(name = "sysUserDO", value = "用户对象", required = true, dataType = "SysUserDO")
    @AceAuth("修改用户集团授控域")
    @RequestMapping(value = "/userGroupControlDomain", method = RequestMethod.POST)
    public R saveUserGroupControlDomain(@RequestBody SysUserDO sysUserDO) {
        if (sysUserService.saveUserGroupControlDomain(sysUserDO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 修改用户应用授控域
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "修改用户应用授控域")
    @ApiImplicitParam(name = "sysUserDO", value = "用户", required = true, dataType = "SysUserDO")
    @AceAuth("修改用户应用授控域")
    @RequestMapping(value = "/userAppControlDomain", method = RequestMethod.POST)
    public R saveUserAppControlDomain(@RequestBody SysUserDO sysUserDO) {
        if (sysUserService.saveUserAppControlDomain(sysUserDO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 修改用户应用授控域并激活
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "修改用户应用授控域")
    @ApiImplicitParam(name = "sysUserDO", value = "用户", required = true, dataType = "SysUserDO")
    @AceAuth("修改用户应用授控域")
    @RequestMapping(value = "/userAppControlDomainAndActive", method = RequestMethod.POST)
    public R saveuserAppControlDomainAndActive(@RequestBody SysUserDO sysUserDO) {
        if (sysUserService.saveuserAppControlDomainAndActive(sysUserDO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 获取租户三员IP地址
     *
     * @return
     * @author zuogang
     * @date 2019/11/5 8:04
     */
    @ApiOperation(value = "获取租户三员IP地址", httpMethod = "GET", notes = "获取租户三员IP地址")
    @AceAuth("获取租户三员IP地址")
    @RequestMapping(value = "/action/getThreeTenantsIpAddress", method = RequestMethod.GET)
    public R getThreeTenantsIpAddress() {
        String adminIpAddress = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("user_name", "admin"))
                .getIpAddress();
        String secIpAddress = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("user_name", "sec"))
                .getIpAddress();
        String auditorIpAddress = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("user_name", "auditor"))
                .getIpAddress();
        return R.ok().put("adminIpAddress", adminIpAddress).put("secIpAddress", secIpAddress).put("auditorIpAddress",
                auditorIpAddress);
    }

    /**
     * 修改租户三员IP地址
     *
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiOperation(value = "修改管理员IP地址校验", httpMethod = "POST", notes = "修改管理员IP地址校验")
    @AceAuth("修改管理员IP地址校验")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @RequestMapping(value = "/action/updThreeTenantsIpAddress", method = RequestMethod.POST)
    public R updThreeTenantsIpAddress(@RequestBody Map<String, String> map) {
        if (sysUserService.updThreeTenantsIpAddress(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 批量重置密码
     *
     * @param map
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2020/3/17 10:33
     */
    @ApiOperation(value = "批量重置密码", httpMethod = "POST", notes = "批量重置密码")
    @AceAuth("批量重置密码")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @RequestMapping(value = "/action/batchResetPassword", method = RequestMethod.POST)
    public R batchResetPassword(@RequestBody Map<String, String> map) {
        if (sysUserService.batchResetPassword(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }


    /**
     * 开发平台-用户管理-列表
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/7/14 11:14
     */
    @ApiOperation(value = "开发平台-用户管理-列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, paramType = "Map")
    @AceAuth("开发平台-用户管理-列表")
    @RequestMapping(value = "/action/listForDev", method = RequestMethod.GET)
    public R listForDev(@RequestParam Map<String, Object> params) {

        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        String searchStr = (String) params.get("searching");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List<SysUserDO> list = sysUserService.list(new QueryWrapper<SysUserDO>().eq("user_type", 4).ne
                    ("user_name", "devadmin").eq("is_delete", 0));
            List<SysUserDO> sysUserDOS = new ArrayList<>(16);
            list.stream().forEach(user -> {
                SysUserRoleDO sysUserRoleDO = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>().eq("user_id",
                        user.getId()));
                if (!Objects.equals("devmaintainer", sysUserRoleDO.getRoleId())) {
                    sysUserDOS.add(user);
                }
            });
            return R.ok().put("list", sysUserDOS);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);

        Page<SysUserDO> page = new Page<>(current, size);
        IPage list = sysUserService.page(page, new QueryWrapper<SysUserDO>().eq("user_type", 4).ne
                ("user_name", "devadmin").eq("is_delete", 0).and(StringUtils.isNotBlank(searchStr)
                , i -> i.like("user_name", searchStr)
                        .or().like("real_name", searchStr)));
        List<SysUserDO> sysUserDOS = list.getRecords();
        sysUserDOS.stream().forEach(user -> {
            SysUserRoleDO sysUserRoleDO = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>().eq("user_id",
                    user.getId()));
            user.setRoleType(sysRoleService.getById(sysUserRoleDO.getRoleId()).getRoleType());
        });
        list.setRecords(sysUserDOS);
        return R.ok().put("page", list);
    }

    /**
     * 开发平台-用户管理-新增
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/7/14 11:14
     */
    @ApiOperation(value = "开发平台-用户管理-新增", httpMethod = "POST", notes = "开发平台-用户管理-新增")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @AceAuth("开发平台-用户管理-新增")
    @RequestMapping(value = "/action/saveUser/forDev", method = RequestMethod.POST)
    public R saveUserForDev(@RequestBody Map<String, Object> map) {
        if (sysUserService.saveUserForDev(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 开发平台-用户管理-修改
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/7/14 11:14
     */
    @ApiOperation(value = "开发平台-用户管理-修改", httpMethod = "POST", notes = "开发平台-用户管理-修改")
    @ApiImplicitParam(name = "map", value = "参数对象", required = true, dataType = "Map")
    @AceAuth("开发平台-用户管理-修改")
    @RequestMapping(value = "/action/updateUser/forDev", method = RequestMethod.POST)
    public R updateUserForDev(@RequestBody Map<String, Object> map) {
        if (sysUserService.updateUserForDev(map)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 创建开发平台超级管理员
     *
     * @return
     * @author zuogang
     * @date 2020/8/10 17:15
     */
    @ApiOperation(value = "创建开发平台超级管理员", httpMethod = "POST", notes = "创建开发平台超级管理员")
    @AceAuth("创建开发平台超级管理员")
    @RequestMapping(value = "/action/addDevadmin", method = RequestMethod.POST)
    public R addDevAdmin() {

        return sysUserService.addDevAdmin();
    }

    /**
     * 获取用户密级类别
     *
     * @return
     * @author yansiyang
     * @date 2019/11/7 19:35
     */
    @ApiOperation(value = "获取用户密级类别", httpMethod = "GET", notes = "获取用户密级类别")
    @RequestMapping(value = "/action/getUserSecretType", method = RequestMethod.GET)
    public R getUserSecretType() {

        List<SysDictValueDO> valueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                .eq("type", "USER_SECRET_TYPE"));
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(valueDOS)) {
            return R.ok().put("instance", valueDOS.get(0));
        }

        return R.ok().put("instance", new SysDictValueDO());
    }
}
