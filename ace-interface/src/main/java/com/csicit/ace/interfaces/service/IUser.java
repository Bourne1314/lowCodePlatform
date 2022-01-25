package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.vo.SysOnlineUserVO;
import com.csicit.ace.common.utils.server.R;

import java.util.List;
import java.util.Map;

/**
 * 用户接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/14 8:28
 */
public interface IUser {


    /**
     * 绑定用户ip
     * @param userId
     * @param ip
     * @return 
     * @author FourLeaves
     * @date 2021/9/1 8:07
     */
    Boolean bingUserIp(String userId, String ip);

    /**
     * 获取用户绑定的用户列表
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    List<SysUserDO> getBindUsers(String userId);

    /**
     * 获取用户绑定的用户主键列表
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    List<String> getBindUserIds(String userId);


    /**
     * 根据权限标识获取用户列表
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    List<SysUserDO> getUserListByAuthCode(String code);

    /**
     * 批量添加用户
     * @param users
     * @return 
     * @author FourLeaves
     * @date 2021/4/19 8:13
     */
    boolean AddUsers(List<SysUserDO> users);

    /**
     * 批量添加个人档案
     * @param personDocDOS
     * @return
     * @author FourLeaves
     * @date 2021/4/19 8:13
     */
    boolean AddPersonDocs(List<BdPersonDocDO> personDocDOS);

    /**
     * 根据角色标识获取用户列表
     * @param code
     * @return 
     * @author FourLeaves
     * @date 2020/12/1 15:29
     */
    List<SysUserDO> getUserListByRoleCode(String code);

    /**
     * 根据工号获取用户
     * @param staffNo
     * @return 
     * @author FourLeaves
     * @date 2020/11/30 11:16
     */
    SysUserDO getUserByStaffNo(String staffNo);

    /**
     * 根据工号列表获取用户列表
     * @param staffNos
     * @return
     * @author FourLeaves
     * @date 2020/11/30 11:16
     */
    List<SysUserDO> getUserListByStaffNos(List<String> staffNos);


    /**
     * 判断用户是否重名
     * @return Integer
     * @author xulei
     * @data
     */
    Integer userRepeat(String userName);


    /**
     * 存入个人档案
     * @param personDoc 个人档案对象，必填(groupId，organizationId，name，cellPhone，code，mainJob.departmentId,user.userName,user.secretLevel)
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author xulei
     * @date 2020/6/30 8.39
     */
    R savePersonAndUser(BdPersonDocDO personDoc);


    /**
     * 根据用户id获取人员档案
     * @param userId
     * @return BdPersonDocDO
     * @author xulei
     * @date 2020/7/14 10:58
     */
     R getBdPersonDoc(String userId);


    /**
     * 通过权限ID获取用户ID
     * @param authId
     * @return 
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    List<String> getUserIdsByAuthId(String authId);

    /**
     * 通过权限ID获取用户
     * @param authId
     * @return
     * @author FourLeaves
     * @date 2020/4/29 14:40
     */
    List<SysUserDO> getUsersByAuthId(String authId);

    /**
     * 获取在线用户数
     *
     * @param
     * @return
     * @author FourLeaves
     * @date 2019/12/11 18:04
     */
    List<SysOnlineUserVO> getOnlineUsers();

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
     * 模糊查询用户列表 用户名 角色名  部门名
     *
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByMatch(String str);

    /**
     * 查询用户列表  用户姓名RealName 用户名 ID
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
     * 根据角色ID获取用户列表
     *
     * @param roleId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByRoleId(String roleId);

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
     * 根据部门ID获取用户列表 depId为0 获取当前用户业务单元下所有用户   depId若为业务单元ID 则查询业务单元下的所有用户
     *
     * @param depId
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersByDepId(String depId);

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
     * 根据密级获取用户列表 true 小于等于
     *
     * @param level
     * @param le
     * @return
     * @author yansiyang
     * @date 2019/8/14 8:23
     */
    List<SysUserDO> getUsersBySecretLevel(Integer level, boolean le);

    /**
     * 根据IDs主键列表 获取用户列表
     *
     * @param ids 用户ID列表
     * @return 用户列表
     * @author JonnyJiang
     * @date 2019/11/6 16:11
     */

    List<SysUserDO> getUsersByIds(List<String> ids);

    /**
     * 获取用户
     *
     * @param id 用户ID
     * @return 用户
     * @author JonnyJiang
     * @date 2019/11/6 16:11
     */

    SysUserDO getUserById(String id);

    /**
     * 获取集团的用户列表
     *
     * @param groupId 集团ID
     * @return 集团的用户列表
     * @author JonnyJiang
     * @date 2019/11/8 10:38
     */

    List<SysUserDO> getUsersByGroupId(String groupId);

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
     * 获取使用该应用的相同集团相同业务单元用户列表
     *
     * @return 集团的用户列表
     * @author JonnyJiang
     * @date 2019/11/8 10:38
     */

    List<SysUserDO> getUseAppUsers();
}
