package com.csicit.ace.bpm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.pojo.domain.WfPropertyDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.domain.WfiTaskPendingDO;
import com.csicit.ace.bpm.service.WfPropertyService;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.service.WfiTaskPendingService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.utils.StringUtils;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/10/23 19:07
 */
@Component
@Order(1)
public class DbUpdateTask implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUpdateTask.class);
    private List<String> versions = new ArrayList<>();

    private static final String TABLE_NAME = "WF_PROPERTY";
    private static final String TABLE_TYPE = "TABLE";
    private static final String VERSION_NAME = "BPM_VERSION";
    public static final String DM_DATABASE_PRODUCT_NAME = "DM DBMS";
    public static final String ST_DATABASE_PRODUCT_NAME = "OSCAR";
    public static final String DATABASE_TYPE_DM = "dm";
    public static final String DATABASE_TYPE_ST = "st";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WfPropertyService wfPropertyService;
    @Autowired
    private BpmManager bpmManager;
    @Autowired
    private WfiFlowService wfiFlowService;
    @Autowired
    private WfiTaskPendingService wfiTaskPendingService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        versions.add("1.0.0");
        versions.add("1.0.1");
        versions.add("1.0.2");
        versions.add("1.0.3");
        versions.add("1.0.4");
        versions.add("1.0.5");
        versions.add("1.0.6");
        versions.add("1.0.7");
        versions.add("1.0.8");
        versions.add("1.0.9");
        versions.add("1.0.10");
        versions.add("1.0.11");
        versions.add("1.0.12");
        versions.add("1.0.13");
        versions.add("1.0.14");
        versions.add(BpmManager.DB_VERSION);
        update();
    }

    public void update() throws SQLException, IOException {
        // 获取数据库相关配置信息
        // 建立连接
        Connection conn = dataSource.getConnection();
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        // 创建ScriptRunner，用于执行SQL脚本
        ScriptRunner runner = new ScriptRunner(conn);
        String databaseProductName = conn.getMetaData().getDatabaseProductName();
        String databaseType;
        if (DbUpdateTask.DM_DATABASE_PRODUCT_NAME.equals(databaseProductName)) {
            databaseType = DATABASE_TYPE_DM;
        } else if (DbUpdateTask.ST_DATABASE_PRODUCT_NAME.equals(databaseProductName)) {
            databaseType = DATABASE_TYPE_ST;
        } else {
            databaseType = ProcessEngineConfigurationImpl.getDefaultDatabaseTypeMappings().getProperty(databaseProductName);
            if (databaseType == null) {
                throw new BpmException("couldn't deduct database type from database product name '" + databaseProductName + "'");
            }
        }
        runner.setErrorLogWriter(null);
        runner.setLogWriter(null);
        if (conn.getMetaData().getTables(null, databaseMetaData.getUserName(), TABLE_NAME, new String[]{TABLE_TYPE}).next()) {
            WfPropertyDO wfProperty = wfPropertyService.getById(VERSION_NAME);
            String version;
            if (wfProperty == null) {
                initVersion();
                version = BpmManager.DB_VERSION;
            } else {
                version = wfProperty.getValue();
                wfPropertyService.update(new WfPropertyDO(), new UpdateWrapper<WfPropertyDO>().set("VALUE", BpmManager.DB_VERSION).eq("NAME", VERSION_NAME));
            }
            Integer index = -1;
            for (int i = 0; i < versions.size(); i++) {
                if (StringUtils.equals(versions.get(i), version)) {
                    index = i + 1;
                    break;
                }
            }
            if (index.equals(-1)) {
                throw new BpmException(LocaleUtils.getBpmEarlierThenDb(BpmManager.DB_VERSION, version));
            }
            for (int i = index; i < versions.size(); i++) {
                runScript(databaseType, runner, i);
            }
        } else {
            runScript(databaseType, runner, 0);
            initVersion();
        }
        // 关闭连接
        conn.close();
    }

    private void initVersion() {
        WfPropertyDO wfProperty = new WfPropertyDO();
        wfProperty.setName(VERSION_NAME);
        wfProperty.setValue(BpmManager.DB_VERSION);
        wfPropertyService.save(wfProperty);
    }

    private void runScript(String databaseType, ScriptRunner runner, Integer index) throws IOException {
        String version;
        for (int i = index; i < versions.size(); i++) {
            // 执行SQL脚本
            version = versions.get(i);
            String scriptName = databaseType + "." + version + ".sql";
            LOGGER.debug("script running: " + scriptName);
            Reader reader = Resources.getResourceAsReader("com.csicit.ace.bpm.db/" + scriptName);
            runner.runScript(reader);
            LOGGER.debug("script run: " + scriptName);
            if (StringUtils.equals(version, "1.0.13")) {
                List<WfiFlowDO> wfiFlows = wfiFlowService.list(new QueryWrapper<>());
                List<String> flowIds = new ArrayList<>();
                Collection<WfiTaskPendingDO> wfiTaskPendings = new ArrayDeque<>();
                for (WfiFlowDO wfiFlow : wfiFlows) {
                    flowIds.add(wfiFlow.getId());
                    try {
                        bpmManager.resolveTaskPending(wfiTaskPendings, wfiFlow.getId(), FlowUtils.getFlow(wfiFlow.getModel()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        LOGGER.error(ex.getMessage());
                    }
                }
                if (flowIds.size() > 0) {
                    wfiTaskPendingService.clear(flowIds);
                }
                if (wfiTaskPendings.size() > 0) {
                    wfiTaskPendingService.saveBatch(wfiTaskPendings);
                }
            }
        }
    }
}
