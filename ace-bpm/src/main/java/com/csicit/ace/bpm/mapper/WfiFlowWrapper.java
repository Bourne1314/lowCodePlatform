package com.csicit.ace.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import org.apache.ibatis.annotations.*;

/**
 * 流程实例
 *
 * @author JonnyJiang
 * @date 2019/9/5 19:01
 */
@Mapper
public interface WfiFlowWrapper extends BaseMapper<WfiFlowDO> {
    /**
     * 删除业务表单
     *
     * @param tableName   业务表单表名
     * @param columnName  业务标识对应的列名
     * @param businessKey 业务标识
     * @return 删除行数
     * @author JonnyJiang
     * @date 2019/11/6 19:39
     */

    @Delete("DELETE FROM ${tableName} WHERE ${columnName}=#{businessKey}")
    Integer deleteFormByBusinessKey(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("businessKey") String businessKey);

    /**
     * 同步数据
     *
     * @param tableName   表名
     * @param columnName  列名
     * @param val         值
     * @param businessKey 业务标识
     * @return 更新行数
     */
    @Update("UPDATE ${tableName} SET ${columnName}=#{val} WHERE ${id}=#{businessKey}")
    Integer syncData(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("val") Object val, @Param("id") String id, @Param("businessKey") String businessKey);

    /**
     * 获取业务数据条数
     *
     * @param tableName   数据表
     * @param idName      id列名
     * @param businessKey 业务标识
     * @return 业务数据条数
     * @author JonnyJiang
     * @date 2020/1/2 17:13
     */

    @Select("SELECT COUNT(1) FROM ${tableName} WHERE ${id}=#{businessKey}")
    Integer getBusinessCount(@Param("tableName") String tableName, @Param("id") String idName, @Param("businessKey") String businessKey);

    /**
     * 获取表单字段值
     *
     * @param tableName   表名
     * @param columnName  列名
     * @param idName      业务标识列名
     * @param businessKey 业务标识
     * @return 获取表单字段值
     * @author JonnyJiang
     * @date 2020/1/13 17:07
     */

    @Select("SELECT ${columnName} FROM ${tableName} WHERE ${id}=#{businessKey}")
    Object getFormValue(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("id") String idName, @Param("businessKey") String businessKey);
}