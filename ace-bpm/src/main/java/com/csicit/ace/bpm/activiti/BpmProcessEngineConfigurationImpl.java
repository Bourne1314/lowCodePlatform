package com.csicit.ace.bpm.activiti;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DelegatingDataSource;

/**
 * @author JonnyJiang
 * @date 2019/10/30 21:17
 */
public class BpmProcessEngineConfigurationImpl extends SpringProcessEngineConfiguration {

    @Value("${spring.cloud.nacos.discovery.server-addr:1}")
    private String serverAddr;


    @Override
    public org.activiti.engine.impl.db.DbSqlSessionFactory createDbSqlSessionFactory() {
        if (dataSource instanceof DelegatingDataSource) {
            DelegatingDataSource dsp = (DelegatingDataSource) dataSource;
            if (dsp.getTargetDataSource() instanceof DruidDataSource) {
                return new DbSqlSessionFactory();
            } else if (serverAddr.length() == 1 && dsp.getTargetDataSource() instanceof DynamicRoutingDataSource) {
                return new DbSqlSessionFactory();
            }
        }
        return super.createDbSqlSessionFactory();
    }

    @Override
    public void initIdGenerator() {
        idGenerator = new com.csicit.ace.bpm.activiti.IdGenerator();
        super.initIdGenerator();
    }
}