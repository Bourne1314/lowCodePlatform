package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.webservice.commonUtils.SqlUtils;
import com.csicit.ace.webservice.mapper.OrgGroupMapper;
import com.csicit.ace.webservice.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 集团管理 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Service("orgGroupService")
public class OrgGroupServiceImpl extends ServiceImpl<OrgGroupMapper, OrgGroupDO> implements OrgGroupService {

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    BdPersonIdTypeService bdPersonIdTypeService;

    @Autowired
    BdPersonDocService personDocService;

    @Autowired
    SqlUtils sqlUtils;

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
        String result = sqlUtils.verifyParentId("org_group", parentId, group.getId(), group.getSortIndex());
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
            sqlUtils.updateSonSortPath("org_group", sortPath, oldGroup.getSortPath().length(), oldGroup.getSortPath());
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
        return true;
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
        sqlUtils.validateTreeTable("org_group", parentId, group.getSortIndex(), sortPath);
        group.setSortPath(sortPath);
        group.setCreateTime(LocalDateTime.now());
        group.setUpdateTime(LocalDateTime.now());
        group.setCreateUser("webservice");
        if (!this.save(group)) {
            return false;
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


        return true;
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


    private String getParentGroupId(String groupId) {
        OrgGroupDO orgGroupDO = getById(groupId);
        if (!Objects.equals("0", orgGroupDO.getParentId())) {
            return this.getParentGroupId(orgGroupDO.getParentId());
        } else {
            return groupId;
        }
    }
}
