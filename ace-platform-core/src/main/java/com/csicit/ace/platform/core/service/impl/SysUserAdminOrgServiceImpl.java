package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysUserAdminOrgDO;
import com.csicit.ace.data.persistent.mapper.SysUserAdminOrgMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgGroupService;
import com.csicit.ace.platform.core.service.OrgOrganizationTypeService;
import com.csicit.ace.platform.core.service.SysUserAdminOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 系统管理-用户-可管理的组织 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:22
 */
@Service
public class SysUserAdminOrgServiceImpl extends BaseServiceImpl<SysUserAdminOrgMapper, SysUserAdminOrgDO> implements
        SysUserAdminOrgService {
    @Autowired
    OrgOrganizationTypeService orgOrganizationTypeService;

    @Autowired
    OrgGroupService orgGroupService;

    @Override
    public List<String> getGroupsByUserId(String userId) {
        List<String> ids = new ArrayList<>(16);
//        List<OrgGroupDO> groups = orgGroupService.list(new QueryWrapper<OrgGroupDO>().select("id"));
//        List<String> allGroupIds;
//        if (groups != null && groups.size() > 0) {
//            allGroupIds = groups.stream().map(OrgGroupDO::getId).collect(Collectors.toList());
//            List<SysUserAdminOrgDO> list = list(new QueryWrapper<SysUserAdminOrgDO>()
//                    .in("organization_id", allGroupIds)
//                    .eq("user_id", userId)
//                    .select("organization_id"));
//            if (list != null && list.size() > 0) {
//                ids = list.stream().map(SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());
//            }
//        }
        List<String> list = list(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 1).eq("user_id", userId)
                .select("organization_id")).stream().map(SysUserAdminOrgDO::getOrganizationId)
                .collect(Collectors.toList());
            list.stream().forEach(orgId -> {
                OrgGroupDO orgGroupDO = orgGroupService.getById(orgId);
                if (orgGroupDO != null && Objects.equals(0, orgGroupDO.getBeDeleted())) {
                    ids.add(orgId);
                }
            });
//            ids = list.stream().map(SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());

        return ids;
    }
}
