package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.mapper.WfdDelegateRuleMapper;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateRuleDO;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateUserDO;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.service.WfdDelegateRuleService;
import com.csicit.ace.bpm.service.WfdDelegateUserService;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.interfaces.service.IUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/19 15:32
 *
 */
@Service("wfdDelegateRuleService")
public class WfdDelegateRuleServiceImpl extends BaseServiceImpl<WfdDelegateRuleMapper, WfdDelegateRuleDO> implements WfdDelegateRuleService {
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private WfdDelegateUserService wfdDelegateUserService;
    @Autowired
    private WfdFlowService wfdFlowService;
    @Autowired
    IUser iUser;

    @Override
    public R getDelegateRuleListOfMe(int current, int size) {
        String currentUserId = securityUtils.getCurrentUserId();
        Page<WfdDelegateUserDO> page = new Page<>(current, size);
        IPage data = wfdDelegateUserService.page(page, new QueryWrapper<WfdDelegateUserDO>()
                .eq("USER_ID", currentUserId).orderByDesc("create_time"));
        List<WfdDelegateUserDO> delegateUsers = data.getRecords();
        if (CollectionUtils.isNotEmpty(delegateUsers)) {
            List<String> ruleIds = delegateUsers.stream().map(WfdDelegateUserDO::getRuleId).collect(Collectors.toList());
            List<WfdDelegateRuleDO> rules = list(new QueryWrapper<WfdDelegateRuleDO>().in("id", ruleIds)
                    .orderByDesc("create_time"));
            List<String> userIds = rules.stream().map(WfdDelegateRuleDO::getDelegateUserId).collect(Collectors.toList());
            List<SysUserDO> userDOS = iUser.getUsersByIds(userIds);
            Map<String, String> idAndNames = new HashMap<>();
            userDOS.forEach(user -> {
                idAndNames.put(user.getId(), user.getRealName());
            });
            for (WfdDelegateRuleDO rule : rules) {
                rule.setUserName(idAndNames.get(rule.getDelegateUserId()));
                rule.setUsedFlagStr(rule.getUsedFlag() == 0 ? "否" : "是");
            }
            return R.ok().put("records", rules).put("total", data.getTotal());
        }
        return R.ok();
    }

    @Override
    public R getDelegateRuleList(int current, int size) {
        String currentUserId = securityUtils.getCurrentUserId();
        Page<WfdDelegateRuleDO> page = new Page<>(current, size);
        IPage data = page(page, new QueryWrapper<WfdDelegateRuleDO>()
                .eq("DELEGATE_USER_ID", currentUserId).eq("app_id", appName).orderByDesc("create_time"));
        List<WfdDelegateRuleDO> rules = data.getRecords();
        if (CollectionUtils.isNotEmpty(rules)) {
            // 填充用姓名
            List<String> delegateIds = rules.stream().map(WfdDelegateRuleDO::getId).collect(Collectors.toList());
            List<WfdDelegateUserDO> delegateUserDOS = wfdDelegateUserService.list(new QueryWrapper<WfdDelegateUserDO>()
                    .in("rule_id", delegateIds).orderByAsc("create_time"));
            for (WfdDelegateRuleDO rule : rules) {
                String names = delegateUserDOS.stream().filter(user -> Objects.equals(user.getRuleId(), rule.getId())).map(WfdDelegateUserDO::getRealName).collect(Collectors.toList()).toString();
                if (names.length() > 2) {
                    rule.setDelegatedUserNames(names.substring(1, names.length() - 1));
                }
                rule.setUsedFlagStr(rule.getUsedFlag() == 0 ? "否" : "是");
            }
        }
        return R.ok().put("records", rules).put("total", data.getTotal());
    }


    @Override
    public boolean saveDelegateRule(WfdDelegateRuleDO wfdDelegateRuleDO) {
        String id = UuidUtils.createUUID();
        String currentUserId = securityUtils.getCurrentUserId();
        int usedFlag = wfdDelegateRuleDO.getUsedFlag();
        LocalDate startDate = wfdDelegateRuleDO.getStartDate();
        LocalDate endDate = wfdDelegateRuleDO.getEndDate();

        wfdDelegateRuleDO.setId(id);
        wfdDelegateRuleDO.setAppId(appName);
        wfdDelegateRuleDO.setDelegateUserId(currentUserId);
        wfdDelegateRuleDO.setCreateTime(LocalDateTime.now());

        String processType = wfdDelegateRuleDO.getProcessType();
        List<WfdDelegateRuleDO> ruleDOS = new ArrayList<>();

        // 覆盖之前的委托规则
        Set<String> flowIds = new HashSet<>();

        // 全部菜单
        if (Objects.equals("0", processType)) {
            List<WfdFlowDO> list = wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                    .eq("app_id", appName));
            for (WfdFlowDO flowDO : list) {
                WfdDelegateRuleDO ruleDO = new WfdDelegateRuleDO();
                ruleDO.setId(UuidUtils.createUUID());
                ruleDO.setProcessId(flowDO.getId());
                flowIds.add(flowDO.getId());
                ruleDO.setProcessName(flowDO.getName());
                ruleDO.setAppId(appName);
                ruleDO.setDelegateUserId(currentUserId);
                ruleDO.setUsedFlag(usedFlag);
                ruleDO.setStartDate(startDate);
                ruleDO.setEndDate(endDate);
                ruleDO.setCreateTime(LocalDateTime.now());
                ruleDOS.add(ruleDO);
            }
        } else if (Objects.equals("1", processType)) {
            // 工作类别
            List<WfdFlowDO> list = wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                    .eq("category_id", wfdDelegateRuleDO.getProcessId()));
            for (WfdFlowDO flowDO : list) {
                WfdDelegateRuleDO ruleDO = new WfdDelegateRuleDO();
                ruleDO.setId(UuidUtils.createUUID());
                ruleDO.setProcessId(flowDO.getId());
                flowIds.add(flowDO.getId());
                ruleDO.setProcessName(flowDO.getName());
                ruleDO.setAppId(appName);
                ruleDO.setDelegateUserId(currentUserId);
                ruleDO.setUsedFlag(usedFlag);
                ruleDO.setStartDate(startDate);
                ruleDO.setEndDate(endDate);
                ruleDO.setCreateTime(LocalDateTime.now());
                ruleDOS.add(ruleDO);
            }
        } else {
            // 单个
            ruleDOS.add(wfdDelegateRuleDO);
            flowIds.add(wfdDelegateRuleDO.getProcessId());
        }
        if (CollectionUtils.isNotEmpty(flowIds)) {
            int count = count(new QueryWrapper<WfdDelegateRuleDO>().in("process_id", flowIds)
                    .eq("delegate_user_id", securityUtils.getCurrentUserId())
                    .eq("app_id", appName));
            if (count > 0 && !remove(new QueryWrapper<WfdDelegateRuleDO>().in("process_id", flowIds)
                    .eq("delegate_user_id", securityUtils.getCurrentUserId())
                    .eq("app_id", appName))) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }
        if (CollectionUtils.isNotEmpty(ruleDOS)) {
            if (saveBatch(ruleDOS)) {
                List<WfdDelegateUserDO> userDOS = new ArrayList<>();
                for (WfdDelegateRuleDO wfdDelegateRuleDOT : ruleDOS) {
                    for (WfdDelegateUserDO wfdDelegateUser : wfdDelegateRuleDO.getDelegatedUsers()) {
                        WfdDelegateUserDO wfdDelegateUserDO = new WfdDelegateUserDO();
                        wfdDelegateUserDO.setRuleId(wfdDelegateRuleDOT.getId());
                        wfdDelegateUserDO.setId(UuidUtils.createUUID());
                        wfdDelegateUserDO.setUserId(wfdDelegateUser.getUserId());
                        wfdDelegateUserDO.setRealName(wfdDelegateUser.getRealName());
                        wfdDelegateUserDO.setCreateTime(LocalDateTime.now());
                        //  不可以委托给自己
                        if (Objects.equals(wfdDelegateUser.getUserId(), currentUserId)) {
                            throw new RException("不可以委托给自己！");
                        }
                        userDOS.add(wfdDelegateUserDO);
                    }
                }
                if (CollectionUtils.isEmpty(userDOS)) {
                    throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "被委托人"));
                }
                if (!wfdDelegateUserService.saveBatch(userDOS)) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeDelegateRules(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            // 外键删除被委托人表数据
            return removeByIds(ids);
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "规则主键"));
    }

    @Override
    public boolean startDelegateRules(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return update(new WfdDelegateRuleDO(), new UpdateWrapper<WfdDelegateRuleDO>()
                    .in("id", ids).set("used_flag", 1));
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "规则主键"));
    }

    @Override
    public boolean stopDelegateRules(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return update(new WfdDelegateRuleDO(), new UpdateWrapper<WfdDelegateRuleDO>()
                    .in("id", ids).set("used_flag", 0));
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "规则主键"));
    }
}
