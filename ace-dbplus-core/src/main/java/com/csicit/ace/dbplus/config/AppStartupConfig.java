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
 * ??????????????????
 *
 * @author shanwj
 * @date 2020/4/14 15:33
 */
@Configuration
@Order(-10)
public class AppStartupConfig implements ApplicationRunner {
    /**
     * asposeExecl ?????????
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
     * ????????????
     */
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void run(ApplicationArguments args) {
        if (StringUtils.isEmpty(Constants.BasePackages)) {
            throw new RException("??????????????????AceInitScan??????????????????????????????!");
        }
        setAsposeLicense();
        appStartupConfig.initSmKeyValue(appName);
        appStartupConfig.checkLicense();
        appStartupConfig.publishChannel(appName);

        //??????????????????api????????????????????????????????????
        InitStorageDataVO data = getInitStorageData();
        if (Objects.nonNull(data)) {
            data.setAppName(appName);
            appStartupConfig.saveInitScanData(data);

        }


        if (!Constants.AppNames.contains(appName)) {
            // ????????????
            boolean lockAppFlg = false;
            AppUpgrade appUpgrade;

            appUpgrade = getAppUpgrade();
            if (appUpgrade != null && Objects.equals(appUpgrade.getAppId(), appName)) {
                // ?????????
                lockAppFlg = lockApp(appUpgrade.getVersion());
            }

            //??????????????????
            if (lockAppFlg) {
                if (appUpdate(appUpgrade)) {
                    // ??????????????????
                    updAppVersion(appUpgrade.getVersion());
                }
                // ??????
                unLockApp(appUpgrade.getVersion());
            }
        }


    }

    /**
     * ??????????????????
     * ?????????????????????
     * api?????? ???????????????????????????
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
     * ??????????????????????????????
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
                        // ????????? ??????????????????????????????
                        if (Constants.isMonomerApp || Constants.AppNames.contains(appName)) {
                            scopeSet.add(1);
                        } else {
                            // ?????? ??????????????? ??????????????????
                            scopeSet.add(3);
                        }
                        configDO.setScopes(scopeSet);
                        // ??????
                        // ????????????????????? ???????????????
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
     * API??????????????????????????????????????????
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

            //??????api??????
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
            //????????????
            if (scheduled != null) {
                ScheduledVO scd = new ScheduledVO();
                scd.setName(scheduled.name());
                scd.setAppId(appName);
                scd.setUrl(scheduled.url());
                scd.setGroup(scheduled.group());
                //?????? ???????????????ScheduledVO ???equals??????
                if (CollectionUtils.isNotEmpty(scheduleds) && scheduleds.contains(scheduled)) {
                    throw new RException("?????????:" + scheduled.group() + " ?????????:" + scheduled.name() + "??????!");
                }
                scheduleds.add(scd);
            }
            //?????????????????????
            if (aceCustomMessenger != null) {
                SysMsgTypeExtendDO msgExtend = new SysMsgTypeExtendDO();
                msgExtend.setAppId(appName);
                msgExtend.setName(appName + "-" + aceCustomMessenger.name());
                msgExtend.setUrl(aceCustomMessenger.url());
                if (CollectionUtils.isNotEmpty(msgExtends) &&
                        msgExtends.stream().map(SysMsgTypeExtendDO::getName)
                                .collect(Collectors.toList()).contains(msgExtend.getName())) {
                    throw new RException("????????????????????????????????????????????????!");
                }
                msgExtends.add(msgExtend);
            }
        });
    }

    /**
     * aspose??????
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
            aceLogger.error("??????AsposeLicense??????!");
            e.printStackTrace();
        }
    }

    /**
     * ?????????????????????
     */
    private boolean lockApp(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("appName", appName);
        map.put("version", version);
        return appStartupConfig.lockApp(map);
    }

    /**
     * ??????????????????
     */
    private boolean unLockApp(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("appName", appName);
        map.put("version", version);
        return appStartupConfig.unLockApp(map);
    }

    /**
     * ?????????????????????????????????
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
     * ???????????????????????????
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
     * ??????????????????
     */
    private void updAppVersion(String version) {
        Map<String, String> map = new HashMap<>();
        map.put("appName", appName);
        map.put("version", version);
        appStartupConfig.updAppVersion(map);
    }

    /**
     * ??????DataBase
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
            throw new RException("???????????????DataBase?????????");
        }
    }

    /**
     * ????????????????????????
     */
    private boolean updateDbModel(AppUpgrade appUpgrade) {
        // ?????????????????????
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
     * ?????????????????????
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
        //???????????????true?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????false
        liquibase.setShouldRun(false);
        return liquibase;
    }
}
