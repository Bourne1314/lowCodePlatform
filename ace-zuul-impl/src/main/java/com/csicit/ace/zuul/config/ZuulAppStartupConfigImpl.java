package com.csicit.ace.zuul.config;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.dbplus.config.AbstractAppStartupConfigImpl;
import com.csicit.ace.dbplus.config.IAppStartupConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 单体版启动项
 *
 * @author Fourleaves
 * @date 2021/6/11 14:53
 */
@Service("zuulAppStartupConfig")
public class ZuulAppStartupConfigImpl extends AbstractAppStartupConfigImpl implements IAppStartupConfig {

    @Autowired
    CacheUtil cacheUtil;
    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;

    @Value("${ace.config.zuul.ip:127.0.0.1}")
    private String zuulIp;

    @Value("${ace.config.zuul.port:2100}")
    private Integer zuulPort;

    @Autowired
    HttpClient httpClient;

    @Override
    public void initSmKeyValue(String appName) {
        try {
            /**
             * platform启动的工作如下：
             * 1、第一次初始化需要生成密钥
             * 2、扫描自身配置项
             * 3、加载数据库配置项
             */
            if (!Objects.equals(appName, Constants.PLATFORM)) {
                GMBaseUtil.initSmKeyValue(cacheUtil);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void checkLicense() {

    }

    @Override
    public void publishChannel(String appName) {

    }

    @Override
    public void saveInitScanData(InitStorageDataVO storageData) {

        String reqUrl;
        String result = null;
        try {
            reqUrl = "http://" + zuulIp + ":" + zuulPort + "/orgauth/initHandler";
            result = httpClient.postReturnString(reqUrl, storageData);
        } catch (Exception e) {
        }
        if (!Objects.equals(result, "true")) {
            aceLogger.error(result);
            aceLogger.error("应用初始化失败,详情请查看zuul日志!");
            System.exit(-1);
        }
    }


    @Override
    public boolean lockApp(Map<String, String> map) {
        String reqUrl;
        String result = null;
        try {
            reqUrl = "http://" + zuulIp + ":" + zuulPort +
                    "/orgauth/appUpdateHandler/lockApp";
            result = httpClient.postReturnString(reqUrl, map);
        } catch (Exception e) {

        }
        if (Objects.equals(result, "true")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean unLockApp(Map<String, String> map) {
        String reqUrl;
        String result = null;
        try {
            reqUrl = "http://" + zuulIp + ":" + zuulPort +
                    "/orgauth/appUpdateHandler/unLockApp";
            result = httpClient.postReturnString(reqUrl, map);
        } catch (Exception e) {
        }
        if (!Objects.equals(result, "true")) {
            aceLogger.error(result);
            aceLogger.error("应用升级过程中，解锁失败！");
            System.exit(-1);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDbItem(AppUpgrade appUpgrade) {

        String reqUrl;
        String result = null;
        try {
            reqUrl = "http://" + zuulIp + ":" + zuulPort +
                    "/orgauth/appUpdateHandler/updateDbItem";
            result = httpClient.postReturnString(reqUrl, appUpgrade);
        } catch (Exception e) {

        }
        if (!Objects.equals(result, "true")) {
            aceLogger.error(result);
            aceLogger.error("应用版本升级失败！");
            System.exit(-1);
            return false;
        }

        return true;
    }

    @Override
    public void updAppVersion(Map<String, String> map) {
        String reqUrl;
        String result = null;
        try {
            reqUrl = "http://" + zuulIp + ":" + zuulPort +
                    "/orgauth/appUpdateHandler/updAppVersion";
            result = httpClient.postReturnString(reqUrl, map);
        } catch (Exception e) {

        }
        if (!Objects.equals(result, "true")) {
            aceLogger.error(result);
            aceLogger.error("应用升级过程中，更新配置项数据失败！");
            System.exit(-1);
        }
    }


}
