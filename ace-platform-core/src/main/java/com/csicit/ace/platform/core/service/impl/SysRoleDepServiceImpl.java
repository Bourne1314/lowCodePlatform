package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysRoleDepDO;
import com.csicit.ace.data.persistent.mapper.SysRoleDepMapper;
import com.csicit.ace.platform.core.service.OrgDepartmentService;
import com.csicit.ace.platform.core.service.SysRoleDepService;
import com.csicit.ace.platform.core.service.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/4/21 8:55
 */
@Service
public class SysRoleDepServiceImpl extends ServiceImpl<SysRoleDepMapper, SysRoleDepDO> implements SysRoleDepService {

    @Autowired
    OrgDepartmentService orgDepartmentService;

    @Autowired
    SysRoleService sysRoleService;

    @Override
    public boolean deleteRoleDep(String id) {
        SysRoleDepDO sysRoleDepDO = getById(id);
        if (removeById(id)) {
            return sysRoleService.deleteRoleAndDep(sysRoleService.getById(sysRoleDepDO.getRoleId()), sysRoleDepDO.getDepId());
        }
        return false;
    }

    @Override
    public boolean addRoleDep(SysRoleDepDO sysRoleDepDO) {
        if (count(new QueryWrapper<SysRoleDepDO>().eq("role_id", sysRoleDepDO.getRoleId())
                .eq("dep_id", sysRoleDepDO.getDepId())) > 0) {
            throw new RException("此部门已关联当前角色，不需要重复操作！");
        }
        sysRoleDepDO.setCreateTime(LocalDateTime.now());
        if (save(sysRoleDepDO)) {
            return sysRoleService.addRoleAndDep(sysRoleService.getById(sysRoleDepDO.getRoleId()), sysRoleDepDO.getDepId());
        }
        return false;
    }

    @Override
    public List<SysRoleDepDO> getRoleDeps(String roleId) {
        List<SysRoleDepDO> sysRoleDepDOS = list(new QueryWrapper<SysRoleDepDO>()
                .orderByAsc("create_time")
                .eq("role_id", roleId));
        if (CollectionUtils.isNotEmpty(sysRoleDepDOS)) {
            List<OrgDepartmentDO> departmentDOS = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                    .in("id", sysRoleDepDOS.stream().map(SysRoleDepDO::getDepId).collect(Collectors.toList()))
                    .orderByAsc("sort_path")
                    .select("id", "name", "code"));
            for (SysRoleDepDO sysRoleDepDO : sysRoleDepDOS) {
                sysRoleDepDO.setDepName(departmentDOS.stream()
                .filter(dep -> Objects.equals(dep.getId(), sysRoleDepDO.getDepId()))
                .findFirst().get().getName());
            }
            return sysRoleDepDOS;
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrgDepartmentDO> getDeps(String roleId) {
        List<SysRoleDepDO> sysRoleDepDOS = list(new QueryWrapper<SysRoleDepDO>()
                .orderByAsc("create_time")
                .eq("role_id", roleId));
        if (CollectionUtils.isNotEmpty(sysRoleDepDOS)) {
            List<OrgDepartmentDO> departmentDOS = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                    .in("id", sysRoleDepDOS.stream().map(SysRoleDepDO::getDepId).collect(Collectors.toList()))
                    .orderByAsc("sort_path")
                    .select("id", "name", "code"));
            return departmentDOS;
        }
        return new ArrayList<>();
    }

    @Override
    public String getDepsName(String roleId) {
        List<OrgDepartmentDO> departmentDOS = getDeps(roleId);
        if (CollectionUtils.isNotEmpty(departmentDOS)) {
            String depsName = "";
            for (OrgDepartmentDO dep : departmentDOS) {
                depsName += "," + dep.getName();
            }
            return depsName.substring(1);
        }
        return null;
    }
}