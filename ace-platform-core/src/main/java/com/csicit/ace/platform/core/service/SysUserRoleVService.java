package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserRoleVDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 用户角色历史版本数据 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-10-22 19:17:41
 */
@Transactional
public interface SysUserRoleVService extends IService<SysUserRoleVDO> {
    /**
     * 通过userID获取该用户的角色版本历史数据
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<Integer, Object> getRoleHistoryByUserId(String appId, String userId);
}
