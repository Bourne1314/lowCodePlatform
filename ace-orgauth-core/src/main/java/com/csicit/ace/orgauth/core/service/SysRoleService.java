package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleService extends IService<SysRoleDO> {


    /**
     * 通过角色编码 判断用户是否有此角色
     * @param roleCode
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    boolean hasRoleByRoleCode(String roleCode, String appId);

    /**
     * 通过角色主键 判断用户是否有此角色
     * @param roleId
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    boolean hasRoleByRoleId(String roleId, String appId);

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    List<SysRoleDO> getRolesByUserId( String userId);

    /**
     * 获取指定集团的所有角色
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/8/14 15:12
     */
    List<SysRoleDO> getRolesByGroupId(String groupId);
}
