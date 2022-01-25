package com.csicit.ace.bpm.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.el.WfdFlowElService;
import com.csicit.ace.bpm.enums.DataType;
import com.csicit.ace.bpm.enums.OperandClass;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Rule;
import com.csicit.ace.bpm.pojo.vo.wfd.Variant;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:30
 */
@Component
public class RuleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleUtils.class);
    @Autowired
    WfdFlowElService elService;

    @Autowired
    ExpressionUtils expressionUtils;

    @Autowired
    HttpSession session;

    @Autowired
    WfdFlowService wfdFlowService;

    /**
     * 计算数据库的规则
     *
     * @param ruleId
     * @param satisfy
     * @return
     * @author yansiyang
     * @date 2019/8/23 17:32
     */
    public boolean calculateRule(Flow flow, String ruleId, boolean satisfy) {
        Rule rule = null;
        if (FlowUtils.isGlobalRule(ruleId)) {
            ruleId = FlowUtils.getGlobalVariantId(ruleId);
            rule = FlowUtils.getGlobalRuleById(ruleId);
        } else if (FlowUtils.isFlowRule(ruleId)) {
            ruleId = FlowUtils.getFlowRuleId(ruleId);
            rule = flow.getRuleById(ruleId);
        }
        if (rule == null) {
            return false;
        }
        return getRuleValue(flow, rule, satisfy);
    }

    /**
     * 计算规则
     *
     * @param flow
     * @param ruleExpression 规则表达式
     * @return
     */
    public boolean calculateRule(Flow flow, String ruleExpression) {
        if (StringUtils.isBlank(ruleExpression)) {
            return true;
        } else {
            return calculateRule(flow, JsonUtils.castObject(ruleExpression, RuleDO.class));
        }
    }

    /**
     * 计算规则
     *
     * @param rule
     * @return
     * @author yansiyang
     * @date 2019/8/23 17:32
     */
    public boolean calculateRule(Flow flow, RuleDO rule) {
        try {
            String type = rule.getType();
            if (Objects.equals(type, "and/or")) {
                String operator = rule.getOperator();
                if (rule.getParts().size() > 0) {
                    if (Objects.equals(operator, "and")) {
                        boolean result = true;
                        List<RuleDO> parts = rule.getParts();
                        for (int i = 0; i < parts.size(); i++) {
                            RuleDO part = parts.get(i);
                            boolean resultT = calculateRule(flow, part);
                            if (!resultT) {
                                result = false;
                                break;
                            }
                        }
                        return result;
                    } else {
                        boolean result = false;
                        List<RuleDO> parts = rule.getParts();
                        for (int i = 0; i < parts.size(); i++) {
                            RuleDO part = parts.get(i);
                            boolean resultT = calculateRule(flow, part);
                            if (resultT) {
                                result = true;
                                break;
                            }
                        }
                        return result;
                    }
                }
            } else if (Objects.equals(type, "expr")) {
                return compareVars(flow, rule);
            } else if (Objects.equals(type, "rule")) {
                boolean satisfy = rule.isSatisfy();
                return calculateRule(flow, rule.getRuleId(), satisfy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.toString());
            return false;
        }
        return false;
    }

    /**
     * 计算表达式的值 比较var1和var2
     *
     * @param rule
     * @return
     * @author yansiyang
     * @date 2019/8/26 12:00
     */
    private boolean compareVars(Flow flow, RuleDO rule) {
        // 获取变量1
        // 判断是全局变量还是流程变量
        if (StringUtils.isBlank(rule.getVar1())) {
            return false;
        }
        Variant variant = FlowUtils.getVariant(rule.getVar1(), flow);
        session.setAttribute(SessionAttribute.WFD_FLOW_EL_FLOW, flow);
        ValueClassDO var1 = setValue(variant);
        String operator = rule.getOperator();
        if (Operator.IS_NULL.equals(operator) || Operator.IS_NOT_NULL.equals(operator)) {
            if (Operator.IS_NULL.equals(operator)) {
                return StringUtils.isBlank(var1.getValue());
            } else {
                return StringUtils.isNotBlank(var1.getValue());
            }
        } else {
            //变量2
            Object var2;
            if (OperandClass.Variant.isEquals(rule.getOperandClass())) {
                Variant variant2 = FlowUtils.getVariant(rule.getVar2(), flow);
                ValueClassDO var22 = setValue(variant2);
                var2 = JsonUtils.castObject(var22.getValue(), var22.getAClass());
            } else {
                var2 = rule.getValue();
            }
            switch (operator) {
                case Operator.EQUAL:
                    if (Objects.equals(var1.getAClass(), Integer.class)) {
                        Integer var1T = Integer.parseInt(var1.getValue());
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), LocalDate.class)) {
                        LocalDate var1T = JsonUtils.castObject(var1.getValue(), LocalDate.class);
                        LocalDate var2T = JsonUtils.castObject(var2, LocalDate.class);
                        return var1T.isEqual(var2T);
                    } else if (Objects.equals(var1.getAClass(), Boolean.class)) {
                        Boolean var1T = JsonUtils.castObject(var1.getValue(), Boolean.class);
                        Boolean var2T = JsonUtils.castObject(var2, Boolean.class);
                        return Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), Float.class)) {
                        Float var1T = JsonUtils.castObject(var1.getValue(), Float.class);
                        Float var2T = JsonUtils.castObject(var2, Float.class);
                        return Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), BigInteger.class)) {
                        BigInteger var1T = JsonUtils.castObject(var1.getValue(), BigInteger.class);
                        BigInteger var2T = JsonUtils.castObject(var2, BigInteger.class);
                        return Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), Long.class)) {
                        Long var1T = JsonUtils.castObject(var1.getValue(), Long.class);
                        Long var2T = JsonUtils.castObject(var2, Long.class);
                        return Objects.equals(var1T, var2T);
                    } else {
                        return false;
                    }
                case Operator.GREATER_THAN:
                    if (Objects.equals(var1.getAClass(), Integer.class)) {
                        Integer var1T = Integer.parseInt(var1.getValue());
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T > var2T;
                    } else if (Objects.equals(var1.getAClass(), LocalDate.class)) {
                        LocalDate var1T = JsonUtils.castObject(var1.getValue(), LocalDate.class);
                        LocalDate var2T = JsonUtils.castObject(var2, LocalDate.class);
                        return var1T.isAfter(var2T);
                    } else if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return StringUtils.compare(var1T, var2T) > 0;
                    }  else if (Objects.equals(var1.getAClass(), Float.class)) {
                        Float var1T = JsonUtils.castObject(var1.getValue(), Float.class);
                        Float var2T = JsonUtils.castObject(var2, Float.class);
                        return var1T > var2T;
                    } else if (Objects.equals(var1.getAClass(), BigInteger.class)) {
                        BigInteger var1T = JsonUtils.castObject(var1.getValue(), BigInteger.class);
                        BigInteger var2T = JsonUtils.castObject(var2, BigInteger.class);
                        if(var1T.compareTo(var2T)==1){
                        return true;
                        }
                    } else if (Objects.equals(var1.getAClass(), Long.class)) {
                        Long var1T = JsonUtils.castObject(var1.getValue(), Long.class);
                        Long var2T = JsonUtils.castObject(var2, Long.class);
                        return var1T > var2T;
                    } else {
                        return false;
                    }
                case Operator.GREATER_THAN_OR_EQUAL:
                    if (Objects.equals(var1.getAClass(), Integer.class)) {
                        Integer var1T = Integer.parseInt(var1.getValue());
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T >= var2T;
                    } else if (Objects.equals(var1.getAClass(), LocalDate.class)) {
                        LocalDate var1T = JsonUtils.castObject(var1.getValue(), LocalDate.class);
                        LocalDate var2T = JsonUtils.castObject(var2, LocalDate.class);
                        return var1T.isAfter(var2T) || var1T.isEqual(var2T);
                    } else if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return StringUtils.compare(var1T, var2T) >= 0;
                    } else if (Objects.equals(var1.getAClass(), Float.class)) {
                        Float var1T = JsonUtils.castObject(var1.getValue(), Float.class);
                        Float var2T = JsonUtils.castObject(var2, Float.class);
                        return var1T >= var2T;
                    } else if (Objects.equals(var1.getAClass(), BigInteger.class)) {
                        BigInteger var1T = JsonUtils.castObject(var1.getValue(), BigInteger.class);
                        BigInteger var2T = JsonUtils.castObject(var2, BigInteger.class);
                        if(var1T.compareTo(var2T)==1||var1T.compareTo(var2T)==0){
                            return true;
                        }
                    } else if (Objects.equals(var1.getAClass(), Long.class)) {
                        Long var1T = JsonUtils.castObject(var1.getValue(), Long.class);
                        Long var2T = JsonUtils.castObject(var2, Long.class);
                        return var1T >= var2T;
                    } else {
                        return false;
                    }
                case Operator.LESS_THAN:
                    if (Objects.equals(var1.getAClass(), Integer.class)) {
                        Integer var1T = Integer.parseInt(var1.getValue());
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T < var2T;
                    } else if (Objects.equals(var1.getAClass(), LocalDate.class)) {
                        LocalDate var1T = JsonUtils.castObject(var1.getValue(), LocalDate.class);
                        LocalDate var2T = JsonUtils.castObject(var2, LocalDate.class);
                        return var1T.isBefore(var2T);
                    } else if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return StringUtils.compare(var1T, var2T) <= 0;
                    } else if (Objects.equals(var1.getAClass(), Float.class)) {
                        Float var1T = JsonUtils.castObject(var1.getValue(), Float.class);
                        Float var2T = JsonUtils.castObject(var2, Float.class);
                        return var1T < var2T;
                    } else if (Objects.equals(var1.getAClass(), BigInteger.class)) {
                        BigInteger var1T = JsonUtils.castObject(var1.getValue(), BigInteger.class);
                        BigInteger var2T = JsonUtils.castObject(var2, BigInteger.class);
                        if(var1T.compareTo(var2T)==-1)
                        {return true;}
                    } else if (Objects.equals(var1.getAClass(), Long.class)) {
                        Long var1T = JsonUtils.castObject(var1.getValue(), Long.class);
                        Long var2T = JsonUtils.castObject(var2, Long.class);
                        return var1T < var2T;
                    } else {
                        return false;
                    }
                case Operator.LESS_THAN_OR_EQUAL:
                    if (Objects.equals(var1.getAClass(), Integer.class)) {
                        Integer var1T = Integer.parseInt(var1.getValue());
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T <= var2T;
                    } else if (Objects.equals(var1.getAClass(), LocalDate.class)) {
                        LocalDate var1T = JsonUtils.castObject(var1.getValue(), LocalDate.class);
                        LocalDate var2T = JsonUtils.castObject(var2, LocalDate.class);
                        return var1T.isBefore(var2T) || var1T.isEqual(var2T);
                    } else if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return StringUtils.compare(var1T, var2T) < 0;
                    } else if (Objects.equals(var1.getAClass(), Float.class)) {
                        Float var1T = JsonUtils.castObject(var1.getValue(), Float.class);
                        Float var2T = JsonUtils.castObject(var2, Float.class);
                        return var1T <= var2T;
                    } else if (Objects.equals(var1.getAClass(), BigInteger.class)) {
                        BigInteger var1T = JsonUtils.castObject(var1.getValue(), BigInteger.class);
                        BigInteger var2T = JsonUtils.castObject(var2, BigInteger.class);
                        if(var1T.compareTo(var2T)==-1||var1T.compareTo(var2T)==0){
                            return true;
                        }
                    } else if (Objects.equals(var1.getAClass(), Long.class)) {
                        Long var1T = JsonUtils.castObject(var1.getValue(), Long.class);
                        Long var2T = JsonUtils.castObject(var2, Long.class);
                        return var1T <= var2T;
                    } else {
                        return false;
                    }
                case Operator.NOT_EQUAL:
                    if (Objects.equals(var1.getAClass(), Integer.class)) {
                        Integer var1T = Integer.parseInt(var1.getValue());
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return !Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), LocalDate.class)) {
                        LocalDate var1T = JsonUtils.castObject(var1.getValue(), LocalDate.class);
                        LocalDate var2T = JsonUtils.castObject(var2, LocalDate.class);
                        return !var1T.isEqual(var2T);
                    } else if (Objects.equals(var1.getAClass(), Boolean.class)) {
                        Boolean var1T = JsonUtils.castObject(var1.getValue(), Boolean.class);
                        Boolean var2T = JsonUtils.castObject(var2, Boolean.class);
                        return !Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return !Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), Float.class)) {
                        Float var1T = JsonUtils.castObject(var1.getValue(), Float.class);
                        Float var2T = JsonUtils.castObject(var2, Float.class);
                        return !Objects.equals(var1T, var2T);
                    } else if (Objects.equals(var1.getAClass(), BigInteger.class)) {
                        BigInteger var1T = JsonUtils.castObject(var1.getValue(), BigInteger.class);
                        BigInteger var2T = JsonUtils.castObject(var2, BigInteger.class);
                        if(var1T.compareTo(var2T)==1||var1T.compareTo(var2T)==-1){
                            return true;
                        }
                    } else if (Objects.equals(var1.getAClass(), Long.class)) {
                        Long var1T = JsonUtils.castObject(var1.getValue(), Long.class);
                        Long var2T = JsonUtils.castObject(var2, Long.class);
                        return !Objects.equals(var1T, var2T);
                    } else {
                        return false;
                    }
                case Operator.START_WITH:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return var1T.startsWith(var2T);
                    }
                case Operator.END_WITH:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return var1T.endsWith(var2T);
                    } else {
                        return false;
                    }
                case Operator.CONTAINS:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        String var2T = JsonUtils.castObject(var2, String.class);
                        return var1T.contains(var2T);
                    } else {
                        return false;
                    }
                case Operator.LENGTH_EQUAL:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T.length() == var2T;
                    } else {
                        return false;
                    }
                case Operator.LENGTH_NOT_EQUAL:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T.length() != var2T;
                    } else {
                        return false;
                    }
                case Operator.LENGTH_GREATER_THAN:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T.length() > var2T;
                    } else {
                        return false;
                    }
                case Operator.LENGTH_GREATER_THAN_OR_EQUAL:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T.length() >= var2T;
                    } else {
                        return false;
                    }
                case Operator.LENGTH_LESS_THAN:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T.length() < var2T;
                    } else {
                        return false;
                    }
                case Operator.LENGTH_LESS_THAN_OR_EQUAL:
                    if (Objects.equals(var1.getAClass(), String.class)) {
                        String var1T = JsonUtils.castObject(var1.getValue(), String.class);
                        Integer var2T = JsonUtils.castObject(var2, Integer.class);
                        return var1T.length() <= var2T;
                    } else {
                        return false;
                    }
            }
        }
        return false;
    }

    /**
     * 获取变量值，转换变量类型
     *
     * @param variant
     * @return
     * @author yansiyang
     * @date 2019/8/27 8:03
     */
    private ValueClassDO setValue(Variant variant) {
        String dtType = variant.getDataType();
        switch (dtType) {
            case DataType.Boolean:
                return new ValueClassDO(getValue(variant), Boolean.class);
            case DataType.Integer:
                return new ValueClassDO(getValue(variant), Integer.class);
            case DataType.String:
                return new ValueClassDO(getValue(variant), String.class);
            case DataType.DateTime:
                return new ValueClassDO(getValue(variant), LocalDateTime.class);
            case DataType.Float:
                return new ValueClassDO(getValue(variant), Float.class);
            case DataType.BigInteger:
                return new ValueClassDO(getValue(variant), BigInteger.class);
            case DataType.Long:
                return new ValueClassDO(getValue(variant), Long.class);
        }
        return new ValueClassDO();
    }

    /**
     * 计算表达式
     *
     * @param variant
     * @return
     * @author yansiyang
     * @date 2019/8/27 8:33
     */
    public String getValue(Variant variant) {
        return expressionUtils.getValue(variant);
    }

    public Boolean getRuleValue(Flow flow, Rule rule, Boolean satisfy) {
        String expression = rule.getExpression();
        expression = ExpressionUtils.replaceAllBlank(expression);
        RuleDO ruleT = JSONObject.parseObject(expression, RuleDO.class);
        return satisfy ? calculateRule(flow, ruleT) : !calculateRule(flow, ruleT);
    }
}
