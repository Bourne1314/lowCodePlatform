package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.interfaces.service.IOrg;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 组织接口实现
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/20 17:25
 */
@Service
public class OrgImpl extends BaseImpl implements IOrg {

    @Override
    public List<OrgOrganizationDO> getOrganizationsAndDepsByOrgName(String orgName) {
        return gatewayService.getOrganizationsAndDepsByOrgName("arg" + orgName);
    }

    @Override
    public List<OrgDepartmentDO> getOrgDepartmentListByUserIds(Set<String> userIds) {
        return gatewayService.getOrgDepartmentListByUserIds(userIds);
    }
    @Override
    public List<OrgDepartmentDO> listdeliverDepartment(Set<String> userIds) {
        return gatewayService.listdeliverDepartment(userIds);
    }


    @Override
    public List<SysUserDO> getUsersByDepartmentId(String departmentId) {
        return gatewayService.getUsersByDepartmentId(departmentId);
    }
    @Override
    public List<SysUserDO> getUsersByUserIds(List<String> userIds){
        return gatewayService.getUsersByUserIds(userIds);
    }


    @Override
    public OrgOrganizationDO getEntityDeptOfCurrentUser() {
        return gatewayService.getEntityDeptOfCurrentUser();
    }

    @Override
    public OrgOrganizationDO getEntityDeptByUserId(String userId) {
        return gatewayService.getEntityDeptByUserId(userId);
    }

    @Override
    public List<String> getOrgIdsWithUserAndPersonByUserId(String userId) {
        List<OrgOrganizationDO> orgs = getOrgsWithUserAndPersonByUserId(userId);
        if (CollectionUtils.isNotEmpty(orgs)) {
            return orgs.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getOrgIdsWithUserAndPersonByCurrentUser() {
        return getOrgIdsWithUserAndPersonByUserId(securityUtils.getCurrentUserId());
    }

    @Override
    public List<OrgOrganizationDO> getOrgsWithUserAndPersonByUserId(String userId) {
        return gatewayService.getOrgsWithUserAndPersonByUserId(userId);
    }

    @Override
    public List<OrgOrganizationDO> getOrgsWithUserAndPersonByCurrentUser() {
        return getOrgsWithUserAndPersonByUserId(securityUtils.getCurrentUserId());
    }

    @Override
    public OrgOrganizationDO getOrgByUser(String userId) {
        return gatewayService.getOrgByUser(userId);
    }

    @Override
    public String getOrgIdByUser(String userId) {
        OrgOrganizationDO org = getOrgByUser(userId);
        if (Objects.nonNull(org)) {
            return org.getId();
        }
        return null;
    }

    @Override
    public OrgOrganizationDO getOrgByCurrentUser() {
        return getOrgByUser(securityUtils.getCurrentUserId());
    }

    @Override
    public String getOrgIdByCurrentUser() {
        return getOrgIdByUser(securityUtils.getCurrentUserId());
    }
}
