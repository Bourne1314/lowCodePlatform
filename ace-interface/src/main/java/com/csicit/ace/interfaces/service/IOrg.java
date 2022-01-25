package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Set;

/**
 * 组织结构
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/20 17:22
 */
public interface IOrg {

    /**
     *  
     * 获取指定用户的所属业务单元主键，及其绑定的人员所属的业务单元主键
     * @param userId
     * @return 
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    List<String> getOrgIdsWithUserAndPersonByUserId(String userId);

    /**
     * 获取当前用户的所属业务单元主键，及其绑定的人员所属的业务单元主键
     * @return 
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    List<String> getOrgIdsWithUserAndPersonByCurrentUser();

    /**
     * 获取指定用户的所属业务单元，及其绑定的人员所属的业务单元
     * @param userId
     * @return 
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    List<OrgOrganizationDO> getOrgsWithUserAndPersonByUserId(String userId);

    /**
     * 获取当前用户的所属业务单元，及其绑定的人员所属的业务单元
     * @return 
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    List<OrgOrganizationDO> getOrgsWithUserAndPersonByCurrentUser();

    /**
     * 获取指定用户的所属业务单元
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    OrgOrganizationDO getOrgByUser(String userId);

    /**
     * 获取指定用户的所属业务单元主键
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    String getOrgIdByUser(String userId);

    /**
     * 获取当前用户的所属业务单元
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    OrgOrganizationDO getOrgByCurrentUser();

    /**
     * 获取当前用户的所属业务单元主键
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    String getOrgIdByCurrentUser();

    /**
     * 根据业务单元名称模糊查询业务单元列表 及 部门列表
     * @param orgName 业务单元名称
     * @return 
     * @author FourLeaves
     * @date 2019/12/20 17:25
     */
    List<OrgOrganizationDO> getOrganizationsAndDepsByOrgName(String orgName);
    /**根据用户Ids获取部门
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:35 2021/9/9
     * @Param List<sysUserDO></>
     * @return List<orgDepartmentDO></>
     **/
    List<OrgDepartmentDO> getOrgDepartmentListByUserIds(Set<String> userIds);
    /**根据转交用户Ids获取部门Id
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:35 2021/9/15
     * @Param List<String></>
     * @return List<orgDepartmentDO></>
     **/
    List<OrgDepartmentDO> listdeliverDepartment(Set<String> userIds);
    /**根据部门获取用户
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:35 2021/9/9
     * @Param departmentId
     * @return 部门下的用户信息
     **/
    List<SysUserDO> getUsersByDepartmentId(String departmentId);
    /**根据用户Ids获取用户信息
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:35 2021/9/9
     * @Param userIds
     * @return 用户实体类
     **/
    List<SysUserDO> getUsersByUserIds(List<String> userIds);
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
