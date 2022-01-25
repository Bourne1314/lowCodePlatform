package com.csicit.ace.bpm.enums;

/**
 * @author JonnyJiang
 * @date 2020/7/20 10:34
 */
public enum RuleType {
    AndOrOr("and/or"),
    And("and"),
    Expr("expr"),
    Rule("rule");

    private String type;

    RuleType(String type) {
        this.type = type;
    }

    public boolean isEquals(String type) {
        return type.equals(type);
    }
}