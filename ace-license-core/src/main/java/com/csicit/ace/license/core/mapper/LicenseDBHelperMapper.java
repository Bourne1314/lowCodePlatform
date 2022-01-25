package com.csicit.ace.license.core.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/4/18 14:21
 */
@Mapper
public interface LicenseDBHelperMapper {
    /***
     * <p>
     *  输入sql，返回键值对列表
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSql")
    List<Map<String, Object>> getMaps(String sql);

    @SelectProvider(type = DaoProvider.class, method = "getSql")
    Map<String, Object> getMap(String sql);

    /***
     * <p>
     *  输入sql，返回Strings
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSql")
    List<String> getStrings(String sql);

    /***
     * <p>
     *  输入sql + params，返回Strings
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithParams")
    List<String> getStringsWithParams(String sql, Object... params);

    /***
     * <p>
     *  输入sql，返回String
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSql")
    String getString(String sql);

    /***
     * <p>
     *  输入sql + params，返回String
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithParams")
    String getStringWithParams(String sql, Object... params);

    /***
     * <p>
     *  输入sql + params，返回键值对列表
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithParams")
    List<Map<String, Object>> getMapsWithParams(String sql, Object... params);

    /***
     * <p>
     *  输入sql，返回Count
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSql")
    Integer getCount(String sql);

    /***
     * <p>
     *  输入sql + params，返回Count
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithParams")
    Integer getCountWithParams(String sql, Object... params);

    /***
     * <p>
     *  输入sql + page + limit，返回键值对列表
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithPages")
    List<Map<String, Object>> getMapsByPage(String sql, int page, int limit);


    /***
     * <p>
     *  输入sql + params + page + limit，返回键值对列表
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithParamsAndPages")
    List<Map<String, Object>> getMapsByPageWithParams(String sql, int page, int limit, Object... params);

    /***
     * <p>
     *  执行Sql更新
     * </p>
     *
     * @param sql
     * @return
     */
    @UpdateProvider(type = DaoProvider.class, method = "getSql")
    Integer updateBySql(String sql);

    /***
     * <p>
     *  执行Sql删除
     * </p>
     *
     * @param sql
     * @return
     */
    @DeleteProvider(type = DaoProvider.class, method = "getSql")
    Integer deteleBySql(String sql);

    /***
     * <p>
     *  执行Sql+Params更新
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @UpdateProvider(type = DaoProvider.class, method = "getSqlWithParams")
    Integer updateBySqlWithParams(String sql, Object... params);

    /***
     * <p>
     *  执行Sql+Params删除
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @DeleteProvider(type = DaoProvider.class, method = "getSqlWithParams")
    Integer deteleBySqlWithParams(String sql, Object... params);

    class DaoProvider {
        public String getSql(String sql) {
            return sql;
        }

        public String getSqlWithParams(String sql, Object... params) {
            return MessageFormat.format(sql, params);
        }

        public String getSqlWithPages(String sql, int page, int limit) {
            int minNum = (page - 1) * limit;
            int maxNum = page * limit;
            return "select * from ( select *,rn from ("
                    + sql
                    + ")) where rn > " + minNum + " and rn <=" + maxNum;
        }

        public String getSqlWithParamsAndPages(String sql, int page, int limit, Object... params) {
            int minNum = (page - 1) * limit;
            int maxNum = page * limit;
            return "select * from ( select *,rn from ("
                    + MessageFormat.format(sql, params)
                    + ")) where rn > " + minNum + " and rn <=" + maxNum;
        }
    }

    /***
     * <p>
     *  输入sql + params，返回当前条件下的最大排序号
     *  (sql,params) 规范 ("select * from sys_user where user_id=''{0}''",userId) 字符串需要用两个单引号
     * </p>
     *
     * @param sql
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "getSqlWithParams")
    Integer getMaxSortNoWithParams(String sql, Object... params);
}
