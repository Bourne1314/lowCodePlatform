package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgCorporationDO;
import com.csicit.ace.common.pojo.domain.OrgCorporationVDO;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.OrgCorporationMapper;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgCorporationService;
import com.csicit.ace.platform.core.service.OrgCorporationVService;
import com.csicit.ace.platform.core.service.OrgGroupService;
import com.csicit.ace.platform.core.service.OrgOrganizationService;
import com.csicit.ace.platform.core.utils.JsonUtils;
import com.csicit.ace.platform.core.utils.UuidUtil;
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
 * 组织-公司组织 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-16 15:31:10
 */
@Service("orgCorporationService")
public class OrgCorporationServiceImpl extends BaseServiceImpl<OrgCorporationMapper, OrgCorporationDO> implements
        OrgCorporationService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    OrgCorporationVService orgCorporationVService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    OrgGroupService orgGroupService;

    @Override
    public R deleteCorp(Map<String, Object> params) {
        // 是否删除子节点
        boolean deleteSons = (boolean) params.get("deleteSons");
        List<String> ids = (ArrayList) params.get("ids");
        List<OrgCorporationDO> list = list(new QueryWrapper<OrgCorporationDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                .in("id", ids)
                .select("id", "name", "sort_path", "group_id", "ORGANIZATION_ID"));
        boolean result = false;
        if (!deleteSons) {
            int count = count(new QueryWrapper<OrgCorporationDO>()
                    .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                    .in("parent_id", ids));
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("DELETE_ERROR_FOR_EXIST_SON"));
            } else {
                removeByIds(ids);
            }
        } else {
            // 通过sort_path删除子节点
            for (int i = 0; i < list.size(); i++) {
                OrgCorporationDO corp = list.get(i);
                String sortPath = corp.getSortPath();
                if (StringUtils.isNotBlank(sortPath)) {
                    remove(new QueryWrapper<OrgCorporationDO>().eq("group_id", corp.getGroupId()).eq("ORGANIZATION_ID", corp.getOrganizationId()).likeRight("sort_path", sortPath));
                }
            }
        }
        // 判断删除是否成功
        int count = count(new QueryWrapper<OrgCorporationDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                .in("id", ids));
        if (count == 0) {
            result = true;
        }

        if (result) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除法人组织", "删除法人组织："+list
                    .parallelStream().map
                            (OrgCorporationDO::getName)
                    .collect(Collectors.toList()), list.get(0).getGroupId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 保存公司
     *
     * @param corp
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 14:58
     */
    @Override
    public boolean saveCorp(OrgCorporationDO corp) {
        String rootId = "0";
        String parentId = corp.getParentId();
        String sortPath;
        if (StringUtils.isBlank(parentId) || corp.getSortIndex() == null) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        } else if (Objects.equals(rootId, parentId)) {
            sortPath = SortPathUtils.getSortPath("", corp.getSortIndex());
        } else {
            OrgCorporationDO parent = this.getById(parentId);
            if (parent == null) {
                // 上级节点是集团
                OrgGroupDO group = orgGroupService.getById(parentId);
                if (group != null) {
                    corp.setParentId("0");
                    sortPath = SortPathUtils.getSortPath("", corp.getSortIndex());
                    corp.setOrganizationId(corp.getParentId());
                } else {
                    OrgOrganizationDO org = orgOrganizationService.getById(parentId);
                    if (org != null) {
                        corp.setParentId("0");
                        sortPath = SortPathUtils.getSortPath("", corp.getSortIndex());
                        corp.setOrganizationId(corp.getParentId());
                    } else {
                        throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
                    }
                }
            } else {
                sortPath = SortPathUtils.getSortPath(parent
                        .getSortPath(), corp.getSortIndex());
            }

        }
        aceSqlUtils.validateTreeTable("org_corporation", parentId, corp.getSortIndex(), sortPath);
        corp.setSortPath(sortPath);
        corp.setCreateTime(LocalDateTime.now());
        corp.setCreateUser(securityUtils.getCurrentUserId());
        corp.setUpdateTime(LocalDateTime.now());
        corp.setId(UuidUtil.createUUID());
        /**
         * 保留固化版本
         */
        OrgCorporationVDO corpV = JsonUtils.castObjectForSetIdNull(corp, OrgCorporationVDO.class);
        corpV.setCorporationId(corp.getId());
        corpV.setLastVersion(1);
        corpV.setVersionBeginDate(LocalDate.now());
        corpV.setVersionBeginUserId(corp.getCreateUser());
        //corpV.setOrganizationId(corp.getId());
        if (!orgCorporationVService.save(corpV)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_VERSION_POJO"));
        }

        corp.setVersionId(corpV.getId());
        if (!this.save(corp)) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        /**
         * 插入业务单元表
         */
//        OrgOrganizationDO org = JsonUtils.castObject(corp, OrgOrganizationDO.class);
//        org.setOrgType("corporation");
//        if (!orgOrganizationService.saveOrg(org)) {
//            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_ORGANIZATION"));
//        }

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存法人组织", "保存法人组织："+corp.getName(), corp.getGroupId(), null)) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }

        return true;
    }

    /**
     * 修改部门
     *
     * @param corp
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 14:59
     */
    @Override
    public boolean updateCorp(OrgCorporationDO corp) {
        /** 验证相关数据  并更新子节点排序路径
         * *******************************************************************************
         */
        String sortPath = "";
        String parentId = corp.getParentId();
        String result = aceSqlUtils.verifyParentId("org_corporation", parentId, corp.getId(), corp.getSortIndex());
        if (Objects.equals("rootParent", result)) {
            // 父节点为根节点
            corp.setSortPath(SortPathUtils.getSortPath("", corp.getSortIndex()));
        } else if (Objects.equals("nullSortPath", result)) {
            // 父节点发生改变 或 sortIndex 发生变化
            OrgCorporationDO parent = this.getById(parentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            sortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), corp.getSortIndex());
            corp.setSortPath(sortPath);
        } else if (Objects.equals("nullParentId", result)) {
            //父节点ID为空
            return false;
        }
        corp.setUpdateTime(LocalDateTime.now());
        OrgCorporationDO oldCorp = getById(corp.getId());
        if (!this.updateById(corp)) {
            return false;
        }
        //index及父节点未发生变化 则不需要变更子节点
        if (!Objects.equals("noChange", result)) {
            aceSqlUtils.updateSonSortPathWithUnique("org_corporation", corp.getSortPath(), oldCorp.getSortPath().length(), oldCorp
                    .getSortPath(), "group_id", corp.getGroupId());
        }
        /**
         * ************************************************************************************
         */

        /**
         * 保留固化版本
         */
        OrgCorporationVDO corpV = JsonUtils.castObject(corp, OrgCorporationVDO.class);
        if (!orgCorporationVService.update(corpV, new UpdateWrapper<OrgCorporationVDO>()
                .eq("corporation_id", corp.getId()).eq("is_last_version", 1))) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_VERSION_POJO"));
        }
        /**
         * 插入业务单元表
         */
        OrgOrganizationDO org = JsonUtils.castObject(corp, OrgOrganizationDO.class);
        if (!orgOrganizationService.updateOrg(org)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_SAVE_ORGANIZATION"));
        }

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新法人组织", "更新法人组织："+corp.getName(), corp.getGroupId(), null)) {
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
     * @date 2019/4/22 14:59
     */
    @Override
    public boolean versionOrg(Map<String, String> map) {
        String versionName = map.get("versionName");
        String versionNo = map.get("versionNo");
        String corporationId = map.get("organizationId");
        OrgCorporationVDO corporationV = orgCorporationVService.getOne(new QueryWrapper<OrgCorporationVDO>().eq
                ("corporation_id", corporationId).eq("is_last_version", 1));
        if (corporationV == null) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        OrgCorporationVDO corporationVNew = corporationV;
        /**
         * 固化之前的数据
         */
        OrgCorporationVDO tempCorpV = new OrgCorporationVDO();
        tempCorpV.setVersionEndUserId(securityUtils.getCurrentUserId());
        tempCorpV.setVersionEndDate(LocalDate.now());
        tempCorpV.setVersionName(versionName);
        tempCorpV.setVersionNo(versionNo);
        tempCorpV.setLastVersion(0);
        tempCorpV.setId(corporationV.getId());
        if (!orgCorporationVService.updateById(tempCorpV)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        /**
         * 创建新的版本
         */
        String versionId = UuidUtil.createUUID();
        corporationVNew.setId(versionId);
        corporationVNew.setVersionBeginUserId(tempCorpV.getVersionEndUserId());
        corporationVNew.setVersionBeginDate(LocalDate.now());
        if (!orgCorporationVService.save(corporationVNew)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        /**
         * 更新VersionID
         */
        if (!this.update(new OrgCorporationDO(), new UpdateWrapper<OrgCorporationDO>()
                .eq("id", corporationId).set("version_id", versionId))) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }

        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "版本化法人组织", "版本化法人组织："+tempCorpV.getName(), tempCorpV.getGroupId(),
                null)) {
            throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        return true;
    }

    /**
     * 保存业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:00
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
     * @date 2019/4/22 15:01
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
     * @date 2019/4/22 15:01
     */
    @Override
    public boolean deleteOrg(Map<String, Object> map) {
        return true;
    }
}
