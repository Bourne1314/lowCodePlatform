package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.server.R;

import java.util.List;

/**
 * 安全管理
 *
 * @author yansiyang
 * @version v1.0
 * @date: Created in 15:28 2019/3/28
 */
public interface ISecurity {


    /**
     * 通过角色编码 判断用户是否有此角色
     * @param roleCode
     * @return 
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    boolean hasRoleByRoleCode(String roleCode);

    /**
     * 通过角色主键 判断用户是否有此角色
     * @param roleId
     * @return 
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    boolean hasRoleByRoleId(String roleId);

    /**
     * 用户退出
     *
     * @return R
     * @author FourLeaves
     * @date 2020/1/2 15:43
     */
    R logout();

    /**
     * 根据 用户Id获取主职岗位
     *
     * @param userId 用户id
     * @return BdPostDO 岗位对象
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    BdPostDO getMainPostByUserId(String userId);


    /**
     * 根据部门 用户Id获取所有岗位
     *
     * @param userId 用户id
     * @return 岗位列表
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    List<BdPostDO> getPostsByUserId(String userId);

    /**
     * 根据部门 用户Id获取岗位
     *
     * @param depId 部门id
     * @param userId 用户id
     * @return 岗位对象
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    BdPostDO getPostByDepIdAndUserId(String depId, String userId);

    /**
     * 根据部门 用户Id获取职务
     *
     * @param depId 部门id
     * @param userId 用户id
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    BdJobDO getJobByDepIdAndUserId(String depId, String userId);

    /**
     * 修改当前用户密码
     *
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return
     * @author yansiyang
     * @date 2019/11/6 10:42
     */
    R updateCurrentUserPassword(String oldPassword, String newPassword);

    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回 用户相关信息
     *
     * @param userKey 用户名  或  用户ID
     * @return R
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    R getUserAfterLogin(String userKey);

    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回 token
     *
     * @param userKey 用户名  或  用户ID
     * @return token
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    String getTokenAfterLogin(String userKey);

    /**
     * 获取当前用户
     *
     * @return 用户对象
     * @author yansiyang
     * @date 2019/5/7 20:08
     */
    SysUserDO getCurrentUser();

    /**
     * 根据用户ID获取指定用户
     *
     * @param userId 用户ID
     * @return 用户
     * @author yansiyang
     * @date 16:44 2019/3/28
     */
    SysUserDO getUserByUserId(String userId);

    /**
     * 根据用户名获取指定用户
     *
     * @param userName 用户名
     * @return 用户
     * @author yansiyang
     * @date 16:44 2019/3/28
     */
    SysUserDO getUserByUserName(String userName);

    /**
     * 获取当前用户部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    List<OrgDepartmentDO> getCurrentDepts();

    /**
     * 获取当前用户主要部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    OrgDepartmentDO getCurrentMainDept();

    /**
     * 获取指定用户主要部门信息
     *
     * @param userId 用户ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    OrgDepartmentDO getMainDeptByUserId(String userId);

    /**
     * 根据部门ID获取指定部门信息
     *
     * @param deptId 部门ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    OrgDepartmentDO getDept(String deptId);

    /**
     * 根据部门编码获取指定部门信息
     *
     * @param code 部门编码
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    OrgDepartmentDO getDeptByCode(String code);

    /**
     * 根据集团ID获取对应部门信息
     *
     * @param groupId 集团ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<OrgDepartmentDO> getDeptsByGroupId(String groupId);

    /**
     * 根据业务单元ID获取对应部门信息
     *
     * @param organizationId 业务单元ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<OrgDepartmentDO> getDeptsByOrganizationId(String organizationId);

    /**
     * 根据当前用户集团的对应部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<OrgDepartmentDO> getDeptsByCurrentGroup();

    /**
     * 根据用户ID获取对应部门信息
     *
     * @param userId 用户ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<OrgDepartmentDO> getDeptsByUserId(String userId);

    /**
     * 获取当前 非管理员 用户集团
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgGroupDO getCurrentGroup();

    /**
     * 获取当前用户集团信息
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgGroupDO> getCurrentGroups();

    /**
     * 获取当前用户所属业务单元
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgOrganizationDO getCurrentOrganization();


    /**
     * 根据父节点ID获取 仅第一层无递归  type=1 子业务单元 type=2 子业务单元和部门 type=3 部门
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgOrganizationDO> getSonOrganizationByParentId(String parentId, int type);

    /**
     * 获取当前用户所属业务单元的顶级业务单元
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgOrganizationDO getCurrentRootOrganization();

    /**
     * 根据业务单元ID获取其顶级业务单元
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgOrganizationDO getRootOrganizationByOrgId(String organizationId);

    /**
     * 根据用户ID获取其顶级业务单元
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    OrgOrganizationDO getRootOrganizationByUserId(String userId);

    /**
     * 获取当前用户所属集团的业务单元列表 及 部门列表
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgOrganizationDO> getCurrentOrganizationsAndDeps();

    /**
     * 获取当前用户所属集团的业务单元列表
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    List<OrgOrganizationDO> getCurrentOrganizations();

    /**
     * 获取当前用户角色信息
     *
     * @return 角色对象
     * @author yansiyang
     * @date 16:52 2019/3/28
     */
    List<SysRoleDO> getCurrentRoles();

    /**
     * 获取指定用户角色信息
     *
     * @param userId 用户ID
     * @return 角色对象
     * @author yansiyang
     * @date 16:52 2019/3/28
     */
    List<SysRoleDO> getRolesByUserId(String userId);

    /**
     * 根据集团ID获取对应角色信息
     *
     * @param groupId 集团ID
     * @return 角色对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<SysRoleDO> getRolesByGroupId(String groupId);

    /**
     * 根据当前用户集团的对应角色信息
     *
     * @return 角色对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<SysRoleDO> getRolesByCurrentGroup();

    /**
     * 获取指定角色信息
     *
     * @param roleId 角色ID
     * @return 角色对象
     * @author yansiyang
     * @date 16:53 2019/3/28
     */
    SysRoleDO getRole(String roleId);

    /**
     * 判断当前用户是否拥有某项权限
     *
     * @param authId 权限主键
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthority(String authId);

    /**
     * 判断当前用户是否拥有某项权限
     *
     * @param authCode 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthCodeInCurrentApp(String authCode);

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId   用户ID
     * @param authCode 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthCodeInCurrentAppWithUserId(String userId, String authCode);

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId 用户ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthorityWithUserId(String userId, String authId);

    /**
     * 判断指定角色是否拥有某项权限
     *
     * @param roleId 角色ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasAuthorityWithRoleId(String roleId, String authId);


    /**
     * 获取当前用户对于某个应用的所有权限
     *
     * @param appId 应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:00 2019/3/28
     */
    List<String> getAuthIdsByApp(String appId);

    /**
     * 获取当前用户对于某个应用的所有权限标识(code)
     *
     * @param appId 应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:00 2019/3/28
     */
    List<String> getAuthCodesByApp(String appId);

    /**
     * 获取当前用户对于当前应用的所有权限标识(code)
     *
     * @return 权限集合
     * @author yansiyang
     * @date 17:00 2019/3/28
     */
    List<String> getAuthCodes();

    /**
     * 获取某个用户对于当前应用的所有权限标识(code)
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:00 2019/3/28
     */
    List<String> getAuthCodesByUser(String userId);


    /**
     * 获取某个用户对于某个应用的所有权限标识(code)
     *
     * @param appId 应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:00 2019/3/28
     */
    List<String> getAllAuthCodesByUserIdAndApp(String appId, String userId);


    /**
     * 获取当前用户所有权限
     *
     * @param
     * @author yansiyang
     * @date 17:00 2019/3/28
     * @return权限集合
     */
    List<String> getAllAuthIds();

    /**
     * 获取指定用户的所有权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllAuthIdsByUserId(String userId);

    /**
     * 获取指定用户关于指定应用的所有权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllAuthIdsByUserIdAndApp(String userId, String appId);

    /**
     * 获取指定角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:05 2019/3/28
     */
    List<String> getAuthIdsByRoleId(String roleId);

    /**
     * 判断当前用户是否拥有使用某项api资源的权限
     *
     * @param api 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasApi(String api);

    /**
     * 判断指定用户是否拥有使用某项api资源的权限
     *
     * @param userId 用户ID
     * @param api    权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    boolean hasApiByUserId(String userId, String api);

    /**
     * 获取当前用户所有的api资源的权限
     *
     * @author yansiyang
     * @date 17:00 2019/3/28
     * @date 16:55 2019/3/28
     */
    List<String> getApis();

    /**
     * 获取当前用户对于某个应用的api资源的权限
     *
     * @param appId 应用ID
     * @author yansiyang
     * @date 17:00 2019/3/28
     * @date 16:55 2019/3/28
     */
    List<String> getApisByApp(String appId);

    /**
     * 获取指定用户的所有api资源的权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllApisByUserId(String userId);

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllApisByUserIdAndApp(String userId, String appId);

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    List<String> getAllApisByRoleId(String roleId);

}
