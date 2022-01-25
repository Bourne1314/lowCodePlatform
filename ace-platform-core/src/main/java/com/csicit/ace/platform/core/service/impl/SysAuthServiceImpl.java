package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.mapper.SysAuthMapper;
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
 * 权限管理 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthService")
public class SysAuthServiceImpl extends BaseServiceImpl<SysAuthMapper, SysAuthDO> implements SysAuthService {

    @Autowired
    AceSqlUtils aceSqlUtils;
    /**
     * auth-api关系表访问接口对象
     */
    @Autowired
    SysAuthApiService sysAuthApiService;

    @Autowired
    SysApiResourceService sysApiResourceService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysAuthScopeUserGroupService sysAuthScopeUserGroupService;

    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    SysUserGroupService sysUserGroupService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysAuthUserService sysAuthUserService;

    @Autowired
    SysAuthRoleService sysAuthRoleService;

    @Autowired
    SysUserRoleLvService sysUserRoleLvService;

    @Autowired
    SysUserRoleVService sysUserRoleVService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    CacheUtil cacheUtil;

    /**
     * 通过ID获取权限信息
     *
     * @param id
     * @return
     */
    public R infoAuth(String id) {
        SysAuthDO sysAuthDO = getById(id);
        if (sysAuthDO != null) {
            // 获取API列表
            List<SysApiResourceDO> sysApiResourceDOList = new ArrayList<>(16);
            sysAuthApiService
                    .list(new QueryWrapper<SysAuthApiDO>()
                            .eq("auth_id", id)).stream().forEach(sysAuthApiDO -> {
                        SysApiResourceDO sysApiResourceDO = sysApiResourceService.getById(sysAuthApiDO.getApiId());
                        sysApiResourceDOList.add(sysApiResourceDO);
                    }
            );
            sysAuthDO.setApis(sysApiResourceDOList);

            // 获取该权限的所有子菜单ID列表
            List<String> cids = list(new QueryWrapper<SysAuthDO>()
                    .eq("app_id", sysAuthDO.getAppId()).likeRight("sort_path",
                            sysAuthDO.getSortPath())).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            return R.ok().put("instance", sysAuthDO).put("cids", cids);
        }

        return R.ok();
    }

    /**
     * 保存权限
     *
     * @param auth 权限对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 16:50
     */
    @Override
    public boolean saveAuth(SysAuthDO auth) {
        //设置排序
        String sortPath;
        if (Objects.equals("0", auth.getParentId())) {
            sortPath = SortPathUtils.getSortPath("", auth.getSortIndex());
        } else {
            SysAuthDO parentGroup = getById(auth.getParentId());
            sortPath = SortPathUtils.getSortPath(parentGroup.getSortPath(), auth.getSortIndex());
        }
        aceSqlUtils.validateTreeTableWithUnique("sys_auth", auth.getParentId(), auth.getSortIndex(), sortPath, "app_id",
                auth.getAppId());
        auth.setSortPath(sortPath);
        auth.setCreateTime(LocalDateTime.now());
        auth.setCreateUser(securityUtils.getCurrentUserId());
        if (save(auth)) {

            // 为拥有该应用权限的应用管理员或者应用审计员添加该权限的管控域

            // 获取应用管理员用户ID
//            List<String> userIds = new ArrayList<>(16);
//            sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
//                    .eq("app_id", auth.getAppId()).eq("is_activated", 1))
//                    .stream().map(SysAuthScopeAppDO::getUserId).collect(Collectors.toList())
//                    .stream().forEach(userId -> {
//                SysUserDO sysUserDO = sysUserService.getById(userId);
//                if (sysUserDO != null && Objects.equals(0, sysUserDO.getBeDeleted())) {
//                    if (!userIds.contains(userId)) {
//                        userIds.add(userId);
//                    }
//                }
//            });
//            List<SysAuthScopeAppDO> list = new ArrayList<>();
//            if (userIds.size() > 0) {
//                userIds.parallelStream().forEach(userId -> {
//                    SysAuthScopeAppDO sysAuthScopeAppDO = new SysAuthScopeAppDO();
//                    sysAuthScopeAppDO.setId(UuidUtils.createUUID());
//                    sysAuthScopeAppDO.setAuthId(auth.getId());
//                    sysAuthScopeAppDO.setUserId(userId);
//                    sysAuthScopeAppDO.setAppId(auth.getAppId());
//                    sysAuthScopeAppDO.setActivated(1);
//                    SysUserRoleLvDO sysUserRoleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
//                            .eq("user_id", userId).eq("is_latest", 1));
//                    Integer roleType = sysRoleService.getById(sysUserRoleVService.getOne(new
//                            QueryWrapper<SysUserRoleVDO>().eq("lv_id", sysUserRoleLvDO.getId())).getRoleId())
//                            .getRoleType();
//                    sysAuthScopeAppDO.setRoleType(roleType);
//                    list.add(sysAuthScopeAppDO);
//                });
//            }
//            if ((list.size() > 0 && sysAuthScopeAppService.saveBatch(list)) || list.size() == 0) {
            // 保存菜单 此时api列表为空
            if (auth.getApis() == null || auth.getApis().size() == 0) {
                return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增权限", "新增权限："+auth.getName(),
                        securityUtils
                                .getCurrentGroupId(),
                        auth.getAppId());
            }
            if (sysAuthApiService.saveAuthApi(auth.getId(), auth.getApis())) {
                return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增权限", "新增权限："+auth.getName(),
                        securityUtils
                                .getCurrentGroupId(),
                        auth.getAppId());
            }
//            }
        }
        return false;
    }

    /**
     * 更新权限
     * 如果涉及更新排序号需要重新计算排序路径
     *
     * @param auth 权限对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 15:54
     */
    @Override
    public boolean updateAuth(SysAuthDO auth) {
        auth.setUpdateTime(LocalDateTime.now());
        SysAuthDO oldAuth = getById(auth.getId());
        if (oldAuth == null) {
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
        if (!Objects.equals(oldAuth.getSortIndex(), auth.getSortIndex())) {
            SysAuthDO parentAuth = getById(auth.getParentId());
            String sortPath = "";
            if (parentAuth != null) {
                sortPath =
                        SortPathUtils.getSortPath(parentAuth.getSortPath(), auth.getSortIndex());
                auth.setSortPath(sortPath);

            } else {
                sortPath =
                        SortPathUtils.getSortPath(sortPath, auth.getSortIndex());
                auth.setSortPath(sortPath);
            }
            aceSqlUtils.updateSonSortPathWithUnique(
                    "sys_auth", sortPath, oldAuth.getSortPath().length(), oldAuth.getSortPath(), "app_id", auth
                            .getAppId());
        }
//        sysAuthApiService.remove(new QueryWrapper<SysAuthApiDO>().eq("auth_id", auth.getId()));
        // 保存菜单 此时api列表为空
//        if (auth.getApis() == null || auth.getApis().size() == 0) {
//            if (updateById(auth)) {
//                return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新权限", auth,
//                        securityUtils
//                                .getCurrentGroupId(), auth
//                                .getAppId());
//            }
//        } else
        if (sysAuthApiService.saveAuthApi(auth.getId(), auth.getApis())) {
            if (updateById(auth)) {
                return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新权限", "更新权限："+auth.getName(),
                        securityUtils
                                .getCurrentGroupId(), auth
                                .getAppId());
            }
        }
        return false;
    }

    /**
     * 删除权限
     * 根据提供的id集合进行删除
     *
     * @param ids 权限对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 15:54
     */
    @Override
    public boolean deleteByIds(Collection<? extends Serializable> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        // 会被删除的权限ID集合
        List<String> authIdList = new ArrayList<>(16);
        // 删除该权限时，对拥有该权限的用户重新计算有效权限
        // 获取用户集
        List<String> userIdList = new ArrayList<>(16);
        // 获取角色集
        List<String> roleList = new ArrayList<>(16);

        StringBuffer stringBuffer = new StringBuffer();

        final List<SysAuthDO> auths = list(new QueryWrapper<SysAuthDO>().in("id", ids));

        ids.forEach(id -> {
            SysAuthDO auth = auths.stream().filter(authT -> Objects.equals(id, authT.getId())).findFirst().get();
            String str = auth.getName();
            String sortPath = auth.getSortPath();

            authIdList.addAll(list(new QueryWrapper<SysAuthDO>().eq("app_id", auth.getAppId())
                    .likeRight("sort_path", sortPath)).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList()));

            stringBuffer.append(str);
            stringBuffer.append(",");
        });
        String appId = getById(authIdList.get(0)).getAppId();
        authIdList.stream().distinct().forEach(authId -> {

            userIdList.addAll(sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                    .eq("auth_id", authId)).stream().map(SysAuthUserDO::getUserId)
                    .collect(Collectors.toList()));

            roleList.addAll(sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                    .eq("auth_id", authId)).stream().map(SysAuthRoleDO::getRoleId)
                    .collect(Collectors.toList()));
        });

        // 先查出 权限 - api
        List<SysAuthApiDO> authApis = sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
        .in("auth_id", authIdList).select("api_id","auth_id"));
        List<SysAuthDO> auths2 = list(new QueryWrapper<SysAuthDO>().in("id", authIdList).select("id", "app_id"));
        if (!removeByIds(authIdList)) {
            authApis.stream().forEach(authApi -> {
                String authId = authApi.getAuthId();
                SysAuthDO authDO = auths2.stream().filter(authT -> Objects.equals(authId, authT.getId())).findFirst().get();
                cacheUtil.hDelete(authDO.getAppId() + "-apis", authApi.getApiId());
            });
            return false;
        }

        // 删除权限对应的菜单权限ID设为""
        List<SysMenuDO> sysMenuDOs = new ArrayList<>(16);
        authIdList.stream().distinct().forEach(authId -> {
            List<SysMenuDO> sysMenuDOList = sysMenuService.list(new QueryWrapper<SysMenuDO>()
                    .eq("auth_id", authId));
            sysMenuDOList.stream().forEach(sysMenuDO -> {
                sysMenuDO.setAuthId("");
            });
            sysMenuDOs.addAll(sysMenuDOList);
        });
        if (sysMenuDOs.size() > 0) {
            sysMenuService.updateBatchById(sysMenuDOs);
        }

        // 更新sysAuthUserLV,sysAuthUserV数据
        userIdList.stream().distinct().forEach(userId -> {
            sysAuthUserService.authUserActivation(userId, appId);
        });
        // 更新sysAuthRoleLV,sysAuthRoleV数据
        roleList.stream().distinct().forEach(roleId -> {
            sysAuthRoleService.authRoleActivation(roleId,appId);
        });
        sysAuthMixService.remove(new QueryWrapper<SysAuthMixDO>().in("auth_id", authIdList));
//        roleList.stream().forEach(roleId -> {
//            List<String> lvIds = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>().eq("role_id", roleId))
//                    .stream().map(SysUserRoleVDO::getLvId).collect(Collectors.toList());
//            if (lvIds != null && lvIds.size() > 0) {
//                userIdList.addAll(sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
//                        .eq("is_latest", 1).eq("app_id", appId).in("id", lvIds)).stream().map
//                        (SysUserRoleLvDO::getUserId).collect(Collectors.toList()));
//            }
//        });
//
//        // 重新计算用户有效权限
//        userIdList.stream().distinct().forEach(userId -> {
//            sysAuthMixService.saveAuthMix(userId);
//        });

        return
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"),
                        "删除权限", "删除权限："+stringBuffer.substring(0, stringBuffer.length() - 1), securityUtils
                                .getCurrentGroupId(), appId);

    }

    /**
     * 通过UserId获取用户有效权限列表
     *
     * @param userId
     * @param appId
     * @return List<SysAuthDO>
     * @author shanwj
     * @date 2019/5/17 11:31
     */
    @Override
    public List<SysAuthDO> queryUserAuthMix(String userId, String appId) {
        List<SysAuthDO> sysAuthDOList = new ArrayList<>(16);
        List<SysAuthMixDO> authMixs = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", userId).eq("app_id", appId).select("id","auth_id"));
        if (CollectionUtils.isEmpty(authMixs)) {
            return sysAuthDOList;
        }
        Set<String> authIds = authMixs.stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toSet());
        return list(new QueryWrapper<SysAuthDO>().eq("app_id", appId).in("id", authIds).orderByAsc("sort_path"));

        //return sysAuthDOList.stream().sorted(Comparator.comparing(SysAuthDO::getSortPath)).collect(Collectors.toList());

    }

//    /**
//     * 修改有效权限组织授控域
//     *
//     * @param grantAuthReciveVO
//     * @return
//     * @author shanwj
//     * @date 2019/5/17 11:31
//     */
//    @Override
//    public boolean saveAuthMixOrgControlDomain(GrantAuthReciveVO grantAuthReciveVO) {
//        String userId = grantAuthReciveVO.getUserId();
//        String authId = grantAuthReciveVO.getAuthId();
//
//        // 删除旧的有效权限组织表数据
//        sysAuthScopeOrgService.remove(new QueryWrapper<SysAuthScopeOrgDO>()
//                .eq("auth_id", authId)
//                .eq("user_id", userId));
//
//        // 添加新的有效权限组织表数据
//        grantAuthReciveVO.getOrgOrganizationDOList().forEach(orgOrganizationDO -> {
//            SysAuthScopeOrgDO sysAuthScopeOrgDO = new SysAuthScopeOrgDO();
//            sysAuthScopeOrgDO.setId(UuidUtils.createUUID());
//            sysAuthScopeOrgDO.setAuthId(authId);
//            sysAuthScopeOrgDO.setUserId(userId);
//            sysAuthScopeOrgDO.setActivated(1);
//            sysAuthScopeOrgDO.setRoleType(0);
//            sysAuthScopeOrgDO.setOrganizationId(orgOrganizationDO.getId());
//            if (!sysAuthScopeOrgService.save(sysAuthScopeOrgDO)) {
//                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//            }
//        });
//        return true;
//    }

//    /**
//     * 修改有效权限用户组授控域
//     *
//     * @param grantAuthReciveVO
//     * @return
//     * @author shanwj
//     * @date 2019/5/17 11:31
//     */
//    @Override
//    public boolean saveAuthMixUserGroupControlDomain(GrantAuthReciveVO grantAuthReciveVO) {
//        String userId = grantAuthReciveVO.getUserId();
//        String authId = grantAuthReciveVO.getAuthId();
//
//        // 删除旧的有效权限用户组表数据
//        sysAuthScopeUserGroupService.remove(new QueryWrapper<SysAuthScopeUserGroupDO>()
//                .eq("auth_id", authId)
//                .eq("user_id", userId));
//
//        // 添加新的有效权限用户组表数据
//        grantAuthReciveVO.getSysUserGroupDOList().forEach(sysUserGroupDO -> {
//            SysAuthScopeUserGroupDO sysAuthScopeUserGroupDO = new SysAuthScopeUserGroupDO();
//            sysAuthScopeUserGroupDO.setId(UuidUtils.createUUID());
//            sysAuthScopeUserGroupDO.setAuthId(authId);
//            sysAuthScopeUserGroupDO.setUserId(userId);
//            sysAuthScopeUserGroupDO.setActivated(1);
//            sysAuthScopeUserGroupDO.setRoleType(0);
//            sysAuthScopeUserGroupDO.setUserGroupId(sysUserGroupDO.getId());
//            if (!sysAuthScopeUserGroupService.save(sysAuthScopeUserGroupDO)) {
//                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//            }
//        });
//        return true;
//    }


    /**
     * 获取该权限的所有上级权限
     *
     * @param parentAuthList
     * @param id
     * @param id
     * @author zuogang
     * @date 2019/7/4 14:54
     */
    @Override
    public void getParentAuths(List<SysAuthDO> parentAuthList, String id) {
        SysAuthDO auth = this.getById(id);

        SysAuthDO sysAuthDO = getById(auth.getParentId());
        if (sysAuthDO != null) {
            parentAuthList.add(sysAuthDO);
            this.getParentAuths(parentAuthList, sysAuthDO.getId());
        }

    }

    /**
     * 获取该权限的所有下级权限
     *
     * @param id
     * @param childAuthList
     * @param id
     * @author zuogang
     * @date 2019/7/4 14:54
     */
    @Override
    public void getChildAuths(String id, List<SysAuthDO> childAuthList) {

        List<SysAuthDO> childs = list(new QueryWrapper<SysAuthDO>().eq("parent_id", id));
        childAuthList.addAll(childs);
        if (childs.size() == 0) {
            return;
        }
        childs.stream().forEach(auth -> {
            this.getChildAuths(auth.getId(), childAuthList);
        });
    }

    /**
     * 移动权限
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:40
     */
    @Override
    public R moveAuth(Map<String, Object> params) {


        String id = (String) params.get("id");
        String aftParentId = (String) params.get("aftParentId");
        SysAuthDO sysAuthDO = getById(id);

        List<String> cids = (List<String>) params.get("cids");
        cids.add(id);
        // 判断该权限是否被菜单绑定
        if (CollectionUtils.isNotEmpty(cids)) {
            if (sysMenuService.count(new QueryWrapper<SysMenuDO>().eq("app_id", sysAuthDO.getAppId()).in("auth_id",
                    cids)) > 0) {
                return R.error(InternationUtils.getInternationalMsg("MOVE_AUTH_FAILED"));
            }
        }

        // 获取修改后的父菜单下的值权限最大序号
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", "sys_auth");
        map.put("appId", sysAuthDO.getAppId());
        map.put("parentId", aftParentId);
        Integer maxSortIndex = aceSqlUtils.getMaxSort(map);
        sysAuthDO.setParentId(aftParentId);
        sysAuthDO.setSortIndex(maxSortIndex + 10);
        String oldSortPath = sysAuthDO.getSortPath();
        String newSortPath = "";


        // 父节点发生改变 或 sortIndex 发生变化
        if (!Objects.equals("0", aftParentId)) {
            SysAuthDO parent = getById(aftParentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            newSortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), sysAuthDO.getSortIndex());
            sysAuthDO.setSortPath(newSortPath);

        } else {
            newSortPath = SortPathUtils.getSortPath("", sysAuthDO.getSortIndex());
            sysAuthDO.setSortPath(newSortPath);
        }

        if (!updateById(sysAuthDO)) {
            return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        }
        aceSqlUtils.updateSonSortPathWithUnique("sys_auth", newSortPath, oldSortPath.length(), oldSortPath,
                "app_id", sysAuthDO.getAppId());
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

}
