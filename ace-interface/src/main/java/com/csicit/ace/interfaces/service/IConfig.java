package com.csicit.ace.interfaces.service;

/**
 * 系统配置项管理
 *
 * @author yansiyang
 * @version v1.0
 * @date 15:27 2019/3/28
 */
public interface IConfig {

    /**
     * 扫描应用配置项 并写入数据库
     * @return 
     * @author FourLeaves
     * @date 2019/12/25 17:34
     */
    void scanConfig();

    /**
     * 获取指定String类型的配置项  集团级
     *
     * @param key 配置项标识
     * @return 配置项内容
     * @author yansiyang
     * @date 15:47 2019/3/28
     */
    String getConfig(String key);

    /**
     * 获取指定String类型的配置项
     *
     * @param key          配置项标识
     * @param defaultValue 缺省值
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/24 19:10
     */

    String getValue(String key, String defaultValue);

    /**
     * 根据当前应用获取指定String类型的配置项  应用及 -> 集团级
     *
     * @param key 配置项标识
     * @return 配置项内容
     * @author yansiyang
     * @date 15:47 2019/3/28
     */
    String getConfigByCurrentApp(String key);

    /**
     * 根据指定应用获取指定String类型的配置项  应用及 -> 集团级
     *
     * @param key 配置项标识
     * @return 配置项内容
     * @author yansiyang
     * @date 15:47 2019/3/28
     */
    String getConfigByApp(String key, String appId);
}
