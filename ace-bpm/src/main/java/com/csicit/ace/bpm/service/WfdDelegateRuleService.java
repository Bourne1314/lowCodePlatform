package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateRuleDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/19 15:30
 */
@Transactional
public interface WfdDelegateRuleService extends IService<WfdDelegateRuleDO> {
    /**
     * 获取委托规则
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return 委托规则
     * @author FourLeaves
     * @date 2020/2/19 15:37
     */
    R getDelegateRuleListOfMe(int current, int size);

    /**
     * 获取委托规则
     *
     * @param current 当前页码
     * @param size    分页大小
     * @return 委托规则
     * @author FourLeaves
     * @date 2020/2/19 15:37
     */
    R getDelegateRuleList(int current, int size);

    /**
     * 保存委托规则
     *
     * @param wfdDelegateRuleDO
     * @return 委托规则
     * @author FourLeaves
     * @date 2020/2/19 19:10
     */
    boolean saveDelegateRule(WfdDelegateRuleDO wfdDelegateRuleDO);

    /**
     * 删除委托规则
     *
     * @param ids
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:11
     */
    boolean removeDelegateRules(List<String> ids);

    /**
     * 启用委托规则
     *
     * @param ids
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:11
     */
    boolean startDelegateRules(List<String> ids);

    /**
     * 暂停委托规则
     *
     * @param ids
     * @return
     * @author FourLeaves
     * @date 2020/2/19 19:11
     */
    boolean stopDelegateRules(List<String> ids);

}
