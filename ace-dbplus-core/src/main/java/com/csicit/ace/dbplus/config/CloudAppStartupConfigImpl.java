package com.csicit.ace.dbplus.config;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 云版启动项
 *
 * @author shanwj
 * @date 2020/4/14 15:36
 */
@ConditionalOnExpression("'${spring.cloud.nacos.discovery.server-addr:1}'.length() > 1")
@Service("cloudAppStartupConfig")
public class CloudAppStartupConfigImpl extends AbstractAppStartupConfigImpl implements IAppStartupConfig {

    @Nullable
    @Autowired
    RedisUtils redisUtils;

    @Autowired
    CacheUtil cacheUtil;
    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    DiscoveryUtils discoveryUtils;

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
        if (Objects.nonNull(redisUtils)) {
            redisUtils.publish("newAceAppEvent", appName);
        } else {
            ServiceInstance pushServer = discoveryUtils.getOneHealthyInstance(Constants.PUSHSERVER);
            if (Objects.nonNull(pushServer)) {
                String reqUrl = "http://" + pushServer.getHost() + ":" + pushServer.getPort()
                        + "/action/createNamespace/" + appName;
                String result = httpClient.client(reqUrl);
                aceLogger.info("应用注册消息服务");
                aceLogger.info(result);
            } else {
                aceLogger.warn("消息服务push暂时不在线");
            }
        }
    }

    @Override
    public void saveInitScanData(InitStorageDataVO storageData) {
        //平台的服务
        if (Constants.AppNames.contains(appName)) {
            scanDataImpl.saveInitScanData(storageData);
        } else {//云版应用
            ServiceInstance orgAuth = discoveryUtils.getOneHealthyInstance(Constants.ORGAUTH);
            if (Objects.nonNull(orgAuth)) {
                String reqUrl;
                String result = null;
                try {
                    reqUrl = "http://" + orgAuth.getHost() + ":" + orgAuth.getPort() + "/orgauth/initHandler";
                    result = httpClient.postReturnString(reqUrl, storageData);
                } catch (Exception e) {
                    ServiceInstance gateway = discoveryUtils.getOneHealthyInstance(Constants.GATEWAY);
                    reqUrl = "http://" + gateway.getHost() + ":" + gateway.getPort() + "/orgauth/orgauth/initHandler";
                    result = httpClient.postReturnString(reqUrl, storageData);
                }
                if (!Objects.equals(result, "true")) {
                    aceLogger.error(result);
                    aceLogger.error("应用初始化失败,详情请查看orgauth服务日志!");
                    System.exit(-1);
                }
            }
        }
    }

    @Override
    public boolean lockApp(Map<String, String> map) {
        //平台的服务
        if (!Constants.AppNames.contains(appName)) {
            //云版应用
            ServiceInstance orgAuth = discoveryUtils.getOneHealthyInstance(Constants.ORGAUTH);
            if (Objects.nonNull(orgAuth)) {
                String reqUrl;
                String result = null;
                try {
                    reqUrl = "http://" + orgAuth.getHost() + ":" + orgAuth.getPort() +
                            "/orgauth/appUpdateHandler/lockApp";
                    result = httpClient.postReturnString(reqUrl, map);
                } catch (Exception e) {
                    ServiceInstance gateway = discoveryUtils.getOneHealthyInstance(Constants.GATEWAY);
                    reqUrl = "http://" + gateway.getHost() + ":" + gateway.getPort() +
                            "/orgauth/orgauth/appUpdateHandler/lockApp";
                    result = httpClient.postReturnString(reqUrl, map);
                }
                if (Objects.equals(result, "true")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean unLockApp(Map<String, String> map) {
        //平台的服务
        if (!Constants.AppNames.contains(appName)) {
            //云版应用
            ServiceInstance orgAuth = discoveryUtils.getOneHealthyInstance(Constants.ORGAUTH);
            if (Objects.nonNull(orgAuth)) {
                String reqUrl;
                String result = null;
                try {
                    reqUrl = "http://" + orgAuth.getHost() + ":" + orgAuth.getPort() +
                            "/orgauth/appUpdateHandler/unLockApp";
                    result = httpClient.postReturnString(reqUrl, map);
                } catch (Exception e) {
                    ServiceInstance gateway = discoveryUtils.getOneHealthyInstance(Constants.GATEWAY);
                    reqUrl = "http://" + gateway.getHost() + ":" + gateway.getPort() +
                            "/orgauth/orgauth/appUpdateHandler/unLockApp";
                    result = httpClient.postReturnString(reqUrl, map);
                }
                if (!Objects.equals(result, "true")) {
                    aceLogger.error(result);
                    aceLogger.error("应用升级过程中，解锁失败！");
                    System.exit(-1);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean updateDbItem(AppUpgrade appUpgrade) {
        //平台的服务
        if (!Constants.AppNames.contains(appName)) {
            //云版应用
            ServiceInstance orgAuth = discoveryUtils.getOneHealthyInstance(Constants.ORGAUTH);
            if (Objects.nonNull(orgAuth)) {
                String reqUrl;
                String result = null;
                try {
                    reqUrl = "http://" + orgAuth.getHost() + ":" + orgAuth.getPort() +
                            "/orgauth/appUpdateHandler/updateDbItem";
                    result = httpClient.postReturnString(reqUrl, appUpgrade);
                } catch (Exception e) {
                    ServiceInstance gateway = discoveryUtils.getOneHealthyInstance(Constants.GATEWAY);
                    reqUrl = "http://" + gateway.getHost() + ":" + gateway.getPort() +
                            "/orgauth/orgauth/appUpdateHandler/updateDbItem";
                    result = httpClient.postReturnString(reqUrl, appUpgrade);
                }
                if (!Objects.equals(result, "true")) {
                    aceLogger.error(result);
                    aceLogger.error("应用版本升级失败！");
                    System.exit(-1);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void updAppVersion(Map<String, String> map) {
        //平台的服务
        if (!Constants.AppNames.contains(appName)) {
            //云版应用
            ServiceInstance orgAuth = discoveryUtils.getOneHealthyInstance(Constants.ORGAUTH);
            if (Objects.nonNull(orgAuth)) {
                String reqUrl;
                String result = null;
                try {
                    reqUrl = "http://" + orgAuth.getHost() + ":" + orgAuth.getPort() +
                            "/orgauth/appUpdateHandler/updAppVersion";
                    result = httpClient.postReturnString(reqUrl, map);
                } catch (Exception e) {
                    ServiceInstance gateway = discoveryUtils.getOneHealthyInstance(Constants.GATEWAY);
                    reqUrl = "http://" + gateway.getHost() + ":" + gateway.getPort() +
                            "/orgauth/orgauth/appUpdateHandler/updAppVersion";
                    result = httpClient.postReturnString(reqUrl, map);
                }
                if (!Objects.equals(result, "true")) {
                    aceLogger.error(result);
                    aceLogger.error("应用升级过程中，更新配置项数据失败！");
                    System.exit(-1);
                }
            }
        }
    }
}
