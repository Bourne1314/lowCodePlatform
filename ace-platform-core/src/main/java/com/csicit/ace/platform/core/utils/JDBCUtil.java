package com.csicit.ace.platform.core.utils;

import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.data.persistent.service.impl.SysGroupDatasourceServiceImpl;
import com.csicit.ace.platform.core.service.SysGroupAppService;
import com.csicit.ace.platform.core.service.impl.SysGroupAppServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC连接工具
 *
 * @author shanwj
 * @date 2019/6/3 17:05
 */
public class JDBCUtil {

    /**
     * app访问接口对象
     */
    static SysGroupAppService sysGroupAppService = SpringContextUtils.getBean(SysGroupAppServiceImpl.class);
    /**
     * 应用数据源访问接口对象
     */
    static SysGroupDatasourceService sysGroupDatasourceService =
            SpringContextUtils.getBean(SysGroupDatasourceServiceImpl.class);

    /**
     * 数据库连接对象
     *
     * @param appId 应用id
     * @return 数据库连接对象
     * @throws Exception
     */
    public static Connection getConnection(String appId) throws Exception {
        SysGroupAppDO app = sysGroupAppService.getById(appId);
        if (app == null || app.getDatasourceId() == null) {
            return null;
        }
        SysGroupDatasourceDO sysGroupDatasourceDO = sysGroupDatasourceService.getById(app.getDatasourceId());
        if (sysGroupDatasourceDO == null) {
            return null;
        }
        Class.forName(sysGroupDatasourceDO.getDriverName());
        return DriverManager.getConnection(sysGroupDatasourceDO.getUrl(),
                sysGroupDatasourceDO.getUsername(), sysGroupDatasourceDO.getPassword());
    }

    /**
     * 获取数据库连接
     *
     * @return java.sql.Connection
     * @author shanwj
     * @date 2019/6/25 9:14
     */
    public static  Connection getConnection(String driverName, String url, String user, String password) throws
            Exception {
        Class.forName(driverName);
        return DriverManager.getConnection(url, user, password);
    }


    public static void closeResource(Connection conn, PreparedStatement st) throws SQLException {
        st.close();
        conn.close();
    }

    public static void closeResource(Connection conn, ResultSet rs, PreparedStatement st) throws SQLException {
        st.close();
        rs.close();
        conn.close();
    }

    public static List<List<String>> getListGroupBy(List<String> ids) {
        List<List<String>> list = new ArrayList<>(16);
        int maxSize = 500;
        if (ids.size() <= maxSize) {
            list.add(ids);
        } else {
            int maxLength = ids.size() / maxSize;
            if (ids.size() % maxSize != 0) {
                maxLength = maxLength + 1;
            }
            for (int i = 0; i < maxLength; i++) {
                list.add( ids.subList(i * maxSize, ((i + 1) * 500 > ids.size()) ? ids.size() : ((i + 1) * 500)));
            }
        }
        return list;
    }

//    public static  <T> QueryWrapper<T> getWrapperForBigData(QueryWrapper<T> wrapper, String string, List<String>
// ids) {
//        int maxSize = 900;
//        if (ids.size() < 1000) {
//            return wrapper.in(string, ids);
//        } else {
//            int count = ids.size() / maxSize + 1;
//            return wrapper.and(a ->
//                    a.in(string, ids.subList(0, maxSize))
//                            .or(count > 1).in(count > 1, string, ids.subList(maxSize> ids.size() ? ids
//                            .size() : maxSize, maxSize * 2 > ids.size() ? ids
//                            .size() : maxSize * 2))
//                            .or(count > 2).in(count > 2, string, ids.subList(maxSize * 2 > ids.size() ? ids
//                            .size() : maxSize * 2, maxSize * 3 > ids.size() ? ids
//                            .size() : maxSize * 3))
//                            .or(count > 3).in(count > 3, string, ids.subList(maxSize * 3> ids.size() ? ids
//                            .size() : maxSize * 3, maxSize * 4 > ids.size() ? ids
//                            .size() : maxSize * 4))
//                            .or(count > 4).in(count > 4, string, ids.subList(maxSize * 4> ids.size() ? ids
//                            .size() : maxSize * 4, maxSize * 5 > ids.size() ? ids
//                            .size() : maxSize * 5))
//                            .or(count > 5).in(count > 5, string, ids.subList(maxSize * 5> ids.size() ? ids
//                            .size() : maxSize * 5, maxSize *6 > ids.size() ? ids
//                            .size() : maxSize * 6))
//                            .or(count > 6).in(count > 6, string, ids.subList(maxSize * 6> ids.size() ? ids
//                            .size() : maxSize * 6, maxSize * 7 > ids.size() ? ids
//                            .size() : maxSize * 7))
//                            .or(count > 7).in(count > 7, string, ids.subList(maxSize * 7> ids.size() ? ids
//                            .size() : maxSize * 7, maxSize * 8 > ids.size() ? ids
//                            .size() : maxSize * 8))
//                            .or(count > 8).in(count > 8, string, ids.subList(maxSize * 8> ids.size() ? ids
//                            .size() : maxSize * 8, maxSize * 9 > ids.size() ? ids
//                            .size() : maxSize * 9))
//                            .or(count > 9).in(count > 9, string, ids.subList(maxSize * 9> ids.size() ? ids
//                            .size() : maxSize * 9, maxSize * 10 > ids.size() ? ids
//                            .size() : maxSize * 10))
//                            .or(count > 10).in(count > 10, string, ids.subList(maxSize * 10> ids.size() ? ids
//                            .size() : maxSize * 10, maxSize * 11 > ids.size() ? ids
//                            .size() : maxSize * 11))
//            );
//        }
//    }
}
