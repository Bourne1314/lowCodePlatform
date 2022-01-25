package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.*;
import liquibase.database.Database;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.util.Map;

/**
 * liquibase 实例对象访问接口
 *
 * @param
 * @author zuogang
 * @return
 * @date 2019/4/22 15:32
 */
@Transactional
public interface LiquibaseService {
    /**
     * 通过liqibase生成第一版changelog
     *
     * @return
     */
    Map<String, Object> getFirstChangelogByDs(ProDatasourceDO datasourceDO);

    /**
     * 生成changlog
     *
     * @return
     */
    void createChangeLogNewVersion(String serviceId, ServletOutputStream outputStream);

    /**
     * 生成changlog
     *
     * @return
     */
    void createChangeLogAllVersion(String serviceId, ServletOutputStream outputStream);

    /**
     * 获取liquibase的dataBase
     */
    Database getDatabase(ProDatasourceDO proDatasourceDO);

    /**
     * 新增数据表
     */
    boolean addLiquibaseTable(ProModelDO instance);

    /**
     * 修改数据表
     */
    boolean updLiquibaseTable(ProModelDO instance);

    /**
     * 删除数据表
     */
    boolean delLiquibaseTable(ProModelDO modelDO);

    /**
     * 新增数据列
     */
    boolean addLiquibaseCol(ProModelColDO proModelColDO);

    /**
     * 修改数据列
     */
    boolean updLiquibaseCol(ProModelColDO proModelColDO);

    /**
     * 删除数据列
     */
    boolean delLiquibaseCol(ProModelColDO proModelColDO);

    /**
     * 新增索引
     */
    boolean addLiquibaseIndex(ProModelIndexDO proModelIndexDO);

    /**
     * 修改索引
     */
    boolean updLiquibaseIndex(ProModelIndexDO proModelIndexDO);

    /**
     * 删除索引
     */
    boolean delLiquibaseIndex(ProModelIndexDO proModelIndexDO);

    /**
     * 新增外键
     */
    boolean addLiquibaseAss(ProModelAssociationDO proModelAssociationDO);

    /**
     * 修改外键
     */
    boolean updLiquibaseAss(ProModelAssociationDO proModelAssociationDO);

    /**
     * 删除外键
     */
    boolean delLiquibaseAss(ProModelAssociationDO proModelAssociationDO);
}
