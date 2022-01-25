package com.csicit.ace.dev.db;

import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.common.utils.server.R;

import java.sql.SQLException;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/11/4 14:17
 *
 * 数据库架构接口
 */
public interface IDBSchema {

    /**
     * 是否支持视图备注
     *
     * @return
     */
    default boolean isSupportViewCommnet(){
        return false;
    }

    /**
     * 同步数据源
     * @param ds
     * @return
     * @throws SQLException
     */
    MetaDatasourceDO synDataModel(MetaDatasourceDO ds) throws SQLException;

    /**
     * 同步数据源
     * @param ds
     * @return
     * @throws SQLException
     */
    boolean createTable(MetaDatasourceDO ds, MetaTableDO table);

    /**
     * 同步数据源
     * @param ds
     * @return
     * @throws SQLException
     */
    boolean updateTable(MetaDatasourceDO ds, MetaTableDO oldTable, MetaTableDO table);


    /**
     * 创建或更新视图
     *
     * @param ds 数据源id
     * @param view 视图
     * @author shanwj
     * @date 2019/6/3 16:19
     */
    R createOrReplaceView(MetaDatasourceDO ds, MetaViewDO view);


    boolean updateView(MetaDatasourceDO ds, MetaViewDO oldView, MetaViewDO view);

    /**
     *
     * @param ds	数据源id
     * @return java.util.List<com.csicit.ace.common.pojo.domain.dev.MetaTableDO>
     * @author shanwj
     * @date 2019/11/4 14:55
     */
    List<MetaTableDO> getTables(MetaDatasourceDO ds) throws SQLException;

    /**
     *  获取数据库列信息
     *
     * @param ds 数据源
     * @param table	 表对象
     * @return java.util.List<com.csicit.ace.common.pojo.domain.dev.MetaTableColDO>
     * @author shanwj
     * @date 2019/11/4 14:57
     */
    List<MetaTableColDO> getTableCols(MetaDatasourceDO ds, MetaTableDO table) throws SQLException;

    /**
     * 获取视图信息（含视图列）
     * @param ds	 数据源
     * @return 视图列表
     * @author shanwj
     * @date 2019/6/3 14:58
     */
    List<MetaViewDO> getViews(MetaDatasourceDO ds) throws SQLException;

    /**
     * 获取视图信息（含视图列）
     *
     * @param ds 数据源
     * @param viewName 视图名称
     * @return 视图对象
     */
    MetaViewDO getView(MetaDatasourceDO ds, String viewName) throws SQLException;

    /**
     * 获取视图列列表
     *
     * @param ds	数据源
     * @param view	视图对象
     * @return java.util.List<com.csicit.ace.common.pojo.domain.dev.MetaViewColDO>
     * @author shanwj
     * @date 2019/11/5 17:13
     */
    List<MetaViewColDO> getViewCols(MetaDatasourceDO ds, MetaViewDO view) throws SQLException;

    /**
     * 获取索引列表
     * @param ds	 数据源
     * @return 索引列表
     * @author shanwj
     * @date 2019/6/3 14:58
     */
    List<MetaIndexDO> getIndexes(MetaDatasourceDO ds, MetaTableDO table) throws SQLException;


    /**
     * 获取外键信息
     * @param ds  数据源
     * @return 外键列表
     * @author shanwj
     * @date 2019/6/3 14:58
     */
    List<MetaAssociationDO> getTableAsses(MetaDatasourceDO ds, MetaTableDO table) throws SQLException;

    /**
     * 获取外键引用的索引信息
     *
     * @param ds 数据源
     * @param ass 外检对象
     * @return 外键引用的索引对象
     */
    MetaIndexDO getReferencedIndex(MetaDatasourceDO ds, MetaAssociationDO ass) throws SQLException;

    /**
     * 重命名表
     *
     * @param ds 数据源
     * @param oldName 原始表名
     * @param newName 新表名
     */
    boolean renameTable(MetaDatasourceDO ds, String oldName, String newName);

    /**
     *  更新表注释
     *
     * @param ds 数据源
     * @param tableName 表名
     * @param comment 注释
     * @author shanwj
     * @date 2019/6/3 16:09
     */
    boolean updateTableComment(MetaDatasourceDO ds, String tableName, String comment);

    /**
     * 更新表列注释
     *
     * @param ds 数据源
     * @param tableName 表名
     * @param columnName 列名
     * @param comment 注释
     * @author shanwj
     * @date 2019/6/3 16:10
     */
    boolean updateTableColumnComment(MetaDatasourceDO ds, String tableName, String columnName, String comment);

    /**
     * 创建表，此方法默认情况下，创建的表只包括ID字段(varchar50)。设置isTree参数，将添加树结构字段，
     * 设置isConcurrent参数，将添加DataVersion字段，便于对数据存储进行乐观并发控制。设置isSecret参数，将添加密级字段
     * @param ds 数据源
     * @param tableName 表名
     * @param caption 标题
     * @param columns 列集合
     * @author shanwj
     * @date 2019/6/3 16:11
     */
    boolean createTable(MetaDatasourceDO ds, String tableName, String caption, List<MetaTableColDO> columns);

    /**
     * 创建表，此方法默认情况下，创建的表只包括ID字段(varchar50)。设置isTree参数，将添加树结构字段，
     * 设置isConcurrent参数，将添加DataVersion字段，便于对数据存储进行乐观并发控制。设置isSecret参数，将添加密级字段
     * @param ds 数据源
     * @param tableName 表名
     * @param caption 标题
     * @author shanwj
     * @date 2019/6/3 16:11
     */
    boolean createTable(MetaDatasourceDO ds, String tableName, String caption);

    /**
     * 创建主键
     *
     * @param ds 数据源
     * @param tableName 表名
     * @param primekeyName 主键名
     * @param cols 列集合
     * @author shanwj
     * @date 2019/6/3 16:12
     */
    boolean createPrimaryKey(MetaDatasourceDO ds, String tableName, String primekeyName, List<String> cols);

    /**
     * 创建外键
     *
     * @param ds 数据源
     * @param index 引用索引
     * @param ass 关联关系
     * @author shanwj
     * @date 2019/6/3 16:13
     */
    boolean createForeignKey(MetaDatasourceDO ds, MetaIndexDO index, MetaAssociationDO ass);

    /**
     * 删除表
     *
     * @param ds 数据源
     * @param tableName 表名
     * @author shanwj
     * @date 2019/6/3 16:15
     */
    boolean dropTable(MetaDatasourceDO ds, String tableName);

    /**
     * 添加列
     * @param ds 数据源
     * @param tableName	 表名
     * @param columnName 列名
     * @param caption 注释
     * @param dataType	数据类型
     * @param dataSize	数据大小
     * @param isNullable 是否为空
     * @param defaultValue	默认值
     * @author shanwj
     * @date 2019/6/3 16:15
     */
    boolean addColumn(MetaDatasourceDO ds, String tableName, String columnName, String caption, String dataType,
                      int dataSize, boolean isNullable, String defaultValue);

    /**
     * 列更名
     * @param ds 数据源
     * @param tableName	 表名
     * @param oldColName 原列名
     * @param newColName 新列名
     * @author shanwj
     * @date 2019/6/3 16:16
     */
    boolean renameTableColumn(MetaDatasourceDO ds, String tableName, String oldColName, String newColName);

    /**
     * 更新列
     *
     * @param ds 数据源
     * @param tableName 表名
     * @param col 列对象
     * @param dataType	数据类型
     * @param dataSize	数据大小
     * @param isNullable 是否为空
     * @param defaultValue	默认值
     * @author shanwj
     * @date 2019/6/3 16:17
     */
    boolean updateTableColumn(MetaDatasourceDO ds, String tableName, MetaTableColDO col, String dataType,
                              int dataSize, boolean isNullable, String defaultValue);

    /**
     * 更新列
     *
     * @param ds 数据源
     * @param oldCol 原列对象
     * @param col 列对象
     * @author shanwj
     * @date 2019/6/3 16:17
     */
    boolean updateTableColumn(MetaDatasourceDO ds, MetaTableColDO oldCol, MetaTableColDO col);
    /**
     * 删除列
     *
     * @param ds 数据源
     * @param tableName 表名
     * @param colName 列名
     * @author shanwj
     * @date 2019/6/3 16:18
     */
    boolean dropColumn(MetaDatasourceDO ds, String tableName, String colName);

    /**
     * 创建或更新视图
     *
     * @param ds 数据源id
     * @param viewName	视图名
     * @param viewSql 视图脚本
     * @author shanwj
     * @date 2019/6/3 16:19
     */
    boolean createOrReplaceView(MetaDatasourceDO ds, String viewName, String viewSql);


    /**
     * 重命名视图
     *
     * @param ds 数据源
     * @param viewName	视图名
     * @param newViewName 新视图名
     * @author shanwj
     * @date 2019/6/3 16:29
     */
    boolean renameView(MetaDatasourceDO ds, String viewName, String newViewName);

    /**
     * 更新视图注释
     * @param ds 数据源
     * @param viewName	视图名
     * @param comment 注释
     * @author shanwj
     * @date 2019/6/3 16:30
     */
    boolean updateViewComment(MetaDatasourceDO ds, String viewName, String comment);

    /**
     * 更新视图列注释
     *
     * @param ds 数据源
     * @param viewName 视图名
     * @param colName 列名
     * @param comment 注释
     * @author shanwj
     * @date 2019/6/3 16:35
     */
    boolean updateViewColumnComment(MetaDatasourceDO ds, String viewName, String colName, String comment);

    /**
     * 删除视图
     *
     * @param ds 数据源id
     * @param viewName	视图名
     * @author shanwj
     * @date 2019/6/3 16:41
     */
    boolean dropView(MetaDatasourceDO ds, String viewName);

    /**
     * 创建索引
     * @param ds 数据源
     * @param tableName	 表名
     * @param index	 索引
     * @author shanwj
     * @date 2019/6/3 16:42
     */
    boolean createIndex(MetaDatasourceDO ds, String tableName, MetaIndexDO index);

    boolean createIndex(MetaDatasourceDO ds, String tableName, String indexName, String cols, boolean isOnly);

    /**
     * 删除索引
     *
     * @param ds 数据源
     * @param tableName 表名
     * @param indexName	 索引名
     * @author shanwj
     * @date 2019/6/3 16:43
     */
    boolean dropIndex(MetaDatasourceDO ds, String tableName, String indexName);

    /**
     * 重命名索引
     * @param ds 数据源
     * @param tableName 表名
     * @param indexName	 原索引名
     * @param newIndexName	新索引名
     * @author shanwj
     * @date 2019/6/3 16:44
     */
    boolean renameIndex(MetaDatasourceDO ds, String tableName, String indexName, String newIndexName);

    /**
     * 删除约束
     *
     * @param ds 数据源
     * @param tableName	 表名
     * @param constraintName 约束名
     * @author shanwj
     * @date 2019/6/3 16:45
     */
    boolean dropConstraint(MetaDatasourceDO ds, String tableName, String constraintName);

    /**
     *
     *
     * @param ds 数据源
     * @param tableName	 表名
     * @param oldName 原约束名
     * @param newName 新约束名
     * @author shanwj
     * @date 2019/6/3 16:46
     */
    boolean renameConstraint(MetaDatasourceDO ds, String tableName, String oldName, String newName);

}
