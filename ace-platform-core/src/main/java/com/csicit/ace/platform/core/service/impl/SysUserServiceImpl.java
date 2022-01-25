package com.csicit.ace.platform.core.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.SysOnlineUserVO;
import com.csicit.ace.common.utils.*;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.cipher.SM4Util;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.AceDBHelperMapper;
import com.csicit.ace.data.persistent.mapper.SysUserMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.UuidUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
@Service("sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    @Autowired
    CacheUtil cacheUtil;
    @Autowired
    SysPasswordPolicyService sysPasswordPolicyService;
    @Autowired
    SysUserLoginService sysUserLoginService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysAuthMixService sysAuthMixService;
    @Autowired
    SysConfigService sysConfigService;
    @Autowired
    SysUserPasswordHistoryService sysUserPasswordHistoryService;
    @Autowired
    BdPersonDocService bdPersonDocService;
    @Autowired
    SysAuditLogService sysAuditLogService;
    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;
    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;
    @Autowired
    SysAuthScopeUserGroupService sysAuthScopeUserGroupService;
    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysAuthService sysAuthService;
    @Autowired
    OrgOrganizationService orgOrganizationService;
    @Autowired
    BdPersonJobService bdPersonJobService;
    @Autowired
    OrgDepartmentService orgDepartmentService;
    @Autowired
    SysUserRoleLvService sysUserRoleLvService;
    @Autowired
    SysUserRoleVService sysUserRoleVService;
    @Autowired
    OrgGroupService orgGroupService;
    @Autowired
    SysGroupAppService sysGroupAppService;
    @Autowired
    SysApiMixService sysApiMixService;
    @Autowired
    AceDBHelperMapper aceDBHelperMapper;

    @Value("${ace.config.openBindUserIp:false}")
    private Boolean openBindUserIp;

    @Override
    public List<String> getUserIdsByMainDepId(String depId) {
        return getUsersByMainDepId(depId).stream().map(SysUserDO::getId)
                .filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.toList());
    }

    @Override
    public List<SysUserDO> getUsersByMainDepIds(List<String> depIds) {
        if (CollectionUtils.isNotEmpty(depIds)) {
            List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
                    .in("department_id", depIds).select("id", "person_doc_id"));
            if (CollectionUtils.isNotEmpty(jobs)) {
                List<String> personDocIds = jobs.stream().map(BdPersonJobDO::getPersonDocId)
                        .filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(personDocIds)) {
                    List<SysUserDO> userDOS = list(new QueryWrapper<SysUserDO>()
                            .eq("is_delete", 0).in("person_doc_id", personDocIds));
                    return userDOS;
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByMainDepId(String depId) {
        if (StringUtils.isNotBlank(depId)) {
            List<String> depIds = new ArrayList<>();
            depIds.add(depId);
            return getUsersByMainDepIds(depIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> fillUserOrgAndGroup(List<String> userIds, List<SysUserDO> users) {
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIds) || org.apache.commons.collections
                .CollectionUtils.isNotEmpty(users)) {
            List<SysUserDO> list = org.apache.commons.collections.CollectionUtils.isNotEmpty(users) ? users : list
                    (new QueryWrapper<SysUserDO>
                            ().in
                            ("id", userIds).eq
                            ("is_delete", 0).select("id", "group_id", "person_doc_id", "organization_id"));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                List<String> orgIds = list.stream().map(SysUserDO::getGroupId).collect(Collectors.toList());
                orgIds.addAll(list.stream().map(SysUserDO::getOrganizationId).collect(Collectors
                        .toSet()));
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgIds)) {
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
        return null;
    }
//
//    @Override
//    public List<String> getUserIdsByOrgIds(List<String> orgIds) {
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgIds)) {
//            List<BdPersonDocDO> personDocDOS = bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>().in
//                    ("organization_id", orgIds).eq("is_delete", 0));
//            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(personDocDOS)) {
//
//                List<String> personDocIds = personDocDOS.stream().map(BdPersonDocDO::getId).collect(Collectors.toList
//                        ());
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(personDocIds)) {
//                    List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().in("person_doc_id", personDocIds).eq
//                            ("is_delete", 0).select("id").eq("user_type", 3)
//                            .orderByAsc("sort_index"));
//                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
//                        return list.stream().map(SysUserDO::getId).collect(Collectors.toList());
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<SysUserDO> getUsersByOrgIds(List<String> orgIds, boolean addOrgId) {
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgIds)) {
//            List<BdPersonDocDO> personDocDOS = bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>().in
//                    ("organization_id", orgIds).eq("is_delete", 0));
//            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(personDocDOS)) {
//
//                List<String> personDocIds = personDocDOS.stream().map(BdPersonDocDO::getId).collect(Collectors.toList
//                        ());
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(personDocIds)) {
//                    List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().in("person_doc_id", personDocIds).eq
//                            ("is_delete", 0).eq("user_type", 3)
//                            .orderByAsc("sort_index"));
//                    // 是否添加业务单元主键
//                    if (addOrgId) {
//                        Map<String, String> personDocIdAndOrgId = new HashMap<>();
//                        personDocDOS.stream().forEach(personDocDO -> {
//                            personDocIdAndOrgId.put(personDocDO.getId(), personDocDO.getOrganizationId());
//                        });
//                        list.stream().forEach(user -> {
//                            user.setOrganizationId(personDocIdAndOrgId.get(user.getPersonDocId()));
//                        });
//                    }
//                    return list;
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<String> getUserIdsByOrgId(String orgId) {
//        if (StringUtils.isNotBlank(orgId)) {
//            List<String> orgIds = new ArrayList<>();
//            orgIds.add(orgId);
//            return getUserIdsByOrgIds(orgIds);
//        }
//        return null;
//    }
//
//    @Override
//    public List<SysUserDO> getUsersByOrgId(String orgId) {
//        if (StringUtils.isNotBlank(orgId)) {
//            List<String> orgIds = new ArrayList<>();
//            orgIds.add(orgId);
//            return getUsersByOrgIds(orgIds, true);
//        }
//        return null;
//    }

    /**
     * 逻辑删除用户
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/10/23 16:30
     */
    @Override
    public boolean removeUsers(List<String> ids) {
        if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            return false;
        }
        if (update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in("id", ids)
                .set("sort_index", -1)
                .setSql("person_doc_id=null")
                .setSql("staff_no=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),staff_no)")
                .setSql("user_name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),user_name)")
                .set("is_delete", 1).isNotNull("staff_no")) || update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in
                ("id", ids)
                .set("sort_index", -1)
                .setSql("person_doc_id=null")
                .setSql("staff_no=CONCAT(CONCAT('del-', SUBSTR(id,1,8)),staff_no)")
                .setSql("user_name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),user_name)")
                .set("is_delete", 1).isNull("staff_no"))) {
            return true;

        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 根据登录名生成排序号
     *
     * @param userName
     * @return
     * @author yansiyang
     * @date 2019/7/9 8:23
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
     * @author yansiyang
     * @date 2019/7/9 8:27
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

        String regex = "^[a-z0-9A-Z_\\-]+$";
        if (!userName.matches(regex)) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_VALID"));
        }
        int count = this.count(new QueryWrapper<SysUserDO>().eq("user_name", userName).eq("is_delete", 0));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_EXIST"));
        }
    }

    @Override
    public R resetDefaultPassword(Map<String, String> map) {
        if (Objects.equals(securityUtils.getCurrentUserName(), "admin")) {
            String defaultPassword = map.get("defaultPassword");
            if (StringUtils.isNotBlank(defaultPassword)) {
                // 加密密码
                String cipherDefaultPassword;
                try {
                    cipherDefaultPassword = GMBaseUtil.encryptString("Cipher" + defaultPassword);
                } catch (Exception e) {
                    return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                }
                SysConfigDO config = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                        "defaultPassword"));

                if (config == null) {
                    throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
                }
                config.setValue(cipherDefaultPassword);
                config.setUpdateTime(LocalDateTime.now());
                if (sysConfigService.updateById(config)) {
                    // 储存到redis
                    cacheUtil.set("defaultPassword", cipherDefaultPassword, CacheUtil.NOT_EXPIRE);
                    if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改默认密码",
                            "修改默认密码：" + config.getName(),
                            securityUtils.getCurrentGroupId(), null)) {
                        return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
                    } else {
                        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                    }
                }
            }
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "password"));
        }
        throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
    }

    @Override
    public R resetUserPassword(String userName) {
        // 判断用户有没有权限
        SysUserDO user = getOne(new QueryWrapper<SysUserDO>().eq("user_name", userName).eq("is_delete", 0));
        if (user == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        } else {
            SysUserDO currentUser = getById(securityUtils.getCurrentUserId());
            if (!Objects.equals("devadmin", currentUser.getUserName())) {
                if (currentUser.getUserType() == 3 || (user.getUserType() <= currentUser.getUserType() && currentUser
                        .getUserType() != 11)) {
                    throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
                }
            }
        }

        String defaultPassword = sysConfigService.getValue("defaultPassword");
        if (StringUtils.isBlank(defaultPassword)) {
            return R.error(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
        /**
         * 加密保存密码
         */
        String password = decryptPassword(userName, defaultPassword, false);

        if (update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("user_name", userName).ne("user_type", 0).set("unlock_time", LocalDateTime.now().minusMinutes(10)
                ).set("is_first_login", 1).set("fail_login_times", 0).set("password", password).set
                        ("password_update_time", LocalDateTime.now())
        )) {
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "重置密码", "用户【" + user
                    .getRealName() + "】重置密码", user
                    .getGroupId(), null)) {
                return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
            }
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    @Override
    public R persondoc(Map<String, String> map) {
        String personDocId = map.get("personDocId");
        String userId = map.get("userId");
        if (StringUtils.isBlank(personDocId) || StringUtils.isBlank(userId)) {
            return R.error(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        /**
         * 判断人员档案是否已关联用户
         */
        int count = this.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                .eq("person_doc_id", personDocId));
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("PERSON_DOC_HAS_USED"));
        }
        SysUserDO user = getById(userId);
        if (user == null) {
            return R.error(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        //判断用户 人员是否在一个集团下
        BdPersonDocDO personDocDO = bdPersonDocService.getById(personDocId);
        if (personDocDO != null) {
            if (!Objects.equals(user.getGroupId(), personDocDO.getGroupId())) {
                throw new RException(InternationUtils.getInternationalMsg("USER_PERSON_NOT_IN_SAME_GROUP"));
            }
        }
        /**
         * 查询人员身份类型  主键
         */
        BdPersonDocDO bdPersonDocDO = bdPersonDocService.getById(personDocId);
        if (bdPersonDocDO == null) {
            return R.error(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        if (this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", userId).set("person_doc_id", personDocId)
                .set("base_doc_type", bdPersonDocDO.getPersonIdTypeId()))) {
            user = getById(userId);
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "将用户绑定到人员档案:", "用户：" +
                            JSONObject.toJSONString(user) + "," +
                            "人员：" +
                            JSONObject.toJSONString(personDocDO),
                    bdPersonDocDO.getGroupId(), null)) {
                return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));

    }

    @Override
    public R updateUser(SysUserDO user) {
        /**
         * 转化汉语拼音
         */
        String realName = user.getRealName();
        if (StringUtils.isNotBlank(realName)) {
            user.setPinyin(PinyinUtils.toHanyuPinyin(realName));
        }
        SysUserDO oldUser = getById(user.getId());
        if (!Objects.equals(user.getUserName(), oldUser.getUserName())) {
            throw new RException(InternationUtils.getInternationalMsg("CAN_NOT_EDIT_USERNAME"));
        }
        if (StringUtils.isNotBlank(user.getPersonDocId())) {
            /**
             * 判断人员档案是否已关联用户
             */
            int count = this.count(new QueryWrapper<SysUserDO>().ne("id", user.getId()).eq("is_delete", 0)
                    .eq("person_doc_id", user.getPersonDocId()));
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("PERSON_DOC_HAS_USED"));
            }
            //判断用户 人员是否在一个集团下
            BdPersonDocDO personDocDO = bdPersonDocService.getById(user.getPersonDocId());
            if (personDocDO != null) {
                if (!Objects.equals(user.getGroupId(), personDocDO.getGroupId())) {
                    throw new RException(InternationUtils.getInternationalMsg("USER_PERSON_NOT_IN_SAME_GROUP"));
                }
            }
            // 更新用户-角色-部门关联关系
            if (Objects.equals(user.getPersonDocId(), oldUser.getPersonDocId())) {
                sysRoleService.updateRoleAndDepForUser(user.getId(), bdPersonJobService.getOne(new
                                QueryWrapper<BdPersonJobDO>()
                                .eq("person_doc_id", user.getPersonDocId()).eq("is_main_job", 1)).getDepartmentId(),
                        bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>()
                                .eq("person_doc_id", oldUser.getPersonDocId()).eq("is_main_job", 1)).getDepartmentId());
            }
        }
        user.setUpdateTime(LocalDateTime.now());
        if (this.updateById(user)) {
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新用户", "更新用户：" + user
                            .getRealName(), user
                            .getGroupId(),
                    null)) {
                // 用户自己更新信息
                if (Objects.equals(user.getId(), securityUtils.getCurrentUserId())) {
                    cacheUtil.hset(securityUtils.getToken(), "user", JSONObject.toJSON(getById(user.getId())));
                }
                return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R deleteUser(String[] userIds) {
        List<String> userIdList = Arrays.asList(userIds);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIdList)) {
            List<SysUserDO> list = list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .in("id", Arrays.asList(userIds))
                    .select("user_name","real_name", "group_id"));
            if (list.size() > 0) {

                if (removeUsers(userIdList)) {
                    if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除用户", "删除用户：" + list
                            .parallelStream
                                    ().map
                                    (SysUserDO::getRealName)
                            .collect(Collectors.toList()), list.get(0).getGroupId(), null)) {
                        return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
                    }
                }
            } else {
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
            }
            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "用户主键"));
    }

    @Override
    public boolean initUser(SysUserDO user) {
        // 判断用户密级不能为空
        if (user.getSecretLevel() == null || user.getSecretLevel() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("ARG_CAN_NOT_BE_EMPTY", "用户密级"));
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
        }
        user.setCreateTime(LocalDateTime.now());
        user.setSortIndex(createSortIndexByUserName(user.getUserName()));
        if (!this.save(user)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        /**
         * 密码加密
         */
        SysPasswordPolicyDO passwordPolicy = sysPasswordPolicyService.getPasswordPolicy();

        /**
         * 校验密码长度
         * 非密、秘密至少8位
         * 机密至少10位
         */
        int length = passwordPolicy.getLen() > 8 ? passwordPolicy.getLen() : 8;
//        /**
//         * 校验密码必须同时包含大写字母、小写字母、数字、特殊符号
//         */
//        String pwPattern = String.format("^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)" +
//                "[a-zA-Z0-9\\W]{%d,}$", length);
//        if (!password.matches(pwPattern)) {
//            throw new RException(String.format(InternationUtils.getInternationalMsg("ERROR_PASSWORD_LENGTH"),
// length));
//        }
        /**
         * 加密保存密码
         */
        try {
            password = GMBaseUtil.pwToCipherPassword(user.getUserName(), password);
        } catch (UnsupportedEncodingException e) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }
        boolean result = this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", user.getId()).set("password", password).set("password_update_time", LocalDateTime.now()));
        if (!result) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        return true;
    }

    @Override
    public R getTokenAfterLogin(String userKey) {
        if (com.csicit.ace.common.utils.StringUtils.isNotBlank(userKey)) {
            SysUserDO user = getOne(new QueryWrapper<SysUserDO>().eq("id", userKey).or().eq("user_name", userKey).or
                    ().eq("staff_no", userKey));
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
        }
        return null;
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/4/22 15:28
     */
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
//        else {
//            int count = this.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0).eq("sort_index", user
//                    .getSortIndex()));
//            if (count > 0) {
//                throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
//            }
//        }
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
        user.setCreateUser(securityUtils.getCurrentUserId());
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

        // 校验工号
        String staffNo = user.getStaffNo();
        int count = this.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0).eq("staff_no", staffNo));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("SAME_STAFFNO_EXIST"));
        }
        // 判断用户的人员ID是否为空格
        if (!this.save(user)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        if (StringUtils.isNotBlank(user.getPersonDocId())) {
            /**
             * 保存用户和角色-部门关系
             */
            sysRoleService.updateRoleAndDepForUser(user.getId(), bdPersonJobService.getOne(new
                    QueryWrapper<BdPersonJobDO>()
                    .eq("person_doc_id", user.getPersonDocId()).eq("is_main_job", 1)).getDepartmentId(), null);
        }

        /**
         * 密码加密
         */
        if (StringUtils.isNotBlank(password) && password != null) {
            saveUserToUpdatePassword(user.getUserName(), password);
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存用户", "保存用户：" + user
                    .getRealName(), user
                    .getGroupId(), null)) {
                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
            }
        } else {
            // 新建用户密码为空 采用默认密码
            password = sysConfigService.getValue("defaultPassword");
            if (StringUtils.isNotBlank(password) && password != null) {
                saveUserToUpdatePassword(user.getUserName(), decryptPassword(user.getUserName(), password, true));
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存用户", "保存用户：" + user
                                .getRealName(), user
                                .getGroupId(),
                        null)) {
                    return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
                }
            }

        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
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


    /**
     * 校验验证码
     *
     * @param userId
     * @param captcha
     * @return
     * @author zuogang
     * @date 2019/4/22 15:29
     */

    @Override
    public boolean validateCaptcha(String userId, String captcha) {
        return true;
    }

    /**
     * 生成验证码
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/22 15:30
     */
    public void createCaptcha() {

    }

    /**
     * 管理员解锁用户
     *
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/4/22 15:30
     */
    @Override
    public boolean unlockUser(String userId) {
        if (this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", userId).set("FAIL_LOGIN_TIMES", 0).set("unlock_time", null)
        )) {
            SysUserDO user = getById(userId);
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "解锁用户", "解锁用户：" + user
                            .getRealName(), user.getGroupId(),
                    null);
        }
        return false;
    }

    /**
     * 保存用户的时候 调用的更新密码   避免首次登录相关问题
     *
     * @param userName
     * @param password
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/6/12 16:21
     */
    public R saveUserToUpdatePassword(String userName, String password) {
        return updatePassword(userName, password, true);
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

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
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
        if (baseMapper.saveOldPassword(UuidUtil.createUUID(), userId) != 1) {
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
                result = this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                        .eq("id", userId).set("is_first_login", 0));
                if (!result) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            }
        }
        // 登陆页面修改密码 此时无token
        JSONObject info = new JSONObject();
        info.put("用户名", user.getRealName());
        info.put("登录名", user.getUserName());
        info.put("修改人登录名", securityUtils.getCurrentUserName());
        if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更改密码", "更改" + info.get
                ("用户名") + "密码"))
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("updatePasswordTime",
                    LocalDateTime.now());

        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }


    /**
     * 修改用户集团授控域
     *
     * @param user
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    @Override
    public boolean saveUserGroupControlDomain(SysUserDO user) {
        String userId = user.getId();
        List<OrgGroupDO> groups = user.getGroups();
        Integer roleType = user.getRoleType();

        SysUserDO userDO = getById(userId);

        //集团三元需要校验IP
        //集团超级管理员、单一业务管理员 不需要校验IP
        // 校验IP地址
        if (Objects.equals(11, roleType) || Objects.equals(22, roleType) || Objects.equals(33, roleType)) {
            // 验证：集团管理员 同一集团管控域下，不同三员角色账号的ip地址不同
            if (groups != null && groups.size() > 0) {
                List<String> groupIds = groups.stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
                // 获取相同集团授控域不同角色的userIds
                List<String> rolewithUserIds = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                        .ne("role_type", roleType).in("organization_id", groupIds))
                        .stream().distinct().map(SysUserAdminOrgDO::getUserId).collect(Collectors.toList());
                this.checkHaveIp(rolewithUserIds, userDO.getIpAddress());
            }
        }

        // 用户类别设置为未激活状态
        String retainGroupAndAppAdmin = sysConfigService.getValue("retainGroupAndAppAdmin");
        if("3".equals(retainGroupAndAppAdmin)){
            userDO.setUserType(55);
        }else {
            userDO.setUserType(11);
        }

        if (!updateById(userDO)) {
            return false;
        }

        // 删除旧的系统管理-用户-可管理的组织
        sysUserAdminOrgService.remove(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 0).eq("user_id", userId));

        // 添加新的系统管理-用户-可管理的组织
        List<SysUserAdminOrgDO> list = new ArrayList<>(16);
        StringBuffer str = new StringBuffer();
        if (groups != null && groups.size() > 0) {
            groups.stream().forEach(group -> {
                SysUserAdminOrgDO sysUserAdminOrgDO = new SysUserAdminOrgDO();
                sysUserAdminOrgDO.setId(UuidUtils.createUUID());
                sysUserAdminOrgDO.setUserId(userId);
                sysUserAdminOrgDO.setOrganizationId(group.getId());
                sysUserAdminOrgDO.setActivated(0);
                sysUserAdminOrgDO.setRoleType(roleType);
                list.add(sysUserAdminOrgDO);
                str.append(group.getName());
                str.append(",");
            });
            if (list.size() > 0 && sysUserAdminOrgService.saveBatch(list)) {
                if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改用户集团管控域" + "修改用户"
                        + user
                        .getRealName() + "集团管控域", str
                        .substring(0, str
                                .length() - 1))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断用户Ids是否拥该IP地址
     *
     * @param rolewithUserIds
     * @param ipAddress
     * @return
     * @author zuogang
     * @date 2019/12/19 16:57
     */
    private void checkHaveIp(List<String> rolewithUserIds, String ipAddress) {
        if (rolewithUserIds != null && rolewithUserIds.size() > 0) {

            // 获取相同ip地址的用户

            List<String> ipAddressList = new ArrayList<>(16);
            if (StringUtils.isNotBlank(ipAddress)) {
                if (ipAddress.contains(";")) {
                    ipAddressList = Arrays.asList(ipAddress.split(";"));
                } else {
                    ipAddressList.add(ipAddress);
                }
            }

            List<SysUserDO> sysUserDOS = list(new QueryWrapper<SysUserDO>().eq("is_delete", 0).in
                    ("id", rolewithUserIds));
            StringBuffer stringBuffer = new StringBuffer();
            if (CollectionUtils.isNotEmpty(sysUserDOS)) {
                for (SysUserDO sysUserDO : sysUserDOS) {
                    ipAddressList.stream().forEach(address -> {
                        // 判断拥有相同IP地址
                        if (StringUtils.isNotBlank(sysUserDO.getIpAddress())) {
                            if (sysUserDO.getIpAddress().indexOf(address) > -1) {
                                stringBuffer.append(sysUserDO.getUserName());
                                stringBuffer.append(",");
                            }
                        }
                    });
                }
            }

            if (stringBuffer.length() > 0) {
                throw new RException(String.format(InternationUtils.getInternationalMsg
                        ("SAME_IP_AND_DIFFERENT_ROLE_TYPE_EXIST"), stringBuffer.substring(0, stringBuffer
                        .length() - 1)));
            }
        }
    }

    /**
     * 修改用户应用授控域并激活
     *
     * @param user
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    @Override
    public boolean saveuserAppControlDomainAndActive(SysUserDO user) {

        if (this.saveUserAppControlDomain(user)) {
            Map<String, String> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("roleType", user.getRoleType().toString());
            return sysUserRoleService.setAppActivated(map);
        }
        return true;
    }

    /**
     * 修改用户应用授控域
     *
     * @param user
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    @Override
    public boolean saveUserAppControlDomain(SysUserDO user) {
        String userId = user.getId();
        Integer roleType = user.getRoleType();
        SysUserDO userDO = getById(userId);
        List<SysGroupAppDO> apps = user.getApps();

        // 校验IP地址
        if (Objects.equals(111, roleType) || Objects.equals(222, roleType) || Objects.equals(333, roleType)) {
            // 验证：应用管理员 同一应用管控域下，不同三员角色账号的ip地址不同
            if (apps != null && apps.size() > 0) {
                List<String> appIds = apps.stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());
                // 获取相同应用授控域不同角色的userIds
                List<String> rolewithUserIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                        .ne("role_type", roleType).in("app_id", appIds))
                        .stream().map(SysAuthScopeAppDO::getUserId).distinct().collect(Collectors.toList());
                this.checkHaveIp(rolewithUserIds, userDO.getIpAddress());
            }
        }

        // 用户类别设置为未激活状态
        userDO.setUserType(22);
        if (!updateById(userDO)) {
            return false;
        }

        // 删除该用户老的权限受控范围
        sysAuthScopeAppService.remove(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("is_activated", 0).eq("user_id", userId));
        sysAuthScopeOrgService.remove(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("is_activated", 0).eq("user_id", userId));
        sysAuthScopeUserGroupService.remove(new QueryWrapper<SysAuthScopeUserGroupDO>()
                .eq("is_activated", 0).eq("user_id", userId));

        // 添加新的系统管理-用户-可管理的组织
        List<SysAuthScopeAppDO> sysAuthScopeAppDOS = new ArrayList<>(16);
        List<SysAuthScopeOrgDO> sysAuthScopeOrgDOS = new ArrayList<>(16);
        List<SysAuthScopeUserGroupDO> sysAuthScopeUserGroupDOS = new ArrayList<>(16);
        StringBuffer appStr = new StringBuffer();
        StringBuffer orgStr = new StringBuffer();
        StringBuffer userGroupStr = new StringBuffer();
        if (apps != null && apps.size() > 0) {
            apps.stream().forEach(sysGroupAppDO -> {

                appStr.append(sysGroupAppDO.getName());
                appStr.append(",");
                // 应用授控域
                SysAuthScopeAppDO sysAuthScopeAppDO = new SysAuthScopeAppDO();
                sysAuthScopeAppDO.setId(UuidUtils.createUUID());
//                    sysAuthScopeAppDO.setAuthId(sysAuthDO.getId());
                sysAuthScopeAppDO.setUserId(userId);
                sysAuthScopeAppDO.setAppId(sysGroupAppDO.getId());
                sysAuthScopeAppDO.setRoleType(roleType);
                sysAuthScopeAppDO.setActivated(0);
                sysAuthScopeAppDOS.add(sysAuthScopeAppDO);
            });
            // 组织授控域
            List<OrgOrganizationDO> organizes = user.getOrganizes();
            if (organizes != null && organizes.size() > 0) {
                organizes.stream().forEach(organize -> {

                    orgStr.append(organize.getName());
                    orgStr.append(",");

                    SysAuthScopeOrgDO sysAuthScopeOrgDO = new SysAuthScopeOrgDO();
                    sysAuthScopeOrgDO.setId(UuidUtils.createUUID());
//                            sysAuthScopeOrgDO.setAuthId(sysAuthDO.getId());
                    sysAuthScopeOrgDO.setUserId(userId);
                    sysAuthScopeOrgDO.setOrganizationId(organize.getId());
                    sysAuthScopeOrgDO.setActivated(0);
                    sysAuthScopeOrgDO.setRoleType(roleType);
                    sysAuthScopeOrgDOS.add(sysAuthScopeOrgDO);
                });
            }

            // 用户组授控域
            List<SysUserGroupDO> userGroups = user.getUserGroups();
            if (userGroups != null && userGroups.size() > 0) {
                userGroups.stream().forEach(userGroup -> {

                    userGroupStr.append(userGroup.getName());
                    userGroupStr.append(",");

                    SysAuthScopeUserGroupDO sysAuthScopeUserGroupDO = new SysAuthScopeUserGroupDO();
                    sysAuthScopeUserGroupDO.setId(UuidUtils.createUUID());
//                            sysAuthScopeUserGroupDO.setAuthId(sysAuthDO.getId());
                    sysAuthScopeUserGroupDO.setUserId(userId);
                    sysAuthScopeUserGroupDO.setUserGroupId(userGroup.getId());
                    sysAuthScopeUserGroupDO.setActivated(0);
                    sysAuthScopeUserGroupDO.setRoleType(roleType);
                    sysAuthScopeUserGroupDOS.add(sysAuthScopeUserGroupDO);
                });
            }
            if (sysAuthScopeAppService.saveBatch(sysAuthScopeAppDOS)) {
                if (sysAuthScopeOrgDOS.size() > 0) {
                    if (!sysAuthScopeOrgService.saveBatch
                            (sysAuthScopeOrgDOS))
                        return false;
                }
                if (sysAuthScopeUserGroupDOS.size() > 0) {
                    if (!sysAuthScopeUserGroupService.saveBatch(sysAuthScopeUserGroupDOS))
                        return false;
                }
                if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改【" + userDO
                                .getUserName() + "】的应用管控域:",
                        appStr.substring(0, appStr
                                .length() - 1) + "；组织管控域：" + (orgStr.length() > 0 ? orgStr.substring(0, orgStr
                                .length() - 1) : null) + "；用户组管控域：" + (userGroupStr
                                .length() > 0 ? userGroupStr.substring(0, userGroupStr
                                .length() - 1) : null))) {
                    return false;
                }
            } else {
                return false;
            }

        }

        return true;
    }

    /**
     * 修改租户三员IP地址
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    @Override
    public boolean updThreeTenantsIpAddress(Map<String, String> map) {
        String adminIpAddress = map.get("adminIpAddress");
        String secIpAddress = map.get("secIpAddress");
        String auditorIpAddress = map.get("auditorIpAddress");
        SysUserDO admin = getOne(new QueryWrapper<SysUserDO>().eq("user_name", "admin"));
        SysUserDO sec = getOne(new QueryWrapper<SysUserDO>().eq("user_name", "sec"));
        SysUserDO auditor = getOne(new QueryWrapper<SysUserDO>().eq("user_name", "auditor"));
        if (!Objects.equals(adminIpAddress, admin.getIpAddress())) {
            admin.setIpAddress(adminIpAddress);
            if (this.updateById(admin)) {
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改租户管理员的IP地址",
                        "修改租户管理员的IP地址：" + adminIpAddress);
            }
        }
        if (!Objects.equals(secIpAddress, sec.getIpAddress())) {
            sec.setIpAddress(secIpAddress);
            if (this.updateById(sec)) {
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改租户保密员的IP地址",
                        "修改租户保密员的IP地址：" + secIpAddress);
            }
        }
        if (!Objects.equals(auditorIpAddress, auditor.getIpAddress())) {
            auditor.setIpAddress(auditorIpAddress);
            if (this.updateById(auditor)) {
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改租户审计员的IP地址",
                        "修改租户审计员的IP地址：" + auditorIpAddress);
            }
        }
        return true;
    }

    /**
     * 批量重置用户密码
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    @Override
    public boolean batchResetPassword(Map<String, String> map) {
        String groupId = map.get("groupId");
        String orgId = map.get("orgId");
        List<String> names = list(new QueryWrapper<SysUserDO>().eq("group_id", groupId)
                .eq("organization_id", orgId).eq("user_type", 3).eq("is_delete", 0))
                .stream().map(SysUserDO::getUserName).collect(Collectors.toList());

        String defaultPassword = sysConfigService.getValue("defaultPassword");
        if (StringUtils.isBlank(defaultPassword)) {
            return false;
        }

        // 判断用户有没有权限
        SysUserDO currentUser = securityUtils.getCurrentUser();
        if (currentUser.getUserType() >= 3 && currentUser.getUserType() != 11) {
            throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }

        names.stream().forEach(name -> {
            /**
             * 加密保存密码
             */
            String password = decryptPassword(name, defaultPassword, false);

            update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                    .eq("user_name", name).ne("user_type", 0).set("unlock_time", LocalDateTime.now
                            ().minusMinutes
                            (10)
                    ).set("is_first_login", 1).set("password", password).set
                            ("password_update_time", LocalDateTime.now()));
        });

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "重置密码", "重置【" + names
                        .toString() + "】密码",
                groupId,
                null)) {
            return false;
        }
        return true;
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
            if (com.csicit.ace.common.utils.StringUtils.isNotBlank(user.getIpAddress())) {
                if (user.getIpAddress().contains(";")) {
                    ipAddressList = Arrays.asList(user.getIpAddress().split(";"));
                } else {
                    ipAddressList.add(user.getIpAddress());
                }
            }
            if (!ipAddressList.contains(ipAddress)) {
                return R.error(InternationUtils.getInternationalMsg("IP_NOT_MATCH"));
            }
        }

        /**
         * 判断账户是否被锁定
         */
        LocalDateTime unlockTime = user.getUnlockTime();
        if (unlockTime != null) {
            long minutes = com.csicit.ace.common.utils.LocalDateTimeUtils.getInterval(LocalDateTime.now(), unlockTime);
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
                long interval = com.csicit.ace.common.utils.LocalDateTimeUtils.getInterval(latestLoginTime,
                        LocalDateTime.now());
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
            sysAuditLogService.saveLoginLog("登录失败!", user.getRealName() + "登录失败!", user.getRealName());

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
        long minutes = com.csicit.ace.common.utils.LocalDateTimeUtils.getInterval(user.getPasswordUpdateTime(),
                LocalDateTime.now());
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
         * 登陆成功
         */
        if (openBindUserIp && Objects.equals(3, user.getUserType())) {
            if (StringUtils.isBlank(user.getIpAddress())) {
                if (!update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                        .set("IS_IP_BIND", 1)
                        .set("ip_address", ipAddress)
                        .eq("id", userId))) {
                    throw new RException("登录异常，请重试！");
                }
            }
        }

        /**
         * 登陆成功 返回用户  Token
         * 密码等敏感信息不应该返回
         */
        if (!sysUserLoginService.save(new SysUserLoginDO(userId, userName, 1))) {
            throw new RException("登录异常，请重试！");
        }
        if (!sysAuditLogService.saveLoginLog("登录成功!", user.getRealName() + "登录成功!", user.getRealName())) {
            throw new RException("登录异常，请重试！");
        }
        /**
         * 加载用户信息、角色信息、权限列表到缓存数据库
         */
        return cacheUserInfos(token, userId, true, 0, null);
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
            BdPersonJobDO mainJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    user.getPersonDocId()).eq("is_main_job", 1));
            if (mainJob != null) {
                OrgDepartmentDO dep = orgDepartmentService.getById(mainJob.getDepartmentId());
                cacheUtil.hset(token, "department", JSONObject.toJSON(dep), seconds);
            }
        }
        /**
         * 用户角色ID列表
         */
        List<String> roleIds = new ArrayList<>();
        List<SysUserRoleLvDO> sysUserRoleLvDOS = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("user_id", userId).select("id").eq("is_latest", 1));
        if (CollectionUtils.isNotEmpty(sysUserRoleLvDOS)) {
            List<String> lvIds = sysUserRoleLvDOS.stream().map(SysUserRoleLvDO::getId).collect(Collectors.toList());
            List<SysUserRoleVDO> sysUserRoleVDOS = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                    .in("lv_id", lvIds).select("id", "role_id"));
            if (CollectionUtils.isNotEmpty(sysUserRoleVDOS)) {
                roleIds.addAll(sysUserRoleVDOS.stream().map(SysUserRoleVDO::getRoleId).collect(Collectors.toList
                        ()));
            }
        }

        cacheUtil.hset(token, "roleIds", JSONObject.toJSON(roleIds), seconds);

        // 判断用户是否是租户 集团 或 应用管理员
        String[] roleIdStrs = {"admin", "sec", "auditor", "groupadmin", "groupsec", "groupauditor",
                "groupsuperadmin", "appadmin",
                "appsec",
                "appauditor", "appsuperadmin", "businessadmin"};
        boolean isAdmin = roleIds.stream().anyMatch(roleId -> Arrays.asList(roleIdStrs).contains(roleId));

        // 判断用户是否是开发平台人员
        String[] roleIdStrs2 = {"devadmin", "devmaintainer", "devdeveloper"};
        boolean isDevUser = roleIds.stream().anyMatch(roleId -> Arrays.asList(roleIdStrs2).contains(roleId));

        /**
         * 统计用户可管理集团的数量
         */
        List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(user.getId());
        cacheUtil.hset(token, "groups", JSONObject.toJSON(groups), seconds);
        cacheUtil.hset(token, "isAdmin", isAdmin ? 1 : 0, seconds);
        cacheUtil.hset(token, "isDevUser", isDevUser ? 1 : 0, seconds);
        /**
         * 用户权限标识列表  API
         */
        List<String> apis = sysApiMixService.list(new QueryWrapper<SysApiMixDO>().eq("user_id", userId)
                .select("id", "api_id")).stream().map
                (SysApiMixDO::getApiId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(apis)) {
            cacheUtil.hset(token, "apis", JSONObject.toJSON(apis), seconds);
        }

        // 普通用户没有资格登录平台
        List<SysGroupAppDO> list = new ArrayList<>(16);

        // 非开发平台用户
        if (!isDevUser) {
            if (isAdmin) {
                if (StringUtils.isNotBlank(user.getGroupId())) {
                    list = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().eq("group_id", user
                            .getGroupId()));
                }
            } else {
                // 用户有权限可以登录的应用列表 & 应用中某些菜单没有权限
                List<String> authMixAppIds = aceDBHelperMapper.getStringsWithParams("select distinct app_id from " +
                        "sys_auth_mix where user_id=''{0}''", userId);
                List<String> noAuthAppIds =
                        aceDBHelperMapper.getStringsWithParams("select id from sys_group_app where group_id=''{0}'' " +
                                "and id in (select distinct app_id from sys_menu where auth_id is" +
                                " null or auth_id='''' )", user.getGroupId());
                // 2020-9-24
                // 没有菜单的 不基于 平台 前端开发的应用
                noAuthAppIds.addAll(aceDBHelperMapper.getStringsWithParams("select id from sys_group_app where " +
                        "group_id=''{0}'' and id not" +
                        " in (select distinct app_id from sys_menu)", user.getGroupId()));
                Set<String> authAppIds = new HashSet<>(authMixAppIds);
                authAppIds.addAll(noAuthAppIds);
                if (CollectionUtils.isNotEmpty(authAppIds)) {
                    list = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().eq("group_id", user
                            .getGroupId()).ne("id", "platform").in("id", authAppIds));

                    // 应用是否在线
                    List<SysGroupAppDO> onlineList = sysGroupAppService.getOnlieList(list);
                    if (CollectionUtils.isEmpty(onlineList)) {
                        throw new RException("当前没有应用在线！");
                    }
                    List<String> onlineAppIds = onlineList.stream().map(SysGroupAppDO::getId).collect(Collectors
                            .toList());
                    list.forEach(app -> {
                        app.setIsOnline(onlineAppIds.contains(app.getId()) ? 1 : 0);
                    });
                }
            }
        }
        SysGroupAppDO mainApp = new SysGroupAppDO();
        if (CollectionUtils.isNotEmpty(list)) {
            SysGroupAppDO t = list.stream().filter(app -> app.getMainApp() == 1).findFirst().orElse(null);
            if (t != null) {
                mainApp = t;
            }
        }

        if (ok) {
            return R.ok().put("token", token).put("isAdmin", isAdmin).put("isDevUser", isDevUser).put("apps", list)
                    .put("mainApp", mainApp);
        } else {
            return R.error(code, msg).put("token", token).put("isAdmin", isAdmin).put("isDevUser", isDevUser).put
                    ("apps", list).put("mainApp",
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
     * 开发平台-用户管理-新增
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/5/17 10:55
     */
    @Override
    public boolean saveUserForDev(Map<String, Object> map) {
        String userName = (String) map.get("userName");
        String realName = (String) map.get("realName");

        int roleType = (Integer) map.get("roleType");
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setId(UuidUtils.createUUID());
        sysUserDO.setUserName(userName);
        sysUserDO.setRealName(realName);
        sysUserDO.setUserType(4);
        sysUserDO.setSecretLevel(5);
        saveUser(sysUserDO);
        // 设置该用户开发平台角色
        if (Objects.equals(2222, roleType)) {
            SysUserRoleDO maintainer = new SysUserRoleDO();
            maintainer.setUserId(sysUserDO.getId());
            maintainer.setRoleId("devmaintainer");
            maintainer.setAppId("dev-platform");
            maintainer.setRealName(realName);
            maintainer.setRoleName("开发平台项目管理人员");
            maintainer.setCreateUser(securityUtils.getCurrentUserId());
            maintainer.setCreateTime(LocalDateTime.now());
            maintainer.setUpdateTime(LocalDateTime.now());
            maintainer.setRemark("");
            maintainer.setId(UuidUtils.createUUID());
            maintainer.setDataVersion(0);
            if (!sysUserRoleService.save(maintainer)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }

            SysUserRoleLvDO maintainerLv = new SysUserRoleLvDO();
            maintainerLv.setId(UuidUtils.createUUID());
            maintainerLv.setUserId(sysUserDO.getId());
            maintainerLv.setVersion(0);
            maintainerLv.setLatest(1);
            maintainerLv.setCreateUser(securityUtils.getCurrentUserId());
            maintainerLv.setCreateTime(LocalDateTime.now());
            maintainerLv.setAppId("dev-platform");
            if (!sysUserRoleLvService.save(maintainerLv)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }

            SysUserRoleVDO maintainerV = new SysUserRoleVDO();
            maintainerV.setId(UuidUtils.createUUID());
            maintainerV.setRoleId("devmaintainer");
            maintainerV.setRoleName("开发平台项目管理人员");
            maintainerV.setLvId(maintainerLv.getId());
            if (!sysUserRoleVService.save(maintainerV)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }


        } else if ((Objects.equals(3333, roleType))) {

            SysUserRoleDO maintainer = new SysUserRoleDO();
            maintainer.setUserId(sysUserDO.getId());
            maintainer.setRoleId("devdeveloper");
            maintainer.setAppId("dev-platform");
            maintainer.setRealName(realName);
            maintainer.setRoleName("开发平台项目开发人员");
            maintainer.setCreateUser(securityUtils.getCurrentUserId());
            maintainer.setCreateTime(LocalDateTime.now());
            maintainer.setUpdateTime(LocalDateTime.now());
            maintainer.setRemark("");
            maintainer.setId(UuidUtils.createUUID());
            maintainer.setDataVersion(0);
            if (!sysUserRoleService.save(maintainer)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }

            SysUserRoleLvDO maintainerLv = new SysUserRoleLvDO();
            maintainerLv.setId(UuidUtils.createUUID());
            maintainerLv.setUserId(sysUserDO.getId());
            maintainerLv.setVersion(0);
            maintainerLv.setLatest(1);
            maintainerLv.setCreateUser(securityUtils.getCurrentUserId());
            maintainerLv.setCreateTime(LocalDateTime.now());
            maintainerLv.setAppId("dev-platform");
            if (!sysUserRoleLvService.save(maintainerLv)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }

            SysUserRoleVDO maintainerV = new SysUserRoleVDO();
            maintainerV.setId(UuidUtils.createUUID());
            maintainerV.setRoleId("devdeveloper");
            maintainerV.setRoleName("开发平台项目开发人员");
            maintainerV.setLvId(maintainerLv.getId());
            if (!sysUserRoleVService.save(maintainerV)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
        }

        if (!sysAuthMixService.saveAuthMixForApp(sysUserDO.getId(), "dev-platform")) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        return true;
    }

    /**
     * 开发平台-用户管理-修改
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/5/17 10:55
     */
    @Override
    public boolean updateUserForDev(Map<String, Object> map) {
        String id = (String) map.get("id");
        String realName = (String) map.get("realName");
        SysUserDO sysUserDO = getById(id);
        if (sysUserDO == null) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NOT_EXIST"));
        }
        sysUserDO.setRealName(realName);
        if (StringUtils.isNotBlank(realName)) {
            sysUserDO.setPinyin(PinyinUtils.toHanyuPinyin(realName));
        }
        sysUserDO.setUpdateTime(LocalDateTime.now());
        if (!updateById(sysUserDO)) {
            return false;
        }
        return true;
    }

    /**
     * 创建开发平台超级管理员
     *
     * @return
     * @author zuogang
     * @date 2019/5/17 10:55
     */
    @Override
    public R addDevAdmin() {
        if (count(new QueryWrapper<SysUserDO>().eq("user_name", "devadmin")) > 0) {
            return R.ok("开发平台管理员账号已创建！");
        }

        // 开发平台超级管理员
        SysUserDO devadmin = new SysUserDO();
        String devadminId = UuidUtils.createUUID();
        devadmin.setId(devadminId);
        devadmin.setUserType(4);
        devadmin.setUserName("devadmin");
        devadmin.setRealName("开发平台超级管理员");
        devadmin.setSecretLevel(5);
        saveUser(devadmin);

        SysUserRoleDO devadminUserRole = new SysUserRoleDO();
        devadminUserRole.setRoleId("devadmin");
        devadminUserRole.setUserId(devadminId);
        devadminUserRole.setRoleType(1111);
        devadminUserRole.setAppId("dev-platform");
        if (!sysUserRoleService.save(devadminUserRole)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        SysUserRoleLvDO devadminLv = new SysUserRoleLvDO();
        devadminLv.setId(UuidUtils.createUUID());
        devadminLv.setUserId(devadminId);
        devadminLv.setLatest(1);
        devadminLv.setVersion(0);
        devadminLv.setAppId("dev-platform");
        if (!sysUserRoleLvService.save(devadminLv)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        SysUserRoleVDO devadminV = new SysUserRoleVDO();
        devadminV.setId(UuidUtils.createUUID());
        devadminV.setLvId(devadminLv.getId());
        devadminV.setRoleId("devadmin");
        if (!sysUserRoleVService.save(devadminV)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        if (!sysAuthMixService.saveAuthMixForApp(devadminId, "dev-platform")) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 创建开发平台超级管理员
     *
     * @return
     * @author zuogang
     * @date 2019/5/17 10:55
     */
    @Override
    public R getUserListForDepOrRole(Map<String, String> map, Integer flg) {
        int current = Integer.parseInt(map.get("current"));
        int size = Integer.parseInt(map.get("size"));
        // 获取当前用户的集团ID
        String groupId = securityUtils.getCurrentUser().getGroupId();
        // 获取当前用户所在的业务单元ID
        String orgId = securityUtils.getCurrentUser().getOrganizationId();
        // 检索条件
        String searchStr = map.get("searchString");
        Page<SysUserDO> page = new Page<>(current, size);
        /**
         * 用户管理左侧 部门/角色
         */
        String depId = map.get("depId");
        String roleId = map.get("roleId");

        // 获取当前用户的密级
        Integer secretLevel = securityUtils.getCurrentUser().getSecretLevel();

        IPage list = null;

        if (StringUtils.isBlank(depId) && StringUtils.isBlank(roleId)) {
            if (StringUtils.isNotBlank(orgId)) {
                list = page(page, new QueryWrapper<SysUserDO>()
                        .eq("group_id", groupId)
                        .eq("organization_id", orgId)
                        .eq("start_flag", "1")
                        .eq("is_delete", 0)
                        .and(flg == 1, i -> i.le("secret_level", secretLevel))
                        .and(StringUtils.isNotBlank(searchStr)
                                , i -> i.like("user_name", searchStr)
                                        .or().like("pinyin", searchStr)
                                        .or().like("real_name", searchStr)
                                        .or().like("email", searchStr)
                                        .or().like("phone_number", searchStr))
                        .orderByAsc("sort_index"));
            }
        } else if (!StringUtils.isBlank(depId)) {
            list = page(page, new QueryWrapper<SysUserDO>()
                    .eq("group_id", groupId)
                    .eq("organization_id", orgId)
                    .inSql("person_doc_id", "select PERSON_DOC_ID from BD_PERSON_JOB where department_id = '" + depId
                            + "'")
                    .eq("start_flag", "1")
                    .eq("is_delete", 0)
                    .and(flg == 1, i -> i.le("secret_level", secretLevel))
//                    .le("secret_level", secretLevel)
                    .and(StringUtils.isNotBlank(searchStr)
                            , i -> i.like("user_name", searchStr)
                                    .or().like("pinyin", searchStr)
                                    .or().like("real_name", searchStr)
                                    .or().like("email", searchStr)
                                    .or().like("phone_number", searchStr))
                    .orderByAsc("sort_index"));

        } else if (!StringUtils.isBlank(roleId)) {
            /**
             * 获取所有拥有此角色的用户ID
             */
            list = page(page, new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .eq("user_type", 3)
                    .eq("group_id", groupId)
                    .eq("organization_id", orgId)
                    .inSql("id", "select user_id from sys_user_role_lv where is_latest=1 and id in (select lv_id from" +
                            " sys_user_role_v  where role_id='" + roleId + "')")
                    .eq("is_delete", 0)
                    .eq("start_flag", "1")
                    .and(flg == 1, i -> i.le("secret_level", secretLevel))
//                    .le("secret_level", secretLevel)
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
}