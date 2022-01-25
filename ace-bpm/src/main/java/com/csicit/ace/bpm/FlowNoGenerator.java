package com.csicit.ace.bpm;

import com.csicit.ace.bpm.enums.SeqResetRule;
import com.csicit.ace.bpm.pojo.domain.WfdFlowCategoryDO;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.service.WfdFlowCategoryService;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工作文号生成器
 *
 * @author JonnyJiang
 * @date 2019/10/11 14:34
 */
public class FlowNoGenerator {
    private static final Pattern pattern = Pattern.compile("\\{V:(.*?)}");

    /**
     * 类别名称
     */
    private static final String CATEGORY_NAME = "{C}";
    /**
     * 流程名称
     */
    private static final String FLOW_NAME = "{F}";
    /**
     * 年
     */
    private static final String YEAR = "{Y}";
    /**
     * 月
     */
    private static final String MONTH = "{M}";
    /**
     * 日
     */
    private static final String DAY = "{D}";
    /**
     * 流水号
     */
    private static final String SEQ_NO = "{N}";

    private Flow flow;
    private Map<String, Object> variables;

    public FlowNoGenerator(Flow flow, Map<String, Object> variables) {
        this.flow = flow;
        this.variables = variables;
    }

    public String generate(String wfiFlowId) {
        String workNoStyle;
        if (StringUtils.isBlank(flow.getWorkNoStyle())) {
            workNoStyle = "{C}_{F} ({Y}-{M}-{D}:{N})";
        } else {
            workNoStyle = flow.getWorkNoStyle();
        }
        LocalDateTime now = LocalDateTime.now();
        String categoryName = "";
        WfdFlowCategoryDO wfdFlowCategory = SpringContextUtils.getBean(WfdFlowCategoryService.class).getById(flow.getCategoryId());
        if (wfdFlowCategory != null) {
            categoryName = wfdFlowCategory.getName();
        }
        String flowName = flow.getName();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());
        WfdFlowService wfdFlowService = SpringContextUtils.getBean(WfdFlowService.class);
        WfiFlowService wfiFlowService = SpringContextUtils.getBean(WfiFlowService.class);
        wfdFlowService.lockSeq(flow.getId());
        WfdFlowDO wfdFlow = wfdFlowService.getById(flow.getId());
        Integer seqNo;
        if (wfdFlow == null) {
            seqNo = 1;
        } else {
            seqNo = getSeqNo(flow, wfdFlow, now);
            wfdFlowService.updateSeq(flow.getId(), seqNo, now);
        }
        Integer seqNoLength = 1;
        if (flow.getWorkNoSeqLength() != null) {
            seqNoLength = flow.getWorkNoSeqLength();
        }
        String flowNoHeader = workNoStyle
                .replace(CATEGORY_NAME, categoryName == null ? "" : categoryName)
                .replace(FLOW_NAME, flowName == null ? "" : flowName)
                .replace(YEAR, year)
                .replace(MONTH, month)
                .replace(DAY, day)
                .replace(SEQ_NO, String.format("%0" + seqNoLength + "d", seqNo));
        Matcher matcher = pattern.matcher(flowNoHeader);
        while (matcher.find()) {
            String group = matcher.group();
            String variableName = group.substring(3, group.length() - 1);
            if (variables.containsKey(variableName)) {
                flowNoHeader = flowNoHeader.replace(group, String.valueOf(variables.get(variableName)));
            }
        }
        return wfiFlowService.getAvailableFlowNo(flow.getCode(), wfiFlowId, flowNoHeader);
    }

    private Integer getSeqNo(Flow flow, WfdFlowDO wfdFlow, LocalDateTime now) {
        Integer seqNo = 1;
        if (SeqResetRule.NEVER.isEquals(flow.getWorkNoSeqResetRule())) {
            return wfdFlow.getSeqNo();
        } else if (wfdFlow.getLatestCreateTime() != null) {
            if (SeqResetRule.YEAR.isEquals(flow.getWorkNoSeqResetRule())) {
                if (now.getYear() == wfdFlow.getLatestCreateTime().getYear()) {
                    return wfdFlow.getSeqNo();
                }
            } else if (SeqResetRule.MONTH.isEquals(flow.getWorkNoSeqResetRule())) {
                if (now.getMonthValue() == wfdFlow.getLatestCreateTime().getMonthValue()) {
                    return wfdFlow.getSeqNo();
                }
            } else if (SeqResetRule.DAY.isEquals(flow.getWorkNoSeqResetRule())) {
                if (now.getDayOfMonth() == wfdFlow.getLatestCreateTime().getDayOfMonth()) {
                    return wfdFlow.getSeqNo();
                }
            }
        }
        return seqNo;
    }
}