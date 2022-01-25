package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfdFlowAgentDO;
import com.csicit.ace.bpm.pojo.vo.FlowAgentDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作代办规则 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Transactional
public interface WfdFlowAgentService extends IBaseService<WfdFlowAgentDO> {

    /**
     * 保存代办规则
     *
     * @param flowAgentDO
     * @return
     * @author zuogang
     * @date 2019/9/17 9:14
     */
    boolean saveAgent(FlowAgentDO flowAgentDO);

    /**
     * 启用委托规则
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2019/9/17 9:14
     */
    boolean enableRule(String[] ids);

    /**
     * 禁用委托规则
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2019/9/17 9:14
     */
    boolean disableRule(String[] ids);
}
