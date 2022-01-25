package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.ProDatasourceMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.*;
import com.csicit.ace.dev.util.DBUtil;
import com.csicit.ace.dev.util.GenUtils;
import com.google.common.collect.ImmutableMap;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.change.AddColumnConfig;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.change.core.CreateIndexChange;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 数据源 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:48:24
 */
@Service("proDatasourceService")
public class ProDatasourceServiceImpl extends BaseServiceImpl<ProDatasourceMapper, ProDatasourceDO>
        implements ProDatasourceService {

    @Autowired
    ProServiceService proServiceService;

    @Autowired
    LiquibaseService liquibaseService;

    @Autowired
    ProChangelogHistoryService proChangelogHistoryService;

    @Autowired
    ProModelService proModelService;

    @Autowired
    ProModelColService proModelColService;

    @Autowired
    ProModelIndexService proModelIndexService;

    @Autowired
    ProModelAssociationService proModelAssociationService;
    private static final Map<String, String> DATA_TYPE = ImmutableMap.<String, String>builder()
            .put("BIGINT", "Bigint")
            .put("BOOLEAN", "Boolean")
            .put("char", "Char")
            .put("VARCHAR2", "Varchar")
            .put("NVARCHAR2", "Varchar")
            .put("VARCHAR", "Varchar")
            .put("datetime", "Datetime")
            .put("TIMESTAMP", "Timestamp")
            .put("timestamp", "Timestamp")
            .put("date", "Timestamp")
            .put("TIME WITHOUT TIME ZONE","Timestamp")
            .put("TIMESTAMP WITHOUT TIME ZONE","Timestamp")
            .put("decimal", "Decimal")
            .put("DECIMAL", "Decimal")
            .put("DOUBLE", "Double")
            .put("DOUBLE PRECISION", "Double")
            .put("FLOAT", "Float")
            .put("float", "Float")
            .put("INTEGER", "Int")
            .put("SMALLINT", "Int")
            .put("int", "Int")
            .put("INT", "Int")
            .put("numeric", "Number")
            .put("NUMBER", "Number")
            .put("BLOB", "BLOB")
            .put("blob", "BLOB")
            .put("CLOB", "CLOB")
            .put("clob", "CLOB")
            .put("LONGTEXT", "CLOB")
            .build();

    /**
     * 重新部署数据结构
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/28 15:05
     */
    private boolean resetDataStructure(ProDatasourceDO instance) {
        ProServiceDO proServiceDO = proServiceService.getById(instance.getServiceId());
        // 删除旧的数据结构
        proModelService.remove(new QueryWrapper<ProModelDO>().eq("SERVICE_ID", instance.getServiceId()));
        proChangelogHistoryService.update(new ProChangelogHistoryDO(), new UpdateWrapper<ProChangelogHistoryDO>()
                .eq("SERVICE_ID", instance.getServiceId()).set("IS_USE_LESS", 0));

        // 新增新的数据结构
        Map<String, Object> map = liquibaseService.getFirstChangelogByDs(instance);
        String changeLog = (String) map.get("changeLog");
        int strStartIndex = changeLog.indexOf("<changeSet");
        if (strStartIndex > -1) {
            int strEndIndex = changeLog.indexOf("</databaseChangeLog>");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            stringBuffer.append("\n");
            stringBuffer.append(changeLog.substring(strStartIndex, strEndIndex));
            changeLog = stringBuffer.toString();

            List<ChangeSet> changeSets = (List<ChangeSet>) map.get("changeSets");
            if (CollectionUtils.isNotEmpty(changeSets)) {
                if (!this.getModelInfoByChangelog(changeSets, proServiceDO.getId())) {
                    return false;
                }
            }
        }
        if (!proChangelogHistoryService.saveChangelogHistory(proServiceDO.getAppId() + "-" + "FIRST-CREATE" + System
                .currentTimeMillis(), changeLog, proServiceDO.getId(), instance.getId())) {
            return false;
        }

        return true;
    }

    /**
     * 添加平台使用表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2020/8/21 15:40
     */
    private boolean createPlatformNeedTable(ProDatasourceDO instance) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("/db/changelog/sqlFile/*.xml");
            if (resources != null && resources.length > 0) {
                for (Resource resource : resources) {
                    if (Objects.equals(resource.getFilename(), "platform_need_table.xml")) {
                        InputStream is = resource.getInputStream();
                        Liquibase liquibase = new Liquibase(is, "xml", new
                                ClassLoaderResourceAccessor(), liquibaseService.getDatabase(instance));
                        if (liquibase != null) {
                            liquibase.update(new Contexts(), new LabelExpression());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (LiquibaseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/28 14:39
     */
    @Override
    public boolean saveDatasource(ProDatasourceDO instance) {


        // 当前已存在数据源，判断平台使用所需日志表是否存在
        if (!DBUtil.validateTableNameExist("SYS_AUDIT_LOG", instance)) {
            // 创建平台使用所需的日志表
            if (!this.createPlatformNeedTable(instance)) {
                return false;
            }
        }

        // 新增为主数据源
        instance.setId(UuidUtils.createUUID());
        if (instance.getMajor() == 1) {
            update(new ProDatasourceDO(), new UpdateWrapper<ProDatasourceDO>().eq("SERVICE_ID", instance.getServiceId())
                    .set("IS_MAJOR", 0));
            this.resetDataStructure(instance);
        }

        if (!save(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/28 14:39
     */
    @Override
    public boolean updateDatasouce(ProDatasourceDO instance, Integer oldMajor) {

        // 当前已存在数据源，判断平台使用所需日志表是否存在
        if (!DBUtil.validateTableNameExist("SYS_AUDIT_LOG", instance)) {
            // 创建平台使用所需的日志表
            if (!this.createPlatformNeedTable(instance)) {
                return false;
            }
        }

        if (!Objects.equals(oldMajor, instance.getMajor())) {
            if (instance.getMajor() == 1) {
                update(new ProDatasourceDO(), new UpdateWrapper<ProDatasourceDO>().eq("SERVICE_ID", instance
                        .getServiceId())
                        .set("IS_MAJOR", 0));
                this.resetDataStructure(instance);
            } else {
                // 删除旧的数据结构
                proModelService.remove(new QueryWrapper<ProModelDO>().eq("SERVICE_ID", instance.getServiceId()));
                proChangelogHistoryService.update(new ProChangelogHistoryDO(), new
                        UpdateWrapper<ProChangelogHistoryDO>()
                        .eq("SERVICE_ID", instance.getServiceId()).set("IS_USE_LESS", 0));
            }
        }

        if (!updateById(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2020/7/28 14:39
     */
    @Override
    public boolean deleteDatasouce(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        List<ProDatasourceDO> proDatasourceDOS = (List<ProDatasourceDO>) listByIds(ids);

        proDatasourceDOS.stream().forEach(item -> {
            if (item.getMajor() == 1) {
                // 删除旧的数据结构
                proModelService.remove(new QueryWrapper<ProModelDO>().eq("SERVICE_ID", item.getServiceId()));
                proChangelogHistoryService.update(new ProChangelogHistoryDO(), new
                        UpdateWrapper<ProChangelogHistoryDO>()
                        .eq("SERVICE_ID", item.getServiceId()).set("IS_USE_LESS", 0));
            }
        });

        if (!removeByIds(ids)) {
            return false;
        }

        return true;
    }

    /**
     * 通过changlog生成模型
     *
     * @param changeSets
     * @param serviceId
     * @return
     * @author zuogang
     * @date 2020/5/22 11:22
     */
    private boolean getModelInfoByChangelog(List<ChangeSet> changeSets, String serviceId) {

        List<ProModelDO> proModelDOS = new ArrayList<>(16);
        List<ProModelColDO> proModelColDOS = new ArrayList<>(16);
        List<ProModelAssociationDO> proModelAssociationDOS = new ArrayList<>(16);
        List<ProModelIndexDO> proModelIndexDOS = new ArrayList<>(16);

        List<CreateTableChange> createTableChanges = new ArrayList<>(16);
        List<AddForeignKeyConstraintChange> addForeignKeyConstraintChanges = new ArrayList<>(16);
        List<CreateIndexChange> createIndexChanges = new ArrayList<>(16);
        List<AddUniqueConstraintChange> addUniqueConstraintChanges = new ArrayList<>(16);

        changeSets.forEach(changeSet -> {
            Change change = changeSet.getChanges().get(0);
            if (Objects.equals("createTable", change.toString())) {
                createTableChanges.add((CreateTableChange) change);
            } else if (Objects.equals("addUniqueConstraint", change.toString())) {
                addUniqueConstraintChanges.add((AddUniqueConstraintChange) change);
            } else if (Objects.equals("createIndex", change.toString())) {
                createIndexChanges.add((CreateIndexChange) change);
            } else if (Objects.equals("addForeignKeyConstraint", change.toString())) {
                addForeignKeyConstraintChanges.add((AddForeignKeyConstraintChange) change);
            }
        });
        // createTableChanges
        createTableChanges.stream().forEach(createTableChange -> {
            // 模型
            ProModelDO proModelDO = new ProModelDO();
            proModelDO.setId(UuidUtils.createUUID());
            proModelDO.setModelName(createTableChange.getRemarks());
            proModelDO.setTableName(createTableChange.getTableName());
            proModelDO.setObjectName(GenUtils.columnToJava(createTableChange.getTableName()));
            proModelDO.setServiceId(serviceId);
            proModelDO.setCreateTime(LocalDateTime.now());
            proModelDO.setRemark("");
            proModelDO.setFileExist(0);
            proModelDO.setFlowExist(0);

            //模型字段
            List<ColumnConfig> columns = createTableChange.getColumns();
            columns.forEach(columnConfig -> {
                ProModelColDO proModelColDO = new ProModelColDO();
                proModelColDO.setId(UuidUtils.createUUID());
                proModelColDO.setModelId(proModelDO.getId());
                proModelColDO.setTabColName(columnConfig.getName());
                proModelColDO.setObjColName(StringUtils.uncapitalize(GenUtils.columnToJava(columnConfig.getName()
                )));
                proModelColDO.setCaption(columnConfig.getRemarks());
                String type = columnConfig.getType();
                proModelColDO.setDataPrecision(0);
                proModelColDO.setDataScale(0);
                if (!type.contains("(")) {
                    proModelColDO.setDataType(DATA_TYPE.get(columnConfig.getType()));
                } else {
                    proModelColDO.setDataType(DATA_TYPE.get(columnConfig.getType().substring(0, columnConfig.getType()
                            .indexOf("("))));
                    String datasize = columnConfig.getType().substring(columnConfig.getType()
                            .indexOf("(") + 1, columnConfig.getType()
                            .indexOf(")"));
                    if (datasize.contains(" CHAR")) {
                        proModelColDO.setDataPrecision(Integer.parseInt(datasize.replace(" CHAR", "")));
                    } else if (datasize.contains(", ")) {
                        if (Objects.equals("*", datasize.substring(0, 1))) {
                            proModelColDO.setDataPrecision(10);
                        } else {
                            proModelColDO.setDataPrecision(Integer.parseInt(datasize.substring(0, datasize.indexOf("," +
                                    ""))));
                            proModelColDO.setDataScale(Integer.parseInt(datasize.substring(datasize.indexOf(", ") +
                                    2, datasize.length())));
                        }
                    } else if (datasize.contains(" BYTE")) {
                        proModelColDO.setDataPrecision(Integer.parseInt(datasize.replace(" BYTE", "")));
                    } else {
                        proModelColDO.setDataPrecision(Integer.parseInt(datasize));
                    }

                }
                if (StringUtils.isBlank(proModelColDO.getDataType())) {
                    System.out.println("dataType:"+columnConfig.getType());
                    throw new RException("模型字段类型获取出错！");
                }

                ConstraintsConfig constraintsConfig = columnConfig.getConstraints();
                if (constraintsConfig != null) {
                    if (constraintsConfig.isNullable() != null) {
                        if (constraintsConfig.isNullable()) {
                            // 能为空
                            proModelColDO.setNullable(1);
                        } else {
                            // 不能为空
                            proModelColDO.setNullable(0);
                        }
                    }
                    if (constraintsConfig.isPrimaryKey() != null) {
                        if (constraintsConfig.isPrimaryKey()) {
                            // 主键
                            proModelColDO.setPkFlg(1);
                        } else {
                            proModelColDO.setPkFlg(0);
                        }
                    }
                    // 模型主键名
                    if (StringUtils.isNotBlank(constraintsConfig.getPrimaryKeyName())) {
                        proModelDO.setPkName(constraintsConfig.getPrimaryKeyName());
                    } else {
                        proModelDO.setPkName("PRIMARY");
                    }
                }


                // 默认值
                if (StringUtils.isNotBlank(columnConfig.getDefaultValue())) {
                    proModelColDO.setDefaultValue(columnConfig.getDefaultValue());
                } else if (columnConfig.getDefaultValueNumeric() != null) {
                    proModelColDO.setDefaultValue(columnConfig.getDefaultValueNumeric().toString());
                } else if (columnConfig.getDefaultValueDate() != null) {
                    proModelColDO.setDefaultValue(columnConfig.getDefaultValueDate().toString());
                } else if (columnConfig.getDefaultValueBoolean() != null) {
                    if (columnConfig.getDefaultValueBoolean()) {
                        proModelColDO.setDefaultValue("1");
                    } else {
                        proModelColDO.setDefaultValue("0");
                    }
                } else if (columnConfig.getDefaultValueComputed() != null) {
                    proModelColDO.setDefaultValue(columnConfig.getDefaultValueComputed().getValue());
                } else if (columnConfig.getDefaultValueSequenceNext() != null) {
                    proModelColDO.setDefaultValue(columnConfig.getDefaultValueSequenceNext().getSequenceSchemaName());
                } else if (StringUtils.isNotBlank(columnConfig.getDefaultValueConstraintName())) {
                    proModelColDO.setDefaultValue(columnConfig.getDefaultValueConstraintName());
                } else {
                    proModelColDO.setDefaultValue("");
                }
                proModelColDO.setSyscol(0);
                proModelColDO.setCreateTime(LocalDateTime.now());
                proModelColDOS.add(proModelColDO);
            });

            proModelDOS.add(proModelDO);

        });

        // addForeignKeyConstraintChanges
        addForeignKeyConstraintChanges.stream().forEach(addForeignKeyConstraintChange -> {
            ProModelAssociationDO proModelAssociationDO = new ProModelAssociationDO();
            proModelAssociationDO.setId(UuidUtils.createUUID());
            proModelAssociationDO.setName(addForeignKeyConstraintChange.getConstraintName());
            proModelDOS.stream().forEach(proModelDO -> {
                if (Objects.equals(proModelDO.getTableName(), addForeignKeyConstraintChange.getBaseTableName
                        ())) {
                    proModelAssociationDO.setModelId(proModelDO.getId());
                } else if (Objects.equals(proModelDO.getTableName(), addForeignKeyConstraintChange
                        .getReferencedTableName())) {
                    proModelAssociationDO.setRefModelId(proModelDO.getId());
                }
            });
            proModelAssociationDO.setColNames(addForeignKeyConstraintChange.getBaseColumnNames());
            proModelAssociationDO.setRefColNames(addForeignKeyConstraintChange.getReferencedColumnNames());
            proModelAssociationDO.setDelAction(addForeignKeyConstraintChange.getOnDelete());
            proModelAssociationDO.setUpdAction(addForeignKeyConstraintChange.getOnUpdate());
            proModelAssociationDO.setCreateTime(LocalDateTime.now());
            proModelAssociationDOS.add(proModelAssociationDO);
        });

        // createIndexChanges
        createIndexChanges.stream().forEach(createIndexChange -> {
            ProModelIndexDO proModelIndexDO = new ProModelIndexDO();
            proModelIndexDO.setId(UuidUtils.createUUID());
            proModelDOS.stream().filter(proModelDO -> Objects.equals(proModelDO.getTableName(),
                    createIndexChange
                            .getTableName())).forEach(proModelDO -> {
                proModelIndexDO.setModelId(proModelDO.getId());
            });
            proModelIndexDO.setName(createIndexChange.getIndexName());
            StringJoiner stringJoiner = new StringJoiner(",");
            List<AddColumnConfig> addColumnConfigs = createIndexChange.getColumns();
            addColumnConfigs.stream().forEach(addColumnConfig -> {
                stringJoiner.add(addColumnConfig.getName());
            });
            proModelIndexDO.setCols(stringJoiner.toString());
            proModelIndexDO.setOnlyOne(0);
            proModelIndexDO.setCreateTime(LocalDateTime.now());
            proModelIndexDOS.add(proModelIndexDO);
        });

        // addUniqueConstraintChanges
        addUniqueConstraintChanges.stream().

                forEach(addUniqueConstraintChange ->

                {
                    ProModelIndexDO proModelIndexDO = new ProModelIndexDO();
                    proModelIndexDO.setId(UuidUtils.createUUID());
                    proModelDOS.stream().filter(proModelDO -> Objects.equals(proModelDO.getTableName(),
                            addUniqueConstraintChange
                                    .getTableName())).forEach(proModelDO -> {
                        proModelIndexDO.setModelId(proModelDO.getId());
                    });
                    proModelIndexDO.setName(addUniqueConstraintChange.getConstraintName());
                    proModelIndexDO.setCols(addUniqueConstraintChange.getColumnNames());
                    proModelIndexDO.setOnlyOne(1);
                    proModelIndexDO.setCreateTime(LocalDateTime.now());
                    proModelIndexDOS.add(proModelIndexDO);
                });

        if (CollectionUtils.isNotEmpty(proModelDOS))

        {
            if (!proModelService.saveBatch(proModelDOS)) {
                return false;
            }
            if (CollectionUtils.isNotEmpty(proModelColDOS)) {
                if (!proModelColService.saveBatch(proModelColDOS)) {
                    return false;
                }
            }
            if (CollectionUtils.isNotEmpty(proModelAssociationDOS)) {
                if (!proModelAssociationService.saveBatch(proModelAssociationDOS)) {
                    return false;
                }
            }
            if (CollectionUtils.isNotEmpty(proModelIndexDOS)) {
                if (!proModelIndexService.saveBatch(proModelIndexDOS)) {
                    return false;
                }
            }
        }

        return true;
    }

}
