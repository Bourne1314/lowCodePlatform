package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfiRoutePresetDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/8/11 16:22
 */
@Transactional
public interface WfiRoutePresetService extends IBaseService<WfiRoutePresetDO> {
    /**
     * 获取流程流转预设信息
     *
     * @param taskId 任务id
     * @return 流程流转预设信息
     * @author JonnyJiang
     * @date 2020/8/28 17:02
     */

    WfiRoutePresetDO getByTaskId(String taskId);

    /**
     * 获取流程预设列表
     * @param flowId 流程实例id
     * @return 流程预设列表
     */
    List<WfiRoutePresetDO> listByFlowId(String flowId);

    /**
     * 按流程实例计数
     * @param flowId	流程实例id
     * @return 流程实例计数
     * @author JonnyJiang
     * @date 2021/8/22 17:37
     */

    Integer countByFlowId(String flowId);
}
