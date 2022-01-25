package com.csicit.ace.platform.core.service;


/**
 * 实例对象访问 实例对象访问接口
 *
 * @param
 * @author zuogang
 * @return
 * @date 2019/4/22 15:32
 */
public interface GeneratorService {
    byte[] generatorCode(String[] tableNames);
}
