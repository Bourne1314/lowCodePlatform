package com.csicit.ace.common.service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/1/2 9:36
 */
public interface AceConfigService {
    /**
     * 获取配置项
     * @param key
     * @return 
     * @author FourLeaves
     * @date 2020/1/2 9:38
     */
    <T> T getConfig(String key, Class<T> clazz);
}
