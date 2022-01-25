package com.csicit.ace.license.core.config;

import com.csicit.ace.license.core.service.LicenseService;
import com.csicit.ace.license.core.service.impl.LicenseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;


/**
 * 授权校验 启动项 定时任务
 *
 * @author FourLeaves
 * @date 2021/8/31 9:17
 */
@ConditionalOnExpression("'${spring.application.name}'.equals('licenseserver')")
@Component
@Slf4j
@Order(-1)
public class LicenseConfig implements ApplicationRunner {

    @Value("${ace.license.showMacInfo:false}")
    private Boolean showMacInfo;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private LicenseService licenseService;

    @Override
    public void run(ApplicationArguments args) {
        if (!appName.equals("licenseserver")) {
            log.error("不允许修改授权服务名称！");
            System.exit(-1);
        }
        if (showMacInfo) {
            licenseService.macInfo();
            System.exit(-1);
        }
        licenseService.checkLicense();
        System.setProperty("ace.uuid", LicenseServiceImpl.serverUuid);
    }

    @Scheduled(cron = "*/10 * * * * ?")
    private void fixRegisterApps() {
        licenseService.fixRegisterApps();
    }

    @Scheduled(cron = "0 */10 * * * ?")
    private void checkLicense() {
        licenseService.checkLicense();
    }

    @Scheduled(cron = "30 */5 * * * ?")
    private void checkManyServer() {
        licenseService.checkManyServer();
    }

}
