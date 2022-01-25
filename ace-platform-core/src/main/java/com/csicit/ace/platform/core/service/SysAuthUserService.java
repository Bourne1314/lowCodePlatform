package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthUserDO;
import com.csicit.ace.common.pojo.domain.SysAuthUserVDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.vo.GrantAuthReciveVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 权限用户关系 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Transactional
public interface SysAuthUserService extends IService<SysAuthUserDO> {


    /**
     * 用户授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 18:28
     */
    boolean saveUserAuth(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 用户授权(激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 18:28
     */
    boolean saveUserAuthActivation(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 权限用户授权(未激活)
     *
     * @param grantAuthReciveVO
     * @return boolean
     * @author zuogang
     * @date 2019/4/17 18:28
     */
    Map<String,Object> saveAuthUserGrant(GrantAuthReciveVO grantAuthReciveVO);

    /**
     * 获取 管理员用户/普通用户在所有应用下的 激活权限
     *
     * @param userId 用户id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    List<SysAuthUserVDO> getActiveAuthUser(String userId);

    /**
     * 获取 普通用户在某应用下的 激活权限
     *
     * @param userId 用户id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    List<SysAuthUserVDO> getActiveAuthUserForApp(String userId, String appId);

    /**
     * 激活当前用户权限
     *
     * @param userId 用户id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysAuthRoleVDO>
     * @author shanwj
     * @date 2019/7/4 20:06
     */
    boolean authUserActivation(String userId, String appId);

    /**
     * 计算当前时间段下的变更过权限的用户列表
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    List<SysUserDO>  getChangeUserList(Map<String, Object> map);

}
