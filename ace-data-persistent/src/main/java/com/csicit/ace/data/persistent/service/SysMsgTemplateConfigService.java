package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.SysMsgTemplateConfigDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 *  访问接口
 *
 * @author shanwj
 * @date 2020/4/7 9:38
 */
@Transactional
public interface SysMsgTemplateConfigService extends IBaseService<SysMsgTemplateConfigDO> {
    /**
     * 保存消息信息配置
     * @param instance
     * @return
     */
    R saveTemplateConfig(SysMsgTemplateConfigDO instance);

    /**
     * 修改消息信息配置
     * @param instance
     * @return
     */
    R updateTemplateConfig(SysMsgTemplateConfigDO instance);

}
