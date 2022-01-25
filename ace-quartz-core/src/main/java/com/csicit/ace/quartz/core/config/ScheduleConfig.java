
package com.csicit.ace.quartz.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.data.persistent.service.QrtzConfigServiceD;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.quartz.core.factory.MyJobFactory;
import com.csicit.ace.quartz.core.plugins.AceJobConfigLoaderPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
public class ScheduleConfig {

    @Value("${spring.datasource.dynamic.datasource.ace.url:#{null}}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.ace.username:#{null}}")
    private String username;
    @Value("${spring.datasource.dynamic.datasource.ace.password:#{null}}")
    private String password;
    @Value("${spring.datasource.dynamic.datasource.ace.driver-class-name:#{null}}")
    private String driverClassName;

    @Autowired
    private MyJobFactory myJobFactory;

    @Autowired
    SysGroupAppServiceD sysGroupAppService;

    @Autowired
    QrtzConfigServiceD qrtzConfigServiceD;

    private void getConfig() {
        List<SysGroupAppDO> sysGroupAppDOList = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>().select
                ("id"));
        List<String> appIds = new ArrayList<>(16);
        if (sysGroupAppDOList != null && sysGroupAppDOList.size() > 0) {
            sysGroupAppDOList.stream().forEach(sysGroupAppDO -> {
                appIds.add(sysGroupAppDO.getId());
            });
        }
        List<QrtzConfigDO> qrtzConfigDOList = qrtzConfigServiceD.list(new QueryWrapper<QrtzConfigDO>()
                .and(appIds == null || appIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", appIds));
        AceJobConfigLoaderPlugin.configs = qrtzConfigDOList;
    }


    @Bean(name = "schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) throws IOException {
        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();

        getConfig();

        //创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        if (Constants.isMonomerApp && !Constants.isZuulApp) {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            druidDataSource.setDriverClassName(driverClassName);
            factory.setDataSource(druidDataSource);
        } else {
            factory.setDataSource(dataSource);
        }

        Properties pro = propertiesFactoryBean.getObject();
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        factory.setQuartzProperties(pro);
        factory.setJobFactory(myJobFactory);
        return factory;
    }
}