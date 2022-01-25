package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 权限管理 实例对象访问接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysAuthService extends IBaseService<SysAuthDO> {
    /**
     * 通过ID获取权限信息
     *
     * @param id
     * @return com.csicit.ace.common.pojo.domain.SysAuthDO
     * @author zuogang
     * @date 2019/5/16 16:00
     */
    R infoAuth(String id);

    /**
     * 保存权限
     *
     * @param auth
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:24
     */
    boolean saveAuth(SysAuthDO auth);

    /**
     * 修改权限
     *
     * @param auth
     * @return boolean
     * @author zuogang
     * @date 2019/4/12 11:24
     */
    boolean updateAuth(SysAuthDO auth);

    /**
     * 删除权限
     * 根据提供的id集合进行删除
     *
     * @param ids 权限对象
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 15:54
     */
    boolean deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 通过UserId获取用户有效权限列表
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return List<SysAuthDO>
     * @author shanwj
     * @date 2019/5/17 11:15
     */
    List<SysAuthDO> queryUserAuthMix(String userId, String appId);

//    /**
//     * 修改有效权限组织授控域
//     *
//     * @param grantAuthReciveVO
//     * @return
//     * @author shanwj
//     * @date 2019/5/17 11:15
//     */
//    boolean saveAuthMixOrgControlDomain(GrantAuthReciveVO grantAuthReciveVO);

//    /**
//     * 修改有效权限用户组授控域
//     *
//     * @param grantAuthReciveVO
//     * @return
//     * @author shanwj
//     * @date 2019/5/17 11:15
//     */
//    boolean saveAuthMixUserGroupControlDomain(GrantAuthReciveVO grantAuthReciveVO);


    /**
     * 获取该权限的所有上级权限
     *
     * @param parentAuthList
     * @param id
     */
    void getParentAuths(List<SysAuthDO> parentAuthList, String id);

    /**
     * 获取该权限的所有下级权限
     *
     * @param id
     * @param childAuthList
     */
    void getChildAuths(String id, List<SysAuthDO> childAuthList);

    /**
     * 移动菜单
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/5/15 8:58
     */
    R moveAuth(Map<String, Object> params);

}
