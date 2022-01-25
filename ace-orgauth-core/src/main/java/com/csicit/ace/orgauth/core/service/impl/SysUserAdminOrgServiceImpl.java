package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysUserAdminOrgDO;
import com.csicit.ace.data.persistent.mapper.SysUserAdminOrgMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.OrgOrganizationService;
import com.csicit.ace.orgauth.core.service.SysUserAdminOrgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
@Service("sysUserAdminOrgServiceO")
public class SysUserAdminOrgServiceImpl extends BaseServiceImpl<SysUserAdminOrgMapper, SysUserAdminOrgDO> implements
        SysUserAdminOrgService {
    @Resource(name = "orgOrganizationServiceO")
    OrgOrganizationService orgOrganizationService;

    @Override
    public List<String> getGroupsByUserId(String userId) {
        List<String> ids = new ArrayList<>(16);
        List<String> list = list(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 1).eq("user_id", userId)
                .select("organization_id")).stream().map(SysUserAdminOrgDO::getOrganizationId)
                .collect(Collectors.toList());

        list.stream().forEach(orgId -> {
            OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById(orgId);
            if (orgOrganizationDO != null && Objects.equals(0, orgOrganizationDO.getBeDeleted())) {
                ids.add(orgId);
            }
        });

        return ids;
    }
}
