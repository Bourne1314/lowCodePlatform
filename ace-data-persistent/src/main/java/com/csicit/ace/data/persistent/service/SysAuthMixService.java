package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 有效权限 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthMixService extends IService<SysAuthMixDO> {
    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId 用户id
     * @param authId 权限主键
     * @return 是否有权限
     * @author JonnyJiang
     * @date 2020/7/14 9:43
     */

    boolean hasAuthorityWithUserId(String userId, String authId);

    /**
     * 判断指定用户是否拥有某项权限
     *
     * @param userId 用户id
     * @param authCode 权限标识
     * @param appId
     * @return 是否有权限
     * @author JonnyJiang
     * @date 2020/7/14 9:43
     */

    boolean hasAuthCodeWithUserId(String userId, String authCode, String appId);

}
