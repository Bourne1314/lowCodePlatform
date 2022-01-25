package com.csicit.ace.dev.util;

import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;

import java.sql.*;
import java.util.Objects;

/**
 * 判断数据库是否可以连接成功
 *
 * @author zuogang
 * @return
 * @date 2020/3/23 17:48
 */
public class DBUtil {

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<changeSet author=\"超级管理员\" id=\"testplat-SYS_APP-1585296466490\"><createTable remarks=\"应用模型\" " +
                "tableName=\"SYS_APP\"><column name=\"ID\" remarks=\"主键\" type=\"VARCHAR(50)\">\n" +
                "<constraints nullable=\"false\"/></column></createTable></changeSet>";
        System.out.println(xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
    }


    public static boolean validateTableNameExist(String tableName, ProDatasourceDO proDatasourceDO) {
        Connection conn = null;

        try {
            conn = getConnection(proDatasourceDO.getDriver(), proDatasourceDO.getUrl(), proDatasourceDO.getUserName()
                    , proDatasourceDO.getPassword());
            String sql = "SELECT 1 FROM " + tableName;

            PreparedStatement st = conn.prepareStatement(sql);
//            st.setString(1, tableName);
            st.execute();

            conn.close();
            conn = null;
            st.close();
            st = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkConnection(String driverName, String url, String user, String password) {
        Connection conn = null;
        try {
            conn = getConnection(driverName, url, user, password);
            conn.close();
            conn = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取数据库连接
     *
     * @return java.sql.Connection
     * @author shanwj
     * @date 2019/6/25 9:14
     */
    public static Connection getConnection(String driverName, String url, String user, String password) throws
            Exception {
        Class.forName(driverName);
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 创建数据及用户
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/3/24 15:01
     */
    public static boolean createDbAndUser(ProDatasourceDO instance) {
        Connection conn = null;
        Statement st = null;
        boolean success = true;
        try {
            if (Objects.equals("mysql", instance.getType())) {
                conn = getConnection(instance.getDriver(), "jdbc:mysql://" + instance.getDbIpAddress() + ":" +
                                instance.getDbPort()
                                + "?serverTimezone=UTC&characterEncoding=utf-8",
                        instance.getDbUser(), instance.getDbPwd());
            } else {
                conn = getConnection(instance.getDriver(), instance.getUrl(),
                        instance.getDbUser(), instance.getDbPwd());
            }

            st = conn.createStatement();
            if (Objects.equals("oracle", instance.getType())) {
                createOracleDB(st, instance);
            } else if (Objects.equals("mysql", instance.getType())) {
                createMysqlDB(st, instance);
            } else if (Objects.equals("dm", instance.getType())) {
                createDmDB(st, instance);
            } else if (Objects.equals("oscar", instance.getType())) {
                createOscarDB(st, instance);
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            if (st != null) {
                st.close();
                st = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 创建dm数据库用户
     *
     * @param
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/3/24 16:09
     */
    private static void createDmDB(Statement st, ProDatasourceDO instance) throws SQLException {
        String sql =
                String.format("create tablespace \"" + instance.getUserName() + "\" datafile '%s' " +
                        "size 40 autoextend on maxsize 16777215 CACHE = NORMAL", instance.getUserName() + ".DBF");
        st.execute(sql);
        sql = "create user \"" + instance.getUserName() + "\" identified by \"" + instance.getPassword() + "\" " +
                "limit failed_login_attemps 3, password_lock_time 1, password_grace_time 10 " +
                "default tablespace \"" + instance.getUserName() + "\" ";
        st.execute(sql);
        st.execute("grant public,resource,dba to " + instance.getUserName().toUpperCase());
    }

    /**
     * 创建Oscar数据库用户
     *
     * @param
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/3/24 16:09
     */
    private static void createOscarDB(Statement st, ProDatasourceDO instance) throws SQLException {
        String sql =
                String.format("CREATE TABLESPACE " + instance.getUserName() + " DATAFILE '/opt/ShenTong/odbs/OSRDB/"
                        + instance.getUserName() + ".dbf' SIZE 90M " +
                        "AUTOEXTEND ON DEFAULT LOGGING INIT 64k NEXT 64k FILL 70 SPLIT 50");
        st.execute(sql);
        sql = "CREATE USER \"" + instance.getUserName() + "\" WITH  DEFAULT TABLESPACE " + instance.getUserName() + "" +
                " PASSWORD '"+instance.getPassword()+"' ";
        st.execute(sql);
        st.execute("GRANT ROLE SYSDBA TO USER "+instance.getUserName()+"");
        st.execute("GRANT ROLE RESOURCE TO USER "+instance.getUserName()+"");
    }

    /**
     * 创建mysql数据库
     *
     * @param st
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/3/24 16:09
     */
    private static void createMysqlDB(Statement st, ProDatasourceDO instance) throws SQLException {
        String sql = "CREATE DATABASE " + instance.getDbName() + " DEFAULT CHARACTER SET utf8 COLLATE " +
                "utf8_general_ci";
        st.execute(sql);
    }

    /**
     * 创建oracle数据库用户
     *
     * @param st
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/3/24 16:09
     */
    private static void createOracleDB(Statement st, ProDatasourceDO instance) throws SQLException {
        String sql =
                String.format("CREATE SMALLFILE TABLESPACE \"" + instance.getUserName() + "\" DATAFILE '%s' SIZE 100M" +
                        " " +
                        "AUTOEXTEND ON NEXT 50M MAXSIZE UNLIMITED LOGGING " +
                        "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO", instance.getUserName() + ".DBF");
        st.execute(sql);
        sql = "CREATE USER \"" + instance.getUserName() + "\" PROFILE \"DEFAULT\" " +
                "IDENTIFIED BY \"" + instance.getPassword() + "\" DEFAULT TABLESPACE \"" + instance.getUserName() +
                "\" " +
                "TEMPORARY TABLESPACE \"TEMP\" ACCOUNT UNLOCK";
        st.execute(sql);
        st.execute("grant connect,resource,dba to " + instance.getUserName().toUpperCase());
    }


    public static void release(Connection conn, Statement statement, ResultSet resultSet) {
        try {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn = null;
            statement = null;
            resultSet = null;
        }
    }
}
