package com.csicit.ace.platform.core.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.pojo.vo.ThreeAdminVO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysUserRoleMapper;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.JDBCUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户角色关系管理 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRoleDO> implements
        SysUserRoleService {


    @Autowired
    SysAuthMixService sysAuthMixService;
    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;
    @Autowired
    SysRoleMutexService sysRoleMutexService;
    @Autowired
    SysAuthScopeUserGroupService sysAuthScopeUserGroupService;
    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    OrgOrganizationService orgOrganizationService;
    @Autowired
    SysGroupAppService sysGroupAppService;
    @Autowired
    SysUserGroupService sysUserGroupService;
    @Autowired
    OrgGroupService orgGroupService;
    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;
    @Autowired
    SysAuthService sysAuthService;
    @Autowired
    SysUserRoleLvService sysUserRoleLvService;
    @Autowired
    SysUserRoleVService sysUserRoleVService;
    @Autowired
    SysWaitGrantUserService sysWaitGrantUserService;
    @Autowired
    SysWaitGrantAuthService sysWaitGrantAuthService;
    @Autowired
    SysMessageService sysMessageService;
    @Autowired
    SysAuthRoleService sysAuthRoleService;

    /**
     * 保存用户角色信息
     *
     * @param userId
     * @param roleId
     * @return void
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    private void saveUserRole(String appId, String userId, String roleId, String roleName, String realName) {
        SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
        sysUserRoleDO.setRoleId(roleId);
        sysUserRoleDO.setRoleName(roleName);
        sysUserRoleDO.setUserId(userId);
        sysUserRoleDO.setRealName(realName);
        sysUserRoleDO.setAppId(appId);
        sysUserRoleDO.setCreateTime(LocalDateTime.now());
        sysUserRoleDO.setCreateUser(securityUtils.getCurrentUserId());
        if (!save(sysUserRoleDO))
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));

        SysUserRoleLvDO sysUserRoleLvDO = new SysUserRoleLvDO();
        sysUserRoleLvDO.setId(UuidUtils.createUUID());
        sysUserRoleLvDO.setVersion(0);
        sysUserRoleLvDO.setLatest(1);
        sysUserRoleLvDO.setUserId(userId);
        sysUserRoleLvDO.setCreateTime(LocalDateTime.now());
        sysUserRoleLvDO.setCreateUser(securityUtils.getCurrentUserId());
        if (!sysUserRoleLvService.save(sysUserRoleLvDO))
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));

        SysUserRoleVDO sysUserRoleVDO = new SysUserRoleVDO();
        sysUserRoleVDO.setId(UuidUtils.createUUID());
        sysUserRoleVDO.setRoleId(roleId);
        sysUserRoleVDO.setRoleName(roleName);
        sysUserRoleVDO.setLvId(sysUserRoleLvDO.getId());
        if (!sysUserRoleVService.save(sysUserRoleVDO))
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 通过角色类型获取用户
     *
     * @param roleType 角色类型
     * @param type     1集团 2应用
     * @param status   activated已激活的  all已激活和未激活集合
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysUserDO>
     * @author shanwj
     * @date 2019/5/17 11:10
     */
    private List<SysUserDO> getOrgThreeUsersByRoleType(String roleType, String type, String status) {
        List<SysUserDO> list = new ArrayList<>(16);
        Set<String> userIds = new HashSet<>(16);
        if (Objects.equals("activated", status)) {
            userIds.addAll(sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("is_activated", 1).eq("role_type", Integer.parseInt(roleType))).stream().map
                    (SysUserAdminOrgDO::getUserId)
                    .collect(Collectors.toList()));
        } else if (Objects.equals("all", status)) {
            userIds.addAll(sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("role_type", Integer.parseInt(roleType))).stream().map(SysUserAdminOrgDO::getUserId)
                    .collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(userIds)) {
            list = sysUserService.list(new QueryWrapper<SysUserDO>()
                    .in("id", userIds).orderByAsc("user_name")
                    .eq("is_delete", 0)
                    .select("id", "user_type", "ip_address", "user_name", "real_name"));
            list.stream().forEach(sysUserDO -> {
                getUserControlDomain(sysUserDO, type, status);
                sysUserDO.setRoleType(Integer.parseInt(roleType));
            });
        }
        return list;
    }


    /**
     * 通过角色类型获取用户
     *
     * @param roleType 角色类型
     * @param type     1集团 2应用
     * @param status   activated已激活的  all已激活和未激活集合
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysUserDO>
     * @author shanwj
     * @date 2019/5/17 11:10
     */
    private List<SysUserDO> getAppThreeUsersByRoleType(String roleType, String type, String status, String groupId) {
        List<SysUserDO> list = new ArrayList<>(16);
        Set<String> userIdSet = new HashSet<>(16);
        if (Objects.equals("activated", status)) {
            userIdSet.addAll(sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("is_activated", 1)
                    .select("id", "user_id")
                    .eq("role_type", Integer.parseInt(roleType))).stream().map
                    (SysAuthScopeAppDO::getUserId)
                    .collect(Collectors.toList()));
        } else if (Objects.equals("all", status)) {
            userIdSet.addAll(sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .select("id", "user_id")
                    .eq("role_type", Integer.parseInt(roleType))).stream().map(SysAuthScopeAppDO::getUserId)
                    .collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(userIdSet)) {
            list = sysUserService.list(new QueryWrapper<SysUserDO>()
                    .in("id", userIdSet).in("group_id", groupId)
                    .orderByAsc("user_name")
                    .eq("is_delete", 0)
                    .select("id", "user_type", "ip_address", "user_name", "real_name"));
            list.stream().forEach(sysUserDO -> {
                getUserControlDomain(sysUserDO, type, status);
                sysUserDO.setRoleType(Integer.parseInt(roleType));
            });
        }
        return list;
    }

    @Autowired
    SysConfigService sysConfigService;

    /**
     * 通过UserID获取用户授控域
     *
     * @param sysUserDO
     * @param type      1集团 2应用
     * @param status    activated已激活的  all已激活和未激活集合  unactivated未激活的
     * @return
     * @author zuogang
     * @date 2019/4/22 15:30
     */
    private SysUserDO getUserControlDomain(SysUserDO sysUserDO, String type, String status) {


        if (Objects.equals("2", type)) {
            //  组织授控域
//            List<OrgOrganizationDO> orgOrganizationDOList = new ArrayList<>(16);
            List<String> organIds = new ArrayList<>(16);

            // 应用权限授控域
//            List<SysGroupAppDO> sysGroupAppDOList = new ArrayList<>(16);
            List<SysAuthScopeAppDO> sysAuthScopeAppDOList = new ArrayList<>(16);

            // 用户组授控域
//            List<SysUserGroupDO> sysUserGroupDOList = new ArrayList<>(16);
            List<String> userGroupIds = new ArrayList<>(16);
            if (Objects.equals("all", status)) {

                // 当status为all时，如果sysAuthScopeOrg表中有is_activated为0的数据，就显示为0的数据，如果没有为0的数据，就显示为1的数据
                if (sysAuthScopeAppService.count(new QueryWrapper<SysAuthScopeAppDO>()
                        .eq("is_activated", 0).eq("user_id", sysUserDO.getId())) > 0) {
                    organIds = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                            .eq("is_activated", 0).eq("user_id", sysUserDO.getId()))
                            .stream().map(SysAuthScopeOrgDO::getOrganizationId).distinct().collect
                                    (Collectors.toList());

                    sysAuthScopeAppDOList = sysAuthScopeAppService.list(new
                            QueryWrapper<SysAuthScopeAppDO>()
                            .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));

                    userGroupIds = sysAuthScopeUserGroupService.list(new
                            QueryWrapper<SysAuthScopeUserGroupDO>()
                            .eq("is_activated", 0).eq("user_id", sysUserDO.getId()))
                            .stream().map(SysAuthScopeUserGroupDO::getUserGroupId)
                            .collect(Collectors.toList());
                } else {
                    organIds = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                            .eq("is_activated", 1).eq("user_id", sysUserDO.getId()))
                            .stream().map(SysAuthScopeOrgDO::getOrganizationId).distinct().collect
                                    (Collectors.toList());

                    sysAuthScopeAppDOList = sysAuthScopeAppService.list(new
                            QueryWrapper<SysAuthScopeAppDO>()
                            .eq("is_activated", 1).eq("user_id", sysUserDO.getId()));

                    userGroupIds = sysAuthScopeUserGroupService.list(new
                            QueryWrapper<SysAuthScopeUserGroupDO>()
                            .eq("is_activated", 1).eq("user_id", sysUserDO.getId()))
                            .stream().map(SysAuthScopeUserGroupDO::getUserGroupId)
                            .collect(Collectors.toList());
                }

            } else if (Objects.equals("activated", status)) {

                organIds = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                        .eq("is_activated", 1).eq("user_id", sysUserDO.getId()))
                        .stream().map(SysAuthScopeOrgDO::getOrganizationId).distinct().collect
                                (Collectors.toList());

                sysAuthScopeAppDOList = sysAuthScopeAppService.list(new
                        QueryWrapper<SysAuthScopeAppDO>()
                        .eq("is_activated", 1).eq("user_id", sysUserDO.getId()));

                userGroupIds = sysAuthScopeUserGroupService.list(new
                        QueryWrapper<SysAuthScopeUserGroupDO>()
                        .eq("is_activated", 1).eq("user_id", sysUserDO.getId()))
                        .stream().map(SysAuthScopeUserGroupDO::getUserGroupId)
                        .collect(Collectors.toList());

            } else if (Objects.equals("unactivated", status)) {

                organIds = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                        .eq("is_activated", 0).eq("user_id", sysUserDO.getId()))
                        .stream().map(SysAuthScopeOrgDO::getOrganizationId).distinct().collect
                                (Collectors.toList());

                sysAuthScopeAppDOList = sysAuthScopeAppService.list(new
                        QueryWrapper<SysAuthScopeAppDO>()
                        .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));

                userGroupIds = sysAuthScopeUserGroupService.list(new
                        QueryWrapper<SysAuthScopeUserGroupDO>()
                        .eq("is_activated", 0).eq("user_id", sysUserDO.getId()))
                        .stream().map(SysAuthScopeUserGroupDO::getUserGroupId)
                        .collect(Collectors.toList());

            }

            if (CollectionUtils.isNotEmpty(organIds)) {
                sysUserDO.setOrganizes(orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                        .in("id", new HashSet<>(organIds)).eq("is_delete", 0)));
            }

            List<String> appIds = new ArrayList<>(16);
            sysAuthScopeAppDOList.stream().forEach(sysAuthScopeAppDO -> {
                appIds.add(sysAuthScopeAppDO.getAppId());
            });
            if (CollectionUtils.isNotEmpty(appIds)) {
                sysUserDO.setApps(sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
                        .in("id", new HashSet<>(appIds))));
            }
            if (CollectionUtils.isNotEmpty(userGroupIds)) {
                sysUserDO.setUserGroups(sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                        .in("id", new HashSet<>(userGroupIds))));
            }

        } else if (Objects.equals("1", type)) {
            //  集团授控域
            List<String> organIds = new ArrayList<>(16);
            ;
            QueryWrapper<SysUserAdminOrgDO> wrapper = new QueryWrapper<>();
            if (Objects.equals("activated", status)) {
                wrapper.eq("is_activated", 1)
                        .select("id", "organization_id")
                        .eq("user_id", sysUserDO.getId());
                organIds = sysUserAdminOrgService.list(wrapper)
                        .stream().map(SysUserAdminOrgDO::getOrganizationId)
                        .collect(Collectors.toList());
            } else if (Objects.equals("all", status)) {
                // 存在0的数据时显示0的数据，不存在则显示1的数据
                wrapper.eq("is_activated", 0)
                        .select("id", "organization_id")
                        .eq("user_id", sysUserDO.getId());
                organIds = sysUserAdminOrgService.list(wrapper)
                        .stream().map(SysUserAdminOrgDO::getOrganizationId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(organIds)) {
                    organIds = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                            .eq("is_activated", 1)
                            .select("id", "organization_id")
                            .eq("user_id", sysUserDO.getId()))
                            .stream().map(SysUserAdminOrgDO::getOrganizationId)
                            .collect(Collectors.toList());
                }
            } else if (Objects.equals("unactivated", status)) {
                wrapper.eq("is_activated", 0)
                        .select("id", "organization_id")
                        .eq("user_id", sysUserDO.getId());
                organIds = sysUserAdminOrgService.list(wrapper)
                        .stream().map(SysUserAdminOrgDO::getOrganizationId)
                        .collect(Collectors.toList());
            }

            if (CollectionUtils.isNotEmpty(organIds)) {
                sysUserDO.setGroups(orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                        .in("id", organIds).select("id", "name").eq("is_delete", 0)));
            }
        }
        return sysUserDO;
    }


    /**
     * 删除集团三员管理
     *
     * @param threeAdminVO
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean deleteGroupThreeRole(ThreeAdminVO threeAdminVO) {

        // 删除用户表
        sysUserService.deleteUser(threeAdminVO.getUserIds());

        return true;
    }

    /**
     * 删除应用三员管理
     *
     * @param threeAdminVO
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean deleteAppThreeRole(ThreeAdminVO threeAdminVO) {

        sysUserService.deleteUser(threeAdminVO.getUserIds());
        return true;
    }


    /**
     * 通过UserId获取该用户所拥有的应用三员授控域
     *
     * @param userId
     * @param status
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public SysUserDO getAppThreeDoMain(String userId, String status) {

        SysUserDO sysUserDO = sysUserService.getById(userId);
        sysUserDO = getUserControlDomain(sysUserDO, "2", status);
        return sysUserDO;
    }

    /**
     * 获取集团级待激活人员列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public List<SysUserDO> getGroupToBeActivatedData() {

// 平台管理员组成  "1"：集团应用三员；"2"：集团单一管理员、应用单一管理员；"3"：单一业务管理员
        String retainGroupAndAppAdmin = sysConfigService.getValue("retainGroupAndAppAdmin");

        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        if ("3".equals(retainGroupAndAppAdmin)) {
            queryWrapper.eq("is_delete", 0)
                    .eq("start_flag", "1")
                    .eq("user_type", 55)
                    .orderByAsc("user_name");
        } else {
            queryWrapper.eq("is_delete", 0)
                    .eq("start_flag", "1")
                    .eq("user_type", 11)
                    .orderByAsc("user_name");
        }


        if (StringUtils.isBlank(retainGroupAndAppAdmin) || "1".equals(retainGroupAndAppAdmin)) {
            queryWrapper.inSql("id", "select user_id from sys_user_admin_org where ROLE_TYPE !=44 and  ROLE_TYPE !=4");
        } else if ("2".equals(retainGroupAndAppAdmin)) {
            queryWrapper.inSql("id", "select user_id from sys_user_admin_org where ROLE_TYPE =44");
        } else if ("3".equals(retainGroupAndAppAdmin)) {
            queryWrapper.inSql("id", "select user_id from sys_user_admin_org where ROLE_TYPE =4");
        }
        List<SysUserDO> list = sysUserService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            list.stream().forEach(sysUserDO -> {
                //  集团授控域
                this.getUserControlDomain(sysUserDO, "1", "unactivated");
                List<SysUserAdminOrgDO> sysUserAdminOrgDOS = sysUserAdminOrgService.list(new
                        QueryWrapper<SysUserAdminOrgDO>()
                        .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));
                sysUserDO.setRoleType(sysUserAdminOrgDOS.get(0).getRoleType());
            });
            return list;
        }
        return new ArrayList<>(16);
    }

    /**
     * 激活集团管理员权限
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean setGroupActivated(Map<String, String> map) {
        String id = map.get("id");
        String roleType = map.get("roleType");

        // 通过RoleType获得RoleId
        SysRoleDO role = sysRoleService.getOne(new QueryWrapper<SysRoleDO>()
                .eq("role_type", roleType));

        SysUserDO sysUserDO = sysUserService.getById(id);
        // 用户类型从带激活状态变为正常使用状态
        String retainGroupAndAppAdmin = sysConfigService.getValue("retainGroupAndAppAdmin");
        if ("3".equals(retainGroupAndAppAdmin)) {
            sysUserDO.setUserType(5);
        } else {
            sysUserDO.setUserType(1);
        }
        if (!sysUserService.updateById(sysUserDO)) {
            return false;
        }

        // 当第一次分配权限时
        if (sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                .eq("user_id", id).eq("is_latest", 1)) == 0) {
            // 添加用户角色表数据
            saveUserRole(role.getAppId(), id, role.getId(), role.getName(), sysUserDO.getRealName());
        }

        // 添加有效权限表数据
        sysAuthMixService.saveAuthMix(id);


        // 删除旧的系统管理-用户-可管理的组织
        sysUserAdminOrgService.remove(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 1).eq("user_id", id));

        List<SysUserAdminOrgDO> sysUserAdminOrgDOS = sysUserAdminOrgService.list(new
                QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));
        if (sysUserAdminOrgDOS != null && sysUserAdminOrgDOS.size() > 0) {
            sysUserAdminOrgDOS.stream().forEach(sysUserAdminOrgDO -> {
                sysUserAdminOrgDO.setActivated(1);
            });

            if (!sysUserAdminOrgService.updateBatchById(sysUserAdminOrgDOS)) {
                return false;
            }
        }

        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "激活"), "激活集团级管理员权限", "集团级管理员【" +
                sysUserDO.getRealName() + "】激活权限");
    }

    /**
     * 获取未分配人员列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public List<SysUserDO> getUnallocatedData(Map<String, Object> map) {
        List<SysUserDO> list = new ArrayList<>(16);
        String type = (String) map.get("type");
        if (Objects.equals("1", type)) {
            // 集团级
            list = sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("is_delete", 0)
                    .eq("start_flag", "1")
                    .eq("user_type", 111));
        } else if (Objects.equals("2", type)) {
            String groupId = (String) map.get("groupId");
            // 应用级
            list = sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("group_id", groupId)
                    .eq("is_delete", 0)
                    .eq("start_flag", "1")
                    .eq("user_type", 222));
        } else if (Objects.equals("4", type)) {
            // 业务管理员
            list = sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("is_delete", 0)
                    .eq("start_flag", "1")
                    .eq("user_type", 555));
        }
        return list;
    }

    /**
     * 添加三员管理成员
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean saveThreeRole(Map<String, Object> map) {
        SysUserDO sysUserDO = JsonUtils.castObjectForSetIdNull(map, SysUserDO.class);
        sysUserDO.setIpBind(1);
        sysUserService.saveUser(sysUserDO);

        return true;
    }

    /**
     * 修改三员管理成员
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public R updateThreeRole(Map<String, Object> map) {

        String id = (String) map.get("id");
        String realName = (String) map.get("realName");
        String ipAddress = (String) map.get("ipAddress");
        List<String> ipAddressList = new ArrayList<>(16);
        if (ipAddress.contains(";")) {
            ipAddressList = Arrays.asList(ipAddress.split(";"));
        } else {
            ipAddressList.add(ipAddress);
        }
        SysUserDO sysUserDO = sysUserService.getById(id);
        if (sysUserDO != null) {
            sysUserDO.setRealName(realName);
            sysUserDO.setIpAddress(ipAddress);

            // 校验管理员ip地址
            if (Objects.equals(1, sysUserDO.getUserType())) {
                // 验证：集团级管理员  拥有同一集团管控域时，三员角色账号之间的ip地址不能相同

                // 获取当前用户的集团管控域
                List<String> groupIds = sysUserAdminOrgService.list(new
                        QueryWrapper<SysUserAdminOrgDO>()
                        .eq("is_activated", 1).eq("user_id", id))
                        .stream().distinct().map(SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());

                // 获取当前用户角色
                String roleId = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>()
                        .eq("user_id", id)).getRoleId();

                // 获取不同角色的集团管理员的UserId
                List<String> roleIds = new ArrayList<>(16);
                roleIds.add("groupadmin");
                roleIds.add("groupsec");
                roleIds.add("groupauditor");
                List<String> roleWithUserIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .ne("role_id", roleId).in("role_id", roleIds))
                        .stream().distinct().map(SysUserRoleDO::getUserId).collect(Collectors.toList());

                if (roleWithUserIds != null && roleWithUserIds.size() > 0) {
                    // 获取相同ip地址的集团管理员的UserId
                    List<SysUserDO> ipWithUsers = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                            .eq("user_type", 1).in("id", roleWithUserIds));
                    if (CollectionUtils.isNotEmpty(ipWithUsers)) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (SysUserDO ipWithUser : ipWithUsers) {
                            ipAddressList.stream().forEach(address -> {
                                // 判断拥有相同IP地址
                                if (StringUtils.isNotBlank(ipWithUser.getIpAddress())) {
                                    if (ipWithUser.getIpAddress().indexOf(address) > -1) {
                                        // 判断是否拥有相同集团授控域
                                        if (sysUserAdminOrgService.count(new QueryWrapper<SysUserAdminOrgDO>()
                                                .eq("is_activated", 1).eq("user_id", ipWithUser.getId())
                                                .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                                                .in("ORGANIZATION_ID", groupIds)) > 0) {
                                            stringBuffer.append(ipWithUser.getUserName());
                                            stringBuffer.append(",");
                                        }
                                    }
                                }
                            });
                        }

                        if (stringBuffer.length() > 0) {
                            throw new RException(String.format(InternationUtils.getInternationalMsg
                                    ("SAME_IP_AND_DIFFERENT_ROLE_ID_EXIST"), stringBuffer.substring(0, stringBuffer
                                    .length() - 1)));
                        }
                    }
                }
            }
            if (Objects.equals(2, sysUserDO.getUserType())) {
                // 验证：应用级管理员  拥有同一应用管控域时，三员角色账号之间的ip地址不能相同

                // 获取当前用户的应用管控域
                List<String> appIds = sysAuthScopeAppService.list(new
                        QueryWrapper<SysAuthScopeAppDO>()
                        .eq("is_activated", 1).eq("user_id", id))
                        .stream().map(SysAuthScopeAppDO::getAppId).distinct().collect(Collectors.toList());

                // 获取当前用户角色
                String roleId = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>()
                        .eq("user_id", id)).getRoleId();

                // 获取不同角色的集团管理员的UserId
                List<String> roleIds = new ArrayList<>(16);
                roleIds.add("appadmin");
                roleIds.add("appsec");
                roleIds.add("appauditor");
                List<String> roleWithUserIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .ne("role_id", roleId).in("role_id", roleIds))
                        .stream().distinct().map(SysUserRoleDO::getUserId).collect(Collectors.toList());

                if (roleWithUserIds != null && roleWithUserIds.size() > 0) {
                    // 获取相同ip地址的应用管理员的UserId
                    List<SysUserDO> ipWithUsers = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                            .eq("user_type", 2).in("id", roleWithUserIds));
                    if (CollectionUtils.isNotEmpty(ipWithUsers)) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (SysUserDO sysUserDO1 : ipWithUsers) {
                            ipAddressList.stream().forEach(address -> {
                                // 判断拥有相同IP地址
                                if (StringUtils.isNotBlank(sysUserDO1.getIpAddress())) {
                                    if (sysUserDO1.getIpAddress().indexOf(address) > -1) {
                                        // 判断是否拥有相同集团授控域
                                        if (sysAuthScopeAppService.count(new QueryWrapper<SysAuthScopeAppDO>()
                                                .eq("is_activated", 1).eq("user_id", sysUserDO1.getId())
                                                .and(appIds == null || appIds.size() == 0, i -> i.eq("1", "2"))
                                                .in("app_id", appIds)) > 0) {
                                            stringBuffer.append(sysUserDO1.getUserName());
                                            stringBuffer.append(",");
                                        }
                                    }
                                }
                            });
                        }
                        if (stringBuffer.length() > 0) {
                            throw new RException(String.format(InternationUtils.getInternationalMsg
                                    ("SAME_IP_AND_DIFFERENT_ROLE_ID_EXIST"), stringBuffer.substring(0, stringBuffer
                                    .length() - 1)));
                        }
                    }
                }
            }

            sysUserService.updateUser(sysUserDO);

        } else {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 获取已激活的集团三员管理员列表
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public Map<String, Object> getGroupAllocatedData() {
        List<SysUserDO> allocatedDataList = new ArrayList<>(16);
        String retainGroupAndAppAdmin = sysConfigService.getValue("retainGroupAndAppAdmin");
        if (StringUtils.isBlank(retainGroupAndAppAdmin) || "1".equals(retainGroupAndAppAdmin)) {
            List<SysUserDO> sysManagerList;
            List<SysUserDO> securityManagerList;
            List<SysUserDO> auditManagerList;
            // 集团系统管理员
            sysManagerList = getOrgThreeUsersByRoleType("11", "1", "activated");
            // 集团安全保密员
            securityManagerList = getOrgThreeUsersByRoleType("22", "1", "activated");
            // 集团安全审计员
            auditManagerList = getOrgThreeUsersByRoleType("33", "1", "activated");

            allocatedDataList.addAll(sysManagerList);
            allocatedDataList.addAll(securityManagerList);
            allocatedDataList.addAll(auditManagerList);
        } else if ("2".equals(retainGroupAndAppAdmin)) {
            allocatedDataList = getOrgThreeUsersByRoleType("44", "1", "activated");
        } else if ("3".equals(retainGroupAndAppAdmin)) {
            allocatedDataList = getOrgThreeUsersByRoleType("4", "1", "activated");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("allocatedDataList", allocatedDataList);

        return map;
    }

    /**
     * 获取已激活和未激活的集团三员管理员列表
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public Map<String, Object> getGroupAllThreeData() {
        String retainGroupAndAppAdmin = sysConfigService.getValue("retainGroupAndAppAdmin");
        List<SysUserDO> allThreeDataList = new ArrayList<>(16);
        if (StringUtils.isBlank(retainGroupAndAppAdmin) || "1".equals(retainGroupAndAppAdmin)) {//集团三员
            List<SysUserDO> sysManagerList;
            List<SysUserDO> securityManagerList;
            List<SysUserDO> auditManagerList;
            // 集团系统管理员
            sysManagerList = getOrgThreeUsersByRoleType("11", "1", "all");
            // 集团安全保密员
            securityManagerList = getOrgThreeUsersByRoleType("22", "1", "all");
            // 集团安全审计员
            auditManagerList = getOrgThreeUsersByRoleType("33", "1", "all");

            allThreeDataList.addAll(sysManagerList);
            allThreeDataList.addAll(securityManagerList);
            allThreeDataList.addAll(auditManagerList);
        } else if ("2".equals(retainGroupAndAppAdmin)) {
            // 集团超级管理员
            allThreeDataList = getOrgThreeUsersByRoleType("44", "1", "all");
        } else if ("3".equals(retainGroupAndAppAdmin)) {
            // 单一业务管理员
            allThreeDataList = getOrgThreeUsersByRoleType("4", "1", "all");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("allThreeDataList", allThreeDataList);
        return map;
    }

    /**
     * 获取应用级待激活人员列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public List<SysUserDO> getAppToBeActivatedData(Map<String, Object> params) {
        String groupId = (String) params.get("groupId");
        // 获取应用级待激活人员列表
        List<SysUserDO> list = sysUserService.list(new QueryWrapper<SysUserDO>()
                .eq("group_id", groupId).eq("is_delete", 0)
                .eq("start_flag", "1").eq("user_type", 22));
        if (list != null && list.size() > 0) {
            list.stream().forEach(sysUserDO -> {
                this.getUserControlDomain(sysUserDO, "2", "unactivated");
                SysAuthScopeAppDO sysAuthScopeAppDO = sysAuthScopeAppService.getOne(new
                        QueryWrapper<SysAuthScopeAppDO>().eq("is_activated", 0)
                        .eq("user_id", sysUserDO.getId()));
                if (sysAuthScopeAppDO != null) {
                    sysUserDO.setRoleType(sysAuthScopeAppDO.getRoleType());
                }
            });
            return list.stream().sorted(Comparator.comparing(SysUserDO::getUserName)).collect(Collectors.toList());
        }
        return new ArrayList<>(16);
    }


    /**
     * 激活应用管理员权限
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean setAppActivated(Map<String, String> map) {
        String id = map.get("id");
        String roleType = map.get("roleType");

        // 通过RoleType获得RoleId
        SysRoleDO role = sysRoleService.getOne(new QueryWrapper<SysRoleDO>()
                .eq("role_type", roleType));

        SysUserDO sysUserDO = sysUserService.getById(id);
        // 用户类型从带激活状态变为正常使用状态
        sysUserDO.setUserType(2);
        if (!sysUserService.updateById(sysUserDO)) {
            return false;
        }

        // 当第一次分配权限时
        if (sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                .eq("user_id", id).eq("is_latest", 1)) == 0) {
            // 添加用户角色表数据
            saveUserRole(role.getAppId(), id, role.getId(), role.getName(), sysUserDO.getRealName());
        }

        // 添加有效权限表数据
        sysAuthMixService.saveAuthMix(id);

        // 激活应用管控域

        // 删除该用户老的权限受控范围
        sysAuthScopeAppService.remove(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("is_activated", 1).eq("user_id", id));
        sysAuthScopeOrgService.remove(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("is_activated", 1).eq("user_id", id));
        sysAuthScopeUserGroupService.remove(new QueryWrapper<SysAuthScopeUserGroupDO>()
                .eq("is_activated", 1).eq("user_id", id));

        List<SysAuthScopeAppDO> sysAuthScopeAppDOS = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));
        if (sysAuthScopeAppDOS != null && sysAuthScopeAppDOS.size() > 0) {
            sysAuthScopeAppDOS.stream().forEach(sysAuthScopeAppDO -> {
                sysAuthScopeAppDO.setActivated(1);
            });
            if (!sysAuthScopeAppService.updateBatchById(sysAuthScopeAppDOS)) {
                return false;
            }
        }


        // 激活组织管控域
        List<SysAuthScopeOrgDO> sysAuthScopeOrgDOS = sysAuthScopeOrgService.list(new QueryWrapper<SysAuthScopeOrgDO>()
                .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));
        if (sysAuthScopeOrgDOS != null && sysAuthScopeOrgDOS.size() > 0) {
            sysAuthScopeOrgDOS.stream().forEach(sysAuthScopeOrgDO -> {
                sysAuthScopeOrgDO.setActivated(1);
            });
            if (!sysAuthScopeOrgService.updateBatchById(sysAuthScopeOrgDOS)) {
                return false;
            }
        }

        // 激活用户组管控域
        List<SysAuthScopeUserGroupDO> sysAuthScopeUserGroupDOS = sysAuthScopeUserGroupService.list(new
                QueryWrapper<SysAuthScopeUserGroupDO>()
                .eq("is_activated", 0).eq("user_id", sysUserDO.getId()));
        if (sysAuthScopeUserGroupDOS != null && sysAuthScopeUserGroupDOS.size() > 0) {
            sysAuthScopeUserGroupDOS.stream().forEach(sysAuthScopeUserGroupDO -> {
                sysAuthScopeUserGroupDO.setActivated(1);
            });
            if (!sysAuthScopeUserGroupService.updateBatchById(sysAuthScopeUserGroupDOS)) {
                return false;
            }
        }

        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "激活"), "激活应用级管理员权限", "应用级管理员【" +
                        sysUserDO.getRealName() + "】激活权限", sysUserDO
                        .getGroupId(),
                null);
    }


    /**
     * 获取已激活的应用三员管理员列表
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public Map<String, Object> getAppAllocatedData(Map<String, Object> params) {
        String groupId = (String) params.get("groupId");
        List<SysUserDO> sysManagerList;
        List<SysUserDO> securityManagerList;
        List<SysUserDO> auditManagerList;

        List<SysUserDO> allocatedDataList = new ArrayList<>(16);
        // 集团系统管理员
        sysManagerList = getAppThreeUsersByRoleType("111", "2", "activated", groupId);
        // 集团安全保密员
        securityManagerList = getAppThreeUsersByRoleType("222", "2", "activated", groupId);
        // 集团安全审计员
        auditManagerList = getAppThreeUsersByRoleType("333", "2", "activated", groupId);

        allocatedDataList.addAll(sysManagerList);
        allocatedDataList.addAll(securityManagerList);
        allocatedDataList.addAll(auditManagerList);
        Map<String, Object> map = new HashMap<>();
        map.put("allocatedDataList", allocatedDataList);
//        map.put("securityManagerList", securityManagerList);
//        map.put("auditManagerList", auditManagerList);
        return map;
    }

    /**
     * 获取已激活和未激活的应用三员管理员列表
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public Map<String, Object> getAppAllThreeData(Map<String, Object> params) {
        String retainGroupAndAppAdmin = sysConfigService.getValue("retainGroupAndAppAdmin");
        List<SysUserDO> allThreeDataList = new ArrayList<>(16);
        String groupId = (String) params.get("groupId");
        if (StringUtils.isBlank(retainGroupAndAppAdmin) || "1".equals(retainGroupAndAppAdmin)) {

            List<SysUserDO> sysManagerList;
            List<SysUserDO> securityManagerList;
            List<SysUserDO> auditManagerList;
            // 集团系统管理员
            sysManagerList = getAppThreeUsersByRoleType("111", "2", "all", groupId);
            // 集团安全保密员
            securityManagerList = getAppThreeUsersByRoleType("222", "2", "all", groupId);
            // 集团安全审计员
            auditManagerList = getAppThreeUsersByRoleType("333", "2", "all", groupId);

            allThreeDataList.addAll(sysManagerList);
            allThreeDataList.addAll(securityManagerList);
            allThreeDataList.addAll(auditManagerList);
        } else {
            allThreeDataList = getAppThreeUsersByRoleType("444", "2", "all", groupId);
        }


        Map<String, Object> map = new HashMap<>();
        map.put("allThreeDataList", allThreeDataList);
        return map;
    }

    /**
     * 通过用户ID获取角色信息(激活与未激活)
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public List<SysRoleDO> getRolesByUserId(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String appId = (String) params.get("appId");
        List<String> roleIds = list(new QueryWrapper<SysUserRoleDO>()
                .eq("app_id", appId)
                .eq("user_id", userId)).stream().map(SysUserRoleDO::getRoleId)
                .collect(Collectors.toList());

        List<SysRoleDO> sysRoleDOS = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                .and(roleIds == null || roleIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", roleIds));
        return sysRoleDOS;
    }

    /**
     * 通过角色ID获取用户信息
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public List<SysUserDO> getUsersByRoleId(Map<String, Object> params) {
        String roleId = (String) params.get("roleId");
//        String appId = (String) params.get("appId");
        List<SysUserDO> list = sysUserService.list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index")
                .eq("user_type", 3)
                .inSql("id", "select user_id from sys_user_role where role_id = '" + roleId + "'")
//                + "' and app_id ='" + appId
                .eq("start_flag", 1).eq("is_delete", 0));
        return list;

    }

    /**
     * 通过角色ID获取有效用户信息
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public List<SysUserDO> getActivedUsersByRoleId(Map<String, Object> params) {
        String roleId = (String) params.get("roleId");
        List<String> userIds2 = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("IS_LATEST", 1).isNull("app_id").inSql("id", "select lv_id from sys_user_role_v where " +
                        "role_id='" + roleId + "'")).stream().map(SysUserRoleLvDO::getUserId).collect
                (Collectors.toList());
        List<SysUserDO> list;
        if (CollectionUtils.isNotEmpty(userIds2)) {
            list = sysUserService.list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index")
                    .eq("user_type", 3)
                    .in("id", userIds2)
//                + "' and app_id ='" + appId
                    .eq("start_flag", 1).eq("is_delete", 0));
        } else {
            return new ArrayList<>(16);
        }
        return list;
    }

    /**
     * 用户指派角色
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public R saveUserIdAndRoleIds(Map<String, Object> map) {
        String userId = (String) map.get("userId");
        String realName = (String) map.get("realName");
        String appId = (String) map.get("appId");
        List<JSONObject> roles = JsonUtils.castObject(map.get("roles"), List.class);
        // List<LinkedHashMap> roles = (List<LinkedHashMap>) map.get("roles");
        List<String> roleIds = new ArrayList<>(16);
        roles.stream().forEach(role -> {
            roleIds.add((String) role.get("id"));
        });
        if (CollectionUtils.isNotEmpty(roles)) {
            if (sysRoleMutexService.count(new QueryWrapper<SysRoleMutexDO>()
                    .in("ROLE_ID", roleIds).in("ROLE_MUTEX_ID", roleIds)) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SELECT_ROLE_MUTEX_RELATION"));
            }
        }

        List<String> oldRoleIds = list(new QueryWrapper<SysUserRoleDO>().eq("user_id", userId).eq("app_id", appId))
                .stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
        if (oldRoleIds == null) {
            oldRoleIds = new ArrayList<>();
        }

        // 判断当前用户分配的角色是否发生变化
        if (roleIds.size() == oldRoleIds.size()) {
            if (roleIds.size() == 0) {// size等于0时，说明无变化，不允许改变
                return R.error(InternationUtils.getInternationalMsg("USER_ROLE_RELATION_NOT_CHANGE"));
            } else {
                //判断两个list是否相同
                if (roleIds.containsAll(oldRoleIds)) {
                    return R.error(InternationUtils.getInternationalMsg("USER_ROLE_RELATION_NOT_CHANGE"));
                }
            }
        }
//        else {
//            if (roleIds.size() > 0) {
//                boolean flg = true;
//                for (String roleId1 : roleIds) {
//                    if (!oldRoleIds.contains(roleId1)) {
//                        flg = false;
//                    }
//                }
//                if (flg) {
//                    return R.error(InternationUtils.getInternationalMsg("USER_ROLE_RELATION_NOT_CHANGE"));
//                }
//            }
//
//        }


        oldRoleIds.addAll(roleIds);
        if (CollectionUtils.isNotEmpty(oldRoleIds)) {
            if (sysRoleMutexService.count(new QueryWrapper<SysRoleMutexDO>()
                    .in("ROLE_ID", oldRoleIds).in("ROLE_MUTEX_ID", oldRoleIds)) > 0) {
                return R.error(InternationUtils.getInternationalMsg("ROLE_MUTEX_RELATION"));
            }
        }


        // 添加到待激活的用户角色收取的用户表中
        if (sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("APP_ID", appId).eq("USER_ID", userId)) == 0) {
            if (!sysWaitGrantUserService.saveSysWaitGrantUser(appId, userId)) {
                throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
            }
        }

        // 去除旧的用户角色关系表
        remove(new QueryWrapper<SysUserRoleDO>().eq("user_id", userId).eq("app_id", appId));

        // 保存新的用户角色关系表
        List<SysUserRoleDO> sysUserRoleDOs = new ArrayList<>(16);
        roles.stream().forEach(role -> {
            SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
            sysUserRoleDO.setId(UuidUtils.createUUID());
            sysUserRoleDO.setUserId(userId);
            sysUserRoleDO.setRoleId((String) role.get("id"));
            sysUserRoleDO.setAppId(appId);
            sysUserRoleDO.setRoleName((String) role.get("name"));
            sysUserRoleDO.setRealName(realName);
            sysUserRoleDO.setCreateUser(securityUtils.getCurrentUserId());
            sysUserRoleDO.setCreateTime(LocalDateTime.now());
            sysUserRoleDOs.add(sysUserRoleDO);
        });
        SysUserDO sysUserDO = sysUserService.getById(userId);
        if (sysUserRoleDOs != null && sysUserRoleDOs.size() > 0) {
            if (saveBatch(sysUserRoleDOs)) {
                List<String> roleNames = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                        .in("id", roleIds)).stream().map(SysRoleDO::getName).collect(Collectors.toList());
                StringBuffer stringBuffer = new StringBuffer();
                roleNames.stream().forEach(name -> {
                    stringBuffer.append(name);
                    stringBuffer.append(",");
                });
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "用户指派角色",
                        "用户指派角色：用户【" + sysUserDO.getRealName() + "】拥有【" + stringBuffer
                                .substring(0, stringBuffer.length() - 1) + "】角色", securityUtils
                                .getCurrentGroupId(), appId)) {
                    return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
            }
        }
        if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "用户指派角色", "用户指派角色：用户【" +
                sysUserDO.getRealName() + "】清空角色", securityUtils
                .getCurrentGroupId(), appId)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        } else {
            throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
    }

    @Override
    public R saveRoleIdAndUserIdsForDep(SysRoleDO role, List<SysUserDO> users) {
        String roleName = role.getName();
        String roleId = role.getId();
        String appId = role.getAppId();

        List<String> userIds = users.stream().map(SysUserDO::getId).collect(Collectors.toList());
        List<String> userIds2 = list(new QueryWrapper<SysUserRoleDO>()
                .eq("role_id", roleId).eq("app_id", appId))
                .stream().map(SysUserRoleDO::getUserId).collect(Collectors.toList());
        // 判断角色指派用户关系是否有改变
        if (userIds2.size() == userIds.size()) {
            if (userIds.size() == 0) {
                throw new RException(String.format(InternationUtils.getInternationalMsg
                        ("ROLE_USER_RELATION_NOT_CHANGE")));
            } else {
                if (userIds.containsAll(userIds2)) {
                    throw new RException(String.format(InternationUtils.getInternationalMsg
                            ("ROLE_USER_RELATION_NOT_CHANGE")));
                }
            }
        }

        for (SysUserDO user : users) {
            List<String> roleIds = list(new QueryWrapper<SysUserRoleDO>().eq("user_id", user.getId()).eq
                    ("app_id", appId))
                    .stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roleIds)) {
                if (sysRoleMutexService.count(new QueryWrapper<SysRoleMutexDO>()
                        .eq("role_id", roleId).in("ROLE_MUTEX_ID", roleIds)) > 0 ||
                        sysRoleMutexService.count(new QueryWrapper<SysRoleMutexDO>()
                                .eq("ROLE_MUTEX_ID", roleId).in("role_id", roleIds)) > 0) {
                    return R.error(String.format(InternationUtils.getInternationalMsg
                            ("ROLE_MUTEX_USER_ROLE_RELATION"), user.getUserName()));
                }
            }

        }

        // 获取旧的角色有效用户关系表
        List<String> oldUserIds = this.getEffectiveUserData(roleId);


        // 获取角色新添加的用户
        userIds.forEach(newUserId -> {
            if (!oldUserIds.contains(newUserId)) {
                if (sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                        .eq("APP_ID", appId).eq("USER_ID", newUserId)) == 0) {
                    // 添加的用户需要重新激活有效权限
                    sysWaitGrantUserService.saveSysWaitGrantUser(appId, newUserId);
                }
            }
        });

        // 保存新的用户角色关系表
        List<SysUserRoleDO> sysUserRoleDOs = new ArrayList<>(16);
        users.stream().forEach(user -> {
            SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
            sysUserRoleDO.setId(user.getId() + roleId.substring(0, 6));
            sysUserRoleDO.setUserId(user.getId());
            sysUserRoleDO.setRealName(user.getRealName());
            sysUserRoleDO.setRoleName(roleName);
            sysUserRoleDO.setRoleId(roleId);
            sysUserRoleDO.setAppId(appId);
            sysUserRoleDO.setCreateUser(securityUtils.getCurrentUserId());
            sysUserRoleDO.setCreateTime(LocalDateTime.now());
            sysUserRoleDOs.add(sysUserRoleDO);
        });
        if (sysUserRoleDOs != null && sysUserRoleDOs.size() > 0) {
            if (saveOrUpdateBatch(sysUserRoleDOs)) {
                List<List<String>> list = JDBCUtil.getListGroupBy(userIds);
                List<String> userNames = new ArrayList<>(16);
                list.stream().forEach(ids -> {
                    userNames.addAll(sysUserService.list(new QueryWrapper<SysUserDO>()
                            .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                            .in("id", ids).select("real_name")).stream().map(SysUserDO::getRealName).collect
                            (Collectors.toList()));
                });
                StringBuffer stringBuffer = new StringBuffer();
                userNames.stream().forEach(name -> {
                    stringBuffer.append(name);
                    stringBuffer.append(",");
                });
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "角色指派用户",
                        "角色指派用户：角色【" + roleName + "】拥有【" + stringBuffer
                                .substring(0, stringBuffer.length() - 1) + "】用户", securityUtils
                                .getCurrentGroupId(), appId)) {
                    return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
            }
        }
        if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "角色指派用户", "角色指派用户：角色【" +
                        roleName + "】清空用户",
                securityUtils
                        .getCurrentGroupId(), appId)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        } else {
            throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
    }


    /**
     * 全局角色激活用户
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean activeScopeRoleIdAndUserIds(Map<String, Object> map) {
        String roleId = (String) map.get("roleId");
        List<JSONObject> users = JsonUtils.castObject(map.get("users"), List.class);

        List<String> userIds = new ArrayList<>(16);
        users.stream().forEach(user -> {
            userIds.add((String) user.get("id"));
        });

        // 获取旧的角色有效用户关系表
        List<String> oldUserIds = this.getEffectiveUserData(roleId);

        // 重新需要激活权限的人员列表
        List<String> beActivedUserIds = new ArrayList<>(16);

        // 获取被删除的该角色对应的用户
        oldUserIds.forEach(oldUserId -> {
            if (!userIds.contains(oldUserId)) {
                beActivedUserIds.add(oldUserId);
            }
        });

        // 获取角色新添加的用户
        userIds.forEach(newUserId -> {
            if (!oldUserIds.contains(newUserId)) {
                beActivedUserIds.add(newUserId);
            }
        });

        List<String> appIds = new ArrayList<>(16);

        // 判断该角色与哪些应用下的权限有关联
        List<String> authIds = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                .eq("role_id", roleId)).stream().map(SysAuthRoleDO::getAuthId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(authIds)) {
            appIds = sysAuthService.listByIds(authIds).stream().map(SysAuthDO::getAppId).collect
                    (Collectors.toList());
        }
        // 角色用户关系激活
        if (CollectionUtils.isNotEmpty(beActivedUserIds)) {
            for (String userId : beActivedUserIds) {
                if (!this.scopeRoleUserActive(userId, appIds)) {
                    return false;
                }
            }


        }

        return true;
    }

    @Override
    public boolean scopeRoleUserActive(String userId, List<String> appIds) {
        // 角色用户关系激活
        List<SysUserRoleDO> sysUserRoleDOs = list(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId).isNull("app_id"));
        // 获取当前最新版本号
        Integer version = -1;
        // 判断用户角色版本表是否有数据
        SysUserRoleLvDO sysUserRoleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                .eq("user_id", userId).isNull("app_id").eq("is_latest", 1));
        if (sysUserRoleLvDO != null) {
            version = sysUserRoleLvDO.getVersion();
        }

        sysUserRoleLvService.update(new SysUserRoleLvDO(), new UpdateWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1).eq("user_id", userId).isNull("app_id")
                .set("is_latest", 0));

        // 添加用户角色版本表数据
        SysUserRoleLvDO lateSysUserRoleLvDO = new SysUserRoleLvDO();
        lateSysUserRoleLvDO.setId(UuidUtils.createUUID());
        lateSysUserRoleLvDO.setUserId(userId);
        lateSysUserRoleLvDO.setVersion(version + 1);
        lateSysUserRoleLvDO.setLatest(1);
        lateSysUserRoleLvDO.setAppId(null);
        lateSysUserRoleLvDO.setCreateTime(LocalDateTime.now());
        lateSysUserRoleLvDO.setCreateUser(securityUtils.getCurrentUserId());
        if (!sysUserRoleLvService.save(lateSysUserRoleLvDO)) {
            return false;
        }

        if (CollectionUtils.isNotEmpty(sysUserRoleDOs)) {
            // 添加用户角色履历表数据
            List<SysUserRoleVDO> sysUserRoleVDOS = new ArrayList<>(16);
            sysUserRoleDOs.stream().forEach(sysUserRoleDO -> {
                SysUserRoleVDO sysUserRoleVDO = new SysUserRoleVDO();
                sysUserRoleVDO.setId(UuidUtils.createUUID());
                sysUserRoleVDO.setRoleId(sysUserRoleDO.getRoleId());
                sysUserRoleVDO.setRoleName(sysUserRoleDO.getRoleName());
                sysUserRoleVDO.setLvId(lateSysUserRoleLvDO.getId());
                sysUserRoleVDOS.add(sysUserRoleVDO);
            });
            if (CollectionUtils.isNotEmpty(sysUserRoleVDOS)) {
                if (!sysUserRoleVService.saveBatch(sysUserRoleVDOS)) {
                    return false;
                }

            }
        }
        // 通过遍历应用域下所有appId,然后添加用户与该应用的权限待激活表信息，最后进入应用内进行激活
        // 当前用户为集团管理员，单一业务管理员
        if (securityUtils.getCurrentUser().getUserType() != 1 && securityUtils.getCurrentUser().getUserType() != 5) {
            return false;
        }
//        List<String> appIds = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
//                .isNotNull("GROUP_ID").select("id")).stream().map
//                (AbstractBaseDomain::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(appIds)) {
            appIds.stream().forEach(appId -> {
//                        if (sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
//                                .eq("type", 0).eq("ACTIVATE_ID", userId)
//                                .eq("APP_ID", appId)) == 0) {
//                            sysWaitGrantAuthService.saveSysWaitGrantAuth(userId, 0, appId);
//                        }
                // 该用户重新编辑有效权限
                sysAuthMixService.saveAuthMixForApp(userId, appId);
            });
        }
        return true;
    }


    /**
     * 角色指派用户
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public Map<String, Object> saveRoleIdAndUserIds(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();

        // 获取变更关系的用户ID列表
        List<String> changeUserIds = new ArrayList<>(16);

        String roleName = (String) map.get("roleName");
        String roleId = (String) map.get("roleId");
        String appId = (String) map.get("appId");
        int roleScope = (Integer) map.get("roleScope");
        List<JSONObject> users = JsonUtils.castObject(map.get("users"), List.class);

        List<String> userIds = new ArrayList<>(16);
        users.stream().forEach(user -> {
            userIds.add((String) user.get("id"));
        });
        List<String> userIds2 = list(new QueryWrapper<SysUserRoleDO>()
                .eq("role_id", roleId))
//                .eq("app_id", appId)
                .stream().map(SysUserRoleDO::getUserId).collect(Collectors.toList());
        // 判断角色指派用户关系是否有改变
        if (userIds2.size() == userIds.size()) {
            if (userIds.size() == 0) {
                result.put("error", String.format(InternationUtils.getInternationalMsg(
                        "ROLE_USER_RELATION_NOT_CHANGE")));
                return result;
            } else {
                if (userIds.containsAll(userIds2)) {
                    result.put("error", String.format(InternationUtils.getInternationalMsg(
                            "ROLE_USER_RELATION_NOT_CHANGE")));
                    return result;
                }
            }
        }
//        else {
//            if (userIds.size() > 0) {
//                boolean flg = true;
//                for (String id : userIds) {
//                    if (!userIds2.contains(id)) {
//                        flg = false;
//                    }
//                }
//                if (flg) {
//                    return R.error(String.format(InternationUtils.getInternationalMsg
//                            ("ROLE_USER_RELATION_NOT_CHANGE")));
//                }
//            }
//        }
        if (roleScope != 1) {
            for (JSONObject user : users) {
                List<String> roleIds = list(new QueryWrapper<SysUserRoleDO>()
                        .eq("user_id", user.get("id")).eq("app_id", appId))

                        .stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(roleIds)) {
                    if (sysRoleMutexService.count(new QueryWrapper<SysRoleMutexDO>()
                            .eq("role_id", roleId).in("ROLE_MUTEX_ID", roleIds)) > 0 ||
                            sysRoleMutexService.count(new QueryWrapper<SysRoleMutexDO>()
                                    .eq("ROLE_MUTEX_ID", roleId).in("role_id", roleIds)) > 0) {
                        result.put("error", String.format(InternationUtils.getInternationalMsg
                                ("ROLE_MUTEX_USER_ROLE_RELATION"), (String) user.get("name")));
                        return result;
                    }
                }

            }

            // 获取旧的角色有效用户关系表
            List<String> oldUserIds = this.getEffectiveUserData(roleId);


            // 获取被删除的该角色对应的用户
            oldUserIds.forEach(oldUserId -> {
                if (!userIds.contains(oldUserId)) {
                    if (sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                            .eq("APP_ID", appId).eq("USER_ID", oldUserId)) == 0) {
                        // 被删除角色的用户需要重新激活有效权限
                        changeUserIds.add(oldUserId);
                        sysWaitGrantUserService.saveSysWaitGrantUser(appId, oldUserId);
                    }
                }
            });

            // 获取角色新添加的用户
            userIds.forEach(newUserId -> {
                if (!oldUserIds.contains(newUserId)) {
                    if (sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                            .eq("APP_ID", appId).eq("USER_ID", newUserId)) == 0) {
                        // 添加的用户需要重新激活有效权限
                        changeUserIds.add(newUserId);
                        sysWaitGrantUserService.saveSysWaitGrantUser(appId, newUserId);
                    }
                }
            });
        }

        result.put("changeUserIds", changeUserIds);


        // 去除旧的用户角色关系表
        remove(new QueryWrapper<SysUserRoleDO>().eq("role_id", roleId));
//.eq("app_id", appId)

        // 保存新的用户角色关系表
        List<SysUserRoleDO> sysUserRoleDOs = new ArrayList<>(16);
        if (CollectionUtils.isNotEmpty(users)) {
            users.stream().forEach(user -> {
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setId(UuidUtils.createUUID());
                sysUserRoleDO.setUserId((String) user.get("id"));
                sysUserRoleDO.setRealName((String) user.get("name"));
                sysUserRoleDO.setRoleName(roleName);
                sysUserRoleDO.setRoleId(roleId);
                sysUserRoleDO.setAppId(appId);
                sysUserRoleDO.setCreateUser(securityUtils.getCurrentUserId());
                sysUserRoleDO.setCreateTime(LocalDateTime.now());
                sysUserRoleDOs.add(sysUserRoleDO);
            });
        }
        if (sysUserRoleDOs != null && sysUserRoleDOs.size() > 0) {
            if (saveBatch(sysUserRoleDOs)) {

                List<List<String>> list = JDBCUtil.getListGroupBy(userIds);
                List<String> userNames = new ArrayList<>(16);
                list.stream().forEach(ids -> {
                    userNames.addAll(sysUserService.list(new QueryWrapper<SysUserDO>()
                            .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                            .in("id", ids).select("real_name")).stream().map(SysUserDO::getRealName).collect
                            (Collectors.toList()));
                });
                StringBuffer stringBuffer = new StringBuffer();
                userNames.stream().forEach(name -> {
                    stringBuffer.append(name);
                    stringBuffer.append(",");
                });
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "角色指派用户",
                        "角色指派用户：角色【" + roleName + "】拥有【" + stringBuffer
                                .substring(0, stringBuffer.length() - 1) + "】用户", securityUtils
                                .getCurrentGroupId(), appId)) {
                    result.put("success", InternationUtils.getInternationalMsg("SET_SUCCESS"));
                    return result;
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
            }
        }
        if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "角色指派用户", "角色指派用户：角色【" +
                        roleName + "】清空用户",
                securityUtils
                        .getCurrentGroupId(), appId)) {
            result.put("success", InternationUtils.getInternationalMsg("SET_SUCCESS"));
            return result;
        } else {
            throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
    }

    /**
     * 获取未被激活的用户角色信息
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public Map<String, Object> getWaitActiveUserData(Map<String, Object> params) {
        String appId = (String) params.get("appId");

//        List<String> userIds = list(new QueryWrapper<SysUserRoleDO>()
//                .eq("app_id", appId).eq("is_activated", 0)).stream().map(SysUserRoleDO::getUserId)
//                .collect(Collectors.toList());
        List<String> userIds = sysWaitGrantUserService.list(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("app_id", appId)).stream().map(SysWaitGrantUserDO::getUserId).collect(Collectors.toList());

        List<SysUserDO> sysUserDOS = new ArrayList<>(16);
        if (userIds != null && userIds.size() > 0) {
            Set<String> userIdSet = new HashSet<>(userIds);
            sysUserDOS.addAll(sysUserService.list(new QueryWrapper<SysUserDO>()
                    .in("id", userIdSet).eq("is_delete", 0)));
//            userIds.stream().distinct().forEach(userId -> {
//                SysUserDO sysUserDO = sysUserService.getById(userId);
//                if (Objects.equals(0, sysUserDO.getBeDeleted())) {
//                    sysUserDOS.add(sysUserDO);
//                }
//            });
        }

        Map<String, Object> map = new HashMap<>();
        map.put("sysUserDOS", sysUserDOS);
        return map;
    }


    /**
     * 用户角色关系激活
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean activeByUserId(Map<String, Object> map) {
        String userId = (String) map.get("userId");
        String appId = (String) map.get("appId");


        // 删除待激活的用户角色授予的用户列表
        sysWaitGrantUserService.remove(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("user_id", userId).eq("app_id", appId));


        // 针对用户ID激活用户角色表
        if (StringUtils.isNotBlank(userId)) {
            List<SysUserRoleDO> sysUserRoleDOs = list(new QueryWrapper<SysUserRoleDO>()
                    .eq("user_id", userId).eq("app_id", appId));

            // 获取当前最新版本号
            Integer version = -1;

            // 判断用户角色版本表是否有数据
            SysUserRoleLvDO sysUserRoleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                    .eq("user_id", userId).eq("app_id", appId).eq("is_latest", 1));
            if (sysUserRoleLvDO != null) {
                version = sysUserRoleLvDO.getVersion();
            }

            sysUserRoleLvService.update(new SysUserRoleLvDO(), new UpdateWrapper<SysUserRoleLvDO>()
                    .eq("is_latest", 1).eq("user_id", userId).eq("app_id", appId)
                    .set("is_latest", 0));

            // 添加用户角色版本表数据
            SysUserRoleLvDO lateSysUserRoleLvDO = new SysUserRoleLvDO();
            lateSysUserRoleLvDO.setId(UuidUtils.createUUID());
            lateSysUserRoleLvDO.setUserId(userId);
            lateSysUserRoleLvDO.setVersion(version + 1);
            lateSysUserRoleLvDO.setLatest(1);
            lateSysUserRoleLvDO.setAppId(appId);
            lateSysUserRoleLvDO.setCreateTime(LocalDateTime.now());
            lateSysUserRoleLvDO.setCreateUser(securityUtils.getCurrentUserId());
            if (!sysUserRoleLvService.save(lateSysUserRoleLvDO)) {
                return false;
            }

            if (sysUserRoleDOs != null && sysUserRoleDOs.size() > 0) {

//                sysUserRoleDOs.stream().forEach(sysUserRoleDO -> {
//                    // 设置为激活状态
//                    sysUserRoleDO.setActivated(1);
//                });
//
//                // 更新用户角色表数据，设置为激活状态
//                if (!updateBatchById(sysUserRoleDOs)) {
//                    return false;
//                }


                // 添加用户角色履历表数据
                List<SysUserRoleVDO> sysUserRoleVDOS = new ArrayList<>(16);
                sysUserRoleDOs.stream().forEach(sysUserRoleDO -> {
                    SysUserRoleVDO sysUserRoleVDO = new SysUserRoleVDO();
                    sysUserRoleVDO.setId(UuidUtils.createUUID());
                    sysUserRoleVDO.setRoleId(sysUserRoleDO.getRoleId());
                    sysUserRoleVDO.setRoleName(sysUserRoleDO.getRoleName());
                    sysUserRoleVDO.setLvId(lateSysUserRoleLvDO.getId());
                    sysUserRoleVDOS.add(sysUserRoleVDO);
                });
                if (CollectionUtils.isNotEmpty(sysUserRoleVDOS)) {
                    if (!sysUserRoleVService.saveBatch(sysUserRoleVDOS)) {
                        return false;
                    }
                }
            }

        }

        // 该用户重新编辑有效权限
        sysAuthMixService.saveAuthMixForApp(userId, appId);
        return true;
    }

    /**
     * 通过用户ID获取有效角色信息
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public List<SysRoleDO> getEffectiveRoleData(String userId, String appId) {

        Set<String> roleIds = new HashSet<>(16);
        if (appId != null) {
            // 普通用户查询有效角色信息
            SysUserRoleLvDO sysUserRoleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                    .select("id").eq("user_id", userId).eq("is_latest", 1)
                    .eq("app_id", appId));
            if (sysUserRoleLvDO != null) {
                String lvId = sysUserRoleLvDO.getId();
                roleIds.addAll(sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>().inSql("role_id", "select " +
                        "id from sys_role")
                        .eq("lv_id", lvId).select("role_id")).stream().map(SysUserRoleVDO::getRoleId).collect
                        (Collectors.toSet()));
            }
            // 全局角色
            SysUserRoleLvDO sysUserRoleLvDO1 = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                    .select("id").eq("user_id", userId).eq("is_latest", 1)
                    .isNull("app_id"));
            if (sysUserRoleLvDO1 != null) {
                String lvId = sysUserRoleLvDO1.getId();
                roleIds.addAll(sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>().inSql("role_id", "select " +
                        "id from sys_role")
                        .eq("lv_id", lvId).select("role_id")).stream().map(SysUserRoleVDO::getRoleId).collect
                        (Collectors.toSet()));
            }
        } else {
            // 管理员级别用户 查询有效角色信息
            roleIds.addAll(sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                    .inSql("lv_id", "select id from sys_user_role_lv where is_latest=1 and user_id='" + userId + "'")
                    .inSql("role_id", "select " +
                            "id from sys_role").select("role_id")).stream().map(SysUserRoleVDO::getRoleId).collect
                    (Collectors.toSet()));
        }

        if (CollectionUtils.isNotEmpty(roleIds)) {
            return sysRoleService.list(new QueryWrapper<SysRoleDO>().in("id", roleIds));
        }
        return new ArrayList<>(16);
    }

    /**
     * 通过角色IDs获取有效用户ID
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public List<String> getEffectiveUserDatas(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>(16);
        }
        StringJoiner roleIdsStr = new StringJoiner(",");
        roleIds.stream().forEach(roleId -> {
            roleIdsStr.add("'" + roleId + "'");
        });
        return sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1)
                .inSql("id", "select lv_id from sys_user_role_v where role_id in (" + roleIdsStr.toString() +
                        ")")
        ).stream().distinct()
                .map(SysUserRoleLvDO::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * 通过角色ID获取有效用户ID
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:09
     */
    @Override
    public List<String> getEffectiveUserData(String roleId) {
        return sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("is_latest", 1)
                .inSql("id", "select lv_id from sys_user_role_v where role_id ='" + roleId + "'")
        ).stream().map(SysUserRoleLvDO::getUserId)
                .collect(Collectors.toList());
    }


    /**
     * 一键激活用户角色
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean allActive(Map<String, Object> map) {
        String appId = (String) map.get("appId");


        List<String> userIds = sysWaitGrantUserService.list(new QueryWrapper<SysWaitGrantUserDO>()
                .eq("app_id", appId)).stream().map(SysWaitGrantUserDO::getUserId).distinct().collect(Collectors
                .toList());
        List<String> newUserIds;
        if (userIds.size() >= 500) {
            newUserIds = userIds.subList(0, 500);
        } else {
            newUserIds = userIds;
        }
        StringBuffer sb = new StringBuffer(16);
        if (CollectionUtils.isNotEmpty(newUserIds)) {

            newUserIds.stream().forEach(userId -> {
                Map<String, Object> param = new HashMap<>();
                param.put("userId", userId);
                param.put("appId", appId);
                if (!activeByUserId(param)) {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
                sb.append("S");
                if (sb.length() * 100 / userIds.size() > 1) {
                    // 前台推送
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("count", userIds.size());
                    map1.put("currentNum", sb.length());
                    map1.put("eventName", Constants.OneClickActivateEvent);
                    // 单体版无效，因为单体版启动的appName是ace-zuul,但是前端的appName是platform
                    sysMessageService.fireSocketEvent(new SocketEventVO(Arrays.asList(securityUtils.getCurrentUserId()),
                            Constants.OneClickActivateEvent, map1, appName));
                }
            });
        }
        return true;
    }

    /**
     * 计算当前时间段下的变更过角色的用户列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public List<SysUserDO> getChangeRoleUserList(Map<String, Object> map) {
        String beginTime = (String) map.get("beginTime");
        String endTime = (String) map.get("endTime");
        String appId = (String) map.get("appId");

        // 判断是否为单一业务管理员账号
        Integer userType = securityUtils.getCurrentUser().getUserType();
        if (userType == 5) {
            if (StringUtils.isNotBlank(beginTime)) {
                // 获取当前时间段修改过的用户ID
                return sysUserService.list(new QueryWrapper<SysUserDO>()
                        .eq("is_delete", 0)
                        .inSql("id", "select id from sys_user where id in (select user_id from sys_user_role_lv " +
                                "where app_id='" + appId + "' and create_time between '" + beginTime + "' and '" +
                                endTime +
                                "') ")
                );
            }
            return sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("is_delete", 0)
                    .inSql("id", "select id from sys_user where id in (select user_id from sys_user_role_lv " +
                            "where app_id='" + appId + "') ")
            );
        }

        if (StringUtils.isNotBlank(beginTime)) {
            // 获取当前时间段修改过的用户ID
            return sysUserService.list(new QueryWrapper<SysUserDO>()
                    .eq("is_delete", 0)
                    .inSql("id", "select id from sys_user where id in (select user_id from sys_user_role_lv " +
                            "where app_id='" + appId + "' and create_time between '" + beginTime + "' and '" +
                            endTime +
                            "') and  " +
                            "organization_id in (select ORGANIZATION_ID from SYS_AUTH_SCOPE_ORG where user_id ='" +
                            securityUtils.getCurrentUserId() + "')")
            );
        }
        return sysUserService.list(new QueryWrapper<SysUserDO>()
                .eq("is_delete", 0)
                .inSql("id", "select id from sys_user where id in (select user_id from sys_user_role_lv " +
                        "where app_id='" + appId + "') and  " +
                        "organization_id in (select ORGANIZATION_ID from SYS_AUTH_SCOPE_ORG where user_id ='" +
                        securityUtils.getCurrentUserId() + "')")
        );


    }

    /**
     * 刷新应用三员用户有效权限
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public boolean refreshAppThreeAuth(List<String> roleIds) {
        List<String> userIds = this.getEffectiveUserDatas(roleIds);
//        List<String> userIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
//                .in("role_id", roleIds)).stream()
//                .map(SysUserRoleDO::getUserId).collect(Collectors.toList());
        userIds.stream().forEach(userId -> {
            sysAuthMixService.saveAuthMix(userId);
        });

        return true;
    }
}
