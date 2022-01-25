package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.*;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.ProServiceMapper;
import com.csicit.ace.data.persistent.service.*;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务管理 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019/11/25 11:12
 */
@Service("proServiceServiceD")
public class ProServiceServiceDImpl extends BaseServiceImpl<ProServiceMapper, ProServiceDO> implements
        ProServiceServiceD {

    @Autowired
    ProModelServiceD proModelServiceD;

    @Autowired
    ProModelAssociationServiceD proModelAssociationServiceD;

    @Autowired
    ProModelColServiceD proModelColServiceD;

    @Autowired
    ProModelIndexServiceD proModelIndexServiceD;

    /**
     * 应用升级时，模型更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 15:07
     */
    @Override
    public boolean modelUpdate(AppUpgrade appUpgrade) {

        // 服务信息
        List<ProServiceDO> serviceAdd = new ArrayList<>(16);
        List<ProServiceDO> serviceUpd = new ArrayList<>(16);

        appUpgrade.getServices().stream().forEach(serviceDetail -> {
            ProServiceDO item = JsonUtils.castObject(serviceDetail, ProServiceDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ProServiceDO proServiceDO = getOne(new QueryWrapper<ProServiceDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appUpgrade.getAppId()));
            if (proServiceDO == null) {
                serviceAdd.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(proServiceDO, item)) {
                    item.setId(proServiceDO.getId());
                    serviceUpd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(serviceAdd)) {
            if (!saveBatch(serviceAdd)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(serviceUpd)) {
            if (!updateBatchById(serviceUpd)) {
                return false;
            }
        }

        // 模型
        if (CollectionUtils.isNotEmpty(appUpgrade.getModel())) {
            if (!modelUpdate(appUpgrade.getModel(), appUpgrade.getAppId())) {
                return false;
            }
        }


        // 属性
        if (CollectionUtils.isNotEmpty(appUpgrade.getModelCol())) {
            if (!modelColUpdate(appUpgrade.getModelCol(), appUpgrade.getAppId())) {
                return false;
            }
        }

        // 外键关联
        if (CollectionUtils.isNotEmpty(appUpgrade.getModelAss())) {
            if (!modelAssUpdate(appUpgrade.getModelAss(), appUpgrade.getAppId())) {
                return false;
            }
        }

        // 索引
        if (CollectionUtils.isNotEmpty(appUpgrade.getModelIndex())) {
            if (!modelIndexUpdate(appUpgrade.getModelIndex(), appUpgrade.getAppId())) {
                return false;
            }
        }


        return true;
    }

    private boolean modelIndexUpdate(List<ModelIndexDetail> modelIndexDetails, String appId) {
        List<ProModelIndexDO> add = new ArrayList<>(16);
        List<ProModelIndexDO> upd = new ArrayList<>(16);

        modelIndexDetails.stream().forEach(modelIndexDetail -> {
            ProModelIndexDO item = JsonUtils.castObject(modelIndexDetail, ProModelIndexDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ProModelIndexDO proModelIndexDO = proModelIndexServiceD.getOne(new
                    QueryWrapper<ProModelIndexDO>()
                    .eq("trace_id", item.getTraceId())
                    .inSql("MODEL_ID", "select id from pro_model where service_id in (select id from pro_service " +
                            "where app_id ='" + appId + "')"));
            if (proModelIndexDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(proModelIndexDO, item)) {
                    item.setId(proModelIndexDO.getId());
                    upd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(add)) {
            if (!proModelIndexServiceD.saveBatch(add)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!proModelIndexServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }

    private boolean modelAssUpdate(List<ModelAssDetail> modelAssDetails, String appId) {
        List<ProModelAssociationDO> add = new ArrayList<>(16);
        List<ProModelAssociationDO> upd = new ArrayList<>(16);

        modelAssDetails.stream().forEach(modelAssDetail -> {
            ProModelAssociationDO item = JsonUtils.castObject(modelAssDetail, ProModelAssociationDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ProModelAssociationDO proModelAssociationDO = proModelAssociationServiceD.getOne(new
                    QueryWrapper<ProModelAssociationDO>()
                    .eq("trace_id", item.getTraceId())
                    .inSql("MODEL_ID", "select id from pro_model where service_id in (select id from pro_service" +
                            " where " + "app_id ='" + appId + "')"));
            if (proModelAssociationDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(proModelAssociationDO, item)) {
                    item.setId(proModelAssociationDO.getId());
                    upd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(add)) {
            if (!proModelAssociationServiceD.saveBatch(add)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!proModelAssociationServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }

    private boolean modelColUpdate(List<ModelColDetail> modelColDetails, String appId) {
        List<ProModelColDO> add = new ArrayList<>(16);
        List<ProModelColDO> upd = new ArrayList<>(16);

        modelColDetails.stream().forEach(modelColDetail -> {
            ProModelColDO item = JsonUtils.castObject(modelColDetail, ProModelColDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ProModelColDO proModelColDO = proModelColServiceD.getOne(new QueryWrapper<ProModelColDO>()
                    .eq("trace_id", item.getTraceId()).inSql("model_id", "select id from pro_model where service_id " +
                            "in (select id from pro_service where " +
                            "app_id ='" + appId + "')"));
            if (proModelColDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(proModelColDO, item)) {
                    item.setId(proModelColDO.getId());
                    upd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(add)) {
            if (!proModelColServiceD.saveBatch(add)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!proModelColServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }

    private boolean modelUpdate(List<ModelDetail> modelDetails, String appId) {

        List<ProModelDO> add = new ArrayList<>(16);
        List<ProModelDO> upd = new ArrayList<>(16);

        modelDetails.stream().forEach(modelDetail -> {
            ProModelDO item = JsonUtils.castObject(modelDetail, ProModelDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ProModelDO proModelDO = proModelServiceD.getOne(new QueryWrapper<ProModelDO>()
                    .eq("trace_id", item.getTraceId()).inSql("SERVICE_ID", "select id from pro_service where " +
                            "app_id='" + appId + "'"));
            if (proModelDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(proModelDO, item)) {
                    item.setId(proModelDO.getId());
                    upd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(add)) {
            if (!proModelServiceD.saveBatch(add)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!proModelServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }
}
