package com.csicit.ace.interfaces.service;

/**
 * 编码规则管理
 * 暂不用
 * @author yansiyang
 * @date Created in 17:29 2019/3/28
 * @version v1.0
 */
public interface ICode {

    /**
     * 根据规则获取编码
     *
     * @author yansiyang
     * @date 17:30 2019/3/28
     * @param rule 规则
     * @return 编码
     */
    String getCode(String rule);

    /**
     * 根据规则及单个编码获取编码
     *
     * @author yansiyang
     * @date 17:30 2019/3/28
     * @param rule 规则
     * @param code 编码
     * @return 编码
     */
    String getCode(String rule, String code);

    /**
     * 根据规则及2个编码获取编码
     *
     * @author yansiyang
     * @date 17:30 2019/3/28
     * @param rule 规则名称
     * @param firstCode 编码
     * @param secondCode 编码
     * @return 编码
     */
    String getCode(String rule, String firstCode, String  secondCode);
}
