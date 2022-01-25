package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色关系管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleRelationService extends IService<SysRoleRelationDO> {

    /**
     * 添加下级角色
     *
     * @param id   当前角色id
     * @param cids 下级角色id集合
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:52
     */
    boolean saveChildRoles(String id, List<String> cids);

    /**
     * 添加上级角色
     *
     * @param id     当前角色id
     * @param pids 上级角色id集合
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:52
     */
    boolean saveParentRoles(String id, List<String> pids);

    /**
     * 获取当前角色的所有上级父节点至最上层
     *
     * @param id 当前角色id
     * @return 所有上级角色集合
     * @author shanwj
     * @date 2019/4/19 14:21
     */
    List<String> getAllSuperRoleIds(String id);

    /**
     * 获取当前角色集合的所有上级父节点至最上层
     *
     * @param ids 当前角色集合ids
     * @return 所有上级角色集合
     * @author shanwj
     * @date 2019/4/19 14:21
     */
    List<String> getAllSuperRoleIds(List<String> ids);
}
