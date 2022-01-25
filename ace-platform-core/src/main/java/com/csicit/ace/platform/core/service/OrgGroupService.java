package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 集团管理 实例对象访问接口
 *
 * @author yansiyang
 * @versiond V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface OrgGroupService extends IBaseService<OrgGroupDO>, BaseOrgService {

    /**
     * 移动集团
     * @param mvToTop  是否设置为顶级集团
     * @param id  要移动的集团主键
     * @param targetGroupId  目标集团主键
     * @return
     * @author yansiyang
     * @date 2019/11/21 17:00
     */
    boolean mvGroup(boolean mvToTop, String id, String targetGroupId);
    /**
     * 修改集团
     *
     * @param group 集团对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:53
     */
    boolean updateGroup(OrgGroupDO group);

    /**
     * 保存集团
     *
     * @param group 集团对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:51
     */
    boolean saveGroup(OrgGroupDO group);

    /**
     * 保存集团
     *
     * @param group 集团对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:51
     */
     R saveGroupR(OrgGroupDO group);

    /**
     * 删除集团
     *
     * @param params 集团主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:56
     */
     R deleteGroup(Map<String, Object> params);

    /**
     * 获取用户的集团管控范围
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/6/3 14:14
     */
    List<OrgGroupDO> getGroupsByUserId(String userId);

    /**
     * 获取集团列表（普通用户所在集团对应的最顶级集团的整个集团树）
     *
     * @param groupId 用户集团ID
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:51
     */
    List<OrgGroupDO> listForOrdinaryUser(String groupId);
}
