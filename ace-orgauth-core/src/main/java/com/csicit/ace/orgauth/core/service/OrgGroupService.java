package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 集团管理 实例对象访问接口
 *
 * @author yansiyang
 * @versiond V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface OrgGroupService extends IBaseService<OrgGroupDO> {
    /**
     * 获取用户的集团管控范围
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/6/3 14:14
     */
    List<OrgGroupDO> getGroupsByUserId(String userId);

}

