package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织-组织 实例对象访问接口
 *
 * @author shanwj
 * @version v1.0
 * @date 2019-04-11 10:36:05
 */
@Transactional
public interface OrgOrganizationService extends IBaseService<OrgOrganizationDO> {

    /**
     * 根据业务单元名称模糊查询业务单元列表
     *
     * @param orgName
     * @return
     * @author FourLeaves
     * @date 2019/12/20 17:25
     */
    List<OrgOrganizationDO> getOrganizationsAndDepsByOrgName(String orgName);

    /**
     * 根据父节点ID获取 仅第一层无递归  type=1 子业务单元 type=2 子业务单元和部门 type=3 部门
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgOrganizationDO> getSonOrganizationByParentId(String parentId, Integer type);


    /**
     * 根据ID获取所属业务单元
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgOrganizationDO getOrganizationByID(String organizationId);

    /**
     * 获取当前用户所属业务单元
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgOrganizationDO getCurrentOrganization(String organizationId);

    /**
     * 获取当前用户所属集团的业务单元列表 及 部门列表
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgOrganizationDO> getCurrentOrganizationsAndDeps(String groupId);

    /**
     * 获取当前用户所属集团的业务单元列表
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgOrganizationDO> getCurrentOrganizations(String groupId);

    /**
     * 获取当前用户的实体部门
     *
     * @return
     * @author FourLeaves
     * @date 2020/4/8 8:08
     */
    OrgOrganizationDO getEntityDeptOfCurrentUser();

    /**
     * 获取指定用户的实体部门
     *
     * @param userId 用户主键
     * @return
     * @author FourLeaves
     * @date 2020/4/8 8:08
     */
    OrgOrganizationDO getEntityDeptByUserId(String userId);

}
