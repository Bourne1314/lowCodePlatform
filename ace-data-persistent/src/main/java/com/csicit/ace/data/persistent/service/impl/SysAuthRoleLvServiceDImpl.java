package com.csicit.ace.data.persistent.service.impl;

import com.aspose.slides.internal.mx.add;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.domain.SysAuthApiDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleLvDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthRoleLvMapper;
import com.csicit.ace.data.persistent.service.SysAuthRoleLvServiceD;
import com.csicit.ace.data.persistent.service.SysAuthRoleServiceD;
import com.csicit.ace.data.persistent.service.SysAuthRoleVServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统管理-角色授权版本控制 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:24
 */
@Service("sysAuthRoleLvServiceD")
public class SysAuthRoleLvServiceDImpl extends BaseServiceImpl<SysAuthRoleLvMapper, SysAuthRoleLvDO> implements
        SysAuthRoleLvServiceD {

    @Autowired
    SysAuthRoleServiceD sysAuthRoleServiceD;

    @Autowired
    SysAuthRoleVServiceD sysAuthRoleVServiceD;

    @Override
    public boolean authRoleUpdate(AppUpgrade appUpgrade) {

        // 角色权限授权版本更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuthRoleLv())) {
            List<SysAuthRoleLvDO> authRoleLvAdd = new ArrayList<>(16);
            List<SysAuthRoleLvDO> authRoleLvUpd = new ArrayList<>(16);

            appUpgrade.getAuthRoleLv().stream().forEach(authRoleLvDetail -> {
                SysAuthRoleLvDO item = JsonUtils.castObject(authRoleLvDetail, SysAuthRoleLvDO.class);
                // 判断当前业务数据库中是否存在该条数据
                SysAuthRoleLvDO sysAuthRoleLvDO = getOne(new QueryWrapper<SysAuthRoleLvDO>()
                        .eq("trace_id", item.getTraceId()).eq("app_id", appUpgrade.getAppId()));
                if (sysAuthRoleLvDO == null) {
                    authRoleLvAdd.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(sysAuthRoleLvDO, item)) {
                        item.setId(sysAuthRoleLvDO.getId());
                        authRoleLvUpd.add(item);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(authRoleLvAdd)) {
                if (!saveBatch(authRoleLvAdd)) {
                    return false;
                }

            }
            if (CollectionUtils.isNotEmpty(authRoleLvUpd)) {
                if (!updateBatchById(authRoleLvUpd)) {
                    return false;
                }
            }
        }


        // 角色权限版本历史数据
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuthRoleV())) {
            List<SysAuthRoleVDO> authRoleVAdd = new ArrayList<>(16);
            List<SysAuthRoleVDO> authRoleVUpd = new ArrayList<>(16);
            appUpgrade.getAuthRoleV().stream().forEach(authRoleVDetail -> {
                SysAuthRoleVDO item = JsonUtils.castObject(authRoleVDetail, SysAuthRoleVDO.class);
                SysAuthRoleVDO sysAuthRoleVDO = sysAuthRoleVServiceD.getOne(new QueryWrapper<SysAuthRoleVDO>()
                        .eq("trace_id", item.getTraceId()).inSql("auth_id", "select id from sys_auth where app_id='" +
                                appUpgrade.getAppId() + "'"));
                if (sysAuthRoleVDO == null) {
                    authRoleVAdd.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(sysAuthRoleVDO, item)) {
                        item.setId(sysAuthRoleVDO.getId());
                        authRoleVUpd.add(item);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(authRoleVAdd)) {
                if (!sysAuthRoleVServiceD.saveBatch(authRoleVAdd)) {
                    return false;
                }

            }
            if (CollectionUtils.isNotEmpty(authRoleVUpd)) {
                if (!sysAuthRoleVServiceD.updateBatchById(authRoleVUpd)) {
                    return false;
                }
            }
        }

        // 角色授权
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuthRole())) {
            List<SysAuthRoleDO> authRoleAdd = new ArrayList<>(16);
            List<SysAuthRoleDO> authRoleUpd = new ArrayList<>(16);
            appUpgrade.getAuthRole().stream().forEach(authRoleDetail -> {
                SysAuthRoleDO item = JsonUtils.castObject(authRoleDetail, SysAuthRoleDO.class);
                SysAuthRoleDO sysAuthRoleDO = sysAuthRoleServiceD.getOne(new QueryWrapper<SysAuthRoleDO>()
                        .eq("trace_id", item.getTraceId()).inSql("auth_id", "select id from sys_auth where app_id='" +
                                appUpgrade.getAppId() + "'"));
                if (sysAuthRoleDO == null) {
                    authRoleAdd.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(sysAuthRoleDO, item)) {
                        item.setId(sysAuthRoleDO.getId());
                        authRoleUpd.add(item);
                    }
                }
            });
            if (CollectionUtils.isNotEmpty(authRoleAdd)) {
                if (!sysAuthRoleServiceD.saveBatch(authRoleAdd)) {
                    return false;
                }

            }
            if (CollectionUtils.isNotEmpty(authRoleUpd)) {
                if (!sysAuthRoleServiceD.updateBatchById(authRoleUpd)) {
                    return false;
                }
            }
        }


        return true;
    }
}
