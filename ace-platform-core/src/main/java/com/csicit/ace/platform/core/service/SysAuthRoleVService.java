package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 系统管理-角色授权版本历史数据 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:24
 */
@Transactional
public interface SysAuthRoleVService extends IBaseService<SysAuthRoleVDO> {
    /**
     * 通过角色ID获取该用户的权限版本历史数据
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<Integer, Object> getAuthHistoryByRoleId(String roleId);
}
