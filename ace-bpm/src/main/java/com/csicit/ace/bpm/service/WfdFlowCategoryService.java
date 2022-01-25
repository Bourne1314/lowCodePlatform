package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfdFlowCategoryDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2019/9/3 15:40
 */
@Transactional
public interface WfdFlowCategoryService extends IBaseService<WfdFlowCategoryDO> {
    /**
     * 存在判断
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    String existCheck(WfdFlowCategoryDO instance);
}
