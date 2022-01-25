package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

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
//
//    /**
//     * 通过业务单元主键列表获取用户列表
//     * @param orgIds
//     * @param addOrgId 是否填充用户的orgId属性
//     * @return
//     * @author yansiyang
//     * @date 2019/11/7 8:29
//     */
//    List<SysUserDO> getUsersByOrgIds(List<String> orgIds, boolean addOrgId);
//

    /**
     * 通过主部门主键列表获取用户主键列表
     * @param depId
     * @return 
     * @author FourLeaves
     * @date 2021/3/22 9:50
     */
    List<String> getUserIdsByMainDepId(String depId);

    /**
     * 通过主部门主键列表获取用户列表
     * @param depId
     * @return
     * @author FourLeaves
     * @date 2021/3/22 9:50
     */
    List<SysUserDO> getUsersByMainDepId(String depId);

    /**
     * 通过主部门主键列表获取用户列表
     * @param depIds
     * @return
     * @author FourLeaves
     * @date 2021/3/22 9:50
     */
    List<SysUserDO> getUsersByMainDepIds(List<String> depIds);

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
//
//
//    /**
//     * 通过业务单元主键列表获取用户主键列表
//     * @param orgIds
//     * @return
//     * @author yansiyang
//     * @date 2019/11/7 8:29
//     */
//    List<String> getUserIdsByOrgIds(List<String> orgIds);
//
//    /**
//     * 通过业务单元主键获取用户主键列表
//     * @param orgId
//     * @return
//     * @author yansiyang
//     * @date 2019/11/7 8:29
//     */
//    List<String> getUserIdsByOrgId(String orgId);
//
//    /**
//     * 通过业务单元主键获取用户列表
//     * @param orgId
//     * @return
//     * @author yansiyang
//     * @date 2019/11/7 8:29
//     */
//    List<SysUserDO> getUsersByOrgId(String orgId);

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
    R getTokenAfterLogin(String userKey);

    /**
     * 保存用户
     *
     * @param user
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    R saveUser(SysUserDO user);

    /**
     * 保存租户级用户
     *
     * @param user
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    boolean initUser(SysUserDO user);

    /**
     * 修改用户
     *
     * @param user
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    R updateUser(SysUserDO user);

    /**
     * 删除用户
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    R deleteUser(String[] ids);

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    boolean removeUsers(List<String> ids);

    /**
     * 关联人员档案
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:18
     */
    R persondoc(Map<String, String> map);

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
     * 校验验证码
     *
     * @param userName
     * @param captcha
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:51
     */
    boolean validateCaptcha(String userName, String captcha);


    /**
     * 租户管理员修改默认密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/11/20 8:27
     */
    R resetDefaultPassword(Map<String, String> map);

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
     * 解锁用户
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:52
     */
    boolean unlockUser(String userId);

    /**
     * 管理员重置用户密码
     *
     * @param userName 用户名
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    R resetUserPassword(String userName);


    /**
     * 修改用户集团授控域
     *
     * @param sysUserDO 用户信息
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    boolean saveUserGroupControlDomain(SysUserDO sysUserDO);

    /**
     * 修改用户应用授控域
     *
     * @param sysUserDO 用户信息
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    boolean saveUserAppControlDomain(SysUserDO sysUserDO);

    /**
     * 修改用户应用授控域并激活
     *
     * @param sysUserDO 用户信息
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    boolean saveuserAppControlDomainAndActive(SysUserDO sysUserDO);
    /**
     * 修改管理员IP地址校验
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    boolean updThreeTenantsIpAddress(Map<String, String> map);

    /**
     * 批量重置密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    boolean batchResetPassword(Map<String, String> map);

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
     * 开发平台-用户管理-新增
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/7/14 11:14
     */
    boolean saveUserForDev(Map<String, Object> map);

    /**
     * 开发平台-用户管理-修改
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/7/14 11:14
     */
    boolean updateUserForDev(Map<String, Object> map);

    /**
     * 创建开发平台超级管理员
     *
     * @return
     * @author zuogang
     * @date 2020/7/14 11:14
     */
    R addDevAdmin();

    /**
     * 根据部门和角色作为条件查询用户列表
     *
     * @param map
     * @param flg 委托规则接口判断 0否1是
     * @return
     * @author zuogang
     * @date 2020/11/16 15:37
     */
    R getUserListForDepOrRole(Map<String, String> map, Integer flg);
}
