package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Map;

/**
 * 工作流用接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/10/15 8:26
 */
public interface IWfdFlow {

    /**
     * 根据相关条件获取指定用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/10/15 8:27
     */
    List<SysUserDO> getSomeUsers(String code, List<String> ids, Map<String, Object> map);

    /**
     * 获取登录用户在该应用下的有效权限列表
     */
    List<SysAuthDO> getMixAuth(String appId, String userId);

    /**
     * 在应用启动时检查应用菜单和权限是否包含了工作流的菜单和权限，没有的话就新增相应的权限和菜单
     *
     * @param
     * @return void
     * @author zuogang
     * @date 2020/4/29 9:08
     */
    boolean setAppFLowMenu(String token);
}
