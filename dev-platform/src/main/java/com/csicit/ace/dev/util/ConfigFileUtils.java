package com.csicit.ace.dev.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于读取配置文件
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:36
 */
public class ConfigFileUtils {

    private static Logger logger = LoggerFactory.getLogger(ConfigFileUtils.class);
    public static void main(String[] args) {
        System.out.print(getValue("activity.properties", "regex_name"));
    }

    /**
     * 默认PropertiesConfiguration读取文件格式为ISO
     * 需要设置文件格式
     *
     * @param name 文件名称
     * @param arg  参数
     * @return
     */
    public static String getValue(String name, String arg) {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setEncoding("UTF-8");
        try {
            propertiesConfiguration.load(name);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return propertiesConfiguration.getString(arg);
    }

    /**
     * 获取配置文件信息
     *
     * @param name
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static Configuration getConfig(String name) {
        try {
            return new PropertiesConfiguration(name);
        } catch (ConfigurationException e) {
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取配置文件信息
     *
     * @param name
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static String getArgValue(String name, String arg) {
        return getConfig(name).getString(arg);
    }

    /**
     * 获取配置文件信息
     *
     * @param name
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static Object getProperty(String name, String arg) {
        return getConfig(name).getProperty(arg);
    }
}
