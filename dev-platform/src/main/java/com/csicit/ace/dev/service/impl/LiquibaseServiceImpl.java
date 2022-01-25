package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dev.service.*;
import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.CatalogAndSchema;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LiquibaseServiceImpl implements LiquibaseService {

    @Autowired
    ProServiceService proServiceService;

    @Autowired
    ProModelService proModelService;

    @Autowired
    ProModelColService proModelColService;

    @Autowired
    ProModelIndexService proModelIndexService;

    @Autowired
    ProModelAssociationService proModelAssociationService;

    @Autowired
    ProChangelogHistoryService proChangelogHistoryService;

    @Autowired
    ProDatasourceService proDatasourceService;

    @Autowired
    SecurityUtils securityUtils;

    DataSource dataSource;

    private static final Map<String, String> DATA_TYPE = ImmutableMap.<String, String>builder()
            .put("Bigint", "BIGINT")
            .put("Boolean", "BOOLEAN")
            .put("Char", "CHAR")
            .put("Varchar", "VARCHAR")
            .put("Datetime", "DATETIME")
            .put("Date", "DATE")
            .put("Timestamp", "TIMESTAMP")
            .put("Decimal", "DECIMAL")
            .put("Double", "DOUBLE")
            .put("Float", "FLOAT")
            .put("Int", "INT")
            .put("Number", "NUMBER")
            .put("CLOB", "CLOB")
            .put("BLOB", "BLOB")
            .build();

    private List<String> getChangeLogTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("generator/liquibase/db.changelog-version.xml.vm");
        return templates;
    }


    /**
     * 通过liqibase生成第一版changelog
     *
     * @param datasourceDO
     * @return
     * @author zuogang
     * @date 2020/5/21 17:26
     */
    public Map<String, Object> getFirstChangelogByDs(ProDatasourceDO datasourceDO) {
        Liquibase liquibase = new Liquibase("", new
                ClassLoaderResourceAccessor(), getDatabase(datasourceDO));

        Map<String, Object> map = new HashMap<>();
        if (liquibase != null) {
            CatalogAndSchema catalogAndSchema = new CatalogAndSchema(null, null);
            DiffToChangeLog changeLogWriter = new DiffToChangeLog(new DiffOutputControl());

            try {
                map = liquibase.generateChangeLogF(catalogAndSchema, changeLogWriter);
            } catch (DatabaseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return map;
    }


    /**
     * 设置changelog
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/12/27 10:23
     */
    private void setChangeLogInfo(ZipOutputStream zip, List<String> changesets, String version) {
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("changesets", changesets);
        VelocityContext context = new VelocityContext(map);

        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate("generator/liquibase/db.changelog-version.xml.vm", "UTF-8");
        tpl.merge(context, sw);
        try {
            //添加到zip
            zip.putNextEntry(new ZipEntry("db.changelog-" + version + ".xml"));
            IOUtils.write(sw.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(sw);
            zip.closeEntry();
        } catch (IOException e) {
            try {
                throw new Exception("渲染模板失败", e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * 生成changlog
     *
     * @return
     */
    @Override
    public void createChangeLogNewVersion(String serviceId, ServletOutputStream outputStream) {

        ZipOutputStream zip = new ZipOutputStream(outputStream);

        List<String> logValues = proChangelogHistoryService.list(new QueryWrapper<ProChangelogHistoryDO>().eq
                ("IS_USE_LESS", 1).eq("service_id", serviceId).eq("publish_tag", 0)
                .orderByAsc("create_time")).stream().map(ProChangelogHistoryDO::getLogValue)
                .collect(Collectors.toList());
        List<String> changesets = new ArrayList<>(16);
        logValues.forEach(logValue -> {
            changesets.add(logValue.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        });
        ProChangelogHistoryDO proChangelogHistoryDO = proChangelogHistoryService.getOne(new
                QueryWrapper<ProChangelogHistoryDO>()
                .eq("service_id", serviceId).orderByDesc("publish_version"));

        this.setChangeLogInfo(zip, changesets, proChangelogHistoryDO.getPublishVersion() == null ? "1.0.0" :
                ("1.0." + (proChangelogHistoryDO.getPublishVersion() + 1)));
        proChangelogHistoryService.publishChangeLog(serviceId);
        IOUtils.closeQuietly(zip);
        System.out.println("---createChangeLogNewVersion over---");
    }

    /**
     * 生成changlog
     *
     * @return
     */
    @Override
    public void createChangeLogAllVersion(String serviceId, ServletOutputStream outputStream) {
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        List<String> logValues = proChangelogHistoryService.list(new QueryWrapper<ProChangelogHistoryDO>()
                .eq("IS_USE_LESS", 1).eq("service_id", serviceId).orderByAsc("create_time"))
                .stream().map(ProChangelogHistoryDO::getLogValue)
                .collect(Collectors.toList());
        List<String> changesets = new ArrayList<>(16);
        logValues.forEach(logValue -> {
            changesets.add(logValue.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        });

        this.setChangeLogInfo(zip, changesets, "all");
        IOUtils.closeQuietly(zip);
        System.out.println("---createChangeLogAllVersion over---");
    }


    /**
     * 获取DataBase
     *
     * @return
     */
    @Override
    public Database getDatabase(ProDatasourceDO proDatasourceDO) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(proDatasourceDO.getDriver());
            hikariConfig.setJdbcUrl(proDatasourceDO.getUrl());
            hikariConfig.setUsername(proDatasourceDO.getUserName());
            hikariConfig.setPassword(proDatasourceDO.getPassword());
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
     * 执行liquibase的update方法
     */
    private boolean updateLiquibase(String xml, ProDatasourceDO proDatasourceDO) {
        xml = "<databaseChangeLog xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\" xmlns:xsi=\"http://www" +
                ".w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog " +
                "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.9.xsd\">" + xml + "</databaseChangeLog>";
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        Liquibase liquibase = new Liquibase(is, "xml", new
                ClassLoaderResourceAccessor(), getDatabase(proDatasourceDO));
        if (liquibase != null) {
            try {
                liquibase.update(new Contexts(), new LabelExpression());
            } catch (LiquibaseException e) {
                String message = e.getMessage();
                throw new RException(message);
            }
        }
        return true;
    }

    /**
     * 新增数据表
     *
     * @return
     */
    @Override
    public boolean addLiquibaseTable(ProModelDO instance) {
        ProServiceDO serviceDO = proServiceService.getById(instance.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", instance.getServiceId()));
        try {
            // 范例：
            //  <changeSet author="HP (generated)" id="23-1">
            //    <createTable  remarks="基础应用权限"  tableName="BD_APP_AUTH">
            //    <column name="ID" remarks="权限标识" type="VARCHAR(50)">
            //        <constraints nullable="false" /></column>
            //    </createTable>
            //    </changeSet>
            Document documentTable = null;
            //创建document文档
            documentTable = DocumentHelper.createDocument();
            documentTable.setXMLEncoding("UTF-8");
            Element changeSetElem = DocumentHelper.createElement("changeSet");
            changeSetElem.addAttribute("author", securityUtils.getCurrentUserName());
            String tableId = serviceDO.getAppId() + "-" + instance.getTableName() + "-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            changeSetElem.addAttribute("id", tableId);
            Element createTableElem = DocumentHelper.createElement("createTable");
            createTableElem.addAttribute("remarks", instance.getModelName());
            createTableElem.addAttribute("tableName", instance.getTableName());
            Element columnElem = DocumentHelper.createElement("column");
            columnElem.addAttribute("name", "ID");
            columnElem.addAttribute("remarks", "主键");
            columnElem.addAttribute("type", "VARCHAR(50)");
            Element constraintsElem = DocumentHelper.createElement("constraints");
            constraintsElem.addAttribute("nullable", "false");
            columnElem.add(constraintsElem);
            createTableElem.add(columnElem);
            changeSetElem.add(createTableElem);
            documentTable.add(changeSetElem);


            //    <changeSet author="HP (generated)" id="23-113" objectQuotingStrategy="QUOTE_ALL_OBJECTS">
            //    <addPrimaryKey columnNames="ID" constraintName="CONS134219580" tableName="BD_APP_AUTH"/>
            //    </changeSet>

            // 创建表时默认生成一个ID主键字段
            Document documentId = null;
            documentId = DocumentHelper.createDocument();
            documentId.setXMLEncoding("UTF-8");
            Element changeSetElemId = DocumentHelper.createElement("changeSet");
            changeSetElemId.addAttribute("author", securityUtils.getCurrentUserName());
            String pkId = serviceDO.getAppId() + "-" + instance.getTableName() + "-PRIMARY-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            changeSetElemId.addAttribute("id", pkId);
            changeSetElemId.addAttribute("objectQuotingStrategy", "QUOTE_ALL_OBJECTS");
            Element addPrimaryKeyElemId = DocumentHelper.createElement("addPrimaryKey");
            addPrimaryKeyElemId.addAttribute("columnNames", "ID");
            addPrimaryKeyElemId.addAttribute("constraintName", instance.getPkName());
            addPrimaryKeyElemId.addAttribute("tableName", instance.getTableName());
            changeSetElemId.add(addPrimaryKeyElemId);
            documentId.add(changeSetElemId);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(documentTable.asXML().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(documentId.asXML().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            if (!updateLiquibase(stringBuffer.toString(), proDatasourceDO)) {
                return false;
            }

            if (!proChangelogHistoryService.saveChangelogHistory(tableId, documentTable.asXML(),
                    instance.getServiceId(), proDatasourceDO.getId())) {
                return false;
            }

            if (!proChangelogHistoryService.saveChangelogHistory(pkId, documentId.asXML(),
                    instance.getServiceId(), proDatasourceDO.getId())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 修改数据表
     *
     * @return
     */
    @Override
    public boolean updLiquibaseTable(ProModelDO instance) {
        ProServiceDO serviceDO = proServiceService.getById(instance.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", instance.getServiceId()));
        ProModelDO oldModelDO = proModelService.getById(instance.getId());
        try {
            // 修改表注释
            Document documentTableRemarks = null;
            String tableId = "";
            if (!Objects.equals(oldModelDO.getModelName(), instance.getModelName())) {

                //  <changeSet author="HP (generated)" id="43-1">
                //    <setTableRemarks remarks="基础应用权限TEST" tableName="BD_APP_AUTH"/>
                //    </changeSet>

                //创建document文档
                documentTableRemarks = DocumentHelper.createDocument();
                documentTableRemarks.setXMLEncoding("UTF-8");
                Element changeSetElem = DocumentHelper.createElement("changeSet");
                changeSetElem.addAttribute("author", securityUtils.getCurrentUserName());
                tableId = serviceDO.getAppId() + "-" + oldModelDO.getTableName() + "-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changeSetElem.addAttribute("id", tableId);
                Element setTableRemarksElem = DocumentHelper.createElement("setTableRemarks");
                setTableRemarksElem.addAttribute("remarks", instance.getModelName());
                setTableRemarksElem.addAttribute("tableName", oldModelDO.getTableName());
                changeSetElem.add(setTableRemarksElem);
                documentTableRemarks.add(changeSetElem);

            }

            // 修改表名
            Document documentRenameTable = null;
            String tableId2 = "";
            if (!Objects.equals(oldModelDO.getTableName(), instance.getTableName())) {

                //     <changeSet id="HP (generated)" author="32654">
                //    <renameTable oldTableName="BD_APP_AUTH" newTableName="BD_APP_AUTH_TEST"/>
                //    </changeSet>

                //创建document文档
                documentRenameTable = DocumentHelper.createDocument();
                documentRenameTable.setXMLEncoding("UTF-8");
                Element changeSetElem = DocumentHelper.createElement("changeSet");
                changeSetElem.addAttribute("author", securityUtils.getCurrentUserName());
                tableId2 = serviceDO.getAppId() + "-" + instance.getTableName() + "-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changeSetElem.addAttribute("id", tableId2);
                Element renameTableElem = DocumentHelper.createElement("renameTable");
                renameTableElem.addAttribute("oldTableName", oldModelDO.getTableName());
                renameTableElem.addAttribute("newTableName", instance.getTableName());
                changeSetElem.add(renameTableElem);
                documentRenameTable.add(changeSetElem);

            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(documentTableRemarks == null ? "" : documentTableRemarks.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(documentRenameTable == null ? "" : documentRenameTable.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            if (!updateLiquibase(stringBuffer.toString(), proDatasourceDO)) {
                return false;
            }
            if (documentTableRemarks != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(tableId, documentTableRemarks.asXML(),
                        instance.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }
            if (documentRenameTable != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(tableId2, documentRenameTable.asXML(),
                        instance.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 删除数据表
     *
     * @return
     */
    @Override
    public boolean delLiquibaseTable(ProModelDO modelDO) {
        ProServiceDO serviceDO = proServiceService.getById(modelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", modelDO.getServiceId()));
        try {

            //   <changeSet author="HP (generated)" id="1584438597063-74">
            //    <dropTable tableName="SYS_WAIT_GRANT_USER"/>
            //    </changeSet>

            Document documentDropTableRemark = null;
            //创建document文档
            documentDropTableRemark = DocumentHelper.createDocument();
            documentDropTableRemark.setXMLEncoding("UTF-8");
            Element changeSetElem = DocumentHelper.createElement("changeSet");
            changeSetElem.addAttribute("author", securityUtils.getCurrentUserName());
            String tableId = serviceDO.getAppId() + "-" + modelDO.getTableName() + "-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            changeSetElem.addAttribute("id", tableId);
            Element dropTableElem = DocumentHelper.createElement("dropTable");
            dropTableElem.addAttribute("tableName", modelDO.getTableName());
            changeSetElem.add(dropTableElem);
            documentDropTableRemark.add(changeSetElem);

            if (!updateLiquibase(documentDropTableRemark.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""), proDatasourceDO)) {
                return false;
            }

            if (!proChangelogHistoryService.saveChangelogHistory(tableId, documentDropTableRemark.asXML(),
                    modelDO.getServiceId(), proDatasourceDO.getId())) {
                return false;
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 新增数据列
     *
     * @return
     */
    @Override
    public boolean addLiquibaseCol(ProModelColDO proModelColDO) {
        ProModelDO proModelDO = proModelService.getById(proModelColDO.getModelId());
        ProServiceDO serviceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));

        try {

            //   <changeSet author="HP (generated)" id="1584426969480-501">
            //    <addColumn tableName="BD_APP_AUTH">
            //    <column name="TEST_ID" remarks="应用ID" type="VARCHAR(50)"/>
            //    </addColumn>
            //    </changeSet>

            Document documenCol = null;
            documenCol = DocumentHelper.createDocument();
            documenCol.setXMLEncoding("UTF-8");
            Element changeSetElemId = DocumentHelper.createElement("changeSet");
            changeSetElemId.addAttribute("author", securityUtils.getCurrentUserName());
            String colId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-COL-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            changeSetElemId.addAttribute("id", colId);
            Element addColumnElemId = DocumentHelper.createElement("addColumn");
            addColumnElemId.addAttribute("tableName", proModelDO.getTableName());
            Element columnElemId = DocumentHelper.createElement("column");
            columnElemId.addAttribute("name", proModelColDO.getTabColName());
            columnElemId.addAttribute("remarks", proModelColDO.getCaption());
            // 判断数据类型
            this.setColumnDataTypeAndDefaultValue(columnElemId, "type", proModelColDO.getDataType(), proModelColDO
                    .getDataPrecision(), proModelColDO.getDataScale(), proModelColDO.getDefaultValue());

            // 判断能否为空
            if (Objects.equals(0, proModelColDO.getNullable())) {
                Element constraintsElemId = DocumentHelper.createElement("constraints");
                constraintsElemId.addAttribute("nullable", "false");
                columnElemId.add(constraintsElemId);
            }
            addColumnElemId.add(columnElemId);
            changeSetElemId.add(addColumnElemId);
            documenCol.add(changeSetElemId);

            Document documentpk = null;
            String pkId = "";
            Document documentdroppk = null;
            String droppkId = "";

            // 判断是否主键
            if (Objects.equals(proModelColDO.getPkFlg(), 1)) {

                // 判断该模型是否已拥有主键字段
                String pkNames = "";
                List<String> colNames = proModelColService.list(new QueryWrapper<ProModelColDO>()
                        .eq("pk_flg", 1).eq("model_id", proModelDO.getId()))
                        .stream().map(ProModelColDO::getTabColName).collect(Collectors.toList());
                if (colNames.size() > 0) {
                    // 先删除该模型主键
                    for (String name : colNames) {
                        pkNames = pkNames + name + ", ";
                    }

                    // <changeSet author="HP (generated)" id="1584435842891-35">
                    //   <dropPrimaryKey tableName="BD_APP_AUTH" constraintName="CONS134219580"/>
                    //  </changeSet>

                    documentdroppk = DocumentHelper.createDocument();
                    documentdroppk.setXMLEncoding("UTF-8");

                    Element changeSetElemId3 = DocumentHelper.createElement("changeSet");
                    changeSetElemId3.addAttribute("author", securityUtils.getCurrentUserName());
                    droppkId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-PRIMARY-" + System
                            .currentTimeMillis() + "-" + UuidUtils.createUUID();
                    changeSetElemId3.addAttribute("id", droppkId);
                    Element dropPrimaryKeyElemId = DocumentHelper.createElement("dropPrimaryKey");
                    dropPrimaryKeyElemId.addAttribute("tableName", proModelDO.getTableName());
                    dropPrimaryKeyElemId.addAttribute("constraintName", proModelDO.getPkName());
                    changeSetElemId3.add(dropPrimaryKeyElemId);
                    documentdroppk.add(changeSetElemId3);
                }
                // 再创建模型主键
                documentpk = DocumentHelper.createDocument();
                documentpk.setXMLEncoding("UTF-8");
                Element changeSetElemId2 = DocumentHelper.createElement("changeSet");
                changeSetElemId2.addAttribute("author", securityUtils.getCurrentUserName());
                pkId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-PRIMARY-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changeSetElemId2.addAttribute("id", pkId);
                changeSetElemId2.addAttribute("objectQuotingStrategy", "QUOTE_ALL_OBJECTS");
                Element addPrimaryKeyElemId = DocumentHelper.createElement("addPrimaryKey");
                addPrimaryKeyElemId.addAttribute("columnNames", pkNames + proModelColDO.getTabColName());
                addPrimaryKeyElemId.addAttribute("constraintName", proModelDO.getPkName());
                addPrimaryKeyElemId.addAttribute("tableName", proModelDO.getTableName());
                changeSetElemId2.add(addPrimaryKeyElemId);
                documentpk.add(changeSetElemId2);
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(documenCol.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
//            stringBuffer.append(defaultValueDocument == null ? "" : defaultValueDocument.asXML().replace("<?xml " +
//                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(documentdroppk == null ? "" : documentdroppk.asXML()
                    .replace("<?xml " +
                            "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(documentpk == null ? "" : documentpk.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            if (!updateLiquibase(stringBuffer.toString(), proDatasourceDO)) {
                return false;
            }

            if (!proChangelogHistoryService.saveChangelogHistory(colId, documenCol.asXML(),
                    proModelDO.getServiceId(), proDatasourceDO.getId())) {
                return false;
            }
            if (documentdroppk != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(droppkId, documentdroppk.asXML(),
                        proModelDO.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }
            if (documentpk != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(pkId, documentpk.asXML(),
                        proModelDO.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 设置数据列属性及默认值
     *
     * @return
     */
    private void setColumnDataTypeAndDefaultValue(Element columnElemId, String columnDataType, String dataType, Integer
            dataPrecision, Integer dataScale, String defaultValue) {

        if (StringUtils.isNotBlank(defaultValue)) {
            if (Objects.equals("Bigint", dataType) || Objects.equals("Decimal", dataType) || Objects.equals("Double",
                    dataType)
                    || Objects.equals("Float", dataType) || Objects.equals("Int", dataType) || Objects.equals
                    ("Number", dataType)) {
                columnElemId.addAttribute("defaultValueNumeric", defaultValue);
            } else if (Objects.equals("Boolean", dataType)) {
                columnElemId.addAttribute("defaultValueBoolean", defaultValue);
            } else if (Objects.equals("Varchar", dataType) || Objects.equals("Char", dataType)) {
                columnElemId.addAttribute("defaultValue", defaultValue);
            } else if (Objects.equals("Datetime", dataType) || Objects.equals("Date", dataType) || Objects.equals
                    ("Timestamp", dataType)) {
                columnElemId.addAttribute("defaultValueDate", defaultValue);
            }
        }
        this.setColumnDataType(columnElemId, columnDataType, dataType, dataPrecision, dataScale);

    }

    /**
     * 设置数据列属性
     *
     * @return
     */
    private void setColumnDataType(Element element, String columnDataType, String dataType, Integer dataPrecision,
                                   Integer dataScale) {
        if (Objects.equals("Char", dataType) || Objects.equals("Varchar", dataType) || Objects.equals("Decimal",
                dataType)
                || Objects.equals("Double", dataType) || Objects.equals("Float", dataType) || Objects.equals
                ("Number", dataType)) {
            StringJoiner stringJoiner = new StringJoiner("");
            stringJoiner.add(DATA_TYPE.get(dataType));
            stringJoiner.add("(");
            if (dataScale == 0) {
                stringJoiner.add(dataPrecision.toString());
            } else {
                stringJoiner.add(dataPrecision.toString());
                stringJoiner.add(", ");
                stringJoiner.add(dataScale.toString());
            }
            stringJoiner.add(")");
            element.addAttribute(columnDataType, stringJoiner.toString());
        } else {
            element.addAttribute(columnDataType, DATA_TYPE.get(dataType));
        }
    }

    /**
     * 修改数据列
     *
     * @return
     */
    @Override
    public boolean updLiquibaseCol(ProModelColDO proModelColDO) {
        ProModelDO proModelDO = proModelService.getById(proModelColDO.getModelId());
        ProServiceDO serviceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));

        ProModelColDO oldCol = proModelColService.getById(proModelColDO.getId());
        try {

            // 是否主键改变
            Document pkDocument = null;
            String pkId = "";
            Document dropPkDocument = null;
            String dropPkId = "";
            if (!Objects.equals(oldCol.getPkFlg(), proModelColDO.getPkFlg())) {
                String pkNames = "";
                List<String> colNames = proModelColService.list(new QueryWrapper<ProModelColDO>()
                        .eq("pk_flg", 1).eq("model_id", proModelDO.getId()))
                        .stream().map(ProModelColDO::getTabColName).collect(Collectors.toList());
                if (colNames.size() > 0) {
                    // 先删除该模型主键
                    for (String name : colNames) {
                        pkNames = pkNames + name + ", ";
                    }
                    dropPkDocument = DocumentHelper.createDocument();
                    dropPkDocument.setXMLEncoding("UTF-8");
                    Element changeSetElemId = DocumentHelper.createElement("changeSet");
                    changeSetElemId.addAttribute("author", securityUtils.getCurrentUserName());
                    dropPkId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-PRIMARY-" + System
                            .currentTimeMillis() + "-" + UuidUtils.createUUID();
                    changeSetElemId.addAttribute("id", dropPkId);
                    Element dropPrimaryKeyElemId = DocumentHelper.createElement("dropPrimaryKey");
                    dropPrimaryKeyElemId.addAttribute("tableName", proModelDO.getTableName());
                    dropPrimaryKeyElemId.addAttribute("constraintName", proModelDO.getPkName());
                    changeSetElemId.add(dropPrimaryKeyElemId);
                    dropPkDocument.add(changeSetElemId);
                }
                if (Objects.equals(proModelColDO.getPkFlg(), 1)) {
                    // 设置为主键
                    pkDocument = DocumentHelper.createDocument();
                    pkDocument.setXMLEncoding("UTF-8");
                    Element changeSetElemId2 = DocumentHelper.createElement("changeSet");
                    changeSetElemId2.addAttribute("author", securityUtils.getCurrentUserName());
                    pkId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-PRIMARY-" + System
                            .currentTimeMillis() + "-" + UuidUtils.createUUID();
                    changeSetElemId2.addAttribute("id", pkId);
                    changeSetElemId2.addAttribute("objectQuotingStrategy", "QUOTE_ALL_OBJECTS");
                    Element addPrimaryKeyElemId = DocumentHelper.createElement("addPrimaryKey");
                    addPrimaryKeyElemId.addAttribute("columnNames", pkNames + oldCol.getTabColName());
                    addPrimaryKeyElemId.addAttribute("constraintName", proModelDO.getPkName());
                    addPrimaryKeyElemId.addAttribute("tableName", proModelDO.getTableName());
                    changeSetElemId2.add(addPrimaryKeyElemId);
                    pkDocument.add(changeSetElemId2);
                } else {
                    // 去除主键设置
                    if (colNames.size() > 1) {
                        String newpkNames = "";
                        List<String> newcolNames = proModelColService.list(new QueryWrapper<ProModelColDO>()
                                .ne("id", proModelColDO.getId()).eq("pk_flg", 1).eq("model_id", proModelDO.getId()))
                                .stream().map(ProModelColDO::getTabColName).collect(Collectors.toList());
                        for (int i = 0; i < newcolNames.size(); i++) {
                            if (i == newcolNames.size() - 1) {
                                newpkNames = newpkNames + newcolNames.get(i);
                            } else {
                                newpkNames = newpkNames + newcolNames.get(i) + ", ";
                            }
                        }
                        pkDocument = DocumentHelper.createDocument();
                        pkDocument.setXMLEncoding("UTF-8");
                        Element changeSetElemId2 = DocumentHelper.createElement("changeSet");
                        changeSetElemId2.addAttribute("author", securityUtils.getCurrentUserName());
                        pkId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-PRIMARY-" + System
                                .currentTimeMillis() + "-" + UuidUtils.createUUID();
                        changeSetElemId2.addAttribute("id", pkId);
                        changeSetElemId2.addAttribute("objectQuotingStrategy", "QUOTE_ALL_OBJECTS");
                        Element addPrimaryKeyElemId = DocumentHelper.createElement("addPrimaryKey");
                        addPrimaryKeyElemId.addAttribute("columnNames", newpkNames);
                        addPrimaryKeyElemId.addAttribute("constraintName", proModelDO.getPkName());
                        addPrimaryKeyElemId.addAttribute("tableName", proModelDO.getTableName());
                        changeSetElemId2.add(addPrimaryKeyElemId);
                        pkDocument.add(changeSetElemId2);
                    }
                }
            }

            //      <changeSet id="234" author="ter">
            //        <editColumn tableName="SYS_APP" oldColumnName="INT_VALUE_VL" newColumnName="INT_VALUE_VLQ"
            //            columnDataType="VARCHAR(50)" nullable="0" remarks="DWFEW" defaultValue="3221"></editColumn>
            //    </changeSet>

            Document editColumnDocument = null;
            String editColumnId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-COL-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            editColumnDocument = DocumentHelper.createDocument();
            editColumnDocument.setXMLEncoding("UTF-8");
            Element changeSetElemId1 = DocumentHelper.createElement("changeSet");
            changeSetElemId1.addAttribute("author", securityUtils.getCurrentUserName());
            changeSetElemId1.addAttribute("id", editColumnId);
            Element editColumnElement = DocumentHelper.createElement("editColumn");
            editColumnElement.addAttribute("tableName", proModelDO.getTableName());
            editColumnElement.addAttribute("oldColumnName", oldCol.getTabColName());
            editColumnElement.addAttribute("newColumnName", proModelColDO.getTabColName());
            this.setColumnDataType(editColumnElement, "columnDataType", proModelColDO.getDataType(), proModelColDO
                    .getDataPrecision(), proModelColDO.getDataScale());
            editColumnElement.addAttribute("nullable", proModelColDO.getNullable().toString());
            if (StringUtils.isNotBlank(proModelColDO.getDefaultValue())) {
                if (Objects.equals("Bigint", proModelColDO.getDataType()) || Objects.equals("Decimal",
                        proModelColDO.getDataType()) || Objects.equals("Double", proModelColDO
                        .getDataType()) || Objects.equals("Float", proModelColDO
                        .getDataType()) || Objects.equals("Int", proModelColDO
                        .getDataType()) || Objects.equals("Number", proModelColDO
                        .getDataType())) {
                    editColumnElement.addAttribute("defaultValueNumeric", proModelColDO.getDefaultValue());
                } else if (Objects.equals("Boolean", proModelColDO.getDataType())) {
                    editColumnElement.addAttribute("defaultValueBoolean", proModelColDO.getDefaultValue());
                } else if (Objects.equals("Char", proModelColDO.getDataType()) || Objects.equals("Varchar",
                        proModelColDO.getDataType())) {
                    editColumnElement.addAttribute("defaultValue", proModelColDO.getDefaultValue());
                } else if (Objects.equals("Datetime", proModelColDO.getDataType()) || Objects.equals("Date",
                        proModelColDO.getDataType()) || Objects.equals("Timestamp", proModelColDO.getDataType())) {
                    editColumnElement.addAttribute("defaultValueDate", proModelColDO.getDefaultValue());
                }
            }
            editColumnElement.addAttribute("remarks", proModelColDO.getCaption());
            changeSetElemId1.add(editColumnElement);
            editColumnDocument.add(changeSetElemId1);


            StringBuffer stringBuffer = new StringBuffer();

            stringBuffer.append(dropPkDocument == null ? "" : dropPkDocument.asXML()
                    .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(pkDocument == null ? "" : pkDocument.asXML()
                    .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(editColumnDocument == null ? "" : editColumnDocument.asXML()
                    .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));


            if (!updateLiquibase(stringBuffer.toString(), proDatasourceDO)) {
                return false;
            }
            if (dropPkDocument != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(dropPkId, dropPkDocument.asXML(),
                        proModelDO.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }
            if (pkDocument != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(pkId, pkDocument.asXML(),
                        proModelDO.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }
            if (editColumnDocument != null) {
                if (!proChangelogHistoryService.saveChangelogHistory(editColumnId, editColumnDocument.asXML(),
                        proModelDO.getServiceId(), proDatasourceDO.getId())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除数据列
     *
     * @return
     */
    @Override
    public boolean delLiquibaseCol(ProModelColDO proModelColDO) {
        ProModelDO proModelDO = proModelService.getById(proModelColDO.getModelId());
        ProServiceDO serviceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));
        try {
            //   范例：
            //     <changeSet author="HP (generated)" id="1584426969480-502">
            //    <dropColumn columnName="DATA_VERSION"  tableName="BD_APP_AUTH"/>
            //    </changeSet>
            Document dropColumnDocument = null;
            String dropColumnId = serviceDO.getAppId() + "-" + proModelDO.getTableName() + "-COL-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            dropColumnDocument = DocumentHelper.createDocument();
            dropColumnDocument.setXMLEncoding("UTF-8");
            Element changesetElement = DocumentHelper.createElement("changeSet");
            changesetElement.addAttribute("author", securityUtils.getCurrentUserName());
            changesetElement.addAttribute("id", dropColumnId);
            Element dropColumnElement = DocumentHelper.createElement("dropColumn");
            dropColumnElement.addAttribute("columnName", proModelColDO.getTabColName());
            dropColumnElement.addAttribute("tableName", proModelDO.getTableName());
            changesetElement.add(dropColumnElement);
            dropColumnDocument.add(changesetElement);

            if (!updateLiquibase(dropColumnDocument.asXML()
                    .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), proDatasourceDO)) {
                return false;
            }
            if (!proChangelogHistoryService.saveChangelogHistory(dropColumnId, dropColumnDocument.asXML(), proModelDO
                    .getServiceId(), proDatasourceDO.getId())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 新增索引
     *
     * @return
     */
    @Override
    public boolean addLiquibaseIndex(ProModelIndexDO proModelIndexDO) {
        ProModelDO proModelDO = proModelService.getById(proModelIndexDO.getModelId());
        ProServiceDO proServiceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));
        try {
            Document createIndexDocument = null;
            String createIndexId = "";

            Document uniqueDocument = null;
            String uniqueId = "";

            // 不是唯一索引
            if (Objects.equals(0, proModelIndexDO.getOnlyOne())) {
                //   范例：
                //   <changeSet author="HP (generated)" id="1583823708316-380"
                // objectQuotingStrategy="QUOTE_ALL_OBJECTS">
                //        <createIndex indexName="IDX_QRTZ_FT_INST_JOB_REQ_RCVRY" tableName="QRTZ_FIRED_TRIGGERS">
                //            <column name="SCHED_NAME"/>
                //            <column name="INSTANCE_NAME"/>
                //            <column name="REQUESTS_RECOVERY"/>
                //        </createIndex>
                //   </changeSet>
                createIndexDocument = DocumentHelper.createDocument();
                createIndexDocument.setXMLEncoding("UTF-8");
                Element changeSetElement = DocumentHelper.createElement("changeSet");
                changeSetElement.addAttribute("author", securityUtils.getCurrentUserName());
                createIndexId = proServiceDO.getAppId() + "-" + proModelDO.getTableName() + "-IDX-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changeSetElement.addAttribute("id", createIndexId);
                changeSetElement.addAttribute("objectQuotingStrategy", "QUOTE_ALL_OBJECTS");
                Element createIdxElement = DocumentHelper.createElement("createIndex");
                createIdxElement.addAttribute("indexName", proModelIndexDO.getName());
                createIdxElement.addAttribute("tableName", proModelDO.getTableName());
                List<String> colNames = Arrays.asList(proModelIndexDO.getCols().split(","));
                for (String colName : colNames) {
                    Element columnElement = DocumentHelper.createElement("column");
                    columnElement.addAttribute("name", colName);
                    createIdxElement.add(columnElement);
                }
                changeSetElement.add(createIdxElement);
                createIndexDocument.add(changeSetElement);
            } else {
                //   范例：
                //    <changeSet author="HP (generated)" id="223-73" objectQuotingStrategy="QUOTE_ALL_OBJECTS">
                //      <addUniqueConstraint columnNames="SORT_INDEX, PARENT_ID"
                //        constraintName="UK_BD_APP_AUTH_SORT_INDEX_TEST" tableName="BD_APP_AUTH"/>
                //    </changeSet>
                uniqueDocument = DocumentHelper.createDocument();
                uniqueDocument.setXMLEncoding("UTF-8");
                Element changeSetElement = DocumentHelper.createElement("changeSet");
                changeSetElement.addAttribute("author", securityUtils.getCurrentUserName());
                uniqueId = proServiceDO.getAppId() + "-" + proModelDO.getTableName() + "-UNIQUE-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changeSetElement.addAttribute("id", uniqueId);
                changeSetElement.addAttribute("objectQuotingStrategy", "QUOTE_ALL_OBJECTS");
                Element addUniqueConstraintElement = DocumentHelper.createElement("addUniqueConstraint");
                addUniqueConstraintElement.addAttribute("columnNames", proModelIndexDO.getCols());
                addUniqueConstraintElement.addAttribute("constraintName", proModelIndexDO.getName());
                addUniqueConstraintElement.addAttribute("tableName", proModelDO.getTableName());
                changeSetElement.add(addUniqueConstraintElement);
                uniqueDocument.add(changeSetElement);
            }

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(createIndexDocument == null ? "" : createIndexDocument.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(uniqueDocument == null ? "" : uniqueDocument.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            if (!updateLiquibase(stringBuffer.toString(), proDatasourceDO)) {
                return false;
            }
            if (!((createIndexDocument != null && proChangelogHistoryService.saveChangelogHistory(createIndexId,
                    createIndexDocument.asXML(), proModelDO.getServiceId(), proDatasourceDO.getId())) ||
                    (uniqueDocument != null &&
                            proChangelogHistoryService.saveChangelogHistory(uniqueId, uniqueDocument.asXML(), proModelDO
                                    .getServiceId
                                            (), proDatasourceDO.getId())))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 修改索引
     *
     * @return
     */
    @Override
    public boolean updLiquibaseIndex(ProModelIndexDO proModelIndexDO) {
        return true;
    }

    /**
     * 删除索引
     *
     * @return
     */
    @Override
    public boolean delLiquibaseIndex(ProModelIndexDO proModelIndexDO) {
        ProModelDO proModelDO = proModelService.getById(proModelIndexDO.getModelId());
        ProServiceDO proServiceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));
        try {
            Document dropIdxDocu = null;
            String dropIdxId = "";

            Document dropUniqueDocu = null;
            String dropUniqueId = "";

            // 不是唯一
            if (Objects.equals(0, proModelIndexDO.getOnlyOne())) {
                //   范例：
                //    <changeSet author="HP (generated)" id="34-1184">
                //        <dropIndex  indexName="IDX_BD_APP_AUTH_APP_LIB_ID" tableName="BD_APP_AUTH"/>
                //    </changeSet>
                dropIdxDocu = DocumentHelper.createDocument();
                dropIdxDocu.setXMLEncoding("UTF-8");
                Element changesetEle = DocumentHelper.createElement("changeSet");
                changesetEle.addAttribute("author", securityUtils.getCurrentUserName());
                dropIdxId = proServiceDO.getAppId() + "-" + proModelDO.getTableName() + "-IDX-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changesetEle.addAttribute("id", dropIdxId);
                Element dropIdxEle = DocumentHelper.createElement("dropIndex");
                dropIdxEle.addAttribute("indexName", proModelIndexDO.getName());
                dropIdxEle.addAttribute("tableName", proModelDO.getTableName());
                changesetEle.add(dropIdxEle);
                dropIdxDocu.add(changesetEle);
            } else {
                //   范例：
                //     <changeSet author="HP (generated)" id="1584438597063-73">
                //         <dropUniqueConstraint constraintName="UK_BD_APP_AUTH_SORT_INDEX" tableName="BD_APP_AUTH"/>
                //     </changeSet>
                dropUniqueDocu = DocumentHelper.createDocument();
                dropUniqueDocu.setXMLEncoding("UTF-8");
                Element changeSetEle = DocumentHelper.createElement("changeSet");
                changeSetEle.addAttribute("author", securityUtils.getCurrentUserName());
                dropUniqueId = proServiceDO.getAppId() + "-" + proModelDO.getTableName() + "-UNIQUE-" + System
                        .currentTimeMillis() + "-" + UuidUtils.createUUID();
                changeSetEle.addAttribute("id", dropUniqueId);
                Element dropUniqueEle = DocumentHelper.createElement("dropUniqueConstraint");
                dropUniqueEle.addAttribute("constraintName", proModelIndexDO.getName());
                dropUniqueEle.addAttribute("tableName", proModelDO.getTableName());
                changeSetEle.add(dropUniqueEle);
                dropUniqueDocu.add(changeSetEle);
            }

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(dropIdxDocu == null ? "" : dropIdxDocu.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            stringBuffer.append(dropUniqueDocu == null ? "" : dropUniqueDocu.asXML().replace("<?xml " +
                    "version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            if (!updateLiquibase(stringBuffer.toString(), proDatasourceDO)) {
                return false;
            }
            if (!((dropIdxDocu != null && proChangelogHistoryService.saveChangelogHistory(dropIdxId,
                    dropIdxDocu.asXML(), proModelDO.getServiceId(), proDatasourceDO.getId())) || (dropUniqueDocu !=
                    null &&
                    proChangelogHistoryService.saveChangelogHistory(dropUniqueId, dropUniqueDocu.asXML(), proModelDO
                            .getServiceId
                                    (), proDatasourceDO.getId())))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 新增外键
     *
     * @return
     */
    @Override
    public boolean addLiquibaseAss(ProModelAssociationDO proModelAssociationDO) {
        ProModelDO proModelDO = proModelService.getById(proModelAssociationDO.getModelId());
        ProServiceDO proServiceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));
        try {
//       范例：
//       <changeSet author="HP (generated)" id="1584426969480-403">
//           <addForeignKeyConstraint baseColumnNames="SCHED_NAME,JOB_NAME,JOB_GROUP"
//            baseTableName="QRTZ_TRIGGERS"  constraintName="QRTZ_TRIGGER_TO_JOBS_FK"
//            deferrable="false" initiallyDeferred="false" onDelete="NO ACTION" onUpdate="NO ACTION"
//            referencedColumnNames="SCHED_NAME,JOB_NAME,JOB_GROUP"  referencedTableName="QRTZ_JOB_DETAILS"/>
//       </changeSet>
            Document document = null;
            String id = proServiceDO.getAppId() + "-" + proModelDO.getTableName() + "-FOREIGNKEY-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("UTF-8");
            Element changeSetEle = DocumentHelper.createElement("changeSet");
            changeSetEle.addAttribute("author", securityUtils.getCurrentUserName());
            changeSetEle.addAttribute("id", id);
            Element addForeignKeyConstraintEle = DocumentHelper.createElement("addForeignKeyConstraint");
            addForeignKeyConstraintEle.addAttribute("baseColumnNames", proModelAssociationDO.getColNames());
            addForeignKeyConstraintEle.addAttribute("baseTableName", proModelDO.getTableName());
            addForeignKeyConstraintEle.addAttribute("constraintName", proModelAssociationDO.getName());
            addForeignKeyConstraintEle.addAttribute("deferrable", "false");//约束验证是否可延时（事务中或者事务完成后）
            addForeignKeyConstraintEle.addAttribute("initiallyDeferred", "false");//事务结束的时候才去检查约束
            addForeignKeyConstraintEle.addAttribute("onDelete", proModelAssociationDO.getDelAction());
            addForeignKeyConstraintEle.addAttribute("onUpdate", proModelAssociationDO.getUpdAction());
            addForeignKeyConstraintEle.addAttribute("referencedColumnNames", proModelAssociationDO.getRefColNames());
            addForeignKeyConstraintEle.addAttribute("referencedTableName", proModelService.getById
                    (proModelAssociationDO.getRefModelId()).getTableName());
            changeSetEle.add(addForeignKeyConstraintEle);
            document.add(changeSetEle);
            if (!updateLiquibase(document.asXML().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""),
                    proDatasourceDO)) {
                return false;
            }
            if (!proChangelogHistoryService.saveChangelogHistory(id, document.asXML(), proModelDO.getServiceId(),
                    proDatasourceDO.getId())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 修改外键
     *
     * @return
     */
    @Override
    public boolean updLiquibaseAss(ProModelAssociationDO proModelAssociationDO) {
        return true;
    }

    /**
     * 删除外键
     *
     * @return
     */
    @Override
    public boolean delLiquibaseAss(ProModelAssociationDO proModelAssociationDO) {
        //   <changeSet author="HP (generated)" id="1584439073088-1177">
        //    <dropForeignKeyConstraint baseTableName="BD_APP_AUTH" constraintName="FK_BD_APP_AUTH_APP_LIB_ID"/>
        //    </changeSet>
        ProModelDO proModelDO = proModelService.getById(proModelAssociationDO.getModelId());
        ProServiceDO proServiceDO = proServiceService.getById(proModelDO.getServiceId());
        ProDatasourceDO proDatasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", proModelDO.getServiceId()));
        try {
            Document document = null;
            String id = proServiceDO.getAppId() + "-" + proModelDO.getTableName() + "-FOREIGNKEY-" + System
                    .currentTimeMillis() + "-" + UuidUtils.createUUID();
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("UTF-8");
            Element changeSetEle = DocumentHelper.createElement("changeSet");
            changeSetEle.addAttribute("author", securityUtils.getCurrentUserName());
            changeSetEle.addAttribute("id", id);
            Element dropForeignKeyConstraintEle = DocumentHelper.createElement("dropForeignKeyConstraint");
            dropForeignKeyConstraintEle.addAttribute("baseTableName", proModelDO.getTableName());
            dropForeignKeyConstraintEle.addAttribute("constraintName", proModelAssociationDO.getName());
            changeSetEle.add(dropForeignKeyConstraintEle);
            document.add(changeSetEle);
            if (!updateLiquibase(document.asXML().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""),
                    proDatasourceDO)) {
                return false;
            }
            if (!proChangelogHistoryService.saveChangelogHistory(id, document.asXML(), proModelDO.getServiceId(),
                    proDatasourceDO.getId())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
