package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 有效权限 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysAuthMixService extends IBaseService<SysAuthMixDO> {
    /**
     * 查询指定用户的所有权限并根据app分类
     *
     * @param userId
     * @return
     * @author zuogang
     * @date 2019/4/22 15:34
     */

    Map<String, List> getAuthWithAppByUserId(String userId);

    /**
     * 计算普通用户在某应用下的有效权限
     *
     * @param userId 授权用户id
     * @param appId 应用ID
     * @author shanwj
     * @date 2019/4/19 15:19
     */
    boolean saveAuthMixForApp(String userId, String appId);


    /**
     * 计算管理员用户有效权限 / 普通用户在所有应用下的所有权限
     *
     * @param userId 授权用户id
     * @author shanwj
     * @date 2019/4/19 15:19
     */
    boolean saveAuthMix(String userId);

}
