package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.ExcelUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.AceDBHelperMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogBackupService;
import com.csicit.ace.data.persistent.service.SysAuditLogCountService;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.SysAuthScopeAppService;
import com.csicit.ace.platform.core.service.SysAuthScopeOrgService;
import com.csicit.ace.platform.core.service.SysAuthScopeUserGroupService;
import com.csicit.ace.platform.core.service.SysUserAdminOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/6/3 18:13
 */
@RestController
@RequestMapping("/sysAuditLogs")
@Api("系统日志管理")
public class SysAuditLogController extends BaseController {

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SysAuditLogBackupService sysAuditLogBackupService;

    @Autowired
    SysAuditLogCountService sysAuditLogCountService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;

    @Autowired
    SysAuthScopeUserGroupService sysAuthScopeUserGroupService;

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;

    /**
     * 获取审计日志列表
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "获取单条审计日志", httpMethod = "GET", notes = "获取单条审计日志")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "String")
    @AceAuth("获取单条审计日志")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysAuditLogDO log = sysAuditLogService.getById(id);
        return R.ok().put("log", log);
    }


    @Autowired
    AceDBHelperMapper aceDbHelperMapper;

    public final static String TYPE_ALL = "全部";

    /**
     * 获取审计日志的类别列表
     *
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "获取审计日志的类别列表", httpMethod = "GET", notes = "获取审计日志的类别列表")
    @AceAuth("获取审计日志的类别列表")
    @RequestMapping(value = "/action/getTypeList", method = RequestMethod.GET)
    public R getTypeList() {
        List<String> types = new ArrayList<>();
        types.add(TYPE_ALL);
        types.addAll(aceDbHelperMapper.
                getStrings("select distinct type from sys_audit_log where app_id " +
                        "in ('platform','fileserver','orgauth','report','quartz','ace-zuul')"));
        return R.ok().put("types", types);
    }

    /**
     * 同步审计日志
     *
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "同步审计日志", httpMethod = "GET", notes = "同步审计日志")
    @AceAuth("同步审计日志")
    @RequestMapping(value = "/action/synchronize", method = RequestMethod.GET)
    public R synchronize() {
        // 只有租户级可操作
        if (securityUtils.getCurrentUser().getUserType() != 0) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        Map<String, String> map = sysAuditLogService.validateLogCount();
        if (Boolean.parseBoolean(map.get("edited"))) {
            return R.error().put("errorCode", Integer.parseInt(map.get("errorCode"))).put("msg", map.get("msg"));
        }
        if (sysAuditLogService.synchronize()) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        } else {
            return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
    }

    /**
     * 获取审计日志列表
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/6/3 18:14
     */
    @ApiOperation(value = "获取审计日志列表并分页", httpMethod = "GET", notes = "获取审计日志列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取审计日志列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {

        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");

        boolean beginTimeValid = StringUtils.isNotBlank(beginTime);
        boolean endTimeValid = StringUtils.isNotBlank(endTime);

        Page<SysAuditLogDO> page = new Page<>(current, size);

        String userId = securityUtils.getCurrentUserId();

        String type = (String) params.get("type");

        String roleId = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId)).getRoleId();

        List<String> roleIdList = new ArrayList<>(16);
        List<String> roleTypeList = new ArrayList<>(16);
        IPage list = new Page();
        if (Objects.equals(roleId, "auditor")) {
            // 租户安全审计员可查看：租户系统管理员、租户安全保密员、集团系统管理员、集团安全保密员、应用系统管理员、应用安全保密员
            roleIdList.add("admin");
            roleIdList.add("sec");
            roleIdList.add("groupadmin");
            roleIdList.add("groupsec");
            roleIdList.add("appadmin");
            roleIdList.add("appsec");
            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("ip_address", params.get("ip_address"))
                                            .or().like("title", params.get("title")))
//                                            .or().like("op_content", params.get("op_content")))
                            .in("role_id", roleIdList)
                            .orderByDesc("op_time"));

        } else if (Objects.equals(roleId, "sec")) {
            // 租户安全保密员可查看：所有普通用户 以及所有安全审计员
//            roleIdList.add("auditor");
//            roleIdList.add("groupauditor");
//            roleIdList.add("appauditor");
            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("ip_address", params.get("ip_address"))
                                            .or().like("title", params.get("title")))
//                                            .or().like("op_content", params.get("op_content"))
                            .eq("user_type", "3")
//                            .and(a -> a.eq("user_type", "3")
//                                    .or())
                                    //.in("role_id", roleIdList))
                            .in("app_id", Constants.AppNames)
                            .orderByDesc("op_time")
            );

        } else if (Objects.equals(roleId, "groupauditor")) {
            // 集团安全审计员可查看:相同集团管控域下的集团安全保密员,集团系统管理员,
            // 相同管控应用下的应用安全保密员,应用系统管理员
            Set<String> adminUserIds = new HashSet<>();
            roleIdList.add("groupadmin");
            roleIdList.add("groupsec");
            roleTypeList.add("11");
            roleTypeList.add("22");
            adminUserIds.addAll(getUserIdsForGroupByRoleIds(roleTypeList));
            roleIdList.add("appadmin");
            roleIdList.add("appsec");
            roleTypeList.add("111");
            roleTypeList.add("222");
            adminUserIds.addAll(getUserIdsForAppByRoleIds(roleTypeList));

            List<String> adminUserNames = sysUserService.listByIds(adminUserIds)
                    .stream().map(SysUserDO::getUserName).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(adminUserNames)) {
                list = sysAuditLogService.page(page,
                        new QueryWrapper<SysAuditLogDO>()
                                .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                                .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                                .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                        , "type", type)
                                .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                        , i -> i.like("op_userName", params.get("op_userName"))
                                                .or().like("op_name", params.get("op_name"))
                                                .or().like("title", params.get("title"))
                                                .or().like("ip_address", params.get("ip_address")))
//                                                .or().like("op_content", params.get("op_content"))
                                .in("op_username", adminUserNames)
                                .orderByDesc("op_time"));
            }
        } else if (Objects.equals(roleId, "groupsec")) {
            // 集团安全保密员可查看:集团管控域下的所有普通用户，相同管控域下的集团安全审计员,应用审计员日志
//            roleIdList.add("groupauditor");
//            roleIdList.add("appauditor");
//            roleTypeList.add("33");
//            roleTypeList.add("333");
            // List<String> adminUserNames = sysUserService.listByIds(getUserIdsForGroupByRoleIds(roleTypeList))
                    //.stream().map(SysUserDO::getUserName).collect(Collectors.toList());

            List<String> groupIds = orgGroupService.getGroupsByUserId(userId).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());

            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("title", params.get("title"))
                                            .or().like("ip_address", params.get("ip_address")))
//                                            .or().like("op_content", params.get("op_content")))
                            .and(i -> i.and(a -> a.eq("user_type", "3")
                                    .in("group_id", groupIds))
//                                    .or(CollectionUtils.isNotEmpty(adminUserNames),
//                                            b -> b.in("op_username", adminUserNames))
                            )
                            .in("app_id", Constants.AppNames)
                            .orderByDesc("op_time"));
        } else if(Objects.equals(roleId, "groupsuperadmin")){
            // 集团超级管理员可查看:相同管控域下的集团超级管理员日志，及集团管控域下的应用超级管理员日志
            Set<String> adminUserIds = new HashSet<>();
            roleIdList.add("groupsuperadmin");
            roleTypeList.add("44");
            adminUserIds.addAll(getUserIdsForGroupByRoleIds(roleTypeList));
            roleIdList.add("appsuperadmin");
            roleTypeList.add("444");
            adminUserIds.addAll(getUserIdsForAppByRoleIds(roleTypeList));

            List<String> adminUserNames = sysUserService.listByIds(adminUserIds)
                    .stream().map(SysUserDO::getUserName).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(adminUserNames)) {
                list = sysAuditLogService.page(page,
                        new QueryWrapper<SysAuditLogDO>()
                                .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                                .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                                .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                        , "type", type)
                                .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                        , i -> i.like("op_userName", params.get("op_userName"))
                                                .or().like("op_name", params.get("op_name"))
                                                .or().like("title", params.get("title"))
                                                .or().like("ip_address", params.get("ip_address")))
//                                                .or().like("op_content", params.get("op_content")))
                                .in("op_username", adminUserNames)
                                .orderByDesc("op_time"));
            }

        }else if (Objects.equals(roleId, "appauditor")) {
            // 应用安全审计员可查看:相同应用授控域下的,应用安全保密员,应用集团管理员
            roleIdList.add("appsec");
            roleIdList.add("appadmin");
            roleTypeList.add("111");
            roleTypeList.add("222");
            List<String> adminUserNames = sysUserService.listByIds(getUserIdsForAppByRoleIds(roleTypeList))
                    .stream().map(SysUserDO::getUserName).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(adminUserNames)) {
                list = sysAuditLogService.page(page,
                        new QueryWrapper<SysAuditLogDO>()
                                .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                                .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                                .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                        , "type", type)
                                .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                        , i -> i.like("op_userName", params.get("op_userName"))
                                                .or().like("op_name", params.get("op_name"))
                                                .or().like("title", params.get("title"))
                                                .or().like("ip_address", params.get("ip_address")))
//                                                .or().like("op_content", params.get("op_content")))
                                .in("op_username", adminUserNames)
                                .orderByDesc("op_time")
                );
            }
        } else if (Objects.equals(roleId, "appsec")) {
            // 应用安全保密员可以查看组织管控域下的普通用户信息，相同应用管控域下的安全审计员日志
//            roleIdList.add("appauditor");
//            roleTypeList.add("333");
//            List<String> adminUserNames = sysUserService.listByIds(getUserIdsForAppByRoleIds(roleTypeList))
//                    .stream().map(SysUserDO::getUserName).collect(Collectors.toList());
            // 获取应用授控域
            List<String> appIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("user_id", securityUtils.getCurrentUserId()).eq("is_activated", 1))
                    .stream().map(SysAuthScopeAppDO::getAppId).distinct().collect(Collectors.toList());

            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("title", params.get("title"))
                                            .or().like("ip_address", params.get("ip_address")))
//                                            .or().like("op_content", params.get("op_content")))
                            .and(i -> i.and(a -> a.eq("user_type", "3")
                                    .in("app_id", appIds))
//                                    .or(CollectionUtils.isNotEmpty(adminUserNames),
//                                            b -> b.in("op_username", adminUserNames))
                            )
                            .orderByDesc("op_time")
            );
        }else if (Objects.equals(roleId, "appsuperadmin")) {
            // 应用超级管理员可查看:相同应用授控域下的应用超级管理员
            roleIdList.add("appsuperadmin");
            roleTypeList.add("444");
            List<String> adminUserNames = sysUserService.listByIds(getUserIdsForAppByRoleIds(roleTypeList))
                    .stream().map(SysUserDO::getUserName).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(adminUserNames)) {
                list = sysAuditLogService.page(page,
                        new QueryWrapper<SysAuditLogDO>()
                                .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                                .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                                .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                        , "type", type)
                                .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                        , i -> i.like("op_userName", params.get("op_userName"))
                                                .or().like("op_name", params.get("op_name"))
                                                .or().like("title", params.get("title"))
                                                .or().like("ip_address", params.get("ip_address")))
//                                                .or().like("op_content", params.get("op_content")))
                                .in("op_username", adminUserNames)
                                .orderByDesc("op_time")
                );
            }
        }else if(Objects.equals("businessadmin",roleId)){

                // 单一业务管理员
                Set<String> adminUserIds = new HashSet<>();
                roleIdList.add("businessadmin");
                roleTypeList.add("4");
                adminUserIds.addAll(getUserIdsForGroupByRoleIds(roleTypeList));

                List<String> adminUserNames = sysUserService.listByIds(adminUserIds)
                        .stream().map(SysUserDO::getUserName).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(adminUserNames)) {
                    list = sysAuditLogService.page(page,
                            new QueryWrapper<SysAuditLogDO>()
                                    .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                                    .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                                    .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                            , "type", type)
                                    .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                            , i -> i.like("op_userName", params.get("op_userName"))
                                                    .or().like("op_name", params.get("op_name"))
                                                    .or().like("title", params.get("title"))
                                                    .or().like("ip_address", params.get("ip_address")))
                                    .in("op_username", adminUserNames)
                                    .orderByDesc("op_time"));
                }


        }

        R r = new R();
        r.put("page", list);
        return r;
    }

    /**
     * 获取同一集团管控域下的集团管理员id
     *
     * @param roleTypes
     * @return
     * @author FourLeaves
     * @date 2020/8/14 15:13
     */
    private List<String> getUserIdsForGroupByRoleIds(List<String> roleTypes) {
        Set<String> userIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(roleTypes)) {
            List<SysUserAdminOrgDO> adminOrgs = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("is_activated", 1).select("id", "organization_id").eq("user_id", securityUtils.getCurrentUserId()));
            if (CollectionUtils.isNotEmpty(adminOrgs)) {
                List<String> groupIds = adminOrgs.stream().map(SysUserAdminOrgDO::getOrganizationId)
                        .collect(Collectors.toList());
                List<SysUserAdminOrgDO> adminOrgsT = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                        .eq("is_activated", 1)
                        .in("organization_id", groupIds)
                        .in("role_type", roleTypes)
                        .select("id", "user_id"));
                if (CollectionUtils.isNotEmpty(adminOrgsT)) {
                    userIds.addAll(adminOrgsT.stream().map(SysUserAdminOrgDO::getUserId)
                            .collect(Collectors.toList()));
                }
            }
        }
        return new ArrayList<>(userIds);
    }

    /**
     * 获取同一应用 业务单元 管控域下的应用管理员id
     *
     * @param roleTypes
     * @return
     * @author FourLeaves
     * @date 2020/8/14 15:14
     */
    private List<String> getUserIdsForAppByRoleIds(List<String> roleTypes) {
        Set<String> userIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(roleTypes)) {
            List<SysAuthScopeOrgDO> scopeOrgDOS = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                    .eq("is_activated", 1).select("id", "organization_id").eq("user_id", securityUtils.getCurrentUserId()));
            if (CollectionUtils.isNotEmpty(scopeOrgDOS)) {
                List<String> orgIds = scopeOrgDOS.stream().map(SysAuthScopeOrgDO::getOrganizationId)
                        .collect(Collectors.toList());
                List<SysAuthScopeOrgDO> adminOrgsT = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                        .eq("is_activated", 1).in("organization_id", orgIds)
                        .select("id", "user_id").in("role_type", roleTypes));
                if (CollectionUtils.isNotEmpty(adminOrgsT)) {
                    userIds.addAll(adminOrgsT.stream().map(SysAuthScopeOrgDO::getUserId)
                            .collect(Collectors.toList()));
                }
            }
            List<SysAuthScopeAppDO> scopeAppDOS = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("is_activated", 1).select("id", "app_id").eq("user_id", securityUtils.getCurrentUserId()));
            if (CollectionUtils.isNotEmpty(scopeOrgDOS)) {
                List<String> appIds = scopeAppDOS.stream().map(SysAuthScopeAppDO::getAppId)
                        .collect(Collectors.toList());
                List<SysAuthScopeAppDO> scopeAppDOST = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                        .eq("is_activated", 1).in("app_id", appIds)
                        .select("id", "user_id").in("role_type", roleTypes));
                if (CollectionUtils.isNotEmpty(scopeAppDOST)) {
                    userIds.addAll(scopeAppDOST.stream().map(SysAuthScopeAppDO::getUserId)
                            .collect(Collectors.toList()));
                }
            }
        }
        return new ArrayList<>(userIds);
    }

    /**
     * 集团级别获取集团级别的日志内容
     *
     * @param roleIds
     * @return
     * @author yansiyang
     * @date 2019/9/25 15:50
     */
    private List<SysAuditLogDO> getGroupUserNamesToAuditLog(List<String> roleIds) {
        if (roleIds.size() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        List<SysAuditLogDO> sysAuditLogDOList = new ArrayList<>();
        List<String> groupIds = orgGroupService.getGroupsByUserId(securityUtils.getCurrentUserId())
                .stream().map(OrgGroupDO::getId).collect(Collectors.toList());

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(groupIds)) {
            List<String> userIdList = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                    .in("role_id", roleIds)
                    .select("user_id")).stream().map(SysUserRoleDO::getUserId)
                    .collect(Collectors.toList());

            if (!org.apache.commons.collections.CollectionUtils.isEmpty(userIdList)) {
                List<String> finalGroupIds = groupIds;
                userIdList.stream().forEach(userId -> {
                    // 该用户在该集团管控域下做的操作
                    sysAuditLogDOList.addAll(sysAuditLogService.list(new QueryWrapper<SysAuditLogDO>()
                            .in("group_id", finalGroupIds).eq("OP_USERNAME", sysUserService.getById(userId)
                                    .getUserName())));
                    // groupId为空时做的操作
                    sysAuditLogDOList.addAll(sysAuditLogService.list(new QueryWrapper<SysAuditLogDO>()
                            .isNull("group_id").eq("OP_USERNAME", sysUserService.getById(userId)
                                    .getUserName())));
                });
            }
        }
        return sysAuditLogDOList;
    }

    /**
     * 集团级别获取应用级别的日志信息
     *
     * @param roleIds
     * @return
     * @author yansiyang
     * @date 2019/9/25 15:50
     */
    private List<SysAuditLogDO> getGroupToAppUserNamesToAuditLog(List<String> roleIds) {
        if (roleIds.size() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        List<SysAuditLogDO> sysAuditLogDOList = new ArrayList<>();
        List<String> groupIds = orgGroupService.getGroupsByUserId(securityUtils.getCurrentUserId())
                .stream().map(OrgGroupDO::getId).collect(Collectors.toList());
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(groupIds)) {
            // 获取groupIds对应下的所有应用ID
            List<String> appIds = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
                    .in("group_id", groupIds)).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(appIds)) {
                List<String> userIdList = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .in("role_id", roleIds)
                        .select("user_id")).stream().map(SysUserRoleDO::getUserId)
                        .collect(Collectors.toList());
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(userIdList)) {
                    List<String> finalAppIds = appIds;
                    userIdList.stream().forEach(userId -> {
                        // 该用户在该应用管控域下做的操作
                        sysAuditLogDOList.addAll(sysAuditLogService.list(new QueryWrapper<SysAuditLogDO>()
                                .in("app_id", finalAppIds).eq("OP_USERNAME", sysUserService.getById(userId)
                                        .getUserName())));
                        // appId为空时做的操作
                        sysAuditLogDOList.addAll(sysAuditLogService.list(new QueryWrapper<SysAuditLogDO>()
                                .isNull("app_id").eq("OP_USERNAME", sysUserService.getById(userId)
                                        .getUserName())));
                    });
                }
            }
        }

        return sysAuditLogDOList;
    }

    /**
     * 应用级别获取应用级别的日志信息
     *
     * @param roleIds
     * @return
     * @author yansiyang
     * @date 2019/9/25 15:50
     */
    private List<SysAuditLogDO> getAppUserNamesToAuditLog(List<String> roleIds) {
        if (roleIds.size() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        List<SysAuditLogDO> sysAuditLogDOList = new ArrayList<>();
        String userId = securityUtils.getCurrentUserId();
        List<String> appIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("is_activated", 1).eq("user_id", userId))
                .stream().map(SysAuthScopeAppDO::getAppId).distinct()
                .collect(Collectors.toList());

        List<SysUserRoleDO> userRoleDOS = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .in("role_id", roleIds)
                .select("user_id"));

        if (!org.apache.commons.collections.CollectionUtils.isEmpty(userRoleDOS)) {
            List<String> userIdList = userRoleDOS.stream().map(SysUserRoleDO::getUserId).collect(Collectors.toList
                    ());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIdList)) {
                // 该用户在该应用管控域下做的操作
                sysAuditLogDOList.addAll(sysAuditLogService.list(new QueryWrapper<SysAuditLogDO>()
                        .in("app_id", appIds).eq("OP_USERNAME", sysUserService.getById(userId)
                                .getUserName())));
                // appId为空时做的操作
                sysAuditLogDOList.addAll(sysAuditLogService.list(new QueryWrapper<SysAuditLogDO>()
                        .isNull("app_id").eq("OP_USERNAME", sysUserService.getById(userId)
                                .getUserName())));
            }

        }
        return sysAuditLogDOList;
    }

    /**
     * 导出审计日志列表
     *
     * @param params
     * @return
     * @author xulei
     * @date 2020/07/21 15:12
     */
    @ApiOperation(value = "导出审计日志列表", httpMethod = "GET", notes = "导出审计日志列表Excel")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("导出审计日志列表")
    @RequestMapping(value = "/action/exportExcel", method = RequestMethod.GET)
    public void exportExcel(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        int size = Integer.parseInt((String) params.get("inputArticle"));
        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");

        boolean beginTimeValid = StringUtils.isNotBlank(beginTime);
        boolean endTimeValid = StringUtils.isNotBlank(endTime);

        Page<SysAuditLogDO> page = new Page<>(1, size);

        String userId = securityUtils.getCurrentUserId();

        String type = (String) params.get("type");

        String roleId = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId)).getRoleId();

        List<String> roleIdList = new ArrayList<>(16);
        IPage list = new Page();
        if (Objects.equals(roleId, "auditor")) {
            // 租户安全审计员可查看：租户系统管理员、租户安全保密员、集团系统管理员、集团安全保密员、应用系统管理员、应用安全保密员
            roleIdList.add("admin");
            roleIdList.add("sec");
            roleIdList.add("groupadmin");
            roleIdList.add("groupsec");
            roleIdList.add("appadmin");
            roleIdList.add("appsec");
            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("ip_address", params.get("ip_address"))
                                            .or().like("title", params.get("title")))
//                                            .or().like("op_content", params.get("op_content")))
                            .in("role_id", roleIdList)
                            .orderByDesc("op_time"));

        } else if (Objects.equals(roleId, "sec")) {
            // 租户安全保密员可查看：所有普通用户 以及所有安全审计员
            roleIdList.add("auditor");
            roleIdList.add("groupauditor");
            roleIdList.add("appauditor");
            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("ip_address", params.get("ip_address"))
                                            .or().like("title", params.get("title")))
//                                            .or().like("op_content", params.get("op_content")))
                            .and(a -> a.eq("user_type", "3").or().in("role_id", roleIdList))
                            .orderByDesc("op_time")
            );

        } else if (Objects.equals(roleId, "groupauditor")) {
            // 集团安全审计员可查看:相同集团管控域下的集团安全保密员,集团系统管理员,相同管控应用下的应用安全保密员,应用系统管理员
            roleIdList.add("groupadmin");
            roleIdList.add("groupsec");
            roleIdList.add("appadmin");
            roleIdList.add("appsec");
            List<String> groupIds = new ArrayList<>();
            groupIds.addAll(orgGroupService.getGroupsByUserId(userId)
                    .stream().map(OrgGroupDO::getId).collect(Collectors.toList()));
            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("title", params.get("title"))
                                            .or().like("ip_address", params.get("ip_address")))
//                                            .or().like("op_content", params.get("op_content")))
                            .in("role_id", roleIdList)
                            .and(a -> a.isNull("group_id")
                                    .or(groupIds != null && groupIds.size() > 0, i -> i.in("group_id", groupIds)))
                            .orderByDesc("op_time")
            );

        } else if (Objects.equals(roleId, "groupsec")) {
            // 集团安全保密员可查看:集团管控域下的所有普通用户，相同管控域下的集团安全审计员,应用审计员日志
            roleIdList.add("groupauditor");
            roleIdList.add("appauditor");
            List<String> groupIds = orgGroupService.getGroupsByUserId(userId).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());

            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("title", params.get("title"))
                                            .or().like("ip_address", params.get("ip_address")))
//                                            .or().like("op_content", params.get("op_content")))
                            .and(i -> i.eq("user_type", "3")
                                    .or().in("role_id", roleIdList))
                            .and(a -> a.isNull("group_id")
                                    .or(groupIds != null && groupIds.size() > 0, i -> i.in("group_id", groupIds)))
                            .orderByDesc("op_time")
            );
        } else if (Objects.equals(roleId, "appauditor")) {
            // 应用安全审计员可查看:相同应用授控域下的,应用安全保密员,应用集团管理员
            roleIdList.add("appsec");
            roleIdList.add("appadmin");
            List<String> appIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("user_id", userId))
                    .stream().map(SysAuthScopeAppDO::getAppId).distinct()
                    .collect(Collectors.toList());
            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("title", params.get("title"))
                                            .or().like("ip_address", params.get("ip_address")))
//                                            .or().like("op_content", params.get("op_content")))
                            .in("role_id", roleIdList)
                            .and(a -> a.isNull("app_id")
                                    .or(appIds != null && appIds.size() > 0, i -> i.in("app_id", appIds)))
                            .orderByDesc("op_time")
            );
        } else if (Objects.equals(roleId, "appsec")) {
            // 应用安全保密员可以查看组织管控域下的普通用户信息，相同应用管控域下的安全审计员日志

            // 获取应用授控域
            List<String> appIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("user_id", securityUtils.getCurrentUserId()).eq("is_activated", 1))
                    .stream().map(SysAuthScopeAppDO::getAppId).distinct().collect(Collectors.toList());

            list = sysAuditLogService.page(page,
                    new QueryWrapper<SysAuditLogDO>()
                            .ge(beginTimeValid, "op_time", MapWrapper.formDate(beginTime))
                            .le(endTimeValid, "op_time", MapWrapper.formDate(endTime))
                            .eq(StringUtils.isNotBlank(type) && !Objects.equals(TYPE_ALL, type)
                                    , "type", type)
                            .and(StringUtils.isNotBlank((String) params.get("op_userName"))
                                    , i -> i.like("op_userName", params.get("op_userName"))
                                            .or().like("op_name", params.get("op_name"))
                                            .or().like("title", params.get("title"))
                                            .or().like("ip_address", params.get("ip_address")))
//                                            .or().like("op_content", params.get("op_content")))
                            .and(i -> i.eq("user_type", "3")
                                    .or().eq("role_id", "appauditor"))
                            .and(a -> a.isNull("app_id")
                                    .or(appIds != null && appIds.size() > 0, i -> i.in("app_id", appIds)))
                            .orderByDesc("op_time")
            );
        }
        Map<String, String> map = new LinkedHashMap<>();
        map.put("opName", "操作员姓名");
        map.put("opUsername", "操作员用户名");
        map.put("ipAddress", "ip地址");
        map.put("type", "类别名称");
        map.put("title", "标题");
        map.put("opTime", "操作时间");
        map.put("opContent", "操作内容");
        try {
            ExcelUtil.OutFileToStream(request, response, map, page.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
