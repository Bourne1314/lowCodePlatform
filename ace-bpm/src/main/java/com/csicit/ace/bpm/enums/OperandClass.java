package com.csicit.ace.bpm.enums;

/**
 * @author JonnyJiang
 * @date 2020/7/20 14:15
 */
public enum OperandClass {
    /**
     * 变量
     */
    Variant("var");

    private String operandClass;

    OperandClass(String operandClass) {
        this.operandClass = operandClass;
    }

    public boolean isEquals(String operandClass) {
        return this.operandClass.equals(operandClass);
    }
}
