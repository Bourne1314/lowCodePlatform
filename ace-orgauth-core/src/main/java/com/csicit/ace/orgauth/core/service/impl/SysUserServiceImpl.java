package com.csicit.ace.orgauth.core.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.SysOnlineUserVO;
import com.csicit.ace.common.utils.*;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.cipher.SM4Util;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.SysUserMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.orgauth.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service("sysUserServiceO")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    CacheUtil cacheUtil;

    @Resource(name = "bdPersonJobServiceO")
    BdPersonJobService bdPersonJobService;

    @Resource(name = "orgDepartmentServiceO")
    OrgDepartmentService orgDepartmentService;

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Resource(name = "sysPasswordPolicyServiceO")
    SysPasswordPolicyService sysPasswordPolicyService;

    @Resource(name = "sysUserPasswordHistoryServiceO")
    SysUserPasswordHistoryService sysUserPasswordHistoryService;

    @Autowired
    SecurityUtils securityUtils;

    @Value("${spring.redis.host:#{null}}")
    private String host;

    @Resource(name = "bdPersonDocServiceO")
    BdPersonDocService bdPersonDocService;

    @Resource(name = "orgOrganizationServiceO")
    OrgOrganizationService orgOrganizationService;

    @Resource(name = "sysUserLoginServiceO")
    SysUserLoginService sysUserLoginService;

    @Resource(name = "sysUserRoleLvServiceO")
    SysUserRoleLvService sysUserRoleLvService;

    @Resource(name = "sysUserRoleVServiceO")
    SysUserRoleVService sysUserRoleVService;

    @Resource(name = "sysApiMixServiceO")
    SysApiMixService sysApiMixService;

    @Resource(name = "sysGroupAppServiceO")
    SysGroupAppService sysGroupAppService;

    @Resource(name = "orgGroupServiceO")
    OrgGroupService orgGroupService;

    @Resource(name = "sysUserRoleServiceO")
    SysUserRoleService sysUserRoleService;

    @Resource(name = "sysRoleServiceO")
    SysRoleService sysRoleService;

    @Resource(name = "sysUserGroupUserServiceO")
    SysUserGroupUserService sysUserGroupUserService;

    @Resource(name = "sysUserAdminOrgServiceO")
    SysUserAdminOrgService sysUserAdminOrgService;

    @Resource(name = "sysAuthMixServiceO")
    SysAuthMixService sysAuthMixService;

    @Override
    public boolean addUsers(List<SysUserDO> users) {
        String defaultPassword = sysConfigService.getValue("defaultPassword");
        int count = users.size() / 100;
        int index = 1;
        int maxIndex = getOne(new QueryWrapper<SysUserDO>().eq("is_delete", 0).orderByDesc("sort_index"))
                .getSortIndex();
        for (int i = 0; i < (count + 1); i++) {
            List<SysUserDO> addUsers = users.subList(100 * i, 100 * (i + 1) <= users.size() ? 100 * (i + 1) : users
                    .size());
            for (SysUserDO user : addUsers) {
                user.setUserType(3);
                user.setFirstLogin(0);
                String realName = user.getRealName();
                if (StringUtils.isNotBlank(realName)) {
                    user.setPinyin(PinyinUtils.toHanyuPinyin(realName));
                } else {
                    user.setRealName(user.getUserName());
                    user.setPinyin(PinyinUtils.toHanyuPinyin(user.getUserName()));
                }
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                user.setSortIndex(maxIndex + index);
                index++;
            }
            if (!saveBatch(addUsers)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }
        for (SysUserDO user : users) {
            try {
                String password = GMBaseUtil.pwToCipherPassword(user.getUserName(), defaultPassword);
                if (!update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                        .eq("user_name", user.getUserName())
                        .set("password", password)
                        .set("password_update_time", LocalDateTime.now()))) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            } catch (Exception e) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }
        return true;
    }

    @Override
    public Integer userRepeat(String userName) {
        Integer userCount = count(new QueryWrapper<SysUserDO>().eq("USER_NAME", userName));
        return userCount;
    }

    @Override
    public R saveUser(SysUserDO user) {
        // 判断用户密级不能为空
        if (user.getSecretLevel() == null || user.getSecretLevel() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("ARG_CAN_NOT_BE_EMPTY", "用户密级"));
        }

        if (StringUtils.isNotBlank(user.getPersonDocId())) {
            /**
             * 判断人员档案是否已关联用户
             */
            int count = this.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .eq("person_doc_id", user.getPersonDocId()));
            if (count > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PERSON_DOC_HAS_USED"));
            }
            //判断用户 人员是否在一个集团下
            BdPersonDocDO personDocDO = bdPersonDocService.getById(user.getPersonDocId());
            if (personDocDO != null) {
                if (!Objects.equals(user.getGroupId(), personDocDO.getGroupId())) {
                    throw new RException(InternationUtils.getInternationalMsg("USER_PERSON_NOT_IN_SAME_GROUP"));
                }
            }
        }

        if (user.getSortIndex() == null || user.getSortIndex() < 1) {
            user.setSortIndex(createSortIndexByUserName(user.getUserName()));
        }
        /**
         * https 传输明文密码
         */
        String password = user.getPassword();
        /**
         * 转化汉语拼音
         */
        String realName = user.getRealName();
        if (StringUtils.isNotBlank(realName)) {
            user.setPinyin(PinyinUtils.toHanyuPinyin(realName));
        } else {
            user.setRealName(user.getUserName());
        }
        user.setCreateTime(LocalDateTime.now());
        //user.setCreateUser(securityUtils.getCurrentUserId());
        user.setUpdateTime(LocalDateTime.now());
        // 判断用户类型是否为空
        Integer userType = user.getUserType();
        if (userType == null || userType == 0 || Objects.equals(userType, Integer.valueOf(0))) {
            user.setUserType(3);
        }
        validateUserName(user.getUserName(), user.getUserType());

        // 判断新添的用户是否是管理员
        if (!Objects.equals(3, user.getUserType())) {
            // 判断是否手动输入密码
            if (StringUtils.isNotBlank(password) && password != null) {
                user.setFirstLogin(0);
                sysUserLoginService.save(new SysUserLoginDO(user.getId(), user.getUserName(), 1));
            }
        }

        // 判断用户的人员ID是否为空格
        if (!this.save(user)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        /**
         * 密码加密
         */
        if (StringUtils.isNotBlank(password) && password != null) {

            saveUserToUpdatePassword(user.getUserName(), password);
           /* if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存用户", user, user
                    .getGroupId(), null)) {
            }*/
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        } else {
            // 新建用户密码为空 采用默认密码
            password = sysConfigService.getValue("defaultPassword");
            if (StringUtils.isNotBlank(password) && password != null) {
                saveUserToUpdatePassword(user.getUserName(), decryptPassword(user.getUserName(), password, true));
                /* if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存用户", user, user
                                .getGroupId(),
                        null)) {
                }*/
                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
            }

        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public List<String> getUserIdsByAuthId(String appId, String authId) {
        return sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>().eq("app_id", appId)
                .eq("auth_id", authId).select("user_id", "id")).stream()
                .map(SysAuthMixDO::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<SysUserDO> getUsersByAuthId(String appId, String authId) {
        List<String> userIds = getUserIdsByAuthId(appId, authId);
        if (CollectionUtils.isNotEmpty(userIds)) {
            return list(new QueryWrapper<SysUserDO>().in("id", userIds));
        }
        return null;
    }

    @Override
    public R listUsersBySecretLevelAndRoleOrOrg(Map<String, String> params) {
        int current = Integer.parseInt(params.get("current"));
        int size = Integer.parseInt(params.get("size"));
        int type = Integer.parseInt(params.get("type"));
        int secretLevel = -1;
        try {
            secretLevel = Integer.parseInt(params.get("secretLevel"));
        } catch (Exception e) {

        }
        //int secretLevel = securityUtils.getCurrentUser().getSecretLevel();
        String id = params.get("id");
        String searchStr = params.get("searchStr");
        Page<SysUserDO> page = new Page<>(current, size);
        IPage data = null;
        if (type == 0) {
            // 全部组织
            if (Objects.equals(id, "0")) {
                data = page(page, new QueryWrapper<SysUserDO>()
                        .eq("group_id", securityUtils.getCurrentGroupId())
                        .le(secretLevel > 0, "SECRET_LEVEL", secretLevel)
                        .and(StringUtils.isNotBlank(searchStr),
                                i -> i.like("staff_no", searchStr).or()
                                        .like("real_name", searchStr))
                        .eq("is_delete", 0)
                        .eq("user_type", 3)
                        .isNotNull("person_doc_id")
                        .orderByAsc("sort_index"));
            } else {
                OrgOrganizationDO org = orgOrganizationService.getById(id);
                // 业务单元
                if (org.getBusinessUnit() == 1) {
                    data = page(page, new QueryWrapper<SysUserDO>()
                            .eq("group_id", securityUtils.getCurrentGroupId())
                            .eq("organization_id", id)
                            .le(secretLevel > 0, "SECRET_LEVEL", secretLevel)
                            .and(StringUtils.isNotBlank(searchStr),
                                    i -> i.like("staff_no", searchStr).or()
                                            .like("real_name", searchStr))
                            .eq("is_delete", 0).eq("user_type", 3)
                            .isNotNull("person_doc_id")
                            .orderByAsc("sort_index"));
                } else {
                    List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq
                            ("department_id",
                                    id));
                    if (jobs != null && jobs.size() > 0) {
                        Set<String> peronIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors
                                .toSet());
                        if (peronIds.size() > 0) {
                            data = page(page, new QueryWrapper<SysUserDO>()
                                    .eq("group_id", securityUtils.getCurrentGroupId())
                                    .le(secretLevel > 0, "SECRET_LEVEL", secretLevel)
                                    .and(StringUtils.isNotBlank(searchStr),
                                            i -> i.like("staff_no", searchStr).or()
                                                    .like("real_name", searchStr))
                                    .eq("user_type", 3)
                                    .in("person_doc_id", peronIds)
                                    .eq("is_delete", 0)
                                    .orderByAsc("sort_index"));

                        }
                    }
                }
            }
        } else {
            List<SysUserRoleDO> userRoles = new ArrayList<>();
            // 全部角色
            if (Objects.equals(id, "0")) {
                List<SysGroupAppDO> apps = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().eq("group_id",
                        securityUtils.getCurrentGroupId()).select("id"));
                List<SysRoleDO> roles = new ArrayList<>(16);
                if (apps != null && apps.size() > 0) {
                    Set<String> appIds = apps.stream().map(SysGroupAppDO::getId).collect(Collectors.toSet());
                    roles = sysRoleService.list(new QueryWrapper<SysRoleDO>().in("app_id", appIds));
                }
                roles.addAll(sysRoleService.list(new QueryWrapper<SysRoleDO>().isNull("app_id")));
                if (CollectionUtils.isNotEmpty(roles)) {
                    userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                            .in("role_id", roles.stream().map(SysRoleDO::getId).collect(Collectors.toList())));
                }
            } else {
                userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>().eq("role_id",
                        id));
            }
            if (userRoles != null && userRoles.size() > 0) {
                Set<String> userIds = userRoles.stream().map(SysUserRoleDO::getUserId).collect(Collectors.toSet());
                if (userIds.size() > 0) {
                    data = page(page, new QueryWrapper<SysUserDO>()
                            .le(secretLevel > 0, "SECRET_LEVEL", secretLevel)
                            .and(StringUtils.isNotBlank(searchStr),
                                    i -> i.like("staff_no", searchStr).or()
                                            .like("real_name", searchStr))
                            .orderByAsc("sort_index")
                            .eq("user_type", 3)
                            .in("id", userIds)
                            .eq("is_delete", 0));
                }
            }
        }
        List<SysUserDO> users = new ArrayList<>();
        long total = 0L;
        if (data != null && data.getRecords() != null) {
            total = data.getTotal();
            users = fillUsersOrgAndDep(data.getRecords());
        }
        return R.ok().put("total", total).put("list", users);
    }

    @Override
    public R updateCurrentUserPassword(Map<String, String> map) {
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        String userName = map.get("userName");
        //String userName = securityUtils.getCurrentUserName();
        /**
         * 验证旧的密码是否正确
         */
//        if (!authenticate(userName, oldPassword)) {
//            return R.error(InternationUtils.getInternationalMsg("INPUT_CORRECT_OLD_PASSWORD"));
//        }
        if (StringUtils.isNotBlank(oldPassword) && StringUtils
                .isNotBlank(newPassword) && StringUtils.isNotBlank
                (userName)) {
            return updatePassword(userName, newPassword, false);
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "原密码、新密码、用户名’"));
    }

    @Override
    public List<SysUserDO> getUsersByMatchNameOrID(String str) {
        if (StringUtils.isNotBlank(str)) {
            List<SysUserDO> list = list(new QueryWrapper<SysUserDO>()
                    .orderByAsc("sort_index")
                    .and(i -> i.eq
                            ("user_name", str).or().eq
                            ("id", str).or().eq("real_name", str))
                    .eq("is_delete", 0));
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getAllUsersByOrgAndSon(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            OrgOrganizationDO rootOrg = orgOrganizationService.getById(organizationId);
            if (rootOrg != null) {
                String sortPath = rootOrg.getSortPath();
                if (StringUtils.isNotBlank(sortPath)) {
                    List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                            .select("id").eq("group_id", rootOrg.getGroupId()).eq("is_delete", 0).likeRight("sort_path",
                                    sortPath).eq
                                    ("IS_BUSINESS_UNIT",
                                            1).orderByAsc
                                    ("sort_path"));
                    List<String> orgIds = orgs.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(orgIds)) {
                        return list(new QueryWrapper<SysUserDO>().in("organization_id", orgIds)
                                .eq("is_delete", 0).eq("user_type", 3).orderByAsc("sort_index"));
                    }

                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getAllUsersByDepAndSon(String depId) {
        if (StringUtils.isNotBlank(depId)) {
            OrgDepartmentDO rootDep = orgDepartmentService.getById(depId);
            if (rootDep != null) {
                String sortPath = rootDep.getSortPath();
                if (StringUtils.isNotBlank(sortPath)) {
                    List<OrgDepartmentDO> orgs = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                            .select("id").eq("organization_id", rootDep.getOrganizationId()).eq("is_delete", 0)
                            .likeRight
                                    ("sort_path",
                                            sortPath).orderByAsc
                                    ("sort_path"));
                    List<String> depIds = orgs.stream().map(OrgDepartmentDO::getId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(depIds)) {
                        List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in
                                ("department_id", depIds));
                        if (jobs != null && jobs.size() > 0) {
                            Set<String> peronIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect
                                    (Collectors.toSet());
                            if (peronIds.size() > 0) {
                                List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().eq
                                        ("user_type", 3).in
                                        ("person_doc_id", peronIds).eq("is_delete", 0).orderByAsc("sort_index"));
                                return fillUsersOrgAndDep(list);
                            }
                        }
                    }

                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getSomeUsers(Map<String, Object> map) {
        String code = (String) map.get("code");
        List<String> ids = JsonUtils.castObject(map.get("ids"), List.class);
        List<BdPersonJobDO> jobs;
        if (!CollectionUtils.isEmpty(ids)) {
            switch (code) {
                case "all":
                    return list(new QueryWrapper<SysUserDO>().eq("user_type", 3)
                            .eq("is_delete", 0));
                case "dept":
                    //
                    jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in
                            ("department_id",
                                    ids).eq("is_main_job", 1).select("person_doc_id"));
                    if (!CollectionUtils.isEmpty(jobs)) {
                        Set<String> personIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors
                                .toSet());
                        return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().in("person_doc_id", personIds).eq
                                ("user_type", 3).eq("is_delete", 0).orderByAsc
                                ("sort_index")));
                    }
                    break;
                case "formDept":
                    break;
                case "formGroup":
                    break;
                case "formGroupComp":
                    break;
                case "formJob":
                    break;
                case "formPosition":
                    break;
                case "formUinit":
                    break;
                case "formUser":
                    break;
                case "group":
                    List<SysUserGroupUserDO> userGroupUsers = sysUserGroupUserService.list(new
                            QueryWrapper<SysUserGroupUserDO>().in("user_group_id", ids).select("user_id"));
                    if (!CollectionUtils.isEmpty(userGroupUsers)) {
                        Set<String> userIds = userGroupUsers.stream().map(SysUserGroupUserDO::getUserId).collect
                                (Collectors
                                        .toSet());
                        return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().in("id", userIds).eq
                                ("user_type", 3).eq("is_delete", 0).orderByAsc
                                ("sort_index")));
                    }
                    break;
                case "groupComp":
                    return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().in("group_id", ids).eq("user_type",
                            3).eq("is_delete", 0).orderByAsc
                            ("sort_index")));
                case "initDept":
                    break;
                case "initGroupComp":
                    break;
                case "initiator":
                    break;
                case "initUnit":
                    break;
                case "job":
                    jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in("job_id",
                            ids).select("person_doc_id"));
                    if (!CollectionUtils.isEmpty(jobs)) {
                        Set<String> personIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors
                                .toSet());
                        return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().eq("is_delete", 0).in
                                ("person_doc_id", personIds).eq
                                ("user_type", 3).orderByAsc
                                ("sort_index")));

                    }
                    break;
                case "position":
                    jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in("post_id",
                            ids).select("person_doc_id"));
                    if (!CollectionUtils.isEmpty(jobs)) {
                        Set<String> personIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors
                                .toSet());
                        return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().in("person_doc_id", personIds).eq
                                ("user_type", 3).eq("is_delete", 0).orderByAsc
                                ("sort_index")));

                    }
                    break;
                case "prev":
                    break;
                case "role":
                    if (ids != null && ids.size() > 0) {
                        List<String> lvIds = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                                .in("role_id", ids)).stream().map(SysUserRoleVDO::getLvId)
                                .collect(Collectors.toList());
                        if (lvIds != null && lvIds.size() > 0) {
                            List<String> userIds = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                                    .eq("is_latest", 1).in("id", lvIds)).stream().map(SysUserRoleLvDO::getUserId)
                                    .collect(Collectors.toList());
                            return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                                    .and(userIds == null || userIds.size() == 0, i -> i.eq("1", "2")).in("id",
                                            userIds).eq("user_type", 3)
                                    .orderByAsc
                                            ("sort_index")));
                        }
                    }
//                    List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>().in
//                            ("role_id",
//                                    ids).select("user_id"));
//                    if (CollectionUtils.isNotEmpty(userRoles)) {
//                        Set<String> userIds = userRoles.stream().map(SysUserRoleDO::getUserId).collect(Collectors
//                                .toSet());
//                        return list(new QueryWrapper<SysUserDO>().in("id", userIds).eq("user_type", 3)
//                                .orderByAsc
//                                        ("sort_index"));
//                    }
                    break;
                case "step":
                    break;
                case "unit":
                    return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().in("organization_id", ids)
                            .eq("is_delete", 0).eq("user_type", 3).orderByAsc("sort_index")));
                case "user":
                    if (ids.size() <= 500) {
                        return fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().eq
                                ("is_delete", 0).in
                                ("id", ids).eq
                                ("user_type", 3)
                                .orderByAsc
                                        ("sort_index")));
                    } else {
                        int count = ids.size() / 500;
                        List<SysUserDO> allUsers = new ArrayList<>();
                        for (int i = 1; i <= count; i++) {
                            allUsers.addAll(fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().eq
                                    ("is_delete", 0).in
                                    ("id", ids.subList(500 * (i - 1), 500 * i)).eq
                                    ("user_type", 3))));
                        }
                        allUsers.addAll(fillUsersOrgAndDep(list(new QueryWrapper<SysUserDO>().eq
                                ("is_delete", 0).in
                                ("id", ids.subList(500 * count, ids.size())).eq
                                ("user_type", 3))));
                        allUsers.sort(Comparator.comparing(SysUserDO::getSortIndex));
                        return allUsers;
                    }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByMatch(String str) {
        if (StringUtils.isNotBlank(str)) {
            //str为部门名称
            List<OrgDepartmentDO> list = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().like("name",
                    str).eq("is_delete", 0));
            Set<String> depIds;
            Set<String> userIds = new HashSet<>();
            if (list != null && list.size() > 0) {
                depIds = list.stream().map(OrgDepartmentDO::getId).collect(Collectors.toSet());
                List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in("department_id",
                        depIds));
                Set<String> peronIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors.toSet());
                if (peronIds.size() > 0) {
                    List<SysUserDO> users = list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index")
                            .eq("user_type", 3).in("person_doc_id",
                                    peronIds).eq("is_delete", 0));
                    if (users != null && users.size() > 0) {
                        userIds.addAll(users.stream().map(SysUserDO::getId).collect(Collectors.toSet()));
                    }
                }
            }

            //角色
            Set<String> roleIds = new HashSet<>();
            List<SysRoleDO> roles = sysRoleService.list(new QueryWrapper<SysRoleDO>().like("name", str));
            if (roles != null && roles.size() > 0) {
                roleIds.addAll(roles.stream().map(SysRoleDO::getId).collect(Collectors.toSet()));

                if (roleIds != null && roleIds.size() > 0) {
                    List<String> lvIds = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                            .in("role_id", roleIds)).stream().map(SysUserRoleVDO::getLvId)
                            .collect(Collectors.toList());
                    if (lvIds != null && lvIds.size() > 0) {
                        userIds = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                                .eq("is_latest", 1).in("id", lvIds)).stream().map(SysUserRoleLvDO::getUserId)
                                .collect(Collectors.toSet());
                    }
                }
//                List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>().in
// ("role_id",
//                        roleIds));
//                if (userRoles != null && userRoles.size() > 0) {
//                    userIds.addAll(userRoles.stream().map(SysUserRoleDO::getUserId).collect(Collectors.toSet()));
//                }
            }
            List<SysUserDO> users = new ArrayList<>();

            if (userIds.size() > 0) {
                users.addAll(list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq("user_type",
                        3).in("id", userIds).eq("is_delete", 0)));
            }

            users.addAll(list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq("user_type", 3)
                    .like("real_name", str).eq("is_delete", 0)));
            return fillUsersOrgAndDep(users);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUseAppUsers(String groupId, String orgId, String appId) {

        List<String> userIds = sysApiMixService.list(new QueryWrapper<SysApiMixDO>()
                .eq("app_id", appId)).stream().map(SysApiMixDO::getUserId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(userIds)) {
            return list(new QueryWrapper<SysUserDO>()
                    .in("id", userIds)
                    .eq("group_id", groupId).eq("organization_id", orgId).eq("is_delete", 0)
                    .eq("start_flag", "1").orderByAsc("sort_index"));
        }

        return new ArrayList<>(16);
    }

    @Override
    public List<SysUserDO> getUsersByRoleId(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>().eq("role_id",
                    roleId));
            if (userRoles != null && userRoles.size() > 0) {
                Set<String> userIds = userRoles.stream().map(SysUserRoleDO::getUserId).collect(Collectors.toSet());
                if (userIds.size() > 0) {
                    List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index")
                            .eq("user_type", 3).in("id", userIds).eq("is_delete", 0));

                    return fillUsersOrgAndDep(list);
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByDepId(String depId) {
        if (Objects.equals("0", depId)) {
            SysUserDO user = securityUtils.getCurrentUser();
            String orgId = user.getOrganizationId();
            //查询业务单元下所有的用户
            List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().eq("organization_id", orgId)
                    .eq("is_delete", 0).eq("user_type", 3).orderByAsc("sort_index"));
            ;
            return fillUsersOrgAndDep(list);
        } else if (StringUtils.isNotBlank(depId)) {
            // 判断depId是否为业务单元ID
            int count = orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>().eq("id", depId).eq
                    ("IS_BUSINESS_UNIT", 1));
            if (count > 0) {
                List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().eq("organization_id", depId)
                        .eq("is_delete", 0).eq("user_type", 3).orderByAsc("sort_index"));
                ;
                return fillUsersOrgAndDep(list);
            }

            return getUsersOnlyByDepId(depId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersOnlyByDepId(String depId) {
        if (StringUtils.isNotBlank(depId)) {
            List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq("is_main_job", 1)
                    .eq("department_id",
                            depId));
            if (jobs != null && jobs.size() > 0) {
                Set<String> peronIds = jobs.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors.toSet());
                if (peronIds.size() > 0) {
                    List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().eq("user_type", 3).in
                            ("person_doc_id", peronIds).eq("is_delete", 0).orderByAsc("sort_index"));
                    return fillUsersOrgAndDep(list);
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersBySecretLevel(Integer level) {
        SysUserDO user = securityUtils.getCurrentUser();
        List<SysUserDO> list = new ArrayList<>();
        // 判断是否是集团管理员
        if (Objects.equals(user.getUserType(), 1)) {
            List<String> groupIdS = sysUserAdminOrgService.getGroupsByUserId(user.getId());
            if (!CollectionUtils.isEmpty(groupIdS)) {
                list = list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq
                        ("user_type", 3).in("group_id", groupIdS).eq("secret_level", level).eq("is_delete", 0));
            }
        } else {
            String groupId = user.getGroupId();
            list = list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq
                    ("user_type", 3).eq("group_id", groupId).eq("secret_level", level).eq("is_delete", 0));
        }
        return list;
    }

    @Override
    public List<SysUserDO> getUsersBySecretLevel(Integer level, Integer le) {
        boolean leb = le == 1;
        SysUserDO user = securityUtils.getCurrentUser();
        List<SysUserDO> list = new ArrayList<>();
        // 判断是否是集团管理员
        if (Objects.equals(user.getUserType(), 1)) {
            List<String> groupIdS = sysUserAdminOrgService.getGroupsByUserId(user.getId());
            if (!CollectionUtils.isEmpty(groupIdS)) {
                list = list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq
                        ("user_type", 3).in("group_id", groupIdS).ge(!leb, "secret_level", level).le(leb,
                        "secret_level", level).eq("is_delete", 0));
            }
        } else {
            String groupId = user.getGroupId();
            list = list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq
                    ("user_type", 3).eq("group_id", groupId).ge(!leb, "secret_level", level).le(leb,
                    "secret_level", level).eq("is_delete", 0));
        }
        return fillUsersOrgAndDep(list);
    }

    @Override
    public SysUserDO infoById(String userId) {
        SysUserDO user = getById(userId);
        if (user == null) {
            return new SysUserDO();
        }
        // 人员档案
        String personId = user.getPersonDocId();
        if (StringUtils.isNotBlank(personId)) {
            BdPersonDocDO person = bdPersonDocService.getById(personId);
            user.setPersonDoc(person);
            BdPersonJobDO personJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    personId).eq("is_main_job", 1));
            user.setBdPersonJobDO(personJob);
            user.setDepartmentId(personJob.getDepartmentId());
        }
        return user;
    }

    @Override
    public SysUserDO infoByUserName(String userName) {
        SysUserDO user = getOne(new QueryWrapper<SysUserDO>().eq("is_delete", 0).eq("user_name",
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
        return user;
    }

    @Override
    public SysUserDO getCurrentUser() {
        SysUserDO user = JsonUtils.castObject(securityUtils.getCurrentUser(), SysUserDO.class);
        user.setEmail(host);
        // 人员档案
        String personId = user.getPersonDocId();
        if (StringUtils.isNotBlank(personId)) {
            BdPersonDocDO person = bdPersonDocService.getById(personId);
            user.setPersonDoc(person);
            BdPersonJobDO personJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    personId).eq("is_main_job", 1));
            user.setBdPersonJobDO(personJob);
        }
        return user;
    }

    @Override
    public List<SysUserDO> fillUserOrgAndGroup(List<String> userIds, List<SysUserDO> users) {
        if (!CollectionUtils.isEmpty(userIds) || !org.apache.commons.collections
                .CollectionUtils.isEmpty(users)) {
            List<SysUserDO> list = !CollectionUtils.isEmpty(users) ? users : list(new
                    QueryWrapper<SysUserDO>
                    ().in
                    ("id", userIds).eq
                    ("is_delete", 0).select("id", "group_id", "person_doc_id"));
            if (!CollectionUtils.isEmpty(list)) {
                List<String> orgIds = list.stream().map(SysUserDO::getGroupId).collect(Collectors.toList());
                orgIds.addAll(list.stream().map(SysUserDO::getOrganizationId).collect(Collectors
                        .toSet()));


                if (!CollectionUtils.isEmpty(orgIds)) {
                    List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                            ("is_delete", 0)
                            .select("id", "name")
                            .in("id", orgIds));
                    Map<String, String> idAndName = new HashMap<>();
                    orgs.forEach(org -> {
                        idAndName.put(org.getId(), org.getName());
                    });
                    list.forEach(user -> {
                        user.setOrganizationName(idAndName.get(user.getOrganizationId()));
                        user.setGroupName(idAndName.get(user.getGroupId()));
                    });
                }
            }
            return list;
        }
        return new ArrayList<>();
    }


    /**
     * 更新密码
     *
     * @param userName
     * @param password
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/4/22 15:30
     */
    @Override
    public R updatePassword(String userName, String password, boolean save) {

        if (StringUtils.isBlank(userName) || StringUtils.isBlank
                (password)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_USERNAME_OR_PASSWORD"));
        }

        SysUserDO user = this.getOne(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                .eq("user_name", userName));
        if (user == null) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NOT_EXIST"));
        }
        String userId = user.getId();

        SysPasswordPolicyDO passwordPolicy = sysPasswordPolicyService.getPasswordPolicy();

        /**
         * 校验密码长度
         * 非密、秘密至少8位
         * 机密至少10位
         */
        int length = passwordPolicy.getLen() > 8 ? passwordPolicy.getLen() : 8;
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
         * 当前密码使用n天后才可以修改
         */
        // 判断用户是否时候首次登陆
        // 非首次登陆才判断密码修改期限
        boolean firstLogin = user.getFirstLogin() == 1;
        if (!firstLogin) {
            int minTime = passwordPolicy.getUseMinTime();
            LocalDateTime updateTime = user.getPasswordUpdateTime();
            if (updateTime != null) {
                long minutes = LocalDateTimeUtils.getInterval(updateTime, LocalDateTime.now());
                if (minutes < minTime * 24 * 60) {
                    throw new RException(String.format(InternationUtils.getInternationalMsg
                            ("PASSOWRD_BEFORE_LAST_MODIFY_ERROR"), minTime));
                }
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

        /**
         * 不允许用户修改的密码与前几次相同
         */
        int changeNum = passwordPolicy.getChangeNum();
        /**
         * 与上次比较
         */
        int count = this.count(new QueryWrapper<SysUserDO>().eq("id", userId).eq("password", password));
        if (count > 0) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("NEW_PASSWORD_SAME_ERROR"),
                    changeNum));
        } else {
            /**
             * 与前几次比较
             */
            List<SysUserPasswordHistoryDO> list = sysUserPasswordHistoryService.list(new
                    QueryWrapper<SysUserPasswordHistoryDO>()
                    .eq("user_id", userId).orderByDesc("create_time"));
            int size = (changeNum - 1) > list.size() ? list.size() : (changeNum - 1);
            for (int i = 0; i < size; i++) {
                if (Objects.equals(list.get(i).getPassword(), password)) {
                    throw new RException(String.format(InternationUtils.getInternationalMsg
                            ("NEW_PASSWORD_SAME_ERROR"), changeNum));
                }
            }
        }

        /**
         * 保存老密码
         */
        if (baseMapper.saveOldPassword(UuidUtils.createUUID(), userId) != 1) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_OLD_PASSWORD_ERROR"));
        }
        boolean result = this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", userId).set("password", password).set("password_update_time", LocalDateTime.now()));
        if (!result) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        // 首次登录修改密码
        if (firstLogin) {
            // 取消 首次登录 状态
            // 0 代表 非首次登陆
            if (!save) {
                result = update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                        .eq("id", userId).set("is_first_login", 0));
                if (!result) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            }
            if (!save) {
                // 登陆页面修改密码 此时无token
                sysAuditLogService.saveLogWithUserName(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新密码", "更改" +
                                user.getRealName() + "密码!",
                        securityUtils
                                .getCurrentUser().getUserName(), securityUtils.getCurrentUser().getRealName());
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("updatePasswordTime",
                    LocalDateTime.now());
//            SysAuditLogDO sysAuditLogDO = new SysAuditLogDO();
//            sysAuditLogDO.setOpUsername(user.getUserName());
//            sysAuditLogDO.setOpName(user.getRealName());
//            sysAuditLogDO.setOpTime(LocalDateTime.now());
//            sysAuditLogDO.setOpContent("更新密码:" + user.getUserName());
//            sysAuditLogDO.setIpAddress(IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
//            String id = UuidUtils.createUUID();
//            sysAuditLogDO.setId(id);
//            try {
//                sysAuditLogDO.setSign(GMBaseUtil.getSign(user.getUserName() + id + IpUtils
//                        .getIpAddr(HttpContextUtils.getHttpServletRequest())));
//            } catch (Exception e) {
//                throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//            }
//            if (sysAuditLogService.save(sysAuditLogDO)) {
//                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("updatePasswordTime",
//                        LocalDateTime.now());
//            }
        } else {
            //return R.ok();
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新密码", "更新密码:" + user
                    .getUserName(), user.getGroupId(), null)) {
                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("updatePasswordTime",
                        LocalDateTime.now());
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public List<SysUserDO> fillUsersOrgAndDep(List<SysUserDO> users) {
        if (!CollectionUtils.isEmpty(users)) {
            Map<String, OrgOrganizationDO> personIdDep = new HashMap<>();
            Map<String, String> orgMap = new HashMap<>();
            Set<String> bdPersonIds = users.stream().map(SysUserDO::getPersonDocId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(bdPersonIds)) {
                List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in
                        ("person_doc_id", bdPersonIds).select("department_id", "person_doc_id").eq
                        ("is_main_job",
                                1));
                if (!CollectionUtils.isEmpty(jobs)) {
                    Set<String> depIds = jobs.stream().map(BdPersonJobDO::getDepartmentId).collect(Collectors.toSet());
                    depIds.addAll(users.stream().map(SysUserDO::getOrganizationId).collect(Collectors.toSet()));
                    if (!CollectionUtils.isEmpty(depIds)) {
                        List<OrgOrganizationDO> orgs = orgOrganizationService.list(new
                                QueryWrapper<OrgOrganizationDO>().in
                                ("id", depIds).eq("is_delete", 0).select("id", "name", "code", "sort_path"));
                        jobs.forEach(job -> {
                            String personId = job.getPersonDocId();
                            String depId = job.getDepartmentId();
                            String orgId = job.getOrganizationId();
                            OrgOrganizationDO dep = orgs.stream().filter(tdep -> Objects.equals(tdep.getId(), depId))
                                    .findFirst
                                            ().orElse
                                            (null);
                            if (dep != null) {
                                personIdDep.put(personId, dep);
                            }
                            OrgOrganizationDO org = orgs.stream().filter(tdep -> Objects.equals(tdep.getId(), orgId))
                                    .findFirst
                                            ().orElse
                                            (null);
                            if (org != null) {
                                orgMap.put(org.getId(), org.getName());
                            }
                        });

                    }
                }
            }
            if (personIdDep.size() > 0 || orgMap.size() > 0) {
                users.forEach(user -> {
                    String personId = user.getPersonDocId();
                    if (StringUtils.isNotBlank(personId)) {
                        OrgOrganizationDO dep = personIdDep.get(personId);
                        if (dep != null) {
                            user.setDepartmentId(dep.getId());
                            user.setDepartmentName(dep.getName());
                            user.setDepartmentCode(dep.getCode());
                            user.setDepartmentSortPath(dep.getSortPath());
                        }
                        user.setOrganizationName(orgMap.get(user.getOrganizationId()));
                    }
                });
            }
        }
        return users;
    }

    @Override
    public R login(String userName, String password) {
        SysUserDO user = this.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", userName).eq("is_delete", 0));
        if (user == null) {
            return R.error(InternationUtils.getInternationalMsg("USER_NOT_EXIST"));
        }

        if (user.getSecretLevel() == null) {
            return R.error(InternationUtils.getInternationalMsg("USER_SECRET_LEVEL_IS_NULL"));
        }

        String userId = user.getId();

        // 判断用户是否启用
        if (Objects.equals("0", user.getStartFlag())) {
            return R.error(InternationUtils.getInternationalMsg("USER_NOT_USE"));
        }
        // 校验ip地址
        String ipAddress = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        boolean checkFlg = false;
        if (Objects.equals(3, user.getUserType())) {
            // 普通用户
            if (Objects.equals(1, user.getIpBind())) {
                checkFlg = true;
            }
        } else {
            // 管理员
            String adminIpAddressCheck = sysConfigService.getValue("adminIpAddressCheck");
            if (adminIpAddressCheck != null) {
                if (Objects.equals("open", adminIpAddressCheck)) {
                    checkFlg = true;
                }
            } else {
                checkFlg = false;
            }
        }
        if (checkFlg) {

            List<String> ipAddressList = new ArrayList<>(16);
            if (StringUtils.isNotBlank(user.getIpAddress())) {
                if (user.getIpAddress().contains(";")) {
                    ipAddressList = Arrays.asList(user.getIpAddress().split(";"));
                } else {
                    ipAddressList.add(user.getIpAddress());
                }
            }
            System.out.println("-------------------" + ipAddressList);

            if (!ipAddressList.contains(ipAddress)) {
                return R.error(InternationUtils.getInternationalMsg("IP_NOT_MATCH"));
            }
        }

        /**
         * 判断账户是否被锁定
         */
        LocalDateTime unlockTime = user.getUnlockTime();
        if (unlockTime != null) {
            long minutes = LocalDateTimeUtils.getInterval(LocalDateTime.now(), unlockTime);
            if (minutes > 0) {
                return R.error(String.format(InternationUtils.getInternationalMsg("USER_LOCK")
                        , minutes));
            }
        }

        SysPasswordPolicyDO passwordPolicy = sysPasswordPolicyService.getPasswordPolicy();

        /**
         * 涉密系统强制要求：登录失败n次后( n ≤ 5)，锁定账户m分钟(m ≥ 15)；系统管理员可以提前解锁账户；
         *
         * 非密系统，登录失败n次后( n ≤ 5)，可以选择不锁定账户，改为要求输入图形验证码的方式；
         *
         * 登录失败次数过多导致账户锁定后，应立即重置失败次数；
         */

        /**
         * 策略的最大失败次数
         */
        int failureTimes = passwordPolicy.getFailureTimes();
        /**
         * 登录失败计数应在n分钟后重置为0(n ≥ 15)
         */
        int maxResetFailureTimesTime = passwordPolicy.getResetFailureTimesTime();
        /**
         * 获取最新一次登录时间
         */
        SysUserLoginDO latestLogin = sysUserLoginService.getLatestLogin(userId);
        if (latestLogin != null) {
            LocalDateTime latestLoginTime = latestLogin.getLoginTime();
            if (latestLoginTime != null) {
                long interval = LocalDateTimeUtils.getInterval(latestLoginTime, LocalDateTime.now());
                // 登录失败计数应在n分钟后重置为0
                if (interval >= maxResetFailureTimesTime) {
                    user.setFailLoginTimes(0);
                }
            }
        }
        /**
         * 用户实际登录失败次数
         */
        Integer failLoginTimes = user.getFailLoginTimes();

        if (!authenticate(userName, password)) {
            failLoginTimes = failLoginTimes == null ? 1 : failLoginTimes + 1;
            /**
             * 更新密码失败次数
             */
            this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                    .eq("id", userId).set("fail_login_times", failLoginTimes));
            /**
             * 保存登录日志
             */
            sysUserLoginService.save(new SysUserLoginDO(userId, userName, 0));
            sysAuditLogService.saveLoginLog(user.getRealName()+"登录失败!", userName, user.getRealName());

            if (failLoginTimes >= failureTimes) {
                /**
                 * 锁定账户
                 */
                int lockMintues = passwordPolicy.getLockMinutes();
                /**
                 * 锁定时长小于0分钟 则要求输入图形验证码
                 */
                if (lockMintues <= 0) {
                    /**
                     * 验证码
                     */
                    return R.error(InternationUtils.getInternationalMsg("USER_INPUT_CAPTCHA"));
                } else {
                    /**
                     * 锁定账户lockMintues 分钟
                     */
                    unlockTime = LocalDateTime.now().plusMinutes(lockMintues);
                    user.setUnlockTime(unlockTime);
                    /**
                     * 重置失败次数
                     */
                    user.setFailLoginTimes(0);
                    this.updateById(user);
                    return R.error(String.format(InternationUtils.getInternationalMsg("USER_LOCK"), lockMintues));
                }
            } else {
                return R.error(String.format(InternationUtils.getInternationalMsg("PASSWORD_UNCORRECT"), failureTimes
                        - failLoginTimes));
            }
        }


        /**
         * 登陆成功解锁用户
         */
        user.setUnlockTime(LocalDateTime.now().minusMinutes(100));
        /**
         * 重置失败次数
         */
        user.setFailLoginTimes(0);
        if (!updateById(user)) {
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }

        String token = null;
        try {
            token = SM4Util.getToken(userId, userName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_GET_TOKEN"));
        }


        /**
         * 密码最长使用期限：秘密级系统30天后必须更换密码，机密级系统7天后必须更换密码；不更换密码就不允许登录
         */
        int maxTimes = passwordPolicy.getUseMaxTime();
        long minutes = LocalDateTimeUtils.getInterval(user.getPasswordUpdateTime(), LocalDateTime.now());
        if (minutes >= maxTimes * 24 * 60) {
            return cacheUserInfos(token, userId, false, HttpCode.NEED_MODIFY_PASSWORD, String.format(InternationUtils
                    .getInternationalMsg
                            ("PASSWORD_USE_EXCEED"), maxTimes));
        }

        /**
         * 系统管理员可以重置用户密码，重置后，用户登录必须更改密码；
         *
         * 新建用户首次登录必须更改密码；
         *
         * 被重置过密码的用户提示需不同
         */
        int logCount = sysUserLoginService.count(new QueryWrapper<SysUserLoginDO>().eq("user_id", userId));
        boolean firstLogin = user.getFirstLogin() == 1;
        if (firstLogin) {
            if (logCount > 0) {
                return cacheUserInfos(token, userId, false, HttpCode.NEED_MODIFY_PASSWORD, InternationUtils
                        .getInternationalMsg
                                ("PASSWORD_HAS_RESET"));
            }
            return cacheUserInfos(token, userId, false, HttpCode.NEED_MODIFY_PASSWORD, InternationUtils
                    .getInternationalMsg
                            ("USER_FIRST_LOGIN"));
        }

        /**
         * 登陆成功 返回用户  Token
         * 密码等敏感信息不应该返回
         */

        sysUserLoginService.save(new SysUserLoginDO(userId, userName, 1));
        sysAuditLogService.saveLoginLog("登录成功!", userName, user.getRealName());
        /**
         * 加载用户信息、角色信息、权限列表到缓存数据库
         */
        return cacheUserInfos(token, userId, true, 0, null);
    }

    @Override
    public String getTokenAfterLogin(String userKey) {
        SysUserDO user = getOne(new QueryWrapper<SysUserDO>().eq("id", userKey).or().eq("user_name", userKey).or
                ().eq("staff_no", userKey));
        if (user != null) {
            if (cacheUtil.hasKey(user.getId())) {
                String token = cacheUtil.get(user.getId());
                if (StringUtils.isNotBlank(token) && cacheUtil.hasKey(token)) {
                    return token;
                }
            }
            R r = getUserInfoAfterLogin(user);
            if (r != null) {
                String token = (String) r.get("token");
                if (StringUtils.isNotBlank(token)) {
                    return token;
                }
            }

        }
        return null;
    }

    @Override
    public R getUserInfoAfterLogin(String userKey) {
        if (StringUtils.isNotBlank(userKey)) {
            SysUserDO user = getOne(new QueryWrapper<SysUserDO>().eq("id", userKey).or().eq("user_name", userKey).or
                    ().eq("staff_no", userKey));
            return getUserInfoAfterLogin(user);
        }
        return null;
    }

    public R getUserInfoAfterLogin(SysUserDO user) {
        if (user != null) {
            String token = null;
            String userId = user.getId();
            String userName = user.getUserName();
            try {
                token = SM4Util.getToken(userId, userName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RException(InternationUtils.getInternationalMsg("ERROR_GET_TOKEN"));
            }
            sysUserLoginService.save(new SysUserLoginDO(userId, userName, 1));
            sysAuditLogService.saveLoginLog("登录成功!", userName, user.getRealName());
            return cacheUserInfos(token, userId, true, 0, null);
        }
        return null;
    }

    /**
     * 缓存用户信息  角色信息等
     *
     * @param token
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/5/17 10:55
     */
    @Override
    public R cacheUserInfos(String token, String userId, boolean ok, int code, String msg) {
        String defaultTokenExpireDay = cacheUtil.get("defaultTokenExpireDay");
        // defaultTokenExpireDay 单位：分钟
        int seconds = Integer.parseInt(defaultTokenExpireDay) * 60;
        SysUserDO user = this.getById(userId);
        cacheUtil.hset(token, "user", JSONObject.toJSON(user), seconds);
        cacheUtil.set(userId, token, CacheUtil.NOT_EXPIRE);
        cacheUtil.set(token + "userid", userId, seconds);

        // 缓存登录信息 用作在线人数
        if (user.getUserType() == 3) {
            SysOnlineUserVO sysOnlineUserVO = new SysOnlineUserVO();
            sysOnlineUserVO.setUserId(userId);
            sysOnlineUserVO.setUserName(user.getUserName());
            sysOnlineUserVO.setRealName(user.getRealName());
            sysOnlineUserVO.setLoginTime(LocalDateTime.now());
            sysOnlineUserVO.setGroupId(user.getGroupId());
            sysOnlineUserVO.setLoginIP(securityUtils.getRealIp());
            cacheUtil.set(token + "aceuserid", JSONObject.toJSON(sysOnlineUserVO), seconds);
        }
        /**
         * 主职部门
         */
        if (StringUtils.isNotBlank(user.getPersonDocId())) {
            BdPersonDocDO person = bdPersonDocService.getById(user.getPersonDocId());
            if (person != null) {
                BdPersonJobDO mainJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                        person.getId()).eq("is_main_job", 1));
                if (mainJob != null) {
                    OrgDepartmentDO dep = orgDepartmentService.getById(mainJob.getDepartmentId());
                    cacheUtil.hset(token, "department", JSONObject.toJSON(dep), seconds);
                }
            }
        }
        /**
         * 用户角色ID列表
         */
        List<String> roleIds = new ArrayList<>();
        List<SysUserRoleLvDO> sysUserRoleLvDOS = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("user_id", userId).eq("is_latest", 1));
        if (CollectionUtils.isNotEmpty(sysUserRoleLvDOS)) {
            List<String> lvIds = sysUserRoleLvDOS.stream().map(SysUserRoleLvDO::getId).collect(Collectors.toList());
            List<SysUserRoleVDO> sysUserRoleVDOS = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                    .in("lv_id", lvIds));
            if (CollectionUtils.isNotEmpty(sysUserRoleVDOS)) {
                roleIds.addAll(sysUserRoleVDOS.stream().map(SysUserRoleVDO::getRoleId).collect(Collectors.toList
                        ()));
            }
        }

        cacheUtil.hset(token, "roleIds", JSONObject.toJSON(roleIds), seconds);

        // 判断用户是否是租户 集团 或 应用管理员
        String[] roleIdStrs = {"admin", "sec", "auditor", "groupadmin", "groupsec", "groupauditor","groupsuperadmin", "appadmin",
                "appsec",
                "appauditor","appsuperadmin","businessadmin"};
        boolean isAdmin = roleIds.stream().anyMatch(roleId -> Arrays.asList(roleIdStrs).contains(roleId));

        /**
         * 统计用户可管理集团的数量
         */
        List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(user.getId());
        cacheUtil.hset(token, "groups", JSONObject.toJSON(groups), seconds);
        cacheUtil.hset(token, "isAdmin", isAdmin ? 1 : 0, seconds);
        /**
         * 用户权限标识列表  API
         */
        List<String> apis = sysApiMixService.list(new QueryWrapper<SysApiMixDO>().eq("user_id", userId)).stream().map
                (SysApiMixDO::getApiId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(apis)) {
            cacheUtil.hset(token, "apis", JSONObject.toJSON(apis), seconds);
        }
        // 普通用户没有资格登录平台
        List<SysGroupAppDO> list;
        if (isAdmin) {
            list = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().eq("group_id", user
                    .getGroupId()));
        } else {
            list = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().eq("group_id", user
                    .getGroupId()).ne("id", "platform"));
        }
        SysGroupAppDO mainApp = sysGroupAppService.getOne(new QueryWrapper<SysGroupAppDO>().eq("group_id",
                user.getGroupId()).eq("is_main_app", 1));

        if (ok) {
            return R.ok().put("token", token).put("isAdmin", isAdmin).put("apps", list).put("mainApp", mainApp);
        } else {
            return R.error(code, msg).put("token", token).put("isAdmin", isAdmin).put("apps", list).put("mainApp",
                    mainApp);
        }
    }

    /**
     * 验证密码
     *
     * @param userName
     * @param password
     * @return
     * @author yansiyang
     * @date 2019/5/17 10:55
     */
    @Override
    public boolean authenticate(String userName, String password) {
        try {
            password = GMBaseUtil.pwToCipherPassword(userName, password);
        } catch (UnsupportedEncodingException e) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }
        int count = count(new QueryWrapper<SysUserDO>().eq("user_name", userName).eq("password", password));
        if (count == 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据登录名生成排序号
     *
     * @param userName
     * @return
     * @author xulei
     * @date 2020/6/30 10:17
     */
    public Integer createSortIndexByUserName(String userName) {
        char[] chars = userName.toCharArray();
        Integer sortIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            int ascInt = (int) chars[i] - 48;
            int baseInt = (int) Math.pow(2, i + 5);
            sortIndex += ascInt * baseInt;
        }
        while (true) {
            int count = this.count(new QueryWrapper<SysUserDO>().eq
                    ("sort_index", sortIndex));
            if (count == 0) {
                break;
            } else {
                sortIndex += 1;
            }
        }
        return sortIndex;
    }

    /**
     * 校验用户名 只可以包含数字 大小写字符 下划线
     * 用户名的长度在3-16之间
     *
     * @param userName
     * @param userType
     * @return
     * @author xulei
     * @date 2020/6/30 10:17
     */
    public void validateUserName(String userName, Integer userType) {
        if (userName == null) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_LENGTH_ERROR"));
        }
        if (Objects.equals(3, userType)) {
            if (userName.length() < 3 || userName.length() > 21) {
                throw new RException(InternationUtils.getInternationalMsg("USER_NAME_LENGTH_ERROR"));
            }
        }

        String regex = "^[a-z0-9A-Z_]+$";
        if (!userName.matches(regex)) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_VALID"));
        }
        int count = this.count(new QueryWrapper<SysUserDO>().eq("user_name", userName).eq("is_delete", 0));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_EXIST"));
        }
    }

    /**
     * 保存用户的时候 调用的更新密码   避免首次登录相关问题
     *
     * @param userName
     * @param password
     * @return com.csicit.ace.common.utils.server.R
     * @author xulei
     * @date 2020/6/30 10:17
     */
    public R saveUserToUpdatePassword(String userName, String password) {
        return updatePassword(userName, password, true);
    }

    private String decryptPassword(String userName, String cipherText, boolean save) {
        String password = "";
        try {
            password = GMBaseUtil.decryptString(cipherText);
            // 默认密码初始化时被加密 解密后第7位及之后为正确的默认密码
            password = password.substring(6);
            if (save) {
                return password;
            }
            password = GMBaseUtil.pwToCipherPassword(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }
        return password;
    }
}
