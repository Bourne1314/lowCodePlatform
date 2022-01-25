package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.OrgDepartmentMapper;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.JsonUtils;
import com.csicit.ace.platform.core.utils.UuidUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 组织-部门 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:38
 */
@Service("orgDepartmentService")
public class OrgDepartmentServiceImpl extends BaseServiceImpl<OrgDepartmentMapper, OrgDepartmentDO> implements
        OrgDepartmentService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    OrgDepartmentVService orgDepartmentVService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    BdPersonDocService bdPersonDocService;

    @Override
    public boolean mvDep(boolean mvToTop, String id, String orgId, String targetDepId) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(orgId)) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        OrgDepartmentDO department = getById(id);
        OrgOrganizationDO org = orgOrganizationService.getById(orgId);
        if (department == null || org == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        // 是否设置为顶级部门
        Integer sortIndex = department.getSortIndex();
        String newSortPath = "";
        String parentId = "0";
        if (mvToTop) {
            // 判断此部门是否是目标业务单元的顶级部门
            if (Objects.equals(department.getParentId(), "0") && Objects.equals(department.getOrganizationId(),
                    orgId)) {
                throw new RException(InternationUtils.getInternationalMsg("MV_DEP_SAME_ORG"));
            }
            List<OrgDepartmentDO> sonDeps = list(new QueryWrapper<OrgDepartmentDO>().eq("parent_id", "0").eq
                    ("is_delete", 0).eq("organization_id", orgId));
            List<Integer> sortIndexs = sonDeps.stream().map(OrgDepartmentDO::getSortIndex).collect(Collectors
                    .toList());
            while (sortIndexs.contains(sortIndex)) {
                sortIndex++;
            }
            newSortPath = SortPathUtils.getSortPath("", sortIndex);
        } else {
            if (StringUtils.isBlank(targetDepId)) {
                throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            if (Objects.equals(id, targetDepId)) {
                throw new RException(InternationUtils.getInternationalMsg("MV_DEP_TO_SON"));
            }
            OrgDepartmentDO targetDep = getById(targetDepId);
            if (targetDep == null) {
                throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            // 目标部门是否为此部门子节点
            // 判断依据
            // 同一业务单元下 且 目标节点排序路径包含原节点排序路径
            if (Objects.equals(targetDep.getOrganizationId(), department.getOrganizationId()) && targetDep.getSortPath()
                    .startsWith(department
                            .getSortPath())) {
                throw new RException(InternationUtils.getInternationalMsg("MV_DEP_TO_SON"));
            }
            List<OrgDepartmentDO> sonDeps = list(new QueryWrapper<OrgDepartmentDO>().eq("parent_id", targetDepId).eq
                    ("is_delete", 0));
            List<Integer> sortIndexs = sonDeps.stream().map(OrgDepartmentDO::getSortIndex).collect(Collectors
                    .toList());
            while (sortIndexs.contains(sortIndex)) {
                sortIndex++;
            }
            newSortPath = SortPathUtils.getSortPath(targetDep.getSortPath(), sortIndex);
            parentId = targetDepId;
            if (!Objects.equals(targetDep.getOrganizationId(), department.getOrganizationId())) {
                orgId = targetDep.getOrganizationId();
            }
        }
        // 修改子部门
        List<OrgDepartmentDO> list = list(new QueryWrapper<OrgDepartmentDO>().ne("id", id).eq("is_delete", 0)
                .eq("organization_id", department.getOrganizationId())
                .likeRight("sort_path", department.getSortPath()));
        List<OrgDepartmentDO> listT = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            String oldSortPath = department.getSortPath();
            for (int i = 0; i < list.size(); i++) {
                OrgDepartmentDO dept = list.get(i);
                dept.setOrganizationId(orgId);
                String sortPath = dept.getSortPath().substring(oldSortPath.length());
                dept.setSortPath(newSortPath + sortPath);
                listT.add(dept);
            }
        }
        // 保存
        department.setSortIndex(sortIndex);
        department.setParentId(parentId);
        department.setOrganizationId(orgId);
        department.setSortPath(newSortPath);
        listT.add(department);
        if (!updateBatchById(listT)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        // 修改业务单元
        OrgOrganizationDO rootOrg = orgOrganizationService.getById(orgId);
        String rootOrgPath = rootOrg.getSortPath();
        OrgOrganizationDO oldParentOrg = orgOrganizationService.getById(id);
        List<OrgOrganizationDO> orgs = orgOrganizationService.getSonOrgsByOrgId(id, 2, false);
        List<OrgOrganizationDO> orgsT = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orgs)) {
            String oldSortPath = oldParentOrg.getSortPath();
            for (int i = 0; i < orgs.size(); i++) {
                OrgOrganizationDO dept = orgs.get(i);
                String sortPath = dept.getSortPath().substring(oldSortPath.length());
                dept.setSortPath(rootOrgPath + newSortPath + sortPath);
                orgsT.add(dept);
            }
        }
        oldParentOrg.setSortIndex(sortIndex);
        if (Objects.equals(parentId, "0")) {
            oldParentOrg.setParentId(orgId);
        } else {
            oldParentOrg.setParentId(parentId);
        }
        oldParentOrg.setSortPath(rootOrgPath + newSortPath);
        orgsT.add(oldParentOrg);
        if (!orgOrganizationService.updateBatchById(orgsT)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        return true;
    }

    @Override
    public R deleteDep(Map<String, Object> params) {
        // 是否删除子节点
//        boolean deleteSons = (boolean) params.get("deleteSons");
        List<String> ids = (ArrayList) params.get("ids");
        if (CollectionUtils.isNotEmpty(ids)) {
            List<OrgDepartmentDO> list = list(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0)
                    .in("id", ids)
                    .select("id", "name", "sort_path", "ORGANIZATION_ID", "group_id"));
//            Set<String> idSet = new HashSet<>();
//            idSet.addAll(ids);
            // 存在子部门 不可删除
            if (count(new QueryWrapper<OrgDepartmentDO>().in("parent_id", ids).eq("is_delete", 0)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "存在子部门"));
            }

            // 存在职务信息 不可删除
            List<String> personIds = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().in("department_id", ids))
                    .stream().map(BdPersonJobDO::getPersonDocId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(personIds)) {
                if (bdPersonDocService.count(new QueryWrapper<BdPersonDocDO>().in("id", personIds).eq("is_delete", 0)
                ) > 0) {
                    throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "该部门下存在人员职务信息"));
                }
            }

            // 通过sort_path删除子节点
//            for (int i = 0; i < list.size(); i++) {
//                OrgDepartmentDO dep = list.get(i);
//                String sortPath = dep.getSortPath();
//                if (StringUtils.isNotBlank(sortPath)) {
//                    List<OrgDepartmentDO> orgDepartmentDOS = list(new QueryWrapper<OrgDepartmentDO>().eq("is_delete",
//                            0).likeRight("sort_path", sortPath).eq("ORGANIZATION_ID", dep
//                            .getOrganizationId()));
//                    List<String> sonIds = orgDepartmentDOS.stream().map(OrgDepartmentDO::getId).collect(Collectors
//                            .toList());
//                    idSet.addAll(sonIds);
//                }
//            }
//            ids = new ArrayList<>();
//            ids.addAll(idSet);
            if (this.update(new OrgDepartmentDO(), new UpdateWrapper<OrgDepartmentDO>().setSql("code=CONCAT(CONCAT" +
                    "('del-', SUBSTR" +
                    "(id,1,2)),code)")
                    .setSql("name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),name)").setSql("parent_id=CONCAT" +
                            "(CONCAT" +
                            "('del-', SUBSTR(id,1,2)),parent_id)").set("is_delete", 1)
                    .in("id", ids))) {

                // 删除业务单元表的数据
                if (!orgOrganizationService.removeOrgs(ids)) {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }
                if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除部门", list
                        .parallelStream().map
                                (OrgDepartmentDO::getName)
                        .collect(Collectors.toList()), list.get(0).getGroupId(), null)) {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }
                return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
            }
            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "部门主键"));
    }

    /**
     * 保存部门
     *
     * @param dep
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:02
     */
    @Override
    public boolean saveDep(OrgDepartmentDO dep) {
        String rootId = "0";
        String parentId = dep.getParentId();
        String sortPath;
        if (StringUtils.isBlank(parentId) || dep.getSortIndex() == null) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        } else if (Objects.equals(rootId, parentId)) {
            sortPath = SortPathUtils.getSortPath("", dep.getSortIndex());
        } else {
            OrgDepartmentDO parent = this.getById(parentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), dep.getSortIndex());
        }
        aceSqlUtils.validateTreeTableWithUnique("org_department", parentId, dep.getSortIndex(), sortPath,
                "organization_id", dep.getOrganizationId());
        dep.setSortPath(sortPath);
        dep.setCreateTime(LocalDateTime.now());
        dep.setCreateUser(securityUtils.getCurrentUserId());
        dep.setUpdateTime(LocalDateTime.now());
        dep.setId(UuidUtil.createUUID());
        /**
         * 保留固化版本
         */
        OrgDepartmentVDO depV = JsonUtils.castObjectForSetIdNull(dep, OrgDepartmentVDO.class);
        depV.setDepartmentId(dep.getId());
        depV.setLastVersion(1);
        depV.setVersionBeginDate(LocalDate.now());
        depV.setVersionBeginUserId(dep.getCreateUser());
        if (!orgDepartmentVService.save(depV)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_VERSION_POJO"));
        }

        dep.setVersionId(depV.getId());
        if (!this.save(dep)) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        /**
         * 插入业务单元表
         */
        OrgOrganizationDO org = JsonUtils.castObject(dep, OrgOrganizationDO.class);
        List<String> types = new ArrayList<>();
        types.add("department");

        // 部门为2
        org.setBusinessUnit(2);


        org.setOrgType(types);
        // 如果部门的父主键为0 则修改为所属业务单元主键
        if (Objects.equals("0", org.getParentId())) {
            org.setParentId(dep.getOrganizationId());
        }
        if (!orgOrganizationService.saveOrg(org)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_ORGANIZATION"));
        }

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存部门", dep, dep.getGroupId()
                , null)) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return true;
    }

    /**
     * 修改部门
     *
     * @param dep
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:02
     */
    @Override
    public boolean updateDep(OrgDepartmentDO dep) {
        /** 验证相关数据  并更新子节点排序路径
         * *******************************************************************************
         */
        String parentId = dep.getParentId();
        String sortPath = "";
        String result = aceSqlUtils.verifyParentIdWithUnique("org_department", parentId, dep.getId(), dep.getSortIndex(),
                "organization_id", dep.getOrganizationId());
        if (Objects.equals("rootParent", result)) {
            // 父节点为根节点
            sortPath = SortPathUtils.getSortPath("", dep.getSortIndex());
            dep.setSortPath(sortPath);
        } else if (Objects.equals("nullSortPath", result)) {
            // 父节点发生改变 或 sortIndex 发生变化
            OrgDepartmentDO parentDep = this.getById(parentId);
            if (parentDep == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parentDep
                    .getSortPath(), dep.getSortIndex());
            dep.setSortPath(sortPath);
        } else if (Objects.equals("nullParentId", result)) {
            //父节点ID为空
            return false;
        }
        dep.setUpdateTime(LocalDateTime.now());
        OrgDepartmentDO oldDep = getById(dep.getId());
        // 更新
        if (!this.updateById(dep)) {
            return false;
        }
        //index及父节点未发生变化 则不需要变更子节点
        if (!Objects.equals("noChange", result)) {
            aceSqlUtils.updateSonSortPathWithUnique("org_department", dep.getSortPath(), oldDep.getSortPath().length(),
                    oldDep
                            .getSortPath(), "organization_id", dep.getOrganizationId());
        }
        /**
         * ************************************************************************************
         */
        /**
         * 保留固化版本
         */
        OrgDepartmentVDO depV = JsonUtils.castObject(dep, OrgDepartmentVDO.class);
        if (!orgDepartmentVService.update(depV, new UpdateWrapper<OrgDepartmentVDO>()
                .eq("department_id", dep.getId()).eq("is_last_version", 1))) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_VERSION_POJO"));
        }
        /**
         * 插入业务单元表
         */
        if (Objects.equals("rootParent", result) || Objects.equals("0", dep.getParentId())) {
            dep.setParentId(dep.getOrganizationId());
        }
        OrgOrganizationDO org = JsonUtils.castObject(dep, OrgOrganizationDO.class);
        org.setBusinessUnit(2);
        if (!orgOrganizationService.updateOrg(org)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_ORGANIZATION"));
        }

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新部门", dep, dep.getGroupId()
                , null)) {
            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        }

        return true;
    }

    /**
     * 版本化业务单元
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:03
     */
    @Override
    public boolean versionOrg(Map<String, String> map) {
        String versionName = map.get("versionName");
        String versionNo = map.get("versionNo");
        String depId = map.get("depId");
        OrgDepartmentVDO depV = orgDepartmentVService.getOne(new QueryWrapper<OrgDepartmentVDO>().eq
                ("department_id", depId).eq("is_last_version", 1));
        if (depV == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        OrgDepartmentVDO depVNew = depV;
        /**
         * 固化之前的数据
         */
        OrgDepartmentVDO tempDepV = new OrgDepartmentVDO();
        tempDepV.setVersionEndUserId(securityUtils.getCurrentUserId());
        tempDepV.setVersionEndDate(LocalDate.now());
        tempDepV.setVersionName(versionName);
        tempDepV.setVersionNo(versionNo);
        tempDepV.setLastVersion(0);
        tempDepV.setId(depV.getId());
        if (!orgDepartmentVService.updateById(tempDepV)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        /**
         * 创建新的版本
         */
        String versionId = UuidUtil.createUUID();
        depVNew.setId(versionId);
        depVNew.setVersionBeginUserId(tempDepV.getVersionEndUserId());
        depVNew.setVersionBeginDate(LocalDate.now());
        if (!orgDepartmentVService.save(depVNew)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        /**
         * 更新VersionID
         */
        if (!this.update(new OrgDepartmentDO(), new UpdateWrapper<OrgDepartmentDO>().eq("is_delete", 0)
                .eq("id", depId).set("version_id", versionId))) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "版本化部门", tempDepV, tempDepV
                .getGroupId(), null)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        map.put("organizationId", depId);
        return orgOrganizationService.versionOrg(map);
    }

    /**
     * 保存业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:03
     */
    @Override
    public boolean saveOrg(OrgOrganizationDO orgOrganizationDO) {
        return true;
    }

    /**
     * 修改业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:03
     */
    @Override
    public boolean updateOrg(OrgOrganizationDO orgOrganizationDO) {
        return true;
    }

    /**
     * 删除业务单元
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:03
     */
    @Override
    public boolean deleteOrg(Map<String, Object> map) {
        return true;
    }
}
