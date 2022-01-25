package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.DevUserTokenDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Token管理 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface DevUserTokenService extends IBaseService<DevUserTokenDO> {
    /**
     * 生成token
     *
     * @param userId 用户ID
     */
    R createToken(String userId);

    /**
     * 退出，修改token值
     *
     * @param userId 用户ID
     */
    void logout(String userId);
}
