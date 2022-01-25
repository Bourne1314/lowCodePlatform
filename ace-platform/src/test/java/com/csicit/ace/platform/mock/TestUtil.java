package com.csicit.ace.platform.mock;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestUtil {
    private static String userName = "JARIACEPLAT";
    private static String passWord = "jarisoft11";
    private static String driverName = "dm.jdbc.driver.DmDriver";
    private static String url = "jdbc:dm://192.168.19.9:5236:JARIACEPLAT";

    /**
     * @param sqlName
     * @return
     * @author zuogang
     * @date 2020/2/12 11:13
     */
    public static Connection executeSql(String sqlName) throws Exception {
        Connection conn = null;
        try {
            conn = getConnection();

            ScriptRunner runner = new ScriptRunner(conn);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            // 执行SQL脚本
            runner.runScript(Resources.getResourceAsReader("testSql/" + sqlName));
            conn.close();
            conn = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获取数据库连接
     *
     * @return java.sql.Connection
     * @author shanwj
     * @date 2019/6/25 9:14
     */
    public static Connection getConnection() throws
            Exception {
        Class.forName(driverName);
        return DriverManager.getConnection(url, userName, passWord);
    }


}
