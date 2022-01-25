package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.domain.SysUserGroupDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统管理-用户组 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:36
 */
@Transactional
public interface SysUserGroupService extends IBaseService<SysUserGroupDO> {
    /**
     * 更新用户组
     *
     * @param sysUserGroupDO
     * @return
     * @author yansiyang
     * @date 2019/4/16 11:50
     */
    boolean update(SysUserGroupDO sysUserGroupDO);

    /**
     * 保存用户组
     *
     * @param sysUserGroupDO
     * @return
     * @author yansiyang
     * @date 2019/4/16 11:50
     */
    boolean saveUserGroup(SysUserGroupDO sysUserGroupDO);

    /**
     * 删除用户组
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/5/15 9:21
     */
    R delete(Map<String, Object> map);

    /**
     * 从用户组删除用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/5/15 9:21
     */
    R deleteUser(Map<String, Object> map);

    /**
     * 往用户组添加用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/5/15 9:30
     */
    R addUser(Map<String, Object> map);

    /**
     * 通过应用集合获取用户组列表
     *
     * @param sysUserDO 用户信息
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:34
     */
    List<SysUserGroupDO> getUserGroupsByApps(SysUserDO sysUserDO);

}
