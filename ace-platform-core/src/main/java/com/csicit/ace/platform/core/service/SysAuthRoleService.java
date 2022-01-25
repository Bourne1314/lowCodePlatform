package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthRoleDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 权限角色 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthRoleService extends IService<SysAuthRoleDO> {


    /**
     * 角色授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:23
     */
    boolean saveRoleAuth(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 角色授权(激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/5/5 11:23
     */
    boolean saveRoleAuthActivation(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 角色授权(激活)--角色权限历史数据更新
     *
     * @param appId
     * @param roleId
     * @param roleName
     * @return boolean
     * @author zuogang
     * @date 2019/5/5 11:23
     */
    boolean saveRoleAuthHistory(String appId, String roleId, String roleName);

    /**
     * 角色授权(激活)--相关用户有效权限更新计算
     *
     * @param roleIds
     * @return boolean
     * @author zuogang
     * @date 2019/5/5 11:23
     */
    boolean saveRoleActivateForUserValidAuth(List<String> roleIds, String appId);

    /**
     * 权限角色授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return 
     * @author zuogang
     * @date 2019/4/12 11:23
     */
    Map<String,Object> saveAuthRoleGrant(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 获取当前角色的激活权限
     *
     * @param roleIds 角色id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    List<SysAuthRoleVDO> getActiveAuthRolesForAppId(List<String> roleIds, String appId);

    /**
     * 获取当前角色的激活权限
     *
     * @param roleIds 角色id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    List<SysAuthRoleVDO> getActiveAuthRoles(List<String> roleIds);


    /**
     * 获取当前角色的激活权限
     *
     * @param roleId 角色id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    List<SysAuthRoleVDO> getActiveAuthRole(String roleId);

    /**
     * 角色权限激活
     *
     * @param roleId 角色id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    boolean authRoleActivation(String roleId,String appId);

    /**
     * 计算当前时间段下的变更过权限的角色列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysRoleDO>  getChangeRoleList(Map<String, Object> map);
}
