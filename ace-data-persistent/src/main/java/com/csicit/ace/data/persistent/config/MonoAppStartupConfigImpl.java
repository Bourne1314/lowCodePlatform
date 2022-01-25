package com.csicit.ace.data.persistent.config;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import com.csicit.ace.dbplus.config.AbstractAppStartupConfigImpl;
import com.csicit.ace.dbplus.config.IAppStartupConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 单机版启动项
 *
 * @author shanwj
 * @date 2020/4/14 15:36
 */
// 区分单机版 云版  只看 nacos 注册地址
@ConditionalOnExpression("'${spring.cloud.nacos.discovery.server-addr:1}'.length() == 1")
@Service("monoAppStartupConfig")
public class MonoAppStartupConfigImpl extends AbstractAppStartupConfigImpl implements IAppStartupConfig {

    @Autowired
    PlatformInitKeysUtil platformInitKeysUtil;

    @Override
    public void initSmKeyValue(String appName) {
        platformInitKeysUtil.initSmKeyValue();
    }

    @Override
    public void checkLicense() {

    }

    @Override
    public void publishChannel(String appName) {

    }

    @Override
    public void saveInitScanData(InitStorageDataVO storageData) {
        scanDataImpl.saveInitScanData(storageData);
    }

    @Override
    public boolean lockApp(Map<String, String> map) {
        return scanDataImpl.lockApp(map);
    }

    @Override
    public boolean unLockApp(Map<String, String> map) {
        return scanDataImpl.unLockApp(map);
    }

    @Override
    public boolean updateDbItem(AppUpgrade appUpgrade) {
        return scanDataImpl.updateDbItem(appUpgrade);
    }

    @Override
    public void updAppVersion(Map<String, String> map) {
        scanDataImpl.updAppVersion(map);
    }
}
