package com.csicit.ace.zuul.service;

import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.ISecurity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/7 16:58
 */
@Service("security")
public class SecurityImpl extends BaseImpl implements ISecurity {

    @Override
    public boolean hasRoleByRoleCode(String roleCode) {
        if (StringUtils.isNotBlank(roleCode)) {
            return clientService.hasRoleByRoleCode(roleCode, securityUtils.getAppName());
        }
        return false;
    }

    @Override
    public boolean hasRoleByRoleId(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            return clientService.hasRoleByRoleId(roleId, securityUtils.getAppName());
        }
        return false;
    }

    @Override
    public R logout() {
        return clientService.logout();
    }

    @Override
    public BdPostDO getMainPostByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getMainPostByUserId(userId);
        }
        return null;
    }

    @Override
    public List<BdPostDO> getPostsByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getPostsByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public BdPostDO getPostByDepIdAndUserId(String depId, String userId) {
        if (StringUtils.isNotBlank(depId) && StringUtils.isNotBlank(userId)) {
            return clientService.getPostByDepIdAndUserId(depId, userId);
        }
        return null;
    }

    @Override
    public BdJobDO getJobByDepIdAndUserId(String depId, String userId) {
        if (StringUtils.isNotBlank(depId) && StringUtils.isNotBlank(userId)) {
            return clientService.getJobByDepIdAndUserId(depId, userId);
        }
        return null;
    }

    @Override
    public R updateCurrentUserPassword(String oldPassword, String newPassword) {
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
            Map<String, String> map = new HashMap<>();
            map.put("oldPassword", oldPassword);
            map.put("newPassword", newPassword);
            return clientService.updateCurrentUserPassword(map);
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "?????????????????????"));
    }

    @Override
    public List<OrgOrganizationDO> getSonOrganizationByParentId(String parentId, int type) {
        if (StringUtils.isBlank(parentId)) {
            return new ArrayList<>();
        }
        if (type < 1 || type > 3) {
            return new ArrayList<>();
        }
        return clientService.getSonOrganizationByParentId(parentId, type);
    }

    @Override
    public OrgOrganizationDO getRootOrganizationByOrgId(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            return getCurrentRootOrganizationT(organizationId);
        }
        return null;
    }

    @Override
    public OrgOrganizationDO getRootOrganizationByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            SysUserDO userDO = clientService.getUserById(userId);
            if (userDO != null) {
                String organizationId = userDO.getOrganizationId();
                return getRootOrganizationByOrgId(organizationId);
            }
        }
        return null;
    }

    @Override
    public OrgOrganizationDO getCurrentRootOrganization() {
        OrgOrganizationDO org = getCurrentOrganization();
        if (Objects.equals("0", org.getParentId()) || Objects.equals(org.getGroupId(), org.getParentId())) {
            return org;
        }
        return getCurrentRootOrganizationT(org.getParentId());
    }

    public OrgOrganizationDO getCurrentRootOrganizationT(String id) {
        if (StringUtils.isNotBlank(id)) {
            OrgOrganizationDO org = clientService.getCurrentOrganization(id);
            if (org == null) {
                return null;
            }
            if (Objects.equals("0", org.getParentId()) || Objects.equals(org.getGroupId(), org.getParentId())) {
                return org;
            } else {
                return getCurrentRootOrganizationT(org.getParentId());
            }
        }
        return null;
    }

    @Override
    public String getTokenAfterLogin(String userKey) {
        R r = getUserAfterLogin(userKey);
        if (r != null) {
            String token = (String) r.get("token");
            if (StringUtils.isNotBlank(token)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public R getUserAfterLogin(String userKey) {
        if (StringUtils.isNotBlank(userKey)) {
            return clientService.getUserAfterLogin(userKey);
        }
        return null;
    }

    @Override
    public List<OrgOrganizationDO> getCurrentOrganizationsAndDeps() {
        SysUserDO userDO = getCurrentUser();
        if (userDO != null && StringUtils.isNotBlank(userDO.getGroupId())) {
            return clientService.getCurrentOrganizationsAndDeps(userDO.getGroupId());
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrgDepartmentDO> getDeptsByOrganizationId(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            return clientService.getDeptsByOrganizationId(organizationId);
        }
        return new ArrayList<>();
    }

    @Override
    public OrgOrganizationDO getCurrentOrganization() {
        SysUserDO userDO = getCurrentUser();
        if (userDO != null && StringUtils.isNotBlank(userDO.getOrganizationId())) {
            return clientService.getCurrentOrganization(userDO.getOrganizationId());
        }
        return null;
    }

    @Override
    public List<OrgOrganizationDO> getCurrentOrganizations() {
        SysUserDO userDO = getCurrentUser();
        if (userDO != null && StringUtils.isNotBlank(userDO.getGroupId())) {
            return clientService.getCurrentOrganizations(userDO.getGroupId());
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysRoleDO> getRolesByGroupId(String groupId) {
        if (StringUtils.isNotBlank(groupId)) {
            return clientService.getRolesByGroupId(groupId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysRoleDO> getRolesByCurrentGroup() {
        OrgGroupDO group = getCurrentGroup();
        if (group != null) {
            return getRolesByGroupId(group.getId());
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrgDepartmentDO> getDeptsByGroupId(String groupId) {
        if (StringUtils.isNotBlank(groupId)) {
            return clientService.getDepartmentByGroupId(groupId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrgDepartmentDO> getDeptsByCurrentGroup() {
        OrgGroupDO group = getCurrentGroup();
        if (group != null) {
            return getDeptsByGroupId(group.getId());
        }
        return new ArrayList<>();
    }

    @Override
    public OrgGroupDO getCurrentGroup() {
        return getCurrentGroups().get(0);
    }

    @Override
    public List<OrgGroupDO> getCurrentGroups() {
        return securityUtils.getCurrentGroups();
    }

    @Override
    public SysUserDO getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @Override
    public SysUserDO getUserByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getUserById(userId);
        }
        return null;
    }

    @Override
    public SysUserDO getUserByUserName(String userName) {
        if (StringUtils.isNotBlank(userName)) {
            return clientService.getUserByName(userName);
        }
        return null;
    }


    @Override
    public List<OrgDepartmentDO> getCurrentDepts() {
        SysUserDO currentUser = getCurrentUser();
        if (currentUser != null) {
            return getDeptsByUserId(currentUser.getId());
        }
        return new ArrayList<>();
    }

    @Override
    public OrgDepartmentDO getCurrentMainDept() {
        return securityUtils.getDept();
    }

    @Override
    public OrgDepartmentDO getMainDeptByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getMainDeptByUserId(userId);
        }
        return null;
    }

    @Override
    public OrgDepartmentDO getDept(String deptId) {
        if (StringUtils.isNotBlank(deptId)) {
            return clientService.getDepartmentById(deptId);
        }
        return null;
    }

    @Override
    public OrgDepartmentDO getDeptByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return clientService.getDepartmentByCode(code);
        }
        return null;
    }

    @Override
    public List<OrgDepartmentDO> getDeptsByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getDeptsByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysRoleDO> getCurrentRoles() {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            return getRolesByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysRoleDO> getRolesByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getRolesByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public SysRoleDO getRole(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            return clientService.getRoleById(roleId);
        }
        return null;
    }

    @Override
    public boolean hasAuthority(String authId) {
        if (StringUtils.isNotBlank(authId)) {
            return hasAuthorityWithUserId(securityUtils.getCurrentUserId(), authId);
        }
        return false;
    }

    @Override
    public boolean hasAuthorityWithUserId(String userId, String authId) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(authId)) {
            return clientService.hasAuthorityWithUserId(userId, authId);
        }
        return false;
    }

    @Override
    public boolean hasAuthCodeInCurrentApp(String authCode) {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(authCode)) {
            return hasAuthCodeInCurrentAppWithUserId(userId, authCode);
        }
        return false;
    }

    @Override
    public boolean hasAuthCodeInCurrentAppWithUserId(String userId, String authCode) {
        String appId = securityUtils.getAppName();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(authCode)) {
            return clientService.hasAuthCodeInCurrentApp(userId, authCode, appId);
        }
        return false;
    }

    @Override
    public boolean hasAuthorityWithRoleId(String roleId, String authId) {
        return clientService.hasAuthorityWithRoleId(roleId, authId);
    }

    @Override
    public List<String> getAuthIdsByApp(String appId) {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return getAllAuthIdsByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAuthCodesByApp(String appId) {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return getAllAuthCodesByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAuthCodes() {
        String userId = securityUtils.getCurrentUserId();
        String appId = securityUtils.getAppName();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return getAllAuthCodesByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAuthCodesByUser(String userId) {
        String appId = securityUtils.getAppName();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return getAllAuthCodesByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllAuthCodesByUserIdAndApp(String userId, String appId) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return clientService.getAllAuthCodesByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllAuthIds() {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            return getAllAuthIdsByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllAuthIdsByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getAllAuthIdsByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllAuthIdsByUserIdAndApp(String userId, String appId) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return clientService.getAllAuthIdsByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }


    @Override
    public List<String> getAuthIdsByRoleId(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            return getAuthIdsByRoleId(roleId);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean hasApi(String api) {
        if (StringUtils.isNotBlank(api)) {
            String userId = securityUtils.getCurrentUserId();
            if (StringUtils.isNotBlank(userId)) {
                return hasApiByUserId(userId, api);
            }
        }
        return false;
    }

    @Override
    public boolean hasApiByUserId(String userId, String api) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(api)) {
            return clientService.hasApiByUserId(userId, api);
        }
        return false;
    }

    @Override
    public List<String> getApis() {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            return getAllApisByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getApisByApp(String appId) {
        if (StringUtils.isNotBlank(appId)) {
            String userId = securityUtils.getCurrentUserId();
            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
                return getAllApisByUserIdAndApp(userId, appId);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllApisByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return clientService.getAllApisByUserId(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllApisByUserIdAndApp(String userId, String appId) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(appId)) {
            return clientService.getAllApisByUserIdAndApp(userId, appId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllApisByRoleId(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            return clientService.getAllApisByRoleId(roleId);
        }
        return new ArrayList<>();
    }
}
