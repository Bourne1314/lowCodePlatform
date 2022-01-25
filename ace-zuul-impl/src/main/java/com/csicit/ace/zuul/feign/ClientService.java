package com.csicit.ace.zuul.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.vo.FileVO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.common.utils.server.R;
import feign.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/24 8:49
 */
@FeignClient(url = "http://${ace.config.zuul.ip:127.0.0.1}:${ace.config.zuul.port:2100}", name = "ace")
public interface ClientService {


    /**
     * 绑定用户ip
     * @param userId
     * @param ip
     * @return
     * @author FourLeaves
     * @date 2021/9/1 8:07
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/bingUserIp/{userId}/{ip}", method = RequestMethod.GET)
    Boolean bingUserIp(@PathVariable("userId") String userId, @PathVariable("ip") String ip);

    /**
     * 获取用户绑定的用户列表
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2021/7/29 8:09
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getBindUsers/{userId}", method = RequestMethod.GET)
    List<SysUserDO> getBindUsers(@PathVariable("userId") String userId);

    /**
     * 获取用户绑定的用户主键列表
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2021/7/29 8:09
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getBindUserIds/{userId}", method = RequestMethod.GET)
    List<String> getBindUserIds(@PathVariable("userId") String userId);

    /**
     * 批量添加用户
     * @param users
     * @return
     * @author FourLeaves
     * @date 2021/4/19 8:13
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/addUsers", method = RequestMethod.POST)
    boolean addUsers(@RequestBody List<SysUserDO> users);

    /**
     * 批量添加个人档案
     * @param users
     * @return
     * @author FourLeaves
     * @date 2021/4/19 8:13
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/addPersonDocs", method = RequestMethod.POST)
    boolean addPersonDocs(@RequestBody List<BdPersonDocDO> personDocDOS);

    /**
     * 通过角色编码 判断用户是否有此角色
     * @param roleCode
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    @RequestMapping(value = "/orgauth/sysRoles/action/hasRoleByRoleCode", method = RequestMethod.GET)
    boolean hasRoleByRoleCode(@RequestParam("roleCode") String roleCode, @RequestParam("appId") String appId);

    /**
     * 通过角色主键 判断用户是否有此角色
     * @param roleId
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    @RequestMapping(value = "/orgauth/sysRoles/action/hasRoleByRoleId", method = RequestMethod.GET)
    boolean hasRoleByRoleId(@RequestParam("roleId") String roleId, @RequestParam("appId") String appId);


    /**
     * 通过角色标识获取角色
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:26
     */
    @RequestMapping(value = "/orgauth/sysRoles/action/getRoleByCode", method = RequestMethod.GET)
    SysRoleDO getRoleByCode(@RequestParam("code") String code, @RequestParam("appId") String appId);


    /**
     * 根据权限标识获取用户列表
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUserListByAuthCode", method =
            RequestMethod.GET)
    List<SysUserDO> getUserListByAuthCode(@RequestParam("code") String code, @RequestParam("appId") String appId);



    /**
     * 根据角色标识获取用户列表
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUserListByRoleCode", method =
            RequestMethod.GET)
    List<SysUserDO> getUserListByRoleCode(@RequestParam("code") String code, @RequestParam("appId") String appId);

    /**
     * 根据工号获取用户
     * @param staffNo
     * @return
     * @author FourLeaves
     * @date 2020/11/30 11:16
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUserByStaffNo", method =
            RequestMethod.GET)
    SysUserDO getUserByStaffNo(@RequestParam("staffNo") String staffNo);

    /**
     * 根据工号列表获取用户列表
     * @param staffNos
     * @return
     * @author FourLeaves
     * @date 2020/11/30 11:16
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUserListByStaffNos", method =
            RequestMethod.POST)
    List<SysUserDO> getUserListByStaffNos(@RequestBody List<String> staffNos);


    /**
     * 保存审计日志
     *
     * @return
     * @author FourLeaves
     * @date 2020/7/13 10:44
     */
    @RequestMapping(value = "/orgauth/sysAuditLogs", method =
            RequestMethod.POST)
    boolean saveLog(@RequestBody AuditLogTypeDO type, @RequestParam("title") String title, @RequestParam("opContent")
            Object opContent, @RequestParam("groupId") String groupId, @RequestParam("appId") String appId);

    /**
     * 大屏消息推送
     *
     * @param displayContent 显示内容
     * @param code           大屏通知标识
     * @param appName        应用标识
     * @return
     * @author zuog
     */
    @RequestMapping(value = "/orgauth/bladeVisualMsgs/action/msgPush/{displayContent}/{code}/{appName}", method =
            RequestMethod.GET)
    void bladeVisualMsgPush(@PathVariable("displayContent") String displayContent, @PathVariable("code") String code,
                            @PathVariable("appName") String appName);

    /**
     * 用户重名
     *
     * @param userName
     * @return
     * @author xulei
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/userRepeat", method =
            RequestMethod.POST)
    Integer userRepeat(String userName);

    /**
     * 根据用户id获取人员档案
     *
     * @return
     * @author xulei
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/getBdPersonDoc/{userId}", method = RequestMethod.GET)
    R getBdPersonDoc(@PathVariable("userId") String userId);

    /**
     * 存入人员档案
     *
     * @param personDoc
     * @author xulei
     * @Date 2020/6/30 9:14
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/savePersonAndUser", method =
            RequestMethod.POST)
    R savePersonAndUser(@RequestBody BdPersonDocDO personDoc);

    /**
     * 通过权限ID获取用户ID
     *
     * @param appId
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUserIdsByAuthId/{appId}/{authId}", method =
            RequestMethod.GET)
    List<String> getUserIdsByAuthId(@PathVariable("appId") String appId, @PathVariable("authId") String authId);

    /**
     * 通过权限ID获取用户
     *
     * @param appId
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersByAuthId/{appId}/{authId}", method =
            RequestMethod.GET)
    List<SysUserDO> getUsersByAuthId(@PathVariable("appId") String appId, @PathVariable("authId") String authId);


    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回用户信息 token
     *
     * @param userKey
     * @return
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    @RequestMapping(value = "/orgauth/login/action/getUserAfterLogin/{userKey}", method = RequestMethod.GET)
    R getUserAfterLogin(@PathVariable("userKey") String userKey);

    /**
     * 保存api资源
     *
     * @param apis
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/5/22 10:11
     */
    @RequestMapping(value = "/orgauth/sysApis", method = RequestMethod.POST)
    boolean saveSysApis(@RequestBody List<SysApiResourceDO> apis);

    /**
     * 用户退出
     *
     * @return
     * @author FourLeaves
     * @date 2020/1/2 15:40
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    R logout();

    @RequestMapping(value = "/orgauth/sysApis/{appId}", method = RequestMethod.DELETE)
    boolean delete(@PathVariable("appId") String appId);

    @RequestMapping(value = "/orgauth/codeSequences/action/getNum/{appId}/{bizTag}/{partValue}", method =
            RequestMethod.GET)
    String getNextNum(@PathVariable("appId") String appId,
                      @PathVariable("bizTag") String bizTag,
                      @PathVariable("partValue") String partValue);

    @RequestMapping(value = "/orgauth/codeSequences/action/getCode", method = RequestMethod.GET)
    String getTemplateCode(@RequestBody Map<String, String> map);

    /**
     * 发送消息
     *
     * @param sysMessageDO
     * @return boolean
     * @author shanwj
     * @date 2019/7/5 14:27
     */
    @RequestMapping(value = "/orgauth/sysMsgs", method = RequestMethod.POST)
    R sendMsg(@RequestBody SysMessageDO sysMessageDO);

    /**
     * 获取流程模板
     *
     * @param appId 应用ID
     * @param code  消息模板标识
     * @return
     * @author FourLeaves
     * @date 2020/4/27 16:05
     */
    @RequestMapping(value = "/orgauth/sysMsgs/action/getMsgTem/{appId}/{code}", method = RequestMethod.GET)
    SysMsgTemplateDO getMsgTem(@PathVariable("appId") String appId, @PathVariable("code") String code);

    /**
     * 启用socket事件
     *
     * @param socketEventVO
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/action/fire/socketEvent", method = RequestMethod.POST)
    R fireSocketEvent(@RequestBody SocketEventVO socketEventVO);

    /**
     * 查询用户未读消息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/query/noReadMsg/{userId}/{appId}", method = RequestMethod.GET)
    List<SysMessageDO> getNoReadMsgList(@PathVariable("userId") String userId, @PathVariable("appId") String appId);

    /**
     * 分页查询用户未读消息
     *
     * @param userId  用户id
     * @param size    每页条数
     * @param current 当前页
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/page/noReadMsg/{userId}/{appId}/{size}/{current}", method =
            RequestMethod.GET)
    Page getPageNoReadMsgList(@PathVariable("userId") String userId,
                              @PathVariable("appId") String appId,
                              @PathVariable("size") int size,
                              @PathVariable("current") int current);

    /**
     * 查询用户已读消息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/query/readMsg/{userId}/{appId}", method = RequestMethod.GET)
    List<SysMessageDO> getReadMsgList(@PathVariable("userId") String userId, @PathVariable("appId") String appId);

    /**
     * 分页查询用户已读消息
     *
     * @param userId  用户id
     * @param size    每页条数
     * @param current 当前页
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/page/readMsg/{userId}/{appId}/{size}/{current}", method =
            RequestMethod.GET)
    Page getPageReadMsgList(@PathVariable("userId") String userId,
                            @PathVariable("appId") String appId,
                            @PathVariable("size") int size,
                            @PathVariable("current") int current);

    /**
     * 查询用户所有消息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/query/allMsg/{userId}/{appId}", method = RequestMethod.GET)
    List<SysMessageDO> getAllMsgList(@PathVariable("userId") String userId, @PathVariable("appId") String appId);

    /**
     * 分页查询用户所有消息
     *
     * @param userId  用户id
     * @param size    每页条数
     * @param current 当前页
     * @return
     */
    @RequestMapping(value = "/orgauth/sysMsgs/page/allMsg/{userId}/{appId}/{size}/{current}", method =
            RequestMethod.GET)
    Page<SysMessageDO> getPageAllMsgList(@PathVariable("userId") String userId,
                                         @PathVariable("appId") String appId,
                                         @PathVariable("size") int size,
                                         @PathVariable("current") int current);

    @RequestMapping(value = "/orgauth/sysMsgs/action/update/msgRead", method = RequestMethod.POST)
    boolean updateUserMsgRead(@RequestParam("userId") String userId, @RequestParam("msgId") String msgId);

    @RequestMapping(value = "/orgauth/sysMsgs/action/update/allMsgRead", method = RequestMethod.POST)
    boolean updateUserAllNoReadMsg(@RequestParam("userId") String userId, @RequestParam("appId") String appId);

    @RequestMapping(value = "/orgauth/sysMsgs/action/delete/msg", method = RequestMethod.POST)
    boolean deleteMsg(@RequestParam("msgId") String msgId, @RequestParam("userId") String userId);

    @RequestMapping(value = "/orgauth/sysMsgs/action/delete/msgs/forInterface", method = RequestMethod.POST)
    boolean deleteMsgsForInterface(@RequestBody List<String> ids, @RequestParam("userId") String userId);

    @RequestMapping(value = "/orgauth/sysMsgs/action/delete/all", method = RequestMethod.POST)
    boolean deleteAllMsg(@RequestParam("userId") String userId, @RequestParam("appId") String appId);

    @RequestMapping(value = "/orgauth/sysMsgs/action/delete/readAll", method = RequestMethod.POST)
    boolean deleteAllReadMsg(@RequestParam("userId") String userId, @RequestParam("appId") String appId);


    /*************************************************************************************/

    /**
     * 根据密级、角色或组织信息获取用户并分页
     *
     * @param params type 类型 0 业务单元  1 角色
     *               id 主键
     *               secretLevel 密级
     *               searchStr 查询参数
     * @return
     * @author FourLeaves
     * @date 2020/2/21 10:46
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/listUsersBySecretLevelAndRoleOrOrg", method =
            RequestMethod.GET)
    R listUsersBySecretLevelAndRoleOrOrg(@RequestParam Map<String, String> params);


    /**
     * 获取应用
     *
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2019/12/25 17:44
     */
    @RequestMapping(value = "/orgauth/sysGroupApps/{appId}", method = RequestMethod.GET)
    SysGroupAppDO getAppById(@PathVariable("appId") String appId);

    /**
     * 扫描应用配置项 并写入数据库
     *
     * @param configDOSet
     * @return
     * @author FourLeaves
     * @date 2019/12/25 17:34
     */
    @RequestMapping(value = "/orgauth/sysConfigs/action/scanConfig", method = RequestMethod.POST)
    boolean scanConfig(@RequestBody Set<SysConfigDO> configDOSet);

    // -----------------------启动用的接口---------------------

    /**
     * 给App上锁
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    @RequestMapping(value = "/orgauth/sysGroupApps/action/lockApp", method = RequestMethod.GET)
    boolean lockApp(@RequestParam("appName") String appName);

    /**
     * 解锁app
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    @RequestMapping(value = "/orgauth/sysGroupApps/action/unLockApp", method = RequestMethod.GET)
    boolean unLockApp(@RequestParam("appName") String appName);
    // -------------------------------------------------------

    /**
     * 根据业务单元名称模糊查询业务单元列表 及 部门列表
     *
     * @param orgName
     * @return
     * @author FourLeaves
     * @date 2019/12/20 17:25
     */
    @RequestMapping(value = "/orgauth/organizations/action/getOrganizationsAndDepsByOrgName", method =
            RequestMethod.GET)
    List<OrgOrganizationDO> getOrganizationsAndDepsByOrgName(@RequestParam("orgName") String orgName);


    /**
     * 获取指定用户的所属业务单元，及其绑定的人员所属的业务单元
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    @RequestMapping(value = "/orgauth/organizations/action/getOrgsWithUserAndPersonByUserId/{userId}", method =
            RequestMethod.GET)
    List<OrgOrganizationDO> getOrgsWithUserAndPersonByUserId(@PathVariable("userId") String userId);


    /**
     * 获取指定用户的所属业务单元
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/11/5 15:25
     */
    @RequestMapping(value = "/orgauth/organizations/action/getOrgByUser/{userId}", method =
            RequestMethod.GET)
    OrgOrganizationDO getOrgByUser(@PathVariable("userId") String userId);


    /**
     * 根据父节点获取子节点应用菜单
     *
     * @param parentId
     * @return 菜单列表
     * @author shanwj
     * @date 2019/6/18 18:11
     */
    @RequestMapping(value = "/orgauth/sysMenus/action/listByParentId/{appId}/{parentId}/{userId}", method =
            RequestMethod.GET)
    List<SysMenuDO> listByParentId(@PathVariable("appId") String appId, @PathVariable("parentId") String parentId,
                                   @PathVariable("userId") String userId);


    /**
     * 根据 用户Id获取主职岗位
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/getMainPostByUserId/{userId}", method =
            RequestMethod.GET)
    BdPostDO getMainPostByUserId(@PathVariable("userId") String userId);


    /**
     * 根据部门 用户Id获取所有岗位
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/getPostsByUserId/{userId}", method = RequestMethod
            .GET)
    List<BdPostDO> getPostsByUserId(@PathVariable("userId") String userId);

    /**
     * 根据部门 用户Id获取岗位
     *
     * @param depId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/getPostByDepIdAndUserId/{depId}/{userId}", method =
            RequestMethod.GET)
    BdPostDO getPostByDepIdAndUserId(@PathVariable("depId") String depId, @PathVariable("userId") String userId);

    /**
     * 根据部门 用户Id获取职务
     *
     * @param depId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/orgauth/bdPersonDocs/action/getJobByDepIdAndUserId/{depId}/{userId}", method =
            RequestMethod.GET)
    BdJobDO getJobByDepIdAndUserId(@PathVariable("depId") String depId, @PathVariable("userId") String userId);

    /********************************************************************************************************/
    /**
     * 根据相关条件获取指定用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/10/15 8:27
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getSomeUsers", method = RequestMethod.GET)
    List<SysUserDO> getSomeUsers(@RequestParam Map<String, Object> map);
    /********************************************************************************************************/


    /**
     * 获取登录用户在该应用下的有效权限列表
     *
     * @param appId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/10/15 8:27
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getMixAuth/{appId}/{userId}", method = RequestMethod.GET)
    List<SysAuthDO> getMixAuth(@PathVariable("appId") String appId, @PathVariable("userId") String userId);
    /********************************************************************************************************/

    /**
     * 修改当前用户密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/11/6 10:42
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/updateCurrentUserPassword", method = RequestMethod.POST)
    R updateCurrentUserPassword(@RequestBody Map<String, String> map);


    /**
     * 根据部门ID获取 对应部门及其子部门下所有用户
     *
     * @param depId 部门ID
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getAllUsersByDepAndSon/{depId}", method = RequestMethod
            .GET)
    List<SysUserDO> getAllUsersByDepAndSon(@PathVariable("depId") String depId);


    /**
     * 根据父节点ID获取  type=1 子业务单元 type=2 子业务单元和部门 type=3 部门
     *
     * @return 业务单元对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getSonOrganizationByParentId/{parentId}/{type}",
            method = RequestMethod
                    .GET)
    List<OrgOrganizationDO> getSonOrganizationByParentId(@PathVariable("parentId") String parentId, @PathVariable
            ("type") Integer
            type);

    /**
     * 通过主键获取指定用户信息
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/sysUsers/{id}", method = RequestMethod.GET)
    SysUserDO getUserById(@PathVariable("id") String id);

    /**
     * 模糊查询用户列表 用户姓名RealName  用户名 ID
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersByMatchNameOrID/{str}", method = RequestMethod
            .GET)
    List<SysUserDO> getUsersByMatchNameOrID(@PathVariable("str") String str);

    /**
     * 获取使用该应用的相同集团相同业务单元用户列表
     *
     * @param groupId
     * @param orgId
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUseAppUsers/{groupId}/{orgId}/{appId}", method =
            RequestMethod
                    .GET)
    List<SysUserDO> getUseAppUsers(@PathVariable("groupId") String groupId, @PathVariable("orgId") String orgId,
                                   @PathVariable("appId") String appId);

    /**
     * 根据业务单元ID获取 对应业务单元及其子业务单元下所有用户
     *
     * @param organizationId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getAllUsersByOrgAndSon/{organizationId}", method =
            RequestMethod.GET)
    List<SysUserDO> getAllUsersByOrgAndSon(@PathVariable("organizationId") String organizationId);

    /**
     * 通过用户名获取指定用户信息
     *
     * @param name
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/sysUsers/userName/{name}", method = RequestMethod.GET)
    SysUserDO getUserByName(@PathVariable("name") String name);

    /**
     * 通过主键获取指定部门信息
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/orgDepartments/{id}", method = RequestMethod.GET)
    OrgDepartmentDO getDepartmentById(@PathVariable("id") String id);

    /**
     * 通过code获取指定部门信息
     *
     * @param code
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/orgDepartments/action/getDepartmentByCode/{code}", method =
            RequestMethod.GET)
    OrgDepartmentDO getDepartmentByCode(@PathVariable("code") String code);

    /**
     * 通过用户主键获取指定用户角色列表
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/sysRoles/action/getRolesByUserId/{userId}", method = RequestMethod.GET)
    List<SysRoleDO> getRolesByUserId(@PathVariable("userId") String userId);

    /**
     * 通过主键获取指定角色信息
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/sysRoles/{id}", method = RequestMethod.GET)
    SysRoleDO getRoleById(@PathVariable("id") String id);

    /**
     * 通过配置项名称获取指定配置项信息
     *
     * @param name
     * @return
     * @author yansiyang
     * @date 2019/8/22 19:44
     */
    @RequestMapping(value = "/orgauth/sysConfigs/{name}", method = RequestMethod.GET)
    String getConfigValue(@PathVariable("name") String name);

    /**
     * 获取应用的配置项
     *
     * @param appId
     * @param name
     * @return
     * @author yansiyang
     * @date 2019/8/22 16:33
     */
    @RequestMapping(value = "/orgauth/sysConfigs/action/getConfigValueByApp/{appId}/{name}", method =
            RequestMethod.GET)
    String getConfigValueByApp(@PathVariable("appId") String appId, @PathVariable("name") String name);

    /**
     * 通过字典类型获取字典项
     *
     * @param params 参数
     * @return 字典项列表
     * @author shanwj
     * @date 2019/6/18 18:06
     */
    @RequestMapping(value = "/orgauth/sysDicts", method = RequestMethod.GET)
    List<SysDictValueDO> getDictValue(@RequestParam Map<String, Object> params);

    /**
     * 获取应用菜单
     *
     * @param appId
     * @return 菜单列表
     * @author shanwj
     * @date 2019/6/18 18:11
     */
    @RequestMapping(value = "/orgauth/sysMenus/{appId}", method = RequestMethod.GET)
    List<SysMenuDO> getMenuList(@PathVariable("appId") String appId);

    /**
     * 获取应用菜单无权限
     *
     * @param appId
     * @return 菜单列表
     * @author xulei
     * @date 2020/6/17 10:02
     */
    @RequestMapping(value = "/orgauth/sysMenus/noPermission/{appId}", method = RequestMethod.GET)
    List<SysMenuDO> getMenuListByNoPermission(@PathVariable("appId") String appId);

    /**
     * 在应用启动时检查应用菜单和权限是否包含了工作流的菜单和权限，没有的话就新增相应的权限和菜单
     *
     * @param params
     * @author zuogang
     * @date 2019/6/18 18:11
     */
    @RequestMapping(value = "/orgauth/sysMenus", method = RequestMethod.POST)
    boolean setAppFLowMenu(@RequestBody Map<String, String> params);

    /**
     * 模糊查询获取用户列表 姓名 角色名 部门名
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersByMatch/{str}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByMatch(@PathVariable("str") String str);

    /**
     * 根据角色ID获取用户列表
     *
     * @param roleId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersByRoleId/{roleId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersByRoleId(@PathVariable("roleId") String roleId);

    /**
     * 根据部门ID获取用户列表
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersOnlyByDepId/{depId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersOnlyByDepId(@PathVariable("depId") String depId);

    /**
     * 根据部门ID获取用户列表 depId为0 获取当前用户业务单元下所有用户   depId若为业务单元ID 则查询业务单元下的所有用户
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersDepId/{depId}", method = RequestMethod.GET)
    List<SysUserDO> getUsersDepId(@PathVariable("depId") String depId);

    /**
     * 根据密级获取用户列表
     *
     * @param level
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersBySecretLevel/{level}", method = RequestMethod
            .GET)
    List<SysUserDO> getUsersBySecretLevel(@PathVariable("level") Integer level);

    /**
     * 根据密级获取用户列表 le 1 true 0   false
     *
     * @param level
     * @param le
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    @RequestMapping(value = "/orgauth/sysUsers/action/getUsersBySecretLevel/{level}/{le}", method =
            RequestMethod.GET)
    List<SysUserDO> getUsersBySecretLevel(@PathVariable("level") Integer level, @PathVariable("le") Integer le);

    /**
     * 获取指定集团的所有部门
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/8/14 15:12
     */
    @RequestMapping(value = "/orgauth/orgDepartments/action/getDepartmentByGroupId/{groupId}", method =
            RequestMethod.GET)
    List<OrgDepartmentDO> getDepartmentByGroupId(@PathVariable("groupId") String groupId);

    /**
     * 获取指定集团的所有角色
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/8/14 15:12
     */
    @RequestMapping(value = "/orgauth/sysRoles/action/getRolesByGroupId/{groupId}", method = RequestMethod.GET)
    List<SysRoleDO> getRolesByGroupId(@PathVariable("groupId") String groupId);

    /**
     * 获取当前用户所属业务单元
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getCurrentOrganization/{organizationId}", method =
            RequestMethod.GET)
    OrgOrganizationDO getCurrentOrganization(@PathVariable("organizationId") String organizationId);

    /**
     * 根据ID获取所属业务单元
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getOrganizationByID/{organizationId}", method =
            RequestMethod.GET)
    OrgOrganizationDO getOrganizationByID(@PathVariable("organizationId") String organizationId);

    /**
     * 获取当前用户所属集团的业务单元列表 及 部门列表
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getCurrentOrganizationsAndDeps/{groupId}", method
            = RequestMethod.GET)
    List<OrgOrganizationDO> getCurrentOrganizationsAndDeps(@PathVariable("groupId") String groupId);

    /**
     * 获取当前用户的实体部门
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getEntityDeptOfCurrentUser", method
            = RequestMethod.GET)
    OrgOrganizationDO getEntityDeptOfCurrentUser();

    /**
     * 获取当前用户的实体部门
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getEntityDeptByUserId/{userId}", method
            = RequestMethod.GET)
    OrgOrganizationDO getEntityDeptByUserId(@PathVariable("userId") String userId);

    /**
     * 获取当前用户所属集团的业务单元列表
     *
     * @return 集团对象
     * @author yansiyang
     * @date 16:49 2019/8/14
     */
    @RequestMapping(value = "/orgauth/organizations/action/getCurrentOrganizations/{groupId}", method =
            RequestMethod.GET)
    List<OrgOrganizationDO> getCurrentOrganizations(@PathVariable("groupId") String groupId);

    /**
     * 根据业务单元ID获取对应部门信息
     *
     * @param organizationId 业务单元ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    @RequestMapping(value = "/orgauth/orgDepartments/action/getDeptsByOrganizationId/{organizationId}",
            method = RequestMethod.GET)
    List<OrgDepartmentDO> getDeptsByOrganizationId(@PathVariable("organizationId") String organizationId);

    /**
     * 获取指定用户主要部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    @RequestMapping(value = "/orgauth/orgDepartments/action/getMainDeptByUserId/{userId}", method =
            RequestMethod.GET)
    OrgDepartmentDO getMainDeptByUserId(@PathVariable("userId") String userId);


    /**
     * @Author zhangzhaojun
     * @Description //根据userIds获取部门
     * @Date 15:57 2021/9/9
     * @Param List<sysUserDO></sysUserDO>
     * @return 部门列表
     **/
    @RequestMapping(value = "/orgauth/orgDepartments/action/getOrgDepartmentListByUserIds", method =
            RequestMethod.POST)
    List<OrgDepartmentDO> getOrgDepartmentListByUserIds(@RequestBody Set<String> userIds);
    /**
     * @Author zhangzhaojun
     * @Description //根据userIds获取部门
     * @Date 15:57 2021/9/15
     * @Param List<String></sysUserDO>
     * @return 部门列表
     **/
    @RequestMapping(value = "/orgauth/orgDepartments/action/listdeliverDepartment", method =
            RequestMethod.POST)
    List<OrgDepartmentDO> listdeliverDepartment(@RequestBody Set<String> userIds);

    /**
     * @Author zhangzhaojun
     * @Description //根据部门获取用户
     * @Date 15:57 2021/9/9
     * @Param departmentId
     * @return 部门下的用户信息
     **/
    @RequestMapping(value = "/orgauth/orgDepartments/action/getUsersByDepartmentId", method =
            RequestMethod.GET)
    List<SysUserDO> getUsersByDepartmentId(@RequestParam("departmentId") String departmentId);

    /**
     * @Author zhangzhaojun
     * @Description //根据部门获取用户
     * @Date 15:57 2021/9/9
     * @Param userIds
     * @return List<sysUserDO></sysUserDO>
     **/
    @RequestMapping(value = "/orgauth/orgDepartments/action/getUsersByUserIds", method =
            RequestMethod.GET)
    List<SysUserDO> getUsersByUserIds(@RequestParam("userIds") List<String> UserIds);




    /**
     * 获取指定用户部门列表
     *
     * @return 部门列表
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    @RequestMapping(value = "/orgauth/orgDepartments/action/getDeptsByUserId/{userId}", method =
            RequestMethod.GET)
    List<OrgDepartmentDO> getDeptsByUserId(@PathVariable("userId") String userId);

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId 用户ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/hasAuthorityWithUserId/{userId}/{authId}", method =
            RequestMethod.GET)
    boolean hasAuthorityWithUserId(@PathVariable("userId") String userId, @PathVariable("authId") String authId);

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId   用户ID
     * @param authCode 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/hasAuthCodeInCurrentApp/{userId}/{authCode}/{appId}",
            method = RequestMethod
                    .GET)
    boolean hasAuthCodeInCurrentApp(@PathVariable("userId") String userId, @PathVariable("authCode") String authCode,
                                    @PathVariable("appId") String appId);

    /**
     * 判断指定角色是否拥有某项权限
     *
     * @param roleId 角色ID
     * @param authId 权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/hasAuthorityWithRoleId/{roleId}/{authId}", method =
            RequestMethod.GET)
    boolean hasAuthorityWithRoleId(@PathVariable("roleId") String roleId, @PathVariable("authId") String authId);

    /**
     * 获取指定用户的所有权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAllAuthIdsByUserId/{userId}", method = RequestMethod
            .GET)
    List<String> getAllAuthIdsByUserId(@PathVariable("userId") String userId);

    /**
     * 获取指定用户关于指定应用的所有权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAllAuthIdsByUserIdAndApp/{userId}/{appId}", method =
            RequestMethod.GET)
    List<String> getAllAuthIdsByUserIdAndApp(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId);

    /**
     * 获取指定用户关于指定应用的所有权限标识(code)
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAllAuthCodesByUserIdAndApp/{userId}/{appId}", method
            = RequestMethod.GET)
    List<String> getAllAuthCodesByUserIdAndApp(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId);

    /**
     * 获取指定角色的所有权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:05 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAuthIdsByRoleId/{roleId}", method = RequestMethod.GET)
    List<String> getAuthIdsByRoleId(@PathVariable("roleId") String roleId);

    /**
     * 判断指定用户是否拥有使用某项api资源的权限
     *
     * @param userId 用户ID
     * @param api    权限标识
     * @return boolean
     * @author yansiyang
     * @date 16:55 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/hasApiByUserId/{userId}/{api}", method = RequestMethod
            .GET)
    boolean hasApiByUserId(@PathVariable("userId") String userId, @PathVariable("api") String api);

    /**
     * 获取指定用户的所有api资源的权限
     *
     * @param userId 用户ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAllApisByUserId/{userId}", method = RequestMethod.GET)
    List<String> getAllApisByUserId(@PathVariable("userId") String userId);

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAllApisByUserIdAndApp/{userId}/{appId}", method =
            RequestMethod.GET)
    List<String> getAllApisByUserIdAndApp(@PathVariable("userId") String userId, @PathVariable("appId") String appId);

    /**
     * 获取指定用户关于指定应用的api资源的权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     * @author yansiyang
     * @date 17:03 2019/3/28
     */
    @RequestMapping(value = "/orgauth/sysAuths/action/getAllApisByRoleId/{roleId}", method = RequestMethod.GET)
    List<String> getAllApisByRoleId(@PathVariable("roleId") String roleId);

    /**
     * 查询报表树
     *
     * @param parentId 报表类别Id
     * @param type     报表类型 1报表2仪表盘
     * @return java.util.List<com.csicit.ace.common.pojo.vo.TreeVO>
     * @author shanwj
     * @date 2019/12/13 8:46
     */
    @RequestMapping(value = "/orgauth/reports/query/reportTree/{parentId}/{type}/{appId}", method =
            RequestMethod.GET)
    List<TreeVO> getReportTree(@PathVariable("parentId") String parentId,
                               @PathVariable("type") int type,
                               @PathVariable("appId") String appId);


    /*******************************************************************************************/

    @CrossOrigin
    @ApiOperation(value = "/fileserver删除文件")
    @RequestMapping(value = "/fileInfo/feignDeleteByFileId", method = RequestMethod.POST)
    void deleteByFileId(@RequestBody Map<String, Object> params);


    @CrossOrigin
    @ApiOperation(value = "/fileserver删除文件")
    @RequestMapping(value = "/fileDownload/downloadZipped", method = RequestMethod.POST)
    void downloadZipped(@RequestParam String appId,@RequestParam String configurationKey,@RequestParam String downloadToken) throws IOException;

    @CrossOrigin
    @ApiOperation(value = "/fileserver删除表单文件")
    @RequestMapping(value = "/fileInfo/feignDeleteByFormId", method = RequestMethod.POST)
    void deleteByFormId(@RequestBody Map<String, Object> params);

    @CrossOrigin
    @ApiOperation(value = "/fileserver删除表单文件")
    @RequestMapping(value = "/fileInfo/feignDeleteAllByFormId", method = RequestMethod.POST)
    void deleteAllByFormId(Map<String, Object> params);

    @CrossOrigin
    @ApiOperation(value = "/fileserver单文件共享")
    @RequestMapping(value = "/fileInfo/feignShareByFileId", method = RequestMethod.POST)
    FileInfoDO shareByFileId(@RequestBody Map<String, Object> params);

    @CrossOrigin
    @ApiOperation(value = "/fileserver表单文件共享")
    @RequestMapping(value = "/fileInfo/feignShareByFormId", method = RequestMethod.POST)
    void shareByFormId(@RequestBody Map<String, Object> params);

    @CrossOrigin
    @ApiOperation(value = "/fileserver获取表单文件")
    @RequestMapping(value = "/fileInfo/feignListByFormId", method = RequestMethod.POST)
    List<FileInfoDO> listByFormId(@RequestBody Map<String, Object> params);

    @CrossOrigin
    @ApiOperation(value = "/fileserver获取文件")
    @RequestMapping(value = "/fileInfo/feignGetByFileId", method = RequestMethod.POST)
    FileInfoDO getByFileId(@RequestBody Map<String, Object> params);

    @CrossOrigin
    @RequestMapping(value = "/fileDownload/download", method = RequestMethod.GET)
    Response download(@RequestParam Map<String, Object> params);

    @CrossOrigin
    @RequestMapping(value = "/fileConfiguration/feignLoadByKey", method = RequestMethod.GET)
    FileConfiguration loadFileConfigurationByKey(@RequestBody Map<String, Object> params);

    @CrossOrigin
    @RequestMapping(value = "/fileRepository/feginAllocateSpace", method = RequestMethod.POST)
    FileInfoDO allocateSpace(@RequestBody FileVO file);

    @CrossOrigin
    @RequestMapping(value = "/fileUpload/feignUpload", method = RequestMethod.POST)
    void upload(@RequestBody Map<String, Object> params) throws Exception;

    @CrossOrigin
    @RequestMapping(value = "/fileDownload/exportZip", method = RequestMethod.GET)
    Response exportZip(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String
            formId, @RequestParam("appName") String appId);

    @CrossOrigin
    @RequestMapping(value = "/fileDownload/exportZipBatch", method = RequestMethod.POST)
    Response exportZipBatch(@RequestBody List<ExportInfo> exportInfos);

    @CrossOrigin
    @RequestMapping(value = "/fileUpload/reviewFile", method = RequestMethod.POST)
    R addReviewFile(@RequestParam("uploadFormId") String uploadFormId);

    @CrossOrigin
    @RequestMapping(value = "/fileConfiguration/setFileReview", method = RequestMethod.POST)
    R setReviewFile(@RequestParam("formId") String formId);
}
