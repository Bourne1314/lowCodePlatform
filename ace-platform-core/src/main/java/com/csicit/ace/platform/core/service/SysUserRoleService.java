package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.common.pojo.vo.ThreeAdminVO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户角色关系 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysUserRoleService extends IService<SysUserRoleDO> {

    /**
     * 删除集团三员管理
     *
     * @param threeAdminVO
     * @return
     * @author zuogang
     * @date 2019/4/23 16:55
     */
    boolean deleteGroupThreeRole(ThreeAdminVO threeAdminVO);


    /**
     * 删除应用三员管理
     *
     * @param threeAdminVO
     * @return
     * @author zuogang
     * @date 2019/4/23 16:55
     */
    boolean deleteAppThreeRole(ThreeAdminVO threeAdminVO);


    /**
     * 通过UserId获取该用户所拥有的应用三员授控域
     *
     * @param userId
     * @param status
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    SysUserDO getAppThreeDoMain(String userId, String status);


    /**
     * 获取集团级待激活人员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO> getGroupToBeActivatedData();

    /**
     * 激活集团管理员权限
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean setGroupActivated(Map<String, String> map);

    /**
     * 获取未分配人员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO> getUnallocatedData(Map<String, Object> map);

    /**
     * 添加三员管理成员
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean saveThreeRole(Map<String, Object> map);

    /**
     * 修改三员管理成员
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    R updateThreeRole(Map<String, Object> map);

    /**
     * 获取已激活的集团三员管理员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<String, Object> getGroupAllocatedData();

    /**
     * 获取已激活和未激活的集团三员管理员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<String, Object> getGroupAllThreeData();

    /**
     * 获取应用级待激活人员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO> getAppToBeActivatedData(Map<String, Object> params);

    /**
     * 激活应用管理员权限
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean setAppActivated(Map<String, String> map);

    /**
     * 获取已激活的应用三员管理员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<String, Object> getAppAllocatedData(Map<String, Object> map);

    /**
     * 获取已激活和未激活的应用三员管理员列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<String, Object> getAppAllThreeData(Map<String, Object> params);

    /**
     * 通过用户ID获取角色信息(激活与未激活)
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysRoleDO> getRolesByUserId(Map<String, Object> params);

    /**
     * 通过角色ID获取有效用户信息
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO> getActivedUsersByRoleId(Map<String, Object> params);

    /**
     * 通过角色ID获取用户信息
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO> getUsersByRoleId(Map<String, Object> params);

    /**
     * 用户指派角色
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    R saveUserIdAndRoleIds(Map<String, Object> map);

    /**
     * 角色指派用户
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<String, Object> saveRoleIdAndUserIds(Map<String, Object> map);

    /**
     * 全局角色激活用户关系
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean scopeRoleUserActive(String userId,List<String> appIds);

    /**
     * 全局角色激活用户
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean activeScopeRoleIdAndUserIds(Map<String, Object> map);

    /**
     * 部门关联角色指派用户
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    R saveRoleIdAndUserIdsForDep(SysRoleDO role, List<SysUserDO> userDOS);

    /**
     * 获取未被激活的用户角色信息
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<String, Object> getWaitActiveUserData(Map<String, Object> params);

    /**
     * 用户角色关系激活
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean activeByUserId(Map<String, Object> map);


    /**
     * 通过用户ID获取有效角色信息
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysRoleDO> getEffectiveRoleData(String userId, String appId);

    /**
     * 通过角色IDs获取有效用户信息
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<String> getEffectiveUserDatas(List<String> roleIds);

    /**
     * 通过角色ID获取有效用户信息
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<String> getEffectiveUserData(String roleId);

    /**
     * 一键激活用户角色
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean allActive(Map<String, Object> map);

    /**
     * 计算当前时间段下的变更过角色的用户列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO> getChangeRoleUserList(Map<String, Object> map);

    /**
     * 刷新应用三员用户有效权限
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    boolean refreshAppThreeAuth(List<String> roleIds);

}


