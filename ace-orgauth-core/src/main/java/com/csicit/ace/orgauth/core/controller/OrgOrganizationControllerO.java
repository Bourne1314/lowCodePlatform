package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.orgauth.core.service.BdPersonDocService;
import com.csicit.ace.orgauth.core.service.BdPersonJobService;
import com.csicit.ace.orgauth.core.service.OrgOrganizationService;
import com.csicit.ace.orgauth.core.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/22 10:11
 */
@RestController
@RequestMapping("/orgauth/organizations")
public class OrgOrganizationControllerO {

    @Resource(name = "orgOrganizationServiceO")
    OrgOrganizationService orgOrganizationService;

    @Resource(name = "sysUserServiceO")
    SysUserService sysUserService;

    @Resource(name = "bdPersonJobServiceO")
    BdPersonJobService bdPersonJobService;

    /**
     * 获取指定用户的所属业务单元，及其绑定的人员所属的业务单元
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    @RequestMapping(value = "/action/getOrgsWithUserAndPersonByUserId/{userId}", method =
            RequestMethod.GET)
    List<OrgOrganizationDO> getOrgsWithUserAndPersonByUserId(@PathVariable("userId") String userId) {
        SysUserDO userDO = sysUserService.getById(userId);
        if (Objects.isNull(userDO)) {
            return new ArrayList<>();
        }
        if (StringUtils.isBlank(userDO.getOrganizationId())) {
            return new ArrayList<>();
        }
        List<String> orgIds = new ArrayList<>();
        orgIds.add(userDO.getOrganizationId());
        if (StringUtils.isNotBlank(userDO.getPersonDocId())) {
            List<BdPersonJobDO> jobDOS = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
            .eq("person_doc_id", userDO.getPersonDocId()));
            if (CollectionUtils.isNotEmpty(jobDOS)) {
                orgIds.addAll(jobDOS.stream().map(BdPersonJobDO::getOrganizationId).collect(Collectors.toList()));
            }
        }
        return orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().in("id", orgIds));
    }


    /**
     * 获取指定用户的所属业务单元
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    @RequestMapping(value = "/action/getOrgByUser/{userId}", method =
            RequestMethod.GET)
    OrgOrganizationDO getOrgByUser(@PathVariable("userId") String userId) {
        SysUserDO userDO = sysUserService.getById(userId);
        if (Objects.isNull(userDO)) {
            return new OrgOrganizationDO();
        }
        if (StringUtils.isBlank(userDO.getOrganizationId())) {
            return new OrgOrganizationDO();
        }
        return orgOrganizationService.getById(userDO.getOrganizationId());

    }

    /**
     * 根据业务单元名称模糊查询业务单元列表
     *
     * @param orgName
     * @return
     * @author FourLeaves
     * @date 2019/12/20 17:25
     */
    @RequestMapping(value = "/action/getOrganizationsAndDepsByOrgName", method = RequestMethod.GET)
    List<OrgOrganizationDO> getOrganizationsAndDepsByOrgName(@RequestParam("orgName") String orgName) {
        return orgOrganizationService.getOrganizationsAndDepsByOrgName(orgName);
    }

    /**
     * 根据父节点ID获取 仅第一层无递归  type=1 子业务单元 type=2 子业务单元和部门 type=3 部门
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/action/getSonOrganizationByParentId/{parentId}/{type}", method =
            RequestMethod
                    .GET)
    List<OrgOrganizationDO> getSonOrganizationByParentId(@PathVariable("parentId") String parentId, @PathVariable
            ("type") Integer type) {
        return orgOrganizationService.getSonOrganizationByParentId(parentId, type);
    }

    /**
     * 根据ID获取所属业务单元
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/organizations/action/getOrganizationByID/{organizationId}", method = RequestMethod.GET)
    OrgOrganizationDO getOrganizationByID(@PathVariable("organizationId") String organizationId) {
        return orgOrganizationService.getOrganizationByID(organizationId);
    }

    /**
     * 获取当前用户所属业务单元
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/action/getCurrentOrganization/{organizationId}", method = RequestMethod.GET)
    OrgOrganizationDO getCurrentOrganization(@PathVariable("organizationId") String organizationId) {
        return orgOrganizationService.getCurrentOrganization(organizationId);
    }

    /**
     * 获取当前用户所属集团的业务单元列表 及 部门列表
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/action/getCurrentOrganizationsAndDeps/{groupId}", method = RequestMethod.GET)
    List<OrgOrganizationDO> getCurrentOrganizationsAndDeps(@PathVariable("groupId") String groupId) {
        return orgOrganizationService.getCurrentOrganizationsAndDeps(groupId);
    }

    /**
     * 获取当前用户所属集团的业务单元列表
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/action/getCurrentOrganizations/{groupId}", method = RequestMethod.GET)
    List<OrgOrganizationDO> getCurrentOrganizations(@PathVariable("groupId") String groupId) {
        return orgOrganizationService.getCurrentOrganizations(groupId);
    }

    /**
     * 获取当前用户的实体部门
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/action/getEntityDeptOfCurrentUser", method = RequestMethod.GET)
    OrgOrganizationDO getEntityDeptOfCurrentUser() {
        return orgOrganizationService.getEntityDeptOfCurrentUser();
    }

    /**
     * 获取指定用户的实体部门
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/action/getEntityDeptByUserId/{userId}", method = RequestMethod.GET)
    OrgOrganizationDO getEntityDeptByUserId(@PathVariable("userId") String userId) {
        return orgOrganizationService.getEntityDeptByUserId(userId);
    }
}
