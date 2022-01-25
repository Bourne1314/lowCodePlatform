package com.csicit.ace.dev.service;


import javax.servlet.ServletOutputStream;

/**
 * 实例对象访问 实例对象访问接口
 *
 * @param
 * @author zuogang
 * @return
 * @date 2019/4/22 15:32
 */
public interface GeneratorService {

    /**
     * 生成代码
     *
     * @param appId   应用id
     * @param tableId 表id
     * @return
     */
//    byte[] generatorCode(String appId, String tableId);

    /**
     * 生成代码
     *
     * @param appId    应用id
     * @param tableIds 表id集合
     * @return
     */
//    byte[] generatorCodes(String appId, List<String> tableIds);

    /**
     * 生成代码
     *
     * @param appId 应用id
     * @return
     */
    void generatorCodes(String appId, ServletOutputStream outputStream);

    /**
     * 发布任务
     *
     * @param modelIdList
     * @param appId
     * @return byte[]
     * @author zuogang
     * @date 2019/12/26 16:13
     */
//    boolean publishService(List<String> modelIdList, String appId);

    /**
     * 生成代码
     *
     * @param modelIdList 模型ID
     * @param appId 应用ID
     * @return
     */
//    byte[] genService(List<String> modelIdList, String appId);
}
