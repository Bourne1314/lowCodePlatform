package com.csicit.ace.common.log;

/**
 * 统一日志工具
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/28 11:07
 */
public interface AceLogger {

    void debug(String info);

    void info(String info);

    void warn(String info);

    void error(String info);

    void error(String info, Exception e);

    void debug(String info, Class<?> clazz);

    void info(String info, Class<?> clazz);

    void warn(String info, Class<?> clazz);

    void error(String info, Class<?> clazz);

    void error(String info, Exception e, Class<?> clazz);
}
