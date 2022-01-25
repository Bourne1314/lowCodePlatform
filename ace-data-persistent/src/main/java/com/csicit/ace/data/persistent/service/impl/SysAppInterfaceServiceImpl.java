package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.AppInterfaceDetail;
import com.csicit.ace.common.AppUpgradeJaxb.AppInterfaceInDetail;
import com.csicit.ace.common.AppUpgradeJaxb.AppInterfaceOutDetail;
import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceOutputDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysAppInterfaceMapper;
import com.csicit.ace.data.persistent.service.SysAppInterfaceInputServiceD;
import com.csicit.ace.data.persistent.service.SysAppInterfaceOutputServiceD;
import com.csicit.ace.data.persistent.service.SysAppInterfaceServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 应用接口信息表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-03 09:03:59
 */
@Service("sysAppInterfaceServiceD")
public class SysAppInterfaceServiceImpl extends BaseServiceImpl<SysAppInterfaceMapper, SysAppInterfaceDO> implements
        SysAppInterfaceServiceD {
    @Autowired
    SysAppInterfaceInputServiceD sysAppInterfaceInputServiceD;

    @Autowired
    SysAppInterfaceOutputServiceD sysAppInterfaceOutputServiceD;

    /**
     * 应用升级时，接口更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 17:05
     */
    @Override
    public boolean interfaceUpdate(AppUpgrade appUpgrade) {
        // 接口
        List<SysAppInterfaceDO> add = new ArrayList<>(16);
        List<SysAppInterfaceDO> upd = new ArrayList<>(16);

        appUpgrade.getAppInterface().stream().forEach(appInterfaceDetail -> {
            SysAppInterfaceDO item = JsonUtils.castObject(appInterfaceDetail, SysAppInterfaceDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysAppInterfaceDO sysAppInterfaceDO = getOne(new QueryWrapper<SysAppInterfaceDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appUpgrade.getAppId()));
            if (sysAppInterfaceDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysAppInterfaceDO, item)) {
                    item.setId(sysAppInterfaceDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!updateBatchById(upd)) {
                return false;
            }
        }

        // 入参
        if (CollectionUtils.isNotEmpty(appUpgrade.getAppInterfaceIn())) {
            if (!interfaceInputUpdate(appUpgrade.getAppInterfaceIn(), appUpgrade.getAppId())) {
                return false;
            }
        }

        // 出参
        if (CollectionUtils.isNotEmpty(appUpgrade.getAppInterfaceOut())) {
            if (!interfaceOutUpdate(appUpgrade.getAppInterfaceOut(), appUpgrade.getAppId())) {
                return false;
            }
        }

        return true;
    }

    private boolean interfaceOutUpdate(List<AppInterfaceOutDetail> appInterfaceOutDetails, String appId) {
        List<SysAppInterfaceOutputDO> add = new ArrayList<>(16);
        List<SysAppInterfaceOutputDO> upd = new ArrayList<>(16);

        appInterfaceOutDetails.stream().forEach(appInterfaceOutDetail -> {
            SysAppInterfaceOutputDO item = JsonUtils.castObject(appInterfaceOutDetail, SysAppInterfaceOutputDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysAppInterfaceOutputDO sysAppInterfaceOutputDO = sysAppInterfaceOutputServiceD.getOne(new
                    QueryWrapper<SysAppInterfaceOutputDO>()
                    .eq("trace_id", item.getTraceId()).inSql("INTERFACE_ID", "select id from sys_app_interface where" +
                            " app_id='" + appId + "'"));
            if (sysAppInterfaceOutputDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysAppInterfaceOutputDO, item)) {
                    item.setId(sysAppInterfaceOutputDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!sysAppInterfaceOutputServiceD.saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!sysAppInterfaceOutputServiceD.updateBatchById(upd)) {
                return false;
            }
        }

        return true;
    }

    private boolean interfaceInputUpdate(List<AppInterfaceInDetail> appInterfaceInDetails, String appId) {
        List<SysAppInterfaceInputDO> add = new ArrayList<>(16);
        List<SysAppInterfaceInputDO> upd = new ArrayList<>(16);

        appInterfaceInDetails.stream().forEach(appInterfaceInDetail -> {
            SysAppInterfaceInputDO item = JsonUtils.castObject(appInterfaceInDetail, SysAppInterfaceInputDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysAppInterfaceInputDO sysAppInterfaceInputDO = sysAppInterfaceInputServiceD.getOne(new
                    QueryWrapper<SysAppInterfaceInputDO>()
                    .eq("trace_id", item.getTraceId()).inSql("INTERFACE_ID", "select id from sys_app_interface where " +
                            "app_id='" + appId + "'"));
            if (sysAppInterfaceInputDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysAppInterfaceInputDO, item)) {
                    item.setId(sysAppInterfaceInputDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!sysAppInterfaceInputServiceD.saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!sysAppInterfaceInputServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }
}
