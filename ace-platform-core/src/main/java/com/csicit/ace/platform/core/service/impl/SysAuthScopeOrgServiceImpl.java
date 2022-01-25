package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysAuthScopeOrgDO;
import com.csicit.ace.data.persistent.mapper.SysAuthScopeOrgMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgOrganizationService;
import com.csicit.ace.platform.core.service.SysAuthScopeOrgService;
import com.csicit.ace.platform.core.service.SysUserAdminOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统管理-用户有效权限-授权组织关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthScopeOrgService")
public class SysAuthScopeOrgServiceImpl extends BaseServiceImpl<SysAuthScopeOrgMapper, SysAuthScopeOrgDO> implements
        SysAuthScopeOrgService {

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    /**
     * 获取集团或应用管理员受控于的业务单元ID
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/7/9 9:58
     */
    @Override
    public List<String> getOrgIdsByUserId(String userId) {

        List<String> list = new ArrayList<>();
        List<String> groupIds = sysUserAdminOrgService.getGroupsByUserId(userId);
        // 集团管理员 返回下面的所有业务单元列表
        if (groupIds != null && groupIds.size() > 0) {
            List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                    ("is_delete", 0)
                    .in("group_id", groupIds).select("id").eq("is_business_unit", 1).orderByAsc("sort_path"));
            if (orgs != null && orgs.size() > 0) {
                list.addAll(orgs.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList()));
            }
        } else {
            List<String> orgIds = list(new QueryWrapper<SysAuthScopeOrgDO>().eq("is_activated", 1).eq
                    ("user_id", userId)).stream().map(SysAuthScopeOrgDO::getOrganizationId).distinct().collect(Collectors.toList());
            if (orgIds != null && orgIds.size() > 0) {
                orgIds.stream().forEach(organizationId -> {
                    OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById(organizationId);
                    if (orgOrganizationDO != null && Objects.equals(0, orgOrganizationDO.getBeDeleted())) {
                        list.add(organizationId);
                    }
                });
//                list.addAll(orgs.stream().map(SysAuthScopeOrgDO::getOrganizationId).collect(Collectors.toList()));
            }
        }
        return list;
    }
}
