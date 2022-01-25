package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAuthUserVDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 系统管理-用户授权版本历史数据 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Transactional
public interface SysAuthUserVService extends IBaseService<SysAuthUserVDO> {
    /**
     * 通过userID获取该用户的权限版本历史数据
     *
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:40
     */
    Map<Integer, Object> getAuthHistoryByUserId(String appId, String userId);
}
