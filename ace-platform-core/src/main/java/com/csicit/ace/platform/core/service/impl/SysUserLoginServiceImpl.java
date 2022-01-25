package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.SysUserLoginMapper;
import com.csicit.ace.platform.core.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 登录日志管理 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service("sysUserLoginService")
public class SysUserLoginServiceImpl extends ServiceImpl<SysUserLoginMapper, SysUserLoginDO> implements
        SysUserLoginService {
    @Autowired
    SecurityUtils securityUtils;

    /**
     * 获取最新一次登录记录
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:48
     */
    @Override
    public SysUserLoginDO getLatestLogin(String userId) {
        return getOne(new QueryWrapper<SysUserLoginDO>().eq("user_id", userId).orderByDesc("login_time"));
    }

    @Autowired
    OrgGroupService orgGroupService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    SysWaitGrantAuthService sysWaitGrantAuthService;

    @Autowired
    SysWaitGrantUserService sysWaitGrantUserService;

    /**
     * 管理员登录平台，首页显示内容
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:48
     */
    @Override
    public R getLoginInfo() {
        String userId = securityUtils.getCurrentUserId();
        if (securityUtils.getRoleIds().size() == 0) {
            return R.error("当前用户没管理员角色！");
        }
        String roleId = securityUtils.getRoleIds().get(0);
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        if (Objects.equals("admin", roleId)) {
            // 计算集团总数
            Integer groupCount = orgGroupService.count(new QueryWrapper<OrgGroupDO>()
                    .eq("IS_DELETE", 0));
            map.put("groupCount", groupCount);
            // 计算待激活集团管理人数
            Integer groupBeActiveCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("USER_TYPE", 11).eq("IS_DELETE", 0));
            map.put("groupBeActiveCount", groupBeActiveCount);

        } else if (Objects.equals("sec", roleId)) {
            // 计算待分配集团管理人数
            Integer groupBeAssignCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("USER_TYPE", 111).eq("IS_DELETE", 0));
            map.put("groupBeAssignCount", groupBeAssignCount);
        }else if(Objects.equals("groupsuperadmin", roleId)){
            // 计算管控集团
            List<String> oldGroupIds = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("USER_ID", userId).eq("IS_ACTIVATED", 1)).stream().map
                    (SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());
            List<String> groupIds = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                    .eq("IS_DELETE", 0).and(oldGroupIds == null || oldGroupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("ID", oldGroupIds)).stream().distinct().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            // 计算管控集团总数
            map.put("groupControlCount", groupIds.size());
            // 计算用户总数
            Integer userCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("IS_DELETE", 0).eq("USER_TYPE", 3)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("GROUP_ID", groupIds));
            map.put("userCount", userCount);

        } else if (Objects.equals("groupadmin", roleId)) {
            // 计算管控集团
            List<String> oldGroupIds = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("USER_ID", userId).eq("IS_ACTIVATED", 1)).stream().map
                    (SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());
            List<String> groupIds = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                    .eq("IS_DELETE", 0).and(oldGroupIds == null || oldGroupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("ID", oldGroupIds)).stream().distinct().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            // 计算管控集团总数
            map.put("groupControlCount", groupIds.size());
            // 计算用户总数
            Integer userCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("IS_DELETE", 0).eq("USER_TYPE", 3)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("GROUP_ID", groupIds));
            map.put("userCount", userCount);
            // 待激活应用管理人数
            Integer appBeActiveCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("USER_TYPE", 22).eq("IS_DELETE", 0)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("GROUP_ID", groupIds));
            map.put("appBeActiveCount", appBeActiveCount);
        } else if (Objects.equals("groupsec", roleId)) {
            // 计算管控集团
            List<String> oldGroupIds = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("USER_ID", userId).eq("IS_ACTIVATED", 1)).stream().distinct().map
                    (SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());
            List<String> groupIds = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                    .eq("IS_DELETE", 0).and(oldGroupIds == null || oldGroupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("ID", oldGroupIds)).stream().distinct().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            // 待分配应用管理人数
            Integer appBeAssignCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("USER_TYPE", 222).eq("IS_DELETE", 0)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("GROUP_ID", groupIds));
            map.put("appBeAssignCount", appBeAssignCount);
        } else if (Objects.equals("appadmin", roleId)) {
            // 管控应用总数
            List<String> appIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("USER_ID", userId).eq("IS_ACTIVATED", 1)).stream().map(SysAuthScopeAppDO::getAppId)
                    .collect(Collectors.toList());
            map.put("appControlCount", appIds);
            // 待激活权限的用户人数
            Integer userForAuthCount = sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                    .eq("TYPE", 0).and(appIds == null || appIds.size() == 0, i -> i.eq
                            ("1", "2")).in("APP_ID", appIds));
            map.put("userForAuthCount", userForAuthCount);
            // 待激活权限的角色人数
            Integer roleForAuthCount = sysWaitGrantAuthService.count(new QueryWrapper<SysWaitGrantAuthDO>()
                    .eq("TYPE", 1).and(appIds == null || appIds.size() == 0, i -> i.eq
                            ("1", "2")).in("APP_ID", appIds));
            map.put("roleForAuthCount", roleForAuthCount);
            // 待激活角色的用户人数
            Integer userForRoleCount = sysWaitGrantUserService.count(new QueryWrapper<SysWaitGrantUserDO>()
                    .and(appIds == null || appIds.size() == 0, i -> i.eq
                            ("1", "2")).in("APP_ID", appIds));
            map.put("userForRoleCount", userForRoleCount);
        }else if(Objects.equals("appsuperadmin", roleId)){
            // 管控应用总数
            List<String> appIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("USER_ID", userId).eq("IS_ACTIVATED", 1)).stream().map(SysAuthScopeAppDO::getAppId)
                    .collect(Collectors.toList());
            map.put("appControlCount", appIds);
        }
        else if(Objects.equals("businessadmin", roleId)){
            // 计算管控集团
            List<String> oldGroupIds = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                    .eq("USER_ID", userId).eq("IS_ACTIVATED", 1)).stream().map
                    (SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());
            List<String> groupIds = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                    .eq("IS_DELETE", 0).and(oldGroupIds == null || oldGroupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("ID", oldGroupIds)).stream().distinct().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            // 计算管控集团总数
            map.put("groupControlCount", groupIds.size());
            // 计算用户总数
            Integer userCount = sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("IS_DELETE", 0).eq("USER_TYPE", 3)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq
                            ("1", "2")).in("GROUP_ID", groupIds));
            map.put("userCount", userCount);

        }
        return R.ok().put("map", map);
    }
}
