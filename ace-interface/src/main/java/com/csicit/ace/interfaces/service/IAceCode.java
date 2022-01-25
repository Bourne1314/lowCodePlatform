package com.csicit.ace.interfaces.service;

import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * 业务编码接口
 *
 * @author shanwj
 * @date 2020/7/3 10:36
 */
public interface IAceCode {

    /** 
     *  返回数字序列编号
     *  
     * @param appId	 应用id
     * @param seqBizTag	 数字序列标识
     * @return java.lang.String
     * @author shanwj
     * @date 2020/7/3 10:40
     */
    String getNextNum(@NonNull String appId,@NonNull String seqBizTag);
    /** 
     * 返回数字序列编号
     *
     * @param appId	 应用id
     * @param seqBizTag 数字序列标识
     * @param partValueTag 数字序列次要标识
     * @return java.lang.String
     * @author shanwj
     * @date 2020/7/3 10:40
     */
    String getNextNum(@NonNull String appId,@NonNull String seqBizTag,@NonNull String partValueTag);
    /**
     * 获取业务编码
     *
     * @param appId 业务编码
     * @param templateKey 业务编码标识
     * @return
     */
    String getTemplateCode(String appId,String templateKey);
    /**
     * 获取业务编码
     *
     * @param appId 业务编码
     * @param templateKey 业务编码标识
0     * @param params 参数部件替换参数
     * @return
     */
    String getTemplateCode(String appId,String templateKey,Map<String,String> params);


}
