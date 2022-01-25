package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.GroupDatasourceDetail;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysGroupDatasourceMapper;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 应用绑定数据源 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:31
 */
@Service
public class SysGroupDatasourceServiceImpl extends BaseServiceImpl<SysGroupDatasourceMapper, SysGroupDatasourceDO>
        implements SysGroupDatasourceService {

    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;

    @Override
    public R insert(SysGroupDatasourceDO instance) {
        // 是否主数据源
        if (save(instance)) {
            if (Objects.equals(1, instance.getMajor())) {
                update(new SysGroupDatasourceDO(), new UpdateWrapper<SysGroupDatasourceDO>()
                        .ne("id", instance.getId()).eq("app_id", instance.getAppId()).eq("IS_MAJOR", 1)
                        .set("IS_MAJOR", 0));
                sysGroupAppServiceD.update(new SysGroupAppDO(), new UpdateWrapper<SysGroupAppDO>()
                        .eq("id", instance.getAppId()).set("DATASOURCE_ID", instance.getId()));
            }
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存数据源", "保存数据源："+instance.getName(),
                    securityUtils.getCurrentGroupId(),
                    instance.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R update(SysGroupDatasourceDO instance) {
        // 是否主数据源
        if (Objects.equals(1, instance.getMajor())) {
            update(new SysGroupDatasourceDO(), new UpdateWrapper<SysGroupDatasourceDO>()
                    .eq("app_id", instance.getAppId()).eq("IS_MAJOR", 1)
                    .set("IS_MAJOR", 0));
            sysGroupAppServiceD.update(new SysGroupAppDO(), new UpdateWrapper<SysGroupAppDO>()
                    .eq("id", instance.getAppId()).set("DATASOURCE_ID", instance.getId()));
        }
        if (updateById(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新数据源","更新数据源："+ instance.getName(),
                    securityUtils.getCurrentGroupId(),
                    instance.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R delete(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        List<SysGroupDatasourceDO> list = list(new QueryWrapper<SysGroupDatasourceDO>()
                .and(idList == null || idList.size() == 0, i -> i.eq("1", "2")).in("id", idList));
        if (removeByIds(idList)) {
            SysGroupAppDO sysGroupAppDO = sysGroupAppServiceD.getById(list.get(0).getAppId());
            if (idList.contains(sysGroupAppDO.getDatasourceId())) {
                sysGroupAppDO.setDatasourceId(null);
                sysGroupAppServiceD.updateById(sysGroupAppDO);
            }
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "删除数据源", "删除数据源："+list.parallelStream
                            ().map
                            (SysGroupDatasourceDO::getName)
                            .collect(Collectors.toList()), securityUtils.getCurrentGroupId(),
                    list.get(0).getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    @Override
    public String getDsUrl(String id) {
        SysGroupDatasourceDO datasource = getById(id);
        return getDsUrl(datasource);
    }

    @Override
    public String getDsUrl(SysGroupDatasourceDO datasource) {
        String url;
        if (datasource.getDriverName().contains("oracle")) {
            url = String.format("url=jdbc:oracle:thin:@%s;user=%s;password=%s;driver=%s",
                    datasource.getUrl(), datasource.getUsername(), datasource.getPassword(), datasource.getDriverName
                            ());
        } else {
            int i = datasource.getDriverName().indexOf('.');
            String type = datasource.getDriverName().substring(0, i);
            url = String.format("url=jdbc:%s://%s;user=%s;password=%s;driver=%s",
                    type, datasource.getUrl(), datasource.getUsername(), datasource.getPassword(), datasource
                            .getDriverName());
        }
        return url;
    }


    @Override
    public boolean groupDatasourceUpdate(List<GroupDatasourceDetail> groupDatasources, String appId) {
        List<SysGroupDatasourceDO> add = new ArrayList<>(16);
        List<SysGroupDatasourceDO> upd = new ArrayList<>(16);
        groupDatasources.stream().forEach(groupDatasourceDetail -> {
            SysGroupDatasourceDO item = JsonUtils.castObject(groupDatasourceDetail, SysGroupDatasourceDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysGroupDatasourceDO sysGroupDatasourceDO = getOne(new QueryWrapper<SysGroupDatasourceDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysGroupDatasourceDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysGroupDatasourceDO, item)) {
                    item.setId(sysGroupDatasourceDO.getId());
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

        return true;
    }
}
