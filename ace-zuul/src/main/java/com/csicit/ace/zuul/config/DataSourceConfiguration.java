package com.csicit.ace.zuul.config;//package com.csicit.ace.fileserver.core;/*
// *  Copyright 1999-2021 Seata.io Group.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.baomidou.mybatisplus.core.parser.ISqlParser;
//import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
//import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import io.seata.rm.datasource.DataSourceProxy;
//import org.apache.ibatis.plugin.Interceptor;
//import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class DataSourceConfiguration {
//
////    @Bean
////    @ConfigurationProperties(prefix = "spring.datasource")
////    public DataSource druidDataSource() {
////        DruidDataSource druidDataSource = new DruidDataSource();
////        return druidDataSource;
////    }
//
//    @Resource
//    private DataSourceProperties dataSourceProperties;
//
//    /**
//     * init durid datasource
//     *
//     * @Return: druidDataSource  datasource instance
//     */
//    @Bean
//    @Primary
//    public DruidDataSource druidDataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(dataSourceProperties.getUrl());
//        druidDataSource.setUsername(dataSourceProperties.getUsername());
//        druidDataSource.setPassword(dataSourceProperties.getPassword());
//        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
//        druidDataSource.setInitialSize(0);
//        druidDataSource.setMaxActive(180);
//        druidDataSource.setMaxWait(60000);
//        druidDataSource.setMinIdle(0);
//        druidDataSource.setValidationQuery("Select 1 from DUAL");
//        druidDataSource.setTestOnBorrow(false);
//        druidDataSource.setTestOnReturn(false);
//        druidDataSource.setTestWhileIdle(true);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
//        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
//        druidDataSource.setRemoveAbandoned(true);
//        druidDataSource.setRemoveAbandonedTimeout(1800);
//        druidDataSource.setLogAbandoned(true);
//        return druidDataSource;
//    }
//
//
//    @Bean
//    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws Exception{
//        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
//        //mybtis-plus分页拆件
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        //攻击SQL阻断解析器,加入解析链
//        List<ISqlParser> sqlList = new ArrayList<>();
//        sqlList.add(new BlockAttackSqlParser());
//        paginationInterceptor.setSqlParserList(sqlList);
//
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{paginationInterceptor,new OptimisticLockerInterceptor()});
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
//        return sqlSessionFactoryBean;
//    }
//}