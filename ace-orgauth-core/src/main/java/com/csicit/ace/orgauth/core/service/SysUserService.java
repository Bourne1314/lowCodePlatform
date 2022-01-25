package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysUserService extends IService<SysUserDO> {

    /**
     * 批量添加用户 
     * @param users
     * @return 
     * @author FourLeaves
     * @date 2021/4/19 8:19
     */
    boolean addUsers(List<SysUserDO> users);


    /**
     * 判断用户是否重名
     * @return Integer
     * @param userName
     * @author xulei
     * @date 2020/7/8 15.41
     */
    Integer userRepeat(String userName);

    /**
     * 保存用户
     *
     * @param user
     * @return
     * @author xulei
     * @date 2020/6/30 10:14
     */
    R saveUser(SysUserDO user);

    /**
     * 通过权限ID获取用户ID
     * @param appId
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    List<String> getUserIdsByAuthId(String appId, String authId);

    /**
     * 通过权限ID获取用户
     * @param appId
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    List<SysUserDO> getUsersByAuthId(String appId, String authId);

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
    R listUsersBySecretLevelAndRoleOrOrg(Map<String, String> params);

    /**
     * 修改当前用户密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/11/6 10:42
     */
    R updateCurrentUserPassword(Map<String, String> map);

    /**
     * 模糊查询用户列表 用户名 ID
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByMatchNameOrID(String str);

    /**
     * 根据业务单元ID获取 对应业务单元及其子业务单元下所有用户
     *
     * @param organizationId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getAllUsersByOrgAndSon(String organizationId);


    /**
     * 根据部门ID获取 对应部门及其子部门下所有用户
     *
     * @param depId 部门ID
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getAllUsersByDepAndSon(String depId);


    /**
     * 根据相关条件获取指定用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/10/15 8:27
     */
    List<SysUserDO> getSomeUsers(Map<String, Object> map);


    /**
     * 模糊查询用户列表 用户名 角色名  部门名
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByMatch(String str);

    /**
     * 模糊查询用户列表 用户名 角色名  部门名
     *
     * @param groupId
     * @param orgId
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUseAppUsers(String groupId, String orgId, String appId);


    /**
     * 根据角色ID获取用户列表
     *
     * @param roleId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByRoleId(String roleId);

    /**
     * 根据部门ID获取用户列表 depId为0 获取当前用户业务单元下所有用户   depId若为业务单元ID 则查询业务单元下的所有用户
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByDepId(String depId);


    /**
     * 根据部门ID获取用户列表
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersOnlyByDepId(String depId);


    /**
     * 根据密级获取用户列表
     *
     * @param level
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersBySecretLevel(Integer level);


    /**
     * 根据密级获取用户列表
     *
     * @param level
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersBySecretLevel(Integer level, Integer le);


    /**
     * 查询指定用户信息
     *
     * @param userId 用户主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    SysUserDO infoById(String userId);

    /**
     * 通过用户名称查询指定用户信息
     *
     * @param userName 用户名称
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    SysUserDO infoByUserName(String userName);


    /**
     * 查询当前用户信息
     *
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    SysUserDO getCurrentUser();


    /**
     * 填充用户的 业务单元  集团信息  users不为空则利用users
     *
     * @param userIds
     * @param users
     * @return
     * @author yansiyang
     * @date 2019/11/7 8:29
     */
    List<SysUserDO> fillUserOrgAndGroup(List<String> userIds, List<SysUserDO> users);


    /**
     * 填充用户的部门 业务单元属性
     *
     * @param users
     * @return
     * @author yansiyang
     * @date 2019/8/20 16:28
     */
    List<SysUserDO> fillUsersOrgAndDep(List<SysUserDO> users);

    /**
     * 验证用户名密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:51
     */
    boolean authenticate(String userName, String password);

    /**
     * 更新密码
     *
     * @param userName 用户名
     * @param password 密码
     * @param save     是否为保存用户时调用的方法
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:51
     */
    R updatePassword(String userName, String password, boolean save);


    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    R login(String userName, String password);

    /**
     * 缓存用户的主要信息到缓存
     *
     * @param token
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/15 15:02
     */
    R cacheUserInfos(String token, String userId, boolean ok, int code, String msg);

    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回token
     *
     * @param userKey 用户名  或  用户ID
     * @return
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    R getUserInfoAfterLogin(String userKey);


    /**
     * 开发者自定义验证方式
     * 验证通过后提供用户名  或  用户ID
     * 方法模拟正常登陆 缓存用户相关信息  返回token
     *
     * @param userKey 用户名  或  用户ID
     * @return
     * @author yansiyang
     * @date 2019/9/2 8:30
     */
    String getTokenAfterLogin(String userKey);
}
