package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfiVFlowDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2020/8/11 16:22
 */
@Transactional
public interface WfiVFlowService extends IBaseService<WfiVFlowDO> {
    /**
     * 第一个流程版本号
     */
    Integer FIRST_FLOW_VERSION = 1;

    /**
     * 获取流程实例版本
     *
     * @param flowId 流程实例ID
     * @return 流程实例版本
     * @author JonnyJiang
     * @date 2020/8/25 15:08
     */

    Integer getFlowVersionByFlowId(String flowId);
}
