package com.csicit.ace.dev.db;


import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.utils.jdbc.JDBCUtils;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 数据库接口实现基类
 *
 * @author shanwj
 * @date 2019/11/4 16:22
 */
public abstract class BaseSchema {
    /**
     * sql语句
     */
    protected String SQL = "";
    /**
     * 数据库连接对象
     */
    protected Connection conn = null;
    /**
     * jdbc执行接口对象
     */
    protected PreparedStatement st = null;
    /**
     * 外键级联关系
     */
    protected final static String SETNULL = "SET NULL";
    protected final static String DELETE = "CASCADE";

//    protected final static String DM = "dm";
//    protected final static String ORACLE = "oracle";
//    protected final static String MYSQL = "mysql";

    /**
     * 获取jdbc连接
     *
     * @param ds 数据源对象
     * @return
     */
    protected Connection getConnection(MetaDatasourceDO ds) {
        try {
            if (ds == null) {
                return null;
            }
            return JDBCUtils.getConnection(
                    ds.getDriver(), ds.getUrl(), ds.getUserName(), ds.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected boolean executeBatchSql(Connection con, List<String> sqls) {
        if (con == null) {
            return false;
        }
        if (sqls.size() == 0) {
            return true;
        }
        try {
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            for (String sql : sqls) {
                st.addBatch(sql);
            }
            st.executeBatch();
            con.commit();
            JDBCUtils.closeResource(con, st);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    protected boolean executeSql() {
        if (conn == null) {
            return false;
        }
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement(SQL);
            st.execute();
            conn.commit();
            JDBCUtils.closeResource(conn, st);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    protected String getDbName(String metaName) {
        return getDbName(metaName, false);
    }

    protected String getDbName(String metaName, boolean isLowerCaseDefault) {
        if (metaName.contains(",")) {
            String[] names = metaName.split(",");
//            String metaNameStr = "";
            StringJoiner joiner = new StringJoiner(",");
            for (String name : names) {
                joiner.add(Objects.equals(name.toLowerCase(), name) ?
                        (isLowerCaseDefault ? name.toLowerCase() : name.toUpperCase()) : name);
//                metaNameStr +=
//                        (name.toLowerCase() == name ?
//                                (isLowerCaseDefault ? name.toLowerCase() : name.toUpperCase()) : name) + ",";
            }

            return com.csicit.ace.common.utils.StringUtils.trimChar(joiner.toString(), ',');
        } else {
            return Objects.equals(metaName.toLowerCase(), metaName) ?
                    (isLowerCaseDefault ? metaName.toLowerCase() : metaName.toUpperCase()) : metaName;
        }
    }

    protected String getMetaName(String dbObjName) {
        if (dbObjName.contains(",")) {
            String[] names = dbObjName.split(",");
//            String metaNameStr = "";
            StringJoiner joiner = new StringJoiner(",");
            for (String name : names) {
                joiner.add(Objects.equals(name.toUpperCase(), name) ? name.toLowerCase() : name);
//                metaNameStr += (name.toUpperCase() == name ? name.toLowerCase() : name) + ",";
            }
            return com.csicit.ace.common.utils.StringUtils.trimChar(joiner.toString(), ',');
        } else {
            return Objects.equals(dbObjName.toUpperCase(), dbObjName) ? dbObjName.toLowerCase() : dbObjName;
        }
    }


    /**
     * oracle、dm 类型转换
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    protected String convertToOracleMetaType(ResultSet rs) throws SQLException {
        String dataType = rs.getString("data_type").toUpperCase();
        if (dataType.contains("TIMESTAMP")) {
            return "DateTime";
        } else {
            if (Objects.equals(dataType, "VARCHAR2") || Objects.equals(dataType, "NVARCHAR2")
                    || Objects.equals(dataType, "LONG") || Objects.equals(dataType, "CHAR")
                    || Objects.equals(dataType, "VARCHAR") || Objects.equals(dataType, "CLOB")) {
                return "String";
            }
            if (Objects.equals(dataType, "INTEGER")) {
                return "Int";
            }
            if (Objects.equals(dataType, "BLOB")) {
                return "BLOB";
            }
            if (Objects.equals(dataType, "NCLOB")) {
                return "NCLOB";
            }
            if (Objects.equals(dataType, "DATE")) {
                return "DateTime";
            }
            if (Objects.equals(dataType, "DATETIME")) {
                return "DateTime";
            }
            if (Objects.equals(dataType, "FLOAT")) {
                return "Decimal";
            }
            if (Objects.equals(dataType, "NUMBER")) {
                int scale = rs.getString("data_scale") == null ?
                        0 : Integer.parseInt(rs.getString("data_scale"));
                int precision = rs.getString("data_precision") == null ?
                        0 : Integer.parseInt(rs.getString("data_precision"));
                if (precision == 1 && scale == 0) {
                    return "Bool";
                }
                if (scale == 0) {
                    if (precision > 1 && precision <= 5) {
                        return "Short";
                    } else if (precision > 5 && precision <= 10) {
                        return "Int";
                    } else {
                        return "Long";
                    }
                }
                if (precision >= 0 && scale > 0) {
                    return "Decimal";
                }
            }
            if (Objects.equals(dataType, "BINARY_FLOAT")) {
                return "Float";
            }
            if (Objects.equals(dataType, "BINARY_DOUBLE")
                    || Objects.equals(dataType, "DEC")
                    || Objects.equals(dataType, "NUMERIC")
                    || Objects.equals(dataType, "DOUBLE")) {
                return "Double";
            }
            if (Objects.equals(dataType, "BINARY_INTEGER")) {
                return "Int";
            }
            if (Objects.equals(dataType, "INT")) {
                return "Int";
            }
            if (Objects.equals(dataType, "BIGINT")) {
                return "Int";
            }
            if (Objects.equals(dataType, "SMALLINT")) {
                return "Short";
            }
            if (Objects.equals(dataType, "TIMESTAMP")) {
                return "DateTime";
            }
        }
        throw new RException("ORALCE数据类型匹配失败！(类型：" + dataType + ")");
    }

    /**
     * mysql 类型转换
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    protected String convertToMysqlMetaType(ResultSet rs) throws SQLException {
        String dataType = rs.getString("data_type");
        if (dataType.contains("TIMESTAMP")) {
            return "DateTime";
        } else {

            if (Objects.equals(dataType, "VARCHAR")) {
                return "String";
            }
            if (Objects.equals(dataType, "INTEGER")) {
                return "Int";
            }

            if (Objects.equals(dataType, "SMALLINT")) {
                return "Short";
            }
            if (Objects.equals(dataType, "BIGINT")) {
                return "Long";
            }
            if (Objects.equals(dataType, "BLOB")) {
                return "BLOB";
            }
            if (Objects.equals(dataType, "TEXT")) {
                return "CLOB";
            }
            if (Objects.equals(dataType, "DATE")) {
                return "DateTime";
            }
            if (Objects.equals(dataType, "FLOAT")) {
                return "Float";
            }
            if (Objects.equals(dataType, "DOUBLE")) {
                return "Double";
            }

            if (Objects.equals(dataType, "NUMBER")) {
                int scale = rs.getString("numeric_scale") == null ?
                        0 : Integer.parseInt(rs.getString("numeric_scale"));
                int precision = rs.getString("numeric_precision") == null ?
                        0 : Integer.parseInt(rs.getString("numeric_precision"));
                if (precision == 1 && scale == 0) {
                    return "Bool";
                }
                if (scale == 0) {
                    if (precision > 1 && precision <= 5) {
                        return "Short";
                    } else if (precision > 5 && precision <= 10) {
                        return "Int";
                    } else {
                        return "Long";
                    }
                }
                if (precision >= 0 && scale > 0) {
                    return "Decimal";
                }
            }
        }
        throw new RException("MySQL数据类型匹配失败！(类型：" + dataType + ")");
    }

    /**
     * Oracle 列类型转换
     *
     * @param colType
     * @return
     */
    protected String convertToOracleDBType(String colType) {
        String r = "";
        if (Objects.equals(colType, "String")) {
            return "varchar2";
        }
        if (Objects.equals(colType, "DateTime")) {
            return "date";
        }
        if (Objects.equals(colType, "Int")) {
            return "number(10)";
        }
        if (Objects.equals(colType, "Float")) {
            return "binary_float";
        }
        if (Objects.equals(colType, "Double")) {
            return "binary_double";
        }
        if (Objects.equals(colType, "Short")) {
            return "number(5)";
        }
        if (Objects.equals(colType, "Long")) {
            return "number(19)";
        }
        if (Objects.equals(colType, "Decimal")) {
            return "number";
        }
        if (Objects.equals(colType, "Bool")) {
            return "number(1)";
        }
        if (Objects.equals(colType, "CLOB")) {
            return "clob";
        }
        if (Objects.equals(colType, "BLOB")) {
            return "blob";
        }
        return r;
    }

    /**
     * dm列类型转换
     *
     * @param colType
     * @return
     */
    protected String convertToDmDBType(String colType) {
        String r = "";
        if (Objects.equals(colType, "String")) {
            return "varchar2";
        }
        if (Objects.equals(colType, "DateTime")) {
            return "datetime";
        }
        if (Objects.equals(colType, "Int")) {
            return "number(10)";
        }
        if (Objects.equals(colType, "Float")) {
            return "binary_float";
        }
        if (Objects.equals(colType, "Double")) {
            return "binary_double";
        }
        if (Objects.equals(colType, "Short")) {
            return "number(5)";
        }
        if (Objects.equals(colType, "Long")) {
            return "number(19)";
        }
        if (Objects.equals(colType, "Decimal")) {
            return "number";
        }
        if (Objects.equals(colType, "Bool")) {
            return "number(1)";
        }
        if (Objects.equals(colType, "CLOB")) {
            return "clob";
        }
        if (Objects.equals(colType, "BLOB")) {
            return "blob";
        }
        return r;
    }


    /**
     * mysql 列类型转换
     *
     * @param colType
     * @param dataSize
     * @return
     */
    protected String convertToMysqlDBType(String colType, int dataSize) {
        String r = "";
        if (Objects.equals(colType, "String")) {
            return dataSize > 1000 ? "TEXT" : "VARCHAR";
        }
        if (Objects.equals(colType, "DateTime")) {
            return "DATETIME";
        }
        if (Objects.equals(colType, "Int")) {
            return "INT";
        }
        if (Objects.equals(colType, "Float")) {
            return "FLOAT";
        }
        if (Objects.equals(colType, "Double")) {
            return "DOUBLE";
        }
        if (Objects.equals(colType, "Short")) {
            return "SMALLINT";
        }
        if (Objects.equals(colType, "Long")) {
            return "BIGINT";
        }
        if (Objects.equals(colType, "Decimal")) {
            return "DECIMAL(18,10)";
        }
        if (Objects.equals(colType, "Bool")) {
            return "DECIMAL(1)";
        }
        if (Objects.equals(colType, "CLOB")) {
            return "TEXT";
        }
        if (Objects.equals(colType, "BLOB")) {
            return "Blob";
        }
        return r;
    }

    /**
     * oracle 类型名称定义
     * @param objectName
     * @return
     */
//    protected String wrapDatabaseOracleObjectName(String objectName){
//        if (Objects.equals(objectName.toUpperCase(),objectName)){
//            return objectName;
//        }else{
//            return "\"" + objectName.toUpperCase() + "\"";
//        }
//    }

    /**
     * mysql 类型名称定义
     *
     * @param objectName
     * @return
     */
    protected String wrapDatabaseMysqlObjectName(String objectName) {
        if (Objects.equals(objectName.toUpperCase(), objectName)) {
            return objectName;
        } else {
            return "`" + objectName.toUpperCase() + "`";
        }
    }

    /**
     * 字符串集合转换成逗号分隔字符串
     *
     * @param strs
     * @return
     */
    protected String tranListStrToTrimStr(List<String> strs) {
//        String str = "";
        StringJoiner joiner = new StringJoiner(",");
        for (String s : strs) {
            joiner.add(s);
//            str = str + s + ",";
        }
        return com.csicit.ace.common.utils.StringUtils.trimChar(joiner.toString(), ',');
    }

}
