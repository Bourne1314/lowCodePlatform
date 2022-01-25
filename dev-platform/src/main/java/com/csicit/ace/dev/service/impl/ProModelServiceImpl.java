package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProModelColDO;
import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.ProModelMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.LiquibaseService;
import com.csicit.ace.dev.service.ProModelColService;
import com.csicit.ace.dev.service.ProModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 实体模型 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("proModelService")
public class ProModelServiceImpl extends BaseServiceImpl<ProModelMapper, ProModelDO>
        implements
        ProModelService {
    @Autowired
    ProModelColService proModelColService;
    @Autowired
    LiquibaseService liquibaseService;

    /**
     * 新增实体模型
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addModel(ProModelDO instance) {
        instance.setId(UuidUtils.createUUID());
        instance.setCreateTime(LocalDateTime.now());
        instance.setPkName("PK_" + instance.getTableName());
        if (!liquibaseService.addLiquibaseTable(instance)) {
            return false;
        }
        if (!save(instance)) {
            return false;
        }

        // 创建表时默认生成一个ID主键字段,不然liquibase执行不了
        //增加数据列
        ProModelColDO proModelColDO = new ProModelColDO();
        proModelColDO.setCreateTime(LocalDateTime.now());
        proModelColDO.setId(UuidUtils.createUUID());
        proModelColDO.setCaption("主键");
        proModelColDO.setDataPrecision(50);
        proModelColDO.setDataScale(0);
        proModelColDO.setModelId(instance.getId());
        proModelColDO.setNullable(0);
        proModelColDO.setObjColName("id");
        proModelColDO.setPkFlg(1);
        proModelColDO.setSyscol(0);
        proModelColDO.setDataType("Varchar");
        proModelColDO.setTabColName("ID");
        if (!proModelColService.save(proModelColDO)) {
            return false;
        }

        return true;

    }

    /**
     * 修改实体模型
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean updModel(ProModelDO instance) {
        ProModelDO oldModelDO = getById(instance.getId());
        if (!Objects.equals(oldModelDO.getTableName(), instance.getTableName()) || !Objects.equals(oldModelDO
                .getModelName(), instance.getModelName())) {
            if (!liquibaseService.updLiquibaseTable(instance)) {
                return false;
            }
        }
        instance.setPkName("PK_" + instance.getTableName());

        if (!updateById(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 删除实体模型
     *
     * @param id
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean delModel(String id) {
        ProModelDO modelDO = getById(id);
        if (!liquibaseService.delLiquibaseTable(modelDO)) {
            return false;
        }
        // 删除
        if (!removeById(id)) {
            return false;
        }
        return true;
    }
}
