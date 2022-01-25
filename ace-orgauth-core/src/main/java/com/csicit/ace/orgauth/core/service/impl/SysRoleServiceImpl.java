package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysRoleMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.SysGroupAppService;
import com.csicit.ace.orgauth.core.service.SysRoleService;
import com.csicit.ace.orgauth.core.service.SysUserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleServiceO")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    @Resource(name = "sysGroupAppServiceO")
    SysGroupAppService sysGroupAppService;

    @Resource(name = "sysUserRoleServiceO")
    SysUserRoleService sysUserRoleService;

    @Override
    public boolean hasRoleByRoleCode(String roleCode, String appId) {
        List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", securityUtils.getCurrentUserId())
                .and(i->i.eq("app_id", appId).or().isNull("app_id")));
        List<String> roleIds = userRoles.stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        List<SysRoleDO> roles = list(new QueryWrapper<SysRoleDO>().in("id", roleIds)
                .select("id", "role_code"));
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }
        List<String> roleCodes = roles.stream().map(SysRoleDO::getRoleCode).collect(Collectors.toList());
        return roleCodes.contains(roleCode);
    }

    @Override
    public boolean hasRoleByRoleId(String roleId, String appId) {
        List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", securityUtils.getCurrentUserId())
                .and(i->i.eq("app_id", appId).or().isNull("app_id")));
        List<String> roleIds = userRoles.stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        return roleIds.contains(roleId);
    }

    @Override
    public List<SysRoleDO> getRolesByUserId(String userId) {
        List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId));
        List<String> roleIds = userRoles.stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(roleIds)) {
            return list(new QueryWrapper<SysRoleDO>()
                    .in("id", roleIds));
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysRoleDO> getRolesByGroupId(String groupId) {
        List<SysRoleDO> roles = new ArrayList<>(16);
        if (StringUtils.isNotBlank(groupId)) {
            List<SysGroupAppDO> apps = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().eq("group_id",
                    groupId).select("id"));
            if (apps != null && apps.size() > 0) {
                Set<String> appIds = apps.stream().map(SysGroupAppDO::getId).collect(Collectors.toSet());
                roles = list(new QueryWrapper<SysRoleDO>().in("app_id", appIds));
//                return roles;
            }
        }
        roles.addAll(list(new QueryWrapper<SysRoleDO>().isNull("app_id")));
        return roles;
    }
}
