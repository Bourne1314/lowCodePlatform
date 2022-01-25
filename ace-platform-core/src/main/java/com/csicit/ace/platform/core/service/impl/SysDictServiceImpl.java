package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysDictDO;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.data.persistent.mapper.SysDictMapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysDictService;
import com.csicit.ace.platform.core.service.SysDictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * 字典类型 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:58
 */
@Service("sysDictService")
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDictDO> implements SysDictService {

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SysDictValueService sysDictValueService;

    @Override
    public boolean saveDict(SysDictDO sysDictDO) {
        if (save(sysDictDO)) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增字典类型", "新增字典类型："+sysDictDO.getName(), securityUtils
                            .getCurrentGroupId(),
                    sysDictDO.getAppId());
        }
        return false;
    }

    @Override
    public boolean updateDict(SysDictDO sysDictDO) {
        // 字典类型变更后，对应的字典数据表的类型也要变更
        if (!Objects.equals(sysDictDO.getType(), getById(sysDictDO.getId()).getType())) {
            List<SysDictValueDO> list = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                    .eq("type_id", sysDictDO.getId()));
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
                list.stream().forEach(sysDictValueDO -> {
                    sysDictValueDO.setType(sysDictDO.getType());
                });
                sysDictValueService.updateBatchById(list);
            }
        }
        if (updateById(sysDictDO)) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新字典类型", "更新字典类型："+sysDictDO.getName(), securityUtils
                            .getCurrentGroupId(),
                    sysDictDO.getAppId());
        }
        return false;
    }

    @Override
    public boolean deleteByIds(Collection<? extends Serializable> idList) {
        StringBuffer stringBuffer = new StringBuffer();
        String appId = getById((
                (List<String>) idList).get(0)).getAppId();
        idList.forEach(id -> {
            SysDictDO sysDictDO = getById(id);
            String str = sysDictDO.getName();
            stringBuffer.append(str);
            stringBuffer.append(",");
        });
        removeByIds(idList);
        return
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"),
                        "删除字典数据项", "删除字典数据项："+stringBuffer.substring(0, stringBuffer.length() - 1), securityUtils
                                .getCurrentGroupId(),
                        appId);
    }
}
