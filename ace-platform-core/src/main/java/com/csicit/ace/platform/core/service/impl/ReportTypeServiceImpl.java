package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.mapper.ReportTypeMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.ReportTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;


/**
 * 报表类别 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-07 08:52:49
 */
@Service("reportTypeService")
public class ReportTypeServiceImpl extends BaseServiceImpl<ReportTypeMapper, ReportTypeDO> implements
        ReportTypeService {

    /**
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2020/6/9 17:16
     */
    @Override
    public boolean saveReportType(ReportTypeDO instance) {
        if (!save(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存业务类型 ", instance,
                securityUtils.getCurrentGroupId(),
                instance.getAppId());
    }

    /**
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2020/6/9 17:16
     */
    @Override
    public boolean updateReportType(ReportTypeDO instance) {
        if (!updateById(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新业务类型 ", instance,
                securityUtils.getCurrentGroupId(),
                instance.getAppId());
    }

    /**
     * 删除
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2020/6/9 17:16
     */
    @Override
    public boolean removeReportType(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        List<String> allIds = new ArrayList<>(16);
        ids.stream().forEach(id -> {
            ReportTypeDO reportTypeDO = getById(id);
            allIds.add(id);
            if (!Objects.equals(reportTypeDO.getParentId(), "-1")) {
                this.getAllChildIds(id, allIds);
            }
        });
        String appId = getById(ids.get(0)).getAppId();
        List<String> names = listByIds(allIds).stream().map(ReportTypeDO::getName).collect(Collectors.toList());
        StringJoiner stringJoiner = new StringJoiner(",");
        names.stream().forEach(name -> {
            stringJoiner.add(name);
        });
        if (CollectionUtils.isNotEmpty(allIds)) {
            if (!removeByIds(allIds)) {
                return false;
            }
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除业务类型", stringJoiner
                .toString(), securityUtils.getCurrentGroupId(), appId);
    }

    private void getAllChildIds(String id, List<String> allIds) {

        List<String> childIds = list(new QueryWrapper<ReportTypeDO>().eq("parent_id", id))
                .stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(childIds)) {
            allIds.addAll(childIds);
            childIds.stream().forEach(childId -> {
                this.getAllChildIds(childId, allIds);
            });
        }
    }
}
