package com.csicit.ace.platform.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.OrgOrganizationMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.JsonUtils;
import com.csicit.ace.platform.core.utils.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织-组织 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:16:53
 */
@Service("orgOrganizationService")
public class OrgOrganizationServiceImpl extends BaseServiceImpl<OrgOrganizationMapper, OrgOrganizationDO> implements
        OrgOrganizationService {

    public final static String LOG_FLAG = "业务单元";

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    OrgOrganizationTypeService orgOrganizationTypeService;

    @Autowired
    OrgOrganizationVService orgOrganizationVService;

    @Autowired
    OrgOrganizationVTypeService orgOrganizationVTypeService;

    @Autowired
    OrgDepartmentService orgDepartmentService;

    @Autowired
    OrgGroupService orgGroupService;

    @Autowired
    OrgMaintainOrgService orgMaintainOrgService;

    @Autowired
    OrgCorporationService orgCorporationService;

    BaseOrgService baseOrgService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    BdPersonDocService personDocService;


    @Override
    public boolean saveLoadUnits(List<OrgOrganizationDO> list) {
        int maxSortIndex = 0;
        String groupId = list.get(0).getGroupId();
        OrgOrganizationDO maxOrganization = getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("parent_id", groupId).eq("is_delete", 0)
                .orderByDesc("sort_index"));
        if (maxOrganization != null) {
            maxSortIndex = maxOrganization.getSortIndex();
        }
        Map<String, OrgOrganizationDO> idAndOrgs = new HashMap<>();
        list.parallelStream().forEach(org -> {
            idAndOrgs.put(org.getId(), org);
        });
        Map<String, Integer> idAndCounts = new HashMap<>();
        int count = 0;
        for (OrgOrganizationDO org : list) {
            idAndCounts.put(org.getId(), count);
            count++;
        }
        // 存放同一父节点下的子节点的排序号
        Map<String, Integer> parentIdAndCounts = new HashMap<>();
        parentIdAndCounts.put(groupId, maxSortIndex);
        List<OrgOrganizationDO> orgs = new ArrayList<>(list);

        // 排序 确保子节点在父节点后面
        orgs.forEach(org -> {
            if (!idAndCounts.containsKey(org.getParentId()) && !Objects.equals(org.getParentId(), groupId)) {
                int parentInt = idAndCounts.get(org.getParentId());
                int sonInt = idAndCounts.get(org.getId());
                if (sonInt < parentInt) {
                    list.set(parentInt, idAndOrgs.get(org.getId()));
                    list.set(sonInt, idAndOrgs.get(org.getParentId()));
                }
                idAndCounts.put(org.getId(), parentInt);
                idAndCounts.put(org.getParentId(), sonInt);
            }
        });
        List<String> types = new ArrayList<>();
        types.add("corporation");
        Map<String, String> idAndUUIDs = new HashMap<>();
        idAndUUIDs.put(groupId, groupId);
        // 填充 排序号
        list.forEach(org -> {
            // parentid 为空 或者 不在列表里  都置为 groupId
            if (!idAndCounts.containsKey(org.getParentId())) {
                org.setParentId(groupId);
            }
            if (parentIdAndCounts.containsKey(org.getParentId())) {
                int sortIndex = parentIdAndCounts.get(org.getParentId());
                parentIdAndCounts.put(org.getParentId(), sortIndex + 10);
                org.setSortIndex(sortIndex + 10);
            } else {
                parentIdAndCounts.put(org.getParentId(), 10);
                org.setSortIndex(10);
            }
            org.setBusinessUnit(1);
            org.setOrgType(types);
            idAndUUIDs.put(org.getId(), UuidUtils.createUUID());
        });

        list.stream().forEach(org -> {
            org.setId(idAndUUIDs.get(org.getId()));
            org.setParentId(idAndUUIDs.get(org.getParentId()));
            saveOrg(org);
        });
        if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "保存"), "批量导入业务单元", "批量导入业务单元：" + list
                        .stream().map(OrgOrganizationDO::getName).collect(Collectors.toList()), groupId,
                securityUtils.getAppName())) {
            return true;
        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    @Override
    public boolean mvBusinessUnit(boolean mvToTop, String id, String groupId, String targetOrgId) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(groupId)) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        OrgOrganizationDO org = getById(id);
        OrgGroupDO group = orgGroupService.getById(groupId);
        if (org == null || group == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        // 是否设置为顶级业务单元
        Integer sortIndex = org.getSortIndex();
        String newSortPath = "";
        String parentId = "";
        if (mvToTop) {
            // 判断此业务单元是否是目标集团的顶级业务单元
            if (Objects.equals(org.getParentId(), groupId)) {
                throw new RException(InternationUtils.getInternationalMsg("MV_BUSINESSUNIT_SAME_GROUP"));
            }
            List<OrgOrganizationDO> sonOrgs = list(new QueryWrapper<OrgOrganizationDO>().eq("parent_id", groupId).eq
                    ("is_business_unit", 1).eq("is_delete", 0));
            List<Integer> sortIndexs = sonOrgs.stream().map(OrgOrganizationDO::getSortIndex).collect(Collectors
                    .toList());
            while (sortIndexs.contains(sortIndex)) {
                sortIndex++;
            }
            newSortPath = SortPathUtils.getSortPath(group.getSortPath(), sortIndex);
            parentId = groupId;
        } else {
            if (StringUtils.isBlank(targetOrgId)) {
                throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            if (Objects.equals(id, targetOrgId)) {
                throw new RException(InternationUtils.getInternationalMsg("MV_BUSINESSUNIT_TO_SON"));
            }
            OrgOrganizationDO targetOrg = getById(targetOrgId);
            if (targetOrg == null) {
                throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            // 目标业务单元是否为此业务单元子节点
            // 判断依据
            // 同一集团下 且 目标节点排序路径包含原节点排序路径
            if (Objects.equals(targetOrg.getGroupId(), org.getGroupId()) && targetOrg.getSortPath().startsWith(org
                    .getSortPath())) {
                throw new RException(InternationUtils.getInternationalMsg("MV_BUSINESSUNIT_TO_SON"));
            }
            List<OrgOrganizationDO> sonOrgs = list(new QueryWrapper<OrgOrganizationDO>().eq("parent_id", targetOrgId).eq
                    ("is_business_unit", 1).eq("is_delete", 0));
            List<Integer> sortIndexs = sonOrgs.stream().map(OrgOrganizationDO::getSortIndex).collect(Collectors
                    .toList());
            while (sortIndexs.contains(sortIndex)) {
                sortIndex++;
            }
            parentId = targetOrgId;
            newSortPath = SortPathUtils.getSortPath(targetOrg.getSortPath(), sortIndex);
            if (!Objects.equals(targetOrg.getGroupId(), org.getGroupId())) {
                groupId = targetOrg.getGroupId();
            }
        }
        // 修改业务单元 集团ID 部门
        // 修改原业务单元
        if (updateSonSortPath(id, newSortPath, groupId)) {
            org.setGroupId(groupId);
            org.setParentId(parentId);
            org.setSortPath(newSortPath);
            org.setSortIndex(sortIndex);
            if (updateById(org)) {
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "移动业务单元","移动业务单元："+ org.getName(), groupId,
                        null)) {
                    return true;
                }
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));

    }

    @Override
    public boolean removeOrgs(List<String> ids) {
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            return update(new OrgOrganizationDO(), new UpdateWrapper<OrgOrganizationDO>().setSql
                    ("code=CONCAT" +
                            "(CONCAT" +
                            "('del-', SUBSTR(id,1,2)),code)")
                    .setSql("name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),name)").setSql("parent_id=CONCAT" +
                            "(CONCAT" +
                            "('del-', SUBSTR(id,1,2)),parent_id)").set("is_delete", 1)
                    .eq(ids.size() == 1, "id", ids.get(0))
                    .in(ids.size() > 1, "id", ids));
        }
        return false;
    }

    @Override
    public R deleteOrg(Map<String, Object> params) {
        // 是否删除子节点
//        boolean deleteSons = (boolean) params.get("deleteSons");
        List<String> ids = (ArrayList) params.get("ids");
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            List<OrgOrganizationDO> list = list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                    .in("id", ids).eq("is_business_unit", 1)
                    .select("id", "name", "group_id"));
            //存在子业务单元 不可删除
            if (count(new QueryWrapper<OrgOrganizationDO>().in("parent_id", ids).eq("is_delete", 0)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "存在子业务单元或部门"));
            }

            //存在人员 不可以删
            if (personDocService.count(new QueryWrapper<BdPersonDocDO>().in("organization_id", ids).eq("is_delete",
                    0)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "存在人员"));
            }
            //存在用户 不可以删除
            if (sysUserService.count(new QueryWrapper<SysUserDO>().in("organization_id", ids).eq("is_delete", 0)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "存在用户"));
            }
//            Set<String> idSet = new HashSet<>();
//            idSet.addAll(ids);
//        boolean result = false;
//        if (!deleteSons) {
//            int count = count(new QueryWrapper<OrgOrganizationDO>().eq("is_delete",0)
//                    .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
//                    .in("parent_id", ids));
//            if (count > 0) {
//                return R.error(InternationUtils.getInternationalMsg("DELETE_ERROR_FOR_EXIST_SON"));
//            } else {
//                //removeByIds(ids);
//                if (ids.size() > 0) {
//                    this.update(new OrgOrganizationDO(), new UpdateWrapper<OrgOrganizationDO>().setSql("code=CONCAT
// (CONCAT('del-', SUBSTR(id,1,2)),code)")
//                            .setSql("name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),name)").setSql("parent_id=CONCAT" +
//                                    "(CONCAT" +
//                                    "('del-', SUBSTR(id,1,2)),parent_id)").set("is_delete", 1).in("id", ids));
//                }
//            }
//        } else {
//            // 通过sort_path删除子节点
//            for (int i = 0; i < list.size(); i++) {
//                idSet.addAll(getSonIdsOrgsByOrgId(list.get(i).getId(), 1, false));
//            }
            //       }
//            ids = new ArrayList<>();
//            ids.addAll(idSet);
            if (removeOrgs(ids)) {
//                // 删除业务单元下面的部门
//                List<OrgDepartmentDO> deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().in
//                        ("organization_id", idSet).eq("is_delete", 0));
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(deps)) {
//                    List<String> depIds = deps.stream().map(OrgDepartmentDO::getId).collect(Collectors.toList());
//                    params.put("ids", depIds);
//                    params.put("deleteSons", true);
//                    orgDepartmentService.deleteDep(params);
//                }
//
//                //20191107 0955 用户不在包含业务单元ID属性
//                //20191108 恢复
//                // 删除 用户
//                List<SysUserDO> users = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0).in
//                        ("organization_id", ids).select("id"));
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(users)) {
//                    List<String> userIds = users.stream().map(SysUserDO::getId).collect(Collectors.toList());
//                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIds)) {
//                        if (!sysUserService.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
//                                .in("id", userIds)
//                                .setSql("person_doc_id=null")
//                                .setSql("staff_no=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),staff_no)")
//                                .setSql("user_name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),user_name)")
//                                .set("is_delete", 1))) {
//                            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
//                        }
//                    }
//                }
//
//                // 删除 人员
//                List<BdPersonDocDO> persons = personDocService.list(new QueryWrapper<BdPersonDocDO>().eq("is_delete",
//                        0).in
//                        ("organization_id", ids).select("id"));
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(persons)) {
//                    List<String> personIds = persons.stream().map(BdPersonDocDO::getId).collect(Collectors.toList());
//                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(personIds)) {
//                        if (!personDocService.update(new BdPersonDocDO(), new UpdateWrapper<BdPersonDocDO>()
//                                .in("id", personIds)
//                                .setSql("code=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),code)")
//                                .set("is_delete", 1))) {
//                            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
//                        }
//                        // 删除用户关系
//                        if (sysUserService.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0).in
// ("person_doc_id", personIds))
//                                > 0) {
//                            if (!sysUserService.update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in
// ("person_doc_id",
//                                    ids).setSql("person_doc_id=null"))) {
//                                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
//                            }
//                        }
//                    }
//                }

                if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除业务单元","删除业务单元："+ list
                        .parallelStream().map
                                (OrgOrganizationDO::getName)
                        .collect(Collectors.toList()), list.get(0).getGroupId(), null)) {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }
                return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
            }
            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "业务单元主键"));
    }

    /**
     * 版本化业务单元
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/18 17:32
     */
    @Override
    public boolean versionOrg(Map<String, String> map) {
        String versionName = map.get("versionName");
        String versionNo = map.get("versionNo");
        String orgId = map.get("organizationId");
        if (StringUtils.isBlank(versionName) || StringUtils.isBlank(versionNo) || StringUtils.isBlank(orgId)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_VERSION_DATA"));
        }
        OrgOrganizationVDO orgV = orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>().eq
                ("organization_id", orgId).eq("is_last_version", 1));
        if (orgV == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        OrgOrganizationVDO orgVNew = orgV;
        /**
         * 固化版本
         */
        OrgOrganizationVDO tempOrgV = new OrgOrganizationVDO();
        tempOrgV.setVersionEndUserId(securityUtils.getCurrentUserId());
        tempOrgV.setVersionEndDate(LocalDate.now());
        tempOrgV.setVersionName(versionName);
        tempOrgV.setVersionNo(versionNo);
        tempOrgV.setLastVersion(0);
        tempOrgV.setId(orgV.getId());
        if (!orgOrganizationVService.updateById(tempOrgV)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        /**
         * 创建新的版本
         */
        String versionId = UuidUtil.createUUID();
        orgVNew.setId(versionId);
        orgVNew.setVersionBeginUserId(tempOrgV.getVersionEndUserId());
        orgVNew.setVersionBeginDate(LocalDate.now());
        if (!orgOrganizationVService.save(orgVNew)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        // 业务单元类型 V 也更新
        OrgOrganizationTypeDO type = orgOrganizationTypeService.getById(orgId);
        OrgOrganizationVTypeDO typeV = JsonUtils.castObjectForSetIdNull(type, OrgOrganizationVTypeDO.class);
        typeV.setId(versionId);
        if (!orgOrganizationVTypeService.save(typeV)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        /**
         * 更新VersionID
         */
        if (!this.update(new OrgOrganizationDO(), new UpdateWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                .eq("id", orgId).set("version_id", versionId))) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        List<String> orgTypes = getOrgType(orgId);
        OrgOrganizationTypeDO typeDo = new OrgOrganizationTypeDO();
        orgTypes.forEach(orgType -> {
            setTypeAndService(typeDo, orgType);
            /**
             * 各个业务单元类型更新
             */
//            if (!baseOrgService.versionOrg(map)) {
//                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//            }
        });
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "版本化业务单元", "版本化业务单元："+tempOrgV.getName(), getById
                (orgId).getGroupId
                (), null)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        return true;
    }

//    /**
//     * 保存多个业务单元
//     *
//     * @param orgs
//     * @return
//     * @author yansiyang
//     * @date 2019/5/6 15:51
//     */
//    @Override
//    public boolean saveOrgs(List<JSONObject> orgs) {
//        orgs.parallelStream().forEach(org -> {
//            /**
//             * 保存法人类型的业务单元
//             */
//            if (Objects.equals(org.getString("orgType"), "corporation")) {
//                org.put("id", UuidUtil.createUUID());
//                if (!saveOrg(JsonUtils.castObject(org, OrgOrganizationDO.class))) {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
//                if (!orgCorporationService.saveCorp(JsonUtils.castObject(org, OrgCorporationDO.class))) {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
//            } else {
//                if (!saveOrg(JsonUtils.castObject(org, OrgOrganizationDO.class))) {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
//            }
//        });
//        return true;
//    }

    /**
     * 保存多个业务单元
     *
     * @param org
     * @return
     * @author yansiyang
     * @date 2019/5/6 15:51
     */
    @Override
    public boolean saveOrgs(JSONObject org) {
        /**
         * 保存法人类型的业务单元
         */
        if (Objects.equals(org.getString("orgType"), "corporation")) {
            org.put("id", UuidUtil.createUUID());
            if (!saveOrg(JsonUtils.castObject(org, OrgOrganizationDO.class))) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
//                if (!orgCorporationService.saveCorp(JsonUtils.castObject(org, OrgCorporationDO.class))) {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
        } else {
            if (!saveOrg(JsonUtils.castObject(org, OrgOrganizationDO.class))) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
        }
        ;
        return true;
    }

    /**
     * 保存组织
     *
     * @param org
     * @return
     * @author yansiyang
     * @date 2019/4/16 8:16
     */
    @Override
    public boolean saveOrg(OrgOrganizationDO org) {
        /**
         * sortPath
         */
        String parentId = org.getParentId();
        String rootId = "0";
        String sortPath;
        // 校验编码
        String code = org.getCode();
        int count = this.count(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0).eq("code", code));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("SAME_CODE_EXIST"));
        }
        if (StringUtils.isBlank(parentId) || org.getSortIndex() == null) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        } else if (Objects.equals(rootId, parentId)) {
            sortPath = SortPathUtils.getSortPath("", org.getSortIndex());
        } else {
            OrgOrganizationDO parent = this.getById(parentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), org.getSortIndex());
        }
        aceSqlUtils.validateTreeTableWithUniqueForOrg("org_organization", parentId, org.getSortIndex(), sortPath,
                "group_id",
                org.getGroupId(), org.getBusinessUnit());
        org.setSortPath(sortPath);

        List<String> orgTypes = org.getOrgType();
        if (orgTypes == null || orgTypes.size() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("NULL_ORG_TYPE"));
        }
        /**
         * OrgOrganizationDO 转换成 OrgOrganizationVDO
         * 保存固化版本
         */
        org.setCreateTime(LocalDateTime.now());
        org.setCreateUser(securityUtils.getCurrentUserId());
        org.setUpdateTime(LocalDateTime.now());
        if (StringUtils.isBlank(org.getId())) {
            org.setId(UuidUtils.createUUID());
        }

        OrgOrganizationVDO orgV = JsonUtils.castObjectForSetIdNull(org, OrgOrganizationVDO.class);
        orgV.setOrganizationId(org.getId());
        orgV.setLastVersion(1);
        orgV.setVersionBeginUserId(org.getCreateUser());
        orgV.setVersionBeginDate(LocalDate.now());
        if (!orgOrganizationVService.save(orgV)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_VERSION_POJO"));
        }
        /**
         * 保存组织
         */
        org.setVersionId(orgV.getId());
        // org.setBusinessUnit(1);
        if (this.save(org)) {
            /**
             * 保存组织类型
             */
            OrgOrganizationTypeDO type = new OrgOrganizationTypeDO();
            type.setCorporation(0);
            type.setProject(0);
            type.setAsset(0);
            type.setSales(0);
            type.setStock(0);
            type.setPurchase(0);
            type.setQc(0);
            type.setTraffic(0);
            type.setMaintain(0);
            type.setHr(0);
            type.setAdministration(0);
            type.setFactory(0);
            type.setIsGroup(0);
            type.setFinance(0);
            type.setDepartment(0);
            orgTypes.forEach(orgType -> {
                setTypeAndService(type, orgType);
                /**
                 * 保存各类业务单元表
                 * Group Corperation Department 不在此保存
                 */
//                if (!baseOrgService.saveOrg(org)) {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
            });
            type.setId(org.getId());
            if (!orgOrganizationTypeService.save(type)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            /**
             * 保存组织固化V类型
             */
            OrgOrganizationVTypeDO typeV = new OrgOrganizationVTypeDO();
            orgTypes.forEach(orgType -> {
                setType(typeV, orgType);
            });
            typeV.setId(orgV.getId());
            if (!orgOrganizationVTypeService.save(typeV)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }

            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存业务单元","保存业务单元："+ org.getName(), org
                    .getGroupId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return true;
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改子节点排序路径  包括 org 和 dep
     *
     * @param orgId
     * @param newSortPath 新的排序路径
     * @param newGroupId  新的集团ID
     * @return
     * @author yansiyang
     * @date 2019/11/1 9:57
     */
    @Override
    public boolean updateSonSortPath(String orgId, String newSortPath, String newGroupId) {
        if (StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(newSortPath)) {
            OrgOrganizationDO oldOrg = getById(orgId);
            if (oldOrg != null) {
                List<OrgOrganizationDO> list = getSonOrgsByOrgId(orgId, 3, false);
                if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                    return true;
                }
                // 判断是否修改集团
                boolean changeGroup = StringUtils.isNotBlank(newGroupId) && !Objects.equals(newGroupId, oldOrg
                        .getGroupId());
                String oldSortPath = oldOrg.getSortPath();
                list.forEach(org -> {
                    if (changeGroup) {
                        org.setGroupId(newGroupId);
                    }
                    String sortPath = org.getSortPath().substring(oldSortPath.length());
                    org.setSortPath(newSortPath + sortPath);
                });
                return updateBatchById(list);
            }
        }
        return false;
    }

    /**
     * 递归获取 业务单元的所有子节点列表  包括业务单元和部门
     *
     * @param orgId
     * @param type  1 仅业务单元 2 仅部门 3 业务单元和部门 4集团 5 全部
     * @return
     * @author yansiyang
     * @date 2019/11/1 10:01
     */
    @Override
    public List<OrgOrganizationDO> getSonOrgsByOrgId(String orgId, Integer type, boolean addParent) {
        if (StringUtils.isNotBlank(orgId)) {
            List<OrgOrganizationDO> list = new ArrayList<>();
            if (addParent) {
                list.add(getById(orgId));
            }
            getSonOrgsByOrgId(orgId, list);
            if (type == 1) {
                return list.stream().filter(orgOrganizationDO -> orgOrganizationDO.getBusinessUnit() == 1)
                        .collect(Collectors.toList());
            } else if (type == 2) {
                return list.stream().filter(orgOrganizationDO -> orgOrganizationDO.getBusinessUnit() == 2)
                        .collect(Collectors.toList());
            } else if (type == 3) {
                return list.stream().filter(orgOrganizationDO -> orgOrganizationDO.getBusinessUnit() != 0)
                        .collect(Collectors.toList());
            } else if (type == 4) {
                return list.stream().filter(orgOrganizationDO -> orgOrganizationDO.getBusinessUnit() == 0)
                        .collect(Collectors.toList());
            }
            return list;
        }
        return new ArrayList<>();

    }

    @Override
    public List<String> getSonIdsOrgsByOrgId(String orgId, Integer type, boolean addParent) {
        if (StringUtils.isNotBlank(orgId)) {
            List<OrgOrganizationDO> sons = getSonOrgsByOrgId(orgId, type, addParent);
            List<String> orgIds = new ArrayList<>();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sons)) {
                orgIds.addAll(sons.stream().map(OrgOrganizationDO::getId).collect(Collectors.toSet()));
            }
            return orgIds;
        }
        return new ArrayList<>();
    }

    private void getSonOrgsByOrgId(String orgId, List<OrgOrganizationDO> list) {
        if (StringUtils.isNotBlank(orgId)) {
            List<OrgOrganizationDO> listT = list(new QueryWrapper<OrgOrganizationDO>().eq("parent_id", orgId).eq
                    ("is_delete", 0));
            list.addAll(listT);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(listT)) {
                for (int i = 0; i < listT.size(); i++) {
                    getSonOrgsByOrgId(listT.get(i).getId(), list);
                }
            }
        }
    }

    /**
     * 修改组织
     *
     * @param org
     * @return
     * @author yansiyang
     * @date 2019/4/16 8:16
     */
    @Override
    public boolean updateOrg(OrgOrganizationDO org) {
        /** 验证相关数据  并更新子节点排序路径
         * *******************************************************************************
         */
        String parentId = org.getParentId();
        String sortPath = "";
        String result = aceSqlUtils.verifyParentIdWithUniqueForOrg("org_organization", parentId, org.getId(), org
                .getSortIndex(), "group_id", org.getGroupId(), org.getBusinessUnit());
        if (Objects.equals("rootParent", result)) {
            // 父节点为根节点
            sortPath = SortPathUtils.getSortPath("", org.getSortIndex());
            org.setSortPath(sortPath);
        } else if (Objects.equals("nullSortPath", result)) {
            // 父节点发生改变 或 sortIndex 发生变化
            OrgOrganizationDO parent = this.getById(parentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), org.getSortIndex());
            org.setSortPath(sortPath);
        } else if (Objects.equals("nullParentId", result)) {
            //父节点ID为空
            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        }
        //index及父节点未发生变化 则不需要变更子节点
        if (!Objects.equals("noChange", result)) {
            OrgOrganizationDO oldOrg = getById(org.getId());
            aceSqlUtils.updateSonSortPath("org_organization", sortPath, oldOrg.getSortPath().length(), oldOrg
                    .getSortPath());

        }
        /**
         * ************************************************************************************
         */
        org.setUpdateTime(LocalDateTime.now());
        // 更新不需要version_id 部门更新会将此属性带过来  所以置空
        org.setVersionId(null);
        if (this.updateById(org)) {
            /**
             * 更新历史版本
             */
            OrgOrganizationVDO orgV = JsonUtils.castObjectForSetIdNull(org, OrgOrganizationVDO.class);
            if (!orgOrganizationVService.update(orgV, new UpdateWrapper<OrgOrganizationVDO>()
                    .eq("organization_id", org.getId()).eq("is_last_version", 1))) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            orgV = orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>().select("id").eq
                    ("organization_id", org.getId()).eq("is_last_version", 1));

            List<String> types = org.getOrgType();
            OrgOrganizationTypeDO typeDo = new OrgOrganizationTypeDO();
            typeDo.setCorporation(0);
            typeDo.setProject(0);
            typeDo.setAsset(0);
            typeDo.setSales(0);
            typeDo.setStock(0);
            typeDo.setPurchase(0);
            typeDo.setQc(0);
            typeDo.setTraffic(0);
            typeDo.setMaintain(0);
            typeDo.setHr(0);
            typeDo.setAdministration(0);
            typeDo.setFactory(0);
            typeDo.setIsGroup(0);
            typeDo.setFinance(0);
            typeDo.setDepartment(0);
            if (types != null && types.size() > 0) {
                types.forEach(type -> {
                    setTypeAndService(typeDo, type);
                    /**
                     * 更新各类业务单元表
                     * Group Corperation Department 不在此更新
                     */
//            if (!baseOrgService.updateOrg(org)) {
//                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
//            }
                });
                typeDo.setId(org.getId());
                if (!orgOrganizationTypeService.updateById(typeDo)) {
                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                }

                /**
                 * 更新组织固化V类型
                 */
                OrgOrganizationVTypeDO typeV = new OrgOrganizationVTypeDO();
                types.forEach(orgType -> {
                    setType(typeV, orgType);
                });
                typeV.setId(orgV.getId());
                if (!orgOrganizationVTypeService.updateById(typeV)) {
                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                }
            }

            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新业务单元","更新业务单元："+ org.getName(), org
                    .getGroupId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return true;
        }

        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 获取业务单元类型
     *
     * @param orgId 业务单元ID
     * @return
     * @author yansiyang
     * @date 2019/4/18 11:13
     */
    public List<String> getOrgType(String orgId) {
        List<String> types;
        OrgOrganizationTypeDO typeDO = orgOrganizationTypeService.getById(orgId);
        if (typeDO == null) {
            return new ArrayList<>();
        }
        JSONObject json = JsonUtils.castObject(typeDO, JSONObject.class);
        types = json.keySet().stream().filter(key -> !Objects.equals("id", key) && json.getInteger(key)
                == 1)
                .collect
                        (Collectors.toList());
        if (types.size() == 0) {
            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        }
        if (types.contains("isGroup")) {
            types.remove("isGroup");
            types.add("group");
        }
        return types;
    }

    /**
     * 设置组织的Type 并且确认
     *
     * @param type
     * @return
     * @author yansiyang
     * @date 2019/4/15 19:59
     */
    private OrgOrganizationTypeDO setTypeAndService(OrgOrganizationTypeDO type, String orgType) {
        switch (orgType) {
            case "group":
                type.setIsGroup(1);
                baseOrgService = orgGroupService;
                break;
            case "project":
                type.setProject(1);
                break;
            case "asset":
                type.setAsset(1);
                break;
            case "sales":
                type.setSales(1);
                break;
            case "stock":
                type.setStock(1);
                break;
            case "qc":
                type.setQc(1);
                break;
            case "purchase":
                type.setPurchase(1);
                break;
            case "corporation":
                type.setCorporation(1);
                baseOrgService = orgCorporationService;
                break;
            case "traffic":
                type.setTraffic(1);
                break;
            case "maintain":
                type.setMaintain(1);
                baseOrgService = orgMaintainOrgService;
                break;
            case "hr":
                type.setHr(1);
                break;
            case "administration":
                type.setAdministration(1);
                break;
            case "factory":
                type.setFactory(1);
                break;
            case "finance":
                type.setFinance(1);
                break;
            case "department":
                type.setDepartment(1);
                baseOrgService = orgDepartmentService;
                break;
        }
        return type;
    }

    /**
     * 设置组织V的TypeV
     *
     * @param type
     * @return
     * @author yansiyang
     * @date 2019/4/15 20:00
     */
    private OrgOrganizationVTypeDO setType(OrgOrganizationVTypeDO type, String orgType) {
        switch (orgType) {
            case "group":
                type.setIsGroup(1);
                break;
            case "project":
                type.setProject(1);
                break;
            case "asset":
                type.setAsset(1);
                break;
            case "sales":
                type.setSales(1);
                break;
            case "stock":
                type.setStock(1);
                break;
            case "qc":
                type.setQc(1);
                break;
            case "purchase":
                type.setPurchase(1);
                break;
            case "corporation":
                type.setCorporation(1);
                break;
            case "traffic":
                type.setTraffic(1);
                break;
            case "maintain":
                type.setMaintain(1);
                break;
            case "hr":
                type.setHr(1);
                break;
            case "administration":
                type.setAdministration(1);
                break;
            case "factory":
                type.setFactory(1);
                break;
            case "finance":
                type.setFinance(1);
                break;
            case "department":
                type.setDepartment(1);
                break;
        }
        return type;
    }

}
