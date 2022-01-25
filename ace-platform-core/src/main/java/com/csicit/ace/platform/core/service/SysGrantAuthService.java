package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import com.csicit.ace.common.pojo.vo.GrantAuthVO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 授权管理 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:26
 */
@Transactional
public interface SysGrantAuthService extends IBaseService<GrantAuthReciveVO> {
    /**
     * 权限授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 18:28
     */
    boolean saveUserRoleAuth(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 获取用户授权信息
     *
     * @param userId 用户id
     * @param appId  应用id
     * @return java.util.List<com.csicit.ace.common.pojo.vo.GrantAuthVO>
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    List<GrantAuthVO> getUserAuths(String userId, String appId);

    /**
     * 获取角色授权信息
     *
     * @param roleId 角色id
     * @param appId  应用id
     * @return java.util.List<com.csicit.ace.common.pojo.vo.GrantAuthVO>
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    List<GrantAuthVO> getRoleAuths(String roleId, String appId);

    /**
     * 获取权限用户角色列表
     *
     * @param authId 权限ID
     * @param appId  应用ID
     * @return com.csicit.ace.common.pojo.vo.GrantAuthReciveVO
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    GrantAuthReciveVO getUsersAndRoles(String authId, String appId);

    /**
     * 获取该应用管理员所拥有的应用权限列表
     *
     * @param appId 应用ID
     * @return
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    List<SysAuthDO> getAppAuths(String appId);


    /**
     * 获取待激活的用户列表和角色列表
     *
     * @param appId 应用ID
     * @return
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    GrantAuthReciveVO waitActivationUserAndRoleList(String appId);

    /**
     * 获取当前应用管理员需要激活权限的用户和角色个数
     *
     * @return
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    GrantAuthReciveVO waitActivationUserAndRoleCount();

    /**
     * 一键权限激活
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 18:28
     */
    boolean saveAllActivation(GrantAuthReciveVO grantAuthReciveVO);

}
