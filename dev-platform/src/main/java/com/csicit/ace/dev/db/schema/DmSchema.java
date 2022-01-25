package com.csicit.ace.dev.db.schema;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.jdbc.JDBCUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.db.BaseSchema;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.util.GenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * dm数据库Schema接口实现
 *
 * @author shanwj
 * @date 2019/11/4 14:18
 */
@Service("dmSchema")
public class DmSchema extends BaseSchema implements IDBSchema {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public MetaDatasourceDO synDataModel(MetaDatasourceDO ds) throws SQLException {
        String s = discoveryClient.getServices().get(0);
//        List<ServiceInstance> instances = discoveryClient.getInstances(s);
        Connection con = getConnection(ds);
        if (con == null) {
            ds.setTables(null);
            ds.setViews(null);
            return ds;
        }
        //获取数据表
        ds.setTables(getTables(con, ds.getId()));
        //获取视图
        ds.setViews(getViews(con, ds.getId()));
        con.close();
        return ds;
    }

    @Override
    public boolean createTable(MetaDatasourceDO ds, MetaTableDO table) {
        Connection con = getConnection(ds);
        List<String> sqlList = new ArrayList<>(16);
        //建表
        String colSql = "id  VARCHAR2(50) not null";
        if (table.getTreeFlag() == 1) {
            colSql = colSql + ",parent_id VARCHAR2(50)";
        }
        if (table.getDataVersionFlag() == 1) {
            colSql = colSql + ",data_version INTEGER DEFAULT 0";
        }
        if (table.getSecretFlag() == 1) {
            colSql = colSql + ",secret_level INTEGER";
        }
        String tableSql =
                String.format("create table %s (%s,NOT CLUSTER PRIMARY KEY(ID))", table.getTableName().toUpperCase(),
                        colSql);
        sqlList.add(tableSql);

        //更新表备注
        String tableCommentSql = String.format("comment on table %s is '%s'", table.getTableName(), table.getCaption());
        sqlList.add(tableCommentSql);
        //建列备注
        String idCommentSql = String.format("comment on column %s.%s is '%s'", table.getTableName(), "id", "主键");
        sqlList.add(idCommentSql);
        if (table.getTreeFlag() == 1) {
            String parentIdSql = String.format("comment on column %s.%s is '%s'", table.getTableName(), "parent_id",
                    "父节点id");
            sqlList.add(parentIdSql);
        }
        if (table.getDataVersionFlag() == 1) {
            String versionSql = String.format("comment on column %s.%s is '%s'", table.getTableName(),
                    "data_version", "数据版本");
            sqlList.add(versionSql);
        }
        if (table.getSecretFlag() == 1) {
            String secretSql = String.format("comment on column %s.%s is '%s'", table.getTableName(), "secret_level",
                    "密级 5非密4内部3秘密2机密1绝密");
            sqlList.add(secretSql);
        }
        return executeBatchSql(con, sqlList);
    }

    @Override
    public boolean updateTable(MetaDatasourceDO ds, MetaTableDO oldTable, MetaTableDO table) {
        Connection con = getConnection(ds);
        List<String> sqlList = new ArrayList<>(16);
        if (!Objects.equals(oldTable.getTableName(), table.getTableName())) {
            String nameSql =
                    String.format("alter table %s rename to %s", oldTable.getTableName(), table.getTableName());
            sqlList.add(nameSql);
        }
        if (!Objects.equals(oldTable.getCaption(), table.getCaption())) {
            String captionSql =
                    String.format("comment on table %s is '%s'", table.getTableName(), table.getCaption());
            sqlList.add(captionSql);
        }
        return executeBatchSql(con, sqlList);
    }

    @Override
    public R createOrReplaceView(MetaDatasourceDO ds, MetaViewDO view) {
        Connection con = getConnection(ds);
        List<String> sqlList = new ArrayList<>(16);
        sqlList.add(String.format("create or replace view %s as %s", view.getName(), view.getSqlStr()));
        if (com.csicit.ace.common.utils.StringUtils.isNotBlank(view.getCaption())) {
            sqlList.add(String.format("comment on view %s is '%s'", view.getName(), view.getCaption()));
        }
        boolean b = executeBatchSql(con, sqlList);
        if (b) {
            try {
                Connection con1 = getConnection(ds);
                List<MetaViewColDO> viewCols = getViewCols(con1, view);
                R r = new R();
                r.put("cols", viewCols);
                return r;
            } catch (SQLException e) {
                e.printStackTrace();
                return R.error();
            }
        } else {
            return R.error();
        }
    }

    @Override
    public boolean updateView(MetaDatasourceDO ds, MetaViewDO oldView, MetaViewDO view) {
        Connection con = getConnection(ds);
        List<String> sqlList = new ArrayList<>(16);
        if (!Objects.equals(oldView.getName(), view.getName())) {
            sqlList.add(String.format("rename %s to %s", oldView.getName(), view.getName()));
        }
        if (!Objects.equals(oldView.getCaption(), view.getCaption())) {
            sqlList.add(String.format("comment on table %s is '%s'", view.getName(), view.getCaption()));
        }
        return executeBatchSql(con, sqlList);
    }

    private List<MetaTableDO> getTables(Connection con, String dsId) throws SQLException {
        List<MetaTableDO> tables = new ArrayList<>(16);
        String SQL = "select a.*, b.comments from user_objects a " +
                "left outer join user_tab_comments b on a.object_name = b.table_name " +
                "where a.object_type = 'TABLE' and a.object_name not like 'BIN$%'";
        PreparedStatement st = con.prepareStatement(SQL);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            MetaTableDO table = new MetaTableDO();
            String tableName = rs.getString("object_name");
            table.setDsId(dsId);
            table.setTableName(tableName);
            table.setCaption(rs.getString("comments"));
            table.setObjectName(GenUtils.columnToJava(tableName));
            table.setId(UuidUtils.createUUID());
            List<MetaTableColDO> tableCols = getTableCols(con, table);
            table.setCols(tableCols);
            List<MetaIndexDO> indexList = getIndexes(con, table);
            table.setIndexList(indexList);
            List<MetaAssociationDO> assList = getTableAsses(con, table);
            for (MetaAssociationDO ass : assList) {
                MetaIndexDO referencedIndex = getReferencedIndex(con, ass);
                ass.setRefTableName(referencedIndex.getTableName());
                ass.setRefIndexName(referencedIndex.getName());
            }
            table.setAssList(assList);
            tables.add(table);
        }
        JDBCUtils.closeResource(rs, st);
        return tables;
    }

    @Override
    public List<MetaTableDO> getTables(MetaDatasourceDO ds) throws SQLException {
        Connection con = getConnection(ds);
        List<MetaTableDO> tables = getTables(con, ds.getId());
        con.close();
        return tables;
    }

    private List<MetaTableColDO> getTableCols(Connection con, MetaTableDO table) throws SQLException {
        String sql = "select a.*, b.comments from user_tab_columns a " +
                "left outer join user_col_comments b on a.table_name = b.table_name " +
                "and a.column_name = b.column_name where a.table_name = ?";
        List<MetaTableColDO> list = new ArrayList<>(16);
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, getDbName(table.getTableName()));
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            MetaTableColDO col = new MetaTableColDO();
            String colName = rs.getString("column_name");
            col.setTabColName(colName);
            col.setObjColName(StringUtils.uncapitalize(GenUtils.columnToJava(colName)));
            col.setDataType(convertToOracleMetaType(rs));
            int dataLength = StringUtils.isEmpty(rs.getString("data_length")) ?
                    0 : rs.getInt("data_length");
            String dataDefault = rs.getString("data_default");
            String charUsed = rs.getString("char_used");
            col.setDataSize(dataLength == 0 ?
                    0 : (Objects.equals(charUsed, "C") ?
                    dataLength / 2 : dataLength));
            col.setNullable(Objects.equals(rs.getString("nullable"), "Y") ? 1 : 0);
            col.setDefaultValue(dataDefault);
            col.setCaption(rs.getString("comments"));
            col.setTableId(table.getId());
            list.add(col);
        }
        JDBCUtils.closeResource(rs, st);
        return list;
    }

    @Override
    public List<MetaTableColDO> getTableCols(MetaDatasourceDO ds, MetaTableDO table) throws SQLException {
        Connection con = getConnection(ds);
        List<MetaTableColDO> tableCols = getTableCols(con, table);
        con.close();
        return tableCols;
    }

    public List<MetaViewDO> getViews(Connection con, String dsId) throws SQLException {
        String sql = "select a.view_name, a.text, b.comments from user_views a " +
                "left outer join user_tab_comments b on a.view_name = b.table_name " +
                "where a.view_name not like 'BIN$%' order by a.view_name";

        List<MetaViewDO> list = new ArrayList<>(16);
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            MetaViewDO view = new MetaViewDO();
            view.setDsId(dsId);
            view.setName(rs.getString("view_name"));
            view.setSqlStr(rs.getString("text"));
            view.setCaption(rs.getString("comments"));
            view.setId(UuidUtils.createUUID());
            List<MetaViewColDO> viewCols = getViewCols(con, view);
            view.setCols(viewCols);
            list.add(view);
        }
        JDBCUtils.closeResource(rs, st);
        return list;
    }

    @Override
    public List<MetaViewDO> getViews(MetaDatasourceDO ds) throws SQLException {
        Connection con = getConnection(ds);
        List<MetaViewDO> views = getViews(con, ds.getId());
        con.close();
        return views;
    }

    @Override
    public MetaViewDO getView(MetaDatasourceDO ds, String viewName) throws SQLException {
        SQL = "select a.view_name, a.text, b.comments from user_views a " +
                "left outer join user_tab_comments b on a.view_name = b.table_name " +
                "where a.view_name = ? order by a.view_name";
        MetaViewDO view = null;
        conn = getConnection(ds);
        if (conn == null) {
            return null;
        }
        st = conn.prepareStatement(SQL);
        st.setString(1, viewName);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            view = new MetaViewDO();
            view.setDsId(ds.getId());
            view.setName(rs.getString("view_name"));
            view.setSqlStr(rs.getString("text"));
            view.setCaption(rs.getString("comments"));
            List<MetaViewColDO> viewCols = getViewCols(conn, view);
            view.setCols(viewCols);
        }
        JDBCUtils.closeResource(conn, rs, st);
        return view;
    }

    private List<MetaViewColDO> getViewCols(Connection con, MetaViewDO view) throws SQLException {
        String sql =
                "select a.column_name, a.data_type, a.data_scale, a.data_precision, b.comments from user_tab_columns " +
                        "a " +
                        "left outer join user_col_comments b on a.table_name = b.table_name and a.column_name = b" +
                        ".column_name " +
                        "where a.table_name = ?";
        List<MetaViewColDO> list = new ArrayList<>(16);
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, getDbName(view.getName()));
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            MetaViewColDO col = new MetaViewColDO();
            col.setName(rs.getString("column_name"));
            col.setCaption(rs.getString("comments"));
            col.setDataType(convertToOracleMetaType(rs));
            col.setSyscol(0);
            col.setViewId(view.getId());
            list.add(col);
        }
        JDBCUtils.closeResource(rs, st);
        return list;
    }

    @Override
    public List<MetaViewColDO> getViewCols(MetaDatasourceDO ds, MetaViewDO view) throws SQLException {
        Connection con = getConnection(ds);
        List<MetaViewColDO> viewCols = getViewCols(con, view);
        con.close();
        return viewCols;
    }

    private List<MetaIndexDO> getIndexes(Connection con, MetaTableDO table) throws SQLException {
        List<MetaIndexDO> list = new ArrayList<>(16);
        PreparedStatement st = con.prepareStatement("select index_name,uniqueness from user_indexes where index_type " +
                "= 'NORMAL' " +
                "and table_name = ?");
        st.setString(1, table.getTableName());
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            MetaIndexDO col = new MetaIndexDO();
            col.setName(rs.getString("index_name"));
            col.setOnlyOne(Objects.equals(rs.getString("uniqueness"), "UNIQUE") ? 1 : 0);
            col.setTableName(table.getTableName());
            col.setTableId(table.getId());
            PreparedStatement psmt = con.prepareStatement("select * from user_ind_columns where index_name = ?");
            psmt.setString(1, rs.getString("index_name"));
            ResultSet resultSet = psmt.executeQuery();
            String cols = "";
            StringJoiner joiner = new StringJoiner(",");
            while (resultSet.next()) {
//                cols = cols + resultSet.getString("column_name") + ",";
                joiner.add(resultSet.getString("column_name"));
            }
            cols = com.csicit.ace.common.utils.StringUtils.trimChar(joiner.toString(), ',');
            col.setCols(cols);
            list.add(col);
            JDBCUtils.closeResource(resultSet, psmt);
        }
        JDBCUtils.closeResource(rs, st);
        return list;
    }

    @Override
    public List<MetaIndexDO> getIndexes(MetaDatasourceDO ds, MetaTableDO table) throws SQLException {
        Connection con = getConnection(ds);
        List<MetaIndexDO> indexes = getIndexes(con, table);
        con.close();
        return indexes;
    }

    private List<MetaAssociationDO> getTableAsses(Connection con, MetaTableDO table) throws SQLException {
        List<MetaAssociationDO> list = new ArrayList<>(16);
        PreparedStatement st = con.prepareStatement(
                "select a.constraint_name, a.column_name from user_cons_columns a join " +
                        "user_constraints b on a.constraint_name = b.constraint_name  where B.CONSTRAINT_TYPE= 'R' " +
                        "and b.table_name = ?");
        st.setString(1, table.getTableName());
        ResultSet rs = st.executeQuery();
        List<MetaAssociationDO> assList = new ArrayList<>(16);
        while (rs.next()) {
            MetaAssociationDO ass = new MetaAssociationDO();
            ass.setName(rs.getString("constraint_name"));
            ass.setColumns(rs.getString("column_name"));
            assList.add(ass);
        }
        st.close();
        rs.close();
        JDBCUtils.closeResource(rs, st);
        st = con.prepareStatement("select constraint_name, table_name, r_constraint_name, " +
                "delete_rule  from user_constraints where constraint_type = 'R' and table_name = ?");
        st.setString(1, table.getTableName());
        rs = st.executeQuery();
        while (rs.next()) {
            MetaAssociationDO key = new MetaAssociationDO();
            key.setName(rs.getString("constraint_name"));
            key.setTableName(rs.getString("table_name"));
            key.setRefIndexName(rs.getString("r_constraint_name"));
            key.setDeleteAction(rs.getString("delete_rule"));
            List<MetaAssociationDO> ass = null;
            if (assList != null && assList.size() > 0) {
                ass = assList
                        .stream()
                        .filter(a -> Objects.equals(a.getName(), key.getName()))
                        .collect(Collectors.toList());
            }
            StringBuffer cols = new StringBuffer();
            if (ass != null && ass.size() > 0) {
                ass.stream().forEach(tableDO -> {
                    cols.append(tableDO.getColumns());
                    cols.append(",");
                });
            }
            String col = com.csicit.ace.common.utils.StringUtils.trimChar(cols.toString(), ',');
            key.setColumns(col);
            key.setTableId(table.getId());
            list.add(key);
        }
        JDBCUtils.closeResource(rs, st);
        con.close();
        return list;
    }

    @Override
    public List<MetaAssociationDO> getTableAsses(MetaDatasourceDO ds, MetaTableDO table) throws SQLException {
        Connection con = getConnection(ds);
        List<MetaAssociationDO> tableAsses = getTableAsses(con, table);
        con.close();
        return tableAsses;
    }

    private MetaIndexDO getReferencedIndex(Connection con, MetaAssociationDO ass) throws SQLException {
        String sql = "select constraint_name, table_name ,index_name from user_constraints where constraint_name = ?";
        MetaIndexDO index = new MetaIndexDO();
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, ass.getRefIndexName());
        ResultSet rs = st.executeQuery();
        if (!rs.next()) {
            throw new RException("外键" + ass.getName() + "引用的唯一性约束" + ass.getRefIndexName() + "不存在");
        }
        index.setName(rs.getString("index_name"));
        index.setTableName(rs.getString("table_name"));
        return index;
    }

    @Override
    public MetaIndexDO getReferencedIndex(MetaDatasourceDO ds, MetaAssociationDO ass) throws SQLException {
        Connection con = getConnection(ds);
        MetaIndexDO referencedIndex = getReferencedIndex(con, ass);
        con.close();
        return referencedIndex;
    }

    @Override
    public boolean renameTable(MetaDatasourceDO ds, String oldName, String newName) {
        conn = getConnection(ds);
        SQL = String.format("alert table %s rename to '%s'", oldName, newName);
        return executeSql();
    }

    @Override
    public boolean updateTableComment(MetaDatasourceDO ds, String tableName, String comment) {
        conn = getConnection(ds);
        SQL = String.format("comment on table %s is %s", tableName, comment);
        return executeSql();
    }

    @Override
    public boolean updateTableColumnComment(MetaDatasourceDO ds, String tableName, String columnName, String comment) {
        conn = getConnection(ds);
        SQL = String.format("comment on column %s.%s is %s", tableName, columnName, comment);
        return executeSql();
    }

    @Override
    public boolean createTable(MetaDatasourceDO ds, String tableName, String caption, List<MetaTableColDO> columns) {
        conn = getConnection(ds);
        List<String> sqls = new ArrayList<>(16);
        if (columns != null && columns.size() > 0) {
            for (MetaTableColDO col : columns) {
                sqls.add(getColumnDescSql(col.getTabColName(), col.getDataType(), col.getDataSize(),
                        col.getNullable() == 0 ? false : true, col.getDefaultValue()));
            }
        }
        String keyName = com.csicit.ace.common.utils.StringUtils.replaceStrMid(tableName, 27) + "_PK";
        String sql = String.format("id VARCHAR2(50) constraint %s primary key not null", keyName);
        String colStr = sqls.size() > 0 ? tranListStrToTrimStr(sqls) : sql;
        SQL = String.format("create table %s (%s)", tableName, colStr);
        if (executeSql()) {
            if (updateTableComment(ds, tableName, caption)) {
                if (columns != null && columns.size() > 0) {
                    for (MetaTableColDO col : columns) {
                        if (!updateTableColumnComment(ds, tableName, col.getTabColName(), col.getCaption())) {
                            return false;
                        }
                    }

                }
                return true;
            }
            return false;
        }
        return false;

    }

    @Override
    public boolean createTable(MetaDatasourceDO ds, String tableName, String caption) {
        return createTable(ds, tableName, caption, null);
    }

    @Override
    public boolean createPrimaryKey(MetaDatasourceDO ds, String tableName, String primekeyName, List<String> cols) {
        conn = getConnection(ds);
        SQL = String.format("alter table %s add (constraint %s primary kye (%s) enable validate)",
                tableName, primekeyName, tranListStrToTrimStr(cols));
        return executeSql();
    }

    @Override
    public boolean createForeignKey(MetaDatasourceDO ds, MetaIndexDO index, MetaAssociationDO ass) {
        Connection con = getConnection(ds);
        List<String> sqlList = new ArrayList<>(16);
        String deleteRule = "";
        if (Objects.equals(ass.getDeleteAction(), DELETE)) {
            deleteRule = " on delete cascade ";
        }
        if (Objects.equals(ass.getDeleteAction(), SETNULL)) {
            deleteRule = " on delete set null ";
        }
        String sql = String.format("alter table %s add constraint %s foreign key (%s) references %s (%s)",
                ass.getTableName(), ass.getName(), ass.getColumns(),
                ass.getRefTableName(), index.getCols()) + deleteRule;
        sqlList.add(sql);
        if (ass.getCreateFk() == 1) {
            sqlList.add(String.format("create %s index %s on %s (%s)", ass.getIndexOne() == 1 ? "unique" : "",
                    ass.getTableName() + "_" + ass.getName(), ass.getTableName(), ass.getColumns()));
        }
        return executeBatchSql(con, sqlList);
    }

    @Override
    public boolean dropTable(MetaDatasourceDO ds, String tableName) {
        conn = getConnection(ds);
        if (conn == null) {
            return false;
        }
        SQL = String.format("drop table %s cascade constraints purge", tableName.toUpperCase());
        return executeSql();
    }

    @Override
    public boolean addColumn(MetaDatasourceDO ds, String tableName, String columnName, String caption, String dataType,
                             int dataSize, boolean isNullable, String defaultValue) {
        Connection con = getConnection(ds);
        List<String> sqlList = new ArrayList<>(16);
        String columnDescSql = getColumnDescSql(columnName, dataType, dataSize, isNullable, defaultValue);
        String addSql = String.format("alter table %s add %s", tableName, columnDescSql);
        sqlList.add(addSql);
        String commentSql = String.format("comment on column %s.%s is '%s'", tableName, columnName, caption);
        sqlList.add(commentSql);
        return executeBatchSql(con, sqlList);
    }

    @Override
    public boolean renameTableColumn(MetaDatasourceDO ds, String tableName, String oldColName, String newColName) {
        if (!Objects.equals(getDbName(oldColName), getDbName(newColName))) {
            conn = getConnection(ds);
            SQL = String.format("alter table %s rename column %s to %s", tableName, oldColName, newColName);
            return executeSql();
        }
        return true;
    }

    @Override
    public boolean updateTableColumn(MetaDatasourceDO ds, String tableName, MetaTableColDO col, String dataType,
                                     int dataSize, boolean isNullable, String defaultValue) {
        conn = getConnection(ds);
        SQL = String.format("alter table %s modify (%s)", tableName,
                getColumnDescSql(col.getTabColName(), dataType, dataSize, isNullable, defaultValue));
        return executeSql();
    }

    @Override
    public boolean updateTableColumn(MetaDatasourceDO ds, MetaTableColDO oldCol, MetaTableColDO col) {
        Connection con = getConnection(ds);
        List<String> sqls = new ArrayList<>(16);
        if (!Objects.equals(oldCol.getTabColName(), col.getTabColName())) {
            sqls.add(String.format("alter table %s rename column %s to %s",
                    col.getTableName(), oldCol.getTabColName(), col.getTabColName()));
        }
        if (!Objects.equals(oldCol.getDataType(), col.getDataType())) {
            String dataType = convertToDmDBType(col.getDataType());
            if ((Objects.equals(col.getDataType(), "String"))) {
                dataType = dataType + "(" + col.getDataSize() + " CHAR)";
            }
            sqls.add(String.format(" alter table %s modify %s %s",
                    col.getTableName(), col.getTabColName(), dataType));
        }
        if (!Objects.equals(oldCol.getCaption(), col.getCaption())) {
            sqls.add(String.format("comment on column %s.%s is '%s'",
                    col.getTableName(), col.getTabColName(), col.getCaption()));
        }
        return executeBatchSql(con, sqls);
    }

    @Override
    public boolean dropColumn(MetaDatasourceDO ds, String tableName, String colName) {
        conn = getConnection(ds);
        SQL = String.format("alter table %s drop column %s", tableName, colName);
        return executeSql();
    }

    @Override
    public boolean createOrReplaceView(MetaDatasourceDO ds, String viewName, String viewSql) {
        conn = getConnection(ds);
        SQL = String.format("create or replace view %s as %s", viewName, viewSql);
        return executeSql();
    }

    @Override
    public boolean renameView(MetaDatasourceDO ds, String viewName, String newViewName) {
        conn = getConnection(ds);
        SQL = String.format("rename %s to %s", viewName, newViewName);
        return executeSql();
    }

    @Override
    public boolean updateViewComment(MetaDatasourceDO ds, String viewName, String comment) {
        conn = getConnection(ds);
        SQL = String.format("comment on table %s is '%s'", viewName, comment);
        return executeSql();
    }

    @Override
    public boolean updateViewColumnComment(MetaDatasourceDO ds, String viewName, String colName, String comment) {
        conn = getConnection(ds);
        SQL = String.format("comment on column %s.%s is '%s' ", viewName, colName, comment);
        return executeSql();
    }

    @Override
    public boolean dropView(MetaDatasourceDO ds, String viewName) {
        conn = getConnection(ds);
        SQL = String.format("drop view %s", viewName);
        return executeSql();
    }

    @Override
    public boolean createIndex(MetaDatasourceDO ds, String tableName, MetaIndexDO index) {
        conn = getConnection(ds);
        if (index.getOnlyOne() == 1) {
            SQL = String.format("ALTER TABLE %s ADD CONSTRAINT %s UNIQUE (%s)",
                    tableName, index.getName(), index.getCols());
        } else {
            SQL = String.format("create index %s on %s (%s)", index.getName(), tableName, index.getCols());
        }
        return executeSql();
    }

    @Override
    public boolean createIndex(MetaDatasourceDO ds, String tableName, String indexName, String cols, boolean isOnly) {
        conn = getConnection(ds);
        String str = isOnly ? "unique" : "";
        SQL = String.format("create %s index %s on %s (%s)", str, indexName, tableName, cols);
        return executeSql();
    }

    @Override
    public boolean dropIndex(MetaDatasourceDO ds, String tableName, String indexName) {
        Connection con = getConnection(ds);
        try {
            con.setAutoCommit(false);
            String selectSql = String.format("select constraint_name from user_constraints " +
                    "where constraint_type = 'U' and index_name = '%s'", indexName);
            PreparedStatement st = con.prepareStatement(selectSql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String constraintName = rs.getString("constraint_name");
                PreparedStatement pst = con.prepareStatement(String.format("alter table %s drop constraint %s",
                        tableName, constraintName));
                pst.execute();
                pst.close();
            }
            JDBCUtils.closeResource(rs, st);
            st = con.prepareStatement(String.format("select 1 from user_indexes where index_type = 'NORMAL' and " +
                    "index_name = '%s' ", indexName));
            rs = st.executeQuery();
            if (rs.next()) {
                PreparedStatement pst = con.prepareStatement("drop index " + indexName);
                pst.execute();
                pst.close();
            }
            con.commit();
            JDBCUtils.closeResource(con, rs, st);
            return true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean renameIndex(MetaDatasourceDO ds, String tableName, String indexName, String newIndexName) {
        conn = getConnection(ds);
        SQL = String.format("alter index %s rename to %s", indexName, newIndexName);
        return executeSql();
    }

    @Override
    public boolean dropConstraint(MetaDatasourceDO ds, String tableName, String constraintName) {
        conn = getConnection(ds);
        SQL = String.format("alter table %s drop constraint %s", tableName, constraintName);
        return executeSql();
    }

    @Override
    public boolean renameConstraint(MetaDatasourceDO ds, String tableName, String oldName, String newName) {
        conn = getConnection(ds);
        SQL = String.format("alter table %s rename constraint %s to %s", tableName,
                oldName, newName);
        return executeSql();
    }

    /**
     * 获取列语句
     *
     * @param columnName
     * @param dataType
     * @param dataSize
     * @param isNullable
     * @param defaultValue
     * @return
     */
    private String getColumnDescSql(String columnName, String dataType,
                                    int dataSize, boolean isNullable, String defaultValue) {
        String defaultValueSql = "";
        if (StringUtils.isNotEmpty(defaultValue)) {
            if (Objects.equals(dataType, "String")
                    || Objects.equals(dataType, "CLOB")
                    || Objects.equals(dataType, "BLOB")) {
                defaultValueSql = String.format("default '%s'",
                        com.csicit.ace.common.utils.StringUtils.trimChar(defaultValue, ','));
            } else {
                defaultValueSql = String.format("default %s", defaultValue);
            }
        }
        String dType = StringUtils.isEmpty(dataType) ? "" : convertToDmDBType(dataType);
        String dSize = dataSize == 0 ? "" : (Objects.equals(dataType, "String") ? ("(" + dataSize + " CHAR)") : "");
        String isNull = isNullable ? "" : "not null";
        return String.format("%s %s%s %s %s", columnName, dType, dSize, defaultValueSql, isNull);
    }


}
