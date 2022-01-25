package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.mapper.WfdDelegateUserMapper;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateRuleDO;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateUserDO;
import com.csicit.ace.bpm.service.WfdDelegateRuleService;
import com.csicit.ace.bpm.service.WfdDelegateUserService;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/19 15:31
 */
@Service("wfdDelegateUserService")
public class WfdDelegateUserServiceImpl extends BaseServiceImpl<WfdDelegateUserMapper, WfdDelegateUserDO> implements WfdDelegateUserService {

    @Autowired
    WfdDelegateRuleService wfdDelegateRuleService;

    @Autowired
    SecurityUtils securityUtils;

    @Override
    public String getEffectiveDelegateUserId(String flowId, String userId) {
        Set<String> originUserIds = new HashSet<>();
        originUserIds.add(userId);
        return getEffectiveDelegateUserIdRecursive(flowId, userId, originUserIds);
    }

    /**
     * 递归获取被委托人用户主键
     *
     * @param flowId
     * @param userId        第一个委托人
     * @param originUserIds 委托人递归字符串 防止出现循环递归  A-B-C-A
     * @return
     * @author FourLeaves
     * @date 2020/2/24 10:12
     */
    private String getEffectiveDelegateUserIdRecursive(String flowId, String userId, Set<String> originUserIds) {
        WfdDelegateUserDO delegateUser = getEffectiveDelegateUser(flowId, userId);
        if (delegateUser == null) {
            return null;
        } else {
            if (!originUserIds.contains(delegateUser.getUserId())) {
                originUserIds.add(delegateUser.getUserId());
                String delegateUserId = getEffectiveDelegateUserIdRecursive(flowId, delegateUser.getUserId(), originUserIds);
                if (StringUtils.isNotBlank(delegateUserId)) {
                    return delegateUserId;
                }
            } else {
                return null;
            }
        }
        return delegateUser.getUserId();
    }


    /**
     * 获取指定用户的制定流程的委托人
     *
     * @param flowId
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/2/24 12:14
     */
    private WfdDelegateUserDO getEffectiveDelegateUser(String flowId, String userId) {
        LocalDate today = LocalDate.now();
        if (StringUtils.isNotBlank(flowId) && StringUtils.isNotBlank(userId)) {
            List<WfdDelegateRuleDO> rules = wfdDelegateRuleService.list(new QueryWrapper<WfdDelegateRuleDO>()
                    .eq("process_id", flowId)
                    .eq("used_flag", 1)
                    .eq("app_id", appName)
                    .eq("delegate_user_id", userId).and(j -> j.or(i -> i.isNull("start_date").isNull("end_date"))
                            .or(i -> i.isNotNull("start_date").isNull("end_date")
                                    .le("start_date", today))
                            .or(i -> i.isNull("start_date").isNotNull("end_date")
                                    .ge("end_date", today))
                            .or(i -> i.isNotNull("start_date").isNotNull("end_date")
                                    .le("start_date", today)
                                    .ge("end_date", today))));
            if (CollectionUtils.isNotEmpty(rules)) {
                return getOne(new QueryWrapper<WfdDelegateUserDO>().in("rule_id",
                        rules.stream().map(WfdDelegateRuleDO::getId).collect(Collectors.toList())));
            }
        }
        return null;
    }
}
