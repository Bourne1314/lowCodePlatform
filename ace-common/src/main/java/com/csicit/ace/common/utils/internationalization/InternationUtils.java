package com.csicit.ace.common.utils.internationalization;

import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
public class InternationUtils implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Logger.class);
    /**
     * redis工具对象
     */
    static CacheUtil cacheUtil = SpringContextUtils.getBean(CacheUtil.class);
    /**
     * HttpServletRequest 请求
     */
    static SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
    /**
     * 中文
     */
    private static final String LANGUAGE_ZH_CN = "zh-CN";
    /**
     * 英语
     */
    private static final String LANGUAGE_EN_US = "en-US";
    /**
     * 默认包名+默认文件头
     */
    private static final String DEFAULT_PACKAGE_NAME = "com.csicit.ace.common.resources.content";

    /**
     * 获取国际化消息内容
     * 默认使用平台提供初始化
     *
     * @param key 消息key
     * @return 消息内容
     * @author shanwj
     * @date 2019/4/12 11:30
     */
    public static String getInternationalMsg(String key) {
        return getInternationalMsgOrigin(DEFAULT_PACKAGE_NAME, key);
    }

    /**
     * 获取国际化消息内容
     *
     * @param key  消息key
     * @param args 消息模板参数值
     * @return 消息内容
     * @author shanwj
     * @date 2019/4/17 18:41
     */

    public static String getInternationalMsg(String key, Object... args) {
        return String.format(getInternationalMsgOrigin(DEFAULT_PACKAGE_NAME, key), args);
    }

    /**
     * @param path 应用自定义国际化内容路径
     * @param key  消息key
     * @param args 消息模板参数值
     * @return 消息内容
     * @author shanwj
     * @date 2019/4/17 18:44
     */
    public static String getInternationalMsgWithPath(String path, String key, Object... args) {
        return String.format(getInternationalMsgOrigin(path, key), args);
    }

    /**
     * 获取应用自定义国际化消息内容
     *
     * @param path 应用自定义国际化内容路径
     * @param key  消息key
     * @return 消息内容
     */
    public static String getInternationalMsgOrigin(String path, String key) {
        String language = null;
        try {
            language = securityUtils.getValue("defaultlanguage");
        } catch (Exception e) {

        }
        if (StringUtils.isBlank(language)) {
            language = cacheUtil.get("platform_internationalization");
        }
        if (Objects.equals(language, LANGUAGE_EN_US)) {
            return ResourceBundle.getBundle(path, new Locale("en", "US")).getString(key);
        }
        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle(path, new Locale("zh", "CN"));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        if (resourceBundle == null) {
            try {
                resourceBundle = ResourceBundle.getBundle(path);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        if (resourceBundle == null) {
            throw new RuntimeException("resource bundle not found: " + path);
        }
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            throw new RuntimeException("resource bundle key not found: " + key);
        }
    }
}
