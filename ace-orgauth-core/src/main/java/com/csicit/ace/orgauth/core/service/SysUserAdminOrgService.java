package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.SysUserAdminOrgDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统管理-用户-可管理的组织 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-15 20:14:22
 * @version V1.0
 */
@Transactional
public interface SysUserAdminOrgService extends IBaseService<SysUserAdminOrgDO> {
    /**
     * 获取指定用户可管理集团的ID列表
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/5/15 14:39
     */
    List<String> getGroupsByUserId(String userId);
}
