package com.csicit.ace.dev.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.domain.dev.ProInfoDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.ProInfoMapper;
import com.csicit.ace.data.persistent.service.OrgGroupServiceD;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.dev.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 应用管理 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019/11/25 11:12
 */
@Service("proInfoService")
public class ProInfoServiceImpl extends BaseServiceImpl<ProInfoMapper, ProInfoDO> implements
        ProInfoService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    OrgGroupServiceD orgGroupServiceD;

    @Autowired
    BdPersonIdTypeServiceD bdPersonIdTypeServiceD;

    @Autowired
    OrgOrganizationServiceD orgOrganizationServiceD;

    @Autowired
    OrgOrganizationVServiceD orgOrganizationVServiceD;

    @Autowired
    OrgOrganizationTypeServiceD orgOrganizationTypeServiceD;

    @Autowired
    OrgOrganizationVTypeServiceD orgOrganizationVTypeServiceD;


    /**
     * 删除项目
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public boolean saveInfo(ProInfoDO instance) {
        instance.setIsDelete(0);
        instance.setCreateUser(securityUtils.getCurrentUserId());
        instance.setCreateTime(LocalDateTime.now());
        // 判断是否存在集团
        OrgGroupDO groupDO1 = orgGroupServiceD.getOne(new QueryWrapper<OrgGroupDO>().eq("name", "软件开发集团"));
        if (groupDO1 != null) {
            instance.setGroupId(groupDO1.getId());
        } else {
            // 插入集团表
            OrgGroupDO orgGroupDO = new OrgGroupDO();
            orgGroupDO.setBeDeleted(0);
            orgGroupDO.setParentId("0");
            orgGroupDO.setCode("devplatform-group");
            orgGroupDO.setName("软件开发集团");
            orgGroupDO.setSortIndex(989898);
            String sortPath = SortPathUtils.getSortPath("", orgGroupDO.getSortIndex());
            orgGroupDO.setSortPath(sortPath);
            aceSqlUtils.validateTreeTable("org_group", "0", orgGroupDO.getSortIndex(), sortPath);
            orgGroupDO.setThreeAdmin(0);
            orgGroupDO.setSecretLevel(5);
            orgGroupDO.setCreateUser(securityUtils.getCurrentUserId());
            orgGroupDO.setCreateTime(LocalDateTime.now());
            orgGroupDO.setUpdateTime(LocalDateTime.now());
            orgGroupDO.setId(UuidUtils.createUUID());
            orgGroupDO.setDataVersion(0);
            if (!orgGroupServiceD.save(orgGroupDO)) {
                return false;
            }

            // 插入业务单元表
            OrgOrganizationDO org = JsonUtils.castObject(orgGroupDO, OrgOrganizationDO.class);
            org.setBusinessUnit(0);
            org.setGroupId(orgGroupDO.getId());
            List<String> types = new ArrayList<>();
            types.add("group");
            org.setOrgType(types);
            org.setCreateTime(LocalDateTime.now());
            org.setCreateUser(securityUtils.getCurrentUserId());
            org.setUpdateTime(LocalDateTime.now());
            org.setId(UuidUtils.createUUID());

            OrgOrganizationVDO orgV = JsonUtils.castObjectForSetIdNull(org, OrgOrganizationVDO.class);
            orgV.setOrganizationId(org.getId());
            orgV.setLastVersion(1);
            orgV.setVersionBeginUserId(org.getCreateUser());
            orgV.setVersionBeginDate(LocalDate.now());
            if (!orgOrganizationVServiceD.save(orgV)) {
                return false;
            }

            org.setVersionId(orgV.getId());
            if (!orgOrganizationServiceD.save(org)) {
                return false;
            }

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
            type.setIsGroup(1);
            type.setFinance(0);
            type.setDepartment(0);
            type.setId(org.getId());
            if (!orgOrganizationTypeServiceD.save(type)) {
               return false;
            }
            /**
             * 保存组织固化V类型
             */
            OrgOrganizationVTypeDO typeV = new OrgOrganizationVTypeDO();
            typeV.setIsGroup(1);
            typeV.setId(orgV.getId());
            if (!orgOrganizationVTypeServiceD.save(typeV)) {
                return false;
            }

            // 项目信息设置集团ID
            instance.setGroupId(orgGroupDO.getId());
        }


        if (!save(instance)) {
            return false;
        }

        return true;
    }

    /**
     * 删除项目
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public boolean deleteByIds(List<String> ids) {
        if (ids.size() == 0) {
            return true;
        }
        List<ProInfoDO> infoDOS = list(new QueryWrapper<ProInfoDO>().in("id", ids));
        infoDOS.stream().forEach(serviceDO -> {
            serviceDO.setIsDelete(1);
        });
        if (!updateBatchById(infoDOS)) {
            return false;
        }
        return true;
    }

    /**
     * 获取角色信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public ProInfoDO getProInfoDO(String id) {
        ProInfoDO instance = getById(id);

        return instance;
    }

}
