package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.SysRoleMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    /**
     * 角色关系接口对象
     */
    @Autowired
    SysRoleRelationService sysRoleRelationService;
    /**
     * 用户角色接口对象
     */
    @Autowired
    SysUserRoleService sysUserRoleService;
    /**
     * 角色互斥接口对象
     */
    @Autowired
    SysRoleMutexService sysRoleMutexService;

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SysUserRoleLvService sysUserRoleLvService;
    @Autowired
    SysUserRoleVService sysUserRoleVService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysAuthRoleService sysAuthRoleService;

    @Autowired
    SysAuthRoleLvService sysAuthRoleLvService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    OrgDepartmentService orgDepartmentService;

    @Autowired
    SysRoleDepService sysRoleDepService;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    SysWaitGrantAuthService sysWaitGrantAuthService;

    @Autowired
    SysAuthService sysAuthService;

    /**
     * 填充角色关联的部门名称
     *
     * @param roleList
     * @return
     * @author FourLeaves
     * @date 2021/3/19 11:21
     */
    @Override
    public List<SysRoleDO> fillDepName(List<SysRoleDO> roleList) {
        if (CollectionUtils.isNotEmpty(roleList)) {
            List<SysRoleDepDO> sysRoleDepDOS = sysRoleDepService.list(new QueryWrapper<SysRoleDepDO>()
                    .orderByAsc("create_time")
                    .in("role_id", roleList.stream().map(SysRoleDO::getId).collect(Collectors.toList())));
            if (CollectionUtils.isNotEmpty(sysRoleDepDOS)) {
                List<OrgDepartmentDO> departmentDOS = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                        .in("id", sysRoleDepDOS.stream().map(SysRoleDepDO::getDepId).collect(Collectors.toList()))
                        .select("id", "name", "code"));
                if (CollectionUtils.isNotEmpty(departmentDOS)) {
                    for (SysRoleDO roleDO : roleList) {
                        List<String> depIds = sysRoleDepDOS.stream().filter(dd -> Objects.equals(dd.getRoleId(),
                                roleDO.getId())).collect(Collectors.toList())
                                .stream().map(SysRoleDepDO::getDepId).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(depIds)) {
                            roleDO.setDeps(departmentDOS.stream().filter(dd -> depIds.contains(dd.getId())).collect
                                    (Collectors.toList()));
                            String depsName = "";
                            for (OrgDepartmentDO dep : roleDO.getDeps()) {
                                depsName += "," + dep.getName();
                            }
                            roleDO.setDepsName(depsName.substring(1));
                        }
                    }
                }
            }
        }
        return roleList;
    }

    /**
     * 根据id查询角色
     *
     * @param id
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:00
     */
    @Override
    public SysRoleDO infoRole(String id) {
        // 根据ID获取角色信息
        SysRoleDO sysRoleDO = getById(id);

        // 获取下级角色列表
        List<SysRoleDO> cRoles = new ArrayList<>(16);
        sysRoleRelationService
                .list(new QueryWrapper<SysRoleRelationDO>()
                        .eq("pid", id)).stream().forEach(sysRoleRelationDO -> {
            SysRoleDO cRoleDO = getById(sysRoleRelationDO.getCid());
            cRoles.add(cRoleDO);
        });
        sysRoleDO.setCRoles(cRoles);


        //获得互斥角色
        List<String> mRoleIds = new ArrayList<>(16);
        sysRoleMutexService
                .list(new QueryWrapper<SysRoleMutexDO>()
                        .eq("role_id", id)).stream().forEach(sysRoleMutexDO -> {
            mRoleIds.add(sysRoleMutexDO.getRoleMutexId());
        });
        sysRoleMutexService
                .list(new QueryWrapper<SysRoleMutexDO>()
                        .eq("role_mutex_id", id)).stream().forEach(sysRoleMutexDO -> {
            mRoleIds.add(sysRoleMutexDO.getRoleId());
        });
        List<SysRoleDO> mRoles1 = new ArrayList<>(16);
        mRoleIds.stream().forEach(mRoleId -> {
            if (!mRoles1.contains(getById(mRoleId))) {
                mRoles1.add(getById(mRoleId));
            }
        });
        sysRoleDO.setMRoles(mRoles1);

        return sysRoleDO;
    }

    /**
     * 更新用户-角色-部门对应关系
     *
     * @param userId   用户主键
     * @param depId    部门主键
     * @param oldDepId 旧的部门主键
     * @return boolean
     * @author FourLeaves
     * @date 2021/3/22 9:39
     */
    @Override
    public boolean updateRoleAndDepForUser(String userId, String depId, String oldDepId) {
        if (Objects.equals(depId, oldDepId)) {
            return true;
        }
        SysUserDO user = sysUserService.getById(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("realName", user.getRealName());
        Set<String> roleIds = new HashSet<>();
        List<String> allRoleIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId)).stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
        roleIds.addAll(allRoleIds);
        List<SysRoleDO> allRoles = list(new QueryWrapper<SysRoleDO>().in("id", allRoleIds));
        Set<String> appIds = allRoles.stream().map(SysRoleDO::getAppId).distinct().collect(Collectors.toSet());
        if (StringUtils.isNotBlank(depId) && StringUtils.isBlank(oldDepId)) {
            // 新增
            roleIds.addAll(sysRoleDepService.list(new QueryWrapper<SysRoleDepDO>()
                    .eq("dep_id", depId)).stream().map(SysRoleDepDO::getRoleId).collect(Collectors.toList()));

        } else if (StringUtils.isNotBlank(oldDepId) && StringUtils.isBlank(depId)) {
            // 删除
            roleIds.removeAll(sysRoleDepService.list(new QueryWrapper<SysRoleDepDO>()
                    .eq("dep_id", oldDepId)).stream().map(SysRoleDepDO::getRoleId).collect(Collectors.toList()));
        } else {
            // 更新
            roleIds.addAll(sysRoleDepService.list(new QueryWrapper<SysRoleDepDO>()
                    .eq("dep_id", depId)).stream().map(SysRoleDepDO::getRoleId).collect(Collectors.toList()));
            roleIds.removeAll(sysRoleDepService.list(new QueryWrapper<SysRoleDepDO>()
                    .eq("dep_id", oldDepId)).stream().map(SysRoleDepDO::getRoleId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<SysRoleDO> roleDOS = list(new QueryWrapper<SysRoleDO>()
                    .in("id", roleIds));
            appIds.addAll(roleDOS.stream().map(SysRoleDO::getAppId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(roleDOS)) {
                for (String appId : appIds) {
                    map.put("appId", appId);
                    List<SysRoleDO> roleList = roleDOS.stream().filter(r -> Objects.equals(r.getAppId(), appId))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(roleList)) {
                        map.put("roles", roleList);
                        sysUserRoleService.saveUserIdAndRoleIds(map);
                    } else {
                        // 对应的app角色被清空
                        if (sysUserRoleService.count(new QueryWrapper<SysUserRoleDO>()
                                .eq("app_id", appId).eq("user_id", userId)) > 0
                                && !sysUserRoleService.remove(new QueryWrapper<SysUserRoleDO>()
                                .eq("app_id", appId).eq("user_id", userId))) {
                            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                        }
                        if (sysUserRoleLvService.count(new QueryWrapper<SysUserRoleLvDO>()
                                .eq("app_id", appId).eq("user_id", userId)) > 0
                                && !sysUserRoleLvService.remove(new QueryWrapper<SysUserRoleLvDO>()
                                .eq("app_id", appId).eq("user_id", userId))) {
                            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                        }
                    }
                }

            }
        }
        return true;
    }

    @Override
    public boolean deleteRoleAndDep(SysRoleDO role, String depId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleName", role.getName());
        map.put("roleId", role.getId());
        map.put("appId", role.getAppId());
        map.put("roleScope", role.getRoleScope());
        if (StringUtils.isNotBlank(depId)) {
            // 删除角色部门
            List<String> userIds = sysUserService.getUserIdsByMainDepId(depId);
            List<SysUserDO> userRoleDOS = sysUserRoleService.getUsersByRoleId(map);
            if (CollectionUtils.isNotEmpty(userRoleDOS)) {
                List<SysUserDO> users = userRoleDOS.stream().filter(u -> !userIds.contains(u.getId()))
                        .collect(Collectors.toList());
                map.put("users", users);
                sysUserRoleService.saveRoleIdAndUserIds(map);
            }
        }
        return true;
    }

    /**
     * 更新角色-部门对应关系
     *
     * @param role  角色
     * @param depId 部门主键
     * @return boolean
     * @author FourLeaves
     * @date 2021/3/22 9:39
     */
    @Override
    public boolean addRoleAndDep(SysRoleDO role, String depId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleName", role.getName());
        map.put("roleId", role.getId());
        map.put("appId", role.getAppId());
        if (StringUtils.isNotBlank(depId)) {
            List<SysUserDO> userDOS = sysUserService.getUsersByMainDepId(depId);
            if (CollectionUtils.isNotEmpty(userDOS)) {
                map.put("users", userDOS);
                sysUserRoleService.saveRoleIdAndUserIdsForDep(role, userDOS);
            }
        }
        return true;
    }


    /**
     * 新增角色
     *
     * @param role 角色对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:00
     */
    @Override
    public boolean saveRole(SysRoleDO role) {

        if (StringUtils.isNotBlank(role.getRoleCode())) {
            int count = count(new QueryWrapper<SysRoleDO>()
                    .eq("role_code", role.getRoleCode()).eq("app_id", role.getAppId()));
            if (count > 0) {
                throw new RException("同一应用下，角色标识不可重复！");
            }
        }
        role.setId(UuidUtils.createUUID());
        role.setCreateTime(LocalDateTime.now());
        role.setCreateUser(securityUtils.getCurrentUserId());
        if (save(role)) {
            // 新增角色 存在 关联部门
            // 20210810 角色 存在 多个关联部门  此处关闭
//            if (StringUtils.isNotBlank(role.getDepId())) {
//                updateRoleAndDep(role, role.getDepId(), null);
//            }
            if (role.getCids() != null) {
                if (!sysRoleRelationService.saveChildRoles(role.getId(), role.getCids()))
                    return false;
            }
            if (role.getMids() != null) {
                if (!sysRoleMutexService.saveMutexRoles(role.getId(), role.getMids()))
                    return false;
            }
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增角色", role,
                    securityUtils
                            .getCurrentGroupId(), role
                            .getAppId());
        }
        return false;
    }

    /**
     * 更新角色
     *
     * @param role 角色对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:01
     */
    @Override
    public boolean updateRole(SysRoleDO role) {
        if (StringUtils.isNotBlank(role.getRoleCode())) {
            int count = count(new QueryWrapper<SysRoleDO>()
                    .eq("role_code", role.getRoleCode()).ne("id", role.getId()).eq("app_id", role.getAppId()));
            if (count > 0) {
                throw new RException("同一应用下，角色标识不可重复！");
            }
        }
        if (role.getCids() != null) {
            if (!sysRoleRelationService.saveChildRoles(role.getId(), role.getCids()))
                return false;
        }
        if (role.getMids() != null) {
            if (!sysRoleMutexService.saveMutexRoles(role.getId(), role.getMids()))
                return false;
        }
        role.setUpdateTime(LocalDateTime.now());
        // 20210810 角色 存在 多个关联部门  此处关闭
//        SysRoleDO oldRole = getById(role.getId());
//        if (!Objects.equals(oldRole.getDepId(), role.getDepId())) {
//            updateRoleAndDep(role, role.getDepId(), oldRole.getDepId());
//        }
        if (updateById(role)) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新角色",
                    "角色【" + role.getName() + "】更新",
                    securityUtils
                            .getCurrentGroupId(), role
                            .getAppId());
        }
        return false;
    }

    /**
     * 删除角色
     *
     * @param ids
     * @return boolean
     * @author shanwj
     * @date 2019/5/16 11:24
     */
    @Override
    public boolean deleteByIds(Collection<? extends Serializable> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        StringBuffer stringBuffer = new StringBuffer();
        String appId = getById(((List<String>) ids).get(0)).getAppId();
        ids.forEach(id -> {
            String str = getById(id).getName();
            stringBuffer.append(str);
            stringBuffer.append(",");
        });


        // 删除该角色时，对拥有该角色的用户重新进行有效权限
        // 获取用户集
        List<String> userIds = new ArrayList<>(16);

        userIds.addAll(sysUserRoleService.getEffectiveUserDatas((List<String>) ids));

        List<String> appIds = new ArrayList<>(16);


        ids.forEach(id -> {

//            // 角色用户的版本控制表需要更新
//            List<String> userIds2 = sysUserRoleService.getEffectiveUserData((id.toString()));
//            userIds.addAll(userIds2);
//            userIds2.stream().forEach(userid -> {
//                SysUserRoleLvDO roleLvDO;
//                if (appId == null) {// 全局角色
//                    roleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
//                            .eq("user_id", userid).eq("IS_LATEST", 1).isNull("app_id"));
//                } else {// 普通角色，防止角色用户关系存在未激活的情况下，删除角色时，不能直接调用sysUserRoleService.activeByUserId方法
//                    roleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
//                            .eq("user_id", userid).eq("IS_LATEST", 1).eq("app_id", appId));
//                }
//                if (roleLvDO != null) {
//                    // 去除删掉的角色，获取履历信息
//                    List<SysUserRoleVDO> sysUserRoleVDOS = sysUserRoleVService.list(new
//                            QueryWrapper<SysUserRoleVDO>()
//                            .select("role_id", "role_name").eq("lv_id", roleLvDO.getId()).ne("role_id", id
//                                    .toString()));
//                    // 新增版本
//                    sysUserRoleLvService.update(new SysUserRoleLvDO(), new UpdateWrapper<SysUserRoleLvDO>()
//                            .eq("id", roleLvDO.getId()).set("IS_LATEST", 0).set("CREATE_USER", securityUtils
//                                    .getCurrentUserId())
//                            .set("CREATE_TIME", LocalDateTime.now()));
//
//                    roleLvDO.setVersion(roleLvDO.getVersion() + 1);
//                    roleLvDO.setId(UuidUtils.createUUID());
//                    if (sysUserRoleLvService.save(roleLvDO)) {
//                        sysUserRoleVDOS.stream().forEach(sysUserRoleVDO -> {
//                            sysUserRoleVDO.setLvId(roleLvDO.getId());
//                        });
//                        sysUserRoleVService.saveBatch(sysUserRoleVDOS);
//                    }
//                }
//
//            });


            // 更改角色授权版本控制表数据
            sysAuthRoleLvService.update(new SysAuthRoleLvDO(), new UpdateWrapper<SysAuthRoleLvDO>()
                    .eq("is_last_version", 1).eq("role_id", id.toString())
//                    .eq("app_id", appId)
                    .set("is_last_version", 0)
                    .set("version_end_time", LocalDateTime.now())
                    .set("version_end_user_id", securityUtils.getCurrentUser().getId()));

            // 判断该角色与哪些应用下的权限有关联
            List<String> authIds = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                    .eq("role_id", id.toString())).stream().map(SysAuthRoleDO::getAuthId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(authIds)) {
                appIds.addAll(sysAuthService.listByIds(authIds).stream().map(SysAuthDO::getAppId).collect
                        (Collectors.toList()));
            }

        });


        if (!remove(new QueryWrapper<SysRoleDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                .in("id", ids))) {
            return false;
        }

        userIds.stream().distinct().forEach(userId -> {
            if (appId != null) {// 普通角色
                Map<String, Object> map = new HashMap<>();
                map.put("appId", appId);
                map.put("userId", userId);
                sysUserRoleService.activeByUserId(map);
            } else {
                // 全局角色，
                sysUserRoleService.scopeRoleUserActive(userId, appIds);
            }
        });


        return
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除角色", "删除角色："
                                + stringBuffer.substring(0, stringBuffer.length() - 1),
                        securityUtils.getCurrentGroupId(),
                        appId);
    }


    /**
     * 获取角色所有上级角色
     *
     * @param list
     * @param id
     * @author zuogang
     * @date 2019/7/4 14:54
     */
    private void getParentRoles(List<String> list, String id) {
        List<String> nowPids = sysRoleRelationService
                .list(new QueryWrapper<SysRoleRelationDO>()
                        .eq("cid", id)).stream()
                .map(SysRoleRelationDO::getPid).collect(Collectors.toList());
        list.addAll(nowPids);
        nowPids.stream().forEach(nowId -> {
            getParentRoles(list, nowId);
        });
    }

    /**
     * 获取角色所有下级角色
     *
     * @param list
     * @param id
     * @author zuogang
     * @date 2019/7/4 14:54
     */
    private void getChildRoles(List<String> list, String id) {
        List<String> nowCids = sysRoleRelationService
                .list(new QueryWrapper<SysRoleRelationDO>()
                        .eq("pid", id)).stream()
                .map(SysRoleRelationDO::getCid).collect(Collectors.toList());
        list.addAll(nowCids);
        nowCids.stream().forEach(nowId -> {
            getChildRoles(list, nowId);
        });
    }

    /**
     * 获得该角色ID的所有互斥角色列表
     *
     * @param id
     * @author zuogang
     * @date 2019/7/4 14:54
     */
    private List<SysRoleDO> getMutexRoles(String id) {
        List<String> mutexIds = new ArrayList<>(16);
        sysRoleMutexService
                .list(new QueryWrapper<SysRoleMutexDO>()
                        .eq("role_id", id)).stream().forEach(sysRoleMutexDO -> {
            mutexIds.add(sysRoleMutexDO.getRoleMutexId());
        });
        sysRoleMutexService
                .list(new QueryWrapper<SysRoleMutexDO>()
                        .eq("role_mutex_id", id)).stream().forEach(sysRoleMutexDO -> {
            mutexIds.add(sysRoleMutexDO.getRoleId());
        });
        List<SysRoleDO> mutexList2 = new ArrayList<>(16);
        mutexIds.stream().forEach(mutexId -> {
            if (!mutexList2.contains(getById(mutexId))) {
                mutexList2.add(getById(mutexId));
            }
        });
        return mutexList2;
    }

    /**
     * 通过该角色ID获得可作为下级角色的列表数据
     *
     * @param appId
     * @param roleId
     * @param type   1修改组件使用 2编辑下级角色组件使用 3编辑互斥角色组件使用
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:00
     */
    @Override
    public List<SysRoleDO> getChildAndMutualRoleList(String appId, String roleId, String type) {
        // 获得所有该应用下角色列表
        List<SysRoleDO> allList = list(new QueryWrapper<SysRoleDO>()
                .eq("app_id", appId));
//                .or()
//                .eq("ROLE_SCOPE", 1)

        // 获得该角色ID的所有上级角色列表
        List<SysRoleDO> parentList = new ArrayList<>(16);
        List<String> pids = new ArrayList<>(16);
        getParentRoles(pids, roleId);
        // pids去重并获取上级列表
        pids.stream().forEach(id -> {
            if (!parentList.contains(getById(id))) {
                parentList.add(getById(id));
            }
        });
        // 删除本角色的所有上级列表
        allList.removeAll(parentList);
        // 删除本角色
        allList.remove(getById(roleId));

        if (Objects.equals("2", type)) {
            // 选择下级角色按钮按下

            // 删除本角色的所有互斥角色
            allList.removeAll(getMutexRoles(roleId));

        } else if (Objects.equals("3", type)) {
            // 选择互斥角色按钮按下

            // 获得该角色ID的所有下级角色列表
            List<SysRoleDO> childList = new ArrayList<>(16);
            List<String> cids = new ArrayList<>(16);
            getChildRoles(cids, roleId);
            // cids去重并获取下级列表
            cids.stream().forEach(id -> {
                if (!childList.contains(getById(id))) {
                    childList.add(getById(id));
                }
            });

            // 删除所有下级角色列表
            allList.removeAll(childList);
        }
        return allList;
    }

}
