package com.csicit.ace.common.utils.jdbc;

import java.sql.*;

/**
 * jdbc操作工具
 *
 * @author shanwj
 * @date 2019/11/5 8:36
 */
public class JDBCUtils {

    public static Connection getConnection(String driverName, String url, String user, String password) throws
            Exception {
        Class.forName(driverName);
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeResource(Connection conn, Statement st) throws SQLException {
        st.close();
        conn.close();
        conn = null;
    }

    public static void closeResource(Connection conn, ResultSet rs, PreparedStatement st) throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if(st!=null){
            st.close();
        }
        if(conn!=null){
            conn.close();
        }
    }

    public static void closeResource(ResultSet rs, PreparedStatement st) throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if(st!=null){
            st.close();
        }
    }

    public static String getDriverType(String url){
        url = url.substring(5,url.length());
        String type = url.split(":")[0];
        return type;
    }
}
