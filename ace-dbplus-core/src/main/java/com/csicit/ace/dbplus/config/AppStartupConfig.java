package com.csicit.ace.dbplus.config;

import com.aspose.cells.License;
import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.AppUpgradeJaxb.GroupDatasourceDetail;
import com.csicit.ace.common.annotation.*;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import com.csicit.ace.common.pojo.vo.ScheduledVO;
import com.csicit.ace.common.utils.ScanClassUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.XmlUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用启动配置
 *
 * @author shanwj
 * @date 2020/4/14 15:33
 */
@Configuration
@Order(-10)
public class AppStartupConfig implements ApplicationRunner {
    /**
     * asposeExecl 授权码
     */
    private final static String licenseStr =
            "<License><Data><LicensedTo>IARI DEEPSOFT TECHNOLOGY CO., LTD</LicensedTo><EmailTo>huchangping1@sina.com" +
                    "</EmailTo><LicenseType>Developer OEM</LicenseType><LicenseNote>Limited to 1 developer, unlimited" +
                    " " +
                    "physical locations</LicenseNote><OrderID>181121011033</OrderID><UserID>134983271</UserID><OEM>" +
                    "This is a redistributable license</OEM><Products><Product>Aspose.Total Product Family</Product>" +
                    "</Products><EditionType>Enterprise</EditionType><SerialNumber>1072000f-ecb2-4c41-8fad" +
                    "-a0ed1548eec7" +
                    "</SerialNumber><SubscriptionExpiry>20191121</SubscriptionExpiry><LicenseVersion>3.0" +
                    "</LicenseVersion>" +
                    "<LicenseInstructions>https://purchase.aspose" +
                    ".com/policies/use-license</LicenseInstructions></Data>" +
                    "<Signature>AxmKMg/ujmWls/dGQz+Pqtlb9ko/U0FhjozJzsj6QIYyGTUVCVCcZR7PgW2vcIdNh/E" +
                    "+4x1dQAx7qyv78e1rPao" +
                    "Nkr74M60nZfLzIXVssjOByEPdymzQsKIc6vUbl2299vg+h4gsnmJgGrwsFuxpDUFSHqcS1CyKk2czl7NbpoM=</Signature" +
                    "></License>";

    @Autowired
    IAppStartupConfig appStartupConfig;

    @Autowired
    AceLogger aceLogger;

    @Autowired
    Map<String, ICustomUpdateScript> customUpdateScriptMap;

    DataSource dataSource;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void run(ApplicationArguments args) {
        if (StringUtils.isEmpty(Constants.BasePackages)) {
            throw new RException("启动类需配置AceInitScan注解，并填写扫描包名!");
        }
        setAsposeLicense();
        appStartupConfig.initSmKeyValue(appName);
        appStartupConfig.checkLicense();
        appStartupConfig.publishChannel(appName);

        //项目启动更新api、定时任务、消息发送方式
        InitStorageDataVO data = getInitStorageData();
        if (Objects.nonNull(data)) {
            data.setAppName(appName);
            appStartupConfig.saveInitScanData(data);

        }


        if (!Constants.AppNames.contains(appName)) {
            // 应用升级
            boolean lockAppFlg = false;
            AppUpgrade appUpgrade;

            appUpgrade = getAppUpgrade();
            if (appUpgrade != null && Objects.equals(appUpgrade.getAppId(), appName)) {
                // 事务锁
                lockAppFlg = lockApp(appUpgrade.getVersion());
            }

            //执行升级业务
            if (lockAppFlg) {
                if (appUpdate(appUpgrade)) {
                    // 应用版本升级
                    updAppVersion(appUpgrade.getVersion());
                }
                // 解锁
                unLockApp(appUpgrade.getVersion());
            }
        }


    }

    /**
     * 初始化扫描包
     * 将注解信息存储
     * api资源 自定义消息发送信使
     *
     * @return InitStorageDataVO
     * @author shanwj
     * @date 2020/4/13 19:27
     */
    private InitStorageDataVO getInitStorageData() {
        InitStorageDataVO initStorageData = new InitStorageDataVO();
        initStorageData.setAppName(appName);
        Set<Class<?>> controllerClasses = new LinkedHashSet<>();
        Set<Class<?>> configClasses = new LinkedHashSet<>();

        ScanClassUtils.getInitScanClass(Constants.BasePackages, controllerClasses, configClasses);
        List<SysApiResourceDO> apis = new ArrayList<>(16);
        List<SysMsgTypeExtendDO> msgExtends = new ArrayList<>(16);
        List<ScheduledVO> scheduleds = new ArrayList<>(16);
        List<SysConfigDO> sysConfigs = new ArrayList<>(16);
        controllerClasses.forEach(clazz -> {
            RequestMapping rm = clazz.getAnnotation(RequestMapping.class);
            if (rm == null || rm.value().length == 0) {
                return;
            }
            initControllerData(rm, clazz, apis, msgExtends, scheduleds);
        });
        configClasses.stream().forEach(clazz -> {
            AceConfigClass ac = clazz.getAnnotation(AceConfigClass.class);
            if (ac == null) {
                return;
            }
            initConfigData(clazz, sysConfigs);
        });
        initStorageData.setApis(apis);
        initStorageData.setMsgExtends(msgExtends);
        initStorageData.setScheduleds(scheduleds);
        initStorageData.setSysConfigs(sysConfigs);
        return initStorageData;
    }

    /**
     * 配置类配置项扫描获取
     *
     * @param clazz
     * @param sysConfigs
     */
    private void initConfigData(Class clazz, List<SysConfigDO> sysConfigs) {
        Field[] fields = clazz.getFields();
        Map<String, Set<Integer>> nameAndScopes = new HashMap<>();
        if (fields.length > 0) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                    AceConfigField aceConfigField = field.getAnnotation(AceConfigField.class);
                    if (aceConfigField != null) {
                        SysConfigDO configDO = new SysConfigDO();
                        if (StringUtils.isNotBlank(aceConfigField.name())) {
                            configDO.setName(aceConfigField.name());
                        } else {
                            configDO.setName(field.getName());
                        }

                        Set<Integer> scopeSet = new HashSet<>();
                        for (int i : aceConfigField.scopes()) {
                            if (i == 2) {
                                scopeSet.add(i);
                                break;
                            }
                        }
                        // 单机版 平台应用必须有租户级
                        if (Constants.isMonomerApp || Constants.AppNames.contains(appName)) {
                            scopeSet.add(1);
                        } else {
                            // 云版 非平台应用 必须有应用级
                            scopeSet.add(3);
                        }
                        configDO.setScopes(scopeSet);
                        // 注意
                        // 相同名称的配置 取范围大的
                        if (nameAndScopes.containsKey(configDO.getName())
                                && nameAndScopes.get(configDO.getName()).size() >= configDO.getScopes().size()) {
                            continue;
                        }
                        nameAndScopes.put(configDO.getName(), configDO.getScopes());
                        configDO.setAppId(appName);
                        configDO.setType(field.getType().getSimpleName());
                        configDO.setValue(aceConfigField.defaultValue());
                        configDO.setRemark(aceConfigField.remark());
                        configDO.setUpdateType(aceConfigField.updateType());
                        sysConfigs.add(configDO);
                    }
                }
            }
        }
    }

    /**
     * API资源、拓展消息信使、计划任务
     *
     * @param rm
     * @param clazz
     * @param apis
     * @param msgExtends
     * @param scheduleds
     */
    private void initControllerData(RequestMapping rm, Class clazz, List<SysApiResourceDO> apis,
                                    List<SysMsgTypeExtendDO> msgExtends,
                                    List<ScheduledVO> scheduleds) {
        Arrays.asList(clazz.getDeclaredMethods()).forEach(method -> {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            String value;
            String methodStr;
            if (requestMapping != null) {
                value = (requestMapping.value().length > 0 ? requestMapping.value()[0] : "");
                methodStr = requestMapping.method().length > 0 ? requestMapping.method()[0].toString() : "GET";
            } else if (getMapping != null) {
                value = (getMapping.value().length > 0 ? getMapping.value()[0] : "");
                methodStr = "GET";
            } else if (postMapping != null) {
                value = (postMapping.value().length > 0 ? postMapping.value()[0] : "");
                methodStr = "POST";
            } else if (putMapping != null) {
                value = (putMapping.value().length > 0 ? putMapping.value()[0] : "");
                methodStr = "PUT";
            } else if (deleteMapping != null) {
                value = (deleteMapping.value().length > 0 ? deleteMapping.value()[0] : "");
                methodStr = "DELETE";
            } else {
                return;
            }
            AceAuth aceAuth = method.getAnnotation(AceAuth.class);
            AceCustomMessenger aceCustomMessenger = method.getAnnotation(AceCustomMessenger.class);
            AceScheduled scheduled = method.getAnnotation(AceScheduled.class);

            //权限api资源
            if (aceAuth != null) {
                String authId = clazz.getName() + "." + method.getName() + "_" + appName;
                String authName = aceAuth.value();
                SysApiResourceDO sysApiResourceDO = new SysApiResourceDO();
                sysApiResourceDO.setAppId(appName);
                sysApiResourceDO.setId(authId);
                sysApiResourceDO.setName(authName);
                sysApiResourceDO.setApiUrl(
                        rm.value()[0] + value);
                sysApiResourceDO.setApiMethod(methodStr);
                apis.add(sysApiResourceDO);
            }
            //计划任务
            if (scheduled != null) {
                ScheduledVO scd = new ScheduledVO();
                scd.setName(scheduled.name());
                scd.setAppId(appName);
                scd.setUrl(scheduled.url());
                scd.setGroup(scheduled.group());
                //注意 这里重写了ScheduledVO 的equals方法
                if (CollectionUtils.isNotEmpty(scheduleds) && scheduleds.contains(scheduled)) {
                    throw new RException("任务组:" + scheduled.group() + " 下任务:" + scheduled.name() + "重复!");
                }
                scheduleds.add(scd);
            }
            //自定义消息信使
            if (aceCustomMessenger != null) {
                SysMsgTypeExtendDO msgExtend = new SysMsgTypeExtendDO();
                msgExtend.setAppId(appName);
                msgExtend.setName(appName + "-" + aceCustomMessenger.name());
                msgExtend.setUrl(aceCustomMessenger.url());
                if (CollectionUtils.isNotEmpty(msgExtends) &&
                        msgExtends.stream().map(SysMsgTypeExtendDO::getName)
                                .collect(Collectors.toList()).contains(msgExtend.getName())) {
                    throw new RException("存在名称一样的自定义消息发送方式!");
                }
                msgExtends.add(msgExtend);
            }
        });
    }

    /**
     * aspose授权
     *
     * @param
     * @return void
     * @author shanwj
     * @date 2020/4/27 11:30
     */
    private void setAsposeLicense() {
        try {
            InputStream license = new ByteArrayInputStream(licenseStr.getBytes());
            License aposeLic = new License();
            aposeLic.setLicense(license);
        } catch (Exception e) {
            aceLogger.error("授权AsposeLicense异常!");
            e.printStackTrace();
        }
    }

    /**
     * 应用升级事务锁
     */
    private boolean lockApp(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("appName", appName);
        map.put("version", version);
        return appStartupConfig.lockApp(map);
    }

    /**
     * 应用升级解锁
     */
    private boolean unLockApp(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("appName", appName);
        map.put("version", version);
        return appStartupConfig.unLockApp(map);
    }

    /**
     * 获取应用配置项文件信息
     */
    private AppUpgrade getAppUpgrade() {
        AppUpgrade appUpgrade = null;
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //Resource resource = new ClassPathResource("/db/appConfig.xml");
        try {
            Resource[] resources = resolver.getResources("db/*.xml");
            if (resources != null && resources.length > 0) {
                for (Resource resource : resources) {
                    if (Objects.equals(resource.getFilename(), "appConfig.xml")) {
                        InputStream is = resources[0].getInputStream();
                        byte[] bytes;
                        bytes = new byte[is.available()];
                        is.read(bytes);
                        String str = new String(bytes);
                        appUpgrade = (AppUpgrade) XmlUtils.unmarshalXml(str, AppUpgrade.class);
                        is.close();
                    }
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return appUpgrade;
    }


    /**
     * 应用启动时更新操作
     */
    private boolean appUpdate(AppUpgrade appUpgrade) {
        if (updateDbModel(appUpgrade)) {
            if (updateDbItem(appUpgrade)) {
                updateCustomScript();
                return true;
            }
        }
        return false;
    }

    /**
     * 应用版本升级
     */
    private void updAppVersion(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("appName", appName);
        map.put("version", version);
        appStartupConfig.updAppVersion(map);
    }

    /**
     * 获取DataBase
     *
     * @return
     */
    private Database getDatabase(GroupDatasourceDetail groupDatasourceDetail) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(groupDatasourceDetail.getDriverName());
            hikariConfig.setJdbcUrl(groupDatasourceDetail.getUrl());
            hikariConfig.setUsername(groupDatasourceDetail.getUsername());
            hikariConfig.setPassword(groupDatasourceDetail.getPassword());
            dataSource = new HikariDataSource(hikariConfig);
            Connection jdbcConnection = null;
            jdbcConnection = dataSource.getConnection();
            if (!jdbcConnection.getAutoCommit()) {
                jdbcConnection.commit();
            }
            DatabaseConnection connection = new JdbcConnection(jdbcConnection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
            return database;
        } catch (Exception e) {
            throw new RException("获取数据源DataBase出错！");
        }
    }

    /**
     * 更新应用数据结构
     */
    private boolean updateDbModel(AppUpgrade appUpgrade) {
        // 获取数据源信息
        List<GroupDatasourceDetail> groupDatasourceDetails = appUpgrade.getGroupDatasource()
                .stream().filter(item -> item.getMajor() == 1).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(groupDatasourceDetails)) {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            try {
                Resource[] resources = resolver.getResources("/db/changelog/*.xml");
                if (resources != null && resources.length > 0) {
                    for (Resource resource : resources) {
                        if (Objects.equals(resource.getFilename(), "dbChangelog.xml")) {
                            InputStream is = resource.getInputStream();
                            Liquibase liquibase = new Liquibase(is, "xml", new
                                    ClassLoaderResourceAccessor(), getDatabase(groupDatasourceDetails.get(0)));
                            if (liquibase != null) {
                                liquibase.update(new Contexts(), new LabelExpression());
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (LiquibaseException e) {
                e.printStackTrace();
            }
//            Resource resource = new ClassPathResource("/db/changelog/dbChangelog.xml");
        } else {
            return false;
        }
        return true;
    }

    /**
     * 更新应用数据项
     */
    private boolean updateDbItem(AppUpgrade appUpgrade) {
        return appStartupConfig.updateDbItem(appUpgrade);
    }

    private void updateCustomScript() {
        for (Map.Entry<String, ICustomUpdateScript> entry : customUpdateScriptMap.entrySet()) {
            entry.getValue().updateCustomScript();
        }
    }


    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setContexts("development,test,production");
        liquibase.setDropFirst(false);
        //如果设置为true：第一次执行不会报错，第二次将会报错，导致程序无法启动，所以第一次执行完后一定要改为：false
        liquibase.setShouldRun(false);
        return liquibase;
    }
}
