package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfiDeliverDO;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/9/5 17:59
 */
@Transactional
public interface WfiDeliverService extends IBaseService<WfiDeliverDO> {
    /**
     * 获取转交信息列表(按办理时间正序排序)
     *
     * @param flowId 流程实例ID
     * @return 转交信息列表
     * @author JonnyJiang
     * @date 2019/11/13 16:26
     */

    List<WfiDeliverDO> listByFlowId(String flowId);

    /**
     * 更新转交信息
     *
     * @param wfiDeliverId 转交信息id
     * @param deliverInfo  转交信息
     * @author JonnyJiang
     * @date 2019/12/31 11:28
     */

    void updateDeliverInfo(String wfiDeliverId, DeliverInfo deliverInfo);
}
