package com.csicit.ace.bpm.activiti;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.DbUpdateTask;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author JonnyJiang
 * @date 2019/7/10 20:02
 */
@Order(1000)
@Configuration
public class BpmConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BpmConfiguration.class);
    private static String actualDatabaseType;
    /**
     * activiti DML语句最多为28个参数，DM最多支持2048个，2048 / 28 = 73.14，所以一个批量操作最多包含73个DML语句
     */
    private static final int DEFAULT_MAX_NR_OF_STATEMENTS_BULK_INSERT_DM = 73;

    @Autowired
    private SecurityUtils securityUtils;

    public static String getActualDatabaseType() {
        return actualDatabaseType;
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager) throws SQLException {
        String schemaUpdate = ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_TRUE;
        BpmProcessEngineConfigurationImpl config = new BpmProcessEngineConfigurationImpl();
        DatabaseMetaData databaseMetaData = dataSource.getConnection().getMetaData();
        String databaseProductName = databaseMetaData.getDatabaseProductName();
        String databaseType;
        if (DbUpdateTask.DM_DATABASE_PRODUCT_NAME.equals(databaseProductName)) {
            actualDatabaseType = DbUpdateTask.DATABASE_TYPE_DM;
            databaseType = ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE;
            config.setDatabaseType(databaseType);
            config.setMaxNrOfStatementsInBulkInsert(DEFAULT_MAX_NR_OF_STATEMENTS_BULK_INSERT_DM);
            config.setDatabaseSchema(databaseMetaData.getUserName());
        } else if (DbUpdateTask.ST_DATABASE_PRODUCT_NAME.equals(databaseProductName)) {
            actualDatabaseType = DbUpdateTask.DATABASE_TYPE_ST;
            databaseType = ProcessEngineConfigurationImpl.DATABASE_TYPE_ORACLE;
            config.setDatabaseType(databaseType);
//            config.setMaxNrOfStatementsInBulkInsert(DEFAULT_MAX_NR_OF_STATEMENTS_BULK_INSERT_DM);
            config.setDatabaseSchema(databaseMetaData.getUserName());
        } else {
            databaseType = ProcessEngineConfigurationImpl.getDefaultDatabaseTypeMappings().getProperty(databaseProductName);
            if (databaseType == null) {
                throw new BpmException("couldn't deduct database type from database product name '" + databaseProductName + "'");
            }
            actualDatabaseType = databaseType;
            config.setDatabaseType(databaseType);
        }
        config.setDataSource(dataSource);
        LOGGER.debug("DatabaseType:" + config.getDatabaseType());
        LOGGER.debug("ActualDatabaseType:" + actualDatabaseType);
        config.setDatabaseSchemaUpdate(schemaUpdate);
        config.setProcessEngineName(securityUtils.getAppName());
        config.setTransactionManager(transactionManager);
        config.setHistoryLevel(HistoryLevel.FULL);
        return config;
    }

    @Bean
    public ProcessEngine processEngine(SpringProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(SpringProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public HistoryManager historyManager(SpringProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.getHistoryManager();
    }

    @Bean
    public ExpressionManager expressionManager(SpringProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.getExpressionManager();
    }
}