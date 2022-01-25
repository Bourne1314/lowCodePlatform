package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.OrgOrganizationMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.OrgDepartmentService;
import com.csicit.ace.orgauth.core.service.OrgOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 组织-组织 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:16:53
 */
@Service("orgOrganizationServiceO")
public class OrgOrganizationServiceImpl extends BaseServiceImpl<OrgOrganizationMapper, OrgOrganizationDO> implements
        OrgOrganizationService {

    @Resource(name = "orgDepartmentServiceO")
    OrgDepartmentService orgDepartmentService;

    @Autowired
    SecurityUtils securityUtils;

    @Override
    public List<OrgOrganizationDO> getOrganizationsAndDepsByOrgName(String orgName) {
        orgName = orgName.substring(3);
        String groupId = securityUtils.getCurrentGroupId();
        return list(new QueryWrapper<OrgOrganizationDO>()
                .like(StringUtils.isNotBlank(orgName), "name", orgName)
                .eq("group_id", groupId)
                .eq("is_delete", 0)
                .eq("is_business_unit", 1)
                .orderByAsc("sort_path"));
//        List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
//                .like(StringUtils.isNotBlank(orgName), "name", orgName)
//                .eq("group_id", groupId)
//                .eq("is_delete", 0)
//                .eq("is_business_unit", 1)
//                .orderByAsc("sort_path"));
//        List<OrgOrganizationDO> list = new ArrayList<>();
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgs)) {
//            for (OrgOrganizationDO org : orgs) {
//                list.addAll(orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
//                        .likeRight("sort_path", org.getSortPath())
//                        .eq("group_id", groupId)
//                        .ge("is_business_unit", 1)
//                        .eq("is_delete", 0)
//                        .orderByAsc("sort_path")));
//            }
//            List<OrgOrganizationDO> listUnique = new ArrayList<>();
//            Set<String> orgIds = new HashSet<>();
//            for (OrgOrganizationDO org : list) {
//                if (!orgIds.contains(org.getId())) {
//                    orgIds.add(org.getId());
//                    listUnique.add(org);
//                }
//            }
//            List<OrgOrganizationDO> listT = TreeUtils.makeTree(listUnique, OrgOrganizationDO.class);
//            return listT;
//        }
//        return new ArrayList<>();
    }

    @Override
    public List<OrgOrganizationDO> getSonOrganizationByParentId(String parentId, Integer type) {
        List<OrgOrganizationDO> list = new ArrayList<>();
        OrgOrganizationDO parentOrg = getById(parentId);
        if (parentOrg != null) {
            if (type == 1) {
                list = list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .eq("parent_id", parentId).eq("is_business_unit", 1).orderByAsc("sort_path"));
            } else if (type == 2) {
                list = list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .eq("parent_id", parentId).orderByAsc("sort_path"));
            } else {
                list = list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .eq("parent_id", parentId).eq("is_business_unit", 2).orderByAsc("sort_path"));
            }
        }
        return list;
    }

    @Override
    public OrgOrganizationDO getOrganizationByID(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            return getById(organizationId);
        } else if (StringUtils.isNotBlank(securityUtils.getCurrentUser().getGroupId())) {
            organizationId = securityUtils.getCurrentUser().getGroupId();
            return getById(organizationId);
        }

        return null;
    }

    @Override
    public OrgOrganizationDO getCurrentOrganization(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            return getById(organizationId);
        }
        return null;
    }

    @Override
    public List<OrgOrganizationDO> getCurrentOrganizationsAndDeps(String groupId) {
        if (StringUtils.isNotBlank(groupId)) {

        } else if (StringUtils.isNotBlank(securityUtils.getCurrentUser().getGroupId())) {
            groupId = securityUtils.getCurrentUser().getGroupId();
        }
        if (StringUtils.isNotBlank(groupId)) {
            List<OrgOrganizationDO> list = list(new QueryWrapper<OrgOrganizationDO>().eq
                    ("group_id", groupId).ne("id", groupId).eq("is_delete", 0).orderByAsc("sort_path"));
            List<OrgOrganizationDO> listT = TreeUtils.makeTree(list, OrgOrganizationDO.class);
            return listT;
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrgOrganizationDO> getCurrentOrganizations(String groupId) {
        if (StringUtils.isNotBlank(groupId)) {

        } else if (StringUtils.isNotBlank(securityUtils.getCurrentUser().getGroupId())) {
            groupId = securityUtils.getCurrentUser().getGroupId();
        }
        if (StringUtils.isNotBlank(groupId)) {
            List<OrgOrganizationDO> list = list(new QueryWrapper<OrgOrganizationDO>().eq
                    ("group_id", groupId).eq("is_delete", 0).eq("IS_BUSINESS_UNIT", 1).orderByAsc("sort_path"));
            List<OrgOrganizationDO> listT = TreeUtils.makeTree(list, OrgOrganizationDO.class);
            return listT;
        }
        return new ArrayList<>();
    }

    @Override
    public OrgOrganizationDO getEntityDeptOfCurrentUser() {
        return getEntityDeptByUserId(securityUtils.getCurrentUserId());
    }

    @Override
    public OrgOrganizationDO getEntityDeptByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            OrgDepartmentDO mainDept = orgDepartmentService.getMainDeptByUserId(userId);
            if (mainDept != null) {
                if (mainDept.getEntity() == 1) {
                    return JsonUtils.castObject(mainDept, OrgOrganizationDO.class);
                } else {
                    return getParentEntityOrg(Objects.equals(mainDept.getParentId(), "0") ? mainDept.getOrganizationId() : (mainDept.getParentId()));
                }
            } else {
                return getById(securityUtils.getCurrentUser().getOrganizationId());
            }

        }
        return new OrgOrganizationDO();
    }

    private OrgOrganizationDO getParentEntityOrg(String orgId) {
        OrgOrganizationDO orgOrganizationDO = getById(orgId);
        if (orgOrganizationDO != null) {
            if (orgOrganizationDO.getBusinessUnit() == 1) {
                return orgOrganizationDO;
            } else {
                OrgDepartmentDO orgDepartmentDO = orgDepartmentService.getById(orgId);
                if (orgDepartmentDO != null) {
                    if (orgDepartmentDO.getEntity() == 1) {
                        return JsonUtils.castObject(orgDepartmentDO, OrgOrganizationDO.class);
                    } else {
                        return getParentEntityOrg(Objects.equals(orgDepartmentDO.getParentId(), "0") ? orgDepartmentDO.getOrganizationId() : (orgDepartmentDO.getParentId()));
                    }
                }
            }
        }
        return new OrgOrganizationDO();
    }
}
