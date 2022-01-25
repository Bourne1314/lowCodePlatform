package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysUserAdminOrgDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.OrgGroupMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.OrgGroupService;
import com.csicit.ace.orgauth.core.service.SysUserAdminOrgService;
import com.csicit.ace.orgauth.core.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集团管理 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Service("orgGroupServiceO")
public class OrgGroupServiceImpl extends BaseServiceImpl<OrgGroupMapper, OrgGroupDO> implements OrgGroupService {
    @Resource(name = "sysUserAdminOrgServiceO")
    SysUserAdminOrgService sysUserAdminOrgService;

    @Resource(name = "sysUserServiceO")
    SysUserService sysUserService;

    @Override
    public List<OrgGroupDO> getGroupsByUserId(String userId) {
        List<OrgGroupDO> list = new ArrayList<>();

        SysUserDO user = sysUserService.getById(userId);
        if (user == null) {
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
        if (StringUtils.isNotBlank(user.getGroupId())) {
            OrgGroupDO group = getById(user.getGroupId());
            if (group != null) {
                list.add(group);
            }
        }

        List<SysUserAdminOrgDO> adminOrgs = sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                .eq("is_activated", 1).eq("user_id", userId));
        if (adminOrgs != null && adminOrgs.size() > 0) {
            List<String> orgIds = adminOrgs.stream().map(SysUserAdminOrgDO::getOrganizationId).collect(Collectors.toList());
            List<OrgGroupDO> listT = list(new QueryWrapper<OrgGroupDO>().eq("is_delete", 0).orderByAsc("sort_path")
                    .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                    .in("id", orgIds));
            if (listT != null && listT.size() > 0) {
                list.addAll(listT);
            }
        }
        return list;
    }
}
