package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.OrgGroupMapper;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 集团管理 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Service("orgGroupService")
public class OrgGroupServiceImpl extends BaseServiceImpl<OrgGroupMapper, OrgGroupDO> implements OrgGroupService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    BdPersonIdTypeService bdPersonIdTypeService;

    @Autowired
    BdPersonDocService personDocService;

    @Autowired
    PersonIdTypeConfig personIdTypeConfig;

    @Autowired
    SysUserRoleService sysUserRoleService;

    /**
     * 是否开启 自动创建三元
     */
    @Value("${ace.config.autoCreateThree:false}")
    private boolean autoCreateThree;

//    @Override
//    public boolean removeGroups(List<String> ids) {
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
//            if (update(new OrgGroupDO(), new UpdateWrapper<OrgGroupDO>().in("id", ids)
//                    .setSql("code=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),code)")
//                    .setSql("name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),name)")
//                    .set("is_delete", 1))) {
//                // 删除业务单元
//                List<OrgOrganizationDO> orgOrganizationDOS = orgOrganizationService.list(new
//                        QueryWrapper<OrgOrganizationDO>().in("group_id", ids).select("id"));
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgOrganizationDOS)) {
//                    List<String> orgIds = orgOrganizationDOS.stream().map(OrgOrganizationDO::getId).collect
// (Collectors.toList());
//                    return orgOrganizationService.removeOrgs(orgIds);
//                }
//            }
//        }
//        return false;
//    }


    @Override
    public boolean mvGroup(boolean mvToTop, String id, String targetGroupId) {
        if (StringUtils.isBlank(id)) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        OrgGroupDO group = getById(id);
        if (group == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        // 是否设置为顶级业务单元
        Integer sortIndex = group.getSortIndex();
        String newSortPath = "";
        String parentId = "0";
        if (mvToTop) {
            if (Objects.equals(group.getParentId(), "0")) {
                throw new RException(InternationUtils.getInternationalMsg("MV_GROUP_IS_TOP"));
            }
            List<OrgGroupDO> sonGroups = list(new QueryWrapper<OrgGroupDO>().eq("parent_id", "0").eq("is_delete", 0));
            List<Integer> sortIndexs = sonGroups.stream().map(OrgGroupDO::getSortIndex).collect(Collectors
                    .toList());
            while (sortIndexs.contains(sortIndex)) {
                sortIndex++;
            }
            newSortPath = SortPathUtils.getSortPath("", sortIndex);
        } else {
            if (StringUtils.isBlank(targetGroupId)) {
                throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            if (Objects.equals(id, targetGroupId)) {
                throw new RException(InternationUtils.getInternationalMsg("MV_GROUP_TO_SON"));
            }
            OrgGroupDO targetGroup = getById(targetGroupId);
            if (targetGroup == null) {
                throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            // 目标集团是否为此集团子节点
            // 判断依据
            //  目标节点排序路径包含原节点排序路径
            if (targetGroup.getSortPath().startsWith(group.getSortPath())) {
                throw new RException(InternationUtils.getInternationalMsg("MV_GROUP_TO_SON"));
            }
            List<OrgGroupDO> sonGroups = list(new QueryWrapper<OrgGroupDO>().eq("parent_id", targetGroupId).eq
                    ("is_delete", 0));
            List<Integer> sortIndexs = sonGroups.stream().map(OrgGroupDO::getSortIndex).collect(Collectors
                    .toList());
            while (sortIndexs.contains(sortIndex)) {
                sortIndex++;
            }
            parentId = targetGroupId;
            newSortPath = SortPathUtils.getSortPath(targetGroup.getSortPath(), sortIndex);
        }

        // 修改子部门
        List<OrgGroupDO> list = list(new QueryWrapper<OrgGroupDO>().ne("id", id).eq("is_delete", 0)
                .likeRight("sort_path", group.getSortPath()));
        List<OrgGroupDO> listT = new ArrayList<>();
        String oldSortPath = group.getSortPath();
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                OrgGroupDO groupT = list.get(i);
                String sortPath = groupT.getSortPath().substring(oldSortPath.length());
                groupT.setSortPath(newSortPath + sortPath);
                listT.add(groupT);
            }
        }
        // 保存
        group.setSortIndex(sortIndex);
        group.setParentId(parentId);
        group.setSortPath(newSortPath);
        listT.add(group);
        if (!updateBatchById(listT)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        //修改业务单元
        List<OrgOrganizationDO> orgs = orgOrganizationService.getSonOrgsByOrgId(id, 5, false);
        OrgOrganizationDO oldParentOrg = orgOrganizationService.getById(id);
        List<OrgOrganizationDO> orgsT = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orgs)) {
            for (int i = 0; i < orgs.size(); i++) {
                OrgOrganizationDO groupT = orgs.get(i);
                String sortPath = groupT.getSortPath().substring(oldSortPath.length());
                groupT.setSortPath(newSortPath + sortPath);
                orgsT.add(groupT);
            }
        }
        oldParentOrg.setSortIndex(sortIndex);
        oldParentOrg.setParentId(parentId);
        oldParentOrg.setSortPath(newSortPath);
        orgsT.add(oldParentOrg);
        if (!orgOrganizationService.updateBatchById(orgsT)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        return true;
    }

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
            List<String> orgIds = adminOrgs.stream().map(SysUserAdminOrgDO::getOrganizationId).collect(Collectors
                    .toList());
            List<OrgGroupDO> listT = list(new QueryWrapper<OrgGroupDO>().eq("is_delete", 0).orderByAsc("sort_path")
                    .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                    .in("id", orgIds));
            if (listT != null && listT.size() > 0) {
                list.addAll(listT);
            }
        }
        return list;
    }

    /**
     * 修改集团以及业务单元表
     *
     * @param group
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:59
     */
    @Override
    public boolean updateGroup(OrgGroupDO group) {

        /** 验证相关数据  并更新子节点排序路径
         * *******************************************************************************
         */
        String parentId = group.getParentId();
        String sortPath = "";
        String result = aceSqlUtils.verifyParentId("org_group", parentId, group.getId(), group.getSortIndex());
        if (Objects.equals("rootParent", result)) {
            // 父节点为根节点
            sortPath = SortPathUtils.getSortPath("", group.getSortIndex());
            group.setSortPath(sortPath);
        } else if (Objects.equals("nullSortPath", result)) {
            // 父节点发生改变 或 sortIndex 发生变化
            OrgGroupDO parentGroup = this.getById(parentId);
            if (parentGroup == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parentGroup
                    .getSortPath(), group.getSortIndex());
            group.setSortPath(sortPath);
        } else if (Objects.equals("nullParentId", result)) {
            //父节点ID为空
            return false;
        }
        OrgGroupDO oldGroup = this.getById(group.getId());
        group.setUpdateTime(LocalDateTime.now());
        if (!this.updateById(group)) {
            return false;
        }
        if (!Objects.equals("noChange", result)) {
            aceSqlUtils.updateSonSortPath("org_group", sortPath, oldGroup.getSortPath().length(), oldGroup.getSortPath());
        }
        /**
         * ************************************************************************************
         */


        /**
         * 修改业务单元表内容
         */
        OrgOrganizationDO org = JsonUtils.castObject(group, OrgOrganizationDO.class);
        org.setBusinessUnit(0);
        org.setGroupId(group.getId());
        if (!orgOrganizationService.updateOrg(org)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_UPDATE_ORGANIZATION"));
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改集团", "修改集团："+group.getName())) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_UPDATE_ORGANIZATION"));
        }
        return true;
    }

    @Override
    public R saveGroupR(OrgGroupDO group) {
        String parentId = group.getParentId();
        String rootId = "0";
        String sortPath;
        if (StringUtils.isBlank(parentId) || group.getSortIndex() == null) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        } else if (Objects.equals(rootId, parentId)) {
            sortPath = SortPathUtils.getSortPath("", group.getSortIndex());
        } else {
            OrgGroupDO parentGroup = this.getById(parentId);
            if (parentGroup == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parentGroup
                    .getSortPath(), group.getSortIndex());
        }
        aceSqlUtils.validateTreeTable("org_group", parentId, group.getSortIndex(), sortPath);
        group.setSortPath(sortPath);
        group.setCreateTime(LocalDateTime.now());
        group.setUpdateTime(LocalDateTime.now());
        group.setCreateUser(securityUtils.getCurrentUserId());
        // 校验编码
        String code = group.getCode();
        int count = orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0).eq("code", code));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("SAME_CODE_EXIST"));
        }
        if (!this.save(group)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        /**
         * 插入业务单元表
         */
        OrgOrganizationDO org = JsonUtils.castObject(group, OrgOrganizationDO.class);
        org.setBusinessUnit(0);
        org.setGroupId(group.getId());
        List<String> types = new ArrayList<>();
        types.add("group");
        org.setOrgType(types);
        if (!orgOrganizationService.saveOrg(org)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_ORGANIZATION"));
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存集团", "保存集团："+group.getName())) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_ORGANIZATION"));
        }

        List<BdPersonIdTypeDO> personIdTypeDOS = personIdTypeConfig.getPersonIdTypes();
        if (CollectionUtils.isNotEmpty(personIdTypeDOS)) {
            String groupId = org.getId();
            personIdTypeDOS.forEach(personIdType -> {
                personIdType.setId(personIdType.getCode() + groupId.substring(personIdType.getCode().length()));
                personIdType.setGroupId(groupId);
                personIdType.setCreateTime(LocalDateTime.now());
                personIdType.setCreateUser(securityUtils.getCurrentUserId());
                personIdType.setUpdateTime(LocalDateTime.now());
            });
            if (!bdPersonIdTypeService.saveBatch(personIdTypeDOS)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存证件类型","保存证件类型："+ personIdTypeDOS
                    .stream().map
                            (BdPersonIdTypeDO::getName).collect(Collectors.toList()), group.getId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
        }

        if (autoCreateThree) {
            List<SysUserDO> userDOList = new ArrayList<>();

            SysUserDO groupAdmin = new SysUserDO();
            groupAdmin.setId(UuidUtils.createUUID());
            groupAdmin.setUserName(group.getCode() + "Admin");
            groupAdmin.setUserType(111);
            groupAdmin.setRealName(group.getName() + "管理员");
            groupAdmin.setSecretLevel(5);
            userDOList.add(groupAdmin);

            SysUserDO groupSec = new SysUserDO();
            groupSec.setId(UuidUtils.createUUID());
            groupSec.setUserName(group.getCode() + "Sec");
            groupSec.setUserType(111);
            groupSec.setRealName(group.getName() + "保密员");
            groupSec.setSecretLevel(5);
            userDOList.add(groupSec);

            SysUserDO groupAuditor = new SysUserDO();
            groupAuditor.setId(UuidUtils.createUUID());
            groupAuditor.setUserName(group.getCode() + "Auditor");
            groupAuditor.setUserType(111);
            groupAuditor.setRealName(group.getName() + "审计员");
            groupAuditor.setSecretLevel(5);
            userDOList.add(groupAuditor);

            List<OrgGroupDO> groupDOS = new ArrayList<>();
            groupDOS.add(group);

            userDOList.stream().forEach(sysUserDO -> {
                sysUserService.saveUser(sysUserDO);
                sysUserDO.setGroups(groupDOS);
                if (sysUserDO.getUserName().endsWith("Admin")) {
                    sysUserDO.setRoleType(11);
                } else if (sysUserDO.getUserName().endsWith("Sec")) {
                    sysUserDO.setRoleType(22);
                } else {
                    sysUserDO.setRoleType(33);
                }
                if (!sysUserService.saveUserGroupControlDomain(sysUserDO)) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
                Map<String, String> map = new HashMap<>();
                map.put("id", sysUserDO.getId());
                map.put("roleType", sysUserDO.getRoleType() + "");
                if (!sysUserRoleService.setGroupActivated(map)) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
                if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增集团三员账号", "集团名称：" + group.getName() + "，三员名称：" + sysUserDO
                        .getRealName())) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            });
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("admin",groupAdmin.getUserName())
                    .put("sec",groupSec.getUserName())
                    .put("auditor",groupAuditor.getUserName());
        }

        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")) .put("show", 0);
    }

    /**
     * 保存集团以及对应的业务单元表
     *
     * @param group
     * @return boolean 保存成功 true
     * @author yansiyang
     * @date 2019/4/12 14:54
     */
    @Override
    public boolean saveGroup(OrgGroupDO group) {
        return Objects.equals(saveGroupR(group).get("code"), "40000");
    }

    /**
     * 版本化业务单元
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:05
     */
    @Override
    public boolean versionOrg(Map<String, String> map) {
        return true;
    }

    /**
     * 删除集团及从表内容
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:56
     */
    @Override
    public R deleteGroup(Map<String, Object> params) {
        List<String> ids = (ArrayList) params.get("ids");
        if (CollectionUtils.isNotEmpty(ids)) {
            List<OrgGroupDO> list = list(new QueryWrapper<OrgGroupDO>().eq("is_delete", 0).in("id", ids)
                    .select("id", "name", "sort_path"));
            // 存在子集团 不可以删除
            if (count(new QueryWrapper<OrgGroupDO>().in("parent_id", ids).eq("is_delete", 0)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "存在子集团"));
            }

            // 存在业务单元 不可以删除
            if (orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>().in("parent_id", ids).eq
                    ("is_delete", 0))
                    > 0) {
                throw new RException(InternationUtils.getInternationalMsg("PARAM_CAN_NOT_BE_DELETED", "存在业务单元"));
            }


//            boolean result = false;
            // 保存集团 及其子集团 主键
//            Set<String> idSet = new HashSet<>();
//            idSet.addAll(ids);


//            // 通过sort_path删除子节点
//            for (int i = 0; i < list.size(); i++) {
//                OrgGroupDO group = list.get(i);
//                String sortPath = group.getSortPath();
//                if (StringUtils.isNotBlank(sortPath)) {
//                    List<OrgGroupDO> sonGroups = list(new QueryWrapper<OrgGroupDO>().eq("is_delete", 0).likeRight
//                            ("sort_path", sortPath));
//                    List<String> sonIds = sonGroups.stream().map(OrgGroupDO::getId).collect(Collectors.toList());
//                    idSet.addAll(sonIds);
//                }
//            }


            if (update(new OrgGroupDO(), new UpdateWrapper<OrgGroupDO>().setSql("code=CONCAT(CONCAT('del-', SUBSTR" +
                    "(id,1,2)),code)")
                    .setSql("name=CONCAT(CONCAT('del-', SUBSTR(id,1,2)),name)").setSql("parent_id=CONCAT" +
                            "(CONCAT" +
                            "('del-', SUBSTR(id,1,2)),parent_id)").set("is_delete", 1)
                    .in("id", ids))) {
                // 删除业务单元里面的集团记录
//                ids = new ArrayList<>();
//                ids.addAll(idSet);
                if (!orgOrganizationService.removeOrgs(ids)) {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }


                //                // 删除集团下的业务单元
//                List<OrgOrganizationDO> orgOrganizationDOS = orgOrganizationService.list(new
//                        QueryWrapper<OrgOrganizationDO>().select("id").eq("is_delete", 0).eq("is_business_unit", 1).in
//                        ("group_id", idSet));
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orgOrganizationDOS)) {
//                    List<String> orgIds = orgOrganizationDOS.stream().map(OrgOrganizationDO::getId).collect
//                            (Collectors.toList());
//                    params.put("ids", orgIds);
//                    params.put("deleteSons", true);
//                    orgOrganizationService.deleteOrg(params);
//                }


                if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除集团","删除集团："+ list
                        .parallelStream
                                ().map
                                (OrgGroupDO::getName)
                        .collect(Collectors.toList()))) {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }
                return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
            } else {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "集团主键"));
    }

    /**
     * 保存业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:05
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
     * @date 2019/4/22 15:05
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
     * @date 2019/4/22 15:05
     */
    @Override
    public boolean deleteOrg(Map<String, Object> map) {
        return true;
    }

    /**
     * 获取集团列表（普通用户所在集团对应的最顶级集团的整个集团树）
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:59
     */
    @Override
    public List<OrgGroupDO> listForOrdinaryUser(String groupId) {
        List<OrgGroupDO> orgGroupDOS = new ArrayList<>(16);
        String parentGroupId = this.getParentGroupId(groupId);
        List<OrgGroupDO> list = list(new QueryWrapper<OrgGroupDO>()
                .eq("is_delete", 0).orderByAsc("sort_path"));
        List<OrgGroupDO> listT = TreeUtils.makeTree(list, OrgGroupDO.class);
        for (OrgGroupDO orgGroupDO : listT) {
            if (Objects.equals(parentGroupId, orgGroupDO.getId())) {
                orgGroupDOS.add(orgGroupDO);
                break;
            }
        }
        return orgGroupDOS;
    }

    private String getParentGroupId(String groupId) {
        OrgGroupDO orgGroupDO = getById(groupId);
        if (!Objects.equals("0", orgGroupDO.getParentId())) {
            return this.getParentGroupId(orgGroupDO.getParentId());
        } else {
            return groupId;
        }
    }
}
