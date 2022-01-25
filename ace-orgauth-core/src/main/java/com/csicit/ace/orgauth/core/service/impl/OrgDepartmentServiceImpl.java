package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.data.persistent.mapper.OrgDepartmentMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.BdPersonJobService;
import com.csicit.ace.orgauth.core.service.OrgDepartmentService;
import com.csicit.ace.orgauth.core.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 组织-部门 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:38
 */
@Service("orgDepartmentServiceO")
public class OrgDepartmentServiceImpl extends BaseServiceImpl<OrgDepartmentMapper, OrgDepartmentDO> implements
        OrgDepartmentService {

    @Resource(name = "sysUserServiceO")
    SysUserService sysUserService;


    @Resource(name = "orgDepartmentServiceO")
    OrgDepartmentService orgDepartmentService;
    @Resource(name = "bdPersonJobServiceO")
    BdPersonJobService bdPersonJobService;

    @Override
    public List<OrgDepartmentDO> getDeptsByUserId(String userId) {
        SysUserDO user = sysUserService.getById(userId);
        if (user != null) {
            String personId = user.getPersonDocId();
            if (StringUtils.isNotBlank(personId)) {
                List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                        personId));
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(jobs)) {
                    Set<String> depIds = jobs.stream().map(BdPersonJobDO::getDepartmentId).collect(Collectors.toSet());
                    if (!org.apache.commons.collections.CollectionUtils.isEmpty(depIds)) {
                        return list(new QueryWrapper<OrgDepartmentDO>().in("id", depIds));
                    }
                }
            }
        }
        return new ArrayList<>();
    }


    @Override
    public OrgDepartmentDO getMainDeptByUserId(String userId) {
        SysUserDO user = sysUserService.getById(userId);
        if (user != null) {
            String personId = user.getPersonDocId();
            if (StringUtils.isNotBlank(personId)) {
                BdPersonJobDO job = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id", personId).eq
                        ("is_main_job", 1));
                if (job != null) {
                    String depId = job.getDepartmentId();
                    if (StringUtils.isNotBlank(depId)) {
                        return getById(depId);
                    }
                }
            }
        }
        return new OrgDepartmentDO();
    }

    @Override
    public List<OrgDepartmentDO> getOrgDepartmentListByUserIds(Set<String> userIds) {
        List<BdPersonJobDO> bdPersonJobDOS = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in("person_doc_id",userIds).eq("is_main_job",1));
        Set<String> bdSet = new HashSet<>();
        for(BdPersonJobDO bdPersonJobDO:bdPersonJobDOS){
            bdSet.add(bdPersonJobDO.getDepartmentId());
        }
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(bdSet)) {
            return list(new QueryWrapper<OrgDepartmentDO>().in("id", bdSet).select("id",
                    "name", "parent_id", "sort_index").eq("is_delete", 0));
        }
          return new ArrayList<>();
    }
    @Override


    public List<OrgDepartmentDO> listdeliverDepartment(Set<String> userIds) {
        List<SysUserDO> sysUserDOS = sysUserService.list(new QueryWrapper<SysUserDO>().in("id",userIds));
        List<String> userIdList= new ArrayList<>();
        for(Integer i = 0;i < sysUserDOS.size();i++){
            userIdList.add(sysUserDOS.get(i).getPersonDocId());
        }
        Set<String> userIdSet = new HashSet<>(userIdList);
        List<BdPersonJobDO> bdPersonJobDOS = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in("person_doc_id",userIdSet).eq("is_main_job",1).select("department_id"));
        Set<String> bdSet = new HashSet<>();
        for(BdPersonJobDO bdPersonJobDO:bdPersonJobDOS){
            bdSet.add(bdPersonJobDO.getDepartmentId());
        }
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(bdSet)) {
            return list(new QueryWrapper<OrgDepartmentDO>().in("id", bdSet).select("id",
                    "name", "parent_id", "sort_index").eq("is_delete", 0));
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByDepartmentId(String departmentId) {

        List<BdPersonJobDO> bdPersonJobDOS = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq("department_id",departmentId));

        Set<String> personDocIdSet = bdPersonJobDOS.stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors.toSet());

        if(!personDocIdSet.isEmpty()){
            return sysUserService.list(new QueryWrapper<SysUserDO>().in("person_doc_id",personDocIdSet));
        }
        return new ArrayList<>();
    }
    @Override
    public List<SysUserDO> getUsersByUserIds(List<String> userIds) {
        List<SysUserDO> sysUserDOS = sysUserService.list(new QueryWrapper<SysUserDO>().in("id",userIds));
        return sysUserDOS;
    }

    @Override
    public List<OrgDepartmentDO> getDeptsByOrganizationId(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            List<OrgDepartmentDO> list = list(new QueryWrapper<OrgDepartmentDO>().eq
                    ("organization_id", organizationId).eq("is_delete", 0).orderByAsc("sort_path"));
            List<OrgDepartmentDO> listT = TreeUtils.makeTree(list, OrgDepartmentDO.class);
            return listT;
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrgDepartmentDO> getDepartmentByGroupId(String groupId) {
        if (StringUtils.isNotBlank(groupId)) {
            List<OrgDepartmentDO> list = list(new QueryWrapper<OrgDepartmentDO>().eq("group_id",
                    groupId).orderByAsc("sort_path").eq("is_delete", 0));
            if (list != null && list.size() > 0) {
                return TreeUtils.makeTree(list, OrgDepartmentDO.class);
            }
        }
        return new ArrayList<>();
    }
}
