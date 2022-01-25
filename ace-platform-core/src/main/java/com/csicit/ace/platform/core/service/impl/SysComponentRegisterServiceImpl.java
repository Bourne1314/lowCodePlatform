package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysComponentRegisterDO;
import com.csicit.ace.data.persistent.mapper.SysComponentRegisterMapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysComponentRegisterService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 组件注册 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Service("sysComponentRegisterService")
public class SysComponentRegisterServiceImpl extends BaseServiceImpl<SysComponentRegisterMapper,
        SysComponentRegisterDO> implements
        SysComponentRegisterService {

    /**
     * 新增组件注册信息
     *
     * @param sysComponentRegisterDO
     * @return
     * @author zuogang
     * @date 2020/4/2 10:33
     */
    @Override
    public boolean addComponent(SysComponentRegisterDO sysComponentRegisterDO) {
        sysComponentRegisterDO.setCreateTime(LocalDateTime.now());
        if (!save(sysComponentRegisterDO)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增组件注册信息",
                "新增组件注册信息："+ sysComponentRegisterDO.getName(),
                securityUtils
                        .getCurrentGroupId(), sysComponentRegisterDO
                        .getAppId());
    }

    /**
     * 修改组件注册信息
     *
     * @param sysComponentRegisterDO
     * @return
     * @author zuogang
     * @date 2020/4/2 10:34
     */
    @Override
    public boolean editComponent(SysComponentRegisterDO sysComponentRegisterDO) {
        if (!updateById(sysComponentRegisterDO)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "修改"), "修改组件注册信息",
                "修改组件注册信息："+ sysComponentRegisterDO.getName(),
                securityUtils
                        .getCurrentGroupId(), sysComponentRegisterDO
                        .getAppId());
    }

    /**
     * 删除组件注册信息
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2020/4/2 10:34
     */
    @Override
    public boolean delComponent(List<String> ids) {
        if (ids.size() == 0) {
            return true;
        }
        StringBuffer stringBuffer = new StringBuffer();
        String appId = getById((ids).get(0)).getAppId();
        ids.forEach(id -> {
            String str = getById(id).getName();
            stringBuffer.append(str);
            stringBuffer.append(",");
        });
        if (!remove(new QueryWrapper<SysComponentRegisterDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                .in("id", ids))) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除组件注册信息", "删除组件注册信息："+stringBuffer
                        .substring(0, stringBuffer.length
                                () -
                                1),
                securityUtils.getCurrentGroupId(),
                appId);
    }
}
