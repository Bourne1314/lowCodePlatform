package com.csicit.ace.dbplus.config;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;

import java.util.Map;

/**
 * @author shanwj
 * @date 2020/4/14 15:34
 */
public interface IAppStartupConfig {

    /**
     * 初始化密钥配置
     *
     * @param appName 应用名称
     */
    void initSmKeyValue(String appName);

    /**
     * 授权验证
     */
    void checkLicense();

    /**
     * 消息通道发布
     *
     * @param appName 应用名称
     */
    void publishChannel(String appName);

    /**
     * 保存初始化扫描数据
     *
     * @param storageData 扫描数据存储对象
     */
    void saveInitScanData(InitStorageDataVO storageData);

    /**
     * 应用升级 事务锁
     */
    boolean lockApp(Map<String, String> map);

    /**
     * 应用升级 解锁
     */
    boolean unLockApp(Map<String, String> map);

    /**
     * 更新应用数据项
     */
    boolean updateDbItem(AppUpgrade appUpgrade);

    /**
     * 应用版本升级
     */
    void updAppVersion(Map<String, String> map);
}

